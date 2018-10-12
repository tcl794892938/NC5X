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
 * 客商管理档案
 * 
 * 创建日期：(2003-6-3 11:29:13)
 * 
 * @author：屈淑轩
 */
public class CustManUI extends ToftPanel implements TreeSelectionListener,
		ListSelectionListener, BillEditListener, BillEditListener2,
		ActionListener, IAssign, IDirectlyAdd,IImportableEditor,ILinkMaintain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7935728431386592872L;

	private static final String MODULE_CODE="10080806";//节点编号
	
	/** 采购信息 数据导入使用 */
	private static final String BUY_INFO = "(采购信息)";
	/** 销售信息 数据导入使用 */
	private static final String SALE_INFO = "(销售信息)";

	private XTreePane AreaTree = null;

	private UISplitPane SplitPane1 = null;

	private UIPanel CustPanel = null;

	private BillListPanel ListPanel = null;

	private BillCardPanel cardPanel = null;
	//银行帐户
	private Uif2Dialog custBankDialog;

	/**
	 * 菜单组
	 */// by tcl
	private ButtonObject btn_zdydc = new ButtonObject("渠道客商导出","导出渠道成员客商", 0, "导出");

	// 总观状态
	private ButtonObject btn_Add = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "增加" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "增加" */, 1, "增加"); /*-=notranslate=-*/

	private ButtonObject btn_Edit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "修改" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "修改" */, 2, "修改"); /*-=notranslate=-*/

	private ButtonObject btn_Del = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "删除" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "删除" */, 3, "删除"); /*-=notranslate=-*/

	private ButtonObject btn_Query = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000006")/* @res "查询" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000006")/* @res "查询" */, 0, "查询"); /*-=notranslate=-*/

	private ButtonObject btn_Upgrade = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000127")/* @res "升级" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000021")/* @res "档案升级到集团" */, 0, "升级"); /*-=notranslate=-*/


	private ButtonObject btn_DocMan = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000126")/* @res "档案管理" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPT10080806-000126")/* @res "档案管理" */, 5, "档案管理"); /*-=notranslate=-*/

	private ButtonObject btn_Ref = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "刷新" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "刷新" */, 5, "刷新"); /*-=notranslate=-*/

	private ButtonObject btn_Detail = new CardBtnVO().getButtonVO().buildButton();

	private ButtonObject btn_Print = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000007")/* @res "打印" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000007")/* @res "打印" */, 0, "打印"); /*-=notranslate=-*/

	private ButtonObject btn_Output = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000056")/* @res "导出" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000056")/* @res "导出" */, 0, "导出");

	// 详细状态
	private ButtonObject btn_Card_Add = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "增加" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000002")/* @res "增加" */, 1, "增加"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Edit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "修改" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000045")/* @res "修改" */, 2, "修改"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Del = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "删除" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000039")/* @res "删除" */, 3, "删除"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Explorer = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000021")/* @res "浏览" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000021")/* @res "浏览" */, 5, "浏览"); /*-=notranslate=-*/

	private ButtonObject btn_Card_First = new FirstBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Next = new NextBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Pre = new PrevBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Last = new LastBtnVO().getButtonVO().buildButton();
	private ButtonObject btn_Card_Ref = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "刷新" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000009")/* @res "刷新" */, 5, "刷新"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Back = new ReturnBtnVO().getButtonVO().buildButton();

	// 编辑状态
	private ButtonObject btn_Card_AddRow = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000012")/* @res "增行" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000012")/* @res "增行" */, 5, "增行"); /*-=notranslate=-*/

	private ButtonObject btn_Card_DelRow = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000013")/* @res "删行" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000013")/* @res "删行" */, 5, "删行"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Save = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "保存" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "保存" */, 3, "保存"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Mod_Save = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "保存" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000001")/* @res "保存" */, 5, "保存"); /*-=notranslate=-*/

	private ButtonObject btn_Card_Cancel = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000008")/* @res "取消" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UC001-0000008")/* @res "取消" */, 0, "取消"); /*-=notranslate=-*/
	
	private ButtonObject btn_Batch_update = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206","UPP10081206-000012")/*@res "批改"*/,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206","UPP10081206-000012")/*@res "批改"*/, 2, "批改"); /*-=notranslate=-*/
	
    private ButtonObject btn_BankAccount = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "银行账户" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "银行账户" */, 5, "银行账户");

	private ButtonObject btn_Card_BankAccount = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "银行账户" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
					"UPP10080806-000105")/* @res "银行账户" */, 5, "银行账户"); 
	
	private ButtonObject[] list_btns ;

	private ButtonObject[] card_detail ;

	private ButtonObject[] card_edit_Add;

	private ButtonObject[] card_edit_Mod;

	//是否导入档案
	private boolean isImport;

	/*
	 * 当前模板状态 0、列表 1、 卡片
	 */
	int billState = 1;

	/*
	 * 地区分类
	 */
	XTreeModel areaModel;

	CustAreaVO areaVos[];

	TreeDetail detail;

	/*
	 * 缓冲
	 */
	XTreeModel cacheModel;

	// 当前编辑的行
	CustManVO editVo = null;

	int hRow = -1;


	// 本位币
	private String m_currency = null;

	//
	nc.vo.pub.lang.UFBoolean corpADD = null;

	// 查询条件
	private String sCurrentQueryCondition = null;

