package nc.ui.dahuan.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class HtRetRefModel extends AbstractRefModel {

	public HtRetRefModel() {
		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"ctcode"});
		setFieldName(new String[] {"合同编号"});
		setHiddenFieldCode(new String[] { "pk_contract" });
		setPkFieldCode("pk_contract");

		setOrderPart(" ctcode ");
		
		setTableName("dh_contract");
		setRefCodeField("ctcode");
		setRefNameField("ctcode");
		setCacheEnabled(false);
		
		
	}
}
