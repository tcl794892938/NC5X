package nc.ui.dahuan.htinfo.htquery;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.htinfo.htquery.HtConLogoEntity;
import nc.vo.pub.SuperVO;


public class MyEventHandler extends AbstractMyEventHandler{
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == IdhButton.FILEUPLOAD){
			HtConLogoEntity htc = (HtConLogoEntity)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", htc.getHtcode());
		}
	}
	@Override
	protected void onBoQuery() throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		String pkUser = this._getOperator();
		String pkCorp = this._getCorp().getPrimaryKey();
		
		// 通查特殊处理
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"+pkCorp+"' and u.cuserid = '"+pkUser+"' " 
					+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHBG' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int retCot = (Integer)iQ.executeQuery(deptsql, new ColumnProcessor());
		
		String conf = "";
		if(0<retCot){
			conf = "";
		}else{
			conf = " and exists (select 1 from dh_contract where dh_conchange.pk_contract = dh_contract.pk_contract " +
					" and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+pkUser+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=dh_contract.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=dh_contract.ht_dept)))";
		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+conf);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	
	
}
