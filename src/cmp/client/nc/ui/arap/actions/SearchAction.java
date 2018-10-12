package nc.ui.arap.actions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.impl.arap.proxy.Proxy;
import nc.itf.fi.pub.Currency;
import nc.ui.arap.engine.IActionRuntime;
import nc.ui.arap.actions.search.MapKeyGather;
import nc.ui.arap.billquery.CheckflagFilterEditer;
import nc.ui.arap.billquery.DjztFilterEditer;
import nc.ui.arap.billquery.KjqjFilterEditor;
import nc.ui.arap.billquery.NormalLogicalQueryPanel;
import nc.ui.arap.billquery.QueryPubMethod;
import nc.ui.arap.billquery.SxbzFilterEditer;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.arap.pubdj.AdjustVoAfterQuery;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.arap.selectedpay.Cache;
import nc.ui.arap.selectedpay.CacheHeadException;
import nc.ui.arap.selectedpay.EndResultSetException;
import nc.ui.arap.selectedpay.OverFlowException;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.DjlxRefModel1;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.FocusUtils;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillItem;
import nc.ui.querytemplate.ICriteriaEditor;
import nc.ui.querytemplate.IQueryTemplateTotalVOProcessor;
import nc.ui.querytemplate.filter.IFilter;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.filtereditor.IFilterEditorFactory;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.meta.IFilterMeta;
import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.querytemplate.querytreeeditor.QueryTreeEditor;
import nc.ui.querytemplate.simpleeditor.SimpleEditor;
import nc.ui.querytemplate.value.IFieldValue;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.ui.querytemplate.valueeditor.DefaultFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditorFactory;
import nc.ui.querytemplate.valueeditor.RefElementEditor;
import nc.ui.querytemplate.valueeditor.UIRefpaneCreator;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.pub.PubConstData;
import nc.vo.arap.pub.QryCondArrayVO;
import nc.vo.arap.pub.QryCondVO;
import nc.vo.arap.pub.StrResPorxy;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.querytemplate.TemplateInfo;

/**
 * @function
 * @author maji
 * @version V6.0
 * @since V6.0
 */
