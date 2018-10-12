package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class TsztBtnVO {
	
	public static final String tsztBtnVO= "µ÷ÊÔ¼ÇÂ¼";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode( "TsztBtnVO");
        btnvo.setBtnNo(IdhButton.TSZT);
        btnvo.setBtnName( tsztBtnVO);
        btnvo.setHintStr( tsztBtnVO);
        btnvo.setBtnChinaName( tsztBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
        return btnvo;            
  }
}
