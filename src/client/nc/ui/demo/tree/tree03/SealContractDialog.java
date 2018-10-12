package nc.ui.demo.tree.tree03;

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
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.lang.UFDate;

public class SealContractDialog extends UIDialog {
	
	BillCardPanel card = null;	
	SealContractDialog dg = null;
	DhContractVO dhvo = null;
	boolean sealflag = false;
	
	public SealContractDialog(Container parent){
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
		return sealflag;
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
			this.card.loadTemplet("0001AA1000000000MLIK");
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	/**
	 * 初始化界面UI
	 */
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("合同执行时间确认");
		// 设置最适合的大小
		this.setSize(new Dimension(300,150));
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
		
		// 开始时间
		UFDate stdate = dhvo.getDstartdate();
		card.setHeadItem("start_time", stdate);
		// 交货时间
		UFDate eddate = dhvo.getDjhdate();
		card.setHeadItem("end_time", eddate);
	}
	
	class ConfirmListener implements MouseListener{
		// 确定按钮点击事件
		public void mouseClicked(MouseEvent mouseevent) {
			// 更新主合同的项目经理和执行开始标识
			
			Object stdate = card.getHeadItem("start_time").getValueObject();
			Object eddate = card.getHeadItem("end_time").getValueObject();
			
			if((null == stdate || "".equals(stdate))||(null == eddate || "".equals(eddate))){
				MessageDialog.showHintDlg(dg, "提示", "请确认项目的【开始时间】【交货时间】");
				return;
			}				
			try{
				
				dhvo.setDstartdate(new UFDate(stdate.toString()));
				dhvo.setDjhdate(new UFDate(eddate.toString()));
				
				dhvo.setIs_conexe(1);
				
				HYPubBO_Client.update(dhvo);
				sealflag = true;
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
			sealflag= false;
			dg.setVisible(false);
		}

		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
}
