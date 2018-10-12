package nc.ui.dahuan.htcw;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
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
		String corpid = this._getCorp().getPrimaryKey();
		
		//��������
		//��������˾�Ĳɹ���ͬ���߲������
		condition = strWhere.toString() + " and nvl(vbillstatus,0) in(2,3) " 
		+ " and httype='1' and pk_corp<>'1002' and pk_corp='"+corpid+"' ";
		setListVO();
	}
	
	private void setListVO() throws Exception{
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

	// ���
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

		/*int ret = checkBillStatus(dhcVOs);
		if(2==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "����", "����״̬�����ı䣬��ˢ�º����");
			return;
		}*/
		
		// ��¼�û�
		String user = _getOperator();
		// ��ǰ����
		UFDate date = this._getDate();
		// ������˲���

		for(DhContractVO cvo : dhcVOs){
			cvo.setVbillstatus(1);
			//cvo.setVapproveid(user);
			//cvo.setDapprovedate(date);
			HYPubBO_Client.update(cvo);
		}
		
		// ����ˢ��
		setListVO();
		
	}
	
	/**
	 * ����״̬У��
	 * @return 1�����ݿ�����ˣ�2������״̬�����ı䣻
	 * */
	private int checkBillStatus(DhContractVO[] listVO) throws Exception{
		// ��ҪУ��ĵ�������
		int length = listVO.length;
		
		// SQL����ƴ�ӣ�����ѡ�񵥾ݵ������͵���״̬�жϷ��������ĵ��������Ƿ��ѡ�������һ��		
		String condition = "";		
		for(int i=0;i<length;i++){
			DhContractVO vo = listVO[i];
			if(i==0){
				condition = " and (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='8')";
			}else{
				condition += " or (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='8')";
			}
		}
		
		// ��ѯ���
		String sql = " select count(1) from dh_contract where 1=1 " + condition;
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int value = (Integer)query.executeQuery(sql, new ColumnProcessor());
		
		// У����
		if(value != length){
			return 2;
		}
		
		return 1;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT",dvo.getCtcode());
		}
		if(intBtn == IdhButton.RET_COMMIT){
				retCommit();
		}
	}	
	
//	 ����
	private void retCommit() throws Exception {
		AggregatedValueObject aggVO = this.getBufferData().getCurrentVO();
		if(null == aggVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		DhContractVO patVO = (DhContractVO)aggVO.getParentVO();
		if(null == patVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		String pk = patVO.getPrimaryKey();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		IVOPersistence iv = NCLocator.getInstance().lookup(IVOPersistence.class);
		DhContractVO newVO = (DhContractVO)query.retrieveByPK(DhContractVO.class, pk);
		// �ȽϽ����ϵĵ���״̬�����ݿ��еĵ���״̬
		if(patVO.getVbillstatus().intValue() != newVO.getVbillstatus().intValue()){
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "����״̬�ѷ����ı䣬��ˢ�º��ٲ���");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(newVO);
		
		if(flag){
			newVO.setVbillstatus(8);
			newVO.setCmodifyid(this._getOperator());
			
			String dtsql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
			String tmdate = (String)query.executeQuery(dtsql, new ColumnProcessor());
			newVO.setTmodifytime(tmdate);
			
			iv.updateVO(newVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(newVO.getRet_vemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(newVO.getPk_contract());
			rtvo.setRet_address("�������ȷ��");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���سɹ�");
			setListVO();
		}
	}
	
}