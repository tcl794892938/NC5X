package nc.ui.dahuan.conmodify.outside.dept;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.bfriend.button.RetCommitBtnVO;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.contractmodify.ConModfiyVO;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillManageUI implements ILinkQuery {

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
	protected void initPrivateButton() {
		FileUpLoadBtnVO fvo = new FileUpLoadBtnVO();
		addPrivateButton(fvo.getButtonVO());
		RetCommitBtnVO rvo = new RetCommitBtnVO();
		addPrivateButton(rvo.getButtonVO());
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
		((ButtonVO)this.getButtonManager().getButton(IdhButton.RET_COMMIT).getData()).setExtendStatus(new int[]{5});
	}
	
	

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			ConModfiyVO mvo = (ConModfiyVO)vo.getParentVO();
			int mstatus = mvo.getModify_status();
			if(1==mstatus){
				return 5;
			}else if(2==mstatus){
				return 4;
			}else if(3==mstatus){
				return 3;
			}else{
				return 2;
			}
		}
		return 1;
	}

	@Override
	public void setDefaultData() throws Exception { }

	public void doQueryAction(ILinkQueryData querydata) { }

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		BillCardPanel card = this.getBillCardPanel();
		BillModel model = card.getBillModel();
		if("parent_contractid".equals(e.getKey())){
			
			Object ptconObj = card.getHeadItem("parent_contractid").getValueObject();
			
			card.setHeadItem("contractname", null);
			card.setHeadItem("projectname", null);
			card.setHeadItem("parent_custname", null);
			card.setHeadItem("rate", null);
			card.setHeadItem("old_fc_summny", null);
			card.setHeadItem("fc_summny", null);
			card.setHeadItem("old_summny", null);
			card.setHeadItem("summny", null);
			card.setHeadItem("old_managerid", null);
			card.setHeadItem("managerid", null);
			card.setHeadItem("old_contractorid", null);
			card.setHeadItem("contractorid", null);
			card.setHeadItem("old_budget", null);
			card.setHeadItem("budget", null);
			card.setHeadItem("parent_corpid", null);
			//card.setHeadItem("zzr", null);
			
			model.clearBodyData();
			
			if(null != ptconObj){
				String ptconid = ptconObj.toString();
				
				String sql = " select a.ctcode,c.invname ctname,a.xmname,b.custname,a.pk_corp,a.currenty_rate,a.vapproveid,a.curr_amount,a.dctjetotal," +
							" a.pk_xmjl,a.pk_fuzong,a.pk_fzr,a.xm_amount from (select v.ctcode,v.ctname,v.vdef6 xmname,v.pk_corp,v.currenty_rate," +
							" case when v.pk_cust1 is not null then v.pk_cust1 else v.pk_cust2 end as pk_cust,v.vapproveid,v.curr_amount,v.dctjetotal," +
							" v.pk_xmjl,v.pk_fuzong,v.pk_fzr,v.xm_amount from dh_contract v where v.pk_contract = '"+ptconid+"') a" +
							" left join bd_invbasdoc c on a.ctname = c.pk_invbasdoc left join bd_cumandoc m on a.pk_cust = m.pk_cumandoc" +
							" left join bd_cubasdoc b on m.pk_cubasdoc = b.pk_cubasdoc";
				
				IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					List<Map<String,Object>> maplist = (List<Map<String,Object>>)iQ.executeQuery(sql, new MapListProcessor());
					if(null != maplist && maplist.size()>0){
						Map<String,Object> map = maplist.get(0);
						card.setHeadItem("contractname", map.get("ctname"));
						card.setHeadItem("projectname", map.get("xmname"));
						card.setHeadItem("parent_custname", map.get("custname"));
						card.setHeadItem("rate", map.get("currenty_rate"));
						card.setHeadItem("old_fc_summny", map.get("curr_amount"));
						card.setHeadItem("fc_summny", map.get("curr_amount"));
						card.setHeadItem("old_summny", map.get("dctjetotal"));
						card.setHeadItem("summny", map.get("dctjetotal"));
						card.setHeadItem("old_managerid", map.get("pk_xmjl"));
						card.setHeadItem("managerid", map.get("pk_xmjl"));
						card.setHeadItem("old_contractorid", map.get("pk_fzr"));
						card.setHeadItem("contractorid", map.get("pk_fzr"));
						card.setHeadItem("old_budget", map.get("xm_amount"));
						card.setHeadItem("budget", map.get("xm_amount"));
						card.setHeadItem("parent_corpid", map.get("pk_corp"));
						//card.setHeadItem("zzr", map.get("pk_fuzong"));
						
						DhContractBVO[] bvos = (DhContractBVO[])HYPubBO_Client.queryByCondition(DhContractBVO.class, " pk_contract = '"+ptconid+"' ");
						if(null !=  bvos && bvos.length>0){
							for(int i=0;i<bvos.length;i++){
								model.addLine();
								DhContractBVO bvo = bvos[i];
								model.setValueAt(bvo.getPk_invbasdoc(), i, "pk_invbasdoc");
								model.setCellEditable(i, "pk_invbasdoc", false);
								model.setValueAt(bvo.getInvcode(), i, "invcode");
								model.setCellEditable(i, "invcode", false);
								model.setValueAt(bvo.getInvname(), i, "invname");
								model.setCellEditable(i, "invname", false);
								model.setValueAt(bvo.getVggxh(), i, "stylemodel");
								model.setCellEditable(i, "stylemodel", false);
								model.setValueAt(bvo.getPk_danw(), i, "meaname");
								model.setCellEditable(i, "meaname", false);
								model.setValueAt(bvo.getNnumber(), i, "nums");
								model.setCellEditable(i, "nums", false);
								model.setValueAt(bvo.getCurrenty_amount(), i, "fc_price");
								model.setCellEditable(i, "fc_price", false);
								model.setValueAt(bvo.getDjprice(), i, "price");
								model.setCellEditable(i, "price", false);
								model.setValueAt(bvo.getCurr_amount_sum(), i, "fc_amount");
								model.setCellEditable(i, "fc_amount", false);
								model.setValueAt(bvo.getDjetotal(), i, "amount");
								model.setCellEditable(i, "amount", false);
								model.setValueAt(bvo.getDghsj(), i, "delivery_date");
								model.setCellEditable(i, "delivery_date", false);
								model.setValueAt(bvo.getVmen(), i, "vemo");
								model.setCellEditable(i, "vemo", false);
							}
						}
					}
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if("invcode".equals(e.getKey())){
			int row = e.getRow();
			model.setValueAt(null, row, "invname");
			model.setValueAt(null, row, "stylemodel");
			model.setValueAt(null, row, "meaname");
			Object invObj = model.getValueAt(row, "pk_invbasdoc");
			if(null != invObj){
				String pkInv = invObj.toString();
				String sql = "select b.invname,(select m.measname from bd_measdoc m where m.pk_measdoc = b.pk_measdoc) meaname," +
					" b.invspec||b.invtype sptype from bd_invbasdoc b where b.pk_invbasdoc = '"+pkInv+"' ";
				IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					List<Map<String,Object>> maplist = (List<Map<String,Object>>)iQ.executeQuery(sql, new MapListProcessor());
					if(null != maplist && maplist.size()>0){
						Map<String,Object> map = maplist.get(0);
						model.setValueAt(map.get("invname"), row, "invname");
						model.setValueAt(map.get("sptype"), row, "stylemodel");
						model.setValueAt(map.get("meaname"), row, "meaname");
					}
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if("nums".equals(e.getKey()) || "fc_price".equals(e.getKey())){
			int row = e.getRow();
			
			// 数量
			Object numsObj = model.getValueAt(row, "nums");
			UFDouble nums = new UFDouble(numsObj==null?"0.00":numsObj.toString());
			// 外币单价
			Object fcpObj = model.getValueAt(row, "fc_price");
			UFDouble fcprice = new UFDouble(fcpObj==null?"0.00":fcpObj.toString());
			// 汇率
			Object rateObj = card.getHeadItem("rate").getValueObject();
			UFDouble rate = new UFDouble(rateObj==null?"1.00":rateObj.toString());
			
			// 本币单价
			UFDouble price = fcprice.multiply(rate);
			model.setValueAt(price, row, "price");
			
			// 外币总额
			UFDouble fcamount = fcprice.multiply(nums);
			model.setValueAt(fcamount, row, "fc_amount");
			
			// 本币总额
			UFDouble amount = price.multiply(nums);
			model.setValueAt(amount, row, "amount");
			
		}
	}

	
	
}
