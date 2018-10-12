package nc.ui.dahuan.conmodify.inside.dept;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.pub.lang.UFDouble;

public class BudgetDialog extends UIDialog {

	BillCardPanel card;
	UFDouble budget;
	BudgetDialog rtdg;
	boolean flag=false;
	
	public BudgetDialog(Container parent) {
		super(parent);
		this.setModal(false);
	}
	
	public boolean showBudgetDialog(){
		initialize();
		initDialog();
		rtdg = this;
		this.showModal();
		return flag;
	}
	
	private void initialize(){
		if(null == card){
			card = new BillCardPanel();
			card.loadTemplet("0001AA1000000000SIJ5");
		}
	}

	private void initDialog() {
		// 设置对话框主题
		this.setTitle("项目预算");
		// 设置最适合的大小
		this.setSize(new Dimension(600,400));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");		
		UIButton notBtn = new UIButton("取  消");
		// 加监听
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		UIPanel panel = new UIPanel();
		panel.add(sureBtn);
		panel.add(notBtn);
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			Object bgObj = card.getHeadItem("budget").getValueObject();
			if(null == bgObj || "".equals(bgObj)){
				MessageDialog.showHintDlg(rtdg, "提示", "请维护项目预算");
				return;
			}
			
			budget = new UFDouble(bgObj.toString());
			flag = true;
			rtdg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class NotMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			flag = false;
			rtdg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	public UFDouble getBudget() {
		return budget;
	}

	public void setBudget(UFDouble budget) {
		this.budget = budget;
	}
	

}
