package nc.ui.demo.tree.tree03;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RelationContractModel extends AbstractRefModel {

	public RelationContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"t.jobcode","t.jobname","t.ctcode","t.ctname","t.dctjetotal"});
		setFieldName(new String[] {"项目编号","项目名称","合同编号","合同名称","合同金额"});
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
