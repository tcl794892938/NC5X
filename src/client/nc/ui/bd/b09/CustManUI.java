package nc.ui.bd.b09;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.trade.excelimport.IImportableEditor;
import nc.itf.trade.excelimport.ImportableInfo;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.area.IAreaClassAndAddressQry;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.sfapp.IBillcodeRuleQryService;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.b08.CubasdocBO_Client;
import nc.ui.bd.b08.CustDocUI;
import nc.ui.bd.batchupdate.BatchUpdateDlg;
import nc.ui.bd.custdoc.importable.CustdocBillAdjustor;
import nc.ui.bd.def.DefquoteQueryUtil;
import nc.ui.bd.pub.BDDocManageDlg;
import nc.ui.bd.pub.BillExportUtil;
import nc.ui.bd.pub.FinishEditEvent;
import nc.ui.bd.pub.FinishEditListener;
import nc.ui.bd.pub.IDirectlyAdd;
import nc.ui.bd.pub.dialog.Uif2Dialog;
import nc.ui.bd.ref.IRefQueryDlg;
import nc.ui.bd.util.IAssign;
import nc.ui.bd.util.UITreeToTree;
import nc.ui.bd.util.XTreePane;
import nc.ui.dahuan.exceltools.ExcelTools;
import nc.ui.dahuan.exceltools.ExcelUtils;
import nc.ui.dahuan.exceltools.IOUtils;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTabbedPaneTabChangeEvent;
import nc.ui.pub.bill.BillTabbedPaneTabChangeListener;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.general.PrintVODataSource;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.buttonstate.CardBtnVO;
import nc.ui.trade.buttonstate.FirstBtnVO;
import nc.ui.trade.buttonstate.LastBtnVO;
import nc.ui.trade.buttonstate.NextBtnVO;
import nc.ui.trade.buttonstate.PrevBtnVO;
import nc.ui.trade.buttonstate.ReturnBtnVO;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.trade.excelimport.InputItemCreator;
import nc.ui.trade.excelimport.inputitem.InputItemImpl;
import nc.vo.bd.BDMsg;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b07.AreaclVO;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAddrVO;
import nc.vo.bd.b08.CustAreaVO;
import nc.vo.bd.b08.CustBankVO;
import nc.vo.bd.b08.CustBasMapping;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.bd.b08.ICustConst;
import nc.vo.bd.b08.importable.checker.CustdocAggVOChecker;
import nc.vo.bd.b08.importable.processor.CustAddrVOProcessor;
import nc.vo.bd.b08.importable.processor.CustManVOProcessor;
import nc.vo.bd.b09.CbmVO;
import nc.vo.bd.b09.CbmVOAttrInfoQryUtil;
import nc.vo.bd.b09.CmdocVO;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.bd.b09.CustManVO;
import nc.vo.bd.def.DefVO;
import nc.vo.bd.log.BDBusinessLogUtil;
import nc.vo.bd.log.BDLogUtil;
import nc.vo.bd.log.OperateType;
import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.bd.loginfo.ErrlogmsgVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.billcodemanage.BillcodeRuleVO;
import nc.vo.pub.filemanage.BDAssociateFileUtil;
import nc.vo.pub.filemanage.IBDFileManageConst;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.util.tree.IOPCreator;
import nc.vo.util.tree.MethodGroup;
import nc.vo.util.tree.TreeDetail;
import nc.vo.util.tree.TreeOperationException;
import nc.vo.util.tree.XTreeModel;
import nc.vo.util.tree.XTreeNode;

/**
 * ���̹�����
 * 
 * �������ڣ�(2003-6-3 11:29:13)
 * 
 * @author��������
 */
public class CustManUI extends ToftPanel implements TreeSelectionListener,
		ListSelectionListener, BillEditListener, BillEditListener2,
		ActionListener, IAssign, IDirectlyAdd,IImportableEditor,ILinkMaintain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7935728431386592872L;

	private static final String MODULE_CODE="10080806";//�ڵ���
	
	/** �ɹ���Ϣ ���ݵ���ʹ�� */
	private static final String BUY_INFO = "(�ɹ���Ϣ)";
	/** ������Ϣ ���ݵ���ʹ�� */
	private static final String SALE_INFO = "(������Ϣ)";

	private XTreePane AreaTree = null;

	private UISplitPane SplitPane1 = null;

	private UIPanel CustPanel = null;

	private BillListPanel ListPanel = null;

	private BillCardPanel cardPanel = null;
	//�����ʻ�
	private Uif2Dialog custBankDialog;

	/**
	 * �˵���
	 */// by tcl
	private ButtonObject btn_zdydc = new ButtonObject("�������̵���","����������Ա����", 0, "����");

	// �ܹ�״̬
	private ButtonObject btn_Add = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "����" */, 1, "����"); /*-=notranslate=-*/

	private ButtonObject btn_Edit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "�޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "�޸�" */, 2, "�޸�"); /*-=notranslate=-*/

	private ButtonObject btn_Del = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "ɾ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "ɾ��" */, 3, "ɾ��"); /*-=notranslate=-*/

	private ButtonObject btn_Query = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000006")/* @res "��ѯ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000006")/* @res "��ѯ" */, 0, "��ѯ"); /*-=notranslate=-*/

	private ButtonObject btn_Upgrade = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000127")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000021")/* @res "��������������" */, 0, "����"); /*-=notranslate=-*/


	private ButtonObject btn_DocMan = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000126")/* @res "��������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000126")/* @res "��������" */, 5, "��������"); /*-=notranslate=-*/

	private ButtonObject btn_Ref = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "ˢ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "ˢ��" */, 5, "ˢ��"); /*-=notranslate=-*/

	private ButtonObject btn_Detail = new CardBtnVO().getButtonVO().buildButton();

	private ButtonObject btn_Print = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000007")/* @res "��ӡ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000007")/* @res "��ӡ" */, 0, "��ӡ"); /*-=notranslate=-*/

	private ButtonObject btn_Output = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000056")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000056")/* @res "����" */, 0, "����");

	// ��ϸ״̬
	private ButtonObject btn_Card_Add = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "����" */, 1, "����"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Edit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "�޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "�޸�" */, 2, "�޸�"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Del = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "ɾ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "ɾ��" */, 3, "ɾ��"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Explorer = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000021")/* @res "���" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000021")/* @res "���" */, 5, "���"); /*-=notranslate=-*/

	private ButtonObject btn_Card_First = new FirstBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Next = new NextBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Pre = new PrevBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Last = new LastBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Ref = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "ˢ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "ˢ��" */, 5, "ˢ��"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Back = new ReturnBtnVO().getButtonVO().buildButton();

	// �༭״̬
	private ButtonObject btn_Card_AddRow = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000012")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000012")/* @res "����" */, 5, "����"); /*-=notranslate=-*/

	private ButtonObject btn_Card_DelRow = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000013")/* @res "ɾ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000013")/* @res "ɾ��" */, 5, "ɾ��"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Save = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "����" */, 3, "����"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Mod_Save = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "����" */, 5, "����"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Cancel = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000008")/* @res "ȡ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000008")/* @res "ȡ��" */, 0, "ȡ��"); /*-=notranslate=-*/
	
	private ButtonObject btn_Batch_update = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206","UPP10081206-000012")/*@res "����"*/,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206","UPP10081206-000012")/*@res "����"*/, 2, "����"); /*-=notranslate=-*/
	
    private ButtonObject btn_BankAccount = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "�����˻�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "�����˻�" */, 5, "�����˻�");

	private ButtonObject btn_Card_BankAccount = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "�����˻�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "�����˻�" */, 5, "�����˻�"); 
	
	private ButtonObject[] list_btns ;

	private ButtonObject[] card_detail ;

	private ButtonObject[] card_edit_Add;

	private ButtonObject[] card_edit_Mod;

	//�Ƿ��뵵��
	private boolean isImport;

	/*
	 * ��ǰģ��״̬ 0���б� 1�� ��Ƭ
	 */
	int billState = 1;

	/*
	 * ��������
	 */
	XTreeModel areaModel;

	CustAreaVO areaVos[];

	TreeDetail detail;

	/*
	 * ����
	 */
	XTreeModel cacheModel;

	// ��ǰ�༭����
	CustManVO editVo = null;

	int hRow = -1;


	// ��λ��
	private String m_currency = null;

	//
	nc.vo.pub.lang.UFBoolean corpADD = null;

	// ��ѯ����
	private String sCurrentQueryCondition = null;

