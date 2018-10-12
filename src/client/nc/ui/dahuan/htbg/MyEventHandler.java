package nc.ui.dahuan.htbg;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.xmrz.HtlogoVO;
import nc.vo.pub.SuperVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		
		 

	}	

	
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	@Override
	protected void onBoQuery() throws Exception {
		
		StringBuffer strWhere = new StringBuffer();
		
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		//添加过滤条件
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		String sql = " select case when t.vdef6 is null then (select v.invname from bd_invbasdoc v where v.pk_invbasdoc = t.ctname) "
			+ " else t.vdef6 end htname,t.ctcode htcode,t.pk_contract from dh_contract t where t.httype in ('0', '1') "
			+ " and t.is_seal = '1' ";
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		List<HtlogoVO> lit = (List<HtlogoVO>)query.executeQuery(sql, new BeanListProcessor(HtlogoVO.class));

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(lit.toArray(new HtlogoVO[]{}));//把lit的数据转换类型

		updateBuffer();
		
	}
	
}