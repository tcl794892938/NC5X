package nc.ui.bxgt.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

/**
 * 批改按钮
 * 
 * @author bwy
 * 
 */
public class GenBillNOButton {
	public static final String BtnVO = "单据号生成";

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.GEN_BILLNO);
		btnVo.setBtnName(BtnVO);

		btnVo.setBtnChinaName(BtnVO);
		// 设置按钮在什么状态下显示
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_EDIT });
		btnVo.setBusinessStatus(null);

		return btnVo;
	}
}
