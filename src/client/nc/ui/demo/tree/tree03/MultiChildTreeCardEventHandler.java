/*
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.demo.tree.tree03;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class MultiChildTreeCardEventHandler extends TreeManageEventHandler {

	IdhServer pfserver = (IdhServer)NCLocator.getInstance().lookup(IdhServer.class);
	IUAPQueryBS iQuery = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	public MultiChildTreeCardEventHandler(BillManageUI billUI,
			ICardController control) {
		super(billUI, control);

	}

	public boolean isAllowAddNode(TableTreeNode node) {

		return super.isAllowAddNode(node);
	}

	public boolean isAllowDelNode(TableTreeNode node) {

		return super.isAllowDelNode(node);
	}

	
	
	

	protected void onBoDelete() throws Exception {
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);		
		// �жϺ�ͬ����
		DhContractVO vo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
		if(2 == vo.getHttype()){
			// ���ͬ�ж������Ӻ�ͬ
			String jobcode = vo.getJobcode();
			String pkcorp = vo.getPk_corp();
			String sql = "select count(1) from dh_contract where jobcode like '"+jobcode+"%' and jobcode <> '"+jobcode+"' and pk_corp = '"+pkcorp+"'";
			Integer count = (Integer)query.executeQuery(sql, new ColumnProcessor());
			if(count > 1){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","�����ͬ�����Ӻ�ͬ������ɾ��");
				return;
			}
			// ���µĺ�ͬ����ɾ��
			DhContractVO dhcvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class,vo.getPk_contract());
			int isseal = dhcvo.getIs_seal()==null ? 0 : dhcvo.getIs_seal().intValue();
			if(1==isseal){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","�����ͬ�Ѹ��£�����ɾ��");
				return;
			}
		}else{
			int vbillstatus = vo.getVbillstatus();
			if(8 != vbillstatus){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","�ú�ͬ�������У�����ɾ��");
				return;
			}
		}
		if(MessageDialog.ID_OK == MessageDialog.showOkCancelDlg(this.getBillUI(), "��ʾ","�Ƿ�ȷ��ɾ��")){
			HYPubBO_Client.deleteByWhereClause(DhContractBVO.class, "  pk_contract = '"+vo.getPk_contract()+"'");
			HYPubBO_Client.deleteByWhereClause(DhContractVO.class, "  pk_contract = '"+vo.getPk_contract()+"'");
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","ɾ���������");
			MultiChildTreeCardUI ui = (MultiChildTreeCardUI)this.getBillUI();
			ui.updateTreeData();
		}
	}
	



	@Override
	public void onButton(ButtonObject bo) {
		// ��������ťУ��
		String code = bo.getCode().trim();
		boolean flag = false;
		int ttc = 0;
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String user = this._getOperator();
		String corp = this._getCorp().getPrimaryKey();
		if("���Ƶ���".equals(code)){			
			String sql = "select count(1) from dh_fkgx a,dh_fkgx_d b where a.pk_fkgx=b.pk_fkgx and nvl(a.dr,0)=0"
				 		+ " and nvl(b.dr,0)=0 and b.pk_dept_user='"+user+"' and a.pk_corp='"+corp+"'";
			
			String tcsql = " select count(1) from sm_user_role f where f.cuserid='"+user+"' and f.pk_corp='"+corp+"' " +
					" and f.pk_role=(select r.pk_role from sm_role r where r.role_code='DHCBFY') ";
			
			
			try {
				ttc = (Integer)query.executeQuery(tcsql, new ColumnProcessor());
				
				if(ttc==0){
					int count = (Integer)query.executeQuery(sql, new ColumnProcessor());
					if(count < 1){
						MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","��ֻͬ��ҵ����Ա����");
						return;
					}else{
						flag = true;	
					}
				}else{
					flag = true;	
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
						
		}
		
		super.onButton(bo);
		
		if(flag){
			BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
			BillCardLayout layout = (BillCardLayout)card.getLayout();
			layout.setHeadScale(60);
			layout.layoutContainer(card);
			// Ĭ�ϴ�������
			String sql2 = "select a.pk_user1 from dh_fkgx a,dh_fkgx_d b where a.pk_fkgx=b.pk_fkgx and nvl(a.dr,0)=0"
		 		+ " and nvl(b.dr,0)=0 and b.pk_dept_user='"+user+"' and a.pk_corp='"+corp+"'";
			try {
				if(ttc!=0){
					String mauser = (String)query.executeQuery(sql2, new ColumnProcessor());
					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(mauser);
					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("htaddress").setValue("�Ͼ�");
				}
				// ������˵�10����
				UIRefPane pduUif = (UIRefPane)card.getHeadItem("ctname").getComponent();
				pduUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
				
				UIRefPane invUif = (UIRefPane)card.getBodyItem("invcode").getComponent();
				invUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
				
			} catch (BusinessException e) {
				e.printStackTrace();
			}
						
		}
		
	}
	
	

	@Override
	protected void onBoCard() throws Exception {
		((BillManageUI)this.getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}
	
	/**
	 * �����ֶ�У��
	 * @param colvalue : �ֶ�ֵ
	 * @param colname �� �ֶ�����
	 * */
	private String checkCertianColumn(String[] colvalue,String[] colname){
		String message = "";
		for(int i=0 ; i<colvalue.length ; i++){
			if(null == colvalue[i] || "".equals(colvalue[i])){
				message += colname[i] + ",";
			}
		}
		return message;
	}
	
	/**
	 * У����Ŀ����ͺ�ͬ����Ƿ��ظ�
	 * @param pkCont ��ͬ����
	 * @param xmcode ��Ŀ����
	 * @param htcode ��ͬ���
	 * @return У����
	 * */
	private String checkXmCodeOnly(String pkCont,String xmcode,String htcode,String pkcorp) throws Exception{
		String sql1 ="";
		String sql2 ="";
		String sql3 ="";
		String sql4 ="";
		
		if("".equals(pkCont)){
			sql1 = "select count(1) from bd_jobbasfil where jobcode='"+xmcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' and pk_jobtype = '0001AA100000000013ZG'";
			sql2 = "select count(1) from bd_jobbasfil where jobname='"+htcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' and pk_jobtype = '0001AA100000000013ZG'";	
			sql3 = "select count(1) from dh_contract where jobcode='"+xmcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' ";
			sql4 = "select count(1) from dh_contract where ctcode='"+htcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' ";	
		}else{
			sql1 = "select count(1) from bd_jobbasfil where jobcode='"+xmcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' and pk_jobtype = '0001AA100000000013ZG'";
			sql2 = "select count(1) from bd_jobbasfil where jobname='"+htcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' and pk_jobtype = '0001AA100000000013ZG'";	
			sql3 = "select count(1) from dh_contract where jobcode='"+xmcode+"' and nvl(dr,0)=0 and pk_contract <> '"+pkCont+"' and pk_corp = '"+pkcorp+"'";
			sql4 = "select count(1) from dh_contract where ctcode='"+htcode+"' and nvl(dr,0)=0 and pk_contract <> '"+pkCont+"' and pk_corp = '"+pkcorp+"'";
		}
		int count1 = (Integer)iQuery.executeQuery(sql1, new ColumnProcessor());
		int count2 = (Integer)iQuery.executeQuery(sql2, new ColumnProcessor());
		int count3 = (Integer)iQuery.executeQuery(sql3, new ColumnProcessor());
		int count4 = (Integer)iQuery.executeQuery(sql4, new ColumnProcessor());
		
		if(count1 != 0 || count3 != 0){
			return "��Ŀ�����Ѵ���";
		}else if(count2 != 0 || count4 != 0){
			return "��ͬ����Ѵ���";
		}else{
			return "";
		}
		
	}
	
	/**
	 * ��ͬ���벿�Ź�����У��
	 * @param htcode : ��ͬ��
	 * @param dpbelongname : ��ͬ��������
	 * @param dpdrawname : ��ͬ�Ƶ�����
	 * */
	private String checkCodeDeptRelation(String htcode,String dpbelongname,String dpdrawname) throws Exception{
		// ��ͬ�ĵ�һ����
		String htcodeFrt = htcode.substring(0, 1);
		// ��ͬ�������ŵĵ�һ����
		String dpbelongFrt = dpbelongname.substring(0, 1);
		// ��ͬ�Ƶ����ŵĵ�һ����
		String dpdrawFrt = dpdrawname.substring(0, 1);
		
		if("�������̲�".equals(dpdrawname)){
			// ����У��			
		}else if(dpdrawname.contains("�Զ���")){
			// �����Զ����Ĳ��ţ���ͬ����������,�Һ�ͬ�������ŵĵ�һ���ֺͺ�ͬ�ŵĵ�һ����Ҫ��ͬ
			if(!htcode.contains("����")){
				return "�Զ����ĺ�ͬ����Ҫ���е���";
			}
			if(!htcodeFrt.equals(dpbelongFrt)){
				return "��ͬ�������ŵĵ�һ���ֺͺ�ͬ�ŵĵ�һ����Ҫ��ͬ";
			}
		}else{
			if(!htcodeFrt.equals(dpdrawFrt)){
				return "��ͬ�Ƶ����ŵĵ�һ���ֺͺ�ͬ�ŵĵ�һ����Ҫ��ͬ";
			}
			if(!htcodeFrt.equals(dpbelongFrt)){
				return "��ͬ�������ŵĵ�һ���ֺͺ�ͬ�ŵĵ�һ����Ҫ��ͬ";
			}
		}
		return "";
	}
	
	/**
	 * ��ͬ����У��
	 * */
	private String checkDataValue() throws Exception{
		
		// �ж����������޸�(����pk_contract�Ƿ���ֵ)
		BillCardPanel htcard = this.getBillCardPanelWrapper().getBillCardPanel();
		// ��ͬ����
		String pkCont = dealValueOfStr(htcard, "pk_contract");
		// ��Ŀ����
		String xmcode = dealValueOfStr(htcard, "jobcode");
		// ��ͬ���
		String htcode = dealValueOfStr(htcard, "ctcode");
		// ��ͬ���
		String pkcorp = dealValueOfStr(htcard, "pk_corp");
		
		String retmessage = checkXmCodeOnly(pkCont,xmcode, htcode,pkcorp);
		if(!"".equals(retmessage)){
			return retmessage;
		}
		
		// ��ͬ����
		int httype = dealValueOfInt(htcard, "httype");
		if(httype != 0 && httype != 1){
			return "";
		}else{
			//  �����˺�
			String htbankno = dealValueOfStr(htcard,"sax_no");
//			if(htbankno.length() < 12){
//				return "�����˺Ų���С��12λ";
//			}	
			
			// ��ͬ��������
			UIRefPane deptbelongRefp = (UIRefPane)htcard.getHeadItem("ht_dept").getComponent();
			String dpbelongname = deptbelongRefp.getRefName();
			// ��ͬ�Ƶ�����
			UIRefPane deptdrawRefp = (UIRefPane)htcard.getHeadItem("pk_deptdoc").getComponent();
			String dpdrawname = deptdrawRefp.getRefName();
			// ��ͬ����
			String htname = dealValueOfRef(htcard, "ctname");
			// ��ͬ����
			String htlx = dealValueOfRef(htcard, "pk_cttype");
			// ���ۿͻ�
			String htkh = dealValueOfRef(htcard, "pk_cust1");
			// ��Ӧ��
			String htgys = dealValueOfRef(htcard, "pk_cust2");
			// ��������
			String htbank = dealValueOfStr(htcard,"pk_bank");			
			// ǩԼ�ص�
			String htaddress = dealValueOfStr(htcard,"htaddress");
			// ǩԼʱ��
			String httime = dealValueOfStr(htcard,"htrq");
			// ���㷽ʽ
			String jsfs = dealValueOfRef(htcard,"pk_skfs");
			// ǩԼ��
			String htqdr = dealValueOfRef(htcard,"pk_fzr");
			// ��Ŀ����
			String xmjl = dealValueOfRef(htcard,"pk_xmjl");
			// Ԥ����
			String ysr = dealValueOfRef(htcard,"pk_ysid");
			// ����
			String pkfuzong = dealValueOfRef(htcard,"pk_fuzong");
			// ��ʼʱ��
			String dstartdate = dealValueOfStr(htcard,"dstartdate");
			// �����ص�
			String vjhaddress = dealValueOfStr(htcard,"vjhaddress");
			
			// ����
			String pkCurrty = dealValueOfStr(htcard,"pk_currenty");
			
			// ����
			String currtyRate = dealValueOfStr(htcard,"currenty_rate");
			
			// ��Ŀ����
			String xmname = dealValueOfStr(htcard,"vdef6");
			
			if(0==httype){
				String[] clovalue = new String[]{
						dpbelongname,dpdrawname,htname,htlx,htkh,htbank,htbankno,
						htaddress,httime,jsfs,htqdr,ysr,pkfuzong,xmjl,xmname,pkCurrty,currtyRate
				}; 
				String[] cloname = new String[]{
					"[��ͬ��������]","[��ͬ�Ƶ�����]","[��ͬ����]","[��ͬ����]","[���ۿͻ�]","[��������]","[�����˺�]",
					"[ǩ���ص�]","[ǩ��ʱ��]","[���㷽ʽ]","[ǩ����]","[Ԥ����]","[����]","[��Ŀ����]","[��Ŀ����]","[����]","[����]"
				};
				String message = checkCertianColumn(clovalue,cloname);
				if(!"".equals(message)){
					return "��ά��������Ϣ��\n"+message.substring(0, message.length()-1);
				}
			}else{
				String[] clovalue = new String[]{
						dpbelongname,dpdrawname,htname,htlx,htgys,htbank,htbankno,
						htaddress,httime,jsfs,htqdr,ysr,pkfuzong,vjhaddress,xmjl,xmname,pkCurrty,currtyRate
				}; 
				String[] cloname = new String[]{
					"[��ͬ��������]","[��ͬ�Ƶ�����]","[��ͬ����]","[��ͬ����]","[��Ӧ��]","[��������]","[�����˺�]",
					"[ǩ���ص�]","[ǩ��ʱ��]","[���㷽ʽ]","[ǩ����]","[Ԥ����]","[����]","[��/�����ص�]","[��Ŀ����]","[��Ŀ����]","[����]","[����]"
				};
				String message = checkCertianColumn(clovalue,cloname);
				if(!"".equals(message)){
					return "��ά��������Ϣ��\n"+message.substring(0, message.length()-1);
				}
			}
			
//			String cdrMsg = checkCodeDeptRelation(htcode, dpbelongname, dpdrawname);
//			if(!"".equals(cdrMsg)){
//				return cdrMsg;
//			}
			
			// У��ǩԼ�˺�Ԥ���˲���һ��
			UIRefPane fzrUif = (UIRefPane)htcard.getHeadItem("pk_fzr").getComponent();
			String fzrName = fzrUif.getRefName();
			UIRefPane ysrUif = (UIRefPane)htcard.getHeadItem("pk_ysid").getComponent();
			String ysrName = ysrUif.getRefName();
			
			UIRefPane cttype = (UIRefPane)htcard.getHeadItem("pk_cttype").getComponent();
			String cttype2 = cttype.getRefCode();
			
			if(fzrName.equals(ysrName)&&!cttype2.equals("002")){
				return "Ԥ���˺�ǩԼ�˲�����ͬһ��";
			}
			
		}	
		
		return "";
	}


	protected void onBoSave() throws Exception {
		
		// �ɹ���ͬ�ŵ��Զ�����
		BillCardPanel htcard = this.getBillCardPanelWrapper().getBillCardPanel();
		int httype = dealValueOfInt(htcard, "httype");
		
		Object obj1=htcard.getHeadItem("ctcode").getValueObject();
		Object obj3=htcard.getHeadItem("ctname").getValueObject();
		Object obj4=htcard.getHeadItem("pk_cttype").getValueObject();
		Object obj5=htcard.getHeadItem("xm_amount").getValueObject();
		String htbh=obj1==null?"":obj1.toString();
		String htmc=obj3==null?"":obj3.toString();
		String htlx=obj4==null?"":obj4.toString();
		String yscb=obj5==null?"":obj5.toString();
		String temp=htbh.substring(htbh.length()-2, htbh.length());
		
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//У��ɹ���ֻͬ���ɹ��̲��Ƶ�
		if(httype==1&&!_getCorp().getPk_corp().equals("1002")){//��������
			Object objdept=htcard.getHeadItem("pk_deptdoc").getValueObject();
			String sql="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
					"and pk_corp='"+_getCorp().getPk_corp()+"' and deptname='���̹���' and canceled='N'";
			Object gc=iQ.executeQuery(sql, new ColumnProcessor());
			if(gc==null){
				MessageDialog.showHintDlg(htcard, "��ʾ", "�ù�˾δ���ù��̹������������ɹ���ͬ!");
				return ;
			}
			if(objdept==null||!objdept.equals(gc)){
				MessageDialog.showHintDlg(htcard, "��ʾ", "�ɹ���ͬ�Ƶ�������Ϊ���̹���!");
				return ;
			}
		}
		
		if("����".equals(temp) || "�ɱ�".equals(temp)){
			if("".equals(htmc) || "".equals(htlx) || "".equals(yscb)){
				MessageDialog.showHintDlg(htcard, "", "����,�ɱ���ͬ�ĺ�ͬ���ơ���ͬ���͡�Ԥ��ɱ�����Ϊ��!");
				return ;
			}
			String temp2=htbh.replace(temp, "00");
			String temsql="select vdef6 from dh_contract where nvl(dr,0)=0 and ctcode='"+temp2+"'";
			Object oobj=iQ.executeQuery(temsql, new ColumnProcessor());
			String str1=oobj==null?"":oobj.toString();
			htcard.setHeadItem("vdef6", str1);
		}
		
		Object obja=htcard.getHeadItem("dctjetotal").getValueObject();
		UFDouble htje=obja==null?new UFDouble(0):new UFDouble(obja.toString());
		
		if(httype==2 && htje.compareTo(new UFDouble(0))!=0){
			MessageDialog.showHintDlg(htcard, "��ʾ", "���ͬ������д��ͬ���");
			return;
		}
		
		// �鿴�����ֵ
		String jmjobcode = dealValueOfStr(htcard, "jobcode");
		String jmctcode = dealValueOfStr(htcard, "ctcode");
		
		if(1==httype){
			VOTreeNode nodeT = this.getBillTreeManageUI().getBillTreeSelectNode();
			if(null != nodeT){
				String jobcode = nodeT.getCode();
				String pkcorp = this._getCorp().getPrimaryKey();
				
				DhContractVO[] tvos = (DhContractVO[])HYPubBO_Client.queryByCondition(DhContractVO.class, 
												" jobcode = '"+jobcode+"' and pk_corp = '"+pkcorp+"' and nvl(dr,0)=0 ");
				
				if(null != tvos && tvos.length == 1){
					DhContractVO  tvo = tvos[0];
					int tType = tvo.getHttype();
					//IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					if(tType != 1 && tType != 0){
						// ���ͬ
						String xsql = "select t.jobcode,t.ctcode from dh_contract t where t.jobcode like '"+jobcode+"%' "+
						   		" and t.pk_corp = '"+pkcorp+"' and nvl(t.dr, 0) = 0 and t.httype = 1 order by t.jobcode,t.ctcode ";
						List<Map<String,String>> mapQ = (List<Map<String,String>>)iQ.executeQuery(xsql, new MapListProcessor());
						if(null != mapQ && mapQ.size() > 0){
							boolean tflag = true;
							for(int i=0;i<mapQ.size();i++){
								Map<String,String> mapS = mapQ.get(i);
								String jobcodeS = mapS.get("jobcode");
								int jxwz = jobcodeS.length()-3;
								int gyh = Integer.parseInt(jobcodeS.substring(jxwz));
								int j = i+1;
								if(j != gyh){
									String xj = String.valueOf(j);
									//String[] ss = new String[]{"00","0",""};
									String[] ss = new String[]{"00","0",""};
									String[] ss1 = new String[]{"0","",""};//by tcl ���ĺ�ͬ�������2017-02-27
									
									int xs = xj.length()-1;
									String xj1 = ss[xs]+xj;
									String xj2 = ss1[xs]+xj;
									String jobcodeM = tvo.getJobcode()+xj1;
									String ctcodeM = tvo.getCtcode()+"-"+xj2;
									
									if("".equals(jmjobcode) || "".equals(jmctcode) || 
											!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
										int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
														"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
										if(MessageDialog.ID_OK == retBtn){
											htcard.setHeadItem("jobcode", jobcodeM);
											htcard.setHeadItem("ctcode", ctcodeM);
										}
									}
									tflag = false;
									break;
								}
							}
							if(tflag){
								int j = mapQ.size()+1;
								String xj = String.valueOf(j);
								//String[] ss = new String[]{"00","0",""};
								String[] ss = new String[]{"00","0",""};
								String[] ss1 = new String[]{"0","",""};//by tcl ���ĺ�ͬ�������2017-02-27
								
								int xs = xj.length()-1;
								String xj1 = ss[xs]+xj;
								String xj2 = ss1[xs]+xj;
								String jobcodeM = tvo.getJobcode()+xj1;
								String ctcodeM = tvo.getCtcode()+"-"+xj2;
								if("".equals(jmjobcode) || "".equals(jmctcode) || 
										!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
									int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
													"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
									if(MessageDialog.ID_OK == retBtn){
										htcard.setHeadItem("jobcode", jobcodeM);
										htcard.setHeadItem("ctcode", ctcodeM);
									}
								}
								
							}
						}else{
							String jobcodeM = tvo.getJobcode()+"001";
							String ctcodeM = tvo.getCtcode()+"-01";
							
							if("".equals(jmjobcode) || "".equals(jmctcode) || 
									!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
								int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
												"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
								if(MessageDialog.ID_OK == retBtn){
									htcard.setHeadItem("jobcode", jobcodeM);
									htcard.setHeadItem("ctcode", ctcodeM);
								}
							}
						}
					}else{
						// �����ͬ,�ҵ����ͬ
						String njobcode = tvo.getJobcode().substring(0, tvo.getJobcode().length()-3);
						
						DhContractVO[] ntvos = (DhContractVO[])HYPubBO_Client.queryByCondition(DhContractVO.class, 
													" jobcode = '"+njobcode+"' and pk_corp = '"+pkcorp+"' and nvl(dr,0)=0 ");
						if(null == ntvos || ntvos.length != 1){
							MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��������");
							return;
						}
						DhContractVO ntvo = ntvos[0];
						
						String xsql = "select t.jobcode,t.ctcode from dh_contract t where t.jobcode like '"+njobcode+"%' "+
				   							" and t.pk_corp = '"+pkcorp+"' and nvl(t.dr, 0) = 0 and t.httype = 1 order by t.jobcode,t.ctcode ";
						List<Map<String,String>> mapQ = (List<Map<String,String>>)iQ.executeQuery(xsql, new MapListProcessor());
						if(null != mapQ && mapQ.size() > 0){
							boolean tflag = true;
							for(int i=0;i<mapQ.size();i++){
								Map<String,String> mapS = mapQ.get(i);
								String jobcodeS = mapS.get("jobcode");
								int jxwz = jobcodeS.length()-3;
								int gyh = Integer.parseInt(jobcodeS.substring(jxwz));
								int j = i+1;
								if(j != gyh){
									String xj = String.valueOf(j);
									//String[] ss = new String[]{"00","0",""};
									String[] ss = new String[]{"00","0",""};
									String[] ss1 = new String[]{"0","",""};//by tcl ���ĺ�ͬ�������2017-02-27
									
									int xs = xj.length()-1;
									String xj1 = ss[xs]+xj;
									String xj2 = ss1[xs]+xj;
									String jobcodeM = tvo.getJobcode()+xj1;
									String ctcodeM = tvo.getCtcode()+"-"+xj2;
							
									if("".equals(jmjobcode) || "".equals(jmctcode) || 
											!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
										int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
													"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
										if(MessageDialog.ID_OK == retBtn){
											htcard.setHeadItem("jobcode", jobcodeM);
											htcard.setHeadItem("ctcode", ctcodeM);
										}
									}
									tflag = false;
									break;
								}
							}
							if(tflag){
								int j = mapQ.size()+1;
								String xj = String.valueOf(j);
								//String[] ss = new String[]{"00","0",""};
								String[] ss = new String[]{"00","0",""};
								String[] ss1 = new String[]{"0","",""};//by tcl ���ĺ�ͬ�������2017-02-27
								
								int xs = xj.length()-1;
								String xj1 = ss[xs]+xj;
								String xj2 = ss1[xs]+xj;
								String jobcodeM = tvo.getJobcode()+xj1;
								String ctcodeM = tvo.getCtcode()+"-"+xj2;
								
								if("".equals(jmjobcode) || "".equals(jmctcode) || 
										!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
									int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
												"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
									if(MessageDialog.ID_OK == retBtn){
										htcard.setHeadItem("jobcode", jobcodeM);
										htcard.setHeadItem("ctcode", ctcodeM);
									}
								}
							}
						}else{
							String jobcodeM = ntvo.getJobcode()+"001";
							String ctcodeM = ntvo.getCtcode()+"-01";
					
							if("".equals(jmjobcode) || "".equals(jmctcode) || 
									!jobcodeM.equals(jmjobcode) || !ctcodeM.equals(jmctcode) ){
								int retBtn = MessageDialog.showOkCancelDlg(htcard, "ѡ��", 
											"��Ŀ����ӦΪ"+jobcodeM+"����ͬ���ӦΪ"+ctcodeM+",�Ƿ��޸ģ�");
								if(MessageDialog.ID_OK == retBtn){
									htcard.setHeadItem("jobcode", jobcodeM);
									htcard.setHeadItem("ctcode", ctcodeM);
								}
							}
						}
					}
				}else{
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��������");
					return;
				}
				
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����������ѡ���Ӧ�����ͬ");
				return;
			}
		}
		
		// ����ֵУ��		
		this.getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		String msg = checkDataValue();
		if(!"".equals(msg)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", msg);
			return;
		}
		
		// ��������ֵͳ��
		if(httype != 1 && httype != 0){
			// ���ͬ
			String htcode = dealValueOfStr(htcard, "ctcode");
			if(htcode.trim().length()==2){
				htcard.setHeadItem("pk_deptdoc", null);
			}
			htcard.setHeadItem("vbillstatus", 1);
		}else{
			// �ӱ�ͳ�ƽ��
			DefaultTableModel totalmodel = htcard.getBillModel().getTotalTableModel();
			Vector vor = totalmodel.getDataVector();
			UFDouble dwaibi = (UFDouble)((Vector)vor.get(0)).get(8);
			UFDouble dscaje = (UFDouble)((Vector)vor.get(0)).get(10);
//			if(dscaje.compareTo(new UFDouble("0.00"))<=0){
//				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","��ͬ���ϼ�С�ڵ����㣬���ܱ���");
//				return;
//			}
			htcard.setHeadItem("vbillstatus", 8);
			// ��ͬ���
			htcard.setHeadItem("dctjetotal", dscaje);
			htcard.setHeadItem("curr_amount", dwaibi);
			if(httype == 0){
				// ���۽��
				htcard.setHeadItem("dsaletotal", dscaje);
				htcard.setHeadItem("dcaigtotal", null);
				htcard.setHeadItem("pk_cust2", null);
			}else{
				// �ɹ����
				htcard.setHeadItem("dcaigtotal", dscaje);
				htcard.setHeadItem("dsaletotal", null);
				htcard.setHeadItem("pk_cust1", null);
			}
		}
		Object ctname=htcard.getHeadItem("ctname").getValueObject();
		String name=ctname==null?"":ctname.toString();
		String typesql="select invname from bd_invbasdoc where pk_invbasdoc='"+name+"' and nvl(dr,0)=0";
		Object obj=iQuery.executeQuery(typesql, new ColumnProcessor());
		String str=obj==null?"":obj.toString();
		Boolean fag=(str.equals("�ֳ�����"))||(str.equals("ʩ���ɱ�"))||(str.equals("�ӹ��ɱ�"));
		Object xmaobj = htcard.getHeadItem("xm_amount").getValueObject();
		UFDouble xmmny=new UFDouble(0);
		if(xmaobj!=null&&!"".equals(xmaobj)){
			xmmny=new UFDouble(xmaobj.toString());
		}
		if(2==httype && fag && xmmny.compareTo(new UFDouble(0))==0){
			MessageDialog.showHintDlg(getBillUI(), "��ʾ", str+"�����ͬ��Ԥ��ɱ�����Ϊ�ջ�Ϊ0��");
			return;
		}
		
		
		// �������ִ��
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
			
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			DhContractVO parvo = (DhContractVO)billVO.getParentVO();			
				
			//����Ƿ���ϴ���Ŀ���� wanglong 2014-02-24	
			pfserver.checkJobaseData(parvo);
			
			if("".equals(parvo.getPk_contract() == null ? "" : parvo.getPk_contract())){
				String primKey = HYPubBO_Client.insert(parvo);	
				parvo.setPk_contract(primKey);
			}else{
				HYPubBO_Client.update(parvo);
			}
				
			HYPubBO_Client.deleteByWhereClause(DhContractBVO.class, " pk_contract = '"+parvo.getPk_contract()+"'");
			DhContractBVO[] chvos = (DhContractBVO[])billVO.getChildrenVO();
			DhContractBVO[] nevos = new DhContractBVO[chvos.length];
			for(int i=0 ; i<chvos.length ; i++){
				DhContractBVO bvo = chvos[i];
				bvo.setPk_contract(parvo.getPk_contract());
				String primKeyBB = HYPubBO_Client.insert(bvo);	
				bvo.setPk_contract_b(primKeyBB);
				nevos[i]=bvo;
			}
				
			billVO.setParentVO(parvo);
			billVO.setChildrenVO(nevos);
		}

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
			
		setAddNewOperate(isAdding(), billVO);

		// ���ñ����״̬
		setSaveOperateState();
			
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		
		refashTreeVO();

	}
	
	private void refashTreeVO(){
		BillTreeManageUI treeUI = (BillTreeManageUI) getBillTreeManageUI();

		AggregatedValueObject aggvo = this.getBillTreeManageUI()
				.getBufferData().getCurrentVO();
		DhContractVO headvo = (DhContractVO) aggvo.getParentVO();
		treeUI.getBillTreeData().insertNodeToTree(headvo);
		VOTreeNode selectnode = new VOTreeNode("");
		selectnode.setNodeID(headvo.getPk_contract());
		selectnode.setCode(headvo.getCtcode());

		selectnode.setData(aggvo.getParentVO());
		setSelectionPath(headvo);
		this.onTreeSelected(selectnode);
	}

	public void setSelectionPath(DhContractVO headvo) {
		TreePath selectTreePath = null;
		MultiChildTreeCardUI treeUI = (MultiChildTreeCardUI) getBillTreeManageUI();

		DefaultTreeModel treeModel = (DefaultTreeModel) treeUI.getBillTree()
				.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();

		Enumeration en = root.preorderEnumeration();
		while (en.hasMoreElements()) {
			TableTreeNode treeNode = (TableTreeNode) en.nextElement();
			if ("root".equalsIgnoreCase(treeNode.getNodeID().toString().trim()))
				continue;
			VOTreeNode voTreeNode = (VOTreeNode) treeNode;
			CircularlyAccessibleValueObject deptdocVO = voTreeNode.getData();
			if (headvo.getCtcode().equalsIgnoreCase(
					deptdocVO.getAttributeValue("ctcode").toString())) {
				TreeNode[] treeNodes = treeModel.getPathToRoot(voTreeNode);
				selectTreePath = new TreePath(treeNodes);
				break;
			}
		}

		if (selectTreePath != null) {
			treeUI.getBillTree().expandPath(selectTreePath);
			treeUI.getBillTree().setSelectionPath(selectTreePath);
		}
	}

	public void onTreeSelected(VOTreeNode selectnode) {
		super.onTreeSelected(selectnode);
	}

	protected void setAddNewOperate(boolean isAdding,
			AggregatedValueObject billVO) throws Exception {

		super.setAddNewOperate(isAdding, billVO);
	}
	
	protected void onBoEdit() throws Exception {
		//v_deptperonal ��Ա��ϵ��ͼ
		String user = this._getOperator();
		String corp = this._getCorp().getPrimaryKey();
		String sql = "select count(1) from v_deptperonal where pk_corp = '"+corp+"' and pk_user = '"+user+"' and pdsn_level='1'";
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
//		int count = (Integer)query.executeQuery(sql, new ColumnProcessor());
//		if(count != 1){
//			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","��ֻͬ��ҵ����Ա����");
//			return;
//		}
//		
		DhContractVO patVO = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
//		if(!user.equals(patVO.getVoperatorid())){
//			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","��ֻͬ���������˵��ݵ����޸�");
//			return;
//		}
		
		
		DhContractVO newVO = (DhContractVO)query.retrieveByPK(DhContractVO.class, patVO.getPk_contract());
		
		if(2 == newVO.getHttype()){
			// ���ͬ�ж������Ӻ�ͬ
			String jobcode = newVO.getJobcode();
			String jobsql = "select count(1) from dh_contract where jobcode like '"+jobcode+"%' and pk_corp = '"+corp+"' ";
			Integer jobcnt = (Integer)query.executeQuery(jobsql, new ColumnProcessor());
			if(jobcnt > 1){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","�����ͬ�����Ӻ�ͬ�������޸�");
				return;
			}else{
				super.onBoEdit();
				BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
				BillCardLayout layout = (BillCardLayout)card.getLayout();
				layout.setHeadScale(60);
				layout.layoutContainer(card);
				this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setEdit(false);
				
				Object ctname=card.getHeadItem("ctname").getValueObject();
				String name=ctname==null?"":ctname.toString();
				String typesql="select invname from bd_invbasdoc where pk_invbasdoc='"+name+"' and nvl(dr,0)=0";
				Object obj=iQuery.executeQuery(typesql, new ColumnProcessor());
				String str=obj==null?"":obj.toString();
				Boolean fag=(str.equals("�ֳ�����"))||(str.equals("ʩ���ɱ�"))||(str.equals("�ӹ��ɱ�"));
				if(fag){
					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xm_amount").setEnabled(true);
				}
				// ������˵�10����
//				UIRefPane pduUif = (UIRefPane)card.getHeadItem("ctname").getComponent();
//				pduUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
//				
//				UIRefPane invUif = (UIRefPane)card.getBodyItem("invcode").getComponent();
//				invUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
//				
			}
		}else{		
			if(8 != newVO.getVbillstatus()){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ","�������������в����޸�");
				return;
			}
			
			super.onBoEdit();
			BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
			BillCardLayout layout = (BillCardLayout)card.getLayout();
			layout.setHeadScale(60);
			layout.layoutContainer(card);
			this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setEdit(false);
			
			//	 ������˵�10����
//			UIRefPane pduUif = (UIRefPane)card.getHeadItem("ctname").getComponent();
//			pduUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
//			pduUif.setPK(newVO.getCtname());
//			
//			UIRefPane invUif = (UIRefPane)card.getBodyItem("invcode").getComponent();
//			invUif.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' ", true);
			
		}
	}

	protected void onBoQuery() throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		String pkUser = this._getOperator();
		String pkCorp = this._getCorp().getPrimaryKey();
		
		//2017-12-14�Զ���Ȩ��(ֻ���Զ������Ĳ�ѯ���а��������ĺ�ͬ)
		String sqlss="select count(1) from v_deptperonal v where v.pk_user='"+pkUser+"' and pk_corp='"+pkCorp+"' and dept_name='�Զ�����' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer it=(Integer)iQ.executeQuery(sqlss, new ColumnProcessor());
		String str="";
		if(it>0){
			str=" or dh_contract.ctcode like '%����%' ";
		}
		
		String cwhere = strWhere.toString() + 
						" and (exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=dh_contract.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=dh_contract.ht_dept)) "+str+") ";
		
		
		/*String cwhere = strWhere.toString() + 
		" and (dh_contract.pk_deptdoc in (select vd.pk_deptdoc from v_deptperonal vd where vd.pk_corp = '"+pkCorp+"' " +
		" and vd.pk_user = '"+pkUser+"') or dh_contract.ht_dept in (select vd.pk_deptdoc from v_deptperonal vd " +
		" where vd.pk_corp = '"+pkCorp+"' and vd.pk_user = '"+pkUser+"'))";*/
		
		SuperVO[] queryVos = queryHeadVOs(cwhere);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		
		this.getBillTreeManageUI().getBillListPanel().getHeadBillModel().sortByColumn("jobcode", true);//������Ŀ������������
		//��ĿԤ��
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=model.getRowCount();
		for(int i=0;i<row;i++){
			Object obj=model.getValueAt(i, "httype");
			if(obj!=null&&obj.equals("���ۺ�ͬ")){
				model.setValueAt(null, i, "xm_amount");
			}
		}
	}

	

	@Override
	protected void onBoRefresh() throws Exception {
		super.onBoRefresh();
//		�޸��߼���ĿԤ��
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=this.getBillTreeManageUI().getBillListPanel().getHeadTable().getSelectedRow();
		Object obj=model.getValueAt(row, "httype");
		if(row > 0 && obj!=null&&obj.equals("���ۺ�ͬ")){
			model.setValueAt(null, row, "xm_amount");
		}
	}

	@Override
	protected void onBoCommit() throws Exception {
		DhContractVO patVO = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
		// �жϵ�ǰ�������Ƿ����Ƶ���
		String operid = _getOperator();
		String zdr = patVO.getVoperatorid();
		if(!zdr.equals(operid)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���øõ��ݵ��Ƶ����ύ�˵���");
			return;
		}
		String htcode = patVO.getCtcode();
		if(htcode.equalsIgnoreCase("")){
			this.getBillUI().showErrorMessage("��ͬ���벻����Ϊ�գ�");
			return ;
		}else{
		 int num = pfserver.queryfilesystem(htcode); //��ͬ�����и����ſ����ύ wanglong
		 if(num > 0){
			 
		 }else {
			 this.getBillUI().showErrorMessage("�����ϴ���ͬ�ı���ſ����ύ��");
				return ;
		 }
		}

		String pk = patVO.getPrimaryKey();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		DhContractVO newVO = (DhContractVO)query.retrieveByPK(DhContractVO.class, pk);
		
		// �ȽϽ����ϵĵ���״̬�����ݿ��еĵ���״̬
		if(patVO.getVbillstatus().intValue() != newVO.getVbillstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "����״̬�ѷ����ı䣬��ˢ�º��ڲ���");
			return;
		}
		
		String cttype=newVO.getPk_cttype();//��ͬ����
		if(3 == newVO.getVbillstatus()){
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "�������ύ");
			return;
		}else if(8 == newVO.getVbillstatus()){
			
			// �ύʱ��Ҫȷ����ĿԤ��
			
			// ֻ�����ۺ�ͬ��Ҫ��ĿԤ�� 2016-10-20�����ɹ�
			if(0==newVO.getHttype()||1==newVO.getHttype()){
				ConExeComDialog cedg = new ConExeComDialog(this.getBillUI());
				if(cedg.showConExecuteModel(newVO)){
				
					newVO.setVbillstatus(3);
					if(cttype.equals("0001A11000000000GHGU")){
						newVO.setYs_flag("1");
						newVO.setVbillstatus(2);//Ԥ��������
					}else{
						newVO.setYs_flag("0");
					}
					iv.updateVO(newVO);
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ύ�ɹ�");
					this.onBoRefresh();
				}
			}else{
				newVO.setVbillstatus(3);
				if(cttype.equals("0001A11000000000GHGU")){//����
					newVO.setYs_flag("1");
					newVO.setVbillstatus(2);
				}else{
					newVO.setYs_flag("0");
				}
				iv.updateVO(newVO);
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ύ�ɹ�");
				this.onBoRefresh();
			}	
			
		}
		
	}
	
	
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == IdhButton.RET_COMMIT) {
			retCommit();
		}
		
		if (intBtn == IdhButton.SEAL) {
			htSeal();
		}
		if (intBtn == IdhButton.FILEUPLOAD) {
			DhContractVO cvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			
			String pkCorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			
			if("1001".equals(pkCorp)){
				DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getCtcode());
			}else{
				if(null == cvo.getRelationid() || "".equals(cvo.getRelationid())){
					DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getCtcode());
				}else{
					IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					String sql = "select ctcode from dh_contract where pk_contract = '"+cvo.getRelationid()+"'";
					String relcode = (String)iQ.executeQuery(sql, new ColumnProcessor());
					DocumentManagerHT.showDM(this.getBillUI(), "DHHT", relcode);
				}
			}
			
		}
		if(intBtn == IdhButton.YCHT){
			String pkUser = this._getOperator();
			String pkCorp = this._getCorp().getPrimaryKey();
			
			String sql="select t.* from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=0 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') " +
				"and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=t.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=t.ht_dept)) order by t.ctcode ";
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DhContractVO> list=(List<DhContractVO>)iQ.executeQuery(sql, new BeanListProcessor(DhContractVO.class));
			
			getBufferData().clear();
			addDataToBuffer(list.toArray(new DhContractVO[0]));
			updateBuffer();
		}
		if(intBtn == IdhButton.FHSK){
			String pkUser = this._getOperator();
			String pkCorp = this._getCorp().getPrimaryKey();
			String sql="select t.* from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=1 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') " +
				"and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=t.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=t.ht_dept)) order by t.ctcode ";
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DhContractVO> list=(List<DhContractVO>)iQ.executeQuery(sql, new BeanListProcessor(DhContractVO.class));
			
			getBufferData().clear();
			addDataToBuffer(list.toArray(new DhContractVO[0]));
			updateBuffer();
		}
		
	}
	
	// ����
	private void retCommit() throws Exception {
		AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
		if(null == aggVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		DhContractVO patVO = (DhContractVO)aggVO.getParentVO();
		// �жϵ�ǰ�������Ƿ����Ƶ���
		String operid = _getOperator();
		String zdr = patVO.getVoperatorid();
		if(!zdr.equals(operid)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���øõ��ݵ��Ƶ��˲��ش˵���");
			return;
		}
		String pk = patVO.getPrimaryKey();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		DhContractVO newVO = (DhContractVO)query.retrieveByPK(DhContractVO.class, pk);
		// �ȽϽ����ϵĵ���״̬�����ݿ��еĵ���״̬
		if(patVO.getVbillstatus().intValue() != newVO.getVbillstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "����״̬�ѷ����ı䣬��ˢ�º��ڲ���");
			return;
		}
		if(3!=newVO.getVbillstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "�������������в��ɲ���");
			return;
		}
		newVO.setVbillstatus(8);
		iv.updateVO(newVO);
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���سɹ�");
		this.onBoRefresh();
	}

	// ����ʵ��
	private void htSeal() throws Exception{
		AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
		if(null == aggVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ѡ���ͬ����");
			return;
		}
		DhContractVO dhvo = (DhContractVO)aggVO.getParentVO();
		if(null == dhvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ѡ���ͬ����");
			return;
		}

		String pk = dhvo.getPrimaryKey();
		DhContractVO newdhvo = (DhContractVO)NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByPK(DhContractVO.class, pk);
		int status = newdhvo.getVbillstatus();
		if(status != 1){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ͬ�����������δ����");
			return;
		}
		int seal = newdhvo.getIs_seal()==null ? 0 : newdhvo.getIs_seal();
		if(seal == 1){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ͬ�����Ѹ���");
			return;
		}		
		
		String user = _getOperator();
		UFDate date = _getDate();
		
		// �жϵ�ǰ�������Ƿ����Ƶ���
		String zdr = dhvo.getVoperatorid();
		if(!zdr.equals(user)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���øõ��ݵ��Ƶ��˸��´˵���");
			return;
		}
		
		// ���ۺ�ͬ�Ͳɹ���Ҫ�ж�ʱ��
		if(0==newdhvo.getHttype() || 1==newdhvo.getHttype()){
			SealContractDialog dig = new SealContractDialog(this.getBillUI());
			if(dig.showConExecuteModel(newdhvo)){
				DhContractVO newdhvo2 = (DhContractVO)NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByPK(DhContractVO.class, pk);
				NCLocator.getInstance().lookup(IdhServer.class).SealDhht(newdhvo2, user, date);
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������");	
			}
			
		}else{
			NCLocator.getInstance().lookup(IdhServer.class).SealDhht(newdhvo, user, date);
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������");
		}
		this.onBoRefresh();
	}
	
	
	@Override
	protected void onBoImport() throws Exception {
		RelationContractDialog dg = new RelationContractDialog(this.getBillUI());
		AggregatedValueObject billvo = dg.showConExecuteModel();
		if(null != billvo){
			getBufferData().addVOsToBuffer(new AggregatedValueObject[] { billvo });
			int nCurrentRow = getBufferData().getVOBufferSize() - 1;
			if (nCurrentRow >= 0) {
				getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
				getBufferData().setCurrentRow(nCurrentRow);
			}
			
			refashTreeVO();
		}
	}

	/**
	 * �ַ�����ֵ����
	 * @param itemkey : �ֶ���
	 * */
	private String dealValueOfStr(BillCardPanel card,String itemkey){
		String value = card.getHeadItem(itemkey).getValueObject() == null ? "" 
			 				: card.getHeadItem(itemkey).getValueObject().toString();
		return value;
	}
	
	/**
	 * ������ֵ����
	 * @param itemkey : �ֶ���
	 * */
	private String dealValueOfRef(BillCardPanel card,String itemkey){
		UIRefPane ikref = (UIRefPane)card.getHeadItem(itemkey).getComponent();
		return ikref.getRefPK();
	}
	
	/**
	 * ������ֵ����
	 * @param itemkey : �ֶ���
	 * */
	private int dealValueOfInt(BillCardPanel card,String itemkey){
		Object ikobj = card.getHeadItem(itemkey).getValueObject();
		if(null == ikobj || "".equals(ikobj)){
			return 0;
		}else{
			String ikstr = ikobj.toString();
			return Integer.parseInt(ikstr);
		}
	}
	
	/**
	 * С����ֵ����
	 * @param itemkey : �ֶ���
	 * */
	private UFDouble dealValueOfDouble(BillCardPanel card,String itemkey){
		Object ikobj = card.getHeadItem(itemkey).getValueObject();
		if(null == ikobj || "".equals(ikobj)){
			return new UFDouble("0.00");
		}else{
			String ikstr = ikobj.toString();
			return new UFDouble(ikstr);
		}
	}

}