package nc.ui.bxgt.djtc;

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

public class CustMnyDlg extends UIDialog {

	public CustMnyDlg(Container parent) {
		super(parent);
		initialize();
		initDialog();
		this.showModal();
	}

	private CustMnyDlg dialog;

	private BillCardPanel card;
	
	private String pk_cust;
	
	private Object value1;
	
	private boolean flag=false;

	private void initialize() {
		if (null == this.card) {
			this.card = new BillCardPanel();
			this.card.loadTemplet("0001AA100000000OZGEU");//修改客户金额模版
		}
	}

	private void initDialog() {
		dialog = this;
		// 设置对话框主题
		this.setTitle("修改客户金额");
		// 设置最适合的大小
		this.setSize(new Dimension(600, 150));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");
		UIButton notBtn = new UIButton("取  消");
		sureBtn.setPreferredSize(new Dimension(80, 25));
		notBtn.setPreferredSize(new Dimension(80, 25));
		// 加监听
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());

		UIPanel b_panel = new UIPanel();
		b_panel.add(sureBtn);
		b_panel.add(notBtn);
		// 将panel加载到对话框中
		this.add(card, BorderLayout.CENTER);
		this.add(b_panel, BorderLayout.SOUTH);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}
	
	class SureMouseLister implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			
			Object obj1=card.getHeadItem("pk_cust").getValueObject();
			Object obj2=card.getHeadItem("money").getValueObject();
			if((obj1==null||"".equals(obj1))&&(obj2==null||"".equals(obj2))){
				MessageDialog.showHintDlg(dialog, "提示", "客户跟金额不能同时为空，请重新输入！");
				return ;
			}
			pk_cust=obj1==null?"":obj1.toString();
			value1=obj2;
			
			flag=true;
			dialog.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	class NotMouseLister implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			flag=false;
			dialog.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	public String getPk_cust() {
		return pk_cust;
	}

	public void setPk_cust(String pk_cust) {
		this.pk_cust = pk_cust;
	}

	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
