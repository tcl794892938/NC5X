package nc.ui.trade.card;

import java.util.Observer;

import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.BillStatus;

/**
 * 单据卡片的类型基类
 * 创建日期：(2002-12-20 15:07:36)
 * @author：樊冠军
 */
public abstract class BillCardUI
	extends AbstractBillUI
	implements ICheckRetVO, Observer, BillEditListener, BillEditListener2 ,ILinkApprove,ILinkMaintain{


	//3界面控制类
	private ICardController m_uiCtl = null;


/**
 * TestBill 构造子注解。
 */
public BillCardUI() {
	super();
	initialize();
}
/**
* 返回卡片模版。
* 创建日期：(2002-12-23 9:44:25)
*/
public final  BillCardPanel getBillCardPanel() {
	return m_BillCardPanelWrapper.getBillCardPanel();
}

/**
 * 子类实现该方法，返回业务界面的标题。
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return m_BillCardPanelWrapper.getBillCardPanel().getBillData().getTitle();
}

//按钮处理事件,进行事件转发
public final void onButtonClicked(ButtonObject bo) {
	getCardEventHandler().onButton(bo);
}

	//2单据模板卡片基类的包装类  XXX lj++   at 2008-03-30   private->protected
	protected BillCardPanelWrapper m_BillCardPanelWrapper;
	//当前单据的操作状态
	private int m_billOperate = IBillOperate.OP_NOTEDIT;
	private java.awt.BorderLayout m_borderLayOut = null;
	//1界面对应的Action处理事件类
	private CardEventHandler m_btnAction = null;
	//4单据UI对应的数据模型类
	private BillUIBuffer m_modelData = null;

/**
 * TestBill 构造子注解。
 * 用于单据联查和审批流使用
 */
public BillCardUI(
	String pk_corp,
	String pk_billType,
	String pk_busitype,
	String operater,
	String billId) {
	super();
	initialize();
	setBusinessType(pk_busitype);
	//加载数据
	try {
		getBufferData().addVOToBuffer(loadHeadData(billId));
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	} catch (Exception ex) {
		ex.printStackTrace();
	}
}

/**
 * 编辑后事件。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public  void afterEdit(nc.ui.pub.bill.BillEditEvent e){}

/**
 * This method is called whenever the observed object is changed. An
 * application calls an <tt>Observable</tt> object's
 * <code>notifyObservers</code> method to have all the object's
 * observers notified of the change.
 *
 * @param   o     the observable object.
 * @param   arg   an argument passed to the <code>notifyObservers</code>
 *                 method.
 */
public void afterUpdate(){}

/**
 * 编辑前处理。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
	return true;
}

/**
 * This method is called whenever the observed object is changed. An
 * application calls an <tt>Observable</tt> object's
 * <code>notifyObservers</code> method to have all the object's
 * observers notified of the change.
 *
 * @param   o     the observable object.
 * @param   arg   an argument passed to the <code>notifyObservers</code>
 *                 method.
 */
public boolean beforeUpdate(){
	return true;
}

/**
 * 行改变事件。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {}

/**
 * 创建Wrapper的方法
 * 创建日期：(2004-2-3 14:08:28)
 * @return nc.ui.trade.bill.BillCardPanelWrapper
 */
protected BillCardPanelWrapper createBillCardPanelWrapper() throws Exception {
	return new BillCardPanelWrapper(
		getClientEnvironment(),
		getUIControl(),
		getBusinessType(),
		getNodeKey(),
		getUserBillData(),
		getBillDef(getUIControl().getHeadZYXKey(), getUIControl().getBodyZYXKey()));
}

/**
 * 实例化前台界面的业务委托类
 * 如果进行事件处理需要重载该方法
 * 创建日期：(2004-1-3 18:13:36)
 */
protected BusinessDelegator createBusinessDelegator() {
	if(getUIControl().getBusinessActionType() == nc.ui.trade.businessaction.IBusinessActionType.BD)
		return new BDBusinessDelegator();
	else
		return new BusinessDelegator();
}

/**
 * 实例化界面初始控制器
 * 创建日期：(2004-1-3 18:13:36)
 */
protected abstract ICardController createController();

/**
 * 实例化界面编辑前后事件处理,
 * 如果进行事件处理需要重载该方法
 * 创建日期：(2004-1-3 18:13:36)
 */
protected CardEventHandler createEventHandler() {
	return new CardEventHandler(this, getUIControl());
}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-1-8 16:19:19)
 * @return nc.ui.trade.pub.BillCardPanelWrapper
 */
