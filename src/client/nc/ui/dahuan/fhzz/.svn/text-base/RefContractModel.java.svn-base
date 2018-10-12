package nc.ui.dahuan.fhzz;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","v.ctname"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("v_dhdelivery v ");
		
		addWherePart(getConfString());
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.ctname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return  " and v.pk_contract in (select cn.pk_contract from dh_contract cn where cn.pk_deptdoc in ( select vd.pk_deptdoc from v_deptperonal vd "
				+ " where vd.pk_corp='"+pkCorp+"' and vd.pk_user='"+pkUser+"'))";
		
	}
	
	
	
	
}
