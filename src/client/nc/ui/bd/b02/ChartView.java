package nc.ui.bd.b02;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.accsubj.IAccsubjBusiQueryForeign;
import nc.itf.uap.bd.multibook.IGLOrgBookAcc;
import nc.itf.uap.busibean.ISysInitQry;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.bd.b00.SubjAlterDlg;
import nc.ui.bd.b00.SubjEditionDlg;
import nc.ui.bd.pub.BillExportUtil;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.glpub.IChartModel;
import nc.ui.glpub.IPara;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.print.IDataSource;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.valueeditor.DefaultFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditorFactory;
import nc.ui.querytemplate.valueeditor.RefElementEditor;
import nc.ui.querytemplate.valueeditor.UIRefpaneCreator;
import nc.ui.trade.buttonstate.CardBtnVO;
import nc.ui.trade.buttonstate.CopyBtnVO;
import nc.ui.trade.buttonstate.ExportBtnVO;
import nc.ui.trade.buttonstate.QueryBtnVO;
import nc.vo.bd.ICorpKind;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.b02.CorpKindInfo;
import nc.vo.bd.b02.ISubjConstants;
import nc.vo.bd.b02.SubjassVO;
import nc.vo.bd.b52.GlbookVO;
import nc.vo.bd.b54.GlorgVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.subjscheme.SubjschemeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.sm.nodepower.OrgnizeTypeVO;

public class ChartView extends ToftPanel implements ListSelectionListener, 
		IPara, IUiPanel, IDataSource, IAccsubjView, ValueChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String BUTTON_NAME_REFRESH = nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081802", "UC001-0000009")/* @res "ˢ��" */;

	private static final String BUTTON_NAME_RETURN = nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081802", "UC001-0000038")/* @res "����" */;

	private static final String EDIT_STATE_EDITABLE = nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081802", "UPP10081802-000103")/*
																	    * @res
																	    * "�ɱ༭"
																	    */;

	private static final String EDIT_STATE_UN_EDITABLE = nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081802", "UPP10081802-000104")/*
																	    * @res
																	    * "���ɱ༭"
																	    */;
	private ButtonObject m_exportButton = new ExportBtnVO().getButtonVO().buildButton();
//		new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000056")/* @res "����" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000056")/* @res "����" */, 2, "����"); /*-=notranslate=-*/

	private ChartModel m_ChartModel = null;

	private UIRefPane m_choosePane = null;

	private UIPanel m_northPanel = null;

	private boolean m_isGroup = false;

	private ChartUi ivjChartUi = null;

	public IParent m_parent;

	private ButtonObject[] m_arryCurrentButtons = null;

	private ButtonObject m_addButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000002")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000002")/* @res "����" */, 2, "����"); /*-=notranslate=-*/

	private ButtonObject m_delButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000039")/* @res "ɾ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000039")/* @res "ɾ��" */, 2, "ɾ��"); /*-=notranslate=-*/

	private ButtonObject m_modButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000045")/* @res "�޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000045")/* @res "�޸�" */, 2, "�޸�"); /*-=notranslate=-*/

	private ButtonObject m_alterButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000012")/* @res "��Ŀ���" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000012")/* @res "��Ŀ���" */, 2, "��Ŀ���"); /*-=notranslate=-*/

	private ButtonObject m_amendButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000011")/* @res "��Ŀ�޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000011")/* @res "��Ŀ�޸�" */, 2, "��Ŀ�޸�"); /*-=notranslate=-*/

	private ButtonObject m_copyButton = new CopyBtnVO().getButtonVO().buildButton();
//		new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000043")/* @res "����" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000043")/* @res "����" */, 2, "����"); /*-=notranslate=-*/

	private ButtonObject m_refreshButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000009")/* @res "ˢ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000009")/* @res "ˢ��" */, 2, "ˢ��"); /*-=notranslate=-*/

	private ButtonObject m_linkButton = new CardBtnVO().getButtonVO().buildButton();
//		new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UPT10081802-000006")/* @res "�л�" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UPT10081802-000006")/* @res "�л�" */, 2, "�л�"); /*-=notranslate=-*/

	//	private ButtonObject m_sort = new ButtonObject(
	//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
	//					"UPT10081802-000014")/* @res "�鿴" */,
	//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
	//					"UPT10081802-000014")/* @res "�鿴" */, 2, "�鿴"); /*-=notranslate=-*/
	//
//	private ButtonObject m_Assort = new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC000-0000559")/* @res "����" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC000-0000559")/* @res "����" */, 2, "����"); /*-=notranslate=-*/

	private ButtonObject m_printButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000007")/* @res "��ӡ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000007")/* @res "��ӡ" */, 2, "��ӡ"); /*-=notranslate=-*/

	private ButtonObject m_selectAllButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000041")/* @res "ȫѡ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UC001-0000041")/* @res "ȫѡ" */, 2, "ȫѡ"); /*-=notranslate=-*/

	private ButtonObject m_selectNoneButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000007")/* @res "ȡ��ȫѡ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000007")/* @res "ȡ��ȫѡ" */, 2, "ȡ��ȫѡ");

	private ButtonObject m_assignAccsubjButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPP10081802-000053")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPP10081802-000053")/* @res "����" */, 2, "����"); /*-=notranslate=-*/

	private ButtonObject m_assignCopyButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
	"UPP10081802-000140")/* @res "�緽������"*/,
	nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
	"UPP10081802-000140")/* @res "�緽������"*/, 2, "�緽������"); /*-=notranslate=-*/

	private ButtonObject m_editionButton = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000013")/* @res "��Ŀ�汾��ѯ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
					"UPT10081802-000013")/* @res "��Ŀ�汾��ѯ" */, 2, "��Ŀ�汾��ѯ");

	private ButtonObject m_queryButton = new QueryBtnVO().getButtonVO().buildButton();
//		new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000006")/* @res "��ѯ" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
//					"UC001-0000006")/* @res "��ѯ" */, 2, "��ѯ"); /*-=notranslate=-*/




	private boolean m_grpctrl = false;//��Ŀ�������Ƿ�����¼������Ƽ���Ϊ0��

	private String m_pk_user = null;

	//	private String m_currYear = null; //��ǰ��½�Ļ�����
	//
	//	private String m_currPeriod = null; //��ǰ��½�Ļ����
	//
	private String m_pk_glorgbook = null;//��ǰ��½�Ļ�������˲�

	private String[] m_currPeriod = null;

	private String m_pk_subjscheme = null;//��ǰѡ��Ŀ�Ŀ����

	private String m_pk_periodscheme = null;//��ǰ��Ӧ�Ļ���ڼ䷽��

	IUiPanel m_cardView;

	//	private LocalizerDlg m_localizerdlg = null;

	private AssortDlg m_assortdlg = null;

