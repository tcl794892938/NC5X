package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

public class LinkBtnVO {

	public static final String BLinkBtnVO= "Áª²é";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("BLinkBtnVO");
		btnvo.setBtnNo(IdhButton.LINK_BillQUERY);
		btnvo.setBtnName(BLinkBtnVO);
		btnvo.setHintStr(BLinkBtnVO);
		btnvo.setBtnChinaName(BLinkBtnVO);
		btnvo.setOperateStatus(
				new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ALL });
		btnvo.setBusinessStatus(new int[] { IBillStatus.ALL });
		return btnvo;
	}
}
