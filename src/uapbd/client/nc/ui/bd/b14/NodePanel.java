package nc.ui.bd.b14;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UILabelLayout;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b14.InvclVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
/**
 * �˴���������˵����
 * �������ڣ�(2001-05-30 10:04:51)
 * @author��ף��
 */
public class NodePanel extends nc.ui.pub.beans.UIPanel {
	private nc.ui.pub.beans.UITextField ivjCodeInput = null;
	private nc.ui.pub.beans.UILabel ivjCodeLabel = null;
	private nc.ui.pub.beans.UITextField ivjNameInput = null;
	private nc.ui.pub.beans.UILabel ivjNameLabel = null;
	private nc.ui.pub.beans.UITextField ivjPriceInput = null;
	private nc.ui.pub.beans.UILabel ivjPriceLabel = null;
	private nc.ui.pub.beans.UILabel ivjRuleLabel = null;
	private InvclVO m_parent = null;
	private InvclVO m_me = null;
	private int m_statu=0;
	private nc.ui.pub.beans.UITextField ivjAvgMMInput = null;
	private nc.ui.pub.beans.UILabel ivjAvgMMLabel = null;
	private nc.ui.pub.beans.UITextField ivjAvgPurInput = null;
	private nc.ui.pub.beans.UILabel ivjAvgPurLabel = null;
	private nc.ui.pub.beans.UITextField ivjCostInput = null;
	private nc.ui.pub.beans.UILabel ivjCostLabel = null;
	private nc.ui.pub.beans.UICheckBox ivjUIChkSealdate = null;
	private nc.ui.pub.beans.UILabel ivjUIlbSealdate = null;
	private nc.ui.pub.beans.UITextField ivjForinvnameInput = null;
	private nc.ui.pub.beans.UILabel ivjForinvnameLabel = null;
	//Ϊ��ƥ�� uiLableLayout,���ӣ�nouse
	private nc.ui.pub.beans.UITextField CodeRule = null;
/**
 * NodePanel ������ע�⡣
 */
public NodePanel() {
	super();
	initialize();
}
/**
 * NodePanel ������ע�⡣
 * @param p0 java.awt.LayoutManager
 */
public NodePanel(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * NodePanel ������ע�⡣
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public NodePanel(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * NodePanel ������ע�⡣
 * @param p0 boolean
 */
public NodePanel(boolean p0) {
	super(p0);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-05-30 15:11:04)
 * @param curNode nc.vo.bd.b14.InvclVO
 * @exception nc.vo.pub.BusinessException �쳣˵����
 */
public void checkVO(InvclVO curNode) throws nc.vo.pub.BusinessException {
	StringBuffer error = new StringBuffer();
	
	try
	{
		if (!LocalModel
			.isRelationOK(
				this.m_parent == null ? null : this.m_parent.getInvclasscode(),
				curNode.getInvclasscode(),
				curNode.getInvclasslev().intValue()))
		{
			error.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000015")/*@res "������벻����Ҫ��"*/+"\r\n");
		}
	}
	catch (Exception e)
	{
	}
	if (curNode.getInvclassname() == null
		|| curNode.getInvclassname().length() == 0)
	{
		error.append(MultiLangTrans.getTransStr("MC3",new String[]{"["+nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0000563")+"]"/*@res "��������"*/})+"\r\n");
	}
	if (curNode.getAvgprice() != null)
	{
		if (curNode.getAvgprice().doubleValue() < 0)
			error.append(MultiLangTrans.getTransStr("MC9",new String[]{"["+nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0001775")+"]"/*@res "ƽ������"*/,"0"})+"\r\n");
	}
	if (curNode.getAveragecost() != null)
	{
		if (curNode.getAveragecost().doubleValue() <0)
			error.append(MultiLangTrans.getTransStr("MC9",new String[]{"["+nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000007")+"]"/*@res "ƽ���ɱ�"*/,"0"})+"\r\n");
	}
	if (curNode.getAveragepurahead() != null)
	{
		if (curNode.getAveragepurahead().intValue() < 0)
			error.append(MultiLangTrans.getTransStr("MC9",new String[]{"["+nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000008")+"]"/*@res "ƽ���ɹ���ǰ��"*/,"0"})+"\r\n");
	}
	if (curNode.getAveragemmahead() != null)
	{
		if (curNode.getAveragemmahead().intValue() < 0)
			error.append(MultiLangTrans.getTransStr("MC9",new String[]{"["+nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000009")+"]"/*@res "ƽ��������ǰ��"*/,"0"})+"\r\n");
	}
	String code = curNode.getInvclasscode();
	if (code!=null && code.length()!=code.getBytes().length){
		error.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000018")/*@res "��������в������뺺��"*/);
	}
	
	if (error.length() != 0)
		throw new nc.vo.pub.BusinessException(error.toString());
}
/**
 * ���� AvgMMInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getAvgMMInput() {
	if (ivjAvgMMInput == null) {
		try {
			ivjAvgMMInput = new nc.ui.pub.beans.UITextField();
			ivjAvgMMInput.setName("AvgMMInput");
//			ivjAvgMMInput.setBounds(137, 217, 247, 20);
			ivjAvgMMInput.setPreferredSize(new Dimension(246, 20));
			ivjAvgMMInput.setTextType("TextInt");
			// user code begin {1}
			ivjAvgMMInput.setMinValue(0);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAvgMMInput;
}
/**
 * ���� AvgMMLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getAvgMMLabel() {
	if (ivjAvgMMLabel == null) {
		try {
			ivjAvgMMLabel = new nc.ui.pub.beans.UILabel();
			ivjAvgMMLabel.setName("AvgMMLabel");
			ivjAvgMMLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000009")/*@res "ƽ��������ǰ��"*/);
			ivjAvgMMLabel.setBounds(29, 215, 94, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAvgMMLabel;
}
/**
 * ���� AvgPurInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getAvgPurInput() {
	if (ivjAvgPurInput == null) {
		try {
			ivjAvgPurInput = new nc.ui.pub.beans.UITextField();
			ivjAvgPurInput.setName("AvgPurInput");
//			ivjAvgPurInput.setBounds(137, 182, 246, 20);
			ivjAvgPurInput.setPreferredSize(new Dimension(246,20));
			ivjAvgPurInput.setTextType("TextInt");
			ivjAvgPurInput.setNumPoint(0);
			// user code begin {1}
			ivjAvgPurInput.setMinValue(0);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAvgPurInput;
}
/**
 * ���� AvgPurLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getAvgPurLabel() {
	if (ivjAvgPurLabel == null) {
		try {
			ivjAvgPurLabel = new nc.ui.pub.beans.UILabel();
			ivjAvgPurLabel.setName("AvgPurLabel");
//			ivjAvgPurLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000008")/*@res "ƽ���ɹ���ǰ��"*/);
			ivjAvgPurLabel.setText("�Ƿ�����տ�");
			ivjAvgPurLabel.setBounds(29, 181, 97, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAvgPurLabel;
}
/**
 * ���� CodeInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getCodeInput() {
	if (ivjCodeInput == null) {
		try {
			ivjCodeInput = new nc.ui.pub.beans.UITextField();
			ivjCodeInput.setName("CodeInput");
//		ivjCodeInput.setBounds(137, 53, 243, 20);
			ivjCodeInput.setPreferredSize(new Dimension(246,20));
			ivjCodeInput.setMaxLength(12);
			// user code begin {1}
			ivjCodeInput.setMaxLength(42);
			ivjCodeInput.setSelectallWhenFocus(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCodeInput;
}
/**
 * ���� CodeLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getCodeLabel() {
	if (ivjCodeLabel == null) {
		try {
			ivjCodeLabel = new nc.ui.pub.beans.UILabel();
			ivjCodeLabel.setName("CodeLabel");
			ivjCodeLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0000567")/*@res "�������"*/);
			ivjCodeLabel.setBounds(30, 50, 52, 22);
			ivjCodeLabel.setILabelType(5/** �����*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCodeLabel;
}
/**
 * ���� CostInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getCostInput() {
	if (ivjCostInput == null) {
		try {
			ivjCostInput = new nc.ui.pub.beans.UITextField();
			ivjCostInput.setName("CostInput");
//			ivjCostInput.setBounds(137, 150, 246, 20);
			ivjCostInput.setPreferredSize(new Dimension(246,20));
			ivjCostInput.setTextType("TextDbl");
			ivjCostInput.setMaxLength(21);
			ivjCostInput.setNumPoint(8);
			// user code begin {1}
			ivjCostInput.setMinValue(0.0);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCostInput;
}
/**
 * ���� CostLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getCostLabel() {
	if (ivjCostLabel == null) {
		try {
			ivjCostLabel = new nc.ui.pub.beans.UILabel();
			ivjCostLabel.setName("CostLabel");
			ivjCostLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000007")/*@res "ƽ���ɱ�"*/);
			ivjCostLabel.setLocation(30, 144);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCostLabel;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-05-30 14:32:05)
 * @return nc.vo.bd.b14.InvclVO
 */
public InvclVO getCurEditNode() throws Exception {
	m_me.setInvclasscode(this.getCodeInput().getText().trim());
	m_me.setInvclassname(this.getNameInput().getText().trim());
	m_me.setForinvname(this.getForinvnameInput().getText().trim());
	String tmp = this.getPriceInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAvgprice(null);
	else
		m_me.setAvgprice(new UFDouble(tmp));
	tmp = this.getCostInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragecost(null);
	else
		m_me.setAveragecost(new UFDouble(tmp));
	tmp = this.getAvgPurInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragepurahead(null);
	else
		m_me.setAveragepurahead(new Integer(tmp));
	tmp = this.getAvgMMInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragemmahead(null);
	else
		m_me.setAveragemmahead(new Integer(tmp));
	if (getUIChkSealdate().isSelected())
	{
		m_me.setSealdate(nc.ui.pub.ClientEnvironment.getInstance().getBusinessDate().toString());
	}
	else
	{
	   m_me.setSealdate(null);
	}
	checkVO(m_me);
	return m_me;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-05-30 14:32:05)
 * @return nc.vo.bd.b14.InvclVO
 */
public InvclVO getCurNewNode() throws Exception {
	if (m_me == null)
		m_me = new InvclVO();
	m_me.setInvclasscode(this.getCodeInput().getText().trim());
	m_me.setInvclassname(this.getNameInput().getText().trim());
	m_me.setForinvname(this.getForinvnameInput().getText().trim());
	String tmp = this.getPriceInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAvgprice(null);
	else
		m_me.setAvgprice(new UFDouble(tmp));
	tmp = this.getCostInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragecost(null);
	else
		m_me.setAveragecost(new UFDouble(tmp));
	tmp = this.getAvgPurInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragepurahead(null);
	else
		m_me.setAveragepurahead(new Integer(tmp));
	tmp = this.getAvgMMInput().getText();
	if (tmp == null || tmp.trim().length() == 0)
		m_me.setAveragemmahead(null);
	else
		m_me.setAveragemmahead(new Integer(tmp));
	m_me.setEndflag(new UFBoolean("Y"));
	m_me.setInvclasslev(new Integer(m_parent == null ? 1 : m_parent.getInvclasslev().intValue() + 1));

	if (getUIChkSealdate().isSelected())
	{
		m_me.setSealdate(nc.ui.pub.ClientEnvironment.getInstance().getBusinessDate().toString());
	}else
	{
	   m_me.setSealdate(null);
	}

	checkVO(m_me);
	return m_me;
}
/**
 * ���� NameInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getNameInput() {
	if (ivjNameInput == null) {
		try {
			ivjNameInput = new nc.ui.pub.beans.UITextField();
//			ivjNameInput.setName("NameInput");
			ivjNameInput.setBounds(137, 84, 244, 20);
			ivjNameInput.setPreferredSize(new Dimension(246,20));
			ivjNameInput.setMaxLength(40);
			// user code begin {1}
			ivjNameInput.setMaxLength(240);
			ivjNameInput.setSelectallWhenFocus(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNameInput;
}
/**
 * ���� NameLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getNameLabel() {
	if (ivjNameLabel == null) {
		try {
			ivjNameLabel = new nc.ui.pub.beans.UILabel();
			ivjNameLabel.setName("NameLabel");
			ivjNameLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0000563")/*@res "��������"*/);
			ivjNameLabel.setLocation(30, 79);
			ivjNameLabel.setILabelType(5/** �����*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNameLabel;
}
/**
 * ���� PriceInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getPriceInput() {
	if (ivjPriceInput == null) {
		try {
			ivjPriceInput = new nc.ui.pub.beans.UITextField();
			ivjPriceInput.setName("PriceInput");
//			ivjPriceInput.setBounds(137, 115, 245, 20);
			ivjPriceInput.setPreferredSize(new Dimension(246,20));
			ivjPriceInput.setTextType("TextDbl");
			ivjPriceInput.setMaxLength(21);
			ivjPriceInput.setNumPoint(8);
			// user code begin {1}
			ivjPriceInput.setMaxLength(17);
			ivjPriceInput.setNumPoint(4);
			ivjPriceInput.setMinValue(0.0);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPriceInput;
}
/**
 * ���� PriceLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getPriceLabel() {
	if (ivjPriceLabel == null) {
		try {
			ivjPriceLabel = new nc.ui.pub.beans.UILabel();
			ivjPriceLabel.setName("PriceLabel");
			ivjPriceLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0001775")/*@res "ƽ������"*/);
			ivjPriceLabel.setLocation(30, 113);
			ivjPriceLabel.setILabelType(1/** �����*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPriceLabel;
}
/**
 * ���� RuleLabel ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getRuleLabel() {
	if (ivjRuleLabel == null) {
		try {
			ivjRuleLabel = new nc.ui.pub.beans.UILabel();
			ivjRuleLabel.setName("RuleLabel");
			ivjRuleLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000016")/*@res "�������"*/);
			ivjRuleLabel.setBounds(32, 19, 297, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRuleLabel;
}
/**
 * ���� UIChkSealdate ����ֵ��
 * @return nc.ui.pub.beans.UICheckBox
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UICheckBox getUIChkSealdate() {
	if (ivjUIChkSealdate == null) {
		try {
			ivjUIChkSealdate = new nc.ui.pub.beans.UICheckBox();
			ivjUIChkSealdate.setName("UIChkSealdate");
			ivjUIChkSealdate.setBounds(138, 245, 18, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIChkSealdate;
}
/**
 * ���� UIlbSealdate ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getUIlbSealdate() {
	if (ivjUIlbSealdate == null) {
		try {
			ivjUIlbSealdate = new nc.ui.pub.beans.UILabel();
			ivjUIlbSealdate.setName("UIlbSealdate");
			ivjUIlbSealdate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000017")/*@res "��� "*/);
			ivjUIlbSealdate.setLocation(31, 240);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIlbSealdate;
}
private nc.ui.pub.beans.UITextField getForinvnameInput() {
	if (ivjForinvnameInput == null) {
		try {
			ivjForinvnameInput = new nc.ui.pub.beans.UITextField();
			ivjForinvnameInput.setBounds(137, 84, 244, 20);
			ivjForinvnameInput.setPreferredSize(new Dimension(246,20));
			ivjForinvnameInput.setMaxLength(40);
			// user code begin {1}
			ivjForinvnameInput.setMaxLength(240);
			ivjForinvnameInput.setSelectallWhenFocus(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForinvnameInput;
}
private nc.ui.pub.beans.UILabel getForinvnameLabel() {
	if (ivjForinvnameLabel == null) {
		try {
			ivjForinvnameLabel = new nc.ui.pub.beans.UILabel();
			ivjForinvnameLabel.setName("ForinvnameLabel");
			ivjForinvnameLabel.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001385")/*��������*/);
			ivjForinvnameLabel.setLocation(30, 113);
			ivjForinvnameLabel.setILabelType(1/** �����*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForinvnameLabel;
}

/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	System.out.println(exception);
	exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("NodePanel");
//		setLayout(null);
		setLayout(new UILabelLayout());
		setSize(574, 313);
		add(getRuleLabel(), getRuleLabel().getName());
		add(getCodeRule(), getCodeRule().getName()); //Ϊ��ƥ��UILabelLayout()�ã���������;
		add(getCodeLabel(), getCodeLabel().getName());
		add(getCodeInput(), getCodeInput().getName());
		add(getNameLabel(), getNameLabel().getName());
		add(getNameInput(), getNameInput().getName());
		add(getForinvnameLabel(), getForinvnameLabel().getName());
		add(getForinvnameInput(), getForinvnameInput().getName());
		add(getPriceLabel(), getPriceLabel().getName());
		add(getPriceInput(), getPriceInput().getName());
		add(getCostLabel(), getCostLabel().getName());
		add(getCostInput(), getCostInput().getName());
		add(getAvgPurLabel(), getAvgPurLabel().getName());
		add(getAvgPurInput(), getAvgPurInput().getName());
		add(getAvgMMLabel(), getAvgMMLabel().getName());
		add(getAvgMMInput(), getAvgMMInput().getName());
		add(getUIChkSealdate(), getUIChkSealdate().getName());
		add(getUIlbSealdate(), getUIlbSealdate().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	switchStatu(0);
	try
	{
	StringBuffer tag = new StringBuffer(getRuleLabel().getText());
	tag.append("    ");
	for (int i = 0; i < LocalModel.getCodeRule().length; i++)
	{
		for (int j = 0; j < LocalModel.getCodeRule()[i]; j++)
		{
			tag.append("X");
		}
		if (i != LocalModel.getCodeRule().length - 1)
			tag.append(" -- ");
	}
	getRuleLabel().setText(tag.toString());
	}catch(Exception e)
	{

	}
	// user code end
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		NodePanel aNodePanel;
		aNodePanel = new NodePanel();
		frame.setContentPane(aNodePanel);
		frame.setSize(aNodePanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIPanel �� main() �з����쳣");
		exception.printStackTrace(System.out);
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-05-30 13:12:22)
 * @param node nc.vo.bd.b14.InvclVO
 */
public void setNode(InvclVO parent, InvclVO me)
{
	if (parent == null)
		m_parent = null;
	else
		m_parent = (InvclVO) parent.clone();
	if (me == null)
		m_me = null;
	else
		m_me = (InvclVO) me.clone();
	if (parent != null)
	{
		try
		{
			getCodeInput().setFixText(
				parent.getInvclasscode(),
				LocalModel.getMustCodeLenOfLevel(parent.getInvclasslev().intValue()));
		}
		catch (Exception e)
		{
		}
		//setText(parent.getInvclasscode());
		getNameInput().setText(parent.getInvclassname());
		getForinvnameInput().setText(parent.getForinvname());
		Object tmp = parent.getAvgprice();
		if (tmp == null)
			tmp = "";
		else
			getPriceInput().setText(tmp.toString());
		tmp = parent.getAveragecost();
		if (tmp == null)
			tmp = "";
		else
			getCostInput().setText(tmp.toString());
		tmp = parent.getAveragepurahead();
		if (tmp == null)
			tmp = "";
		else
			getAvgPurInput().setText(tmp.toString());
		tmp = parent.getAveragemmahead();
		if (tmp == null)
			tmp = "";
		getAvgMMInput().setText(tmp.toString());
	}
	if (me != null)
	{
		try
		{
			getCodeInput().setFixText(
				me.getInvclasscode(),
				LocalModel.getMustCodeLenOfLevel(me.getInvclasslev().intValue() - 1));
		}
		catch (Exception e)
		{
		}
		//setText(me.getInvclasscode());
		getNameInput().setText(me.getInvclassname());
		getForinvnameInput().setText(me.getForinvname());
		Object tmp = me.getAvgprice();
		if (tmp == null)
			tmp = "";
		getPriceInput().setText(tmp.toString());
		tmp = me.getAveragecost();
		if (tmp == null)
			tmp = "";
		getCostInput().setText(tmp.toString());
		tmp = me.getAveragepurahead();
		if (tmp == null)
			tmp = "";
		getAvgPurInput().setText(tmp.toString());
		tmp = me.getAveragemmahead();
		if (tmp == null)
			tmp = "";
		getAvgMMInput().setText(tmp.toString());
		if (m_statu == 2)
		{
			getCodeInput().setEditable(false);
		}
		getUIChkSealdate().setSelected(me.getSealdate()!=null?true:false);

	}
	if (parent == null && me == null)
	{
		getCodeInput().setText("");
		getNameInput().setText("");
		getForinvnameInput().setText("");
		getPriceInput().setText("");
		getCostInput().setText("");
		getAvgPurInput().setText("");
		getAvgMMInput().setText("");
		getUIChkSealdate().setSelected(false);

	}

}
/**
 * �л�����Ԫ�ر༭״̬
 * �������ڣ�(2001-05-30 10:55:58)
 * @param statu int
 */
public void switchStatu(int statu)
{
	m_statu=0;
	if (statu == 0 || statu == 3) //���,ɾ��
	{
		getCodeInput().setEditable(false);
		getNameInput().setEditable(false);
		getForinvnameInput().setEditable(false);
		getPriceInput().setEditable(false);
		getCostInput().setEditable(false);
		getAvgPurInput().setEditable(false);
		getAvgMMInput().setEditable(false);
		getUIChkSealdate().setEnabled(false);

	}
	else if (statu == 1 || statu == 2) //���ӣ��޸�
	{
		getCodeInput().setEditable(true);
		getNameInput().setEditable(true);
		getForinvnameInput().setEditable(true);
		getPriceInput().setEditable(true);
		getCostInput().setEditable(true);
		getAvgPurInput().setEditable(true);
		getAvgMMInput().setEditable(true);
		if (statu == 2 && m_me != null && m_me.getEndflag().booleanValue() == false)
		{
			getCodeInput().setEditable(false);
		}
		if (statu == 1)
		{
			getUIChkSealdate().setEnabled(false);
		}else{
			getUIChkSealdate().setEnabled(true);
		}
		
		getCodeInput().requestFocus();
	}

}
/**
 * ���� CodeInput ����ֵ��
 * @return nc.ui.pub.beans.UITextField
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UITextField getCodeRule() {
	if (CodeRule == null) {
		try {
			CodeRule = new nc.ui.pub.beans.UITextField();
			CodeRule.setName("CodeRule");
//		    ivjCodeInput.setBounds(137, 53, 243, 20);
			CodeRule.setPreferredSize(new Dimension(246,20));
			CodeRule.setMaxLength(12);
			CodeRule.hide();
			
		} catch (java.lang.Throwable ivjExc) {
			
		}
	}
	return CodeRule;
}
}