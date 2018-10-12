package nc.ui.dahuan.conmodify.inside.group;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.dahuan.conmodify.inside.dept.RetCommitDialog;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.contractmodify.ConModfiyVO;
import nc.vo.dahuan.contractmodify.ConModifyDVO;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends ManageEventHandler {

	public String cxWhere = "";
	
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == IdhButton.FILEUPLOAD){
			AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
			if(null == aggvo){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
				return;
			}
			ConModfiyVO mvo = (ConModfiyVO)aggvo.getParentVO();
			
			DhContractVO dvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, mvo.getParent_contractid());
			
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
		}
		
	}

	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		cxWhere = strWhere.toString()+" and zzr = '"+this._getOperator()+"' ";
		
		refreshVO();
		
	}

	private void refreshVO() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(cxWhere);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	@Override
	protected void onBoCard() throws Exception {
		ClientUI ui = (ClientUI)this.getBillUI();
		ui.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = ui.getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(70);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		ClientUI ui = (ClientUI)this.getBillUI();
		if(ui.isListPanelSelected()){
			ui.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			BillCardPanel card = ui.getBillCardPanel();
			BillCardLayout layout = (BillCardLayout)card.getLayout();
			layout.setHeadScale(70);
			layout.layoutContainer(card);
		}
		ui.setBillOperate(IBillOperate.OP_ADD);
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		ConModfiyVO mvo = (ConModfiyVO)aggvo.getParentVO();
		ConModifyDVO[] xvos = (ConModifyDVO[])aggvo.getChildrenVO();
		
		// 集团合同信息
		updateDataBase(mvo.getParent_contractid(),mvo,xvos);
		
		// 子公司合同信息
		updateDataBase(mvo.getChild_contractid(),mvo,xvos);
		
		mvo.setModify_status(3);
		mvo.setZzdate(this._getDate());
		HYPubBO_Client.update(mvo);
		refreshVO();
		
	}
	
	// 根据合同PK更新信息
	private void updateDataBase(String contractid,ConModfiyVO mvo,ConModifyDVO[] xvos) throws Exception{
		DhContractVO jtvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, contractid);
		JobmngfilVO jmvo = (JobmngfilVO)HYPubBO_Client.queryByPrimaryKey(JobmngfilVO.class, jtvo.getPk_jobmandoc());
		jtvo.setXm_amount(mvo.getBudget());
		if(0==jtvo.getHttype().intValue()){
			jmvo.setDef2(mvo.getSummny().toString());
			jtvo.setDsaletotal(mvo.getSummny());
			jtvo.setDctjetotal(mvo.getSummny());
			jtvo.setCurr_amount(mvo.getFc_summny());
		}else if(1==jtvo.getHttype().intValue()){
			jmvo.setDef3(mvo.getSummny().toString());
			jtvo.setDcaigtotal(mvo.getSummny());
			jtvo.setDctjetotal(mvo.getSummny());
			jtvo.setCurr_amount(mvo.getFc_summny());
		}
		HYPubBO_Client.update(jmvo);
		HYPubBO_Client.update(jtvo);
		
		HYPubBO_Client.deleteByWhereClause(DhContractBVO.class, " pk_contract = '"+jtvo.getPk_contract()+"' ");
		
		for(ConModifyDVO xvo : xvos){
			DhContractBVO jbvo = new DhContractBVO();
			jbvo.setPk_contract(jtvo.getPk_contract());
			jbvo.setInvcode(xvo.getInvcode());
			jbvo.setPk_invbasdoc(xvo.getPk_invbasdoc());
			jbvo.setVggxh(xvo.getStylemodel());
			jbvo.setNnumber(xvo.getNums());
			jbvo.setDjprice(xvo.getPrice());
			jbvo.setDjetotal(xvo.getAmount());
			jbvo.setDghsj(xvo.getDelivery_date());
			jbvo.setVmen(xvo.getVemo());
			jbvo.setInvname(xvo.getInvname());
			jbvo.setPk_danw(xvo.getMeaname());
			jbvo.setCurrenty_amount(xvo.getFc_price());
			jbvo.setCurr_amount_sum(xvo.getFc_amount());
			HYPubBO_Client.insert(jbvo);
		}
		
	}

	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		ConModfiyVO mvo = (ConModfiyVO)aggvo.getParentVO();
		
		RetCommitDialog rcmDg = new RetCommitDialog(this.getBillUI());
		boolean flag = rcmDg.showRetCommitDialog();
		if(flag){
			mvo.setRefusalvemo(rcmDg.getVemo());
			mvo.setModify_status(4);
			HYPubBO_Client.update(mvo);
			refreshVO();
		}
	}

}
