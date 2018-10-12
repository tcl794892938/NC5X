package nc.ui.dahuan.fkd;

import java.util.ArrayList;

import nc.ui.dahuan.billref.ReffksqSourceDLG;
import nc.ui.dahuan.queryref.RefFksqQueryDLG;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkd.DhFkbillBVO;
import nc.vo.dahuan.fkd.DhFkbillVO;
import nc.vo.dahuan.fksq.DhFksqbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends AbstractMyEventHandler {

	private AggregatedValueObject oldaggvo = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		oldaggvo = this.getBillCardPanelWrapper().getBillVOFromUI();
	}

	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		this.getBillUI().setUserObject(oldaggvo);
		super.onBoSave();

	}

	public void onBillRef() throws Exception {

		nc.vo.trade.pub.HYBillVO[] aggvos = queryToFksq();
		ArrayList list = new ArrayList();

		if (aggvos == null || aggvos.length <= 0)
			return;

		
		String pk_cust = "" ;//付款单位
		nc.vo.dahuan.fkd.MyBillVO aggvo = new nc.vo.dahuan.fkd.MyBillVO();
		DhFkbillVO newheadvo = new DhFkbillVO();
		for (int i = 0; i < aggvos.length; i++) {
			DhFkbillBVO bodyvo = new DhFkbillBVO();
			DhFksqbillVO headvo = (DhFksqbillVO) aggvos[i].getParentVO();
			
			 if(pk_cust.equalsIgnoreCase(""))
			  pk_cust = headvo.getPk_cust2();
			 else if(!pk_cust.equalsIgnoreCase(headvo.getPk_cust2())){
				 this.getBillUI().showErrorMessage("多合同付款时,付款单位必须一致!");
				 return;
			 }
			if (i == 0) {
				newheadvo.setDbilldate(this._getDate());
				newheadvo.setPk_corp(headvo.getPk_corp());
				newheadvo.setPk_cust2(pk_cust);
				newheadvo.setPk_fkfy(headvo.getPk_fkfs());
				newheadvo.setPk_busitype(headvo.getPk_busitype());
				newheadvo.setPk_billtype("fkdj");
				newheadvo.setVbillstatus(new Integer(8));
			}

			bodyvo.setCtcode(headvo.getCtcode());
			bodyvo.setVprojectname(headvo.getVprojectname());
			bodyvo.setDctjetotal(headvo.getDctjetotal());
			bodyvo.setDfkje(headvo.getDfkje());
			bodyvo.setDfkbl(headvo.getDfkbl());
			bodyvo.setVfkcontext(headvo.getVfkcontext());
			bodyvo.setVsourcebillrowid(headvo.getPk_fksqbill());
			bodyvo.setVlastbillid(headvo.getVlastbillid());
			list.add(bodyvo);
		}

		SuperVO[] bodyvos = (DhFkbillBVO[]) list.toArray(new DhFkbillBVO[0]);
		aggvo.setParentVO(newheadvo);
		aggvo.setChildrenVO(bodyvos);

		// 设置为新增处理
		getBillUI().setBillOperate(IBillOperate.OP_REFADD);
		// 填充界面

		AggregatedValueObject[] vos = new AggregatedValueObject[] { aggvo };
		super.setRefData(vos);

	}

	private nc.vo.trade.pub.HYBillVO[] queryToFksq() {

		// 上游单据节点号
		String srcFunNode = "1223"; // 付款计划节点
		// 业务类型暂定为空
		String businessType = null;
		// 查询模板ID
		String strQueryTemplateId = "1001AA1000000002S3XM";
		// 公司pk_corp
		String m_sCorpID = this._getCorp().getPk_corp();
		// 当前日期
		String m_sLogDate = this._getDate().toString();
		// 操作人员
		String m_sUserID = this._getOperator();
		// 初始化查询对话框
		RefFksqQueryDLG qcDLG = new RefFksqQueryDLG(this.getBillUI(), null,
				m_sCorpID, srcFunNode, m_sUserID, null);

		if (qcDLG == null) {
			this.getBillUI().showErrorMessage("获取查询条件时失败,可能无法获取查询模板!");
			return null;
		}

		if (qcDLG.showModal() == UIDialog.ID_OK) {
			String whereStr = qcDLG.getWhereSQL();
			if (whereStr == null || whereStr.length() <= 0) {
				whereStr = " 1=1 ";
			} else {
				whereStr = " " + whereStr + " ";
			}

			ReffksqSourceDLG billReferUI = new ReffksqSourceDLG(null,
					m_sCorpID, m_sUserID, srcFunNode, whereStr, "FKSQ",
					businessType, strQueryTemplateId, null, null, null, this
							.getBillUI());

			// 放入查询条件的DLG
			billReferUI.setQueyDlg(qcDLG);
			// 加载模版
			billReferUI.addBillUI();
			// 加载数据
			billReferUI.loadHeadData();
			if (billReferUI.showModal() == UIDialog.ID_OK) {
				nc.vo.trade.pub.HYBillVO[] rtnVOs = (nc.vo.trade.pub.HYBillVO[]) billReferUI
						.getRetVos();
				return rtnVOs;
			}
		}
		return null;
	}

}