//  目前列表界面上显示的客商是否为查询得到的数据。
    private boolean isQueryCustOnList = false;
    
	int State = 0;
	
	private int billOperate = IBillOperate.OP_INIT; 

	// 鼠标监听器
	class BMouseAdpater extends MouseAdapter {
		// 双击鼠标
		public void mouseClicked(MouseEvent e) {
			onMouseClicked(e);
		}
	}
	
	private BDAssociateFileUtil custFileUtil;

	//自定义 引用 中 统计型
	BillItem[] defComb = null;

	BillItem[] b_defComb = null;

	private String m_loginPkCorp = null;//记录当前登陆公司

	private String m_loginUserID = null;//记录当前登陆用户

	TreeDetail upgradeDetail;//用于升级的TreeDetail

	private UITreeToTree upgradeDlg = null;//提供数据升级到集团

	//标识是否通过其它节点打开该节点直接增加客商，若是则点击保存按钮后直接退出客商管理界面。
	private boolean isDirectlyAdd = false;
   //是否自动编码
	private UFBoolean isAutoCode = null;
	//是否断号补号
	private UFBoolean isCodeFill;
	 //单据号对象VO
	private BillCodeObjValueVO billCodeObjVO;
	
	private Map<String,UFDouble[]> moneyMap=new HashMap<String,UFDouble[]>();
	
	

  
	/**
	 * BillTest 构造子注解。
	 */
	public CustManUI() {
		super();
		init();
	}


	/**
	 * 影射单据模板中没有实现的监听 -- 单据模板完善后，废弃该功能 创建日期：(2003-6-19 16:47:39)
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
	 * 编辑前处理. 创建日期:(2005-05-13 2:02:27)
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
														  * "集团分配来的客商,在公司内不能修改集团设置的银行。"
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
					"10080806", "UPP10080806-000083")/* 公司自己添加的银行数据。 */);

		}
		return true;

	}

	/**
	 * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (State == 0) {
			return;
		}
		try {
			//依靠 Table_code 进行区分
			String table_code = e.getTableCode();
			String key = e.getKey();
			//表头编辑
			if (table_code == null) {

				if (key.equals("custname")) {
					afterEditCustName(e);
				}				
				// 客商类别发生了变化以后
				else if (e.getKey().equals("custflag")) {
					afterEditCustFlag(e);
				}
				//
				else if (e.getKey().equals("custstate")) {
                    //设置业务页签批准日期的可编辑性
					setEditableOfRatifyDate();
				}
				// 客商总公司编码
				else if (e.getKey().equals("pk_cubasdoc1")) {
					afterEditPK_cubasdoc1();
				}
				// 客商类型-- 内部外部
				else if (e.getKey().equals("custprop")) {
					afterEditCustProp(e);
				}
				// 冻结标志
				else if (e.getKey().equals("frozenflag")) {
					afterEditFrozenFlag(e,"frozendate");
				}
				else if (e.getKey().equals("b_frozenflag")) {
					afterEditFrozenFlag(e,"b_frozendate");
				}
				else if(e.getKey().equals("stockpriceratio")){
					afterEditStockPriceRatio();
				}else if(e.getKey().equals("prepaidratio")){
					checkAndCorrectRatio("prepaidratio",NCLangRes.getInstance().getStrByID("common","UC000-0004200")/*预收款比例*/,null);
				}else if(e.getKey().equals("innerctldays")){
					Object innerDay = getCardPanel().getHeadItem("innerctldays").getValueObject();
					if(innerDay != null && Double.parseDouble(innerDay.toString()) > Short.MAX_VALUE)
						getCardPanel().setHeadItem("innerctldays",Short.MAX_VALUE);
				}
			}
			// 表体编辑
			else {
					if (e.getKey().equals("defaddrflag")) {
						afterEditDefAddrFlag(e);
					}
					//发货地址重复检查
					if (e.getKey().equals("addrname")) {
						afterEditAddrName(e);
					}
//				}
			}
		} catch (Exception ex) {
			handleException(ex);
		}

	}
	
	//根据客商总公司编码是否有值来控制”基于客商总公司信用控制“和”基于客商总公司账期控制“的可编辑性。
	private void afterEditPK_cubasdoc1() {
		if(getCardHeadItemValue("custflag")!=null&&!getCardHeadItemValue("custflag").equals("1")){
			//客商总公司
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
	 * 预收款比例值合法性校验。
	 * @param field   字段名
	 * @param fieldName  字段名多语资源
	 * @param defaultValue 默认值
	 */
	private void checkAndCorrectRatio(String field, String fieldName,Integer defaultValue) {
		String value = getCardHeadItemValue(field);
		if(value != null && value.length() > 0 && (Integer.parseInt(value) > 100 || Integer.parseInt(value) < 0)){
			getCardPanel().setHeadItem(field,defaultValue);
			showErrorMessage(
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000533",null,new String[]{"[" + fieldName + "]",0 + ""})/*{0}不能小于{1}*/
					+ "," +
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000540",null,new String[]{" ",100 + ""})/*{0}必须不大于{1}*/
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
																			   * "收发货地址名称"
																			   */}));
			}
		}
	}

	private void afterEditDefAddrFlag(nc.ui.pub.bill.BillEditEvent e) {
		if (((Boolean) e.getValue()).booleanValue()) {
			// 取消所有收发货地址 默认属性
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
			String stockRationName = "[" + NCLangRes.getInstance().getStrByID("10080806","UPP10080806-000100") + "]";/*存货最低售价比例*/
			getCardPanel().setHeadItem("stockpriceratio",new Integer(100));
			showErrorMessage(
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000533",null,new String[]{stockRationName,0 + ""})/*{0}不能小于{1}*/
					+ "," +
					NCLangRes.getInstance().getStrByID("uffactory_hyeaa","UPPuffactory_hyeaa-000540",null,new String[]{" ",100 + ""})/*{0}必须不大于{1}*/
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
			System.out.println("选定的数据为：" + e.getValue().toString()
					+ "类型" + e.getValue().getClass());

//			int rowCount = getCardPanel().getBillModel("BANK")
//					.getRowCount();
//			int[] row = new int[rowCount];
//			for (int i = 0; i < rowCount; i++) {
//				row[i] = i;
//			}
//			getCardPanel().getBillModel("BANK").delLine(row);
			if (e.getValue().equals(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"10080806", "UPP10080806-000024")/*"外部单位" */)) {

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
										"UC000-0001589")/* @res "客户" */)) {
			index = 0;
		} else if (e.getValue().equals(
				nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10080806", "UC000-0000275")/*
													 * @res "供应商"
													 */)) {
			index = 1;
		}
		setCustVisaul(index);
	}


	/**
	 * 清空界面
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
				"UPP10080806-000026")/* @res "公司自建客商" */);

	}

	/**
	 * -------------------------------------------------- 功能： 判断公司是否可以增加
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-8 14:31:23)
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
	 * 此处插入方法说明。 创建日期：(2003-6-4 14:33:34)
	 * 
	 * @return nc.ui.bd.util.XTreeModel
	 */
	public XTreeModel getAreaModel() {
		return areaModel;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-6-3 11:34:57)
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
	 * 获取缓冲数据模型 创建日期：(2003-6-5 10:39:55)
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
	 * 此处插入方法说明。 创建日期：(2003-6-3 11:34:57)
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
	 * 取得本本位币
	 */
	private String getCurrency() {
		if (m_currency == null) {
			try {
				//本位币
				m_currency = nc.ui.pub.para.SysInitBO_Client.getPkValue(
						getCorpPrimaryKey(), "BD301");
				if (m_currency == null)
					//集团本位币
					m_currency = nc.ui.pub.para.SysInitBO_Client.getPkValue(
							getCorpPrimaryKey(), "BD211");
			} catch (Exception e) {
				Logger.error(e.getMessage(),e);
			}
		}
		return m_currency;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-6-3 11:34:57)
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
	 * 此处插入方法说明。 创建日期：(2003-6-3 11:34:57)
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
	 * 获取当前公司代码 创建日期：(2003-6-19 16:47:39)
	 */
	public String getpk_corp() {
		return m_loginPkCorp;
	}
	public void setPk_corp(String newPk_corp){
		this.m_loginPkCorp = newPk_corp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-6-3 11:34:57)
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
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
				"UPP10080806-000027")/* @res "客商管理档案" */;
	}
	
	private BDAssociateFileUtil getCustFileUtil() {
		if(custFileUtil==null) {
			custFileUtil=new BDAssociateFileUtil(IBDFileManageConst.CUST_FILEMANAGE_PATH);
		}
		return custFileUtil;
	}
	
	

	/**
	 * -------------------------------------------------- 功能： 设置树的细节
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-10-10 19:07:12)
	 * --------------------------------------------------
	 */
	public TreeDetail getTreeDetail() {
		if (detail == null) {
			// 指定树的详细配置
			detail = new TreeDetail();
			{

				// 公司数据类型的指定
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
					mg.setHowDisplay(new boolean[] { false, true, true }); // 显示排序码和名称
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
					cbmg.setHowDisplay(new boolean[] { false, true, true }); // 显示排序码和名称
					cbmg.setAimClass(nc.vo.bd.b09.CustManVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				// 必要
				detail.setMg(new MethodGroup[] { mg, cbmg });

				detail.setRootname(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10080806", "UC000-0001235")/* @res "地区分类" */);
			}
		}

		return detail;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(Throwable exception) {
         Logger.error(exception.getMessage(),exception);
         showHintMessage(exception.getMessage());
	}

	/**
	 * 初始化系统
	 * 
	 * 创建日期：(2003-6-3 11:30:02)
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

		//单据状态
		billState = 0;

		// 设置按钮
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
			//读取“供应商管理”模块是否启用
			isFrozenEditable = !SFServiceFacility.getCreateCorpQueryService().isEnabled(m_loginPkCorp, "4002");
			// 读取地区分类
			areaVos = CubasdocBO_Client.getCustArea(m_loginPkCorp,ClientEnvironment.getInstance().getUser().getPrimaryKey());
			getAreaModel().createTree(areaVos);
			getCacheModel().createTree(areaVos);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * -------------------------------------------------- 功能： 初始化客商档案的自定义项
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-8 14:31:23)
	 * --------------------------------------------------
	 * @param voUserDef 
	 */
	public void initBaseDef(BillData bd, DefVO[] voUserDef) {

		/*
		 * 如果页签中所有的项目都不显示，则该页签不显示
		 */
		if (voUserDef == null || voUserDef.length == 0) {
			System.out.println("客商档案自定义为空");
		} else {
			// 首先将可用的自定义的信息装载 -- 可以显示的自定义信息
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

					// 统计
					if (voUserDef[i].getType().equals("统计")) {
						itemDef.setDataType(BillItem.USERDEF);
						itemDef.setRefType(voUserDef[i].getDefdef()
								.getPk_bdinfo());
						itemDef.reCreateComponent();
                        //保存主键
						itemDef.setDataType(BillItem.UFREF);
						itemDef.setIsDef(true);
					}
					// 日期
					else if (voUserDef[i].getType().equals("日期")) {
						itemDef.setDataType(BillItem.DATE);
						itemDef.reCreateComponent();
					}
					// 备注
					else if (voUserDef[i].getType().equals("备注")) {

						itemDef.setDataType(BillItem.STRING);
						if (voUserDef[i].getLengthnum() != null) {
							itemDef.setLength(voUserDef[i].getLengthnum()
									.intValue());
						}
						itemDef.reCreateComponent();
					}
					// 数字
					else if (voUserDef[i].getType().equals("数字")) {
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
	 * 初始化模板 创建日期：(2003-6-19 16:47:39)
	 */
	public void initBill() {

		//String templetID = "0001AA1000000000KOHF";

		nc.vo.pub.bill.BillTempletVO cardTvo = getCardPanel().getTempletData(
				MODULE_CODE, null, m_loginUserID, m_loginPkCorp);

		// 加载模板
		BillData billData = new BillData(cardTvo);

		// 初始化 自定义信息项目
		initAllDef(billData);

		BillListData bld = new BillListData(cardTvo);

		// 初始话列表
		hideListDef(bld);
		//初始列表
		bld.getHeadItem("pk_pricegroup").setShow(false);
		bld.getHeadItem("pk_pricegroupcorp").setShow(false);

		getCardPanel().setBillData(billData);
		getListPanel().setListData(bld);

		getCardPanel().setBodyMenuShow(false);

		//生成结算单位，允许列表表头多选
		getListPanel().getHeadTable().getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// 加装 监听
		getListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getListPanel().getHeadTable().addMouseListener(new BMouseAdpater());
		getCardPanel().addBillEditListenerHeadTail(this);
		getCardPanel().addEditListener("ADDR", this);
		
		//自动执行编辑公式
		getCardPanel().setAutoExecHeadEditFormula(true);
		//添加图标
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
		String[] defObjs=new String[]{"客商管理档案","客商档案"};
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
	 * 初始化自定义项
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 * @param voUserDef 
	 */
	public void initDef(BillData bd, DefVO[] voUserDef) {

		if (voUserDef == null || voUserDef.length == 0) {
			System.out.println("客商档案自定义为空");
		} else {
			// 首先将可用的自定义的信息装载 -- 可以显示的自定义信息
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

					// 根据类型 设置不同展示逻辑
					if (voUserDef[i].getType().equals("统计")) {
						itemDef.setDataType(BillItem.USERDEF);
						itemDef.setRefType(voUserDef[i].getDefdef()
								.getPk_bdinfo());
						itemDef.reCreateComponent();
                        //保存主键
						itemDef.setDataType(BillItem.UFREF);
						itemDef.setIsDef(true);
					} else if (voUserDef[i].getType().equals("日期")) {
						itemDef.setDataType(BillItem.DATE);
						itemDef.reCreateComponent();
					} else if (voUserDef[i].getType().equals("备注")) {
						itemDef.setDataType(BillItem.STRING);
						if (voUserDef[i].getLengthnum() != null) {
							itemDef.setLength(voUserDef[i].getLengthnum()
									.intValue());
						}
						itemDef.reCreateComponent();
					} else if (voUserDef[i].getType().equals("数字")) {
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
	 * -------------------------------------------------- 功能： 外部调用
	 * 
	 * 输入： command : 命令代码 param：和该命令对应的参数
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-3 14:24:27)
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
			// 展现一个客商信息
			showCustInfo((CustManVO) param);
			break;
		default:
			System.out.print("nc.ui.bd.b09.CustManUI.invoke 请用了错误的命令代码");
		}
	}

	/**
	 * 按条件查询 condition == null 对该单位下所有的档案进行查询，] condition != null 有条件的进行查询
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void loadQueryData(String condition) {
		try {
			CustManVO[] vos = CumandocBO_Client.QueryCust(condition,getpk_corp(),ClientEnvironment.getInstance().getUser().getPrimaryKey());
			int len = vos == null ? 0 : vos.length;
			Vector v = new Vector();
			for (int i = 0; i < len; i++) {
				// 处理缓冲-- 一定程度上是用来维护同一个存取方式
				if (!getCacheModel().containsKey(vos[i].getKey())) {
					getCacheModel().addNode(vos[i]);
				}
				// 处理全部查询的情况
				if (condition == null) {
					getCacheModel().getNodeParent(vos[i].getKey()).setFlag(1);
				}
				//显示
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
	 * -------------------------------------------------- 功能：
	 * 
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-8 14:31:23)
	 * --------------------------------------------------
	 */
	public void newMethod() {
	}

	/**
	 * 新增一个客商 -- 清空界面、设置一些预置的数据 创建日期：(2003-6-19 16:47:39)
	 */
	public void onAdd() {
		if (corpCanAdd() == false) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000044")/* @res "集团控制，公司不能添加客商" */);
			return;
		}
		// 清空界面
		clearCardPanel();
		getCardPanel().addNew();
		onChange();
		setButtons(card_edit_Add);
		//setTitleText("新增客商");
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();
		
		billOperate = IBillOperate.OP_ADD;

		//预置的数据
		setCardDefaultData();

		getCardPanel().transferFocusTo(0);

		

	}
	
	/**
	 * 更新表体按钮可用性
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
		//	  预置的数据
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
		//设置业务页签批准日期的可编辑性
        setEditableOfRatifyDate();
		// 设置默认值
		String custflag = getCardHeadItemValue("custflag");
		if (custflag != null && custflag.equals("0")) {
			BillItem[] items = getCardPanel().getHeadShowItems("BUY");
			int len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
				items[i].setValue(null);
			}

		} else if (custflag != null && custflag.equals("1")) {
			//		    获取所有销售的录入项
			BillItem[] items = getCardPanel().getHeadShowItems("SALE");
			int len = items == null ? 0 : items.length;
			for (int i = 0; i < len; i++) {
				items[i].setEnabled(false);
				items[i].setValue(null);
			}
		}
		afterEditPK_cubasdoc1();
//		3.自动编码
		if(!isImport) {
			setAutoCustCode();
		}
		//执行表头公式
		getCardPanel().execHeadTailEditFormulas();
	}

	private String getCardHeadItemValue(String field) {
		return getCardPanel().getHeadItem(field).getValueObject() == null ? null:getCardPanel().getHeadItem(field).getValueObject().toString() ;
	}

	private BillCodeObjValueVO getBillCodeObjValueVO() {
		if(billCodeObjVO==null) {
			billCodeObjVO = new BillCodeObjValueVO();
		}
		// 地区分类
		XTreeNode node = getAreaTree().getSelectedNode();
		if (node != null)
		{   
			billCodeObjVO.setAttributeValue("地区分类", node.getPrimaryKey());
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
		//自动编号并且断号补号，则不能编辑
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
     * 设置业务信息页签批准日期的可编辑性
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
	 * 标题增行 创建日期：(2003-6-19 16:47:39)
	 */
	public void onAddRow() {
		getCardPanel().addLine();
		
	}

	/**
	 * 地区分类变换的时候，相应的更新该地区下客商信息
	 * 
	 * 尽可能的保持刷新前后，表的选中情况一致
	 * 
	 * 分为读库刷新，和缓冲刷新
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
				/* 缓冲设计 */
				XTreeNode cacheNode = getCacheModel().getNode(key);
				// 如果节点数据空，或者对该地区分类，没有进行过查询
				if (ref || cacheNode.getFlag() == 0) {

					listVO = CumandocBO_Client.getCust(key, getpk_corp());

					if (cacheNode.getChildCount() > 0) {
						for (int i = cacheNode.getChildCount() - 1; i >= 0; i--)
							// 删除客商在缓存 中的映射
							if (((XTreeNode) cacheNode.getChildAt(i))
									.getValue() instanceof CustManVO) {
								getCacheModel().deleteNode(
										((XTreeNode) cacheNode.getChildAt(i))
												.getPrimaryKey());
							}
					}

					//
					if (listVO != null && listVO.length > 0) {
						// 加入到缓冲中
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
					 * 重新设置表的选中情况
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
	 * 从卡片返回列表
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onBack() {
		// 更改按钮组
		setButtons(list_btns);
		onChange();
		//		//刷新当前选中的行。
		int row = getListPanel().getHeadTable().getSelectedRow();
		if (row < 0) {
				return;
		}
		hRow = row;
		selectCust();
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {
		/* ========== 列表状态 =========== */
		// 新增
		if (bo == btn_Add) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory", "UPPuifactory-000061")/*
																 * @res
																 * "开始进行增加单据，请等待......"
																 */);
			onAdd();
			return;
		}
		// 编辑
		if (bo == btn_Edit) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
													 * @res "开始进行编辑单据，请等待......"
													 */);
			onEdit();
			return;
		}
		//批改
		if (bo == btn_Batch_update) {
			
			onBatchUpdate();
			return;
		}
	
		// 删除
		if (bo == btn_Del) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
													 * @res "开始进行档案删除，请等待......"
													 */);
			onDelete();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "档案删除完成,耗时(ms):" */
							);
			return;
		}
		// 查询
		if (bo == btn_Query) {
			onQuery();
			return;
		}

		// 刷新
		if (bo == btn_Ref) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
													 * @res "开始进行刷新单据，请等待......"
													 */);
			onRef();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "单据刷新完成,耗时(ms):" */
							);
			return;
		}
		// 详细
		if (bo == btn_Detail) {
			onDetail();
			return;
		}
		//
		if (bo == btn_DocMan) {
			onDocMan();
			return;
		}
		// 打印
		if (bo == btn_Print) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
													 * @res "开始进行打印单据，请等待......"
													 */);
			onPrint();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "单据打印完成,耗时(ms):" */
							);
			return;
		}
		// 升级
		if (bo == btn_Upgrade) {
			onUpgrade();
			return;
		}
		if (bo == btn_Output) {
			on_exportOutSystem();
			return;
		}

		/* =============== 卡片状态 =========== */
		// 卡片新增
		if (bo == btn_Card_Add) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory", "UPPuifactory-000061")/*
																 * @res
																 * "开始进行增加单据，请等待......"
																 */);
			onCardAdd();
			return;
		}
		// 编辑
		if (bo == btn_Card_Edit) {
			State = 1;
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
													 * @res "开始进行编辑单据，请等待......"
													 */);
			onCardEdit();
			return;
		}
		if (bo == btn_Card_Del) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
													 * @res "开始进行档案删除，请等待......"
													 */);
			onCardDelete();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "档案删除完成,耗时(ms):" */
							);
			
			return;
		}
		// 首张
		if (bo == btn_Card_First) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res 开始执行操作, 请等待...
													 */
			);
			scrollRecord(0);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res 执行完毕.耗时(ms)
													 */
							);
			return;
		}
		// 末张
		if (bo == btn_Card_Last) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res 开始执行操作, 请等待...
													 */
			);
			scrollRecord(2);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res 执行完毕.耗时(ms)
													 */
							);
			return;
		}

		// 下一张
		if (bo == btn_Card_Next) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res 开始执行操作, 请等待...
													 */
			);
			scrollRecord(1);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res 执行完毕.耗时(ms)
													 */
							);
			return;
		}

		// 上一张
		if (bo == btn_Card_Pre) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000179")/*
													 * @res 开始执行操作, 请等待...
													 */
			);
			scrollRecord(-1);
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000180")/*
													 * @res 执行完毕.耗时(ms)
													 */
							);
			return;
		}
		// 刷新
		if (bo == btn_Card_Ref) {
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
													 * @res "开始进行刷新单据，请等待......"
													 */);
			onCardRef();
			showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "单据刷新完成,耗时(ms):" */
							);
			
			return;
		}
		// 返回
		if (bo == btn_Card_Back) {
			onBack();
			return;
		}

		/* ============== 卡片编辑状态 ========= */
		// 表体新增一行
		if (bo == btn_Card_AddRow) {
			onAddRow();
			return;
		}
		// 标题删除一行
		if (bo == btn_Card_DelRow) {
			onDeleteRow();
			return;
		}
		// 保存新增
		if (bo == btn_Card_Save) {
			onSaveAdd();
			return;
		}
		// 保存修改
		if (bo == btn_Card_Mod_Save) {
			onSaveMod();
			return;
		}
		// 取消编辑
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
			MessageDialog.showHintDlg(this, "提示", "无渠道客商导出！");
			return ;
		}
		
		String[] headColsCN=new String[]{"客商编码","客商名称","开户银行","银行帐号"};
		String[] mapColsCN=new String[]{"custcode","custname","def1","def2"};
		