//	private int m_settleType = 0;/* 0����ͨ��˾��1���ܽ����������˲���2���ֽ��������˲���3���ܽ������ı����˲� */

	private SubjAlterDlg m_subjAlterDlg = null;

	private SubjEditionDlg m_subjEditionDlg = null;

	private int EditionQuery = 0;
	
	private QueryConditionDLG queryDlg;

	
	private String codeRule;
	private  final String INITCODE="BD601";
	/**
	 * MainPanel ������ע�⡣
	 */
	public ChartView() {
		super();
		initialize();
	}

	/**
	 * ��ȡ"��Ƭ��ʾ"���л���ť
	 * @added on 2008.11.17
	 * @return ��Ƭ��ʾ��ť
	 */
	public ButtonObject getLinkButton() {
		return m_linkButton;
	}
	
	public void addListener(Object objListener, Object objUserdata) {
		return;
	}

	private void assignAccsubj() {
		java.util.Vector tempVec = getChartModel().getAccsubjLists();
		java.util.Vector selectedVec = new java.util.Vector();
		for (int i = 0; i < tempVec.size(); i++) {
			AccsubjVO vo = (AccsubjVO) tempVec.elementAt(i);
			if (vo.getSelectedflag().booleanValue()) {
				selectedVec.addElement(new String[] { vo.getBeginYear(),
						vo.getBeginPeriod(), vo.getSubjcode() });
			}
		}
		if (selectedVec.size() == 0) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		String msg = "";
		for (int i = 0; i < selectedVec.size(); i++) {
			String[] paraStr = (String[]) selectedVec.elementAt(i);
			if (!canBeAssign(paraStr[0], paraStr[1]))
				msg += ", " + paraStr[2];
		}
		if (msg.length() > 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10081802", "UPP10081802-000105")/*
													  * @res
													  * "��ѡ���Ŀ�������ڼ����ڵ�ǰ��½�ڼ䣬���ܽ��з��䣡"
													  */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
							"UC000-0003069")/* @res "��Ŀ����" */
					+ ":" + msg.substring(2));
			return;
		}

		if (tempVec.size() > 0) {
			AccsubjVO[] vos = new AccsubjVO[tempVec.size()];
			tempVec.copyInto(vos);

			AssignAccsubjDlg dlg = new AssignAccsubjDlg(this, m_pk_glorgbook,
					m_pk_subjscheme, vos, getCurrPeriod()[0],
					getCurrPeriod()[1]);
			dlg.showModal();
		}

	}

	private void assignCopyAccsubj() {
		java.util.Vector tempVec = getChartModel().getAccsubjLists();
		java.util.Vector selectedVec = new java.util.Vector();
		for (int i = 0; i < tempVec.size(); i++) {
			AccsubjVO vo = (AccsubjVO) tempVec.elementAt(i);
			if (vo.getSelectedflag().booleanValue()) {
				selectedVec.addElement(new String[] { vo.getBeginYear(),
						vo.getBeginPeriod(), vo.getSubjcode() });
			}
		}
		if (selectedVec.size() == 0) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		String msg = "";
		for (int i = 0; i < selectedVec.size(); i++) {
			String[] paraStr = (String[]) selectedVec.elementAt(i);
			if (!canBeAssign(paraStr[0], paraStr[1]))
				msg += ", " + paraStr[2];
		}
		if (msg.length() > 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
			"UPP10081802-000141")/* @res "��ѡ���Ŀ�������ڼ����ڵ�ǰ��½�ڼ䣬���ܽ��п緽�����ƣ�"*/
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
							"UC000-0003069")/* @res "��Ŀ����" */
					+ ":" + msg.substring(2));
			return;
		}

		if (tempVec.size() > 0) {
			AccsubjVO[] vos = new AccsubjVO[tempVec.size()];
			tempVec.copyInto(vos);

			AssignCopyAccsubjDlg dlg = new AssignCopyAccsubjDlg(this,
					m_pk_glorgbook, m_pk_subjscheme, vos, ClientEnvironment
							.getInstance().getDate());
			dlg.showModal();
		}

	}

	/**
	 * �ж������ڼ�ΪvoYear + voPeriod�Ŀ�Ŀ�Ƿ�������䡣 �������ڣ�(2004-6-8 10:00:16)
	 * 
	 * @return boolean
	 * @param voYear
	 *            java.lang.String
	 * @param voPeriod
	 *            java.lang.String
	 */
	private boolean canBeAssign(String voYear, String voPeriod) {
		//�����Ŀ�������ڼ� ���ڻ��ߵ��� ��ǰ��½�ڼ䣬���������
		return (voYear + voPeriod).compareTo(getCurrPeriod()[0]
				+ getCurrPeriod()[1]) <= 0;
	}

	/**





















	 * 
	 * �õ����е���������ʽ���� Ҳ���Ƿ������ж����������ı��ʽ
	 *  
	 */
	public java.lang.String[] getAllDataItemExpress() {
		return new String[] { "subjcode", "subjname", "dispname", "subjtype",
				"balanorient", "subjlev", "sealflag", "unit", "asstype" };
	}

	/*
	 * �������ֱ��ģ��õ��Ľ����һ���������Ķ��巽ʽ��ͬ�� ��������
	 * 
	 * (1) ÿһ��������(�Ʊ��˳���) ����������չ,û���κ�������ϵ
	 * ------------------------------------------ ���\��Ŀ | ��Ŀ01 | ��Ŀ02
	 * ---------------------------------- (����) �� | (��Ŀ01)�� | (��Ŀ02)��
	 * ------------------------------------------ �Ʊ���: (�Ʊ���)
	 * 
	 * (2) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ) ------------------------ ���\��Ŀ | (��Ŀ) ��
	 * ------------------------ (����) �� | (���) ----------------------- �Ʊ���: (�Ʊ���)
	 * 
	 * (3) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ ����) ------------------------ ���\��Ŀ |
	 * (��Ŀ) �� ------------------------ (����) �� | (���) ------------------------
	 * �Ʊ���: (�Ʊ���)
	 * 
	 * ��ӡ���: -------------------------------- ���\��Ŀ | ��Ŀ1 | ��Ŀ2
	 * -------------------------------- 1999 | 100 | 400 2000 | 200 | 500 3001 |
	 * 300 | 600 -------------------------------- �Ʊ���: xxx
	 */
	public java.lang.String[] getAllDataItemNames() {
		return null;
	}

	/**
	 * ---------------------------------------------- ����˵���� �˴����뷽��˵����
	 * 
	 * ���룺 <|>
	 * 
	 * ����� �˴��������˵��
	 * 
	 * �쳣�� ���ܲ������쳣
	 * 
	 * �������ڣ�(2003-7-24 9:46:55) ----------------------------------------------
	 * 
	 * @return nc.ui.bd.b02.AssortDlg
	 */
	public AssortDlg getAssortDlg() {
		if (m_assortdlg == null) {
			m_assortdlg = new AssortDlg(this, m_pk_glorgbook, m_pk_subjscheme);
		}
		return m_assortdlg;
	}

	/**
	 * 
	 * @version (00-6-8 16:17:27)
	 * 
	 * @return ButtonObject[]
	 */
	public ButtonObject[] getButtons() {
		//		m_sort.removeAllChildren();
		//		m_sort.addChildButton(m_Localizer);
		//		m_sort.addChildButton(m_Assort);

		m_modButton.removeAllChildren();
		m_modButton.addChildButton(m_alterButton);
		m_modButton.addChildButton(m_amendButton);

		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		if (pk_corp.equals("0001"))

			m_arryCurrentButtons = new ButtonObject[14];
		else
			m_arryCurrentButtons = new ButtonObject[13];

		int index = 0;
		m_arryCurrentButtons[index++] = m_addButton;
		m_arryCurrentButtons[index++] = m_modButton;
		m_arryCurrentButtons[index++] = m_copyButton;
		m_arryCurrentButtons[index++]= m_queryButton;
		m_arryCurrentButtons[index++] = m_delButton;
		m_arryCurrentButtons[index++] = m_refreshButton;
		m_arryCurrentButtons[index++] = m_linkButton;
		//
		m_arryCurrentButtons[index++] = m_selectAllButton;
		m_arryCurrentButtons[index++] = m_selectNoneButton;
		m_arryCurrentButtons[index++] = m_assignAccsubjButton;
		if (pk_corp.equals("0001"))
			m_arryCurrentButtons[index++] = m_assignCopyButton;

		m_arryCurrentButtons[index++] = m_editionButton;
		m_arryCurrentButtons[index++] = m_printButton;
		m_arryCurrentButtons[index++]=m_exportButton;

		return m_arryCurrentButtons;
	}

	private ChartModel getChartModel() {
		if (m_ChartModel == null) {
			m_ChartModel = new ChartModel((IPara) this);
			m_ChartModel.addPropertyChangeListener(getChartUi());
		}
		return m_ChartModel;
	}

	/**
	 * ���� ChartUi ����ֵ��
	 * 
	 * @return nc.ui.bd.b02.ChartUi
	 */
	public ChartUi getChartUi() {
		if (ivjChartUi == null) {
			try {
				ivjChartUi = new nc.ui.bd.b02.ChartUi();
				ivjChartUi.setName("ChartUi");
				
				//˫���л�����Ƭ����
				ivjChartUi.getAccsubjList().getTable().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()>1) {
							showDetail();
						}
					}
					
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjChartUi;
	}

	/**
	 * ��ǰ��¼�Ļ���ڼ� �������ڣ�(2001-4-28 11:40:19)
	 * 
	 * @return java.lang.String
	 */
	public nc.ui.pub.ClientEnvironment getClientEnvironment() {
		return super.getClientEnvironment();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-10 11:05:25)
	 * 
	 * @return nc.vo.bd.b02.CorpKindInfo
	 * @param pk_corp
	 *            java.lang.String
	 */
	public static CorpKindInfo getCorpKindInfo(String pk_corp) {
		String pk_balanceKind = ICorpKind.CORPKIND_BALANCE; //������������
		boolean bBalanceCenter = false;
		boolean bSuperBalanceCenter = false;
		try {
			if (pk_corp != null && (!pk_corp.equals("0001"))) {
				nc.vo.bd.CorpVO corp = nc.ui.bd.CorpBO_Client
						.findByPrimaryKey(pk_corp);
				if (corp != null) {
					String corpKind = corp.getpkCorpkind();
					if (corpKind.equals(pk_balanceKind)) {
						bBalanceCenter = true;
						if (corp.getFathercorp() == null) {
							bSuperBalanceCenter = true;
						} else {
							nc.vo.bd.CorpVO fathercorp = nc.ui.bd.CorpBO_Client
									.findByPrimaryKey(corp.getFathercorp());
							if (fathercorp != null) {
								String fatherCorpKind = fathercorp
										.getpkCorpkind();
								if (fatherCorpKind != null
										&& fatherCorpKind
												.equals(pk_balanceKind)) {
									bSuperBalanceCenter = false;
								} else
									bSuperBalanceCenter = true;
							} else {
								bSuperBalanceCenter = true;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		}
		return new CorpKindInfo(bBalanceCenter, bSuperBalanceCenter);
	}

	/**
	 * 
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2 ���� null : û������ ���� 1 : �������� ���� 2 : ˫������
	 *  
	 */
	public java.lang.String[] getDependentItemExpressByExpress(
			java.lang.String itemName) {
		return null;
	}

	public java.lang.String[] getItemValuesByExpress(
			java.lang.String itemExpress) {
		java.util.Vector tempVec = getChartModel().getAccsubjLists();
		String ret[] = new String[tempVec.size()];
		for (int i = 0; i < tempVec.size(); i++) {
			AccsubjVO vo = (AccsubjVO) tempVec.elementAt(i);
			if (itemExpress.equals("subjcode")) {
				ret[i] = vo.getSubjcode();
			} else if (itemExpress.equals("subjname")) {
				ret[i] = vo.getSubjname();
			} else if (itemExpress.equals("dispname")) {
				ret[i] = vo.getDispname();
			} else if (itemExpress.equals("subjtype")) {
				ret[i] = vo.getSubjtypename();
			} else if(itemExpress.equals("remcode")) {
				ret[i] = vo.getRemcode();
			} else if(itemExpress.equals("endflag")) {
				boolean flag=vo.getEndflag().booleanValue();
				if(flag){
					ret[i] = "Y";
				}else{
					ret[i] = "N";
				}
				
			}else if (itemExpress.equals("balanorient")) {
				//if (vo.getBalanorient() == null) {
				//ret[i] = "��";
				//} else {
				if (vo.getBalanorient().intValue() == 1)
					ret[i] = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10081802", "UPP10081802-000043")/* @res "��" */;
				else if (vo.getBalanorient().intValue() == 2)
					ret[i] = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10081802", "UPP10081802-000044")/* @res "��" */;
				//}
			} else if (itemExpress.equals("subjlev")) {
				ret[i] = vo.getSubjlev().toString();
			} else if (itemExpress.equals("sealflag")) {
				if (vo.getSealflag() == null)
					ret[i] = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10081802", "UPP10081802-000106")/* @res "��" */;
				else
					ret[i] = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10081802", "UPP10081802-000107")/* @res "��" */;
			} else if (itemExpress.equals("unit")) {
				if (vo.getUnit() == null || vo.getUnit().trim().equals(""))
					ret[i] = "";
				else
					ret[i] = vo.getUnit();
			} else if (itemExpress.equals("asstype")) {
				StringBuffer tempStrBuf = new StringBuffer("");
				java.util.Vector tempAssVec = vo.getSubjass();
				if (tempAssVec != null&&tempAssVec.size()!=0)
				{
					for (int j = 0; j < tempAssVec.size(); j++)
					{
						String bdName=((SubjassVO) tempAssVec.elementAt(j)).getBdname();
						if(bdName==null||bdName.length()==0)continue;
						tempStrBuf.append("[" + ((SubjassVO) tempAssVec.elementAt(j)).getBdname().trim() + "]");
					}
					String strtemp = tempStrBuf.toString();
					ret[i] = strtemp;
				}
				else
					ret[i] = "";

			} else if (itemExpress.equals("beginperiod")) {
				ret[i] = vo.getBeginYear() + "." + vo.getBeginPeriod();
			} else if (itemExpress.equals("endperiod")) {
				ret[i] = (vo.getEndYear() == null) ? "" : (vo.getEndYear()
						+ "." + vo.getEndPeriod());
			} else if (itemExpress.equals("createperiod")) {
				ret[i] = vo.getCreateYear() + "." + vo.getCreatePeriod();
			}
		}
		return ret;
	}

	/**
	 * ---------------------------------------------- ����˵���� ��ȡ��ѯ�Ի���
	 * ----------------------------------------------
	 * 
	 * @return nc.ui.bd.b02.LocalizerDlg
	 */
	//	public LocalizerDlg getLocalizer() {
	//		if (m_localizerdlg == null) {
	//			m_localizerdlg = new LocalizerDlg(this);
	//		}
	//		return m_localizerdlg;
	//	}
	//
	/*
	 * ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public java.lang.String getModuleName() {
		return "10081802";
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-12 14:26:52)
	 * 
	 * @return nc.ui.bd.b00.SubjAlterDlg
	 */
	private nc.ui.bd.b00.SubjAlterDlg getSubjAlterDlg() {
		if (m_subjAlterDlg == null) {
			m_subjAlterDlg = new nc.ui.bd.b00.SubjAlterDlg(this,
					m_pk_glorgbook, m_pk_subjscheme, m_pk_periodscheme);
			
		}
		return m_subjAlterDlg;
	}

	private nc.ui.bd.b00.SubjEditionDlg getSubjEditionDlg() {
		if (m_subjEditionDlg == null) {
			m_subjEditionDlg = new nc.ui.bd.b00.SubjEditionDlg(this);
			m_subjEditionDlg.setGlorgbookInfo(m_pk_glorgbook, m_pk_subjscheme,
					m_pk_periodscheme);
		}
		return m_subjEditionDlg;
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	private nc.ui.pub.beans.UITable getTable() {
		nc.ui.pub.beans.UITable table = (nc.ui.pub.beans.UITable) (getChartUi()
				.getAccsubjList().getTable());
		return table;
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	private nc.ui.pub.beans.table.VOTableModel getTableModel() {
		nc.ui.pub.beans.table.VOTableModel vm = (nc.ui.pub.beans.table.VOTableModel) (getChartUi()
				.getAccsubjList().getTable().getModel());
		return vm;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
				"UPP10081802-000095")/* @res "��ƿ�Ŀ����" */;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnection() {

		getTable().getSelectionModel().addListSelectionListener(this);
	}

	private void processLoginInfo(String pk_glorgbook, String pk_subjscheme,
			String pk_periodscheme) {
		m_pk_glorgbook = pk_glorgbook;
		m_pk_subjscheme = pk_subjscheme;
		m_pk_periodscheme = pk_periodscheme;
		try {
			IUAPQueryBS iIUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName()); 
			if (pk_glorgbook.equals("0001")) {
				m_isGroup = true;
				if (m_pk_subjscheme == null) {
//					m_pk_subjscheme = ISubjConstants.PK_BASE_ACCSUJBSCHEME;
					m_pk_periodscheme = ISubjConstants.PK_BASE_ACCPERIODSCHEME;

//					getChooseRefPane().setPK(m_pk_subjscheme);
				} else {
					m_pk_periodscheme = ((SubjschemeVO) iIUAPQueryBS
							.retrieveByPK(SubjschemeVO.class,
									m_pk_subjscheme)).getPk_accperiodscheme();
				}
			
			} else {
				GlorgbookVO orgbookVO = BDGLOrgBookAccessor
						.getGlOrgBookVOByPrimaryKey(m_pk_glorgbook);
				if (orgbookVO == null) {
					throw new Exception("can not find" + m_pk_glorgbook
							+ " in BDGLOrgBookAccessor");
				}
				GlbookVO bookVO = (GlbookVO) iIUAPQueryBS.retrieveByPK(
						GlbookVO.class, orgbookVO.getPk_glbook());

				m_pk_subjscheme = bookVO.getPk_subjscheme();
				m_pk_periodscheme = bookVO.getPk_accperiodscheme();

//				if (m_pk_periodscheme == null)
//					m_pk_periodscheme = ISubjConstants.PK_BASE_ACCPERIODSCHEME;


			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, null, "error: processLoginInfo_"
					+ pk_glorgbook + "_" + pk_subjscheme + "_"
					+ pk_periodscheme);
		}

		getClientEnvironment().putValue(IPara.KEY_ACCSUBJ_PK_GLORGBOOK,
				m_pk_glorgbook);
		if(m_pk_subjscheme!=null){
		getClientEnvironment().putValue(IPara.KEY_ACCSUBJ_PK_SUBJSCHEME,
				m_pk_subjscheme);
		}
		getClientEnvironment().putValue(IPara.KEY_ACCSUBJ_PK_ACCPERIODSCHEME,
				m_pk_periodscheme);

		if (m_cardView != null) {
			((CardView) m_cardView).setLoginInfo(m_pk_glorgbook,
					m_pk_subjscheme, m_pk_periodscheme);
		}
		

		m_assortdlg = null;
		m_subjAlterDlg = null;
		m_subjEditionDlg = null;
		m_currPeriod = null;
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initData() {
		try {
		String pk_corp =getClientEnvironment().getCorporation().getPk_corp();
		if (pk_corp.equals("0001")) {
			processLoginInfo(pk_corp, null, null);
		} else {
			String pk_glorgbook = null;
			GlorgbookVO loginVO = (GlorgbookVO) ClientEnvironment.getInstance()
					.getValue(ClientEnvironment.GLORGBOOKPK);
			if (loginVO == null) {
				pk_glorgbook = BDGLOrgBookAccessor
						.getDefaultPk_GLOrgBook(pk_corp);
			} else {
				pk_glorgbook = loginVO.getPrimaryKey();
			}
			if (pk_glorgbook == null) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10081802",
								"UPP10081802-000108")/* @res "���Ƚ�����������˲�" */
						+ ".");
				return;
			}
		filterGlorgbookRefModel(pk_corp);
		processLoginInfo(pk_glorgbook, null, null);
		}

		m_pk_user = getClientEnvironment().getUser().getPrimaryKey();
	
		if(m_pk_subjscheme==null){
			return;
		}
			IUAPQueryBS iIUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName()); 
			SubjschemeVO schemeVO = (SubjschemeVO) iIUAPQueryBS
					.retrieveByPK(SubjschemeVO.class, m_pk_subjscheme);
			if (schemeVO == null) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, null,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10081802",
								"UPP10081802-000109")/* @res "δ�鵽���ÿ�Ŀ����" */
								+ ".");
				return;
			}
			int controlLevel = schemeVO.getControllevel().intValue();
			m_grpctrl = (controlLevel != 0);
			setCodeRule(pk_corp, schemeVO);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		// user code end
	}

	private void setCodeRule(String pk_corp, SubjschemeVO schemeVO) throws BusinessException {
		codeRule=schemeVO.getSubjcoderule();
		if (!pk_corp.equals("0001")) {
			ISysInitQry sysInitQry=(ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
		Object param=sysInitQry.getParaByPk_org(
						OrgnizeTypeVO.ZHUZHANG_TYPE, m_pk_glorgbook,
						INITCODE, new UFBoolean(false));
		codeRule=param==null?"":param.toString();
		}
		getChartUi().setCodeRule(codeRule);
	}

	/**
	 * @param pk_corp
	 * @throws BusinessException
	 */
	private void filterGlorgbookRefModel(String pk_corp)
			throws BusinessException {
		GlorgVO glorgVO = BDGLOrgBookAccessor.getPk_GLOrg(pk_corp);
		if (glorgVO == null)
			return;
		IGLOrgBookAcc iIGLOrgBookAcc = (IGLOrgBookAcc) NCLocator.getInstance()
				.lookup(IGLOrgBookAcc.class.getName());
		GlorgVO[] subGlorgVOs = iIGLOrgBookAcc.getSubGLOrgVOsByPk(glorgVO
				.getPrimaryKey(), "entitytype=1 and pid_glorg='"
				+ glorgVO.getPrimaryKey() + "'", true);
		if (subGlorgVOs == null || subGlorgVOs.length == 0)
			return;

		StringBuffer sql = new StringBuffer();

		for (int i = 0; i < subGlorgVOs.length; i++) {
			sql.append(",'" + subGlorgVOs[i].getPrimaryKey() + "'");
		}
		sql.deleteCharAt(0);

		String wherePart = "pk_glorg in (" + sql.toString() + ")";
		((AbstractRefTreeModel) getChooseRefPane().getRefModel())
				.setClassWherePart(wherePart);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("MainPanel");
			setLayout(new java.awt.BorderLayout());
			setSize(774, 419);
			add(getChartUi(), BorderLayout.CENTER);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initData();
		add(getNorthPanel(), BorderLayout.NORTH);
		initConnection();
		getChartUi().setChartModel((IChartModel) getChartModel());
		getChartUi().addSortList(getChartModel().getAccsubjLists());
		loadSubjectData();

		// user code end
	}

	private UIPanel getNorthPanel() {
		if (m_northPanel == null) {
			m_northPanel = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_northPanel.setLayout(layout);

			m_northPanel.setPreferredSize(new Dimension(300, 30));
			UILabel label = new UILabel(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10080418", "UPT10080418-000029")/*
																  * @res
																  * "�����˲�ѡ��"
																  */);
			if (m_isGroup)
				label.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10081800", "UPT10081800-000011")/* @res "��Ŀ����ѡ��" */);
			label.setSize(150, 22);
			label.setLocation(20, 5);
			m_northPanel.add(label);

			m_northPanel.add(getChooseRefPane());
		}
		return m_northPanel;
	}

	private UIRefPane getChooseRefPane() {
		if (m_choosePane == null) {
			m_choosePane = new UIRefPane();
			if (m_isGroup) {
				m_choosePane.setRefNodeName("��Ŀ����");
				m_choosePane.setPK(m_pk_subjscheme);
			} else {
				m_choosePane.setRefNodeName("�����˲�");
				m_choosePane.getRefModel().setSealedDataShow(true);
				m_choosePane.getRefModel().setPk_GlOrgBook(
						OrgnizeTypeVO.ZHUZHANG_TYPE, m_pk_glorgbook);
				//				String pk_corp = ClientEnvironment.getInstance()
				//						.getCorporation().getPrimaryKey();

				//				String wherePart = " where pk_entityorg ='"
				//						+ pk_corp
				//						+ "' or (pid_glorg in (select pk_glorg from bd_glorg where
				// pk_entityorg ='"
				//						+ pk_corp + "') and entitytype = 1)";
				//				m_choosePane.getRefModel().setWherePart(wherePart);
				m_choosePane.setPK(m_pk_glorgbook);
			}
			m_choosePane.setLocation(180, 5);
			m_choosePane.addValueChangedListener(this);
			m_choosePane.setButtonFireEvent(true);
		}
		return m_choosePane;

	}

	public Object invoke(Object objData, Object objUserData) {
		return null;
	}

	/*
	 * ���ظ��������Ƿ�Ϊ������ ������ɲ������㣻��������ֻ��Ϊ�ַ������� �硰������Ϊ�������������롱Ϊ��������
	 */
	public boolean isNumber(java.lang.String itemExpress) {
		return false;
	}

	private boolean isPeriodAllowEdit(AccsubjVO editVO) {
		boolean isEdit = true;
		if (editVO == null) {
			isEdit = false;
		} else {
			String voBeginPeriod = editVO.getBeginYear() + "-"
					+ editVO.getBeginPeriod();
			String currPeriod = getCurrPeriod()[0] + "-" + getCurrPeriod()[1];
			if (currPeriod.compareTo(voBeginPeriod) < 0) {
				isEdit = false;
			}
		}
		return isEdit;
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			ChartView aChartView;
			aChartView = new ChartView();
			frame.setContentPane(aChartView);
			frame.setSize(aChartView.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.ToftPanel �� main() �з����쳣");
			exception.printStackTrace(System.out);
		}
	}

	public void nextClosed() {
		System.out.println("Next Closed,What I can do for it?");
	}

	/**
	 * ---------------------------------------------- ����˵���� ���ݿ�Ŀ���ͽ���ɸѡ
	 * 
	 * ----------------------------------------------
	 */
	private void onAssort() {
		if (getAssortDlg().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			int style = getAssortDlg().getStyle();
			if (style > -1) {
				if (style == 0) {
					//ȫ����ѯ
					refresh();
				} else {
					// �������Ͳ�ѯ
					String typepk = getAssortDlg().getStylePk();
					try {
						getChartModel().QueryByCondition(typepk);
					} catch (Throwable e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {
		if (m_pk_glorgbook == null) return;
   
		
		if (getCurrPeriod() == null) {
			return;
		}
		try {
			if (bo == m_amendButton) { //��Ŀ�޸�
								
				if (getTable().getSelectedRow() < 0) {
					showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
					return;

				}
				showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
								"UPPuifactory-000067")/*
														 * @res "��ʼ���б༭���ݣ���ȴ�......"
														 */);
				if (!isPeriodAllowEdit((AccsubjVO) getChartModel().getCurrent())) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10081802", "UPP10081802-000097")+","
							+ MultiLangTrans.getTransStr("MP1",
									new String[] { nc.ui.ml.NCLangRes
											.getInstance()
											.getStrByID("10081802",
													"UC001-0000045") })); //"��ǰ��½�ڼ����ڿ�Ŀ�������ڼ䣬���ܽ����޸ģ�"
					return;
				}
				showNext();
				m_cardView.invoke(getChartModel().getCurrent(), OPERATOR_EDIT);
			} else if (bo == m_alterButton) { //��Ŀ���
				
				if (getTable().getSelectedRow() >= 0) {
					AccsubjVO vo = (AccsubjVO) getChartModel().getCurrent();
					if (vo != null) {
						getSubjAlterDlg().setAlterSubj(vo.getPk_accsubj());
					}
				}
				if(getCurrPeriod()==null||getCurrPeriod().length==0)return;
				int result = getSubjAlterDlg().showModal();
				if (result == UIDialog.ID_OK) {
					nc.vo.bd.b00.SubjAlterVO vo = getSubjAlterDlg()
							.getSubjAlterVO();
					vo.setOperator(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
					vo.setTime(ClientEnvironment.getInstance().getDate());
					if (vo.getAlterType() == nc.vo.bd.b00.SubjAlterType.INT_ALTER_STOP) {
						getChartModel().stopSubj(vo);
						getChartModel().removeVO(vo.getAlterSubjVO());
						nc.ui.pub.beans.MessageDialog.showHintDlg(this, null,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"10081802", "UPP10081802-000112")/* "��Ŀ�ѱ�ͣ��" */
										+ ","
										+ nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("10081802",
														"UC000-0003072")/* "��Ŀ����" */
										+ ":"
										+ vo.getAlterSubjVO().getSubjcode());
					} else {
						showNext();
						m_cardView.invoke(vo, OPERATOR_ALTER);
					}
				}
			} else if (bo == m_linkButton) { //�л�
				showDetail();
			} else if (bo == m_addButton) { //����
				showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"uifactory", "UPPuifactory-000061")/*
																	 * @res
																	 * "��ʼ�������ӵ��ݣ���ȴ�......"
																	 */);
				if (getCurrPeriod() == null) {
					return;
				}
				
				showNext();
				m_cardView.invoke(null, OPERATOR_ADD);
			} else if (bo == m_copyButton) { //����
				if (getCurrPeriod() == null) {
					return;
				}
				
				showNext();
				m_cardView.invoke(getChartModel().getCurrent(), OPERATOR_ADD);
			} else if (bo == m_delButton) { //ɾ��
				
				if (getTable().getSelectedRow() < 0) {
					showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
					return;
				}				
				if (!isPeriodAllowEdit((AccsubjVO) getChartModel().getCurrent())) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10081802", "UPP10081802-000097")+","
							+ MultiLangTrans.getTransStr("MP1",
									new String[] { nc.ui.ml.NCLangRes
											.getInstance()
											.getStrByID("10081802",
													"UC001-0000039") })); //"��ǰ��½�ڼ����ڿ�Ŀ�������ڼ䣬���ܽ���ɾ����"
					return;
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory", "UPPuifactory-000070")/*
															 * @res
															 * "��ʼ���е���ɾ������ȴ�......"
															 */);
				if (verifyDelete(((AccsubjVO) getChartModel().getCurrent())
						.getSubjcode())) {
					getChartModel().delCurrent();
					onRefresh();
				}
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory", "UPPuifactory-000071")/* @res "����ɾ�����" */
				);
				
			} else if (bo == m_refreshButton) { //ˢ��
				onRefresh();
			} else if (bo == m_printButton) { //��ӡ
				print();
			} else if (bo == m_selectAllButton) { //ȫѡ
				selectAll();
			} else if (bo == m_selectNoneButton) { //ȡ��ȫѡ
				selectNone();
			} else if (bo == m_assignAccsubjButton) { //�����Ŀ(ȡ������)
		
		      assignAccsubj();
			} else if (bo == m_assignCopyButton) {
				assignCopyAccsubj();
			} 
			 else if (bo == m_editionButton) { //EditonQuery��Ŀ�汾��ѯ
				if (getTable().getSelectedRow() >= 0) {
					AccsubjVO vo = (AccsubjVO) getChartModel().getCurrent();
					if (vo != null)
						getSubjEditionDlg().setSubjPk(vo.getPk_accsubj());
				}

				int result = getSubjEditionDlg().showModal();
				if (result != UIDialog.ID_OK) {
					return;
				}
				getChartModel().querySubjEdition(
						getSubjEditionDlg().getSelectedSubjValue());
				m_refreshButton.setName(BUTTON_NAME_RETURN);
				setButtonStates(EDIT_STATE_UN_EDITABLE);
				EditionQuery = 1;


			}else if (bo == m_exportButton) {
				exportData();
			}else if(bo==m_queryButton) {
				
				onQuery();
			}

		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}
	}

	private void showDetail() {
		showNext();
		if(m_refreshButton.getName().equals(BUTTON_NAME_RETURN)){
			m_cardView.invoke(getChartModel().getCurrent(),
					OPERATOR_BROWSER_HISTORY);					
		}else{
		m_cardView.invoke(getChartModel().getCurrent(),
				OPERATOR_BROWSER);
		}
	}

	private void onRefresh() {
		showHintMessage(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000076")/*
												 * @res "��ʼ����ˢ�µ��ݣ���ȴ�......"
												 */);
		if (EditionQuery == 1) {
			m_refreshButton.setName(BUTTON_NAME_REFRESH);
		}
		refresh();
		setButtonStates(EDIT_STATE_UN_EDITABLE);
		EditionQuery = 0;
		showHintMessage(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000077")/* @res "����ˢ�²�������" */
						);
	}

	/**
	 * @throws BusinessException
	 */
	private void onQuery() throws Exception {
		checkSchemeNull();
		QueryConditionDLG queryDlg = getAccsubjQryDlg();		
		if (queryDlg.showModal() == UIDialog.ID_OK) {

			String whereSql = queryDlg.getWhereSQL();
			String condition = null;
			if (getCorpPrimaryKey().equals("0001"))
				condition = " and acc.pk_subjscheme='" + m_pk_subjscheme + "' and acc.pk_glorgbook='0001' and (acc.stoped='N' or acc.stoped is null)";
			else
				condition = " and acc.pk_glorgbook='" + m_pk_glorgbook + "'  and (acc.stoped='N' or acc.stoped is null)";
			if (whereSql == null || whereSql.length() == 0)
				whereSql = condition.substring(5);
			else {
				StringBuffer strbuf = new StringBuffer("("+whereSql+")");
				strbuf.append(condition);
				whereSql = strbuf.toString();
			}
			IAccsubjBusiQueryForeign iIAccsubjBusiQueryForeign = (IAccsubjBusiQueryForeign) NCLocator
					.getInstance().lookup(
							IAccsubjBusiQueryForeign.class.getName());
			AccsubjVO[] accsubjVOs = iIAccsubjBusiQueryForeign
					.findAccSubjVOsByWhereClauseWithSub(whereSql);
			if (accsubjVOs == null || accsubjVOs.length == 0)
			{	
                getChartModel().removeAll();
                getChartModel().firePropertyChange("Refresh", null, null);
                return;
            }
			getChartModel().setAccsubjLists(accsubjVOs);
		}
	}

	/**
	 * У���Ŀ�����ķǿ���
	 */
	private void checkSchemeNull() {
		if(m_pk_subjscheme==null){
			throw new BusinessRuntimeException(NCLangRes.getInstance().getStrByID("10081802", "UPP10081802-000169")
					/*����ѡ���Ŀ������*/);
		}
	}
	
	/**
	 * �Ƿ���ѡ���� ��Ŀ����
	 */
	boolean isSchemeSelected() {
		return m_pk_subjscheme != null;
	}

	private QueryConditionDLG getAccsubjQryDlg() {
		if(queryDlg==null){
		queryDlg = new QueryConditionDLG(this, null, getQryTempletInfo());
		queryDlg.registerFieldValueEelementEditorFactory(new IFieldValueElementEditorFactory() {

					public IFieldValueElementEditor createFieldValueElementEditor(
							FilterMeta meta) {
						if ("subjtype.pk_subjtype".equals(meta.getFieldCode())) {
							UIRefPane refPane = new UIRefpaneCreator(queryDlg
									.getQueryContext()).createUIRefPane(meta);
							AccsubjTypeRefModel accsubjTypeRefmodel = new AccsubjTypeRefModel(
									m_pk_glorgbook, m_pk_subjscheme);
							refPane.setRefModel(accsubjTypeRefmodel);
							return new RefElementEditor(refPane, meta
									.getReturnType());
						}
						if ("acc.balanorient".equals(meta.getFieldCode())) {
							UIComboBox comBox = new UIComboBox();
							comBox.addItems(new DefaultConstEnum[] {
											new DefaultConstEnum(new Integer(1),
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"10081802",
																	"UPP10081802-000043")),
											new DefaultConstEnum(new Integer(2),
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"10081802",
																	"UPP10081802-000044")) });
							return new DefaultFieldValueElementEditor(comBox);
						}
						return null;
					}
				});
		}
		return queryDlg;
	}
	

	private TemplateInfo getQryTempletInfo() {
		TemplateInfo tempinfo = new TemplateInfo();
		tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey());
		tempinfo.setFunNode("10081802");
		tempinfo.setUserid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		tempinfo.setBusiType(null);
		tempinfo.setNodekey(null);
		return tempinfo;
	}
	private void exportData() throws Exception{

		AccsubjVO[] accSubjVos=getSubjVosForExport();
		BillExportUtil.output(accSubjVos,"bsaccsubj",this); 
	}

	private AccsubjVO[] getSubjVosForExport() {
		Vector subjVec=  getChartModel().getAccsubjLists();
		if(subjVec==null||subjVec.size()==0)return null;
		  AccsubjVO[] accsubjVos=new  AccsubjVO[subjVec.size()];
		  subjVec.copyInto(accsubjVos);
		  
		  for(int i=0;i<accsubjVos.length;i++) {
		  	accsubjVos[i].setPk_corp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		  	Vector subjassVec=accsubjVos[i].getSubjass();
		  	if(subjassVec==null||subjassVec.size()==0)continue;
		  	for(int j=0;j<subjassVec.size();j++) {
		  	SubjassVO subjassVo=(SubjassVO)	subjassVec.get(j);
		  	if(subjassVo==null)continue;
		  	//U8ת������
		  	if(subjassVo.getPk_bdinfo().equals("00010000000000000001")) {
		  		accsubjVos[i].setFree1("Y");
		  	}else if(subjassVo.getPk_bdinfo().equals("00010000000000000071")){
		  		accsubjVos[i].setFree2("Y");
		  	}else if(subjassVo.getPk_bdinfo().equals("00010000000000000072")){
		  		accsubjVos[i].setFree3("Y");
		  	}else if(subjassVo.getPk_bdinfo().equals("00010000000000000002")){
		  		accsubjVos[i].setFree4("Y");
		  	}else if(subjassVo.getPk_bdinfo().equals("00010000000000000015")){
		  		accsubjVos[i].setFree5("Y");
		  		accsubjVos[i].setFree6("00010000000000000015");
		  	}else if(subjassVo.getPk_bdinfo().equals("00010000000000000021")){
		  		accsubjVos[i].setFree5("Y");
		  		accsubjVos[i].setFree6("00010000000000000021");
		  	}
		  	}
		  }
		  return accsubjVos;
	}

	/**
	 * ---------------------------------------------- ����˵���� ���б��㶨��һ��ָ���Ļ�ƿ�Ŀ��
	 * 
	 * ----------------------------------------------
	 */

	/*
	 * public void onLoaclizer() {
	 * 
	 * //for test // m_localizerdlg = null;
	 * 
	 * if (getLocalizer().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) { int
	 * index = getLocalizer().getSelectIndex(); int aim = index == 0 ?
	 * AccsubjKey.ACCSUBJ_CODE : AccsubjKey.ACCSUBJ_REMCODE; String condition =
	 * getLocalizer().getConditionStr();
	 * 
	 * int size = getTableModel().getRowCount(); int row = -1; AccsubjVO vo; for
	 * (int i = 0; i < size; i++) { vo = (AccsubjVO) getTableModel().getVO(i);
	 * try { if (vo.getValue(aim) != null &&
	 * vo.getValue(aim).toString().equals(condition)) { row = i; break; } }
	 * catch (Exception ex) { ex.printStackTrace(); } } if (row != -1) { // ������
	 * �б�ָ������ nc.ui.pub.beans.UITable table = getChartUi().getAccsubjList()
	 * .getTable(); table.getSelectionModel().setSelectionInterval(row, row);
	 * table.scrollRectToVisible(table.getCellRect(row, 0, true)); } } }
	 */

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-10 9:56:57)
	 */
	private void print() {
		nc.ui.pub.print.PrintEntry pEntry = new nc.ui.pub.print.PrintEntry(
				null, this);
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		pEntry.setTemplateID(pk_corp, "10081802", m_pk_user, null);
		if (pEntry.selectTemplate() < 0) {
			return;
		}
		pEntry.preview();
	}

	private void refresh() {
		try {
			getChartModel().QueryAll(m_pk_glorgbook, m_pk_subjscheme);
			int newRow = getTable().getSelectedRow();
			if (newRow > getTable().getRowCount()) {
				getTable().clearSelection();
			}
			//			else{
			//				getTable().getSelectionModel().setSelectionInterval(newRow,
			// newRow);
			//			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	public void removeListener(Object objListener, Object objUserdata) {
		return;
	}

	private void selectAll() {
		int[] rows = getTable().getSelectedRows();
		java.util.Vector tempVec = getChartModel().getAccsubjLists();
		int length = 0;
		if (rows == null || rows.length <= 1) {
			length = tempVec.size();
			for (int i = 0; i < tempVec.size(); i++) {
				((AccsubjVO) (tempVec.elementAt(i)))
						.setSelectedflag(new UFBoolean(true));
			}
		} else {
			length = rows.length;
			for (int i = 0; i < rows.length; i++) {
				((AccsubjVO) (tempVec.elementAt(convertRowNum(rows[i]))))
				.setSelectedflag(new UFBoolean(true));			
			}
		}
		//		showHintMessage("select rows: " + length);
		repaint();
	}

	private void selectNone() {
		java.util.Vector tempVec = getChartModel().getAccsubjLists();
       TableCellEditor cellEditor=   getTable().getCellEditor();  
       if(cellEditor!=null)
    	   cellEditor.stopCellEditing();
		int[] rows = getTable().getSelectedRows();
		int length = 0;
		if (rows == null || rows.length <= 1) {
			length = tempVec.size();
			for (int i = 0; i < tempVec.size(); i++) {
				((AccsubjVO) (tempVec.elementAt(i)))
						.setSelectedflag(new UFBoolean(false));
			}
		} else {
			length = rows.length;
			for (int i = 0; i < rows.length; i++) {
				((AccsubjVO) (tempVec.elementAt(convertRowNum(rows[i]))))
						.setSelectedflag(new UFBoolean(false));
			}
		}
		repaint();
	}
   /**
 * @param row
 * @return
 */
	private int convertRowNum(int row) {
		String subjcode = (String) getTable().getValueAt(row,
				ChartUi.SUBJCODE_COLUMN_NUM);
		String period = (String) getTable().getValueAt(row,
				ChartUi.STARTPEIROD_COLUMN_NUM);
		for (int j = 0; j < getChartModel().getSize(); j++) {
			AccsubjVO subjVO = (AccsubjVO) getChartModel().getVO(j);
			if (subjcode.equals(subjVO.getSubjcode())
					&& (period.equals(subjVO.getBeginYear() + "."
							+ subjVO.getBeginPeriod()))) {
				return j;

			}
		}
		return -1;
	}
   
	private void setButtonStates(String str) {
		int state = getChartModel().getState();

		if (state == 0) {
			m_addButton.setEnabled(true);
			m_modButton.setEnabled(true);
			m_copyButton.setEnabled(true);
			m_refreshButton.setEnabled(true);
			m_linkButton.setEnabled(true);
			//			m_sort.setEnabled(true);
			//			m_Localizer.setEnabled(true);
			//m_Assort.setEnabled(true);
			m_printButton.setEnabled(true);
			m_selectAllButton.setEnabled(true);
			m_selectNoneButton.setEnabled(true);
			m_alterButton.setEnabled(true);
			m_amendButton.setEnabled(true);
			m_editionButton.setEnabled(true);
			m_assignAccsubjButton.setEnabled(true);
			m_assignCopyButton.setEnabled(true);
            m_queryButton.setEnabled(true);
			if (str.equals(EDIT_STATE_EDITABLE)) {
				//m_addButton.setEnabled(true);
				m_delButton.setEnabled(true);
				//m_modButton.setEnabled(true);

			} else if (str.equals(EDIT_STATE_UN_EDITABLE)) {

				m_delButton.setEnabled(false);
				//m_modButton.setEnabled(false);
			}
			for (int i = 0; i < m_arryCurrentButtons.length; i++)
				updateButton(m_arryCurrentButtons[i]);
			updateButton(m_amendButton);
			updateButton(m_alterButton);
			//			updateButton(m_Localizer);
			//updateButton(m_Assort);
		} else {
			m_addButton.setEnabled(false);
			m_delButton.setEnabled(false);
			m_copyButton.setEnabled(false);
			m_modButton.setEnabled(false);
			m_refreshButton.setEnabled(true);
			m_linkButton.setEnabled(true);
			//			m_sort.setEnabled(false);
			//			m_Localizer.setEnabled(false);
			//m_Assort.setEnabled(false);
			m_printButton.setEnabled(true);
			m_selectAllButton.setEnabled(false);
			m_selectNoneButton.setEnabled(false);
			m_alterButton.setEnabled(false);
			m_amendButton.setEnabled(false);
			m_editionButton.setEnabled(true);
			m_assignAccsubjButton.setEnabled(false);
			m_assignCopyButton.setEnabled(false);
			m_queryButton.setEnabled(false);
			for (int i = 0; i < m_arryCurrentButtons.length; i++)
				updateButton(m_arryCurrentButtons[i]);
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-8-15 11:16:24)
	 * 
	 * @param p_obj
	 *            java.lang.Object
	 */
	public void showMe(IParent p_parent) {
		p_parent.getUiManager().removeAll();
		p_parent.getUiManager().add(this, this.getName());
		m_parent = p_parent;
	}

	private IUiPanel showNext() {
		checkSchemeNull();
		if (m_cardView == null) {
			m_cardView = (IUiPanel) m_parent.showNext("nc.ui.bd.b02.CardView");
			((CardView) m_cardView).setLoginInfo(m_pk_glorgbook,
					m_pk_subjscheme, m_pk_periodscheme);
		} else
			m_parent.showNext(m_cardView);

		m_cardView.addListener(getChartModel(), null);
		return m_cardView;
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		VOTableModel vm = getTableModel();
		nc.ui.pub.beans.UITable tb = getTable();
		int selectrow = getTable().getSelectedRow();
		if (selectrow >= 0 && selectrow < tb.getRowCount()) {
			AccsubjVO vo = (AccsubjVO) (vm.getVO(selectrow));
			//			if (m_grpctrl
			//					&& !(m_pk_glorgbook.equals(vo.getPk_create_glorgbook()))) {
			if (!m_pk_glorgbook.equals(vo.getPk_create_glorgbook())) {
				setButtonStates(EDIT_STATE_UN_EDITABLE);
			} else {
				setButtonStates(EDIT_STATE_EDITABLE);
			}
		} else {
			//			System.out
			//					.print("nc.ui.bd.b02.ChartView.valueChanged(ListSelectionEvent)
			// û��ѡ����\n");
		}
	}

	private boolean verifyDelete(String subjcode) {
		return MessageDialog.showOkCancelDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "����ɾ��" */,
						subjcode + " : "+nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "�Ƿ�ȷ��Ҫɾ��?" */
				, MessageDialog.ID_CANCEL) == UIDialog.ID_OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.pub.beans.ValueChangedListener#valueChanged(nc.ui.pub.beans.ValueChangedEvent)
	 */
	public void valueChanged(ValueChangedEvent event) {
		String value = ((UIRefPane) event.getSource()).getRefPK();
		boolean haschanged = false;
		if (m_isGroup) {
			if (value == null)
				((UIRefPane) event.getSource()).setPK(m_pk_subjscheme);
			else if (m_pk_subjscheme == null || !m_pk_subjscheme.equals(value)) {
				haschanged = true;
				queryDlg=null;
				m_pk_subjscheme = value;
			}
			//���Ŀ�Ŀ�������
			SubjschemeVO subjvo= BDGLOrgBookAccessor.getSubjSchemeVOByPk_subjscheme(m_pk_subjscheme);
		    getChartUi().setCodeRule(subjvo.getSubjcoderule());
		    
		} else {
			if (value == null)
				((UIRefPane) event.getSource()).setPK(m_pk_glorgbook);
			else if (m_pk_glorgbook == null || !m_pk_glorgbook.equals(value)) {
				haschanged = true;
				queryDlg=null;
				m_pk_glorgbook = value;
				m_pk_subjscheme = BDGLOrgBookAccessor
						.getPk_SubjSchemeByPk_OrgBook(m_pk_glorgbook);
			}
           //���Ŀ�Ŀ�������
			SubjschemeVO subjvo= BDGLOrgBookAccessor.getSubjSchemeVOByPk_GlOrgBook(m_pk_glorgbook);
		    getChartUi().setCodeRule(subjvo.getSubjcoderule());
		}
		if (haschanged && m_pk_glorgbook != null) {
			processLoginInfo(m_pk_glorgbook, m_pk_subjscheme, null);
		}

		refresh();

	}

	private String[] getCurrPeriod() {
		if (m_currPeriod == null) {
			m_currPeriod = getLoginPeriod(m_pk_periodscheme);
		}
		return m_currPeriod;
	}

	public static String[] getLoginPeriod(String pk_accperiodscheme) {
		String[] currPeriod = null;
		try {

			AccountCalendar calendar = AccountCalendar.getInstanceByPeriodScheme(pk_accperiodscheme);
			calendar.setDate(ClientEnvironment.getInstance().getDate());
			AccperiodVO periodVO = calendar.getYearVO();
			
			
			currPeriod = new String[2];
			if(periodVO==null) {
				MessageDialog.showErrorDlg(null, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10081802", "UPP10081802-000114")/*
																				    * @res
												s								    * "��ǰ��½������Ч�Ļ���ڼ䣬���顣"
																				    */);
				return null;
			}
			currPeriod[0] = periodVO.getPeriodyear();
			
			currPeriod[1] = calendar.getMonthVO().getMonth();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(null, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081802", "UPP10081802-000114")/*
																			    * @res
											s								    * "��ǰ��½������Ч�Ļ���ڼ䣬���顣"
																			    */);
			return null;
		}
		return currPeriod;
	}
	private void loadSubjectData() {

		if (!m_isGroup) {
			getChooseRefPane().setPK(m_pk_glorgbook);
			String glorgBookPk = getChooseRefPane().getRefPK();
			if (glorgBookPk != null && glorgBookPk.length() != 0)
				refresh();
		} else {
			refresh();
		}
	}
	
	/**
	 * Ϊ���ݵ�����ر��ṩ�ķ���
	 */
	protected void addNewForImport() {
		showNext();
		((CardView) m_cardView).addNewForImport();
	}

	@Override
	public boolean onClosing() {
		Vector v=getChartModel().getAccsubjLists();
		if(v!=null){
			v.removeAllElements();
		}
		return super.onClosing();
	}
	
	
}