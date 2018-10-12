package nc.ui.fts.fts401;
/**
 * ����֪ͨ by tcl
 */
import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.vo.bd.b19.BalatypeVO;
import nc.vo.fts.fts401.IReceiveConst;
import nc.vo.fts.fts401.ReceiveDecorateVO;
import nc.vo.fts.pub.CenterUnitUtil;
import nc.vo.fts.pub.proxy.FTSProxy;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tam.account.IAccConst;
import nc.vo.tam.account.itfvo.AccParamVO;

/**
 * TODO ����֪ͨ�Ƶ�ǰ����ί�е��ݻ������²�������Ϣ
 * @author chengfei
 * @version 1.0 2010-7-28
 * @since v5.7
 */
public class FillInfoBeforeMakeBillDlg extends UIDialog implements ActionListener,ValueChangedListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

//	private UICheckBox cbx_CommissionBill;
//
//	private UICheckBox cbx_SFBill;

	private UIRefPane  ref_FundOrg;

	private UIRefPane  ref_FinanceOrg;

	private UIRefPane  ref_Acc;

	private UIRefPane  ref_PlanItem;
	
	private UIRefPane  ref_Balatype;

	private UIRefPane  ref_FundType;

	private UIRefPane  ref_Remark;

	private UIButton   uibtn_OK;

	private UIButton   uibtn_Cancel;

	private ReceiveDecorateVO  decorateVO;

	private String     billType;
/**
 * ��ǰ��˾
 * ����ί�е���ʱ���ݽ��������ʽ���֯PK
 * ���ɵ��ȵ���ʱ���ݽ������ǹ�˾PK
 */
	private String     pk_CurrFundOrg;

	private UFDate     currdate;

	private String     pk_currency;



	/**
	 *
	 * FillInfoBeforeMakeBillDlg�Ĺ��췽��
	 * @param parent
	 * @param billType SF ���ɵ��ȵ���  FTS  ����ί�е���
	 */
	public FillInfoBeforeMakeBillDlg(Container parent,String billType,String pk_CurrFundOrg,UFDate currdate,String pk_currency) {
		super(parent);
		this.billType = billType;
		this.pk_CurrFundOrg = pk_CurrFundOrg;
		this.currdate = currdate;
		this.pk_currency = pk_currency;
//		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		init();
	}

	public FillInfoBeforeMakeBillDlg(Frame owner) {
		super(owner);
		init();
	}

	private void init(){
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);

		if(billType.endsWith(IReceiveConst.BillType_FTS)){
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000242")/*@res "����֪ͨ����ί�е��ݲ�����Ϣ����"*/);
		}
		else{
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000243")/*@res "����֪ͨ���ɵ��ȵ��ݲ�����Ϣ����"*/);
		}

//		cbx_CommissionBill = new UICheckBox("ί���ո�����");
//		cbx_CommissionBill.setBounds(new Rectangle(10,10,100,25));
//		contentPanel.add(cbx_CommissionBill);
//
//		cbx_SFBill = new UICheckBox("�����²�����");
//		cbx_SFBill.setBounds(new Rectangle(120,10,100,25));
//		contentPanel.add(cbx_SFBill);

		UILabel ul_FundOrg = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000244")/*@res "��/�����ʽ���֯"*/);
		ul_FundOrg.setBounds(new Rectangle(10,10,120,25));
		ref_FundOrg = new UIRefPane("�ʽ���֯");
		ref_FundOrg.setPK(pk_CurrFundOrg);
