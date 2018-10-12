package nc.ui.dahuan.fhzz;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefXmNameModel extends AbstractRefModel {

	public RefXmNameModel() {

		setRefNodeName("项目名称");
		setRefTitle("项目名称");
		setFieldCode(new String[] {" distinct v.jobname"});
		setFieldName(new String[] {"项目名称"});
		setHiddenFieldCode(new String[] { "v.jobname" });
		setPkFieldCode("v.jobname");

		setOrderPart(" v.jobname ");
		
		setTableName("v_dhdelivery v ");
		
		addWherePart(getConfString());
		
		
		setRefCodeField("v.jobname");
		setRefNameField("v.jobname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return  " and v.pk_contract in (select cn.pk_contract from dh_contract cn where cn.pk_deptdoc in ( select vd.pk_deptdoc from v_deptperonal vd "
				+ " where vd.pk_corp='"+pkCorp+"' and vd.pk_user='"+pkUser+"'))";
		
	}
	
	
	
	
}