//		 用户选择路径
		String path = ExcelUtils.getChooseFilePath(this, "渠道客商信息");
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			// 如果为空，就不往下执行了
			return;

		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}
		
//		 构造一个输出流
		IOUtils util = new IOUtils(path, false, true);
		// 构造excel工具类对象
		ExcelTools excelTools = new ExcelTools();
		// 创建一个sheet
		excelTools.createSheet("渠道客商信息");
		// 判断查询出的数据是否为空（不会走）
		if (CollectionUtils.isEmpty(maplist)) {
			// 创建一行
			excelTools.createRow(0);
			// 创建一个单元格
			short ct = 0;
			excelTools.createCell(ct);
			// 如果为空，就直接向excel文件中写入“无数据！”
			excelTools.setValue("无数据！");

		} else {
			// 创建一行
			excelTools.createRow(0);
			// 判断列头是否为空
			if (null != headColsCN) {

				for (short i = 0; i < headColsCN.length; i++) {
					// 创建一个单元格
					excelTools.createCell(i);
					// 将值写到单元格
					excelTools.setValue(headColsCN[i]);

				}

				for (int i = 0; i < maplist.size(); i++) {
					// 迭代list
					Map<String, Object> map = maplist.get(i);

					// 创建一行
					excelTools.createRow(i + 1);

					for(int j=0;j<mapColsCN.length;j++){
						
						excelTools.createCell((short)j);
						excelTools.setValue(map.get(mapColsCN[j]));
						
					}

				}

			} 

		}
		// 将excel写到磁盘上
		excelTools.writeExcel(util.getOutputStream());
		// 闭关流
		util.closeStream();
		
		MessageDialog.showHintDlg(this, "提示", "导出成功！");
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
		//复用自定义项1为登录公司，主要服务于资金的客商新增。
		basVO.setLoginCorp(m_loginPkCorp);
		dialog.setParam(basVO);
		dialog.showModal();
		//更新界面
		onCardRef();
		getListPanel().getHeadTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		getListPanel().setBodyValueVO("BANK", editVo.getBanks());
		getListPanel().getBodyBillModel("BANK").execLoadFormula();
	}
	
	private Uif2Dialog getCustBankDlg() {
		if (custBankDialog == null) {
			custBankDialog = new Uif2Dialog(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10080806",
							"UPP10080806-000105")/* @res "银行账户" */,
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
					"10080806", "UPP10080806-000095")/* 请先先查询要导出的客商数据! */);
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
	 * 卡片状态下进行增加
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onCardAdd() {
		if (corpCanAdd() == false) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000044")/* @res "集团控制，公司不能添加客商" */);
			return;
		}
		// 编辑界面 清成初始状态
		clearCardPanel();
		getCardPanel().addNew();
		// 变换按钮状态
		setButtons(card_edit_Add);
		// 设置表体
		//setTitleText("新增客商");
		// 可编辑
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();

		//预置的数据
		setCardDefaultData();

		getCardPanel().transferFocusTo(0);
		billOperate = IBillOperate.OP_ADD;

	}

	/**
	 * 从卡片返回列表
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onCardCancel() {
		returnCustCode();
		if (editVo == null) {
			// 从列表状态新增，回到列表状态
			onBack();
		}
		// 返回明细状态
		else {
			// 更改按钮组
			setButtons(card_detail);

			// 数据设置操作 部分数据可能设置不到
			getCardPanel().setBillValueVO(editVo.convert());
			setSaleMny();
			getCardPanel().execTailLoadFormulas();
			getCardPanel().getBillModel("BANK").execLoadFormula();
			getCardPanel().getBillModel("ADDR").execLoadFormula();

			// 可编辑性
			getCardPanel().setEnabled(false);
			State = 0;
		}
		billOperate = IBillOperate.OP_NOTEDIT;

	}

	/**
	 * 新增取消时，退回已经申请的客商编码
	 */
	private void returnCustCode() {
		if(billOperate==IBillOperate.OP_ADD&&isAutoSetCustCode()&&isCodeFill()) {
			String custCode=(String) getCardPanel().getHeadItem("custcode").getValueObject();
			String pk_areacl=(String) getCardPanel().getHeadItem("pk_areacl").getValueObject();
			BillCodeObjValueVO valueVO=new BillCodeObjValueVO();
			valueVO.setAttributeValue("地区分类", pk_areacl);
			try {
				getAutoCodeService().returnBillCodeOnDelete(m_loginPkCorp, "customer", custCode, valueVO);
			} catch (BusinessException e) {
				handleException(e);
			}
		}
	}
	
	
	

	/**
	 * 
	 * 详细状态下，删除指定的客商
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onCardDelete() {
		onDelete();
		onBack();
	}

	/**
	 * 对客商进行编辑
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onCardEdit() {

		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// 可编辑
		getCardPanel().setEnabled(true);
		
		updateBodyButtons();
		// 加工卡片界面
		processCardforEdit();
		billOperate = IBillOperate.OP_EDIT;
		setDataToCard();
		setItemEditable();
		// 更改按钮组
		setButtons(card_edit_Mod);
	}
	/**
	 * 卡片刷新 创建日期：(2003-6-19 16:47:39)
	 */
	public void onCardRef() {
		//
		try {
			CustManVO vo = CumandocBO_Client.getCustData(editVo.getKey(),
					getpk_corp());
			if (vo != null) {
				editVo = vo;
				//进行缓存的更新
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
	 * 显示切换 创建日期：(2003-6-3 14:35:55)
	 */
	public void onChange() {

		removeAll();
		//	详细 到 普通
		if (billState == 1) {
			billState = 0;
			add(getSplitPane1(), "Center");
		}
		//	普通 到 详细
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
	 * 删除选中的客商
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onDelete() {
		// 是否选中客商
		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		
		if (MessageDialog.showOkCancelDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "档案删除" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "是否确认删除该基本档案?" */
				, MessageDialog.ID_CANCEL) == UIDialog.ID_OK) {
			try {
				
			   CustBasVO parentVO = (CustBasVO)editVo.getParentVO();
				
				// 执行删除操作
				// 如果删除的是集团分配下来的客商的管理档案
				if (parentVO.getAttributeValue("pk_corp").equals(
						"0001")) {
					// 删除集团分配下来的客商的管理档案相当于在集团取消分配客商
					ErrLogReturnValue result = CumandocBO_Client.deleteAssignedDoc(getpk_corp(), editVo.getCustBasVO().getPrimaryKey());
					
					processResult(result);
				}
				// 如果是公司自己添加的管理档案
				else {
					CumandocBO_Client.deleteByCubasID(editVo.getKey(),
							getpk_corp(), ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				}
				writeOperateLog(editVo, OperateType.DELETE);
				// 同时对缓存进行同步
				getCacheModel().deleteNode(
						getCacheModel().getNode(editVo.getKey()));
				// 界面同步
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
	 * 返回结果处理
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
	 * 删除表体中选中的行 对于集团分配到公司的客商基本档案其所对应的银行数据不能删除 创建日期：(2003-6-9 14:49:40)
	 */
	public void onDeleteRow() {	
		getCardPanel().delLine();
	}

	/**
	 * 详细信息
	 * 
	 * 创建日期：(2003-6-19 16:47:39)
	 */
	public void onDetail() {
		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// 切换界面
		onChange();

		// 更改按钮组
		setButtons(card_detail);

		// 更改标题
		//setTitleText("客商详细信息");

		// 数据设置操作 部分数据可能设置不到

		setDataToCard();
		// 可编辑性
		getCardPanel().setEnabled(false);

	}

	/**
	 * -------------------------------------------------- 功能： 文档管理
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-12-11 13:58:31)
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
						"UPT10080806-000118");/* @res"基本信息" */
		String baseDirname =  getCustFileUtil().getFileDir(editVo.getParentVO());;


		if (isAssigned) {
			BDDocManageDlg.showFileManageDlg(this, null, new String[] { dirname, baseDirname }, new String[] {
					showname, baseShowname }, dirname);

		} else {
			BDDocManageDlg.showFileManageDlg(this,null,
					new String[] { dirname }, new String[] { showname },dirname);
		}
		
		writeOperateLog(basvo,nc.ui.ml.NCLangRes.getInstance().getStrByID("10080804","UPP10080804-000027")
				/* @res "档案管理" */);

	}
	
	/**
	 * 写操作日志
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
	 * 对选中的客商进行编辑
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
	 */
	public void onEdit() {
		clearCardPanel();

		if (editVo == null) {
			showErrorMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		// 切换界面
		onChange();
		// 可编辑性
		getCardPanel().setEnabled(true);
		
		
		updateBodyButtons();
		// 加工卡片界面
		processCardforEdit();
		// 更改按钮组
		setButtons(card_edit_Mod);
		billOperate = IBillOperate.OP_EDIT;
		setDataToCard();
		
		setItemEditable();
		
	}

	/**
	 * 设置可编辑性
	 */
	private void setItemEditable() {
		setCodeEditable();
		if(isDirectlyAdd) {
			getCardPanel().getHeadItem("custprop").setEnabled(false);
		}
	}


	/**
	 * 双击操作 创建日期：(2003-6-19 16:47:39)
	 */
	public void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			// 执行一次表头选择操作
			int row = getListPanel().getHeadTable().getSelectedRow();
			if (row != hRow) {
				hRow = row;
				selectCust();
			}
			// 执行切换
			if (editVo != null) {
				onDetail();
			}
		}

	}

	/**
	 * -------------------------------------------------- 功能：
	 * 
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-3 14:46:06)
	 * --------------------------------------------------
	 */
	public void onPrint() {

		if (getListPanel().getHeadBillModel().getRowCount() == 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10080806", "UPP10080806-000046")/*
																			    * @res
																			    * "没有可打印的数据"
																			    */);
			return;
		}
		try {
			//加载用户可使用的打印模板
			PrintEntry print = new PrintEntry(this);
			print.setTemplateID(m_loginPkCorp, MODULE_CODE, m_loginUserID, null);

			if (print.selectTemplate() < 0)
				return;

			//准备业务数据VO（ExtendedAggrigatedValueObject）
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

			//从缓存中获取列表数据
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
			printData.addTableVO("body", "body", vos); //为打印设置数据源
			PrintVODataSource dataSource = new PrintVODataSource(printData,
					print, "h_", null);
			print.setDataSource(dataSource); //预览
			print.preview();
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-6-19 16:47:39)
	 */
	public void onRef() {
		moneyMap.clear();

		XTreeNode node = getAreaTree().getSelectedNode();
		//		if(node == null){
		//		      MessageDialog.showHintDlg(this,"提示"，"请现选中地区分类。");
		//	}

		if (node != null) {
			//对于根节点，刷新地区分类树和全部缓冲结构
			if (node.getLevelNumber() == 0) {
				recontructAreaTree();
			}
			if (node.getPrimaryKey() != null) {
				onAreaChange(node.getPrimaryKey(), true);
			}
		}

	}

	private void recontructAreaTree() {
		// 读取地区分类
		try {
			areaVos = CubasdocBO_Client.getCustArea(m_loginPkCorp,ClientEnvironment.getInstance().getUser().getPrimaryKey());
			getAreaModel().createTree(areaVos);
			getCacheModel().createTree(areaVos);
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	/**
	 * 保存从卡片状态下获取当前新增的客商信息 创建日期：(2003-6-19 16:47:39)
	 */
	protected boolean onSaveAdd() {
		// 终止编辑
		getCardPanel().stopEditing();

		CustManVO custVO = null;
		boolean fail = false;
		String[] keys = null;

		int index = 0;
		try {

			custVO = getCustManVO(true);
			
			// 转换成原有系统的数据结构，遵循原来的检查规则
			index = ((UIComboBox) getCardPanel().getHeadItem("custflag")
					.getComponent()).getSelectedIndex();
			dataNotNullValidate(index);
			// 客户
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
			}
			// 供应商
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}
			// 客商
			if (index == 2) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}

			custVO.validate();
			CubasdocVO.validateAllAddrVO((CustAddrVO[]) getCardPanel()
					.getBillModel("ADDR").getBodyValueVOs(CustAddrVO.class.getName()));
			custVO.setStatus(nc.vo.pub.VOStatus.NEW);
			
			//数据入库
			//集团价格分组自动赋值给公司价格分组
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
			//管理档案的主记码自动赋给基本档案
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
					//如果用户选择强制保存
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
					"UPP10080806-000084"));/* "未知错误，保存失败。 */
			return false;
		}
		if (!fail) {
			try {
				billOperate = IBillOperate.OP_NOTEDIT;
//				写入用户上机日志。
				writeOperateLog(custVO,OperateType.SAVE);	
				//如果是数据导入，可以不执行以下逻辑，以提高导入效率
				if(!isImport) {
					//========= 设置用户界面状态、以及缓冲同步 ===========
	                custVO = CumandocBO_Client.getCustData(custVO.getKey(),
	                        getpk_corp());
					getCacheModel().addNode(custVO);

					// 添加到列表界面
	                onAreaChange(custVO.getAssKey(), false);
	                
	               // 更改按钮组
					setButtons(card_detail);
					setListCurrentRow(keys[0]);
					setDataToCard();
				}
				 //可编辑性
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
	                                               /*地区分类数据不同步，请在列表界面选中根节点刷新数据。*/                			
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
	 * 根据客商pk计算设置表头选择的行
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
	 * 保存修改 创建日期：(2003-6-19 16:47:39)
	 */
	protected boolean onSaveMod() {
		// 终止编辑
		getCardPanel().stopEditing();

		CustManVO custVO = null;
		int index = 0;
		boolean fail = false;
		try {
			custVO = getCustManVO(true);
			// 转换成原有系统的数据结构，遵循原来的检查规则
			index = ((UIComboBox) getCardPanel().getHeadItem("custflag")
					.getComponent()).getSelectedIndex();
			dataNotNullValidate(index);
			// 客户
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).validateSale();
			}
			// 供应商
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[1]).validatePur();
			}
			// 客商
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
			//数据入库
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
			int changedSealflag = 0; //-1:由true变为false;0:unchanged;1:由false变为true
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
			// 保存失败
			fail = true;
			String hint = ex.getHint();
			if (hint != null && "1".equals(hint)) {
				if(CustDocUI.handleCheckInfo(this, 1)){
				    //如果用户选择强制保存
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
					"UPP10080806-000084"));/* "未知错误，保存失败。 */
			handleException(ex);
			return false;
		}
		if (!fail) {
			billOperate = IBillOperate.OP_NOTEDIT;
			try { //========= 设置用户界面状态、以及缓冲同步 ===========
				writeOperateLog(custVO,OperateType.SAVE);
                custVO = CumandocBO_Client.getCustData(custVO.getKey(),
                        getpk_corp());
                //进行缓存的更新
                getCacheModel()
                        .deleteNode(custVO.getParentVO().getPrimaryKey());
                getCacheModel().addNode(custVO); 
                //如果列表界面显示的是查询得到的数据，重查数据。
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
                // 更改按钮组
				setButtons(card_detail);
				btn_Card_AddRow.setVisible(true);
				btn_Card_DelRow.setVisible(true);
				
				setListCurrentRow(custVO.getParentVO().getPrimaryKey());
				editVo = custVO;
				setDataToCard();
				// 可编辑性
				getCardPanel().setEnabled(false);
				showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
				State = 0;
				return true;
			}catch(TreeOperationException te) {
				Logger.error(te.getMessage(),te);
                showErrorMessage(NCLangRes.getInstance().getStrByID("10080804","UPP10080804-000067"));
                                                   /*地区分类数据不同步，请在列表界面选中根节点刷新数据。*/                			
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
	 * -------------------------------------------------- 功能： 根据数据调整卡片的显示方式
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-10-13 16:13:37)
	 * --------------------------------------------------
	 */
	public void processCardforEdit() {

		// 如果该客商信息是集团分配的，则部分数据不可以编辑
		if (editVo.getParentVO().getAttributeValue("pk_corp").equals("0001")) {
			// 基本信息不可以编辑
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
					"10080806", "UPP10080806-000026")/* @res "公司自建客商" */);
		}
		//		
		// 根据客商的管理信息，设置采购和销售的可编辑性
		int custflag = ((CbmVO) editVo.convert().getParentVO()).getType();
		if (custflag == CbmVO.KH) {
			setCustVisaul(0);
		} else if (custflag == CbmVO.GYS) {
			setCustVisaul(1);
		} else {
			getCardPanel().setHeadItem("custflag", "2");
			setCustVisaul(2);
		}
		//处理业务信息页签
		processBusiTab();
	}

	private void processBusiTab() {
		//设置业务页签批准日期的可编辑性
		setEditableOfRatifyDate();
		Integer iTemp = ((CustBasVO) editVo.getCustBasVO()).getCustprop();
		int iCustprop = (iTemp == null ? 0 : iTemp.intValue());

		if (iCustprop == 0) //外部单位
		{
			getCardPanel().getHeadItem("pk_corp1").setEnabled(false);
			getCardPanel().getHeadItem("pk_corp1").setValue(null);
		}
	}

	/**
	 * 记录滚动 ： 0 -到第一条 -1 到上一条 1 到下一条 2 到末尾
	 * 
	 * 
	 * 创建日期：(2003-6-24 15:57:14)
	 */
	public void scrollRecord(int style) {
		enableSrollButtons();
		int rowCount = getListPanel().getHeadTable().getRowCount();		
		// hRow 记录当前的选种的行数
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
	 * 表头选择发生了变化以后，维护数据同步 创建日期：(2003-6-24 15:57:14)
	 */
	public String selectCust() {
		String key = getListPanel().getHeadBillModel().getValueAt(hRow,
				"pk_cubasdoc").toString();
		try {
			XTreeNode node = getCacheModel().getNode(key);
			editVo = (CustManVO) node.getValue();

			//设置数据
			getListPanel().getBodyTable().clearSelection();
			getListPanel().setBodyValueVO("BANK", editVo.getBanks());
			getListPanel().setBodyValueVO("ADDR", editVo.getAddrs());
			// 加载公式
			getListPanel().getBodyBillModel("BANK").execLoadFormula();
			getListPanel().getBodyBillModel("ADDR").execLoadFormula();
			updateFilePath();
			
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
		}
		return null;

	}
	
	/**
	 * 更新文档图标处文件列表
	 */
	private void updateFilePath() {
		if(editVo!=null&&billState==1) {
			String dirname = getCustFileUtil().getFileDir(editVo.getParentVO(),m_loginPkCorp);
			getCardPanel().getAccessoriesAction().setFiledir(dirname);
		}
	}

	/**
	 * 根据客商类型- 设置销售、采购界面的可编辑性 创建日期：(2003-6-19 16:47:39)
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

		// 获取所有销售的录入项
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
			//设置值
			getCardPanel().setHeadItem("discountrate", "100");
			getCardPanel().setHeadItem("stockpriceratio", "100");			
			getCardPanel().setHeadItem("pk_currtype1", getCurrency());
			getCardPanel().getHeadItem("developdate").setValue(
					getBusinessDate());
			//是否信用管制
			getCardPanel().setHeadItem("credlimitflag", "1");
//			冻结日期和冻结标志的控制关系
			String frozenFlag = getCardHeadItemValue("frozenflag");
			if (frozenFlag == null || !frozenFlag.equals("true")) {
				getCardPanel().getHeadItem("frozendate").setEnabled(false);
			}
			afterEditPK_cubasdoc1();
		}

		// 获取所有采购的销售项
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

			//冻结日期和冻结标志的控制关系
			String frozenFlag = getCardHeadItemValue("b_frozenflag");
			if (frozenFlag == null || !frozenFlag.equals("true")) {
				getCardPanel().getHeadItem("b_frozendate").setEnabled(false);
			}
		}
	}

	/**
	 * 将数据插入到卡片 创建日期
	 */
	public void setDataToCard() {
		// 重新设置
		setCustDataToCard(editVo);
		// 设置表体数据状态
		setBodyState();
		//获得订单应收、业务应收、信用余额三个字段
		setSaleMny();
	}

	private void setSaleMny() {
		try {
		UFDouble[] mny = getMoney(editVo);
		if(mny != null) {			
			//订单应收应付
			getCardPanel().setHeadItem("ordawmny",mny[0]);
			//业务应收/应付
			getCardPanel().setHeadItem("busawmny",mny[1]);
			//信用额度
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
	 * 调用供应链提供的服务，获取这三个字段的数据
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

		// 数据设置操作 部分数据可能设置不到
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
		
//		// 客商标志

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

		// 客商类型
		Integer iTemp = ((CustBasVO) custvo.getCustBasVO()).getCustprop();
		int iCustprop = (iTemp == null ? 0 : iTemp.intValue());
		UIComboBox custPropCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custprop").getComponent();
		if (custPropCBox != null) {
			custPropCBox.setSelectedIndex(iCustprop);
		}
		if (iCustprop == 0) //外部单位
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
				//是否信用管制
				iTemp = ((CumandocVO[]) custvo.getChildrenVO())[0]
						.getCredlimitflag();
				int iCredlimitflag = (iTemp == null ? 0 : iTemp.intValue());
				UIComboBox credlimitCBox = (UIComboBox) getCardPanel()
						.getHeadItem("credlimitflag").getComponent();
				if (credlimitCBox != null) {
					credlimitCBox.setSelectedIndex(iCredlimitflag);
				}
				//设置销售信息的默认值
				//发展日期
				if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
				if (((CumandocVO[]) custvo.getChildrenVO())[0].getDevelopdate() == null)
					getCardPanel().getHeadItem("developdate").setValue(
							getBusinessDate());
				//默认交易币种
				if (((CumandocVO[]) custvo.getChildrenVO())[0]
						.getPk_currtype1() == null)
					getCardPanel().getHeadItem("pk_currtype1").setValue(
							getCurrency());
				}
				//冻结日期的可编辑性
				getCardPanel().getHeadItem("frozenflag").setEnabled(State==1&&isFrozenEditable);
				nc.vo.pub.lang.UFBoolean frozenFlag = ((CumandocVO[]) custvo
						.getChildrenVO())[0].getFrozenflag();
				if (frozenFlag == null || !frozenFlag.booleanValue())
					getCardPanel().getHeadItem("frozendate").setEnabled(false);
				
			}
			if (sCustFlag2.equals("1") || sCustFlag2.equals("3")) {
				//设置采购信息的默认值
				//发展日期
				if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
				if (((CumandocVO[]) custvo.getChildrenVO())[1].getDevelopdate() == null)
					getCardPanel().getHeadItem("b_developdate").setValue(
							getBusinessDate());
				//默认交易币种
				if (((CumandocVO[]) custvo.getChildrenVO())[1]
						.getPk_currtype1() == null)
					getCardPanel().getHeadItem("b_pk_currtype1").setValue(
							getCurrency());
				}
				//冻结日期的可编辑性
				getCardPanel().getHeadItem("b_frozenflag").setEnabled(State==1&&isFrozenEditable);
				nc.vo.pub.lang.UFBoolean frozenFlag = ((CumandocVO[]) custvo
						.getChildrenVO())[1].getFrozenflag();
				if (frozenFlag == null || !frozenFlag.booleanValue())
					getCardPanel().getHeadItem("b_frozendate")
							.setEnabled(false);
			}
			afterEditPK_cubasdoc1();
		} catch (Exception ex) {
			Logger.error("设置销售/采购的默认值出现问题。");
			Logger.error(ex.getMessage(),ex);
		}
	}

	/**
	 * -------------------------------------------------- 功能： 外部调用
	 * 
	 * 输入： 要展现的客商的信息 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-3 14:26:57)
	 * --------------------------------------------------
	 */
	public void showCustInfo(CustManVO vo) {
		// 查询该客商信息
		editVo = vo;
		// 切换到卡片状态，展现

		removeAll();

		billState = 1;
		add(getCardPanel(), "Center");

		this.validate();
		this.repaint();

		// 数据设置操作 部分数据可能设置不到

		setDataToCard();
		// 可编辑性
		getCardPanel().setEnabled(false);

	}

	/**
	 * 表头选择发生变化、响应的变换表体数据 创建日期：(2003-6-5 9:34:06)
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
		//远程支付节点增加的客商在【远程支付－客商管理档案】和【基本档案－客商管理档案】都不可以进行"升级"、"生成结算单位"操作。
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
	 * ---------------------------------------------- 功能说明： 对用户选择的客商档案进行升级 输入：
	 * String[] units :参与该次分配的单位 Object[] docs :选定进行分配的数据
	 * 
	 * 异常： 可能产生的异常 补充： 如果指定了主要的类型信息，则返回的全是改种类型的信息
	 * 
	 * 创建日期：(2003-8-18 14:06:39) ----------------------------------------------
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
																			    * "目前只支持升级一个客商"
																			    */
					+ ".");
			return null;
		}
		//增加对地区分类是否集团的判断，如果选中的地区是公司级的地区，则不支持升级
		try {
			IAreaClassAndAddressQry areclQueryService = (IAreaClassAndAddressQry) NCLocator.getInstance().lookup(
					IAreaClassAndAddressQry.class.getName());
		    AreaclVO areaclVO = areclQueryService.queryAreaclByPk(((CustBasVO) docs[0]).getPk_areacl());

			if (areaclVO == null || !areaclVO.getPk_corp().equals("0001")) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10080806",
								"UPP10080806-000049")/* @res "请选择集团级的地区分类" */
						+ ".");
				return null;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return null;
		}
		//使用UITreeToTree送进来的units为null，实际升级时，使用当前登陆公司。
		boolean isCanceled = false;
		Boolean isForceCombineCode = null;
		Boolean isForceCombineName = null;

		while (!isCanceled) {
			try {
				CumandocBO_Client.upgrade((CustBasVO) docs[0], m_loginPkCorp,
						isForceCombineCode, isForceCombineName);
				//成功退出
				MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10080806",
								"UPP10080806-000051")/* @res "升级成功" */
						+ ".");
				isCanceled = true;

			} catch (BusinessException ex) {
				Logger.error(ex.getMessage(),ex);
				if("1".equals(ex.getHint())){
					//弹出选择编码重复的处理方式的对话框
					if (MessageDialog.showOkCancelDlg(this, null, ex
							.getMessage()
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080806", "UPP10080806-000101")
									/*是否先将该档案分配到这些公司作为客户，再与编码或名称重复的档案（只有客商属性是客户才能成功）合并？*/
							) == MessageDialog.ID_OK) {
						isForceCombineCode = new Boolean(true);
					} else {
						isCanceled = true;
					}
				} else if ("2".equals(ex.getHint())) {
					//弹出选择名称重复的处理方式的对话框
					int userChoose = MessageDialog.showYesNoCancelDlg(this,
							null, ex.getMessage()
									+ nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("10080806",
													"UPP10080806-000101"),
							/*是否先将该档案分配到这些公司作为客户，再与编码或名称重复的档案（只有客商属性是客户才能成功）合并？*/						
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
	 * ---------------------------------------------- 功能说明： 取消分配 输入： String[]
	 * units :参与该次分配的单位 Object[] docs :选定进行分配的数据
	 * 
	 * 异常： 可能产生的异常
	 * 
	 * 创建日期：(2003-8-18 14:06:39) ----------------------------------------------
	 */
	public java.lang.String cancelAssign(java.lang.String[] units,
			java.lang.Object[] docs) {
		return null;
	}

	/**
	 * -------------------------------------------------- 功能： 请求获取指定单位已经分配的情况
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充： 可以以几次数的方式给出，也可以单独 只给出数据
	 * 
	 * 创建日期：(2003-9-28 15:08:57)
	 * --------------------------------------------------
	 */
	public java.lang.Object[] getAssigned(java.lang.String unitPrimaryKey) {
		return null;
	}


	/**
	 * 从卡片上获取 客商信息 -- 并且进行数据检查 创建日期：(2003-6-19 16:47:39)
	 */
	private CustManVO getCustManVO(boolean isChanged) {
		//取值
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
		
		//客商类型，客商属性
		CustManVO custVO = rs.convert();
		UIComboBox custFlagCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custflag").getComponent();

		if (custFlagCBox != null) {
			int index = custFlagCBox.getSelectedIndex();
			//客户
			if (index == 0) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(0 + "");
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(4 + "");
			}
			//供应商
			if (index == 1) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(" ");
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(1 + "");
			}
			//客商
			if (index == 2) {
				((CumandocVO) custVO.getChildrenVO()[0]).setCustflag(2 + ""); // 客户
				((CumandocVO) custVO.getChildrenVO()[1]).setCustflag(3 + ""); // 供应商
			}
		}
		// 客商属性
		UIComboBox custPropCBox = (UIComboBox) getCardPanel().getHeadItem(
				"custprop").getComponent();
		if (custPropCBox != null) {
			int index = custPropCBox.getSelectedIndex();
			((CustBasVO) custVO.getCustBasVO()).setCustprop(new Integer(index));
		}

		// 是否信用管制
		UIComboBox custCBox = (UIComboBox) getCardPanel().getHeadItem(
				"credlimitflag").getComponent();
		if (custCBox != null) {
			int index = custCBox.getSelectedIndex();
			((CumandocVO) custVO.getChildrenVO()[0])
					.setCredlimitflag(new Integer(index));
			((CumandocVO) custVO.getChildrenVO()[1])
					.setCredlimitflag(new Integer(-1));
		}
		//公司主键
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
	 * 此处插入方法说明。 创建日期：(2003-6-13 8:46:43)
	 * 
	 * @return nc.ui.bd.b08.AssignDlg
	 */
	public UITreeToTree getUpgradeDlg() {
		if (upgradeDlg == null) {
			upgradeDlg = new UITreeToTree(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10080806", "UPP10080806-000053")/*
																			    * @res
																			    * "客商档案升级"
																			    */);
			upgradeDlg.setTreeDetail(getUpgradeTreeDetail());
			
			upgradeDlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000053")/* @res "客商档案升级" */);
			upgradeDlg.setUser(this);

			upgradeDlg.setMarjorClass(CustBasVO.class);

			upgradeDlg.getUITreeToTreePanel1().getUIButton1().setVisible(false);
			//upgradeDlg.getUITreeToTreePanel1().getAssignBtn().setLocation()Visible(false);

		}
		return upgradeDlg;
	}

	/**
	 * -------------------------------------------------- 功能： 获取制定树的详细信息
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-10-10 16:20:55)
	 * --------------------------------------------------
	 */
	public TreeDetail getUpgradeTreeDetail() {
		if (upgradeDetail == null) {
			// 指定树的详细配置
			upgradeDetail = new TreeDetail();
			{

				// 公司数据类型的指定
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
					mg.setHowDisplay(new boolean[] { false, true, true }); // 显示排序码和名称
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
					cbmg.setHowDisplay(new boolean[] { false, true, true }); // 显示排序码和名称
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
					cmg.setHowDisplay(new boolean[] { false, true, true }); // 显示排序码和名称
					cmg.setAimClass(nc.vo.bd.b08.CustBasVO.class);
				} catch (Exception ex) {
					Logger.error(ex.getMessage(),ex);
				}

				// 必要
				upgradeDetail.setMg(new MethodGroup[] { mg, cbmg, cmg });

				//
				upgradeDetail.setRootname(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10080806", "UPP10080806-000054")/*
																	  * @res
																	  * "地区分类 "
																	  */);

				//
				upgradeDetail.setLeafClass(nc.vo.bd.b08.CustBasVO.class);
			}
		}
		return upgradeDetail;
	}

	/**
	 * -------------------------------------------------- 功能：
	 * 使用了惰性获取数据的方式以后，根据param索取相关的数值
	 * 
	 * 输入： 节点负载的数据 输出： 该节点下级节点数据 异常：
	 * 
	 * 补充： 使用者应该采取措施，降低到后台库里存取数据的次数， 同时使用者应该检测传入的类型
	 * 
	 * 创建日期：(2003-9-28 15:08:57)
	 * --------------------------------------------------
	 */
	public java.lang.Object[] getValue(java.lang.Object[] param) {
		// 这个地方 param 为地区分类
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
	 * -------------------------------------------------- 功能：
	 * 
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-3 14:46:06)
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
									"10080806", "UPP10080806-000055")/* @res "√" */;
					}
					data[i][j] = tmp;
				}
			}
			String title = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UPP10080806-000056")/* @res "客商管理档案列表" */;
			Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);
			Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);
			String topstr = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UC000-0000404")/* @res "公司" */
					+ ":"
					+ ClientEnvironment.getInstance().getCorporation()
							.getUnitname();
			String botstr = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080806", "UC000-0000677")/* @res "制表日期" */
					+ ":" + ClientEnvironment.getInstance().getDate();
			PrintDirectEntry print = new PrintDirectEntry();
			print.setTitle(title); //标题 可选
			print.setTitleFont(font); //标题字体 可选
			print.setContentFont(font1); //内容字体（表头、表格、表尾） 可选
			print.setTopStr(topstr); //表头信息 可选
			print.setBottomStr(botstr); //表尾信息 可选
			print.setColNames(colname); //表格列名（二维数组形式）
			print.setData(data); //表格数据
			print.setColWidth(width); //表格列宽 可选
			print.setAlignFlag(align); //表格每列的对齐方式（0-左, 1-中, 2-右）可选
			print.setCombinCellRange(null); //多表头部分――合并单元格范围
			print.preview(); //预览
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	/**
	 * -------------------------------------------------- 功能：
	 * 
	 * 
	 * 输入：
	 * 
	 * 输出：
	 * 
	 * 异常：
	 * 
	 * 补充：
	 * 
	 * 
	 * 创建日期：(2003-11-3 14:46:06)
	 * --------------------------------------------------
	 */
	public void onUpgrade() {
		getUpgradeDlg().setTotalData(areaVos);

		getUpgradeDlg().showModal();

	}

	private boolean isFrozenEditable = false;//冻结标志能否编辑

	//增加了模糊查询
	/*
	 * int QICount = 5; String QIcode[] = { "bd_cubasdoc.custcode",
	 * "bd_cubasdoc.custname", "bd_cubasdoc.custshortname",
	 * "bd_cubasdoc.engname", "bd_cubasdoc.mnecode" }; String QIname[] = {
	 * "客商编号", "客商名称", "客商简称", "外文名称", "助记码" }; int QIpro[] = {
	 * QueryItem.Style_String, QueryItem.Style_String, QueryItem.Style_String,
	 * QueryItem.Style_String, QueryItem.Style_String };
	 */
	//查询对话框
	private IRefQueryDlg m_queryDlg = null;

	private String m_sealflag = null;//记录修改数据的封存标志

	private IBillcodeRuleService autoCodeService;

	private IBillcodeRuleQryService autoCodeQry;

	/**
	 * 此处插入方法说明。 创建日期：(2004-7-17 22:24:38)
	 * 
	 * @return nc.ui.bd.b09.CustQueryDlg
	 */
	private IRefQueryDlg getQueryDlg() {
		if (m_queryDlg == null) {
				TemplateInfo tempinfo = new TemplateInfo();
				tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
//				考虑到远程结算节点，这里不能使用getModuleCode();
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
	 * 查询
	 * 
	 * 创建日期：(2003-6-9 14:49:40)
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
		// 客户
		if (custflag == 0) {
			notCheckTableCode = table_code_buy;
		}
		// 供应商
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

		//增加多子表的循环
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
																			   * "行"
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
																			   * "页签"
																			   */
											+ ") "
											+ (i + 1)
											+ "("
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("_Bill",
															"UPP_Bill-000002")/*
																			   * @res
																			   * "行"
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
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
	 * 
	 * 创建日期：(2001-8-8 13:52:37)
	 */
	
	public boolean onClosing() {
		FinishEditEvent event = null;
    	if(billOperate == IBillOperate.OP_ADD || billOperate == IBillOperate.OP_EDIT){
    			int i = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UCH001"/*是否保存已修改的数据？*/),UIDialog.ID_CANCEL);
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
	 * 增加新增客商时初始化界面
	 */
	public void initForAdding() {
		System.out.println("进入initForAdding.");
		isDirectlyAdd = true;
		resetButtons();
		State = 1;
		onAdd();
		updateButtons();
	}


	/**
	 * 重新设置按钮组
	 */
	private void resetButtons() {
		card_edit_Add = new ButtonObject[] { btn_Card_AddRow, btn_Card_DelRow,btn_Card_Save};
		list_btns = new ButtonObject[] { btn_Edit, btn_BankAccount,btn_Detail};
		card_detail = new ButtonObject[] {btn_Card_Edit, btn_Card_BankAccount , btn_Card_Back };
	}


	/**
	 * 设置界面的初始值
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
		
		//		 设置表体数据状态
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
	 * 批改操作
	 */
	private void onBatchUpdate() {
		BatchUpdateDlg dlg = new BatchUpdateDlg(this, "custdoc", false,
				new CustBUItemFactory());
		dlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081206",
				"UPP10081206-000012")/* @res "批改" */);
		dlg.setDefaultRefOrg(m_loginPkCorp);
		dlg.setSelectedPks(getSelectedPKs());
		int updateResult = dlg.showModal();
		// 刷新
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
					"10081206", "UPP10081206-000122")/* @res "批改完毕" */);
		case BatchUpdateDlg.CANCELUPDATE:
			//没有执行批改，什么也不做
		}
	}
	
	/**
	 * 返回界面中选中的客商主键
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
	 * 下面的方法是为了实现数据导入功能
	 *************************************************************************************/
	
	/** 
     * 返回档案是否可导入信息
     *
     * @see nc.itf.trade.excelimport.IImportableEditor#getImportableInfo()
     */
    public ImportableInfo getImportableInfo() {
		UFBoolean canImport = UFBoolean.FALSE; // 可导入
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
	 * 保存操作
	 *
	 * @see nc.itf.trade.excelimport.IImportableEditor#save()
	 */
	public void save() throws Exception {
		checkVO(getCustManVO(false));
		if (!onSaveAdd())
			throw new RuntimeException("保存失败");
	}

	/**
	 * 对当前界面的VO数据进行校验
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
	 * 设置默认数据
	 */
	private void setDefaultDataForImport(){
		setCardDefaultData();
		// 新增的时候 业务信息的 客商属性 默认为 "客商"
		// 这样以便设 采购信息 和 业务信息 的默认值
		setCustVisaul(2);
	}
	
	/**
	 * 根据业务规则处理参数vo
	 */
	private void process(ExtendedAggregatedValueObject aggvo) {
		if (aggvo != null) {
			// 处理主表"客商管理档案"VO
			CustManVOProcessor.processVO(aggvo.getParentVO());
			// 处理子表"客商收发货地址"VO数组
			CustAddrVOProcessor.processVOs(aggvo.getTableVO("ADDR"));
		}
	}
	
	/**
	 * 根据业务逻辑调整单据相关控件
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
	 * 处理列表中的特殊属性：
	 * 1，将特殊属性"defaddrflag"(子表 客商收发货地址 "是否默认地址")都设为必输项
	 * 2，将属性"sealflag_b"(页签 业务信息 "是否封存")设为不可编辑
	 * 3，将"采购信息"、"销售信息"页签的所有属性添加后缀以进行区分
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
		

		// 依据待定位数据的相关信息，在UI界面上去定位
		if (StringUtils.isNotBlank(pk_areacl)) {
			XTreeNode locationNode = null;
			// 根据待定位的客商档案的地区分类主键，加载所有数据（包括左树、右侧列表/卡片上的VO）
			onAreaChange(pk_areacl, false);
			try {
				locationNode = getAreaModel().getNode(pk_areacl);	
			} catch (TreeOperationException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showWarningDlg(this,
						NCLangRes.getInstance().getStrByID("_bill",
								"UPP_Bill-000034")/* @res "提示" */, NCLangRes
								.getInstance().getStrByID("_beans",
										"UPP_Beans-000079")/* @res "定位" */
								+ NCLangRes.getInstance().getStrByID("common",
										"UC001-0000053")/* @res "数据" */
								+ NCLangRes.getInstance().getStrByID(
										"template", "UPP_Template-000065")/*
																			 * @res
																			 * "错误"
																			 */
				)/* @res "定位数据错误" */;
			}
			TreePath treePath = null;
			// 定位左树上的地区分类（对应当前客商所属的地区分类）
			if (locationNode != null) {
				treePath = new TreePath(locationNode.getPath());
				getAreaTree().gettree().expandPath(treePath.getParentPath());
				getAreaTree().gettree().setSelectionPath(treePath);
			}
			
			// 定位右侧列表上的待定数据
			try {
				locationNode = getCacheModel().getNode(pk_areacl);
			} catch (TreeOperationException e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showWarningDlg(this,
						NCLangRes.getInstance().getStrByID("_bill",
								"UPP_Bill-000034")/* @res "提示" */, NCLangRes
								.getInstance().getStrByID("_beans",
										"UPP_Beans-000079")/* @res "定位" */
								+ NCLangRes.getInstance().getStrByID("common",
										"UC001-0000053")/* @res "数据" */
								+ NCLangRes.getInstance().getStrByID(
										"template", "UPP_Template-000065")/*
																			 * @res
																			 * "错误"
																			 */
				)/* @res "定位数据错误" */;
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
			// 根据当前行定位列表界面上的客商记录
			selectCust();
			// 切换到卡片界面
			onDetail();		
	}
	}
}