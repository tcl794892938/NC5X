package nc.ui.dahuan.fkjhfd;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkjh.DhFkjhSingleVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkjh.DhFkjhprintVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintDVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
import nc.vo.pub.AggregatedValueObject;
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

		// �����������ӹ�������
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		condition = strWhere.toString()
				+ " and dh_fkjhbill.is_single=1 "
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
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			DhFkjhbillVO fkvo = (DhFkjhbillVO) aggvo.getParentVO();
			fkvo = (DhFkjhbillVO) HYPubBO_Client.queryByPrimaryKey(
					DhFkjhbillVO.class, fkvo.getPk_fkjhbill());

			// �ֵ���ӡ
			DhFkjhSingleVO[] singlevos = (DhFkjhSingleVO[]) HYPubBO_Client
					.queryByCondition(DhFkjhSingleVO.class, " pk_fkjh = '"
							+ fkvo.getPk_fkjhbill() + "' ");
			DhFkjhbillVO[] nvos = new DhFkjhbillVO[singlevos.length];
			for (int i = 0; i < singlevos.length; i++) {
				DhFkjhbillVO jvo = (DhFkjhbillVO) fkvo.clone();
				jvo.setDfkje(singlevos[i].getSingle_amount());
				nvos[i] = jvo;
			}

			shengchengVO(nvos, _getDate());

			fkvo.setIs_print(3);
			fkvo.setShrid3(_getOperator());
			fkvo.setShrdate3(_getDate());
			HYPubBO_Client.update(fkvo);
			setListVO();
		}
	}

	private void shengchengVO(DhFkjhbillVO[] vos, UFDate date) throws Exception {

		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		for (DhFkjhbillVO vo : vos) {
			String pkcorp = vo.getPk_corp() == null ? "" : vo.getPk_corp();
			String pkdept = vo.getFk_dept() == null ? "" : vo.getFk_dept();
			String voperid = vo.getVoperatorid() == null ? "" : vo
					.getVoperatorid();
			String shrid = vo.getShrid1() == null ? "" : vo.getShrid1();
			String pkcust = vo.getPk_cust2() == null ? "" : vo.getPk_cust2();
			String pkbank = vo.getPk_bank() == null ? "" : vo.getPk_bank();
			String nsay = vo.getSay_no() == null ? "" : vo.getSay_no();
			String pkfz = vo.getShrid2() == null ? "" : vo.getShrid2();
			String pk_fkjhbill = vo.getPk_fkjhbill() == null ? "" : vo
					.getPk_fkjhbill();
			String pk_cwid = vo.getCwid() == null ? "" : vo.getCwid();

			DhFkjhprintVO pvo = new DhFkjhprintVO();
			DhFkbdprintVO bdvo = new DhFkbdprintVO();
			DhFkbdprintDVO bdcvo = new DhFkbdprintDVO();

			String corpSQL = "select t.unitname from bd_corp t where t.pk_corp = '"
					+ pkcorp + "'";
			String corpname = (String) iQ.executeQuery(corpSQL,
					new ColumnProcessor());
			pvo.setCorp_name(corpname);
			bdvo.setCorp_name(corpname);

			String depetSQL = "select t.deptname from bd_deptdoc t where t.pk_deptdoc = '"
					+ pkdept + "'";
			String deptname = (String) iQ.executeQuery(depetSQL,
					new ColumnProcessor());
			pvo.setDept_name(deptname);
			bdvo.setDept_name(deptname);

			String userSQL = "select t.user_name from sm_user t where t.cuserid = '"
					+ voperid + "'";
			String username = (String) iQ.executeQuery(userSQL,
					new ColumnProcessor());
			pvo.setDept_saleman(username);
			bdvo.setDept_saleman(username);

			String masterSQL = "select t.user_name from sm_user t where t.cuserid = '"
					+ shrid + "'";
			String mastername = (String) iQ.executeQuery(masterSQL,
					new ColumnProcessor());
			pvo.setDept_master(mastername);
			bdvo.setDept_master(mastername);

			String fzSQL = "select t.user_name from sm_user t where t.cuserid = '"
					+ pkfz + "'";
			String fzname = (String) iQ.executeQuery(fzSQL,
					new ColumnProcessor());
			pvo.setFzname(fzname);
			bdvo.setFzname(fzname);

			String cuSQL = "select c.custname from bd_cubasdoc c,bd_cumandoc t "
					+ "where c.pk_cubasdoc = t.pk_cubasdoc and t.pk_cumandoc='"
					+ pkcust + "'";
			String cusname = (String) iQ.executeQuery(cuSQL,
					new ColumnProcessor());
			pvo.setSay_corp(cusname);
			bdvo.setSay_corp(cusname);

			// ����
			String cwSQL = "select t.user_name from sm_user t where t.cuserid = '"
					+ pk_cwid + "'";
			String cwName = (String) iQ.executeQuery(cwSQL,
					new ColumnProcessor());
			pvo.setCwname(cwName);
			bdvo.setCwname(cwName);

			pvo.setSay_bank(pkbank);
			bdvo.setSay_bank(pkbank);

			pvo.setSay_no(nsay);
			bdvo.setSay_no(nsay);

			pvo.setPrint_date(date);
			bdvo.setPrint_date(date);

			bdvo.setPk_fkjhbill(pk_fkjhbill);

			String saycon = "";
			if (vo.getIffknr1().booleanValue()) {
				saycon = "��ͬԤ����";
			} else if (vo.getIffknr2().booleanValue()) {
				saycon = "��ͬ������";
			} else if (vo.getIffknr3().booleanValue()) {
				saycon = "��ͬβ��";
			} else if (vo.getIffknr4().booleanValue()) {
				saycon = "��������";
			} else if (vo.getIffknr5().booleanValue()) {
				saycon = "ȫ���";
			} else if (vo.getIffknr6().booleanValue()) {
				saycon = "��ͬ���Կ�";
			} else {
				saycon = "";
			}

			String balSQL = "select t.balanname from bd_balatype t where t.pk_balatype='"
					+ vo.getPk_fkfs() + "'";
			String saytype = (String) iQ.executeQuery(balSQL,
					new ColumnProcessor());

			pvo.setHtcode1(vo.getCtcode());
			pvo.setHtamount1(vo.getDfkje());
			pvo.setSay_content1(saycon);
			pvo.setSay_type1(saytype);
			bdvo.setHtcode1(vo.getCtcode());
			bdvo.setHtamount1(vo.getDfkje());
			bdvo.setSay_content1(saycon);
			bdvo.setSay_type1(saytype);

			pvo.setAmount_sum(vo.getDfkje());
			bdvo.setAmount_sum(vo.getDfkje());

			bdcvo.setHtcode(vo.getCtcode());
			bdcvo.setHtamount(vo.getDfkje());
			bdcvo.setSay_content(saycon);
			bdcvo.setSay_type(saytype);

			String ptno = date.toString().substring(0, 4)
					+ date.toString().substring(5, 7);
			String ptnoSQL = "select max(t.print_no) from dh_fkjhprint t where t.print_no like '"
					+ ptno + "%'";
			Object ptobj = iQ.executeQuery(ptnoSQL, new ColumnProcessor());
			if (null == ptobj || "".equals(ptobj)) {
				ptno += "0001";
			} else {
				ptno = String.valueOf(Integer.valueOf(ptobj.toString())
						.intValue() + 1);
			}

			pvo.setPrint_no(ptno);
			bdvo.setPrint_no(ptno);

			HYPubBO_Client.insert(pvo);
			String prmkey = HYPubBO_Client.insert(bdvo);

			bdcvo.setPk_fkbd(prmkey);
			HYPubBO_Client.insert(bdcvo);
		}
	}

}