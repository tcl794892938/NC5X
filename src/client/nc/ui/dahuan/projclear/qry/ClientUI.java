package nc.ui.dahuan.projclear.qry;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillManageUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}
	
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this,this.getUIControl());
	}

	
	
	@Override
	protected void initPrivateButton() {
		ButtonVO refBtn = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refBtn.setBtnName("项目清算协议");
		addPrivateButton(refBtn);
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void initSelfData() {
	}

	@Override
	public void setDefaultData() throws Exception {}

}
