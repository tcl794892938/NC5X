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
		// ���öԻ�������
		this.setTitle("��Ʊ˰��");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(300, 150));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton sureBtn = new UIButton("ȷ  ��");
		UIButton notBtn = new UIButton("ȡ  ��");
		sureBtn.setPreferredSize(new Dimension(80, 25));
		notBtn.setPreferredSize(new Dimension(80, 25));
		//����label
		UILabel label=new UILabel("˰��:");
		label.setPreferredSize(new Dimension(50,30));
		field=new UITextField();
		field.setPreferredSize(new Dimension(100,25));
		field.setTextType(UITextType.TextDbl);
		// �Ӽ���
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		
		UIPanel a_panel = new UIPanel();
		UIPanel b_panel= new UIPanel();
		a_panel.add(label);
		a_panel.add(field);
		b_panel.add(sureBtn);
		b_panel.add(notBtn);
		// ��panel���ص��Ի�����
		this.add(a_panel, BorderLayout.CENTER);
		this.add(b_panel, BorderLayout.SOUTH);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	}
	

	class SureMouseLister implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			
			field.getTextType();
			String text=field.getText();
			if(text==null||"".equals(text)){
				MessageDialog.showHintDlg(dialog, "��ʾ", "������˰�ʣ�");
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
