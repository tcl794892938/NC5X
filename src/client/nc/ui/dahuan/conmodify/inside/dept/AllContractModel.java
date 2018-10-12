package nc.ui.dahuan.conmodify.inside.dept;

import nc.ui.bd.ref.AbstractRefModel;

public class AllContractModel extends AbstractRefModel {

	public AllContractModel() {

		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"t.ctcode"});
		setFieldName(new String[] {"��ͬ���"});
		setHiddenFieldCode(new String[] { "t.pk_contract"});
		setPkFieldCode("t.pk_contract");

		setOrderPart(" t.ctcode ");
		
		setTableName(" dh_contract t ");
		
		setRefCodeField("t.ctcode");
		setRefNameField("t.ctcode");
		setCacheEnabled(false);
	
	}
}
