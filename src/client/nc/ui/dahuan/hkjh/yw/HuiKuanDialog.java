package nc.ui.dahuan.hkjh.yw;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class HuiKuanDialog extends UIDialog {

	BillCardPanel card;
	HuiKuanDialog hkdlg;
	String pk_corp;
	String pk_user;
	HkdhVO hk_dhvo;
	UFDate uf_date;
	
	IUAPQueryBS iQ=null;
	
	public HuiKuanDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showHuiKuanDialog(HkdhVO hvo,String pkCorp,String pkUser,UFDate ufdate) throws Exception{
		
		pk_corp = pkCorp;
		pk_user = pkUser;
		hk_dhvo = hvo;
		uf_date = ufdate;
		
		iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
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
			this.card.loadTemplet("0001AA1000000000MQ62");//��ͬ������������
			
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("�ؿ����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(400,300));
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
		
	}
	
	private void initValue(){
		String newWherePart = " and exists (select 1 from (select c.pk_contract, c.dctjetotal from dh_contract c "
            +" where nvl(c.dr, 0) = 0 and nvl(c.httype, 2) = 0 and nvl(c.is_seal, 0) = 1) m "
            +" left join (select t.pk_contract, sum(nvl(t.ct_amount, 0)) ct_amount "
            +" from (select d.pk_contract, d.ct_amount from dh_hkdh_d d where nvl(d.dr, 0) = 0 "
            +" union all select r.pk_contract, r.ct_amount from dh_hkreplace r) t group by t.pk_contract) l "
            +" on m.pk_contract = l.pk_contract where  "
            +"  m.pk_contract = v.pk_contract) ";
	
		UIRefPane uif = (UIRefPane)card.getHeadItem("pk_contract").getComponent();
		uif.getRefModel().addWherePart(newWherePart);
	}
	
	private String getConfString(){
		return " and v.pk_deptdoc in (select f.pk_deptdoc from v_deptperonal f where f.pk_corp='"+pk_corp+"' and f.pk_user='"+pk_user+"') ";
	}
	
	
	class HeadTailEditListener implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			
			if("pk_contract".equals(e.getKey())){
				try{
					Object conObj = card.getHeadItem("pk_contract").getValueObject();
					if(null != conObj){
						String sql = "select v.invname,c.vdef6,to_char(nvl(c.dctjetotal,0)-nvl(c.ljfkjhje,0),'fm99999999990.00') syje" +
								" from dh_contract c left join bd_invbasdoc v on c.ctname = v.pk_invbasdoc "+
									//" and c.pk_corp = v.pk_corp where c.pk_contract = '"+conObj.toString()+"' ";
									 " where c.pk_contract = '"+conObj.toString()+"' ";
						
						Map<String,String> vmap = (Map<String,String>)iQ.executeQuery(sql, new MapProcessor());
						card.setHeadItem("ctname", vmap.get("invname"));
						card.setHeadItem("xmname", vmap.get("vdef6"));
						card.setHeadItem("ctamount", vmap.get("syje"));
					}else{
						card.setHeadItem("ctname", null);
						card.setHeadItem("xmname", null);
						card.setHeadItem("ctamount", null);
					}
				}catch(Exception e2){
					e2.printStackTrace();
				}
			}
			
			if(e.getKey().equals("hk_amount")||e.getKey().equals("discount_amount")){
				//card.execHeadLoadFormulas();
				card.execHeadTailEditFormulas();
			}
		}

		public void bodyRowChange(BillEditEvent e) {
			
		}
		
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			try{
				
				card.dataNotNullValidate();
				card.stopEditing();
				//�鿴������д�����ֵ
				String sql = " select nvl(h.rmb_amount, 0) - nvl(b.hk_amount, 0) fp_amount from dh_hkdh h left join (select d.pk_hkdh, sum(nvl(d.hk_amount, 0)) hk_amount "
							+" from dh_hkdh_d d where nvl(d.dr, 0) = 0 group by d.pk_hkdh) b on h.pk_hkdh = b.pk_hkdh where nvl(h.dr, 0) = 0 "
							+" and h.pk_hkdh = '"+hk_dhvo.getPk_hkdh()+"' ";
				
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Object objAmt = iQ.executeQuery(sql, new ColumnProcessor());       
				UFDouble ufd_amt = new UFDouble(objAmt==null?"0.00":objAmt.toString());
				
				Object conObj = card.getHeadItem("pk_contract").getValueObject();
				if(null != conObj){
					
					DhContractVO hvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conObj.toString());
					UFDouble ufd_htamt = hvo.getLjfkjhje()==null ? hvo.getDctjetotal() : hvo.getDctjetotal().sub(hvo.getLjfkjhje(), 2);//��ͬδ����ʣ����
					
					//ufd_amt = ufd_htamt.compareTo(ufd_amt)>0?ufd_amt:ufd_htamt;//ȡʣ����С�Ĳ��ս��
					
					Object objCtAmt = card.getHeadItem("hk_amount").getValueObject();
					Object objCtAmt2 = card.getHeadItem("ctamount").getValueObject();
					UFDouble ufd_ctamt = new UFDouble(objCtAmt==null?"0.00":objCtAmt.toString());
					UFDouble ufd_ctamt2 = new UFDouble(objCtAmt2==null?"0.00":objCtAmt2.toString());
					
					if(null != objAmt){
						
						if(ufd_ctamt.compareTo(ufd_amt)>0){
							MessageDialog.showHintDlg(hkdlg, "��ʾ", "�ؿ���������������ؿ��["+ufd_amt.toString()+"]");
							return;
						}
						if(ufd_ctamt2.compareTo(ufd_htamt)>0){
							MessageDialog.showHintDlg(hkdlg, "��ʾ", "�ѷ������������������["+ufd_htamt.toString()+"]");
							return;
						}
					}else{
						MessageDialog.showHintDlg(hkdlg, "��ʾ", "�ûؿ�������ȫ���䣬�����ٷ���");
						return;
					}
					
					HkdhDVO dvo = new HkdhDVO();
					dvo.setPk_hkdh(hk_dhvo.getPk_hkdh());
					dvo.setPk_contract(conObj.toString());
					
					UIRefPane conUif = (UIRefPane)card.getHeadItem("pk_contract").getComponent();
					
					dvo.setCtcode(conUif.getRefCode());
					dvo.setCtname(card.getHeadItem("ctname").getValueObject().toString());
					dvo.setXmname(card.getHeadItem("xmname").getValueObject().toString());
					
					Object disamount = card.getHeadItem("discount_amount").getValueObject();//by tcl
					UFDouble disamount1 = new UFDouble(disamount==null?"0.00":disamount.toString());
					dvo.setDiscount_amount(disamount1);
					Object hkamount = card.getHeadItem("hk_amount").getValueObject();
					UFDouble hkamount1 = new UFDouble(hkamount==null?"0.00":hkamount.toString());
					dvo.setHk_amount(hkamount1);
					
					dvo.setCt_amount(ufd_ctamt2);
					dvo.setVoperid(pk_user);
					dvo.setDbilldate(uf_date);
					
					String deptsql = "select v.pk_deptdoc from v_deptperonal v where v.pk_corp='"+pk_corp+"' and v.pk_user='"+pk_user+"' ";
					List<Map<String,String>> deptlist = (List<Map<String,String>>)iQ.executeQuery(deptsql, new MapListProcessor());
					if(null != deptlist && deptlist.size()==1){
						Map<String,String> deptmap = deptlist.get(0);
						dvo.setPk_dept(deptmap.get("pk_deptdoc"));
					}
					
					/************************************** ���º�ͬ��������  start  *********************************************/
					DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conObj.toString());
					UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
					ctvo.setLjfkjhje(ljfkjhje.add(dvo.getCt_amount(), 2));
					HYPubBO_Client.update(ctvo);
					/************************************** ���º�ͬ��������  end   *********************************************/
					
					HYPubBO_Client.insert(dvo);//���»ؿ��ӱ�
					
					hk_dhvo.setSure_flag(1);
					HYPubBO_Client.update(hk_dhvo);//���»ؿ�����
					
					hkdlg.setVisible(false);
					
				}
			}catch(Exception en){
				//en.printStackTrace();
				MessageDialog.showHintDlg(card, "��ʾ", en.getMessage());
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
