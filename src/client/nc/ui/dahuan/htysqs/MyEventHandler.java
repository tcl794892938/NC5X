package nc.ui.dahuan.htysqs;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

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
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	@Override
	protected void onBoCard() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = getBillManageUI().getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}
	
	public String condition = "";

	// 查询过滤条件
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		String user = this._getOperator();
		String corpid = this._getCorp().getPrimaryKey();
		
		condition = strWhere.toString() + " and nvl(vbillstatus,0) = 3 " 
											   + " and pk_ysid = '"+user+"' "
											   + " and nvl(ys_flag,0) = 1 ";
		
		setListVOs();
		
	}

	private void setListVOs() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	// 弃审
	@Override
	public void onBoCancelAudit() throws Exception {
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		DhContractVO dhcVO = (DhContractVO)aggvo.getParentVO();
		if(null == dhcVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(dhcVO);
		
		if(flag){
			dhcVO.setYs_flag("0");
			dhcVO.setVbillstatus(8);
			HYPubBO_Client.update(dhcVO);
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "弃审完成");
			// 界面刷新
			setListVOs();
		}
		
		
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
		}
	}
	
	
	
}