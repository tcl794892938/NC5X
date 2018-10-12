package nc.ui.dahuan.jcInfo.invAudit;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.dahuan.jcInfo.invInfo.BgInvEntity;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	public String condition = "";

	// 查询
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		condition = strWhere.toString()
		 			+ " and nvl(dh_invbasdoc.bg_status,0)=1 and dh_invbasdoc.vapproveid = '"+this._getOperator()+"'";

		refreshListPanel();
	}
	
	public void refreshListPanel() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);
		getBufferData().clear();
		addDataToBuffer(queryVos);
		updateBuffer();
	}
	
	// 驳回
	@Override
	protected void onBoCancelAudit() throws Exception {
		BillManageUI bui = (BillManageUI)this.getBillUI();
		
		BgInvEntity[] bginvvos;
		
		if(bui.isListPanelSelected()){
			bginvvos = (BgInvEntity[])bui.getBillListPanel().getHeadBillModel().getBodySelectedVOs(BgInvEntity.class.getName());
		}else{
			BgInvEntity bgvo = (BgInvEntity)this.getBufferData().getCurrentVO().getParentVO();
			bginvvos = new BgInvEntity[]{ bgvo };
		}
		
		if(0==bginvvos.length){
			MessageDialog.showHintDlg(bui, "提示", "请选择单据");
			return;
		}
		
		for(BgInvEntity bnvo : bginvvos){
			bnvo.setBg_status(0);
			HYPubBO_Client.update(bnvo);
		}
		
		MessageDialog.showHintDlg(bui, "提示", "单据都已驳回");
		
		refreshListPanel();
	}
	
	// 审核
	@Override
	public void onBoAudit() throws Exception {
		BillManageUI bui = (BillManageUI)this.getBillUI();
		
		BgInvEntity[] bginvvos;
		
		if(bui.isListPanelSelected()){
			bginvvos = (BgInvEntity[])bui.getBillListPanel().getHeadBillModel().getBodySelectedVOs(BgInvEntity.class.getName());
		}else{
			BgInvEntity bgvo = (BgInvEntity)this.getBufferData().getCurrentVO().getParentVO();
			bginvvos = new BgInvEntity[]{ bgvo };
		}
		
		if(0==bginvvos.length){
			MessageDialog.showHintDlg(bui, "提示", "请选择单据");
			return;
		}
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		// 单位
		String measql = "select max(pk_measdoc) from bd_invbasdoc";
		String pkmea = (String)iQ.executeQuery(measql, new ColumnProcessor());
		
		// 税目
		String taxsql = "select max(pk_taxitems) from bd_invbasdoc";
		String pktax = (String)iQ.executeQuery(taxsql, new ColumnProcessor());
		
		// 创建时间
		String timesql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
		String timeret = (String)iQ.executeQuery(timesql, new ColumnProcessor());
		
		for(BgInvEntity bnvo : bginvvos){
			
			// 判断档案是变更还是新增
			if(null == bnvo.getPk_invbasdoc() || "".equals(bnvo.getPk_invbasdoc())){
				InvbasdocVO invbasvo = new InvbasdocVO();
				invbasvo.setAssistunit(new UFBoolean(false));
				invbasvo.setInvcode(bnvo.getInv_no());
				invbasvo.setInvname(bnvo.getInv_name());
				invbasvo.setPk_corp(bnvo.getPk_corp());
				invbasvo.setPk_invcl(bnvo.getPk_invcl());
				invbasvo.setPk_measdoc(pkmea);
				invbasvo.setDiscountflag(new UFBoolean(false));
				invbasvo.setDr(0);
				invbasvo.setLaborflag(new UFBoolean(false));
				invbasvo.setPk_taxitems(pktax);
				invbasvo.setSetpartsflag(new UFBoolean(false));
				invbasvo.setUnitvolume(new UFDouble("0.00"));
				invbasvo.setUnitweight(new UFDouble("0.00"));
				invbasvo.setAutobalancemeas(new UFBoolean(false));
				invbasvo.setIsmngstockbygrswt(new UFBoolean(false));
				invbasvo.setIsstorebyconvert(new UFBoolean(false));
				invbasvo.setAsset(new UFBoolean(false));
				invbasvo.setCreatetime(new UFDateTime(timeret));
				invbasvo.setCreator(bnvo.getVoperid());
				invbasvo.setIsretail(new UFBoolean(false));
				invbasvo.setIselectrans(new UFBoolean(false));
				String pkinvbas = HYPubBO_Client.insert(invbasvo);
				
				InvmandocVO invmanvo = new InvmandocVO();
				invmanvo.setPk_invbasdoc(pkinvbas);
				invmanvo.setAccflag(new UFBoolean(false));
				invmanvo.setInvlifeperiod(2);
				invmanvo.setIsappendant(new UFBoolean(false));
				invmanvo.setIsinvretfreeofchk(new UFBoolean(true));
				invmanvo.setIsinvretinstobychk(new UFBoolean(false));
				invmanvo.setPk_corp(bnvo.getPk_corp());
				invmanvo.setWholemanaflag(new UFBoolean(false));
				invmanvo.setQualitymanflag(new UFBoolean(false));
				invmanvo.setIsrecurrentcheck(new UFBoolean(false));
				HYPubBO_Client.insert(invmanvo);
			}else{
				InvbasdocVO invbasvo = (InvbasdocVO)iQ.retrieveByPK(InvbasdocVO.class, bnvo.getPk_invbasdoc());
				invbasvo.setInvcode(bnvo.getInv_no());
				invbasvo.setInvname(bnvo.getInv_name());
				invbasvo.setPk_invcl(bnvo.getPk_invcl());
				HYPubBO_Client.update(invbasvo);
			}
			
			bnvo.setBg_status(2);
			bnvo.setVapprovedate(this._getDate());
			HYPubBO_Client.update(bnvo);
		}
		
		MessageDialog.showHintDlg(bui, "提示", "单据都已审核");
		
		refreshListPanel();
	}

	// 全选
	@Override
	protected void onBoSelAll() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// 全消
	@Override
	protected void onBoSelNone() throws Exception {
		((BillManageUI)this.getBillUI()).getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}

	

		
	
}