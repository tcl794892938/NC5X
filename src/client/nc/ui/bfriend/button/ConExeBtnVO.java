package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ConExeBtnVO {
	public static final String conexeBtnVO= "Ö´ÐÐ";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("ConExeBtnVO");
		btnvo.setBtnNo(IdhButton.CONEXE);
		btnvo.setBtnName(conexeBtnVO);
		btnvo.setHintStr(conexeBtnVO);
		btnvo.setBtnChinaName(conexeBtnVO);
		
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});

		return btnvo;
	}
}
