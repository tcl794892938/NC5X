package nc.ui.dh.httk;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

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
		// 放开合同条款模板号
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEdit(true);
	}
	
		
}