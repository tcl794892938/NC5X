package nc.ui.dahuan.fhqr;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fhtz.DhDeliveryVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillManageUI {

	
	
	public ClientUI() {
		super();
		try {
			initValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)throws Exception {	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)throws Exception {	}

	@Override
	protected void initSelfData() {	}

	@Override
	public void setDefaultData() throws Exception {	}

	public void initValue() throws Exception{
		
		String pkCorp = this._getCorp().getPrimaryKey();
		
		String sql = " select * from dh_delivery t where nvl(t.dr,0)=0 and t.relationid is not null and nvl(t.isdelivery,0)=0 "+
				" and exists(select 1 from dh_contract f where f.pk_contract = t.relationid and f.pk_corp='"+pkCorp+"') ";
		
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<DhDeliveryVO> dlist = (List<DhDeliveryVO>)iQ.executeQuery(sql, new BeanListProcessor(DhDeliveryVO.class));
		DhDeliveryVO[] queryVos = dlist.toArray(new DhDeliveryVO[0]);
		
		getBufferData().clear();
		if (queryVos != null && queryVos.length != 0)
		{
			for (int i = 0; i < queryVos.length; i++)
			{
				AggregatedValueObject aVo =
					(AggregatedValueObject) Class
						.forName(getUIControl().getBillVoName()[0])
						.newInstance();
				aVo.setParentVO(queryVos[i]);
				getBufferData().addVOToBuffer(aVo);
			}
			setListHeadData(queryVos);
			getBufferData().setCurrentRow(0);
			setBillOperate(IBillOperate.OP_NOTEDIT);
		}
		else
		{
			setListHeadData(queryVos);
			getBufferData().setCurrentRow(-1);
			setBillOperate(IBillOperate.OP_INIT);
		}
	}

	@Override
	protected void initPrivateButton() {
		ButtonVO aBtn = ButtonVOFactory.getInstance().build(IBillButton.Audit);
		aBtn.setBtnName("发货确认");
		addPrivateButton(aBtn);
	}
	
	
	
}
