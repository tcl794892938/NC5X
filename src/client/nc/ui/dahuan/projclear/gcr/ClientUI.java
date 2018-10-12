package nc.ui.dahuan.projclear.gcr;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.projclear.ProjectClearVO;
import nc.vo.pub.AggregatedValueObject;
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
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void initSelfData() {
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{3});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{3});
	}
	
	

	@Override
	protected void initPrivateButton() {
		ButtonVO caBtn = ButtonVOFactory.getInstance().build(IBillButton.CancelAudit);
		caBtn.setBtnName("复核驳回");
		addPrivateButton(caBtn);
		
		ButtonVO atBtn = ButtonVOFactory.getInstance().build(IBillButton.Audit);
		atBtn.setBtnName("复核通过");
		addPrivateButton(atBtn);
		
		ButtonVO refBtn = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refBtn.setBtnName("项目清算协议");
		addPrivateButton(refBtn);
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			ProjectClearVO pcvo = (ProjectClearVO)vo.getParentVO();
			int pcs = pcvo.getPc_status();
			if(0==pcs||3==pcs||5==pcs){
				return 2;
			}else if(1==pcs){
				return 3;
			}else if(2==pcs){
				return 4;
			}else if(4==pcs){
				return 5;
			}else{
				return 6;
			}
		}
		return 1;
	}

	@Override
	public void setDefaultData() throws Exception {}

}
