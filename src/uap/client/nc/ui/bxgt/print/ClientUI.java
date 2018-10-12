package nc.ui.bxgt.print;

import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.button.SynchroButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class ClientUI extends BillCardUI {

	public ClientUI() {
		super();
		try {
			this.setBillOperate(IBillOperate.OP_EDIT);
			this.getButtonManager().getButton(IBillButton.Query).setEnabled(true);
			this.getBillCardPanel().getBillTable().setSortEnabled(false);//表体不可排序
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected ICardController createController() {
		return new ClientUICtrl();
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, this.getUIControl());
	}

	public String getRefBillType() {
		return null;
	}

	protected void initSelfData() {
		this.getButtonManager().getButton(IBxgtButton.PRINT_EXCEL).setEnabled(
				true);
	}

	public void setDefaultData() throws Exception {

	}

	protected void initPrivateButton() {
		SynchroButton genbtn = new SynchroButton();
		addPrivateButton(genbtn.printExcel());
	}

	@Override
	public boolean onClosing() {
		return true;
	}

}
