package nc.ui.dahuan.hkjh.wh;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","v.invname","v.syje"});
		setFieldName(new String[] {"合同编号","合同名称","剩余金额"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("vsale_contract v ");
		setWherePart(" v.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and v.syje<>0 and v.sealflag<>'Y'", true);
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return " and v.pk_deptdoc in (select f.pk_deptdoc from v_deptperonal f where f.pk_corp='"+pkCorp+"' and f.pk_user='"+pkUser+"') ";
	}
	
	
	
	
}
