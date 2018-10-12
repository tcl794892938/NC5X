package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ExceptFHSKBtnVO {

	public static final String readBtnVO = "�ѷ���δ��/����";
	public static final String readBtnVO2 = "�ѷ���δ��/�����";

	public ButtonVO getButtonVO() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("excptfhsk");
		btnvo.setBtnNo(IdhButton.FHSK);
		btnvo.setBtnName(readBtnVO);
		btnvo.setHintStr("����һ���ѷ���δ��/����ĺ�ͬ");
		btnvo.setBtnChinaName(readBtnVO);

		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });

		return btnvo;
	}
	
	public ButtonVO getButtonVODC() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("excptfhskdc");
		btnvo.setBtnNo(IdhButton.FHSKDC);
		btnvo.setBtnName(readBtnVO2);
		btnvo.setHintStr("����һ���ѷ���δ��/����ĺ�ͬ");
		btnvo.setBtnChinaName(readBtnVO2);

		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });

		return btnvo;
	}
}
