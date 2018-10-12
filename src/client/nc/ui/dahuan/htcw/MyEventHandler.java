package nc.ui.dahuan.htcw;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	@Override
	protected void onBoCard() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = getBillManageUI().getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}

	public String condition = "";
	// 查询过滤条件
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		String user = this._getOperator();
		String corpid = this._getCorp().getPrimaryKey();
		
		//财务审批
		//非溧阳公司的采购合同都走财务审核
		condition = strWhere.toString() + " and nvl(vbillstatus,0) in(2,3) " 
		+ " and httype='1' and pk_corp<>'1002' and pk_corp='"+corpid+"' ";
		setListVO();
	}
	
	private void setListVO() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	// 全选
	@Override
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// 全消
	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}

	// 审核
	@Override
	public void onBoAudit() throws Exception {
		DhContractVO[] dhcVOs;
		
		// 判断列表还是卡片
		if(getBillManageUI().isListPanelSelected()){
			dhcVOs = (DhContractVO[]) this.getBillManageUI().getBillListPanel().getHeadBillModel()
															.getBodySelectedVOs(DhContractVO.class.getName());

		}else{
			DhContractVO dhcVO = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			dhcVOs = new DhContractVO[]{ dhcVO };
		}
		
		if(0==dhcVOs.length){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}		

		/*int ret = checkBillStatus(dhcVOs);
		if(2==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "警告", "单据状态发生改变，请刷新后操作");
			return;
		}*/
		
		// 登录用户
		String user = _getOperator();
		// 当前日期
		UFDate date = this._getDate();
		// 后天审核操作

		for(DhContractVO cvo : dhcVOs){
			cvo.setVbillstatus(1);
			//cvo.setVapproveid(user);
			//cvo.setDapprovedate(date);
			HYPubBO_Client.update(cvo);
		}
		
		// 界面刷新
		setListVO();
		
	}
	
	/**
	 * 单据状态校验
	 * @return 1、单据可以审核；2、单据状态发生改变；
	 * */
	private int checkBillStatus(DhContractVO[] listVO) throws Exception{
		// 需要校验的单据数量
		int length = listVO.length;
		
		// SQL条件拼接：根据选择单据的主键和单据状态判断符合条件的单据数量是否和选择的数量一致		
		String condition = "";		
		for(int i=0;i<length;i++){
			DhContractVO vo = listVO[i];
			if(i==0){
				condition = " and (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='8')";
			}else{
				condition += " or (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='8')";
			}
		}
		
		// 查询结果
		String sql = " select count(1) from dh_contract where 1=1 " + condition;
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int value = (Integer)query.executeQuery(sql, new ColumnProcessor());
		
		// 校验结果
		if(value != length){
			return 2;
		}
		
		return 1;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT",dvo.getCtcode());
		}
		if(intBtn == IdhButton.RET_COMMIT){
				retCommit();
		}
	}	
	
//	 驳回
	private void retCommit() throws Exception {
		AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
		if(null == aggVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		DhContractVO patVO = (DhContractVO)aggVO.getParentVO();
		if(null == patVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		String pk = patVO.getPrimaryKey();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		DhContractVO newVO = (DhContractVO)query.retrieveByPK(DhContractVO.class, pk);
		// 比较界面上的单据状态和数据库中的单据状态
		if(patVO.getVbillstatus().intValue() != newVO.getVbillstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "提示", "单据状态已发生改变，请刷新后再操作");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(newVO);
		
		if(flag){
			newVO.setVbillstatus(8);
			newVO.setCmodifyid(this._getOperator());
			
			String dtsql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
			String tmdate = (String)query.executeQuery(dtsql, new ColumnProcessor());
			newVO.setTmodifytime(tmdate);
			
			iv.updateVO(newVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(newVO.getRet_vemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(newVO.getPk_contract());
			rtvo.setRet_address("财务部审核确认");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "驳回成功");
			setListVO();
		}
	}
	
}