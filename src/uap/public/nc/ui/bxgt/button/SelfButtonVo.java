package nc.ui.bxgt.button;

import nc.vo.trade.button.ButtonVO;

public class SelfButtonVo implements IBxgtButton {
	private static SelfButtonVo m_btnObj = null;

	private SelfButtonVo() {
	}

	public static SelfButtonVo getInstance() {
		if (m_btnObj == null) {
			m_btnObj = new SelfButtonVo();
		}
		return m_btnObj;
	}

	// 同步
	public ButtonVO getSynchroBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("同步");
		btnvo.setBtnNo(IBxgtButton.SYNCHRONOUS);
		btnvo.setHintStr("同步单据");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD });

		return btnvo;
	}

	// 批改
	public ButtonVO getBatchEditBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("批改");
		btnvo.setBtnNo(IBxgtButton.BATCH_EDIT);
		btnvo.setHintStr("单据批改");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});

		return btnvo;
	}

	// 预收款单同步
	public ButtonVO getPrePaymentBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("预收款单同步");
		btnvo.setBtnNo(IBxgtButton.PRE_PAYMENT);
		btnvo.setHintStr("同步预收款单");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}

	// 锁定
	public ButtonVO getLockBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("锁定");
		btnvo.setBtnNo(IBxgtButton.LOCK);
		btnvo.setHintStr("锁定单据");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}

	// 解锁
	public ButtonVO getUnLockBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("解锁");
		btnvo.setBtnNo(IBxgtButton.UNLOCK);
		btnvo.setHintStr("解锁单据");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}
	
//	 确认标志
	public ButtonVO getOkMark() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("确认");
		btnvo.setBtnNo(IBxgtButton.OKMARK);
		btnvo.setHintStr("确认标志");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}
	
	//取消确认标志
	public ButtonVO getCancelMark(){
		ButtonVO btn=new ButtonVO();
		btn.setBtnName("取消");
		btn.setBtnNo(IBxgtButton.CANCEL);
		btn.setHintStr("取消确认标志");
		return btn;
	}
	
	/**
	 * 标记按钮组
	 */
	public ButtonVO getMarkBtn(){
		ButtonVO groupBtnVO=new ButtonVO();
		groupBtnVO.setBtnName("标记");
		groupBtnVO.setHintStr("确认取消标志");
		groupBtnVO.setBtnNo(IBxgtButton.MARK_GROUP);
		return groupBtnVO;
	}


	/**
	 * 锁单组按钮
	 * 
	 * @return
	 */
	public ButtonVO getLockGroupBtn() {

		ButtonVO groupBtnVO = new ButtonVO();
		groupBtnVO.setBtnName("锁单");
		groupBtnVO.setHintStr("锁定单据");
		groupBtnVO.setBtnNo(IBxgtButton.LOCK_GROUP);
		return groupBtnVO;
	}
	
	/**
	 * 发票税率
	 */
	public ButtonVO getTaxBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("税率更改");
		btnVO.setHintStr("更改采购发票税率");
		btnVO.setBtnNo(IBxgtButton.TAX_RATE);
		return btnVO;
	}
	
	/**
	 * 客户金额修改
	 */
	public ButtonVO getCusMnyBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("客户金额更改");
		btnVO.setHintStr("更改广东商销售订单客户金额");
		btnVO.setBtnNo(IBxgtButton.CUST_MNY);
		return btnVO;
	}
	
	/**
	 * 删除单据
	 */
	public ButtonVO getDelBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("删除");
		btnVO.setHintStr("删除流程单据");
		btnVO.setBtnNo(IBxgtButton.DELBILL);
		return btnVO;
	}
	
	/**
	 * 参与排序
	 */
	public ButtonVO getOrderseqBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("确认排序");
		btnVO.setHintStr("确认参与单据号排序");
		btnVO.setBtnNo(IBxgtButton.ORDERSEQ);
		return btnVO;
	}
	
	/**
	 * 取消排序
	 */
	public ButtonVO getCancelOrderBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("取消排序");
		btnVO.setHintStr("取消参与单据号排序");
		btnVO.setBtnNo(IBxgtButton.CANCELORDER);
		return btnVO;
	}
	
	/**
	 * 排序按钮组
	 */
	public ButtonVO getOrderGrpBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("排序");
		btnVO.setHintStr("确认与取消排序");
		btnVO.setBtnNo(IBxgtButton.ORDERGROUP);
		return btnVO;
	}
	
}
