package nc.ui.dahuan.fkallquery;

import nc.ui.bd.ref.AbstractRefModel;

public class RefFkDateModel extends AbstractRefModel {

	public RefFkDateModel() {

		setRefNodeName("��������");
		setRefTitle("��������");
		setFieldCode(new String[] {"v.fkyearmonth"});
		setFieldName(new String[] {"��������"});
		setPkFieldCode("v.fkyearmonth");

		setTableName("v_fkjhfkdate v ");
		
		setRefCodeField("v.fkyearmonth");
		setRefNameField("v.fkyearmonth");
		setCacheEnabled(false);
	
	}
	
}