public abstract class SearchAction extends DefaultAction implements
		PubConstData {
	protected nc.ui.arap.querytemplate.QueryConditionDLG m_qryDlg = null;

	private windowListenerQuery m_windowsQuery = null;

	protected nc.ui.arap.billquery.NormalLogicalQueryPanel m_NormalLogicalQueryPanel = null;

	/**
	 * ����:�ı�ҳǩ(���ݺ��б�֮��) �������ڣ�(2001-5-21 15:07:39) ����:���� ����:tabIndex ҳǩ��,isChange
	 * false���л�ҳǩ
	 * 
	 * @throws BusinessException
	 */
	// refactoring:copied from DjPanel,note that the original one still exist
	// and keep its avail in DjPanel
	@SuppressWarnings("restriction")
	public void changeTab() throws BusinessException {
		DjPanel djp = (DjPanel) getActionRunntimeV0();
		djp.changeTab(this.getActionRunntimeV0().getCurrWorkPage() * -1, false);

		// 0ARAPDjDataBuffer dataBuffer = this.getDataBuffer();
		// BillModel billModel = getBillListPanel().getHeadBillModel();
		// int row = this.getBillListPanel().getHeadTable().getSelectedRow();
		// int rowcount_h = getBillListPanel().getHeadBillModel().getRowCount();
		// if (row >= rowcount_h)
		// row = rowcount_h - 1;
		// if(rowcount_h>0&&row<0)
		// row=0;
		// if (row <0 || rowcount_h <= 0) {
		// return;
		// }
		// String djoid =
		// getBillListPanel().getHeadBillModel().getValueAt(row,"vouchid").toString();
		// DJZBVO cur_djzbvo = (nc.vo.ep.dj.DJZBVO)
		// (dataBuffer.getDJZBVO(djoid));
		// DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO)
		// (cur_djzbvo.getParentVO());
		// DJZBItemVO[] items = (nc.vo.ep.dj.DJZBItemVO[])
		// (cur_djzbvo.getChildrenVO());
		// DjPanel djp=(DjPanel)getActionRunntimeV0();
		// if (this.getActionRunntimeV0().getCurrWorkPage() ==
		// ArapBillWorkPageConst.LISTPAGE) {
		// ((java.awt.CardLayout)djp.getLayout()).show(djp,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
		// "UPT2006030102-000021")/* @res "����" */);
		// this.getActionRunntimeV0().setCurrentpage(ArapBillWorkPageConst.CARDPAGE);
		// if (djp.getJPanel1().getComponentCount() < 1) { // ����ǳ�ʼ��,��һ���л�������
		// djp.getJPanel1().add(this.getArapDjPanel(), "Center");
		// try {
		// this.getArapDjPanel().addDjStateChangeListener(djp.getStateChange());
		// } catch (Throwable e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// "2006030102", "UPP2006030102-000438") + e);
		// }
		// this.getArapDjPanel().setTabTitle();
		// // try {
		// // this.getArapDjPanel().changeTab2(0,bInitializing);
		// // } catch (Throwable e) {
		// // Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// // "2006030102", "UPP2006030102-000439") + e);
		// // }
		// try {
		// this.getArapDjPanel().setM_DjState(0);
		// } catch (Throwable e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// "2006030102", "UPP200630102-000440") + e);
		// }
		// }
		//
		// // ����ѯ����,ˢ�µ���
		// if (rowcount_h > 0) {
		// try {
		// // ���õ���
		// this.getArapDjPanel().setDj(cur_djzbvo);
		//
		// } catch (Throwable e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		// "2006030102", "UPP2006030102-000441") + e);
		// }
		//
		// } else
		// this.getBillCardPanel().getBillData().clearViewData();
		// if (this.getActionRunntimeV0() instanceof FiFlowPanel) {
		// try {
		// ((FiFlowPanel) this.getActionRunntimeV0()).cpyAndAddBoStatListener();
		// ((FiFlowPanel) this.getActionRunntimeV0()).updateLocalButtons();
		// } catch (Exception e) {
		// ExceptionHandler.consume(e);
		// }
		// }
		// if (this.getDataBuffer().getCurrentDJZBVO() != null) {
		// DJZBHeaderVO headvo = (DJZBHeaderVO) this.getDataBuffer()
		// .getCurrentDJZBVO().getParentVO();
		// if (headvo.getDjzt() == DJZBVOConsts.m_intDJStatus_UNSaved) {
		// EditAction ea = new EditAction();
		// ea.setActionRunntimeV0(this.getActionRunntimeV0());
		// try {
		// ea.edit();
		// } catch (BusinessException e) {
		// ExceptionHandler.consume(e);
		// }
		// }
		// }
		//
		// } else {
		// ((java.awt.CardLayout) djp.getLayout()).show(((DjPanel)
		// this.getActionRunntimeV0()),
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
		// "UPT2006030102-000049")/* @res "�б�" */);
		// this.getActionRunntimeV0().setCurrentpage(ArapBillWorkPageConst.LISTPAGE);
		//
		// // ��Ƭ�л����б�ʱ������������ͷ
		// if (items != null && items.length > 0) {
		// head.setHbbm(items[0].getHbbm());
		// head.setYwybm(items[0].getYwybm());
		// head.setDeptid(items[0].getDeptid());
		// head.setBzbm(items[0].getBzbm());
		// head.setSzxmid(items[0].getSzxmid());
		// head.setJobid(items[0].getJobid());
		// head.setJobid(items[0].getJobid());
		// head.setZrdeptid(items[0].getZrdeptid());
		// head.setItem_bill_djbh(items[0].getItem_bill_pk());
		// }
		// getBillListPanel().getHeadBillModel().execLoadFormulaByRow(row);
		// getBillListPanel().getBodyBillModel().execLoadFormula();
		// if (head == null) {
		// cur_djzbvo = dataBuffer.getCurrentDJZBVO();
		// if (cur_djzbvo != null)
		// head = (nc.vo.ep.dj.DJZBHeaderVO) (cur_djzbvo.getParentVO());
		// }
		//
		// // �����б�״̬�µı�ͷ��������ʾ
		// row = this.getBillListPanel().getHeadTable().getSelectedRow();
		// String strCurrentPK = getDataBuffer().getCurrentDJZBVOPK();
		// if (null != strCurrentPK) {
		// if (row == -1) {
		// row = ((DjPanel) this.getParent())
		// .getHeadRowInexByPK(strCurrentPK);
		// } else if (!strCurrentPK.equals(billModel.getValueAt(row,
		// "vouchid"))) {
		// for (int i = 0; i < billModel.getRowCount(); i++) {
		// if (strCurrentPK.equals(billModel.getValueAt(row,
		// "vouchid"))) {
		// row = i;
		// break;
		// }
		// }
		// }
		// }
		// try {
		// ((DjPanel) this.getParent()).djlbHeadRowChange(row);
		// } catch (Exception e) {
		// nc.bs.logging.Log.getInstance(this.getClass()).error(
		// e.getMessage(), e);
		// throw new BusinessException(e.getMessage());
		// }
		// try {
		// if (!this.getDataBuffer().isEmpty()) {
		// updateListPanel(this.getDataBuffer().elementArray()
		// .toArray(
		// new DJZBVO[this.getDataBuffer()
		// .elementArray().size()]));
		// }
		// } catch (Exception e) {
		// ExceptionHandler.consume(e);
		// }
		//
		// }
	}

	/**
	 * ��һҳ
	 */
	public void previous() throws Exception {

		((DjPanel) this.getParent()).reserveListSelected();
		Vector vos = null;
		try {
			vos = ((DjPanel) this.getParent()).getVoCache().previous();
		} catch (CacheHeadException ex) {
			int startindex = ((DjPanel) this.getParent()).getVoCache()
					.getFlag();
			if (startindex == 0) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "��������"
																		 */);
			}

			startindex = startindex
					- ((DjPanel) this.getParent()).getVoCache().getMaxRecords();
			Vector bills = queryHeaders(new Integer(startindex), new Integer(
					((DjPanel) this.getParent()).getVoCache().getMaxRecords()));
			if (bills == null) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "��������"
																		 */);
			}
			if (bills.size() == 0) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "��������"
																		 */);
				// this.getBillListPanel1().getHeadBillModel().setBodyDataVO(null);
			} else {
				// parent.currcahcecount=new
				// Integer(parent.currcahcecount.intValue()-1);
				((DjPanel) this.getParent()).getVoCache()
						.initial(
								bills,
								((DjPanel) this.getParent()).getVoCache()
										.getPages() - 1);
				((DjPanel) this.getParent()).getVoCache().setFlag(startindex);
				vos = ((DjPanel) this.getParent()).getVoCache().getData();
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new BusinessException(ex.getMessage());
		}
		DJZBVO[] djvos = new DJZBVO[vos.size()];
		vos.copyInto(djvos);
		updateListPanel(djvos);
		selectListVO();
	}

	/**
	 * �������س��ò�ѯҳ��
	 * 
	 * @return
	 */
	protected abstract UIPanel getNormalLogicalQueryPanel();

	/**
	 * ��������ѯ�����ı�ͷ�б�
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract nc.vo.ep.dj.DjCondVO getCondVO() throws Exception;

	/**
	 * У�鳣��������ѯҳ��Ŀǰֻ��WSZZר�ã�
	 * 
	 * @return
	 */
	protected boolean checkPanel() {
		return true;
	}

	protected String getFbDjrqSql() {
		ICriteriaEditor criteriaEditor = (ICriteriaEditor) this.getQryDlg()
				.getQryCondEditor().getCurrentCriteriaEditor();
		// ���ﷵ��һ��List����Ϊ�û�������SimpleEditor�������˶��zb.djrq����
		List<IFilter> filters = criteriaEditor.getFiltersByFieldCode("zb.djrq");
		if (filters == null || filters.size() == 0) {
			return null;
		}
		// ���ֻ��һ��zb.djrq
		IFilter filter = filters.get(0);
		// ����ֵ�б�ֻ����"����"������ʱ�б���2��ֵ������������ֻ��һ��
		List<IFieldValueElement> values = filter.getFieldValue()
				.getFieldValues();
		IFieldValueElement value = values.get(0);
		String fbDJrqSql = "";
		// ���ظ�ֵ��sql��ʽ
		String beginRq = value.getSqlString();
		if (values.size() > 1) {
			IFieldValueElement endValue = values.get(1);
			// ���ظ�ֵ��sql��ʽ
			String endRq = endValue.getSqlString();
			fbDJrqSql = " (fb.billdate >= '" + beginRq
					+ "' AND fb.billdate <= '" + endRq + "') ";
		} else {
			fbDJrqSql = " (fb.billdate = '" + beginRq + "')";
		}
		return fbDJrqSql;
	}

	/**
	 * ����:��ѯ���� �������ڣ�(2001-5-21 15:11:54) ����:����
	 * 
	 * @param djcondvo
	 *            DjCondVO
	 * @throws BusinessException
	 */
	@SuppressWarnings("restriction")
	public boolean showDjQuery() throws BusinessException {

		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();

		// getArapDjPanel().setM_DjState(0); //���ò�ѯ״̬
		// this.m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000446")/*@res
		// "��ѯ����..."*/);

		// ���ù��õĲ�ѯģ��
		if (getDjSettingParam().getSyscode() != ArapBillWorkPageConst.SysCode_DjQuery) {
			// getQryDlg().hideUnitButton();
		}
		// ��֧�ֶ൥λ��ѯ
		getQryDlg().showModal();
		if (getQryDlg().getResult() != UIDialog.ID_OK) {
			this.getParent().showHintMessage(
					(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPP2006030102-000447"))/*
													 * @res "ȡ����ѯ"
													 */);
			// m_djPanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000447")/*@res
			// "ȡ����ѯ"*/);
			return true;
		}
		if (this.getActionRunntimeV0().getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
			changeTab();
		}
		if (!checkPanel()) {
			return true;
		}
		//
		// //����
		// //on_close_freeLock();
		// //�ǵ��ݲ�ѯ,���б�Ҫ����
		// if (getDjSettingParam().getSyscode() != -1){
		// String[] strPKArray = getDataBuffer().getAllLockedDJZBVO_PK();
		// try {
		// if (strPKArray != null && strPKArray.length > 0)
		// nc.ui.pub.lock.LockBO_Client.freePKArrayByUser(
		// strPKArray,
		// getDjSettingParam().getPk_user(),
		// null);
		//
		// } catch (Exception e) {
		// nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(),e);
		// m_djPanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000014")/*@res
		// "���ݽ�������,���β����Զ�ȡ��"*/);
		// return true;
		// }
		// }
		//
		//
		// //��ս���
		// m_djPanel.getBillListPanel().getHeadBillModel().clearBodyData();
		// m_djPanel.getBillListPanel().getBodyBillModel().clearBodyData();
		// //�������õ��ݼ���
		// //if (m_Djzbvos_Hsb == null)
		// //m_Djzbvos_Hsb = new Hashtable();
		// if (!this.getDataBuffer().isEmpty())
		// this.getDataBuffer().clearBuffer();
		//
		// nc.vo.ep.dj.DJZBHeaderVO[] djzbheadervo = null; //��ͷvo����
		//
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "���ڲ�ѯ����,���Ժ�..."*/);
		// nc.bs.logging.Log.getInstance(this.getClass()).warn("���ڲ�ѯ����,���Ժ�...");
		// ���²�ѯ�����б��ͷ
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "���ڲ�ѯ����,���Ժ�..."*/);
		try {
			((DjPanel) this.getParent()).setCur_Djcondvo(getCondVO());
			refreshBill(true);
		} catch (Exception e) {
			if (e instanceof nc.vo.ep.dj.ShenheException) {
				Logger
						.debug(((nc.vo.ep.dj.ShenheException) e).m_ResMessage.strMessage);
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("2006030102", "UPP2006030102-000449")/*
																			 * @res
																			 * "���ݼ���ʧ�ܣ���ѯʧ��"
																			 */);
			}
			if (e instanceof java.rmi.RemoteException) {
				if (((java.rmi.RemoteException) e).detail instanceof nc.vo.ep.dj.ShenheException) {
					java.rmi.RemoteException remE = (java.rmi.RemoteException) e;
					nc.vo.ep.dj.ShenheException shE = (nc.vo.ep.dj.ShenheException) (remE.detail);
					Logger.debug(shE.m_ResMessage.strMessage);
					throw new BusinessException(nc.ui.ml.NCLangRes
							.getInstance().getStrByID("2006030102",
									"UPP2006030102-000449")/*
															 * @res
															 * "���ݼ���ʧ�ܣ���ѯʧ��"
															 */);
				}
			}
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000450")/*
															 * @res "��ѯ�����б����:"
															 */
					+ e);
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("2006030102", "UPP2006030102-000451")
					+ e.getMessage()/*
									 * @res "��ѯʧ��"
									 */);
		}

		if (this.getActionRunntimeV0().getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
			changeTab();
		}
		return true;
		// //���ϲ�ѯ�����б��ͷ
		//
		// if ((djzbheadervo == null || djzbheadervo.length < 1)) {
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000424")/*@res
		// "û����ص���"*/);
		// //refactoring:useless var
		// //m_Djzbvo = null;
		// //refactoring
		// nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDataBuffer();
		// //getArapDjPanel().setM_Djzbvo(null);
		// dataBuffer.setCurrentDJZBVO(null);
		// return true;
		// }
		//
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000452")/*@res
		// "���¿��ܼ���("*/ + getDjSettingParam().getSyscode() + ")");
		// int lockCount = 0;
		//
		// //����Эͬ���Ʊ�־
		// long t = System.currentTimeMillis();
		// //if (m_Xtflag == null)
		// //m_Xtflag = new Hashtable();
		//
		// //ˢ�µ����б�
		// nc.bs.logging.Log.getInstance(this.getClass()).warn("debugging7");
		// try {
		//
		// nc.vo.ep.dj.DJZBVO temp_djzbvo = null;
		// for (int i = 0; i < djzbheadervo.length; i++) {
		// temp_djzbvo = new nc.vo.ep.dj.DJZBVO();
		// temp_djzbvo.setParentVO(djzbheadervo[i]);
		// temp_djzbvo.setChildrenVO(null);
		// this.getDataBuffer().putDJZBVO(djzbheadervo[i].getVouchid(),
		// temp_djzbvo);
		// }
		// m_djPanel.refDjLb(djzbheadervo);
		// m_djPanel.getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(0,
		// 0);
		// m_djPanel.djlbHeadRowChange(0);
		//
		// } catch (Throwable e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000453")/*@res
		// "ˢ�µ����б����:"*/ + e);
		// }
		// String str =
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000454")/*@res
		// "��ѯ���,��ѯ��{0}�ŵ���"*/;
		// String strLang =
		// NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000454",null,new
		// String[]{String.valueOf(djzbheadervo.length)});
		// m_djPanel.showHintMessage(strLang);
		// // m_djPanel.setButtonsState(0);
		//
		// //���ع�ʽ
		// try {
		// m_djPanel.getBillListPanel().getHeadBillModel().execLoadFormula();
		// } catch (Exception e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037")/*@res
		// "���ع�ʽ����:"*/ + e);
		// }
		// try {
		// //getBillListPanel().getBodyBillModel().execLoadFormula();
		// } catch (Exception e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037")/*@res
		// "���ع�ʽ����:"*/ + e);
		// }
		//
		// //changeTab(0);
		// m_arapDjPanel.setM_DjState(0);
		// return true;

	}

	/**
	 * ����:��ѯ���� �������ڣ� ����:
	 * 
	 * @param
	 * 
	 * @throws BusinessException
	 */
	protected nc.vo.arap.pub.QryCondArrayVO[] getCondArrayVO(
			nc.vo.arap.pub.QryCondArrayVO[] vos) {
		// String strNodeID = getDjSettingParam().getNodeID();
		if (getDjSettingParam().getSyscode() == 2)// �������ĵ���ȷ��
			// if(true)
			return vos;
		nc.vo.arap.pub.QryCondArrayVO[] newArray = new nc.vo.arap.pub.QryCondArrayVO[vos.length + 2];
		System.arraycopy(vos, 0, newArray, 0, vos.length);

		QryCondVO condVO = new QryCondVO();
		QryCondVO[] condVOArray = new QryCondVO[1];
		QryCondArrayVO arrayVO = new QryCondArrayVO();
		condVO.setFldorigin("zb");
		condVO.setQryfld("hzbz");
		condVO.setFldtype(new Integer(STRING));
		condVO.setBoolopr(" = ");
		condVO.setValue(DJZBVOConsts.BADDEBTS_DEFAULT_1);
		condVOArray[0] = condVO;
		arrayVO.setLogicAnd(false);
		arrayVO.setItems(condVOArray);
		newArray[vos.length] = arrayVO;

		condVO = new QryCondVO();
		condVO.setFldorigin("zb");
		condVO.setQryfld("lybz");
		condVO.setFldtype(new Integer(INTEGER));
		condVO.setBoolopr(" <> ");
		condVO.setValue(" 11 ");

		condVOArray = new QryCondVO[1];
		condVOArray[0] = condVO;
		arrayVO = new QryCondArrayVO();
		arrayVO.setLogicAnd(false);
		arrayVO.setItems(condVOArray);
		newArray[vos.length + 1] = arrayVO;

		return newArray;
	}

	// protected NormalPanel getNormalPanelForPub() {
	// m_pNormalPane = (NormalPanel) ((DjPanel)
	// this.getParent()).getAttribute(MapKeyGather.getNormalPanelForPub);
	// if (null == m_pNormalPane) {
	// m_pNormalPane = new nc.ui.arap.billquery.NormalPanel();
	// ((DjPanel)
	// this.getParent()).setAttribute(MapKeyGather.getNormalPanelForPub,
	// m_pNormalPane);
	// }
	// return m_pNormalPane;
	// }

	/**
	 * ����:�õ���ѯ����Ի���,���QueryConditionClient������DjPanel��������д��ڣ�
	 * ��ֱ�����ã��������´����������½��������ü���DjPanel��������С� �������ڣ�(2007-5-16 12:32:57) ���ߣ�����
	 * 
	 * @return nc.ui.arap.pub.QueryDialog
	 */
	protected nc.ui.arap.querytemplate.QueryConditionDLG getQryDlg() {
		m_qryDlg = (nc.ui.arap.querytemplate.QueryConditionDLG) ((DjPanel) this
				.getParent()).getAttribute(MapKeyGather.qryDlg);
		if (null == m_qryDlg) {
			UIPanel normalPanel = getNormalLogicalQueryPanel();
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey());
			tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance()
					.getCorporation().getPrimaryKey());
			tempinfo.setFunNode(getDjSettingParam().getNodeID());
			tempinfo.setUserid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey());
			final String strNodeID = this.getDjSettingParam().getNodeID();// ��ǰ�ڵ��
			// if (normalPanel instanceof IQueryInit)
			// this.initQueryData((IQueryInit) normalPanel);
			m_qryDlg = new nc.ui.arap.querytemplate.QueryConditionDLG(this
					.getParent(), (INormalQueryPanel) normalPanel, tempinfo,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0002782")/*
											 * @res "��ѯ����"
											 */, getActionRunntimeV0()) {
				private static final long serialVersionUID = 1L;

				public String checkCondition() {
					String result = getQryCondEditor().checkCondition();
					if (result == null) {
						ICriteriaEditor criteriaEditor = m_qryDlg
								.getQryCondEditor().getCurrentCriteriaEditor();
						List<IFilter> filters = null;
						if (criteriaEditor instanceof SimpleEditor) {
							SimpleEditor simpleeditor = (SimpleEditor) criteriaEditor;
							List<IFilterEditor> editors = simpleeditor
									.getFilterEditors();
							filters = new ArrayList<IFilter>();
							for (IFilterEditor editor : editors) {
								String fieldCode = editor.getFilter()
										.getFilterMeta().getFieldCode();
								if ("zb.djrq".equals(fieldCode)
										|| "zb.shrq".equals(fieldCode)
										|| "zb.effectdate".equals(fieldCode)
										|| "zb.officialprintdate"
												.equals(fieldCode)) {
									filters.add(editor.getFilter());
								}
							}
						} else {
							QueryTreeEditor queryTreeEditor = (QueryTreeEditor) criteriaEditor;
							filters = queryTreeEditor
									.getFiltersByFieldCode("zb.djrq");
						}
						for (IFilter filter : filters) {
							// maybe here you need to validate whether operator
							// is between
							// eg.
							boolean isBetween = filter.getOperator()
									.getOperatorCode().equals(
											IOperatorConstants.BETWEEN);
							if (isBetween) {
								IFieldValue iFieldValue = filter
										.getFieldValue();
								if (iFieldValue == null) {
									return nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004",
													"UPP2004-v57-000059")/*
																			 * @res
																			 * "���ڱ���ѡ��һ��ʱ�䷶Χ"
																			 */;
								}
								List<IFieldValueElement> values = iFieldValue
										.getFieldValues();
								if (values == null || values.size() != 2
										|| values.get(0) == null
										|| values.get(1) == null) {
									return nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004",
													"UPP2004-v57-000059")/*
																			 * @res
																			 * "���ڱ���ѡ��һ��ʱ�䷶Χ"
																			 */;
								}
							}
						}
					}
					return result;
				}
			};

			// ��������Ĭ��ֵ
			m_qryDlg
					.registerQueryTemplateTotalVOProceeor(new IQueryTemplateTotalVOProcessor() {
						public void processQueryTempletTotalVO(
								QueryTempletTotalVO totalVO) {
							QueryConditionVO[] conds = totalVO
									.getConditionVOs();
							// ������δ��Ĭ��ֵʱ�����ñ��ֵ�Ĭ��ֵ
							String fieldCode = null;
							int condsLength = conds.length;
							QueryPubMethod.updateDefItems(
									((AbstractRuntime) getActionRunntimeV0())
											.getProxy(), conds,
									SearchAction.this.getDjSettingParam()
											.getPk_corp());

							for (int i = 0; i < condsLength; i++) {
								fieldCode = conds[i].getFieldCode();
								// if (fieldCode.equalsIgnoreCase("fb.bzbm")
								// && (null == conds[i].getValue() || 0 ==
								// conds[i]
								// .getValue().length())) {
								// conds[i].setValue(SearchAction.this
								// .getDjSettingParam()
								// .getLocalCurrPK());
								// } else
								if (fieldCode.equalsIgnoreCase("zb.djrq")
										&& (null == conds[i].getValue() || 0 == conds[i]
												.getValue().length())) {
									UFDate date = ClientEnvironment.getInstance().getBusinessDate();
									UFDate dateBefore = date.getDateBefore(30);
									conds[i].setValue(dateBefore + "," + date);
									/*conds[i].setValue(ClientEnvironment
											.getInstance().getBusinessDate()
											.toString()
											+ ","
											+ ClientEnvironment.getInstance()
													.getBusinessDate()
													.toString());*/
									conds[i].setIfMust(UFBoolean.TRUE);
								} else if (fieldCode.equalsIgnoreCase("zb.dr")
										&& (null == conds[i].getValue() || 0 == conds[i]
												.getValue().length())) {
									conds[i].setValue("0");
								} else if (fieldCode
										.equalsIgnoreCase("fb.wldx")
										&& (null == conds[i].getValue() || 0 == conds[i]
												.getValue().length())) {
									conds[i].setValue(initWldxCombo());
								} else if (fieldCode.equals("zb.djlxbm")
										&& ((DjPanel) SearchAction.this
												.getParent()).getIsAloneNode()
												.booleanValue()) {
									// ����ʹ�ý������������ڵ���������֤�ڵ��ѯ�������͵�Ψһ��
									conds[i].setIfImmobility(UFBoolean.TRUE);
									conds[i]
											.setValue(((DjPanel) SearchAction.this
													.getParent()).getBillType());
								}
							}
						}
					});

			// ��Խ���һ���ֶζ�Ӧ���ݿ����ֶε������޸�simpedit�е�Filter
			m_qryDlg.registerFilterEditorFactory(new IFilterEditorFactory() {
				public IFilterEditor createFilterEditor(IFilterMeta meta) {
					if ("zb.djzt".equals(meta.getFieldCode())
							&& ("2006030102".equals(strNodeID)
									|| "2008030102".equals(strNodeID) || "20040302"
									.equals(strNodeID))) {
						return new DjztFilterEditer(m_qryDlg.getQueryContext(),
								meta);
					} else if ("zb.sxbz".equals(meta.getFieldCode())) {
						return new SxbzFilterEditer(m_qryDlg.getQueryContext(),
								meta);
					} else if ("fb.checkflag".equals(meta.getFieldCode())) {
						return new CheckflagFilterEditer(m_qryDlg
								.getQueryContext(), meta);
					} else if ("zb.kjqj".equals(meta.getFieldCode())) {// arap_djzb���޴��ֶΣ�����Ԥ�ᵥʹ��,��Ϊƴ��djkjnd��djkjqj�ı�ʾ�ֶ�
						return new KjqjFilterEditor(m_qryDlg.getQueryContext(),
								meta);
					}
					return null;
				}
			});

			// ���sql����д��
			m_qryDlg
					.registerFieldValueEelementEditorFactory(new IFieldValueElementEditorFactory() {

						public IFieldValueElementEditor createFieldValueElementEditor(
								FilterMeta meta) {

							if ("zb.djlxbm".equals(meta.getFieldCode())) {// ������дSQL����ɶ�ѡ
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								if (((DjPanel) SearchAction.this.getParent())
										.getIsAloneNode().booleanValue()) {
									refPane
											.setRefModel(new DjlxRefModel1(
													((DjPanel) SearchAction.this
															.getParent())
															.getBillType(),
													((DjPanel) SearchAction.this
															.getParent())
															.getIsAloneNode()
															.booleanValue()));
									refPane.getRefModel()
											.setMatchPkWithWherePart(true);
								} else {
									refPane.setRefModel(new DjlxRefModel1());
								}

								refPane.getRefModel().setFun_code(strNodeID);
								try {
									refPane
											.getRefModel()
											.setWherePart(
													getArapDjPanel()
															.getDjlxWhere(true),
													true);
								} catch (BusinessException e) {
									ExceptionHandler.consume(e);
								}// DjlxRefModel1������д��setWherePart�������ڷ����и��ݽڵ������where����
								// refPane.setMultiSelectedEnabled(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.bfyhzh".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.dfyhzh".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("zb.djzt".equals(meta.getFieldCode())
									&& ("2006030102".equals(strNodeID)
											|| "2008030102".equals(strNodeID) || "20040302"
											.equals(strNodeID))) {
								UIComboBox combo = initDjztCombo(meta);
								return new DefaultFieldValueElementEditor(combo);
							} else if ("zb.lrr".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("zb.shr".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.bzbm".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.hbbm".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								if (null != getHbbmWhereString()) {
									refPane.getRefModel().setWherePart(
											getHbbmWhereString());
								}
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.deptid".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("zb.djlxbm".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.pj_jsfs".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.item_bill_pk".equals(meta
									.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								// NCdp202770638
								// ���ݹ����ѯ�������������ֶ�Ӧ�ò��յ�����/��������������������ֻ�ܲ���δ�رյ�
								refPane.setSealedDataButtonShow(false);
								// refPane.getRefUIConfig().setSealedDataButtonShow(false);
								refPane.getRefModel().setSealedDataShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.chmc".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.getRefModel().addWherePart(
										" and pk_corp = '"
												+ getDjSettingParam()
														.getPk_corp() + "'",
										true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.invcode".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.getRefModel().addWherePart(
										" and pk_corp = '"
												+ getDjSettingParam()
														.getPk_corp() + "'",
										true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.szxmid".equals(meta.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							} else if ("fb.accountid".equals(meta
									.getFieldCode())) {
								UIRefPane refPane = new UIRefpaneCreator(
										m_qryDlg.getQueryContext())
										.createUIRefPane(meta);
								refPane.setSealedDataButtonShow(true);
								return new RefElementEditor(refPane, meta
										.getReturnType());
							}

							return null;
						}
					});
			if (!"20080310".equals(strNodeID)) {// ���ý���ڵ�������ʾ��������
				m_qryDlg.setVisibleNormalPanel(true);
			}
			m_qryDlg.addWindowListener(getWindowsQuery());
			if (null != m_qryDlg) {
				((DjPanel) this.getParent()).setAttribute(MapKeyGather.qryDlg,
						m_qryDlg);
			}
		}
		return m_qryDlg;
	}

	// //�ж��Ƿ�ʹ�ý������ͷ�ʽ
	// private boolean isUsedTraderType(String strNodeID) {
	// if
	// (strNodeID.startsWith("2006030121")||strNodeID.startsWith("2006030123")||strNodeID.startsWith("2008030122")||strNodeID.startsWith("2008030124")||strNodeID.startsWith("20040320")||strNodeID.startsWith("20040321")||strNodeID.startsWith("20040321"))
	// {
	// return true;
	// } else{
	// return false;
	// }
	// }
	private UIComboBox initDjztCombo(FilterMeta meta) {
		UIComboBox combo = new UIComboBox();
		combo.addItem(new DefaultConstEnum(new Integer(0), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030102", "UPP2006030102-001077")/*
																				 * @res
																				 * "�ݴ�"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(1), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000040")/*
																				 * @res
																				 * "δȷ��"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(2), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000056")/*
																				 * @res
																				 * "��ȷ��"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(3), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000038")/*
																				 * @res
																				 * "δ���"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(4), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000039")/*
																				 * @res
																				 * "�����"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(5), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000057")/*
																				 * @res
																				 * "�����"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(6), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000058")/*
																				 * @res
																				 * "��ǩ��"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(7), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000059")/*
																				 * @res
																				 * "δ��Ч"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(8), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000060")/*
																				 * @res
																				 * "����Ч"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(9), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000061")/*
																				 * @res
																				 * "δ֧��"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(10), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000062")/*
																				 * @res
																				 * "֧����"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(11), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000063")/*
																				 * @res
																				 * "֧���ɹ�"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(12), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000064")/*
																				 * @res
																				 * "֧��ʧ��"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(13), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverGoingMsg())/*
																 * @res "�տ���"
																 */));
		combo.addItem(new DefaultConstEnum(new Integer(14), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverSuccessMsg())/*
																 * @res "�տ�ɹ�"
																 */));
		combo.addItem(new DefaultConstEnum(new Integer(15), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverFailureMsg())/*
																 * @res "�տ�ʧ��"
																 */));
		return combo;

	}

	protected windowListenerQuery getWindowsQuery() {
		m_windowsQuery = (windowListenerQuery) ((DjPanel) this.getParent())
				.getAttribute(MapKeyGather.windowListenerQuery);
		if (null == m_windowsQuery) {
			m_windowsQuery = new windowListenerQuery();
			((DjPanel) this.getParent()).setAttribute(
					MapKeyGather.windowListenerQuery, m_windowsQuery);
		}
		return m_windowsQuery;
	}

	protected abstract void setQryDlgSize();

	protected String getRefeshDefaultWhereCondition() {
		StringBuilder condition = new StringBuilder();
		condition.append(" (zb.djrq = '").append(
				ClientEnvironment.getInstance().getBusinessDate().toString())
				.append("'");
		condition.append(" and (fb.billdate = '").append(
				ClientEnvironment.getInstance().getBusinessDate().toString())
				.append("'");
		condition.append(" and fb.bzbm = '").append(
				this.getDjSettingParam().getLocalCurrPK()).append("')");
		return condition.toString();
	}

	// protected abstract void addCondition();

	/**
	 * ˢ�°�ť��������<br>
	 */
	public void refersh() throws Exception {
		refreshBill(false);
	}

	/**
	 * ���½�������<br>
	 * 
	 * @param isQuery
	 *            �Ƿ�Ϊ��ѯ����<br>
	 * @return ��ѯ�����¼����<br>
	 */
	public void refreshBill(boolean isQuery) throws Exception {
		if ((nc.ui.arap.querytemplate.QueryConditionDLG) ((DjPanel) this
				.getParent()).getAttribute(MapKeyGather.qryDlg) == null) {
			this.getQryDlg().getQryCondEditor().initUIData();
			((DjPanel) this.getParent()).setCur_Djcondvo(getCondVO());
		}
		int panelProp = ((DjPanel) this.getParent()).getPanelProp();
		int currWorkPage = this.getActionRunntimeV0().getCurrWorkPage();
		if (currWorkPage == ArapBillWorkPageConst.LISTPAGE
				|| (currWorkPage == ArapBillWorkPageConst.CARDPAGE && isQuery)) {
			// �б����Ĳ�ѯ��ˢ�¡���Ƭ����Ĳ�ѯ��ť��ִ�����¶���
			Vector<DJZBVO> vos = new Vector<DJZBVO>();
			if (panelProp == 2) {
				String vouchid = getDataBuffer().getAllDJZBVO_PK()[0];
				vos
						.add(((AbstractRuntime) this.getActionRunntimeV0())
								.getProxy().getIArapBillPrivate().findBillByPK(
										vouchid));
			} else {
				this.getDataBuffer()
						.setListSelectedVOs(new ArrayList<DJZBVO>());
				// //////////////////////////////////////////////////
				// NCdp202859658����������˫����������Ϣ���ڵ��ݹ���Ի����п�Ƭ�����а���ѯ������ѯnull(�������ڸ�����)
				// /////////////////////////////////////////////////
				Cache voCache = ((DjPanel) this.getParent()).getVoCache();
				if (null == voCache) {
					voCache = new Cache(null);
					((DjPanel) this.getParent()).setVoCache(voCache);
				}
				Vector bills = this.queryHeaders(new Integer(0), new Integer(
						null == voCache ? 0 : voCache.getMaxRecords()));
				voCache.initial(bills, 0);
				voCache.setFlag(0);
				vos = voCache.getData();
			}
			DJZBVO[] djvos = new DJZBVO[vos.size()];
			vos.copyInto(djvos);
			updateListPanel(djvos);
			selectListVO();
		} else {
			String vouchid = getDataBuffer().getCurrentDJZBVOPK();
			if (vouchid == null) {
				return;
			}
			String djdl = getDataBuffer().getCurrentDjdl();
			DJZBVO djzbvo = null;
			if ("ss".equals(djdl)) {
				djzbvo = Proxy.getIArapItemPrivate()
						.findSsByPrimaryKey(vouchid);
			} else {
				djzbvo = ((AbstractRuntime) this.getActionRunntimeV0())
						.getProxy().getIArapBillPrivate().findBillByPK(vouchid);
			}
			if (null != djzbvo) {

				if ("hj".equals(djdl)) {
					DJZBItemVO[] items = (DJZBItemVO[]) djzbvo.getChildrenVO();
					for (DJZBItemVO itemVO : items) {
						changeHJSkFkyhzh(itemVO);
					}
				}
				AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(djzbvo);
				getArapDjPanel().setDj(djzbvo);
			} else {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("2004", "UPP2004-v57-000060")/*
																	 * @res
																	 * "�����Ѿ�������,�����²�ѯ!"
																	 */);
			}

		}
	}

	private void changeHJSkFkyhzh(DJZBItemVO itemvo) {
		Integer fx = itemvo.getFx();
		if (fx == DJZBVOConsts.FX_DF) {
			itemvo.setSkyhzh(itemvo.getBfyhzh());
			itemvo.setFkyhzh(itemvo.getDfyhzh());
		} else if ((fx == DJZBVOConsts.FX_JF)) {
			itemvo.setSkyhzh(itemvo.getDfyhzh());
			itemvo.setFkyhzh(itemvo.getBfyhzh());
		}
	}

	/**
	 * ��һҳ
	 */
	public void next() throws Exception {
		((DjPanel) this.getParent()).reserveListSelected();

		Vector vos = null;
		try {
			vos = ((DjPanel) this.getParent()).getVoCache().next();
		} catch (EndResultSetException ex) {
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("20080305", "UPP20080305-000029")/*
																	 * @res
																	 * "���޿������ݣ��밴ˢ�°�ť����ͬ������"
																	 */);

		} catch (OverFlowException ex) {
			int startindex = ((DjPanel) this.getParent()).getVoCache()
					.getFlag();
			startindex = startindex
					+ ((DjPanel) this.getParent()).getVoCache().getMaxRecords();
			Vector bills = this.queryHeaders(new Integer(startindex),
					new Integer(((DjPanel) this.getParent()).getVoCache()
							.getMaxRecords()));
			if (bills == null) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "��������"
																		 */);
			}
			if (bills.size() == 0) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "��������"
																		 */);
			} else {
				((DjPanel) this.getParent()).getVoCache().initial(bills, 0);
				((DjPanel) this.getParent()).getVoCache().setFlag(startindex);
				vos = ((DjPanel) this.getParent()).getVoCache().getData();
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new BusinessException(ex.getMessage());
		}
		DJZBVO[] djs = new DJZBVO[vos.size()];
		vos.copyInto(djs);

		updateListPanel(djs);
		selectListVO();

	}

	private void selectListVO() {
		for (int row = 0; row < getBillListPanel().getHeadBillModel()
				.getRowCount(); row++) {
			String vouchid = (String) getBillListPanel().getHeadBillModel()
					.getValueAt(row, "vouchid");
			DJZBVO djzbvo = this.getDataBuffer().getDJZBVO(vouchid);
			if (this.getDataBuffer().getListSelectedVOs().contains(djzbvo)) {
				getBillListPanel().getHeadBillModel().setValueAt(
						ArapConstant.UFBOOLEAN_TRUE, row, "isselected");
			}
		}
	}

	protected abstract java.util.Vector queryHeaders(Integer initPos,
			Integer initCount) throws Exception;

	public void updateListPanel(DJZBVO[] zbvos) throws Exception {
		ArapDjPanel arapDjPanel = this.getArapDjPanel();
		this.getBillListPanel().getHeadBillModel().clearBodyData();
		this.getBillListPanel().getBodyBillModel().clearBodyData();
		// ArrayList<DJZBVO> arrayList = new ArrayList<DJZBVO>();
		// arrayList.addAll(Arrays.asList(zbvos));
		// this.getDataBuffer().setListSelectedVOs(arrayList);
		// �������õ��ݼ���
		if (!this.getDataBuffer().isEmpty())
			this.getDataBuffer().clearBuffer();
		if ((zbvos == null || zbvos.length < 1)) {
			// nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDataBuffer();
			// getArapDjPanel().setM_Djzbvo(null);
			// dataBuffer.setCurrentDJZBVO(null);
			// throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("2006030102", "UPP2006030102-000424")/*
			// * @res
			// * "û����ص���"
			// */);
			getDataBuffer().setCurrentDJZBVO(null);
			return;
		}
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000452")
				+ getDjSettingParam().getSyscode() + ")");
		// ˢ�µ����б�
		try {
			nc.vo.ep.dj.DJZBHeaderVO[] djzbheadervos = new DJZBHeaderVO[zbvos.length];
			for (int i = 0; i < djzbheadervos.length; i++) {
				djzbheadervos[i] = (DJZBHeaderVO) zbvos[i].getParentVO();
				this.getDataBuffer().putDJZBVO(djzbheadervos[i].getVouchid(),
						zbvos[i]);
			}
			((DjPanel) this.getParent()).refDjLb(djzbheadervos);
		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000453")/*
															 * @res "ˢ�µ����б����:"
															 */
					+ e);
		}

		// ���ع�ʽ
		try {
			this.getBillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000037")/*
															 * @res "���ع�ʽ����:"
															 */
					+ e);
		}
		arapDjPanel.setM_DjState(0);
		int rownum = this.getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < rownum; i++) {
			Object demcObj = this.getBillListPanel().getHeadBillModel()
					.getValueAt(i, "dwmc");
			Object dwbmObj = this.getBillListPanel().getHeadBillModel()
					.getValueAt(i, "dwbm");
			String dwmc = demcObj == null ? null : demcObj.toString();
			String dwbm = dwbmObj == null ? null : dwbmObj.toString();
			BillItem dwbmBillItem = this.getBillListPanel().getHeadItem("dwbm");
			if (dwbmBillItem != null && dwmc != null && dwbm != null) {
				this.getBillListPanel().getHeadBillModel().setValueAt(
						new DefaultConstEnum(dwbm, dwmc), i, "dwbm");
			}
		}
	}

	private String initWldxCombo() {
		ARAPDjSettingParam djSettingParam = this.getDjSettingParam();
		if (djSettingParam.getSyscode() == ResMessage.$SysCode_AR) {
			return String.valueOf(DJZBVOConsts.WLDX_KH.intValue());
		} else if (djSettingParam.getSyscode() == ResMessage.$SysCode_AP) {
			return String.valueOf(DJZBVOConsts.WLDX_GYS.intValue());
		}
		// else if(djSettingParam.getSyscode() == ResMessage.$SysCode_EP){
		// }
		return String.valueOf(DJZBVOConsts.WLDX_KH.intValue());
	}

	class windowListenerQuery implements java.awt.event.WindowListener {
		public void windowOpened(java.awt.event.WindowEvent e) {
			nc.bs.logging.Log.getInstance(this.getClass()).debug(
					"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@test1111111111");
			FocusUtils.focusNextComponent(getNormalLogicalQueryPanel());
			// getNormalPanelForPub().getrefDjlx().requestFocus();
			// m_pNormalPane.getrefDjlx().requestDefaultFocus();
			// if (null != m_pNormalPane.getrefDjlx().getFocusTraversalPolicy())
			// m_pNormalPane.getrefDjlx().getFocusTraversalPolicy().getDefaultComponent(m_pNormalPane.getrefDjlx())
			// .requestFocus();
			nc.bs.logging.Log.getInstance(this.getClass()).debug(
					"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@test222222222222");
		}

		public void windowClosed(java.awt.event.WindowEvent e) {
		}

		public void windowDeiconified(java.awt.event.WindowEvent e) {
		}

		public void windowActivated(java.awt.event.WindowEvent e) {
		}

		public void windowDeactivated(java.awt.event.WindowEvent e) {
		}

		public void windowIconified(java.awt.event.WindowEvent e) {
		}

		public void windowClosing(java.awt.event.WindowEvent e) {
		}

	};

	protected nc.vo.ep.dj.DjCondVO getDefaultCondVO() throws Exception {
		// �����ڱ�־
		if (getDjSettingParam().getIsQc())
			((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
					.setQcBz("Y");
		else
			((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
					.setQcBz("N");
		String[] dwbms = null;
		dwbms = new String[1];
		dwbms[0] = getDjSettingParam().getPk_corp();
		nc.vo.arap.pub.QryCondArrayVO[] vos = null;
		vos = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
				.getValueCondVO();

		// "���ڲ�ѯ����,���Ժ�..."*/);
		nc.bs.logging.Log.getInstance(this.getClass()).warn("���ڲ�ѯ����,���Ժ�..."); /*-=nottranslate=-*/
		// ���²�ѯ�����б��ͷ

		// �����Զ��������ͳ���������ѯ����
		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		// TODO Ǩ��
		cur_Djcondvo.m_NorCondVos = this.getCondArrayVO(vos);
		// cur_Djcondvo.m_DefCondVos = getQryDlg().getConditionVO();
		if (UFBoolean.TRUE
				.equals(((DjPanel) this.getParent()).getIsAloneNode())) {
			cur_Djcondvo.defWhereSQL = "zb.djrq = '"
					+ getDjSettingParam().getLoginDate() + "' AND fb.bzbm = '"
					+ Currency.getLocalCurrPK(getDjSettingParam().getPk_corp())
					+ "' and zb.djlxbm = '"
					+ getDataBuffer().getCurrentDjlxbm() + "'";
		} else {
			cur_Djcondvo.defWhereSQL = "zb.djrq = '"
					+ getDjSettingParam().getLoginDate() + "' AND fb.bzbm = '"
					+ Currency.getLocalCurrPK(getDjSettingParam().getPk_corp())
					+ "'";
		}
		cur_Djcondvo.isCHz = false;
		cur_Djcondvo.operator = getDjSettingParam().getPk_user();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();
		cur_Djcondvo.pk_corp = dwbms;
		cur_Djcondvo.isLinkPz = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
				.getchkBoxHasPZNum().isSelected();
		// set the "ƾ֤�Ƿ��Ѿ�����" info into the vo
		cur_Djcondvo.VoucherFlag = ((NormalLogicalQueryPanel) getNormalLogicalQueryPanel())
				.getVoucherStatus();

		cur_Djcondvo.isUsedGL = getDjSettingParam().getGlIsUsed()
				.booleanValue();
		// this.m_djPanel.setCur_Djcondvo(cur_Djcondvo);
		// return DJZBBO_Client.queryDjLb_djcond(cur_Djcondvo);
		return cur_Djcondvo;
	}

	private String getHbbmWhereString() {
		String wherePart = null;
		String strNodeID = this.getDjSettingParam().getNodeID();// ��ǰ�ڵ��
		if (strNodeID.equals("2006030101") || strNodeID.equals("2006030102")
				|| strNodeID.equals("2006030103")
				|| strNodeID.equals("2006030121")
				|| strNodeID.equals("2006030123")) {
			wherePart = "bd_cumandoc.pk_corp='"
					+ getDjSettingParam().getPk_corp()
					+ "'AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2' )";
		} else if (strNodeID.equals("2008030101")
				|| strNodeID.equals("2008030102")
				|| strNodeID.equals("2008030103")
				|| strNodeID.equals("2008030122")
				|| strNodeID.equals("2008030124")) {
			wherePart = "bd_cumandoc.pk_corp='"
					+ getDjSettingParam().getPk_corp()
					+ "'AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2' )";
		}
		return wherePart;
	}
}
