package nc.ui.dahuan.htfilemodel;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;

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
	protected void onBoElse(int intBtn) throws Exception {
		if(IdhButton.FILEUPLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", "合同模板");
		}
	}
		
}