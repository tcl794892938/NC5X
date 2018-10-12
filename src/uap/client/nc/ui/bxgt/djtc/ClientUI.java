package nc.ui.bxgt.djtc;

import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.button.SelfButtonVo;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class ClientUI extends BillCardUI {

	public ClientUI() {
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
		ButtonObject btnObj = this.getButtonManager().getButton(
				IBxgtButton.LOCK_GROUP);
		ButtonObject lockVo = SelfButtonVo.getInstance().getLockBtn()
				.buildButton();
		ButtonObject unLockVO = SelfButtonVo.getInstance().getUnLockBtn()
				.buildButton();
		btnObj.addChildButton(lockVo);
		btnObj.addChildButton(unLockVO);

		ButtonObject btnObj1 = this.getButtonManager().getButton(
				IBxgtButton.MARK_GROUP);
		ButtonObject markVo = SelfButtonVo.getInstance().getOkMark()
				.buildButton();
		ButtonObject cancelVO = SelfButtonVo.getInstance().getCancelMark()
				.buildButton();
		btnObj1.addChildButton(markVo);
		btnObj1.addChildButton(cancelVO);
		
		ButtonObject btnObj2=this.getButtonManager().getButton(IBxgtButton.ORDERGROUP);
		ButtonObject orderseq=SelfButtonVo.getInstance().getOrderseqBtn().buildButton();
		ButtonObject cancelsq=SelfButtonVo.getInstance().getCancelOrderBtn().buildButton();
		btnObj2.addChildButton(orderseq);
		btnObj2.addChildButton(cancelsq);
		
		btnObj.setEnabled(false);
		btnObj1.setEnabled(false);
		btnObj2.setEnabled(false);
		this.getButtonManager().getButton(IBxgtButton.SYNCHRONOUS).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.BATCH_EDIT).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.TAX_RATE).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.CUST_MNY).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.DELBILL)
				.setEnabled(false);
		updateButtons();

	}

	public void setDefaultData() throws Exception {

	}

	protected void initPrivateButton() {
		addPrivateButton(SelfButtonVo.getInstance().getSynchroBtn());
		addPrivateButton(SelfButtonVo.getInstance().getBatchEditBtn());
		addPrivateButton(SelfButtonVo.getInstance().getPrePaymentBtn());
		addPrivateButton(SelfButtonVo.getInstance().getLockGroupBtn());
		addPrivateButton(SelfButtonVo.getInstance().getMarkBtn());
		addPrivateButton(SelfButtonVo.getInstance().getOrderGrpBtn());
		addPrivateButton(SelfButtonVo.getInstance().getTaxBtn());
		addPrivateButton(SelfButtonVo.getInstance().getCusMnyBtn());
		addPrivateButton(SelfButtonVo.getInstance().getDelBtn());
	}

}
