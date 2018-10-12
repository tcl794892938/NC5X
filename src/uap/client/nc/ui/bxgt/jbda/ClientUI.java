package nc.ui.bxgt.jbda;

import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.button.SynchroButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class ClientUI extends BillCardUI {

	public ClientUI() {
		super();
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

		this.getButtonManager().getButton(IBxgtButton.BASE_SYN)
				.setEnabled(false);
		this.getButtonManager().getButton(IBxgtButton.CUST_DOWN)
		.setEnabled(true);
		this.getButtonManager().getButton(IBxgtButton.CUST_SYN)
		.setEnabled(true);
		this.getButtonManager().getButton(IBxgtButton.AUTO_SYN)
		.setEnabled(true);
	}

	public void setDefaultData() throws Exception {

	}

	protected void initPrivateButton() {
		SynchroButton genbtn = new SynchroButton();
		addPrivateButton(genbtn.getButtonVO());
		addPrivateButton(genbtn.getCustDownBtn());
		addPrivateButton(genbtn.getCustBtn());
		addPrivateButton(genbtn.getAutoSynBtn());
	}

	@Override
	public boolean onClosing() {
		return true;
	}

}
