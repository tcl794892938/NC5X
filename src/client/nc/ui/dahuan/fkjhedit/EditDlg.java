package nc.ui.dahuan.fkjhedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.dahuan.fkedit.FkjhEditVO;

public class EditDlg extends UIDialog {

	private static final long serialVersionUID = 1L;

	public EditDlg(Container parent) {
		super(parent);
		initialize();
		initDialog();
	}

	private BillListPanel panel;

	private void initialize() {
		if (null == this.panel) {
			this.panel = new BillListPanel();
			this.panel.loadTemplet("0001AA1000000000YXZJ");// 加载模版
		}
	}

	private void initDialog() {
		// 设置对话框主题
		this.setTitle("修改记录");
		// 设置最适合的大小
		this.setSize(new Dimension(800, 600));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置panel
		this.add(panel, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}

	public BillListPanel getPanel() {
		return panel;
	}

	public void setPanel(BillListPanel panel) {
		this.panel = panel;
	}
	
	public void showEditInfo(FkjhEditVO[] fvos){
		
		panel.getHeadBillModel().setBodyDataVO(fvos);
		panel.getHeadBillModel().loadLoadRelationItemValue();
		panel.getHeadBillModel().execLoadFormula();
		this.showModal();
	}

}
