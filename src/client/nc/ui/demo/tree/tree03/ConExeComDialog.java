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
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.card) {
			// 初始化卡片单据模板
			this.card = new BillCardPanel();
			// 根据id加载配置的单据模板,配置的单据模板id 在表pub_billtemplet的pk_billtemplet字段中获得
			this.card.loadTemplet("0001AA1000000000HK6R");
			
			/*if(dhvo.getHttype()==1){//项目成本说明
				this.card.getHeadItem("vshuoming").setEnabled(true);
			}else if(dhvo.getHttype()==0){
				this.card.getHeadItem("vshuoming").setEnabled(false);
			}*/
			this.card.addBillEditListenerHeadTail(new HeadAfterEditListner());
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	/**
	 * 初始化界面UI
	 */
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("合同执行");
		// 设置最适合的大小
		this.setSize(new Dimension(600,175));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton confirm = new UIButton("确  定");
		UIButton negate = new UIButton("取  消");
		// 加监听
		confirm.addMouseListener(new ConfirmListener());
		negate.addMouseListener(new NegateListener());
		UIPanel panel = new UIPanel();
		panel.add(confirm);
		panel.add(negate);
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	/**
	 * 初始化数据
	 */
	private void initValue(){
		String pkXmjl = dhvo.getPk_xmjl();
		UIRefPane uiref = (UIRefPane)card.getHeadItem("conexe").getComponent();
		uiref.setPK(pkXmjl);
	}
	
	class ConfirmListener implements MouseListener{
		// 确定按钮点击事件
		public void mouseClicked(MouseEvent mouseevent) {
			// 更新主合同的项目经理和执行开始标识
			
			UIRefPane uiref = (UIRefPane)card.getHeadItem("conexe").getComponent();
			String conexepk = uiref.getRefPK();
			Object xmaobj = card.getHeadItem("xm_amount").getValueObject();
			Object vshuoming=card.getHeadItem("vshuoming").getValueObject();
			if("".equals(conexepk)||(null == xmaobj || "".equals(xmaobj))){
				MessageDialog.showHintDlg(dg, "提示", "请维护好【项目经理】【项目预算】");
				return;
			}
			
			UFDouble xmysmny=new UFDouble(xmaobj.toString());
			UFDouble htmny=dhvo.getDctjetotal()==null?new UFDouble(0):dhvo.getDctjetotal();
			if(xmysmny.compareTo(htmny)>0&&dhvo.getHttype()==0){
				if(null == vshuoming || "".equals(vshuoming)){
					MessageDialog.showHintDlg(dg, "提示", "项目预算成本大于销售合同金额，请填写说明！");
					return;
				}
			}
			if(xmysmny.compareTo(htmny)<0&&dhvo.getHttype()==1){
				if(null == vshuoming || "".equals(vshuoming)){
					MessageDialog.showHintDlg(dg, "提示", "项目预算成本小于采购合同金额，请填写说明！");
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
		// 取消按钮点击事件
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
				if(xmmny.compareTo(htmny)>0&&dhvo.getHttype()==0){//【预算成本】大于合同金额需要填写说明
					card.getHeadItem("vshuoming").setEnabled(true);
				}else if(xmmny.compareTo(htmny)<0&&dhvo.getHttype()==1){//【预算成本】小于合同金额需要填写说明
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
