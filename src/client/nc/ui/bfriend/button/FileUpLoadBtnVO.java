package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class FileUpLoadBtnVO {


	public static final String fileuploadBtnVO= "合同文本";
	public static final String fileglloadBtnVO= "额度管理";
	public static final String filedwloadBtnVO= "承兑附件";
	
	public static final String filedchgBtnVO= "变更附件";
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("FileUpLoadBtnVO");
		btnvo.setBtnNo(IdhButton.FILEUPLOAD);
		btnvo.setBtnName(fileuploadBtnVO);
		btnvo.setHintStr(fileuploadBtnVO);
		btnvo.setBtnChinaName(fileuploadBtnVO);
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		return btnvo;
	}
	
	public ButtonVO getButtonVO3(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("fileglloadBtnVO");
		btnvo.setBtnNo(IdhButton.FILEUPLOAD);
		btnvo.setBtnName(fileglloadBtnVO);
		btnvo.setHintStr(fileglloadBtnVO);
		btnvo.setBtnChinaName(fileglloadBtnVO);
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		return btnvo;
	}
	
	public ButtonVO getButtonVO2(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("FileDownLoadBtnVO");
		btnvo.setBtnNo(IdhButton.FILEDOWNLOAD);
		btnvo.setBtnName(filedwloadBtnVO);
		btnvo.setHintStr(filedwloadBtnVO);
		btnvo.setBtnChinaName(filedwloadBtnVO);
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		return btnvo;
	}
	
	public ButtonVO getButtonVO4(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("filedchgBtnVO");
		btnvo.setBtnNo(IdhButton.FILEDOWNLOAD);
		btnvo.setBtnName(filedchgBtnVO);
		btnvo.setHintStr(filedchgBtnVO);
		btnvo.setBtnChinaName(filedchgBtnVO);
		btnvo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		return btnvo;
	}
	
}
