package nc.ui.dahuan.hkjh.yr;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends ListEventHandler {

	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoQuery() throws Exception {}

	@Override
	protected void onBoImport() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择付款单");
			return;
		}
		DhFkjhbillVO dvo = (DhFkjhbillVO)aggvo.getParentVO();
		//判断回款类型
		String pkFkFs = dvo.getPk_fkfs();
		String fkfssql = "select t.balanname from bd_balatype t where t.pk_balatype = '"+pkFkFs+"'";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String balaname = (String)iQ.executeQuery(fkfssql, new ColumnProcessor());
		
		HuiKuanDialog dg = new HuiKuanDialog(this.getBillUI());
		if(dg.showHuiKuanDialog(balaname)){
			if("电汇".equals(dg.getBalaname())){
				dhImport(dvo,dg.getVbillcode(),iQ);
			}else if("承兑".equals(dg.getBalaname())){
				cdImport(dvo,dg.getVbillcode(),iQ);
			}else if("其他".equals(dg.getBalaname())){
				qtImport(dvo,dg.getVbillcode(),iQ);
			}
			
			ClientUI ui = (ClientUI)this.getBillUI();
			ui.initBillData("");
		}
	}
	
	// 电汇
	private void dhImport(DhFkjhbillVO dvo,String vbillcode,IUAPQueryBS iQ) throws Exception{
		
		String consql = " select f.relationid from dh_contract f where f.ctcode = " +
				" (select t.ctcode from dh_fkjhbill t where t.pk_fkjhbill = '"+dvo.getPk_fkjhbill()+"') ";
		String pkCon = (String)iQ.executeQuery(consql, new ColumnProcessor());
		
		DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkCon);
		
		String invsql = "select c.invname from bd_invbasdoc c where c.pk_invbasdoc = '"+ctvo.getCtname()+"' ";
		String invName = (String)iQ.executeQuery(invsql, new ColumnProcessor());
		
		HkdhVO hvo = new HkdhVO();
		hvo.setDbilldate(this._getDate());
		hvo.setHk_amount(dvo.getDfkje());
		hvo.setPk_cust(ctvo.getPk_cust1());
		hvo.setVoperid(this._getOperator());
		hvo.setRemark("回款引入："+dvo.getCtcode());
		hvo.setDr(0);
		hvo.setRmb_amount(dvo.getCurrenty_amount());
		hvo.setBzbl(dvo.getCurrenty_rate());
		hvo.setPk_bizong(dvo.getPk_currenty());
		hvo.setHkbillno(vbillcode);
		hvo.setBill_flag(0);
		hvo.setSure_flag(1);
		hvo.setVemo(dvo.getFknr());
		hvo.setPk_corp(this._getCorp().getPk_corp());
		hvo.setIs_relation(1);
		hvo.setRelationid(dvo.getPk_fkjhbill());
		String pkHvo = HYPubBO_Client.insert(hvo);
		
		HkdhDVO bvo = new HkdhDVO();
		bvo.setPk_hkdh(pkHvo);
		bvo.setPk_contract(pkCon);
		bvo.setCtcode(ctvo.getCtcode());
		bvo.setCtname(invName);
		bvo.setXmname(ctvo.getVdef6());
		bvo.setCt_amount(dvo.getDfkje());
		bvo.setVoperid(ctvo.getVoperatorid());
		bvo.setPk_dept(ctvo.getPk_deptdoc());
		bvo.setDbilldate(this._getDate());
		String pkBvo = HYPubBO_Client.insert(bvo);
		
		UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
		ctvo.setLjfkjhje(ljfkjhje.add(dvo.getDfkje(), 2));
		HYPubBO_Client.update(ctvo);
		
		dvo.setIs_relation(1);
		dvo.setRelationid(pkHvo);
		HYPubBO_Client.update(dvo);
		
	}
	// 承兑
	private void cdImport(DhFkjhbillVO dvo,String vbillcode,IUAPQueryBS iQ) throws Exception{
		String consql = " select f.relationid from dh_contract f where f.ctcode = " +
						" (select t.ctcode from dh_fkjhbill t where t.pk_fkjhbill = '"+dvo.getPk_fkjhbill()+"') ";
		String pkCon = (String)iQ.executeQuery(consql, new ColumnProcessor());
		
		DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkCon);
		
		String invsql = "select c.invname from bd_invbasdoc c where c.pk_invbasdoc = '"+ctvo.getCtname()+"' ";
		String invName = (String)iQ.executeQuery(invsql, new ColumnProcessor());
		
		HkdhVO hvo = new HkdhVO();
		hvo.setDbilldate(this._getDate());
		hvo.setHk_amount(dvo.getDfkje());
		hvo.setPk_cust(ctvo.getPk_cust1());
		hvo.setVoperid(this._getOperator());
		hvo.setRemark("回款引入："+dvo.getCtcode());
		hvo.setDr(0);
		hvo.setRmb_amount(dvo.getCurrenty_amount());
		hvo.setBzbl(dvo.getCurrenty_rate());
		hvo.setPk_bizong(dvo.getPk_currenty());
		hvo.setHkbillno(vbillcode);
		hvo.setBill_flag(1);
		hvo.setSure_flag(1);
		hvo.setVemo(dvo.getFknr());
		hvo.setPk_corp(this._getCorp().getPk_corp());
		hvo.setIs_relation(1);
		hvo.setRelationid(dvo.getPk_fkjhbill());
		String pkHvo = HYPubBO_Client.insert(hvo);
		
		HkdhDVO bvo = new HkdhDVO();
		bvo.setPk_hkdh(pkHvo);
		bvo.setPk_contract(pkCon);
		bvo.setCtcode(ctvo.getCtcode());
		bvo.setCtname(invName);
		bvo.setXmname(ctvo.getVdef6());
		bvo.setCt_amount(dvo.getDfkje());
		bvo.setVoperid(ctvo.getVoperatorid());
		bvo.setPk_dept(ctvo.getPk_deptdoc());
		bvo.setDbilldate(this._getDate());
		String pkBvo = HYPubBO_Client.insert(bvo);
		
		UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
		ctvo.setLjfkjhje(ljfkjhje.add(dvo.getDfkje(), 2));
		HYPubBO_Client.update(ctvo);
		
		dvo.setIs_relation(1);
		dvo.setRelationid(pkHvo);
		HYPubBO_Client.update(dvo);
	}
	// 其他
	private void qtImport(DhFkjhbillVO dvo,String vbillcode,IUAPQueryBS iQ) throws Exception{
		String consql = " select f.relationid from dh_contract f where f.ctcode = " +
						" (select t.ctcode from dh_fkjhbill t where t.pk_fkjhbill = '"+dvo.getPk_fkjhbill()+"') ";
		String pkCon = (String)iQ.executeQuery(consql, new ColumnProcessor());
		
		DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkCon);
		
		String invsql = "select c.invname from bd_invbasdoc c where c.pk_invbasdoc = '"+ctvo.getCtname()+"' ";
		String invName = (String)iQ.executeQuery(invsql, new ColumnProcessor());
		
		HkdhVO hvo = new HkdhVO();
		hvo.setDbilldate(this._getDate());
		hvo.setHk_amount(dvo.getDfkje());
		hvo.setPk_cust(ctvo.getPk_cust1());
		hvo.setVoperid(this._getOperator());
		hvo.setRemark("回款引入："+dvo.getCtcode());
		hvo.setDr(0);
		hvo.setRmb_amount(dvo.getCurrenty_amount());
		hvo.setBzbl(dvo.getCurrenty_rate());
		hvo.setPk_bizong(dvo.getPk_currenty());
		hvo.setHkbillno(vbillcode);
		hvo.setBill_flag(2);
		hvo.setSure_flag(1);
		hvo.setVemo(dvo.getFknr());
		hvo.setPk_corp(this._getCorp().getPk_corp());
		hvo.setIs_relation(1);
		hvo.setRelationid(dvo.getPk_fkjhbill());
		String pkHvo = HYPubBO_Client.insert(hvo);
		
		HkdhDVO bvo = new HkdhDVO();
		bvo.setPk_hkdh(pkHvo);
		bvo.setPk_contract(pkCon);
		bvo.setCtcode(ctvo.getCtcode());
		bvo.setCtname(invName);
		bvo.setXmname(ctvo.getVdef6());
		bvo.setCt_amount(dvo.getDfkje());
		bvo.setVoperid(ctvo.getVoperatorid());
		bvo.setPk_dept(ctvo.getPk_deptdoc());
		bvo.setDbilldate(this._getDate());
		String pkBvo = HYPubBO_Client.insert(bvo);
		
		UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
		ctvo.setLjfkjhje(ljfkjhje.add(dvo.getDfkje(), 2));
		HYPubBO_Client.update(ctvo);
		
		dvo.setIs_relation(1);
		dvo.setRelationid(pkHvo);
		HYPubBO_Client.update(dvo);
	}
	
}
