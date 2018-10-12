package nc.ui.dahuan.xmrz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import sun.awt.AppContext;

import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.vo.fa.base.SwithCardSubVO;
import nc.vo.pub.lang.UFDate;

public class HtlogoaddDialog extends UIDialog {
	
	BillCardPanel card = null;	
	HtlogoDialog htdg;
	HtlogoaddDialog addg;
	int status;
	int editrow;
	IUAPQueryBS query; 
	
	public HtlogoaddDialog(Container parent){
		super(parent);
		htdg = (HtlogoDialog)parent;
		this.setModal(false);
	}
	
	// 增行
	public void showHtlogoDialog() {	
		initialize();
		initDialog();
		initValue();
		status = 0;
		addg = this;
		this.showModal();
	}
	
	// 修改
	public void showHtlogoDialog(int row,UFDate date,String cont,Object rzlx) {	
		initialize();
		initDialog();
		initValue(date,cont,rzlx);
		status = 1;
		editrow = row;
		addg = this;
		this.showModal();
		
	}
	
	// 查看
	public void showHtlogoDialog(UFDate date,String cont,Object rzlx) {	
		initialize();
		initDialog2();
		initValue(date,cont,rzlx);
		addg = this;
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
			this.card.loadTemplet("0001AA1000000000GQVA");//合同事项弹出框的主键
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("日志内容");
		// 设置最适合的大小
		this.setSize(new Dimension(600,300));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sure = new UIButton("确  定");

		UIButton nos = new UIButton("取  消");
		// 加监听
		sure.addMouseListener(new sureHtlogo());
		nos.addMouseListener(new nosHtlogo());
		UIPanel panel = new UIPanel();
		panel.add(sure);
		panel.add(nos);
		
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue(){
		card.setHeadItem("logo_date", ClientEnvironment.getInstance().getDate());
	}
	
	private void initValue(UFDate date,String cont,Object rzlx) {
		
		String rs=rzlx.toString();
		int i=0;
		if(rs.equals("设计")){
			i=0;
		}else if(rs.equals("交货")){
			i=1;
		}else if(rs.equals("土建")){
			i=2;
		}else if(rs.equals("安装")){
			i=3;
		}else if(rs.equals("调试")){
			i=4;
		}else if(rs.equals("完工")){
			i=5;
		}else if(rs.equals("采购")){
			i=6;
		}else if(rs.equals("其他")){
			i=7;
		}else if(rs.equals("维保")){
			i=8;
		}
		card.setHeadItem("logo_date",date );
		card.setHeadItem("logo_content", cont);
		card.setHeadItem("rzlx", i);
	}
	
	class sureHtlogo implements MouseListener{
		
		public void mouseClicked(MouseEvent e) {
			
			Object logodate = card.getHeadItem("logo_date").getValueObject();
			if(null == logodate || "".equals(logodate)){
				MessageDialog.showHintDlg(addg, "提示", "请维护日期");
				return;
			}
			Object content = card.getHeadItem("logo_content").getValueObject();
			if(null == content || "".equals(content)){
				MessageDialog.showHintDlg(addg, "提示", "请维护日志内容");
				return;
			}
			Object rzlx = card.getHeadItem("rzlx").getValueObject();
			if(null == rzlx || "".equals(rzlx)){
				MessageDialog.showHintDlg(addg, "提示", "请维护日志类型");
				return;
			}
			BillModel bd = htdg.card.getBillModel();
			if(0==status){				
				bd.addLine();
				int row = bd.getRowCount()-1;
				bd.setValueAt(logodate, row, "logo_date");
				bd.setValueAt(content, row, "content");
				bd.setValueAt(rzlx, row, "rzlx");
			}else{
				bd.setValueAt(logodate, editrow, "logo_date");
				bd.setValueAt(content, editrow, "content");
				bd.setValueAt(rzlx, editrow, "rzlx");
			}	
			addg.setVisible(false);
//			addg.dispose();
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}}
	
	
	
	class nosHtlogo implements MouseListener{	
		
		public void mouseClicked(MouseEvent e) {
			addg.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}}
	
	private void initDialog2() {
		// 设置对话框主题
		this.setTitle("日志内容");
		// 设置最适合的大小
		this.setSize(new Dimension(350,250));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
}
