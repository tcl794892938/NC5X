package nc.ui.arap.sedgather;

import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.arap.sedgather.SedGatherHVO;
import nc.vo.pub.AggregatedValueObject;
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
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,int arg1) throws Exception {

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {

	}

	@Override
	protected void initSelfData() {
		// 按钮控制
		// 提交
		ButtonVO commit = (ButtonVO)this.getButtonManager().getButton(IBillButton.Commit).getData();
		commit.setExtendStatus(new int[]{2});
		
		// 审核
		ButtonVO audit = (ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData();
		audit.setExtendStatus(new int[]{3,4});
		
		// 弃审
		ButtonVO canaudit = (ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData();
		canaudit.setExtendStatus(new int[]{4,5,6});
		
		// 打印
		ButtonVO print = (ButtonVO)this.getButtonManager().getButton(IBillButton.Print).getData();
		print.setExtendStatus(new int[]{5});

	}
	
	
	

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		// IX,自由态=8,提交态=3,审批进行中=2,审批通过=1,审批不通过=0
		if(null != vo){
			SedGatherHVO hvo = (SedGatherHVO)vo.getParentVO();
			if(null != hvo){
				int vstatus = hvo.getVbillstatus();
				if(8 == vstatus){
					return 2;
				}else if(3 == vstatus){
					return 3;
				}else if(2 == vstatus){
					return 4;
				}else if(1 == vstatus){
					return 5;
				}else if(0 == vstatus){
					return 6;
				}
			}
		}
		return 1;
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
        if (billId != null) {
            try {
            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
            	AggregatedValueObject vo = loadHeadData(billId);
                getBufferData().addVOToBuffer(vo);
                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

	}

	@Override
	protected void initPrivateButton() {
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if( listButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}
		
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}	
		
		if( hasCommit ){
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}
		
		if( hasAudit ){
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}
		
		if( hasCancelAudit ){
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);	
		}
		
		ButtonVO btnVoAdd = nc.ui.trade.button.ButtonVOFactory.getInstance().build(nc.ui.trade.button.IBillButton.Add);
		btnVoAdd.setBtnName("参照");
		addPrivateButton(btnVoAdd);
	    

	}

	
	
}
