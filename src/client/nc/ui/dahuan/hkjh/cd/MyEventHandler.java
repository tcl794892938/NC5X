package nc.ui.dahuan.hkjh.cd;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.hkjh.HkcdDVO;
import nc.vo.dahuan.hkjh.HkcdVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}
		
	@Override
	protected void onBoSave() throws Exception {
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		
		// 校验必输项
		card.dataNotNullValidate();
		

		//  表体校验
		if(0==card.getBillModel().getRowCount()){
			MessageDialog.showHintDlg(card, "提示", "表体不能为空");
			return;
		}
		
		
		// 校验合计金额
		Object obj_rmb = card.getHeadItem("rmb_amount").getValueObject();
		UFDouble ufd_rmb = new UFDouble(obj_rmb==null?"0.00":obj_rmb.toString());
		
		UFDouble ufd_ctamtSum = new UFDouble("0.00");
		BillModel bmodel = card.getBillModel();
		for(int i=0;i<bmodel.getRowCount();i++){
			String pkContract = bmodel.getValueAt(i, "pk_contract").toString();
			DhContractVO hvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkContract);
			UFDouble ufd_max = hvo.getDctjetotal().sub(new UFDouble(hvo.getLjfkjhje()==null?"0.00":hvo.getLjfkjhje().toString()), 2);
			
			Object obj_ctamt = bmodel.getValueAt(i, "ct_amount");
			UFDouble ufd_ctamt = new UFDouble(obj_ctamt==null?"0.00":obj_ctamt.toString());
			
			if(ufd_max.compareTo(ufd_ctamt)<0){
				MessageDialog.showHintDlg(card, "提示", "第"+(i+1)+"行合同分配金额超出合同剩余金额["+ufd_max+"]");
				return;
			}
			ufd_ctamtSum = ufd_ctamtSum.add(ufd_ctamt,2);
		}
		
		if(ufd_ctamtSum.compareTo(ufd_rmb)>0){
			MessageDialog.showHintDlg(card, "提示", "合同分配金额超出回款金额");
			return;
		}
		
		super.onBoSave();
	}

	@Override
	public void onBoAudit() throws Exception {
		// 确认
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		UFDate date = ClientEnvironment.getInstance().getServerTime().getDate();
		
		HkcdVO cdvo = (HkcdVO)this.getBufferData().getCurrentVO().getParentVO();
		
		/************************************** 更新回款单主表 start  *********************************************/
		HkdhVO dhvo = new HkdhVO();
		dhvo.setBzbl(cdvo.getBzbl());
		dhvo.setDbilldate(cdvo.getDbilldate());
		dhvo.setDr(0);
		dhvo.setHk_amount(cdvo.getHk_amount());
		dhvo.setHkbillno(cdvo.getHkbillno());
		dhvo.setPk_bizong(cdvo.getPk_bizong());
		dhvo.setPk_cust(cdvo.getPk_cust());
		dhvo.setRemark(cdvo.getRemark());
		dhvo.setRmb_amount(cdvo.getRmb_amount());
		dhvo.setVoperid(pkUser);
		dhvo.setBill_flag(1);
		dhvo.setSure_flag(1);
		dhvo.setVemo(cdvo.getVemo());
		dhvo.setPk_corp(cdvo.getPk_corp());
		
		dhvo.setDiscount_amount(cdvo.getDiscount_amount());
		dhvo.setGive_amount(cdvo.getGive_amount());
		String pkhk = HYPubBO_Client.insert(dhvo);
		/************************************** 更新回款单主表 end  *********************************************/
		
		/************************************** 更新回款单子表  start  *********************************************/
		
		HkcdDVO[] dcdvos = (HkcdDVO[])this.getBufferData().getCurrentVO().getChildrenVO();
		for(HkcdDVO cdvob : dcdvos){
			HkdhDVO dhdvo = new HkdhDVO();
			dhdvo.setPk_hkdh(pkhk);
			dhdvo.setPk_contract(cdvob.getPk_contract());
			dhdvo.setCtcode(cdvob.getCtcode());
			
			dhdvo.setCtname(cdvob.getCtname_list());
			
			dhdvo.setXmname(cdvob.getXmname());
			dhdvo.setCt_amount(cdvob.getCt_amount());
			dhdvo.setVoperid(cdvo.getVoperid());
			dhdvo.setDbilldate(cdvo.getDbilldate());
			dhdvo.setPk_dept(cdvo.getPk_dept());
			HYPubBO_Client.insert(dhdvo);
		
		/************************************** 更新回款单子表  end  *********************************************/
		
		/************************************** 更新合同档案主表  start  *********************************************/
			DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, cdvob.getPk_contract());
			UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
			ctvo.setLjfkjhje(ljfkjhje.add(cdvob.getCt_amount(), 2));
			HYPubBO_Client.update(ctvo);
		/************************************** 更新合同档案主表  end   *********************************************/
		}
		cdvo.setVbillstatus(2);
		cdvo.setVapproveid(pkUser);
		cdvo.setVapprovedate(date);
		
		HYPubBO_Client.update(cdvo);
		this.onBoRefresh();
	}

	@Override
	protected void onBoCancelAudit() throws Exception {
		// 驳回
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		UFDate date = ClientEnvironment.getInstance().getServerTime().getDate();
		
		HkcdVO cdvo = (HkcdVO)this.getBufferData().getCurrentVO().getParentVO();
		cdvo.setVbillstatus(3);
		cdvo.setVapproveid(pkUser);
		cdvo.setVapprovedate(date);
		
		HYPubBO_Client.update(cdvo);
		this.onBoRefresh();
	}

	@Override
	protected void onBoCommit() throws Exception {
		// 提交
		HkcdVO cdvo = (HkcdVO)this.getBufferData().getCurrentVO().getParentVO();
		cdvo.setVbillstatus(1);
		HYPubBO_Client.update(cdvo);
		this.onBoRefresh();
	}

	@Override
	protected void onBoQuery() throws Exception {
		
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String corpid = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String rolesql = "select count(1) from sm_user_role r, dh_hkrole k where r.cuserid = '"+userid+"' "
						+" and r.pk_corp = k.pk_corp and r.pk_corp = '"+corpid+"' and r.pk_role = k.pk_role and nvl(r.dr,0)=0 ";
		
		int count = (Integer)iQ.executeQuery(rolesql, new ColumnProcessor());
		
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;
		
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";
		
	
		if(0==count){
			strWhere += " and dh_hkcd.voperid = '"+userid+"' ";
		}
		
		String sql = " select distinct dh_hkcd.* from dh_hkcd, dh_hkcd_d where dh_hkcd.pk_hkcd = dh_hkcd_d.pk_hkcd and nvl(dh_hkcd.dr, 0) = 0 "+
		   			" and nvl(dh_hkcd_d.dr, 0) = 0 and not exists(select 1 from dh_hkdh where dh_hkdh.seal_flag=1 " +
		   			" and dh_hkdh.hkbillno=dh_hkcd.hkbillno) and "+strWhere;
		
		List<HkcdVO> queryVos = (List<HkcdVO>)iQ.executeQuery(sql, new BeanListProcessor(HkcdVO.class));
		
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos.toArray(new HkcdVO[0]));
	
		updateBuffer();

	}
	
}