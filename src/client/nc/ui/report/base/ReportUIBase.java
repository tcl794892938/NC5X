package nc.ui.report.base;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.controller.ReportCtl;
import nc.ui.trade.report.group.GroupTableModel;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.trade.report.ConvertTool;
import nc.vo.trade.report.IReportModelSelectType;
import nc.vo.trade.report.ReportDataType2UFDateType;
import nc.vo.trade.report.ReportVO;
import nc.vo.trade.report.ReportVOMetaClass;
import nc.vo.trade.report.TableField;

/**
 * ���ܱ�������
 * 
 * @author������ �޸� dengjt
 */

public abstract class ReportUIBase extends nc.ui.pub.ToftPanel {
	// �ڱ�������ʾ��ѯ������Panel
	private nc.ui.pub.beans.UIPanel conditionPanel = null;

	// �����б������ָ���
	private UISplitPane veriSplitPane = null;

	// ����ģ��
	private ReportBaseClass m_report = null;

	// ����������ӡ�ı���ģ��
	private ReportBaseClass m_reportForPrint = null;

	// �ָ����ţ�
	protected static final char columnSystemDelimiter = '_';

	// ����ģ���ֶ���
	// if the value of column_system fields of ReportModelVOs has same prefix
	// which follow by character '_'
	// then we say these RemportModelVO blong to a group
	// we put column_code values of ReportModelVOs of a group into a arraylist
	// and put the ArrayList into another Arraylist
	private ArrayList reportModelColumnGroups = new ArrayList();

	private IReportCtl m_uiCtl;

	// ���ݵ�ReportModelVO,��Ϊ����ģ����ܻ�ı���������������Լ�������������
	private nc.vo.pub.report.ReportModelVO[] copyOfReportModelVOs = null;

	// ��ѯ�Ի���
	private QueryDLG m_qryDlg = null;

	// ���������Ӱ�ť�Լ���Ӧ�¼�������
	private ButtonAssets button_action_map = new ButtonAssets(this);

	// ������ʾTable
	private UITable groupTable = null;

	// ����hash��
	private HashMap groupMap = new HashMap();

	// ����key�б�
	private ArrayList groupKeys = new ArrayList();

	private CircularlyAccessibleValueObject[] allBodyDataVO = null;

	private boolean needGroup = false;

	private UIScrollPane groupSPane = null;

	/**
	 * TotalReportBase ������ע�⡣
	 */
	public ReportUIBase() {
		super();
		initialize();
	}

	/**
	 * TotalReportBase ������ע�⡣
	 */
	public ReportUIBase(FramePanel fp) {
		super();
		setFrame(fp);
		initialize();
	}

	/**
	 * ���ݱ���ģ�����ݣ���Ϊ��ĳЩ�����п��ܱ��ı�
	 */
	protected void backupReportModelVOs() {
		ArrayList al = new ArrayList();
		for (int i = 0; i < getReportBase().getAllBodyVOs().length; i++) {
			// ����ֻ���� select_type!=δ��ѡ���VO
			if (!getReportBase().getAllBodyVOs()[i].getSelectType().equals(
					IReportModelSelectType.UNSELECTED))
				al.add(getReportBase().getAllBodyVOs()[i].clone());

		}
		copyOfReportModelVOs = (nc.vo.pub.report.ReportModelVO[]) al
				.toArray(new nc.vo.pub.report.ReportModelVO[0]);

	}

	/**
	 * ��ϲ�ѯ����
	 * 
	 * @return java.lang.String
	 * @param customSqlWhere
	 *            �û�����Ĳ�ѯ����
	 */
	protected String combineDefaultSqlWhere(String customSqlWhere) {

		String result = null;
		boolean isCustomSqlWhereEmpty = (customSqlWhere == null || customSqlWhere
				.length() == 0);
		boolean isDefaultSqlWhereEmpty = (getUIControl().getDefaultSqlWhere() == null || getUIControl()
				.getDefaultSqlWhere().trim().length() == 0);

		if (isCustomSqlWhereEmpty)
			result = getUIControl().getDefaultSqlWhere();
		if (isDefaultSqlWhereEmpty)
			result = customSqlWhere;
		if ((!isCustomSqlWhereEmpty) && (!isDefaultSqlWhereEmpty))
			result = customSqlWhere + " and "
					+ getUIControl().getDefaultSqlWhere();
		if (result != null && result.trim().length() == 0)
			result = null;
		return result;

	}

	/**
	 * ����'_'�ָ���ReportModel��fieldNameת��Ϊ��'.'�ָ���VOFieldName
	 * 
	 * @return java.lang.String
	 * @param reportFieldName
	 *            java.lang.String
	 */
	protected String convertReportModelFieldNameToVOFieldName(
			String reportFieldName) {
		if (reportFieldName.indexOf('_') == -1)
			return reportFieldName;
		else
			return reportFieldName.substring(0, reportFieldName.indexOf('_'))
					+ "."
					+ reportFieldName.substring(
							reportFieldName.indexOf('_') + 1, reportFieldName
									.length());
	}

