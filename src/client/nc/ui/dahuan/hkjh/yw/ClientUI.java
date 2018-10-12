package nc.ui.dahuan.hkjh.yw;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
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
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Refbill).getData()).setExtendStatus(new int[]{3,4});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.ExportBill).getData()).setExtendStatus(new int[]{2,5});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{2,5});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Delete).getData()).setExtendStatus(new int[]{4,5});
		
	}

	public void setDefaultData() throws Exception {}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			HkdhVO hkvo = (HkdhVO)vo.getParentVO();
			if(null != hkvo.getBill_flag() && (1==hkvo.getBill_flag() || 2==hkvo.getBill_flag() || 3==hkvo.getBill_flag()||4==hkvo.getBill_flag())){
				// 承兑的单据在该画面只能查询
				return 2;
			}else{
				HkdhDVO[] dvos = (HkdhDVO[])vo.getChildrenVO();
				if(null == dvos || dvos.length==0){
					// 没做过任何分配，按钮可用
					return 3;
				}else{//电汇回款
					UFDouble ufd_body = new UFDouble("0.00");
					for(HkdhDVO dvo : dvos){
						ufd_body = ufd_body.add(dvo.getHk_amount()==null?dvo.getCt_amount():dvo.getHk_amount(), 2);
					}
					UFDouble ufd_head = hkvo.getRmb_amount()==null?new UFDouble("0.00"):hkvo.getRmb_amount();
					if(ufd_body.compareTo(ufd_head)<0){
						// 子表合计小于表头总额,按钮可用
						return 4;
					}else{
						return 5;
					}
				}
			}
		}
		return 1;
	}

	
}
