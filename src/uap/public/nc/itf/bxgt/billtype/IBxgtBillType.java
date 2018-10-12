package nc.itf.bxgt.billtype;

public interface IBxgtBillType {
	/** 销售出库单标识 */
	final static int SALE_OUT = 1;

	/** 销售订单标识 */
	final static int SALE_ORDER = 2;

	/** 库存所有标识 */
	final static int SALE_OUT_ALL = 3;

	/** 销售发票标识 */
	final static int SALE_INVOICE = 4;

	/** 产成品入库单标识 */
	final static int CCP_IN = 5;

	/** 收款单 */
	final static int BXGT_SK = 6;
	
	/**结算单*/
	final static int BXGT_JS = 7 ;
	
	/**货位 */
	final static int BXGT_HW = 8 ;
	
	/** 预收款单标识 */
	final static int ADV_REC = 9;
	
	/** 结算单标识 */
	final static int SET_BILL = 10;
	
	/** 采购订单标识 */
	final static int PURCHASE_ORDER = 11;
	
	/** 采购入库标识 */
	final static int PURCHASE_IN= 12;
	
	/** 采购发票标识 */
	final static int PURCHASE_INVOICE = 13;
	
	/**请购单标识**/
	final static int PRAY_BILL=14;
	
	
}
