package nc.ui.dahuan.fhzz;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fhzz.DhDelZZVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
       
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
		ButtonVO advo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData();
		advo.setExtendStatus(new int[]{2});
		
		ButtonVO ctvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Commit).getData();
		ctvo.setExtendStatus(new int[]{4});
		
		ButtonVO rfvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Refresh).getData();
		rfvo.setExtendStatus(new int[]{2,3,4});
	}

	public void setDefaultData() throws Exception {}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			DhDelZZVO zzvo = (DhDelZZVO)vo.getParentVO();
			if("已发货终止".equals(zzvo.getIsdel()) || "已全部交货".equals(zzvo.getIsdel())){
				return 3;
			}else{
				if("采购合同".equals(zzvo.getHttype())){
					return 2;
				}else{
					return 4;
				}
				
			}
		}
		return 1;
	}

	
	
}
