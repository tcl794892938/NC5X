package nc.ui.dahuan.jcInfo.custAudit;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	public String condition = "";

	// ��ѯ
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		condition = strWhere.toString();

		refreshListPanel();
	}
	
	public void refreshListPanel() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);
		getBufferData().clear();
		addDataToBuffer(queryVos);
		updateBuffer();
	}
	
	// ����
	@Override
	protected void onBoCancelAudit() throws Exception {}
	
	// ���
	@Override
	public void onBoAudit() throws Exception {}

	// ȫѡ
	@Override
	protected void onBoSelAll() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// ȫ��
	@Override
	protected void onBoSelNone() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}

	

		
	
}