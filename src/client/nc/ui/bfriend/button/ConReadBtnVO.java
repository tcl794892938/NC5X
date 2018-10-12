package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ConReadBtnVO {
	
	public static final String readBtnVO= "阅读日志完成";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode("conread");
        btnvo.setBtnNo(IdhButton.JIEFENG);
        btnvo.setBtnName(readBtnVO);
        btnvo.setHintStr("确认日志已经阅读,黄色未读,紫色已读");
        btnvo.setBtnChinaName(readBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
        return btnvo;            
  }

}
