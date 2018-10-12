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
			MessageDialog.showHintDlg(card, "��ʾ", "�������Ͳ���Ϊ�գ�");
			return;
		}
		if (stime == null || "".equals(stime)) {
			MessageDialog.showHintDlg(card, "��ʾ", "��ʼ���ڲ���Ϊ�գ�");
			return;
		}
		strWhere += " where t.bill_date>='" + stime + "'";
		if (etime != null && !"".equals(etime)) {
			strWhere +=" and t.bill_date<='"+etime+"'";
		}
		StringBuffer sql = new StringBuffer();
		if ("��������".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere2=strWhere.replaceAll("t.bill_date", "t.djrq");
			String strWhere3=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere4=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			sql.append("select * from (");
			sql.append(" select t.csaleid as billpk, 1 as seq, '���۶���' as billtype, t.vreceiptcode as billcode from so_sale t");
			sql.append(strWhere1);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.csaleid = t.csaleid)");
			sql.append(" union");
			sql.append(" select t.vouchid, 2, '�����տ' as billtype, t.djbh from arap_djzb t");
			sql.append(strWhere2);
			sql.append(" and nvl(t.dr,0)=0 and t.djlxbm = 'D2' and exists(select 1 from bxgt_isedit d where d.vouchid = t.vouchid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,3, '����Ʒ��ⵥ' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere3);
			sql.append(" and t.cbilltypecode = '46' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,4, '���۳��ⵥ' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere4);
			sql.append(" and t.cbilltypecode = '4C' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append("  )order by seq asc ,billcode asc ");
		} else if ("�ɹ�����".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dpraydate");
			String strWhere2=strWhere.replaceAll("t.bill_date", "t.dorderdate");
			String strWhere3=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			String strWhere4=strWhere.replaceAll("t.bill_date", "t.dinvoicedate");
			sql.append("select * from (");
			sql.append(" select t.cpraybillid as billpk,1 as seq, '�빺��' as billtype, t.vpraycode as billcode from po_praybill t");
			sql.append(strWhere1);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.cpraybillid = t.cpraybillid)");
			sql.append(" union");
			sql.append(" select t.corderid,2,'�ɹ�����' as seq,t.vordercode from po_order t");
			sql.append(strWhere2);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.corderid = t.corderid)");
			sql.append(" union");
			sql.append(" select t.cgeneralhid,3, '�ɹ���ⵥ' as billtype, t.vbillcode from ic_general_h t");
			sql.append(strWhere3);
			sql.append(" and t.cbilltypecode = '45' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" union");
			sql.append(" select t.cinvoiceid,4,'�ɹ���Ʊ' as billtype,t.vinvoicecode from po_invoice t");
			sql.append(strWhere4);
			sql.append(" and nvl(t.dr,0)=0 and exists (select 1 from bxgt_isedit d where d.cinvoiceid = t.cinvoiceid)");
			sql.append(" )order by seq asc,billcode asc ");
		} else if ("���ϳ���".equals(billtype)) {
			String strWhere1=strWhere.replaceAll("t.bill_date", "t.dbilldate");
			sql.append("select t.cgeneralhid,1 as seq, '���ϳ��ⵥ' as billtype, t.vbillcode as billcode from ic_general_h t");
			sql.append(strWhere1);
			sql.append(" and t.cbilltypecode = '4D' and nvl(t.dr,0) =0 and exists(select 1 from bxgt_isedit d where d.cgeneralhid = t.cgeneralhid)");
			sql.append(" order by t.vbillcode asc ");
		}
		String sqla=sql.toString();
		
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<Map<String,Object>> list=(List<Map<String,Object>>)iQ.executeQuery(sqla, new MapListProcessor());
		if(list==null||list.size()<=0){
			MessageDialog.showHintDlg(card, "��ʾ", "�����ݣ�");
			return ;
		}
		//Ϊ���帳ֵ(ÿ�����)
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
	 * ����Excel
	 * @throws Exception
	 */
	public void onPrintExcel() throws Exception {

		BillCardPanel card=this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel=card.getBillModel();
		int row=bmodel.getRowCount();
		if(row<=0){
			MessageDialog.showHintDlg(card, "��ʾ", "�����ݵ�����");
			return ;
		}
		//����
		String path=ExcelUtils.getChooseFilePath(card, "��ӡ�ĵ���");
		//�жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			MessageDialog.showHintDlg(card, "��ʾ", "��ȡ������������ʧ�ܣ�");
			return;
		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}
		//�ȶԱ������map
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
		excelTools.createSheet("��ӡ����");
		//���ݲ�Ϊ��
		excelTools.createRow(0);
		short i=0;
		while(it.hasNext()){
			String key=it.next();
			//����������
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
			// ����Object����
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
