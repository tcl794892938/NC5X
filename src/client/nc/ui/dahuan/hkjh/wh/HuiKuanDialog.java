package nc.ui.dahuan.hkjh.wh;

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
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.dahuan.hkjh.HkwhDVO;
import nc.vo.dahuan.hkjh.HkwhVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class HuiKuanDialog extends UIDialog {

	BillCardPanel card;
	HuiKuanDialog hkdlg;
	String pk_corp;
	String pk_user;
	HkwhVO hk_dhvo;
	UFDate uf_date;
	
	IUAPQueryBS iQ=null;
	
	public HuiKuanDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showHuiKuanDialog(HkwhVO hvo,String pkCorp,String pkUser,UFDate ufdate) throws Exception{
		
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
			this.card.loadTemplet("1001AA1000000003AS0F");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("�ؿ����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(600,200));
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
		String newWherePart = " and v.syje>0 and v.pk_cust1 = '"+hk_dhvo.getPk_cust()+"' ";
	
		UIRefPane uif = (UIRefPane)card.getHeadItem("pk_contract").getComponent();
		uif.getRefModel().addWherePart(newWherePart);
	}
	
	
	class HeadTailEditListener implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			
			if("pk_contract".equals(e.getKey())){
				try{
					Object conObj = card.getHeadItem("pk_contract").getValueObject();
					if(null != conObj){
						String sql = "select v.invname,c.vdef6,c.currenty_rate,c.pk_currenty from dh_contract c left join bd_invbasdoc v on c.ctname = v.pk_invbasdoc "+
									//" and c.pk_corp = v.pk_corp where c.pk_contract = '"+conObj.toString()+"' ";
									" where c.pk_contract = '"+conObj.toString()+"' ";// by tcl
						
						Map<String,Object> vmap = (Map<String,Object>)iQ.executeQuery(sql, new MapProcessor());
						card.setHeadItem("ctname", vmap.get("invname"));
						card.setHeadItem("xmname", vmap.get("vdef6"));
						card.setHeadItem("pk_bizong", vmap.get("pk_currenty"));
						UFDouble bzbl = vmap.get("currenty_rate")==null?new UFDouble("0.0000"):new UFDouble(vmap.get("currenty_rate").toString());
						card.setHeadItem("bzbl", bzbl);
						card.setHeadItem("wb_amount", hk_dhvo.getHk_amount());
						card.setHeadItem("ctamount", hk_dhvo.getHk_amount().multiply(bzbl, 2));
					}else{
						card.setHeadItem("ctname", null);
						card.setHeadItem("xmname", null);
						card.setHeadItem("pk_bizong", null);
						card.setHeadItem("bzbl", null);
						card.setHeadItem("wb_amount", null);
						card.setHeadItem("ctamount", null);
					}
				}catch(Exception e2){
					e2.printStackTrace();
				}
			}
		}

		public void bodyRowChange(BillEditEvent e) {
			
		}
		
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			try{
				
				Object conObj = card.getHeadItem("pk_contract").getValueObject();
				if(null != conObj){
					IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
					
					String sysql = "select v.syje from vsale_contract v where v.pk_contract='"+conObj.toString()+"'";
					Object syAmt = iQ.executeQuery(sysql, new ColumnProcessor());       
					UFDouble sy_amt = new UFDouble(syAmt==null?"0.00":syAmt.toString());
					
					Object objAmt = card.getHeadItem("ctamount").getValueObject();
					UFDouble ufd_amt = new UFDouble(objAmt==null?"0.00":objAmt.toString());
					
					if(ufd_amt.compareTo(sy_amt)>0){
						MessageDialog.showHintDlg(hkdlg, "��ʾ", "�������������������");
						return;
					}
					
					
					HkwhDVO dvo = new HkwhDVO();
					dvo.setPk_hkwh(hk_dhvo.getPk_hkwh());
					dvo.setPk_contract(conObj.toString());
					
					UIRefPane conUif = (UIRefPane)card.getHeadItem("pk_contract").getComponent();
					
					dvo.setCtcode(conUif.getRefCode());
					dvo.setCtname(card.getHeadItem("ctname").getValueObject().toString());
					dvo.setXmname(card.getHeadItem("xmname").getValueObject().toString());
					dvo.setCt_amount(ufd_amt);
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
					hk_dhvo.setBzbl(new UFDouble(card.getHeadItem("bzbl").getValueObject().toString()));
					hk_dhvo.setPk_bizong(card.getHeadItem("pk_bizong").getValueObject().toString());
					hk_dhvo.setRmb_amount(ufd_amt);
					HYPubBO_Client.update(hk_dhvo);//���»ؿ�����
					
					
					/************************************** ���»ؿ�  start  *********************************************/
					HkdhVO fpvo = new HkdhVO();
					fpvo.setSure_flag(1);
					fpvo.setBill_flag(3);
					fpvo.setHkbillno(hk_dhvo.getHkbillno());
					fpvo.setPk_cust(hk_dhvo.getPk_cust());
					fpvo.setHk_amount(hk_dhvo.getHk_amount());
					fpvo.setPk_bizong(hk_dhvo.getPk_bizong());
					fpvo.setBzbl(hk_dhvo.getBzbl());
					fpvo.setRmb_amount(hk_dhvo.getRmb_amount());
					fpvo.setVemo(hk_dhvo.getVemo());
					fpvo.setRemark(hk_dhvo.getRemark());
					fpvo.setVoperid(hk_dhvo.getVoperid());
					fpvo.setDbilldate(hk_dhvo.getDbilldate());
					fpvo.setPk_corp(hk_dhvo.getPk_corp());
					String pkFp = HYPubBO_Client.insert(fpvo);
					
					HkdhDVO fpdvo = new HkdhDVO();
					fpdvo.setPk_hkdh(pkFp);
					fpdvo.setPk_contract(dvo.getPk_contract());
					fpdvo.setCtcode(dvo.getCtcode());
					fpdvo.setCtname(dvo.getCtname());
					fpdvo.setXmname(dvo.getXmname());
					fpdvo.setCt_amount(dvo.getCt_amount());
					fpdvo.setPk_dept(dvo.getPk_dept());
					fpdvo.setVoperid(dvo.getVoperid());
					fpdvo.setDbilldate(dvo.getDbilldate());
					HYPubBO_Client.insert(fpdvo);
					/************************************** ���»ؿ�  end  *********************************************/
					
					hkdlg.setVisible(false);
					
				}
			}catch(Exception en){
				en.printStackTrace();
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
