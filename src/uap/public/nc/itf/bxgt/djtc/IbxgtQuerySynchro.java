package nc.itf.bxgt.djtc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import nc.vo.pub.lang.UFDouble;

public interface IbxgtQuerySynchro {
	/** 销售订单查询 * */
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOrder(
			String strWhere, String zt) throws Exception;

	/** 采购订单查询 * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseOrder(
			String strWhere, String zt) throws Exception;

	/** 销售出库单查询 * */
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOutOrder(
			String strWhere, String zt) throws Exception;

	/** 采购入库单查询 * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInOrder(
			String strWhere, String zt) throws Exception;

	/** 采购发票查询 * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInvoiceOrder(
			String strWhere, String zt) throws Exception;

	/** 材料出库单查询* */
	public Map<Integer, Map<String, Object>> bxgtQueryMatInOrder(
			String strWhere, String zt) throws Exception;

	/** 销售发票查询 * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQuerySaleInvoiceOrder(
	 * String strWhere, String zt) throws Exception;
	 */

	/** 到货单查询 * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQueryArriveOrder( String
	 * strWhere, String zt) throws Exception;
	 */

	/** 发货单查询 * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQueryDeliveryOrder( String
	 * strWhere, String zt) throws Exception;
	 */

	/** 预收款单同步 * */
	// public boolean bxgtSynchroAdvanceReceive(String[]
	// pkPurchaseInvoiceOrders) throws Exception;
	/** 结算单同步 * */
	// public boolean bxgtSynchroSettle(String[] pkSettles) throws Exception;
	/** 发货单同步 * */
	/*
	 * public boolean bxgtSynchroDeliveryOrder(String[] pkDeliveryOrders) throws
	 * Exception;
	 */

	/** 销售发票同步 * */
	/*
	 * public boolean bxgtSynchroSaleInvoiceOrder(String[] pkSaleInvoiceOrders)
	 * throws Exception;
	 */

	/** 到货单同步 * */
	/*
	 * public boolean bxgtSynchroArriveOrder(String[] pkArriveOrders) throws
	 * Exception;
	 */

	/** 同步收款单 */
	// public void bxgtSynchroSK(String[] vouchids) throws Exception;
	/** 查询销售订单下游销售出库单据 */
	/*
	 * public String[] bxgtQuerySaleOutBySaleOrders(String[] cSourcePkHid)
	 * throws Exception;
	 */

	/** 同步销售执行表 */
	/*
	 * public void bxgtSynchroSaleExecute(String[] pkSaleOutorders) throws
	 * Exception;
	 */

	/** 产成品入库单同步 */
	// public void bxgtSynchroProducIn(String[] pkSaleOutOrders) throws
	// Exception;
	/** 锁定销售出库单 */
	/* public void bxgtLockSaleOut(String[] pkSaleOut) throws Exception; */
	/** 锁定预收款单 */
	/* public void bxgtLockSK(String[] vouchid) throws Exception ; */

	/** 同步结算单 */
	// public void bxgtSynchroSetlle(String[] pk_settle) throws Exception ;
	/** 采购相关订单删除* */
	// public void deletePurchaseBill(String[] billPks,int billTypeFlag) throws
	// Exception;
	/** 向B账单插入数据时先进行删除 */
	// public void deleteBill(String[] billPks,int billTypeFlag) throws
	// Exception ;
	/** 请购单同步* */
	// public void bxgtSynchroPraybill(String[] pkPraybills) throws Exception;
	/** 销售订单同步 * */
	public boolean bxgtSynchroSaleOrder(String[] pkSaleOrders) throws Exception;

	/** 销售出库单同步 * */
	public boolean bxgtSynchroSaleOutOrder(String[] pkSaleOutOrders)
			throws Exception;

	/** 采购订单同步 * */
	public boolean bxgtSynchroPurchaseOrder(String[] pkPurchaseOrders)
			throws Exception;

	/** 采购入库单同步 * */
	public boolean bxgtSynchroPurInOrder(String[] pkPurchaseInOrders)
			throws Exception;

	/** 采购发票同步 * */
	public boolean bxgtSynchroPurInvoiceOrder(String[] pkPurchaseInvoiceOrders)
			throws Exception;

	/** 材料出库同步* */
	public ArrayList<Object[]> bxgtSynMaterialOrder(String[] pkMaterials) throws Exception;

	/** 出入库单附表-货位 */
	public void bxgtSynchroSpace(String[] pkSaleOutOrders) throws Exception;

	/** 出入库单附表-累计结算 */
	public void bxgtSynchroAccount(String[] pkSaleOutOrders) throws Exception;

	/** 批改的销售订单 */
	public void bxgtBatchUpdateSaleOrder(String[] pkSaleOrders, int days)
			throws Exception;

	/** 批改的采购订单* */
	public ArrayList<Object[]> bxgtBatchUpdatePurchaseOrder(String[] pkPurchaseOrders, int days)
			throws Exception;

	/** 批改的材料出库* */
	public ArrayList<Object[]> bxgtBatchUpdateMaterialOrder(String[] pkMaterialOrders, int days)
			throws Exception;

	/** 锁定销售订单 */
	public String[] bxgtLockSaleOrders(String[] pkSaleOrders) throws Exception;

	/** 解锁销售订单 */
	public String bxgtUnLockSaleOrders(String[] pkSaleOrders) throws Exception;

	/** 锁定采购订单* */
	public String[] bxgtLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception;

	/** 解锁采购订单* */
	public String bxgtUnLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception;

	/** 锁定材料出库* */
	public String bxgtLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception;

	/** 解锁材料出库* */
	public String bxgtUnLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception;

	/** 获得同步预收款单所需pk * */
	public void getNeedPks() throws Exception;

	/** 确认单据操作* */
	public void confirmBill(String[] pks, String billtype) throws Exception;
	/** 取消单据操作* */
	public void unConfirmBill(String[] pks, String billtype) throws Exception;
	/** 单据排序操作* */
	public void orderSeqBill(String[] pks, String billtype) throws Exception;
	/** 取消单据排序操作* */
	public void unOrderSeqBill(String[] pks, String billtype) throws Exception;
	/** 删除单据操作* */
	public void deleteSaleOrOrder(String[] pks, String billtype) throws Exception;
	/** 采购发票税率更改* */
	public void batchInvoiceTaxRate(String[] pks, UFDouble taxRate,Integer it) throws Exception;
	
	/**广东销售客户金额修改**/
	public void batchCustAndMoney(String[] pks,Object cust,Object money)throws Exception;
	
	/**基本档案同步**/
	public LinkedHashMap<String, Object> synBaseDoc()throws Exception;//暂未使用
	
	public void updateBasedoc(String lastts,String currts) throws Exception;
	
	/**同步开票客商**/
	public int synCustBasedoc(String[] custcode,String billtime)throws Exception; 
	
}
