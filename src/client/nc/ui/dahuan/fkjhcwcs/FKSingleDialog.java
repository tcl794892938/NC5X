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
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.card) {
			// 初始化卡片单据模板
			this.card = new BillCardPanel();
			// 根据id加载配置的单据模板,配置的单据模板id 在表pub_billtemplet的pk_billtemplet字段中获得
			this.card.loadTemplet("0001AA1000000000NY4V");//合同事项弹出框的主键
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("分单");
		// 设置最适合的大小
		this.setSize(new Dimension(400,400));
		// 设置对话框位置，正中央
		this.setLocationRelativeTo(getParent());
		// 设置对话框布局
		this.setLayout(new BorderLayout());
		// 设置按钮
		UIButton sureBtn = new UIButton("确  定");		
		UIButton notBtn = new UIButton("取  消");
		UIButton addBtn = new UIButton("增  行");		
		UIButton delBtn = new UIButton("删  行");
		
		// 加监听
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
		// 将panel加载到对话框中
		this.add(panel, BorderLayout.SOUTH);
		// 将单据面板放入对话框的中间
		this.add(this.card, BorderLayout.CENTER);
		// 设置关闭方式
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
				// 总额 
				UFDouble sumamount = new UFDouble(card.getHeadItem("sumamount").getValueObject()==null?"0.00"
						 							:card.getHeadItem("sumamount").getValueObject().toString());
				// 分配额 
				UFDouble fkamount = new UFDouble("0.00");
				BillModel bmodel = card.getBillModel();
				int rows = bmodel.getRowCount();
				for(int i=0;i<rows;i++){
					UFDouble rowamount = new UFDouble(bmodel.getValueAt(i, "fkamount")==null?"0.00"
														:bmodel.getValueAt(i, "fkamount").toString());
					fkamount = fkamount.add(rowamount);
				}
				// 余额
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
			// 总额 
			UFDouble sumamount = new UFDouble(card.getHeadItem("sumamount").getValueObject()==null?"0.00"
					 							:card.getHeadItem("sumamount").getValueObject().toString());
			// 分配额 
			UFDouble fkamount = new UFDouble("0.00");
			BillModel bmodel = card.getBillModel();
			int rows = bmodel.getRowCount();
			for(int i=0;i<rows;i++){
				UFDouble rowamount = new UFDouble(bmodel.getValueAt(i, "fkamount")==null?"0.00"
													:bmodel.getValueAt(i, "fkamount").toString());
				fkamount = fkamount.add(rowamount);
			}
			// 余额
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
				MessageDialog.showHintDlg(slgdialog, "提示", "请分配完");
				return;
			}else if(syamount.compareTo(new UFDouble("0.00"))<0){
				MessageDialog.showHintDlg(slgdialog, "提示", "分配金额已超");
				return;
			}else{
				BillModel bmodel = card.getBillModel();
				int rows = bmodel.getRowCount();
				String slgmegssage = "";
				
				DhFkjhSingleVO[] svos = new DhFkjhSingleVO[rows];
				for(int i=0;i<rows;i++){
					int hang = i+1;
					String sglamt = bmodel.getValueAt(i, "fkamount")==null?"0.00":bmodel.getValueAt(i, "fkamount").toString();
					slgmegssage += "第"+hang+"单金额："+sglamt+"； ";
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
