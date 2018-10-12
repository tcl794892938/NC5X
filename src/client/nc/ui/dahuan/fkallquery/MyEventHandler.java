package nc.ui.dahuan.fkallquery;

import java.util.ArrayList;
import java.util.List;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.dahuan.exceltools.ExcelUtils;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		
		if(IdhButton.FILEUPLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "付款单额度管理");
		}
		else if(IdhButton.FILEDOWNLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "付款单附件");
		}
	}




	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf) throws Exception {

		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0){
			strWhere = "1=1";
		}else{
			strWhere = strWhere.replaceAll("dh_fkjhbill.jobcode like '%", "dh_fkjhbill.jobcode like '");
		}
		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		int month = this._getDate().getMonth();
		int year = this._getDate().getYear();
		String stdate = "";
		if(month>6){
			
			int stmonth = month-6;
			if(stmonth>9){
				stdate = year+"-"+stmonth+"-01";
			}else{
				stdate = year+"-0"+stmonth+"-01";
			}
		}else{
			int styear = year-1;
			int stmonth = 6+month;
			if(stmonth>9){
				stdate = styear+"-"+stmonth+"-01";
			}else{
				stdate = styear+"-0"+stmonth+"-01";
			}
		}
		
		sqlWhereBuf.append(strWhere+" and ((nvl(dh_fkjhbill.sealflag,0)=0) or (nvl(dh_fkjhbill.sealflag,0)=1 and dh_fkjhbill.dbilldate>'"+stdate+"'))");
		return true;
	
	}

	@Override
	protected void onBoExport() throws Exception {
		ClientUI cui = (ClientUI)this.getBillUI();
		BillModel bmodel = cui.getBillListPanel().getHeadBillModel();
		
		UITable btable = cui.getBillListPanel().getHeadTable();
		
		int rows = bmodel.getRowCount();
		int cols = btable.getColumnCount();
		
		List<Object[]> lists = new ArrayList<Object[]>();
		String[] headColsCN = new String[]{
				"合同编号","合同名称","项目名称","付款单位","付款方式",
				"合同金额","累计已付款","计划付款金额","付款内容","付款日期"
		};
		
		for(int i=0;i<rows;i++){
			
			Object[] objs = new Object[cols];
			
			for(int j=0;j<cols;j++){
				objs[j] = btable.getValueAt(i, j);
			}
			
			lists.add(objs);
		}
		
		ExcelUtils.doExport("付款单", lists, headColsCN, cui);
	}

	
	
}