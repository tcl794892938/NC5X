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
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and ((dh_projectclear.pc_status=2 and dh_projectclear.fzr='"+_getOperator()+"') or (dh_projectclear.pc_status=4 and dh_projectclear.fzr<>'"+_getOperator()+"')) " +
				" and exists(select 1 from dh_contract where dh_contract.pk_contract = dh_projectclear.salecontractid) ");

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}



	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjClearVemo pcv = new ProjClearVemo(this.getBillUI());
		if(pcv.showProjClearVemo()){
		
			ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
			if(pcvo.getPc_status()==2){
				pcvo.setPc_status(5);
				pcvo.setFzr(this._getOperator());
				pcvo.setFzstatus("����");
				pcvo.setFzdate(this._getDate());
				pcvo.setFzvemo(pcv.getVemo());
			}else{//����2����
				pcvo.setPc_status(7);
				pcvo.setFzr2(this._getOperator());
				pcvo.setFzstatus2("����");
				pcvo.setFzdate2(this._getDate());
				pcvo.setFzvemo2(pcv.getVemo());
			}
			
			HYPubBO_Client.update(pcvo);
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����������");
			this.onBoRefresh();
		}
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		if(pcvo.getPc_status()==2){
			pcvo.setPc_status(4);
			pcvo.setFzr(this._getOperator());
			pcvo.setFzstatus("������");
			pcvo.setFzdate(this._getDate());
			pcvo.setFzvemo("����ͨ��");
		}else{
			pcvo.setPc_status(8);
			pcvo.setFzr2(this._getOperator());
			pcvo.setFzstatus2("������");
			pcvo.setFzdate2(this._getDate());
			pcvo.setFzvemo2("����ͨ��");
		}
		
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ͨ�����");
		this.onBoRefresh();
	}



	@Override
	public void onBillRef() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		String htz = pcvo.getSalectcode().substring(0, pcvo.getSalectcode().length()-3);
		BtnPowerVO powerVO = new BtnPowerVO(htz,"false","false","true");
		DocumentManagerHT.showDM(this.getBillUI(), "XMQS", htz, powerVO);
	}

	
}
