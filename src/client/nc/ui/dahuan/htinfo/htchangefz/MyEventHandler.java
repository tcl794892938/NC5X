package nc.ui.dahuan.htinfo.htchangefz;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.dahuan.htinfo.htchange.DocumentManagerHT;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.b38.JobbasfilVO;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.dahuan.htinfo.htquery.HtConLogoDtlEntity;
import nc.vo.dahuan.htinfo.htquery.HtConLogoEntity;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


public class MyEventHandler extends AbstractMyEventHandler{
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	String condition = "";
	
	@Override 
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn==IdhButton.FILEUPLOAD){
			HtChangeEntity cvo = (HtChangeEntity)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getHtcode());
		}
	}	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		condition = strWhere.toString() + " and corp_manager = '"+this._getOperator()+"' and htstatus = 1 ";
		
		clearListVO();
	}

	private void clearListVO() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	@Override
	protected void onBoSelAll() throws Exception {
		((ClientUI)this.getBillUI()).getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	@Override
	protected void onBoSelNone() throws Exception {
		((ClientUI)this.getBillUI()).getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}

	@Override
	public void onBoAudit() throws Exception {
		// 审核
		ClientUI ui = (ClientUI)this.getBillUI();
		
		if(ui.isListPanelSelected()){
			int num = ui.getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				int isselected = ui.getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject aggvo = this.getBufferData().getVOByRowNo(row);
					HtChangeEntity htc = (HtChangeEntity)aggvo.getParentVO();
					HtChangeDtlEntity[] htcdds = (HtChangeDtlEntity[])aggvo.getChildrenVO();
					changeContractData(htc,htcdds,this._getDate());
				}
			}
		}else{
			AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();		
			HtChangeEntity htc = (HtChangeEntity)aggvo.getParentVO();
			HtChangeDtlEntity[] htcdds = (HtChangeDtlEntity[])aggvo.getChildrenVO();
			changeContractData(htc,htcdds,this._getDate());
		}
		
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "审批操作完成");
		clearListVO();
	}	
	
	//审核事件操作
	private void changeContractData(HtChangeEntity htc,HtChangeDtlEntity[] htcdtls, UFDate nowdate) throws Exception {
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// 更新合同档案
		DhContractVO dvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, htc.getPk_contract());
		DhContractBVO[] dcvolit = (DhContractBVO[])HYPubBO_Client.queryByCondition(DhContractBVO.class, " pk_contract = '"+dvo.getPk_contract()+"' ");
		
		// 存储旧的合同档案
		HtConLogoEntity logen = new HtConLogoEntity();
		logen.setPk_contract(htc.getPk_contract());
		logen.setPk_cust(dvo.getPk_cust1()==null?dvo.getPk_cust2():dvo.getPk_cust1());
		logen.setPk_deptmanager(dvo.getVapproveid());
		logen.setPk_htname(dvo.getCtname());
		logen.setHttype(htc.getHttype());
		logen.setCttype(htc.getHtctpe());
		logen.setJobcode(dvo.getJobcode());
		logen.setJobname(htc.getHtprojname());
		logen.setHtcode(htc.getHtcode());
		logen.setHtname(htc.getHtpduname());
		logen.setCustname(htc.getHtcust());
		logen.setHtamount(dvo.getDctjetotal());
		logen.setBank_name(htc.getHtbank());
		logen.setSax_no(htc.getHtsaxno());
		logen.setHtstyle(htc.getHtstyle());
		logen.setVoperdate(dvo.getDbilldate());
		logen.setVemo(dvo.getVmen());
		logen.setHtdate(dvo.getHtrq());
		logen.setHtexedate(dvo.getDstartdate());
		logen.setHtaddress(dvo.getHtaddress());
		logen.setHtdeladdress(dvo.getVjhaddress());
		logen.setHtdeldate(dvo.getDjhdate());
		logen.setXmaccount(dvo.getXm_amount());//项目预算
		
		String deptsql = "select t.deptname from bd_deptdoc t where t.pk_deptdoc='"+dvo.getHt_dept()+"'";
		String htdept = (String)iQ.executeQuery(deptsql, new ColumnProcessor());
		logen.setBelong_dept(htdept);
		
		deptsql = "select t.deptname from bd_deptdoc t where t.pk_deptdoc='"+dvo.getPk_deptdoc()+"'";
		String pkdept = (String)iQ.executeQuery(deptsql, new ColumnProcessor());
		logen.setAdding_dept(pkdept);
		
		String psnsql = "select t.psnname from bd_psnbasdoc t where t.pk_psnbasdoc='"+dvo.getPk_xmjl()+"'";
		String xmjl = (String)iQ.executeQuery(psnsql, new ColumnProcessor());
		logen.setHtmanager(xmjl);
		
		psnsql = "select t.psnname from bd_psnbasdoc t where t.pk_psnbasdoc='"+dvo.getPk_fzr()+"'";
		String fzr = (String)iQ.executeQuery(psnsql, new ColumnProcessor());
		logen.setHtcontractor(fzr);
		
		String corpsql = "select t.unitname from bd_corp t where t.pk_corp='"+dvo.getPk_corp()+"'";
		String corpname = (String)iQ.executeQuery(corpsql, new ColumnProcessor());
		logen.setCorp_name(corpname);
		
		String usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getPk_fuzong()+"'";
		String fzname = (String)iQ.executeQuery(usersql, new ColumnProcessor());
		logen.setCorp_manager(fzname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getVapproveid()+"'";
		String zgname = (String)iQ.executeQuery(usersql, new ColumnProcessor());
		logen.setDept_manager(zgname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getPk_ysid()+"'";
		String ysname = (String)iQ.executeQuery(usersql, new ColumnProcessor());
		logen.setDept_contractor(ysname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getVoperatorid()+"'";
		String vpname = (String)iQ.executeQuery(usersql, new ColumnProcessor());
		logen.setVoperator(vpname);
		
		//项目预算
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getXm_amount()+"'";
		UFDouble ys = (UFDouble)iQ.executeQuery(usersql, new ColumnProcessor());
		logen.setXmaccount(ys);
		
		String pklogo = HYPubBO_Client.insert(logen);
		
		
		for(DhContractBVO ctvo : dcvolit){
			HtConLogoDtlEntity logdd = new HtConLogoDtlEntity();
			logdd.setPk_conlogo(pklogo);
			logdd.setPk_pdu(ctvo.getPk_invbasdoc());
			logdd.setPdu_no(ctvo.getInvcode());
			logdd.setPdu_name(ctvo.getInvname());
			logdd.setPdu_stylemodel(ctvo.getVggxh());
			logdd.setMeadoc_name(ctvo.getPk_danw());
			logdd.setPdu_num(ctvo.getNnumber());
			logdd.setPdu_piece(ctvo.getDjprice());
			logdd.setPdu_amount(ctvo.getDjetotal());
			logdd.setDelivery_date(ctvo.getDghsj());
			logdd.setVemo(ctvo.getVmen());
			HYPubBO_Client.insert(logdd);
		}
		
		
		// 合同金额、项目经理、合同签约人		
		int httype = dvo.getHttype().intValue();
		if(0==httype){
			dvo.setDsaletotal(htc.getHtamount());
		}else if(1==httype){
			dvo.setDcaigtotal(htc.getHtamount());
		}else{
			// 虚合同不操作
		}
		dvo.setDctjetotal(htc.getHtamount());
		
		// 汇率
		dvo.setCurr_amount(htc.getHtamount().div(dvo.getCurrenty_rate()));
		
		
		dvo.setPk_xmjl(htc.getHtmanager());
		dvo.setPk_fzr(htc.getHtcontractor());
		if(dvo.getHt_changenum()==null){
			dvo.setHt_changenum(1);
		}else{
			int chnum = dvo.getHt_changenum().intValue() + 1;
			dvo.setHt_changenum(chnum);
		}
		
		dvo.setXm_amount(ys);//把项目预算赛进去
		
		HYPubBO_Client.update(dvo);
		
		HYPubBO_Client.deleteByWhereClause(DhContractBVO.class, " pk_contract = '"+dvo.getPk_contract()+"' ");
		
		for(HtChangeDtlEntity htcdd : htcdtls){
			DhContractBVO bvo = new DhContractBVO();
			bvo.setPk_contract(dvo.getPk_contract());
			bvo.setInvcode(htcdd.getPdu_no());
			bvo.setPk_invbasdoc(htcdd.getPk_pdu());
			bvo.setVggxh(htcdd.getPdu_stylemodel());
			bvo.setNnumber(htcdd.getPdu_num());
			bvo.setDjprice(htcdd.getPdu_piece());
			bvo.setDjetotal(htcdd.getPdu_amount());
			bvo.setDghsj(htcdd.getDelivery_date());
			bvo.setVmen(htcdd.getVemo());
			bvo.setInvname(htcdd.getPdu_name());
			bvo.setPk_danw(htcdd.getMeadoc_name());
			bvo.setCurrenty_amount(htcdd.getPdu_piece().multiply(dvo.getCurrenty_rate()));
			bvo.setCurr_amount_sum(htcdd.getPdu_amount().multiply(dvo.getCurrenty_rate()));
			HYPubBO_Client.insert(bvo);
		}
		
		// 更新项目档案
		JobbasfilVO[] jobbaslit = (JobbasfilVO[])HYPubBO_Client.queryByCondition(JobbasfilVO.class, " jobcode = '"+dvo.getJobcode()+"'");
		if(null != jobbaslit && jobbaslit.length > 0){
			JobbasfilVO jobbas = jobbaslit[0];
			
			JobmngfilVO[] jobmnglit = (JobmngfilVO[])HYPubBO_Client.queryByCondition(JobmngfilVO.class, " pk_jobbasfil = '"+jobbas.getPrimaryKey()+"'");
			if(null != jobmnglit && jobmnglit.length > 0){
				JobmngfilVO jobmng = jobmnglit[0];
				
				// 由于人员档案参照不同
				jobmng.setPk_psndoc(dvo.getPk_fzr());
				
				
				if(0==httype){
					jobmng.setDef2(htc.getHtamount().toString());
				}else if(1==httype){
					jobmng.setDef3(htc.getHtamount().toString());
				}else{
					// 虚合同不操作
				}
				
				HYPubBO_Client.update(jobmng);
			}
		}
		
		// 更新合同变更单
		htc.setHtstatus(2);
		htc.setSure_date(nowdate);
		HYPubBO_Client.update(htc);
	}

	@Override
	protected void onBoCancelAudit() throws Exception {

		AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
		if(null == aggVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		HtChangeEntity patVO = (HtChangeEntity)aggVO.getParentVO();
		if(null == patVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		String pk = patVO.getPrimaryKey();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		HtChangeEntity newVO = (HtChangeEntity)query.retrieveByPK(HtChangeEntity.class, pk);
		// 比较界面上的单据状态和数据库中的单据状态
		if(patVO.getHtstatus().intValue() != newVO.getHtstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "提示", "单据状态已发生改变，请刷新后在操作");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(newVO);
		
		if(flag){
			newVO.setHtstatus(0);
			iv.updateVO(newVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(newVO.getRetvemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(newVO.getPk_contract());
			rtvo.setRet_address("合同变更");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "驳回完成");
			clearListVO();
		}
	
	}
	
	
	
}
	
	
