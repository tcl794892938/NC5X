package nc.ui.dahuan.httreeqs;

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
		
		//����������������� nvl(fuzong_flag,0) = 0, vbillstatus = 1���� 2 ��ʱ������
		//1��Ԥ���Ժ�ys_flag=1,vbillstatus��8��Ϊ3,Ȼ������˹���vbillstatus��3��Ϊ2��
		//	���ʱ�򣬲���Ҫ���н��и���������nvl(fuzong_flag,0) = 0 ��vbillstatus��2��Ϊ1����������
		//2��vbillstatus��3��Ϊ2������Ҫ���������������ܻ�δ��˵�ʱ��nvl(fuzong_flag,0) = 0��vbillstatus = 2����������
		//��������������fuzong_flag=1,��vbillstatus��2��Ϊ1����������
		//���ҵ�ǰ��¼�û�Ϊ�������� vapproveid
		//nvl(vdef4,0) =1 ���������Ѿ����
		
//		���ۺ�ͬ���߲������
		//�ɹ���ͬ�����������˾����Ҫ�ߣ�������˾ֱ�Ӳ�����˽ڵ�
		String httype="";
		if(!_getCorp().getPk_corp().equals("1002")){//����������˾����Ҫ���˲ɹ���ͬ
			httype+= " and httype = 0 ";
		}
		condition = strWhere.toString() + " and nvl(vbillstatus,0) =2 and pk_corp='"+corpid+"' " 
											   + " and vapproveid = '"+user+"' "
											   + " and nvl(ys_flag,0) = 1 and pk_ysid is not null "
											   + " and nvl(is_seal,0) =0 "
											   + " and nvl(is_pay,0) = 0 "
											   + " and nvl(fuzong_flag,0) = 0 and nvl(vdef4,0)=1 "+httype;
							//fuzong_flag ������״̬ 0����δ��ˣ�1�������

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

	// ����
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		DhContractVO dhcVO = (DhContractVO)aggvo.getParentVO();
		if(null == dhcVO){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}		

		/*int ret = checkBillStatus(dhcVO);
		if(1!=ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "����", "�����ѱ�����������������");
			return;
		}*/

		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(dhcVO);

		if(flag){
			dhcVO.setVbillstatus(8);
			dhcVO.setYs_flag("0");
			dhcVO.setVdef4("0");
			dhcVO.setDapprovedate(null);
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
			rtvo.setRet_address("���ź�ͬ����");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���������");
			// ����ˢ��
			setListVO();
		}
	}
	
	/**
	 * ����״̬У��
	 * @return 1�����ݿ�����ˣ�2������״̬�����ı䣻3�����ݲ����ύ̬
	 * */
	private int checkBillStatus(DhContractVO vo) throws Exception{
		
		// SQL����ƴ�ӣ�����ѡ�񵥾ݵ������͵���״̬�жϷ��������ĵ��������Ƿ��ѡ�������һ��		
		String sql = " select count(1) from dh_contract where " +
						" pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"'";
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int value = (Integer)query.executeQuery(sql, new ColumnProcessor());
		
		// У����
		if(value != 1){
			return 2;
			//2��ʾ����״̬�����ı�
		}
		
		return 1;
		//1��ʾ���ݿ������
	}
	

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
		}
	}
		
	
}