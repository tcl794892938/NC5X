package nc.itf.bxgt.djtc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import nc.vo.pub.lang.UFDouble;

public interface IbxgtQuerySynchro {
	/** ���۶�����ѯ * */
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOrder(
			String strWhere, String zt) throws Exception;

	/** �ɹ�������ѯ * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseOrder(
			String strWhere, String zt) throws Exception;

	/** ���۳��ⵥ��ѯ * */
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOutOrder(
			String strWhere, String zt) throws Exception;

	/** �ɹ���ⵥ��ѯ * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInOrder(
			String strWhere, String zt) throws Exception;

	/** �ɹ���Ʊ��ѯ * */
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInvoiceOrder(
			String strWhere, String zt) throws Exception;

	/** ���ϳ��ⵥ��ѯ* */
	public Map<Integer, Map<String, Object>> bxgtQueryMatInOrder(
			String strWhere, String zt) throws Exception;

	/** ���۷�Ʊ��ѯ * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQuerySaleInvoiceOrder(
	 * String strWhere, String zt) throws Exception;
	 */

	/** ��������ѯ * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQueryArriveOrder( String
	 * strWhere, String zt) throws Exception;
	 */

	/** ��������ѯ * */
	/*
	 * public Map<Integer, Map<String, Object>> bxgtQueryDeliveryOrder( String
	 * strWhere, String zt) throws Exception;
	 */

	/** Ԥ�տͬ�� * */
	// public boolean bxgtSynchroAdvanceReceive(String[]
	// pkPurchaseInvoiceOrders) throws Exception;
	/** ���㵥ͬ�� * */
	// public boolean bxgtSynchroSettle(String[] pkSettles) throws Exception;
	/** ������ͬ�� * */
	/*
	 * public boolean bxgtSynchroDeliveryOrder(String[] pkDeliveryOrders) throws
	 * Exception;
	 */

	/** ���۷�Ʊͬ�� * */
	/*
	 * public boolean bxgtSynchroSaleInvoiceOrder(String[] pkSaleInvoiceOrders)
	 * throws Exception;
	 */

	/** ������ͬ�� * */
	/*
	 * public boolean bxgtSynchroArriveOrder(String[] pkArriveOrders) throws
	 * Exception;
	 */

	/** ͬ���տ */
	// public void bxgtSynchroSK(String[] vouchids) throws Exception;
	/** ��ѯ���۶����������۳��ⵥ�� */
	/*
	 * public String[] bxgtQuerySaleOutBySaleOrders(String[] cSourcePkHid)
	 * throws Exception;
	 */

	/** ͬ������ִ�б� */
	/*
	 * public void bxgtSynchroSaleExecute(String[] pkSaleOutorders) throws
	 * Exception;
	 */

	/** ����Ʒ��ⵥͬ�� */
	// public void bxgtSynchroProducIn(String[] pkSaleOutOrders) throws
	// Exception;
	/** �������۳��ⵥ */
	/* public void bxgtLockSaleOut(String[] pkSaleOut) throws Exception; */
	/** ����Ԥ�տ */
	/* public void bxgtLockSK(String[] vouchid) throws Exception ; */

	/** ͬ�����㵥 */
	// public void bxgtSynchroSetlle(String[] pk_settle) throws Exception ;
	/** �ɹ���ض���ɾ��* */
	// public void deletePurchaseBill(String[] billPks,int billTypeFlag) throws
	// Exception;
	/** ��B�˵���������ʱ�Ƚ���ɾ�� */
	// public void deleteBill(String[] billPks,int billTypeFlag) throws
	// Exception ;
	/** �빺��ͬ��* */
	// public void bxgtSynchroPraybill(String[] pkPraybills) throws Exception;
	/** ���۶���ͬ�� * */
	public boolean bxgtSynchroSaleOrder(String[] pkSaleOrders) throws Exception;

	/** ���۳��ⵥͬ�� * */
	public boolean bxgtSynchroSaleOutOrder(String[] pkSaleOutOrders)
			throws Exception;

	/** �ɹ�����ͬ�� * */
	public boolean bxgtSynchroPurchaseOrder(String[] pkPurchaseOrders)
			throws Exception;

	/** �ɹ���ⵥͬ�� * */
	public boolean bxgtSynchroPurInOrder(String[] pkPurchaseInOrders)
			throws Exception;

	/** �ɹ���Ʊͬ�� * */
	public boolean bxgtSynchroPurInvoiceOrder(String[] pkPurchaseInvoiceOrders)
			throws Exception;

	/** ���ϳ���ͬ��* */
	public ArrayList<Object[]> bxgtSynMaterialOrder(String[] pkMaterials) throws Exception;

	/** ����ⵥ����-��λ */
	public void bxgtSynchroSpace(String[] pkSaleOutOrders) throws Exception;

	/** ����ⵥ����-�ۼƽ��� */
	public void bxgtSynchroAccount(String[] pkSaleOutOrders) throws Exception;

	/** ���ĵ����۶��� */
	public void bxgtBatchUpdateSaleOrder(String[] pkSaleOrders, int days)
			throws Exception;

	/** ���ĵĲɹ�����* */
	public ArrayList<Object[]> bxgtBatchUpdatePurchaseOrder(String[] pkPurchaseOrders, int days)
			throws Exception;

	/** ���ĵĲ��ϳ���* */
	public ArrayList<Object[]> bxgtBatchUpdateMaterialOrder(String[] pkMaterialOrders, int days)
			throws Exception;

	/** �������۶��� */
	public String[] bxgtLockSaleOrders(String[] pkSaleOrders) throws Exception;

	/** �������۶��� */
	public String bxgtUnLockSaleOrders(String[] pkSaleOrders) throws Exception;

	/** �����ɹ�����* */
	public String[] bxgtLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception;

	/** �����ɹ�����* */
	public String bxgtUnLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception;

	/** �������ϳ���* */
	public String bxgtLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception;

	/** �������ϳ���* */
	public String bxgtUnLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception;

	/** ���ͬ��Ԥ�տ����pk * */
	public void getNeedPks() throws Exception;

	/** ȷ�ϵ��ݲ���* */
	public void confirmBill(String[] pks, String billtype) throws Exception;
	/** ȡ�����ݲ���* */
	public void unConfirmBill(String[] pks, String billtype) throws Exception;
	/** �����������* */
	public void orderSeqBill(String[] pks, String billtype) throws Exception;
	/** ȡ�������������* */
	public void unOrderSeqBill(String[] pks, String billtype) throws Exception;
	/** ɾ�����ݲ���* */
	public void deleteSaleOrOrder(String[] pks, String billtype) throws Exception;
	/** �ɹ���Ʊ˰�ʸ���* */
	public void batchInvoiceTaxRate(String[] pks, UFDouble taxRate,Integer it) throws Exception;
	
	/**�㶫���ۿͻ�����޸�**/
	public void batchCustAndMoney(String[] pks,Object cust,Object money)throws Exception;
	
	/**��������ͬ��**/
	public LinkedHashMap<String, Object> synBaseDoc()throws Exception;//��δʹ��
	
	public void updateBasedoc(String lastts,String currts) throws Exception;
	
	/**ͬ����Ʊ����**/
	public int synCustBasedoc(String[] custcode,String billtime)throws Exception; 
	
}
