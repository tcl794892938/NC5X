package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class UpdMoneyBtnVO {
public static final String updMonBtnVO= "½ð¶î¸üÐÂ";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("UpdMoneyBtnVO");
		btnvo.setBtnNo(IdhButton.UPDMONEY);
		btnvo.setBtnName(updMonBtnVO);
		btnvo.setHintStr(updMonBtnVO);
		btnvo.setBtnChinaName(updMonBtnVO);
		
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});

		return btnvo;
	}
}
