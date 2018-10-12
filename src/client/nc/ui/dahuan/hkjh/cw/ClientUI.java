package nc.ui.dahuan.hkjh.cw;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;



 public class ClientUI extends AbstractClientUI {
       
   
	private static final long serialVersionUID = 1L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Delete).getData()).setExtendStatus(new int[]{2});
	}

	public void setDefaultData() throws Exception {}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		if("bzbl".equals(e.getKey()) || "hk_amount".equals(e.getKey())||"give_amount".equals(e.getKey())||"discount_amount".equals(e.getKey())){
			BillCardPanel card = this.getBillCardPanel();
			UFDouble bzbl = get_ufdouble(card.getHeadItem("bzbl").getValueObject());
			UFDouble hk_amount = get_ufdouble(card.getHeadItem("hk_amount").getValueObject());
			
			UFDouble gv_amount = get_ufdouble(card.getHeadItem("give_amount").getValueObject());
			UFDouble dt_amount = get_ufdouble(card.getHeadItem("discount_amount").getValueObject());
			
			UFDouble rmb_amt = bzbl.multiply(hk_amount,2).add(dt_amount,2).sub(gv_amount,2);
			
			card.getHeadItem("rmb_amount").setValue(rmb_amt);
		}
	}

	private UFDouble get_ufdouble(Object obj){
		if(null == obj){
			return new UFDouble("0.00");
		}else{
			return new UFDouble(obj.toString());
		}
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			// 已经做过分配的单据不可修改和删除
			HkdhVO hkvo = (HkdhVO)vo.getParentVO();
			try {
				HkdhDVO[] dvos = (HkdhDVO[])HYPubBO_Client.queryByCondition(HkdhDVO.class, " pk_hkdh = '"+hkvo.getPk_hkdh()+"' and nvl(dr,0)=0 ");
				if(null == dvos || dvos.length==0){
					return 2;
				}else{
					return 3;
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
		return 1;
	}
	
	
	
}
