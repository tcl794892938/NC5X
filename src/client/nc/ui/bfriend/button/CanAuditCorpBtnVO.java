package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CanAuditCorpBtnVO {

	public static final String caCorpBtnVO= "¸±×ÜÆúÉó";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("CanAuditCorpBtnVO");
		btnvo.setBtnNo(IdhButton.CANAUDIT_CORP);
		btnvo.setBtnName(caCorpBtnVO);
		btnvo.setHintStr(caCorpBtnVO);
		btnvo.setBtnChinaName(caCorpBtnVO);

		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		
		return btnvo;
	}
	
}
