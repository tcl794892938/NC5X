package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class SjjlBtnVo {
	public static final String sjjlBtnVO= "ÏîÄ¿¼ÇÂ¼";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode( "SjjlBtnVo");
        btnvo.setBtnNo(IdhButton.SJJL);
        btnvo.setBtnName( sjjlBtnVO);
        btnvo.setHintStr( sjjlBtnVO);
        btnvo.setBtnChinaName( sjjlBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
         return btnvo;            
  }
}
