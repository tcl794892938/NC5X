package nc.ui.arap.bx.actions;

/**
 * ��������--���ݲ�ѯ
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
 * ��ѯ�
 * 
 * nc.ui.arap.bx.actions.QueryAction
 */
public class QueryAction extends BXDefaultAction {
	
	public void query() throws Exception{
		
		BXBillMainPanel mainPanel = getMainPanel();
				
		mainPanel.getQryDlg().showModal();
		
		if(mainPanel.getQryDlg().getResult() != QueryConditionClient.ID_OK)
			return;//û��ѡ��ȷ����

		
		doQuery(false);


	}
	
	/**
	* ˢ�µ�ʱ�����
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
		
		//Ȩ�޹��� by tcl 2016-11-02
		String user=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
		//���񾭰���Ȩ�޹���
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
		
		if(it>0){//������Ȩ��
			llr=" and zb.deptid in(select v.pk_deptdoc from v_tcl_userpsn v where " +
			" v.pk_corp='"+pk_corp+"' and v.userid='"+user+"') ";
		}
		
		DjCondVO condVO = getCondVO();
		condVO.setDefWhereSQL(condVO.getDefWhereSQL()+llr);
		
		int size = getIBXBillPrivate().querySize(condVO);
		
		if(condVO.isAppend){ //�Ƿ�׷����ʾ��ѯ���
			getVoCache().setPage(new PageUtil(size+getVoCache().getVoCache().size(),getVoCache().getPage().getThisPageNumber(),getVoCache().getMaxRecords()));
		}else{
			getVoCache().setPage(new PageUtil(size,Page.STARTPAGE,getVoCache().getMaxRecords()));
		}
		
		getVoCache().setQueryPage(new PageUtil(size,Page.STARTPAGE,getVoCache().getMaxRecords()*4));
		List<BXHeaderVO> bills = queryHeadersByPage(getVoCache().getQueryPage(),condVO);
		
		if(isCard()){
			if(condVO.isAppend){
				mainPanel.appendListVO(bills); //׷�Ӳ�ѯ���
				changeToList();
				mainPanel.updateView();
			}else{
				changeToList();
				mainPanel.setListVO(bills);  //���ò�ѯ���
			}
		}else{
			if(condVO.isAppend){
				mainPanel.appendListVO(bills); //׷�Ӳ�ѯ���
				mainPanel.updateView();
			}else{
				mainPanel.setListVO(bills);  //���ò�ѯ���
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