//  Ŀǰ�б��������ʾ�Ŀ����Ƿ�Ϊ��ѯ�õ������ݡ�
    private boolean isQueryCustOnList = false;
    
	int State = 0;
	
	private int billOperate = IBillOperate.OP_INIT; 

	// ��������
	class BMouseAdpater extends MouseAdapter {
		// ˫�����
		public void mouseClicked(MouseEvent e) {
			onMouseClicked(e);
		}
	}
	
	private BDAssociateFileUtil custFileUtil;

	//�Զ��� ���� �� ͳ����
	BillItem[] defComb = null;

	BillItem[] b_defComb = null;

	private String m_loginPkCorp = null;//��¼��ǰ��½��˾

	private String m_loginUserID = null;//��¼��ǰ��½�û�

	TreeDetail upgradeDetail;//����������TreeDetail

	private UITreeToTree upgradeDlg = null;//�ṩ��������������

	//��ʶ�Ƿ�ͨ�������ڵ�򿪸ýڵ�ֱ�����ӿ��̣������������水ť��ֱ���˳����̹�����档
	private boolean isDirectlyAdd = false;
   //�Ƿ��Զ�����
	private UFBoolean isAutoCode = null;
	//�Ƿ�ϺŲ���
	private UFBoolean isCodeFill;
	 //���ݺŶ���VO
	private BillCodeObjValueVO billCodeObjVO;
	
	private Map<String,UFDouble[]> moneyMap=new HashMap<String,UFDouble[]>();
	
	

  
	/**
	 * BillTest ������ע�⡣
	 */
	public CustManUI() {
		super();
		init();
	}


	/**
	 * Ӱ�䵥��ģ����û��ʵ�ֵļ��� -- ����ģ�����ƺ󣬷����ù��� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void actionPerformed(ActionEvent e) {
		JComponent editorComp = (JComponent) e.getSource();
		if (editorComp instanceof javax.swing.JComboBox) {
			//			BillEditEvent be = new BillEditEvent(e.getSource(), new Integer(
			//					((JComboBox) editorComp).getSelectedIndex()), editorComp
			//					.getName(), BillItem.HEAD);
			//afterEdit(be);
		} else if (editorComp instanceof javax.swing.JList) {
			BillEditEvent be = new BillEditEvent(e.getSource(), new Integer(
					((JList) editorComp).getSelectedIndex()), editorComp
					.getName(), BillItem.HEAD);
			afterEdit(be);
		}
	}

	/**
	 * �༭ǰ����. ��������:(2005-05-13 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(BillEditEvent e) {

		if (editVo != null
				&& editVo.getParentVO().getAttributeValue("pk_corp").equals(
						"0001")) {
			if ("0001".equals(getCardPanel().getBodyValueAt(e.getRow(),
					"pk_corp"))) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10080806", "UPP10080806-000081")/*
														  * @res
														  * "���ŷ������Ŀ���,�ڹ�˾�ڲ����޸ļ������õ����С�"
														  */);
				return false;
			} else if (e.getKey().equalsIgnoreCase("defflag")) {
				CustBankVO[] bvo = editVo.getBanks();
				if (bvo != null) {
					for (int i = 0; i < bvo.length; i++) {
						if ("0001".equals(bvo[i].getPk_corp())
								&& bvo[i].getDefflag() != null
								&& bvo[i].getDefflag().booleanValue())
							return false;
					}
				}
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000083")/* ��˾�Լ���ӵ��������ݡ� */);

		}
		return true;

	}

	/**
	 * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (State == 0) {
			return;
		}
		try {
			//���� Table_code ��������
			String table_code = e.getTableCode();
			String key = e.getKey();
			//��ͷ�༭
			if (table_code == null) {

				if (key.equals("custname")) {
					afterEditCustName(e);
				}				
				// ����������˱仯�Ժ�
				else if (e.getKey().equals("custflag")) {
					afterEditCustFlag(e);
				}
				//
				else if (e.getKey().equals("custstate")) {
                    //����ҵ��ҳǩ��׼���ڵĿɱ༭��
					setEditableOfRatifyDate();
				}
				// �����ܹ�˾����
				else if (e.getKey().equals("pk_cubasdoc1")) {
					afterEditPK_cubasdoc1();
				}
				// ��������-- �ڲ��ⲿ
				else if (e.getKey().equals("custprop")) {
					afterEditCustProp(e);
				}
				// �����־
				else if (e.getKey().equals("frozenflag")) {
					afterEditFrozenFlag(e,"frozendate");
				}
				else if (e.getKey().equals("b_frozenflag")) {
					afterEditFrozenFlag(e,"b_frozendate");
				}
				else if(e.getKey().equals("stockpriceratio")){
					afterEditStockPriceRatio();
				}else if(e.getKey().equals("prepaidratio")){
					checkAndCorrectRatio("prepaidratio",NCLangRes.getInstance().getStrByID("common","UC000-0004200")/*Ԥ�տ����*/,null);
				}else if(e.getKey().equals("innerctldays")){
					Object innerDay = getCardPanel().getHeadItem("innerctldays").getValueObject();
					if(innerDay != null && Double.parseDouble(innerDay.toString()) > Short.MAX_VALUE)
						getCardPanel().setHeadItem("innerctldays",Short.MAX_VALUE);
				}
			}
			// ����༭
			else {
					if (e.getKey().equals("defaddrflag")) {
						afterEditDefAddrFlag(e);
					}
					//������ַ�ظ����
					if (e.getKey().equals("addrname")) {
						afterEditAddrName(e);
					}
//				}
			}
		} catch (Exception ex) {
			handleException(ex);
		}

	}
	
	//���ݿ����ܹ�˾�����Ƿ���ֵ�����ơ����ڿ����ܹ�˾���ÿ��ơ��͡����ڿ����ܹ�˾���ڿ��ơ��Ŀɱ༭�ԡ�
	private void afterEditPK_cubasdoc1() {
		if(getCardHeadItemValue("custflag")!=null&&!getCardHeadItemValue("custflag").equals("1")){
			//�����ܹ�˾
			Object pk_cubasdoc1 = getCardHeadItemValue("pk_cubasdoc1");
			BillItem creditControl = cardPanel.getHeadItem("creditcontrol");
			BillItem accLimitControl = cardPanel.getHeadItem("acclimitcontrol");
			if (pk_cubasdoc1 == null) {
				creditControl.setValue(UFBoolean.FALSE);
				accLimitControl.setValue(UFBoolean.FALSE);
			}
			
			if(billOperate != IBillOperate.OP_ADD&&billOperate != IBillOperate.OP_EDIT) {
				creditControl.setEnabled(false);
				accLimitControl.setEnabled(false);
			}else {
				creditControl.setEnabled(pk_cubasdoc1!=null);
				accLimitControl.setEnabled(pk_cubasdoc1!=null);
			}
				
		}
	}

	/**
	 * Ԥ�տ����ֵ�Ϸ���У�顣
	 * @param field   �ֶ���
	 * @param fieldName  �ֶ���������Դ
	 * @param defaultValue Ĭ��ֵ
	 */
	private void checkAndCorrectRatio(String field, String fieldName,Integer defaultValue) {
		String value = getCardHeadItemValue(field);
		if(value != null && value.length() > 0 && (Integer.parseInt(value) > 100 || Integer.parseInt(value) < 0)){
			getCardPanel().setHeadItem(field,defaultValue);
			showErrorMessage(
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000533",null,new String[]{"[" + fieldName + "]",0 + ""})/*{0}����С��{1}*/
					+ "," +
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000540",null,new String[]{" ",100 + ""})/*{0}���벻����{1}*/
			);							
		}
	}

	private void afterEditAddrName(nc.ui.pub.bill.BillEditEvent e) throws ValidationException {
		Object temp = getCardPanel().getBillModel("ADDR")
				.getValueAt(e.getRow(), "addrname");
		if (temp == null || temp.toString().length() == 0)
			return;
		int RowCount = getCardPanel().getBillModel("ADDR")
				.getRowCount();
		if (RowCount == 1) {
			getCardPanel().getBillModel("ADDR").setValueAt(
					new Boolean(true), e.getRow(),
					"defaddrflag");
		}
		for (int j = 0; j < getCardPanel().getBillModel("ADDR")
				.getRowCount(); j++) {
			if (j == e.getRow())
				continue;
			if (getCardPanel().getBillModel("ADDR").getValueAt(
					j, "addrname") == null)
				continue;
			if (temp.equals(getCardPanel().getBillModel("ADDR")
					.getValueAt(j, "addrname"))) {
				getCardPanel().getBillModel("ADDR").setValueAt(
						null, e.getRow(), "addrname");
				showErrorMessage(
						MultiLangTrans
								.getTransStr(
										"MC1",
										new String[] { nc.ui.ml.NCLangRes
												.getInstance()
												.getStrByID(
														"10080806",
														"UPP10080806-000025") /*
																			   * @res
																			   * "�շ�����ַ����"
																			   */}));
			}
		}
	}

	private void afterEditDefAddrFlag(nc.ui.pub.bill.BillEditEvent e) {
		if (((Boolean) e.getValue()).booleanValue()) {
			// ȡ�������շ�����ַ Ĭ������
			BillModel addrModel = getCardPanel().getBillModel("ADDR");
			for (int i = 0; i < addrModel.getRowCount(); i++) {
				if (((Boolean) addrModel.getValueAt(i, "defaddrflag"))
						.booleanValue()) {
					addrModel.setValueAt(new Boolean(false), i, "defaddrflag");
					int old_rowState = addrModel.getRowState(i);
					if (old_rowState == BillModel.NORMAL)
						addrModel.setRowState(i, BillModel.MODIFICATION);
				}

			}
			addrModel.setValueAt(new Boolean(true), e.getRow(), "defaddrflag");
		}
	}


	private void afterEditStockPriceRatio() {
		String value = getCardHeadItemValue("stockpriceratio");
		if(value != null && value.length() > 0 && (Integer.parseInt(value) > 100 || Integer.parseInt(value) < 0)){
			String stockRationName = "[" + NCLangRes.getInstance().getStrByID("10080806","UPP10080806-000100") + "]";/*�������ۼ۱���*/
			getCardPanel().setHeadItem("stockpriceratio",new Integer(100));
			showErrorMessage(
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000533",null,new String[]{stockRationName,0 + ""})/*{0}����С��{1}*/
					+ "," +
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000540",null,new String[]{" ",100 + ""})/*{0}���벻����{1}*/
			);							
		}
	}

	private void afterEditFrozenFlag(nc.ui.pub.bill.BillEditEvent e,String field) {
		if (e.getValue().equals("true")) {
			getCardPanel().getHeadItem(field).setEnabled(
					true);
		} else {
			getCardPanel().getHeadItem(field).setEnabled(
					false);
			getCardPanel().getHeadItem(field).clearViewData();
		}
	}

	private void afterEditCustProp(nc.ui.pub.bill.BillEditEvent e) {
		boolean change = e.getOldValue() == null ? e.getValue() != null
				: !e.getOldValue().equals(e.getValue());
		if (change) {
			System.out.println("ѡ��������Ϊ��" + e.getValue().toString()
					+ "����" + e.getValue().getClass());

//			int rowCount = getCardPanel().getBillModel("BANK")
//					.getRowCount();
//			int[] row = new int[rowCount];
//			for (int i = 0; i < rowCount; i++) {
//				row[i] = i;
//			}
//			getCardPanel().getBillModel("BANK").delLine(row);
			if (e.getValue().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10080806", "UPP10080806-000024")/*"�ⲿ��λ" */)) {

				getCardPanel().getHeadItem("pk_corp1").setEnabled(
						false);
				getCardPanel().getHeadItem("pk_corp1").setValue(
						null);
				UICheckBox freecust=(UICheckBox) getCardPanel().getHeadItem("freecustflag").getComponent();
				freecust.setEnabled(true);

			} else {
				getCardPanel().getHeadItem("pk_corp1").setEnabled(
						getCardPanel().getHeadItem("custprop")
								.getComponent().isEnabled());
				UICheckBox freecust=(UICheckBox) getCardPanel().getHeadItem("freecustflag").getComponent();
				freecust.setEnabled(false);
				freecust.setSelected(false);

			}
		}
	}

	private void afterEditCustName(nc.ui.pub.bill.BillEditEvent e) {
		String name = e.getValue() == null ? "" : e.getValue()
				.toString();
		if (name != null && name.length() > 0) {
			if (name.length() > 40) {
				getCardPanel().setHeadItem("custshortname",
						name.substring(0, 40));
			} else {
				getCardPanel().setHeadItem("custshortname", name);
			}
		}
		getCardPanel().setHeadItem("copyname", name);
	}

	private void afterEditCustFlag(nc.ui.pub.bill.BillEditEvent e) {
		int index = 2;
		if (e.getValue()
				.equals(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("10080806",
										"UC000-0001589")/* @res "�ͻ�" */)) {
			index = 0;
		} else if (e.getValue().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10080806", "UC000-0000275")/*
													 * @res "��Ӧ��"
													 */)) {
			index = 1;
		}
		setCustVisaul(index);
	}


	/**
	 * ��ս���
	 */
	public void clearCardPanel() {
		CmdocVO vo = new CmdocVO();
		getCardPanel().setBillValueVO(vo);
		int len = defComb == null ? 0 : defComb.length;
		for (int i = 0; i < len; i++) {
			((JComboBox) defComb[i].getComponent()).setSelectedItem(null);
		}
		len = b_defComb == null ? 0 : b_defComb.length;
		for (int i = 0; i < len; i++) {
			((JComboBox) b_defComb[i].getComponent()).setSelectedItem(null);
		}

		btn_Card_AddRow.setVisible(true);
		btn_Card_DelRow.setVisible(true);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
				"UPP10080806-000026")/* @res "��˾�Խ�����" */);

	}

	/**
	 * -------------------------------------------------- ���ܣ� �жϹ�˾�Ƿ��������
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-8 14:31:23)
	 * --------------------------------------------------
	 */
	public boolean corpCanAdd() {
		if (corpADD == null) {
			try {
				corpADD = nc.ui.pub.para.SysInitBO_Client.getParaBoolean(
						getpk_corp(), "BD002");
			} catch (Exception ex) {
				Logger.error(ex.getMessage(),ex);
			}
		}
		return corpADD.booleanValue();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-4 14:33:34)
	 * 
	 * @return nc.ui.bd.util.XTreeModel
	 */
	public XTreeModel getAreaModel() {
		return areaModel;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-3 11:34:57)
	 * 
	 * @return nc.ui.bd.util.XTreePane
	 */
	public XTreePane getAreaTree() {
		if (AreaTree == null) {
			try {
				AreaTree = new XTreePane();
				AreaTree.setName("AreaTree");
				AreaTree.setTreeDetail(getTreeDetail());
				areaModel = (XTreeModel) getAreaTree().gettree().getModel();

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return AreaTree;
	}

	/**
	 * ��ȡ��������ģ�� �������ڣ�(2003-6-5 10:39:55)
	 * 
	 * @return nc.ui.bd.util.XTreeModel
	 */
	public XTreeModel getCacheModel() {
		if (cacheModel == null) {
			try {
				cacheModel = IOPCreator.createTreeModel(getTreeDetail());
			} catch (Exception ex) {
				Logger.error(ex.getMessage(),ex);
			}
		}
		return cacheModel;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-3 11:34:57)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	public BillCardPanel getCardPanel() {
		if (cardPanel == null) {
			try {
				cardPanel = new BillCardPanel();
				cardPanel.setName("CardPanel");
			} catch (Exception ex) {
				handleException(ex);
			}
			//			cardPanel.addBodyEditListener2(this);
			cardPanel.addBodyEditListener2("BANK", this);
		}
		return cardPanel;
	}

	/**
	 * ȡ�ñ���λ��
	 */
	private String getCurrency() {
		if (m_currency == null) {
			try {
				//��λ��
				m_currency = nc.ui.pub.para.SysInitBO_Client.getPkValue(
						getCorpPrimaryKey(), "BD301");
				if (m_currency == null)
					//���ű�λ��
					m_currency = nc.ui.pub.para.SysInitBO_Client.getPkValue(
							getCorpPrimaryKey(), "BD211");
			} catch (Exception e) {
				Logger.error(e.getMessage(),e);
			}
		}
		return m_currency;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-3 11:34:57)
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	public UIPanel getCustPanel() {
		if (CustPanel == null) {
			try {
				CustPanel = new UIPanel();
				CustPanel.setName("CustPanel");
				CustPanel.setLayout(new java.awt.BorderLayout());

				CustPanel.add(getListPanel(), "Center");
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return CustPanel;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-3 11:34:57)
	 * 
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	public BillListPanel getListPanel() {
		if (ListPanel == null) {
			try {
				ListPanel = new BillListPanel();
				ListPanel.setName("ListPanel");
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return ListPanel;
	}

	/**
	 * ��ȡ��ǰ��˾���� �������ڣ�(2003-6-19 16:47:39)
	 */
	public String getpk_corp() {
		return m_loginPkCorp;
	}
	public void setPk_corp(String newPk_corp){
		this.m_loginPkCorp = newPk_corp;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-3 11:34:57)
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	public UISplitPane getSplitPane1() {
		try {
			if (SplitPane1 == null) {
				SplitPane1 = new UISplitPane(1);
				SplitPane1.setName("UISplitPane1");
				SplitPane1.setDividerSize(3);
				SplitPane1.add(getAreaTree(), "left");
				SplitPane1.add(getCustPanel(), "right");
			}
		} catch (Exception ex) {
			handleException(ex);
		}
		return SplitPane1;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
				"UPP10080806-000027")/* @res "���̹�����" */;
	}
	
	private BDAssociateFileUtil getCustFileUtil() {
		if(custFileUtil==null) {
			custFileUtil=new BDAssociateFileUtil(IBDFileManageConst.CUST_FILEMANAGE_PATH);
		}
		return custFileUtil;
	}
	
	

	/**
	 * -------------------------------------------------- ���ܣ� ��������ϸ��
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-10-10 19:07:12)
	 * --------------------------------------------------
	 */
	public TreeDetail getTreeDetail() {
		if (detail == null) {
			// ָ��������ϸ����
			detail = new TreeDetail();
			{

				// ��˾�������͵�ָ��
				MethodGroup mg = new MethodGroup();
				try {

					mg.setKeyField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getPrimaryKey", null));
					mg.setAssKeyField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getPk_fatherarea", null));
					mg.setNameField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getAreaclname", null));
					mg.setSortCodeFiled(nc.vo.bd.b08.CustAreaVO.class
							.getMethod("getAreaclcode", null));
					mg.setHowDisplay(new boolean[] { false, true, true }); // ��ʾ�����������
					mg.setAimClass(nc.vo.bd.b08.CustAreaVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				MethodGroup cbmg = new MethodGroup();
				try {
					cbmg.setKeyField(nc.vo.bd.b09.CustManVO.class.getMethod(
							"getKey", null));
					cbmg.setAssKeyField(nc.vo.bd.b09.CustManVO.class.getMethod(
							"getAssKey", null));
					cbmg.setNameField(nc.vo.bd.b09.CustManVO.class.getMethod(
							"getCustName", null));
					cbmg.setSortCodeFiled(nc.vo.bd.b09.CustManVO.class
							.getMethod("getCustCode", null));
					cbmg.setHowDisplay(new boolean[] { false, true, true }); // ��ʾ�����������
					cbmg.setAimClass(nc.vo.bd.b09.CustManVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				// ��Ҫ
				detail.setMg(new MethodGroup[] { mg, cbmg });

				detail.setRootname(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10080806", "UC000-0001235")/* @res "��������" */);
			}
		}

		return detail;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(Throwable exception) {
         Logger.error(exception.getMessage(),exception);
         showHintMessage(exception.getMessage());
	}

	/**
	 * ��ʼ��ϵͳ
	 * 
	 * �������ڣ�(2003-6-3 11:30:02)
	 */
	public void init() {
		try {
			setName("BillTemplet");
			setSize(774, 419);
			add(getSplitPane1());
		} catch (Exception ex) {
			handleException(ex);
		}
		m_loginPkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
		m_loginUserID = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();

		getAreaTree().gettree().addTreeSelectionListener(this);

		//����״̬
		billState = 0;

		// ���ð�ť
		//btn_Query.removeAllChildren();
		//btn_Query.addChildButton(btn_Query_Set);
		//btn_Query.addChildButton(btn_Query_Repeat);
		btn_Card_Explorer.removeAllChildren();
		btn_Card_Explorer.setChildButtonGroup(new ButtonObject[] { btn_Card_First,
				btn_Card_Pre, btn_Card_Next, btn_Card_Last });
		list_btns = new ButtonObject[] { btn_Add, btn_Edit, btn_BankAccount ,btn_Batch_update ,btn_Del, btn_Query,
				btn_Ref, btn_Detail, btn_DocMan,btn_Print, btn_Upgrade,
				btn_Output,btn_zdydc };
		card_detail = new ButtonObject[] { btn_Card_Explorer, btn_Card_Add,
				btn_Card_Edit, btn_Card_BankAccount ,btn_Card_Del, btn_Card_Ref, btn_Card_Back };
		card_edit_Add = new ButtonObject[] { btn_Card_AddRow, btn_Card_DelRow,
				btn_Card_Save, btn_Card_Cancel };
		card_edit_Mod = new ButtonObject[] { btn_Card_AddRow, btn_Card_DelRow,
				btn_Card_Mod_Save, btn_Card_Cancel };
		setButtons(list_btns);

		initBill();
      
		try {
			//��ȡ����Ӧ�̹���ģ���Ƿ�����
			isFrozenEditable = !SFServiceFacility.getCreateCorpQueryService().isEnabled(m_loginPkCorp, "4002");
			// ��ȡ��������
			areaVos = CubasdocBO_Client.getCustArea(m_loginPkCorp,ClientEnvironment.getInstance().getUser().getPrimaryKey());
			getAreaModel().createTree(areaVos);
			getCacheModel().createTree(areaVos);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * -------------------------------------------------- ���ܣ� ��ʼ�����̵������Զ�����
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-8 14:31:23)
	 * --------------------------------------------------
	 * @param voUserDef 
	 */
	public void initBaseDef(BillData bd, DefVO[] voUserDef) {

		/*
		 * ���ҳǩ�����е���Ŀ������ʾ�����ҳǩ����ʾ
		 */
		if (voUserDef == null || voUserDef.length == 0) {
			System.out.println("���̵����Զ���Ϊ��");
		} else {
			// ���Ƚ����õ��Զ������Ϣװ�� -- ������ʾ���Զ�����Ϣ
			BillItem[] sd = bd.getHeadShowItems("B_DEF");
			int len = sd == null ? 0 : sd.length;
			for (int i = 0; i < len; i++) {
				sd[i].setShow(false);
			}
			for (int i = 0; i < voUserDef.length; i++) {
				if (voUserDef[i] != null) {
					BillItem itemDef = bd.getHeadItem("b_"
							+ voUserDef[i].getFieldName());
					itemDef.setShow(true);
					itemDef.setName(voUserDef[i].getDefname());

					// ͳ��
					if (voUserDef[i].getType().equals("ͳ��")) {
						itemDef.setDataType(BillItem.USERDEF);
						itemDef.setRefType(voUserDef[i].getDefdef()
								.getPk_bdinfo());
						itemDef.reCreateComponent();
                        //��������
						itemDef.setDataType(BillItem.UFREF);
						itemDef.setIsDef(true);
					}
					// ����
					else if (voUserDef[i].getType().equals("����")) {
						itemDef.setDataType(BillItem.DATE);
						itemDef.reCreateComponent();
					}
					// ��ע
					else if (voUserDef[i].getType().equals("��ע")) {

						itemDef.setDataType(BillItem.STRING);
						if (voUserDef[i].getLengthnum() != null) {
							itemDef.setLength(voUserDef[i].getLengthnum()
									.intValue());
						}
						itemDef.reCreateComponent();
					}
					// ����
					else if (voUserDef[i].getType().equals("����")) {
						itemDef.setDataType(BillItem.DECIMAL);
						itemDef.setLength(voUserDef[i].getLengthnum()
								.intValue());

						if (voUserDef[i].getDigitnum() != null
								&& voUserDef[i].getDigitnum().intValue() > 0) {
							itemDef.setDecimalDigits(voUserDef[i].getDigitnum()
									.intValue());
						} else {
							itemDef.setDecimalDigits(0);
						}
					}
					itemDef.setIsDef(true);
				}
			}
		}

	}

	/**
	 * ��ʼ��ģ�� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void initBill() {

		//String templetID = "0001AA1000000000KOHF";

		nc.vo.pub.bill.BillTempletVO cardTvo = getCardPanel().getTempletData(
				MODULE_CODE, null, m_loginUserID, m_loginPkCorp);

		// ����ģ��
		BillData billData = new BillData(cardTvo);

		// ��ʼ�� �Զ�����Ϣ��Ŀ
		initAllDef(billData);

		BillListData bld = new BillListData(cardTvo);

		// ��ʼ���б�
		hideListDef(bld);
		//��ʼ�б�
		bld.getHeadItem("pk_pricegroup").setShow(false);
		bld.getHeadItem("pk_pricegroupcorp").setShow(false);

		getCardPanel().setBillData(billData);
		getListPanel().setListData(bld);

		getCardPanel().setBodyMenuShow(false);

		//���ɽ��㵥λ�������б��ͷ��ѡ
		getListPanel().getHeadTable().getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// ��װ ����
		getListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getListPanel().getHeadTable().addMouseListener(new BMouseAdpater());
		getCardPanel().addBillEditListenerHeadTail(this);
		getCardPanel().addEditListener("ADDR", this);
		
		//�Զ�ִ�б༭��ʽ
		getCardPanel().setAutoExecHeadEditFormula(true);
		//���ͼ��
		getCardPanel().addDefaultTabAction(IBillItem.HEAD);
		getCardPanel().addDefaultTabAction(IBillItem.BODY);
		
		getCardPanel().addTabbedPaneTabChangeListener(new BillTabbedPaneTabChangeListener() {
			private ArrayList<Action> actions=getCardPanel().getDefaultTabActions(IBillItem.BODY);
			public void afterTabChanged(BillTabbedPaneTabChangeEvent e) {
				
				if(e.getBtvo().getTabcode().equals("BANK")) {
					btn_Card_AddRow.setEnabled(false);
					btn_Card_DelRow.setEnabled(false);
					for(Action action : actions) {
						if(action instanceof BillTableLineAction)
						   action.setEnabled(false);
					}
				}else {
					btn_Card_AddRow.setEnabled(true);
					btn_Card_DelRow.setEnabled(true);
					if(billOperate==IBillOperate.OP_ADD
							||billOperate==IBillOperate.OP_EDIT) {
					for(Action action : actions) {
						if(action instanceof BillTableLineAction)
						  action.setEnabled(true);
					}
					}
				}
				updateButtons();
			}
		},IBillItem.BODY);

	}


	private void initAllDef(BillData billData) {
		String[] defObjs=new String[]{"���̹�����","���̵���"};
		List<DefVO[]> defList = DefquoteQueryUtil.getInstance().queryDefusedVO(defObjs);
		initDef(billData,defList.get(0));
		initBaseDef(billData,defList.get(1));
	}

	private void hideListDef(BillListData bld) {
		String biname = "b_def";
		String corpDef = "def";
		for (int i = 1; i < 31; i++) {
			if(i < 21)
			  bld.getHeadItem(biname + i).setShow(false);
			if(bld.getHeadItem(corpDef + i) != null)
			  bld.getHeadItem(corpDef + i).setShow(false);
		}
	}


	/**
	 * ��ʼ���Զ�����
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 * @param voUserDef 
	 */
	public void initDef(BillData bd, DefVO[] voUserDef) {

		if (voUserDef == null || voUserDef.length == 0) {
			System.out.println("���̵����Զ���Ϊ��");
		} else {
			// ���Ƚ����õ��Զ������Ϣװ�� -- ������ʾ���Զ�����Ϣ
			BillItem[] sd = bd.getHeadShowItems("DEF");
			int len = sd == null ? 0 : sd.length;
			for (int i = 0; i < len; i++) {
				sd[i].setShow(false);
			}
			for (int i = 0; i < voUserDef.length; i++) {
				if (voUserDef[i] != null) {
					BillItem itemDef = bd.getHeadItem(voUserDef[i]
							.getFieldName());
					itemDef.setShow(true);
					itemDef.setName(voUserDef[i].getDefname());

					// �������� ���ò�ͬչʾ�߼�
					if (voUserDef[i].getType().equals("ͳ��")) {
						itemDef.setDataType(BillItem.USERDEF);
						itemDef.setRefType(voUserDef[i].getDefdef()
								.getPk_bdinfo());
						itemDef.reCreateComponent();
                        //��������
						itemDef.setDataType(BillItem.UFREF);
						itemDef.setIsDef(true);
					} else if (voUserDef[i].getType().equals("����")) {
						itemDef.setDataType(BillItem.DATE);
						itemDef.reCreateComponent();
					} else if (voUserDef[i].getType().equals("��ע")) {
						itemDef.setDataType(BillItem.STRING);
						if (voUserDef[i].getLengthnum() != null) {
							itemDef.setLength(voUserDef[i].getLengthnum()
									.intValue());
						}
						itemDef.reCreateComponent();
					} else if (voUserDef[i].getType().equals("����")) {
						itemDef.setDataType(BillItem.DECIMAL);
						itemDef.setLength(voUserDef[i].getLengthnum()
								.intValue());
						if (voUserDef[i].getDigitnum() != null
								&& voUserDef[i].getDigitnum().intValue() > 0) {
							itemDef.setDecimalDigits(voUserDef[i].getDigitnum()
									.intValue());
						} else {
							itemDef.setDecimalDigits(0);
						}
						itemDef.reCreateComponent();
					}
					itemDef.setIsDef(true);
				}
			}
		}
	}

	/**
	 * -------------------------------------------------- ���ܣ� �ⲿ����
	 * 
	 * ���룺 command : ������� param���͸������Ӧ�Ĳ���
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-3 14:24:27)
	 * --------------------------------------------------
	 * 
	 * @param command
	 *            int
	 * @param param
	 *            java.lang.Object
	 */
	public void invoke(int command, Object param) {
		switch (command) {
		case 1:
			// չ��һ��������Ϣ
			showCustInfo((CustManVO) param);
			break;
		default:
			System.out.print("nc.ui.bd.b09.CustManUI.invoke �����˴�����������");
		}
	}

	/**
	 * ��������ѯ condition == null �Ըõ�λ�����еĵ������в�ѯ��] condition != null �������Ľ��в�ѯ
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void loadQueryData(String condition) {
		try {
			CustManVO[] vos = CumandocBO_Client.QueryCust(condition,getpk_corp(),ClientEnvironment.getInstance().getUser().getPrimaryKey());
			int len = vos == null ? 0 : vos.length;
			Vector v = new Vector();
			for (int i = 0; i < len; i++) {
				// ������-- һ���̶���������ά��ͬһ����ȡ��ʽ
				if (!getCacheModel().containsKey(vos[i].getKey())) {
					getCacheModel().addNode(vos[i]);
				}
				// ����ȫ����ѯ�����
				if (condition == null) {
					getCacheModel().getNodeParent(vos[i].getKey()).setFlag(1);
				}
				//��ʾ
				CmdocVO cmdoc = vos[i].convert();
				CbmVO cbm = (CbmVO) cmdoc.getParentVO();
				boolean isSealed = false;
				if (cbm.getCmfirst() != null) {
					isSealed = !(cbm.getCmfirst().getSealflag() == null);
				}
				cbm.getCubase().setAttributeValue("sealflag_b",
						new nc.vo.pub.lang.UFBoolean(isSealed));

				v.addElement(cmdoc.getParentVO());
			}
			if (len == 0) {
				getListPanel().setHeaderValueVO(null);
			} else {
				CbmVO[] hvos = new CbmVO[v.size()];
				v.copyInto(hvos);
				getListPanel().setHeaderValueVO(hvos);
				getListPanel().getHeadBillModel().execLoadFormula();
				getListPanel().getHeadTable().clearSelection();
//				getListPanel().setEnabled(false);
			}

		} catch (Exception ex) {
			handleException(ex);
		}
		getListPanel().setBodyValueVO("BANK", null);
		getListPanel().setBodyValueVO("ADDR", null);

		editVo = null;
		hRow = -1;

	}

	/**
	 * -------------------------------------------------- ���ܣ�
	 * 
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-8 14:31:23)
	 * --------------------------------------------------
	 */
	public void newMethod() {
	}

	/**
	 * ����һ������ -- ��ս��桢����һЩԤ�õ����� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onAdd() {
		if (corpCanAdd() == false) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000044")/* @res "���ſ��ƣ���˾������ӿ���" */);
			return;
		}
		// ��ս���
		clearCardPanel();
		getCardPanel().addNew();
		onChange();
		setButtons(card_edit_Add);
		//setTitleText("��������");
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();
		
		billOperate = IBillOperate.OP_ADD;

		//Ԥ�õ�����
		setCardDefaultData();

		getCardPanel().transferFocusTo(0);

		

	}
	
	/**
	 * ���±��尴ť������
	 */
	private void updateBodyButtons() {
		String tableCode=getCardPanel().getCurrentBodyTableCode();	
		if(tableCode.equals("BANK")) {
			ArrayList<Action> bodyActions = getCardPanel().getDefaultTabActions(IBillItem.BODY);
			for(Action action : bodyActions) {
				if(action instanceof BillTableLineAction)
				   action.setEnabled(false);
			}
		}
	}



	/**
	 *  
	 */
	private void setCardDefaultData() {
		//	  Ԥ�õ�����
		XTreeNode node = getAreaTree().getSelectedNode();
		if (node != null) {
			getCardPanel().setHeadItem("pk_areacl", node.getPrimaryKey());
		}
		getCardPanel().setHeadItem("pk_corp",getpk_corp());
		if (getCardHeadItemValue("discountrate") == null
				|| getCardHeadItemValue("discountrate").length() == 0)
			getCardPanel().setHeadItem("discountrate", "100");
		if (getCardHeadItemValue("stockpriceratio") == null
				|| getCardHeadItemValue("stockpriceratio").length() == 0)
			getCardPanel().setHeadItem("stockpriceratio", "100");
		if (getCardHeadItemValue("b_discountrate") == null
				|| getCardHeadItemValue("b_discountrate").length() == 0)
			getCardPanel().setHeadItem("b_discountrate", "100");
		if (getCardHeadItemValue("pk_currtype1") == null
				|| getCardHeadItemValue("pk_currtype1").length() == 0)
			getCardPanel().setHeadItem("pk_currtype1", getCurrency());
		if (getCardHeadItemValue("b_pk_currtype1") == null
				|| getCardHeadItemValue("b_pk_currtype1").length() == 0)
			getCardPanel().setHeadItem("b_pk_currtype1", getCurrency());
		if (getCardHeadItemValue("custprop") == null
				|| getCardHeadItemValue("custprop").length() == 0
				|| getCardHeadItemValue("custprop").equals("0")) {
			getCardPanel().setHeadItem("custprop", "0");
			getCardPanel().getHeadItem("pk_corp1").setEnabled(false);
			getCardPanel().setHeadItem("pk_corp1", null);
		}
		if (getCardHeadItemValue("custflag") == null
				|| getCardHeadItemValue("custflag").length() == 0)
			getCardPanel().setHeadItem("custflag", "0");		
		getCardPanel().getHeadItem("b_developdate").setValue(
				getBusinessDate());
		getCardPanel().getHeadItem("developdate").setValue(
				getBusinessDate());
		getCardPanel().getHeadItem("frozenflag").setEnabled(isFrozenEditable);
		getCardPanel().getHeadItem("b_frozenflag").setEnabled(isFrozenEditable);
		if (getCardHeadItemValue("frozenflag") == null
				|| getCardHeadItemValue("frozenflag")
						.equalsIgnoreCase("false"))
			getCardPanel().getHeadItem("frozendate").setEnabled(false);
		if (getCardPanel().getHeadItem("b_frozenflag") == null
				|| getCardHeadItemValue("b_frozenflag")
						.equalsIgnoreCase("false"))
			getCardPanel().getHeadItem("b_frozendate").setEnabled(false);

		getCardPanel().getHeadItem("sealflag_b").setEnabled(false);
//		if (getCardHeadItemValue("acclimit") == null
//				|| getCardHeadItemValue("acclimit").length() == 0)
//			getCardPanel().getHeadItem("acclimit").setValue("30");
		//����ҵ��ҳǩ��׼���ڵĿɱ༭��
        setEditableOfRatifyDate();
		// ����Ĭ��ֵ
		String custflag = getCardHeadItemValue("custflag");
		if (custflag != null && custflag.equals("0")) {
			BillItem[] items = getCardPanel().getHeadShowItems("BUY");
			int len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
				items[i].setValue(null);
			}

		} else if (custflag != null && custflag.equals("1")) {
			//		    ��ȡ�������۵�¼����
			BillItem[] items = getCardPanel().getHeadShowItems("SALE");
			int len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
				items[i].setValue(null);
			}
		}
		afterEditPK_cubasdoc1();
//		3.�Զ�����
		if(!isImport) {
			setAutoCustCode();
		}
		//ִ�б�ͷ��ʽ
		getCardPanel().execHeadTailEditFormulas();
	}

	private String getCardHeadItemValue(String field) {
		return getCardPanel().getHeadItem(field).getValueObject() == null ? null:getCardPanel().getHeadItem(field).getValueObject().toString() ;
	}

	private BillCodeObjValueVO getBillCodeObjValueVO() {
		if(billCodeObjVO==null) {
			billCodeObjVO = new BillCodeObjValueVO();
		}
		// ��������
		XTreeNode node = getAreaTree().getSelectedNode();
		if (node != null)
		{   
			billCodeObjVO.setAttributeValue("��������", node.getPrimaryKey());
		}
		return billCodeObjVO;
	}
	

	private void setAutoCustCode() {
		if(!isAutoSetCustCode())
			return;		
		String custCode=null;
		
		try {
			custCode = getAutoCodeService().getBillCode_RequiresNew("customer",getpk_corp(),null,getBillCodeObjValueVO());
			
		} catch (ValidationException e) {
			Logger.error(e.getMessage(),e);
			MessageDialog.showErrorDlg(this,null,e.getMessage());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
			MessageDialog.showErrorDlg(this,null,e.getMessage());
		}
		getCardPanel().setHeadItem("custcode",custCode);	
		setCodeEditable();
	}
	
	private void setCodeEditable() {
		//�Զ���Ų��ҶϺŲ��ţ����ܱ༭
        if(isAutoSetCustCode()&&isCodeFill()) {
        	getCardPanel().getHeadItem("custcode").setEnabled(false);
        }
	}

	private boolean isAutoSetCustCode() {
		if(isAutoCode == null) {
			try {
				isAutoCode = SysinitAccessor.getInstance().getParaBoolean(m_loginPkCorp, "BD037");
			} catch (BusinessException e1) {
				MessageDialog.showErrorDlg(this, null, e1.getMessage());
				Logger.error(e1.getMessage(),e1);
			}	
			isAutoCode=isAutoCode==null?UFBoolean.FALSE:isAutoCode;
		}
	    return isAutoCode.booleanValue();
	}
	
	private boolean isCodeFill() {
		if(isCodeFill==null) {
			BillcodeRuleVO codeRule=null;
			try {
				codeRule=getAutoCodeQry().findBillcodeRuleVOByPrimaryKey(CustDocUI.PK_CODE_RULE);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(),e);
				MessageDialog.showErrorDlg(this, null, e.getMessage());
			}
			isCodeFill=codeRule.isAutoFill()==null?UFBoolean.FALSE:codeRule.isAutoFill();
		}
	    return isCodeFill.booleanValue();
	}
	
	
	
	
    /**
     * ����ҵ����Ϣҳǩ��׼���ڵĿɱ༭��
     */
	private void setEditableOfRatifyDate() {
		String stat = getCardHeadItemValue("custstate");
        if(stat == null || !"1".equals(stat)){
        	getCardPanel().getHeadItem("ratifydate").setEnabled(false);
        	getCardPanel().setHeadItem("ratifydate",null);
        }else{
        	String date = getCardHeadItemValue("ratifydate");
        	if(getCardPanel().getHeadItem("ratifydate").isEdit())
        		getCardPanel().getHeadItem("ratifydate").setEnabled(true);
        	if(date == null || date.length() <= 0)
        	    getCardPanel().setHeadItem("ratifydate",getBusinessDate());
        }
	}

	private UFDate getBusinessDate() {
		return ClientEnvironment.getInstance().getBusinessDate();
	}

	/**
	 * �������� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onAddRow() {
		getCardPanel().addLine();
		
	}

	/**
	 * ��������任��ʱ����Ӧ�ĸ��¸õ����¿�����Ϣ
	 * 
	 * �����ܵı���ˢ��ǰ�󣬱��ѡ�����һ��
	 * 
	 * ��Ϊ����ˢ�£��ͻ���ˢ��
	 * 
	 * ----------------------------------------------
	 * 
	 * @param key java.lang.String
	 */

	public void onAreaChange(String key, boolean ref) {
		int rowSelected = hRow;
		hRow = -1;
        isQueryCustOnList = false;
	
		if (key == null) {
			getListPanel().getHeadTable().clearSelection();
			getListPanel().setHeaderValueVO(null);
			editVo = null;
		} else {
			try {
				CustManVO[] listVO = null;
				/* ������� */
				XTreeNode cacheNode = getCacheModel().getNode(key);
				// ����ڵ����ݿգ����߶Ըõ������࣬û�н��й���ѯ
				if (ref || cacheNode.getFlag() == 0) {

					listVO = CumandocBO_Client.getCust(key, getpk_corp());

					if (cacheNode.getChildCount() > 0) {
						for (int i = cacheNode.getChildCount() - 1; i >= 0; i--)
							// ɾ�������ڻ��� �е�ӳ��
							if (((XTreeNode) cacheNode.getChildAt(i))
									.getValue() instanceof CustManVO) {
								getCacheModel().deleteNode(
										((XTreeNode) cacheNode.getChildAt(i))
												.getPrimaryKey());
							}
					}

					//
					if (listVO != null && listVO.length > 0) {
						// ���뵽������
						for (int i = 0; i < listVO.length; i++)
							getCacheModel().addNode(listVO[i]);
					}
					cacheNode.setFlag(1);
				}
				int len = cacheNode.getChildCount();
				ArrayList list = new ArrayList();
				for (int i = 0; i < len; i++) {
					XTreeNode cnode = (XTreeNode) cacheNode.getChildAt(i);
					if (cnode.getValue() instanceof nc.vo.bd.b09.CustManVO ){		
						list.add(cnode.getValue());
					}
				}
				listVO = (CustManVO[])list.toArray(new CustManVO[0]);;				
                
				if (listVO == null || listVO.length == 0) {
					getListPanel().getHeadTable().clearSelection();
					getListPanel().setHeaderValueVO(null);
					getListPanel().setBodyValueVO("BANK", null);
					getListPanel().setBodyValueVO("ADDR", null);
					editVo = null;
				} else {
                    setCustToList(listVO);
					/*
					 * �������ñ��ѡ�����
					 */
					int rowCount = getListPanel().getHeadTable().getRowCount();
					if (rowSelected >= 0 && rowSelected < rowCount || rowCount > 0 && (rowSelected = 0) == 0)
						getListPanel().getHeadTable().getSelectionModel()
								.setSelectionInterval(rowSelected, rowSelected);
					else {
						getListPanel().getHeadTable().clearSelection();
						editVo = null;
						getListPanel().setBodyValueVO("BANK", null);
						getListPanel().setBodyValueVO("ADDR", null);
					}
				}
			} catch (Exception ex) {
				handleException(ex);
				showErrorMessage(ex.getMessage());
			}
		}

	}
    
    /**
     * @param listVO
     */
    private void setCustToList(CustManVO[] listVO)
    {
        ArrayList list = new ArrayList();

        for (int i = 0; i < listVO.length; i++)
        {
            CmdocVO cmdoc = listVO[i].convert();
            CbmVO cbm = (CbmVO) cmdoc.getParentVO();
            boolean isSealed = false;
            if (cbm.getCmfirst() != null)
            {
                isSealed = !(cbm.getCmfirst().getSealflag() == null);
            }
            cbm.getCubase().setAttributeValue("sealflag_b",
                    new nc.vo.pub.lang.UFBoolean(isSealed));

            list.add(cmdoc.getParentVO());
        }
        CbmVO[] hvos = (CbmVO[]) list.toArray(new CbmVO[0]);
        getListPanel().setHeaderValueVO(hvos);
//        getListPanel().setEnabled(false);
        getListPanel().getHeadBillModel().execLoadFormula();
    }


	/**
	 * �ӿ�Ƭ�����б�
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onBack() {
		// ���İ�ť��
		setButtons(list_btns);
		onChange();
		//		//ˢ�µ�ǰѡ�е��С�
		int row = getListPanel().getHeadTable().getSelectedRow();
		if (row < 0) {
				return;
		}
		hRow = row;
		selectCust();
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
		/* ========== �б�״̬ =========== */
		// ����
		if (bo == btn_Add) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory", "UPPuifactory-000061")/*
																 * @res
																 * "��ʼ�������ӵ��ݣ���ȴ�......"
																 */);
			onAdd();
			return;
		}
		// �༭
		if (bo == btn_Edit) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
													 * @res "��ʼ���б༭���ݣ���ȴ�......"
													 */);
			onEdit();
			return;
		}
		//����
		if (bo == btn_Batch_update) {
			
			onBatchUpdate();
			return;
		}
	
		// ɾ��
		if (bo == btn_Del) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
													 * @res "��ʼ���е���ɾ������ȴ�......"
													 */);
			onDelete();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "����ɾ�����,��ʱ(ms):" */
							);
			return;
		}
		// ��ѯ
		if (bo == btn_Query) {
			onQuery();
			return;
		}

		// ˢ��
		if (bo == btn_Ref) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
													 * @res "��ʼ����ˢ�µ��ݣ���ȴ�......"
													 */);
			onRef();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "����ˢ�����,��ʱ(ms):" */
							);
			return;
		}
		// ��ϸ
		if (bo == btn_Detail) {
			onDetail();
			return;
		}
		//
		if (bo == btn_DocMan) {
			onDocMan();
			return;
		}
		// ��ӡ
		if (bo == btn_Print) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
													 * @res "��ʼ���д�ӡ���ݣ���ȴ�......"
													 */);
			onPrint();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "���ݴ�ӡ���,��ʱ(ms):" */
							);
			return;
		}
		// ����
		if (bo == btn_Upgrade) {
			onUpgrade();
			return;
		}
		if (bo == btn_Output) {
			on_exportOutSystem();
			return;
		}

		/* =============== ��Ƭ״̬ =========== */
		// ��Ƭ����
		if (bo == btn_Card_Add) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory", "UPPuifactory-000061")/*
																 * @res
																 * "��ʼ�������ӵ��ݣ���ȴ�......"
																 */);
			onCardAdd();
			return;
		}
		// �༭
		if (bo == btn_Card_Edit) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
													 * @res "��ʼ���б༭���ݣ���ȴ�......"
													 */);
			onCardEdit();
			return;
		}
		if (bo == btn_Card_Del) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
													 * @res "��ʼ���е���ɾ������ȴ�......"
													 */);
			onCardDelete();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "����ɾ�����,��ʱ(ms):" */
							);
			
			return;
		}
		// ����
		if (bo == btn_Card_First) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res ��ʼִ�в���, ��ȴ�...
													 */
			);
			scrollRecord(0);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res ִ�����.��ʱ(ms)
													 */
							);
			return;
		}
		// ĩ��
		if (bo == btn_Card_Last) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res ��ʼִ�в���, ��ȴ�...
													 */
			);
			scrollRecord(2);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res ִ�����.��ʱ(ms)
													 */
							);
			return;
		}

		// ��һ��
		if (bo == btn_Card_Next) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res ��ʼִ�в���, ��ȴ�...
													 */
			);
			scrollRecord(1);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res ִ�����.��ʱ(ms)
													 */
							);
			return;
		}

		// ��һ��
		if (bo == btn_Card_Pre) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res ��ʼִ�в���, ��ȴ�...
													 */
			);
			scrollRecord(-1);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res ִ�����.��ʱ(ms)
													 */
							);
			return;
		}
		// ˢ��
		if (bo == btn_Card_Ref) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
													 * @res "��ʼ����ˢ�µ��ݣ���ȴ�......"
													 */);
			onCardRef();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "����ˢ�����,��ʱ(ms):" */
							);
			
			return;
		}
		// ����
		if (bo == btn_Card_Back) {
			onBack();
			return;
		}

		/* ============== ��Ƭ�༭״̬ ========= */
		// ��������һ��
		if (bo == btn_Card_AddRow) {
			onAddRow();
			return;
		}
		// ����ɾ��һ��
		if (bo == btn_Card_DelRow) {
			onDeleteRow();
			return;
		}
		// ��������
		if (bo == btn_Card_Save) {
			onSaveAdd();
			return;
		}
		// �����޸�
		if (bo == btn_Card_Mod_Save) {
			onSaveMod();
			return;
		}
		// ȡ���༭
		if (bo == btn_Card_Cancel) {
			onCardCancel();
			return;
		}
		if(bo == btn_BankAccount) {
			onBankAccount();
		}
		if(bo == btn_Card_BankAccount) {
			onCardBankAccount();
			return;
		}
		
		if(bo==btn_zdydc){
			try {
				this.onQDKSExport();//by tcl
			} catch (Exception e) {
			}
			return ;
		}
	}
	
	private void onQDKSExport()throws Exception{
		
		String sql="select distinct c.custcode,c.custname,t.def1,t.def2,c.sealflag from bd_cubasdoc c " +
				"left join bd_cumandoc t on c.pk_cubasdoc=t.pk_cubasdoc where c.isconnflag='Y' " +
				"and nvl(c.dr,0)=0 and nvl(t.dr,0)=0 order by c.custcode";
		
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<Map<String, Object>> maplist=(List<Map<String, Object>>)iQ.executeQuery(sql, new MapListProcessor());
		if(maplist==null||maplist.size()<=0){
			MessageDialog.showHintDlg(this, "��ʾ", "���������̵�����");
			return ;
		}
		
		String[] headColsCN=new String[]{"���̱���","��������","��������","�����ʺ�"};
		String[] mapColsCN=new String[]{"custcode","custname","def1","def2"};
		
//		 �û�ѡ��·��
		String path = ExcelUtils.getChooseFilePath(this, "����������Ϣ");
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			// ���Ϊ�գ��Ͳ�����ִ����
			return;

		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}
		
