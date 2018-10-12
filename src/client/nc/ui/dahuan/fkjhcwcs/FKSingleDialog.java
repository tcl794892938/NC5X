package nc.ui.dahuan.fkjhcwcs;

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
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.fkjh.DhFkjhSingleVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class FKSingleDialog extends UIDialog {

	BillCardPanel card;
	FKSingleDialog slgdialog;
	String value;
	DhFkjhbillVO fkjhbillVO;
	
	public FKSingleDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showFKSingleDialog(DhFkjhbillVO fkjhVO) throws Exception{
		
		initialize();
		initDialog();
		fkjhbillVO = fkjhVO;
		initValue();
		slgdialog = this;
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
			this.card.loadTemplet("0001AA1000000000NY4V");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("�ֵ�");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(400,400));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton sureBtn = new UIButton("ȷ  ��");		
		UIButton notBtn = new UIButton("ȡ  ��");
		UIButton addBtn = new UIButton("��  ��");		
		UIButton delBtn = new UIButton("ɾ  ��");
		
		// �Ӽ���
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		addBtn.addMouseListener(new AddLineMouseLister());
		delBtn.addMouseListener(new DelLineMouseLister());
		
		this.card.addEditListener(new BodyEditLister());
		
		UIPanel panel = new UIPanel();
		panel.add(sureBtn);
		panel.add(notBtn);
		panel.add(addBtn);
		panel.add(delBtn);
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue() throws Exception{
		SuperVO[] spervos = HYPubBO_Client.queryByCondition(DhFkjhSingleVO.class, " pk_fkjh = '"+fkjhbillVO.getPk_fkjhbill()+"' ");
		UFDouble syDob = fkjhbillVO.getDfkje();
		if(null != spervos && spervos.length>0){
			BillModel bmodel = card.getBillModel();
			for(int i=0;i<spervos.length;i++ ){
				DhFkjhSingleVO slgVO = (DhFkjhSingleVO)spervos[i];
				bmodel.addLine();
				bmodel.setValueAt(slgVO.getSingle_amount(), i, "fkamount");
				syDob = syDob.sub(slgVO.getSingle_amount());
			}
		}
		
		card.setHeadItem("pk_fkjhbill", fkjhbillVO.getPk_fkjhbill());
		card.setHeadItem("sumamount", fkjhbillVO.getDfkje());
		card.setHeadItem("syamount", syDob);
	}
	
	class BodyEditLister implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			if("fkamount".equals(e.getKey())){
				// �ܶ� 
				UFDouble sumamount = new UFDouble(card.getHeadItem("sumamount").getValueObject()==null?"0.00"
						 							:card.getHeadItem("sumamount").getValueObject().toString());
				// ����� 
				UFDouble fkamount = new UFDouble("0.00");
				BillModel bmodel = card.getBillModel();
				int rows = bmodel.getRowCount();
				for(int i=0;i<rows;i++){
					UFDouble rowamount = new UFDouble(bmodel.getValueAt(i, "fkamount")==null?"0.00"
														:bmodel.getValueAt(i, "fkamount").toString());
					fkamount = fkamount.add(rowamount);
				}
				// ���
				UFDouble syamount = sumamount.sub(fkamount);
				card.setHeadItem("syamount", syamount);
			}
			
		}

		public void bodyRowChange(BillEditEvent e) {
			// TODO Auto-generated method stub
			
		}

	
		
	}
	
	class AddLineMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			card.getBillModel().addLine();
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	class DelLineMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			int selrow = card.getBillTable().getSelectedRow();	
			card.getBillModel().delLine(new int[]{selrow});
			// �ܶ� 
			UFDouble sumamount = new UFDouble(card.getHeadItem("sumamount").getValueObject()==null?"0.00"
					 							:card.getHeadItem("sumamount").getValueObject().toString());
			// ����� 
			UFDouble fkamount = new UFDouble("0.00");
			BillModel bmodel = card.getBillModel();
			int rows = bmodel.getRowCount();
			for(int i=0;i<rows;i++){
				UFDouble rowamount = new UFDouble(bmodel.getValueAt(i, "fkamount")==null?"0.00"
													:bmodel.getValueAt(i, "fkamount").toString());
				fkamount = fkamount.add(rowamount);
			}
			// ���
			UFDouble syamount = sumamount.sub(fkamount);
			card.setHeadItem("syamount", syamount);
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			UFDouble syamount = new UFDouble(card.getHeadItem("syamount").getValueObject()==null?"0.00"
												:card.getHeadItem("syamount").getValueObject().toString());
			if(syamount.compareTo(new UFDouble("0.00"))>0){
				MessageDialog.showHintDlg(slgdialog, "��ʾ", "�������");
				return;
			}else if(syamount.compareTo(new UFDouble("0.00"))<0){
				MessageDialog.showHintDlg(slgdialog, "��ʾ", "�������ѳ�");
				return;
			}else{
				BillModel bmodel = card.getBillModel();
				int rows = bmodel.getRowCount();
				String slgmegssage = "";
				
				DhFkjhSingleVO[] svos = new DhFkjhSingleVO[rows];
				for(int i=0;i<rows;i++){
					int hang = i+1;
					String sglamt = bmodel.getValueAt(i, "fkamount")==null?"0.00":bmodel.getValueAt(i, "fkamount").toString();
					slgmegssage += "��"+hang+"����"+sglamt+"�� ";
					DhFkjhSingleVO sglvo = new DhFkjhSingleVO();
					sglvo.setPk_fkjh(fkjhbillVO.getPk_fkjhbill());
					sglvo.setSingle_amount(new UFDouble(sglamt));
					svos[i]=sglvo;
				}
				
				try {
					
					HYPubBO_Client.deleteByWhereClause(DhFkjhSingleVO.class," pk_fkjh = '"+fkjhbillVO.getPk_fkjhbill()+"' ");
					
					HYPubBO_Client.insertAry(svos);
					
					fkjhbillVO.setIs_single(1);
					fkjhbillVO.setSingle_remark(slgmegssage);
					HYPubBO_Client.update(fkjhbillVO);
					slgdialog.setVisible(false);
				} catch (UifException e) {
					e.printStackTrace();
				}
				
			}
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class NotMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			slgdialog.setVisible(false);	
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

}
