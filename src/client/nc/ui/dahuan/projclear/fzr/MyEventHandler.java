package nc.ui.dahuan.projclear.fzr;

import nc.ui.dahuan.projclear.DocumentManagerHT;
import nc.ui.dahuan.projclear.ProjClearVemo;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.projclear.ProjectClearVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.datapower.BtnPowerVO;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and ((dh_projectclear.pc_status=2 and dh_projectclear.fzr='"+_getOperator()+"') or (dh_projectclear.pc_status=4 and dh_projectclear.fzr<>'"+_getOperator()+"')) " +
				" and exists(select 1 from dh_contract where dh_contract.pk_contract = dh_projectclear.salecontractid) ");

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}



	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		ProjClearVemo pcv = new ProjClearVemo(this.getBillUI());
		if(pcv.showProjClearVemo()){
		
			ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
			if(pcvo.getPc_status()==2){
				pcvo.setPc_status(5);
				pcvo.setFzr(this._getOperator());
				pcvo.setFzstatus("驳回");
				pcvo.setFzdate(this._getDate());
				pcvo.setFzvemo(pcv.getVemo());
			}else{//副总2驳回
				pcvo.setPc_status(7);
				pcvo.setFzr2(this._getOperator());
				pcvo.setFzstatus2("驳回");
				pcvo.setFzdate2(this._getDate());
				pcvo.setFzvemo2(pcv.getVemo());
			}
			
			HYPubBO_Client.update(pcvo);
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "审批驳回完成");
			this.onBoRefresh();
		}
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		if(pcvo.getPc_status()==2){
			pcvo.setPc_status(4);
			pcvo.setFzr(this._getOperator());
			pcvo.setFzstatus("已审批");
			pcvo.setFzdate(this._getDate());
			pcvo.setFzvemo("审批通过");
		}else{
			pcvo.setPc_status(8);
			pcvo.setFzr2(this._getOperator());
			pcvo.setFzstatus2("已审批");
			pcvo.setFzdate2(this._getDate());
			pcvo.setFzvemo2("审批通过");
		}
		
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "审批通过完成");
		this.onBoRefresh();
	}



	@Override
	public void onBillRef() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		String htz = pcvo.getSalectcode().substring(0, pcvo.getSalectcode().length()-3);
		BtnPowerVO powerVO = new BtnPowerVO(htz,"false","false","true");
		DocumentManagerHT.showDM(this.getBillUI(), "XMQS", htz, powerVO);
	}

	
}
