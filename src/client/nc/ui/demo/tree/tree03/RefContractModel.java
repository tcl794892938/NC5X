package nc.ui.demo.tree.tree03;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","v.invname"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("dhadd_contract v ");
		
		addWherePart(getConfString());
		
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
