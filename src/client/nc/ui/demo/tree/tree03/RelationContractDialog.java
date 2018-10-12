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
	 * 初始化对话框面板中数据
	 */
	private void initialize() {
		// 初始化为空
		if (null == this.card) {
			// 初始化卡片单据模板
			this.card = new BillCardPanel();
			// 根据id加载配置的单据模板,配置的单据模板id 在表pub_billtemplet的pk_billtemplet字段中获得
			this.card.loadTemplet("0001AA1000000000TLTY");
					
			// 增行和删行按钮
			this.card.setBodyMenu(null);
		}
	}
	
	/**
	 * 初始化界面UI
	 */
	private void initDialog() {
		// 设置对话框主题
		this.setTitle("合同引入");
		// 设置最适合的大小
		this.setSize(new Dimension(300,250));
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
	
	class ConfirmListener implements MouseListener{
		// 确定按钮点击事件
		public void mouseClicked(MouseEvent mouseevent) {
			Object xmObj = card.getHeadItem("xmcode").getValueObject();
			if(null == xmObj || "".equals(xmObj)){
				MessageDialog.showHintDlg(card, "提示", "请维护项目编码");
				return;
			}
			Object ctObj = card.getHeadItem("ctcode").getValueObject();
			if(null == ctObj || "".equals(ctObj)){
				MessageDialog.showHintDlg(card, "提示", "请维护合同编码");
				return;
			}
			Object xmamt = card.getHeadItem("xm_amount").getValueObject();
			if(null == xmamt || "".equals(xmamt)){
				MessageDialog.showHintDlg(card, "提示", "请维护项目预算");
				return;
			}
			Object xmjlObj = card.getHeadItem("xmjl").getValueObject();
			if(null == xmjlObj || "".equals(xmjlObj)){
				MessageDialog.showHintDlg(card, "提示", "请维护合同签约人");
				return;
			}
			Object relationObj = card.getHeadItem("relationid").getValueObject();
			if(null == relationObj || "".equals(relationObj)){
				MessageDialog.showHintDlg(card, "提示", "请维护合同关联");
				return;
			}
			
			try {
				DhContractVO yvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, relationObj.toString());
				
				// 部门
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
					MessageDialog.showHintDlg(card, "提示", "项目编码已存在");
					return;
				}else if(count2 != 0 || count4 != 0){
					MessageDialog.showHintDlg(card, "提示", "合同编号已存在");
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
		// 取消按钮点击事件
		public void mouseClicked(MouseEvent mouseevent) {
			dg.setVisible(false);
		}

		public void mouseEntered(MouseEvent mouseevent) {}
		public void mouseExited(MouseEvent mouseevent) {}
		public void mousePressed(MouseEvent mouseevent) {}
		public void mouseReleased(MouseEvent mouseevent) {}
		
	}
	
}
