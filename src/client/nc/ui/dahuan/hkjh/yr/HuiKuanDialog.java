package nc.ui.dahuan.hkjh.yr;

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

public class HuiKuanDialog extends UIDialog {

	BillCardPanel card;
	HuiKuanDialog hkdlg;
	String vbillcode;
	String balaname;
	boolean flag = false;
	
	public HuiKuanDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public boolean showHuiKuanDialog(String balabname) throws Exception{
		initialize();
		initDialog();
		initValue(balabname);
		hkdlg = this;
		this.showModal();
		return flag;
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
			this.card.loadTemplet("0001AA1000000000SEMC");//合同事项弹出框的主键
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("回款单号");
		// 设置最适合的大小
		this.setSize(new Dimension(400,200));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		
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
	
	private void initValue(String balabname){
		card.setHeadItem("balan", balabname);
	}
	
	class SureMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			Object hkno = card.getHeadItem("hkno").getValueObject();
			if(null == hkno || "".equals(hkno)){
				MessageDialog.showHintDlg(card, "提示", "请填写回款单号");
				return;
			}else{
				vbillcode = hkno.toString();
			}
			
			Object balan = card.getHeadItem("balan").getValueObject();
			if(null == balan || "".equals(balan)){
				MessageDialog.showHintDlg(card, "提示", "请填写回款类型");
				return;
			}else{
				balaname = balan.toString();
			}
			
			flag = true;
			hkdlg.setVisible(false);
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class NotMouseLister implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			flag = false;
			hkdlg.setVisible(false);	
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	public String getBalaname() {
		return balaname;
	}

	public void setBalaname(String balaname) {
		this.balaname = balaname;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	
	
}
