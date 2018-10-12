package nc.ui.dahuan.fkbdprint;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
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
		String corpname = this._getCorp().getUnitname();
		
		
		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		//����󶨲�ѯ�������ǹ��̲��Ѿ���ӡ���� is_print = 1
		String strCondition = " select distinct dh_fkbdprint.* from dh_fkbdprint,dh_fkbdprint_d where dh_fkbdprint.pk_fkbd = dh_fkbdprint_d.pk_fkbd " +
					"  and nvl(dh_fkbdprint_d.dr,0)=0 and nvl(dh_fkbdprint.dr,0)=0 and dh_fkbdprint.corp_name like '%"+corpname+"%' and " 
			+strWhere;
		
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
				IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, dyvo);
		        
		        print.setDataSource(dataSource);
			}
			
		}else{
			DhFkbdprintVO fkvo = (DhFkbdprintVO)this.getBufferData().getCurrentVO().getParentVO();
			
			IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, fkvo);
	        
	        print.setDataSource(dataSource);
		}
		
		if(print.selectTemplate()==1){
			print.preview();
		}
	}
	
	
		
}