package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CwbhBtnVO {
	public static final String cwbhBtnVO= "²ÆÎñ²µ»Ø";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode("CwbhBtnVO");
        btnvo.setBtnNo(IdhButton.CWBH);
        btnvo.setBtnName(cwbhBtnVO);
        btnvo.setHintStr(cwbhBtnVO);
        btnvo.setBtnChinaName(cwbhBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
         return btnvo;            
  }
}
