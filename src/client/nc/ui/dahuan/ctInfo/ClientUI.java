package nc.ui.dahuan.ctInfo;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.busi.CustmandocDefaultRefModel;
import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.ctInfo.CustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
		
		ButtonManager btm = this.getButtonManager();
		
		// 修改
		((ButtonVO)btm.getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{2,7});
		// 提交
		((ButtonVO)btm.getButton(IBillButton.Commit).getData()).setExtendStatus(new int[]{2,7});
		// 撤销
		((ButtonVO)btm.getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{6});
		// 工程部驳回
		((ButtonVO)btm.getButton(IdhButton.RET_COMMIT).getData()).setExtendStatus(new int[]{6});
		// 工程部审批
		((ButtonVO)btm.getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{6});
		// 不同意
		((ButtonVO)btm.getButton(IdhButton.NOAGREE).getData()).setExtendStatus(new int[]{3});
		// 同意
		((ButtonVO)btm.getButton(IdhButton.AGREE).getData()).setExtendStatus(new int[]{3});
		// 财务确认
		((ButtonVO)btm.getButton(IdhButton.CWQR).getData()).setExtendStatus(new int[]{4});
		//财务驳回
		((ButtonVO)btm.getButton(IdhButton.CWBH).getData()).setExtendStatus(new int[]{5});
	}

	public void setDefaultData() throws Exception {
		UIRefPane uif = (UIRefPane)this.getBillCardPanel().getHeadItem("dhcust_area").getComponent();
		uif.getRefModel().addWherePart(" and areaclcode like '01%' ", true);
		UIRefPane uif2 = (UIRefPane)this.getBillCardPanel().getHeadItem("pkcumandoc").getComponent();
		CustmandocDefaultRefModel cuif = (CustmandocDefaultRefModel)uif2.getRefModel();
		String classconf = " (pk_corp='"+this._getCorp().getPrimaryKey()+"' or pk_corp= '0001') and areaclcode like '01%' ";
		cuif.setClassWherePart(classconf);
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			CustVO cvo = (CustVO)vo.getParentVO();
			int flag = cvo.getDhcust_flag();
			if(1==flag){
				// 已提交
				return 6;
			}else if(2==flag){
				// 被驳回
				return 2;
			}else if(3==flag){
				// 审批通过
				return 3;
			}else if(4==flag){
				// 同意
				return 4;
			}else if(5==flag){
				// 财务确认
				return 5;
			}else{
				return 7;
			}
		}
		return 1;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if("dhcust_type".equals(e.getKey())){
			Object dctype = e.getValue();
			if("供应商".equals(dctype)){
				UIRefPane uif = (UIRefPane)this.getBillCardPanel().getHeadItem("dhcust_area").getComponent();
				uif.getRefModel().addWherePart(" and areaclcode like '02%' ", true);
				UIRefPane uif2 = (UIRefPane)this.getBillCardPanel().getHeadItem("pkcumandoc").getComponent();
				CustmandocDefaultRefModel cuif = (CustmandocDefaultRefModel)uif2.getRefModel();
				String classconf = " (pk_corp='"+this._getCorp().getPrimaryKey()+"' or pk_corp= '0001') and areaclcode like '02%' ";
				cuif.setClassWherePart(classconf);
			}else{
				UIRefPane uif = (UIRefPane)this.getBillCardPanel().getHeadItem("dhcust_area").getComponent();
				uif.getRefModel().addWherePart(" and areaclcode like '01%' ", true);
				UIRefPane uif2 = (UIRefPane)this.getBillCardPanel().getHeadItem("pkcumandoc").getComponent();
				CustmandocDefaultRefModel cuif = (CustmandocDefaultRefModel)uif2.getRefModel();
				String classconf = " (pk_corp='"+this._getCorp().getPrimaryKey()+"' or pk_corp= '0001') and areaclcode like '01%' ";
				cuif.setClassWherePart(classconf);
			}
		}
		if("pkcumandoc".equals(e.getKey())){
			
			BillCardPanel card = this.getBillCardPanel();
			UIRefPane manUif = (UIRefPane)card.getHeadItem("pkcumandoc").getComponent();
			
			String sql = "select t.custname,t.custshortname,t.pk_areacl,t.taxpayerid,t.phone1,t.zipcode,t.conaddr,t.registerfund,"+
				       "t.pk_cubasdoc,m.def1,m.def2 from bd_cumandoc m,bd_cubasdoc t  where m.pk_cumandoc = '"+manUif.getRefPK()+"' "+
				       " and t.pk_cubasdoc = m.pk_cubasdoc ";
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			try{
				List<Map<String,Object>> mplit = (List<Map<String,Object>>)iQ.executeQuery(sql, new MapListProcessor());
				if(null != mplit && mplit.size()==1){
					Map<String,Object> mp = mplit.get(0);
					card.setHeadItem("dhcust_name", mp.get("custname"));
					card.setHeadItem("dhcust_shortname", mp.get("custshortname"));
					card.setHeadItem("dhcust_area", mp.get("pk_areacl"));
					card.setHeadItem("dhcust_sayno", mp.get("taxpayerid"));
					card.setHeadItem("dhcust_tel", mp.get("phone1"));
					card.setHeadItem("dhcust_pos", mp.get("zipcode"));
					card.setHeadItem("cust_address", mp.get("conaddr"));
					card.setHeadItem("zuce_amount", mp.get("registerfund"));
					card.setHeadItem("pk_cubasdoc", mp.get("pk_cubasdoc"));
					card.setHeadItem("dhcust_saxno", mp.get("def2"));
					card.setHeadItem("dhcust_bank", mp.get("def1"));
				}else{
					MessageDialog.showHintDlg(this, "提示", "该客商信息有误，请先查阅矫正");
				}
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}

	@Override
	protected void initPrivateButton() {
		
		ButtonVO btn=new FileUpLoadBtnVO().getButtonVO4();
		addPrivateButton(btn);
		
		super.initPrivateButton();
	}

	
	
}
