package nc.ui.dahuan.xmrz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import sun.awt.AppContext;

import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.vo.fa.base.SwithCardSubVO;
import nc.vo.pub.lang.UFDate;

public class HtlogoaddDialog extends UIDialog {
	
	BillCardPanel card = null;	
	HtlogoDialog htdg;
	HtlogoaddDialog addg;
	int status;
	int editrow;
	IUAPQueryBS query; 
	
	public HtlogoaddDialog(Container parent){
		super(parent);
		htdg = (HtlogoDialog)parent;
		this.setModal(false);
	}
	
	// ����
	public void showHtlogoDialog() {	
		initialize();
		initDialog();
		initValue();
		status = 0;
		addg = this;
		this.showModal();
	}
	
	// �޸�
	public void showHtlogoDialog(int row,UFDate date,String cont,Object rzlx) {	
		initialize();
		initDialog();
		initValue(date,cont,rzlx);
		status = 1;
		editrow = row;
		addg = this;
		this.showModal();
		
	}
	
	// �鿴
	public void showHtlogoDialog(UFDate date,String cont,Object rzlx) {	
		initialize();
		initDialog2();
		initValue(date,cont,rzlx);
		addg = this;
		this.showModal();
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
			this.card.loadTemplet("0001AA1000000000GQVA");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("��־����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(600,300));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton sure = new UIButton("ȷ  ��");

		UIButton nos = new UIButton("ȡ  ��");
		// �Ӽ���
		sure.addMouseListener(new sureHtlogo());
		nos.addMouseListener(new nosHtlogo());
		UIPanel panel = new UIPanel();
		panel.add(sure);
		panel.add(nos);
		
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue(){
		card.setHeadItem("logo_date", ClientEnvironment.getInstance().getDate());
	}
	
	private void initValue(UFDate date,String cont,Object rzlx) {
		
		String rs=rzlx.toString();
		int i=0;
		if(rs.equals("���")){
			i=0;
		}else if(rs.equals("����")){
			i=1;
		}else if(rs.equals("����")){
			i=2;
		}else if(rs.equals("��װ")){
			i=3;
		}else if(rs.equals("����")){
			i=4;
		}else if(rs.equals("�깤")){
			i=5;
		}else if(rs.equals("�ɹ�")){
			i=6;
		}else if(rs.equals("����")){
			i=7;
		}else if(rs.equals("ά��")){
			i=8;
		}
		card.setHeadItem("logo_date",date );
		card.setHeadItem("logo_content", cont);
		card.setHeadItem("rzlx", i);
	}
	
	class sureHtlogo implements MouseListener{
		
		public void mouseClicked(MouseEvent e) {
			
			Object logodate = card.getHeadItem("logo_date").getValueObject();
			if(null == logodate || "".equals(logodate)){
				MessageDialog.showHintDlg(addg, "��ʾ", "��ά������");
				return;
			}
			Object content = card.getHeadItem("logo_content").getValueObject();
			if(null == content || "".equals(content)){
				MessageDialog.showHintDlg(addg, "��ʾ", "��ά����־����");
				return;
			}
			Object rzlx = card.getHeadItem("rzlx").getValueObject();
			if(null == rzlx || "".equals(rzlx)){
				MessageDialog.showHintDlg(addg, "��ʾ", "��ά����־����");
				return;
			}
			BillModel bd = htdg.card.getBillModel();
			if(0==status){				
				bd.addLine();
				int row = bd.getRowCount()-1;
				bd.setValueAt(logodate, row, "logo_date");
				bd.setValueAt(content, row, "content");
				bd.setValueAt(rzlx, row, "rzlx");
			}else{
				bd.setValueAt(logodate, editrow, "logo_date");
				bd.setValueAt(content, editrow, "content");
				bd.setValueAt(rzlx, editrow, "rzlx");
			}	
			addg.setVisible(false);
//			addg.dispose();
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}}
	
	
	
	class nosHtlogo implements MouseListener{	
		
		public void mouseClicked(MouseEvent e) {
			addg.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}}
	
	private void initDialog2() {
		// ���öԻ�������
		this.setTitle("��־����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(350,250));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
}
