package nc.ui.dahuan.hkjh.sw;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.hkjh.HkswVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;



 public class ClientUI extends AbstractClientUI {
       
   
	private static final long serialVersionUID = 1L;

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
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{4});
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Delete).getData()).setExtendStatus(new int[]{4});
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Commit).getData()).setExtendStatus(new int[]{4});
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{2});
		
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{2});
		
	}

	public void setDefaultData() throws Exception {
		// 制单部门
		String sql = "select t.pk_deptdoc from v_deptperonal t where t.pk_corp='"+this._getCorp().getPrimaryKey()+"' and t.pk_user='"+this._getOperator()+"'";
		List<Map<String,String>> mplit = (List<Map<String,String>>)NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new MapListProcessor());
		if(null != mplit && mplit.size()==1){
			String pkDept = mplit.get(0).get("pk_deptdoc");
			this.getBillCardPanel().setHeadItem("pk_dept", pkDept);
		}
	}

	
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		if("ctcode".equals(e.getKey())){
			UIRefPane custUif = (UIRefPane)this.getBillCardPanel().getHeadItem("pk_cust").getComponent();
			String pkCust = custUif.getRefPK();
			if(null == pkCust || "".equals(pkCust)){
				MessageDialog.showHintDlg(this, "提示", "请先选择回款单位");
				return false;
			}else{
				String newWherePart = " and exists (select 1 from (select c.pk_contract, c.dctjetotal from dh_contract c "
						            +" where nvl(c.dr, 0) = 0 and nvl(c.httype, 2) = 0 and nvl(c.is_seal, 0) = 1) m "
						            +" left join (select t.pk_contract, sum(nvl(t.ct_amount, 0)) ct_amount "
						            +" from (select d.pk_contract, d.ct_amount from dh_hkdh_d d where nvl(d.dr, 0) = 0 "
						            +" union all select r.pk_contract, r.ct_amount from dh_hkreplace r) t group by t.pk_contract) l "
						            +" on m.pk_contract = l.pk_contract where m.pk_contract = v.pk_contract "
						            //+" and nvl(m.dctjetotal, 0) > nvl(l.ct_amount, 0) ) and v.pk_cust1 = '"+pkCust+"' ";
						            +"  ) and v.pk_cust1 = '"+pkCust+"' ";
				UIRefPane ctcodeUif = (UIRefPane)this.getBillCardPanel().getBodyItem("ctcode").getComponent();
				ctcodeUif.getRefModel().addWherePart(newWherePart,true);
			}
		}
		
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		BillCardPanel card = this.getBillCardPanel();
		
		if("ctcode".equals(e.getKey())){
			
			Object objContract = e.getValue();
			
			if(null != objContract && !"".equals(objContract)){
				try{
					DefaultConstEnum enumCon = (DefaultConstEnum)objContract;
					
					DhContractVO dhvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, enumCon.getValue().toString());
					
					BillModel bmodel = card.getBillModel();
					int row = e.getRow();
					bmodel.setValueAt(dhvo.getCtname(), row, "ctname");
					
					String fulems = "ctname_list->getColValue(bd_invbasdoc, invname, pk_invbasdoc, ctname)";
					card.execBodyFormulas(row, new String[]{fulems});
					
					bmodel.setValueAt(dhvo.getVdef6(), row, "xmname");
					UFDouble djt=dhvo.getDctjetotal()==null?new UFDouble(0):dhvo.getDctjetotal();
					UFDouble ljt=dhvo.getLjfkjhje()==null?new UFDouble(0):dhvo.getLjfkjhje();
					bmodel.setValueAt(djt.sub(ljt), row, "ct_amount");
					
				}catch(Exception be){
					be.printStackTrace();
				}
			}
		}
		
		if("bzbl".equals(e.getKey()) || "hk_amount".equals(e.getKey())||"give_amount".equals(e.getKey())||"discount_amount".equals(e.getKey())){
			UFDouble bzbl = get_ufdouble(card.getHeadItem("bzbl").getValueObject());
			UFDouble hk_amount = get_ufdouble(card.getHeadItem("hk_amount").getValueObject());
			
			UFDouble gv_amt = get_ufdouble(card.getHeadItem("give_amount").getValueObject());
			UFDouble dt_amount = get_ufdouble(card.getHeadItem("discount_amount").getValueObject());
			
			UFDouble rmb_amt = bzbl.multiply(hk_amount,2).add(dt_amount,2).sub(gv_amt,2);
			
			card.getHeadItem("rmb_amount").setValue(rmb_amt);
		}
	}
	
	private UFDouble get_ufdouble(Object obj){
		if(null == obj){
			return new UFDouble("0.00");
		}else{
			return new UFDouble(obj.toString());
		}
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			HkswVO cdvo = (HkswVO)vo.getParentVO();
			int vbillstatus = cdvo.getVbillstatus();
			if(1==vbillstatus){
				// 提交
				return 2;
			}else if(2==vbillstatus){
				// 确认
				return 3;
			}else{
				// 驳回 未提交
				return 4;
			}
		}
		return 1;
	}

	@Override
	protected void initPrivateButton() {
		
		ButtonVO btn=new FileUpLoadBtnVO().getButtonVO();
		addPrivateButton(btn);
		
		super.initPrivateButton();
	}
	
	
	
	
	
}
