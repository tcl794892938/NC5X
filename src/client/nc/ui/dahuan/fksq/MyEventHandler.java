package nc.ui.dahuan.fksq;

import java.util.ArrayList;

import nc.ui.dahuan.billref.ReffkjhSourceDLG;
import nc.ui.dahuan.queryref.RefFkjhQueryDLG;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fksq.DhFksqbillVO;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	public void onBillRef() throws Exception {

		nc.vo.trade.pub.HYBillVO[] aggvos = queryToFkjh();
		ArrayList list = new ArrayList();

		if (aggvos == null || aggvos.length <= 0)
			return;

		nc.vo.dahuan.fksq.MyBillVO aggvo = new nc.vo.dahuan.fksq.MyBillVO();

		for (int i = 0; i < aggvos.length; i++) {
			DhFksqbillVO headvo = new DhFksqbillVO();
			DhFkjhbillVO headvo1 = (DhFkjhbillVO) aggvos[i].getParentVO();
			headvo.setCtcode(headvo1.getCtcode());
			headvo.setVprojectname(headvo1.getVprojectname());
			headvo.setDbilldate(this._getDate());
			headvo.setDctjetotal(headvo1.getDctjetotal());
			headvo.setPk_billtype("FKSQ");
			headvo.setPk_corp(headvo1.getPk_corp());
			headvo.setPk_cust2(headvo1.getPk_cust2());
			headvo.setPk_fkfs(headvo1.getPk_fkfs());
			headvo.setVbillstatus(new Integer(8));
			headvo.setVoperatorid(this._getOperator());
			headvo.setVsourcebillid(headvo1.getPk_fkjhbill());
			headvo.setVsourcebilltype(headvo1.getPk_billtype());
			headvo.setStatus(nc.vo.pub.VOStatus.NEW);
			list.add(headvo);
		}
		SuperVO[] vos = (DhFksqbillVO[]) list.toArray(new DhFksqbillVO[0]);
		SuperVO[] queryVos = vos;

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

	private nc.vo.trade.pub.HYBillVO[] queryToFkjh() {

		// ����չʾ�������ݵ� ���ս���(Ԥ���ӿ�,�������Զ������չʾ��)
		String referClassName = "";
		// ���ε��ݽڵ��
		String srcFunNode = "1203"; // ����ƻ��ڵ�
		// ҵ�������ݶ�Ϊ��
		String businessType = null;
		// ��ѯģ��ID
		String strQueryTemplateId = "0001AA1000000000ID9V";
		// ��˾pk_corp
		String m_sCorpID = this._getCorp().getPk_corp();
		// ��ǰ����
		String m_sLogDate = this._getDate().toString();
		// ������Ա
		String m_sUserID = this._getOperator();
		// ��ʼ����ѯ�Ի���
		RefFkjhQueryDLG qcDLG = new RefFkjhQueryDLG(this.getBillUI(), null,
				m_sCorpID, srcFunNode, m_sUserID, null);

		if (qcDLG == null) {
			this.getBillUI().showErrorMessage("��ȡ��ѯ����ʱʧ��,�����޷���ȡ��ѯģ��!");
			return null;
		}

		if (qcDLG.showModal() == UIDialog.ID_OK) {
			String whereStr = qcDLG.getWhereSQL();
			if (whereStr == null || whereStr.length() <= 0) {
				whereStr = " 1=1 ";
			} else {
				whereStr = " " + whereStr + " ";
			}

			ReffkjhSourceDLG billReferUI = new ReffkjhSourceDLG(null,
					m_sCorpID, m_sUserID, srcFunNode, whereStr, "FKJH",
					businessType, strQueryTemplateId, null, null, null, this
							.getBillUI());

			// �����ѯ������DLG
			billReferUI.setQueyDlg(qcDLG);
			// ����ģ��
			billReferUI.addBillUI();
			// ��������
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