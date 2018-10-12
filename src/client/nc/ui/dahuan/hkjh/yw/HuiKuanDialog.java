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
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.card) {
			// 初始化卡片单据模板
			this.card = new BillCardPanel();
			// 根据id加载配置的单据模板,配置的单据模板id 在表pub_billtemplet的pk_billtemplet字段中获得
			this.card.loadTemplet("0001AA1000000000MQ62");//合同事项弹出框的主键
			
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("回款分配");
		// 设置最适合的大小
		this.setSize(new Dimension(400,300));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		
		this.card.addBillEditListenerHeadTail(new HeadTailEditListener());
		
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");		
		UIButton notBtn = new UIButton("取  消");
		// 加监听
		sureBtn.addMouseListener(new SureMouseLister());
		notBtn.addMouseListener(new NotMouseLister());
		UIPanel panel = new UIPanel();
		panel.add(sureBtn);
		panel.add(notBtn);
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
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
				//查看可以填写的最大值
				String sql = " select nvl(h.rmb_amount, 0) - nvl(b.hk_amount, 0) fp_amount from dh_hkdh h left join (select d.pk_hkdh, sum(nvl(d.hk_amount, 0)) hk_amount "
							+" from dh_hkdh_d d where nvl(d.dr, 0) = 0 group by d.pk_hkdh) b on h.pk_hkdh = b.pk_hkdh where nvl(h.dr, 0) = 0 "
							+" and h.pk_hkdh = '"+hk_dhvo.getPk_hkdh()+"' ";
				
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Object objAmt = iQ.executeQuery(sql, new ColumnProcessor());       
				UFDouble ufd_amt = new UFDouble(objAmt==null?"0.00":objAmt.toString());
				
				Object conObj = card.getHeadItem("pk_contract").getValueObject();
				if(null != conObj){
					
					DhContractVO hvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conObj.toString());
					UFDouble ufd_htamt = hvo.getLjfkjhje()==null ? hvo.getDctjetotal() : hvo.getDctjetotal().sub(hvo.getLjfkjhje(), 2);//合同未付款剩余金额
					
					//ufd_amt = ufd_htamt.compareTo(ufd_amt)>0?ufd_amt:ufd_htamt;//取剩余最小的参照金额
					
					Object objCtAmt = card.getHeadItem("hk_amount").getValueObject();
					Object objCtAmt2 = card.getHeadItem("ctamount").getValueObject();
					UFDouble ufd_ctamt = new UFDouble(objCtAmt==null?"0.00":objCtAmt.toString());
					UFDouble ufd_ctamt2 = new UFDouble(objCtAmt2==null?"0.00":objCtAmt2.toString());
					
					if(null != objAmt){
						
						if(ufd_ctamt.compareTo(ufd_amt)>0){
							MessageDialog.showHintDlg(hkdlg, "提示", "回款金额超过允许分配的最大回款额["+ufd_amt.toString()+"]");
							return;
						}
						if(ufd_ctamt2.compareTo(ufd_htamt)>0){
							MessageDialog.showHintDlg(hkdlg, "提示", "已分配金额超过允许分配的最大额["+ufd_htamt.toString()+"]");
							return;
						}
					}else{
						MessageDialog.showHintDlg(hkdlg, "提示", "该回款金额已完全分配，不可再分配");
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
					
					/************************************** 更新合同档案主表  start  *********************************************/
					DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conObj.toString());
					UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
					ctvo.setLjfkjhje(ljfkjhje.add(dvo.getCt_amount(), 2));
					HYPubBO_Client.update(ctvo);
					/************************************** 更新合同档案主表  end   *********************************************/
					
					HYPubBO_Client.insert(dvo);//更新回款子表
					
					hk_dhvo.setSure_flag(1);
					HYPubBO_Client.update(hk_dhvo);//更新回款主表
					
					hkdlg.setVisible(false);
					
				}
			}catch(Exception en){
				//en.printStackTrace();
				MessageDialog.showHintDlg(card, "提示", en.getMessage());
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
