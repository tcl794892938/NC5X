package nc.ui.dh.httk;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

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

	@Override
	protected void onBoSave() throws Exception {
		BillData data = this.getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(data != null){
			data.dataNotNullValidate();
		}
		
		super.onBoSave();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		// �ſ���ͬ����ģ���
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEdit(true);
	}
	
		
}