public final BillCardPanelWrapper getBillCardWrapper() {
	return this.m_BillCardPanelWrapper;
}

/**
 * 获得单据编号，对于需要获得单据编号的单据必须重载此方法。
 * 创建日期：(2003-6-28 10:03:55)
 * @exception java.lang.Exception 异常说明。
 */
protected String getBillNo() throws java.lang.Exception {
	return null;
}

/**
 * 获得单据的操作状态。
 * 创建日期：(2003-7-21 14:47:32)
 * @return int
 */
public final int getBillOperate() {
	return m_billOperate;
}

/**
 * 获取前台单据缓存的数据模型
 * 创建日期：(2003-6-2 10:48:39)
 * @return nc.vo.pub.SuperVO[]
 */
public final BillUIBuffer getBufferData() {
	if(m_modelData==null)
		m_modelData = createBillUIBuffer();
	return m_modelData;
}
protected BillUIBuffer createBillUIBuffer() {
	return new BillUIBuffer(getUIControl(),getBusiDelegator());
}

/**
 * 返回当前UI对应的控制类实例。
 * 创建日期：(2004-01-06 15:46:35)
 * @return nc.ui.tm.pub.ControlBase
 */
protected final CardEventHandler getCardEventHandler() {
	if (m_btnAction == null)
		m_btnAction = createEventHandler();
	return m_btnAction;
}

/**
 * 返回卡片主表二维数组，第一维为必须为2。
 * 第二维不限，
 * 第一行为字段属性，第二行为显示的位数。
 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
 * 注意:
 * 1.必须保证二行的长度相等。否则系统按默认值2取。
 * 2.必须在该方法内进行实例化,该方法在构造中调用，
 * 本类未实例化
 * 创建日期：(2001-12-25 10:19:03)
 * @return java.lang.String[][]
 */
public String[][] getCardHeadShowNum(){
	return null;
}

/**
 * 返回卡片子表二维数组，第一维为必须为2。
 * 第二维不限，
 * 第一行为字段属性，第二行为显示的位数。
 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
 * 注意:
 * 1.必须保证二行的长度相等。否则系统按默认值2取。
 * 2.必须在该方法内进行实例化,该方法在构造中调用，
 * 本类未实例化
 * 创建日期：(2001-12-25 10:19:03)
 * @return java.lang.String[][]
 */
public String[][] getCardItemShowNum() {
	return null;
}

/**
 * 获得界面变化数据VO。
 * 创建日期：(2004-1-7 10:01:01)
 * @return nc.vo.pub.AggregatedValueObject
 * @exception java.lang.Exception 异常说明。
 */
public AggregatedValueObject getChangedVOFromUI()
	throws java.lang.Exception {
	return this.m_BillCardPanelWrapper.getChangedVOFromUI();

}

/**
 * 获得当前UI对应的布局管理器。
 * 各具体的UI可根据情况进行方法重载,
 * 默认实现为BorderLayout
 * 创建日期：(2004-1-3 21:57:38)
 * @return java.awt.LayoutManager
 */
protected java.awt.BorderLayout getLayOutManager() {
	if (m_borderLayOut==null)
		m_borderLayOut= new java.awt.BorderLayout();
	return m_borderLayOut;
}

/**
 * 返回当前UI对应的控制类实例。
 * 创建日期：(2003-5-27 15:46:35)
 * @return nc.ui.tm.pub.ControlBase
 */
public final ICardController getUIControl() {
	if (m_uiCtl == null)
		m_uiCtl = createController();
	return m_uiCtl;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-02-03 9:11:45)
 * @return nc.ui.trade.bill.IBillData
 */
public BillData getUserBillData() {
	return null;
}

/**
* 返回数据VO。
* 创建日期：(2001-12-18 16:59:11)
* @return nc.vo.pub.AggregatedValueObject
*/
public final AggregatedValueObject getVo() throws Exception {
	return getBufferData().getCurrentVO();
}

/**
 * 获得界面全部的数据VO。
 * 创建日期：(2004-1-7 10:01:01)
 * @return nc.vo.pub.AggregatedValueObject
 * @exception java.lang.Exception 异常说明。
 */
