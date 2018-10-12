package nc.ui.dahuan.stuff;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dahuan.stuff.StuffVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
		return new MyEventHandler(this,getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {}

	@Override
	protected void initSelfData() {
		// 按钮控制
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.Delete).getData()).setExtendStatus(new int[]{2});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{3});
		((ButtonVO)this.getButtonManager().getButton(IBillButton.ImportBill).getData()).setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
	}
	
	
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			StuffVO svo = (StuffVO)vo.getParentVO();
			int vbs = svo.getVbillstatus();
			if(0==vbs){
				return 2;//未审核
			}else if(1==vbs){
				return 3;// 已审核
			}
		}
		return 1;
	}

	@Override
	public void setDefaultData() throws Exception {
		// 部门设置
		String sql ="select t.pk_deptdoc from v_deptperonal t where t.pk_corp='"+_getCorp().getPk_corp()+"' and t.pk_user='"+_getOperator()+"'";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		String pkDept = (String)iQ.executeQuery(sql, new ColumnProcessor());
		this.getBillCardPanel().setHeadItem("pk_dept", pkDept);
	}

	public void doQueryAction(ILinkQueryData querydata) {
		try {
			
			String pkCorp = querydata.getUserObject().toString();
			
			String sql = " select * from dh_stuff f where f.vbillstatus=1 and nvl(f.dr,0)=0 and f.pk_corp='"+pkCorp+"' and exists(select 1 from "+
			"  dh_contract t where t.ctcode='"+querydata.getBillID()+"' and t.pk_corp='"+pkCorp+"' and t.pk_jobmandoc = f.pk_jobmngdoc) ";
			
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<StuffVO> list = (List<StuffVO>)iQ.executeQuery(sql, new BeanListProcessor(StuffVO.class));
			
			getBufferData().clear();
			
			if(null != list && list.size()>0){
				for(StuffVO svo : list){
					AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(getUIControl().getBillVoName()[0]).newInstance();
					aVo.setParentVO(svo);
					getBufferData().addVOToBuffer(aVo);
				}
			}
			
			if (getBufferData().getVOBufferSize() != 0) {

				this.setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
				this.setBillOperate(IBillOperate.OP_NOTEDIT);
				getBufferData().setCurrentRow(0);
			} else {
				this.setListHeadData(null);
				this.setBillOperate(IBillOperate.OP_INIT);
				getBufferData().setCurrentRow(-1);
				this.showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
								"UPPuifactory-000066")/* @res "没有查到任何满足条件的数据!" */);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
