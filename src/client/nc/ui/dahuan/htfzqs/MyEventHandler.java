package nc.ui.dahuan.htfzqs;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
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
import nc.vo.pub.lang.UFDateTime;

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
		
		//副总弃审的时候，副总已经审核过了 nvl(fuzong_flag,0) = 1，并且还未盖章

		//合同状态vbillstatus为1，即为合同已经经过副总审批
		//预审标志ys_flag为1,且 不为空，副总审核标志fuzong_flag为=1 ，盖章标志is_seal为0，付款标志is_pay为0
//		vdef5 总经理审批标记
//		销售合同都走部门审核
		//采购合同如果是溧阳公司，需要走，其他公司直接财务审核节点
		
		if(_getCorp().getPk_corp().equals("1001")){//集团只查询销售合同，并且审批进行中
			
			condition = strWhere.toString() + " and nvl(vbillstatus,0) = 2 and pk_corp='"+_getCorp().getPk_corp()+"' " 
			+ " and nvl(ys_flag,0) = 1 "
			+" and nvl(fuzong_flag,0) = 1 "
			+ " and nvl(is_seal,0) =0 "
			+ " and nvl(is_pay,0) = 0 "
			+" and pk_fuzong = '"+user+"' and nvl(vdef5,0)=0 and httype= 0 " ;
		}else if(_getCorp().getPk_corp().equals("1002")){//溧阳只查询采购合同并且审批通过
			
			condition = strWhere.toString() + " and nvl(vbillstatus,0) = 1 and pk_corp='"+_getCorp().getPk_corp()+"' " 
			+ " and nvl(ys_flag,0) = 1 "
			+" and nvl(fuzong_flag,0) = 1 "
			+ " and nvl(is_seal,0) =0 "
			+ " and nvl(is_pay,0) = 0 "
			+" and pk_fuzong = '"+user+"' and nvl(vdef5,0)=0 and httype= 1 " ;
		}else{//其他家公司没有这个功能
			condition = strWhere.toString() + " and nvl(ys_flag,0)=2 " ;
		}
		
		setListVO();
	}
	
	private void setListVO() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	// 弃审
	@Override
	public void onBoCancelAudit() throws Exception {
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		DhContractVO dhcVO = (DhContractVO)aggvo.getParentVO();
		
		if(null == dhcVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}		

		/*int ret = checkBillStatus(dhcVO);
		if(1!=ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "警告", "单据状态发生改变，请刷新后操作");
			return;
		}*/
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(dhcVO);
		
		if(flag){
			dhcVO.setVbillstatus(8);
			dhcVO.setFuzong_flag("0");
			dhcVO.setVdef4("0");
			dhcVO.setYs_flag("0");
			dhcVO.setCmodifyid(this._getOperator());
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String dtsql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
			String tmdate = (String)iQ.executeQuery(dtsql, new ColumnProcessor());
			
			dhcVO.setTmodifytime(tmdate);
			HYPubBO_Client.update(dhcVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(dhcVO.getRet_vemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(dhcVO.getPk_contract());
			rtvo.setRet_address("副总合同弃审");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "弃审已完成");
			// 界面刷新
			setListVO();
		}
	}
	
	/**
	 * 单据状态校验
	 * @return 1、单据可以审核；2、单据状态发生改变；
	 * */
	private int checkBillStatus(DhContractVO vo) throws Exception{
		
		// 查询结果
		String sql = " select count(1) from dh_contract where pk_contract='"+vo.getPrimaryKey()+"' and nvl(vbillstatus,8)=2 and nvl(fuzong_flag,0) = 1 ";
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int value = (Integer)query.executeQuery(sql, new ColumnProcessor());
		
		// 校验结果
		if(value != 1){
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
	}		
	
}