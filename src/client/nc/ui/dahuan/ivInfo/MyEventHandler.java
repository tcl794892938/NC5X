package nc.ui.dahuan.ivInfo;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.dahuan.ctInfo.CustVO;
import nc.vo.dahuan.ivInfo.PdutVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == IdhButton.RET_COMMIT){
			onBoRetCommit();
		}	
		if(intBtn == IdhButton.CWQR){
			onBoCwQr();
		}
		if(intBtn == IdhButton.NOAGREE){
			onBoNoAgree();
		}
		if(intBtn == IdhButton.AGREE){
			onBoAgree();
		}
	}
	
	
	@Override
	protected void onBoDelete() throws Exception {
		
		PdutVO vo=(PdutVO)getBufferData().getCurrentVO().getParentVO();
		if(vo==null){
			MessageDialog.showHintDlg(getBillUI(), "��ʾ", "�����쳣����ѡ�񵥾ݣ�");
			return ;
		}
		
		if(vo.getDhpdu_flag()!=0){
			MessageDialog.showHintDlg(getBillUI(),"��ʾ", "����״̬��Ϊδ�ύ��");
			return ;
		}
		
		super.onBoDelete();
	}
	

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		String cotsql = "select count(1) from sm_user_role u where u.pk_corp = '"+_getCorp().getPrimaryKey()+"' and u.cuserid = '"+_getOperator()+"' " 
				+ " and u.pk_role in (select r.pk_role from sm_role r where r.role_code in ('DH00','DH01','DH08','CS2') and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		int cot = (Integer)iQ.executeQuery(cotsql, new ColumnProcessor());
		if(cot<1){
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and dhpdu_flag<>5 ");

			getBufferData().clear();
			// �������ݵ�Buffer
			addDataToBuffer(queryVos);

			updateBuffer();
		}else{
			int month = this._getDate().getMonth();
			int year = this._getDate().getYear();
			String stdate = "";
			if(month>6){
				
				int stmonth = month-6;
				if(stmonth>9){
					stdate = year+"-"+stmonth+"-01";
				}else{
					stdate = year+"-0"+stmonth+"-01";
				}
			}else{
				int styear = year-1;
				int stmonth = 6+month;
				if(stmonth>9){
					stdate = styear+"-"+stmonth+"-01";
				}else{
					stdate = styear+"-0"+stmonth+"-01";
				}
			}
			
			String str="";
			if(strWhere.toString().indexOf("dh_product.voperdate")==-1){
				str=strWhere.toString()+" and ((dhpdu_flag<>5) or (dhpdu_flag=5 and voperdate>'"+stdate+"')) ";
			}else{
				str=strWhere.toString();
			}
			
			SuperVO[] queryVos = queryHeadVOs(str);
			
			getBufferData().clear();
			// �������ݵ�Buffer
			addDataToBuffer(queryVos);
			
			updateBuffer();
			
		}
		
	}

	@Override
	protected void onBoEdit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			if(this._getOperator().equals(ctvo.getVoperid())){
				super.onBoEdit();
				BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
				UIRefPane ivuif = (UIRefPane)card.getHeadItem("pkivmandoc").getComponent();
				String conf = " and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' and exists (select 1 from bd_invmandoc  " +
							"  where nvl(bd_invmandoc.sealflag,'N')='N' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc) ";
				ivuif.getRefModel().addWherePart(conf);
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ֻ�иõ��ݵ��Ƶ��˲ſ�ִ�д˲���");
			}
		}
	}

	
	
	@Override
	protected void onBoSave() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillData data = card.getBillData();
		if(data != null){
			data.dataNotNullValidate();
		}
		
		int jsStus = (Integer)card.getHeadItem("dj_status").getValueObject();
		Object pkdhinv = card.getHeadItem("pk_dhpdu").getValueObject();
		Object pkinvobj = card.getHeadItem("pkivmandoc").getValueObject();
		String ivcode = (String)card.getHeadItem("pdu_no").getValueObject();
		String ivname = (String)card.getHeadItem("pdu_name").getValueObject();
		String pkincl = (String)card.getHeadItem("pdu_type").getValueObject();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		if(1==jsStus){
			
			if(null == pkinvobj || "".equals(pkinvobj)){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ά����Ҫ����Ĵ����Ϣ");
				return;
			}
			
			if(ivcode.length() != 5){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ӦΪ5λ");
				return;
			}
			String invclsql = "select t.invclasscode from bd_invcl t where t.pk_invcl = '"+pkincl+"'";
			String invclcode = (String)iQ.executeQuery(invclsql, new ColumnProcessor());
			if(!ivcode.startsWith(invclcode)){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������Ӧ��"+invclcode+"��ͷ");
				return;
			}
			
			String bgsql = "select count(1) from bd_invbasdoc t where (t.invcode = '"+ivcode+"' or t.invname = '"+ivname+"') " +
					" and t.pk_invbasdoc<>'"+pkinvobj.toString()+"' ";
			int retbg = (Integer)iQ.executeQuery(bgsql, new ColumnProcessor());
			if(retbg != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô���ظ�");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') " +
					" and nvl(t.dhpdu_flag,0)<>5 "; 
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô���ظ�");
				return;
			}
		}else if(0==jsStus){
			
			if(ivcode.length() != 5){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ӦΪ5λ");
				return;
			}
			String invclsql = "select t.invclasscode from bd_invcl t where t.pk_invcl = '"+pkincl+"'";
			String invclcode = (String)iQ.executeQuery(invclsql, new ColumnProcessor());
			if(!ivcode.startsWith(invclcode)){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������Ӧ��"+invclcode+"��ͷ");
				return;
			}
			
			String xzsql = "select count(1) from bd_invbasdoc t where (t.invcode = '"+ivcode+"' or t.invname = '"+ivname+"')";
			int retxz = (Integer)iQ.executeQuery(xzsql, new ColumnProcessor());
			if(retxz != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô���ظ�");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') ";
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô���ظ�");
				return;
			}
		}else{
			if(null == pkinvobj || "".equals(pkinvobj)){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ά����Ҫע���Ĵ����Ϣ");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') " +
					" and nvl(t.dhpdu_flag,0)<>5 ";
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô���ظ�");
				return;
			}
		}
		
		super.onBoSave();
	}

	// �ύ
	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			if(this._getOperator().equals(ctvo.getVoperid())){
				PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
				nctvo.setDhpdu_flag(1);
				HYPubBO_Client.update(nctvo);
				super.onBoRefresh();
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ֻ�иõ��ݵ��Ƶ��˲ſ�ִ�д˲���");
			}
		}
	}	
	
	// ����
	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			if(this._getOperator().equals(ctvo.getVoperid())){
				PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
				nctvo.setDhpdu_flag(0);
				HYPubBO_Client.update(nctvo);
				super.onBoRefresh();
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ֻ�иõ��ݵ��Ƶ��˲ſ�ִ�д˲���");
			}
		}
	}
	
	// ���̲����
	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			nctvo.setDhpdu_flag(3);
			nctvo.setGcbid(this._getOperator());
			nctvo.setGcbdate(this._getDate());
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}
	
	// ���̲�����
	public void onBoRetCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			nctvo.setDhpdu_flag(2);
			nctvo.setGcbid(this._getOperator());
			nctvo.setGcbdate(this._getDate());
			
			CanAudMemoDialog cdg = new CanAudMemoDialog(this.getBillUI());
			if(cdg.showCanAudMemoDialog(nctvo)){
				HYPubBO_Client.update(nctvo);
			}
			super.onBoRefresh();
		}
	}
	
	// ��ͬ��
	public void onBoNoAgree() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			nctvo.setFuzongid(this._getOperator());
			nctvo.setVappdate(this._getDate());
			nctvo.setDhpdu_flag(2);
			
			CanAudMemoDialog cdg = new CanAudMemoDialog(this.getBillUI());
			if(cdg.showCanAudMemoDialog(nctvo)){
				HYPubBO_Client.update(nctvo);
			}
			super.onBoRefresh();
		}
	}
	
	// ����ͬ��
	public void onBoAgree() throws Exception{
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			nctvo.setDhpdu_flag(4);
			nctvo.setFuzongid(this._getOperator());
			nctvo.setVappdate(this._getDate());
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}

	// ����ȷ��
	public void onBoCwQr() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			
			if(1==nctvo.getDj_status()){
				// ���
				InvbasdocVO inb = (InvbasdocVO)HYPubBO_Client.queryByPrimaryKey(InvbasdocVO.class, nctvo.getPkivmandoc());
				inb.setPk_invcl(nctvo.getPdu_type());
				inb.setInvcode(nctvo.getPdu_no());
				inb.setInvname(nctvo.getPdu_name());
				inb.setPk_measdoc(nctvo.getPk_dnaw());
				HYPubBO_Client.update(inb);
			}else if(2==nctvo.getDj_status()){
				// ע��
				InvmandocVO[] invs = (InvmandocVO[])HYPubBO_Client.queryByCondition(InvmandocVO.class, " pk_invbasdoc = '"+nctvo.getPkivmandoc()+"' ");
				for(InvmandocVO inv : invs){
					inv.setSealflag(new UFBoolean(true));
					inv.setSealdate(this._getDate());
					HYPubBO_Client.update(inv);
				}
			}else{
				// ����
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				String cfsql = "select count(1) from bd_invbasdoc t where t.invcode='"+nctvo.getPdu_no()+"' and t.invname = '"+nctvo.getPdu_name()+"'";
				int retcf = (Integer)iQ.executeQuery(cfsql, new ColumnProcessor());
				if(retcf == 0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ô����δά��");
					return;
				}
			}
			
			
			nctvo.setDhpdu_flag(5);
			nctvo.setCaiwuid(this._getOperator());
			nctvo.setSuredate(this._getDate());
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		
		String user=_getOperator();
		String pk_corp=_getCorp().getPk_corp();
		IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if(!pk_corp.equals("1002")){
//			��ѯ���̲�pk
			String sql2="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
				"and pk_corp='"+pk_corp+"' and deptname='���̹���' and canceled='N'";
			
			Object gc=query.executeQuery(sql2, new ColumnProcessor());
			
			if(gc==null){
				MessageDialog.showHintDlg(getBillUI(), "��ʾ", "����ά�����Ź��̹���");
				return ;
			}
			
			String sql3="select count(1) from dh_fkgx_d where nvl(dr,0)=0 and pk_dept_user='"+user+"' " +
					"and pk_fkgx=(select pk_fkgx from dh_fkgx x where nvl(x.dr,0)=0 " +
					"and pk_deptdoc='"+gc+"' and pk_corp='"+pk_corp+"')";
			Integer it2=(Integer)query.executeQuery(sql3, new ColumnProcessor());
			
			if(it2 < 1){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ֻ�й��̲�ҵ��Ա�ſ��Ƶ�");
				return;
			}
		}
		super.onBoAdd(bo);
	}
	
}