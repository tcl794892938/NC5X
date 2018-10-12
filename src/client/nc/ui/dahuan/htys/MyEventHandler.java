package nc.ui.dahuan.htys;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

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
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	@Override
	protected void onBoCard() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = getBillManageUI().getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}
	
	public String condition = "";

	// ��ѯ��������
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		
		String user = this._getOperator();
		
		//���ۺ�ͬ����Ԥ��
		//�ɹ���ͬ�����������˾����Ҫ��Ԥ��������˾ֱ�Ӳ�����˽ڵ㣬����Ԥ��
		String httype="";
		if(!_getCorp().getPk_corp().equals("1002")){//����������˾����Ҫ���˲ɹ���ͬ
			httype+= " and httype = 0 ";
		}
		condition = strWhere.toString() + " and nvl(vbillstatus,0) in(2,3) " //�ύ
											   + " and pk_ysid = '"+user+"' "+httype
											   + " and nvl(vdef4,0)=0 " //������˱�־
											   + " and nvl(fuzong_flag,0)=0 and pk_corp='"+_getCorp().getPk_corp()+"'"; //������˱��
		
		setListVOs();
		
	}

	private void setListVOs() throws Exception{
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

	// ����
	@Override
	public void onBoAudit() throws Exception {
		DhContractVO[] dhcVOs;
		
		// �ж��б��ǿ�Ƭ
		if(getBillManageUI().isListPanelSelected()){
			dhcVOs = (DhContractVO[]) this.getBillManageUI().getBillListPanel().getHeadBillModel()
															.getBodySelectedVOs(DhContractVO.class.getName());

		}else{
			DhContractVO dhcVO = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			dhcVOs = new DhContractVO[]{ dhcVO };
		}
		
		if(0==dhcVOs.length){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		for(DhContractVO vo : dhcVOs){
			if(vo.getYs_flag().equals("1")){//��Ԥ��
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����Ѿ�Ԥ��ĵ��ݣ������ٴ�Ԥ��");
				return ;
			}
		}

		for(DhContractVO vo : dhcVOs){
			vo.setYs_flag("1");
			vo.setVbillstatus(2);
			vo.setRet_vemo(null);
			vo.setCmodifyid(null);
			vo.setTmodifytime(null);
			HYPubBO_Client.update(vo);
		}
		
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������");
		// ����ˢ��
		setListVOs();
		
	}
//	 ����
	@Override
	public void onBoCancelAudit() throws Exception {

		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
			
		DhContractVO dhcVO = (DhContractVO)aggvo.getParentVO();
		if(null==dhcVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		if(dhcVO.getYs_flag().equals("0")){//δԤ��
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������δԤ��");
			return ;
		}

		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(dhcVO);
		 
		if(flag){
			dhcVO.setYs_flag("0");
			dhcVO.setVbillstatus(8);
			dhcVO.setCmodifyid(this._getOperator());
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String dtsql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
			String tmdate = (String)iQ.executeQuery(dtsql, new ColumnProcessor());
			
			dhcVO.setTmodifytime(tmdate);
			HYPubBO_Client.update(dhcVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(dhcVO.getRet_vemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(dhcVO.getPk_contract());
			rtvo.setRet_address("��ͬԤ��");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����Ѳ���");
			// ����ˢ��
			setListVOs();
		}
		
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
		}
	}


	
	
	
}