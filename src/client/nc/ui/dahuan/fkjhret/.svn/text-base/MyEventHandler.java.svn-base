package nc.ui.dahuan.fkjhret;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		this.getButtonManager().getButton(IBillButton.Edit).setVisible(false);//设置’Edit‘按钮不可见
	}
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		String pkuser = this._getOperator();
		String pkcorp = this._getCorp().getPrimaryKey();
		
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		
		String strCondition = strWhere.toString()+	" and dh_retrecord.ret_type = 1 ";
		
		SuperVO[] queryVos = queryHeadVOs(strCondition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
}