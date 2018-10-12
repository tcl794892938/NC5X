package nc.ui.dahuan.projclear.zdr;

import javax.swing.table.DefaultTableModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dahuan.projclear.DocumentManagerHT;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.projclear.ProjectClearVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
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

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and zdr = '"+this._getOperator()+"' ");

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}



	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		UIRefPane scUIR = (UIRefPane)card.getHeadItem("salecontractid").getComponent();
		//scUIR.getRefModel().addWherePart("  and not exists(select 1 from dh_projectclear f where f.salecontractid=v.pk_contract and nvl(f.dr,0)=0) ", true);
		
	}



	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		
		String pkPC = card.getHeadItem("pk_projectclear").getValueObject().toString();
		
		UIRefPane scUIR = (UIRefPane)card.getHeadItem("salecontractid").getComponent();
		String pkUIR = scUIR.getRefPK();
		scUIR.getRefModel().addWherePart("  and not exists(select 1 from dh_projectclear f where f.salecontractid=v.pk_contract" +
				"  and nvl(f.dr,0)=0 and f.pk_projectclear<>'"+pkPC+"') ", true);
		scUIR.setPK(pkUIR);
	}



	@Override
	protected void onBoSave() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		card.dataNotNullValidate();
		
		DefaultTableModel dtm = card.getBillModel().getTotalTableModel();
		UFDouble dvamount = new UFDouble(dtm.getValueAt(0, 11)==null?"0.00":dtm.getValueAt(0, 11).toString());
		UFDouble hxamount = new UFDouble(card.getHeadItem("hxamount").getValueObject().toString());
		
		if(dvamount.compareTo(new UFDouble(0))<0){
			MessageDialog.showHintDlg(getBillUI(),"提示", "净损失金额不能大于核销金额！");
			return ;
		}
		
		Object obj=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("overamount").getValueObject();
		UFDouble ovamount=obj==null?new UFDouble(0):new UFDouble(obj.toString());
		
		if(hxamount.compareTo(ovamount)>0){
			MessageDialog.showHintDlg(getBillUI(), "提示", "核销金额不能大于应收欠款金额！");
			return ;
		}
		card.setHeadItem("lossamount", hxamount.sub(dvamount,2));
		card.setHeadItem("pc_status", 0);
		
		super.onBoSave();
	}

	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		pcvo.setPc_status(0);
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "驳回完成");
		this.onBoRefresh();
	}

	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		String htz = pcvo.getSalectcode().substring(0, pcvo.getSalectcode().length()-3);
		String querySql = "  select count(1) count from sm_pub_filesystem   where sm_pub_filesystem.path  like '"
			+ htz + "/%' and sm_pub_filesystem.isfolder = 'n' and nvl(dr,0)=0 ";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		int cot = (Integer)iQ.executeQuery(querySql, new ColumnProcessor());
		if(cot<1){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请上传项目清算协议");
			return;
		}
		
		pcvo.setPc_status(1);
		pcvo.setGcdate(null);
		pcvo.setGcr(null);
		pcvo.setGcstatus(null);
		pcvo.setGcvemo(null);
		
		pcvo.setFzdate(null);
		pcvo.setFzstatus(null);
		pcvo.setFzvemo(null);
		
		pcvo.setFzr2(null);
		pcvo.setFzdate2(null);
		pcvo.setFzstatus2(null);
		pcvo.setFzvemo2(null);
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "提交完成");
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
		int status = pcvo.getPc_status();
		if(0==status){
			DocumentManagerHT.showDM(this.getBillUI(), "XMQS", htz);
		}else{
			BtnPowerVO powerVO = new BtnPowerVO(htz,"false","false","true");
			DocumentManagerHT.showDM(this.getBillUI(), "XMQS", htz, powerVO);
		}
		
	}

	
	
}
