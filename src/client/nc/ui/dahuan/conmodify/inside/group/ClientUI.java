package nc.ui.dahuan.conmodify.inside.group;

import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

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
	protected void initPrivateButton() {
		FileUpLoadBtnVO fvo = new FileUpLoadBtnVO();
		addPrivateButton(fvo.getButtonVO());
		ButtonVO avo = ButtonVOFactory.getInstance().build(IBillButton.Audit);
		avo.setBtnName("同意");
		addPrivateButton(avo);
		ButtonVO cvo = ButtonVOFactory.getInstance().build(IBillButton.CancelAudit);
		cvo.setBtnName("不同意");
		addPrivateButton(cvo);
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
	public void setDefaultData() throws Exception { }

	public void doQueryAction(ILinkQueryData querydata) { }

	
}
