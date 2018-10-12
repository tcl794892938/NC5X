package nc.ui.dahuan.fkjh;

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
		
		setTableName("vdh_contract v ");
		
		addWherePart(getConfString());
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		if(pkCorp.equals("1002")){
			return " and v.pk_deptdoc in (select f.pk_deptdoc from v_deptperonal f where f.pk_corp='"+pkCorp+"' and f.pk_user='"+pkUser+"') " +
				 "and exists (select 1 from dh_contract t where t.pk_contract=v.pk_contract " +
					"and exists(select 1 from bd_jobmngfil g where g.pk_jobmngfil=t.pk_jobmandoc and g.sealflag='N'))";
		}
		
		return	"and exists (select 1 from dh_contract t where t.pk_contract=v.pk_contract " +
				"and exists(select 1 from bd_jobmngfil g where g.pk_jobmngfil=t.pk_jobmandoc and g.sealflag='N'))";
	}
	
	
	
	
}
