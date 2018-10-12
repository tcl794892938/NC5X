package nc.ui.dahuan.fhtz;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"v.ctcode","v.invname"});
		setFieldName(new String[] {"��ͬ���","��ͬ����"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("vpur_contract v ");
		
		addWherePart(getConfString());
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return " and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=v.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=v.ht_dept))";
		
	}
	
	
	
	
}
