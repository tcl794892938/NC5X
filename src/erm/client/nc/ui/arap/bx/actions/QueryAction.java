package nc.ui.arap.bx.actions;

/**
 * 报销管理--单据查询
 */
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.bx.BXBillMainPanel;
import nc.ui.er.pub.BillWorkPageConst;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.arap.bx.util.Page;
import nc.vo.arap.bx.util.PageUtil;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.pub.BusinessException;

/**
 * @author twei
 *
 * 查询活动
 * 
 * nc.ui.arap.bx.actions.QueryAction
 */
public class QueryAction extends BXDefaultAction {
	
	public void query() throws Exception{
		
		BXBillMainPanel mainPanel = getMainPanel();
				
		mainPanel.getQryDlg().showModal();
		
		if(mainPanel.getQryDlg().getResult() != QueryConditionClient.ID_OK)
			return;//没有选择“确定”

		
		doQuery(false);


	}
	
	/**
	* 刷新的时候调用
	 */
	public void refresh() throws Exception {
		
		doQuery(true);
	}
	
	public void doQuery(boolean isrefresh) throws Exception {
		
		BXBillMainPanel mainPanel = getMainPanel();
		
		if(isCard()){
			if(isrefresh){
				if(getCurrentSelectedVO()!=null && getCurrentSelectedVO().getParentVO().getPrimaryKey()!=null){
					List<BXVO> values = getIBXBillPrivate().queryVOsByPrimaryKeys(new String[]{getCurrentSelectedVO().getParentVO().getPrimaryKey()}, getCurrentSelectedVO().getParentVO().getDjdl());
					if(values!=null && values.size()!=0){
						getVoCache().addVO(values.get(0));
						mainPanel.updateView();
					}
					return ;
				}else{
					return ;
				}
			}else{
				//do query
			}
		}
		
		//权限过滤 by tcl 2016-11-02
		String user=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
		//财务经办人权限过滤
		String sql="select count(1) from sm_user_role r where r.pk_corp='"+pk_corp+"' " +
				" and r.pk_role=(select pk_role from sm_role s where s.role_code='08') " +
				"and r.cuserid='"+user+"' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer it=0;
		try {
			it=(Integer)iQ.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
		}
		
		String llr="";
		
		if(it>0){//经办人权限
			llr=" and zb.deptid in(select v.pk_deptdoc from v_tcl_userpsn v where " +
			" v.pk_corp='"+pk_corp+"' and v.userid='"+user+"') ";
		}
		
		DjCondVO condVO = getCondVO();
		condVO.setDefWhereSQL(condVO.getDefWhereSQL()+llr);
		
		int size = getIBXBillPrivate().querySize(condVO);
		
		if(condVO.isAppend){ //是否追加显示查询结果
			getVoCache().setPage(new PageUtil(size+getVoCache().getVoCache().size(),getVoCache().getPage().getThisPageNumber(),getVoCache().getMaxRecords()));
		}else{
			getVoCache().setPage(new PageUtil(size,Page.STARTPAGE,getVoCache().getMaxRecords()));
		}
		
		getVoCache().setQueryPage(new PageUtil(size,Page.STARTPAGE,getVoCache().getMaxRecords()*4));
		List<BXHeaderVO> bills = queryHeadersByPage(getVoCache().getQueryPage(),condVO);
		
		if(isCard()){
			if(condVO.isAppend){
				mainPanel.appendListVO(bills); //追加查询结果
				changeToList();
				mainPanel.updateView();
			}else{
				changeToList();
				mainPanel.setListVO(bills);  //设置查询结果
			}
		}else{
			if(condVO.isAppend){
				mainPanel.appendListVO(bills); //追加查询结果
				mainPanel.updateView();
			}else{
				mainPanel.setListVO(bills);  //设置查询结果
			}
		}
		getVoCache().setChangeView(false);
	}

	public void changeToList(){
		try {
			CardAction action = new CardAction();
			action.setActionRunntimeV0(this.getActionRunntimeV0());
			action.changeTab(BillWorkPageConst.LISTPAGE, true, false,null);
		} catch (BusinessException ev) {
			Logger.error(ev.getMessage());
		}
	}


}
