package nc.ui.dahuan.stuff;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.jobname"});
		setFieldName(new String[] {"合同编号"});
		setHiddenFieldCode(new String[] { "v.pk_jobmngfil" });
		setPkFieldCode("v.pk_jobmngfil");

		setOrderPart(" v.jobname ");
		
		setTableName(" v_constuff v ");
		
		String pkcorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
		setWherePart(" v.pk_corp = '"+pkcorp+"' ");
		
		setRefCodeField("v.jobname");
		setRefNameField("v.jobname");
		setCacheEnabled(false);
	
	}
	
}
