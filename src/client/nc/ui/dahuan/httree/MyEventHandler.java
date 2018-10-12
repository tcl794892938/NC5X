package nc.ui.dahuan.httree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhServer;
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
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends AbstractMyEventHandler{

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
		
		//部门审批的时候，是提交态或者预审，vbillstatus = 3 
		// 由于自动化部门的单据特殊，故此不用部门主管的值校验，而是根据部门
		//且 预审标志 ys_flag 为1 （没有预审的合同 ys_flag 状态为0）并且不为null
        //副总审核标志 nvl(fuzong_flag,0) = 0
		//盖章标志is_seal为0，付款标志is_pay为0
		//当前部门主管已审核(根据审核日期来判断)nvl(vdef4,0)=0
//		vdef4表示部门审核标记
//		销售合同都走部门审核
		//采购合同如果是溧阳公司，需要走，其他公司直接财务审核节点
		String httype="";
		if(!_getCorp().getPk_corp().equals("1002")){//不是溧阳公司，需要过滤采购合同
			httype+= " and dh_contract.httype = 0 ";
		}
		condition = strWhere.toString() + " and nvl(dh_contract.vbillstatus,0) =2 and nvl(dh_contract.ys_flag,0) = 1 "
										+ " and dh_contract.pk_ysid is not null and nvl(vdef4,0)=0 and nvl(dh_contract.fuzong_flag,0) = 0 " 
										+ " and nvl(dh_contract.is_seal,0) =0 and nvl(dh_contract.is_pay,0) = 0 "
//										+ " and (dh_contract.ht_dept in (select vd.pk_deptdoc from v_deptperonal vd "
//										+ " where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"') "
//										+ " or dh_contract.pk_deptdoc in (select vd.pk_deptdoc from v_deptperonal vd "
//										+ "  where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"')) ";
										+ " and dh_contract.pk_deptdoc in (select vd.pk_deptdoc from v_deptperonal vd "
										+ " where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"')  "+httype ;

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
		if(3==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "警告", "单据已被处理过，不可再审核");
			return;
		}else if(2==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "警告", "单据状态发生改变，请刷新后操作");
			return;
		}*/
		
		// 登录用户
		String user = _getOperator();
		// 当前日期
		UFDate date = this._getDate();
		// 后天审核操作
		NCLocator.getInstance().lookup(IdhServer.class).AuditDhhtbmzg(isManagerAudit(dhcVOs),user,date);
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "审核完成");
		// 界面刷新
		setListVO();
		
	}
	
	/**
	 * 单据状态校验
	 * @return 1、单据可以审核；2、单据状态发生改变；3、单据不是提交态
	 * */
	private int checkBillStatus(DhContractVO[] listVO) throws Exception{
		// 需要校验的单据数量
		int length = listVO.length;
		
		// SQL条件拼接：根据选择单据的主键和单据状态判断符合条件的单据数量是否和选择的数量一致		
		String condition = "";		
		for(int i=0;i<length;i++){
			DhContractVO vo = listVO[i];
			if(3 != vo.getVbillstatus().intValue()&&2 != vo.getVbillstatus().intValue()){
				return 3;
			}
			if(i==0){
				condition = " and (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"')";
			}else{
				condition += " or (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"')";
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
	
	/** 是否需要副总审核判断 
	 * @return Map中键aaa表示需要副总审核的合同单；键bbb表示不需要副总审核的合同单
	 **/
	private Map<String,List<DhContractVO>> isManagerAudit(DhContractVO[] listVO){
		Map<String,List<DhContractVO>> map = new HashMap<String, List<DhContractVO>>();
		for(DhContractVO vo : listVO){
			// 判断合同类型:销售合同(0)、采购合同(1)、虚合同(2)
			int httype = vo.getHttype();
			if(2 == httype){
				map = setManagerMap(map,vo,"bbb");
			}else if(1 == httype){
				// 采购合同根据采购金额判断,大于300万需要副总签字
				UFDouble cgt = vo.getDcaigtotal();
				if(cgt.compareTo(new UFDouble("2999999.99"))==1){
					map = setManagerMap(map,vo,"aaa");
				}else{
					map = setManagerMap(map,vo,"bbb");
				}
			}else if(0 == httype){
				boolean flag1 = !(vo.getCtcode().contains("备") || vo.getCtcode().contains("磨机"));
				boolean flag2 = vo.getCtcode().contains("磨机") && vo.getDsaletotal().compareTo(new UFDouble("2999999.99"))==1;
				if(flag1 || flag2){
					map = setManagerMap(map,vo,"aaa");
				}else{
					map = setManagerMap(map,vo,"bbb");
				}
			}
		}
		return map;
		
	}
	
	private Map<String,List<DhContractVO>> setManagerMap(Map<String,List<DhContractVO>> map,DhContractVO vo,String value){
		if(map.containsKey(value)){
			List<DhContractVO> dhListVO = map.get(value);
			dhListVO.add(vo);
			map.put(value, dhListVO);
		}else{
			List<DhContractVO> dhListVO = new ArrayList<DhContractVO>();
			dhListVO.add(vo);
			map.put(value, dhListVO);
		}
		return map;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
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
			MessageDialog.showErrorDlg(this.getBillUI(), "提示", "单据状态已发生改变，请刷新后在操作");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(newVO);
		
		if(flag){
			newVO.setVbillstatus(8);
			newVO.setYs_flag("0");
			newVO.setDapprovedate(null);
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
			rtvo.setRet_address("部门合同审核");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "驳回成功");
			setListVO();
		}
	}	
	
}