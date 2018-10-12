package nc.ui.dahuan.hkjh.yw;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.pub.lang.UFDate;

public class EditDialog extends UIDialog {

	BillCardPanel card;
	EditDialog hkdlg;
	HkdhDVO[] hk_dtlvos;
	UFDate uf_date;
	
	public EditDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showEditDialog(HkdhDVO[] newhk_dtlvos,UFDate newuf_date) throws Exception{
		
		hk_dtlvos = newhk_dtlvos;
		uf_date = newuf_date;
		initialize();
		initDialog();
		initValue();
		hkdlg = this;
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
			this.card.loadTemplet("0001AA1000000000MZEM");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("�ؿ����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(400,200));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		
		this.card.addBillEditListenerHeadTail(new HeadTailEditListener());
		
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
		
		this.setResizable(true);
		
	}
	
	private void initValue(){
		this.card.getBillModel().setBodyDataVO(hk_dtlvos);
	}
	
	
	class HeadTailEditListener implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			
			
		}

		public void bodyRowChange(BillEditEvent e) {
			
		}
		
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			try{
				HkdhDVO[] dvos = (HkdhDVO[])card.getBillModel().getBodyValueVOs(HkdhDVO.class.getName());
				for(HkdhDVO dvo : dvos){
					dvo.setDbilldate(uf_date);
					HYPubBO_Client.update(dvo);
				}
			}catch(Exception e2){
				e2.printStackTrace();
			}finally{
				hkdlg.setVisible(false);
			}
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class NotMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			hkdlg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

}
