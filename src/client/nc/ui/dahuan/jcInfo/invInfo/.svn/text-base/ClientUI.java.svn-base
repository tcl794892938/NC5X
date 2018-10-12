package nc.ui.dahuan.jcInfo.invInfo;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.jcInfo.invInfo.BgInvEntity;
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
		
		// 修改
		ButtonVO editvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData();
		editvo.setExtendStatus(new int[]{4});
		// 提交
		ButtonVO comvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Commit).getData();
		comvo.setExtendStatus(new int[]{4});
	}

	public void setDefaultData() throws Exception {
		
		// 单据状态
		this.getBillCardPanel().setHeadItem("bg_status", 0);
		
		// 制单部门
		String deptsql = "select vd.pk_deptdoc from v_deptperonal vd  where vd.pk_corp='"+this._getCorp().getPrimaryKey()+"' " +
								" and vd.pk_user='"+this._getOperator()+"' ";
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object deptobj = iQ.executeQuery(deptsql, new ColumnProcessor());
		String pkdept = deptobj == null ?"":deptobj.toString();
		((UIRefPane)this.getBillCardPanel().getHeadItem("pk_dept").getComponent()).setPK(pkdept);
		
		// 副总过滤
		UIRefPane uif = (UIRefPane)this.getBillCardPanel().getHeadItem("vapproveid").getComponent();
		uif.getRefModel().addWherePart(" and sm_user.cuserid in (select t.cuserid from sm_user_role t where t.pk_role = '0001F41000000000FREA' and pk_corp = '"+this._getCorp().getPrimaryKey()+"')", true);
	}
	
	

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			BgInvEntity bgentity = (BgInvEntity)vo.getParentVO();
			int flag = bgentity.getBg_status();
			if(flag == 2){
				return 2;
			}else if(flag == 1){
				return 3;
			}else{
				return 4;
			}
		}
		return 1;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		// 原本基础信息
		if("pk_invbasdoc".equals(e.getKey())){
			UIRefPane invuif = (UIRefPane)e.getSource();
			String pkinv = invuif.getRefPK();
			String invsql = "select t.pk_invcl,t.invcode,t.invname from bd_invbasdoc t where t.pk_invbasdoc='"+pkinv+"'";
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			try{
				List<Map<String,String>> invlit = (List<Map<String,String>>)iQ.executeQuery(invsql, new MapListProcessor());
				if(null != invlit && invlit.size() > 0){
					Map<String,String> invmp = invlit.get(0);
					this.getBillCardPanel().setHeadItem("og_invno",invmp.get("invcode"));
					this.getBillCardPanel().setHeadItem("inv_no",invmp.get("invcode"));
					this.getBillCardPanel().setHeadItem("og_invname",invmp.get("invname"));
					this.getBillCardPanel().setHeadItem("inv_name",invmp.get("invname"));
					((UIRefPane)this.getBillCardPanel().getHeadItem("og_invcl").getComponent()).setPK(invmp.get("pk_invcl"));
					((UIRefPane)this.getBillCardPanel().getHeadItem("pk_invcl").getComponent()).setPK(invmp.get("pk_invcl"));
				}else{
					this.getBillCardPanel().setHeadItem("og_invno",null);
					this.getBillCardPanel().setHeadItem("inv_no",null);
					this.getBillCardPanel().setHeadItem("og_invname",null);
					this.getBillCardPanel().setHeadItem("inv_name",null);
					((UIRefPane)this.getBillCardPanel().getHeadItem("og_invcl").getComponent()).setPK(null);
					((UIRefPane)this.getBillCardPanel().getHeadItem("pk_invcl").getComponent()).setPK(null);
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	

}
