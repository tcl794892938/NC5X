package nc.ui.dahuan.billref;


import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.query.HYBillSourceDLG;

public class ReffksqSourceDLG extends HYBillSourceDLG {

	public ReffksqSourceDLG(String pkField, String pkCorp,
			String operator, String funNode, String queryWhere,
			String billType, String businessType, String templateId,
			String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
	}

	public ReffksqSourceDLG(String pkField, String pkCorp,
			String operator, String funNode, String queryWhere,
			String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);
	}

	public void getBillVO() {
		m_billVo = "nc.vo.trade.pub.HYBillVO";
		m_billHeadVo = "nc.vo.dahuan.fksq.DhFksqbillVO";
		m_billBodyVo = "nc.vo.dahuan.fksq.DhFksqbillVO";
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet("0001AA1000000000HNA8");
				
				//进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}
				ivjbillListPanel.setMultiSelect(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}


	public Class getBodyVOClass() {
		return null;
	}
	

}
