package nc.ui.bxgt.button;

public interface IBxgtButton {
	/** 同步 */
	public static final int SYNCHRONOUS = 200;

	/** 批改 */
	public static final int BATCH_EDIT = 201;

	/** 预收款单同步 */
	public static final int PRE_PAYMENT = 202;

	/** 锁定 */
	public static final int LOCK = 203;

	/** 解锁 */
	public static final int UNLOCK = 204;

	/** 锁单按钮组 */
	public static final int LOCK_GROUP = 205;

	/** 确认标志* */
	public static final int OKMARK = 206;

	/** 取消确认 * */
	public static final int CANCEL = 207;

	/** 确认取消标志 * */
	public static final int MARK_GROUP = 208;

	/** 删除* */
	public static final int DELBILL = 215;

	/** 修改销售订单客户金额* */
	public static final int CUST_MNY = 209;

	/** 单据号排序* */
	public static final int GEN_BILLNO = 210;

	/** 采购发票税率调整* */
	public static final int TAX_RATE = 211;

	/** 基本档案同步* */
	public static final int BASE_SYN = 212;

	/** 客商模版下载* */
	public static final int CUST_DOWN = 213;

	/** 客户档案同步* */
	public static final int CUST_SYN = 214;

	/** 导出EXCEL（打印）* */
	public static final int PRINT_EXCEL = 216;

	/** 一键同步客商* */
	public static final int AUTO_SYN = 217;
	
	/** 参与排序* */
	public static final int ORDERSEQ = 218;

	/** 解除排序 * */
	public static final int CANCELORDER = 219;

	/** 排序按钮组 * */
	public static final int ORDERGROUP = 220;
}
