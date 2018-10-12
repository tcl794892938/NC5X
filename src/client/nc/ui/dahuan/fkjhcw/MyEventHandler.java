package nc.ui.dahuan.fkjhcw;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	  String condition ="";
	  
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
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
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		
		condition = strWhere.toString() + "  and nvl(dh_fkjhbill.is_print,0) = 1 and nvl(dh_fkjhbill.is_pay,0) = 0 ";
		
		setListValue();
	}
	
	private void setListValue() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		
		if(intBtn == IdhButton.FKJHPAY){
			fkjhPay();
		}
	}
	
	// �������
	private void fkjhPay() throws Exception{

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		List<DhContractVO> htList = new ArrayList<DhContractVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// ��ǰ������
		String user = this._getOperator();
		// ��ǰʱ��
		UFDate date = this.getBillUI()._getServerTime().getDate();
		
		// �жϿ�Ƭ���滹���б�����
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				// �����Ƿ�ѡ
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// �жϵ����Ƿ��Ѵ�ӡ��δ����
					int isprint = newfkvo.getIs_print()==null?0:newfkvo.getIs_print();
					int ispay = newfkvo.getIs_pay()==null?0:newfkvo.getIs_pay();
					if(isprint != 1){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����δ��ӡ");
						return;
					}
					if(ispay == 1){
						MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����Ѹ���");
						return;
					}
					
					newfkvo.setIs_pay(1);
					newfkvo.setShrdate4(date);
					newfkvo.setShrid4(user);
					fkList.add(newfkvo);
					
					DhContractVO htvo = (DhContractVO)query.retrieveByPK(DhContractVO.class, newfkvo.getVsourcebillid());
					htvo.setIs_pay(1);
					
					// �ۼ�ʵ�ʸ���ͳ��
					UFDouble ljfk = htvo.getLjfkjhje()==null?new UFDouble(0):htvo.getLjfkjhje();
					UFDouble dffk = newfkvo.getDfkje()==null?new UFDouble(0):newfkvo.getDfkje();
					
					htvo.setLjfkjhje(ljfk.add(dffk));
					
					htList.add(htvo);
				
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
			
			// �жϵ����Ƿ��Ѵ�ӡ��δ����
			int isprint = newfkvo.getIs_print()==null?0:newfkvo.getIs_print();
			int ispay = newfkvo.getIs_pay()==null?0:newfkvo.getIs_pay();
			if(isprint != 1){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "����δ��ӡ");
				return;
			}
			if(ispay == 1){
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�����Ѹ���");
				return;
			}
			
			newfkvo.setIs_pay(1);
			newfkvo.setShrdate4(date);
			newfkvo.setShrid4(user);
			fkList.add(newfkvo);
			
			DhContractVO htvo = (DhContractVO)query.retrieveByPK(DhContractVO.class, newfkvo.getVsourcebillid());
			htvo.setIs_pay(1);
			
			// �ۼ�ʵ�ʸ���ͳ��
			UFDouble ljfk = htvo.getLjfkjhje()==null?new UFDouble(0):htvo.getLjfkjhje();
			UFDouble dffk = newfkvo.getDfkje()==null?new UFDouble(0):newfkvo.getDfkje();
			
			htvo.setLjfkjhje(ljfk.add(dffk));
			
			htList.add(htvo);

		}
		
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		iv.updateVOList(fkList);
		iv.updateVOList(htList);
		MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�������");
	
		setListValue();
	
	}
		
}