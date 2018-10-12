package nc.ui.dahuan.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class HtInfoDelRefModel extends AbstractRefModel {

	public HtInfoDelRefModel() {
		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"httype","ctcode","ctname"});
		setFieldName(new String[] {"��ͬ����","��ͬ���","��ͬ����"});
		setHiddenFieldCode(new String[] { "pk_contract" });
		setPkFieldCode("pk_contract");

		setOrderPart(" ctcode ");
		
		setTableName("v_dhdelivery");
		setRefCodeField("ctcode");
		setRefNameField("ctname");
		setCacheEnabled(false);
		
		
	}
}