public AggregatedValueObject getVOFromUI() throws java.lang.Exception {
	return  this.m_BillCardPanelWrapper.getBillVOFromUI();
}

/**
* 此处插入方法说明。
* 创建日期：(2003-9-15 11:03:30)
*/
protected void initEventListener()
{
	m_BillCardPanelWrapper.getBillCardPanel().addEditListener(this);
	m_BillCardPanelWrapper.getBillCardPanel().addBodyEditListener2(this);
}

private void initialize()
{

	try
	{
		this.setLayout(getLayOutManager());

		//设置按钮
		if (getUIControl()==null)
		    throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000108")/*@res "没有设置当前UI的界面控制器"*/);
		setButtons(getButtonManager().getButtonAry(getUIControl().getCardButtonAry()));

		//设置模版切换按钮
		getCardEventHandler().initNodeKeyButton();

		//设置其它按钮
		getCardEventHandler().initActionButton();

		//初始化UI界面
		initUI();

		//设置对model的监听，observer模式
		getBufferData().addObserver(this);

	}
	catch (Exception e)
	{
		e.printStackTrace();
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000109")/*@res "发生异常，界面初始化错误"*/+e.getMessage());

	}
}

/**
* 初始化设置当前UI,
* 对应的各抽象UI必须实现该方法(BillCardUI,BillManageUI)
* 创建日期：(2004-2-19 13:20:37)
*/
public final void initUI() throws Exception {
	if (getUIControl().getBillType() == null)
		return;
	this.removeAll();

	//设置UI
	this.m_BillCardPanelWrapper = createBillCardPanelWrapper();

	this.add(
		m_BillCardPanelWrapper.getBillCardPanel(),
		java.awt.BorderLayout.CENTER);

	//设置事件监听
	initEventListener();

	//设置单据状态
	if (getUIControl().isExistBillStatus())
		getBillCardWrapper().initHeadComboBox(
			getBillField().getField_BillStatus(),
			new BillStatus().strStateRemark,
			true);

	//设置小数位数
	getBillCardWrapper().setCardDecimalDigits(
		getCardHeadShowNum(),
		getCardItemShowNum());

	//初始化UI数据
	initSelfData();

	//设置初始状态
	if (getUIControl() instanceof ISingleController)
		if (((ISingleController) getUIControl()).isSingleDetail())
			setBillOperate(IBillOperate.OP_NOTEDIT);
		else
			setBillOperate(IBillOperate.OP_INIT);
	else
		setBillOperate(IBillOperate.OP_INIT);
}

/**
 * 查询整个单据VO数据(用于单据联查和工作流使用）
 * 创建日期：(2003-7-16 15:48:52)
 * @param key java.lang.String
 * @exception java.lang.Exception 异常说明。
 */
protected AggregatedValueObject loadHeadData(String key)
	throws java.lang.Exception {
	AggregatedValueObject retVo =
		(AggregatedValueObject) Class
			.forName(getUIControl().getBillVoName()[0])
			.newInstance();
	//查询主表
	//因为有的单据的表头VO中以有自定义项目，比如新品牌指标，
   //必须通过单据对应的DMO来查询
   SuperVO tmpvo = (SuperVO)Class.forName(getUIControl().getBillVoName()[1]).newInstance();
   retVo.setParentVO(getBusiDelegator().queryByPrimaryKey(tmpvo.getClass(),key));
   //子表数据
	//setChcekManAndDate(retVo);
	return retVo;
}

/**
 * 设置单据编号。
 * 创建日期：(2003-6-28 10:03:55)
 * @exception java.lang.Exception 异常说明。
 */
protected void setBillNo() throws java.lang.Exception {
	BillItem noItem =
		getBillCardPanel().getHeadItem(getBillField().getField_BillNo());
	if (noItem == null)
		return;
	noItem.setValue(getBillNo());
	noItem.setEnabled(getBusiDelegator().getParaBillNoEditable().booleanValue());
	noItem.setEdit(getBusiDelegator().getParaBillNoEditable().booleanValue());
}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-7-21 14:47:32)
 * @param newBillOperate int
 */
public final void setBillOperate(int newBillOperate) throws Exception {
	m_billOperate = newBillOperate;
	setTotalUIState(newBillOperate);
}

