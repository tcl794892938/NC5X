package nc.ui.dahuan.xmrz;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","v.invname"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("vsale_contract v ");
		try{
			addWherePart(getConfString());
		}catch(Exception e3){
			e3.printStackTrace();
		}
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.invname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString() throws Exception{
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"+pkCorp+"' and u.cuserid = '"+pkUser+"' " 
						+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHXM' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		int retCot = (Integer)query.executeQuery(deptsql, new ColumnProcessor());

		if(0<retCot){
			return "";
		}else{
			return " and  exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=v.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=v.ht_dept))";
		}
	}
	
	
	
	
}
