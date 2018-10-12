package nc.ui.dahuan.billref;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.WindowConstants;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.query.HYBillSourceDLG;
import nc.vo.bfriend.pub.BfStringUtil;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;

public class RefdhhtSourceDLG extends HYBillSourceDLG {

	public RefdhhtSourceDLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
	}

	public RefdhhtSourceDLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);
		initialize();
	}
	
	private void initialize() {

		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		setSize(scrSize.width, scrSize.height);
		setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114")/*@res "单据的参照界面"*/);
		setContentPane(getUIDialogContentPane());

		//获取单据对应的单据vo名称
		getBillVO();
	}

	public void getBillVO() {
		m_billVo = "nc.vo.trade.pub.HYBillVO";
		m_billHeadVo = "nc.vo.dahuan.ctbill.DhContractVO";
		m_billBodyVo = "nc.vo.dahuan.ctbill.DhContractVO";
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet("0001AA1000000000GXTK");

				// 进行主子隐藏列的判断
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

	@Override
	public void loadHeadData() {

		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and ("
							+ getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo = qryHeadAllData(tmpWhere);

			if (getbillListPanel().getBillListData().isMeataDataTemplate())
				getbillListPanel().getBillListData()
						.setHeaderValueObjectByMetaData(tmpHeadVo);
			else
				getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000237")/* @res "错误" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000490")/* @res "数据加载失败！" */);
		}

	}

	public CircularlyAccessibleValueObject[] qryHeadAllData(String where) {

		IdhServer pfserver = NCLocator.getInstance().lookup(IdhServer.class);
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		String strSql = " select  dh_contract.*  from dh_contract where nvl(dh_contract.dr,0) = 0 and dh_contract.vbillstatus = 1 ";

		if (where != null && where.trim().length() > 0)
			strSql = strSql + " AND " + where;

		strSql = strSql
				+ " order by  dh_contract.vbillno,dh_contract.dbilldate ";
		DhContractVO[] billHs = null;
		ArrayList list = new ArrayList();

		try {
			list = (ArrayList) iQ.executeQuery(strSql, new BeanListProcessor(
					DhContractVO.class));
			if (list.size() > 0) {
				billHs = (DhContractVO[]) list.toArray(new DhContractVO[0]);
			}

			/**
			 * ******************************* 查询合同实际付款金额wanglong
			 * ***********************
			 */
			String[] htcodes = new String[billHs.length];
			for (int i = 0; i < billHs.length; i++) {
				htcodes[i] = billHs[i].getCtcode();
			}
			String strwhere = BfStringUtil.getWherePartByKeys(
					"TEMQ_CT.jobname ", htcodes, false);
			HtzxReportVO[] htzxvos = pfserver.queryHtzxVo(strwhere);
			HashMap htzxmap = new HashMap();
			ArrayList listvos = new ArrayList();
			if (null != htzxvos && htzxvos.length > 0) {
				for (int i = 0; i < htzxvos.length; i++) {
					htzxmap.put(htzxvos[i].getJobname(), htzxvos[i]);
				}

			}

			for (int i = 0; i < billHs.length; i++) {
				HtzxReportVO htzxvo = (HtzxReportVO) htzxmap.get(billHs[i]
						.getCtcode());
				UFDouble ufsjfk = new UFDouble(0.00);
				UFDouble ufsjsp = new UFDouble(0.00);
				if (htzxvo != null) {
					ufsjfk = htzxvo.getSjfk();// 实际付款
					ufsjsp = htzxvo.getSjsp();// 实际收票
				}

				if (htzxvo != null) {
					UFDouble ufsjsk = htzxvo.getSjsk() == null ? new UFDouble(
							0.00) : htzxvo.getSjsk();// 实际收款
					if (billHs[i].getCtcode().endsWith("-00")) {
						billHs[i].setLjskje(ufsjsk);

					}
				}
				UFDouble ufhtje = billHs[i].getDctjetotal() == null ? new UFDouble(
						0.00)
						: billHs[i].getDctjetotal();
				if (ufhtje.doubleValue() > ufsjfk.doubleValue()) {
					
					billHs[i].setLjsjfkje(ufsjfk);
					billHs[i].setSjfkbl(ufsjfk.div(ufhtje).multiply(new UFDouble(100), 2));
					billHs[i].setSjspje(ufsjsp); //by tcl
					listvos.add(billHs[i]);
				}
			}

			/**
			 * ******************************* end查询合同实际付款金额
			 * ***********************
			 */

			if (listvos.size() > 0) {
				billHs = (DhContractVO[]) listvos.toArray(new DhContractVO[0]);
			}

		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return billHs;

	}
	
}
