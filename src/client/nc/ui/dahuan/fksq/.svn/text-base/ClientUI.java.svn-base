package nc.ui.dahuan.fksq;

import java.util.HashSet;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;



 public class ClientUI extends AbstractClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String[] itemkeys = new String[]{
				fileDef.getField_Corp(),
				fileDef.getField_Operator(),
				fileDef.getField_Billtype(),
				fileDef.getField_BillStatus()
				};
		Object[] values = new Object[]{
				pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype,
				new Integer(IBillStatus.FREE).toString()
				};
		
		for(int i = 0; i < itemkeys.length; i++){
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if(item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if(item != null)
				item.setValue(values[i]);
		}
		getBillCardPanel().setHeadItem("vbillstatus", 8);
		getBillCardPanel().setHeadItem("dbilldate", this._getDate());
		getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
		
	}
	

	 public HashSet getUpSourcrBillbid(){
		  HashSet hbodyid = new HashSet(0);  
	    if (IBillOperate.OP_EDIT== this.getBillOperate()) {
	    	nc.vo.dahuan.fksq.MyBillVO billvo = (nc.vo.dahuan.fksq.MyBillVO) getBillCardPanel().getBillValueVO(
	    			nc.vo.dahuan.fksq.MyBillVO.class.getName(), nc.vo.dahuan.fksq.DhFksqbillVO.class.getName(),
	    			nc.vo.dahuan.fksq.DhFksqbillBVO.class.getName());
	      if (billvo != null && billvo.getChildrenVO() != null
	          && billvo.getChildrenVO().length > 0) {
	        int iLength = billvo.getChildrenVO().length;
	        for (int i = 0; i < iLength; i++) {
	          hbodyid.add(billvo.getChildrenVO()[i].getAttributeValue("vsourcebillrowid"));
	        }
	      }
	    }
		  return hbodyid;
	  }
}
