package nc.ui.dahuan.htallquery;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.job.IJobtypePrivate;
import nc.ui.bfriend.button.ExceptFHSKBtnVO;
import nc.ui.bfriend.button.ExceptHTBtnVO;
import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.MultiChildBillTreeManageUI;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

public class MultiChildTreeCardUI extends MultiChildBillTreeManageUI implements
		ILinkQuery {

	private static final long serialVersionUID = 1L;

	private IJobtypePrivate pfserver = null;

	protected AbstractManageController createController() {
		return new MultiChildTreeCardController();
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	public MultiChildTreeCardUI() {
		super();
		modifyRootNodeShowName("合同");
		this.getBillCardPanel().setAutoExecHeadEditFormula(true); // 设置表头编辑公式可执行
		this.getButtonManager().getButton(IdhButton.YCHT).setEnabled(true);
		this.getButtonManager().getButton(IdhButton.FHSK).setEnabled(true);
		this.getButtonManager().getButton(IdhButton.YCHTDC).setEnabled(true);
		this.getButtonManager().getButton(IdhButton.FHSKDC).setEnabled(true);
	}

	protected IVOTreeData createTableTreeData() {
		return new MultiChildTreeCardData();
	}

	protected IVOTreeData createTreeData() {

		return new MultiChildTreeCardData();
	}

	public void updateTreeData() {
		this.getBillTree().setModel(getBillTreeModel(getCreateTreeData()));

	}

	protected UITree getBillTree() {
		return super.getBillTree();
	}

	protected ManageEventHandler createEventHandler() {
		return new MultiChildTreeCardEventHandler(this, getUIControl());
	}

	public String getRefBillType() {

		return null;
	}

	protected void initSelfData() {
		getBillListPanel().getHeadTable().setCellSelectionEnabled(false);
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true);
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	public void setDefaultData() throws Exception {}


	private JobtypeVO queryJobtype() {

		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
		try {
			JobtypeVO[] typevos = getJobtypePrivate().queryAllJobtypeVOs(
					pk_corp, false);
			for (int i = 0; i < typevos.length; i++) {
				if (typevos[i].getJobtypecode().equalsIgnoreCase("01")) {
					return typevos[i];
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return null;

	}

	private IJobtypePrivate getJobtypePrivate() {
		if (pfserver == null) {
			pfserver = (IJobtypePrivate) NCLocator.getInstance().lookup(
					IJobtypePrivate.class.getName());
			return pfserver;
		}
		return pfserver;
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	protected void initPrivateButton() {		
		FileUpLoadBtnVO BtnVO7 = new FileUpLoadBtnVO();
		ButtonVO btn7= BtnVO7.getButtonVO();
		btn7.setBtnName("合同文本");
		addPrivateButton(btn7);
		
		ExceptHTBtnVO btn=new ExceptHTBtnVO();
		addPrivateButton(btn.getButtonVO());
		addPrivateButton(btn.getButtonVODC());
		
		ExceptFHSKBtnVO btn2=new ExceptFHSKBtnVO();
		addPrivateButton(btn2.getButtonVO());
		addPrivateButton(btn2.getButtonVODC());
	}
	
}
