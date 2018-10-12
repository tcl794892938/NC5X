package nc.ui.dahuan.fkallquery;

import nc.ui.bd.ref.AbstractRefModel;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","v.invname"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("vdh_contract v ");
		
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
}
