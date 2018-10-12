package nc.ui.dahuan.hkjh.yr;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillListUI implements ILinkQuery {


	@Override
	protected IListController createController() {
		return new ClientUICtrl();
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	@Override
	protected ListEventHandler createEventHandler() {
		return new MyEventHandler(this,this.getUIControl());
	}

	
	
	@Override
	protected void initPrivateButton() {
		ButtonVO impBtn = ButtonVOFactory.getInstance().build(IBillButton.ImportBill);
		impBtn.setBtnName("引入");
		addPrivateButton(impBtn);
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {

	}

	@Override
	public void setDefaultData() throws Exception {

	}

	public void doQueryAction(ILinkQueryData querydata) {

	}

	@Override
	protected void initBillData(String strWhere) throws Exception {
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
		String sql = " select f.* from dh_fkjhbill f where exists (select 1 from dh_contract y where exists (select 1 from dh_contract t " +
				" where t.is_relation = 1 and t.pk_corp = '"+pkCorp+"' and t.is_seal = 1  and nvl(t.dr, 0) = 0 and t.relationid = y.pk_contract) " +
				" and y.ctcode = f.ctcode) and f.is_pay = 1 and nvl(is_relation, 0) = 0 ";
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<DhFkjhbillVO> list = (List<DhFkjhbillVO>)iQ.executeQuery(sql, new BeanListProcessor(DhFkjhbillVO.class));
		DhFkjhbillVO[] queryVos = list.toArray(new DhFkjhbillVO[0]);
		
		//清空缓冲数据
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

	
}
