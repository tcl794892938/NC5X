package nc.ui.dahuan.fhzz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;

public class DeliveryDialog extends UIDialog {
	
	BillCardPanel card = null;	
	String pkContract;
	
	public DeliveryDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showDeliveryDialog(String pkcon) throws Exception{
		pkContract = pkcon;
		initialize();
		initDialog();
		initValue();	
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
			this.card.loadTemplet("0001AA1000000000IJOK");//合同事项弹出框的主键
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("发货通知单联查");
		// 设置最适合的大小
		this.setSize(new Dimension(900,450));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		// 加监听
		// 将panel加载到对话框中
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue() throws Exception {
		String sql = "select a.vbillno,a.htcode,(select t.invname from bd_invbasdoc t where t.pk_invbasdoc = a.htname) htname,"
			      + " b.pduname,b.stylemodel,b.pdunum,b.pduamount,b.remark from dh_delivery a, dh_delivery_d b"
			      + " where a.pk_dhdelivery = b.pk_dhdelivery and nvl(a.dr, 0) = 0  and nvl(b.dr, 0) = 0 "
			      + " and a.pk_contract = '"+pkContract+"'";
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		List<Map<String,Object>> objlit = (List<Map<String,Object>>)iQ.executeQuery(sql, new MapListProcessor());
		
		BillModel bd = card.getBillModel();
		bd.clearBodyData();
		if(null != objlit && objlit.size() > 0){
			for(int i=0;i<objlit.size();i++){
				Map<String,Object> objmp = objlit.get(i);
				bd.addLine();
				bd.setValueAt(objmp.get("vbillno"), i, "vbillno");
				bd.setValueAt(objmp.get("htcode"), i, "htcode");
				bd.setValueAt(objmp.get("htname"), i, "htname");
				bd.setValueAt(objmp.get("pduname"), i, "pduname");
				bd.setValueAt(objmp.get("stylemodel"), i, "stylemodel");
				bd.setValueAt(objmp.get("pdunum"), i, "pdunum");
				bd.setValueAt(objmp.get("pduamount"), i, "pduamount");
				bd.setValueAt(objmp.get("remark"), i, "remark");				
			}
		}

	}
	
}
