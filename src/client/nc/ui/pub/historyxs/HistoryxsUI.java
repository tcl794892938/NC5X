package nc.ui.pub.historyxs;

import java.awt.Container;

import nc.ui.bfriend.dialog.TempletBasedUIDialog;

public class HistoryxsUI extends TempletBasedUIDialog {

	
	/**
	 * 价格影响系数历史变更显示表 wanglong
	 */
	private static final long serialVersionUID = 1L;
	
	public HistoryxsUI(Container parent, String strBillType, String strCorp, String strOperator, String strNodekey) {
		super(parent, strBillType, strCorp, strOperator, strNodekey);
		// TODO Auto-generated constructor stub
	}
	
	public String getTitle() {
		return "合同执行明细";
	}

}
