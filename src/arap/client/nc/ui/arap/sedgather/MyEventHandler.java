package nc.ui.arap.sedgather;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.arap.sedgather.AggSedGatherVO;
import nc.vo.arap.sedgather.SedGatherDVO;
import nc.vo.arap.sedgather.SedGatherHVO;
import nc.vo.trade.pub.IBillStatus;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billmanageUI, IControllerBase icontroll) {
		super(billmanageUI, icontroll);
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		SedGatherDialog dlg = new SedGatherDialog(this.getBillUI());
		AggSedGatherVO aggvo = dlg.showSedGatherDialog();
		
		if(null != aggvo){
			if (getBillManageUI().isListPanelSelected())
				getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
			
			
			BillCardPanel cardp = this.getBillCardPanelWrapper().getBillCardPanel();
			cardp.setBillValueVO(aggvo);
			
			cardp.setHeadItem("vbillstatus", IBillStatus.FREE);
			cardp.setHeadItem("pk_billtype", "DHSG");
			cardp.setHeadItem("pk_corp", _getCorp().getPrimaryKey());
			cardp.setTailItem("voperatorid", _getOperator());
			cardp.setTailItem("voperatordate", _getDate());

		}
	}
	
	@Override
	protected void onBoPrint() throws Exception {
		
		AggSedGatherVO aggvo  = (AggSedGatherVO)this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showErrorDlg(this.getBillManageUI(), "警告", "数据有误");
			return;
		}
		
		SedGatherHVO hvo = aggvo.getParentVO();
		if(null == hvo){
			MessageDialog.showErrorDlg(this.getBillManageUI(), "警告", "数据有误");
			return;
		}
		int vbillstatus = hvo.getVbillstatus();
		if(1 != vbillstatus){
			MessageDialog.showHintDlg(this.getBillManageUI(), "提示", "请先将单据审批流走完");
			return;
		}
		
		SedGatherDVO[] dvos = aggvo.getChildrenVO();
		if(null == dvos || dvos.length != 1){
			MessageDialog.showErrorDlg(this.getBillManageUI(), "警告", "数据有误");
			return;
		}
		
		// 收款单PK
		String pksk = dvos[0].getPk_gathering();
		
		// 销售订单PK
		String pksale = hvo.getPk_saleorder();
		
		String nodecode = this.getBillUI()._getModuleCode();
		
		SKDataSource skdata = new SKDataSource(pksale,nodecode);
		
		PrintEntry print = new PrintEntry(null,skdata);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
				._getModuleCode(), getBillUI()._getOperator(), getBillUI()
				.getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.preview();
	}

	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
}
