package nc.ui.dahuan.stuff;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("��ͬ");
		setRefTitle("��ͬ");
		setFieldCode(new String[] {"v.jobname"});
		setFieldName(new String[] {"��ͬ���"});
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
