package nc.ui.bxgt.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

/**
 * ���İ�ť
 * 
 * @author bwy
 * 
 */
public class GenBillNOButton {
	public static final String BtnVO = "���ݺ�����";

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.GEN_BILLNO);
		btnVo.setBtnName(BtnVO);

		btnVo.setBtnChinaName(BtnVO);
		// ���ð�ť��ʲô״̬����ʾ
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_EDIT });
		btnVo.setBusinessStatus(null);

		return btnVo;
	}
}
