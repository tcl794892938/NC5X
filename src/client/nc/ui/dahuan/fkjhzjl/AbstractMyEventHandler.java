package nc.ui.dahuan.fkjhzjl;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends ManageEventHandler{

        public AbstractMyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		     	}
	
		   	
	
}