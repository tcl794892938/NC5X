package nc.ui.bfriend.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ConReadBtnVO {
	
	public static final String readBtnVO= "�Ķ���־���";
	
	public  ButtonVO getButtonVO(){
        ButtonVO btnvo = new ButtonVO();
        btnvo.setBtnCode("conread");
        btnvo.setBtnNo(IdhButton.JIEFENG);
        btnvo.setBtnName(readBtnVO);
        btnvo.setHintStr("ȷ����־�Ѿ��Ķ�,��ɫδ��,��ɫ�Ѷ�");
        btnvo.setBtnChinaName(readBtnVO);
        
        btnvo.setOperateStatus( new int [] {IBillOperate.OP_NOTEDIT });
        
        return btnvo;            
  }

}
