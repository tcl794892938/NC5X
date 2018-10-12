package nc.ui.bxgt.button;

import nc.vo.trade.button.ButtonVO;

/**
 * 基本档案同步按钮
 * 
 * @author tcl
 * 
 */
public class SynchroButton {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.BASE_SYN);
		btnVo.setBtnName("同步");
		btnVo.setHintStr("同步所有基本档案");

		return btnVo;
	}
	
	public ButtonVO getCustBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.CUST_SYN);
		btnVo.setBtnName("同步客户");
		btnVo.setHintStr("同步客户档案");

		return btnVo;
	}
	
	public ButtonVO getCustDownBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.CUST_DOWN);
		btnVo.setBtnName("客户模版下载");
		btnVo.setHintStr("同步客户所需模版");

		return btnVo;
	}
	
	public ButtonVO getAutoSynBtn(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.AUTO_SYN);
		btnVo.setBtnName("一键同步");
		btnVo.setHintStr("一键同步客商档案");

		return btnVo;
	}
	
	public ButtonVO printExcel(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IBxgtButton.PRINT_EXCEL);
		btnVo.setBtnName("导出Excel");
		btnVo.setHintStr("导出单据号和单据类型");

		return btnVo;
	}
}
