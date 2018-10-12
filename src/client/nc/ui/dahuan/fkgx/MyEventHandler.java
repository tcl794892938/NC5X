package nc.ui.dahuan.fkgx;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkgx.DhFkgxDVO;
import nc.vo.dahuan.fkgx.DhFkgxVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		// ȷ�ϱ�ͷ��Ϣ����
		BillCardPanel panel = this.getBillCardPanelWrapper().getBillCardPanel();
		Object pkDept = panel.getHeadItem(DhFkgxVO.PK_DEPTDOC).getValueObject();
		Object pkUser = panel.getHeadItem(DhFkgxVO.PK_USER1).getValueObject();
		
		if(null == pkDept || null == pkUser || "".equals(pkDept) || "".equals(pkUser)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ά���ò��źͲ���������Ϣ");
			return;
		}
		
		super.onBoLineAdd();
	}
	
	

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		// ���Ų����޸�
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		card.getHeadItem("pk_deptdoc").setEdit(false);
	}

	@Override
	protected void onBoSave() throws Exception {
		// ����У��
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		// ���������ֶα����У�鲿�������ظ�
		Object pkUser = card.getHeadItem("pk_user1").getValueObject();
		if(null == pkUser || "".equals(pkUser)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����˱���");
			return;
		}
		Object pkDept = card.getHeadItem("pk_deptdoc").getValueObject();
		if(null == pkDept || "".equals(pkDept)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������Ϣ����");
			return;
		}
		int rows = card.getBillModel().getRowCount();
		if(rows <= 0){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ܼ�����ά������Ա����Ϣ");
			return;
		}

		Object fkgxPK = card.getHeadItem("pk_fkgx").getValueObject();
		// �ж����޸ı��滹���������棬��������������У�鲿�ŵ�Ψһ��
		if(null == fkgxPK || "".equals(fkgxPK)){
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			SQLParameter param = new SQLParameter();
			String sql = "select count(1) from dh_fkgx t where t.pk_deptdoc=? and nvl(t.dr,0)=0 and t.pk_corp=?";
			param.clearParams();
			param.addParam(pkDept);
			param.addParam(_getCorp().getPrimaryKey());
			Integer count = (Integer)query.executeQuery(sql,param, new ColumnProcessor());
			if(count>0){
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ò�����Ϣ�Ѵ���");
				return;
			}
		}
		

		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			// �������
			DhFkgxVO mvo = (DhFkgxVO)billVO.getParentVO();
			String hy="";
			if(null == mvo.getPk_fkgx() || "".equals(mvo.getPk_fkgx())){
				hy = (String)HYPubBO_Client.insert(mvo);//��������ֵ
				
			}else{
				hy = mvo.getPk_fkgx();
				HYPubBO_Client.update(mvo);
			}
			
			// �ӱ����
			DhFkgxDVO[] dvos = (DhFkgxDVO[])billVO.getChildrenVO();
			DhFkgxDVO[] nvos = new DhFkgxDVO[dvos.length];
			for(int i=0;i<dvos.length;i++){
				DhFkgxDVO dvo = dvos[i];
				String pkDDuser = dvo.getPk_dept_user();
				if(!"".equals(pkDDuser) && null != pkDDuser){
					dvo.setPk_fkgx(hy);
					dvo.setPk_fkgx_d(null);
					nvos[i]=dvo;
				}
			}
			HYPubBO_Client.deleteByWhereClause(DhFkgxDVO.class, " pk_fkgx = '"+mvo.getPk_fkgx()+"'");
			HYPubBO_Client.insertAry(nvos);
		}

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// ���ñ����״̬
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	
	}
	
		
}