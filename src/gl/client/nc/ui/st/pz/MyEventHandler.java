package nc.ui.st.pz;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);

		
	}
	
	@Override
	protected void onBoSave() throws Exception {

		
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj1=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("styear").getValueObject();
		Object obj2=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("stmonth").getValueObject();
		
		String sql="select count(*) from rq where styear='"+obj1.toString()+"' and stmonth='"+obj2.toString()+"' ";
		Object obj3=iQ.executeQuery(sql, new ColumnProcessor());
		int a=obj3==null?0:Integer.parseInt(obj3.toString());
		
		if(getBillUI().getBillOperate()==IBillOperate.OP_ADD){
			if(a>0){
				MessageDialog.showHintDlg(getBillUI(), "提示", "该日期已作凭证！");
				return;
			}
		}
		if(getBillUI().getBillOperate()==IBillOperate.OP_EDIT){
			Object obj4=getBufferData().getCurrentVO().getParentVO().getAttributeValue("styear");
			Object obj5=getBufferData().getCurrentVO().getParentVO().getAttributeValue("stmonth");
			if(!obj1.toString().equals(obj4.toString()) || !obj2.toString().equals(obj5.toString())){
				if(a>0){
					MessageDialog.showHintDlg(getBillUI(), "提示", "该日期已作凭证！");
					return;	
				}
			}else{
				if(a>1){
					MessageDialog.showHintDlg(getBillUI(), "提示", "该日期已作凭证！");
					return;
				}
			}
		}
		
		/*BillModel model=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int row=model.getRowCount();
		if(row>0){
			for(int i=0;i<row;i++){
				Object obj=model.getValueAt(i, "pk_pzcx");
				if(obj==null){
					model.setRowState(BillModel.ADD, i);
				}else{
					model.setRowState(BillModel.MODIFICATION, i);
				}
			}
		}*/
		
		super.onBoSave();
	}

	
	
	
	
	

		
		
		
		
		
	

	@Override
	protected void onBoQuery() throws Exception {
		
		super.onBoQuery();
	}

	
	

}