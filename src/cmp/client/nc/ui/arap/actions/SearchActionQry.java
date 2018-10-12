package nc.ui.arap.actions;

import java.util.ArrayList;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.fi.pub.Currency;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.actions.common.DjFilterHelper;
import nc.ui.arap.actions.search.MapKeyGather;
import nc.ui.arap.billquery.NormalLogicalQueryPanel;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.DjPanel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.pub.BusinessException;
import nc.vo.arap.pub.QryCondArrayVO;
import nc.vo.arap.pub.QryCondVO;

public class SearchActionQry extends SearchAction {


	/**
	 * @param pubDjPanel
	 * @param arapDjPanel
	 */
	public SearchActionQry() {
		super();
	}
	protected UIPanel getNormalLogicalQueryPanel() {
		m_NormalLogicalQueryPanel = (NormalLogicalQueryPanel) ((DjPanel) this.getParent()).getAttribute(MapKeyGather.searchActionQry);
		if (null == m_NormalLogicalQueryPanel){	
			m_NormalLogicalQueryPanel = new nc.ui.arap.billquery.NormalLogicalQueryPanel(getDjSettingParam().getSyscode());
			if (getDjSettingParam().getIsQc()) {
				m_NormalLogicalQueryPanel.getcombVoucherStatus().setEditable(false);
			} 
			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm
					|| !getDjSettingParam().getGlIsUsed().booleanValue()
					|| getDjSettingParam().getIsQc()) {
				m_NormalLogicalQueryPanel.getcombVoucherStatus().setEnabled(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setSelected(false);
			}
			// 设置系统标志
			//m_NormalLogicalQueryPanel.setSysCode(getDjSettingParam().getSyscode());	
			if (getDjSettingParam().getGlIsUsed().booleanValue()) {
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(true);
			
			}
			((DjPanel) this.getParent()).setAttribute(MapKeyGather.searchActionQry, m_NormalLogicalQueryPanel);					 			 		
		}	 
		return m_NormalLogicalQueryPanel;
	}
	private String getDefWhereSQL(){
		if (this.m_qryDlg==null) {
			return getRefeshDefaultWhereCondition();
		}else {
			return getQryDlg().getWhereSqlWithoutPower();
//			return this.getQryDlg().getWhereSQL()+" and "+getFbDjrqSql();
		}
	}
//	protected UIPanel getNormalPanel() {
//		m_pNormalPane = (NormalPanel) ((DjPanel) this.getParent()).getAttribute(MapKeyGather.searchActionQry);
//		if (null == m_pNormalPane){	
//			m_pNormalPane = new nc.ui.arap.billquery.NormalPanel();
//			if (getDjSettingParam().getIsQc()) {
//				m_pNormalPane.getcombVoucherStatus().setEditable(false);
//				String djlxWhere = "";
//				djlxWhere = " where ( dr=0 and dwbm='"
//						+ getDjSettingParam().getPk_corp()
//						+ "') and (djdl='ys' or djdl='yf' or djdl='sk' or djdl='fk')  ";
//				m_pNormalPane.getrefDjlx().setWhereString(djlxWhere);
//			} else {
//				m_pNormalPane.getcombWldx().addItemListener(
//						new java.awt.event.ItemListener() {
//							public void itemStateChanged(
//									java.awt.event.ItemEvent e) {
//								String value = m_pNormalPane.getcombWldx()
//										.getSelectedItem() == null ? ""
//										: m_pNormalPane.getcombWldx()
//												.getSelectdItemValue()
//												.toString();
//								// .getSelectedItem().toString();
//								if (value.equals("0")) // 客户
//								{
//									m_pNormalPane.getrefCustom()
//											.setRefNodeName("客户辅助核算");
//								} else if (value.equals("1")) // 供应商
//								{
//									m_pNormalPane.getrefCustom()
//											.setRefNodeName("供应商辅助核算");
//								} else {
//									// if
//									// (value.equals(getUINormalPane().m_strAll))
//									// {
//									m_pNormalPane.getrefCustom()
//											.setRefNodeName("客商辅助核算");
//								}
//							}
//						});
//			}
//			// FocusUtils.focusNextComponent(m_pNormalPane.getCondPane());
//			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm
//					|| !getDjSettingParam().getGlIsUsed().booleanValue()
//					|| getDjSettingParam().getIsQc()) {
//				m_pNormalPane.getcombVoucherStatus().setEnabled(false);
//				m_pNormalPane.getchkBoxHasPZNum().setVisible(false);
//				m_pNormalPane.getchkBoxHasPZNum().setSelected(false);
//
//			}
//			resetDjlxRefBySyscode(m_pNormalPane.getrefDjlx());
//			m_pNormalPane.setMultiSelect(true);
//			// 设置系统标志
//			m_pNormalPane.setSysCode(getDjSettingParam().getSyscode());
//			if (getDjSettingParam().getGlIsUsed().booleanValue()) {
//				m_pNormalPane.getchkBoxHasPZNum().setVisible(true);
//				
//			}
//			m_pNormalPane.setIncludeDrValid(true);
//			m_pNormalPane.setMultiSelect(true);
//		
//			((DjPanel) this.getParent()).setAttribute(MapKeyGather.searchActionQry, m_pNormalPane);					 			 		
//		}	 
//		return m_pNormalPane;
//	}

