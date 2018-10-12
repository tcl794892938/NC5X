package nc.ui.bxgt.djtc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.textfield.UITextType;

public class ProcessEditDlg extends UIDialog implements ActionListener {

	private UIButton ok_btn;

	private UIButton cancl_btn;

	private UILabel label;

	private UILabel label_1;

	private UIComboBox txtLoad1;

	private UITextField txtLoad2;

	public String value1; // ����

	public int value2 ;// ����

	String[] proPks = null;// ������PKֵ

	String billType = ""; // ���ĵĵ�������

	public ProcessEditDlg(Container parent, String title, String[] pks) {
		super(parent, title);
		proPks = pks;
		// this.billType = billType;
		init();
	}

	public void actionPerformed(ActionEvent e) {
//		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
//				.lookup(IbxgtQuerySynchro.class.getName());
		
		if (e.getSource() == getCancl_btn()) {
			closeCancel();
		} else if (e.getSource() == getOk_btn()) {
//			 �����ǰ���Ӻ����
			value1 = (String) txtLoad1.getSelectdItemValue();
			// �������
			value2 = Integer.valueOf(txtLoad2.getText());
			// 
			String strWhere = "and a.csaleid in ('";
			for (int i = 0; i < proPks.length; i++) {
				strWhere += proPks[i] + "','";
			}
			strWhere = strWhere.substring(0, strWhere.lastIndexOf(",")) + ")";
			if ("��ǰ".equals(value1)) {
				value2 = -value2;
			}
			if ("�Ƴ�".equals(value1)) {
				value2 = +value2;
			}
			setValue2(value2);
			// 2015-4-10 bwy ����
			// if ("���۶���".equals(billType)) {
			// try {
			// int i = MessageDialog.showOkCancelDlg(this, "��ʾ", "ȷ��������");
			// if (i == UIDialog.ID_OK) {
			// ibxgt.bxgtBatchUpdateSaleOrder(proPks, value2,
			// strWhere, "������");
			//
			// }
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }
			// }

			closeOK();
		}
	}

	private void init() {
		getContentPane().setBackground(new Color(230, 231, 245));

		getContentPane().setLayout(null);
		getContentPane().setFocusTraversalPolicyProvider(true);
		setSize(new Dimension(402, 270));

		getContentPane().add(getLabel());
		getContentPane().add(getLabel_1());

		getContentPane().add(getTxtLoad1());
		getContentPane().add(getTxtLoad2());

		getContentPane().add(getOk_btn());
		getContentPane().add(getCancl_btn());

	}

	public UIButton getOk_btn() {
		if (this.ok_btn == null) {
			this.ok_btn = new UIButton();
			this.ok_btn.setBounds(90, 205, 75, 20);
			this.ok_btn.addActionListener(this);
			this.ok_btn.setText("ȷ��");
		}

		return this.ok_btn;
	}

	public UIButton getCancl_btn() {
		if (this.cancl_btn == null) {
			this.cancl_btn = new UIButton();
			this.cancl_btn.setText("ȡ��");
			this.cancl_btn.setBounds(220, 205, 84, 20);
			this.cancl_btn.addActionListener(this);
		}
		return this.cancl_btn;
	}

	public UILabel getLabel() {
		if (this.label == null) {
			this.label = new UILabel();
			this.label.setText("����");
			this.label.setBounds(50, 20, 120, 18);
		}
		return this.label;
	}

	public UILabel getLabel_1() {
		if (this.label_1 == null) {
			this.label_1 = new UILabel();
			this.label_1.setBounds(50, 66, 120, 18);
			this.label_1.setText("����");
		}
		return this.label_1;
	}

	public UIComboBox getTxtLoad1() {
		if (this.txtLoad1 == null) {
			this.txtLoad1 = new UIComboBox();
			this.txtLoad1.setBounds(156, 18, 171, 20);
			this.txtLoad1.setVisible(true);
			Object[] os = new Object[] { "��ǰ", "�Ƴ�" };
			this.txtLoad1.addItems(os);
		}
		return this.txtLoad1;
	}

	public UITextField getTxtLoad2() {
		if (this.txtLoad2 == null) {
			try {
				this.txtLoad2 = new UITextField();
				this.txtLoad2.setName("rq2");
				this.txtLoad2.setBounds(156, 65, 171, 20);
				this.txtLoad2.setVisible(true);
				this.txtLoad2.setTextType(UITextType.TextInt);
			} catch (Throwable ivjExc) {
			}
		}
		return this.txtLoad2;
	}

	public void valueChanged(ValueChangedEvent event) {
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public int getValue2() {
//		value2 = Integer.valueOf(txtLoad2.getText());
		return value2;
	}

	public void setValue2(int value2) {
		this.value2 = value2;
	}

}