/**
* 设置表体复杂数据。
* 需要进行界面转换的数据,如自定义参照的数据
* 如果需要，则重载该方法
* 创建日期：(2003-1-6 14:56:37)
* @param vo nc.vo.pub.CircularlyAccessibleValueObject[]
*/
protected void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {
}

/**
* 设置UI层界面数据。
* 创建日期：(2004-2-25 19:05:24)
* @param vo nc.vo.pub.AggregatedValueObject
*/
public final void setCardUIData(AggregatedValueObject vo) throws Exception {
	getBillCardWrapper().setCardData(vo);
	if (vo.getParentVO() != null)
		setHeadSpecialData(vo.getParentVO());
	if (vo.getChildrenVO() != null)
		setBodySpecialData(vo.getChildrenVO());
}

	/**
* 设置单据UI状态为CARD,重载父类的方法
* 创建日期：(2004-1-11 21:07:42)
*/
public final void setCardUIState()
{
	return;
}

/**
* 设置指定行表头复杂数据。
* 需要进行界面转换的数据,如自定义参照的数据
* 创建日期：(2003-1-6 14:56:37)
* @param vo nc.vo.pub.CircularlyAccessibleValueObject[]
* @param intRow int
*/
protected void setHeadSpecialData(CircularlyAccessibleValueObject vo)
	throws Exception {
}

/**
* 设置单据按钮状态，根据串入的VO数据进行判断，可能为空。
* 创建日期：(2002-12-31 11:19:21)
* @param vo nc.vo.pub.AggregatedValueObject
*/
protected void setTotalUIState(int intOpType) throws Exception {

	//设置按钮状态
	getButtonManager().setButtonByOperate(intOpType);
	updateButtons();
	//根据操作类型设置UI状态
	switch (intOpType) {
		case OP_ADD :
			{
				getBillCardPanel().setEnabled(true);
				getBillCardPanel().addNew();
				setDefaultData();
				setBillNo();
				getBillCardPanel().transferFocusToFirstEditItem();
				break;
			}
		case OP_EDIT :
			{
				getBillCardPanel().setEnabled(true);
				m_BillCardPanelWrapper.setRowStateToNormal();
				getBillCardPanel().transferFocusToFirstEditItem();
				break;
			}
		case OP_REFADD :
			{
				getBillCardPanel().setEnabled(true);
				//getBillCardPanel().addNew();
				setDefaultData();
				setBillNo();
				break;
			}
		case OP_INIT :
			{
				this.m_BillCardPanelWrapper.setCardData(null);
			}
		case OP_NOTEDIT :
			{
				getBillCardPanel().setEnabled(false);
				break;
			}
		default :
			{
				break;
			}
	}

}

/**
 * This method is called whenever the observed object is changed. An
 * application calls an <tt>Observable</tt> object's
 * <code>notifyObservers</code> method to have all the object's
 * observers notified of the change.
 *
 * @param   o     the observable object.
 * @param   arg   an argument passed to the <code>notifyObservers</code>
 *                 method.
 */
public void update(java.util.Observable o, java.lang.Object arg)
{
	if(beforeUpdate()){
		try
		{
			this.m_BillCardPanelWrapper.setCardData(getBufferData().getCurrentVO());
			if (getBufferData().getCurrentVO() != null)
				setBodySpecialData(getBufferData().getCurrentVO().getChildrenVO());
			this.m_BillCardPanelWrapper.getBillCardPanel().updateValue();
			updateBtnStateByCurrentVO();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}
	}

	afterUpdate();

}

//更新单据状态
public void updateBtnStateByCurrentVO() throws Exception {
	//设置单据状态
	if (getUIControl().isExistBillStatus())
		getButtonManager().setButtonByBillStatus(
			getBufferData(),
			getUIControl().isEditInGoing());
	//设置扩展状态
	getButtonManager().setButtonByextendStatus(
		getExtendStatus(getBufferData().getCurrentVO()));
	//设置页状态
	getButtonManager().setPageButtonState(getBufferData());
	updateButtons();
}

	public void doApproveAction(ILinkApproveData approvedata) {

		initialize();
		// 加载数据
		try {
			getBufferData()
					.addVOToBuffer(loadHeadData(approvedata.getBillID()));
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		initialize();
		// 加载数据
		try {
			getBufferData()
					.addVOToBuffer(loadHeadData(maintaindata.getBillID()));
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	@Override
	protected void saveOnClosing() throws Exception {
		getCardEventHandler().onBoSave();
	}
	
	
}