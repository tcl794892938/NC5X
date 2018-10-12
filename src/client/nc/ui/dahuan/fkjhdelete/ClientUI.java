package nc.ui.dahuan.fkjhdelete;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillCardUI {

	public ClientUI() {
		
		this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
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
	
	

	@Override
	public void afterEdit(BillEditEvent e) {
	}

	protected void initPrivateButton() {
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Add )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Delete )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Query )
				hasCancelAudit = true;
		}		
		if( hasCommit ){
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.Add);
			btnVo.setBtnName("录入");
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}
		
		if( hasAudit ){
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
				.build(nc.ui.trade.button.IBillButton.Delete);
			btnVo2.setBtnName("删除");
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}
		
		if( hasCancelAudit ){
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.Query);
			btnVo3.setBtnName("查询");
			btnVo3.setHintStr("查询记录");
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);	
		}	
		
	}

}
