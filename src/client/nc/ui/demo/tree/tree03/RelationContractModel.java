package nc.ui.demo.tree.tree03;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RelationContractModel extends AbstractRefModel {

	public RelationContractModel() {

		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"t.jobcode","t.jobname","t.ctcode","t.ctname","t.dctjetotal"});
		setFieldName(new String[] {"��Ŀ���","��Ŀ����","��ͬ���","��ͬ����","��ͬ���"});
		setHiddenFieldCode(new String[] { "t.pk_contract" });
		setPkFieldCode("t.pk_contract");

		setOrderPart(" t.jobcode ");
		
		setTableName(" v_relationcon t ");
		
		addWherePart(getConfString());
		
		setRefCodeField("t.ctcode");
		setRefNameField("t.ctcode");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return " and t.custname = (select c.unitname from bd_corp c where c.pk_corp='"+pkCorp+"') ";
	}
	
	
	
	
}
