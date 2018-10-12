package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

public class ImportHttkBtnVO {

	public static final String BImportTkBtnVO= "导入合同条款";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("BLinkBtnVO");
		btnvo.setBtnNo(IdhButton.IMP_Httk);
		btnvo.setBtnName(BImportTkBtnVO);
		btnvo.setHintStr(BImportTkBtnVO);
		btnvo.setBtnChinaName(BImportTkBtnVO);
		btnvo.setOperateStatus(
				new int[] { IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
		//btnvo.setBusinessStatus(new int[] { IBillStatus.ALL });
		return btnvo;
	}
}