//		 ����һ�������
		IOUtils util = new IOUtils(path, false, true);
		// ����excel���������
		ExcelTools excelTools = new ExcelTools();
		// ����һ��sheet
		excelTools.createSheet("����������Ϣ");
		// �жϲ�ѯ���������Ƿ�Ϊ�գ������ߣ�
		if (CollectionUtils.isEmpty(maplist)) {
			// ����һ��
			excelTools.createRow(0);
			// ����һ����Ԫ��
			short ct = 0;
			excelTools.createCell(ct);
			// ���Ϊ�գ���ֱ����excel�ļ���д�롰�����ݣ���
			excelTools.setValue("�����ݣ�");

		} else {
			// ����һ��
			excelTools.createRow(0);
			// �ж���ͷ�Ƿ�Ϊ��
			if (null != headColsCN) {

				for (short i = 0; i < headColsCN.length; i++) {
					// ����һ����Ԫ��
					excelTools.createCell(i);
					// ��ֵд����Ԫ��
					excelTools.setValue(headColsCN[i]);

				}

				for (int i = 0; i < maplist.size(); i++) {
					// ����list
					Map<String, Object> map = maplist.get(i);

					// ����һ��
					excelTools.createRow(i + 1);

					for(int j=0;j<mapColsCN.length;j++){
						
						excelTools.createCell((short)j);
						excelTools.setValue(map.get(mapColsCN[j]));
						
					}

				}

			} 

		}
		// ��excelд��������
		excelTools.writeExcel(util.getOutputStream());
		// �չ���
		util.closeStream();
		
		MessageDialog.showHintDlg(this, "��ʾ", "�����ɹ���");
	}

	private void onCardBankAccount() {
		onBankAccount();
	}

	private void onBankAccount() {
		if(editVo==null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		int selectedRow = getListPanel().getHeadTable().getSelectedRow();
		Uif2Dialog dialog = getCustBankDlg();
		CustBasVO basVO=(CustBasVO)editVo.getParentVO().clone();
		//�����Զ�����1Ϊ��¼��˾����Ҫ�������ʽ�Ŀ���������
		basVO.setLoginCorp(m_loginPkCorp);
		dialog.setParam(basVO);
		dialog.showModal();
		//���½���
		onCardRef();
		getListPanel().getHeadTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		getListPanel().setBodyValueVO("BANK", editVo.getBanks());
		getListPanel().getBodyBillModel("BANK").execLoadFormula();
	}
	
	private Uif2Dialog getCustBankDlg() {
		if (custBankDialog == null) {
			custBankDialog = new Uif2Dialog(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
							"UPP10080806-000105")/* @res "�����˻�" */,
					getModuleCode(),
					CustDocUI.CUST_BANK_CONFIG_PATH);
		}
		return custBankDialog;
	}

	public void on_exportOutSystem() {
		CircularlyAccessibleValueObject cvo[] = getListPanel()
				.getHeadBillModel().getBodyValueVOs(CbmVO.class.getName());
		if (cvo == null || cvo.length == 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000095")/* �����Ȳ�ѯҪ�����Ŀ�������! */);
			return;
		}
		CustBasVO[] basvo = new CustBasVO[cvo.length];
		for (int i = 0; i < cvo.length; i++) {
			String key = ((CbmVO) cvo[i]).getCubase().getPrimaryKey();
			try {
				XTreeNode node = getCacheModel().getNode(key);
				CustManVO manvo = (CustManVO) node.getValue();
				basvo[i] = (CustBasVO) manvo.getParentVO();
			} catch (TreeOperationException e1) {
				Logger.error(e1.getMessage(),e1);
			}
		}
		try {
			BillExportUtil.output(basvo, "bscubas", this);
		} catch (Throwable e) {
			showErrorMessage(e.getMessage());
			Logger.error(e.getMessage(),e);
		}
	}

	/**
	 * ��Ƭ״̬�½�������
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onCardAdd() {
		if (corpCanAdd() == false) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000044")/* @res "���ſ��ƣ���˾������ӿ���" */);
			return;
		}
		// �༭���� ��ɳ�ʼ״̬
		clearCardPanel();
		getCardPanel().addNew();
		// �任��ť״̬
		setButtons(card_edit_Add);
		// ���ñ���
		//setTitleText("��������");
		// �ɱ༭
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();

		//Ԥ�õ�����
		setCardDefaultData();

		getCardPanel().transferFocusTo(0);
		billOperate = IBillOperate.OP_ADD;

	}

	/**
	 * �ӿ�Ƭ�����б�
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onCardCancel() {
		returnCustCode();
		if (editVo == null) {
			// ���б�״̬�������ص��б�״̬
			onBack();
		}
		// ������ϸ״̬
		else {
			// ���İ�ť��
			setButtons(card_detail);

			// �������ò��� �������ݿ������ò���
			getCardPanel().setBillValueVO(editVo.convert());
			setSaleMny();
			getCardPanel().execTailLoadFormulas();
			getCardPanel().getBillModel("BANK").execLoadFormula();
			getCardPanel().getBillModel("ADDR").execLoadFormula();

			// �ɱ༭��
			getCardPanel().setEnabled(false);
			State = 0;
		}
		billOperate = IBillOperate.OP_NOTEDIT;

	}

	/**
	 * ����ȡ��ʱ���˻��Ѿ�����Ŀ��̱���
	 */
	private void returnCustCode() {
		if(billOperate==IBillOperate.OP_ADD&&isAutoSetCustCode()&&isCodeFill()) {
			String custCode=(String) getCardPanel().getHeadItem("custcode").getValueObject();
			String pk_areacl=(String) getCardPanel().getHeadItem("pk_areacl").getValueObject();
			BillCodeObjValueVO valueVO=new BillCodeObjValueVO();
			valueVO.setAttributeValue("��������", pk_areacl);
			try {
				getAutoCodeService().returnBillCodeOnDelete(m_loginPkCorp, "customer", custCode, valueVO);
			} catch (BusinessException e) {
				handleException(e);
			}
		}
	}
	
	
	

	/**
	 * 
	 * ��ϸ״̬�£�ɾ��ָ���Ŀ���
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onCardDelete() {
		onDelete();
		onBack();
	}

	/**
	 * �Կ��̽��б༭
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onCardEdit() {

		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// �ɱ༭
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();
		// �ӹ���Ƭ����
		processCardforEdit();
		billOperate = IBillOperate.OP_EDIT;
		setDataToCard();
		setItemEditable();
		// ���İ�ť��
		setButtons(card_edit_Mod);
	}
	/**
	 * ��Ƭˢ�� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onCardRef() {
		//
		try {
			CustManVO vo = CumandocBO_Client.getCustData(editVo.getKey(),
					getpk_corp());
			if (vo != null) {
				editVo = vo;
				//���л���ĸ���
                String pk_cubasdoc = editVo.getParentVO().getPrimaryKey();
				getCacheModel()
                        .deleteNode(pk_cubasdoc);
                getCacheModel().addNode(editVo); 
                moneyMap.remove(pk_cubasdoc);
				setDataToCard();
				setListAfterEditQueryCust();
			} else {
				showErrorMessage(nc.vo.bd.BDMsg.MSG_REFRESH());
				onAreaChange(editVo.getAssKey(), true);
				onBack();
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * ��ʾ�л� �������ڣ�(2003-6-3 14:35:55)
	 */
	public void onChange() {

		removeAll();
		//	��ϸ �� ��ͨ
		if (billState == 1) {
			billState = 0;
			add(getSplitPane1(), "Center");
		}
		//	��ͨ �� ��ϸ
		else {
			billState = 1;
			add(getCardPanel(), "Center");
			enableSrollButtons();
			updateButtons();
			updateFilePath();
		}

		this.validate();
		this.repaint();
	}

	/**
	 * ɾ��ѡ�еĿ���
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onDelete() {
		// �Ƿ�ѡ�п���
		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		
		if (MessageDialog.showOkCancelDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "����ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?" */
				, MessageDialog.ID_CANCEL) == UIDialog.ID_OK) {
			try {
				
			   CustBasVO parentVO = (CustBasVO)editVo.getParentVO();
				
				// ִ��ɾ������
				// ���ɾ�����Ǽ��ŷ��������Ŀ��̵Ĺ�����
				if (parentVO.getAttributeValue("pk_corp").equals(
						"0001")) {
					// ɾ�����ŷ��������Ŀ��̵Ĺ������൱���ڼ���ȡ���������
					ErrLogReturnValue result = CumandocBO_Client.deleteAssignedDoc(getpk_corp(), editVo.getCustBasVO().getPrimaryKey());
					
					processResult(result);
				}
				// ����ǹ�˾�Լ���ӵĹ�����
				else {
					CumandocBO_Client.deleteByCubasID(editVo.getKey(),
							getpk_corp(), ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				}
				writeOperateLog(editVo, OperateType.DELETE);
				// ͬʱ�Ի������ͬ��
				getCacheModel().deleteNode(
						getCacheModel().getNode(editVo.getKey()));
				// ����ͬ��
				onAreaChange(editVo.getAssKey(), true);
			} catch (Exception ex) {
				handleException(ex);
				if (ex.getMessage() != null) {
					String s=nc.vo.bd.BDMsg.MSG_DATA_DELETE_FAIL()+ ex.getMessage();
					showHintMessage(s);
					showErrorMessage(s);
				}
			}

		}
	}




	/**
	 * ���ؽ������
	 * @param result
	 * @throws BusinessException
	 */
	private void processResult(ErrLogReturnValue result)
			throws BusinessException {
		if(result.getErrLogResult()!=null) {
			ErrlogmsgVO[] vos=result.getErrLogResult().getErrlogmsgs();
			if(vos!=null) {
				String message="";
				for(ErrlogmsgVO vo : vos) {
					message+=System.getProperty("line.separator");
					message+=vo.getErrormsg().replaceAll(BDMsg.MSG_DATA_ASSIGN_CANCEL_FAIL(), "");
				}
				throw new BusinessException(message);
			}
		}
	}
	

	/**
	 * ɾ��������ѡ�е��� ���ڼ��ŷ��䵽��˾�Ŀ��̻�������������Ӧ���������ݲ���ɾ�� �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onDeleteRow() {	
		getCardPanel().delLine();
	}

	/**
	 * ��ϸ��Ϣ
	 * 
	 * �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onDetail() {
		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// �л�����
		onChange();

		// ���İ�ť��
		setButtons(card_detail);

		// ���ı���
		//setTitleText("������ϸ��Ϣ");

		// �������ò��� �������ݿ������ò���

		setDataToCard();
		// �ɱ༭��
		getCardPanel().setEnabled(false);

	}

	/**
	 * -------------------------------------------------- ���ܣ� �ĵ�����
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-12-11 13:58:31)
	 * --------------------------------------------------
	 */
	public void onDocMan() {
		if (editVo == null) {
			MessageDialog.showErrorDlg(this, null, nc.vo.bd.BDMsg
					.MSG_CHOOSE_DATA());
			return;
		}

		String key = getListPanel().getHeadBillModel().getValueAt(hRow,
				"pk_cubasdoc").toString();
		CustManVO vo = null;
		try {
			XTreeNode node = getCacheModel().getNode(key);
			vo = (CustManVO) node.getValue();

		} catch (Exception ex) {
		}

		if (vo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		CustBasVO basvo = (CustBasVO)vo.getParentVO();
		String pkcorp = vo.getManCorp();
		String showname = vo.getCustCode()+ "_" + vo.getCustName();
		String dirname = getCustFileUtil().getFileDir(editVo.getParentVO(),pkcorp);

		String basePkcorp = vo.getBasCorp();
		boolean isAssigned = false;
		if (!basePkcorp.equals(pkcorp))
			isAssigned = true;

		String baseShowname = showname
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
						"UPT10080806-000118");/* @res"������Ϣ" */
		String baseDirname =  getCustFileUtil().getFileDir(editVo.getParentVO());;


		if (isAssigned) {
			BDDocManageDlg.showFileManageDlg(this, null, new String[] { dirname, baseDirname }, new String[] {
					showname, baseShowname }, dirname);

		} else {
			BDDocManageDlg.showFileManageDlg(this,null,
					new String[] { dirname }, new String[] { showname },dirname);
		}
		
		writeOperateLog(basvo,nc.ui.ml.NCLangRes.getInstance().getStrByID("10080804","UPP10080804-000027")
				/* @res "��������" */);

	}
	
	/**
	 * д������־
	 * @param headerVO
	 * @param operate_type
	 */
	private void writeOperateLog(CustBasVO headerVO,String operate_type) {
		try {
			BDBusinessLogUtil.logBizOperate(ICustConst.CUST_MAN_NODE_CODE,
					operate_type, headerVO.getPrimaryKey(),
					headerVO.getCustcode(), headerVO.getCustname());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
			throw new BusinessRuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * ��ѡ�еĿ��̽��б༭
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onEdit() {
		clearCardPanel();

		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// �л�����
		onChange();
		// �ɱ༭��
		getCardPanel().setEnabled(true);
		
		
		updateBodyButtons();
		// �ӹ���Ƭ����
		processCardforEdit();
		// ���İ�ť��
		setButtons(card_edit_Mod);
		billOperate = IBillOperate.OP_EDIT;
		setDataToCard();
		
		setItemEditable();
		
	}

	/**
	 * ���ÿɱ༭��
	 */
	private void setItemEditable() {
		setCodeEditable();
		if(isDirectlyAdd) {
			getCardPanel().getHeadItem("custprop").setEnabled(false);
		}
	}


	/**
	 * ˫������ �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			// ִ��һ�α�ͷѡ�����
			int row = getListPanel().getHeadTable().getSelectedRow();
			if (row != hRow) {
				hRow = row;
				selectCust();
			}
			// ִ���л�
			if (editVo != null) {
				onDetail();
			}
		}

	}

	/**
	 * -------------------------------------------------- ���ܣ�
	 * 
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-3 14:46:06)
	 * --------------------------------------------------
	 */
	public void onPrint() {

		if (getListPanel().getHeadBillModel().getRowCount() == 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10080806", "UPP10080806-000046")/*
																			    * @res
																			    * "û�пɴ�ӡ������"
																			    */);
			return;
		}
		try {
			//�����û���ʹ�õĴ�ӡģ��
			PrintEntry print = new PrintEntry(this);
			print.setTemplateID(m_loginPkCorp, MODULE_CODE, m_loginUserID, null);

			if (print.selectTemplate() < 0)
				return;

			//׼��ҵ������VO��ExtendedAggrigatedValueObject��
			nc.vo.pub.general.GeneralExVO printData = new nc.vo.pub.general.GeneralExVO();

            CircularlyAccessibleValueObject vo = null;
            XTreeNode node = (XTreeNode) getAreaTree().getSelectedNode();
            if (node != null)
            {
                vo = (CircularlyAccessibleValueObject) node.getValue();
            }
            if (vo == null)
                vo = new CustAreaVO();

			GeneralSuperVO headVO = new GeneralSuperVO();
			String[] attrNames = vo.getAttributeNames();
			for (int i = 0; i < attrNames.length; i++) {
				headVO.setAttributeValue("h_" + attrNames[i], vo
						.getAttributeValue(attrNames[i]));

			}

			CircularlyAccessibleValueObject[] vos = nc.ui.pub.general.PrintDataUtil
					.getVOsfromBillModel(getListPanel().getHeadBillModel());

			//�ӻ����л�ȡ�б�����
			Hashtable table = new Hashtable();
//          XTreeNode cacheNode =
            // getCacheModel().getNode(vo.getPrimaryKey());
            int len = vos == null ? 0 : vos.length;

			for (int i = 0; i < len; i++) {
                XTreeNode cnode = getCacheModel().getNode(
                        vos[i].getAttributeValue("pk_cubasdoc"));
                //              XTreeNode cnode = (XTreeNode) cacheNode.getChildAt(i);
                if (cnode.getValue() instanceof nc.vo.bd.b09.CustManVO) {
					CustManVO manVO = (CustManVO) cnode.getValue();
					CustBasVO dataVO = ((CbmVO) manVO.convert().getParentVO())
							.getCubase();
					table.put(dataVO.getPrimaryKey(), dataVO);
				}
			}
			for (int i = 0; i < vos.length; i++) {
				if (table.containsKey(vos[i].getAttributeValue("pk_cubasdoc"))) {
					CustBasVO dataVO = (CustBasVO) table.get(vos[i]
							.getAttributeValue("pk_cubasdoc"));
					String[] bas_attrNames = dataVO.getAttributeNames();
					for (int j = 0; j < bas_attrNames.length; j++) {
						if (bas_attrNames[j].startsWith("m_")
								|| bas_attrNames[j].startsWith("b_"))
							vos[i].setAttributeValue(bas_attrNames[j]
									.substring(2), dataVO
									.getAttributeValue(bas_attrNames[j]));
						else
							vos[i].setAttributeValue(bas_attrNames[j], dataVO
									.getAttributeValue(bas_attrNames[j]));
					}
				}
			}

			printData.setParentVO(headVO);
			printData.addTableVO("body", "body", vos); //Ϊ��ӡ��������Դ
			PrintVODataSource dataSource = new PrintVODataSource(printData,
					print, "h_", null);
			print.setDataSource(dataSource); //Ԥ��
			print.preview();
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-19 16:47:39)
	 */
	public void onRef() {
		moneyMap.clear();

		XTreeNode node = getAreaTree().getSelectedNode();
		//		if(node == null){
		//		      MessageDialog.showHintDlg(this,"��ʾ"��"����ѡ�е������ࡣ");
		//	}

		if (node != null) {
			//���ڸ��ڵ㣬ˢ�µ�����������ȫ������ṹ
			if (node.getLevelNumber() == 0) {
				recontructAreaTree();
			}
			if (node.getPrimaryKey() != null) {
				onAreaChange(node.getPrimaryKey(), true);
			}
		}

	}

	private void recontructAreaTree() {
		// ��ȡ��������
		try {
			areaVos = CubasdocBO_Client.getCustArea(m_loginPkCorp,ClientEnvironment.getInstance().getUser().getPrimaryKey());
			getAreaModel().createTree(areaVos);
			getCacheModel().createTree(areaVos);
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	/**
	 * ����ӿ�Ƭ״̬�»�ȡ��ǰ�����Ŀ�����Ϣ �������ڣ�(2003-6-19 16:47:39)
	 */
	protected boolean onSaveAdd() {
		// ��ֹ�༭
		getCardPanel().stopEditing();

		CustManVO custVO = null;
		boolean fail = false;
		String[] keys = null;

		int index = 0;
		try {

			custVO = getCustManVO(true);
			
			// ת����ԭ��ϵͳ�����ݽṹ����ѭԭ���ļ�����
			index = ((UIComboBox) getCardPanel().getHeadItem("custflag")
					.getComponent()).getSelectedIndex();
			dataNotNullValidate(index);
			// �ͻ�
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
			}
			// ��Ӧ��
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}
			// ����
			if (index == 2) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}

			custVO.validate();
			CubasdocVO.validateAllAddrVO((CustAddrVO[]) getCardPanel()
					.getBillModel("ADDR").getBodyValueVOs(CustAddrVO.class.getName()));
			custVO.setStatus(nc.vo.pub.VOStatus.NEW);
			
			//�������
			//���ż۸�����Զ���ֵ����˾�۸����
			if (index != 1) {			
				String pk_pricegoupcorp = (String) custVO.getChildrenVO()[0]
						.getAttributeValue("pk_pricegroupcorp");
				if (pk_pricegoupcorp == null) {
					String pk_pricegroup = (String) custVO.getParentVO()
								.getAttributeValue("pk_pricegroup");
					custVO.getChildrenVO()[0].setAttributeValue(
							"pk_pricegroupcorp", pk_pricegroup);
				}
			}
			//���������������Զ�������������
			String cmnecode = (String) custVO.getChildrenVO()[0]
					.getAttributeValue("cmnecode");

			if (cmnecode != null) {
				custVO.getParentVO().setAttributeValue("mnecode", cmnecode);
			}
			keys = CumandocBO_Client.insert(custVO, index);
			custVO.getParentVO().setPrimaryKey(keys[0]);
			CumandocVO childVO = (CumandocVO) custVO.getChildrenVO()[0];
			childVO.setPrimaryKey(keys[1]);
			if (childVO.getPk_cusmandoc2() == null)
				childVO.setPk_cusmandoc2(keys[1]);
			if (childVO.getPk_cusmandoc3() == null)
				childVO.setPk_cusmandoc3(keys[1]);

			childVO = (CumandocVO) custVO.getChildrenVO()[1];
			childVO.setPrimaryKey(keys[2]);
			if (childVO.getPk_cusmandoc2() == null)
				childVO.setPk_cusmandoc2(keys[2]);
			if (childVO.getPk_cusmandoc3() == null)
				childVO.setPk_cusmandoc3(keys[2]);
			//custVO.getChildrenVO()[1].setPrimaryKey(keys[2]);

		} catch (BusinessException ex) {
			fail = true;
			String hint = ex.getHint();
			if (hint != null && "1".equals(hint)) {
				if(CustDocUI.handleCheckInfo(this, 1)){
					//����û�ѡ��ǿ�Ʊ���
					try {
						keys = CumandocBO_Client.insertForce(custVO, index);
						custVO.getParentVO().setPrimaryKey(keys[0]);
						CumandocVO childVO = (CumandocVO) custVO
									.getChildrenVO()[0];
						childVO.setPrimaryKey(keys[1]);
						if (childVO.getPk_cusmandoc2() == null)
							childVO.setPk_cusmandoc2(keys[1]);
						if (childVO.getPk_cusmandoc3() == null)
							childVO.setPk_cusmandoc3(keys[1]);

						childVO = (CumandocVO) custVO.getChildrenVO()[1];
						childVO.setPrimaryKey(keys[2]);
						if (childVO.getPk_cusmandoc2() == null)
							childVO.setPk_cusmandoc2(keys[2]);
						if (childVO.getPk_cusmandoc3() == null)
							childVO.setPk_cusmandoc3(keys[2]);
						fail = false;
					} catch (BusinessException e) {
						Logger.error(e.getMessage(),e);
						showErrorMessage(e.getMessage());
						return false;
					}
				}
			} else {
				Logger.error(ex.getMessage(),ex);
				showErrorMessage(ex.getMessage());
				return false;
			}
		}catch (Exception ex) {
			fail = true;
			handleException(ex);
			showHintMessage(NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000084"));/* "δ֪���󣬱���ʧ�ܡ� */
			return false;
		}
		if (!fail) {
			try {
				billOperate = IBillOperate.OP_NOTEDIT;
//				д���û��ϻ���־��
				writeOperateLog(custVO,OperateType.SAVE);	
				//��������ݵ��룬���Բ�ִ�������߼�������ߵ���Ч��
				if(!isImport) {
					//========= �����û�����״̬���Լ�����ͬ�� ===========
	                custVO = CumandocBO_Client.getCustData(custVO.getKey(),
	                        getpk_corp());
					getCacheModel().addNode(custVO);

					// ��ӵ��б����
	                onAreaChange(custVO.getAssKey(), false);
	                
	               // ���İ�ť��
					setButtons(card_detail);
					setListCurrentRow(keys[0]);
					setDataToCard();
				}
				 //�ɱ༭��
				getCardPanel().setEnabled(false);
				showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
				State = 0;
				
//				if (isDirectlyAdd) {
//					closeCurrentNode();
//				}
				return true;

			}catch(TreeOperationException te) {
				Logger.error(te.getMessage(),te);
	            showErrorMessage(NCLangRes.getInstance().getStrByID("10080804","UPP10080804-000067"));
	                                               /*�����������ݲ�ͬ���������б����ѡ�и��ڵ�ˢ�����ݡ�*/                			
	            getCardPanel().setEnabled(false);
	            onCardCancel();
	            return false;
			}catch (Exception exc) {
				handleException(exc);
				return false;
			}			

		}
		else{
	        return false;
	   }
	}


//	private void closeCurrentNode() {
//		List openModules = ClientEnvironment.getInstance().getOpenModules();
//        Iterator it = openModules.iterator();
//        IFuncWindow window = null;
//        while (it.hasNext()) {
//            // MainFrame openModule = (MainFrame) it.next();
//            window = (IFuncWindow) it.next();
//            if (window.getFuncPanel().getModuleCode().equals(MODULE_CODE)) 
//                break;
//        }		
//        if(window != null)
//        	window.closeWindow();
//	}

	/**
	 * ���ݿ���pk�������ñ�ͷѡ�����
	 * 
	 * @param custbaspk
	 */
	private void setListCurrentRow(String custbaspk) {
		int rowCount = getListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			Object primary_PK = getListPanel().getHeadBillModel().getValueAt(i,
					"pk_cubasdoc");
			if (primary_PK != null
					&& primary_PK.toString().trim().equals(custbaspk)) {
				getListPanel().getHeadTable().setRowSelectionInterval(i, i);
				break;
			}
		}
	}

	/**
	 * �����޸� �������ڣ�(2003-6-19 16:47:39)
	 */
	protected boolean onSaveMod() {
		// ��ֹ�༭
		getCardPanel().stopEditing();

		CustManVO custVO = null;
		int index = 0;
		boolean fail = false;
		try {
			custVO = getCustManVO(true);
			// ת����ԭ��ϵͳ�����ݽṹ����ѭԭ���ļ�����
			index = ((UIComboBox) getCardPanel().getHeadItem("custflag")
					.getComponent()).getSelectedIndex();
			dataNotNullValidate(index);
			// �ͻ�
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
			}
			// ��Ӧ��
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}
			// ����
			if (index == 2) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}

			custVO.validate();
			CubasdocVO.validateAllAddrVO((CustAddrVO[]) getCardPanel()
					.getBillModel("ADDR").getBodyValueVOs(CustAddrVO.class.getName()));
			custVO.setStatus(nc.vo.pub.VOStatus.UPDATED);
			custVO.setUserid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey());
			//�������
			if (index != 1) {
				String pk_pricegroup = (String) custVO.getParentVO()
						.getAttributeValue("pk_pricegroup");
				String pk_pricegoupcorp = (String) custVO.getChildrenVO()[0]
						.getAttributeValue("pk_pricegroupcorp");
				if (pk_pricegoupcorp == null) {
					custVO.getChildrenVO()[0].setAttributeValue(
							"pk_pricegroupcorp", pk_pricegroup);
				}
			}
			String objtmp = getCardHeadItemValue("sealflag_b");
			boolean isSealed = (objtmp == null || objtmp
					.equalsIgnoreCase("false")) ? false : true;
			int changedSealflag = 0; //-1:��true��Ϊfalse;0:unchanged;1:��false��Ϊtrue
			if (isSealed
					&& (m_sealflag == null || m_sealflag.equalsIgnoreCase("N"))) {
				changedSealflag = 1;
			} else if (!isSealed
					&& (m_sealflag != null && !m_sealflag.equalsIgnoreCase("N"))) {
				changedSealflag = -1;
			}
			switch (changedSealflag) {
			case -1: {
				((CumandocVO) custVO.getChildrenVO()[0]).setSealflag(null);
				((CumandocVO) custVO.getChildrenVO()[1]).setSealflag(null);
				break;
			}
			case 0: {
				if (m_sealflag != null && !m_sealflag.equalsIgnoreCase("N")) {
					((CumandocVO) custVO.getChildrenVO()[0])
							.setSealflag(new UFDate(m_sealflag));
					((CumandocVO) custVO.getChildrenVO()[1])
							.setSealflag(new UFDate(m_sealflag));
				} else {
					((CumandocVO) custVO.getChildrenVO()[0]).setSealflag(null);
					((CumandocVO) custVO.getChildrenVO()[1]).setSealflag(null);
				}
				break;
			}
			case 1: {
				UFDate date = ClientEnvironment.getInstance().getDate();
				((CumandocVO) custVO.getChildrenVO()[0]).setSealflag(date);
				((CumandocVO) custVO.getChildrenVO()[1]).setSealflag(date);
				break;
			}
			}
			CumandocBO_Client.update(custVO, index);
		} catch (nc.vo.pub.BusinessException ex) {
			// ����ʧ��
			fail = true;
			String hint = ex.getHint();
			if (hint != null && "1".equals(hint)) {
				if(CustDocUI.handleCheckInfo(this, 1)){
				    //����û�ѡ��ǿ�Ʊ���
					try {
						CumandocBO_Client.updateForce(custVO, index);
						fail = false;
					} catch (BusinessException e) {					
						Logger.error(e.getMessage(),e);
						showErrorMessage(e.getMessage());
						return false;
					}
				}
			}
			else {
				Logger.error(ex.getMessage(),ex);
				showErrorMessage(ex.getMessage());
				return false;
			}
		} catch (Exception ex) {
			fail = true;
			showHintMessage(NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000084"));/* "δ֪���󣬱���ʧ�ܡ� */
			handleException(ex);
			return false;
		}
		if (!fail) {
			billOperate = IBillOperate.OP_NOTEDIT;
			try { //========= �����û�����״̬���Լ�����ͬ�� ===========
				writeOperateLog(custVO,OperateType.SAVE);
                custVO = CumandocBO_Client.getCustData(custVO.getKey(),
                        getpk_corp());
                //���л���ĸ���
                getCacheModel()
                        .deleteNode(custVO.getParentVO().getPrimaryKey());
                getCacheModel().addNode(custVO); 
                //����б������ʾ���ǲ�ѯ�õ������ݣ��ز����ݡ�
                if (isQueryCustOnList)
                {
                    setListAfterEditQueryCust();
                }
                else {    
                	
                	String pk_area = null;
                	if(getAreaTree().getSelectedNode() != null)
                		pk_area = getAreaTree().getSelectedNode().getPrimaryKey();
                	String freshPk = pk_area == null ? custVO.getAssKey() : pk_area;
                    onAreaChange(freshPk, false);
                }
                // ���İ�ť��
				setButtons(card_detail);
				btn_Card_AddRow.setVisible(true);
				btn_Card_DelRow.setVisible(true);
				
				setListCurrentRow(custVO.getParentVO().getPrimaryKey());
				editVo = custVO;
				setDataToCard();
				// �ɱ༭��
				getCardPanel().setEnabled(false);
				showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
				State = 0;
				return true;
			}catch(TreeOperationException te) {
				Logger.error(te.getMessage(),te);
                showErrorMessage(NCLangRes.getInstance().getStrByID("10080804","UPP10080804-000067"));
                                                   /*�����������ݲ�ͬ���������б����ѡ�и��ڵ�ˢ�����ݡ�*/                			
                getCardPanel().setEnabled(false);
                onCardCancel();
                return false;
			}catch (Exception ex) {
				handleException(ex);
				return false;
			}
		}
		else{
		        return false;
		}
	}

	private void writeOperateLog(CustManVO custVO,OperateType oper) throws BusinessException {
		BDLogUtil.writeLog4BD(MODULE_CODE, ((CumandocVO) custVO.getChildrenVO()[0]).getPrimaryKey(), ((CustBasVO)custVO.getParentVO()).getCustcode(), oper, null);
	}

    /**
     * @throws TreeOperationException
     */
    private void setListAfterEditQueryCust() throws TreeOperationException
    {
        int count = getListPanel().getHeadBillModel().getRowCount();
        ArrayList list = new ArrayList();
        for (int i = 0; i < count; i++)
        {
            String pk = (String) getListPanel().getHeadBillModel().getValueAt(
                    i, "pk_cubasdoc");
            if (getCacheModel().getNode(pk) != null)
            {
                list.add(getCacheModel().getNode(pk).getValue());
            }
        }
        if (list.size() > 0)
            setCustToList((CustManVO[]) list.toArray(new CustManVO[0]));
    }

	/**
	 * -------------------------------------------------- ���ܣ� �������ݵ�����Ƭ����ʾ��ʽ
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-10-13 16:13:37)
	 * --------------------------------------------------
	 */
	public void processCardforEdit() {

		// ����ÿ�����Ϣ�Ǽ��ŷ���ģ��򲿷����ݲ����Ա༭
		if (editVo.getParentVO().getAttributeValue("pk_corp").equals("0001")) {
			// ������Ϣ�����Ա༭
			BillItem[] items = getCardPanel().getHeadShowItems("BASE");
			int len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
			}

			items = getCardPanel().getHeadShowItems("Commu");
			len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);				
			}

			items = getCardPanel().getHeadShowItems("B_DEF");
			len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
			}
		} else {
			btn_Card_AddRow.setVisible(true);
			btn_Card_DelRow.setVisible(true);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000026")/* @res "��˾�Խ�����" */);
		}
		//		
		// ���ݿ��̵Ĺ�����Ϣ�����òɹ������۵Ŀɱ༭��
		int custflag = ((CbmVO) editVo.convert().getParentVO()).getType();
		if (custflag == CbmVO.KH) {
			setCustVisaul(0);
		} else if (custflag == CbmVO.GYS) {
			setCustVisaul(1);
		} else {
			getCardPanel().setHeadItem("custflag", "2");
			setCustVisaul(2);
		}
		//����ҵ����Ϣҳǩ
		processBusiTab();
	}

	private void processBusiTab() {
		//����ҵ��ҳǩ��׼���ڵĿɱ༭��
		setEditableOfRatifyDate();
		Integer iTemp = ((CustBasVO) editVo.getCustBasVO()).getCustprop();
		int iCustprop = (iTemp == null ? 0 : iTemp.intValue());

		if (iCustprop == 0) //�ⲿ��λ
		{
			getCardPanel().getHeadItem("pk_corp1").setEnabled(false);
			getCardPanel().getHeadItem("pk_corp1").setValue(null);
		}
	}

	/**
	 * ��¼���� �� 0 -����һ�� -1 ����һ�� 1 ����һ�� 2 ��ĩβ
	 * 
	 * 
	 * �������ڣ�(2003-6-24 15:57:14)
	 */
	public void scrollRecord(int style) {
		enableSrollButtons();
		int rowCount = getListPanel().getHeadTable().getRowCount();		
		// hRow ��¼��ǰ��ѡ�ֵ�����
		switch (style) {
		case 0:
			hRow = 0;			
			break;
		case -1:
			if(hRow > 0)
			  hRow--;
			break;
		case 1:
			if(hRow < rowCount - 1)
			hRow++;
			break;
		case 2:			
			hRow = rowCount - 1;
			break;
		}
		processPageButtons(rowCount);
		selectCust();
		getListPanel().getHeadTable().getSelectionModel().setSelectionInterval(hRow,hRow);
		getListPanel().setBodyValueVO("BANK", null);
		getListPanel().setBodyValueVO("ADDR", null);
		setDataToCard();
	}

	private void enableSrollButtons() {
		btn_Card_Last.setEnabled(true);
		btn_Card_Next.setEnabled(true);
		btn_Card_First.setEnabled(true);
		btn_Card_Pre.setEnabled(true);
	}
    private void processPageButtons(int total) {
    	if(hRow == 0) {
    		btn_Card_First.setEnabled(false);
    		btn_Card_Pre.setEnabled(false);
    	}
    	else if(hRow == total - 1) {
    		btn_Card_Last.setEnabled(false);
    		btn_Card_Next.setEnabled(false);
    	}
    	updateButtons();
    }

	/**
	 * ��ͷѡ�����˱仯�Ժ�ά������ͬ�� �������ڣ�(2003-6-24 15:57:14)
	 */
	public String selectCust() {
		String key = getListPanel().getHeadBillModel().getValueAt(hRow,
				"pk_cubasdoc").toString();
		try {
			XTreeNode node = getCacheModel().getNode(key);
			editVo = (CustManVO) node.getValue();

			//��������
			getListPanel().getBodyTable().clearSelection();
			getListPanel().setBodyValueVO("BANK", editVo.getBanks());
			getListPanel().setBodyValueVO("ADDR", editVo.getAddrs());
			// ���ع�ʽ
			getListPanel().getBodyBillModel("BANK").execLoadFormula();
			getListPanel().getBodyBillModel("ADDR").execLoadFormula();
			updateFilePath();
			
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
		}
		return null;

	}
	
	/**
	 * �����ĵ�ͼ�괦�ļ��б�
	 */
	private void updateFilePath() {
		if(editVo!=null&&billState==1) {
			String dirname = getCustFileUtil().getFileDir(editVo.getParentVO(),m_loginPkCorp);
			getCardPanel().getAccessoriesAction().setFiledir(dirname);
		}
	}

	/**
	 * ���ݿ�������- �������ۡ��ɹ�����Ŀɱ༭�� �������ڣ�(2003-6-19 16:47:39)
	 */
	protected void setCustVisaul(int flag) {
		boolean sale = true;
		boolean buy = true;
		switch (flag) {
		case 0:
			sale = true;
			buy = false;
			break;
		case 1:
			sale = false;
			buy = true;
			break;
		case 2:
			sale = true;
			buy = true;
			break;
		}

		// ��ȡ�������۵�¼����
		BillItem[] items = getCardPanel().getHeadShowItems("SALE");
		int len = items == null ? 0 : items.length;
		if(sale) {
			for (int i = 0; i < len; i++) {
				if(items[i].isEdit())
				   items[i].setEnabled(sale);
			}
		}
		else {
			for (int i = 0; i < len; i++) {
				 items[i].setEnabled(sale);
			     items[i].setValue(null);
		    }
		}

		if (sale) {
			//����ֵ
			getCardPanel().setHeadItem("discountrate", "100");
			getCardPanel().setHeadItem("stockpriceratio", "100");			
			getCardPanel().setHeadItem("pk_currtype1", getCurrency());
			getCardPanel().getHeadItem("developdate").setValue(
					getBusinessDate());
			//�Ƿ����ù���
			getCardPanel().setHeadItem("credlimitflag", "1");
//			�������ںͶ����־�Ŀ��ƹ�ϵ
			String frozenFlag = getCardHeadItemValue("frozenflag");
			if (frozenFlag == null || !frozenFlag.equals("true")) {
				getCardPanel().getHeadItem("frozendate").setEnabled(false);
			}
			afterEditPK_cubasdoc1();
		}

		// ��ȡ���вɹ���������
		items = getCardPanel().getHeadShowItems("BUY");
		len = items == null ? 0 : items.length;
		if(buy) {
			 for (int i = 0; i < len; i++) 
			   if(items[i].isEdit())
				   items[i].setEnabled(buy);
			}
		 else {
			 for (int i = 0; i < len; i++) {
		       	items[i].setEnabled(buy);
				items[i].setValue(null);
			 }
		 }			

		if (buy) {
			getCardPanel().setHeadItem("b_discountrate", "100");
			getCardPanel().setHeadItem("b_pk_currtype1", getCurrency());
			getCardPanel().getHeadItem("b_developdate").setValue(
					getBusinessDate());

			//�������ںͶ����־�Ŀ��ƹ�ϵ
			String frozenFlag = getCardHeadItemValue("b_frozenflag");
			if (frozenFlag == null || !frozenFlag.equals("true")) {
				getCardPanel().getHeadItem("b_frozendate").setEnabled(false);
			}
		}
	}

	/**
	 * �����ݲ��뵽��Ƭ ��������
	 */
	public void setDataToCard() {
		// ��������
		setCustDataToCard(editVo);
		// ���ñ�������״̬
		setBodyState();
		//��ö���Ӧ�ա�ҵ��Ӧ�ա�������������ֶ�
		setSaleMny();
	}

	private void setSaleMny() {
		try {
		UFDouble[] mny = getMoney(editVo);
		if(mny != null) {			
			//����Ӧ��Ӧ��
			getCardPanel().setHeadItem("ordawmny",mny[0]);
			//ҵ��Ӧ��/Ӧ��
			getCardPanel().setHeadItem("busawmny",mny[1]);
			//���ö��
			if(getCardPanel().getHeadItem("creaditbalance") != null);
				getCardPanel().setHeadItem("creditbalance",mny[2]);
		}
		}catch(IndexOutOfBoundsException e) {
			Logger.error("Supply chain returns improper array short of length.");			
		}catch(Exception e) {
			Logger.error(e.getMessage(),e);
		}
	}


	/**
	 * ���ù�Ӧ���ṩ�ķ��񣬻�ȡ�������ֶε�����
	 * @param editVo
	 * @return
	 * @throws BusinessException
	 */
	private UFDouble[] getMoney(CustManVO editVo) throws BusinessException {
		String pk_cubasdoc=editVo.getParentVO().getPrimaryKey();
		if(!moneyMap.containsKey(pk_cubasdoc)){
			UFDouble[] mny = SupplyChainMny.getInstance().resetMnyFields(editVo,getBusinessDate());
			moneyMap.put(pk_cubasdoc, mny);
		}
		return moneyMap.get(pk_cubasdoc);
	}
	
	
	private void setBodyState() {
		BillModel bm = getCardPanel().getBillModel("ADDR");
		int rowCount = bm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			bm.setRowState(i, BillModel.NORMAL);
		}
		bm = getCardPanel().getBillModel("BANK");
		rowCount = bm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			bm.setRowState(i, BillModel.NORMAL);
		}
	}

	/**
	 * @param custvo
	 */
	private void setCustDataToCard(CustManVO custvo) {

		// �������ò��� �������ݿ������ò���
		CmdocVO docVO = custvo.convert();
		Object objtmp = ((CbmVO) docVO.getParentVO())
				.getAttributeValue("sealflag");
		m_sealflag = (objtmp == null) ? null : objtmp.toString();
		boolean isSealed = (m_sealflag == null || m_sealflag.equals("N")) ? false
				: true;

		getCardPanel().setBillValueVO(docVO);
		getCardPanel().getHeadItem("sealflag_b").setValue(
				new nc.vo.pub.lang.UFBoolean(isSealed));
		getCardPanel().execHeadLoadFormulas();
		getCardPanel().execTailLoadFormulas();
		int count = getCardPanel().getBillModel("BANK").getRowCount();
		for (int i = 0; i < count; i++) {
			String bankpk = (String) getCardPanel().getBillModel("BANK")
					.getValueAt(i, "pk_custbank");
			if (bankpk != null && bankpk.trim().length() > 0)
				getCardPanel().getBillModel("BANK").execLoadFormula();
		}
		getCardPanel().getBillModel("ADDR").execLoadFormula();
		getCardPanel().getBillModel("BANK").execLoadFormula();
		
//		// ���̱�־

		String sCustFlag1 = ((CumandocVO) ((CbmVO)docVO.getParentVO()).getCmfirst()).getCustflag();
	    String sCustFlag2 = ((CumandocVO) ((CbmVO)docVO.getParentVO()).getCmsecond()).getCustflag();
		UIComboBox custFlagCBox = null;
		if (sCustFlag1 != null && sCustFlag2 != null) {
			custFlagCBox = (UIComboBox) getCardPanel().getHeadItem("custflag")
					.getComponent();
			if (sCustFlag1.equals("0")) {
				custFlagCBox.setSelectedIndex(0);
			} else if (sCustFlag2.equals("1")) {
				custFlagCBox.setSelectedIndex(1);
			} else if (sCustFlag1.equals("2")) {
				custFlagCBox.setSelectedIndex(2);
			}
		}

		// ��������
		Integer iTemp = ((CustBasVO) custvo.getCustBasVO()).getCustprop();
		int iCustprop = (iTemp == null ? 0 : iTemp.intValue());
		UIComboBox custPropCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custprop").getComponent();
		if (custPropCBox != null) {
			custPropCBox.setSelectedIndex(iCustprop);
		}
		if (iCustprop == 0) //�ⲿ��λ
		{
			getCardPanel().getHeadItem("pk_corp1").setEnabled(false);
			getCardPanel().getHeadItem("pk_corp1").setValue(null);
		}else {
			UICheckBox freecust=(UICheckBox) getCardPanel().getHeadItem("freecustflag").getComponent();
			freecust.setEnabled(false);
			freecust.setSelected(false);
		}
       
		try {
			if (sCustFlag1.equals("0") || sCustFlag1.equals("2")) {
				//�Ƿ����ù���
				iTemp = ((CumandocVO[]) custvo.getChildrenVO())[0]
						.getCredlimitflag();
				int iCredlimitflag = (iTemp == null ? 0 : iTemp.intValue());
				UIComboBox credlimitCBox = (UIComboBox) getCardPanel()
						.getHeadItem("credlimitflag").getComponent();
				if (credlimitCBox != null) {
					credlimitCBox.setSelectedIndex(iCredlimitflag);
				}
				//����������Ϣ��Ĭ��ֵ
				//��չ����
				if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
				if (((CumandocVO[]) custvo.getChildrenVO())[0].getDevelopdate() == null)
					getCardPanel().getHeadItem("developdate").setValue(
							getBusinessDate());
				//Ĭ�Ͻ��ױ���
				if (((CumandocVO[]) custvo.getChildrenVO())[0]
						.getPk_currtype1() == null)
					getCardPanel().getHeadItem("pk_currtype1").setValue(
							getCurrency());
				}
				//�������ڵĿɱ༭��
				getCardPanel().getHeadItem("frozenflag").setEnabled(State==1&&isFrozenEditable);
				nc.vo.pub.lang.UFBoolean frozenFlag = ((CumandocVO[]) custvo
						.getChildrenVO())[0].getFrozenflag();
				if (frozenFlag == null || !frozenFlag.booleanValue())
					getCardPanel().getHeadItem("frozendate").setEnabled(false);
				
			}
			if (sCustFlag2.equals("1") || sCustFlag2.equals("3")) {
				//���òɹ���Ϣ��Ĭ��ֵ
				//��չ����
				if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
				if (((CumandocVO[]) custvo.getChildrenVO())[1].getDevelopdate() == null)
					getCardPanel().getHeadItem("b_developdate").setValue(
							getBusinessDate());
				//Ĭ�Ͻ��ױ���
				if (((CumandocVO[]) custvo.getChildrenVO())[1]
						.getPk_currtype1() == null)
					getCardPanel().getHeadItem("b_pk_currtype1").setValue(
							getCurrency());
				}
				//�������ڵĿɱ༭��
				getCardPanel().getHeadItem("b_frozenflag").setEnabled(State==1&&isFrozenEditable);
				nc.vo.pub.lang.UFBoolean frozenFlag = ((CumandocVO[]) custvo
						.getChildrenVO())[1].getFrozenflag();
				if (frozenFlag == null || !frozenFlag.booleanValue())
					getCardPanel().getHeadItem("b_frozendate")
							.setEnabled(false);
			}
			afterEditPK_cubasdoc1();
		} catch (Exception ex) {
			Logger.error("��������/�ɹ���Ĭ��ֵ�������⡣");
			Logger.error(ex.getMessage(),ex);
		}
	}

	/**
	 * -------------------------------------------------- ���ܣ� �ⲿ����
	 * 
	 * ���룺 Ҫչ�ֵĿ��̵���Ϣ �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-3 14:26:57)
	 * --------------------------------------------------
	 */
	public void showCustInfo(CustManVO vo) {
		// ��ѯ�ÿ�����Ϣ
		editVo = vo;
		// �л�����Ƭ״̬��չ��

		removeAll();

		billState = 1;
		add(getCardPanel(), "Center");

		this.validate();
		this.repaint();

		// �������ò��� �������ݿ������ò���

		setDataToCard();
		// �ɱ༭��
		getCardPanel().setEnabled(false);

	}

	/**
	 * ��ͷѡ�����仯����Ӧ�ı任�������� �������ڣ�(2003-6-5 9:34:06)
	 * 
	 * @param e
	 *            javax.swing.event.ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		int row = getListPanel().getHeadTable().getSelectedRow();
		if (hRow == row || row < 0) {
			return;
		}
		hRow = row;
		String settleUnitpk = selectCust();
		//Զ��֧���ڵ����ӵĿ����ڡ�Զ��֧�������̹��������͡��������������̹��������������Խ���"����"��"���ɽ��㵥λ"������
		if (settleUnitpk != null && settleUnitpk.length() > 0) {
			btn_Upgrade.setEnabled(false);

		} else {
			btn_Upgrade.setEnabled(true);		
		}
		updateButtons();
	}

	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
		TreePath tp = e.getPath();
		if (tp == null)
			return;

		XTreeNode node = (XTreeNode) tp.getLastPathComponent();
		String key = node.getPrimaryKey();
		onAreaChange(key, false);
	}

	/**
	 * ---------------------------------------------- ����˵���� ���û�ѡ��Ŀ��̵����������� ���룺
	 * String[] units :����ôη���ĵ�λ Object[] docs :ѡ�����з��������
	 * 
	 * �쳣�� ���ܲ������쳣 ���䣺 ���ָ������Ҫ��������Ϣ���򷵻ص�ȫ�Ǹ������͵���Ϣ
	 * 
	 * �������ڣ�(2003-8-18 14:06:39) ----------------------------------------------
	 */
	public java.lang.String assign(java.lang.String[] units,
			java.lang.Object[] docs) {

		if (docs == null || docs.length == 0) {
			MessageDialog.showErrorDlg(this, null, nc.vo.bd.BDMsg
					.MSG_CHOOSE_DATA());
			return null;
		}
		if (docs.length > 1) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10080806", "UPP10080806-000048")/*
																			    * @res
																			    * "Ŀǰֻ֧������һ������"
																			    */
					+ ".");
			return null;
		}
		//���ӶԵ��������Ƿ��ŵ��жϣ����ѡ�еĵ����ǹ�˾���ĵ�������֧������
		try {
			IAreaClassAndAddressQry areclQueryService = (IAreaClassAndAddressQry) NCLocator.getInstance().lookup(
					IAreaClassAndAddressQry.class.getName());
		    AreaclVO areaclVO = areclQueryService.queryAreaclByPk(((CustBasVO) docs[0]).getPk_areacl());

			if (areaclVO == null || !areaclVO.getPk_corp().equals("0001")) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10080806",
								"UPP10080806-000049")/* @res "��ѡ���ż��ĵ�������" */
						+ ".");
				return null;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return null;
		}
		//ʹ��UITreeToTree�ͽ�����unitsΪnull��ʵ������ʱ��ʹ�õ�ǰ��½��˾��
		boolean isCanceled = false;
		Boolean isForceCombineCode = null;
		Boolean isForceCombineName = null;

		while (!isCanceled) {
			try {
				CumandocBO_Client.upgrade((CustBasVO) docs[0], m_loginPkCorp,
						isForceCombineCode, isForceCombineName);
				//�ɹ��˳�
				MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10080806",
								"UPP10080806-000051")/* @res "�����ɹ�" */
						+ ".");
				isCanceled = true;

			} catch (BusinessException ex) {
				Logger.error(ex.getMessage(),ex);
				if("1".equals(ex.getHint())){
					//����ѡ������ظ��Ĵ���ʽ�ĶԻ���
					if (MessageDialog.showOkCancelDlg(this, null, ex
							.getMessage()
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080806", "UPP10080806-000101")
									/*�Ƿ��Ƚ��õ������䵽��Щ��˾��Ϊ�ͻ����������������ظ��ĵ�����ֻ�п��������ǿͻ����ܳɹ����ϲ���*/
							) == MessageDialog.ID_OK) {
						isForceCombineCode = new Boolean(true);
					} else {
						isCanceled = true;
					}
				} else if ("2".equals(ex.getHint())) {
					//����ѡ�������ظ��Ĵ���ʽ�ĶԻ���
					int userChoose = MessageDialog.showYesNoCancelDlg(this,
							null, ex.getMessage()
									+ nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("10080806",
													"UPP10080806-000101"),
							/*�Ƿ��Ƚ��õ������䵽��Щ��˾��Ϊ�ͻ����������������ظ��ĵ�����ֻ�п��������ǿͻ����ܳɹ����ϲ���*/						
									MessageDialog.YES_NO_CANCEL_OPTION);
					if (userChoose == MessageDialog.ID_YES) {
						isForceCombineName = new Boolean(true);
					} else if (userChoose == MessageDialog.ID_NO) {
						isForceCombineName = new Boolean(false);
					} else {
						isCanceled = true;
					}
				} else {
					showErrorMessage(ex.getMessage());
					return ex.getMessage();
				}
			}catch(Exception e){
				Logger.error(e.getMessage(),e);
				showHintMessage(e.getMessage());
				isCanceled = true;
			}
		}
		return null;
	}

	/**
	 * ---------------------------------------------- ����˵���� ȡ������ ���룺 String[]
	 * units :����ôη���ĵ�λ Object[] docs :ѡ�����з��������
	 * 
	 * �쳣�� ���ܲ������쳣
	 * 
	 * �������ڣ�(2003-8-18 14:06:39) ----------------------------------------------
	 */
	public java.lang.String cancelAssign(java.lang.String[] units,
			java.lang.Object[] docs) {
		return null;
	}

	/**
	 * -------------------------------------------------- ���ܣ� �����ȡָ����λ�Ѿ���������
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺 �����Լ������ķ�ʽ������Ҳ���Ե��� ֻ��������
	 * 
	 * �������ڣ�(2003-9-28 15:08:57)
	 * --------------------------------------------------
	 */
	public java.lang.Object[] getAssigned(java.lang.String unitPrimaryKey) {
		return null;
	}


	/**
	 * �ӿ�Ƭ�ϻ�ȡ ������Ϣ -- ���ҽ������ݼ�� �������ڣ�(2003-6-19 16:47:39)
	 */
	private CustManVO getCustManVO(boolean isChanged) {
		//ȡֵ
		CmdocVO rs = null;
		if (isChanged){
			rs = (CmdocVO) getCardPanel().getBillValueChangeVOExtended(
					CmdocVO.class.getName(),
					CbmVO.class.getName(),
					new String[] { CustAddrVO.class.getName(),
							CustBankVO.class.getName() });
		    restoreTsFromBuffer(rs);
		}
		else
			rs = (CmdocVO) getCardPanel().getBillValueVOExtended(
					CmdocVO.class.getName(),
					CbmVO.class.getName(),
					new String[] { CustAddrVO.class.getName(),
							CustBankVO.class.getName() });
//		if (rs != null) 
//			setCorpForNewAddedCustBank(rs);
		
		//�������ͣ���������
		CustManVO custVO = rs.convert();
		UIComboBox custFlagCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custflag").getComponent();

		if (custFlagCBox != null) {
			int index = custFlagCBox.getSelectedIndex();
			//�ͻ�
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(0 + "");
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(4 + "");
			}
			//��Ӧ��
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(" ");
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(1 + "");
			}
			//����
			if (index == 2) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(2 + ""); // �ͻ�
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(3 + ""); // ��Ӧ��
			}
		}
		// ��������
		UIComboBox custPropCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custprop").getComponent();
		if (custPropCBox != null) {
			int index = custPropCBox.getSelectedIndex();
			((CustBasVO) custVO.getCustBasVO()).setCustprop(new Integer(index));
		}

		// �Ƿ����ù���
		UIComboBox custCBox = (UIComboBox) getCardPanel().getHeadItem(
				"credlimitflag").getComponent();
		if (custCBox != null) {
			int index = custCBox.getSelectedIndex();
			((CumandocVO) custVO.getChildrenVO()[0])
					.setCredlimitflag(new Integer(index));
			((CumandocVO) custVO.getChildrenVO()[1])
					.setCredlimitflag(new Integer(-1));
		}
		//��˾����
		((CumandocVO) custVO.getChildrenVO()[0]).setPk_corp(getpk_corp());
		((CumandocVO) custVO.getChildrenVO()[1]).setPk_corp(getpk_corp());
		custVO.setBanks(new CustBankVO[0]);
		return custVO;
	}

	private void restoreTsFromBuffer(CmdocVO rs) {
        if(editVo != null){
        	UFDateTime dTime = ((CustBasVO)editVo.getParentVO()).getTs();
        	((CbmVO)rs.getParentVO()).setAttributeValue("ts", dTime);
        }
	}


	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-6-13 8:46:43)
	 * 
	 * @return nc.ui.bd.b08.AssignDlg
	 */
	public UITreeToTree getUpgradeDlg() {
		if (upgradeDlg == null) {
			upgradeDlg = new UITreeToTree(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10080806", "UPP10080806-000053")/*
																			    * @res
																			    * "���̵�������"
																			    */);
			upgradeDlg.setTreeDetail(getUpgradeTreeDetail());
			
			upgradeDlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000053")/* @res "���̵�������" */);
			upgradeDlg.setUser(this);

			upgradeDlg.setMarjorClass(CustBasVO.class);

			upgradeDlg.getUITreeToTreePanel1().getUIButton1().setVisible(false);
			//upgradeDlg.getUITreeToTreePanel1().getAssignBtn().setLocation()Visible(false);

		}
		return upgradeDlg;
	}

	/**
	 * -------------------------------------------------- ���ܣ� ��ȡ�ƶ�������ϸ��Ϣ
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-10-10 16:20:55)
	 * --------------------------------------------------
	 */
	public TreeDetail getUpgradeTreeDetail() {
		if (upgradeDetail == null) {
			// ָ��������ϸ����
			upgradeDetail = new TreeDetail();
			{

				// ��˾�������͵�ָ��
				MethodGroup mg = new MethodGroup();
				try {

					mg.setKeyField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getPrimaryKey", null));
					mg.setAssKeyField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getPk_fatherarea", null));
					mg.setNameField(nc.vo.bd.b08.CustAreaVO.class.getMethod(
							"getAreaclname", null));
					mg.setSortCodeFiled(nc.vo.bd.b08.CustAreaVO.class
							.getMethod("getAreaclcode", null));

					//
					mg.setCombinProviderKeyField(nc.vo.bd.b08.CustAreaVO.class
							.getMethod("getPrimaryKey", null));
					//
					mg.setHowDisplay(new boolean[] { false, true, true }); // ��ʾ�����������
					mg.setAimClass(nc.vo.bd.b08.CustAreaVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				MethodGroup cbmg = new MethodGroup();
				try {
					cbmg.setKeyField(nc.vo.bd.b08.CbdocVO.class.getMethod(
							"getKey", null));

					cbmg.setNameField(nc.vo.bd.b08.CbdocVO.class.getMethod(
							"getCustName", null));
					cbmg.setSortCodeFiled(nc.vo.bd.b08.CbdocVO.class.getMethod(
							"getCustCode", null));
					cbmg.setCombinKeyField(nc.vo.bd.b08.CbdocVO.class
							.getMethod("getAssKey", null));
					cbmg.setHowDisplay(new boolean[] { false, true, true }); // ��ʾ�����������
					cbmg.setAimClass(nc.vo.bd.b08.CbdocVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				MethodGroup cmg = new MethodGroup();
				try {
					cmg.setKeyField(nc.vo.bd.b08.CustBasVO.class.getMethod(
							"getPrimaryKey", null));
					cmg.setAssKeyField(nc.vo.bd.b08.CustBasVO.class.getMethod(
							"getPk_areacl", null));
					cmg.setNameField(nc.vo.bd.b08.CustBasVO.class.getMethod(
							"getCustname", null));
					cmg.setSortCodeFiled(nc.vo.bd.b08.CustBasVO.class
							.getMethod("getCustcode", null));
					cmg.setHowDisplay(new boolean[] { false, true, true }); // ��ʾ�����������
					cmg.setAimClass(nc.vo.bd.b08.CustBasVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				// ��Ҫ
				upgradeDetail.setMg(new MethodGroup[] { mg, cbmg, cmg });

				//
				upgradeDetail.setRootname(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10080806", "UPP10080806-000054")/*
																	  * @res
																	  * "�������� "
																	  */);

				//
				upgradeDetail.setLeafClass(nc.vo.bd.b08.CustBasVO.class);
			}
		}
		return upgradeDetail;
	}

	/**
	 * -------------------------------------------------- ���ܣ�
	 * ʹ���˶��Ի�ȡ���ݵķ�ʽ�Ժ󣬸���param��ȡ��ص���ֵ
	 * 
	 * ���룺 �ڵ㸺�ص����� ����� �ýڵ��¼��ڵ����� �쳣��
	 * 
	 * ���䣺 ʹ����Ӧ�ò�ȡ��ʩ�����͵���̨�����ȡ���ݵĴ����� ͬʱʹ����Ӧ�ü�⴫�������
	 * 
	 * �������ڣ�(2003-9-28 15:08:57)
	 * --------------------------------------------------
	 */
	public java.lang.Object[] getValue(java.lang.Object[] param) {
		// ����ط� param Ϊ��������
		int len = param == null ? 0 : param.length;
		if (len == 0)
			return null;
		else {
			String[] pk_area = new String[len];
			for (int i = 0; i < len; i++)
				pk_area[i] = ((CustAreaVO) param[i]).getPk_areacl();
			CustBasVO[] vos = null;
			try {
				vos = CubasdocBO_Client.getCustInArea(pk_area, m_loginPkCorp);
			} catch (Exception ex) {
				Logger.error(ex.getMessage(),ex);
			}
			return vos;
		}
	}

	/**
	 * -------------------------------------------------- ���ܣ�
	 * 
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-3 14:46:06)
	 * --------------------------------------------------
	 */
	public void onPrint_direct() {

		try {
			BillItem[] oldbiBodies = getListPanel().getHeadBillModel()
					.getBodyItems();
			Vector v = new Vector();
			for (int i = 0; i < oldbiBodies.length; i++) {
				if (oldbiBodies[i].isShow())
					v.add(oldbiBodies[i]);
			}
			BillItem[] biBodies = new BillItem[v.size()];
			for (int i = 0; i < v.size(); i++) {
				biBodies[i] = (BillItem) v.elementAt(i);
			}
			int iCols = biBodies.length;
			String[] sPrintNames = new String[iCols];
			int[] align = new int[iCols];
			int[] width = new int[iCols];
			for (int i = 0; i < iCols; i++) {
				sPrintNames[i] = biBodies[i].getName();
				align[i] = (biBodies[i].getDataType() == 2 ? 2 : 0);
				width[i] = biBodies[i].getWidth();
			}
			String[][] colname = new String[][] { sPrintNames };
			Object[][] data = new Object[getListPanel().getHeadTable()
					.getRowCount()][iCols];
			for (int i = 0; i < getListPanel().getHeadTable().getRowCount(); i++) {
				for (int j = 0; j < iCols; j++) {
					Object tmp = getListPanel().getHeadTable().getValueAt(i, j);
					if (tmp != null) {
						if (tmp.toString().equals("false"))
							tmp = "";
						if (tmp.toString().equals("true"))
							tmp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080806", "UPP10080806-000055")/* @res "��" */;
					}
					data[i][j] = tmp;
				}
			}
			String title = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000056")/* @res "���̹������б�" */;
			Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);
			Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);
			String topstr = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UC000-0000404")/* @res "��˾" */
					+ ":"
					+ ClientEnvironment.getInstance().getCorporation()
							.getUnitname();
			String botstr = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UC000-0000677")/* @res "�Ʊ�����" */
					+ ":" + ClientEnvironment.getInstance().getDate();
			PrintDirectEntry print = new PrintDirectEntry();
			print.setTitle(title); //���� ��ѡ
			print.setTitleFont(font); //�������� ��ѡ
			print.setContentFont(font1); //�������壨��ͷ����񡢱�β�� ��ѡ
			print.setTopStr(topstr); //��ͷ��Ϣ ��ѡ
			print.setBottomStr(botstr); //��β��Ϣ ��ѡ
			print.setColNames(colname); //�����������ά������ʽ��
			print.setData(data); //�������
			print.setColWidth(width); //����п� ��ѡ
			print.setAlignFlag(align); //���ÿ�еĶ��뷽ʽ��0-��, 1-��, 2-�ң���ѡ
			print.setCombinCellRange(null); //���ͷ���֨D�D�ϲ���Ԫ��Χ
			print.preview(); //Ԥ��
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	/**
	 * -------------------------------------------------- ���ܣ�
	 * 
	 * 
	 * ���룺
	 * 
	 * �����
	 * 
	 * �쳣��
	 * 
	 * ���䣺
	 * 
	 * 
	 * �������ڣ�(2003-11-3 14:46:06)
	 * --------------------------------------------------
	 */
	public void onUpgrade() {
		getUpgradeDlg().setTotalData(areaVos);

		getUpgradeDlg().showModal();

	}

	private boolean isFrozenEditable = false;//�����־�ܷ�༭

	//������ģ����ѯ
	/*
	 * int QICount = 5; String QIcode[] = { "bd_cubasdoc.custcode",
	 * "bd_cubasdoc.custname", "bd_cubasdoc.custshortname",
	 * "bd_cubasdoc.engname", "bd_cubasdoc.mnecode" }; String QIname[] = {
	 * "���̱��", "��������", "���̼��", "��������", "������" }; int QIpro[] = {
	 * QueryItem.Style_String, QueryItem.Style_String, QueryItem.Style_String,
	 * QueryItem.Style_String, QueryItem.Style_String };
	 */
	//��ѯ�Ի���
	private IRefQueryDlg m_queryDlg = null;

	private String m_sealflag = null;//��¼�޸����ݵķ���־

	private IBillcodeRuleService autoCodeService;

	private IBillcodeRuleQryService autoCodeQry;

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-7-17 22:24:38)
	 * 
	 * @return nc.ui.bd.b09.CustQueryDlg
	 */
	private IRefQueryDlg getQueryDlg() {
		if (m_queryDlg == null) {
				TemplateInfo tempinfo = new TemplateInfo();
				tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
//				���ǵ�Զ�̽���ڵ㣬���ﲻ��ʹ��getModuleCode();
				tempinfo.setFunNode(MODULE_CODE);   
				tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
				tempinfo.setBusiType(null);
				tempinfo.setNodekey(null);
				m_queryDlg = new CustManQueryDlg(this, tempinfo);
				m_queryDlg.setPk_corp("0001");
			}
			return m_queryDlg;
		}

	/**
	 * ��ѯ
	 * 
	 * �������ڣ�(2003-6-9 14:49:40)
	 */
	public void onQuery() {		
		try {		
			if (getQueryDlg().showModal() == UIDialog.ID_OK) {
				getAreaTree().gettree().setSelectionRow(0);
				sCurrentQueryCondition = ((CustManQueryDlg)getQueryDlg()).getWhereSQL();
				loadQueryData(sCurrentQueryCondition);
                isQueryCustOnList = true;
			}
		} catch (Exception ex) {
			handleException(ex);
		}
	}


	private void dataNotNullValidate(int custflag) throws ValidationException {

		String table_code_buy = "BUY";
		String table_code_sale = "SALE";

		String notCheckTableCode = null;
		// �ͻ�
		if (custflag == 0) {
			notCheckTableCode = table_code_buy;
		}
		// ��Ӧ��
		if (custflag == 1) {
			notCheckTableCode = table_code_sale;
		}

		BillData bill = getCardPanel().getBillData();

		String message = null;
		BillItem[] headtailitems = bill.getHeadTailItems();
		if (headtailitems != null) {
			for (int i = 0; i < headtailitems.length; i++) {
				if (notCheckTableCode == null
						|| !notCheckTableCode.equals(headtailitems[i]
								.getTableCode())) {
					if (headtailitems[i].isNull())
						if (isNULL(headtailitems[i].getValueObject())) {
							message = "[" + headtailitems[i].getName() + "]";
							break;
						}
				}
			}
		}
		if (message != null)
			throw new NullFieldException(message.toString());

		//���Ӷ��ӱ��ѭ��
		String[] tableCodes = bill.getTableCodes(BillData.BODY);
		if (tableCodes != null) {
			for (int t = 0; t < tableCodes.length; t++) {
				String tablecode = tableCodes[t];
				for (int i = 0; i < bill.getBillModel(tablecode).getRowCount(); i++) {
					BillItem[] items = bill.getBodyItemsForTable(tablecode);
					OUTER: for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						int[] rows = null;
						if (item.isNull()) {
							if (rows != null) {
								for (int ii = 0; ii < rows.length; ii++) {
									if (rows[ii] == i)
										continue OUTER;
								}
							}
							Object aValue = bill.getBillModel(tablecode)
									.getValueAt(i, item.getKey());
							if (isNULL(aValue)) {
								if (tableCodes.length == 1) {
									message = " "
											+ (i + 1)
											+ "("
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("_Bill",
															"UPP_Bill-000002")/*
																			   * @res
																			   * "��"
																			   */
											+ ") " + item.getName();
								} else
									message = " "
											+ bill.getTableName(IBillItem.BODY,
													tablecode)
											+ "("
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("_Bill",
															"UPP_Bill-000003")/*
																			   * @res
																			   * "ҳǩ"
																			   */
											+ ") "
											+ (i + 1)
											+ "("
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("_Bill",
															"UPP_Bill-000002")/*
																			   * @res
																			   * "��"
																			   */
											+ ") " + "[" +item.getName() + "]";
								break;
							}
						}
					}
					if (message != null) {
						break;
					}
				}
			}
			if (message != null)
				throw new NullFieldException(message.toString());
		}
	}

	private boolean isNULL(Object o) {
		if (o == null || o.equals(""))
			return true;
		return false;
	}

	/**
	 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
	 * 
	 * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա�
	 * 
	 * �������ڣ�(2001-8-8 13:52:37)
	 */
	
	public boolean onClosing() {
		FinishEditEvent event = null;
    	if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
    			int i = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UCH001"/*�Ƿ񱣴����޸ĵ����ݣ�*/),UIDialog.ID_CANCEL);
    			switch (i) {
    			case UIDialog.ID_YES:
    			{
    					if(billOperate == IBillOperate.OP_ADD ){
    						return onSaveAdd();
    					}
    					else {
    						return onSaveMod();
    					}
    				
    			}
    			case UIDialog.ID_NO:
    			{
    				event = new FinishEditEvent(this, false, null);
        			fireEditFinish(event);
        			returnCustCode();
        			return true;
    			}
    			case UIDialog.ID_CANCEL:
    			{
    				return false;
    			}
    				

    			default:{
    				event = new FinishEditEvent(this, true, editVo);
            		fireEditFinish(event);
            		return true;
    			}
    			}

    		}else{
    			event = new FinishEditEvent(this, true, editVo);
        		fireEditFinish(event);
        		return true;
    		}		  		
	}
	
	public IBillcodeRuleQryService getAutoCodeQry() {
		if(autoCodeQry==null) {
			autoCodeQry=NCLocator.getInstance().lookup(IBillcodeRuleQryService.class);
		}
		return autoCodeQry;
	}
	public IBillcodeRuleService getAutoCodeService() {
		if(autoCodeService==null) {
			autoCodeService = (IBillcodeRuleService) NCLocator.getInstance().lookup(IBillcodeRuleService.class.getName());
		}
		return autoCodeService;
	}

	/**
	 * 
	 * @param lister
	 */
	public void addFinishEditLister(FinishEditListener lister) {
		listenerList.add(FinishEditListener.class, lister);
	}

	/**
	 * 
	 * @param e
	 */
	public void fireEditFinish(FinishEditEvent e) {
		if (e == null)
			return;
		EventListener[] listeners = listenerList
				.getListeners(FinishEditListener.class);
		if (listeners != null && listeners.length > 0) {
			for (int i = 0; i < listeners.length; i++) {
				((FinishEditListener) listeners[i]).editFinished(e);
			}
		}

	}

	/**
	 * ������������ʱ��ʼ������
	 */
	public void initForAdding() {
		System.out.println("����initForAdding.");
		isDirectlyAdd = true;
		resetButtons();
		State = 1;
		onAdd();
		updateButtons();
	}


	/**
	 * �������ð�ť��
	 */
	private void resetButtons() {
		card_edit_Add = new ButtonObject[] { btn_Card_AddRow, btn_Card_DelRow,btn_Card_Save};
		list_btns = new ButtonObject[] { btn_Edit, btn_BankAccount,btn_Detail};
		card_detail = new ButtonObject[] {btn_Card_Edit, btn_Card_BankAccount , btn_Card_Back };
	}


	/**
	 * ���ý���ĳ�ʼֵ
	 */
	public void setInitData(Object data) {

		if (data == null || !(data instanceof CustManVO)) {
			return;
		}

		editVo = (CustManVO) data;
		String pk_corp = (String)editVo.getParentVO().getAttributeValue("pk_corp");
	    if(pk_corp != null && pk_corp.length() > 0){
	           setPk_corp(pk_corp);
	           getCardPanel().setHeadItem("pk_corp",pk_corp);
	           BillItem[] items = getCardPanel().getHeadItems();
	           modifyCorpForRefItems(items,pk_corp);
	           items = getCardPanel().getBodyItems();
	           modifyCorpForRefItems(items,pk_corp);
	           
	           recontructAreaTree();
	        }
		processCardforEdit();
		setCustDataToCard(editVo);
		
		//		 ���ñ�������״̬
		BillModel bm = getCardPanel().getBillModel("ADDR");
		int rowCount = bm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			bm.setRowState(i, BillModel.ADD);
		}

	}


	private void modifyCorpForRefItems(BillItem[] items,String pk_corp) {
		 UIRefPane refpane = null;
		 for(BillItem item : items){
			 if(item.getDataType() == IBillItem.UFREF){
				 refpane=(UIRefPane)item.getComponent();
				 refpane.getRefModel().setPk_corp(pk_corp);
			 }
		 }
	}
	
	/**
	 * ���Ĳ���
	 */
	private void onBatchUpdate() {
		BatchUpdateDlg dlg = new BatchUpdateDlg(this, "custdoc", false,
				new CustBUItemFactory());
		dlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206",
				"UPP10081206-000012")/* @res "����" */);
		dlg.setDefaultRefOrg(m_loginPkCorp);
		dlg.setSelectedPks(getSelectedPKs());
		int updateResult = dlg.showModal();
		// ˢ��
		switch (updateResult) {
		case BatchUpdateDlg.UPDATEALL:
			try {
				getCacheModel().createTree(areaVos);
			} catch (TreeOperationException e) {
				Logger.error(e.getMessage(), e);
				showHintMessage("Error occurs when refreshing the area class tree.");
			}
		case BatchUpdateDlg.UPDATEBYPKS:
			onRef();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10081206", "UPP10081206-000122")/* @res "�������" */);
		case BatchUpdateDlg.CANCELUPDATE:
			//û��ִ�����ģ�ʲôҲ����
		}
	}
	
	/**
	 * ���ؽ�����ѡ�еĿ�������
	 * @return
	 */
	private String[] getSelectedPKs() {
		UITable headTable = getListPanel().getHeadTable();
		if(headTable.getSelectedRowCount()==0) {
			return new String[0];
		}
		int[] selecteds = headTable.getSelectedRows();
		ArrayList al = new ArrayList();
		for (int i = 0; i < selecteds.length; i++) {
			AggregatedValueObject billvo = getListPanel()
					.getBillValueVO(selecteds[i],
							"nc.vo.bd.b09.CustManVO",
							"nc.vo.bd.b08.CustBasVO",
							"nc.vo.bd.b09.CumandocVO");
			
			al.add(((CustBasVO) billvo.getParentVO()).getPrimaryKey());
		}
		return (String[])al.toArray(new String[al.size()]);
	}
	
	
	public void bodyRowChange(BillEditEvent e) {
		
	}

	/*************************************************************************************
	 * ����ķ�����Ϊ��ʵ�����ݵ��빦��
	 *************************************************************************************/
	
	/** 
     * ���ص����Ƿ�ɵ�����Ϣ
     *
     * @see nc.itf.trade.excelimport.IImportableEditor#getImportableInfo()
     */
    public ImportableInfo getImportableInfo() {
		UFBoolean canImport = UFBoolean.FALSE; // �ɵ���
		try {
			canImport = SysInitBO_Client.getParaBoolean(getpk_corp(), "BD002");
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
		return new ImportableInfo(canImport.booleanValue(),
				ImportableInfo.REASON);
	}
	
	public void addNew() {
		isImport=true;
		if (billState == 0) {
			onChange();
		}
		State = 1;
		onCardAdd();
	}

	public void cancel() {
		getBillData().clearViewData();
		getCardPanel().setEnabled(false);
		State = 0;
		billOperate = IBillOperate.OP_NOTEDIT;
	}

	/** 
	 * �������
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#save()
	 */
	public void save() throws Exception {
		checkVO(getCustManVO(false));
		if (!onSaveAdd())
			throw new RuntimeException("����ʧ��");
	}

	/**
	 * �Ե�ǰ�����VO���ݽ���У��
	 */
	private void checkVO(CustManVO custManVO) {
		CustdocAggVOChecker.checkNumber(custManVO);
//		CustdocAggVOChecker.checkCustdocAggVO(custManVO);
	}

	
	public void setValue(Object obj) {
		setDefaultDataForImport();
		ExtendedAggregatedValueObject aggvo = (ExtendedAggregatedValueObject) obj;
		process(aggvo);
		getCardPanel().getBillData().setImportBillValueVO(aggvo);
		adjustBillByLogic();
	}

	/**
	 * ����Ĭ������
	 */
	private void setDefaultDataForImport(){
		setCardDefaultData();
		// ������ʱ�� ҵ����Ϣ�� �������� Ĭ��Ϊ "����"
		// �����Ա��� �ɹ���Ϣ �� ҵ����Ϣ ��Ĭ��ֵ
		setCustVisaul(2);
	}
	
	/**
	 * ����ҵ����������vo
	 */
	private void process(ExtendedAggregatedValueObject aggvo) {
		if (aggvo != null) {
			// ��������"���̹�����"VO
			CustManVOProcessor.processVO(aggvo.getParentVO());
			// �����ӱ�"�����շ�����ַ"VO����
			CustAddrVOProcessor.processVOs(aggvo.getTableVO("ADDR"));
		}
	}
	
	/**
	 * ����ҵ���߼�����������ؿؼ�
	 */
	private void adjustBillByLogic() {
		CustdocBillAdjustor adjustor = new CustdocBillAdjustor(getBillData());
		adjustor.adjustBillByCustprop();
		adjustor.adjustBillByPk_cubasdoc1();
		adjustor.adjustBillByCuststate();
		adjustor.adjusyBillByFrozenflag();
		adjustor.adjustBillByCustflag();
		adjustor.adjustBillByInnerctldays();
	}
	
	public List<InputItem> getInputItems() {
		List<InputItem> items = InputItemCreator.getInputItems(getBillData(), false);
		processSpecialItems(items);
		return items;
	}

	/**
	 * �����б��е��������ԣ�
	 * 1������������"defaddrflag"(�ӱ� �����շ�����ַ "�Ƿ�Ĭ�ϵ�ַ")����Ϊ������
	 * 2��������"sealflag_b"(ҳǩ ҵ����Ϣ "�Ƿ���")��Ϊ���ɱ༭
	 * 3����"�ɹ���Ϣ"��"������Ϣ"ҳǩ������������Ӻ�׺�Խ�������
	 */
	private void processSpecialItems(List<InputItem> items) {
		for (InputItem item : items) {
			if ("defaddrflag".equals(item.getItemKey())) {
				InputItemImpl newItem = InputItemImpl.getEquivalent(item);
				newItem.setNotNull(true);
				items.set(items.indexOf(item), newItem);
			}
			if ("sealflag_b".equals(item.getItemKey())) {
				InputItemImpl newItem = InputItemImpl.getEquivalent(item);
				newItem.setEdit(false);
				items.set(items.indexOf(item), newItem);
			}
			if (CbmVOAttrInfoQryUtil.isBuyOrSaleAttribute(item.getItemKey())) {
				InputItemImpl newItem = InputItemImpl.getEquivalent(item);
				if (CbmVOAttrInfoQryUtil.isBuyAttribute(item.getItemKey())) {
					newItem.setShowName(item.getShowName() + BUY_INFO);
				} else {
					newItem.setShowName(item.getShowName() + SALE_INFO);
				}
				items.set(items.indexOf(item), newItem);
			}
		}
	}
	
	private BillData getBillData() {
		return getCardPanel().getBillData();
	}


	@SuppressWarnings("unchecked")
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		String pk_cubasdoc = maintaindata.getBillID();
		if (StringUtils.isBlank(pk_cubasdoc))
			return;
		
		IUAPQueryBS queryService = NCLocator.getInstance().lookup(
				IUAPQueryBS.class);
		String whereSQL = "pk_cubasdoc = '" + pk_cubasdoc + "'";
		String pk_areacl = null;
		Collection<CustBasVO> results = null;
		try {
			results = queryService.retrieveByClause(CustBasVO.class, new CustBasMapping(), whereSQL, new String[] { "pk_areacl" });
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessRuntimeException(e.getMessage());
		}
			CustBasVO[] custBasVOs = results.toArray(new CustBasVO[0]);
			if(custBasVOs==null||custBasVOs.length==0){
				return;
			}
			pk_areacl = custBasVOs[0].getPk_areacl();
		

		// ���ݴ���λ���ݵ������Ϣ����UI������ȥ��λ
		if (StringUtils.isNotBlank(pk_areacl)) {
			XTreeNode locationNode = null;
			// ���ݴ���λ�Ŀ��̵����ĵ������������������������ݣ������������Ҳ��б�/��Ƭ�ϵ�VO��
			onAreaChange(pk_areacl, false);
			try {
				locationNode = getAreaModel().getNode(pk_areacl);	
			} catch (TreeOperationException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showWarningDlg(this,
						NCLangRes.getInstance().getStrByID("_bill",
								"UPP_Bill-000034")/* @res "��ʾ" */, NCLangRes
								.getInstance().getStrByID("_beans",
										"UPP_Beans-000079")/* @res "��λ" */
								+ NCLangRes.getInstance().getStrByID("common",
										"UC001-0000053")/* @res "����" */
								+ NCLangRes.getInstance().getStrByID(
										"template", "UPP_Template-000065")/*
																			 * @res
																			 * "����"
																			 */
				)/* @res "��λ���ݴ���" */;
			}
			TreePath treePath = null;
			// ��λ�����ϵĵ������ࣨ��Ӧ��ǰ���������ĵ������ࣩ
			if (locationNode != null) {
				treePath = new TreePath(locationNode.getPath());
				getAreaTree().gettree().expandPath(treePath.getParentPath());
				getAreaTree().gettree().setSelectionPath(treePath);
			}
			
			// ��λ�Ҳ��б��ϵĴ�������
			try {
				locationNode = getCacheModel().getNode(pk_areacl);
			} catch (TreeOperationException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showWarningDlg(this,
						NCLangRes.getInstance().getStrByID("_bill",
								"UPP_Bill-000034")/* @res "��ʾ" */, NCLangRes
								.getInstance().getStrByID("_beans",
										"UPP_Beans-000079")/* @res "��λ" */
								+ NCLangRes.getInstance().getStrByID("common",
										"UC001-0000053")/* @res "����" */
								+ NCLangRes.getInstance().getStrByID(
										"template", "UPP_Template-000065")/*
																			 * @res
																			 * "����"
																			 */
				)/* @res "��λ���ݴ���" */;
			}
			
			int totalCustByOneAreaCl = locationNode.getChildCount();
			int currentRow = -1;
			ArrayList list = new ArrayList();
			for (int i = 0; i < totalCustByOneAreaCl; i++) {
				XTreeNode currentNode = (XTreeNode) locationNode.getChildAt(i);
				if(currentNode.getValue() instanceof CustManVO){
					CustManVO cumanVO = (CustManVO) currentNode.getValue();
					CustBasVO custBasVO = (CustBasVO) cumanVO.getParentVO();
					list.add(custBasVO.getPrimaryKey());
				}
			}
			currentRow=list.indexOf(pk_cubasdoc);
			getListPanel().getHeadTable().setRowSelectionInterval(currentRow,
					currentRow);
			// ���ݵ�ǰ�ж�λ�б�����ϵĿ��̼�¼
			selectCust();
			// �л�����Ƭ����
			onDetail();		
	}
	}
}