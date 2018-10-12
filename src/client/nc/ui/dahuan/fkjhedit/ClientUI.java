package nc.ui.dahuan.fkjhedit;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.BusinessException;
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
		
		if(e.getKey().equals("print_no")){
			
			BillCardPanel card=this.getBillCardPanel();
			Object obj=card.getHeadItem("ctcode").getValueObject();
			if(obj==null||"".equals(obj)){
				MessageDialog.showHintDlg(card, "提示", "请先录入合同号!");
				card.setHeadItem("print_no", null);
				return ;
			}
			
			IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String sql="select * from dh_fkjhbill t where nvl(t.dr,0)=0 and nvl(t.sealflag,0)=0 " +
					" and t.ctcode='"+obj.toString()+"' and t.print_no='"+e.getValue()+"' ";
			DhFkjhbillVO vo=null;
			try {
				vo=(DhFkjhbillVO)iQ.executeQuery(sql, new BeanProcessor(DhFkjhbillVO.class));
			} catch (BusinessException e1) {
			}
			if(vo!=null){
				card.setHeadItem("oldmny", vo.getDfkje());
				card.setHeadItem("pk_customer", vo.getPk_cust2());
			}
			
		}
	}

	protected void initPrivateButton() {
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Add )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Save )
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
				.build(nc.ui.trade.button.IBillButton.Save);
			btnVo2.setBtnName("确认");
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
