package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CanAuditDeptBtnVO {


	public static final String caDeptBtnVO= "≤ø√≈∆˙…Û";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("CanAuditDeptBtnVO");
		btnvo.setBtnNo(IdhButton.CANAUDIT_DEPT);
		btnvo.setBtnName(caDeptBtnVO);
		btnvo.setHintStr(caDeptBtnVO);
		btnvo.setBtnChinaName(caDeptBtnVO);

		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		
		return btnvo;
	}
	
}
