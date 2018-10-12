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

	// ͬ��
	public ButtonVO getSynchroBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("ͬ��");
		btnvo.setBtnNo(IBxgtButton.SYNCHRONOUS);
		btnvo.setHintStr("ͬ������");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD });

		return btnvo;
	}

	// ����
	public ButtonVO getBatchEditBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("����");
		btnvo.setBtnNo(IBxgtButton.BATCH_EDIT);
		btnvo.setHintStr("��������");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});

		return btnvo;
	}

	// Ԥ�տͬ��
	public ButtonVO getPrePaymentBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("Ԥ�տͬ��");
		btnvo.setBtnNo(IBxgtButton.PRE_PAYMENT);
		btnvo.setHintStr("ͬ��Ԥ�տ");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}

	// ����
	public ButtonVO getLockBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("����");
		btnvo.setBtnNo(IBxgtButton.LOCK);
		btnvo.setHintStr("��������");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}

	// ����
	public ButtonVO getUnLockBtn() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("����");
		btnvo.setBtnNo(IBxgtButton.UNLOCK);
		btnvo.setHintStr("��������");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}
	
//	 ȷ�ϱ�־
	public ButtonVO getOkMark() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnName("ȷ��");
		btnvo.setBtnNo(IBxgtButton.OKMARK);
		btnvo.setHintStr("ȷ�ϱ�־");
		// btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
		// IBillOperate.OP_NOADD_NOTEDIT,IBillOperate.OP_ADD
		// ,IBillOperate.OP_EDIT});
		return btnvo;
	}
	
	//ȡ��ȷ�ϱ�־
	public ButtonVO getCancelMark(){
		ButtonVO btn=new ButtonVO();
		btn.setBtnName("ȡ��");
		btn.setBtnNo(IBxgtButton.CANCEL);
		btn.setHintStr("ȡ��ȷ�ϱ�־");
		return btn;
	}
	
	/**
	 * ��ǰ�ť��
	 */
	public ButtonVO getMarkBtn(){
		ButtonVO groupBtnVO=new ButtonVO();
		groupBtnVO.setBtnName("���");
		groupBtnVO.setHintStr("ȷ��ȡ����־");
		groupBtnVO.setBtnNo(IBxgtButton.MARK_GROUP);
		return groupBtnVO;
	}


	/**
	 * �����鰴ť
	 * 
	 * @return
	 */
	public ButtonVO getLockGroupBtn() {

		ButtonVO groupBtnVO = new ButtonVO();
		groupBtnVO.setBtnName("����");
		groupBtnVO.setHintStr("��������");
		groupBtnVO.setBtnNo(IBxgtButton.LOCK_GROUP);
		return groupBtnVO;
	}
	
	/**
	 * ��Ʊ˰��
	 */
	public ButtonVO getTaxBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("˰�ʸ���");
		btnVO.setHintStr("���Ĳɹ���Ʊ˰��");
		btnVO.setBtnNo(IBxgtButton.TAX_RATE);
		return btnVO;
	}
	
	/**
	 * �ͻ�����޸�
	 */
	public ButtonVO getCusMnyBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("�ͻ�������");
		btnVO.setHintStr("���Ĺ㶫�����۶����ͻ����");
		btnVO.setBtnNo(IBxgtButton.CUST_MNY);
		return btnVO;
	}
	
	/**
	 * ɾ������
	 */
	public ButtonVO getDelBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("ɾ��");
		btnVO.setHintStr("ɾ�����̵���");
		btnVO.setBtnNo(IBxgtButton.DELBILL);
		return btnVO;
	}
	
	/**
	 * ��������
	 */
	public ButtonVO getOrderseqBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("ȷ������");
		btnVO.setHintStr("ȷ�ϲ��뵥�ݺ�����");
		btnVO.setBtnNo(IBxgtButton.ORDERSEQ);
		return btnVO;
	}
	
	/**
	 * ȡ������
	 */
	public ButtonVO getCancelOrderBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("ȡ������");
		btnVO.setHintStr("ȡ�����뵥�ݺ�����");
		btnVO.setBtnNo(IBxgtButton.CANCELORDER);
		return btnVO;
	}
	
	/**
	 * ����ť��
	 */
	public ButtonVO getOrderGrpBtn(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnName("����");
		btnVO.setHintStr("ȷ����ȡ������");
		btnVO.setBtnNo(IBxgtButton.ORDERGROUP);
		return btnVO;
	}
	
}