	/**
	 * ����'.'�ָ���VOFieldNameת��Ϊ��'_'�ָ���ReportModel��fieldName
	 * 
	 * @return java.lang.String
	 * @param voFieldName
	 *            java.lang.String
	 */
	protected String convertVOFieldNameToReportModelFieldName(String voFieldName) {
		if (voFieldName.indexOf('.') == -1)
			return voFieldName;
		else
			return voFieldName.substring(0, voFieldName.indexOf('.'))
					+ "_"
					+ voFieldName.substring(voFieldName.indexOf('.') + 1,
							voFieldName.length());
	}

	/**
	 * �Ӵ����VO�д�����ѯ�ַ�������
	 * 
	 * @return java.lang.String[]
	 */
	protected String[] createConditionsFromConditionVO(
			nc.vo.pub.query.ConditionVO[] vos) {
		String[] conditions = new String[vos.length];
		for (int i = 0; i < vos.length; i++) {
			conditions[i] = vos[i].getFieldName() + vos[i].getOperaCode();
			if (vos[i].getRefResult() != null)
				conditions[i] += vos[i].getRefResult().getRefName();
			else
				conditions[i] += vos[i].getValue();
		}
		return conditions;
	}

	/**
	 * Ĭ�Ϸ��ش����ݿ��в�ѯ���ı���������Ϣ��������Լ����ƣ�ֻ�����ش˷������ɡ�
	 * 
	 * @return nc.ui.trade.report.controller.IReportCtl
	 */
	protected IReportCtl createIReportCtl() {
		return new ReportCtl();
	}

