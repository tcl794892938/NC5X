package nc.ui.dahuan.projclear.zdr;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.projclear.ProjectClearVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillManageUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}
	
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this,this.getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void initSelfData() {
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Delete).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Commit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{3});
	}
	
	

	@Override
	protected void initPrivateButton() {
		ButtonVO caBtn = ButtonVOFactory.getInstance().build(IBillButton.CancelAudit);
		caBtn.setBtnName("驳回");
		addPrivateButton(caBtn);
		
		ButtonVO refBtn = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refBtn.setBtnName("项目清算协议");
		addPrivateButton(refBtn);
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			ProjectClearVO pcvo = (ProjectClearVO)vo.getParentVO();
			int pcs = pcvo.getPc_status();
			if(0==pcs||3==pcs||5==pcs||7==pcs){
				return 2;
			}else if(1==pcs){
				return 3;
			}else {
				return 4;
			}
		}
		return 1;
	}

	@Override
	public void setDefaultData() throws Exception {}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if("salecontractid".equals(e.getKey())){
			try{
				BillCardPanel card = this.getBillCardPanel();
				BillModel model = card.getBillModel();
				
				Object salecontractidObj = card.getHeadItem("salecontractid").getValueObject();
				if(null != salecontractidObj && !"".equals(salecontractidObj)){
					
					String salesql = "select v.ctcode,v.ctname,v.pk_cust1,v.vdef6,v.dctjetotal,v.pk_fuzong,sum(nvl(d.localcreditamount, 0)) localcreditamount, " +
							" nvl(v.dctjetotal,0)-sum(nvl(d.localcreditamount, 0)) overamount from " +
							" (select (select m.pk_jobbasfil from bd_jobmngfil m where m.pk_jobmngfil = t.pk_jobmandoc) pk_jobbasfil,t.pk_corp,t.vdef6, " +
							" t.ctcode,t.ctname,t.pk_cust1,t.dctjetotal,t.pk_fuzong from dh_contract t where t.pk_contract = '"+salecontractidObj.toString()+"') v," +
							" gl_freevalue f,gl_detail d,bd_accsubj a,gl_voucher g where v.pk_jobbasfil = f.checkvalue and nvl(f.dr, 0) = 0 " +
							" and f.checktype = '0001A11000000000CGMX' and d.assid = f.freevalueid and nvl(d.dr, 0) = 0  and d.explanation <> '期初' " +
							" and d.pk_accsubj = a.pk_accsubj  and nvl(a.dr, 0) = 0 and d.pk_voucher = g.pk_voucher and nvl(g.dr, 0) = 0 " +
							" and g.discardflag = 'N' and ((a.subjcode in ('1122', '2203') and nvl(d.localcreditamount, 0) <> 0) or " +
							" ((a.subjcode in ('2202', '1123') or a.subjcode like '6602%') and nvl(d.localdebitamount, 0) <> 0)) and g.discardflag = 'N'" +
							" and g.errmessage is null and g.period <> '00' and g.pk_corp = v.pk_corp " +
							" group by v.ctcode, v.ctname,v.vdef6, v.pk_cust1, v.dctjetotal,v.pk_fuzong  order by v.ctcode, v.ctname, v.pk_cust1, v.dctjetotal,v.pk_fuzong ";
					
					IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					List<Map<String,Object>> saleList = (List<Map<String,Object>>)iQ.executeQuery(salesql, new MapListProcessor());
					if(null == saleList || saleList.size()==0){
						card.setHeadItem("salectcode", null);
						card.setHeadItem("salectname", null);
						card.setHeadItem("customerid", null);
						card.setHeadItem("projectname", null);
						card.setHeadItem("saleamount", null);
						card.setHeadItem("retamount", null);
						card.setHeadItem("overamount", null);
						card.setHeadItem("fzr", null);
						model.clearBodyData();
					}else{
						Map<String,Object> saleMap = saleList.get(0);
						card.setHeadItem("salectcode", saleMap.get("ctcode"));
						card.setHeadItem("salectname", saleMap.get("ctname"));
						card.setHeadItem("customerid", saleMap.get("pk_cust1"));
						card.setHeadItem("projectname", saleMap.get("vdef6"));
						card.setHeadItem("saleamount", saleMap.get("dctjetotal"));
						card.setHeadItem("retamount", saleMap.get("localcreditamount"));
						card.setHeadItem("overamount", saleMap.get("overamount"));
						card.setHeadItem("fzr", saleMap.get("pk_fuzong"));
						model.clearBodyData();
						
						String ctcode = saleMap.get("ctcode").toString();
						int length = ctcode.length()-2;
						String leftcode = ctcode.substring(0, length);
						
						String pursql = "select v.pk_contract, v.ctcode, v.ctname,  v.pk_cust2, v.dctjetotal ," +
								" nvl(s.localdebitamount,0) localdebitamount," +
								" v.dctjetotal- nvl(s.localdebitamount,0) jyamount" +
								" from (select t.pk_contract," +
								"(select m.pk_jobbasfil from bd_jobmngfil m" +
								" where m.pk_jobmngfil = t.pk_jobmandoc) pk_jobbasfil," +
								" t.pk_corp, t.ctcode, t.ctname, t.pk_cust2, t.dctjetotal  " +
								"from dh_contract t where t.ctcode like '"+leftcode+"%'" +
								"  and t.httype = 1 and nvl(t.dctjetotal, 0) <> 0 )v " +
								"left join " +
								"(select  f.checkvalue,g.pk_corp, sum(nvl(d.localdebitamount, 0)) localdebitamount " +
								"from gl_freevalue f," +
								" gl_detail d," +
								" bd_accsubj a," +
								"  gl_voucher g" +
								" where nvl(f.dr, 0) = 0" +
								" and f.checktype = '0001A11000000000CGMX'" +
								" and d.assid = f.freevalueid " +
								" and nvl(d.dr, 0) = 0" +
								" and d.explanation <> '期初' " +
								" and d.pk_accsubj = a.pk_accsubj" +
								" and nvl(a.dr, 0) = 0" +
								" and d.pk_voucher = g.pk_voucher" +
								" and nvl(g.dr, 0) = 0" +
								" and g.discardflag = 'N'" +
								" and ((a.subjcode in ('1122', '2203') and" +
								" nvl(d.localcreditamount, 0) <> 0) or" +
								" ((a.subjcode in ('2202', '1123') or a.subjcode like '6602%') and" +
								"  nvl(d.localdebitamount, 0) <> 0))" +
								" and g.discardflag = 'N'" +
								" and g.errmessage is null" +
								" and g.period <> '00'" +
								" group by f.checkvalue,g.pk_corp ) s " +
								"on v.pk_jobbasfil=s.checkvalue and v.pk_corp=s.pk_corp" +
								" order by v.ctcode, v.ctname, v.pk_cust2, v.dctjetotal ";
						
						List<Map<String,Object>> purList = (List<Map<String,Object>>)iQ.executeQuery(pursql, new MapListProcessor());
						if(null != purList && purList.size() > 0){
							for(int i=0;i<purList.size();i++){
								Map<String,Object> purMap = purList.get(i);
								model.addLine();
								model.setValueAt(purMap.get("pk_contract"), i, "purcontractid");
								model.setValueAt(purMap.get("ctcode"), i, "purctcode");
								model.setValueAt(purMap.get("ctname"), i, "purctname");
								model.setValueAt(purMap.get("pk_cust2"), i, "supplierid");
								UFDouble dctj = new UFDouble(purMap.get("dctjetotal")==null?"0.00":purMap.get("dctjetotal").toString());
								model.setValueAt(dctj, i, "puramount");
								UFDouble paya = new UFDouble(purMap.get("localdebitamount")==null?"0.00":purMap.get("localdebitamount").toString());
								model.setValueAt(paya, i, "payamount");
								UFDouble jya = new UFDouble(purMap.get("jyamount")==null?"0.00":purMap.get("jyamount").toString());
								model.setValueAt(jya, i, "jyamount");
								model.execEditFormulas(i);
							}
						}
					}
					
				}else{
					card.setHeadItem("salectcode", null);
					card.setHeadItem("salectname", null);
					card.setHeadItem("customerid", null);
					card.setHeadItem("projectname", null);
					card.setHeadItem("saleamount", null);
					card.setHeadItem("retamount", null);
					card.setHeadItem("overamount", null);
					model.clearBodyData();
					
				}
			}catch(Exception ep){
				ep.printStackTrace();
			}
		}
	}

	
	
}
