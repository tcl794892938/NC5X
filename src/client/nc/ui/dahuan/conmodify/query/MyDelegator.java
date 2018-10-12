package nc.ui.dahuan.conmodify.query;

import java.lang.reflect.Array;
import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

public class MyDelegator extends BDBusinessDelegator {

	@Override
	public SuperVO[] queryHeadAllData(Class headVoClass, String strBillType, String strWhere) throws Exception {
		
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"+pkCorp+"' and u.cuserid = '"+pkUser+"' " 
		+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHBG' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		
		int retCot = (Integer)iQ.executeQuery(deptsql, new ColumnProcessor());

		String conf = "";
		if(0<retCot){
			conf = "";
		}else{
			conf = " and exists (select 1 from dh_contract where vbg_contract.parent_contractid = dh_contract.pk_contract " +
					" and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=dh_contract.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=dh_contract.ht_dept)))";
		}
		
		String sql = "select * from vbg_contract where " + strWhere + conf;
		Collection result = (Collection)iQ.executeQuery(sql, new BeanListProcessor(headVoClass));
		
		return (SuperVO[])result.toArray((SuperVO[])Array.newInstance(headVoClass, 0));
	}

	@Override
	public CircularlyAccessibleValueObject[] queryBodyAllData(Class voClass, String billType, String key, String strWhere) throws Exception {
		
		String sql = "select * from vbg_contract_b where parent_contractid = '"+key+"' order by ts ";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection result = (Collection)iQ.executeQuery(sql, new BeanListProcessor(voClass));
		
		return (SuperVO[])result.toArray((SuperVO[])Array.newInstance(voClass, 0));
	}

	
	
}
