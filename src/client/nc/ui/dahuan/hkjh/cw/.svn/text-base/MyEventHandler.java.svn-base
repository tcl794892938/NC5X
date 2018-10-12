package nc.ui.dahuan.hkjh.cw;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	protected void onBoSave() throws Exception {

		this.getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		
		super.onBoSave();
	}

	
}