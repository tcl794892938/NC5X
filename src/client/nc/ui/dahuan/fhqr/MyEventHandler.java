package nc.ui.dahuan.fhqr;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fhtz.DhDeliveryVO;
import nc.vo.pub.AggregatedValueObject;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
	    if(null != aggvo){
	      DhDeliveryVO dvo = (DhDeliveryVO)aggvo.getParentVO();
	      
	        dvo.setIsdelivery(1);
	        dvo.setAuditperson(this._getOperator());
	        HYPubBO_Client.update(dvo);
	        MessageDialog.showHintDlg(this.getBillUI(), "提示", "发货确认完成");
	        
	        ClientUI ui = (ClientUI)this.getBillUI();
	        ui.initValue();
	        
	    }else{
	    	MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
	    }  
	}

	
	
}
