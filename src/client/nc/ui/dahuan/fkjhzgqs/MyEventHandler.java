package nc.ui.dahuan.fkjhzgqs;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	  public String condition = "";
	  
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		
		//����ƻ�����
		// ����������ӹ�������
		//�����ڲ���Ԥ��֮ǰ������ˣ�������˱�־shrflag1Ϊ1 �Ƶ���ʶvoperatorflag = 1���Ҳ�����˱�־Ϊ0
		
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		condition = strWhere.toString() 
						+ " and nvl(dh_fkjhbill.voperatorflag,0) = 1 and  nvl(dh_fkjhbill.shrflag1,0)=1  and nvl(dh_fkjhbill.cwflag,0) = 0 "
						+ " and dh_fkjhbill.fk_dept in (select v.pk_deptdoc from v_deptperonal v " 
						+ " where v.pk_user = '"+user+"' and v.pk_corp = '"+pkCorp+"')";

		setListValue();
	}
	
	private void setListValue() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	// ȫѡ
	@Override
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// ȫ��
	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}
	
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	//����
	@Override
	protected void onBoCancelAudit() throws Exception {			
			// �жϲ�����Ա����ά��
			AggregatedValueObject modelVO = getBufferData().getCurrentVO();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			// �жϵ����Ƿ����ύ��δ���
			String zgsh = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			String fzsh = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			if("0".equals(zgsh)||"".equals(zgsh)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����δ�������������");
				return;
			}
			if("1".equals(fzsh)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog(newfkvo);
			
			if(flag){
				newfkvo.setShrflag1("0");
				newfkvo.setCwflag("0");
				newfkvo.setVoperatorflag("0");
				newfkvo.setShrdate1(null);
				newfkvo.setCwdate(null);
				newfkvo.setShrid1(null);
				newfkvo.setCwid(null);
				newfkvo.setRet_date(this._getDate());
				newfkvo.setRet_user(this._getOperator());
				NCLocator.getInstance().lookup(IVOPersistence.class).updateVO(newfkvo);
				
				RetRecordVO rtvo = new RetRecordVO();
				rtvo.setRet_user(this._getOperator());
				rtvo.setRet_date(this._getDate());
				rtvo.setRet_vemo(newfkvo.getQishenyuanyin());
				rtvo.setRet_type(1);
				rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
				rtvo.setRet_address("���Ÿ�������");			
				HYPubBO_Client.insert(rtvo);
				
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�������");
				setListValue();
			}

	}
		
}