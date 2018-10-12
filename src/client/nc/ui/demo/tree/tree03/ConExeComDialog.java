package nc.ui.demo.tree.tree03;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.b38.JobbasfilVO;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class ConExeComDialog extends UIDialog {
	
	BillCardPanel card = null;	
	ConExeComDialog dg = null;
	DhContractVO dhvo = null;
	boolean retExe = false;
	
	public ConExeComDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public boolean showConExecuteModel(DhContractVO dhcvo) throws Exception{
		this.dhvo = dhcvo;
		initialize();
		initDialog();
		initValue();
		dg = this;
		this.showModal();
		return retExe;
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
			this.card.loadTemplet("0001AA1000000000HK6R");
			
			/*if(dhvo.getHttype()==1){//��Ŀ�ɱ�˵��
				this.card.getHeadItem("vshuoming").setEnabled(true);
			}else if(dhvo.getHttype()==0){
				this.card.getHeadItem("vshuoming").setEnabled(false);
			}*/
			this.card.addBillEditListenerHeadTail(new HeadAfterEditListner());
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	/**
	 * ��ʼ������UI
	 */
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("��ִͬ��");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(600,175));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton confirm = new UIButton("ȷ  ��");
		UIButton negate = new UIButton("ȡ  ��");
		// �Ӽ���
		confirm.addMouseListener(new ConfirmListener());
		negate.addMouseListener(new NegateListener());
		UIPanel panel = new UIPanel();
		panel.add(confirm);
		panel.add(negate);
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	/**
	 * ��ʼ������
	 */
	private void initValue(){
		String pkXmjl = dhvo.getPk_xmjl();
		UIRefPane uiref = (UIRefPane)card.getHeadItem("conexe").getComponent();
		uiref.setPK(pkXmjl);
	}
	
	class ConfirmListener implements MouseListener{
		// ȷ����ť����¼�
		public void mouseClicked(MouseEvent mouseevent) {
			// ��������ͬ����Ŀ�����ִ�п�ʼ��ʶ
			
			UIRefPane uiref = (UIRefPane)card.getHeadItem("conexe").getComponent();
			String conexepk = uiref.getRefPK();
			Object xmaobj = card.getHeadItem("xm_amount").getValueObject();
			Object vshuoming=card.getHeadItem("vshuoming").getValueObject();
			if("".equals(conexepk)||(null == xmaobj || "".equals(xmaobj))){
				MessageDialog.showHintDlg(dg, "��ʾ", "��ά���á���Ŀ��������ĿԤ�㡿");
				return;
			}
			
			UFDouble xmysmny=new UFDouble(xmaobj.toString());
			UFDouble htmny=dhvo.getDctjetotal()==null?new UFDouble(0):dhvo.getDctjetotal();
			if(xmysmny.compareTo(htmny)>0&&dhvo.getHttype()==0){
				if(null == vshuoming || "".equals(vshuoming)){
					MessageDialog.showHintDlg(dg, "��ʾ", "��ĿԤ��ɱ��������ۺ�ͬ������д˵����");
					return;
				}
			}
			if(xmysmny.compareTo(htmny)<0&&dhvo.getHttype()==1){
				if(null == vshuoming || "".equals(vshuoming)){
					MessageDialog.showHintDlg(dg, "��ʾ", "��ĿԤ��ɱ�С�ڲɹ���ͬ������д˵����");
					return;
				}
			}
			try{
				dhvo.setIs_conexe(1);
				dhvo.setPk_xmjl(conexepk);
				dhvo.setXm_amount(xmysmny);
				dhvo.setVdef3(vshuoming.toString());
				HYPubBO_Client.update(dhvo);
				retExe = true;
				dg.setVisible(false);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
	class NegateListener implements MouseListener{
		// ȡ����ť����¼�
		public void mouseClicked(MouseEvent mouseevent) {
			retExe = false;
			dg.setVisible(false);
		}

		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
	class HeadAfterEditListner implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			
			if(e.getKey().equals("xm_amount")){
				
				card.setHeadItem("vshuoming", null);
				Object xmaobj = card.getHeadItem("xm_amount").getValueObject();
				
				UFDouble xmmny=new UFDouble(0);
				if(xmaobj!=null&&!"".equals(xmaobj)){
					xmmny=new UFDouble(xmaobj.toString());
				}
				UFDouble htmny=dhvo.getDctjetotal()==null?new UFDouble(0):dhvo.getDctjetotal();
				if(xmmny.compareTo(htmny)>0&&dhvo.getHttype()==0){//��Ԥ��ɱ������ں�ͬ�����Ҫ��д˵��
					card.getHeadItem("vshuoming").setEnabled(true);
				}else if(xmmny.compareTo(htmny)<0&&dhvo.getHttype()==1){//��Ԥ��ɱ���С�ں�ͬ�����Ҫ��д˵��
					card.getHeadItem("vshuoming").setEnabled(true);
				}else{
					card.getHeadItem("vshuoming").setEnabled(false);
				}
				
			}
		}

		public void bodyRowChange(BillEditEvent e) {
		}
	}
	
}
