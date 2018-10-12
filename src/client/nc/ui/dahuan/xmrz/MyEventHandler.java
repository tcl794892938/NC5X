package nc.ui.dahuan.xmrz;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.ConReadBtnVO;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.xmrz.HtlogoDetail;
import nc.vo.dahuan.xmrz.HtlogoVO;
import nc.vo.pub.lang.UFBoolean;

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
	
	private IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	private String sql = "";
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		
		int it=this.getBillManageUI().getBillListPanel().getHeadTable().getSelectedRow();
		
		 if(intBtn == IdhButton.SJJL){//设计记录 0
			sjjl();
		/*}else if(intBtn == IdhButton.JHZT){//交货记录 1
			jhzt();
		}else if(intBtn == IdhButton.TJJL){//土建记录 2
			tjjl();
		}else if(intBtn == IdhButton.KSZT){//安装记录 3 
			kszt();
		}else if(intBtn == IdhButton.TSZT){//调试状态 4
			tszt(); 
		}else if(intBtn == IdhButton.WGZT){//完工记录 5
			wgzt();
		}else if(intBtn == IdhButton.CGJL){//采购记录 6
			cgjl();
		}else if(intBtn == IdhButton.QTZT){//其他记录 7
			qtzt();*/
		}else if(intBtn == IdhButton.JIEFENG){//确认阅读日志8
			qrrz();
		}
		 
		updateListView();
		//this.getBillManageUI().getBillListPanel().getHeadTable().changeSelection(it, 0, false, false);
		this.getBillManageUI().getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(it, it);//设置鼠标光点
	}	
	
	private void qrrz() throws Exception{
		
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		if(vo.getIs_anzhuang().equals(new UFBoolean("Y"))||
				vo.getIs_caigou().equals(new UFBoolean("Y"))||
				vo.getIs_jiaohuo().equals(new UFBoolean("Y"))||
				vo.getIs_qita().equals(new UFBoolean("Y"))||
				vo.getIs_sheji().equals(new UFBoolean("Y"))||
				vo.getIs_tiaoshi().equals(new UFBoolean("Y"))||
				vo.getIs_tujian().equals(new UFBoolean("Y"))||
				vo.getIs_wangong().equals(new UFBoolean("Y"))){
			
			//确认前删除之前日志
			String user=_getOperator();
			String pkContract=vo.getPk_contract();
			HYPubBO_Client.deleteByWhereClause(HtlogoDetail.class, " pk_contract = '"+pkContract+"' and pk_lookman ='"+user+"' ");
			
			HtlogoDetail htvo=new HtlogoDetail();
			htvo.setPk_contract(pkContract);
			htvo.setPk_lookman(user);
			HYPubBO_Client.insert(htvo);
			
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "该条日志并未产生任何记录，请重新操作！");
			return ;
		}
	}

	private void qtzt() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(7,vo.getPk_contract(),_getOperator());
	}

	private void cgjl() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(6,vo.getPk_contract(),_getOperator());
	}

	private void wgzt() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(5,vo.getPk_contract(),_getOperator());
	}



	private void tszt() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(4,vo.getPk_contract(),_getOperator());
	}



	private void kszt() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(3,vo.getPk_contract(),_getOperator());
	}
	
	private void tjjl() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(2,vo.getPk_contract(),_getOperator());
	}



	private void jhzt() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(1,vo.getPk_contract(),_getOperator());
	}
	

	private void sjjl() throws Exception {
		HtlogoVO vo = (HtlogoVO)this.getBufferData().getCurrentVO().getParentVO();
		
		HtlogoDialog dg = new HtlogoDialog(this.getBillUI());
		dg.showHtlogoDialog(0,vo.getPk_contract(),_getOperator());
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
				
		// 自动化部门特殊处理
		String deptsql = "select count(1) from sm_user_role u where u.pk_corp = '"+pkCorp+"' and u.cuserid = '"+user+"' " 
					+ " and u.pk_role = (select r.pk_role from sm_role r where r.role_code = 'DHXM' and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		int retCot = (Integer)query.executeQuery(deptsql, new ColumnProcessor());
		
		if(0<retCot){
			// 项目日志通查
			sql = " select distinct dt.htname,dt.htcode,dt.pk_contract,dt.customername,dt.manager,dt.startdate,dt.donedate,dt.contractor,dt.projectbudget,dt.htamount,dt.is_sheji,"
				+ " dt.is_jiaohuo,dt.is_tujian,dt.is_anzhuang,dt.is_tiaoshi,dt.is_wangong,dt.is_caigou,dt.is_qita," 
				+ "(case when dd.pk_contract is null then 'N' else 'Y' end ) as is_read from v_xmrz dt " 
				+ " left join bd_jobmngfil m on dt.pk_jobmandoc=m.pk_jobmngfil " 
				+ " left join (select * from dh_htlogo_detail d where d.pk_lookman ='"+user+"') dd on dt.pk_contract = dd.pk_contract" 
				+ " where nvl(m.dr,0)=0 and nvl(m.sealflag,'N')='N' and " //过滤封存
				+strWhere.toString()
				+" and dt.htcode not like'%宝%' and dt.htcode not like'%备%' order by dt.htcode " ; //过滤含宝 和含备 的数据 by tcl;
		}else{
			// 项目日志中，查询已付款的合同，并且本部门只能看本部门的合同信息
			sql = " select distinct dt.htname,dt.htcode,dt.pk_contract,dt.customername,dt.manager,dt.startdate,dt.donedate,dt.contractor,dt.projectbudget,dt.htamount,dt.is_sheji,"
				+ " dt.is_jiaohuo,dt.is_tujian,dt.is_anzhuang,dt.is_tiaoshi,dt.is_wangong,dt.is_caigou,dt.is_qita," 
				+ "(case when dd.pk_contract is null then 'N' else 'Y' end ) as is_read from v_xmrz dt " 
				+ " left join bd_jobmngfil m on dt.pk_jobmandoc=m.pk_jobmngfil "
				+ " left join (select * from dh_htlogo_detail d where d.pk_lookman ='"+user+"') dd on dt.pk_contract = dd.pk_contract" 
				+ " where nvl(m.dr,0)=0 and nvl(m.sealflag,'N')='N' and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+user+"' and v_deptperonal.pk_corp = '"+pkCorp+"' " +
					" and (v_deptperonal.pk_deptdoc=dt.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=dt.ht_dept)) " 
				+ " and "+strWhere.toString()
				+ " and dt.htcode not like'%宝%' and dt.htcode not like'%备%' order by dt.htcode " ; //过滤含宝 和含备 的数据 by tcl
		}
		
		updateListView();
	}

	private void updateListView() throws Exception{
		
		List<HtlogoVO> lit = (List<HtlogoVO>)query.executeQuery(sql, new BeanListProcessor(HtlogoVO.class));
		
		getBufferData().clear();
		
		// 增加数据到Buffer
		addDataToBuffer(lit.toArray(new HtlogoVO[]{}));//把lit的数据转换类型

		updateBuffer();
		
		//判断日志并修改颜色 by tcl
		int column=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("htcode").getShowOrder()-1;
		//int[] colorcols = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 ,9,10,11,12,13,14,15};
		int[] colorcols = new int[] {column};
		ArrayList<Integer> list=new ArrayList<Integer>();//未读
		ArrayList<Integer> listread=new ArrayList<Integer>();//已读
		for(int i=0;i<lit.size();i++){
			HtlogoVO vo=lit.get(i);
			if(vo.getIs_anzhuang().equals(new UFBoolean("Y"))||
					vo.getIs_caigou().equals(new UFBoolean("Y"))||
					vo.getIs_jiaohuo().equals(new UFBoolean("Y"))||
					vo.getIs_qita().equals(new UFBoolean("Y"))||
					vo.getIs_sheji().equals(new UFBoolean("Y"))||
					vo.getIs_tiaoshi().equals(new UFBoolean("Y"))||
					vo.getIs_tujian().equals(new UFBoolean("Y"))||
					vo.getIs_wangong().equals(new UFBoolean("Y"))){
				if(vo.getIs_read().equals(new UFBoolean("N"))){//再判断是否阅读
					list.add(i);
				}
				else{
					listread.add(i);
				}
			}
		}
		int[] colorrows=new int[list.size()];
		int[] colorrows2=new int[listread.size()];
		for(int i=0;i<list.size();i++){
			colorrows[i]=list.get(i);
		}
		for(int i=0;i<listread.size();i++){
			colorrows2[i]=listread.get(i);
		}
		((BillManageUI)getBillUI()).getBillListPanel().getHeadBillModel().setForeground(Color.RED, colorrows, colorcols);
		((BillManageUI)getBillUI()).getBillListPanel().getHeadBillModel().setForeground(new Color(178,107,255), colorrows2, colorcols);
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

		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	
	}
}