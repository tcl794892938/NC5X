package nc.ui.dahuan.fhzz;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fhzz.DhDelZZVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
			if("�ѷ�����ֹ".equals(zzvo.getIsdel()) || "��ȫ������".equals(zzvo.getIsdel())){
				return 3;
			}else{
				if("�ɹ���ͬ".equals(zzvo.getHttype())){
					return 2;
				}else{
					return 4;
				}
				
			}
		}
		return 1;
	}

	
	
}
