package nc.ui.dahuan.fkjhzjl;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	public String condition = "";

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		// ����������ӹ�������
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		condition = strWhere.toString()
				+ " and nvl(dh_fkjhbill.is_single,0)=0 "
				+ " and dh_fkjhbill.shrflag2 = '1' and nvl(dh_fkjhbill.is_pay,0)=0 and nvl(dh_fkjhbill.is_print,0)=0"
				+ " and exists (select v_deptperonal.pk_deptdoc from v_deptperonal "
				+ " where v_deptperonal.pk_user = '" + user
				+ "' and v_deptperonal.pk_corp = '" + pkCorp + "' "
				+ " and (v_deptperonal.pk_deptdoc=dh_fkjhbill.pk_dept or "
				+ " v_deptperonal.pk_deptdoc=dh_fkjhbill.fk_dept))";

		setListVO();
	}

	private void setListVO() throws Exception {
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	// ȫѡ
	@Override
	protected void onBoSelAll() throws Exception {

		getBillManageUI().getBillListPanel().getParentListPanel()
				.selectAllTableRow();
	}

	// ȫ��
	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel()
				.cancelSelectAllTableRow();
	}

	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	@Override
	protected void onBoPrint() throws Exception {

		String pkFkjhbill = "";

		if (getBillManageUI().isListPanelSelected()) {

			DhFkjhbillVO[] vos = (DhFkjhbillVO[]) getBillManageUI()
					.getBillListPanel().getHeadBillModel().getBodySelectedVOs(
							DhFkjhbillVO.class.getName());
			if (vos.length < 1) {
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ", "�빴ѡ����");
				return;
			} else if (vos.length > 5) {
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
						"һ���Դ�ӡ���ó���5��");
				return;
			}

			int hdrows = getBillManageUI().getBillListPanel()
					.getHeadBillModel().getRowCount();
			for (int i = 0; i < hdrows; i++) {
				int seld = getBillManageUI().getBillListPanel()
						.getHeadBillModel().getRowState(i);
				if (4 == seld) {
					this.getBillManageUI().getBillListPanel().getHeadTable()
							.setRowSelectionInterval(i, i);
					break;
				}
			}
			String pk_user=_getOperator();
			// �Ƶ�����
			UFDate billdate = vos[0].getDbilldate();
			// ���λ
			String saycorp = vos[0].getPk_cust2() == null ? "" : vos[0]
					.getPk_cust2();
			// ��������
			String bankname = vos[0].getPk_bank() == null ? "" : vos[0]
					.getPk_bank();
			// �����˺�
			String sayno = vos[0].getSay_no() == null ? "" : vos[0].getSay_no();
			// ��˾
			String corpname = vos[0].getPk_corp() == null ? "" : vos[0]
					.getPk_corp();
			// ���첿��
			String deptname = vos[0].getPk_dept() == null ? "" : vos[0]
					.getPk_dept();
			// ������
			String deptsaleman = vos[0].getVoperatorid() == null ? "" : vos[0]
					.getVoperatorid();
			// ��������
			String deptmaster = vos[0].getShrid1() == null ? "" : vos[0]
					.getShrid1();
			// �ֹܾ���
			String corpmaster = vos[0].getShrid2() == null ? "" : vos[0]
					.getShrid2();

			// ����
			String pkCw = vos[0].getCwid() == null ? "" : vos[0].getCwid();

			String fkpkcon = "";

			for (DhFkjhbillVO vo : vos) {
				int ispay = vo.getIs_pay() == null ? 0 : vo.getIs_pay();
				pkFkjhbill = vo.getPk_fkjhbill();

				if (!pk_user.equals(vo.getVoperatorid() == null ? "" : vo
						.getVoperatorid())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"�������Ϊ�Ƶ��˲��������ɰ�");
					return;
				}
				String date = vo.getDbilldate().toString().substring(0, 7);
				if (!date.equals(billdate.toString().substring(0, 7))) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"�󶨵��ݲ���ͬһ�����Ƶ������ɰ�");
					return;
				}
				if (!corpname.equals(vo.getPk_corp() == null ? "" : vo
						.getPk_corp())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"���������˾��ͬ�����ɰ�");
					return;
				}
				if (!corpmaster.equals(vo.getShrid2() == null ? "" : vo
						.getShrid2())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"����ֹܾ���ͬ�����ɰ�");
					return;
				}
				if (!deptname.equals(vo.getPk_dept() == null ? "" : vo
						.getPk_dept())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"����Ƶ����Ų�ͬ�����ɰ�");
					return;
				}
				if (!deptsaleman.equals(vo.getVoperatorid() == null ? "" : vo
						.getVoperatorid())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"������������˲�ͬ�����ɰ�");
					return;
				}
				if (!deptmaster.equals(vo.getShrid1() == null ? "" : vo
						.getShrid1())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"����������ܲ�ͬ�����ɰ�");
					return;
				}
				if (!saycorp.equals(vo.getPk_cust2() == null ? "" : vo
						.getPk_cust2())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"������λ��ͬ�����ɰ�");
					return;
				}
				if (!bankname.equals(vo.getPk_bank() == null ? "" : vo
						.getPk_bank())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"����������в�ͬ�����ɰ�");
					return;
				}
				if (!sayno.equals(vo.getSay_no() == null ? "" : vo.getSay_no())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"��������˺Ų�ͬ�����ɰ�");
					return;
				}

				if (!pkCw.equals(vo.getCwid() == null ? "" : vo.getCwid())) {
					MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
							"���˵Ĳ���Ա��ͬ�����ɰ�");
					return;
				}

				fkpkcon += "'" + vo.getPk_fkjhbill() + "',";
			}
			fkpkcon = fkpkcon.substring(0, fkpkcon.length() - 1);

			String sql = "select pk_fkjhprint,count(1) cot from dh_fkjhbill where pk_fkjhbill in ("
					+ fkpkcon + ") group by pk_fkjhprint";

			IUAPQueryBS query = NCLocator.getInstance().lookup(
					IUAPQueryBS.class);
			List<Map<String, String>> ml = (List<Map<String, String>>) query
					.executeQuery(sql, new MapListProcessor());
			if (null == ml && ml.size() < 1) {
				MessageDialog.showErrorDlg(getBillManageUI(), "����", "����ƻ���������");
				return;
			} else if (ml.size() > 1) {
				MessageDialog.showHintDlg(getBillManageUI(), "��ʾ",
						"��ѡ�ĸ���д������������Ӵ�ӡ");
				return;
			} else {
				String pkfkpt = ml.get(0).get("pk_fkjhprint") == null ? "" : ml
						.get(0).get("pk_fkjhprint");
				if ("".equals(pkfkpt)) {
					NCLocator.getInstance().lookup(IdhServer.class).BoundPrint(
							vos, fkpkcon, "", _getOperator(), vos[0].getDbilldate());
				}
			}

		} else {
			DhFkjhbillVO fkvo = (DhFkjhbillVO) this.getBufferData()
					.getCurrentVO().getParentVO();
			String pkprint = fkvo.getPk_fkjhprint() == null ? "" : fkvo
					.getPk_fkjhprint();
			pkFkjhbill = fkvo.getPk_fkjhbill();

			if ("".equals(pkprint)) {
				DhFkjhbillVO[] vos = new DhFkjhbillVO[] { fkvo };
				String pkfkvon = fkvo.getPk_fkjhbill();
				NCLocator.getInstance().lookup(IdhServer.class).BoundPrint(vos,
						"'" + pkfkvon + "'", "", _getOperator(), vos[0].getDbilldate());
			}

		}

		// String nodeCode = getBillManageUI()._getModuleCode();
		// String pkcorp = this._getCorp().getPrimaryKey();
		// String pkuser = this._getOperator();
		//		
		// IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, pkFkjhbill);
		// PrintEntry print = new PrintEntry(null, dataSource);
		// print.setTemplateID(pkcorp, nodeCode, pkuser, null,null);
		// if(print.selectTemplate() == 1)
		// print.preview();

		setListVO();
	}

}