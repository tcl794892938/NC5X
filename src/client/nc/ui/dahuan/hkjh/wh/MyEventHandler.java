package nc.ui.dahuan.hkjh.wh;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.dahuan.hkjh.HkwhDVO;
import nc.vo.dahuan.hkjh.HkwhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	protected void onBoSave() throws Exception {

		this.getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		
		super.onBoSave();
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			HkwhVO hvo = (HkwhVO)aggvo.getParentVO();
			HuiKuanDialog dlg = new HuiKuanDialog(this.getBillUI());
			dlg.showHuiKuanDialog(hvo, this._getCorp().getPk_corp(), this._getOperator(), this._getDate());
			super.onBoRefresh();
		}
	}

	
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			HkwhVO hvo = (HkwhVO)aggvo.getParentVO();
			HkwhDVO dvo = ((HkwhDVO[])aggvo.getChildrenVO())[0];
			
			/************************************** 更新合同档案主表  start  *********************************************/
			DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class,dvo.getPk_contract());
			UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
			ctvo.setLjfkjhje(ljfkjhje.sub(dvo.getCt_amount(), 2));
			HYPubBO_Client.update(ctvo);
			/************************************** 更新合同档案主表  end   *********************************************/
			
			/************************************** 更新回款  start  *********************************************/
			HkdhVO mvo = ((HkdhVO[])HYPubBO_Client.queryByCondition(HkdhVO.class, " hkbillno='"+hvo.getHkbillno()+"' and bill_flag=3 and sure_flag=1 "))[0];
			HYPubBO_Client.deleteByWhereClause(HkdhDVO.class, " pk_hkdh = '"+mvo.getPk_hkdh()+"'");
			HYPubBO_Client.delete(mvo);
			/************************************** 更新回款  end  *********************************************/
			
			HYPubBO_Client.delete(dvo);
			
			hvo.setSure_flag(0);
			hvo.setBzbl(null);
			hvo.setPk_bizong(null);
			hvo.setRmb_amount(null);
			HYPubBO_Client.update(hvo);//更新回款主表
			
			super.onBoRefresh();
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String corpid = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;
		
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";
		
		String sql = " select distinct dh_hkwh.* from dh_hkwh left join dh_hkwh_d on dh_hkwh.pk_hkwh = dh_hkwh_d.pk_hkwh and nvl(dh_hkwh_d.dr, 0) = 0 "+
			" where nvl(dh_hkwh.dr, 0) = 0 and not exists(select 1 from dh_hkdh where dh_hkdh.seal_flag=1 " +
			" and dh_hkdh.hkbillno=dh_hkwh.hkbillno) and  "+strWhere;
		
		List<HkwhVO> queryVos = (List<HkwhVO>)iQ.executeQuery(sql, new BeanListProcessor(HkwhVO.class));
		
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos.toArray(new HkwhVO[0]));
	
		updateBuffer();

	
	}

	
	
}