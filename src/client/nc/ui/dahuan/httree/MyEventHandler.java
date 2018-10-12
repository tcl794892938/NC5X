package nc.ui.dahuan.httree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhServer;
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
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends AbstractMyEventHandler{

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
		
		//����������ʱ�����ύ̬����Ԥ��vbillstatus = 3 
		// �����Զ������ŵĵ������⣬�ʴ˲��ò������ܵ�ֵУ�飬���Ǹ��ݲ���
		//�� Ԥ���־ ys_flag Ϊ1 ��û��Ԥ��ĺ�ͬ ys_flag ״̬Ϊ0�����Ҳ�Ϊnull
        //������˱�־ nvl(fuzong_flag,0) = 0
		//���±�־is_sealΪ0�������־is_payΪ0
		//��ǰ�������������(��������������ж�)nvl(vdef4,0)=0
//		vdef4��ʾ������˱��
//		���ۺ�ͬ���߲������
		//�ɹ���ͬ�����������˾����Ҫ�ߣ�������˾ֱ�Ӳ�����˽ڵ�
		String httype="";
		if(!_getCorp().getPk_corp().equals("1002")){//����������˾����Ҫ���˲ɹ���ͬ
			httype+= " and dh_contract.httype = 0 ";
		}
		condition = strWhere.toString() + " and nvl(dh_contract.vbillstatus,0) =2 and nvl(dh_contract.ys_flag,0) = 1 "
										+ " and dh_contract.pk_ysid is not null and nvl(vdef4,0)=0 and nvl(dh_contract.fuzong_flag,0) = 0 " 
										+ " and nvl(dh_contract.is_seal,0) =0 and nvl(dh_contract.is_pay,0) = 0 "
//										+ " and (dh_contract.ht_dept in (select vd.pk_deptdoc from v_deptperonal vd "
//										+ " where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"') "
//										+ " or dh_contract.pk_deptdoc in (select vd.pk_deptdoc from v_deptperonal vd "
//										+ "  where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"')) ";
										+ " and dh_contract.pk_deptdoc in (select vd.pk_deptdoc from v_deptperonal vd "
										+ " where vd.pk_corp = '"+corpid+"' and vd.pk_user = '"+user+"')  "+httype ;

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
		
		// �ж��б����ǿ�Ƭ
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
		if(3==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "����", "�����ѱ������������������");
			return;
		}else if(2==ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "����", "����״̬�����ı䣬��ˢ�º����");
			return;
		}*/
		
		// ��¼�û�
		String user = _getOperator();
		// ��ǰ����
		UFDate date = this._getDate();
		// ������˲���
		NCLocator.getInstance().lookup(IdhServer.class).AuditDhhtbmzg(isManagerAudit(dhcVOs),user,date);
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������");
		// ����ˢ��
		setListVO();
		
	}
	
	/**
	 * ����״̬У��
	 * @return 1�����ݿ�����ˣ�2������״̬�����ı䣻3�����ݲ����ύ̬
	 * */
	private int checkBillStatus(DhContractVO[] listVO) throws Exception{
		// ��ҪУ��ĵ�������
		int length = listVO.length;
		
		// SQL����ƴ�ӣ�����ѡ�񵥾ݵ������͵���״̬�жϷ��������ĵ��������Ƿ��ѡ�������һ��		
		String condition = "";		
		for(int i=0;i<length;i++){
			DhContractVO vo = listVO[i];
			if(3 != vo.getVbillstatus().intValue()&&2 != vo.getVbillstatus().intValue()){
				return 3;
			}
			if(i==0){
				condition = " and (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"')";
			}else{
				condition += " or (pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"')";
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
	
	/** �Ƿ���Ҫ��������ж� 
	 * @return Map�м�aaa��ʾ��Ҫ������˵ĺ�ͬ������bbb��ʾ����Ҫ������˵ĺ�ͬ��
	 **/
	private Map<String,List<DhContractVO>> isManagerAudit(DhContractVO[] listVO){
		Map<String,List<DhContractVO>> map = new HashMap<String, List<DhContractVO>>();
		for(DhContractVO vo : listVO){
			// �жϺ�ͬ����:���ۺ�ͬ(0)���ɹ���ͬ(1)�����ͬ(2)
			int httype = vo.getHttype();
			if(2 == httype){
				map = setManagerMap(map,vo,"bbb");
			}else if(1 == httype){
				// �ɹ���ͬ���ݲɹ�����ж�,����300����Ҫ����ǩ��
				UFDouble cgt = vo.getDcaigtotal();
				if(cgt.compareTo(new UFDouble("2999999.99"))==1){
					map = setManagerMap(map,vo,"aaa");
				}else{
					map = setManagerMap(map,vo,"bbb");
				}
			}else if(0 == httype){
				boolean flag1 = !(vo.getCtcode().contains("��") || vo.getCtcode().contains("ĥ��"));
				boolean flag2 = vo.getCtcode().contains("ĥ��") && vo.getDsaletotal().compareTo(new UFDouble("2999999.99"))==1;
				if(flag1 || flag2){
					map = setManagerMap(map,vo,"aaa");
				}else{
					map = setManagerMap(map,vo,"bbb");
				}
			}
		}
		return map;
		
	}
	
	private Map<String,List<DhContractVO>> setManagerMap(Map<String,List<DhContractVO>> map,DhContractVO vo,String value){
		if(map.containsKey(value)){
			List<DhContractVO> dhListVO = map.get(value);
			dhListVO.add(vo);
			map.put(value, dhListVO);
		}else{
			List<DhContractVO> dhListVO = new ArrayList<DhContractVO>();
			dhListVO.add(vo);
			map.put(value, dhListVO);
		}
		return map;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
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
			MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "����״̬�ѷ����ı䣬��ˢ�º��ڲ���");
			return;
		}
		
		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(newVO);
		
		if(flag){
			newVO.setVbillstatus(8);
			newVO.setYs_flag("0");
			newVO.setDapprovedate(null);
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
			rtvo.setRet_address("���ź�ͬ���");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���سɹ�");
			setListVO();
		}
	}	
	
}