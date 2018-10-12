package nc.ui.arap.actions;
/**
 * �ֽ����---���ݲ�ѯ
 */
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.PowerClientService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.actions.common.DjFilterHelper;
import nc.ui.arap.actions.search.MapKeyGather;
import nc.ui.arap.billquery.NormalLogicalQueryPanel;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.ep.dj.DjPanel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.vo.arap.global.ResMessage;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uap.rbac.power.UserPowerQueryVO;

public class SearchActionNor extends SearchAction {


	/**
	 * @param pubDjPanel
	 * @param arapDjPanel
	 */
	public SearchActionNor() {
		super();
	}
	
	//�ݲ����ǲ�ѯ��Ϊ��2016-10-25 by tcl
	private String getDefWhereSQL(){
		if (this.m_qryDlg==null) {
			return getRefeshDefaultWhereCondition();
		}else {
			//liaobx ��Ӧ�ù���arap_djfb,arap_djfb�ϵ��������ܴ�arap_djzb�ϵõ���������ѯ������
			/*  from arap_djzb zb
 			inner join arap_djfb fb on zb.vouchid = fb.vouchid
 			where (zb.qcbz = 'N')
   			and (zb.pzglh = 0)
   			and (zb.dwbm = '1006' and fb.dwbm = '1006')
   			and (zb.djlxbm <> 'DR')
   			and (zb.dr = 0)
   			and (zb.hzbz = '-1')
   			and (zb.lybz <> 11)
   			and ((zb.djrq >= '2008-01-01' and zb.djrq <= '2008-10-31') and
       		(fb.billdate >= '2008-01-01' AND fb.billdate <= '2008-10-31'))
 					order by zb.djrq, zb.djlxbm, zb.djbh  
			 */
			//����Ҫƴ�������sql
			String user=ClientEnvironment.getInstance().getUser().getPrimaryKey();
			String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
			//����Ȩ�޹���
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
				llr=" and exists(select 1 from v_tcl_userpsn v where v.userid=zb.lrr and v.pk_deptdoc " +
				" in(select v.pk_deptdoc from v_tcl_userpsn v where " +
				" v.pk_corp='"+pk_corp+"' and v.userid='"+user+"')) ";
			}
			
			//String llr=" and zb.lrr='"+ClientEnvironment.getInstance().getUser().getPrimaryKey()+"'";
			if(!getQryDlg().getWhereSQL().contains("fb.")){
				Debug.debug("��̨sql:"+getQryDlg().getWhereSQL()+llr);
				return getQryDlg().getWhereSQL()+llr;
			}

			String fbDjrqSql=getFbDjrqSql();
			if (fbDjrqSql!=null) {
				fbDjrqSql=" and "+fbDjrqSql;
				Debug.debug("��̨sql:"+getQryDlg().getWhereSQL()+fbDjrqSql+llr);
				return getQryDlg().getWhereSQL()+fbDjrqSql;
			}else {
				Debug.debug("��̨sql:"+getQryDlg().getWhereSQL()+llr);
				return getQryDlg().getWhereSQL()+llr;
			}
		}
	}
	/*
	 * ���� Javadoc��
	 *
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#queryDjHeader()
	 */
	protected nc.vo.ep.dj.DjCondVO getCondVO() throws Exception {
		// �����ڱ�־
		if (getDjSettingParam().getIsQc())
			((NormalLogicalQueryPanel) getNormalLogicalQueryPanel()).setQcBz("Y");
		else
			((NormalLogicalQueryPanel) getNormalLogicalQueryPanel()).setQcBz("N");
		String[] dwbms = null;
		dwbms = new String[1];
		dwbms[0] = getDjSettingParam().getPk_corp();
		nc.vo.arap.pub.QryCondArrayVO[] vos = null;


		vos = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel()).getValueCondVO(getQryDlg().getWhereSQL().contains("fb."));

		// "���ڲ�ѯ����,���Ժ�..."*/);
		nc.bs.logging.Log.getInstance(this.getClass()).warn("���ڲ�ѯ����,���Ժ�..."); /*-=nottranslate=-*/
		// ���²�ѯ�����б��ͷ
		// �����Զ��������ͳ���������ѯ����
		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
