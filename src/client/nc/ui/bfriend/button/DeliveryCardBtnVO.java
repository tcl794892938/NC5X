package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DeliveryCardBtnVO {

	public static final String deliveryCardBtnVO= "发货确认";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("DeliveryCardBtnVO");
		btnvo.setBtnNo(IdhButton.DELIVERYCARD);
		btnvo.setBtnName(deliveryCardBtnVO);
		btnvo.setHintStr(deliveryCardBtnVO);
		btnvo.setBtnChinaName(deliveryCardBtnVO);
		btnvo.setOperateStatus(new int[]{ IBillOperate.OP_NOTEDIT });
		return btnvo;
	}
}
