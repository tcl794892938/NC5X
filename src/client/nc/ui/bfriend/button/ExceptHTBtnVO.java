package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ExceptHTBtnVO {

	public static final String readBtnVO = "异常合同";
	public static final String readBtnVO2 = "异常合同导出";

	public ButtonVO getButtonVO() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("excptht");
		btnvo.setBtnNo(IdhButton.YCHT);
		btnvo.setBtnName(readBtnVO);
		btnvo.setHintStr("超过一年未收付款的合同");
		btnvo.setBtnChinaName(readBtnVO);

		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });

		return btnvo;
	}
	
	public ButtonVO getButtonVODC() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("excpthtdc");
		btnvo.setBtnNo(IdhButton.YCHTDC);
		btnvo.setBtnName(readBtnVO2);
		btnvo.setHintStr("超过一年未收付款的合同");
		btnvo.setBtnChinaName(readBtnVO2);

		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });

		return btnvo;
	}
}
