package nc.ui.demo.tree.tree03;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.demo.contract.MultiBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class RelationContractDialog extends UIDialog {
	
	BillCardPanel card = null;	
	RelationContractDialog dg = null;
	AggregatedValueObject billvo = null;
	
	public RelationContractDialog(Container parent){
		super(parent);
		this.setModal(false);
	}
	
	public AggregatedValueObject showConExecuteModel() throws Exception{
		initialize();
		initDialog();
		dg = this;
		this.showModal();
		return billvo;
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
			this.card.loadTemplet("0001AA1000000000TLTY");
					
			// ���к�ɾ�а�ť
			this.card.setBodyMenu(null);
		}
	}
	
	/**
	 * ��ʼ������UI
	 */
	private void initDialog() {
		// ���öԻ�������
		this.setTitle("��ͬ����");
		// �������ʺϵĴ�С
		this.setSize(new Dimension(300,250));
		// ���öԻ���λ�ã�������
		this.setLocationRelativeTo(getParent());
		// ���öԻ��򲼾�
		this.setLayout(new BorderLayout());
		// ���ð�ť
		UIButton confirm = new UIButton("ȷ  ��");
		UIButton negate = new UIButton("ȡ  ��");
		// �Ӽ���
		confirm.addMouseListener(new ConfirmListener());
		negate.addMouseListener(new NegateListener());
		UIPanel panel = new UIPanel();
		panel.add(confirm);
		panel.add(negate);
		// ��panel���ص��Ի�����
		this.add(panel, BorderLayout.SOUTH);
		// ������������Ի�����м�
		this.add(this.card, BorderLayout.CENTER);
		// ���ùرշ�ʽ
		this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
		
	}
	
	class ConfirmListener implements MouseListener{
		// ȷ����ť����¼�
		public void mouseClicked(MouseEvent mouseevent) {
			Object xmObj = card.getHeadItem("xmcode").getValueObject();
			if(null == xmObj || "".equals(xmObj)){
				MessageDialog.showHintDlg(card, "��ʾ", "��ά����Ŀ����");
				return;
			}
			Object ctObj = card.getHeadItem("ctcode").getValueObject();
			if(null == ctObj || "".equals(ctObj)){
				MessageDialog.showHintDlg(card, "��ʾ", "��ά����ͬ����");
				return;
			}
			Object xmamt = card.getHeadItem("xm_amount").getValueObject();
			if(null == xmamt || "".equals(xmamt)){
				MessageDialog.showHintDlg(card, "��ʾ", "��ά����ĿԤ��");
				return;
			}
			Object xmjlObj = card.getHeadItem("xmjl").getValueObject();
			if(null == xmjlObj || "".equals(xmjlObj)){
				MessageDialog.showHintDlg(card, "��ʾ", "��ά����ͬǩԼ��");
				return;
			}
			Object relationObj = card.getHeadItem("relationid").getValueObject();
			if(null == relationObj || "".equals(relationObj)){
				MessageDialog.showHintDlg(card, "��ʾ", "��ά����ͬ����");
				return;
			}
			
			try {
				DhContractVO yvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, relationObj.toString());
				
				// ����
				String pkCorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
				String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
				
				String	sql1 = "select count(1) from bd_jobbasfil where jobcode='"+xmObj.toString()+"' and nvl(dr,0)=0 and pk_corp = '"+pkCorp+"' and pk_jobtype = '0001AA100000000013ZG'";
				String	sql2 = "select count(1) from bd_jobbasfil where jobname='"+ctObj.toString()+"' and nvl(dr,0)=0 and pk_corp = '"+pkCorp+"' and pk_jobtype = '0001AA100000000013ZG'";	
				String	sql3 = "select count(1) from dh_contract where jobcode='"+xmObj.toString()+"' and nvl(dr,0)=0 and pk_corp = '"+pkCorp+"' ";
				String	sql4 = "select count(1) from dh_contract where ctcode='"+ctObj.toString()+"' and nvl(dr,0)=0 and pk_corp = '"+pkCorp+"' ";	
				IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				int count1 = (Integer)iQ.executeQuery(sql1, new ColumnProcessor());
				int count2 = (Integer)iQ.executeQuery(sql2, new ColumnProcessor());
				int count3 = (Integer)iQ.executeQuery(sql3, new ColumnProcessor());
				int count4 = (Integer)iQ.executeQuery(sql4, new ColumnProcessor());
				
				if(count1 != 0 || count3 != 0){
					MessageDialog.showHintDlg(card, "��ʾ", "��Ŀ�����Ѵ���");
					return;
				}else if(count2 != 0 || count4 != 0){
					MessageDialog.showHintDlg(card, "��ʾ", "��ͬ����Ѵ���");
					return;
				}
				
				String sql11 = "select v.pk_deptdoc from v_deptperonal v where v.pk_corp='"+pkCorp+"' and v.pk_user = '"+pkUser+"'";
				String deptid = (String)iQ.executeQuery(sql11, new ColumnProcessor());
				
				String sql12 = "select v.pk_user from v_deptperonal v where v.pk_corp='"+pkCorp+"' and v.pk_deptdoc='"+deptid+"' and v.pdsn_level=0";
				String userid = (String)iQ.executeQuery(sql12, new ColumnProcessor());
				
				DhContractVO dvo = (DhContractVO)yvo.clone();
				dvo.setXm_amount(new UFDouble(xmamt.toString()));//by tcl
				dvo.setCtcode(ctObj.toString());
				dvo.setJobcode(xmObj.toString());
				dvo.setPk_corp(pkCorp);
				dvo.setDcaigtotal(null);
				dvo.setDsaletotal(dvo.getDctjetotal());
				dvo.setHttype(0);
				dvo.setVoperatorid(pkUser);
				dvo.setVapproveid(userid);
				dvo.setPk_contract(null);
				dvo.setPk_jobmandoc(null);
				if("1002".equals(pkCorp)){
					dvo.setPk_cust1("1002A1100000000082EK");
				}else if("1010".equals(pkCorp)){
					dvo.setPk_cust1("1010A11000000000010O");
				}
				dvo.setPk_cust2(null);
				dvo.setPk_deptdoc(deptid);
				dvo.setHt_dept(deptid);
				dvo.setPk_fzr(xmjlObj.toString());
				dvo.setPk_xmjl(xmjlObj.toString());
				dvo.setIs_seal(0);
				dvo.setIs_relation(1);
				dvo.setRelationid(yvo.getPk_contract());
				String dvopk = HYPubBO_Client.insert(dvo);
				dvo.setPk_contract(dvopk);
				
				yvo.setIs_relation(1);
				yvo.setRelationid(dvopk);
				HYPubBO_Client.update(yvo);
				
				DhContractBVO[] ybvos = (DhContractBVO[])HYPubBO_Client.queryByCondition(DhContractBVO.class, " pk_contract = '"+yvo.getPk_contract()+"' ");
				List<DhContractBVO> bvolist = new ArrayList<DhContractBVO>();
				for(DhContractBVO ybvo : ybvos){
					DhContractBVO bvo = (DhContractBVO)ybvo.clone();
					bvo.setPk_contract_b(null);
					bvo.setPk_contract(dvopk);
					String bvopk = HYPubBO_Client.insert(bvo);
					bvo.setPk_contract_b(bvopk);
					bvolist.add(bvo);
				}
				billvo = (AggregatedValueObject) Class.forName(MultiBillVO.class.getName()).newInstance();
				billvo.setParentVO(dvo);
				billvo.setChildrenVO(bvolist.toArray(new DhContractBVO[0]));
				
				dg.setVisible(false);
			} catch (UifException e1) {
				e1.printStackTrace();
			} catch (BusinessException e2) {
				e2.printStackTrace();
			} catch (InstantiationException e3) {
				e3.printStackTrace();
			} catch (IllegalAccessException e4) {
				e4.printStackTrace();
			} catch (ClassNotFoundException e5) {
				e5.printStackTrace();
			}
			
		}
		
		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
	class NegateListener implements MouseListener{
		// ȡ����ť����¼�
		public void mouseClicked(MouseEvent mouseevent) {
			dg.setVisible(false);
		}

		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
}
