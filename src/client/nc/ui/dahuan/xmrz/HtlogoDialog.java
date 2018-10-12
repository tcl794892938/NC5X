package nc.ui.dahuan.xmrz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.xmrz.HtlogoBVO;
import nc.vo.dahuan.xmrz.HtlogoDetail;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class HtlogoDialog extends UIDialog {
	
	BillCardPanel card = null;	
	String title = "";
	int std;
	String pkContract;
	DefaultConstEnum userEnum;
	HtlogoDialog htdg;
	IUAPQueryBS query; 
	
	public HtlogoDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public void showHtlogoDialog(int status,String pkcon,String user) throws Exception{
		if(status == 0){
			title = "��Ŀ��־";
		}else if(status == 1){
			title = "������־";
		}else if(status == 2){
			title = "������־";
		}else if(status == 3){
			title = "��װ��־";
		}else if(status == 4){
			title = "������־";
		}else if(status == 5){
			title = "�깤��־";
		}else if(status == 6){
			title = "�ɹ���־";
		}else if(status == 7){
			title = "������־";
		}
		std = status;
		pkContract = pkcon;
		
		query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String sql = "select user_name from sm_user where cuserid = '"+user+"'";
		
		Object userObj = query.executeQuery(sql, new ColumnProcessor());
		String ussername = userObj==null?"":userObj.toString();
		
		userEnum = new DefaultConstEnum(user,ussername);
		
		initialize();
		initDialog();
		initValue();	
		htdg = this;
		
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
			this.card.loadTemplet("0001AA1000000000GLGY");//��ͬ������������
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	
	private void initDialog() {
		// ���öԻ�������
		this.setTitle(title);
		// �������ʺϵĴ�С
		this.setSize(new Dimension(900,650));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton addline = new UIButton("��  ��");
		
		UIButton delline = new UIButton("ɾ  ��");
		UIButton edit = new UIButton("��  ��");
		UIButton save = new UIButton("��  ��");
		UIButton look = new UIButton("��  ��");//by tcl
		// �Ӽ���
		addline.addMouseListener(new addHtlogoLine());
		delline.addMouseListener(new delHtlogoLine());
		edit.addMouseListener(new editHtlogoLine());
		save.addMouseListener(new saveHtlogoLine());
		look.addMouseListener(new lookHtlogoLine());
		UIPanel panel = new UIPanel();
		panel.add(save);
		panel.add(edit);
		panel.add(addline);
		panel.add(delline);
		panel.add(look);
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initValue() throws Exception {
		String sql = "select t.logo_date,t.rzlx,t.content ,s.user_name,t.logo_person from dh_htlogo_d t join sm_user  s on  t.logo_person = s.cuserid"
			+" where t.pk_contract = '"+pkContract+"' ";//and t.vfstatus = "+std+"
		
		List<Map<String,String>> maplist = (List<Map<String,String>>)query.executeQuery(sql, new MapListProcessor());
		if(null != maplist && maplist.size() > 0){
			BillModel bd = card.getBillModel();
			for(Map<String,String> mp : maplist){
				bd.addLine();
				int row = bd.getRowCount()-1;
				bd.setValueAt(mp.get("logo_date"), row, "logo_date");
				bd.setValueAt(mp.get("logo_person"), row, "logo_person");
				bd.setValueAt(mp.get("user_name"), row, "personname");
				bd.setValueAt(mp.get("content"), row, "content");
				bd.setValueAt(mp.get("rzlx"),row,"rzlx");
			}
		}
	}
	
	class addHtlogoLine implements MouseListener{
		//���������ݵ�ʱ�򣬵�������
		public void mouseClicked(MouseEvent e) {
			BillModel bd = card.getBillModel();
			int frw = bd.getRowCount()-1;//��ʼ����
			HtlogoaddDialog addg = new HtlogoaddDialog(htdg);
			addg.showHtlogoDialog();			
			int row = bd.getRowCount()-1;
			if(row>frw){
				bd.setValueAt(userEnum.getValue(), row, "logo_person");
				bd.setValueAt(userEnum.getName(), row, "personname");
			}
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
	class delHtlogoLine implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			UITable bt = card.getBillTable();

			if(bt.getSelectedRow() > -1){
				int row = bt.getSelectedRow();
				BillModel bd = card.getBillModel();
				String loggoper = bd.getValueAt(row, "logo_person")==null?"":bd.getValueAt(row, "logo_person").toString();
				if(userEnum.getValue().equals(loggoper)){
					bd.delLine(new int[]{ bt.getSelectedRow() });//ɾ����ǰ��
				}else {
					MessageDialog.showWarningDlg(bt, "��ʾ", "�����˺���д�˲�һ��,���ɸ��ĸ�����־");
				}
				
			}
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
	class editHtlogoLine implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			
			UITable bt = card.getBillTable();
			if(bt.getSelectedRow() > -1){
				int row = bt.getSelectedRow();
				BillModel bd = card.getBillModel();
				String loggoper = bd.getValueAt(row, "logo_person")==null?"":bd.getValueAt(row, "logo_person").toString();
				if(userEnum.getValue().equals(loggoper)){
					Object logo_date = bd.getValueAt(row, "logo_date");
					Object content = bd.getValueAt(row, "content");
					Object rzlx=bd.getValueAt(row, "rzlx");
					if(null != logo_date && null != content&&null!=rzlx){
						HtlogoaddDialog addg = new HtlogoaddDialog(htdg);
						addg.showHtlogoDialog(row,(UFDate)logo_date,content.toString(),rzlx);
					}else{
						MessageDialog.showWarningDlg(bt, "��ʾ", "����־��Ϣ����");
						return;
					}					
				}else {
					MessageDialog.showWarningDlg(bt, "��ʾ", "�����˺���д�˲�һ��,���ɸ��ĸ�����־");
					return;
				}
			}
			
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
	
	
	
	class saveHtlogoLine implements MouseListener{//����		
		
		public void mouseClicked(MouseEvent e) {
			
			BillModel bd = card.getBillModel();
			
			HtlogoBVO[] bvos = (HtlogoBVO[])bd.getBodyValueVOs(HtlogoBVO.class.getName());
			
			List<HtlogoBVO> nbvos = new ArrayList<HtlogoBVO>();

			// ����bvos��1�����е������޳���2��pk_contract��ֵ			
			for(int i=0;i<bvos.length;i++){
				HtlogoBVO bvo = bvos[i];
				if(null == bvo.getLogo_date() || "".equals(bvo.getLogo_date()) || 
						null == bvo.getContent() || "".equals(bvo.getContent())||
						null == bvo.getRzlx() || "".equals(bvo.getRzlx())){
				}else{
					bvo.setPk_contract(pkContract);
					bvo.setVfstatus(std);
					nbvos.add(bvo);
				}
			}
						
			try{
				HYPubBO_Client.deleteByWhereClause(HtlogoBVO.class, " pk_contract = '"+pkContract+"' ");
				HYPubBO_Client.insertAry(nbvos.toArray(new HtlogoBVO[]{}));
			}catch (BusinessException e1) {
				e1.printStackTrace();
			}finally{
				htdg.close();
			}
			
			//���Ƿ�鿴��,ƥ�䵱ǰ��¼�û� by tcl
			String pk_user=ClientEnvironment.getInstance().getUser().getPrimaryKey();
			boolean flag=false;
			for(int i=0;i<nbvos.size();i++){
				HtlogoBVO vo=nbvos.get(i);
				if(vo.getLogo_person().equals(pk_user));//�����־���µģ��鿴�������¼������״̬Ϊδ����
				flag=true;
				break;
			}
			if(flag){
				try {
					HYPubBO_Client.deleteByWhereClause(HtlogoDetail.class, " pk_contract = '"+pkContract+"' ");
				} catch (UifException e1) {
					e1.printStackTrace();
				}
			}
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
	
	class lookHtlogoLine implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			
			UITable bt = card.getBillTable();
			if(bt.getSelectedRow() > -1){
				int row = bt.getSelectedRow();
				BillModel bd = card.getBillModel();
				Object logo_date = bd.getValueAt(row, "logo_date");
				Object content = bd.getValueAt(row, "content");
				//Object rzlx=bd.getValueAt(row, "rzlx");
				Object rzlx=bd.getValueAt(row, "rzlx");
				if(null != logo_date && null != content&&null!=rzlx){
					HtlogoaddDialog addg = new HtlogoaddDialog(htdg);
					addg.showHtlogoDialog((UFDate)logo_date,content.toString(),rzlx);
				}else{
					MessageDialog.showWarningDlg(bt, "��ʾ", "����־��Ϣ����");
					return;
				}					
			}
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
	
}