//		TODO Ǩ��
		cur_Djcondvo.m_NorCondVos = this.getCondArrayVO(vos); 
//		cur_Djcondvo.m_DefCondVos = getQryDlg().getConditionVO();
		UserPowerQueryVO userPowerQueryVO=new UserPowerQueryVO();
		userPowerQueryVO.setCorpPK(getDjSettingParam().getPk_corp());
		userPowerQueryVO.setOrgPK(getDjSettingParam().getPk_corp());
		userPowerQueryVO.setResouceId(201);//sm_powerresource �����������
		userPowerQueryVO.setUserPK(getDjSettingParam().getPk_user());
		String djlxbmPower=PowerClientService.getInstance().getUserPowerSql(userPowerQueryVO, "��������"); /*-=nottranslate=-*/
		if(UFBoolean.TRUE.equals(((DjPanel) this.getParent()).getIsAloneNode())){
			if(null != djlxbmPower){
				cur_Djcondvo.defWhereSQL =getDefWhereSQL() + " and zb.djlxbm = '"+getDataBuffer().getCurrentDjlxbm()+"' and zb.djlxbm in (" + djlxbmPower+")";
			}else{
				cur_Djcondvo.defWhereSQL =getDefWhereSQL() + " and zb.djlxbm = '"+getDataBuffer().getCurrentDjlxbm()+"'";
			}
		}else {
			if(null != djlxbmPower){//
				cur_Djcondvo.defWhereSQL =getDefWhereSQL()+" and zb.djlxbm in (" + djlxbmPower+")" ;
			}else{//��Ȩ��ʱ
				cur_Djcondvo.defWhereSQL =getDefWhereSQL() ;
			}
		}
		cur_Djcondvo.isCHz = false;
		cur_Djcondvo.operator = getDjSettingParam().getPk_user();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();
		cur_Djcondvo.pk_corp = dwbms;
		cur_Djcondvo.isLinkPz = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel()).getchkBoxHasPZNum().isSelected();
		// set the "ƾ֤�Ƿ��Ѿ�����" info into the vo
		cur_Djcondvo.VoucherFlag = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel()).getVoucherStatus();

		cur_Djcondvo.isUsedGL = getDjSettingParam().getGlIsUsed().booleanValue();
		// this.m_djPanel.setCur_Djcondvo(cur_Djcondvo);
		// return DJZBBO_Client.queryDjLb_djcond(cur_Djcondvo);
		return cur_Djcondvo;
	}
	protected UIPanel getNormalLogicalQueryPanel() {
//		if(1==1)
//			return BXBillMainPanel.getQueryNormalPane();
		m_NormalLogicalQueryPanel = (NormalLogicalQueryPanel) ((DjPanel) this.getParent()).getAttribute(MapKeyGather.searchActionNor);
		if (null == m_NormalLogicalQueryPanel) {
			m_NormalLogicalQueryPanel = new NormalLogicalQueryPanel(getDjSettingParam().getSyscode());
			m_NormalLogicalQueryPanel.getcombVoucherStatus().setVisible(true);
//			m_NormalLogicalQueryPanel.getchkBoxIncludeDr().setVisible(true);
			m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(true);
			if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_EC_SignatureConfirm || !getDjSettingParam().getGlIsUsed().booleanValue()
					|| getDjSettingParam().getIsQc()) {
				m_NormalLogicalQueryPanel.getcombVoucherStatus().setEnabled(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(false);
				// m_NormalLogicalQueryPanel.getcombVoucherStatus().setSelected(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setSelected(false);
			}
			// ����ϵͳ��־
			int syscode=getDjSettingParam().getSyscode();
			//m_NormalLogicalQueryPanel.setSysCode(syscode);
			if (syscode == ResMessage.$SysCode_XT_AR || syscode == ResMessage.$SysCode_XT_AP || syscode == ResMessage.$SysCode_XT_EP) {
				m_NormalLogicalQueryPanel.getcombVoucherStatus().setEnabled(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setSelected(false);
				m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(false);
			}
			if (syscode == ResMessage.$SysCode_AR || syscode == ResMessage.$SysCode_AP
					|| syscode == ResMessage.$SysCode_EP || syscode== ResMessage.$SysCode_DjQuery) {
				if (getDjSettingParam().getGlIsUsed().booleanValue()) {
					m_NormalLogicalQueryPanel.getchkBoxHasPZNum().setVisible(true);

				}
			}
			if(!"2006030102".equals(((DjPanel) this.getParent()).getNodeCode()) && !"2008030102".equals(((DjPanel) this.getParent()).getNodeCode())&& !"20040302".equals(((DjPanel) this.getParent()).getNodeCode())){
				m_NormalLogicalQueryPanel.getcombAudit().setEnabled(false);
			}
			String strPK = nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp();
			m_NormalLogicalQueryPanel.getrefDwbm().setPK(strPK);
			m_NormalLogicalQueryPanel.getrefDwbm().setEnabled(false);
			((DjPanel) this.getParent()).setAttribute(MapKeyGather.searchActionNor, m_NormalLogicalQueryPanel);
		}
		return m_NormalLogicalQueryPanel;
	}
//	/**
//	 * ���ܣ��õ���ѯģ�峣�ò�ѯ����pane ���ߣ����� ����ʱ�䣺(2007-5-16 15:51:39) ������ <|>����ֵ�� �㷨�� �쳣������
//	 *
//	 * @return nc.ui.arap.pub.NormalPanel
//	 */
//	protected UIPanel getNormalPanel() {
//		m_pNormalPane = (NormalPanel) ((DjPanel) this.getParent()).getAttribute(MapKeyGather.searchActionNor);
//		if (null == m_pNormalPane) {
//			m_pNormalPane = new NormalPanel();
//			if (getDjSettingParam().getIsQc()) {
//				m_pNormalPane.getcombVoucherStatus().setEditable(false);
//				String djlxWhere = "";
//
//				if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AR) {
//					djlxWhere = " where ( dr=0 and dwbm='" + getDjSettingParam().getPk_corp()
//							+ "') and (djdl='ys' or djdl='sk'or djdl='yf' or djdl='fk') ";
//					//
//					m_pNormalPane.getcombWldx().removeAllItems();
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumKH);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumBM);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumYWY);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumAll);
//					m_pNormalPane.getcombWldx().setSelectedIndex(0);
//					m_pNormalPane.getrefCustom().setRefNodeName("�ͻ���������");//ԭ���ͻ�������������
//					m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//
//				} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AP) {
//					djlxWhere = " where ( dr=0 and dwbm='" + getDjSettingParam().getPk_corp()
//							+ "') and (djdl='ys' or djdl='sk'or djdl='yf' or djdl='fk') ";
//					//
//					m_pNormalPane.getcombWldx().removeAllItems();
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumGYS);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumBM);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumYWY);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumAll);
//					m_pNormalPane.getcombWldx().setSelectedIndex(0);
//					m_pNormalPane.getrefCustom().setRefNodeName("��Ӧ�̸�������");//ԭ����Ӧ�̵�����������
//					m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//					//
//
//				} else
//					djlxWhere = " where ( dr=0 and dwbm='" + getDjSettingParam().getPk_corp()
//							+ "') and (djdl='ys' or djdl='yf' or djdl='sk' or djdl='fk')  ";
//				m_pNormalPane.getrefDjlx().setWhereString(djlxWhere);
//			} else {
//				if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AR) {
//
//					m_pNormalPane.getcombWldx().removeAllItems();
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumKH);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumBM);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumYWY);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumAll);
//					m_pNormalPane.getcombWldx().setSelectedIndex(0);
//					m_pNormalPane.getrefCustom().setRefNodeName("�ͻ���������");
//					m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//
//				} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AP) {
//
//					m_pNormalPane.getcombWldx().removeAllItems();
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumGYS);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumBM);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumYWY);
//					m_pNormalPane.getcombWldx().addItem(m_pNormalPane.m_enumAll);
//					m_pNormalPane.getcombWldx().setSelectedIndex(0);
//					m_pNormalPane.getrefCustom().setRefNodeName("��Ӧ�̸�������");
//					m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//
//				}
//
//				m_pNormalPane.getcombWldx().addItemListener(new java.awt.event.ItemListener() {
//					public void itemStateChanged(java.awt.event.ItemEvent e) {
//						String value = m_pNormalPane.getcombWldx().getSelectedItem() == null ? "" : m_pNormalPane
//								.getcombWldx().getSelectdItemValue().toString();
//						// .getSelectedItem().toString();
//						if (value.equals("0")) // �ͻ�
//						{
//							m_pNormalPane.getrefCustom().setRefNodeName("�ͻ���������");
//							m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//						} else if (value.equals("1")) // ��Ӧ��
//						{
//							m_pNormalPane.getrefCustom().setRefNodeName("��Ӧ�̸�������");
//							m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//						} else {
//							// if
//							// (value.equals(getUINormalPane().m_strAll))
//							// {
//							m_pNormalPane.getrefCustom().setRefNodeName("���̸�������");
//							m_pNormalPane.getrefCustom().getRefModel().setSealedDataShow(true);
//						}
//					}
//
//				});
//
//			}
//
//			// FocusUtils.focusNextComponent(m_pNormalPane.getCondPane());
//			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm || !getDjSettingParam().getGlIsUsed().booleanValue()
//					|| getDjSettingParam().getIsQc()) {
//				m_pNormalPane.getcombVoucherStatus().setEnabled(false);
//				m_pNormalPane.getchkBoxHasPZNum().setVisible(false);
//				// m_pNormalPane.getcombVoucherStatus().setSelected(false);
//				m_pNormalPane.getchkBoxHasPZNum().setSelected(false);
//
//			}
//			resetDjlxRefBySyscode(m_pNormalPane.getrefDjlx());
//			m_pNormalPane.setMultiSelect(true);
//			// ����ϵͳ��־
//
//			m_pNormalPane.setSysCode(getDjSettingParam().getSyscode());
//			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AR
//					|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AP
//					|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_EP) {
//				m_pNormalPane.getcombVoucherStatus().setEnabled(false);
//				m_pNormalPane.getchkBoxHasPZNum().setSelected(false);
//				m_pNormalPane.getchkBoxHasPZNum().setVisible(false);
//			}
//			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AR || getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AP
//					|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EP || getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_DjQuery) {
//				if (getDjSettingParam().getGlIsUsed().booleanValue()) {
//					m_pNormalPane.getchkBoxHasPZNum().setVisible(true);
//
//				}
//			}
//			((DjPanel) this.getParent()).setAttribute(MapKeyGather.searchActionNor, m_pNormalPane);
//		}
//		return m_pNormalPane;
//	}



	/*
	 * ���� Javadoc�� ��ѯ��ͷ
	 *
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#queryHeaders(java.lang.Integer, java.lang.Integer)
	 */
	protected Vector queryHeaders(Integer initPos, Integer initCount) throws Exception {
		return new DjFilterHelper(((AbstractRuntime)this.getActionRunntimeV0())).
		queryDjs(initPos, initCount,((DjPanel) this.getParent()).getCur_Djcondvo()==null?getDefaultCondVO():((DjPanel) this.getParent()).getCur_Djcondvo());

	}

	/*
	 * ���� Javadoc�� ���ò�ѯ�Ի���ߴ�
	 *
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlSearch#setQryDlgSize()
	 */
	protected void setQryDlgSize() {

		this.m_qryDlg.setSize(650, 400);
//		this.m_qryDlg.setSize(536, 400);
	}
}