//		ref_FundOrg.set
		ref_FundOrg.setWhereString(getFundOrgSqlWhere());
		ref_FundOrg.setBounds(new Rectangle(130,10,100,25));
		ref_FundOrg.addValueChangedListener(this);


		UILabel ul_FinanceOrg = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000245")/*@res "��/���λ"*/);
		ul_FinanceOrg.setBounds(new Rectangle(250,10,70,25));
		ref_FinanceOrg = new UIRefPane("��Ա��λ") ;
		ref_FinanceOrg.setWhereString(getFundOrgSqlWhere());
		ref_FinanceOrg.setBounds(new Rectangle(320,10,100,25));
		ref_FinanceOrg.addValueChangedListener(this);
		ref_FinanceOrg.getRefModel().setWherePart(this.getSettleUnitSqlWhere());




		UILabel ul_Acc = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000246")/*@res "��/���λ�ڲ��˻�"*/);
		ul_Acc.setBounds(new Rectangle(10,45,120,25));
		ref_Acc = new UIRefPane("�ڲ��˻�");
		ref_Acc.setWhereString(getFundOrgSqlWhere());
		ref_Acc.setBounds(new Rectangle(130,45,100,25));
		ref_Acc.getRefModel().setWherePart(this.getAccidSqlWhere());

		UILabel ul_Balatype = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000593")/*@res "���㷽ʽ"*/);
		ref_Balatype = new UIRefPane("���㷽ʽ");
		ref_Balatype.addValueChangedListener(this);
		
		UILabel ul_FundType = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000594")/*@res "�ʽ�����"*/);
		ref_FundType = new UIRefPane("�ʽ�����");
		ref_FundType.setWhereString(getFundOrgSqlWhere());
		ref_FundType.setEnabled(false);

		UILabel ul_PlanItem = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000247")/*@res "��/����ƻ���Ŀ"*/);
		ref_PlanItem = new UIRefPane("�ʽ�ƻ���Ŀ");
		ref_PlanItem.setWhereString(getFundOrgSqlWhere());


		UILabel ul_Remark= new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002185")/*@res "ժҪ"*/);
		ref_Remark = new UIRefPane("����ժҪ");
		ref_Remark.setWhereString(getFundOrgSqlWhere());
		ref_Remark.setAutoCheck(false);

		if(billType.equals(IReceiveConst.BillType_FTS)){
			contentPanel.add(ul_FundOrg);
			contentPanel.add(ref_FundOrg);
			contentPanel.add(ul_FinanceOrg);
			contentPanel.add(ref_FinanceOrg);
			contentPanel.add(ul_Acc);
			contentPanel.add(ref_Acc);
			
			ul_Balatype.setBounds(new Rectangle(10,80,120,25));
			contentPanel.add(ul_Balatype);
			ref_Balatype.setBounds(new Rectangle(130,80,100,25));
			contentPanel.add(ref_Balatype);

			ul_FundType.setBounds(new Rectangle(250,80,70,25));
			contentPanel.add(ul_FundType);
			ref_FundType.setBounds(new Rectangle(320,80,100,25));
			contentPanel.add(ref_FundType);

			ul_PlanItem.setBounds(new Rectangle(10,115,120,25));
			contentPanel.add(ul_PlanItem);
			ref_PlanItem.setBounds(new Rectangle(130,115,100,25));
			contentPanel.add(ref_PlanItem);
			//ref_PlanItem.getRefModel().setWherePart(" pk_corp='" + CenterUnitUtil.instance.getSettleCenterCorpPKByCenterPK(ref_FundOrg.getRefPK()) + 
					//"' and isnull(fp_dim_planitem.dr,0)=0 and isunused != 'Y'");

			ul_Remark.setBounds(new Rectangle(250,115,70,25));
			contentPanel.add(ul_Remark);
			ref_Remark.setBounds(new Rectangle(320,115,100,25));
			contentPanel.add(ref_Remark);
		}
		else if(billType.equals(IReceiveConst.BillType_SF)){
			
			ul_Balatype.setBounds(new Rectangle(10,10,120,25));
			contentPanel.add(ul_Balatype);
			ref_Balatype.setBounds(new Rectangle(130,10,100,25));
			contentPanel.add(ref_Balatype);

			ul_FundType.setBounds(new Rectangle(250,10,70,25));
			contentPanel.add(ul_FundType);
			ref_FundType.setBounds(new Rectangle(320,10,100,25));
			contentPanel.add(ref_FundType);

			ul_PlanItem.setBounds(new Rectangle(10,45,120,25));
			contentPanel.add(ul_PlanItem);
			ref_PlanItem.setBounds(new Rectangle(130,45,100,25));
			contentPanel.add(ref_PlanItem);
			//by tcl
			//ref_PlanItem.getRefModel().setWherePart(" pk_corp ='"+pk_CurrFundOrg+"' and isnull(dr,0) = 0 and isunused != 'Y'");

			ul_Remark.setBounds(new Rectangle(250,45,70,25));
			contentPanel.add(ul_Remark);
			ref_Remark.setBounds(new Rectangle(320,45,100,25));
			contentPanel.add(ref_Remark);
		}


		uibtn_OK = new UIButton();
		uibtn_OK.setName("UIButtonOK");
		uibtn_OK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "ȷ��"*/);
		uibtn_OK.setBounds(new Rectangle(130, 150, 80, 22));
		uibtn_OK.addActionListener(this);
		contentPanel.add(uibtn_OK);

		uibtn_Cancel = new UIButton();
		uibtn_Cancel.setName("UIButtonCancel");
		uibtn_Cancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/);
		uibtn_Cancel.setBounds(new Rectangle(230, 150, 80, 22));
		uibtn_Cancel.addActionListener(this);
		contentPanel.add(uibtn_Cancel);

		this.setSize(440, 210);
		this.add(contentPanel);
	}




	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.uibtn_OK){
			closeOK();
		}
		else if(e.getSource() == this.uibtn_Cancel){
			super.closeCancel();
		}
	}
	@Override
	public void closeOK() {

		if(billType.equals(IReceiveConst.BillType_FTS)){
			if(ref_FundOrg.getRefPK() == null || ref_FundOrg.getRefPK().length() == 0){
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000249")/*@res "��ѡ��һ���ʽ���֯!"*/);
				return;
			}
			else{
				if(!ref_FundOrg.getRefPK().equals(pk_CurrFundOrg)){

					try {
						java.util.Vector vec_center = FTSProxy.getClearPub().getCenterList(pk_CurrFundOrg, ref_FundOrg.getRefPK());
						if(vec_center == null || vec_center.size() == 0){
							MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000250")/*@res "��ѡ�ʽ���֯�͵�ǰ�ʽ���֯����ͬһ�ʽ���ϵ"*/);
							return;
						}
						String pk_Corp = CenterUnitUtil.instance.getSettleCenterCorpPKByCenterPK(ref_FundOrg.getRefPK());
						if(!FTSProxy.getCommon().isStartProcess(pk_Corp,currdate).booleanValue()){
							MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000251")/*@res "��ѡ�ʽ���֯��δ��ʼ����ҵ��"*/);
							return;
						}
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, e.getMessage());
						return;
					}
				}




			}
			if (ref_FinanceOrg.getRefPK() == null || ref_FinanceOrg.getRefPK().length() == 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000252")/*@res "��ѡ��һ����/���λ!"*/);
				return;
			}
			if (ref_Acc.getRefPK() == null || ref_Acc.getRefPK().length() == 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000253")/*@res "��ѡ��һ����/���λ�ڲ��˻�!"*/);
				return;
			}
		}

		ReceiveDecorateVO decorateVO = new  ReceiveDecorateVO();
		decorateVO.setPk_FundOrg(ref_FundOrg.getRefPK());
		decorateVO.setPk_FinanceOrg(ref_FinanceOrg.getRefPK());
		decorateVO.setPk_accid(ref_Acc.getRefPK());
		decorateVO.setPk_FundType(ref_FundType.getRefPK());
		decorateVO.setPk_planItem(ref_PlanItem.getRefPK());
		decorateVO.setBalatype(ref_Balatype.getRefPK());
		decorateVO.setRemark(ref_Remark.getUITextField().getText());
		setDecorateVO(decorateVO);

		setResult(ID_OK);
		close();
		fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_OK));
		return;
	}
	private String getFundOrgSqlWhere(){
//		String sqlWhere =  " pk_corp='" + ref_FundOrg.getRefPK() + "' and isnull(fp_dim_planitem.dr,0)=0 and isunused != 'Y' ";
//		return sqlWhere;
		return "";
	}
	/**
	 *
	 * TODO ��ȡ��Ա��λ���չ���sql
	 * @return
	 * @version 1.0 2010-8-4
	 * @author chengfei
	 * @since NC5.7
	 */
	private String getSettleUnitSqlWhere(){
		if(ref_FundOrg.getRefPK() == null || ref_FundOrg.getRefPK().length() == 0){
			return "";
		}
		else{
			return " pk_settlecent = '" + ref_FundOrg.getRefPK() +"'";
		}
	}
	/**
	 *
	 * TODO ��ȡ�ڲ��˻����չ���sql
	 * @return
	 * @version 1.0 2010-8-4
	 * @author chengfei
	 * @since NC5.7
	 */
	private String getAccidSqlWhere(){
		String whereSql = " (bd_accid.acctype = '" + IAccConst.ACCCL_CURRENT
		+ "' or bd_accid.acctype = '" + IAccConst.ACCCL_ACCORD + "')"
	    + " and bd_accid.pk_currtype = '" + pk_currency + "'"
		+ " and (bd_accid.frozenflag = '" + IAccConst.FROZENFLAG_NORMAL + "' or "
		+ " bd_accid.frozenflag = '" + IAccConst.FROZENFLAG_PARTFROZEN + "')"
		+ " and isnull(bd_accid.dr,0) = 0";

		if(ref_FinanceOrg.getRefPK() != null && ref_FinanceOrg.getRefPK().length() != 0){
			whereSql += " and bd_accid.ownercorp = '" + CenterUnitUtil.instance.getUnitCorpPKByUnitPK(ref_FinanceOrg.getRefPK()) +"'";
		}
		if(ref_FundOrg.getRefPK() != null && ref_FundOrg.getRefPK().length() != 0){
			whereSql += " and bd_accid.pk_corp = '" + CenterUnitUtil.instance.getSettleCenterCorpPKByCenterPK(ref_FundOrg.getRefPK()) +"'";
		}
		return whereSql;
	}

	/**
	 * TODO �ֶ�����
	 * @return the decorateVO
	 */
	public ReceiveDecorateVO getDecorateVO() {
		return decorateVO;
	}

	/**
	 * TODO �ֶ�����
	 * @param decorateVO the decorateVO to set
	 */
	public void setDecorateVO(ReceiveDecorateVO decorateVO) {
		this.decorateVO = decorateVO;
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.beans.ValueChangedListener#valueChanged(nc.ui.pub.beans.ValueChangedEvent)
	 */
	public void valueChanged(ValueChangedEvent event) {
		if(event.getSource() == this.ref_FundOrg){
			this.ref_FinanceOrg.getRefModel().setWherePart(getSettleUnitSqlWhere(), true);
			this.ref_Acc.getRefModel().setWherePart(getAccidSqlWhere(), true);
			
			if(billType.equals(IReceiveConst.BillType_FTS)){
				this.ref_PlanItem.getRefModel().setWherePart(" pk_corp ='"+CenterUnitUtil.instance.getSettleCenterCorpPKByCenterPK(ref_FundOrg.getRefPK())+
						"' and isnull(dr,0) = 0 and isunused != 'Y'");
			}
			
		}
		if(event.getSource() == this.ref_Balatype ){
			BalatypeVO balatypevo = new BalatypeVO();
			try {
				balatypevo = FTSProxy.getBalaTypeService().findBalaTypeVOByPK(ref_Balatype.getRefPK());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();						
			}
			if(balatypevo != null){
				this.ref_FundType.setPK(balatypevo.getCapitaltype());
			}
		}
		if(event.getSource() == this.ref_FinanceOrg){
			this.ref_Acc.getRefModel().setWherePart(getAccidSqlWhere(), true);
			AccParamVO accParamVO = new AccParamVO();
			accParamVO.setPk_currency(pk_currency);
			if(StringUtil.isEmptyWithTrim(ref_FundOrg.getRefPK())){
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000249")/*@res "��ѡ��һ���ʽ���֯!"*/);
				return;
			}
			if(StringUtil.isEmptyWithTrim(ref_FinanceOrg.getRefPK())){
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("36104002","UPT36104002-000151")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("ftsclientcommn","UPPftsclientcommn-000254")/*@res "��ѡ��һ����Ա��λ!"*/);
				return;
			}
			accParamVO.setPk_corp(CenterUnitUtil.instance.getCorpPKByUnitPK(ref_FinanceOrg.getRefPK()));
			accParamVO.setPk_fungorg(CenterUnitUtil.instance.getSettleCenterCorpPKByCenterPK(ref_FundOrg.getRefPK()));

			try {
				String pk_ClearAcc = FTSProxy.getDefaultAccidService().getDefaultAcc(accParamVO);
				ref_Acc.setPK(pk_ClearAcc);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}