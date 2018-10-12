package nc.ui.bxgt.print;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.pub.ExcelTools;
import nc.ui.bxgt.pub.ExcelUtils;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class MyEventHandler extends CardEventHandler {

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == IBxgtButton.PRINT_EXCEL) {
			this.onPrintExcel();
		}
	}

	@Override
	protected void onBoQuery() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object billtype = card.getHeadItem("billtype").getValueObject();
		Object stime = card.getHeadItem("stime").getValueObject();
		Object etime = card.getHeadItem("etime").getValueObject();

		String strWhere = "";

		if (billtype == null || "".equals(billtype)) {
			MessageDialog.showHintDlg(card, "提示", "单据类型不可为空！");
			return;
		}
		if (stime == null || "".equals(stime)) {
			MessageDialog.showHintDlg(card, "提示", "起始日期不可为空！");
			return;
		}
		strWhere += " where t.bill_date>='" + stime + "'";
		if (etime != null && !"".equals(etime)) {
			strWhere +=" and t.bill_date<='"+etime+"'";
		}
		StringBuffer sql = new StringBuffer();
		if ("销售流程".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere2=strWhere.replaceAll("t.bill_date", "t.djrq");
			String strWhere3=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere4=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			sql.append("select * from (");
			sql.append(" select t.csaleid as billpk, 1 as seq, '销售订单' as billtype, t.vreceiptcode as billcode from so_sale t");
			sql.append(strWhere1);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.csaleid = t.csaleid)");
			sql.append(" union");
			sql.append(" select t.vouchid, 2, '销售收款单' as billtype, t.djbh from arap_djzb t");
			sql.append(strWhere2);
			sql.append(" and nvl(t.dr,0)=0 and t.djlxbm = 'D2' and exists(select 1 from bxgt_isedit d where d.vouchid = t.vouchid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,3, '产成品入库单' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere3);
			sql.append(" and t.cbilltypecode = '46' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,4, '销售出库单' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere4);
			sql.append(" and t.cbilltypecode = '4C' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append("  )order by seq asc ,billcode asc ");
		} else if ("采购流程".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dpraydate");
			String strWhere2=strWhere.replaceAll("t.bill_date", "t.dorderdate");
			String strWhere3=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere4=strWhere.replaceAll("t.bill_date", "t.dinvoicedate");
			sql.append("select * from (");
			sql.append(" select t.cpraybillid as billpk,1 as seq, '请购单' as billtype, t.vpraycode as billcode from po_praybill t");
			sql.append(strWhere1);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.cpraybillid = t.cpraybillid)");
			sql.append(" union");
			sql.append(" select t.corderid,2,'采购订单' as seq,t.vordercode from po_order t");
			sql.append(strWhere2);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.corderid = t.corderid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,3, '采购入库单' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere3);
			sql.append(" and t.cbilltypecode = '45' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" union");
			sql.append(" select t.cinvoiceid,4,'采购发票' as billtype,t.vinvoicecode from po_invoice t");
			sql.append(strWhere4);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.cinvoiceid = t.cinvoiceid)");
			sql.append(" )order by seq asc,billcode asc ");
		} else if ("材料出库".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			sql.append("select t.cgeneralhid,1 as seq, '材料出库单' as billtype, t.vbillcode as billcode from ic_general_h t");
			sql.append(strWhere1);
			sql.append(" and t.cbilltypecode = '4D' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" order by t.vbillcode asc ");
		}
		String sqla=sql.toString();
		
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<Map<String,Object>> list=(List<Map<String,Object>>)iQ.executeQuery(sqla, new MapListProcessor());
		if(list==null||list.size()<=0){
			MessageDialog.showHintDlg(card, "提示", "无数据！");
			return ;
		}
		//为表体赋值(每次清空)
		BillModel bmodel=card.getBillModel();
		bmodel.clearBodyData();
		for(int i=0;i<list.size();i++){
			bmodel.addLine();
			Map<String,Object> map=list.get(i);
			bmodel.setValueAt(map.get("billtype"), i, "billtype");
			bmodel.setValueAt(map.get("billcode"), i, "billno");
		}
		return ;
	}

	/**
	 * 导出Excel
	 * @throws Exception
	 */
	public void onPrintExcel() throws Exception {

		BillCardPanel card=this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel=card.getBillModel();
		int row=bmodel.getRowCount();
		if(row<=0){
			MessageDialog.showHintDlg(card, "提示", "无数据导出！");
			return ;
		}
		//导出
		String path=ExcelUtils.getChooseFilePath(card, "打印的单据");
		//判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "提示", "已取消操作，导出失败！");
			return;
		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}
		//先对表体存入map
		List<String> list=null;
		LinkedHashMap<String, List<String>> map=new LinkedHashMap<String, List<String>>();
		for(int i=0;i<row;i++){
			String billtype=bmodel.getValueAt(i, "billtype").toString();
			String billno=bmodel.getValueAt(i, "billno").toString();
			
			if(!map.containsKey(billtype)){
				list=new ArrayList<String>();
			}else{
				list=map.get(billtype);
			}
			list.add(billno);
			map.put(billtype, list);
		}
		
		exportExcel(map,path);
	}
	
	private void exportExcel(LinkedHashMap<String, List<String>> map,String path)throws Exception{
		
		Iterator<String> it=map.keySet().iterator();
		Iterator<String> it2=map.keySet().iterator();
		
		OutputStream os = new FileOutputStream(path);
		//String[] headColsCN = list.toArray(new String[0]);
		ExcelTools excelTools = new ExcelTools();
		excelTools.createSheet("打印单号");
		//数据不为空
		excelTools.createRow(0);
		short i=0;
		while(it.hasNext()){
			String key=it.next();
			//设置列名称
			excelTools.createCell(i);
			excelTools.setValue(0, i, key);
			i++;
		}
		i=0;
		while(it2.hasNext()){
			String key=it2.next();
			List<String> list=map.get(key);
			for(int k=0;k<list.size();k++){
				excelTools.createRow(k+1);
				excelTools.createCell(i);
				excelTools.setValue(list.get(k));
			}
			i++;
		}
		
		/*if (null != headColsCN) {
			for (short i = 0; i < headColsCN.length; i++) {
				excelTools.createCell(i);
				excelTools.setValue(headColsCN[i]);
			}
			// 定义Object数组
			Object[] array = null;
			for (int i = 0; i < map.size(); i++) {
				map.get(i)
				array = (Object[]) list.get(i);
				if (null != array) {
					excelTools.createRow(i + 1);
					for (short j = 0; j < array.length; j++) {
						excelTools.createCell(j);
						excelTools.setValue(array[j]);
					}
				}
			}
		}*/
		excelTools.writeExcel(os);
		os.close();
	}
	

}
