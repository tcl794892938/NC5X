package nc.ui.bxgt.button;

import nc.vo.trade.button.ButtonVO;

/**
 * ��������ͬ����ť
 * 
 * @author tcl
 * 
 */
public class SynchroButton {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.BASE_SYN);
		btnVo.setBtnName("ͬ��");
		btnVo.setHintStr("ͬ�����л�������");

		return btnVo;
	}
	
	public ButtonVO getCustBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.CUST_SYN);
		btnVo.setBtnName("ͬ���ͻ�");
		btnVo.setHintStr("ͬ���ͻ�����");

		return btnVo;
	}
	
	public ButtonVO getCustDownBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.CUST_DOWN);
		btnVo.setBtnName("�ͻ�ģ������");
		btnVo.setHintStr("ͬ���ͻ�����ģ��");

		return btnVo;
	}
	
	public ButtonVO getAutoSynBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.AUTO_SYN);
		btnVo.setBtnName("һ��ͬ��");
		btnVo.setHintStr("һ��ͬ�����̵���");

		return btnVo;
	}
	
	public ButtonVO printExcel(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.PRINT_EXCEL);
		btnVo.setBtnName("����Excel");
		btnVo.setHintStr("�������ݺź͵�������");

		return btnVo;
	}
}
