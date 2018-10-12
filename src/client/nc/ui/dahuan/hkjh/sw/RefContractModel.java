package nc.ui.dahuan.hkjh.sw;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"v.ctcode","v.invname","v.syje"});
		setFieldName(new String[] {"��ͬ���","��ͬ����","ʣ����"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("vsale_contract v ");
		setWherePart(" v.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and v.syje<>0 and v.sealflag<>'Y'");
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
}
