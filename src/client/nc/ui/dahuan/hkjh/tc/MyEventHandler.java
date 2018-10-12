package nc.ui.dahuan.hkjh.tc;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dahuan.exceltools.ExcelUtils;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	protected void onBoExport() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		HkdhVO dhvo = (HkdhVO)aggvo.getParentVO();
		
		//回款单位
		String sql = "select b.custname from bd_cubasdoc b where b.pk_cubasdoc =(select m.pk_cubasdoc from bd_cumandoc m where m.pk_cumandoc = '"+dhvo.getPk_cust()+"')";
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String custname = (String)iQ.executeQuery(sql, new ColumnProcessor());
		
		String[] headCN = new String[]{
				"单据号",dhvo.getHkbillno(),
				"回款单位",custname,
				"总额",dhvo.getRmb_amount().toString()
		};
		
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] obj = new Object[7];
		list.add(obj);
		Object[] obj2 = new Object[]{
				"合同号","合同名称","项目名称","已分配金额","分配人","分配部门","分配日期"
		};
		list.add(obj2);
		
		BillModel bmodel = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int rows = bmodel.getRowCount();
		UFDouble hjamt = new UFDouble("0.00");
		
		for(int i=0;i<rows;i++){
			Object[] nobj = new Object[]{
				bmodel.getValueAt(i, "ctcode"),
				bmodel.getValueAt(i, "ctname"),
				bmodel.getValueAt(i, "xmname"),
				bmodel.getValueAt(i, "ct_amount"),
				bmodel.getValueAt(i, "vopername"),
				bmodel.getValueAt(i, "deptname"),
				bmodel.getValueAt(i, "dbilldate"),
			};
			list.add(nobj);
			hjamt = bmodel.getValueAt(i, "ct_amount")==null?hjamt:hjamt.add(new UFDouble(bmodel.getValueAt(i, "ct_amount").toString()),2);
		}
		
		Object[] xjobj = new Object[]{
			"小计",null,null,hjamt,null,null,null
		};
		list.add(xjobj);
		
		ExcelUtils.doExport("回款单", list, headCN, this.getBillUI());
		
		
	}

	@Override
	protected void onBoQuery() throws Exception {

		
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String corpid = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;
		
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";
		
		
		String sql = " select distinct dh_hkdh.* from dh_hkdh left join dh_hkdh_d on dh_hkdh.pk_hkdh = dh_hkdh_d.pk_hkdh and nvl(dh_hkdh_d.dr, 0) = 0 "+
			"  where nvl(dh_hkdh.dr, 0) = 0 and "+strWhere;
		
		int month = this._getDate().getMonth();
		int year = this._getDate().getYear();
		String stdate = "";
		if(month>6){
			
			int stmonth = month-6;
			if(stmonth>9){
				stdate = year+"-"+stmonth+"-01";
			}else{
				stdate = year+"-0"+stmonth+"-01";
			}
		}else{
			int styear = year-1;
			int stmonth = 6+month;
			if(stmonth>9){
				stdate = styear+"-"+stmonth+"-01";
			}else{
				stdate = styear+"-0"+stmonth+"-01";
			}
		}
		
		sql+=" and ((dh_hkdh.seal_flag<>1) or (dh_hkdh.seal_flag=1 and dh_hkdh.dbilldate>'"+stdate+"')) ";
		
		List<HkdhVO> queryVos = (List<HkdhVO>)iQ.executeQuery(sql, new BeanListProcessor(HkdhVO.class));
		
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos.toArray(new HkdhVO[0]));
	
		updateBuffer();

	
	}
	
}