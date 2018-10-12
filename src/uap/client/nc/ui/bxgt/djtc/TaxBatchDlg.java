package nc.ui.bxgt.djtc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.textfield.UITextType;

public class TaxBatchDlg extends UIDialog {

	public TaxBatchDlg(Container parent) {
		super(parent);
		initDialog();
		this.showModal();
	}
	
	private TaxBatchDlg dialog;
	private Object textValue;
	private UITextField field;

	private void initDialog() {
		dialog=this;
		// 设置对话框主题
		this.setTitle("发票税率");
		// 设置最适合的大小
		this.setSize(new Dimension(300, 150));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");
		UIButton notBtn = new UIButton("取  消");
		sureBtn.setPreferredSize(new Dimension(80, 25));
		notBtn.setPreferredSize(new Dimension(80, 25));
		//设置label
		UILabel label=new UILabel("税率:");
		label.setPreferredSize(new Dimension(50,30));
		field=new UITextField();
		field.setPreferredSize(new Dimension(100,25));
		field.setTextType(UITextType.TextDbl);
		// 加监听
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		
		UIPanel a_panel = new UIPanel();
		UIPanel b_panel= new UIPanel();
		a_panel.add(label);
		a_panel.add(field);
		b_panel.add(sureBtn);
		b_panel.add(notBtn);
		// 将panel加载到对话框中
		this.add(a_panel, BorderLayout.CENTER);
		this.add(b_panel, BorderLayout.SOUTH);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}
	

	class SureMouseLister implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			
			field.getTextType();
			String text=field.getText();
			if(text==null||"".equals(text)){
				MessageDialog.showHintDlg(dialog, "提示", "请输入税率！");
				return ;
			}
			else{
				textValue=text;
			}
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
			textValue=null;
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
	

	public Object getTextValue() {
		return textValue;
	}

	public void setTextValue(Object textValue) {
		this.textValue = textValue;
	}
	
}
