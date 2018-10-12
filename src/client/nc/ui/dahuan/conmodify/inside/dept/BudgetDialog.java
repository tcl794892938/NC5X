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
		// ���öԻ�������
		this.setTitle("��ĿԤ��");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(600,400));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton sureBtn = new UIButton("ȷ  ��");		
		UIButton notBtn = new UIButton("ȡ  ��");
		// �Ӽ���
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		UIPanel panel = new UIPanel();
		panel.add(sureBtn);
		panel.add(notBtn);
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			Object bgObj = card.getHeadItem("budget").getValueObject();
			if(null == bgObj || "".equals(bgObj)){
				MessageDialog.showHintDlg(rtdg, "��ʾ", "��ά����ĿԤ��");
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
