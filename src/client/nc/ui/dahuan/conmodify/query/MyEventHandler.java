package nc.ui.dahuan.conmodify.query;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.conmodqry.ConModQueryVO;
import nc.vo.dahuan.contractmodify.ConModfiyVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
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

		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		
		int month = this._getDate().getMonth();
		int year = this._getDate().getYear();
		String stdate = "";
		if(month>6){
			
			int stmonth = month-6;
			if(stmonth>9){
				stdate = year+"-"+stmonth+"-01";
			}else{
				stdate = year+"-0"+stmonth+"-01";
			}
		}else{
			int styear = year-1;
			int stmonth = 6+month;
			if(stmonth>9){
				stdate = styear+"-"+stmonth+"-01";
			}else{
				stdate = styear+"-0"+stmonth+"-01";
			}
		}

		sqlWhereBuf.append(strWhere+" and ((vbg_contract.modify_status<>3) or (vbg_contract.modify_status=3 and vbg_contract.zddate>'"+stdate+"')) ");
		return true;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn==IdhButton.FILEUPLOAD){
			AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
			if(null == aggvo){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
				return;
			}
			ConModQueryVO cqvo = (ConModQueryVO)aggvo.getParentVO();
			ConModfiyVO cmvo = (ConModfiyVO)HYPubBO_Client.queryByPrimaryKey(ConModfiyVO.class, cqvo.getPk_contractmodify());
			DhContractVO dvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, cmvo.getParent_contractid());
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
			
		}
	}
	
}
