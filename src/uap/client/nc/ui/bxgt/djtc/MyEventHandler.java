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

		// 查询模板
		HYQueryDLG querydialog = (HYQueryDLG) getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;

		// 查询条件处理
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

		// 单据模板PK
		String cardpk = "";

		// 主键PK
		String billpk = "";

		// 各个单据类型的单据查询
		if ("销售订单".equals(billtype)) {
			// 销售订单
			mapMap = ibxgt.bxgtQuerySaleOrder(strsoWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OL7M9"; // so000000000saleorder
				billpk = "csaleid"; // 2015-4-10 bwy 修改 caleid-->csaleid
			}
		} else if ("销售出库".equals(billtype)) {
			// 销售出库单
			mapMap = ibxgt.bxgtQuerySaleOutOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OLZJ0";
				billpk = "cgeneralhid";
			}
		} else if ("采购订单".equals(billtype)) {
			// 采购订单
			mapMap = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OP7X2";
				billpk = "corderid";//
			}
		} else if ("采购入库".equals(billtype)) {
			// 采购入库
			mapMap = ibxgt.bxgtQueryPurchaseInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OU75I";
				billpk = "cgeneralhid";
			}
		} else if ("采购发票".equals(billtype)) {
			// 采购发票
			mapMap = ibxgt.bxgtQueryPurchaseInvoiceOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUCKR";
				billpk = "cinvoiceid";
			}
		} else if ("材料出库".equals(billtype)) {
			// 材料出库
			mapMap = ibxgt.bxgtQueryMatInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUJ25";
				billpk = "cgeneralhid";
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "单据类型错误");
			return;
		}

		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();
		// 画面查询条件展示
		cardPanel.setHeadItem("zt", zt);
		cardPanel.setHeadItem("billpk", billpk);
		cardPanel.setHeadItem("bill_type", billtype);
		cardPanel.setHeadItem("bill_date", datefrom);
		cardPanel.setHeadItem("~", dateend);

		// 设置默认按钮显示
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
		// 根据查询条件控制按钮
		if ("外账套".equals(zt) && !"".equals(cardpk)) {
			this.getButtonManager().getButton(IBxgtButton.SYNCHRONOUS)
					.setEnabled(true);
		} else if ("本账套".equals(zt)) {
			if ("销售订单".equals(billtype) && !"".equals(cardpk)) {
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
			} else if (("采购订单".equals(billtype) || "材料出库".equals(billtype))
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
			} else if ("采购发票".equals(billtype) && !"".equals(cardpk)) {
				this.getButtonManager().getButton(IBxgtButton.TAX_RATE)
						.setEnabled(true);
			}
		}
		this.getBillUI().updateButtons();

		// 查询结果，表体的字段展示
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
		cardPanel.getBodyPanel().setTotalRowShow(true);// 合计
		cardPanel.setBodyMultiSelect(true);

		// 界面值的处理
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
	 * 同步单据
	 * 
	 * @throws Exception
	 */
	public void onBoSynchroBill() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();

		BillModel bmodel = card.getBillModel();
		int rowsum = bmodel.getRowCount();

		// 单据PK的规整
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请勾选需要同步的单据");
			return;
		}

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());
		Boolean retflag = false;

		// 各个单据类型的单据同步
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("销售订单".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroSaleOrder(pklist.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "销售订单同步成功！");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
			this
					.writeToExcel(pklist.toArray(new String[0]), billtype,
							templist);// 导出excel
			/*
			 * } else if ("发货单".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroDeliveryOrder(pklist .toArray(new String[0]));
			 */
		} else if ("销售出库".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroSaleOutOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "销售出库单同步成功！");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);

			/*
			 * } else if ("销售发票".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroSaleInvoiceOrder(pklist .toArray(new
			 * String[0]));
			 */
		} else if ("采购订单".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroPurchaseOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "采购订单同步成功！");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
			this
					.writeToExcel(pklist.toArray(new String[0]), billtype,
							templist);// 导出excel
			/*
			 * } else if ("到货单".equals(billtype)) { retflag =
			 * ibxgt.bxgtSynchroArriveOrder(pklist .toArray(new String[0]));
			 */
		} else if ("采购入库".equals(billtype)) {
			retflag = ibxgt
					.bxgtSynchroPurInOrder(pklist.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "采购入库单同步成功！");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);

		} else if ("采购发票".equals(billtype)) {
			retflag = ibxgt.bxgtSynchroPurInvoiceOrder(pklist
					.toArray(new String[0]));
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "采购发票同步成功！");
			this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
		} else if ("材料出库".equals(billtype)) {
			ArrayList<Object[]> list = ibxgt.bxgtSynMaterialOrder(pklist
					.toArray(new String[0]));
			if (null == list) {
				retflag = false;
			} else if (list.size() > 0) {
				int it = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示",
						"部分材料库存不足，是否导出excel？");
				if (it == UIDialog.ID_OK) {
					this.materialToExcel(list, BxgtStepButton.CLCK);
				}
				retflag = true;
			} else if (list.size() == 0) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "材料出库同步成功！");
				this.refreshData(billtype, BxgtStepButton.TONG_BU, templist);
				this.writeToExcel(pklist.toArray(new String[0]), billtype,
						templist);
				retflag = true;
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "单据类型错误");
			return;
		}

		if (!retflag) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "同步失败");
			return;
		}
	}

	/**
	 * 批量修改
	 * 
	 * @throws Exception
	 */
	public void onBoBatchEdit() throws Exception {
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (null == billtype || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "请勾选需要修改的单据");
			return;
		}

		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		BillModel bmodel = card.getBillModel();
		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();
		int rowsum = bmodel.getRowCount();

		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// 单据PK的规整
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				if (bmodel.getValueAt(i, "pk_sd") == Boolean.FALSE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "错误",
							"包含未锁定的单据，无法批改！");
					return;
				} else if (bmodel.getValueAt(i, "pk_qr") == Boolean.TRUE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "错误",
							"包含已确认的单据，无法批改！");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请勾选需要修改的单据");
			return;
		}
		ProcessEditDlg editDlg = new ProcessEditDlg(this.getBillUI(), "批量修改",
				pklist.toArray(new String[0]));
		int flag = editDlg.showModal();
		if (flag == UIDialog.ID_OK) {
			// 2015-4-10 bwy 批改
			String[] pks = pklist.toArray(new String[0]);
			int days = editDlg.getValue2();
			String strWhere = "";
			// 单据模板PK
			String cardpk = "";
			Map<Integer, Map<String, Object>> map = null;
			if ("销售订单".equals(billtype)) {
				strWhere = "and a.csaleid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "本账套";
				ibxgt.bxgtBatchUpdateSaleOrder(pks, days);
				MessageDialog.showHintDlg(this.getBillUI(), "提示 ", "批改成功！");
				// 批改后刷新界面
				/*
				 * map = ibxgt.bxgtQuerySaleOrder(strWhere, zt); if (map != null &&
				 * map.size() > 0) { cardpk = "0001AA100000000OL7M9"; billpk =
				 * "csaleid"; }
				 */
				this.onBoRefreshes();

			} else if ("采购订单".equals(billtype)) {
				strWhere = "and o.corderid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "本账套";

				ArrayList<Object[]> list = ibxgt.bxgtBatchUpdatePurchaseOrder(
						pks, days);
				if (list == null) {
					throw new BusinessException("未找到下游入库单！");
				} else if (list.size() > 0) {
					int it = MessageDialog.showOkCancelDlg(this.getBillUI(),
							"提示 ", "批改后库存可用量不足，是否导出excel？");
					if (it == UIDialog.ID_OK) {
						this.materialToExcel(list, BxgtStepButton.RKPG);
					}
					return;
				}
				MessageDialog.showHintDlg(this.getBillUI(), "提示 ", "批改成功！");
				// 批改后刷新界面
				/*
				 * map = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt); if (map !=
				 * null && map.size() > 0) { cardpk = "0001AA100000000OP7X2";
				 * billpk = "corderid"; }
				 */
				this.onBoRefreshes();
			} else if ("材料出库".equals(billtype)) {
				strWhere = "and a.cgeneralhid in ('";
				for (int i = 0; i < pks.length; i++) {
					strWhere += pks[i] + "','";
				}
				strWhere = strWhere.substring(0, strWhere.lastIndexOf(","))
						+ ")";
				String zt = "本账套";
				ArrayList<Object[]> list = ibxgt.bxgtBatchUpdateMaterialOrder(
						pks, days);
				if (list == null) {
					throw new BusinessException("数据异常！");
				} else if (list.size() > 0) {
					int it = MessageDialog.showOkCancelDlg(this.getBillUI(),
							"提示 ", "批改后库存可用量不足，是否导出excel？");
					if (it == UIDialog.ID_OK) {
						this.materialToExcel(list, BxgtStepButton.CKPG);
					}
					return;
				}
				MessageDialog.showHintDlg(this.getBillUI(), "提示 ", "批改成功！");
				// 批改后刷新界面
				/*
				 * map = ibxgt.bxgtQueryMatInOrder(strWhere, zt); if (map !=
				 * null && map.size() > 0) { cardpk = "0001AA100000000OUJ25";
				 * billpk = "cgeneralhid"; }
				 */
				this.onBoRefreshes();
			}

			/*
			 * // 查询结果，表体的字段展示 BillModel bmodel2 = cardPanel.getBillModel();
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
			 * cardPanel.setBodyMultiSelect(true); // 界面值的处理 if (null != map &&
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
	 * 锁定单据
	 */
	public void onBoLock() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();

		BillModel bmodel = card.getBillModel();
		int rowsum = bmodel.getRowCount();

		// 单据PK的规整
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED
					&& Boolean.FALSE == bmodel.getValueAt(i, "pk_sd")) {
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请勾选需要锁定或未锁定的单据");
			return;
		}
		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// 各个单据类型的单据锁定
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("销售订单".equals(billtype)) {
			String[] billcodes = ibxgt.bxgtLockSaleOrders(pklist
					.toArray(new String[0]));
			String str = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (billcodes != null && billcodes.length > 0) {
				for (int i = 0; i < billcodes.length; i++) {
					str += "【" + billcodes[i] + "】";
					map.put(billcodes[i], null);
				}
				str = "单据号为" + str + "的单据流程未走完，其他单据锁定成功！";
				for (int i = templist.size() - 1; i >= 0; i--) {
					String billno = bmodel.getValueAt(templist.get(i),
							"vreceiptcode").toString();
					if (map.containsKey(billno)) {
						templist.remove(i);
					}
				}
			} else {
				str = "销售订单锁定成功！";
			}
			MessageDialog.showHintDlg(this.getBillUI(), "提示", str);
			this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
		} else if ("采购订单".equals(billtype)) {
			String[] billcodes = ibxgt.bxgtLockPurchaseOrders(pklist
					.toArray(new String[0]));
			String str = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (billcodes != null && billcodes.length > 0) {// 对templist做修改
				for (int i = 0; i < billcodes.length; i++) {
					str += "【" + billcodes[i] + "】";
					map.put(billcodes[i], null);
				}
				str = "单据号为" + str + "的单据流程未走完，其他单据锁定成功！";
				for (int i = templist.size() - 1; i >= 0; i--) {
					String billno = bmodel.getValueAt(templist.get(i),
							"vordercode").toString();
					if (map.containsKey(billno)) {
						templist.remove(i);
					}
				}
			} else {
				str = "采购订单锁定成功！";
			}
			MessageDialog.showHintDlg(this.getBillUI(), "提示", str);
			this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
			return;
		} else if ("材料出库".equals(billtype)) {
			String str = ibxgt.bxgtLockMaterialOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "材料出库锁定成功！");
				this.refreshData(billtype, BxgtStepButton.SUO_DAN, templist);
				return;
			} else {
				MessageDialog.showErrorDlg(this.getBillUI(), "提示", "锁单失败 ！");
				return;
			}
		}
	}

	/**
	 * 解锁单据
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

		// 单据PK的规整
		List<String> pklist = new ArrayList<String>();
		for (int i = 0; i < rowsum; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED
					&& bmodel.getValueAt(i, "pk_sd") == Boolean.TRUE) {
				if (bmodel.getValueAt(i, "pk_qr") == Boolean.TRUE) {
					MessageDialog.showHintDlg(this.getBillUI(), "提示",
							"包含确认的单据，无法解锁！");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请勾选需要解除锁定的单据");
			return;
		}
		IbxgtQuerySynchro ibxgt = (IbxgtQuerySynchro) NCLocator.getInstance()
				.lookup(IbxgtQuerySynchro.class.getName());

		// 各个单据类型的单据锁定
		String billtype = card.getHeadItem("bill_type").getValueObject()
				.toString();
		if ("销售订单".equals(billtype)) {
			String str = ibxgt.bxgtUnLockSaleOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "订单解除锁定成功！");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "订单解除锁定失败！");
				return;
			}
		} else if ("采购订单".equals(billtype)) {
			String str = ibxgt.bxgtUnLockPurchaseOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "采购订单解锁成功！");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "采购订单解锁失败！");
				return;
			}
		} else if ("材料出库".equals(billtype)) {
			String str = ibxgt.bxgtUnLockMaterialOrders(pklist
					.toArray(new String[0]));
			if ("".equals(str)) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "材料出库解锁成功！");
				this.refreshData(billtype, BxgtStepButton.JIE_SUO, templist);
				return;
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "解锁失败！");
				return;
			}
		}
	}

	/**
	 * 同步预收款单
	 * 
	 * @throws Exception
	 */
	public void onAdvanceReceive() throws Exception {
		// 预收款单同步 xm 2015.5.6
		IbxgtQuerySynchro ibar = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibar.getNeedPks();
		MessageDialog.showHintDlg(this.getBillUI(), "提示", "同步预收款单成功！");
	}

	/**
	 * 确认标志
	 */
	public void onBoMark() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "请先查询出数据！");
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
					MessageDialog.showErrorDlg(this.getBillUI(), "错误",
							"包含未锁定的单据，无法确认！");
					return;
				}
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.confirmBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.QUE_REN, templist);
	}

	/**
	 * 取消标记
	 */
	public void onBoCancel() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "请先查询出数据！");
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
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
			return;
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);

		ibqs.unConfirmBill(pklist.toArray(new String[0]), billtype.toString());
		this.refreshData(billtype.toString(), BxgtStepButton.QU_XIAO, templist);
	}

	/**
	 * 发票税率修改
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
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
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
		MessageDialog.showHintDlg(card, "提示", "更改成功！");

	}

	/**
	 * 销售订单客户金额修改
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
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
			return;
		}
		CustMnyDlg cMny = new CustMnyDlg(card);

		if (!cMny.isFlag()) {// 非确定按钮
			return;
		}

		Object cust = cMny.getPk_cust();// 客户
		Object obj = cMny.getValue1(); // 金额
		if (obj != null && !"".equals(obj)) {
			if (pklist.size() > 1) {
				MessageDialog.showHintDlg(card, "提示", "批改金额不支持多条数据！");
				return;
			}
		}

		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibqs.batchCustAndMoney(pklist.toArray(new String[0]), cust, obj);

		// 修改后刷新界面
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if (pklist.size() == 1) {// 修改金额（肯定）
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
		MessageDialog.showHintDlg(card, "提示", "更改成功！");

	}

	/**
	 * 删除功能
	 */
	public void onBoDelete() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		Object billtype = card.getHeadItem("bill_type").getValueObject();
		String billpk = card.getHeadItem("billpk").getValueObject().toString();
		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "请先查询出数据！");
			return;
		}
		BillModel bmodel = card.getBillModel();
		int row = bmodel.getRowCount();
		List<String> pklist = new ArrayList<String>();
		ArrayList<Integer> templist = new ArrayList<Integer>();
		for (int i = 0; i < row; i++) {
			if (bmodel.getRowState(i) == BillModel.SELECTED) {
				if (bmodel.getValueAt(i, "pk_sd") == Boolean.TRUE) {
					MessageDialog.showErrorDlg(this.getBillUI(), "错误",
							"包含锁定的单据，无法删除！");
					return;
				}
				pklist.add(bmodel.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}
		if (null == pklist || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
			return;
		}

		// 确认删除功能
		int it = MessageDialog.showYesNoDlg(card, "提示", "是否确认删除？");
		if (it != UIDialog.ID_YES) {
			return;
		}
		int it2 = MessageDialog.showYesNoDlg(card, "提示", "再次确认是否删除？删除后概不负责！");
		if (it2 != UIDialog.ID_YES) {
			return;
		}
		IbxgtQuerySynchro ibqs = NCLocator.getInstance().lookup(
				IbxgtQuerySynchro.class);
		ibqs.deleteSaleOrOrder(pklist.toArray(new String[0]), billtype
				.toString());
		// 删除前台数据
		for (int i = templist.size(); i > 0; i--) {
			int k = templist.get(i - 1);
			card.getBodyPanel().delLine(new int[] { k });
		}
		MessageDialog.showHintDlg(card, "提示", "删除成功！");
	}

	public void onOrderSeq() throws Exception {

		ArrayList<Integer> templist = new ArrayList<Integer>();
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("bill_type").getValueObject();

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "请先查询出数据！");
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
					MessageDialog.showErrorDlg(this.getBillUI(), "错误",
							"包含未确认的单据，无法排序！");
					return;
				}
				pklist.add(bm.getValueAt(i, billpk).toString());
				templist.add(i);
			}
		}

		if (pklist == null || pklist.size() <= 0) {
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
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
			MessageDialog.showHintDlg(card, "提示", "请先查询出数据！");
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
			MessageDialog.showHintDlg(card, "提示", "请先选择数据！");
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
			// 同步
			onBoSynchroBill();
		} else if (intBtn == IBxgtButton.BATCH_EDIT) {
			// 批改
			onBoBatchEdit();
		} else if (intBtn == IBxgtButton.PRE_PAYMENT) {
			// 同步预收款单
			onAdvanceReceive();
		} else if (intBtn == IBxgtButton.LOCK) {
			// 锁定
			onBoLock();
		} else if (intBtn == IBxgtButton.UNLOCK) {
			// 解锁
			onBoUnLock();
		} else if (intBtn == IBxgtButton.OKMARK) {
			// 确认标志
			onBoMark();
		} else if (intBtn == IBxgtButton.CANCEL) {
			// 取消标志
			onBoCancel();
		} else if (intBtn == IBxgtButton.TAX_RATE) {
			// 发票税率
			onTaxEdit();
		} else if (intBtn == IBxgtButton.CUST_MNY) {
			// 客户金额修改
			onCustMny();
		} else if (intBtn == IBxgtButton.DELBILL) {
			// 删除
			onBoDelete();
		} else if (intBtn == IBxgtButton.ORDERSEQ) {
			// 排序
			onOrderSeq();
		} else if (intBtn == IBxgtButton.CANCELORDER) {
			// 取消排序
			onCancelSeq();
		}
	}

	public void onBoRefreshes() throws Exception {

		// 查询模板
		HYQueryDLG querydialog = (HYQueryDLG) getQueryUI();

		// 查询条件处理
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

		// 单据模板PK
		String cardpk = "";

		// 各个单据类型的单据查询
		if ("销售订单".equals(billtype)) {
			// 销售订单
			mapMap = ibxgt.bxgtQuerySaleOrder(strsoWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OL7M9"; // so000000000saleorder
			}
		} else if ("销售出库".equals(billtype)) {
			// 销售出库单
			mapMap = ibxgt.bxgtQuerySaleOutOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OLZJ0";
			}
		} else if ("采购订单".equals(billtype)) {
			// 采购订单
			mapMap = ibxgt.bxgtQueryPurchaseOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OP7X2";
			}
		} else if ("采购入库".equals(billtype)) {
			// 采购入库
			mapMap = ibxgt.bxgtQueryPurchaseInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OU75I";
			}
		} else if ("采购发票".equals(billtype)) {
			// 采购发票
			mapMap = ibxgt.bxgtQueryPurchaseInvoiceOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUCKR";
			}
		} else if ("材料出库".equals(billtype)) {
			// 材料出库
			mapMap = ibxgt.bxgtQueryMatInOrder(strWhere, zt);
			if (null != mapMap && mapMap.size() > 0) {
				cardpk = "0001AA100000000OUJ25";
			}
		} else {
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "单据类型错误");
			return;
		}

		BillCardPanel cardPanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();

		// 查询结果，表体的字段展示
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

		// 界面值的处理
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
	 * 导出到excel
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

		if ("销售订单".equals(billtype)) {
			coyle = "vreceiptcode";
		} else if ("采购订单".equals(billtype)) {
			coyle = "vordercode";
		} else if ("材料出库".equals(billtype)) {
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

		String path = getChooseFilePath(card, "导出文件");
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "提示", "已取消操作，导出失败！");
			return;
		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}

		OutputStream os = new FileOutputStream(path);

		String[] headColsCN = new String[] { "单据类型", "单据号" };

		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("导出数据");
		// 判断查询出的数据是否为空
		if (CollectionUtils.isEmpty(list)) {
			excelTools.createRow(0);
			short ct = 0;
			excelTools.createCell(ct);
			excelTools.setValue("无数据！");

		} else {
			excelTools.createRow(0);
			if (null != headColsCN) {
				for (short i = 0; i < headColsCN.length; i++) {
					excelTools.createCell(i);
					excelTools.setValue(headColsCN[i]);
				}
				// 定义Object数组
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
	 * 导出到excel库存不足
	 * 
	 * @param pksales
	 * @throws Exception
	 */
	private void materialToExcel(ArrayList<Object[]> list, int num)
			throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		String path = getChooseFilePath(card, "库存物料导出");
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "提示", "已取消操作，导出失败！");
			return;
		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}

		OutputStream os = new FileOutputStream(path);

		String[] headColsCN = null;
		if (num == BxgtStepButton.CLCK) {
			headColsCN = new String[] { "公司", "库存组织", "仓库", "物料", "规格型号",
					"本次出库总量", "库存可用量" };
		} else if (num == BxgtStepButton.RKPG || num == BxgtStepButton.CKPG) {
			headColsCN = new String[] { "公司", "库存组织", "仓库", "物料", "规格型号",
					"批改后库存可用量" };
		}

		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("导出数据");
		// 判断查询出的数据是否为空
		if (CollectionUtils.isEmpty(list)) {
			excelTools.createRow(0);
			short ct = 0;
			excelTools.createCell(ct);
			excelTools.setValue("无数据！");

		} else {
			excelTools.createRow(0);
			if (null != headColsCN) {
				for (short i = 0; i < headColsCN.length; i++) {
					excelTools.createCell(i);
					excelTools.setValue(headColsCN[i]);
				}
				// 定义Object数组
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
	 * 文件路径
	 */
	private String getChooseFilePath(BillCardPanel card, String defaultFileName) {
		// 新建一个文件选择框
		JFileChooser fileChooser = new JFileChooser();
		// 设置默认文件名
		fileChooser.setSelectedFile(new File(defaultFileName));
		// 打开保存框
		int retVal = fileChooser.showSaveDialog(card);
		// 定义返回变量
		String path = null;
		// 判断是否打开
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// 确认打开，获取选择的路径
			path = fileChooser.getSelectedFile().getPath();
		}
		// 返回路径
		return path;
	}

	/**
	 * 刷新界面数据
	 */
	private void refreshData(String billtype, int stepType,
			ArrayList<Integer> templist) {

		if (templist == null || templist.size() <= 0) {
			return;
		}
		String colkey = "";
		String value = "";
		if ("销售订单".equals(billtype) || "采购订单".equals(billtype)
				|| "材料出库".equals(billtype)) {
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
		} else if ("销售出库".equals(billtype) || "采购入库".equals(billtype)
				|| "采购发票".equals(billtype)) {
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