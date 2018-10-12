package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CgjlBtnVO {
	
	public static final String cgjlBtnVO= "²É¹º¼ÇÂ¼";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode( "CgjlBtnVO");
        btnvo.setBtnNo(IdhButton.CGJL);
        btnvo.setBtnName( cgjlBtnVO);
        btnvo.setHintStr( cgjlBtnVO);
        btnvo.setBtnChinaName( cgjlBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
        return btnvo;            
  }
}
