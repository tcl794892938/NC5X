package nc.ui.dahuan.fkjhywdy;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
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

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		String pkuser = this._getOperator();
		String pkcorp = this._getCorp().getPrimaryKey();
		
		
		
		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		//����󶨲�ѯ�������ǹ��̲��Ѿ���ӡ���� is_print = 1
		String strCondition = " select distinct dh_fkbdprint.* from dh_fkbdprint,dh_fkbdprint_d where dh_fkbdprint.pk_fkbd = dh_fkbdprint_d.pk_fkbd " +
					"  and nvl(dh_fkbdprint_d.dr,0)=0 and nvl(dh_fkbdprint.dr,0)=0 " 
		+" and (exists (select 1 from dh_fkjhbill where dh_fkjhbill.voperatorid = '"+pkuser+"' "
        +" and dh_fkjhbill.pk_corp = '"+pkcorp+"' and dh_fkjhbill.pk_fkjhprint = dh_fkbdprint.pk_fkprint and dh_fkjhbill.is_print = 2 and "+strWhere+") "
        +" or exists (select 1 from dh_fkjhbill where dh_fkjhbill.voperatorid = '"+pkuser+"' "
        +" and dh_fkjhbill.pk_corp = '"+pkcorp+"' and dh_fkjhbill.pk_fkjhbill = dh_fkbdprint.pk_fkjhbill and dh_fkjhbill.is_print = 3 and "+strWhere+" ))";		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<DhFkbdprintVO> volit = (List<DhFkbdprintVO>)iQ.executeQuery(strCondition, new BeanListProcessor(DhFkbdprintVO.class));
		
		getBufferData().clear();
		// �������ݵ�Buffers
		addDataToBuffer(volit.toArray(new DhFkbdprintVO[0]));

		updateBuffer();
	}

	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf) throws Exception {

		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}
		//��д strWhere�Ĳ�ѯ��ʽ
		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
		}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoPrint();
		
		BillManageUI dyui = (BillManageUI)this.getBillUI();
		PrintEntry print = new PrintEntry(dyui);
		
		String nodeCode = dyui._getModuleCode();
		String pkcorp = this._getCorp().getPrimaryKey();
		String pkuser = this._getOperator();
        print.setTemplateID(pkcorp, nodeCode, pkuser, null,null);
		
        
		if(dyui.isListPanelSelected()){
			
			DhFkbdprintVO[] vos = (DhFkbdprintVO[])dyui.getBillListPanel().getHeadBillModel().getBodySelectedVOs(DhFkbdprintVO.class.getName());
			for(DhFkbdprintVO dyvo : vos){
				
				if(null == dyvo.getPk_fkjhbill() || "".equals(dyvo.getPk_fkjhbill())){
					DhFkjhbillVO[] list = (DhFkjhbillVO[])HYPubBO_Client.queryByCondition(DhFkjhbillVO.class, " pk_fkjhprint = '"+dyvo.getPk_fkprint()+"' ");
					for(DhFkjhbillVO fkjhbill : list){
						fkjhbill.setIs_print(1);
						fkjhbill.setShrdate3(_getDate());
						HYPubBO_Client.update(fkjhbill);
					}
				}else{
					DhFkjhbillVO jvo = (DhFkjhbillVO)HYPubBO_Client.queryByPrimaryKey(DhFkjhbillVO.class, dyvo.getPk_fkjhbill());
					jvo.setIs_print(1);
					jvo.setShrdate3(_getDate());
					HYPubBO_Client.update(jvo);
				}
				
				IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, dyvo);
		        
		        print.setDataSource(dataSource);
			}
			
		}else{
			DhFkbdprintVO fkvo = (DhFkbdprintVO)this.getBufferData().getCurrentVO().getParentVO();
			if(null == fkvo.getPk_fkjhbill() || "".equals(fkvo.getPk_fkjhbill())){
				DhFkjhbillVO[] list = (DhFkjhbillVO[])HYPubBO_Client.queryByCondition(DhFkjhbillVO.class, " pk_fkjhprint = '"+fkvo.getPk_fkprint()+"' ");
				for(DhFkjhbillVO fkjhbill : list){
					fkjhbill.setIs_print(1);
					fkjhbill.setShrdate3(_getDate());
					HYPubBO_Client.update(fkjhbill);
				}
			}else{
				DhFkjhbillVO jvo = (DhFkjhbillVO)HYPubBO_Client.queryByPrimaryKey(DhFkjhbillVO.class, fkvo.getPk_fkjhbill());
				jvo.setIs_print(1);
				jvo.setShrdate3(_getDate());
				HYPubBO_Client.update(jvo);
			}
			
			IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, fkvo);
	        
	        print.setDataSource(dataSource);
		}
		if(print.selectTemplate()==1){
			print.preview();
		}
	}
	
	
		
}