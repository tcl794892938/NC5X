package nc.ui.dahuan.hkjh.wh;

import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.hkjh.HkwhDVO;
import nc.vo.dahuan.hkjh.HkwhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{4});
	}

	public void setDefaultData() throws Exception {}


	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			// 已经做过分配的单据不可修改和删除
			HkwhVO hkvo = (HkwhVO)vo.getParentVO();
			try {
				HkwhDVO[] dvos = (HkwhDVO[])HYPubBO_Client.queryByCondition(HkwhDVO.class, " pk_hkwh = '"+hkvo.getPk_hkwh()+"' and nvl(dr,0)=0 ");
				if(null == dvos || dvos.length==0){
					return 2;
				}else{
					HkwhDVO rvo = dvos[0];
					if(_getOperator().equals(rvo.getVoperid())){
						return 4;
					}
					
					return 3;
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
		return 1;
	}
	
	
	
}
