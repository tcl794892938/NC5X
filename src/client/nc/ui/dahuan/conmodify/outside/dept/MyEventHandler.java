package nc.ui.dahuan.conmodify.outside.dept;

import javax.swing.table.DefaultTableModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.contractmodify.ConModfiyVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		String qxWhere = " and zdr='"+this._getOperator()+"' ";
		
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+qxWhere);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}



	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == IdhButton.RET_COMMIT){
			onBoRetCommit();
		}
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
	protected void onBoSave() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel model = card.getBillModel();
		
		// 非空判断
		card.dataNotNullValidate();
		
		String patconid = card.getHeadItem("parent_contractid").getValueObject().toString();
		
		Object objPkPrimary = card.getHeadItem("pk_contractmodify").getValueObject();
		
		String sql = "select count(1) from dh_contractmodify m where m.parent_contractid='"+patconid+"' and nvl(m.dr,0)=0 and m.modify_status<>3";
		
		if(null != objPkPrimary && !"".equals(objPkPrimary)){
			sql += " and pk_contractmodify <> '"+objPkPrimary.toString()+"' ";
		}
		
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		int cot = (Integer)iQ.executeQuery(sql, new ColumnProcessor());
		if(cot>0){
			MessageDialog.showHintDlg(card, "提示", "该合同存在未走完的审批");
			return;
		}
		
		// 金额合计
		int rows = model.getRowCount();
		if(rows<0){
			MessageDialog.showHintDlg(card, "提示", "表体不能为空");
			return;
		}
		DefaultTableModel total = model.getTotalTableModel();
		card.setHeadItem("fc_summny", total.getValueAt(0, 10));
		card.setHeadItem("summny", total.getValueAt(0, 11));
		
		super.onBoSave();
	}

	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		ConModfiyVO mvo = (ConModfiyVO)aggvo.getParentVO();
		
		String querySql = "  select count(1) from sm_pub_filesystem f where f.path like "+
					"(select t.ctcode from dh_contract t where t.pk_contract='"+mvo.getParent_contractid()+"')||'/%变更%' and f.isfolder = 'n' ";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		int cot = (Integer)iQ.executeQuery(querySql, new ColumnProcessor());
		if(cot<1){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请上传变更文本");
			return;
		}
		
		mvo.setModify_status(1);
		HYPubBO_Client.update(mvo);
		this.onBoRefresh();
	}
	
	private void onBoRetCommit() throws Exception{
		
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
			this.onBoRefresh();
		}
	}
	
}
