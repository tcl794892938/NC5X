package nc.ui.dahuan.conmodify.outside.dept;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"t.ctcode",
				"(select v.invname from bd_invbasdoc v where v.pk_invbasdoc = t.ctname) ctname","t.vdef6",
				"trim(to_char(round(t.dctjetotal,2),'9999999.99')) dctjetotal"
		});
		setFieldName(new String[] {"合同编号","合同名称","项目名称","合同金额"});
		setHiddenFieldCode(new String[] { "t.pk_contract" });
		setPkFieldCode("t.pk_contract");

		setOrderPart(" t.ctcode ");
		
		setTableName(" dh_contract t ");
		
		addWherePart(getConfString());
		
		setRefCodeField("t.ctcode");
		setRefNameField("t.ctcode");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		if(pkCorp.equals("1002")){//过滤溧阳
			return " and t.vapproveid = '"+pkUser+"' and t.pk_corp = '"+pkCorp+"' and nvl(t.is_relation,0) = 0 and nvl(t.dr,0)=0 and nvl(t.is_delivery,0)=0 " +
			" and t.is_seal = 1 and t.httype <> 2 " +
			" and not exists (select 1 from bd_jobmngfil m where m.pk_jobmngfil=t.pk_jobmandoc and nvl(m.dr,0)=0 and m.sealflag='Y')";
		}
		
//		权限更改(采购合同-工程部下属，销售合同-原部门主任)
		String sql="select count(1) from dh_fkgx where nvl(dr,0)=0 and pk_user1='"+pkUser+"' and pk_corp='"+pkCorp+"'";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer it=0;
		try {
			it=(Integer)iQ.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(it>0){//部门主任(销售合同权限)
			return " and t.vapproveid = '"+pkUser+"' and t.pk_corp = '"+pkCorp+"' and nvl(t.is_relation,0) = 0 and nvl(t.dr,0)=0 and nvl(t.is_delivery,0)=0 " +
			" and t.is_seal = 1 and t.httype=0  " +
			" and not exists (select 1 from bd_jobmngfil m where m.pk_jobmngfil=t.pk_jobmandoc and nvl(m.dr,0)=0 and m.sealflag='Y')";
		}
		
//		查询工程部pk
		String sql2="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
			"and pk_corp='"+pkCorp+"' and deptname='工程管理部' and canceled='N'";
		
		Object gc=null;
		try {
			gc=iQ.executeQuery(sql2, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		String sql3="select count(1) from dh_fkgx_d where nvl(dr,0)=0 and pk_dept_user='"+pkUser+"' " +
				"and pk_fkgx=(select pk_fkgx from dh_fkgx x where nvl(x.dr,0)=0 " +
				"and pk_deptdoc='"+gc+"' and pk_corp='"+pkCorp+"')";
		Integer it2=0;
		try {
			it2=(Integer)iQ.executeQuery(sql3, new ColumnProcessor());
		} catch (BusinessException e) {
		}
		
		if(it2>0){
			return " and t.pk_corp = '"+pkCorp+"' and nvl(t.is_relation,0) = 0 and nvl(t.dr,0)=0 and nvl(t.is_delivery,0)=0 " +
			" and t.is_seal = 1 and t.httype = 1 " +
			" and not exists (select 1 from bd_jobmngfil m where m.pk_jobmngfil=t.pk_jobmandoc and nvl(m.dr,0)=0 and m.sealflag='Y')";
		}
		
		/*return " and t.vapproveid = '"+pkUser+"' and t.pk_corp = '"+pkCorp+"' and nvl(t.is_relation,0) = 0 and nvl(t.dr,0)=0 and nvl(t.is_delivery,0)=0 " +
				" and t.is_seal = 1 and t.httype <> 2 " +
				" and not exists (select 1 from bd_jobmngfil m where m.pk_jobmngfil=t.pk_jobmandoc and nvl(m.dr,0)=0 and m.sealflag='Y')";*/
		
		return " and t.pk_contract='***'";
	}
	
	
	
	
}
