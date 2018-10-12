package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class JhztBtnVO {
	
	public static final String jhztBtnVO= "½»»õ¼ÇÂ¼";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode( "JhztBtnVO");
        btnvo.setBtnNo(IdhButton.JHZT);
        btnvo.setBtnName( jhztBtnVO);
        btnvo.setHintStr( jhztBtnVO);
        btnvo.setBtnChinaName( jhztBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
         return btnvo;            
  }

}
