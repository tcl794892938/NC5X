package nc.ui.dahuan.fkbdprint;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
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
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		String pkuser = this._getOperator();
		String pkcorp = this._getCorp().getPrimaryKey();
		String corpname = this._getCorp().getUnitname();
		
		
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		//付款绑定查询的条件是工程部已经打印过了 is_print = 1
		String strCondition = " select distinct dh_fkbdprint.* from dh_fkbdprint,dh_fkbdprint_d where dh_fkbdprint.pk_fkbd = dh_fkbdprint_d.pk_fkbd " +
					"  and nvl(dh_fkbdprint_d.dr,0)=0 and nvl(dh_fkbdprint.dr,0)=0 and dh_fkbdprint.corp_name like '%"+corpname+"%' and " 
			+strWhere;
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<DhFkbdprintVO> volit = (List<DhFkbdprintVO>)iQ.executeQuery(strCondition, new BeanListProcessor(DhFkbdprintVO.class));
		
		getBufferData().clear();
		// 增加数据到Buffers
		addDataToBuffer(volit.toArray(new DhFkbdprintVO[0]));

		updateBuffer();
	}

	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf) throws Exception {

		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}
		//重写 strWhere的查询公式
		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
		}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoPrint();
		
		BillManageUI dyui = (BillManageUI)this.getBillUI();
		PrintEntry print = new PrintEntry(dyui);
		
		String nodeCode = dyui._getModuleCode();
		String pkcorp = this._getCorp().getPrimaryKey();
		String pkuser = this._getOperator();
        print.setTemplateID(pkcorp, nodeCode, pkuser, null,null);
		
        
		if(dyui.isListPanelSelected()){
			
			DhFkbdprintVO[] vos = (DhFkbdprintVO[])dyui.getBillListPanel().getHeadBillModel().getBodySelectedVOs(DhFkbdprintVO.class.getName());
			for(DhFkbdprintVO dyvo : vos){
				IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, dyvo);
		        
		        print.setDataSource(dataSource);
			}
			
		}else{
			DhFkbdprintVO fkvo = (DhFkbdprintVO)this.getBufferData().getCurrentVO().getParentVO();
			
			IDataSource dataSource = new FkjhClientUIPRTS(nodeCode, fkvo);
	        
	        print.setDataSource(dataSource);
		}
		
		if(print.selectTemplate()==1){
			print.preview();
		}
	}
	
	
		
}