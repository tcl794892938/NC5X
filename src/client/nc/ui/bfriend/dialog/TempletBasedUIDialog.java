package nc.ui.bfriend.dialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import nc.ui.bfriend.button.BFButtonVO;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.AggregatedValueObject;

public abstract class TempletBasedUIDialog extends UIDialog implements
		ActionListener, BillEditListener {

	// 确定
	public static final int OK = 0;

	// 取消
	public static final int CANCEL = 1;
	
	// 编辑
	public static final int EDIT = 10;

	// 应用
	public static final int APPLY = 11;

	// 增行
	public static final int ADDLINE = 12;

	// 删行
	public static final int DELLINE = 13;
	
	public static final int BODY = 1;
	public static final int HEAD = 0;


	private BillCardPanel m_billCardPanel = null;

	private nc.ui.pub.beans.UIPanel ivjPanel = null;

	private UIPanel cmdPanel = null;

	private String strBusiType;

	private String strBillType;

	private String strCorp;

	private String strOperator;

	private String strNodeKey;

	ArrayList m_defAry = null;
	
	private Hashtable m_btnHas = new Hashtable();

	public TempletBasedUIDialog(Container parent, String strBillType,
			String strCorp, String strOperator, String strNodekey) {
		super(parent);
		this.strBillType = strBillType;
		this.strCorp = strCorp;
		this.strOperator = strOperator;
		this.strNodeKey = strNodekey;
		initialize();
	}

	private void initialize() {
		try {
			setName("ChangeHistory");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(750, 480);

			setContentPane(getUIDialogContentPanel());
		} catch (java.lang.Throwable ivjExc) {
		}
	}

	private nc.ui.pub.beans.UIPanel getUIDialogContentPanel() {
		if (ivjPanel == null) {
			try {
				ivjPanel = new nc.ui.pub.beans.UIPanel();
				ivjPanel.setName("ParentPanel");
				ivjPanel.setLayout(new java.awt.BorderLayout());
				ivjPanel.add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
				ivjPanel.add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjPanel;
	}

	public BillCardPanel getBillCardPanel() {
		if (m_billCardPanel == null) {

			try {
				m_billCardPanel = new BillCardPanel();
				m_billCardPanel.loadTemplet(strBillType, strBusiType,
						strOperator, strCorp, strNodeKey);

				initTotalSumRow();

				initSelfData();
				
				initEventListener();
				
				setCardDecimalDigits();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return m_billCardPanel;
	}

	protected void initEventListener() {
		getBillCardPanel().addEditListener(this);
	}

	protected void initTotalSumRow() {
		// 设置是否显示合计行
		getBillCardPanel().setTatolRowShow(true);
		// 设置是否显示行号
		getBillCardPanel().setRowNOShow(true);
	}

	private UIPanel getCmdPanel() {
		if (cmdPanel == null) {
			try {
				cmdPanel = new nc.ui.pub.beans.UIPanel();
				cmdPanel.setName("CmdPanel");
				// 添加按钮

				BFButtonVO[] btns = getButtons();
				if (btns != null) {
					for (int i = 0; i < btns.length; i++) {
						UIButton btn = btns[i].buildButton();
						btn.addActionListener(this);
						btn.setSize(40, 30);
						cmdPanel.add(btn);
						
						m_btnHas.put(new Integer(btns[i].getNo()), btn);
					}
				}
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return cmdPanel;
	}

	public void setVO(AggregatedValueObject aggVO) {
		getBillCardPanel().setBillValueVO(aggVO);
		execLoadFormula();
	}

	protected void execLoadFormula() {
		// 执行表头加载公式
		BillItem[] billItems = getBillCardPanel().getHeadItems();
		if (billItems != null) {
			for (int i = 0; i < billItems.length; i++) {
				BillItem tmpItem = billItems[i];
				String[] strLoadFormula = tmpItem.getLoadFormula();
				getBillCardPanel().execHeadFormulas(strLoadFormula);
			}
		}
		// 执行表体加载公式
		if (getBillCardPanel().getBillModel() != null) {
			getBillCardPanel().getBillModel().execLoadFormula();
		}

	}

	public String getTitle() {
		return "核销明细";
	}

	protected void initSelfData() {
	}

	public void actionPerformed(ActionEvent e) {
		int intBtn = Integer.parseInt(e.getActionCommand());
		try {
			onButtonAction(intBtn);
		} catch (Exception e1) {
			e1.printStackTrace();
			MessageDialog.showErrorDlg(this, "错误", e1.getMessage());
		}
	}

	protected void onButtonAction(int intBtn) throws Exception {

	}

	protected BFButtonVO[] getButtons() {
		return null;
	}
	
	public final UIButton getButton(int intBtn) {
		Integer IntBtn = new Integer(intBtn);
		if (m_btnHas.containsKey(IntBtn))
			return (UIButton) m_btnHas.get(IntBtn);
		return null;
	}

	public void afterEdit(BillEditEvent e) {
		
	}

	public void bodyRowChange(BillEditEvent e) {
		
	}
	
	protected void initComboBox(BillItem billItem, Object[] values,
			boolean isWhithIndex) {

		if (billItem != null && billItem.getDataType() == BillItem.COMBO) {
			billItem.setWithIndex(isWhithIndex);

			nc.ui.pub.beans.UIComboBox cmb = (nc.ui.pub.beans.UIComboBox) billItem
					.getComponent();

			cmb.removeAllItems();

			for (int i = 0; i < values.length; i++) {
				cmb.addItem(values[i]);
			}
		}

	}
	
	protected void setCardDecimalDigits() throws Exception {
		
		setCardDecimalDigits(HEAD, getHeadShowNum());
		
		setCardDecimalDigits(BODY, getItemShowNum());
		
	}
	
	protected void setCardDecimalDigits(
			int intHeadOrItem,
			String[][] strShow)
			throws Exception {

		if (strShow == null) {
			return;
		}

		if (strShow.length < 2) {
			return;
		}
		if (strShow[0].length != strShow[1].length) {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"uifactory", "UPPuifactory-000059")/* @res "显示位数组第一、二行不匹配" */);
		}
		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = Integer.valueOf(strShow[1][i]);
			BillItem tmpItem = null;
			switch (intHeadOrItem) {
			case HEAD: {
				tmpItem = getBillCardPanel().getHeadItem(attrName);
				if (tmpItem == null)
					getBillCardPanel().getTailItem(attrName);
				break;
			}
			case BODY: {
				tmpItem = getBillCardPanel().getBodyItem(attrName);
				break;
			}
			}

			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}
	
	public String[][] getHeadShowNum() {
		return null;
	}
	
	public String[][] getItemShowNum() {
		return null;
	}

}