	/**
	 * ������ѯ�Ի���
	 * 
	 * @return nc.ui.trade.report.query.QueryDLG
	 */
	protected QueryDLG createQueryDLG() {
		QueryDLG dlg = new QueryDLG();

		dlg.setTempletID(getUIControl()._getPk_corp(), getModuleCode(),
				getUIControl()._getOperator(), null);
		dlg.setNormalShow(false);
	
		return dlg;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField
	 * @param vo
	 *            nc.vo.pub.report.ReportModelVO
	 */
	protected TableField createTableFieldFromReportModelVO(
			nc.vo.pub.report.ReportModelVO vo) {
		return new TableField(convertReportModelFieldNameToVOFieldName(vo
				.getColumnCode()), vo.getColumnUser(), vo.getDataType()
				.intValue() == 1
				|| vo.getDataType().intValue() == 2);
	}

	/**
	 * �õ����������еı���
	 * 
	 * @return java.lang.String[]
	 */
	protected String[] getAllColumnCodes() {
		nc.vo.pub.report.ReportModelVO[] vos = getReportBase().getAllBodyVOs();
		String[] names = new String[vos.length];
		for (int i = 0; i < vos.length; i++) {
			names[i] = vos[i].getColumnCode();
		}
		return names;
	}

	/**
	 * �õ����е��ֶ����ƺ���������
	 * 
	 * @param fields
	 *            java.util.ArrayList
	 * @param dataTypes
	 *            java.util.ArrayList
	 */
	protected void getAllFieldsAndDataType(ArrayList fields, ArrayList dataTypes) {
		if (fields == null || dataTypes == null)
			throw new IllegalArgumentException(
					"getQueryFieldsAndDataType param is null");

		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();

		// TableField[] vfs = getVisibleFields();
		// List vfl = Arrays.asList(vfs);
		for (int i = 0; i < vos.length; i++) {
			TableField f = createTableFieldFromReportModelVO(vos[i]);
			fields.add(f.getFieldName());
			dataTypes.add(new Integer(ReportDataType2UFDateType.convert(vos[i]
					.getDataType())));
		}

	}

	/**
	 * �˷�������Ԥ����ť���Զ��尴ť
	 * 
	 * @author dengjt �������� 2005-8-24
	 */
	private ButtonObject[] getAllBtnAry() {
		if (button_action_map.getVisibleButtonsByOrder().size() == 0)
			return null;
		return (ButtonObject[]) button_action_map.getVisibleButtonsByOrder()
				.toArray(new ButtonObject[0]);
	}

	/**
	 * 
	 * @return java.lang.String[]
	 * @param column_code
	 *            java.lang.String
	 */
	protected String[] getColumnGroupsByColumnCode(String column_code) {
		for (int i = 0; i < reportModelColumnGroups.size(); i++) {
			ArrayList al = (ArrayList) reportModelColumnGroups.get(i);
			if (al.contains(column_code))
				return (String[]) al.toArray(new String[0]);
		}

		return new String[0];
	}

	/**
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	protected nc.ui.pub.beans.UIPanel getConditionPanel() {
		if (conditionPanel == null) {
			conditionPanel = new nc.ui.pub.beans.UIPanel();
			conditionPanel.setName("ConditionPanel");
			java.awt.FlowLayout l = new java.awt.FlowLayout();
			l.setAlignment(java.awt.FlowLayout.LEFT);
			l.setHgap(10);

			conditionPanel.setLayout(l);// java.awt.GridLayout(2,6,0,1));
		}
		return conditionPanel;
	}

	/**
	 * �õ�����ģ�������ص���(select_type == 1,����û�ڽ�������ʾ���С� select_type==2
	 * ���б���Ϊ�����������С��ų�����)
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	protected TableField[] getInvisibleFields() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		ArrayList invisible = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
					.intValue()) {
				try {
					getReportBase().getBillTable().getColumn(
							vos[i].getColumnUser());

				}
				// �׳��쳣��˵���������Ѿ�����
				catch (Exception e) {
					invisible.add(createTableFieldFromReportModelVO(vos[i]));
				}

			}
		}

		TableField[] invisibleFields = (TableField[]) invisible
				.toArray(new TableField[0]);

		return invisibleFields;
	}

	/**
	 * �õ�����ģ���ϵĿɼ��У����ǽ����Ͽ��Կ�������
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	protected TableField[] getVisibleFields() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		ArrayList visible = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
					.intValue()) {
				try {
					getReportBase().getBillTable().getColumn(
							vos[i].getColumnUser());
					visible.add(createTableFieldFromReportModelVO(vos[i]));
				} catch (Exception e) {

				}
			}
		}

		TableField[] visibleFields = (TableField[]) visible
				.toArray(new TableField[0]);

		return visibleFields;
	}

	public QueryDLG getQryDlg() {
		if (m_qryDlg == null) {
			m_qryDlg = createQueryDLG();
		}
		return m_qryDlg;
	}

	/**
	 * 
	 * @return nc.ui.report.base.ReportBase
	 */
	public ReportBaseClass getReportBase() {
		if (m_report == null) {
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(getUIControl()._getPk_corp(),
						getModuleCode(), getUIControl()._getOperator(), null);
				
				m_report.getBillTable().getSelectionModel()
						.addListSelectionListener(new ListSelectionListener() {

							public void valueChanged(ListSelectionEvent e) {
								updateAllButtons();
							}

						});
			} catch (Exception ex) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("uifactory_report",
								"UPPuifactory_report-000032")/*
																 * @res "������Ϣ"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory_report", "UPPuifactory_report-000033")/*
																			 * @res
																			 * "δ�ҵ�����ģ��!"
																			 */);
				Logger.error(ex.getMessage(), ex);
			}
		}
		return m_report;
	}

	protected UITable getGroupTable() {
		if (groupTable == null) {
			groupTable = new UITable(new DefaultTableModel());
			groupTable.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int row = groupTable.getSelectedRow();
					if (row == -1)
						return;
					int count = groupTable.getModel().getColumnCount();
					StringBuffer key = new StringBuffer();
					for (int i = 0; i < count; i++) {
						key
								.append(groupTable.getModel()
										.getValueAt(row, i) == null ? ""
										: groupTable.getModel().getValueAt(row,
												i).toString());
						if (i != count - 1)
							key.append(":");
					}
					ArrayList tmpVO = (ArrayList) groupMap.get(key.toString());
					if (tmpVO != null && tmpVO.size() > 0) {
						setBodyDataVO(
								(CircularlyAccessibleValueObject[]) tmpVO
										.toArray(new CircularlyAccessibleValueObject[0]),
								false);
					}
					setHeadItems(convertVOKeysToModelKeys((String[]) groupKeys
							.toArray(new String[0])),
							getValuesFromGroupTable(row));
				}

			});
		}
		return groupTable;
	}

	/**
	 * ����ģ��������ñ���title
	 * 
	 */
	protected void updateTitle() {
		updateTitle(getReportBase().getReportTitle());
	}

	/**
	 * ����������ַ������ñ���title
	 * 
	 * @param strTitle
	 */
	protected void updateTitle(String strTitle) {
		setTitleText(strTitle);
	}

	protected void updateReportBase() {
		if (m_report == null)
			return;
		try {
			m_report.setTempletID(getUIControl()._getPk_corp(),
					getModuleCode(), getUIControl()._getOperator(), null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��ñ���ģ����࣬�˻���Ϊ������ӡԤ����
	 * 
	 * @return nc.ui.report.base.ReportBase
	 */
	public ReportBaseClass getReportBaseForPrint() {
		if (m_reportForPrint == null) {
			try {
				m_reportForPrint = new ReportBaseClass();
				m_reportForPrint.setName("ReportBaseForPrint");
				m_reportForPrint.setTempletID(getUIControl()._getPk_corp(),
						getModuleCode(), getUIControl()._getOperator(), null);
			} catch (Exception ex) {
				System.out.println("����:δ�ҵ�����ģ��......");
			}
		}
		return m_reportForPrint;
	}

	/**
	 * �õ���Ӧ�ڱ���ģ���������е�VO��metaClass��
	 * 
	 * @return nc.vo.trade.report.ReportVOMetaClass
	 */
	protected ReportVOMetaClass getReportVOMetaClassOfAllFields()

	{
		ArrayList fs = new ArrayList();
		ArrayList ds = new ArrayList();
		getAllFieldsAndDataType(fs, ds);
		String[] fieldsname = (String[]) fs.toArray(new String[0]);
		Integer[] datatypes = (Integer[]) ds.toArray(new Integer[0]);
		String[] fieldAlias = (String[]) ConvertTool
				.createFieldAlias(fieldsname);

		return new ReportVOMetaClass(fieldsname, fieldAlias, datatypes,
				getUIControl().getAllTableAlias(), getUIControl()
						.getTableJoinClause());

	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return getReportBase().getReportTitle();
	}

	/**
	 * ���ص�ǰUI��Ӧ�Ŀ�����ʵ����
	 * 
	 * @return nc.ui.tm.pub.ControlBase
	 */
	public IReportCtl getUIControl() {
		if (m_uiCtl == null)
			m_uiCtl = createIReportCtl();
		return m_uiCtl;
	}

	/**
	 * �����������δ��̬�ĸ�UI������ʱ������ȷ����
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getVOFromUI() {
		ReportVOMetaClass voClass = getReportVOMetaClassOfAllFields();
		ReportItem[] items = getReportBase().getBody_Items();
		int rows = getReportBase().getRowCount();
		ReportVO[] result = new ReportVO[rows];
		for (int row = 0; row < rows; row++) {
			result[row] = voClass.createReportVO();
			for (int i = 0; i < items.length; i++) {
				// System.out.println(items[i].getKey());
				result[row].setAttributeValue(items[i].getKey(),
						getReportBase().getBodyValueAt(row, items[i].getKey()));

			}
		}

		return result;
	}

	/**
	 * ���ص�ǰVO������ӷ�������Ҳ�������ֱ�Ӵӽ����ϵõ����ӽ����ϵõ�VO�ȽϺ�ʱ�����ǣ�������ݺ�
	 * VO˳���������أ�����ֱ��getVOFromUI����ΪһЩ��������������û���ڷ���������ֳ���
	 * 
	 * @return
	 */
	public CircularlyAccessibleValueObject[] getCurrentVO() {
		CircularlyAccessibleValueObject[] cvos = null;
		if ((cvos = getCurrentVOFromGroupMap()) == null) {
			cvos = getVOFromUI();
		}
		return cvos;
	}

	/**
	 * �����������ݡ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public CircularlyAccessibleValueObject[] getAllBodyDataVO() {
		if (allBodyDataVO == null) {
			try {
				allBodyDataVO = getVOFromUI();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return allBodyDataVO;
	}

	/**
	 * �˴����뷽��˵����
	 */
	protected void initColumnGroups() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		HashMap tmpHash = new HashMap();

		for (int i = 0; i < vos.length; i++) {
			int index = vos[i].getColumnSystem().indexOf(columnSystemDelimiter);
			String key;
			ArrayList al;
			if (index != -1) {
				key = vos[i].getColumnSystem().substring(0, index);
			} else {
				key = vos[i].getColumnSystem();
			}
			if (tmpHash.get(key) == null) {
				al = new ArrayList();
				tmpHash.put(key, al);
			} else {
				al = (ArrayList) tmpHash.get(key);
			}
			al.add(vos[i].getColumnCode());

		}
		reportModelColumnGroups.addAll(tmpHash.values());
	}

	private void initialize() {
		// Ȩ��֮�ƣ����ڰ�ťΪpublic��̬��������Ʒ�����ֱ�����ã��˴�ͳһ���¶���
		ButtonAssets.reInitButton();
		
		setName("GeneralPane");
		setSize(774, 419);

		UISplitPane horiSplitPane = new UISplitPane(UISplitPane.VERTICAL_SPLIT,
				null, getVeriSplitPane());
		// ���ݿ�����Ϣ�������Ƿ���ʾ��ѯ������ʾ��
		if (true) { //wanglong
			horiSplitPane.setLeftComponent(null);
			horiSplitPane.setDividerLocation(0);
			horiSplitPane.setEnabled(false);
			horiSplitPane.setDividerSize(0);
		} else {
			horiSplitPane.setLeftComponent(getConditionPanel());
			horiSplitPane.setDividerLocation(80);
			horiSplitPane.setOneTouchExpandable(true);
			horiSplitPane.setDividerSize(6);
		}

		add(horiSplitPane);

		// �����Զ��尴ť
		setPrivateButtons();

		getReportBase();

		setButtons(getAllBtnAry());
		// ���°�ť��ʹ��ʼ״ֻ̬�ܲ�ѯ�� dengjt
		updateAllButtons();
		// ����ģ��model VO
		backupReportModelVOs();

		if (getUIControl().getGroupKeys() != null)
			needGroup = true;

		initColumnGroups();

		setDigitFormat();

		getReportBase().setShowNO(true);

	}

	/**
	 * @param veriSplitPane
	 */
	private void setVeriSplitEnabled(boolean enabled) {
		if (enabled) {
			veriSplitPane.setLeftComponent(getGroupPanel());
			veriSplitPane.setDividerLocation(200);
			veriSplitPane.setEnabled(true);
			veriSplitPane.setDividerSize(15);
			veriSplitPane.setOneTouchExpandable(true);
			getReportBase().setShowNO(true);
		} else {
			veriSplitPane.setLeftComponent(null);
			veriSplitPane.setDividerLocation(0);
			veriSplitPane.setEnabled(false);
			veriSplitPane.setDividerSize(0);
			veriSplitPane.setOneTouchExpandable(false);
			getReportBase().setShowNO(false);
		}
	}

	/**
	 * ��Ӧ��ť�¼���
	 * 
	 * @param bo
	 */
	public void onButtonClicked(ButtonObject bo) {
		try {
			if (button_action_map.get(bo) != null) {
				IButtonActionAndState action = (IButtonActionAndState) button_action_map
						.get(bo);
				action.execute();
			}
		} catch (nc.vo.pub.BusinessException ex) {
			showErrorMessage(ex.getMessage());
			Logger.error(ex.getMessage(), ex);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		updateAllButtons();
	}

	/**
	 * �Խ���vo����
	 */
	protected void onSort(String[] fields, int[] asc) {
		CircularlyAccessibleValueObject[] vos = null;
		if ((vos = getCurrentVO()) == null)
			return;
		getReportBase().getReportSortUtil().multiSort(vos, fields, asc);
		setBodyDataVO(vos, false);
	}

	private CircularlyAccessibleValueObject[] getCurrentVOFromGroupMap() {
		if (groupKeys.size() == 0)
			return null;
		int selectedRow = 0;
		if (getGroupTable().getSelectedRow() != -1)
			selectedRow = getGroupTable().getSelectedRow();
		String[] keys = getValuesFromGroupTable(selectedRow);
		String key = "";
		for (int i = 0; i < keys.length; i++) {
			key += keys[i];
			if (i != keys.length - 1)
				key += ":";
		}
		return (groupMap.get(key) == null ? null
				: (CircularlyAccessibleValueObject[]) ((ArrayList) groupMap
						.get(key))
						.toArray(new CircularlyAccessibleValueObject[0]));
	}

	/**
	 * ����ģ���ӡ
	 */
	protected void onPrintTemplet() {
		getReportBase().printData();
	}

	/**
	 * �������а�ť״̬
	 */
	protected void updateAllButtons() {
		boolean hasData = false;
		BillModel bm = m_report.getBillModel();
		if (bm != null
				&& (bm.getDataVector() == null || bm.getDataVector().size() == 0)) {
			hasData = false;
		} else {
			hasData = true;
		}
		setButtons(getAllBtnAry());
		setAllButtonState(hasData);
		updateButtons();

	}

	/**
	 * ����ģ������VO��Ŀǰ���ص��Ǳ���������VO����ģ�����ṩ����Ӧ���ܺ���ֱ�ӵ���ģ�������Ӧ����
	 * 
	 * @return ģ��modelVO
	 */
	protected ReportModelVO[] getModelVOs() {
		return copyOfReportModelVOs; // getReportBase().getAllBodyVOs();
	}

	/**
	 * ��������button״̬��������IButtonActionAndState�ӿڵ�isButtonAvailable
	 * 
	 * @param hasData
	 */
	private void setAllButtonState(boolean hasData) {
		Iterator it = button_action_map.keySet().iterator();
		while (it.hasNext()) {
			ButtonObject obj = (ButtonObject) it.next();
			IButtonActionAndState state = (IButtonActionAndState) button_action_map
					.get(obj);
			int result = state.isButtonAvailable();
			if (result == 0) {
				obj.setEnabled(false);
			} else if (result == 1) {
				obj.setEnabled(true);
			} else if (result == IButtonActionAndState.ENABLE_ALWAYS) {
				obj.setEnabled(true);
			} else if (result == IButtonActionAndState.ENABLE_WHEN_HAS_DATA) {
				if (hasData) {
					obj.setEnabled(true);
				} else
					obj.setEnabled(false);
			}

		}
	}

	/**
	 * ��������
	 */
	protected void onFilter(String strFomula) throws Exception {
		getReportBase().filter(strFomula);
	}

	/**
	 * ��������
	 * 
	 * @param rows:
	 *            the cross rows
	 * @param cols:
	 *            the cross cols
	 * @param values:
	 *            the cross values
	 * @throws Exception
	 */
	protected void onCross(String[] rows, String[] cols, String[] values)
			throws Exception {
		getReportBase().drawCrossTable(rows, cols, values);
	}

	/**
	 * ������ʾ�������ݣ��������⣬����˳���Լ�������ʾ����
	 * 
	 * @param title
	 *            ����ʾ����
	 * @param fieldNames
	 *            ����keyֵ�����ᰴ������˳����ʾ
	 * @param showNames
	 *            ��Ӧkey����ʾ���ƣ���Ϊ�գ��ռ���Ϊ��������ʾ���ơ������key�ĳ��Ȳ�һ�£�Ҳ��ΪΪ��
	 * @throws Exception
	 */
	protected void onColumnFilter(String title, String[] fieldNames,
			String[] showNames, boolean isAdjustOrder) throws Exception {
		CircularlyAccessibleValueObject[] vos = getBodyDataVO();
		getReportBase().hideColumn(getAllColumnCodes());

		// ����ģ���б���
		getReportBase().setReportTitle(title);
		// ������ʾ����
		setTitleText(title);
		getReportBase().showHiddenColumn(fieldNames);
		if (isAdjustOrder)
			setColumnOrder(fieldNames);
		if (showNames != null && showNames.length == fieldNames.length)
			setColumnName(fieldNames, showNames);
		setBodyDataVO(vos, true);
	}

	/**
	 * ���Ľ���ģ�����ʾ����
	 * 
	 * @param fieldNames
	 * @param showNames
	 */
	private void setColumnName(String[] fieldNames, String[] showNames) {
		ReportItem[] items = getReportBase().getBody_Items();
		HashMap tmpHas = new HashMap();
		for (int i = 0; i < items.length; i++) {
			tmpHas.put(items[i].getKey(), items[i]);
		}

		for (int i = 0; i < fieldNames.length; i++) {
			if (tmpHas.containsKey(fieldNames[i])) {
				ReportItem tmpItem = (ReportItem) tmpHas.get(fieldNames[i]);
				if (!tmpItem.getName().equals(showNames[i]))
					tmpItem.setName(showNames[i]);
			}

		}
		getReportBase().setBody_Items(items);
	}

	/**
	 * ֱ�Ӵ�ӡ
	 * 
	 * @throws Exception
	 */
	public void onPrintDirect() throws Exception {
		PrintDirectEntry print = PrintManager.getDirectPrinter(getReportBase()
				.getBillTable(), getReportBase().getHead_Items());
		print.setTitle(getTitle());
		print.preview();
	}

	/**
	 * ��ӡԤ��
	 * 
	 * @throws Exception
	 */
	public void onPrintPreview() throws Exception {
		getReportBase().previewData();
	}

	protected abstract void onQuery() throws Exception;

	/**
	 * �����ϴεĲ�ѯ�������²�ѯ
	 */
	protected void onRefresh() throws Exception {
	}

	/**
	 * 
	 * @param context
	 *            С�ƺϼ�������
	 * @throws Exception
	 */
	protected void onSubTotal(SubtotalContext context) throws Exception {
		getReportBase().setSubtotalContext(context);
		getReportBase().subtotal();
	}

	/**
	 * ����
	 * 
	 * @param context
	 * @throws Exception
	 * @throws Exception
	 */
	protected void onGroup(String[] keys) {
		String[] colNames = getColumnNamesByKeys(convertVOKeysToModelKeys(keys));
		onGroup(keys, colNames);
	}

	/**
	 * ����
	 * 
	 * @param keys
	 * @param names
	 */
	protected void onGroup(String[] keys, String[] names) {
		String[] convertGroupKeys = convertVOKeysToModelKeys((String[]) groupKeys
				.toArray(new String[0]));
		// ��ʾ�ϴ����ص���
		getReportBase().showHiddenColumn(convertGroupKeys);
		// �����ϴ����ӵı�ͷ
		removeHeadItems(convertGroupKeys);
		// ���������¼
		groupKeys.clear();
		// ��Ϊȡ������
		if (keys == null || names == null || keys.length == 0
				|| names.length == 0 || keys.length != names.length) {
			setVeriSplitEnabled(false);
			// ���û�ԭ����
			setBodyDataVO(allBodyDataVO, false);
			return;
		}

		groupKeys.addAll(Arrays.asList(keys));
		// �����ָ���
		setVeriSplitEnabled(true);
		// �ӽ����ȡVO
		if (allBodyDataVO == null || allBodyDataVO.length == 0)
			allBodyDataVO = getVOFromUI();
		// ��շ���hash
		groupMap.clear();
		// ������key hash��key����ʽ�� a:b:c::��ֵ��Ϊ��" ",��Ӱ��ʹ��
		for (int i = 0; i < allBodyDataVO.length; i++) {
			StringBuffer key = new StringBuffer();
			for (int j = 0; j < keys.length; j++) {
				key
						.append(allBodyDataVO[i].getAttributeValue(keys[j]) == null ? " "
								: allBodyDataVO[i].getAttributeValue(keys[j])
										.toString());
				if (j != keys.length - 1)
					key.append(":");
			}
			addVoToHashmap(key.toString(), allBodyDataVO[i]);
		}
		String[] convertedKeys = convertVOKeysToModelKeys(keys);
		extractItemsToHead(convertedKeys, names);
		getReportBase().hideColumn(convertedKeys);
		// �����model
		GroupTableModel model = new GroupTableModel();
		model.addColumns(names);
		model.addRows(groupMap.keySet());
		getGroupTable().setModel(model);
		// ��ʾ��һ������
		ArrayList tmpVO = (ArrayList) groupMap.get(groupMap.keySet().iterator()
				.next());
		if (tmpVO != null && tmpVO.size() > 0) {
			setBodyDataVO((CircularlyAccessibleValueObject[]) tmpVO
					.toArray(new CircularlyAccessibleValueObject[0]), false);
			setHeadItems(convertedKeys, getValuesFromGroupTable(0));
		}
	}

	/**
	 * @param strings
	 */
	private void removeHeadItems(String[] strs) {
		ReportItem[] headItems = getReportBase().getHead_Items();
		ArrayList list = new ArrayList();
		if (headItems != null && headItems.length > 0) {
			for (int i = 0; i < headItems.length; i++) {
				if (contains(strs, headItems[i].getKey()))
					continue;
				list.add(headItems[i]);
			}
		}
		getReportBase().setHead_Items(
				(ReportItem[]) list.toArray(new ReportItem[0]));
	}

	/**
	 * �ӷ���table�л�ȡָ��������
	 * 
	 * @param row
	 * @return
	 */
	private String[] getValuesFromGroupTable(int row) {
		UITable table = getGroupTable();
		int count = table.getColumnCount();
		String[] str = new String[count];
		for (int i = 0; i < str.length; i++) {
			str[i] = (String) table.getModel().getValueAt(row, i);
		}
		return str;
	}

	/**
	 * ��keys����Ӧ��item��ȡ����ͷ
	 * 
	 * @param keys
	 *            ����
	 * @param colNames
	 *            ������
	 */
	private void extractItemsToHead(String[] keys, String[] colNames) {
		String[] convertKeys = convertVOKeysToModelKeys(keys);
		ReportItem[] items = new ReportItem[keys.length];
		for (int i = 0; i < convertKeys.length; i++) {
			items[i] = new ReportItem();
			items[i].setKey(convertKeys[i]);
			items[i].setShow(true);
			items[i].setName(colNames[i]);
		}
		getReportBase().addHeadItem(items);
	}

	/**
	 * @param keys
	 * @return
	 */
	public String[] convertVOKeysToModelKeys(String[] keys) {
		if (keys == null || keys.length == 0)
			return null;
		String[] convertedKeys = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			convertedKeys[i] = convertVOFieldNameToReportModelFieldName(keys[i]);
		}
		return convertedKeys;
	}

	/**
	 * ���ñ�ͷ����
	 * 
	 * @param key
	 * @param value
	 */
	private void setHeadItems(String[] keys, Object[] values) {
		if (keys == null || values == null || keys.length == 0
				|| values.length == 0)
			return;
		if (keys.length != values.length) {
			System.out.println("����ֵ����Ŀ��ƥ��");
			return;
		}
		for (int i = 0; i < keys.length; i++) {
			getReportBase().setHeadItem(keys[i], values[i]);
		}
	}

	/**
	 * ͨ��keys���col��ʾ��
	 * 
	 * @param keys
	 *            ������ModelKey������'_'�ָ���key
	 * @return
	 */
	protected String[] getColumnNamesByKeys(String[] keys) {
		if (keys == null || keys.length == 0)
			return null;
		ReportModelVO[] fields = getModelVOs();
		// ��ʹ�ù̶����飬��ֹ����Ԫ��ΪNULL������������ж�
		ArrayList list = new ArrayList();
		if (fields != null && fields.length != 0) {
			for (int i = 0; i < keys.length; i++) {
				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getColumnCode().equals(keys[i]))
						list.add(fields[j].getColumnUser());
				}
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * ͨ��key���Col��ʾ��
	 * 
	 * @param key
	 *            ������ModelKey������'_'�ָ���key
	 */
	protected String getColumnNameByKey(String key) {
		if (key == null)
			return null;
		ReportModelVO[] fields = getModelVOs();
		if (fields != null && fields.length != 0) {
			for (int j = 0; j < fields.length; j++) {
				if (fields[j].getColumnCode().equals(key))
					return fields[j].getColumnUser();
			}
		}
		return null;
	}

	/**
	 * ��VO���ӵ�HashMap��
	 * 
	 * @param key
	 * @param vo
	 */
	private void addVoToHashmap(String key, CircularlyAccessibleValueObject vo) {
		ArrayList list = null;
		if ((list = (ArrayList) groupMap.get(key)) == null) {
			list = new ArrayList();
			list.add(vo);
			groupMap.put(key, list);
		} else
			list.add(vo);
	}

	// protected void onCode
	/**
	 * �˷������ڲ�ѯ�����ݺ���á��������ش˷������Զ���ЩVO���ж�������
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	protected CircularlyAccessibleValueObject[] processVOs(
			CircularlyAccessibleValueObject[] vos) {
		return vos;
	}

	/**
	 * ����������ñ������е�˳��
	 * 
	 */
	protected void setColumnOrder(String[] column_codes) {
		ReportItem[] items = getReportBase().getBody_Items();

		ArrayList al = new ArrayList();
		HashMap tmpHas = new HashMap();
		for (int i = 0; i < items.length; i++) {
			tmpHas.put(items[i].getKey(), items[i]);
		}

		for (int i = 0; i < column_codes.length; i++) {
			if (tmpHas.containsKey(column_codes[i])) {
				al.add(tmpHas.get(column_codes[i]));
				tmpHas.remove(column_codes[i]);
			}

		}
		al.addAll(tmpHas.values());

		ReportItem[] newitems = (ReportItem[]) al.toArray(new ReportItem[0]);
		getReportBase().setBody_Items(newitems);

	}

	/**
	 * �����������͸�ʽ
	 */
	protected void setDigitFormat() {
		ReportItem[] items = getReportBase().getBody_Items();

		for (int i = 0; i < items.length; i++) {
			if (items[i].getDataType() == 2 || items[i].getDataType() == 1) {
				items[i].setDecimalDigits(2);
			}

		}

	}

	/**
	 * �����Զ��尴ť,���ش˷������˷����ڳ�ʼ��ʱ���á�
	 */
	protected void setPrivateButtons() {
	}

	/**
	 * ������ע�ᰴť
	 */
	protected void unRegisterButton(ButtonObject obj) {
		if (obj != null) {
			button_action_map.remove(obj);
		}

	}

	/**
	 * ע�ᰴť��-1��ʾ�������
	 */
	protected void registerButton(ButtonObject obj, IButtonActionAndState action) {
		registerButton(obj, action, -1);
	}

	/**
	 * 
	 * ע�ᰴť,����ָ��λ�ü��밴ť��λ�ô��㿪ʼ����
	 */
	protected void registerButton(ButtonObject obj,
			IButtonActionAndState action, int pos) {
		if (obj == null || action == null) {
			System.out.println("��ť����Ϊ��,���ܼ���");
			return;
		}
		if (button_action_map.get(obj) != null) {
			System.out.println("�˰�ť�Ѿ�����");
			return;
		}
		button_action_map.put(obj, action, pos);
	}

	/**
	 * ��������ַ���������ʾ�ڲ�ѯ��ʾ�����
	 * 
	 * @param conditions
	 *            ����ʾ�ַ�������
	 */
	public void showCondition(String[] conditions) {

		getConditionPanel().removeAll();
		if (conditions == null || conditions.length == 0)
			return;

		String[] temp = conditions;
		nc.ui.pub.beans.UILabel tmp = new nc.ui.pub.beans.UILabel(temp[0]);
		java.awt.FontMetrics metrics = tmp.getFontMetrics(tmp.getFont());
		int[] widths = new int[conditions.length];
		for (int i = 0; i < conditions.length; i++) {
			widths[i] = metrics.stringWidth(conditions[i]);

		}
		Arrays.sort(widths);

		int width = widths[widths.length - 1];
		int heigth = metrics.getHeight();
		for (int i = 0; i < conditions.length; i++) {
			nc.ui.pub.beans.UILabel l = new nc.ui.pub.beans.UILabel(
					conditions[i]);
			getConditionPanel().add(l);
			l.setPreferredSize(new java.awt.Dimension(width, heigth));

		}
		getConditionPanel().invalidate();
		getConditionPanel().repaint();

	}

	/**
	 * 
	 * @return ���� bodyDataVO��
	 */
	public CircularlyAccessibleValueObject[] getBodyDataVO() {
		return getReportBase().getBodyDataVO();
	}

	/**
	 * @param bodyDataVO
	 *            Ҫ���õ� bodyDataVO��
	 * @param isLoadFormula
	 *            �Ƿ�ִ�м��ع�ʽ
	 */
	protected void setBodyDataVO(CircularlyAccessibleValueObject[] dataVO,
			boolean isLoadFormula) {
		getReportBase().setBodyDataVO(dataVO, isLoadFormula);
		if (needGroup) {
			needGroup = false;
			onGroup(getUIControl().getGroupKeys());
		}
		updateAllButtons();
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 * @param type
	 *            java.lang.Integer
	 */
	protected TableField[] getVisibleFieldsByDataType(Integer type) {
		TableField[] visibleFields = getVisibleFields();
		ArrayList al = new ArrayList();
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getDataType().equals(type)) {
				TableField f = createTableFieldFromReportModelVO(vos[i]);
				if (Arrays.asList(visibleFields).contains(f)) {
					al.add(f);
				}
			}
		}
		return (TableField[]) al.toArray(new TableField[0]);
	}

	/**
	 * @return ���� groupKeys��
	 */
	protected ArrayList getGroupKeys() {
		return groupKeys;
	}

	public boolean contains(String[] source, String element) {
		if (source != null) {
			for (int i = 0; i < source.length; i++) {
				if (source[i].equals(element))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return ���� veriSplitPane��
	 */
	private UISplitPane getVeriSplitPane() {
		if (veriSplitPane == null) {

			veriSplitPane = new UISplitPane(UISplitPane.HORIZONTAL_SPLIT,
					false, null, getReportBase());

			setVeriSplitEnabled(false);
		}
		return veriSplitPane;
	}

	private UIScrollPane getGroupPanel() {
		if (groupSPane == null) {
			groupSPane = new UIScrollPane();
			groupSPane.setViewportView(getGroupTable());
		}
		return groupSPane;
	}
	
	
	protected abstract void onQueryMx() throws Exception;
	
	protected abstract void onClearItemyj() throws Exception;
	
	protected abstract void onQueryYcht() throws Exception;
	
	protected abstract void onQueryFpMx() throws Exception;
	
	protected abstract void onQueryFh() throws Exception;
	
	protected abstract void onQueryZb() throws Exception;
	
	protected abstract void onQueryYCPZ() throws Exception;
}