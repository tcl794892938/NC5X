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
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.card) {
			// 初始化卡片单据模板
			this.card = new BillCardPanel();
			// 根据id加载配置的单据模板,配置的单据模板id 在表pub_billtemplet的pk_billtemplet字段中获得
			this.card.loadTemplet("0001AA1000000000MZEM");//合同事项弹出框的主键
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("回款分配");
		// 设置最适合的大小
		this.setSize(new Dimension(400,200));
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
