package nc.ui.dahuan.fkjhfc;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		DhFkjhbillVO fvo = (DhFkjhbillVO)aggvo.getParentVO();
		fvo.setSealflag(1);
		fvo.setSealdate(this._getDate());
		fvo.setSealid(this._getOperator());
		HYPubBO_Client.update(fvo);
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "封存成功");
		this.onBoRefresh();
	}
	
}
