package nc.ui.dahuan.fkjhjl;

import java.util.ArrayList;
import java.util.List;

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
import nc.vo.pub.lang.UFDate;

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
		
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		
		//�������ʱ��������˱�־shrflag1=1������Ԥ���־cwflagΪ1��������˱�־shrflag2=0
		condition = strWhere.toString() 
						+ " and nvl(dh_fkjhbill.shrflag1,0) = 1 " 
						+ " and nvl(dh_fkjhbill.cwflag,0) = 1 " 
						+ " and nvl(dh_fkjhbill.shrflag2,0) = 0 " 
						+ " and dh_fkjhbill.shrid2 = '"+user+"'" 
						+ " order by dh_fkjhbill.pk_cust2";

		setListvalue();
	}
	
	private void setListvalue() throws Exception{
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
	
	@Override
	public void onBoAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// ��ǰ������
		String user = this._getOperator();
		// ��ǰʱ��
		UFDate date = this.getBillUI()._getServerTime().getDate();
		
		// �жϿ�Ƭ���滹���б���
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				// �����Ƿ�ѡ
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// �жϵ����Ƿ��ѱ���������Ҹ���δ���
					String cwsh = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
					String fzsh = newfkvo.getShrflag2()==null?"":newfkvo.getShrflag2();
					if("0".equals(cwsh)||"".equals(cwsh)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����δ���������");
						return;
					}
					if("1".equals(fzsh)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
						return;
					}
					
					newfkvo.setShrflag2("1");
					newfkvo.setShrdate2(date);
					
					fkList.add(newfkvo);
				}
			}

		} else {

			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			//�жϵ����Ƿ��ѱ���������Ҹ���δ���
			String cwsh = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			String fzsh = newfkvo.getShrflag2()==null?"":newfkvo.getShrflag2();
			if("0".equals(cwsh)||"".equals(cwsh)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����δ���������");
				return;
			}
			if("1".equals(fzsh)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
				return;
			}
			
			newfkvo.setShrflag2("1");
			newfkvo.setShrdate2(date);
			
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����������");
		setListvalue();
	}
	
//	�ܾ�
	@Override
	protected void onBoCancelAudit() throws Exception {
			
			// �жϲ�����Ա����ά��
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)iQ.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			Object printflag = newfkvo.getIs_print()==null?"":newfkvo.getIs_print();
			
			if("0".equals(cwflag)||"".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����û�б��������");
				return;
			}
			if("1".equals(printflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ���ӡ");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
			boolean flag = cadg.showCanAudMemoDialog(newfkvo);
			
			if(flag){
				newfkvo.setShrflag1("0");
				newfkvo.setCwflag("0");
				newfkvo.setVoperatorflag("0");
				newfkvo.setIs_print(0);
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
				rtvo.setRet_address("���ܸ�������");			
				HYPubBO_Client.insert(rtvo);
				
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "���ܲ������");
				setListvalue();
			}		
	}
}