	public static String[] getCorps(String[] Corps) {
		if(Corps==null || Corps.length==0){
			String sCorp = nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp();
			return new String[]{sCorp};
		}
		String[] strCorps = new String[Corps.length];
		for(int i=0;i<Corps.length;i++){
			strCorps[i] =Corps[i];
		}
		return strCorps;
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#getCondVO()
	 */
	protected DjCondVO getCondVO() throws Exception {
		//nc.vo.pub.query.RefResultVO[] refs = null;
		String[] dwbms = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
				.getDwbmStr() == null ? new String[] { getDjSettingParam()
				.getPk_corp() }
				: ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
						.getDwbmStr();
		//refs = this.getQryDlg().getMutiUnits();
		//dwbms =  getCorps(refs);
		// if (strDjdl.equals("ss"))
		// m_djPanel.getUINorPane().setCorps(dwbms);
		// else
		((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setQcBz(null);
		//((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setCorps(dwbms);
		nc.vo.arap.pub.QryCondArrayVO[] vos = null;
		((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setQueryNode(true);
		vos = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getValueCondVO();
		// 常用条件vo

//		this.getQryDlg().getConditionVO(); // 自定义条件VO

		// 自定义查询条件
//		this.getQryDlg().getWhereSQL();

		// ///////////////////////////////////////////////////////////
		// 以上调用公用的查询模板

		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "正在查询单据,请稍候..."*/);
		nc.bs.logging.Log.getInstance(this.getClass()).warn("正在查询单据,请稍候..."); /*-=nottranslate=-*/
		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		// 以下查询单据列表表头

		// if (strDjdl.equals("ss"))
		// djzbheadervo = DJZBBO_Client.queryDjLbQ_SS(vos,
		// m_djPanel.getQryDlg().getConditionVO(), true, refs);
		// else {
//		nc.vo.pub.query.ConditionVO[] cvo = this.getQryDlg().getConditionVO();
		cur_Djcondvo.defWhereSQL = getDefWhereSQL();
		if(null!=cur_Djcondvo.defWhereSQL&&cur_Djcondvo.defWhereSQL.contains("zb.dr = 1")){
			cur_Djcondvo.isJustQryDel = true;//只查询已删除的单据
			vos = isJustQryDel(vos);
		}
		cur_Djcondvo.m_NorCondVos = vos;
//		cur_Djcondvo.m_DefCondVos = cvo;
		cur_Djcondvo.isCHz = false;
		cur_Djcondvo.m_UseFlag=111;
		cur_Djcondvo.operator = getDjSettingParam().getPk_user();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();
		if(null != ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getrefDwbm().getRefPK()){
			cur_Djcondvo.pk_corp = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getDwbmStr();
		}else {
			cur_Djcondvo.pk_corp = dwbms;
		}
		cur_Djcondvo.isLinkPz = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getchkBoxHasPZNum().isSelected();
		// set the "凭证是否已经记帐" info into the vo
		cur_Djcondvo.VoucherFlag = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getVoucherStatus();
		cur_Djcondvo.isUsedGL = getDjSettingParam().getGlIsUsed()
				.booleanValue();
		//cur_Djcondvo.refs = refs;
		
		//重设查询条件 by tcl 2016-11-03
		String user=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
		//财务权限过滤
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
			llr=" and exists(select 1 from v_tcl_userpsn v where v.userid=zb.lrr and v.pk_deptdoc " +
			" in(select v.pk_deptdoc from v_tcl_userpsn v where " +
			" v.pk_corp='"+pk_corp+"' and v.userid='"+user+"')) ";
		}
		cur_Djcondvo.setDefWhereSQL(cur_Djcondvo.getDefWhereSQL()+llr);
		return cur_Djcondvo;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#queryHeaders(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	protected Vector queryHeaders(Integer initPos, Integer initCount)
			throws Exception {
//		return ProxyCommon.getIArapBillPrivate().filterDj(initPos,
//				initCount, ((DjPanel) this.getParent()).getCur_Djcondvo());
		return new DjFilterHelper(((AbstractRuntime)this.getActionRunntimeV0())).qeruyDjs_Qry(initPos, initCount,((DjPanel) this.getParent()).getCur_Djcondvo()==null?getDefaultCondVO():((DjPanel) this.getParent()).getCur_Djcondvo());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#setQryDlgSize()
	 */
	protected void setQryDlgSize() {
		this.m_qryDlg.setSize(650, 400);
	}
	
	/*
	 * (non-Javadoc)
	 * @see nc.ui.arap.actions.SearchAction#getDefaultCondVO()
	 */
	protected DjCondVO getDefaultCondVO() throws Exception {
		nc.vo.pub.query.RefResultVO[] refs = null;
		String[] dwbms = new String[]{getDjSettingParam().getPk_corp()};
//		refs = this.getQryDlg().getMutiUnits();
		// if (strDjdl.equals("ss"))
		// m_djPanel.getUINorPane().setCorps(dwbms);
		// else
		((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setQcBz(null);
		//((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setCorps(dwbms);
		nc.vo.arap.pub.QryCondArrayVO[] vos = null;
		((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).setQueryNode(true);
		vos = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getValueCondVO();
		// 常用条件vo

//		this.getQryDlg().getConditionVO(); // 自定义条件VO

		// 自定义查询条件
		this.getQryDlg().getWhereSQL();

		// ///////////////////////////////////////////////////////////
		// 以上调用公用的查询模板

		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "正在查询单据,请稍候..."*/);
		nc.bs.logging.Log.getInstance(this.getClass()).warn("正在查询单据,请稍候..."); /*-=nottranslate=-*/
		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		// 以下查询单据列表表头

		// if (strDjdl.equals("ss"))
		// djzbheadervo = DJZBBO_Client.queryDjLbQ_SS(vos,
		// m_djPanel.getQryDlg().getConditionVO(), true, refs);
		// else {
//		nc.vo.pub.query.ConditionVO[] cvo = this.getQryDlg().getConditionVO();
		cur_Djcondvo.defWhereSQL ="zb.djrq = '"+getDjSettingParam().getLoginDate()+ "' AND fb.bzbm = '" + Currency.getLocalCurrPK(getDjSettingParam().getPk_corp())+ "'";
		cur_Djcondvo.m_NorCondVos = vos;
//		cur_Djcondvo.m_DefCondVos = cvo;
		cur_Djcondvo.isCHz = false;
		cur_Djcondvo.operator = getDjSettingParam().getPk_user();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();
		if(null != ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getrefDwbm().getRefPK()){
			cur_Djcondvo.pk_corp = new String[]{((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getrefDwbm().getRefPK().toString()};
		}else {
			cur_Djcondvo.pk_corp = dwbms;
		}
		cur_Djcondvo.isLinkPz = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getchkBoxHasPZNum().isSelected();
		// set the "凭证是否已经记帐" info into the vo
		cur_Djcondvo.VoucherFlag = ((NormalLogicalQueryPanel)getNormalLogicalQueryPanel()).getVoucherStatus();
		cur_Djcondvo.isUsedGL = getDjSettingParam().getGlIsUsed()
				.booleanValue();
		cur_Djcondvo.refs = refs;
		return cur_Djcondvo;
	}
	
	private QryCondArrayVO[] isJustQryDel(QryCondArrayVO[] vos) {
		ArrayList<QryCondArrayVO> array = new ArrayList<QryCondArrayVO>();
		for(int i = 0 ; i < vos.length; i++){
			QryCondVO[] qryCondVOs = vos[i].getItems();
			boolean flag = true;
			for(int j = 0; j < qryCondVOs.length; j++){
				if("dr".equals(qryCondVOs[j].getQryfld())){
					flag = false;
				}
			}
			if(flag){
				array.add(vos[i]);
			}
		}
		return array.toArray(new QryCondArrayVO[array.size()]);
	}
}
