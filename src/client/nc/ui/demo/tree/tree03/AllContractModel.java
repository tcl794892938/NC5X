package nc.ui.demo.tree.tree03;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class AllContractModel extends AbstractRefModel {

	public AllContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode"});
		setFieldName(new String[] {"合同编号"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("dh_contract v ");
		
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.ctcode");
		setCacheEnabled(false);
	
	}
	
}
