package nc.ui.bxgt.djbillno;

import nc.ui.bxgt.button.GenBillNOButton;
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
			this.setBillOperate(IBillOperate.OP_EDIT); //编辑
			this.getButtonManager().getButton(IBillButton.Query).setEnabled(
					true);
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

	}

	public void setDefaultData() throws Exception {

	}

	protected void initPrivateButton() {
		// 单据号生成
		GenBillNOButton genbtn = new GenBillNOButton();
		addPrivateButton(genbtn.getButtonVO());
	}

	@Override
	public boolean onClosing() {
		return true;
	}

}
