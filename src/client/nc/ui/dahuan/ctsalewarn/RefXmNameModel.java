package nc.ui.dahuan.ctsalewarn;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefXmNameModel extends AbstractRefModel {

	public RefXmNameModel() {

		setRefNodeName("项目名称");
		setRefTitle("项目名称");
		setFieldCode(new String[] {" distinct v.vdef6"});
		setFieldName(new String[] {"项目名称"});
		setHiddenFieldCode(new String[] { "v.vdef6" });
		setPkFieldCode("v.vdef6");

		setOrderPart(" v.vdef6 ");
		
		setTableName("vsale_contract v ");
		try{
			addWherePart(getConfString());
		}catch(Exception e3){
			e3.printStackTrace();
		}
		
		setRefCodeField("v.vdef6");
		setRefNameField("v.vdef6");
		setCacheEnabled(false);
	
	}
	
	private String getConfString() throws Exception{
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"+pkCorp+"' and u.cuserid = '"+pkUser+"' " 
						+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHSX' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		int retCot = (Integer)query.executeQuery(deptsql, new ColumnProcessor());

		if(0<retCot){
			return "";
		}else{
			return " and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=v.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=v.ht_dept))";
		}
	}
	
	
	
	
}
