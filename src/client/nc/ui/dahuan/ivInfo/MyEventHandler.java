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
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
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
			MessageDialog.showHintDlg(getBillUI(), "提示", "数据异常！请选择单据！");
			return ;
		}
		
		if(vo.getDhpdu_flag()!=0){
			MessageDialog.showHintDlg(getBillUI(),"提示", "单据状态需为未提交！");
			return ;
		}
		
		super.onBoDelete();
	}
	

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		String cotsql = "select count(1) from sm_user_role u where u.pk_corp = '"+_getCorp().getPrimaryKey()+"' and u.cuserid = '"+_getOperator()+"' " 
				+ " and u.pk_role in (select r.pk_role from sm_role r where r.role_code in ('DH00','DH01','DH08','CS2') and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		int cot = (Integer)iQ.executeQuery(cotsql, new ColumnProcessor());
		if(cot<1){
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and dhpdu_flag<>5 ");

			getBufferData().clear();
			// 增加数据到Buffer
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
			// 增加数据到Buffer
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
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有该单据的制单人才可执行此操作");
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
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请维护需要变更的存货信息");
				return;
			}
			
			if(ivcode.length() != 5){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "存货编码应为5位");
				return;
			}
			String invclsql = "select t.invclasscode from bd_invcl t where t.pk_invcl = '"+pkincl+"'";
			String invclcode = (String)iQ.executeQuery(invclsql, new ColumnProcessor());
			if(!ivcode.startsWith(invclcode)){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "存货编码应以"+invclcode+"开头");
				return;
			}
			
			String bgsql = "select count(1) from bd_invbasdoc t where (t.invcode = '"+ivcode+"' or t.invname = '"+ivname+"') " +
					" and t.pk_invbasdoc<>'"+pkinvobj.toString()+"' ";
			int retbg = (Integer)iQ.executeQuery(bgsql, new ColumnProcessor());
			if(retbg != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货重复");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') " +
					" and nvl(t.dhpdu_flag,0)<>5 "; 
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货重复");
				return;
			}
		}else if(0==jsStus){
			
			if(ivcode.length() != 5){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "存货编码应为5位");
				return;
			}
			String invclsql = "select t.invclasscode from bd_invcl t where t.pk_invcl = '"+pkincl+"'";
			String invclcode = (String)iQ.executeQuery(invclsql, new ColumnProcessor());
			if(!ivcode.startsWith(invclcode)){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "存货编码应以"+invclcode+"开头");
				return;
			}
			
			String xzsql = "select count(1) from bd_invbasdoc t where (t.invcode = '"+ivcode+"' or t.invname = '"+ivname+"')";
			int retxz = (Integer)iQ.executeQuery(xzsql, new ColumnProcessor());
			if(retxz != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货重复");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') ";
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货重复");
				return;
			}
		}else{
			if(null == pkinvobj || "".equals(pkinvobj)){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请维护需要注销的存货信息");
				return;
			}
			String bssql = "select count(1) from dh_product t where (t.pdu_no = '"+ivcode+"' or t.pdu_name = '"+ivname+"') " +
					" and nvl(t.dhpdu_flag,0)<>5 ";
			if(null != pkdhinv && !"".equals(pkdhinv)){
				bssql += " and t.pk_dhpdu <> '"+pkdhinv.toString()+"' ";
			}
			int retbs = (Integer)iQ.executeQuery(bssql, new ColumnProcessor());
			if(retbs != 0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货重复");
				return;
			}
		}
		
		super.onBoSave();
	}

	// 提交
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
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有该单据的制单人才可执行此操作");
			}
		}
	}	
	
	// 撤销
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
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有该单据的制单人才可执行此操作");
			}
		}
	}
	
	// 工程部审核
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
	
	// 工程部驳回
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
	
	// 不同意
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
	
	// 副总同意
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

	// 财务确认
	public void onBoCwQr() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			PdutVO ctvo = (PdutVO)aggvo.getParentVO();
			PdutVO nctvo = (PdutVO)HYPubBO_Client.queryByPrimaryKey(PdutVO.class, ctvo.getPk_dhpdu());
			
			if(1==nctvo.getDj_status()){
				// 变更
				InvbasdocVO inb = (InvbasdocVO)HYPubBO_Client.queryByPrimaryKey(InvbasdocVO.class, nctvo.getPkivmandoc());
				inb.setPk_invcl(nctvo.getPdu_type());
				inb.setInvcode(nctvo.getPdu_no());
				inb.setInvname(nctvo.getPdu_name());
				inb.setPk_measdoc(nctvo.getPk_dnaw());
				HYPubBO_Client.update(inb);
			}else if(2==nctvo.getDj_status()){
				// 注销
				InvmandocVO[] invs = (InvmandocVO[])HYPubBO_Client.queryByCondition(InvmandocVO.class, " pk_invbasdoc = '"+nctvo.getPkivmandoc()+"' ");
				for(InvmandocVO inv : invs){
					inv.setSealflag(new UFBoolean(true));
					inv.setSealdate(this._getDate());
					HYPubBO_Client.update(inv);
				}
			}else{
				// 新增
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				String cfsql = "select count(1) from bd_invbasdoc t where t.invcode='"+nctvo.getPdu_no()+"' and t.invname = '"+nctvo.getPdu_name()+"'";
				int retcf = (Integer)iQ.executeQuery(cfsql, new ColumnProcessor());
				if(retcf == 0){
					MessageDialog.showHintDlg(this.getBillUI(), "提示", "该存货尚未维护");
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
//			查询工程部pk
			String sql2="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
				"and pk_corp='"+pk_corp+"' and deptname='工程管理部' and canceled='N'";
			
			Object gc=query.executeQuery(sql2, new ColumnProcessor());
			
			if(gc==null){
				MessageDialog.showHintDlg(getBillUI(), "提示", "请先维护部门工程管理部");
				return ;
			}
			
			String sql3="select count(1) from dh_fkgx_d where nvl(dr,0)=0 and pk_dept_user='"+user+"' " +
					"and pk_fkgx=(select pk_fkgx from dh_fkgx x where nvl(x.dr,0)=0 " +
					"and pk_deptdoc='"+gc+"' and pk_corp='"+pk_corp+"')";
			Integer it2=(Integer)query.executeQuery(sql3, new ColumnProcessor());
			
			if(it2 < 1){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有工程部业务员才可制单");
				return;
			}
		}
		super.onBoAdd(bo);
	}
	
}