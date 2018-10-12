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
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "�����ȹ���");
		}
		else if(IdhButton.FILEDOWNLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "�������");
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
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
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
				"��ͬ���","��ͬ����","��Ŀ����","���λ","���ʽ",
				"��ͬ���","�ۼ��Ѹ���","�ƻ�������","��������","��������"
		};
		
		for(int i=0;i<rows;i++){
			
			Object[] objs = new Object[cols];
			
			for(int j=0;j<cols;j++){
				objs[j] = btable.getValueAt(i, j);
			}
			
			lists.add(objs);
		}
		
		ExcelUtils.doExport("���", lists, headColsCN, cui);
	}

	
	
}