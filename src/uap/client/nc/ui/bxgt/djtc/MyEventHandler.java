package nc.ui.bxgt.djtc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import nc.bs.framework.common.NCLocator;
import nc.itf.bxgt.djtc.IbxgtQuerySynchro;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.pub.ExcelTools;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.bxgt.menu.BxgtStepButton;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class MyEventHandler extends CardEventHandler {

	public static String[] str = new String[3];

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	public BillItem[] bodyItems = null;

	@Override
	protected UIDialog createQueryUI() {
		return new HYQueryDLG(getBillUI(), null, _getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), _getOperator(), getBillUI()
						.getBusinessType(), getBillUI().getNodeKey());
	}

	@Override
	protected void onBoQuery() throws Exception {

		// ��ѯģ��
		HYQueryDLG querydialog = (HYQueryDLG) getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;

		// ��ѯ��������
		ConditionVO[] condtions = querydialog.getConditionVO();

		String zt = "";
		String billtype = "";
		String datefrom = "";
		String dateend = "";
		String strWhere = "";
		String strsoWhere = "";

		for (ConditionVO condvo : condtions) {
			if ("dj.zt".equals(condvo.getFieldCode())) {
				zt = condvo.getValue();
			} else if ("dj.bill_type".equals(condvo.getFieldCode())) {
				billtype = condvo.getValue();
			} else if ("dj.bill_date".equals(condvo.getFieldCode())) {
				datefrom = condvo.getValue();
				strWhere += " and dj.bill_date >= '" + datefrom + "' ";
				strsoWhere += " and dj.bill_date >= '" + datefrom + "' ";
			} else if ("dj.~".equals(condvo.getFieldCode())) {
				dateend = condvo.getValue();
				strWhere += " and dj.bill_date <= '" + dateend + "' ";
				strsoWhere += " and dj.bill_date <= '" + dateend + "' ";
			} else if ("dj.pk_cust".equals(condvo.getFieldCode())) {
				strsoWhere += " and dj.pk_cust ='" + condvo.getValue() + "' ";
			} else if ("dj.pk_hth".equals(condvo.getFieldCode())) {
				strsoWhere += " and dj.pk_hth ='" + condvo.getValue() + "' ";
			}
		}

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());
		Map<Integer, Map<String, Object>> mapMap = null;

		// ����ģ��PK
		String cardpk = "";

		// ����PK
		String billpk = "";

		// �����������͵ĵ��ݲ�ѯ
		if ("���۶���".equals(billtype)) {
			// ���۶���
			mapMap = ibxgt.bxgtQuerySaleOrder(strsoWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OL7M9"; // so000000000saleorder
				billpk = "csaleid"; // 2015-4-10 bwy �޸� caleid-->csaleid
			}
		} else if ("���۳���".equals(billtype)) {
			// ���۳��ⵥ
			mapMap = ibxgt.bxgtQuerySaleOutOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OLZJ0";
				billpk = "cgeneralhid";
			}
		} else if ("�ɹ�����".equals(billtype)) {
			// �ɹ�����
			mapMap = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OP7X2";
				billpk = "corderid";//
			}
		} else if ("�ɹ����".equals(billtype)) {
			// �ɹ����
			mapMap = ibxgt.bxgtQueryPurchaseInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OU75I";
				billpk = "cgeneralhid";
			}
		} else if ("�ɹ���Ʊ".equals(billtype)) {
			// �ɹ���Ʊ
			mapMap = ibxgt.bxgtQueryPurchaseInvoiceOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUCKR";
				billpk = "cinvoiceid";
			}
		} else if ("���ϳ���".equals(billtype)) {
			// ���ϳ���
			mapMap = ibxgt.bxgtQueryMatInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUJ25";
				billpk = "cgeneralhid";
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ʹ���");
			return;
		}

		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();
		// �����ѯ����չʾ
		cardPanel.setHeadItem("zt", zt);
		cardPanel.setHeadItem("billpk", billpk);
		cardPanel.setHeadItem("bill_type", billtype);
		cardPanel.setHeadItem("bill_date", datefrom);
		cardPanel.setHeadItem("~", dateend);

		// ����Ĭ�ϰ�ť��ʾ
		this.getButtonManager().getButton(IBxgtButton.SYNCHRONOUS).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.BATCH_EDIT).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.LOCK_GROUP).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.MARK_GROUP).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.ORDERGROUP).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.DELBILL)
				.setEnabled(false);
		this.getButtonManager().getButton(IBxgtButton.CUST_MNY).setEnabled(
				false);
		this.getButtonManager().getButton(IBxgtButton.TAX_RATE).setEnabled(
				false);
		// ���ݲ�ѯ�������ư�ť
		if ("������".equals(zt) && !"".equals(cardpk)) {
			this.getButtonManager().getButton(IBxgtButton.SYNCHRONOUS)
					.setEnabled(true);
		} else if ("������".equals(zt)) {
			if ("���۶���".equals(billtype) && !"".equals(cardpk)) {
				this.getButtonManager().getButton(IBxgtButton.BATCH_EDIT)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.LOCK_GROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.MARK_GROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.ORDERGROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.CUST_MNY)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.DELBILL)
						.setEnabled(true);
			} else if (("�ɹ�����".equals(billtype) || "���ϳ���".equals(billtype))
					&& !"".equals(cardpk)) {
				this.getButtonManager().getButton(IBxgtButton.BATCH_EDIT)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.LOCK_GROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.MARK_GROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.ORDERGROUP)
						.setEnabled(true);
				this.getButtonManager().getButton(IBxgtButton.DELBILL)
						.setEnabled(true);
			} else if ("�ɹ���Ʊ".equals(billtype) && !"".equals(cardpk)) {
				this.getButtonManager().getButton(IBxgtButton.TAX_RATE)
						.setEnabled(true);
			}
		}
		this.getBillUI().updateButtons();

		// ��ѯ�����������ֶ�չʾ
		BillModel bmodel = cardPanel.getBillModel();
		BillData billdata = cardPanel.getBillData();

		bmodel.clearBodyData();

		if (bodyItems == null) {
			bodyItems = bmodel.getBodyItems();
		}

		List<BillItem> itemlist = new ArrayList<BillItem>();
		itemlist.addAll(Arrays.asList(bodyItems));

		if (!"".equals(cardpk)) {
			BillCardPanel newbodycard = new BillCardPanel();
			newbodycard.loadTemplet(cardpk);

			for (BillItem newItem : newbodycard.getHeadItems()) {
				newItem.setWidth(newItem.getWidth() * 100);
				itemlist.add(newItem);
			}
		}

		bmodel.setBodyItems(itemlist.toArray(new BillItem[0]));

		billdata.setBodyItems(itemlist.toArray(new BillItem[0]));
		cardPanel.setBillData(billdata);
		cardPanel.getBodyPanel().setTotalRowShow(true);// �ϼ�
		cardPanel.setBodyMultiSelect(true);

		// ����ֵ�Ĵ���
		if (null != mapMap && mapMap.size() > 0) {
			int rowmax = mapMap.size();

			for (int i = 0; i < rowmax; i++) {
				Map<String, Object> mapValue = mapMap.get(i);
				bmodel.addLine();
				for (String keyVaule : mapValue.keySet()) {
					bmodel.setValueAt(mapValue.get(keyVaule), i, keyVaule);
				}
			}
		}

	}

	/**
	 * ͬ������
	 * 
	 * @throws Exception
	 */
	public void onBoSynchroBill() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();

		BillModel bmodel = card.getBillModel();
		int rowsum = bmodel.getRowCount();

		// ����PK�Ĺ���
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�빴ѡ��Ҫͬ���ĵ���");
			return;
		}

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());
		Boolean retflag = false;

		// �����������͵ĵ���ͬ��
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("���۶���".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroSaleOrder(pklist.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���۶���ͬ���ɹ���");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
			this
					.writeToExcel(pklist.toArray(new String[0]), billtype,
							templist);// ����excel
			/*
			 * } else if ("������".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroDeliveryOrder(pklist .toArray(new String[0]));
			 */
		} else if ("���۳���".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroSaleOutOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���۳��ⵥͬ���ɹ���");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);

			/*
			 * } else if ("���۷�Ʊ".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroSaleInvoiceOrder(pklist .toArray(new
			 * String[0]));
			 */
		} else if ("�ɹ�����".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroPurchaseOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ɹ�����ͬ���ɹ���");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
			this
					.writeToExcel(pklist.toArray(new String[0]), billtype,
							templist);// ����excel
			/*
			 * } else if ("������".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroArriveOrder(pklist .toArray(new String[0]));
			 */
		} else if ("�ɹ����".equals(billtype)) {
			retflag = ibxgt
					.bxgtSynchroPurInOrder(pklist.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ɹ���ⵥͬ���ɹ���");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);

		} else if ("�ɹ���Ʊ".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroPurInvoiceOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ɹ���Ʊͬ���ɹ���");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
		} else if ("���ϳ���".equals(billtype)) {
			ArrayList<Object[]> list = ibxgt.bxgtSynMaterialOrder(pklist
					.toArray(new String[0]));
			if (null == list) {
				retflag = false;
			} else if (list.size() > 0) {
				int it = MessageDialog.showOkCancelDlg(this.getBillUI(), "��ʾ",
						"���ֲ��Ͽ�治�㣬�Ƿ񵼳�excel��");
				if (it == UIDialog.ID_OK) {
					this.materialToExcel(list, BxgtStepButton.CLCK);
				}
				retflag = true;
			} else if (list.size() == 0) {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���ϳ���ͬ���ɹ���");
				this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
				this.writeToExcel(pklist.toArray(new String[0]), billtype,
						templist);
				retflag = true;
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ʹ���");
			return;
		}

		if (!retflag) {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ͬ��ʧ��");
			return;
		}
	}

	/**
	 * �����޸�
	 * 
	 * @throws Exception
	 */
	public void onBoBatchEdit() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (null == billtype || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "�빴ѡ��Ҫ�޸ĵĵ���");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bmodel = card.getBillModel();
		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();
		int rowsum = bmodel.getRowCount();

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// ����PK�Ĺ���
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				if (bmodel.getValueAt(i, "pk_sd") == Boolean.FALSE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "����",
							"����δ�����ĵ��ݣ��޷����ģ�");
					return;
				} else if (bmodel.getValueAt(i, "pk_qr") == Boolean.TRUE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "����",
							"������ȷ�ϵĵ��ݣ��޷����ģ�");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�빴ѡ��Ҫ�޸ĵĵ���");
			return;
		}
		ProcessEditDlg editDlg = new ProcessEditDlg(this.getBillUI(), "�����޸�",
				pklist.toArray(new String[0]));
		int flag = editDlg.showModal();
		if (flag == UIDialog.ID_OK) {
			// 2015-4-10 bwy ����
			String[] pks = pklist.toArray(new String[0]);
			int days = editDlg.getValue2();
			String strWhere = "";
			// ����ģ��PK
			String cardpk = "";
			Map<Integer, Map<String, Object>> map = null;
			if ("���۶���".equals(billtype)) {
				strWhere = "and a.csaleid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "������";
				ibxgt.bxgtBatchUpdateSaleOrder(pks, days);
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ ", "���ĳɹ���");
				// ���ĺ�ˢ�½���
				/*
				 * map = ibxgt.bxgtQuerySaleOrder(strWhere, zt); if (map != null &&
				 * map.size() > 0) { cardpk = "0001AA100000000OL7M9"; billpk =
				 * "csaleid"; }
				 */
				this.onBoRefreshes();

			} else if ("�ɹ�����".equals(billtype)) {
				strWhere = "and o.corderid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "������";

				ArrayList<Object[]> list = ibxgt.bxgtBatchUpdatePurchaseOrder(
						pks, days);
				if (list == null) {
					throw new BusinessException("δ�ҵ�������ⵥ��");
				} else if (list.size() > 0) {
					int it = MessageDialog.showOkCancelDlg(this.getBillUI(),
							"��ʾ ", "���ĺ�����������㣬�Ƿ񵼳�excel��");
					if (it == UIDialog.ID_OK) {
						this.materialToExcel(list, BxgtStepButton.RKPG);
					}
					return;
				}
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ ", "���ĳɹ���");
				// ���ĺ�ˢ�½���
				/*
				 * map = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt); if (map !=
				 * null && map.size() > 0) { cardpk = "0001AA100000000OP7X2";
				 * billpk = "corderid"; }
				 */
				this.onBoRefreshes();
			} else if ("���ϳ���".equals(billtype)) {
				strWhere = "and a.cgeneralhid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "������";
				ArrayList<Object[]> list = ibxgt.bxgtBatchUpdateMaterialOrder(
						pks, days);
				if (list == null) {
					throw new BusinessException("�����쳣��");
				} else if (list.size() > 0) {
					int it = MessageDialog.showOkCancelDlg(this.getBillUI(),
							"��ʾ ", "���ĺ�����������㣬�Ƿ񵼳�excel��");
					if (it == UIDialog.ID_OK) {
						this.materialToExcel(list, BxgtStepButton.CKPG);
					}
					return;
				}
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ ", "���ĳɹ���");
				// ���ĺ�ˢ�½���
				/*
				 * map = ibxgt.bxgtQueryMatInOrder(strWhere, zt); if (map !=
				 * null && map.size() > 0) { cardpk = "0001AA100000000OUJ25";
				 * billpk = "cgeneralhid"; }
				 */
				this.onBoRefreshes();
			}

			/*
			 * // ��ѯ�����������ֶ�չʾ BillModel bmodel2 = cardPanel.getBillModel();
			 * BillData billdata = cardPanel.getBillData();
			 * 
			 * bmodel2.clearBodyData();
			 * 
			 * if (bodyItems == null) { bodyItems = bmodel2.getBodyItems(); }
			 * 
			 * List<BillItem> itemlist = new ArrayList<BillItem>();
			 * itemlist.addAll(Arrays.asList(bodyItems));
			 * 
			 * if (!"".equals(cardpk)) { BillCardPanel newbodycard = new
			 * BillCardPanel(); newbodycard.loadTemplet(cardpk);
			 * 
			 * for (BillItem newItem : newbodycard.getHeadItems()) {
			 * newItem.setWidth(newItem.getWidth() * 100);
			 * itemlist.add(newItem); } }
			 * 
			 * bmodel2.setBodyItems(itemlist.toArray(new BillItem[0]));
			 * billdata.setBodyItems(itemlist.toArray(new BillItem[0]));
			 * cardPanel.setBillData(billdata);
			 * cardPanel.setBodyMultiSelect(true); // ����ֵ�Ĵ��� if (null != map &&
			 * map.size() > 0) { int rowmax = map.size();
			 * 
			 * for (int i = 0; i < rowmax; i++) { Map<String, Object> mapValue =
			 * map.get(i); bmodel2.addLine(); for (String keyVaule :
			 * mapValue.keySet()) { bmodel2.setValueAt(mapValue.get(keyVaule),
			 * i, keyVaule); } } }
			 */

		}
	}

	/**
	 * ��������
	 */
	public void onBoLock() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();

		BillModel bmodel = card.getBillModel();
		int rowsum = bmodel.getRowCount();

		// ����PK�Ĺ���
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED
					&& Boolean.FALSE == bmodel.getValueAt(i, "pk_sd")) {
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�빴ѡ��Ҫ������δ�����ĵ���");
			return;
		}
		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// �����������͵ĵ�������
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("���۶���".equals(billtype)) {
			String[] billcodes = ibxgt.bxgtLockSaleOrders(pklist
					.toArray(new String[0]));
			String str = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (billcodes != null && billcodes.length > 0) {
				for (int i = 0; i < billcodes.length; i++) {
					str += "��" + billcodes[i] + "��";
					map.put(billcodes[i], null);
				}
				str = "���ݺ�Ϊ" + str + "�ĵ�������δ���꣬�������������ɹ���";
				for (int i = templist.size() - 1; i >= 0; i--) {
					String billno = bmodel.getValueAt(templist.get(i),
							"vreceiptcode").toString();
					if (map.containsKey(billno)) {
						templist.remove(i);
					}
				}
			} else {
				str = "���۶��������ɹ���";
			}
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", str);
			this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
		} else if ("�ɹ�����".equals(billtype)) {
			String[] billcodes = ibxgt.bxgtLockPurchaseOrders(pklist
					.toArray(new String[0]));
			String str = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (billcodes != null && billcodes.length > 0) {// ��templist���޸�
				for (int i = 0; i < billcodes.length; i++) {
					str += "��" + billcodes[i] + "��";
					map.put(billcodes[i], null);
				}
				str = "���ݺ�Ϊ" + str + "�ĵ�������δ���꣬�������������ɹ���";
				for (int i = templist.size() - 1; i >= 0; i--) {
					String billno = bmodel.getValueAt(templist.get(i),
							"vordercode").toString();
					if (map.containsKey(billno)) {
						templist.remove(i);
					}
				}
			} else {
				str = "�ɹ����������ɹ���";
			}
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", str);
			this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
			return;
		} else if ("���ϳ���".equals(billtype)) {
			String str = ibxgt.bxgtLockMaterialOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���ϳ��������ɹ���");
				this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
				return;
			} else {
				MessageDialog.showErrorDlg(this.getBillUI(), "��ʾ", "����ʧ�� ��");
				return;
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @throws Exception
	 * 
	 */
	public void onBoUnLock() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();

		BillModel bmodel = card.getBillModel();
		int rowsum = bmodel.getRowCount();

		// ����PK�Ĺ���
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED
					&& bmodel.getValueAt(i, "pk_sd") == Boolean.TRUE) {
				if (bmodel.getValueAt(i, "pk_qr") == Boolean.TRUE) {
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ",
							"����ȷ�ϵĵ��ݣ��޷�������");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�빴ѡ��Ҫ��������ĵ���");
			return;
		}
		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// �����������͵ĵ�������
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("���۶���".equals(billtype)) {
			String str = ibxgt.bxgtUnLockSaleOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������������ɹ���");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����������ʧ�ܣ�");
				return;
			}
		} else if ("�ɹ�����".equals(billtype)) {
			String str = ibxgt.bxgtUnLockPurchaseOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ɹ����������ɹ���");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ɹ���������ʧ�ܣ�");
				return;
			}
		} else if ("���ϳ���".equals(billtype)) {
			String str = ibxgt.bxgtUnLockMaterialOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "���ϳ�������ɹ���");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ʧ�ܣ�");
				return;
			}
		}
	}

	/**
	 * ͬ��Ԥ�տ
	 * 
	 * @throws Exception
	 */
	public void onAdvanceReceive() throws Exception {
		// Ԥ�տͬ�� xm 2015.5.6
		IbxgtQuerySynchro ibar = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibar.getNeedPks();
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ͬ��Ԥ�տ�ɹ���");
	}

	/**
	 * ȷ�ϱ�־
	 */
	public void onBoMark() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "���Ȳ�ѯ�����ݣ�");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bm = card.getBillModel();
		int row = bm.getRowCount();
		List<String> pklist = new ArrayList<String>();

		for (int i = 0; i < row; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED
					&& bm.getValueAt(i, "pk_qr") == Boolean.FALSE) {
				if (bm.getValueAt(i, "pk_sd") == Boolean.FALSE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "����",
							"����δ�����ĵ��ݣ��޷�ȷ�ϣ�");
					return;
				}
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.confirmBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.QUE_REN, templist);
	}

	/**
	 * ȡ�����
	 */
	public void onBoCancel() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "���Ȳ�ѯ�����ݣ�");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bm = card.getBillModel();
		int row = bm.getRowCount();
		List<String> pklist = new ArrayList<String>();

		for (int i = 0; i < row; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED
					&& bm.getValueAt(i, "pk_qr") == Boolean.TRUE) {
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.unConfirmBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.QU_XIAO, templist);
	}

	/**
	 * ��Ʊ˰���޸�
	 */
	public void onTaxEdit() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel = card.getBillModel();
		int row = bmodel.getRowCount();

		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < row; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				pklist.add(bmodel.getValueAt(i, "cinvoiceid").toString());
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}
		TaxBatchDlg dlg = new TaxBatchDlg(card);

		Object obj = dlg.getTextValue();
		if (obj == null) {
			return;
		}
		UFDouble taxRate = new UFDouble(obj.toString());

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.batchInvoiceTaxRate(pklist.toArray(new String[0]), taxRate,
				BxgtStepButton.SLXG);
		MessageDialog.showHintDlg(card, "��ʾ", "���ĳɹ���");

	}

	/**
	 * ���۶����ͻ�����޸�
	 */
	public void onCustMny() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel = card.getBillModel();
		int row = bmodel.getRowCount();
		ArrayList<Integer> templist = new ArrayList<Integer>();
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < row; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				pklist.add(bmodel.getValueAt(i, "csaleid").toString());
				templist.add(i);
			}
		}
		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}
		CustMnyDlg cMny = new CustMnyDlg(card);

		if (!cMny.isFlag()) {// ��ȷ����ť
			return;
		}

		Object cust = cMny.getPk_cust();// �ͻ�
		Object obj = cMny.getValue1(); // ���
		if (obj != null && !"".equals(obj)) {
			if (pklist.size() > 1) {
				MessageDialog.showHintDlg(card, "��ʾ", "���Ľ�֧�ֶ������ݣ�");
				return;
			}
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibqs.batchCustAndMoney(pklist.toArray(new String[0]), cust, obj);

		// �޸ĺ�ˢ�½���
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if (pklist.size() == 1) {// �޸Ľ��϶���
			if (cust != null && !"".equals(cust)) {
				String sql = "select t.custname from bd_cubasdoc t where t.pk_cubasdoc="
						+ "(select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
						+ cust.toString() + "') ";
				Object custname = iQ.executeQuery(sql, new ColumnProcessor());
				bmodel.setValueAt(custname, templist.get(0), "ccustomerid");
			}
			if (obj != null && !"".equals(obj)) {
				bmodel.setValueAt(obj, templist.get(0), "nheadsummny");
			}
		} else if (pklist.size() > 1) {
			String sql = "select t.custname from bd_cubasdoc t where t.pk_cubasdoc="
					+ "(select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
					+ cust.toString() + "') ";
			Object custname = iQ.executeQuery(sql, new ColumnProcessor());
			for (int i = 0; i < pklist.size(); i++) {
				bmodel.setValueAt(custname, templist.get(i), "ccustomerid");
			}
		}
		MessageDialog.showHintDlg(card, "��ʾ", "���ĳɹ���");

	}

	/**
	 * ɾ������
	 */
	public void onBoDelete() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		Object billtype = card.getHeadItem("bill_type").getValueObject();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "���Ȳ�ѯ�����ݣ�");
			return;
		}
		BillModel bmodel = card.getBillModel();
		int row = bmodel.getRowCount();
		List<String> pklist = new ArrayList<String>();
		ArrayList<Integer> templist = new ArrayList<Integer>();
		for (int i = 0; i < row; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				if (bmodel.getValueAt(i, "pk_sd") == Boolean.TRUE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "����",
							"���������ĵ��ݣ��޷�ɾ����");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}
		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}

		// ȷ��ɾ������
		int it = MessageDialog.showYesNoDlg(card, "��ʾ", "�Ƿ�ȷ��ɾ����");
		if (it != UIDialog.ID_YES) {
			return;
		}
		int it2 = MessageDialog.showYesNoDlg(card, "��ʾ", "�ٴ�ȷ���Ƿ�ɾ����ɾ����Ų�����");
		if (it2 != UIDialog.ID_YES) {
			return;
		}
		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibqs.deleteSaleOrOrder(pklist.toArray(new String[0]), billtype
				.toString());
		// ɾ��ǰ̨����
		for (int i = templist.size(); i > 0; i--) {
			int k = templist.get(i - 1);
			card.getBodyPanel().delLine(new int[] { k });
		}
		MessageDialog.showHintDlg(card, "��ʾ", "ɾ���ɹ���");
	}

	public void onOrderSeq() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "���Ȳ�ѯ�����ݣ�");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bm = card.getBillModel();
		int row = bm.getRowCount();
		List<String> pklist = new ArrayList<String>();

		for (int i = 0; i < row; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED
					&& bm.getValueAt(i, "pk_px") == Boolean.FALSE) {
				if (bm.getValueAt(i, "pk_qr") == Boolean.FALSE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "����",
							"����δȷ�ϵĵ��ݣ��޷�����");
					return;
				}
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.orderSeqBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.OREDR_SEQ,
				templist);

	}

	public void onCancelSeq() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "��ʾ", "���Ȳ�ѯ�����ݣ�");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bm = card.getBillModel();
		int row = bm.getRowCount();
		List<String> pklist = new ArrayList<String>();

		for (int i = 0; i < row; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED
					&& bm.getValueAt(i, "pk_px") == Boolean.TRUE) {
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "��ʾ", "����ѡ�����ݣ�");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.unOrderSeqBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.CEL_ORDER,
				templist);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == IBxgtButton.SYNCHRONOUS) {
			// ͬ��
			onBoSynchroBill();
		} else if (intBtn == IBxgtButton.BATCH_EDIT) {
			// ����
			onBoBatchEdit();
		} else if (intBtn == IBxgtButton.PRE_PAYMENT) {
			// ͬ��Ԥ�տ
			onAdvanceReceive();
		} else if (intBtn == IBxgtButton.LOCK) {
			// ����
			onBoLock();
		} else if (intBtn == IBxgtButton.UNLOCK) {
			// ����
			onBoUnLock();
		} else if (intBtn == IBxgtButton.OKMARK) {
			// ȷ�ϱ�־
			onBoMark();
		} else if (intBtn == IBxgtButton.CANCEL) {
			// ȡ����־
			onBoCancel();
		} else if (intBtn == IBxgtButton.TAX_RATE) {
			// ��Ʊ˰��
			onTaxEdit();
		} else if (intBtn == IBxgtButton.CUST_MNY) {
			// �ͻ�����޸�
			onCustMny();
		} else if (intBtn == IBxgtButton.DELBILL) {
			// ɾ��
			onBoDelete();
		} else if (intBtn == IBxgtButton.ORDERSEQ) {
			// ����
			onOrderSeq();
		} else if (intBtn == IBxgtButton.CANCELORDER) {
			// ȡ������
			onCancelSeq();
		}
	}

	public void onBoRefreshes() throws Exception {

		// ��ѯģ��
		HYQueryDLG querydialog = (HYQueryDLG) getQueryUI();

		// ��ѯ��������
		ConditionVO[] condtions = querydialog.getConditionVO();

		String zt = "";
		String billtype = "";
		String datefrom = "";
		String dateend = "";
		String strWhere = "";
		String strsoWhere = "";

		for (ConditionVO condvo : condtions) {
			if ("dj.zt".equals(condvo.getFieldCode())) {
				zt = condvo.getValue();
			} else if ("dj.bill_type".equals(condvo.getFieldCode())) {
				billtype = condvo.getValue();
			} else if ("dj.bill_date".equals(condvo.getFieldCode())) {
				datefrom = condvo.getValue();
				strWhere += " and dj.bill_date >= '" + datefrom + "' ";
				strsoWhere += " and dj.bill_date >= '" + datefrom + "' ";
			} else if ("dj.~".equals(condvo.getFieldCode())) {
				dateend = condvo.getValue();
				strWhere += " and dj.bill_date <= '" + dateend + "' ";
				strsoWhere += " and dj.bill_date <= '" + dateend + "' ";
			} else if ("dj.pk_cust".equals(condvo.getFieldCode())) {
				strsoWhere += " and dj.pk_cust ='" + condvo.getValue() + "' ";
			} else if ("dj.pk_hth".equals(condvo.getFieldCode())) {
				strsoWhere += " and dj.pk_hth ='" + condvo.getValue() + "' ";
			}
		}

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());
		Map<Integer, Map<String, Object>> mapMap = null;

		// ����ģ��PK
		String cardpk = "";

		// �����������͵ĵ��ݲ�ѯ
		if ("���۶���".equals(billtype)) {
			// ���۶���
			mapMap = ibxgt.bxgtQuerySaleOrder(strsoWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OL7M9"; // so000000000saleorder
			}
		} else if ("���۳���".equals(billtype)) {
			// ���۳��ⵥ
			mapMap = ibxgt.bxgtQuerySaleOutOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OLZJ0";
			}
		} else if ("�ɹ�����".equals(billtype)) {
			// �ɹ�����
			mapMap = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OP7X2";
			}
		} else if ("�ɹ����".equals(billtype)) {
			// �ɹ����
			mapMap = ibxgt.bxgtQueryPurchaseInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OU75I";
			}
		} else if ("�ɹ���Ʊ".equals(billtype)) {
			// �ɹ���Ʊ
			mapMap = ibxgt.bxgtQueryPurchaseInvoiceOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUCKR";
			}
		} else if ("���ϳ���".equals(billtype)) {
			// ���ϳ���
			mapMap = ibxgt.bxgtQueryMatInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUJ25";
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������ʹ���");
			return;
		}

		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();

		// ��ѯ�����������ֶ�չʾ
		BillModel bmodel = cardPanel.getBillModel();
		BillData billdata = cardPanel.getBillData();

		bmodel.clearBodyData();

		if (bodyItems == null) {
			bodyItems = bmodel.getBodyItems();
		}

		List<BillItem> itemlist = new ArrayList<BillItem>();
		itemlist.addAll(Arrays.asList(bodyItems));

		if (!"".equals(cardpk)) {
			BillCardPanel newbodycard = new BillCardPanel();
			newbodycard.loadTemplet(cardpk);

			for (BillItem newItem : newbodycard.getHeadItems()) {
				newItem.setWidth(newItem.getWidth() * 100);
				itemlist.add(newItem);
			}
		}

		bmodel.setBodyItems(itemlist.toArray(new BillItem[0]));
		billdata.setBodyItems(itemlist.toArray(new BillItem[0]));
		cardPanel.setBillData(billdata);
		cardPanel.setBodyMultiSelect(true);

		// ����ֵ�Ĵ���
		if (null != mapMap && mapMap.size() > 0) {
			int rowmax = mapMap.size();

			for (int i = 0; i < rowmax; i++) {
				Map<String, Object> mapValue = mapMap.get(i);
				bmodel.addLine();
				for (String keyVaule : mapValue.keySet()) {
					bmodel.setValueAt(mapValue.get(keyVaule), i, keyVaule);
				}
			}
		}

	}

	/**
	 * ������excel
	 * 
	 * @param pksales
	 * @throws Exception
	 */
	private void writeToExcel(String[] pksales, String billtype,
			ArrayList<Integer> templist) throws Exception {

		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if (pksales == null || pksales.length <= 0) {
			return;
		}

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel = card.getBillModel();
		String sql = null;
		String coyle = "";

		if ("���۶���".equals(billtype)) {
			coyle = "vreceiptcode";
		} else if ("�ɹ�����".equals(billtype)) {
			coyle = "vordercode";
		} else if ("���ϳ���".equals(billtype)) {
			coyle = "vbillcode";
		}

		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < templist.size(); i++) {
			int t = templist.get(i);
			Object[] obj = new Object[2];
			obj[0] = billtype;
			obj[1] = bmodel.getValueAt(t, coyle);
			list.add(obj);
		}

		String path = getChooseFilePath(card, "�����ļ�");
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "��ʾ", "��ȡ������������ʧ�ܣ�");
			return;
		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}

		OutputStream os = new FileOutputStream(path);

		String[] headColsCN = new String[] { "��������", "���ݺ�" };

		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("��������");
		// �жϲ�ѯ���������Ƿ�Ϊ��
		if (CollectionUtils.isEmpty(list)) {
			excelTools.createRow(0);
			short ct = 0;
			excelTools.createCell(ct);
			excelTools.setValue("�����ݣ�");

		} else {
			excelTools.createRow(0);
			if (null != headColsCN) {
				for (short i = 0; i < headColsCN.length; i++) {
					excelTools.createCell(i);
					excelTools.setValue(headColsCN[i]);
				}
				// ����Object����
				Object[] array = null;
				for (int i = 0; i < list.size(); i++) {
					array = (Object[]) list.get(i);
					if (null != array) {
						excelTools.createRow(i + 1);
						for (short j = 0; j < array.length; j++) {
							excelTools.createCell(j);
							excelTools.setValue(array[j]);
						}
					}
				}
			}
		}
		excelTools.writeExcel(os);
		os.close();
	}

	/**
	 * ������excel��治��
	 * 
	 * @param pksales
	 * @throws Exception
	 */
	private void materialToExcel(ArrayList<Object[]> list, int num)
			throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String path = getChooseFilePath(card, "������ϵ���");
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "��ʾ", "��ȡ������������ʧ�ܣ�");
			return;
		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}

		OutputStream os = new FileOutputStream(path);

		String[] headColsCN = null;
		if (num == BxgtStepButton.CLCK) {
			headColsCN = new String[] { "��˾", "�����֯", "�ֿ�", "����", "����ͺ�",
					"���γ�������", "��������" };
		} else if (num == BxgtStepButton.RKPG || num == BxgtStepButton.CKPG) {
			headColsCN = new String[] { "��˾", "�����֯", "�ֿ�", "����", "����ͺ�",
					"���ĺ��������" };
		}

		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("��������");
		// �жϲ�ѯ���������Ƿ�Ϊ��
		if (CollectionUtils.isEmpty(list)) {
			excelTools.createRow(0);
			short ct = 0;
			excelTools.createCell(ct);
			excelTools.setValue("�����ݣ�");

		} else {
			excelTools.createRow(0);
			if (null != headColsCN) {
				for (short i = 0; i < headColsCN.length; i++) {
					excelTools.createCell(i);
					excelTools.setValue(headColsCN[i]);
				}
				// ����Object����
				Object[] array = null;
				for (int i = 0; i < list.size(); i++) {
					array = (Object[]) list.get(i);
					if (null != array) {
						excelTools.createRow(i + 1);
						for (short j = 0; j < array.length; j++) {
							excelTools.createCell(j);
							excelTools.setValue(array[j]);
						}
					}
				}
			}
		}
		excelTools.writeExcel(os);
		os.close();
	}

	/**
	 * �ļ�·��
	 */
	private String getChooseFilePath(BillCardPanel card, String defaultFileName) {
		// �½�һ���ļ�ѡ���
		JFileChooser fileChooser = new JFileChooser();
		// ����Ĭ���ļ���
		fileChooser.setSelectedFile(new File(defaultFileName));
		// �򿪱����
		int retVal = fileChooser.showSaveDialog(card);
		// ���巵�ر���
		String path = null;
		// �ж��Ƿ��
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// ȷ�ϴ򿪣���ȡѡ���·��
			path = fileChooser.getSelectedFile().getPath();
		}
		// ����·��
		return path;
	}

	/**
	 * ˢ�½�������
	 */
	private void refreshData(String billtype, int stepType,
			ArrayList<Integer> templist) {

		if (templist == null || templist.size() <= 0) {
			return;
		}
		String colkey = "";
		String value = "";
		if ("���۶���".equals(billtype) || "�ɹ�����".equals(billtype)
				|| "���ϳ���".equals(billtype)) {
			if (stepType == BxgtStepButton.SUO_DAN) {
				colkey = "pk_sd";
				value = "Y";
			} else if (stepType == BxgtStepButton.JIE_SUO) {
				colkey = "pk_sd";
				value = "N";
			} else if (stepType == BxgtStepButton.TONG_BU) {
				colkey = "pk_tb";
				value = "Y";
			} else if (stepType == BxgtStepButton.QUE_REN) {
				colkey = "pk_qr";
				value = "Y";
			} else if (stepType == BxgtStepButton.QU_XIAO) {
				colkey = "pk_qr";
				value = "N";
			} else if (stepType == BxgtStepButton.OREDR_SEQ) {
				colkey = "pk_px";
				value = "Y";
			} else if (stepType == BxgtStepButton.CEL_ORDER) {
				colkey = "pk_px";
				value = "N";
			}
		} else if ("���۳���".equals(billtype) || "�ɹ����".equals(billtype)
				|| "�ɹ���Ʊ".equals(billtype)) {
			if (stepType == BxgtStepButton.TONG_BU) {
				colkey = "pk_tb";
				value = "Y";
			}
		}

		BillModel bm = this.getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();

		for (int i = 0; i < templist.size(); i++) {
			bm.setValueAt(value, templist.get(i), colkey);
		}

	}
}