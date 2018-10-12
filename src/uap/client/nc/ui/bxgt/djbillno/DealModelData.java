package nc.ui.bxgt.djbillno;

import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.BillModel;

public class DealModelData {

	/** ���۶������洦�� */
	public static void dealSaleOrder(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("csaleid"), i, "csaleid");
			bmodel.setValueAt(map.get("vreceiptcode"), i, "djno_old");
			bmodel.setValueAt(map.get("dmakedate"), i, "djdate");
			bmodel.setValueAt(map.get("dbilltime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
			bmodel.setValueAt(map.get("nheadsummny"), i, "djamount");
			bmodel.setValueAt(map.get("ts"), i, "djts");
		}
	}

	/** ���۳��⻭�洦�� */
	public static void dealSaleOut(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("cgeneralhid"), i, "csaleid");
			bmodel.setValueAt(map.get("vbillcode"), i, "djno_old");
			bmodel.setValueAt(map.get("dbilldate"), i, "djdate");
			bmodel.setValueAt(map.get("tmaketime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
		}
	}
	
	/** �տ���洦�� */
	public static void dealSaleMoney(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("vouchid"), i, "csaleid");
			bmodel.setValueAt(map.get("djbh"), i, "djno_old");
			bmodel.setValueAt(map.get("djrq"), i, "djdate");
			bmodel.setValueAt(null, i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
			bmodel.setValueAt(map.get("ybje"), i, "djamount");
		}
	}
	
	/** Ԥ�տ���洦�� */
	public static void dealPlanMoney(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("vouchid"), i, "csaleid");
			bmodel.setValueAt(map.get("djbh"), i, "djno_old");
			bmodel.setValueAt(map.get("djrq"), i, "djdate");
			bmodel.setValueAt(null, i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
			bmodel.setValueAt(map.get("ybje"), i, "djamount");
		}
	}

	/** �ɹ��������洦�� */
	public static void dealPurchaseOrder(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("corderid"), i, "csaleid");
			bmodel.setValueAt(map.get("vordercode"), i, "djno_old");
			bmodel.setValueAt(map.get("dorderdate"), i, "djdate");
			bmodel.setValueAt(map.get("tmaketime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
		}
	}

	/** �ɹ���⻭�洦��* */
	public static void dealPurchaseIn(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("cgeneralhid"), i, "csaleid");
			bmodel.setValueAt(map.get("vbillcode"), i, "djno_old");
			bmodel.setValueAt(map.get("dbilldate"), i, "djdate");
			bmodel.setValueAt(map.get("tmaketime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
		}
	}

	/** �ɹ���Ʊ���洦��* */
	public static void dealPoInvoice(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("cinvoiceid"), i, "csaleid");
			bmodel.setValueAt(map.get("vinvoicecode"), i, "djno_old");
			bmodel.setValueAt(map.get("dinvoicedate"), i, "djdate");
			bmodel.setValueAt(map.get("tmaketime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
		}
	}
	
	/** ���ϳ��⻭�洦��**/
	public static void dealMaterialOut(BillModel bmodel,
			List<Map<String, Object>> listmap) {

		for (int i = 0; i < listmap.size(); i++) {
			bmodel.addLine();
			Map<String, Object> map = listmap.get(i);
			bmodel.setValueAt(map.get("cgeneralhid"), i, "csaleid");
			bmodel.setValueAt(map.get("vbillcode"), i, "djno_old");
			bmodel.setValueAt(map.get("dbilldate"), i, "djdate");
			bmodel.setValueAt(map.get("tmaketime"), i, "djtime");
			bmodel.setValueAt(map.get("custname"), i, "custname");
		}
	}
	
}
