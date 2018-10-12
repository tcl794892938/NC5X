package nc.ui.dahuan.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class HtZXRefModel extends AbstractRefModel{
	public HtZXRefModel(){
		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"jobname","def1","pk_custdoc","pk_vendoc","pk_deptdoc","pk_deptdoc1"});
		setFieldName(new String[] {"合同编号","合同名称","客户","供应商","制单部门","归属部门"});
		setHiddenFieldCode(new String[] { "pk_jobmngfil" });
		setPkFieldCode("pk_jobmngfil");

		setOrderPart(" jobname ");
		
		setTableName("temq_ct");
		setRefCodeField("jobname");
		setRefNameField("def1");
		setCacheEnabled(false);
	}
}
