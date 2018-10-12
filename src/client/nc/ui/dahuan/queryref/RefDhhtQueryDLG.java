package nc.ui.dahuan.queryref;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class RefDhhtQueryDLG extends HYQueryDLG {

	private String strWhere = " 1=1 and isnull(dr,0) = 0 ";

	public RefDhhtQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();

		try {
			initData(pk_corp, operator);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public RefDhhtQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,
				nodeKey);
		init();

		try {
			initData(pk_corp, operator);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param businessType
	 * @param currentBillType
	 * @param sourceBilltype
	 * @param nodeKey
	 * @param userObj
	 * @throws Exception
	 */
	public void initData(String pkCorp, String operator) throws Exception {

	
		ClientEnvironment ce = ClientEnvironment.getInstance();
		// 过滤掉虚合同以及盖章的合同  过滤-00销售合同
		if(pkCorp.equals("1002")){
			strWhere = " nvl(dh_contract.httype,0) = 1 and nvl(dh_contract.is_seal,0) = 1 and dh_contract.dcaigtotal<>nvl(dh_contract.ljfkjhje,0)" +  //by tcl
			" and dh_contract.pk_deptdoc in (select v.pk_deptdoc from v_deptperonal v " +
			" where v.pk_user = '"+ce.getUser().getPrimaryKey()+"' and v.pk_corp = '"+ce.getCorporation().getPrimaryKey()+"') ";
		}else{
			strWhere = " nvl(dh_contract.httype,0) = 1 and nvl(dh_contract.is_seal,0) = 1 and dh_contract.dcaigtotal<>nvl(dh_contract.ljfkjhje,0)" ;  //by tcl
			
		}
		

	}

	private void init() {
		// TODO 自动生成方法存根
		setTempletID("1001AA1000000002QB4B");
		hideNormal();
	}

	public java.lang.String getWhereSql() {

		return getWhereSQL();
	}

	public String getWhereSQL() {
		String strwhere = super.getWhereSQL();
		return (strwhere == null ? "" : strwhere+" and ") + strWhere;
	}

}
