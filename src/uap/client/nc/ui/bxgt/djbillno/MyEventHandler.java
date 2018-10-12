package nc.ui.bxgt.djbillno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.pub.CommonDataHelper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class MyEventHandler extends CardEventHandler {

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (IBxgtButton.GEN_BILLNO == intBtn) {
			onBoGenBillNO();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBoQuery() throws Exception {
		// ����ģ��
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		BillModel bmodel = card.getBillModel();

		bmodel.clearBodyData();

		// ��������
		Object byobj = card.getHeadItem("bill_type").getValueObject();
		// ��������
		Object bdobj = card.getHeadItem("bill_month").getValueObject();

		if ((null != byobj && !"".equals(byobj))
				&& (null != bdobj && !"".equals(bdobj))) {
			int by = (Integer) byobj;

			String sqltime = "select t.begindate from bd_accperiodmonth t where nvl(t.dr,0)=0 and t.pk_accperiodmonth='"
					+ bdobj.toString() + "' ";

			// ��ѯ�ӿ�
			IUAPQueryBS iQ = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());

			Object obj = iQ.executeQuery(sqltime, new ColumnProcessor());

			String date = obj.toString().substring(0, 7);

			if (1 == by) {// ���۶���

				String sql = " select b.csaleid,b.vreceiptcode,b.dmakedate,substr(b.dbilltime,12,8) dbilltime,b.ccustomerid,b.nheadsummny,b.ts, "
						+ " (select distinct d.custname from bd_cumandoc c,bd_cubasdoc d where c.pk_cubasdoc = d.pk_cubasdoc  "
						+ " and nvl(c.dr,0)=0 and c.pk_cumandoc=b.ccustomerid) custname "
						+ " from (select distinct m.csaleid from bxgt_isconfirm m where m.csaleid is not null and nvl(m.dr,0)=0) a,so_sale b,"
						+ " (select distinct n.csaleid from bxgt_isorderseq n where n.csaleid is not null and nvl(n.dr,0)=0) c "
						+ " where a.csaleid = b.csaleid and b.csaleid=c.csaleid and nvl(b.dr,0)=0 "
						+ " and b.dbilldate like '"
						+ date.toString()
						+ "%' order by b.dbilltime,b.vreceiptcode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealSaleOrder(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (2 == by) {// ���۳���

				String sql = " select b.cgeneralhid,b.vbillcode,b.dbilldate,substr(b.tmaketime,12,8) tmaketime,b.ts,b.ccustomerid, "
						+ " (select distinct c.custname from bd_cubasdoc c,bd_cumandoc d where d.pk_cubasdoc = c.pk_cubasdoc  "
						+ " and nvl(d.dr,0)=0 and d.pk_cumandoc = b.ccustomerid) custname "
						+ " from (select distinct m.cgeneralhid from bxgt_isconfirm m where m.cgeneralhid is not null and nvl(m.dr, 0) = 0) a,ic_general_h b,"
						+ " (select distinct n.cgeneralhid from bxgt_isorderseq n where n.cgeneralhid is not null and nvl(n.dr,0)=0) c "
						+ " where a.cgeneralhid = b.cgeneralhid and b.cgeneralhid=c.cgeneralhid and b.cbilltypecode='4C' and nvl(b.dr,0)=0 and b.dbilldate like '"
						+ date.toString()
						+ "%' order by b.tmaketime,b.vbillcode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealSaleOut(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (3 == by) {// �տ
				String sql = "select a.vouchid,a.djbh,a.djrq,a.ybje,(select d.custname from bd_cubasdoc d where nvl(d.dr,0)=0 and d.pk_cubasdoc=b.hbbm) custname from arap_djzb a ,arap_djfb b,"
						+ "(select distinct m.vouchid from bxgt_isconfirm m where m.vouchid is not null and nvl(m.dr,0)=0) c, "
						+ "(select distinct n.vouchid from bxgt_isorderseq n where n.vouchid is not null and nvl(n.dr,0)=0) d "
						+ "where a.vouchid=b.vouchid and a.vouchid=c.vouchid and a.vouchid=d.vouchid and a.ybje=b.dfybje"
						+ " and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and a.djlxbm='D2' and a.djrq like '"
						+ date.toString() + "%' order by a.ts,a.djbh";
				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());
				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealSaleMoney(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			}

			else if (4 == by) {// �ɹ�����

				String sql = "select b.corderid,b.vordercode,b.dorderdate,substr(b.tmaketime,12,8) tmaketime,b.cvendorbaseid,"
						+ " (select distinct c.custname from bd_cubasdoc c where b.cvendorbaseid = c.pk_cubasdoc and nvl(c.dr,0)=0) custname from "
						+ " (select distinct m.corderid from bxgt_issyn m where m.corderid is not null and nvl(m.dr, 0) = 0) a,"
						+ " po_order b where a.corderid=b.corderid and nvl(b.dr,0)=0 and b.dorderdate like '"
						+ date.toString()
						+ "%' order by b.tmaketime,b.vordercode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealPurchaseOrder(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (5 == by) {// �ɹ����

				String sql = "select b.cgeneralhid,b.vbillcode,b.dbilldate,substr(b.tmaketime,12,8) tmaketime,b.cproviderid,"
						+ " (select distinct c.custname from bd_cubasdoc c,bd_cumandoc d where d.pk_cubasdoc = c.pk_cubasdoc "
						+ " and nvl(d.dr,0)=0 and d.pk_cumandoc = b.cproviderid) custname from "
						+ " (select distinct m.cgeneralhid from bxgt_isconfirm m where m.cgeneralhid is not null and nvl(m.dr, 0) = 0) a,"
						+ " (select distinct n.cgeneralhid from bxgt_isorderseq n where n.cgeneralhid is not null and nvl(n.dr, 0) = 0) c,"
						+ " ic_general_h b where a.cgeneralhid = b.cgeneralhid and b.cgeneralhid=c.cgeneralhid and nvl(b.dr,0)=0 and b.cbilltypecode='45' and b.dbilldate like '"
						+ date.toString()
						+ "%' order by b.tmaketime,b.vbillcode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealPurchaseIn(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (6 == by) {// �ɹ���Ʊ

				String sql = "select b.cinvoiceid,b.vinvoicecode,b.dinvoicedate,substr(b.tmaketime,12,8) tmaketime,b.cvendorbaseid,"
						+ " (select distinct c.custname from bd_cubasdoc c where b.cvendorbaseid = c.pk_cubasdoc and nvl(c.dr,0)=0) custname from "
						+ " (select distinct m.cinvoiceid from bxgt_isconfirm m where m.cinvoiceid is not null and nvl(m.dr, 0) = 0) a,"
						+ " (select distinct n.cinvoiceid from bxgt_isorderseq n where n.cinvoiceid is not null and nvl(n.dr, 0) = 0) c,"
						+ " po_invoice b where a.cinvoiceid=b.cinvoiceid and b.cinvoiceid=c.cinvoiceid and nvl(b.dr,0)=0 and b.dinvoicedate like '"
						+ date.toString()
						+ "%' order by b.tmaketime,b.vinvoicecode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealPoInvoice(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (7 == by) {// ���ϳ���

				String sql = "select b.cgeneralhid,b.vbillcode,b.dbilldate,substr(b.tmaketime,12,8) tmaketime,b.cproviderid,"
						+ " (select distinct c.custname from bd_cubasdoc c,bd_cumandoc d where d.pk_cubasdoc = c.pk_cubasdoc "
						+ " and nvl(d.dr,0)=0 and d.pk_cumandoc = b.cproviderid) custname from "
						+ " (select distinct m.cgeneralhid from bxgt_isconfirm m where m.cgeneralhid is not null and nvl(m.dr, 0) = 0) a,"
						+ " (select distinct n.cgeneralhid from bxgt_isorderseq n where n.cgeneralhid is not null and nvl(n.dr, 0) = 0) c,"
						+ " ic_general_h b where a.cgeneralhid = b.cgeneralhid and nvl(b.dr,0)=0 and b.cbilltypecode='4D' and b.dbilldate like '"
						+ date.toString()
						+ "%' order by b.tmaketime,b.vbillcode ";

				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());

				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealMaterialOut(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			} else if (8 == by) {// Ԥ�տ
				String sql = "select distinct a.vouchid ,a.djbh,a.djrq,a.ybje,d.custname from arap_djzb a left join arap_djfb b on a.vouchid=b.vouchid"
						+ " left join bd_cubasdoc d on d.pk_cubasdoc=b.hbbm"
						+ "  where nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(d.dr,0)=0 and a.djlxbm='D0'"
						+ " and a.djrq like '"
						+ date.toString()
						+ "%' order by a.djrq,a.djbh";
				List<Map<String, Object>> listmap = (List<Map<String, Object>>) iQ
						.executeQuery(sql, new MapListProcessor());
				if (null != listmap && listmap.size() > 0) {
					DealModelData.dealPlanMoney(bmodel, listmap);
				} else {
					MessageDialog.showHintDlg(card, "��ʾ", "������");
				}
			}
		} else {
			MessageDialog.showHintDlg(card, "��ʾ", "�������ͻ򵥾����ڲ���Ϊ�գ�");
			return;
		}

	}

	// ���ݺ�����
	public void onBoGenBillNO() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		// ��������
		Object byobj = card.getHeadItem("bill_type").getValueObject();
		// ��������
		Object bdobj = card.getHeadItem("bill_month").getValueObject();

		if ((null != byobj && !"".equals(byobj))
				&& (null != bdobj && !"".equals(bdobj))) {
			int by = (Integer) byobj;

			// �洢���ݺ�
			Map<String, Boolean> boomap = new HashMap<String, Boolean>();

			BillModel bmodel = card.getBillModel();
			int rows = bmodel.getRowCount();

			if (1 == by) {// ���۶���(3λ)

				int startno = 1001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					String djno = bmodel.getValueAt(i, "djno_old").toString();
					if (lockobj) {// ����
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = "";
						if (djno.startsWith("SO")) {
							djh = genBillcode(boomap, "SO" + bd, startno);
						} else if (djno.startsWith("GZ")) {
							djh = genBillcode(boomap, "GZ" + bd, startno);
						}
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (2 == by) {// ���۳��⣨4��

				int startno = 10001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "XC" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (3 == by) {// �տ��3��
				int startno = 1001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "SK" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}

			} else if (4 == by) {// �ɹ�������4��
				int startno = 10001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "CD" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (5 == by) {// �ɹ���⣨6��
				int startno = 1000001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "CR" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (6 == by) {// �ɹ���Ʊ��4��
				int startno = 10001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "CF" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (7 == by) {// ���ϳ��⣨6��
				int startno = 1000001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "CC" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			} else if (8 == by) {// Ԥ�տ��3��
				int startno = 1001;

				for (int i = 0; i < rows; i++) {
					// �ж��Ƿ�����
					boolean lockobj = (Boolean) (null == bmodel.getValueAt(i,
							"is_lock") ? false : bmodel
							.getValueAt(i, "is_lock"));
					if (lockobj) {// ����
						String djno = bmodel.getValueAt(i, "djno_old")
								.toString();
						boomap.put(djno, true);
						bmodel.setValueAt(djno, i, "djno_new");
					} else {// δ����
						String bd = bmodel.getValueAt(i, "djdate").toString()
								.substring(2).replaceAll("-", "");
						String djh = genBillcode(boomap, "YS" + bd, startno);
						boomap.put(djh, true);
						bmodel.setValueAt(djh, i, "djno_new");
					}
				}
			}
		} else {
			MessageDialog.showHintDlg(card, "��ʾ", "�������ͻ򵥾����ڲ���Ϊ�գ�");
			return;
		}
	}

	private String genBillcode(Map<String, Boolean> boomap, String djhPrefix,
			int startno) {
		String djh = djhPrefix + String.valueOf(startno).substring(1);
		if (boomap.containsKey(djh)) {
			startno++;
			djh = genBillcode(boomap, djhPrefix, startno);
		}

		return djh;
	}

	// ����
	@Override
	protected void onBoSave() throws Exception {
		List<String> updSqls = new ArrayList<String>();

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		// ��������
		Object byobj = card.getHeadItem("bill_type").getValueObject();
		// ��������
		Object bdobj = card.getHeadItem("bill_month").getValueObject();

		if ((null != byobj && !"".equals(byobj))
				&& (null != bdobj && !"".equals(bdobj))) {

			int by = (Integer) byobj;

			BillModel bmodel = card.getBillModel();
			int rows = bmodel.getRowCount();

			boolean flag = false;
			for (int i = 0; i < rows; i++) {
				Object djhobj = bmodel.getValueAt(i, "djno_new");
				if (null == djhobj || "".equals(djhobj)) {
					flag = true;
				}
			}
			if (flag) {
				MessageDialog.showHintDlg(card, "��ʾ", "�������ɵ��ݺ�");
				return;
			}

			// ���޸ĵ��ݺ�֮ǰ��У�鵥�ݺ��ظ�����
			String str = this.checkBillnoReply();
			if (!"".equals(str)) {
				MessageDialog.showHintDlg(card, "��ʾ", str);
				return;
			}

			if (1 == by) {

				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();

						// �������۶���
						String updsql = "update so_sale set vreceiptcode='"
								+ djhnew + "',dbilltime='" + djtime
								+ "' where csaleid = '" + csaleid + "'";
						updSqls.add(updsql);
						// �����տ
						String updsql2 = "update arap_djfb set ddh='" + djhnew
								+ "' where ddlx = '" + csaleid + "'";
						updSqls.add(updsql2);
						// �������۳���
						String updsql3 = "update ic_general_b set vfirstbillcode='"
								+ djhnew
								+ "',vsourcebillcode='"
								+ djhnew
								+ "' where cfirstbillhid = '" + csaleid + "'";
						updSqls.add(updsql3);
						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(1,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}

			} else if (2 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();

						String updsql = "update ic_general_h set vbillcode='"
								+ djhnew + "',tmaketime='" + djtime
								+ "' where cgeneralhid = '" + csaleid + "'";
						updSqls.add(updsql);
						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(2,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (3 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();

						String updsql = "update arap_djzb set djbh='" + djhnew
								+ "' where vouchid = '" + csaleid + "'";
						updSqls.add(updsql);
						String updsql2 = "update arap_djfb set djbh='" + djhnew
								+ "' where vouchid = '" + csaleid + "'";
						updSqls.add(updsql2);
						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(3,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (4 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();
						// ���¶���
						String updsql = "update po_order set vordercode='"
								+ djhnew + "',tmaketime='" + djtime
								+ "' where corderid = '" + csaleid + "'";
						updSqls.add(updsql);

						// �������۳���
						String updsql2 = "update ic_general_b set vfirstbillcode='"
								+ djhnew
								+ "',vsourcebillcode='"
								+ djhnew
								+ "' where cfirstbillhid = '" + csaleid + "'";
						updSqls.add(updsql2);
						// �޷�Ʊ�ӱ�
						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(4,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (5 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();
						// ������ⵥ
						String updsql = "update ic_general_h set vbillcode='"
								+ djhnew + "',tmaketime='" + djtime
								+ "' where cgeneralhid = '" + csaleid + "'";
						updSqls.add(updsql);

						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(5,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (6 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();
						// ���·�Ʊ
						String updsql = "update po_invoice set vinvoicecode='"
								+ djhnew + "',tmaketime='" + djtime
								+ "' where cinvoiceid = '" + csaleid + "'";
						updSqls.add(updsql);

						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(6,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (7 == by) {
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						String djtime = bmodel.getValueAt(i, "djdate")
								.toString()
								+ " "
								+ bmodel.getValueAt(i, "djtime").toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();
						// ���·�Ʊ
						String updsql = "update ic_general_h set vbillcode='"
								+ djhnew + "',tmaketime='" + djtime
								+ "' where cgeneralhid = '" + csaleid + "'";
						updSqls.add(updsql);

						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(7,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			} else if (8 == by) {// Ԥ�տ
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						String csaleid = bmodel.getValueAt(i, "csaleid")
								.toString();
						String djhold = bmodel.getValueAt(i, "djno_old")
								.toString();
						String djhnew = bmodel.getValueAt(i, "djno_new")
								.toString();
						// String djts = bmodel.getValueAt(i,
						// "djts").toString();

						String updsql = "update arap_djzb set djbh='" + djhnew
								+ "' where vouchid = '" + csaleid + "'";
						updSqls.add(updsql);
						String updsql2 = "update arap_djfb set djbh='" + djhnew
								+ "' where vouchid = '" + csaleid + "'";
						updSqls.add(updsql2);
						String intsql = "insert into bxgt_billcode(djtype,djpk,olddjh,newdjh) values(8,'"
								+ csaleid
								+ "','"
								+ djhold
								+ "','"
								+ djhnew
								+ "')";
						updSqls.add(intsql);
					}
				}
			}
		}

		if (null != updSqls && updSqls.size() > 0) {
			CommonDataHelper.execDatas(updSqls.toArray(new String[0]));
			MessageDialog.showHintDlg(card, "��ʾ", "�����������");
		}
	}

	/**
	 * У�鵥�ݺ��ظ�
	 */
	private String checkBillnoReply() throws Exception {

		String str = "";

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		int type = (Integer) card.getHeadItem("bill_type").getValueObject();

		BillModel bmodel = card.getBillModel();
		int rows = bmodel.getRowCount();
		String bill_no = "";
		String pks = "";
		for (int i = 0; i < rows; i++) {
			String billno = bmodel.getValueAt(i, "djno_new").toString();
			String billpk = bmodel.getValueAt(i, "csaleid").toString();
			bill_no += "'" + billno + "',";
			pks += "'" + billpk + "',";
		}

		bill_no = bill_no.substring(0, bill_no.lastIndexOf(","));
		pks = pks.substring(0, pks.lastIndexOf(","));

		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "";
		String billtype = "";
		if (type == 1) {// ���۶���
			sql = "select h.vreceiptcode from so_sale h where h.vreceiptcode in("
					+ bill_no
					+ ") and h.csaleid not in("
					+ pks
					+ ") and nvl(h.dr,0)=0";
			billtype = "���۶���";
		} else if (type == 2 || type == 5 || type == 7) {
			sql = "select h.vbillcode from ic_general_h h where h.vbillcode in("
					+ bill_no
					+ ") and h.cgeneralhid not in("
					+ pks
					+ ") and nvl(h.dr,0)=0";
			if (type == 2) {
				billtype = "���۳���";
			} else if (type == 5) {
				billtype = "�ɹ����";
			} else {
				billtype = "���ϳ���";
			}

		} else if (type == 4) {
			sql = "select h.vordercode from po_order h where h.vordercode in("
					+ bill_no + ") and h.corderid not in(" + pks
					+ ") and nvl(h.dr,0)=0";
			billtype = "�ɹ�����";
		} else if (type == 6) {
			sql = "select h.vinvoicecode from po_invoice h where h.vinvoicecode in("
					+ bill_no
					+ ") and h.cinvoiceid not in("
					+ pks
					+ ") and nvl(h.dr,0)=0";
			billtype = "�ɹ���Ʊ";
		} else if (type == 3) {
			sql = "select h.djbh from arap_djzb h where h.djbh in(" + bill_no
					+ ") and h.vouchid not in(" + pks + ") and nvl(h.dr,0)=0";
			billtype = "�����տ";
		} else if (type == 8) {
			sql = "select h.djbh from arap_djzb h where h.djbh in(" + bill_no
					+ ") and h.vouchid not in(" + pks + ") and nvl(h.dr,0)=0";
			billtype = "Ԥ�տ";
		}
		ArrayList<String> list = (ArrayList<String>) iQ.executeQuery(sql,
				new ColumnListProcessor());

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				str += "��" + list.get(i) + "��";
			}
		}

		if (!"".equals(str)) {
			str = "��������Ϊ��" + billtype + "�ĵ����У����ݺ�" + str + "��ϵͳ���ڣ����޸ģ�";
		}
		return str;
	}
}
