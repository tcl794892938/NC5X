package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DeliveryListBtnVO {

	public static final String deliveryListBtnVO= "发货确认";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("DeliveryListBtnVO");
		btnvo.setBtnNo(IdhButton.DELIVERYLIST);
		btnvo.setBtnName(deliveryListBtnVO);
		btnvo.setHintStr(deliveryListBtnVO);
		btnvo.setBtnChinaName(deliveryListBtnVO);
		btnvo.setOperateStatus(new int[]{ IBillOperate.OP_NOTEDIT });
		return btnvo;
	}
}
