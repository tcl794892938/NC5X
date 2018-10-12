package nc.ui.dahuan.queryref;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class RefFksqQueryDLG extends HYQueryDLG {

	private String strWhere = " 1=1 and isnull(dr,0) = 0 ";

	public RefFksqQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();

		try {
			initData(pk_corp, operator);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public RefFksqQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
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
       //合同金额大于累计计划付款时，可以申请计划付款
		strWhere = " nvl(dfkje,0.00)- nvl(ljsfkje,0.00) > 0  ";

	}

	private void init() {
		// TODO 自动生成方法存根
		setTempletID("1001AA1000000002S3XM");
		hideNormal();
	}

	public java.lang.String getWhereSql() {

		return getWhereSQL();
	}

	public String getWhereSQL() {
		String strwhere = super.getWhereSQL();
		return (strwhere == null ? "" : strwhere) + strWhere;
	}

}
