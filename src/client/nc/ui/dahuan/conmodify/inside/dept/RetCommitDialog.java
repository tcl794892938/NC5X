package nc.ui.dahuan.conmodify.inside.dept;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;

public class RetCommitDialog extends UIDialog {

	BillCardPanel card;
	String vemo;
	RetCommitDialog rtdg;
	boolean flag=false;
	
	public RetCommitDialog(Container parent) {
		super(parent);
		this.setModal(false);
	}
	
	public boolean showRetCommitDialog(){
		initialize();
		initDialog();
		rtdg = this;
		this.showModal();
		return flag;
	}
	
	private void initialize(){
		if(null == card){
			card = new BillCardPanel();
			card.loadTemplet("0001AA1000000000SIIW");
		}
	}

	private void initDialog() {
		// ���öԻ�������
		this.setTitle("��������");
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
			Object vemoObj = card.getHeadItem("vemo").getValueObject();
			vemo = vemoObj==null?"":vemoObj.toString();
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
	public String getVemo() {
		return vemo;
	}

	public void setVemo(String vemo) {
		this.vemo = vemo;
	}

}
