package nc.ui.dahuan.conmodify.query;

import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ClientUI extends BillManageUI implements ILinkQuery {

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
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

	public void doQueryAction(ILinkQueryData querydata) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initPrivateButton() {
		FileUpLoadBtnVO fBtn = new FileUpLoadBtnVO();
		addPrivateButton(fBtn.getButtonVO());
	}

}
