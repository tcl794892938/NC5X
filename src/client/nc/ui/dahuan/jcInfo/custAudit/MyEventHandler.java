package nc.ui.dahuan.jcInfo.custAudit;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;

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
	
	public String condition = "";

	// 查询
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		condition = strWhere.toString();

		refreshListPanel();
	}
	
	public void refreshListPanel() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);
		getBufferData().clear();
		addDataToBuffer(queryVos);
		updateBuffer();
	}
	
	// 驳回
	@Override
	protected void onBoCancelAudit() throws Exception {}
	
	// 审核
	@Override
	public void onBoAudit() throws Exception {}

	// 全选
	@Override
	protected void onBoSelAll() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// 全消
	@Override
	protected void onBoSelNone() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}

	

		
	
}