package nc.ui.dahuan.projclear.qry;

import nc.ui.dahuan.projclear.DocumentManagerHT;
import nc.ui.pub.beans.MessageDialog;
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

		String pkCorp = this._getCorp().getPrimaryKey();
		
		String strW = strWhere.toString().replaceAll("dh_projectclear.pk_corp", 
				"(select dh_contract.pk_corp from dh_contract where dh_contract.pk_contract=dh_projectclear.salecontractid)");
		
		if(!"1001".equals(pkCorp)){
			strW+=" and (select dh_contract.pk_corp from dh_contract where dh_contract.pk_contract=dh_projectclear.salecontractid)='"+pkCorp+"' ";
		}
		
		int month = this._getDate().getMonth();
		int year = this._getDate().getYear();
		String str=this._getDate().toString().substring(8);
		String stdate = "";
		if(month>6){
			
			int stmonth = month-6;
			if(stmonth>9){
				stdate = year+"-"+stmonth+"-"+str;
			}else{
				stdate = year+"-0"+stmonth+"-"+str;
			}
		}else{
			int styear = year-1;
			int stmonth = 6+month;
			if(stmonth>9){
				stdate = styear+"-"+stmonth+"-"+str;
			}else{
				stdate = styear+"-0"+stmonth+"-"+str;
			}
		}
		
		//日期2016-10-20
		if(strW.indexOf("zddate")==-1){//没有制单日期
			strW+= " and ( (pc_status<>6) or(pc_status=6 and zddate>'"+stdate+"') )";
		}
		
		//strW+= " and ( (pc_status<>6) or(pc_status=6 and zddate>'"+stdate+"') )";
		
		SuperVO[] queryVos = queryHeadVOs(strW);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
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
