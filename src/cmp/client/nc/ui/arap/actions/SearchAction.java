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
	 * 功能:改变页签(单据和列表之间) 创建日期：(2001-5-21 15:07:39) 作者:阿飞 参数:tabIndex 页签码,isChange
	 * false不切换页签
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
		// "UPT2006030102-000021")/* @res "单据" */);
		// this.getActionRunntimeV0().setCurrentpage(ArapBillWorkPageConst.CARDPAGE);
		// if (djp.getJPanel1().getComponentCount() < 1) { // 如果是初始化,第一次切换到单据
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
		// // 联查询单据,刷新单据
		// if (rowcount_h > 0) {
		// try {
		// // 设置单据
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
		// "UPT2006030102-000049")/* @res "列表" */);
		// this.getActionRunntimeV0().setCurrentpage(ArapBillWorkPageConst.LISTPAGE);
		//
		// // 卡片切换到列表时将表体项带入表头
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
		// // 更新列表状态下的表头、表体显示
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
	 * 上一页
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
																		 * "已无数据"
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
																		 * "已无数据"
																		 */);
			}
			if (bills.size() == 0) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "已无数据"
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
	 * 方法返回常用查询页面
	 * 
	 * @return
	 */
	protected abstract UIPanel getNormalLogicalQueryPanel();

	/**
	 * 获得满足查询条件的表头列表
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract nc.vo.ep.dj.DjCondVO getCondVO() throws Exception;

	/**
	 * 校验常用条件查询页（目前只是WSZZ专用）
	 * 
	 * @return
	 */
	protected boolean checkPanel() {
		return true;
	}

	protected String getFbDjrqSql() {
		ICriteriaEditor criteriaEditor = (ICriteriaEditor) this.getQryDlg()
				.getQryCondEditor().getCurrentCriteriaEditor();
		// 这里返回一个List是因为用户可能在SimpleEditor上增加了多个zb.djrq条件
		List<IFilter> filters = criteriaEditor.getFiltersByFieldCode("zb.djrq");
		if (filters == null || filters.size() == 0) {
			return null;
		}
		// 如果只有一个zb.djrq
		IFilter filter = filters.get(0);
		// 返回值列表，只有在"介于"操作符时列表有2个值，其他操作符只有一个
		List<IFieldValueElement> values = filter.getFieldValue()
				.getFieldValues();
		IFieldValueElement value = values.get(0);
		String fbDJrqSql = "";
		// 返回该值的sql形式
		String beginRq = value.getSqlString();
		if (values.size() > 1) {
			IFieldValueElement endValue = values.get(1);
			// 返回该值的sql形式
			String endRq = endValue.getSqlString();
			fbDJrqSql = " (fb.billdate >= '" + beginRq
					+ "' AND fb.billdate <= '" + endRq + "') ";
		} else {
			fbDJrqSql = " (fb.billdate = '" + beginRq + "')";
		}
		return fbDJrqSql;
	}

	/**
	 * 功能:查询单据 创建日期：(2001-5-21 15:11:54) 作者:阿飞
	 * 
	 * @param djcondvo
	 *            DjCondVO
	 * @throws BusinessException
	 */
	@SuppressWarnings("restriction")
	public boolean showDjQuery() throws BusinessException {

		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		cur_Djcondvo.syscode = getDjSettingParam().getSyscode();

		// getArapDjPanel().setM_DjState(0); //设置查询状态
		// this.m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000446")/*@res
		// "查询单据..."*/);

		// 调用公用的查询模板
		if (getDjSettingParam().getSyscode() != ArapBillWorkPageConst.SysCode_DjQuery) {
			// getQryDlg().hideUnitButton();
		}
		// 不支持多单位查询
		getQryDlg().showModal();
		if (getQryDlg().getResult() != UIDialog.ID_OK) {
			this.getParent().showHintMessage(
					(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPP2006030102-000447"))/*
													 * @res "取消查询"
													 */);
			// m_djPanel.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000447")/*@res
			// "取消查询"*/);
			return true;
		}
		if (this.getActionRunntimeV0().getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
			changeTab();
		}
		if (!checkPanel()) {
			return true;
		}
		//
		// //解锁
		// //on_close_freeLock();
		// //非单据查询,才有必要解锁
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
		// "单据解锁出错,本次操作自动取消"*/);
		// return true;
		// }
		// }
		//
		//
		// //清空界面
		// m_djPanel.getBillListPanel().getHeadBillModel().clearBodyData();
		// m_djPanel.getBillListPanel().getBodyBillModel().clearBodyData();
		// //重新设置单据集合
		// //if (m_Djzbvos_Hsb == null)
		// //m_Djzbvos_Hsb = new Hashtable();
		// if (!this.getDataBuffer().isEmpty())
		// this.getDataBuffer().clearBuffer();
		//
		// nc.vo.ep.dj.DJZBHeaderVO[] djzbheadervo = null; //表头vo集合
		//
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "正在查询单据,请稍候..."*/);
		// nc.bs.logging.Log.getInstance(this.getClass()).warn("正在查询单据,请稍候...");
		// 以下查询单据列表表头
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000448")/*@res
		// "正在查询单据,请稍候..."*/);
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
																			 * "单据加锁失败，查询失败"
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
															 * "单据加锁失败，查询失败"
															 */);
				}
			}
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000450")/*
															 * @res "查询单据列表出错:"
															 */
					+ e);
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("2006030102", "UPP2006030102-000451")
					+ e.getMessage()/*
									 * @res "查询失败"
									 */);
		}

		if (this.getActionRunntimeV0().getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
			changeTab();
		}
		return true;
		// //以上查询单据列表表头
		//
		// if ((djzbheadervo == null || djzbheadervo.length < 1)) {
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000424")/*@res
		// "没有相关单据"*/);
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
		// "以下可能加锁("*/ + getDjSettingParam().getSyscode() + ")");
		// int lockCount = 0;
		//
		// //设置协同控制标志
		// long t = System.currentTimeMillis();
		// //if (m_Xtflag == null)
		// //m_Xtflag = new Hashtable();
		//
		// //刷新单据列表
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
		// "刷新单据列表出错:"*/ + e);
		// }
		// String str =
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000454")/*@res
		// "查询完毕,查询到{0}张单据"*/;
		// String strLang =
		// NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000454",null,new
		// String[]{String.valueOf(djzbheadervo.length)});
		// m_djPanel.showHintMessage(strLang);
		// // m_djPanel.setButtonsState(0);
		//
		// //加载公式
		// try {
		// m_djPanel.getBillListPanel().getHeadBillModel().execLoadFormula();
		// } catch (Exception e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037")/*@res
		// "加载公式出错:"*/ + e);
		// }
		// try {
		// //getBillListPanel().getBodyBillModel().execLoadFormula();
		// } catch (Exception e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037")/*@res
		// "加载公式出错:"*/ + e);
		// }
		//
		// //changeTab(0);
		// m_arapDjPanel.setM_DjState(0);
		// return true;

	}

	/**
	 * 功能:查询单据 创建日期： 作者:
	 * 
	 * @param
	 * 
	 * @throws BusinessException
	 */
	protected nc.vo.arap.pub.QryCondArrayVO[] getCondArrayVO(
			nc.vo.arap.pub.QryCondArrayVO[] vos) {
		// String strNodeID = getDjSettingParam().getNodeID();
		if (getDjSettingParam().getSyscode() == 2)// 报帐中心单据确认
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
	 * 功能:得到查询情况对话框,如果QueryConditionClient对象在DjPanel子类对象中存在，
	 * 则直接重用，否则重新创建，并将新建对象引用加入DjPanel子类对象中。 创建日期：(2007-5-16 12:32:57) 作者：马骥
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
			final String strNodeID = this.getDjSettingParam().getNodeID();// 当前节点号
			// if (normalPanel instanceof IQueryInit)
			// this.initQueryData((IQueryInit) normalPanel);
			m_qryDlg = new nc.ui.arap.querytemplate.QueryConditionDLG(this
					.getParent(), (INormalQueryPanel) normalPanel, tempinfo,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0002782")/*
											 * @res "查询条件"
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
																			 * "日期必须选定一个时间范围"
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
																			 * "日期必须选定一个时间范围"
																			 */;
								}
							}
						}
					}
					return result;
				}
			};

			// 用于设置默认值
			m_qryDlg
					.registerQueryTemplateTotalVOProceeor(new IQueryTemplateTotalVOProcessor() {
						public void processQueryTempletTotalVO(
								QueryTempletTotalVO totalVO) {
							QueryConditionVO[] conds = totalVO
									.getConditionVOs();
							// 当币种未设默认值时，设置币种的默认值
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
									// 用于使用交易类型新增节点的情况，保证节点查询单据类型的唯一性
									conds[i].setIfImmobility(UFBoolean.TRUE);
									conds[i]
											.setValue(((DjPanel) SearchAction.this
													.getParent()).getBillType());
								}
							}
						}
					});

			// 针对界面一个字段对应数据库多个字段的需求，修改simpedit中的Filter
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
					} else if ("zb.kjqj".equals(meta.getFieldCode())) {// arap_djzb并无此字段，仅在预提单使用,作为拼接djkjnd、djkjqj的标示字段
						return new KjqjFilterEditor(m_qryDlg.getQueryContext(),
								meta);
					}
					return null;
				}
			});

			// 添加sql条件写法
			m_qryDlg
					.registerFieldValueEelementEditorFactory(new IFieldValueElementEditorFactory() {

						public IFieldValueElementEditor createFieldValueElementEditor(
								FilterMeta meta) {

							if ("zb.djlxbm".equals(meta.getFieldCode())) {// 需求：重写SQL、设可多选
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
								}// DjlxRefModel1类中重写了setWherePart方法，在方法中根据节点号重置where条件
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
								// 单据管理查询，事项审批单字段应该参照到所有/事项审批单，但是现在只能参照未关闭的
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
			if (!"20080310".equals(strNodeID)) {// 费用结算节点无需显示常用条件
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

	// //判断是否使用交易类型方式
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
																				 * "暂存"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(1), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000040")/*
																				 * @res
																				 * "未确认"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(2), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000056")/*
																				 * @res
																				 * "已确认"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(3), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000038")/*
																				 * @res
																				 * "未审核"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(4), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000039")/*
																				 * @res
																				 * "已审核"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(5), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000057")/*
																				 * @res
																				 * "审核中"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(6), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000058")/*
																				 * @res
																				 * "已签字"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(7), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000059")/*
																				 * @res
																				 * "未生效"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(8), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000060")/*
																				 * @res
																				 * "已生效"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(9), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000061")/*
																				 * @res
																				 * "未支付"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(10), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000062")/*
																				 * @res
																				 * "支付中"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(11), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000063")/*
																				 * @res
																				 * "支付成功"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(12), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101", "UPP2006030101-000064")/*
																				 * @res
																				 * "支付失败"
																				 */));
		combo.addItem(new DefaultConstEnum(new Integer(13), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverGoingMsg())/*
																 * @res "收款中"
																 */));
		combo.addItem(new DefaultConstEnum(new Integer(14), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverSuccessMsg())/*
																 * @res "收款成功"
																 */));
		combo.addItem(new DefaultConstEnum(new Integer(15), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("2006030101",
						StrResPorxy.getFTSReceiverFailureMsg())/*
																 * @res "收款失败"
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
	 * 刷新按钮动作处理<br>
	 */
	public void refersh() throws Exception {
		refreshBill(false);
	}

	/**
	 * 更新界面数据<br>
	 * 
	 * @param isQuery
	 *            是否为查询动作<br>
	 * @return 查询结果记录条数<br>
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
			// 列表界面的查询和刷新、卡片界面的查询按钮，执行以下动作
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
				// NCdp202859658待办事务中双击审批流消息，在单据管理对话框中卡片界面中按查询，报查询null(开发已在跟踪中)
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
																	 * "数据已经被更新,请重新查询!"
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
	 * 下一页
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
																	 * "已无可用数据，请按刷新按钮重新同步数据"
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
																		 * "已无数据"
																		 */);
			}
			if (bills.size() == 0) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("20080305", "UPP20080305-000030")/*
																		 * @res
																		 * "已无数据"
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
		// 重新设置单据集合
		if (!this.getDataBuffer().isEmpty())
			this.getDataBuffer().clearBuffer();
		if ((zbvos == null || zbvos.length < 1)) {
			// nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDataBuffer();
			// getArapDjPanel().setM_Djzbvo(null);
			// dataBuffer.setCurrentDJZBVO(null);
			// throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("2006030102", "UPP2006030102-000424")/*
			// * @res
			// * "没有相关单据"
			// */);
			getDataBuffer().setCurrentDJZBVO(null);
			return;
		}
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000452")
				+ getDjSettingParam().getSyscode() + ")");
		// 刷新单据列表
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
															 * @res "刷新单据列表出错:"
															 */
					+ e);
		}

		// 加载公式
		try {
			this.getBillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000037")/*
															 * @res "加载公式出错:"
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
		// 设置期标志
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

		// "正在查询单据,请稍候..."*/);
		nc.bs.logging.Log.getInstance(this.getClass()).warn("正在查询单据,请稍候..."); /*-=nottranslate=-*/
		// 以下查询单据列表表头

		// 根据自定义条件和常用条件查询单据
		nc.vo.ep.dj.DjCondVO cur_Djcondvo = new nc.vo.ep.dj.DjCondVO();
		// TODO 迁移
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
		// set the "凭证是否已经记帐" info into the vo
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
		String strNodeID = this.getDjSettingParam().getNodeID();// 当前节点号
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
