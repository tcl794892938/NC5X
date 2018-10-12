package nc.ui.dahuan.fkjhcwcs;

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
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		
		//����Ԥ���ڲ������֮���������˱�־cwflag=0�����Ҳ�����˱�־shrflag1Ϊ1
		condition = strWhere.toString() 
							+ "  and nvl(dh_fkjhbill.cwflag,0) = 0 and nvl(dh_fkjhbill.shrflag1,0) = 1 ";
		setListValue();
		
	}
	
	public void setListValue() throws Exception{
		
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);
    
		updateBuffer();
	}
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	
	
	// �ֵ�
	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhFkjhbillVO fkvo = (DhFkjhbillVO)aggvo.getParentVO();
			DhFkjhbillVO nfkvo = (DhFkjhbillVO)HYPubBO_Client.queryByPrimaryKey(DhFkjhbillVO.class, fkvo.getPk_fkjhbill());
			new FKSingleDialog(this.getBillUI()).showFKSingleDialog(nfkvo);
			this.onBoRefresh();
		}
	}

	@Override
	//����Ԥ��
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
			
			String slgmeg = "";
			
			for (int row = 0; row < num; row++) {
				// �����Ƿ�ѡ
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// �жϵ����Ƿ����ύ��δ���
					//��������ˣ�����û���
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
					
					if("0".equals(shflag)||"".equals(shflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����û�б��������");
						return;
					}
					if("1".equals(cwflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
						return;
					}
					
					if(0==newfkvo.getIs_single()){
						slgmeg += "["+row+"]";
					}
					
					newfkvo.setCwflag("1");
					newfkvo.setCwdate(date);
					newfkvo.setCwid(user);
					fkList.add(newfkvo);
				}
			}
			
			if(!"".equals(slgmeg)){
				int slgret = MessageDialog.showOkCancelDlg(getBillManageUI(), "ѡ��", "��"+slgmeg+"�и��δ�����ֵ��������Ƿ�ȷ�����ˣ�");
				if(!(MessageDialog.ID_OK==slgret)){
					return;
				}
			}

		} else {
			
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			//�жϵ����Ƿ����ύ��δ���
			//��������ˣ�����û���
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			
			if("0".equals(shflag)||"".equals(shflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����û�б��������");
				return;
			}
			if("1".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
				return;
			}
			
			if(0==newfkvo.getIs_single()){
				int slgret = MessageDialog.showOkCancelDlg(getBillManageUI(), "ѡ��", "��ǰ���δ�����ֵ��������Ƿ�ȷ�����ˣ�");
				if(!(MessageDialog.ID_OK==slgret)){
					return;
				}
			}
			
			newfkvo.setCwflag("1");
			newfkvo.setCwdate(date);
			newfkvo.setCwid(user);
			fkList.add(newfkvo);

		}
		if(null != fkList && fkList.size()>0){
			NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
			MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����������");
			setListValue();
		}else{
			MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�빴ѡ����");
		}
	}

	
	//�ܾ�
	@Override
	protected void onBoCancelAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// �жϿ�Ƭ���滹���б���
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			int[] rowsels = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRows();
			if(0==rowsels.length){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog();
			String meg = cadg.value;
			if(!flag){
				return;
			}
			
			for (int row = 0; row < num; row++) {
				// �����Ƿ�ѡ
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
					
					if("0".equals(shflag)||"".equals(shflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����û�б��������");
						return;
					}
					if("1".equals(cwflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
						return;
					}
					
					newfkvo.setShrflag1("0");
					newfkvo.setCwflag("0");
					newfkvo.setVoperatorflag("0");
					newfkvo.setShrdate1(null);
					newfkvo.setCwdate(null);
					newfkvo.setShrid1(null);
					newfkvo.setCwid(null);
					newfkvo.setRet_date(this._getDate());
					newfkvo.setRet_user(this._getOperator());
					newfkvo.setQishenyuanyin(meg);
					
					RetRecordVO rtvo = new RetRecordVO();
					rtvo.setRet_user(this._getOperator());
					rtvo.setRet_date(this._getDate());
					rtvo.setRet_vemo(meg);
					rtvo.setRet_type(1);
					rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
					rtvo.setRet_address("���񸶿��");			
					HYPubBO_Client.insert(rtvo);
					
					fkList.add(newfkvo);
				}
			}

		} else {
			
			// �жϲ�����Ա����ά��
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "��ѡ�񵥾�");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			
			if("0".equals(shflag)||"".equals(shflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����û�б��������");
				return;
			}
			if("1".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����ѱ��������");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog();
			String meg = cadg.value;
			if(!flag){
				return;
			}
			
			newfkvo.setShrflag1("0");
			newfkvo.setCwflag("0");
			newfkvo.setVoperatorflag("0");
			newfkvo.setShrdate1(null);
			newfkvo.setCwdate(null);
			newfkvo.setShrid1(null);
			newfkvo.setCwid(null);
			newfkvo.setRet_date(this._getDate());
			newfkvo.setRet_user(this._getOperator());
			newfkvo.setQishenyuanyin(meg);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(meg);
			rtvo.setRet_type(1);
			rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
			rtvo.setRet_address("���񸶿��");			
			HYPubBO_Client.insert(rtvo);
			
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "���񲵻����");
		setListValue();
	}
}