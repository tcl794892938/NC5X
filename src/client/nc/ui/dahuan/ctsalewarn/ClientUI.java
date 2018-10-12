package nc.ui.dahuan.ctsalewarn;

import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillListUI implements ILinkQuery {


	@Override
	protected IListController createController() {
		return new ClientUICtrl();
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	@Override
	protected ListEventHandler createEventHandler() {
		return new MyEventHandler(this,this.getUIControl());
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {

	}

	@Override
	public void setDefaultData() throws Exception {

	}

	public void doQueryAction(ILinkQueryData querydata) {

	}

	@Override
	protected void initPrivateButton() {
		ButtonVO rBtn = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		rBtn.setBtnName("Ð¡¼Æ");
		addPrivateButton(rBtn);
	}

	
	
}
