package nc.ui.dahuan.hkjh.sw;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

public class FileUpLoadBtnVO {


	public static final String fileuploadBtnVO= "»Ø¿î¸½¼þ";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("FileUpLoadBtnVO");
		btnvo.setBtnNo(IdhButton.FILEUPLOAD);
		btnvo.setBtnName(fileuploadBtnVO);
		btnvo.setHintStr(fileuploadBtnVO);
		btnvo.setBtnChinaName(fileuploadBtnVO);
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		btnvo.setBusinessStatus(new int[] { IBillStatus.ALL });
		return btnvo;
	}
	
}
