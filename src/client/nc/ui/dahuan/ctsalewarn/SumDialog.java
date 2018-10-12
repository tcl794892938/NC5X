package nc.ui.dahuan.ctsalewarn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;

public class SumDialog extends UIDialog {

	BillCardPanel card;
	SumDialog sumdlg;
	String[] retStrs;
	
	public SumDialog(Container parent){
		super(parent);
		this.setModal(true);
	}
	
	public String[] showSumDialog() throws Exception{
		
		initialize();
		initDialog();
		sumdlg = this;
		this.showModal();
		return retStrs;
	}
	
	/**
	 * ��ʼ���Ի������������
	 */
	private void initialize() {
		// ��ʼ��Ϊ��
		if (null == this.card) {
			// ��ʼ����Ƭ����ģ��
			this.card = new BillCardPanel();
			// ����id�������õĵ���ģ��,���õĵ���ģ��id �ڱ�pub_billtemplet��pk_billtemplet�ֶ��л��
			this.card.loadTemplet("0001AA1000000000WH84");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("С����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(200,250));
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
			
			Object ctcodeObj = sumdlg.card.getHeadItem("isctcode").getValueObject();
			Object ctnameObj = sumdlg.card.getHeadItem("isctname").getValueObject();
			Object xmnameObj = sumdlg.card.getHeadItem("isxmname").getValueObject();
			Object deptObj = sumdlg.card.getHeadItem("isdept").getValueObject();
			Object custObj = sumdlg.card.getHeadItem("iscust").getValueObject();
			
			List<String> strlist = new ArrayList<String>();
			if("true".equals(ctcodeObj)){
				strlist.add("isctcode");
			}
			if("true".equals(ctnameObj)){
				strlist.add("isctname");
			}
			if("true".equals(xmnameObj)){
				strlist.add("isxmname");
			}
			if("true".equals(deptObj)){
				strlist.add("isdept");
			}
			if("true".equals(custObj)){
				strlist.add("iscust");
			}
			retStrs = strlist.toArray(new String[0]);
			
			sumdlg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class NotMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			retStrs = null;
			sumdlg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

}
