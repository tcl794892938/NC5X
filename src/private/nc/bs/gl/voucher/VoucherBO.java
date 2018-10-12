package nc.bs.gl.voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.bd.b18.VouchertypeBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.FrameworkRuntimeException;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.gl.cashflowcase.CashflowcaseDMO;
import nc.bs.gl.corpcontrast.CorpcontrastsubDMO;
import nc.bs.gl.pubinterface.IVoucherAbandon;
import nc.bs.gl.voucherbackup.DetailBackupExtendDMO;
import nc.bs.gl.voucherbackup.VoucherBackupDMO;
import nc.bs.gl.vouchertools.DapsystemDMO;
import nc.bs.glcom.para.GlParaDMO;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.SystemException;
import nc.itf.fi.pub.Accsubj;
import nc.itf.fi.pub.Balatype;
import nc.itf.gl.pub.GLKeyLock;
import nc.itf.gl.pubreconcile.IPubReconcile;
import nc.itf.gl.reconcile.IReconcileExtend;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.gl.datacache.GLParaDataCacheUseUap;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.bd.b18.VouchertypeVO;
import nc.vo.bd.period2.AccperiodmonthUsedException;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.bd.service.IBDOperate;
import nc.vo.gl.cashflowcase.CashflowcaseVO;
import nc.vo.gl.exception.VoucherNoDuplicateException;
import nc.vo.gl.pubinterface.VoucherOperateInterfaceVO;
import nc.vo.gl.pubinterface.VoucherSaveInterfaceVO;
import nc.vo.gl.pubreconcile.PubReconcileInfoVO;
import nc.vo.gl.pubvoucher.DetailVO;
import nc.vo.gl.pubvoucher.GLParameterVO;
import nc.vo.gl.pubvoucher.OperationResultVO;
import nc.vo.gl.pubvoucher.UserKey;
import nc.vo.gl.pubvoucher.UserVO;
import nc.vo.gl.pubvoucher.VoucherKey;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.gl.sysparam.SystemParamConfig;
import nc.vo.gl.verifybegin.GLVouchersToCAVBalance;
import nc.vo.gl.verifybegin.VerifydetailVO;
import nc.vo.gl.voucher.DealclassVO;
import nc.vo.gl.voucher.DtlfreevalueVO;
import nc.vo.gl.voucher.SmallCorpVO;
import nc.vo.gl.voucher.SubjfreevalueVO;
import nc.vo.gl.voucher.VchfreevalueVO;
import nc.vo.gl.voucher.VoucherCheckConfigVO;
import nc.vo.gl.voucher.VoucherCheckMessage;
import nc.vo.gl.voucher.VoucherUserVO;
import nc.vo.gl.voucherlist.VoucherIndexVO;
import nc.vo.gl.vouchertools.DapsystemVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.glcom.wizard.VoWizard;
import nc.vo.glpub.GlBusinessException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.para.ParaMacro;

import org.apache.commons.lang.StringUtils;

/**
 * Voucher的BO类
 *
 * 创建日期：(2001-9-19)
 *
 * @author：
 */
public class VoucherBO implements IBDOperate {
	/**
	 * VoucherBO 构造子注解。
	 */
	public VoucherBO() {
		super();
	}

	private static final Log log = Log.getInstance(VoucherBO.class);

	private OperationResultVO[] abandonByPk(String strVoucherPK, String strOperator, Boolean op) throws BusinessException, Exception {
		if (strVoucherPK == null) {
			return null;
		}
		VoucherVO vo = null;
		if (op.booleanValue()) {
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			vo = dmo.queryByVoucherPk(strVoucherPK);
		} else {
			vo = queryByPk(strVoucherPK);
		}
		if (vo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000538")/*
																																 * @res
																																 * "凭证已被他人删除，请刷新数据。"
																																 */);
		else {
			checkCanAbandon(vo, strOperator, op);
		}
		long time = System.currentTimeMillis();
		OperationResultVO[] result = null;
		boolean bLockSuccess = false;
		GLKeyLock lock = null;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			lock = new GLKeyLock();

			;
			for (int i = 0; i < 5; i++) { // lock.freeKey(strVoucherPK,
				// strOperator, "gl_voucher");
				bLockSuccess = lock.lockKey(strVoucherPK, strOperator, "gl_voucher");
				if (bLockSuccess)
					break;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
			if (!bLockSuccess)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																	 * @res
																																	 * "有其他用户在操作，请稍候再试。"
																																	 */);

			// Vector vecPKs = new Vector();
			// for (int i = 0; i < strVoucherPK.length; i++) {
			// vecPKs.addElement(strVoucherPK[i]);
			// }

			nc.bs.gl.voucher.DealclassDMO dealclassdmo = new nc.bs.gl.voucher.DealclassDMO();
			nc.vo.gl.voucher.DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("abandon");
			java.util.Vector vecabandonclass = new java.util.Vector();
			if (dealclassvos != null && dealclassvos.length != 0)
				for (int m = 0; m < dealclassvos.length; m++) {
					if (dealclassvos[m].getModules() != null) {
						try {
							IVoucherAbandon m_abandonclassall = (IVoucherAbandon) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m].getClassname());
							vecabandonclass.addElement(m_abandonclassall);
						} catch (FrameworkRuntimeException e) {
							// TODO: handle exception
							log.error(e);
						}
					}
					/*
					 * try { Class m_classabandon =
					 * java.lang.Class.forName(dealclassvos[m].getClassname());
					 * Object m_objectabandon = m_classabandon.newInstance();
					 * IVoucherAbandon m_abandonclassall = (IVoucherAbandon)
					 * m_objectabandon;
					 * vecabandonclass.addElement(m_abandonclassall); } catch
					 * (ClassNotFoundException e) { } catch
					 * (NoClassDefFoundError e) { continue; }
					 */
				}
			for (int m = 0; m < vecabandonclass.size(); m++) {
				VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
				opvo.pk_vouchers = new String[] { strVoucherPK };
				opvo.pk_user = strOperator;
				opvo.operatedirection = op;
				OperationResultVO[] t_result = ((IVoucherAbandon) vecabandonclass.elementAt(m)).beforeAbandon(opvo);
				if (t_result != null) {
					for (int i = 0; i < t_result.length; i++) {
						if (t_result[i] != null && t_result[i].m_intSuccess == 2) {
							throw new BusinessException(t_result[i].m_strDescription);
						}
					}
				}
				result = OperationResultVO.appendResultVO(result, t_result);
			}

			if (op.booleanValue()) {
				int updatecount = new VoucherExtendDMO().updateAbandon(new String[] { strVoucherPK }, strOperator, op);
				if (updatecount != 1) {
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000540")/*
																																		 * @res
																																		 * "实际变更的数据与选中的有效数据不符，可能是由于其他用户的操作改变了凭证状态。"
																																		 */);
					// result = new OperationResultVO();
					// result.m_intSuccess = 1;
					// result.m_userIdentical = "操作完成，有部分数据已发生变更，请刷新您的数据。";
				}
			} else {
				int updatecount = new VoucherExtendDMO().updateAbandon(new String[] { strVoucherPK }, strOperator, op);
				if (updatecount != 1) {
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000540")/*
																																		 * @res
																																		 * "实际变更的数据与选中的有效数据不符，可能是由于其他用户的操作改变了凭证状态。"
																																		 */);
					// result = new OperationResultVO();
					// result.m_intSuccess = 1;
					// result.m_userIdentical = "操作完成，有部分数据已发生变更，请刷新您的数据。";
				}
			}

			for (int m = vecabandonclass.size(); m > 0; m--) {
				VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
				opvo.pk_vouchers = new String[] { strVoucherPK };
				opvo.pk_user = strOperator;
				opvo.operatedirection = op;
				OperationResultVO[] t_result = ((IVoucherAbandon) vecabandonclass.elementAt(m - 1)).afterAbandon(opvo);
				result = OperationResultVO.appendResultVO(result, t_result);
			}
		} catch (BusinessException e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			if (bLockSuccess)
				lock.freeKey(strVoucherPK, strOperator, "gl_voucher");
			// try
			// {
			// //lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}
		return result;
	}

	public OperationResultVO[] abandonByPks(String[] strVoucherPKs, String strOperator, Boolean op) throws BusinessException {
		if (strVoucherPKs == null || strVoucherPKs.length == 0)
			return null;
		try {
			OperationResultVO[] r = null;
			for (int i = 0; i < strVoucherPKs.length; i++) {
				r = OperationResultVO.appendResultVO(r, abandonByPk(strVoucherPKs[i], strOperator, op));
			}
			if (r != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < r.length; i++) {
					if (r[i] != null)
						switch (r[i].m_intSuccess) {
						case 0:
							break;
						case 1:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																	 * @res
																																	 * "警告:"
																																	 */+ r[i].m_strDescription + "\n");
							break;
						case 2:
							errflag = true;
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																	 * @res
																																	 * "错误:"
																																	 */+ r[i].m_strDescription + "\n");
							break;
						default:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																	 * @res
																																	 * "信息:"
																																	 */+ r[i].m_strDescription + "\n");
						}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			return r;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	private DetailVO[] catAss(DetailVO[] details) throws BusinessException, Exception {
		if (details == null || details.length == 0)
			return details;
		String[] Ids = null;
		Vector vecIds = new Vector();
		HashMap tempmap = new HashMap();
		for (int j = 0; j < details.length; j++) {
			if (details[j].getAssid() == null)
				continue;
			if (details[j].getAss() == null && tempmap.get(details[j].getAssid()) == null) {
				vecIds.addElement(details[j].getAssid());
				tempmap.put(details[j].getAssid(), details[j].getAssid());
			}
		}
		if (vecIds.size() == 0) {
			VoWizard wizard = new VoWizard();

			wizard.setMatchingIndex(new int[] { VoucherKey.D_DETAILINDEX }, null);

			wizard.sort(details, new int[] { VoucherKey.D_DETAILINDEX });
			return details;
		}
		Ids = new String[vecIds.size()];
		vecIds.copyInto(Ids);

		nc.bs.glcom.ass.FreevalueDMO dmo = new nc.bs.glcom.ass.FreevalueDMO();
		nc.vo.glcom.balance.GlAssVO[] glAssVo = dmo.queryAllByIDs(Ids, null);

		if (glAssVo == null)
			throw new BusinessException("Error AssIDs::" + vecIds);
		HashMap assvocache = new HashMap();
		for (int i = 0; i < glAssVo.length; i++) {
			glAssVo[i].setAssID(glAssVo[i].getAssID().trim());
			assvocache.put(glAssVo[i].getAssID(), glAssVo[i].getAssVos());
		}

		for (int i = 0; i < details.length; i++) {
			if (details[i].getAssid() != null && details[i].getAss() == null) {
				details[i].setAss((nc.vo.glcom.ass.AssVO[]) assvocache.get(details[i].getAssid()));
			}
		}

		VoWizard wizard = new VoWizard();

		wizard.setMatchingIndex(new int[] { VoucherKey.D_DETAILINDEX }, null);

		wizard.sort(details, new int[] { VoucherKey.D_DETAILINDEX });

		return details;
	}

	@SuppressWarnings("unchecked")
	public VoucherVO[] catBridgeVouchers(VoucherVO[] vouchers) throws BusinessException{
		if(null == vouchers || vouchers.length == 0)
			return null;
		List<DetailVO> details = new LinkedList<DetailVO>();
		for (VoucherVO vvo : vouchers) {
			details.addAll(vvo.getDetail());
		}
		String[] Ids = null;
		Vector vecIds = new Vector();
		HashMap tempmap = new HashMap();
		for (int j = 0; j < details.size(); j++) {
			if (details.get(j).getAssid() == null)
				continue;
			if (details.get(j).getAss() == null && tempmap.get(details.get(j).getAssid()) == null) {
				vecIds.addElement(details.get(j).getAssid());
				tempmap.put(details.get(j).getAssid(), details.get(j).getAssid());
			}
		}
		if (vecIds.size() == 0)
			return vouchers;
		Ids = new String[vecIds.size()];
		vecIds.copyInto(Ids);
		nc.bs.glcom.ass.FreevalueDMO dmo;
		nc.vo.glcom.balance.GlAssVO[] glAssVo = null;
		try {
			dmo = new nc.bs.glcom.ass.FreevalueDMO();
			glAssVo = dmo.queryAllByIDs(Ids, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (glAssVo == null)
			throw new BusinessException("Error AssIDs::" + vecIds);
		HashMap assvocache = new HashMap();
		for (int i = 0; i < glAssVo.length; i++) {
			glAssVo[i].setAssID(glAssVo[i].getAssID().trim());
			assvocache.put(glAssVo[i].getAssID(), glAssVo[i].getAssVos());
		}
		for (int i = 0; i < details.size(); i++) {
			if (details.get(i).getAssid() != null && details.get(i).getAss() == null) {
				details.get(i).setAss((nc.vo.glcom.ass.AssVO[]) assvocache.get(details.get(i).getAssid()));
			}
		}
		return vouchers;
	}

	private DetailVO[] catCheckstylename(DetailVO[] details) throws Exception {
		if (details == null || details.length == 0)
			return details;
		nc.vo.bd.b19.BalatypeVO[] balatype = getBalatype(details[0].getPk_corp());
		if (balatype == null)
			return details;
		HashMap map = new HashMap();
		for (int i = 0; i < balatype.length; i++) {
			map.put(balatype[i].getPk_balatype(), balatype[i].getBalanname());
		}
		for (int i = 0; i < details.length; i++) {
			details[i].setCheckstylename((String) map.get(details[i].getCheckstyle()));
		}
		return details;
	}

	private void catCorpname(VoucherVO[] vouchers, SmallCorpVO[] corps) throws BusinessException, Exception {
		if (vouchers == null || vouchers.length == 0)
			return;
		nc.vo.glcom.wizard.VoWizard wizard = new nc.vo.glcom.wizard.VoWizard();
		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_CORP }, new int[] { nc.vo.gl.voucher.SmallCorpKey.PK_CORP });
		wizard.setAppendIndex(new int[] { VoucherKey.V_CORPNAME }, new int[] { nc.vo.gl.voucher.SmallCorpKey.CORP_NAME });
		wizard.concat(vouchers, corps, false);
	}

	private DetailVO[] catCurrcode(DetailVO[] details, nc.vo.bd.b20.CurrtypeVO[] currtype) throws BusinessException, Exception {
		if (details == null || details.length == 0)
			return details;
		VoWizard wizard = new VoWizard();
		int[] intIndexInit = new int[] { VoucherKey.D_PK_CURRTYPE };
		int[] intIndexCurrtype = new int[] { nc.vo.bd.b20.CurrtypeKey.K_Pk_currtype };

		wizard.setMatchingIndex(intIndexInit, intIndexCurrtype);
		wizard.setAppendIndex(new int[] { VoucherKey.D_CURRTYPECODE, VoucherKey.D_CURRTYPENAME }, new int[] { nc.vo.bd.b20.CurrtypeKey.K_Currtypecode, nc.vo.bd.b20.CurrtypeKey.K_Currtypename });

		DetailVO[] result;
		Object[] obj = wizard.concat(details, currtype);
		result = new DetailVO[obj.length];
		System.arraycopy(obj, 0, result, 0, obj.length);
		return result;
	}

	private DetailVO[] catDetailMatchingflag(DetailVO[] details, String[] pk_details) throws BusinessException {
		if (details == null || details.length == 0)
			return details;
		if (pk_details == null || pk_details.length == 0)
			return details;
		for (int i = 0; i < details.length; i++) {
			for (int j = 0; j < pk_details.length; j++) {
				if (details[i].getPk_detail().equals(pk_details[j])) {
					details[i].setIsmatched(new nc.vo.pub.lang.UFBoolean(true));
					break;
				}
			}
		}
		return details;
	}

	private DetailVO[] catDetailMachForOffer(VoucherVO voucher) throws BusinessException {
		DetailVO[] details = voucher.getDetails();
		if (details == null || details.length == 0)
			return details;

		for (int i = 0; i < details.length; i++) {

			if (voucher.getOffervoucher() != null) {
				details[i].setIsmatched(new nc.vo.pub.lang.UFBoolean(true));
			}

		}

		return details;
	}

	private VoucherVO[] catDetailMatchingFlagForOffer(VoucherVO[] vouchers) throws BusinessException {

		for (int i = 0; i < vouchers.length; i++) {
			VoucherVO voucherVO = vouchers[i];
			for (int j = 0; j < voucherVO.getDetails().length; j++) {
				DetailVO detail = voucherVO.getDetails()[j];
				if (detail.getPk_offerdetail() != null) {
					detail.setIsmatched(new UFBoolean(true));

				}
			}
		}

		return vouchers;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-5-28 15:03:34)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	private VoucherVO catDetailPk_corp(VoucherVO voucher) {
		for (int i = 0; i < voucher.getNumDetails(); i++) {
			voucher.getDetail(i).setPk_glorgbook(voucher.getPk_glorgbook());
			voucher.getDetail(i).setPk_glorg(voucher.getPk_glorg());
			voucher.getDetail(i).setPk_glbook(voucher.getPk_glbook());
			voucher.getDetail(i).setPk_corp(voucher.getPk_corp());
		}
		return voucher;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-22 9:38:36)
	 *
	 * @param vouchers
	 *            nc.vo.gl.pubvoucher.VoucherVO[]
	 * @param details
	 *            nc.vo.gl.pubvoucher.DetailVO[]
	 * @BusinessException java.lang.BusinessException 异常说明。
	 */
	private void catDetails(VoucherVO[] vouchers, DetailVO[] details) throws BusinessException {
		if (vouchers == null || vouchers.length == 0)
			return;

		// for (int i = 0; i < vouchers.length; i++) {
		// for (int j = 0; j < details.length; j++) {
		// if (vouchers[i].getPk_voucher().equals(details[j].getPk_voucher())) {
		// vouchers[i].addDetail(details[j]);
		// }
		// }
		// }
		HashMap<String, VoucherVO> voucherhm = new HashMap<String, VoucherVO>();
		for (int i = 0; i < vouchers.length; i++) {
			voucherhm.put(vouchers[i].getPk_voucher(), vouchers[i]);
		}
		for (int i = 0; i < details.length; i++) {
			VoucherVO voucher = voucherhm.get(details[i].getPk_voucher());
			if (voucher != null) {
				voucher.addDetail(details[i]);

			}
		}
	}

	private DetailVO[] catDtlFreevalue(DetailVO[] details) throws BusinessException, Exception {
		if (details == null || details.length == 0)
			return details;
		String[] pk_details = new String[details.length];
		for (int i = 0; i < pk_details.length; i++) {
			pk_details[i] = details[i].getPk_detail();
		}
		DtlfreevalueDMO dmo = new DtlfreevalueDMO();
		DtlfreevalueVO[] app = dmo.queryByDetailPKs(pk_details);

		VoWizard wizard = new VoWizard();
		int[] intIndexInit = new int[] { VoucherKey.D_PK_DETAIL };
		int[] intIndexCurrtype = new int[] { VoucherKey.D_PK_DETAIL };

		wizard.setMatchingIndex(intIndexInit, intIndexCurrtype);
		wizard.setAppendIndex(new int[] { VoucherKey.D_FREEVALUE1, VoucherKey.D_FREEVALUE2, VoucherKey.D_FREEVALUE3, VoucherKey.D_FREEVALUE4, VoucherKey.D_FREEVALUE5, VoucherKey.D_FREEVALUE6,
				VoucherKey.D_FREEVALUE7, VoucherKey.D_FREEVALUE8, VoucherKey.D_FREEVALUE9, VoucherKey.D_FREEVALUE10, VoucherKey.D_FREEVALUE11, VoucherKey.D_FREEVALUE12, VoucherKey.D_FREEVALUE13,
				VoucherKey.D_FREEVALUE14, VoucherKey.D_FREEVALUE15, VoucherKey.D_FREEVALUE16, VoucherKey.D_FREEVALUE17, VoucherKey.D_FREEVALUE18, VoucherKey.D_FREEVALUE19, VoucherKey.D_FREEVALUE20,
				VoucherKey.D_FREEVALUE21, VoucherKey.D_FREEVALUE22, VoucherKey.D_FREEVALUE23, VoucherKey.D_FREEVALUE24, VoucherKey.D_FREEVALUE25, VoucherKey.D_FREEVALUE26, VoucherKey.D_FREEVALUE27,
				VoucherKey.D_FREEVALUE28, VoucherKey.D_FREEVALUE29, VoucherKey.D_FREEVALUE30 }, new int[] { VoucherKey.D_FREEVALUE1, VoucherKey.D_FREEVALUE2, VoucherKey.D_FREEVALUE3,
				VoucherKey.D_FREEVALUE4, VoucherKey.D_FREEVALUE5, VoucherKey.D_FREEVALUE6, VoucherKey.D_FREEVALUE7, VoucherKey.D_FREEVALUE8, VoucherKey.D_FREEVALUE9, VoucherKey.D_FREEVALUE10,
				VoucherKey.D_FREEVALUE11, VoucherKey.D_FREEVALUE12, VoucherKey.D_FREEVALUE13, VoucherKey.D_FREEVALUE14, VoucherKey.D_FREEVALUE15, VoucherKey.D_FREEVALUE16, VoucherKey.D_FREEVALUE17,
				VoucherKey.D_FREEVALUE18, VoucherKey.D_FREEVALUE19, VoucherKey.D_FREEVALUE20, VoucherKey.D_FREEVALUE21, VoucherKey.D_FREEVALUE22, VoucherKey.D_FREEVALUE23, VoucherKey.D_FREEVALUE24,
				VoucherKey.D_FREEVALUE25, VoucherKey.D_FREEVALUE26, VoucherKey.D_FREEVALUE27, VoucherKey.D_FREEVALUE28, VoucherKey.D_FREEVALUE29, VoucherKey.D_FREEVALUE30 });

		DetailVO[] result;
		Object[] obj = null;
		if (app != null && app.length != 0) {
			obj = wizard.concat(details, app);
			result = new DetailVO[obj.length];
			for (int i = 0; i < obj.length; i++) {
				result[i] = (DetailVO) obj[i];
			}
			// System.arraycopy(obj, 0, result, 0, obj.length);
		} else {
			result = details;
		}
		return result;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-5-28 15:03:34)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	private VoucherVO catOppositesubj(VoucherVO voucher) {
		Vector debitaccsubj = new Vector();
		Vector creditaccsubj = new Vector();
		String debitaccsubjpks = "";
		String creditaccsubjpks = "";
		for (int i = 0; i < voucher.getNumDetails(); i++) {
			if (voucher.getDetail(i).getLocalcreditamount().equals(new nc.vo.pub.lang.UFDouble(0))) {
				if (debitaccsubj.size() < 9)
					debitaccsubj.addElement(voucher.getDetail(i).getPk_accsubj());
				else
					continue;
			} else {
				if (creditaccsubj.size() < 9)
					creditaccsubj.addElement(voucher.getDetail(i).getPk_accsubj());
				else
					continue;
			}
		}
		for (int i = 0; i < debitaccsubj.size(); i++) {
			if (debitaccsubj.elementAt(i) != null)
				debitaccsubjpks = debitaccsubjpks + debitaccsubj.elementAt(i).toString() + ",";
		}
		if (debitaccsubjpks.length() > 0)
			debitaccsubjpks = debitaccsubjpks.substring(0, debitaccsubjpks.length() - 1);
		for (int i = 0; i < creditaccsubj.size(); i++) {
			if (creditaccsubj.elementAt(i) != null)
				creditaccsubjpks = creditaccsubjpks + creditaccsubj.elementAt(i).toString() + ",";
		}
		if (creditaccsubjpks.length() > 0)
			creditaccsubjpks = creditaccsubjpks.substring(0, creditaccsubjpks.length() - 1);
		for (int i = 0; i < voucher.getNumDetails(); i++) {
			if (voucher.getDetail(i).getLocalcreditamount().equals(new nc.vo.pub.lang.UFDouble(0))) {
				voucher.getDetail(i).setOppositesubj(creditaccsubjpks);
			} else {
				voucher.getDetail(i).setOppositesubj(debitaccsubjpks);
			}
		}
		return voucher;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-5-28 15:03:34)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	private VoucherVO catRegulationPeriod(VoucherVO voucher) {
		if (voucher.getFree1() == null || voucher.getFree1().trim().length() == 2)
			voucher.setFree1(voucher.getPeriod());
		if (voucher.getVoucherkind().intValue() != 1)
			voucher.setFree1(voucher.getPeriod());
		return voucher;
	}

	private VoucherVO catOrgAndBook(VoucherVO voucher) {
		if (voucher.getPk_glorgbook() == null)
			return voucher;
		try {
			if (voucher.getPk_glorg() == null || voucher.getPk_glorg().trim().length() == 0)
				voucher.setPk_glorg(BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(voucher.getPk_glorgbook()).getPk_glorg());
			if (voucher.getPk_glbook() == null || voucher.getPk_glbook().trim().length() == 0)
				voucher.setPk_glbook(BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(voucher.getPk_glorgbook()).getPk_glbook());
			if (voucher.getPk_corp() == null || voucher.getPk_corp().trim().length() == 0)
				voucher.setPk_corp(BDGLOrgBookAccessor.getPk_corp(voucher.getPk_glorgbook()));
		} catch (Exception e) {
			Log.getInstance(this.getClass().getName()).info("Attention!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Log.getInstance(this.getClass().getName()).info("Attention!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Log.getInstance(this.getClass().getName()).info("There is an BusinessException at class and method::VoucherVO nc.bs.gl.voucher.VoucherBO.catOrgAndBook(VoucherVO voucher)");
			Log.getInstance(this.getClass().getName()).info("It is not a problem for GL system, but it maybe a serious error for other system.");
			Log.getInstance(this.getClass().getName()).info("The BusinessException is ::");
			e.printStackTrace();
			Log.getInstance(this.getClass().getName()).info("Attention!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Log.getInstance(this.getClass().getName()).info("Attention!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		return voucher;
	}

	private DetailVO[] catSubjfreevalue(DetailVO[] details) throws Exception {
		if (details == null || details.length == 0)
			return details;
		String[] pk_details = new String[details.length];
		for (int i = 0; i < pk_details.length; i++) {
			pk_details[i] = details[i].getPk_detail();
		}
		SubjfreevalueDMO dmo = new SubjfreevalueDMO();
		SubjfreevalueVO[] app = dmo.queryByDetailPKs(pk_details);

		if (app != null) {
			HashMap cache = new HashMap();
			for (int i = 0; i < app.length; i++) {
				if (app[i] == null)
					continue;
				cache.put(app[i].getPk_detail(), app[i]);
			}
			for (int i = 0; i < details.length; i++) {
				details[i].setSubjfreevalue((SubjfreevalueVO) cache.get(details[i].getPk_detail()));
			}
		}
		return details;
	}

	private DetailVO[] catCashFlows(DetailVO[] details) throws Exception {
		if (details == null || details.length == 0)
			return details;
		String[] pk_details = new String[details.length];
		for (int i = 0; i < pk_details.length; i++) {
			pk_details[i] = details[i].getPk_detail();
		}
		CashflowcaseDMO dmo = new CashflowcaseDMO();
		CashflowcaseVO[] app = dmo.queryByPKDetails(pk_details);

		if (app != null) {
			HashMap cache = new HashMap();
			for (int i = 0; i < app.length; i++) {
				if (app[i] == null)
					continue;
				if (cache.get(app[i].getPk_detail()) == null) {
					Vector<CashflowcaseVO> vec = new Vector<CashflowcaseVO>();
					vec.addElement(app[i]);
					cache.put(app[i].getPk_detail(), vec);
				} else {
					Vector<CashflowcaseVO> vec = (Vector<CashflowcaseVO>) cache.get(app[i].getPk_detail());
					vec.addElement(app[i]);
					cache.put(app[i].getPk_detail(), vec);
				}
			}
			for (int i = 0; i < details.length; i++) {
				Vector<CashflowcaseVO> vec = (Vector<CashflowcaseVO>) cache.get(details[i].getPk_detail());
				if (vec != null && vec.size() > 0) {
					CashflowcaseVO[] chvos = new CashflowcaseVO[vec.size()];
					chvos = (CashflowcaseVO[]) vec.toArray(chvos);
					details[i].setCashFlow(chvos);
				}
			}
		}
		return details;
	}

	private DetailVO[] catSubjName(DetailVO[] details, nc.vo.bd.b02.AccsubjVO[] accsubjs) throws Exception {
		if (details == null || details.length == 0)
			return details;
		nc.vo.glcom.wizard.VoWizard wizard = new nc.vo.glcom.wizard.VoWizard();
		wizard.setMatchingIndex(new int[] { VoucherKey.D_PK_ACCSUBJ }, new int[] { nc.vo.bd.b02.AccsubjKey.PK_ACCSUBJ });
		wizard.setAppendIndex(new int[] { VoucherKey.D_ACCSUBJCODE, VoucherKey.D_ACCSUBJNAME }, new int[] { nc.vo.bd.b02.AccsubjKey.ACCSUBJ_CODE, nc.vo.bd.b02.AccsubjKey.ACCSUBJ_DISPNAME });
		DetailVO[] result;
		Object[] obj = wizard.concat(details, accsubjs, false);
		result = new DetailVO[obj.length];
		System.arraycopy(obj, 0, result, 0, obj.length);
		return result;
	}

	private void catSystemname(VoucherVO[] vouchers) throws Exception {
		if (vouchers == null || vouchers.length == 0)
			return;
		DapsystemDMO dmo = new DapsystemDMO();
		DapsystemVO[] dapsystem = dmo.queryAll(null);
		VoWizard wizard = new VoWizard();
		int[] intIndexInit = new int[] { VoucherKey.V_PK_SYSTEM };
		int[] intIndexCurrtype = new int[] { VoucherKey.V_PK_SYSTEM };

		wizard.setMatchingIndex(intIndexInit, intIndexCurrtype);
		wizard.setAppendIndex(new int[] { VoucherKey.V_SYSTEMNAME }, new int[] { VoucherKey.V_SYSTEMNAME });

		wizard.concat(vouchers, dapsystem);
	}

	private void sort(VoucherVO[] vouchers) {
		if (vouchers == null || vouchers.length == 0)
			return;
		nc.vo.glcom.wizard.VoWizard wizard = new nc.vo.glcom.wizard.VoWizard();
		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_GLORGBOOK, VoucherKey.V_YEAR, VoucherKey.V_PERIOD, VoucherKey.V_PK_VOUCHERTYPE, VoucherKey.V_NO }, null);
		wizard.sort(vouchers, new int[] { VoucherKey.V_PK_GLORGBOOK, VoucherKey.V_YEAR, VoucherKey.V_PERIOD, VoucherKey.V_PK_VOUCHERTYPE, VoucherKey.V_NO });
	}

	private void catUsername(VoucherVO[] vouchers, UserVO[] users) throws Exception {
		if (vouchers == null || vouchers.length == 0)
			return;
		if (users == null || users.length == 0)
			return;
		nc.vo.glcom.wizard.VoWizard wizard = new nc.vo.glcom.wizard.VoWizard();
		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_CASHER }, new int[] { UserKey.PK_USER });
		wizard.setAppendIndex(new int[] { VoucherKey.V_CASHERNAME }, new int[] { UserKey.USERNAME });
		Object[] obj1 = wizard.concat(vouchers, users, false);

		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_CHECKED }, new int[] { UserKey.PK_USER });
		wizard.setAppendIndex(new int[] { VoucherKey.V_CHECKEDNAME }, new int[] { UserKey.USERNAME });
		Object[] obj2 = wizard.concat(vouchers, users, false);

		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_MANAGER }, new int[] { UserKey.PK_USER });
		wizard.setAppendIndex(new int[] { VoucherKey.V_MANAGERNAME }, new int[] { UserKey.USERNAME });
		Object[] obj3 = wizard.concat(vouchers, users, false);

		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_PREPARED }, new int[] { UserKey.PK_USER });
		wizard.setAppendIndex(new int[] { VoucherKey.V_PREPAREDNAME }, new int[] { UserKey.USERNAME });
		Object[] obj4 = wizard.concat(vouchers, users, false);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-1 13:14:48)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 * @BusinessException BusinessException 异常说明。
	 */
	private VoucherVO[] catVoucherFreeValue(VoucherVO[] voucher) throws Exception {
		// voucher.setNo(getCorrectVoucherNo(voucher));
		if (voucher == null || voucher.length == 0)
			return voucher;
		String[] pks = new String[voucher.length];
		for (int i = 0; i < voucher.length; i++) {
			pks[i] = voucher[i].getPk_voucher();
		}
		VchfreevalueVO[] vchf = new VchfreevalueDMO().queryByVoucherPKs(pks);
		if (vchf == null || vchf.length == 0)
			return voucher;

		HashMap cache = new HashMap();
		for (int i = 0; i < vchf.length; i++) {
			if (vchf[i] != null) {
				cache.put(vchf[i].getPk_voucher(), vchf[i]);
			}
		}
		for (int i = 0; i < voucher.length; i++) {
			VchfreevalueVO vf = (VchfreevalueVO) cache.get(voucher[i].getPk_voucher());
			if (vf != null) {
				voucher[i].setFreevalue1(vf.getFreevalue1());
				voucher[i].setFreevalue2(vf.getFreevalue2());
				voucher[i].setFreevalue3(vf.getFreevalue3());
				voucher[i].setFreevalue4(vf.getFreevalue4());
				voucher[i].setFreevalue5(vf.getFreevalue5());
			}
		}
		return voucher;
	}

	private void catVoucherMatchingflag(VoucherVO[] vouchers, String[] pk_vouchers) throws BusinessException {
		if (vouchers == null || vouchers.length == 0)
			return;
		if (pk_vouchers == null || pk_vouchers.length == 0)
			return;
		HashMap map = new HashMap();
		for (int i = 0; i < pk_vouchers.length; i++) {
			map.put(pk_vouchers[i], "Y");
		}
		for (int i = 0; i < vouchers.length; i++) {
			if (map.get(vouchers[i].getPk_voucher()) == null)
				vouchers[i].setIsmatched(new Boolean(false));
			else
				vouchers[i].setIsmatched(new Boolean(true));
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-1 13:14:48)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 * @BusinessException BusinessException 异常说明。
	 */
	private VoucherVO catVoucherNo(VoucherVO voucher) throws BusinessException {
		voucher.setNo(getCorrectVoucherNo(voucher));
		return voucher;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-28 11:57:58)
	 *
	 * @return nc.vo.gl.pubvoucher.VoucherVO[]
	 * @param vouchers
	 *            nc.vo.gl.pubvoucher.VoucherVO[]
	 * @BusinessException BusinessException 异常说明。
	 */
	public VoucherVO[] catVouchers(VoucherVO[] vouchers) throws BusinessException {
		if (vouchers == null || vouchers.length == 0)
			return vouchers;
		try {
			if (vouchers[0].getCorpname() == null)
				catCorpname(vouchers, getCorps());
			if (vouchers[0].getVouchertypename() == null)
				catVouchertypename(vouchers, getVouchertypes(null));
			if (vouchers[0].getPreparedname() == null)
				catUsername(vouchers, getUsers(null));
			for (int i = 0; i < vouchers.length; i++) {
				DetailVO[] details = vouchers[i].getDetails();
				if (details != null && details.length > 0 && details[0].getCurrtypecode() == null)
					details = catCurrcode(details, getCurrtypes());
				if (details != null && details.length > 0 && details[0].getAccsubjname() == null) {
					String[] pk_accsubj = new String[details.length];
					for (int j = 0; j < details.length; j++)
						pk_accsubj[j] = details[j].getPk_accsubj();
					details = catSubjName(details, getAccsubj(pk_accsubj));
				}
				if (details != null && details.length > 0 && details[0].getAss() == null)
					details = catAss(details);
				// details = catAppend(details);

				details = catDtlFreevalue(details);
				VoWizard wizard = new VoWizard();

				wizard.setMatchingIndex(new int[] { VoucherKey.D_DETAILINDEX }, null);

				wizard.sort(details, new int[] { VoucherKey.D_DETAILINDEX });
				vouchers[i].setDetails(details);
			}
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException("VoucherBO::catVouchers(VoucherVO[]) BusinessException!", e);
		}
		return vouchers;
	}

	private void catVouchertypename(VoucherVO[] vouchers, VouchertypeVO[] types) throws Exception {
		if (vouchers == null || vouchers.length == 0)
			return;
		nc.vo.glcom.wizard.VoWizard wizard = new nc.vo.glcom.wizard.VoWizard();
		wizard.setMatchingIndex(new int[] { VoucherKey.V_PK_VOUCHERTYPE }, new int[] { nc.vo.bd.b18.VoucherTypeKey.pk_vouchertype });
		wizard.setAppendIndex(new int[] { VoucherKey.V_VOUCHERTYPENAME }, new int[] { nc.vo.bd.b18.VoucherTypeKey.vouchtypename });
		Object[] obj = wizard.concat(vouchers, types, false);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-7-18 14:06:11)
	 *
	 * @param voucher
	 *            nc.voucher.gl.pubvoucher.VoucherVO
	 */
	private void checkCanAbandon(VoucherVO voucher, String strOperator, Boolean op) throws BusinessException, Exception {
		OperationResultVO[] result = new VoucherCheckBO().checkPreparedDate(voucher);
		result = checkSystem(result,voucher);
		if (result != null) {
			StringBuffer strMsg = new StringBuffer();
			boolean errflag = false;
			for (int i = 0; i < result.length; i++) {
				switch (result[i].m_intSuccess) {
				case 0:
					break;
				case 1:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																															 * @res
																															 * "警告:"
																															 */+ result[i].m_strDescription + "\n");
					break;
				case 2:
					errflag = true;
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																															 * @res
																															 * "错误:"
																															 */+ result[i].m_strDescription + "\n");
					break;
				default:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																															 * @res
																															 * "信息:"
																															 */+ result[i].m_strDescription + "\n");
				}
			}
			if (strMsg.length() > 0 && errflag)
				throw new BusinessException(strMsg.toString());
		}
		if (op.booleanValue()) {
			String[] pk_details = getRecVoucherPKs(new String[] { voucher.getPk_voucher() });
			if (pk_details != null && pk_details.length != 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000541")/*
																																	 * @res
																																	 * "凭证已有分录被勾对，不能作废。"
																																	 */);
			Boolean isOwnEditable = new Boolean(new GlParaDMO().isEditOwnVoucher(voucher.getPk_glorgbook()).booleanValue());
			if (isOwnEditable.booleanValue() && !strOperator.equals(voucher.getPk_prepared()))
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000543")/*
																																		 * @res
																																		 * "被设定只能由本人修改，您无权作废别人的凭证。"
																																		 */);
			else if (voucher.getPk_manager() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000544")/*
																																		 * @res
																																		 * "已被记账，不能作废。建议先取消记账然后再作废该凭证。"
																																		 */);
			else if (voucher.getPk_checked() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000545")/*
																																		 * @res
																																		 * "已被审核，不能作废。建议先取消审核然后再作废该凭证。"
																																		 */);
			else if (voucher.getPk_casher() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000546")/*
																																		 * @res
																																		 * "已被签字，不能作废。建议先取消签字然后再作废该凭证。"
																																		 */);
			else if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() == 1 && new VoucherExtendDMO().isExistLaterRegulationVoucher(voucher))
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000547")/*
																																		 * @res
																																		 * "是调整期凭证，并且其后面调整期间已经有调整凭证，不允许作废。"
																																		 */);
			VoucherVO t_voucher = (VoucherVO) voucher.clone();
			t_voucher.setDetails(null);
			OperationResultVO[] checkresult = null;
			// 检查余额方向
			checkresult = new VoucherCheckBO().checkBalanceControl(t_voucher);
			if (checkresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < checkresult.length; i++) {
					switch (checkresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ checkresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			//
			checkresult = new VoucherCheckBO().checkAssBalanceControlNew(t_voucher, op);
			if (checkresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < checkresult.length; i++) {
					switch (checkresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ checkresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
		} else {
			if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() == 1 && new VoucherExtendDMO().isExistLaterRegulationVoucher(voucher))
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000548")/*
																																		 * @res
																																		 * "是调整期凭证，并且其后面调整期间已经有调整凭证，不允许取消作废。"
																																		 */);
			Boolean isOwnEditable = new Boolean(new GlParaDMO().isEditOwnVoucher(voucher.getPk_glorgbook()).booleanValue());
			if (isOwnEditable.booleanValue() && !strOperator.equals(voucher.getPk_prepared()))
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																	 * @res
																																	 * "凭证"
																																	 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000543")/*
																																		 * @res
																																		 * "被设定只能由本人修改，您无权作废别人的凭证。"
																																		 */);
			VoucherVO t_voucher = (VoucherVO) voucher.clone();
			OperationResultVO[] checkresult = null;
			// 检查余额方向
			checkresult = new VoucherCheckBO().checkBalanceControl(t_voucher);
			if (checkresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < checkresult.length; i++) {
					switch (checkresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ checkresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			checkresult = new VoucherCheckBO().checkAssBalanceControlNew(t_voucher, op);
			if (checkresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < checkresult.length; i++) {
					switch (checkresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ checkresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
		}
	}

	/**
	 *
	 * @author zhaozh
	 * @since 5.7 河北省农村信用社联合社 非总账制作的凭证不能作废
	 * @param result
	 * @param voucher
	 * @return
	 */
	private OperationResultVO[] checkSystem(OperationResultVO[] result,
			VoucherVO voucher) {
		if(!(voucher.getPk_system().equalsIgnoreCase("GL")||voucher.getPk_system().equalsIgnoreCase("TR"))){
			OperationResultVO rs = new OperationResultVO();
			rs.m_intSuccess = 2;
			rs.m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl5702","UPP2002gl5702-000019")/*@res "非总账制作的凭证不能作废！！！"*/;
			result = OperationResultVO.appendResultVO(result, new OperationResultVO[] { rs });
		}
		return result;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-7-18 14:06:11)
	 *
	 * @param voucher
	 *            nc.voucher.gl.pubvoucher.VoucherVO
	 */
	private void checkCanDelete(VoucherVO voucher, String strOperator, Boolean isOwnEditable) throws BusinessException, Exception {
		String[] pk_details = getRecVoucherPKs(new String[] { voucher.getPk_voucher() });
		if (pk_details != null && pk_details.length != 0)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000549")/*
																																 * @res
																																 * "凭证已有分录被勾对，不能删除。"
																																 */);
		if (isOwnEditable == null) {
			isOwnEditable = new Boolean(new GlParaDMO().isEditOwnVoucher(voucher.getPk_glorgbook()).booleanValue());
		}
		if (isOwnEditable.booleanValue() && !strOperator.equals(voucher.getPk_prepared()))
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																 * @res
																																 * "凭证"
																																 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000550")/*
																																	 * @res
																																	 * "被设定只能由本人修改，您无权删除别人的凭证。"
																																	 */);
		else if (voucher.getPk_manager() != null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																 * @res
																																 * "凭证"
																																 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000551")/*
																																	 * @res
																																	 * "已被记账，不能删除。建议先取消记账然后再删除该凭证。"
																																	 */);
		else if (voucher.getPk_checked() != null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																 * @res
																																 * "凭证"
																																 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000552")/*
																																	 * @res
																																	 * "已被审核，不能删除。建议先取消审核然后再删除该凭证。"
																																	 */);
		else if (voucher.getPk_casher() != null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																 * @res
																																 * "凭证"
																																 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000553")/*
																																	 * @res
																																	 * "已被签字，不能删除。建议先取消签字然后再删除该凭证。"
																																	 */);
		else if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() == 1 && new VoucherExtendDMO().isExistLaterRegulationVoucher(voucher))
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000542")/*
																																 * @res
																																 * "凭证"
																																 */+ voucher.getNo().toString() + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000554")/*
																																	 * @res
																																	 * "是调整期凭证，并且其后面调整期间已经有调整凭证，不允许删除。"
																																	 */);
		VoucherVO t_voucher = (VoucherVO) voucher.clone();
		t_voucher.setDetails(null);
		OperationResultVO[] checkresult = null;
		// 检查余额方向
		checkresult = new VoucherCheckBO().checkBalanceControl(t_voucher);
		if (checkresult != null) {
			StringBuffer strMsg = new StringBuffer(16 * 1024);
			boolean errflag = false;
			for (int i = 0; i < checkresult.length; i++) {
				switch (checkresult[i].m_intSuccess) {
				case 0:
					break;
				case 1:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																															 * @res
																															 * "警告:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				case 2:
					errflag = true;
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																															 * @res
																															 * "错误:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				default:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																															 * @res
																															 * "信息:"
																															 */+ checkresult[i].m_strDescription + "\n");
				}
			}
			if (strMsg.length() > 0 && errflag)
				throw new BusinessException(strMsg.toString());
		}
		// 检查余额方向
		checkresult = new VoucherCheckBO().checkAssBalanceControlNew(t_voucher, new Boolean(true));
		if (checkresult != null) {
			StringBuffer strMsg = new StringBuffer(16 * 1024);
			boolean errflag = false;
			for (int i = 0; i < checkresult.length; i++) {
				switch (checkresult[i].m_intSuccess) {
				case 0:
					break;
				case 1:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																															 * @res
																															 * "警告:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				case 2:
					errflag = true;
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																															 * @res
																															 * "错误:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				default:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																															 * @res
																															 * "信息:"
																															 */+ checkresult[i].m_strDescription + "\n");
				}
			}
			if (strMsg.length() > 0 && errflag)
				throw new BusinessException(strMsg.toString());
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-7-18 14:06:11)
	 *
	 * @param voucher
	 *            nc.voucher.gl.pubvoucher.VoucherVO
	 */
	private OperationResultVO[] checkCanSystemTempSave(VoucherVO voucher) throws BusinessException, Exception {
		OperationResultVO[] checkresult = null;
		ArrayList<OperationResultVO> ckeck_vec = new ArrayList<OperationResultVO>();
		//
		checkError(voucher, null);
		// 检查余额方向
		checkresult = new VoucherCheckBO().checkBalanceControl(voucher);
		if (checkresult != null) {
			StringBuffer strMsg = new StringBuffer(16 * 1024);
			boolean errflag = false;
			for (int i = 0; i < checkresult.length; i++) {
				ckeck_vec.add(checkresult[i]);
				switch (checkresult[i].m_intSuccess) {
				case 0:
					break;
				case 1:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																															 * @res
																															 * "警告:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				case 2:
					errflag = true;
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																															 * @res
																															 * "错误:"
																															 */+ checkresult[i].m_strDescription + "\n");
					break;
				default:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																															 * @res
																															 * "信息:"
																															 */+ checkresult[i].m_strDescription + "\n");
				}
			}
			if (strMsg.length() > 0 && errflag)
				throw new BusinessException(strMsg.toString());
		}
		// 检查余额方向
		OperationResultVO[] checkresult1 = new VoucherCheckBO().checkAssBalanceControlNew(voucher, new Boolean(true));
		if (checkresult1 != null) {
			StringBuffer strMsg = new StringBuffer(16 * 1024);
			boolean errflag = false;
			for (int i = 0; i < checkresult1.length; i++) {
				ckeck_vec.add(checkresult1[i]);
				switch (checkresult1[i].m_intSuccess) {
				case 0:
					break;
				case 1:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																															 * @res
																															 * "警告:"
																															 */+ checkresult1[i].m_strDescription + "\n");
					break;
				case 2:
					errflag = true;
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																															 * @res
																															 * "错误:"
																															 */+ checkresult1[i].m_strDescription + "\n");
					break;
				default:
					strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																															 * @res
																															 * "信息:"
																															 */+ checkresult1[i].m_strDescription + "\n");
				}
			}
			if (strMsg.length() > 0 && errflag)
				throw new BusinessException(strMsg.toString());
		}

		return ckeck_vec.size() > 0 ? ckeck_vec.toArray(new OperationResultVO[0]) : null;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-27 10:51:07)
	 *
	 * @return java.lang.Boolean
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	private Boolean checkVoucherNo(VoucherVO voucher) throws BusinessException, Exception {
		Integer count = new VoucherExtendDMO().getCountVoucherNo(voucher);
		if (count != null && count.intValue() > 1)
			throw new BusinessException(new VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
		return new Boolean(true);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-21 10:14:02)
	 *
	 * @param pks
	 *            java.lang.String[]
	 * @param java.lang.Integer
	 */
	private void copy_deletedVoucher(String pk_voucher) throws BusinessException, Exception {
		VoucherDMO dmo1 = new VoucherDMO();
		VoucherVO voucher = dmo1.findByPrimaryKey(pk_voucher);
		if (voucher == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000555")/*
																																 * @res
																																 * "凭证已被别人删除。"
																																 */);
		voucher.setFree10(pk_voucher);
		String pk = dmo1.insert(voucher);
		new VoucherExtendDMO().logic_deleteByVoucherPK(pk);

		DetailExtendDMO dmo2 = new DetailExtendDMO();
		DetailVO[] details = dmo2.queryByVoucherPks(new String[] { pk_voucher });
//		if (details == null || details.length == 0)
//			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000556")/*
//																																 * @res
//																																 * "凭证的原始数据已被破坏，无法完成操作。"
//																																 */);
		/**@author zhaozh
		 * @since 5.7 暂存的无表体的凭证不能修改保存
		 */
		if (details == null || details.length == 0) {
			if (!voucher.getErrmessage().equals("暂存")) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("20021005",
								"UPP20021005-000556")/*
														 * @res
														 * "凭证的原始数据已被破坏，无法完成操作。"
														 */);
			} else {
				return;
			}
		}
		for (int i = 0; i < details.length; i++) {
			details[i].setPk_voucher(pk);
		}
		String[] pks = dmo2.insertArray(details);
		dmo2.logic_deleteByVoucherPK(pk);

		String[] oldpks = new String[details.length];
		for (int i = 0; i < details.length; i++) {
			oldpks[i] = details[i].getPk_detail();
		}
		/*
		 * DetailappendDMO dmo3 = new DetailappendDMO(); DetailappendVO[]
		 * detailappends = dmo3.queryByDetailPKs(oldpks); if (detailappends !=
		 * null) for (int i = 0; i < detailappends.length; i++) { for (int j =
		 * 0; j < oldpks.length; j++) { if
		 * (detailappends[i].getPk_detail().equals(oldpks[j])) {
		 * detailappends[i].setPk_detail(pks[j]); break; } } }
		 * dmo3.insertArray(detailappends); dmo3.logic_deleteByVoucherPK(pk);
		 */
	}

	/**
	 * 将逻辑删除的数据放到另外一张表里
	 * @param pk_voucher
	 * @throws BusinessException
	 * @throws Exception
	 */
	private void copy_deletedVoucherNew(String pk_voucher) throws BusinessException, Exception {
		VoucherBackupDMO dmo1 = new VoucherBackupDMO();
		VoucherVO voucher = dmo1.findByPrimaryKey(pk_voucher);
		if (voucher == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000555")/*
																																 * @res
																																 * "凭证已被别人删除。"
																																 */);
		voucher.setFree10(pk_voucher);
		String pk = dmo1.insert(voucher);
		DetailBackupExtendDMO dmo2 = new DetailBackupExtendDMO();
		DetailVO[] details = dmo2.queryByVoucherPks(new String[] { pk_voucher });
		/**@author zhaozh
		 * @since 5.7 暂存的无表体的凭证不能修改保存
		 */
		if (details == null || details.length == 0) {
			if (!StringUtils.isEmpty(voucher.getErrmessage()) && voucher.getErrmessage().equals("暂存")) {

			} else {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("20021005",
								"UPP20021005-000556")/*
														 * @res
														 * "凭证的原始数据已被破坏，无法完成操作。"
														 */);
			}
		}else{
			for (int i = 0; i < details.length; i++) {
				details[i].setPk_voucher(pk);
			}
			dmo2.insertArray(details);
		}
	}
	/**
	 * 此处插入方法说明。 创建日期：(2001-12-21 10:14:02)
	 *
	 * @param pks
	 *            java.lang.String[]
	 * @param java.lang.Integer
	 */
	private String copyVoucher(String pk_voucher) throws BusinessException, Exception {
		VoucherDMO dmo1 = new VoucherDMO();
		VoucherVO voucher = dmo1.findByPrimaryKey(pk_voucher);
		if (voucher == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000555")/*
																																 * @res
																																 * "凭证已被别人删除。"
																																 */);
		String pk = dmo1.insert(voucher);

		DetailExtendDMO dmo2 = new DetailExtendDMO();
		DetailVO[] details = dmo2.queryByVoucherPks(new String[] { pk_voucher });
		if (details == null || details.length == 0)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000556")/*
																																 * @res
																																 * "凭证的原始数据已被破坏，无法完成操作。"
																																 */);
		for (int i = 0; i < details.length; i++) {
			details[i].setPk_voucher(pk);
		}
		String[] pks = dmo2.insertArray(details);

		String[] oldpks = new String[details.length];
		for (int i = 0; i < details.length; i++) {
			oldpks[i] = details[i].getPk_detail();
		}
		/*
		 * DetailappendDMO dmo3 = new DetailappendDMO(); DetailappendVO[]
		 * detailappends = dmo3.queryByDetailPKs(oldpks); if (detailappends !=
		 * null) for (int i = 0; i < detailappends.length; i++) { for (int j =
		 * 0; j < oldpks.length; j++) { if
		 * (detailappends[i].getPk_detail().equals(oldpks[j])) {
		 * detailappends[i].setPk_detail(pks[j]); break; } } }
		 * dmo3.insertArray(detailappends);
		 */

		return pk;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-21 10:14:02)
	 *
	 * @param pks
	 *            java.lang.String[]
	 * @param number
	 *            java.lang.Integer
	 */
	public void copyVoucher(VoucherVO voucher, Integer number) throws BusinessException {
		try {
			voucher.clearEmptyDetail();
			if (voucher.getPk_voucher() == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000557")/*
																																	 * @res
																																	 * "不能复制尚未保存的凭证！"
																																	 */);
			else if (voucher.getNumDetails() <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000558")/*
																																	 * @res
																																	 * "复制的凭证分录不能为空！"
																																	 */);
			else if (voucher.getPk_manager() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000559")/*
																																	 * @res
																																	 * "不允许复制已记账的凭证！"
																																	 */);
			VoucherExtendDMO dmo1 = new VoucherExtendDMO();
			VoucherVO[] vouchers = new VoucherVO[number.intValue()];
			int detailnum = voucher.getNumDetails();
			int no = dmo1.getCorrectVoucherNo(voucher).intValue();
			for (int i = 0; i < number.intValue(); i++) {
				vouchers[i] = (VoucherVO) voucher.clone();
				vouchers[i].setPk_voucher(null);
				vouchers[i].setNo(new Integer(no + i));
				DetailVO[] details = new DetailVO[detailnum];
				for (int j = 0; j < detailnum; j++) {
					details[j] = (DetailVO) voucher.getDetail(j).clone();
					details[j].setPk_voucher(null);
					details[j].setPk_detail(null);
				}
				vouchers[i].setDetails(details);
			}
			for (int i = 0; i < number.intValue(); i++) {
				saveVoucher(vouchers[i], true);
			}
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-21 10:14:02)
	 *
	 * @param pks
	 *            java.lang.String[]
	 * @param number
	 *            java.lang.Integer
	 */
	public void copyVoucherUnChecked(VoucherVO voucher, Integer number) throws BusinessException {
		try {
			voucher.clearEmptyDetail();
			if (voucher.getPk_voucher() == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000557")/*
																																	 * @res
																																	 * "不能复制尚未保存的凭证！"
																																	 */);
			else if (voucher.getNumDetails() <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000558")/*
																																	 * @res
																																	 * "复制的凭证分录不能为空！"
																																	 */);
			else if (voucher.getPk_manager() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000559")/*
																																	 * @res
																																	 * "不允许复制已记账的凭证！"
																																	 */);
			VoucherExtendDMO dmo1 = new VoucherExtendDMO();
			VoucherVO[] vouchers = new VoucherVO[number.intValue()];
			int detailnum = voucher.getNumDetails();
			int no = dmo1.getCorrectVoucherNo(voucher).intValue();
			for (int i = 0; i < number.intValue(); i++) {
				vouchers[i] = (VoucherVO) voucher.clone();
				vouchers[i].setPk_voucher(null);
				vouchers[i].setNo(new Integer(no + i + 1));
			}
			String[] pks = dmo1.insertArray(vouchers);
			DetailVO[] details = new DetailVO[number.intValue() * detailnum];
			for (int i = 0; i < number.intValue(); i++) {
				for (int j = 0; j < detailnum; j++) {
					details[i * detailnum + j] = (DetailVO) voucher.getDetail(j).clone();
					details[i * detailnum + j].setPk_voucher(pks[i]);
					details[i * detailnum + j].setPk_detail(null);
				}
			}
			DetailExtendDMO dmo2 = new DetailExtendDMO();
			dmo2.saveArray(details);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-21 10:14:02)
	 *
	 * @param pks
	 *            java.lang.String[]
	 * @param java.lang.Integer
	 */
	private void copyVoucherVO(VoucherVO voucher) throws BusinessException, Exception {
		VoucherExtendDMO dmo1 = new VoucherExtendDMO();
		VoucherVO[] vouchers = new VoucherVO[1];
		int detailnum = voucher.getNumDetails();
		int no = dmo1.getCorrectVoucherNo(voucher).intValue();
		for (int i = 0; i < 1; i++) {
			vouchers[i] = (VoucherVO) voucher.clone();
			vouchers[i].setPk_voucher(null);
			vouchers[i].setNo(new Integer(no + i + 1));
		}
		String[] pks = dmo1.insertArray(vouchers);
		DetailVO[] details = new DetailVO[1 * detailnum];
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < detailnum; j++) {
				details[i * detailnum + j] = (DetailVO) voucher.getDetail(j).clone();
				details[i * detailnum + j].setPk_voucher(pks[i]);
				details[i * detailnum + j].setPk_detail(null);
			}
		}
		DetailExtendDMO dmo2 = new DetailExtendDMO();
		dmo2.saveArray(details);
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	public OperationResultVO[] delete(VoucherVO voucher) throws BusinessException {
		try {
			OperationResultVO[] result = null;
			String strPk_voucher = voucher.getPk_voucher();
			String strOperator = voucher.getPk_prepared();
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			DetailExtendDMO dmo1 = new DetailExtendDMO();
			DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();

			VoucherVO vo = dmo.queryByVoucherPk(strPk_voucher);
			if (vo == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000538")/*
																																	 * @res
																																	 * "凭证已被他人删除，请刷新数据。"
																																	 */);
			else {
				checkCanDelete(vo, strOperator, null);
			}
			GLKeyLock lock = null;
			boolean bLockSuccess = false;
			try {
				// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
				// getBeanHome(nc.bs.pub.lock.LockHome.class,
				// "nc.bs.pub.lock.LockBO");
				// if (home == null)
				// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
				lock = new GLKeyLock();
				for (int i = 0; i < 5; i++) {
					bLockSuccess = lock.lockKey(strPk_voucher, strOperator, "gl_voucher");
					if (bLockSuccess)
						break;
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
				if (!bLockSuccess)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																		 * @res
																																		 * "有其他用户在操作，请稍候再试。"
																																		 */);

				DealclassDMO dealclassdmo = new DealclassDMO();
				DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("delete");
				Vector vecdeleteclass = new Vector();
				VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
				opvo.pk_vouchers = new String[] { strPk_voucher };
				opvo.pk_user = strOperator;
				opvo.userdata = voucher.getUserData();
				if (dealclassvos != null && dealclassvos.length != 0)
					for (int m = 0; m < dealclassvos.length; m++) {
						if (dealclassvos[m].getModules() != null) {
							try {
								nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) NewObjectService.newInstance(dealclassvos[m].getModules(),
										dealclassvos[m].getClassname());
								vecdeleteclass.addElement(m_deleteclassall);
							} catch (FrameworkRuntimeException e) {
								// TODO: handle exception
								log.error(e);
							}
						}
						/*
						 * try { Class m_classdelete =
						 * java.lang.Class.forName(dealclassvos[m].getClassname());
						 * Object m_objectdelete = m_classdelete.newInstance();
						 * nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall =
						 * (nc.bs.gl.pubinterface.IVoucherDelete)
						 * m_objectdelete;
						 * vecdeleteclass.addElement(m_deleteclassall); } catch
						 * (ClassNotFoundException e) { } catch
						 * (NoClassDefFoundError e) { continue; }
						 */
					}

				if (vo.getDeleteclass() != null && !vo.getDeleteclass().trim().equals("")) {
					try {
						Class m_classdelete = java.lang.Class.forName(vo.getDeleteclass());
						Object m_objectdelete = m_classdelete.newInstance();
						nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
						vecdeleteclass.addElement(m_deleteclassall);
					} catch (ClassNotFoundException e) {
					} catch (NoClassDefFoundError e) {
					}
				}
				for (int m = 0; m < vecdeleteclass.size(); m++) {
					OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherDelete) vecdeleteclass.elementAt(m)).beforeDelete(opvo);
					result = OperationResultVO.appendResultVO(result, t_result1);
				}

				// 凭证删除
				dmo.logic_deleteByVoucherPK(strPk_voucher);
				dmo1.logic_deleteByVoucherPK(strPk_voucher);
				dmo3.logic_deleteByVoucherPK(strPk_voucher);

				// return voucher number
				dmo.returnVoucherNoByDelete(vo);

				for (int m = vecdeleteclass.size(); m > 0; m--) {
					OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherDelete) vecdeleteclass.elementAt(m - 1)).afterDelete(opvo);
					result = OperationResultVO.appendResultVO(result, t_result1);
				}
			} finally {
				if (bLockSuccess)
					lock.freeKey(strPk_voucher, strOperator, "gl_voucher");
				// try
				// {
				// lock.remove();
				// }
				// catch (BusinessException e)
				// {
				// e.printStackTrace();
				// }
			}
			return result;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	public OperationResultVO deleteByPk(String strPk_voucher) throws BusinessException {

		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			// if (home == null)
			// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
			lock = new GLKeyLock();
			for (int i = 0; i < 5; i++) {
				bLockSuccess = lock.lockKey(strPk_voucher, null, "gl_voucher");
				if (bLockSuccess)
					break;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
			if (!bLockSuccess)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																	 * @res
																																	 * "有其他用户在操作，请稍候再试。"
																																	 */);
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			VoucherVO vo = dmo.queryByVoucherPk(strPk_voucher);
			DetailExtendDMO dmo1 = new DetailExtendDMO();
			if (vo == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000538")/*
																																	 * @res
																																	 * "凭证已被他人删除，请刷新数据。"
																																	 */);
			else {
				checkCanDelete(vo, vo.getPk_prepared(), new Boolean(false));
			}
			DealclassDMO dealclassdmo = new DealclassDMO();
			DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("delete");
			Vector vecdeleteclass = new Vector();
			VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
			opvo.pk_vouchers = new String[] { strPk_voucher };
			if (dealclassvos != null && dealclassvos.length != 0)
				for (int m = 0; m < dealclassvos.length; m++) {
					if (dealclassvos[m].getModules() != null) {
						try {
							nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
									.getClassname());
							m_deleteclassall.beforeDelete(opvo);
							vecdeleteclass.addElement(m_deleteclassall);
						} catch (FrameworkRuntimeException e) {
							// TODO: handle exception
							log.error(e);
						}
					}
					/*
					 * try { Class m_classdelete =
					 * java.lang.Class.forName(dealclassvos[m].getClassname());
					 * Object m_objectdelete = m_classdelete.newInstance();
					 * nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall =
					 * (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
					 * m_deleteclassall.beforeDelete(opvo);
					 * vecdeleteclass.addElement(m_deleteclassall); } catch
					 * (ClassNotFoundException e) { } catch
					 * (NoClassDefFoundError e) { continue; }
					 */
				}
			if (vo.getDeleteclass() != null && !vo.getDeleteclass().trim().equals("")) {
				try {
					Class m_classdelete = java.lang.Class.forName(vo.getDeleteclass());
					Object m_objectdelete = m_classdelete.newInstance();
					nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
					m_deleteclassall.beforeDelete(opvo);
					vecdeleteclass.addElement(m_deleteclassall);
				} catch (ClassNotFoundException e) {
				} catch (NoClassDefFoundError e) {
				}
			}

			// DetailappendDMO dmo2 = new DetailappendDMO();
			DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
			dmo.logic_deleteByVoucherPK(strPk_voucher);
			dmo1.logic_deleteByVoucherPK(strPk_voucher);
			//

			// dmo2.logic_deleteByVoucherPK(strPk_voucher);
			dmo3.logic_deleteByVoucherPK(strPk_voucher);

			dmo.returnVoucherNoByDelete(vo);
			for (int m = vecdeleteclass.size(); m > 0; m--) {
				((nc.bs.gl.pubinterface.IVoucherDelete) vecdeleteclass.elementAt(m - 1)).afterDelete(opvo);
			}
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException("VoucherBO::delete(VoucherPK) BusinessException!" + e.getMessage(), e);
		} finally {
			if (bLockSuccess)
				lock.freeKey(strPk_voucher, null, "gl_voucher");
			// try
			// {
			// lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}
		return null;
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	public OperationResultVO deleteByPk(String strPk_voucher, Boolean isneedsyscheck) throws BusinessException {

		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			// if (home == null)
			// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
			lock = new GLKeyLock();
			for (int i = 0; i < 5; i++) {
				bLockSuccess = lock.lockKey(strPk_voucher, null, "gl_voucher");
				if (bLockSuccess)
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			if (!bLockSuccess)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																	 * @res
																																	 * "有其他用户在操作，请稍候再试。"
																																	 */);
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			VoucherVO vo = dmo.queryByVoucherPk(strPk_voucher);
			DetailExtendDMO dmo1 = new DetailExtendDMO();
			if (vo == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000538")/*
																																	 * @res
																																	 * "凭证已被他人删除，请刷新数据。"
																																	 */);
			else {
				checkCanDelete(vo, vo.getPk_prepared(), new Boolean(false));
			}
			Vector vecdeleteclass = new Vector();
			VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
			opvo.pk_vouchers = new String[] { strPk_voucher };
			if (isneedsyscheck.booleanValue()) {
				DealclassDMO dealclassdmo = new DealclassDMO();
				DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("delete");
				if (dealclassvos != null && dealclassvos.length != 0)
					for (int m = 0; m < dealclassvos.length; m++) {

						if (dealclassvos[m].getModules() != null) {
							try {
								nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) NewObjectService.newInstance(dealclassvos[m].getModules(),
										dealclassvos[m].getClassname());
								m_deleteclassall.beforeDelete(opvo);
								vecdeleteclass.addElement(m_deleteclassall);
							} catch (FrameworkRuntimeException e) {
								// TODO: handle exception
								log.error(e);
							}
						}
						/*
						 * try { Class m_classdelete =
						 * java.lang.Class.forName(dealclassvos[m].getClassname());
						 * Object m_objectdelete = m_classdelete.newInstance();
						 * nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall =
						 * (nc.bs.gl.pubinterface.IVoucherDelete)
						 * m_objectdelete; m_deleteclassall.beforeDelete(opvo);
						 * vecdeleteclass.addElement(m_deleteclassall); } catch
						 * (ClassNotFoundException e) { } catch
						 * (NoClassDefFoundError e) { continue; }
						 */
					}
				if (vo.getDeleteclass() != null && !vo.getDeleteclass().trim().equals("")) {
					try {
						Class m_classdelete = java.lang.Class.forName(vo.getDeleteclass());
						Object m_objectdelete = m_classdelete.newInstance();
						nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
						m_deleteclassall.beforeDelete(opvo);
						vecdeleteclass.addElement(m_deleteclassall);
					} catch (ClassNotFoundException e) {
					} catch (NoClassDefFoundError e) {
					}
				}
			}

			// DetailappendDMO dmo2 = new DetailappendDMO();
			DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
			dmo.logic_deleteByVoucherPK(strPk_voucher);
			dmo1.logic_deleteByVoucherPK(strPk_voucher);
			// dmo2.logic_deleteByVoucherPK(strPk_voucher);

			dmo3.logic_deleteByVoucherPK(strPk_voucher);

			dmo.returnVoucherNoByDelete(vo);
			if (isneedsyscheck.booleanValue()) {
				for (int m = vecdeleteclass.size(); m > 0; m--) {
					((nc.bs.gl.pubinterface.IVoucherDelete) vecdeleteclass.elementAt(m - 1)).afterDelete(opvo);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException("VoucherBO::delete(VoucherPK) BusinessException!", e);
		} finally {
			if (bLockSuccess)
				lock.freeKey(strPk_voucher, null, "gl_voucher");
			// try
			// {
			// lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}
		return null;
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	private void deleteByPk(String strPk_voucher, String strOperator, Boolean isOwnEditable) throws Exception {

		VoucherExtendDMO dmo = new VoucherExtendDMO();
		DetailExtendDMO dmo1 = new DetailExtendDMO();
		// DetailappendDMO dmo2 = new DetailappendDMO();
		VoucherVO vo = dmo.queryByVoucherPk(strPk_voucher);
		if (vo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000538")/*
																																 * @res
																																 * "凭证已被他人删除，请刷新数据。"
																																 */);
		else {
			checkCanDelete(vo, strOperator, isOwnEditable);
		}
		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			// if (home == null)
			// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
			lock = new GLKeyLock();
			for (int i = 0; i < 5; i++) {
				bLockSuccess = lock.lockKey(strPk_voucher, strOperator, "gl_voucher");
				if (bLockSuccess)
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			if (!bLockSuccess)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																	 * @res
																																	 * "有其他用户在操作，请稍候再试。"
																																	 */);
			DealclassDMO dealclassdmo = new DealclassDMO();
			DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("delete");
			Vector vecdeleteclass = new Vector();
			if (dealclassvos != null && dealclassvos.length != 0)
				for (int m = 0; m < dealclassvos.length; m++) {
					if (dealclassvos[m].getModules() != null) {
						try {
							nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
									.getClassname());
							VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
							opvo.pk_vouchers = new String[] { strPk_voucher };
							opvo.pk_user = strOperator;
							m_deleteclassall.beforeDelete(opvo);
							vecdeleteclass.addElement(m_deleteclassall);
						} catch (FrameworkRuntimeException e) {
							log.error(e);
						}
					}
					/*
					 * try { Class m_classdelete =
					 * java.lang.Class.forName(dealclassvos[m].getClassname());
					 * Object m_objectdelete = m_classdelete.newInstance();
					 * nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall =
					 * (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
					 * VoucherOperateInterfaceVO opvo = new
					 * VoucherOperateInterfaceVO(); opvo.pk_vouchers = new
					 * String[] { strPk_voucher }; opvo.pk_user = strOperator;
					 * m_deleteclassall.beforeDelete(opvo);
					 * vecdeleteclass.addElement(m_deleteclassall); } catch
					 * (ClassNotFoundException e) { } catch
					 * (NoClassDefFoundError e) { continue; }
					 */
				}

			if (vo.getDeleteclass() != null && !vo.getDeleteclass().trim().equals("")) {
				try {
					Class m_classdelete = java.lang.Class.forName(vo.getDeleteclass());
					Object m_objectdelete = m_classdelete.newInstance();
					nc.bs.gl.pubinterface.IVoucherDelete m_deleteclassall = (nc.bs.gl.pubinterface.IVoucherDelete) m_objectdelete;
					VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
					opvo.pk_vouchers = new String[] { strPk_voucher };
					opvo.pk_user = strOperator;
					m_deleteclassall.beforeDelete(opvo);
					vecdeleteclass.addElement(m_deleteclassall);
				} catch (ClassNotFoundException e) {
				} catch (NoClassDefFoundError e) {
				}
			}

			DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
			copy_deletedVoucherNew(strPk_voucher);//备份
			dmo1.deleteByVoucherPK(strPk_voucher);//删除子表
			dmo.deleteByVoucherPK(strPk_voucher);//删除主表
//			dmo.logic_deleteByVoucherPK(strPk_voucher);
//			dmo1.logic_deleteByVoucherPK(strPk_voucher);
//			dmo3.logic_deleteByVoucherPK(strPk_voucher);
			dmo3.deleteByVoucherPK(strPk_voucher);
			dmo.returnVoucherNoByDelete(vo);

			for (int m = vecdeleteclass.size(); m > 0; m--) {
				VoucherOperateInterfaceVO opvo = new VoucherOperateInterfaceVO();
				opvo.pk_vouchers = new String[] { strPk_voucher };
				opvo.pk_user = strOperator;
				((nc.bs.gl.pubinterface.IVoucherDelete) vecdeleteclass.elementAt(m - 1)).afterDelete(opvo);
			}
		} finally {
			if (bLockSuccess)
				lock.freeKey(strPk_voucher, strOperator, "gl_voucher");
			// try
			// {
			// // lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}
	}


	/**
	 * 根据主键在数据库中删除一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	public OperationResultVO deleteByPks(String[] strPk_voucher, String strOperator) throws BusinessException {
		try {

			// 差异凭证不用回写源凭证的差异标记 modify By liyongru for V55 at 20081103
			// this.updateSrcVoucherDifFlag(strPk_voucher);
			for (int i = 0; i < strPk_voucher.length; i++) {
				deleteByPk(strPk_voucher[i], strOperator, null);
			}

		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return null;
	}

	public VoucherVO queryByPkOnlyAccsubj(String pk_voucher) throws BusinessException {
		VoucherVO voucher = null;
		try {
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			voucher = dmo.queryByVoucherPk(pk_voucher);
			if (voucher == null)
				return null;
			VoucherVO[] vouchers = new VoucherVO[1];
			vouchers[0] = voucher;

			DetailExtendDMO dmo1 = new DetailExtendDMO();
			DetailVO[] details = dmo1.queryByVoucherPk(voucher.getPk_voucher());
			if ((details == null || details.length == 0) && voucher.getErrmessage() == null)

				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000560")/*
																																	 * @res
																																	 * "凭证数据错误！凭证的表体数据为空！"
																																	 */);

			voucher.setDetails(details);

		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return voucher;

	}

	/**
	 * @param indexs
	 * @param strOperator
	 * @return
	 * @throws BusinessException
	 */
	public String[][] deleteByPks(VoucherIndexVO[] indexs, String strOperator) throws BusinessException {
		String pks[][] = new PowerFilterBO().filterAccsubjByPower(indexs, strOperator, this);
		deleteByPks(pks[0], strOperator);
		return pks;

	}

	/**
	 * 根据如果目的帐簿里的凭证被删除了 则对于那些差异帐簿里的来源凭证的差异化标记也应该清空,置为非差异化
	 *
	 * @param des_vouchers
	 * @throws BusinessException
	 */
	private void updateSrcVoucherDifFlag(String[] des_vouchers) throws BusinessException {
		if (des_vouchers == null) {
			return;
		}
		FormulaParseFather fp = new FormulaParse();
		Vector<String> src_vouchers = new Vector<String>();
		String tmp = "";
		String tmp_glorgbook = "";
		if (des_vouchers.length > 0) {
			Hashtable<String, String> map = new Hashtable<String, String>();
			/** 检查是否是差异化 */
			String glorgbook = "srcvoucherpk->getColValue(gl_voucher,pk_glorgbook,pk_voucher,voucherkey)";
			map.put("voucherkey", des_vouchers[0]);
			fp.setExpress(glorgbook);
			fp.setData(map);
			tmp_glorgbook = fp.getValue();
			String isdifflag = "isdifflag->getColValue(gl_soblink,isdifflag,pk_desbook,desbookkey)";
			map.put("desbookkey", tmp_glorgbook);
			fp.setExpress(isdifflag);
			fp.setData(map);
			tmp = fp.getValue();
			if (tmp != null) {
				if (tmp.trim().equals("Y")) {
					String[] pk_vouchers = this.querySrcVouchersBydesVouchers(des_vouchers, tmp_glorgbook);
					if (pk_vouchers != null) {
						this.updateVoucherDifflag(pk_vouchers, new UFBoolean(false));
					}
				}
			}
		}
	}

	/**
	 * 根据目的凭证组去查找折算来源凭证组
	 *
	 * @param des_vouchers
	 * @param glorgbook
	 * @return
	 * @throws BusinessException
	 */
	private String[] querySrcVouchersBydesVouchers(String[] des_vouchers, String glorgbook) throws BusinessException {
		String[] src_vouchers = null;
		try {
			src_vouchers = new VoucherExtendDMO().querySrcVouchersByDesVouchers(des_vouchers, glorgbook);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e.getMessage());
		}
		return src_vouchers;
	}

	/**
	 * 通过主键获得VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO
	 * @param key
	 *            String
	 * @BusinessException BusinessException 异常说明。
	 */
	public VoucherVO findByPrimaryKey(String key) throws BusinessException {

		VoucherVO voucher = null;
		try {
			VoucherDMO dmo = new VoucherDMO();
			voucher = dmo.findByPrimaryKey(key);
			VoucherVO[] vouchers = new VoucherVO[1];
			vouchers[0] = voucher;
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(voucher.getPk_glorgbook()));
			catUsername(vouchers, getUsers(null));

			DetailExtendDMO dmo1 = new DetailExtendDMO();
			DetailVO[] details = dmo1.queryByVoucherPk(voucher.getPk_voucher());
			details = catCurrcode(details, getCurrtypes());
			String[] pk_details = getRecDetailPKsByVoucherPK(new String[] { key });
			details = catDetailMatchingflag(details, pk_details);
			details = catDetailMachForOffer(voucher);
			if (pk_details != null && pk_details.length != 0)
				voucher.setIsmatched(new Boolean(true));

			String[] pk_accsubj = new String[details.length];
			for (int j = 0; j < details.length; j++)
				pk_accsubj[j] = details[j].getPk_accsubj();

			details = catSubjName(details, getAccsubj(pk_accsubj));
			// details = catAppend(details);
			details = catDtlFreevalue(details);
			details = catCheckstylename(details);
			details = catAss(details);
			voucher.setDetails(details);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException("VoucherBean::findByPrimaryKey(VoucherPK) BusinessException!", e);
		}
		return voucher;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-28 19:52:02)
	 *
	 * @return nc.vo.bd.b02.AccsubjVO[]
	 * @param strPk_corp
	 *            java.lang.String
	 */
	private nc.vo.bd.b02.AccsubjVO[] getAccsubj(String[] strPk_accsubj) throws BusinessException {
		if (strPk_accsubj == null || strPk_accsubj.length == 0)
			return null;
		String[] pk_accsubj = null;
		Vector vecaccsubj = new Vector();
		HashMap tempmap = new HashMap();
		for (int i = 0; i < strPk_accsubj.length; i++) {
			if (strPk_accsubj[i] != null && tempmap.get(strPk_accsubj[i]) == null) {
				vecaccsubj.addElement(strPk_accsubj[i]);
				tempmap.put(strPk_accsubj[i], strPk_accsubj[i]);
			}
		}
		pk_accsubj = new String[vecaccsubj.size()];
		vecaccsubj.copyInto(pk_accsubj);
		nc.vo.bd.b02.AccsubjVO[] accsubj = null;
		try {
			// accsubj= new nc.bs.bd.b02.AccsubjBO().queryByPks(pk_accsubj);
			accsubj = new Accsubj().queryByPks(pk_accsubj);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException(e.getMessage());

		}

		return accsubj;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-22 9:24:15)
	 *
	 * @return nc.vo.gl.pubvoucher.UserVO[]
	 */
	private nc.vo.bd.b19.BalatypeVO[] getBalatype(String pk_corp) throws Exception {
		// nc.bs.bd.b19.BalatypeDMO dmo = new nc.bs.bd.b19.BalatypeDMO();
		// nc.vo.bd.b19.BalatypeVO[] tempvos = dmo.queryAll(pk_corp);
		nc.vo.bd.b19.BalatypeVO[] tempvos = Balatype.queryAll(pk_corp);
		return tempvos;
	}

	private SmallCorpVO[] getCorps() throws BusinessException {
		nc.vo.bd.CorpVO[] tempVO = null;
		try {
			tempVO = new nc.bs.bd.CorpBO().queryAll(null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		SmallCorpVO[] corps = new SmallCorpVO[tempVO.length];
		for (int i = 0; i < tempVO.length; i++) {
			corps[i] = new SmallCorpVO();
			corps[i].setPk_corp(tempVO[i].getPk_corp());
			corps[i].setUnitcode(tempVO[i].getUnitcode());
			corps[i].setUnitname(tempVO[i].getUnitname());
			corps[i].setUnitshortname(tempVO[i].getUnitshortname());
		}
		return corps;
	}

	public Integer getCorrectVoucherNo(VoucherVO voucher) throws BusinessException {
		try {
			if (voucher == null)
				return new Integer(-1);
			/*
			 * if (voucher.getPk_voucher() != null &&
			 * voucher.getPk_voucher().trim().length() > 0) { Integer tmp = new
			 * VoucherDMO().getNOByPrimaryKey(voucher.getPk_voucher()); if (tmp !=
			 * null && tmp.intValue() > 0) { return tmp; } }
			 */
			String[] strs = getVoucherNumber(voucher);
			return Integer.valueOf(strs[1]);

		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-16 11:09:31)
	 *
	 * @return nc.vo.bd.b20.CurrtypeVO
	 */
	private nc.vo.bd.b20.CurrtypeVO[] getCurrtypes() throws Exception {
		nc.bs.bd.b20.CurrtypeDMO dmo4 = new nc.bs.bd.b20.CurrtypeDMO();
		nc.vo.bd.b20.CurrtypeVO[] vos = dmo4.queryAll(null);
		return vos;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-11-6 9:24:26)
	 *
	 * @return java.lang.String[]
	 * @param pK_vouchers
	 *            java.lang.String[]
	 */
	private String[] getRecDetailPKs(String[] pk_details) throws Exception {
		DealclassDMO dealclassdmo = new DealclassDMO();
		DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("checkrecon");
		Vector veccheckreconclass = new Vector();
		if (dealclassvos != null && dealclassvos.length != 0)
			for (int m = 0; m < dealclassvos.length; m++) {
				if (dealclassvos[m].getModules() != null) {
					try {
						nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall = (nc.bs.gl.pubinterface.ICheckReconcile) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
								.getClassname());
						veccheckreconclass.addElement(m_checkreconclassall);
					} catch (FrameworkRuntimeException e) {
						// TODO: handle exception
						log.error(e);
					}
				}
				/*
				 * try { Class m_classcheckrecon =
				 * java.lang.Class.forName(dealclassvos[m].getClassname());
				 * Object m_objectcheckrecon = m_classcheckrecon.newInstance();
				 * nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall =
				 * (nc.bs.gl.pubinterface.ICheckReconcile) m_objectcheckrecon;
				 * veccheckreconclass.addElement(m_checkreconclassall); } catch
				 * (ClassNotFoundException e) { } catch (NoClassDefFoundError e) { }
				 */
			}
		Vector vecPk_details = new Vector();
		for (int i = 0; i < veccheckreconclass.size(); i++) {
			String[] tmp_pk_details = ((nc.bs.gl.pubinterface.ICheckReconcile) veccheckreconclass.elementAt(i)).getRecDetailPKs(pk_details);
			if (tmp_pk_details != null) {
				for (int j = 0; j < tmp_pk_details.length; j++) {
					vecPk_details.addElement(tmp_pk_details[j]);
				}
			}
		}
		String[] rtPk_details = null;
		if (vecPk_details.size() > 0) {
			rtPk_details = new String[vecPk_details.size()];
			vecPk_details.copyInto(rtPk_details);
		}
		return rtPk_details;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-11-6 9:24:26)
	 *
	 * @return java.lang.String[]
	 * @param pK_vouchers
	 *            java.lang.String[]
	 */
	private java.lang.String[] getRecDetailPKsByVoucherPK(java.lang.String[] pk_vouchers) throws Exception {
		DealclassDMO dealclassdmo = new DealclassDMO();
		DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("checkrecon");
		Vector veccheckreconclass = new Vector();
		if (dealclassvos != null && dealclassvos.length != 0)
			for (int m = 0; m < dealclassvos.length; m++) {
				if (dealclassvos[m].getModules() != null) {
					try {
						nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall = (nc.bs.gl.pubinterface.ICheckReconcile) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
								.getClassname());
						veccheckreconclass.addElement(m_checkreconclassall);
					} catch (FrameworkRuntimeException e) {
						// TODO: handle exception
						log.error(e);
					}
				}
				/*
				 * try { Class m_classcheckrecon =
				 * java.lang.Class.forName(dealclassvos[m].getClassname());
				 * Object m_objectcheckrecon = m_classcheckrecon.newInstance();
				 * nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall =
				 * (nc.bs.gl.pubinterface.ICheckReconcile) m_objectcheckrecon;
				 * veccheckreconclass.addElement(m_checkreconclassall); } catch
				 * (ClassNotFoundException e) { } catch (NoClassDefFoundError e) { }
				 */
			}
		Vector vecPk_details = new Vector();
		for (int i = 0; i < veccheckreconclass.size(); i++) {
			String[] tmp_pk_details = ((nc.bs.gl.pubinterface.ICheckReconcile) veccheckreconclass.elementAt(i)).getRecDetailPKsByVoucherPK(pk_vouchers);
			if (tmp_pk_details != null) {
				for (int j = 0; j < tmp_pk_details.length; j++) {
					vecPk_details.addElement(tmp_pk_details[j]);
				}
			}
		}
		String[] rtPk_details = null;
		if (vecPk_details.size() > 0) {
			rtPk_details = new String[vecPk_details.size()];
			vecPk_details.copyInto(rtPk_details);
		}
		return rtPk_details;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-11-6 9:24:26)
	 *
	 * @return java.lang.String[]
	 * @param pK_vouchers
	 *            java.lang.String[]
	 */
	private java.lang.String[] getRecVoucherPKs(java.lang.String[] pk_vouchers) throws java.lang.Exception {
		DealclassDMO dealclassdmo = new DealclassDMO();
		DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("checkrecon");
		Vector veccheckreconclass = new Vector();
		if (dealclassvos != null && dealclassvos.length != 0)
			for (int m = 0; m < dealclassvos.length; m++) {
				if (dealclassvos[m].getModules() != null) {
					try {
						nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall = (nc.bs.gl.pubinterface.ICheckReconcile) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
								.getClassname());
						veccheckreconclass.addElement(m_checkreconclassall);
					} catch (FrameworkRuntimeException e) {
						// TODO: handle exception
						log.error(e);
					} catch (NullPointerException e) {
						log.error(e);
					}
				}
				/*
				 * try { Class m_classcheckrecon =
				 * java.lang.Class.forName(dealclassvos[m].getClassname());
				 * Object m_objectcheckrecon = m_classcheckrecon.newInstance();
				 * nc.bs.gl.pubinterface.ICheckReconcile m_checkreconclassall =
				 * (nc.bs.gl.pubinterface.ICheckReconcile) m_objectcheckrecon;
				 * veccheckreconclass.addElement(m_checkreconclassall); } catch
				 * (ClassNotFoundException e) { } catch (NoClassDefFoundError e) { }
				 */
			}
		Vector vecPk_details = new Vector();
		for (int i = 0; i < veccheckreconclass.size(); i++) {
			String[] tmp_pk_details = ((nc.bs.gl.pubinterface.ICheckReconcile) veccheckreconclass.elementAt(i)).getRecVoucherPKs(pk_vouchers);
			if (tmp_pk_details != null) {
				for (int j = 0; j < tmp_pk_details.length; j++) {
					vecPk_details.addElement(tmp_pk_details[j]);
				}
			}
		}
		String[] rtPk_details = null;
		if (vecPk_details.size() > 0) {
			rtPk_details = new String[vecPk_details.size()];
			vecPk_details.copyInto(rtPk_details);
		}
		return rtPk_details;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-22 9:24:15)
	 *
	 * @return nc.vo.gl.pubvoucher.UserVO[]
	 */
	private UserVO[] getUsers(String pk_corp) throws BusinessException {
		// nc.bs.gl.pubvoucher.UserDMO userDMO = new
		// nc.bs.gl.pubvoucher.UserDMO();
		// UserVO[] users = userDMO.queryAll(null);
		return null;// users;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-22 9:18:32)
	 *
	 * @return nc.vo.bd.b18.VouchertypeVO[]
	 * @param pk_corp
	 *            java.lang.String
	 */
	private VouchertypeVO[] getVouchertypes(String pk_orgbook) throws Exception {
		VouchertypeBO typeBO = new VouchertypeBO();
		VouchertypeVO[] type = null;
		if (pk_orgbook == null)
			type = typeBO.queryAllByGlorgbookSimple(null);
		else
			type = typeBO.queryAllByGlorgbook(pk_orgbook);
		return type;
	}

	/**
	 * 向数据库中插入一个VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param voucher
	 *            nc.vo.gl.voucher.VoucherVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @BusinessException BusinessException 异常说明。
	 */
	private VoucherVO insert(VoucherVO voucher) throws Exception {

		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			// if (home == null)
			// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
			/*
			 * lock = new GLKeyLock(); for (int i = 0; i < 10; i++) {
			 * bLockSuccess = lock.lockKey(voucher.getPk_glorgbook() +
			 * voucher.getPk_vouchertype() + voucher.getYear() +
			 * voucher.getPeriod(), voucher.getPk_prepared(), "gl_voucher"); if
			 * (bLockSuccess) break; try { Thread.sleep(100); } catch
			 * (InterruptedException e) { } } if (!bLockSuccess) throw new
			 * GLVoucherNumberException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005",
			 * "UPP20021005-000539") @res "有其他用户在操作，请稍候再试。" );
			 */
			try {
				if (voucher.getNo() == null || voucher.getNo().intValue() <= 0) {
					getVoucherNumberForSave(voucher);
				}

				else {
					//if (voucher.getPk_voucher() != null) {
						try {
							updateVoucherNumber(voucher);
						} catch (VoucherNoDuplicateException e) {
							throw new BusinessException(new VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
						} catch (Exception e) {
							throw e;
						}
					//}

				}

			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}

			// 凭证主表
			VoucherDMO dmo = new VoucherDMO();
			String pk = "";
			pk = dmo.insert(voucher);
			voucher.setPk_voucher(pk);

		} finally {
			/*
			 * if (bLockSuccess) lock.freeKey(voucher.getPk_glorgbook() +
			 * voucher.getPk_vouchertype() + voucher.getYear() +
			 * voucher.getPeriod(), voucher.getPk_prepared(), "gl_voucher"); //
			 * try
			 */
			// {
			// lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}
		if (voucher.getFreevalue1() != null || voucher.getFreevalue2() != null || voucher.getFreevalue3() != null || voucher.getFreevalue4() != null || voucher.getFreevalue5() != null) {
			VchfreevalueVO vf = new VchfreevalueVO();
			vf.setFreevalue1(voucher.getFreevalue1());
			vf.setFreevalue2(voucher.getFreevalue2());
			vf.setFreevalue3(voucher.getFreevalue3());
			vf.setFreevalue4(voucher.getFreevalue4());
			vf.setFreevalue5(voucher.getFreevalue5());
			vf.setPk_voucher(voucher.getPk_voucher());
			new VchfreevalueDMO().insert(vf);
		}

		// 凭证子表——分录
		DetailDMO dmo1 = new DetailDMO();

		DetailVO[] details = voucher.getDetails();
		for (int i = 0; i < details.length; i++) {
			details[i].setPk_voucher(voucher.getPk_voucher());
		}
		if (details != null && details.length > 0) {
			String[] pks = dmo1.insertArray(details);

			Vector tempfreevalue = new Vector();
			for (int i = 0; i < details.length; i++) {
				details[i].setPk_detail(pks[i]);
				if (details[i].getFreevalue1() != null || details[i].getFreevalue2() != null || details[i].getFreevalue3() != null || details[i].getFreevalue4() != null
						|| details[i].getFreevalue5() != null || details[i].getFreevalue6() != null || details[i].getFreevalue7() != null || details[i].getFreevalue8() != null
						|| details[i].getFreevalue9() != null || details[i].getFreevalue10() != null || details[i].getFreevalue11() != null || details[i].getFreevalue12() != null
						|| details[i].getFreevalue13() != null || details[i].getFreevalue14() != null || details[i].getFreevalue15() != null || details[i].getFreevalue16() != null
						|| details[i].getFreevalue17() != null || details[i].getFreevalue18() != null || details[i].getFreevalue19() != null || details[i].getFreevalue20() != null
						|| details[i].getFreevalue21() != null || details[i].getFreevalue22() != null || details[i].getFreevalue23() != null || details[i].getFreevalue24() != null
						|| details[i].getFreevalue25() != null || details[i].getFreevalue26() != null || details[i].getFreevalue27() != null || details[i].getFreevalue28() != null
						|| details[i].getFreevalue29() != null || details[i].getFreevalue30() != null) {
					DtlfreevalueVO tempfree = new DtlfreevalueVO();
					tempfree.setPk_detail(details[i].getPk_detail());
					tempfree.setFreevalue1(details[i].getFreevalue1());
					tempfree.setFreevalue2(details[i].getFreevalue2());
					tempfree.setFreevalue3(details[i].getFreevalue3());
					tempfree.setFreevalue4(details[i].getFreevalue4());
					tempfree.setFreevalue5(details[i].getFreevalue5());
					tempfree.setFreevalue6(details[i].getFreevalue6());
					tempfree.setFreevalue7(details[i].getFreevalue7());
					tempfree.setFreevalue8(details[i].getFreevalue8());
					tempfree.setFreevalue9(details[i].getFreevalue9());
					tempfree.setFreevalue10(details[i].getFreevalue10());
					tempfree.setFreevalue11(details[i].getFreevalue11());
					tempfree.setFreevalue12(details[i].getFreevalue12());
					tempfree.setFreevalue13(details[i].getFreevalue13());
					tempfree.setFreevalue14(details[i].getFreevalue14());
					tempfree.setFreevalue15(details[i].getFreevalue15());
					tempfree.setFreevalue16(details[i].getFreevalue16());
					tempfree.setFreevalue17(details[i].getFreevalue17());
					tempfree.setFreevalue18(details[i].getFreevalue18());
					tempfree.setFreevalue19(details[i].getFreevalue19());
					tempfree.setFreevalue20(details[i].getFreevalue20());
					tempfree.setFreevalue21(details[i].getFreevalue21());
					tempfree.setFreevalue22(details[i].getFreevalue22());
					tempfree.setFreevalue23(details[i].getFreevalue23());
					tempfree.setFreevalue24(details[i].getFreevalue24());
					tempfree.setFreevalue25(details[i].getFreevalue25());
					tempfree.setFreevalue26(details[i].getFreevalue26());
					tempfree.setFreevalue27(details[i].getFreevalue27());
					tempfree.setFreevalue28(details[i].getFreevalue28());
					tempfree.setFreevalue29(details[i].getFreevalue29());
					tempfree.setFreevalue30(details[i].getFreevalue30());
					tempfreevalue.addElement(tempfree);
				}
			}
			DtlfreevalueVO[] detailfreevalues = new DtlfreevalueVO[tempfreevalue.size()];
			tempfreevalue.copyInto(detailfreevalues);
			if (detailfreevalues.length > 0) {
				DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
				dmo3.insertArray(detailfreevalues);
			}

			Vector subjfreevalue = new Vector();
			for (int i = 0; i < details.length; i++) {
				if (details[i].getSubjfreevalue() == null)
					continue;
				SubjfreevalueVO sf = details[i].getSubjfreevalue();
				if (sf.getSubjfreevalue1() != null || sf.getSubjfreevalue2() != null || sf.getSubjfreevalue3() != null || sf.getSubjfreevalue4() != null || sf.getSubjfreevalue5() != null
						|| sf.getSubjfreevalue6() != null || sf.getSubjfreevalue7() != null || sf.getSubjfreevalue8() != null || sf.getSubjfreevalue9() != null || sf.getSubjfreevalue10() != null
						|| sf.getSubjfreevalue11() != null || sf.getSubjfreevalue12() != null || sf.getSubjfreevalue13() != null || sf.getSubjfreevalue14() != null || sf.getSubjfreevalue15() != null
						|| sf.getSubjfreevalue16() != null || sf.getSubjfreevalue17() != null || sf.getSubjfreevalue18() != null || sf.getSubjfreevalue19() != null || sf.getSubjfreevalue20() != null
						|| sf.getSubjfreevalue21() != null || sf.getSubjfreevalue22() != null || sf.getSubjfreevalue23() != null || sf.getSubjfreevalue24() != null || sf.getSubjfreevalue25() != null
						|| sf.getSubjfreevalue26() != null || sf.getSubjfreevalue27() != null || sf.getSubjfreevalue28() != null || sf.getSubjfreevalue29() != null || sf.getSubjfreevalue30() != null) {
					sf.setPk_accsubj(details[i].getPk_accsubj());
					sf.setPk_corp(details[i].getPk_corp());
					sf.setPk_detail(details[i].getPk_detail());
					subjfreevalue.addElement(sf);
				}
			}
			SubjfreevalueVO[] subjfreevalues = new SubjfreevalueVO[subjfreevalue.size()];
			subjfreevalue.copyInto(subjfreevalues);
			if (subjfreevalues.length > 0) {
				SubjfreevalueDMO dmo4 = new SubjfreevalueDMO();
				dmo4.insertArray(subjfreevalues);
			}

			voucher.setDetails(details);
		}

		return voucher;
	}

	/**
	 * 向数据库中插入一批VO对象。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @param voucher
	 *            nc.vo.gl.voucher.VoucherVO[]
	 * @return java.lang.String[] 所插入VO对象数组的主键字符串数组。
	 * @BusinessException BusinessException 异常说明。
	 */
	private String[] insertArray(VoucherVO[] vouchers) throws Exception {

		VoucherDMO dmo = new VoucherDMO();
		String[] keys = dmo.insertArray(vouchers);
		return keys;
	}

	public Boolean isExistRegulationVoucher(String pk_glorgbook, String year, String period) throws BusinessException {
		try {
			return new Boolean(new VoucherExtendDMO().isExistRegulationVoucher(pk_glorgbook, year, period));
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO[] 查到的VO对象数组
	 * @param unitCode
	 *            int
	 * @BusinessException BusinessException 异常说明。
	 */
	public VoucherVO[] queryAll(String pk_corp) throws BusinessException {

		VoucherVO[] vouchers = null;
		try {
			VoucherDMO dmo = new VoucherDMO();
			vouchers = null;// dmo.queryAll(pk_corp);

			if (vouchers == null)
				return null;
			String[] pks = new String[vouchers.length];
			for (int i = 0; i < vouchers.length; i++) {
				pks[i] = vouchers[i].getPk_voucher();
			}
			String[] contrasted = getRecVoucherPKs(pks);
			catVoucherMatchingflag(vouchers, contrasted);
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(pk_corp));
			catVoucherFreeValue(vouchers);
			catUsername(vouchers, getUsers(null));
			catVerify(vouchers);
			// nc.vo.bd.b20.CurrtypeVO[] vos = getCurrtypes();
			// for (int i = 0; i < vouchers.length; i++) {
			// DetailDMO dmo1 = new DetailDMO();
			// DetailVO detail = new DetailVO();
			// detail.setPk_voucher(vouchers[i].getPk_voucher());
			// detail.setPk_corp(vouchers[i].getPk_corp());
			// DetailVO[] details = dmo1.queryByVO(detail, new Boolean(true));
			// details = catCurrcode(details, vos);
			// vouchers[i].setDetails(details);
			// }
			sort(vouchers);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return vouchers;
	}

	/**
	 * 根据VO中所设定的条件返回所有符合条件的VO数组
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO[]
	 * @param voucherVO
	 *            nc.vo.gl.voucher.VoucherVO
	 * @param boolean
	 *            带分录VO还是不带分录VO
	 * @BusinessException java.sql.SQLBusinessException 异常说明。
	 */
	public VoucherVO[] queryByConditionVO(nc.vo.gl.voucherquery.VoucherQueryConditionVO[] condVoucherVO, Boolean isIncludeDetails) throws BusinessException {

		VoucherVO[] vouchers = null;
		try {
			VoucherVO[] tmpvouchers = null;
			Vector vecVouchers = new Vector();

			VoucherExtendDMO dmo = new VoucherExtendDMO();
			for (int i = 0; i < condVoucherVO.length; i++) {
				tmpvouchers = dmo.queryByConditionVO(condVoucherVO[i]);
				if (tmpvouchers != null) {
					for (int j = 0; j < tmpvouchers.length; j++) {
						vecVouchers.addElement(tmpvouchers[j]);
					}
				}
			}
			if (vecVouchers == null || vecVouchers.size() == 0)
				return null;
			vouchers = new VoucherVO[vecVouchers.size()];
			vecVouchers.copyInto(vouchers);
			String[] pks = new String[vouchers.length];
			Boolean issequencequery = new Boolean(false);
			for (int i = 0; i < vouchers.length; i++) {
				pks[i] = vouchers[i].getPk_voucher();
			}
			for (int i = 0; i < condVoucherVO.length; i++) {
				if (condVoucherVO[i].getIsequencetimequery().booleanValue()) {
					issequencequery = new Boolean(true);
					break;
				}
			}
			if (!issequencequery.booleanValue()) {
				// String[] contrasted = getRecVoucherPKs(pks);
				// catVoucherMatchingflag(vouchers, contrasted);
				catVoucherFreeValue(vouchers);
			}
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(null));
			catVoucherUserName(vouchers);
//			catVerify(vouchers);

			// Log.getInstance(this.getClass()).debug("@@@@@@in voucher main
			// query over");
			if (isIncludeDetails.booleanValue()) {
				// Log.getInstance(this.getClass()).debug("@@@@@@in detail query
				// logic");
				Vector vecDetails = new Vector();
				DetailExtendDMO dmo1 = new DetailExtendDMO();
				for (int i = 0; i < condVoucherVO.length; i++) {
					DetailVO[] tmpdetails = dmo1.queryByConditionVO(condVoucherVO[i]);
					// Log.getInstance(this.getClass()).debug("@@@@@@in detail
					// query logic condi " + i);
					if (tmpdetails != null) {
						for (int j = 0; j < tmpdetails.length; j++) {
							vecDetails.addElement(tmpdetails[j]);
						}
					}
				}
				DetailVO[] details = new DetailVO[vecDetails.size()];
				vecDetails.copyInto(details);
				if (!issequencequery.booleanValue()) {
					String[] pk_details = getRecDetailPKsByVoucherPK(pks);
					details = catDetailMatchingflag(details, pk_details);
				}
				details = catCurrcode(details, getCurrtypes());

				String[] pk_accsubj = new String[details.length];
				for (int j = 0; j < details.length; j++)
					pk_accsubj[j] = details[j].getPk_accsubj();

				details = catSubjName(details, getAccsubj(pk_accsubj));
				// details = catAppend(details);
				if (!issequencequery.booleanValue()) {
					details = catDtlFreevalue(details);
					details = catCashFlows(details);
				}
				details = catSubjfreevalue(details);
				details = catCheckstylename(details);
				details = catAss(details);
				catVerify(details);
				catDetails(vouchers, details);
			}
			catDetailMatchingFlagForOffer(vouchers);
			// catUsername(vouchers, getUsers(null));
			sort(vouchers);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return vouchers;
	}

	private void catVerify(DetailVO[] details) {
		List<String> pks = new ArrayList<String>();
		for (int i = 0; i < details.length; i++) {
			pks.add(details[i].getPk_detail());
		}
		VoucherExtendDMO dmo = null;
		Map<String, Boolean> statusMap = null;
		try {
			dmo = new VoucherExtendDMO();
			statusMap = dmo.isVerifyDetail(pks);
		} catch (Exception e) {
			Logger.error("查询核销标志错误:"+e.getMessage());
			e.printStackTrace();
		}
		for (int i = 0; i < details.length; i++) {
			details[i].setVerify(statusMap.get(details[i].getPk_detail()));
		}
	}

	private void catVerify(VoucherVO[] vouchers) throws BusinessException{
		List<String> pks = new ArrayList<String>();
		for (int i = 0; i < vouchers.length; i++) {
			pks.add(vouchers[i].getPk_voucher());
		}
		VoucherExtendDMO dmo = null;
		Map<String, Boolean> statusMap = null;
		try {
			dmo = new VoucherExtendDMO();
			statusMap = dmo.isVerifyVoucher(pks);
		} catch (Exception e) {
			Logger.error("查询核销标志错误:"+e.getMessage());
			e.printStackTrace();
		}
		for (int i = 0; i < vouchers.length; i++) {
			vouchers[i].setVerify(statusMap.get(vouchers[i].getPk_voucher()));
		}
	}

	private void catVoucherUserName(VoucherVO[] vouchers) {

		Map<String,String> userpks = new HashMap<String,String>();
		for(VoucherVO vo:vouchers){
			userpks.put(vo.getPk_prepared(), vo.getPk_prepared());
			if (vo.getPk_checked() != null) {
				userpks.put(vo.getPk_checked(), vo.getPk_checked());
			}
			if (vo.getPk_casher() != null) {
				userpks.put(vo.getPk_casher(), vo.getPk_casher());
			}
			if (vo.getPk_manager() != null) {
				userpks.put(vo.getPk_manager(), vo.getPk_manager());
			}
		}
		String[] usedUserPks = userpks.keySet().toArray(new String[]{});
		IUserManageQuery qry = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
		nc.vo.sm.UserVO[] userVOs = null;
		try {
			userVOs = qry.findNamesByPrimaryKeys(usedUserPks);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(nc.vo.sm.UserVO uservo:userVOs){
			//Modified 2010/09/29 hurh
			if(uservo == null)
				continue;

			String username=userpks.get(uservo.getPrimaryKey());
			if(username!=null){
				userpks.put(uservo.getPrimaryKey(), uservo.getUserName());
			}
		}
		for(VoucherVO vo:vouchers){
			vo.setPreparedname(userpks.get(vo.getPk_prepared()));
			if (vo.getPk_checked() != null) {
				vo.setCheckedname(userpks.get(vo.getPk_checked()));
			}
			if (vo.getPk_casher() != null) {
				vo.setCashername(userpks.get(vo.getPk_casher()));
			}
			if (vo.getPk_manager() != null) {
				vo.setManagername(userpks.get(vo.getPk_manager()));
			}
		}


	}

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO[] 查到的VO对象数组
	 * @param unitCode
	 *            int
	 * @BusinessException BusinessException 异常说明。
	 */
	public VoucherVO queryByPk(String pk_voucher) throws BusinessException {

		VoucherVO voucher = null;
		try {
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			voucher = dmo.queryByVoucherPk(pk_voucher);
			if (voucher == null)
				return null;
			VoucherVO[] vouchers = new VoucherVO[1];
			vouchers[0] = voucher;
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(voucher.getPk_glorgbook()));
			catVoucherFreeValue(vouchers);
			catUsername(vouchers, getUsers(null));
			catVerify(vouchers);

			DetailExtendDMO dmo1 = new DetailExtendDMO();
			DetailVO[] details = dmo1.queryByVoucherPk(voucher.getPk_voucher());
			if ((details == null || details.length == 0) && voucher.getErrmessage() == null)

				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000560")/*
																																	 * @res
																																	 * "凭证数据错误！凭证的表体数据为空！"
																																	 *
																																	 */);
			if (details != null && details.length > 0) {
				details = catCurrcode(details, getCurrtypes());
				String[] pk_details = getRecDetailPKsByVoucherPK(new String[] { pk_voucher });
				details = catDetailMatchingflag(details, pk_details);

				// if (pk_details != null && pk_details.length != 0)
				// voucher.setIsmatched(new Boolean(true));

				String[] pk_accsubj = new String[details.length];
				for (int j = 0; j < details.length; j++)
					pk_accsubj[j] = details[j].getPk_accsubj();

				details = catSubjName(details, getAccsubj(pk_accsubj));
				// details = catAppend(details);
				details = catDtlFreevalue(details);
				details = catSubjfreevalue(details);
				details = catCashFlows(details);
				details = catCheckstylename(details);
				details = catAss(details);
				catVerify(details);
				voucher.setDetails(details);
				catDetailMatchingFlagForOffer(vouchers);
			}
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return voucher;
	}

	/**
	 * 根据VO中所设定的条件返回所有符合条件的VO数组
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO[]
	 * @param voucherVO
	 *            nc.vo.gl.voucher.VoucherVO
	 * @param isAnd
	 *            boolean 以与条件查询还是以或条件查询
	 * @BusinessException java.sql.SQLBusinessException 异常说明。
	 */
	public VoucherVO[] queryByPks(String[] strPks) throws BusinessException {

		VoucherVO[] vouchers = null;
		try {
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			vouchers = dmo.queryByPks(strPks);
			if (vouchers == null)
				return null;
			String[] pks = new String[vouchers.length];
			for (int i = 0; i < vouchers.length; i++) {
				pks[i] = vouchers[i].getPk_voucher();
			}
//			String[] contrasted = getRecVoucherPKs(pks);
//			catVoucherMatchingflag(vouchers, contrasted);
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(vouchers[0].getPk_glorgbook()));
			catVoucherFreeValue(vouchers);
			catUsername(vouchers, getUsers(null));
			catDetailMatchingFlagForOffer(vouchers);
			catVerify(vouchers);

			DetailExtendDMO dmo1 = new DetailExtendDMO();
			DetailVO[] details = dmo1.queryByVoucherPks(strPks);
			details = catCurrcode(details, getCurrtypes());
			if(null != details){
				String[] pk_accsubj = new String[details.length];
				for (int j = 0;j < details.length; j++)
					pk_accsubj[j] = details[j].getPk_accsubj();
				String[] pk_details = getRecDetailPKsByVoucherPK(pks);
				details = catDetailMatchingflag(details, pk_details);
				details = catSubjName(details, getAccsubj(pk_accsubj));
				// details = catAppend(details);
				details = catDtlFreevalue(details);
				details = catSubjfreevalue(details);
				details = catCashFlows(details);
				details = catCheckstylename(details);
				details = catAss(details);
				catVerify(details);
				catDetails(vouchers, details);
			}
			sort(vouchers);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return vouchers;
	}

	/**
	 * 根据VO中所设定的条件返回所有符合条件的VO数组
	 *
	 * 创建日期：(2001-9-19)
	 *
	 * @return nc.vo.gl.voucher.VoucherVO[]
	 * @param voucherVO
	 *            nc.vo.gl.voucher.VoucherVO
	 * @param isAnd
	 *            boolean 以与条件查询还是以或条件查询
	 * @BusinessException java.sql.SQLBusinessException 异常说明。
	 */
	public VoucherVO[] queryByVO(VoucherVO condVoucherVO, Boolean isAnd) throws BusinessException {

		VoucherVO[] vouchers = null;
		try {
			VoucherDMO dmo = new VoucherDMO();
			vouchers = dmo.queryByVO(condVoucherVO, isAnd);
			if (vouchers == null)
				return null;
			String[] pks = new String[vouchers.length];
			for (int i = 0; i < vouchers.length; i++) {
				pks[i] = vouchers[i].getPk_voucher();
			}
			String[] contrasted = getRecVoucherPKs(pks);
			catVoucherMatchingflag(vouchers, contrasted);
			catCorpname(vouchers, getCorps());
			catSystemname(vouchers);
			catVouchertypename(vouchers, getVouchertypes(condVoucherVO.getPk_glorgbook()));
			catVoucherFreeValue(vouchers);
			catUsername(vouchers, getUsers(null));
			catVerify(vouchers);
			sort(vouchers);
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return vouchers;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 17:54:14)
	 *
	 * @return nc.vo.gl.pubvoucher.OperationResultVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 * @param isneedcheck
	 *            TODO
	 */
	private OperationResultVO[] saveVoucher(VoucherVO voucher, boolean isneedcheck) throws Exception {
		String verifyclass = "nc.bs.gl.verifysyn.InsertBalance";
		String tbClass = "nc.bs.gl.bugetinterface.BugetInterface";
		// String cfClass = "nc.bs.gl.cashflowcaseio.CashFlowCaseIODMO";

		if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() != 2 && voucher.getVoucherkind().intValue() != 1)
			try {
				OperationResultVO[] result = new VoucherCheckBO().checkPreparedDate(voucher);
				if (result != null) {
					StringBuffer strMsg = new StringBuffer();
					boolean errflag = false;
					for (int i = 0; i < result.length; i++) {
						switch (result[i].m_intSuccess) {
						case 0:
							break;
						case 1:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																	 * @res
																																	 * "警告:"
																																	 */+ result[i].m_strDescription + "\n");
							break;
						case 2:
							errflag = true;
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																	 * @res
																																	 * "错误:"
																																	 */+ result[i].m_strDescription + "\n");
							break;
						default:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																	 * @res
																																	 * "信息:"
																																	 */+ result[i].m_strDescription + "\n");
						}
					}
					if (strMsg.length() > 0 && errflag)
						throw new BusinessException(strMsg.toString());
				}
			} catch (BusinessException e) {
				throw e;
			}
		long tb = System.currentTimeMillis();
		OperationResultVO[] result = null;
		// Vector vecresult = new Vector();

		// $$$$insert voucher$$$$
		appendDetailHead(voucher);

		DealclassDMO dealclassdmo = new DealclassDMO();
		DealclassVO[] dealclassvos = dealclassdmo.queryByModulgroup("save");

		Vector vecsaveclass = new Vector();
		if (dealclassvos != null && dealclassvos.length != 0)
			for (int m = 0; m < dealclassvos.length; m++) {
				if (dealclassvos[m].getModules() != null) {
					try {
						if (isneedcheck) {
							nc.bs.gl.pubinterface.IVoucherSave m_saveclass = (nc.bs.gl.pubinterface.IVoucherSave) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
									.getClassname());
							vecsaveclass.addElement(m_saveclass);
						} else {
							if (dealclassvos[m].getClassname().equals(verifyclass)) {
								nc.bs.gl.pubinterface.IVoucherSave m_saveclass = (nc.bs.gl.pubinterface.IVoucherSave) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
										.getClassname());
								vecsaveclass.addElement(m_saveclass);
							}
							if (dealclassvos[m].getClassname().equals(tbClass)) {
								nc.bs.gl.pubinterface.IVoucherSave m_saveclass = (nc.bs.gl.pubinterface.IVoucherSave) NewObjectService.newInstance(dealclassvos[m].getModules(), dealclassvos[m]
										.getClassname());
								vecsaveclass.addElement(m_saveclass);
							}
							// if
							// (dealclassvos[m].getClassname().equals(cfClass))
							// {
							// nc.bs.gl.pubinterface.IVoucherSave m_saveclass =
							// (nc.bs.gl.pubinterface.IVoucherSave)
							// NewObjectService.newInstance(dealclassvos[m].getModules(),
							// dealclassvos[m]
							// .getClassname());
							// vecsaveclass.addElement(m_saveclass);
							// }

						}
					} catch (FrameworkRuntimeException e) {
						// TODO: handle exception
						log.error(e);
					} catch (NullPointerException e) {
						// TODO: handle exception
						log.error(e);
					}

				}
			}
		// add 接口没有进行class.forname调整???
		if (voucher.getAddclass() != null && !voucher.getAddclass().equals("")) {
			try {
				Class m_class1 = java.lang.Class.forName(voucher.getAddclass());
				Object m_object1 = m_class1.newInstance();
				nc.bs.gl.pubinterface.IVoucherSave m_saveclass = (nc.bs.gl.pubinterface.IVoucherSave) m_object1;
				if (isneedcheck) {
					vecsaveclass.addElement(m_saveclass);
				}
			} catch (ClassNotFoundException e) {
			} catch (NoClassDefFoundError e) {
			}
		}
		if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
			Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::findAllInterface time:" + (System.currentTimeMillis() - tb) + "ms");

		for (int i = 0; i < vecsaveclass.size(); i++) {
			VoucherSaveInterfaceVO iSaveVO = new VoucherSaveInterfaceVO();
			iSaveVO.pk_user = voucher.getPk_prepared();
			iSaveVO.voucher = voucher;
			iSaveVO.userdata = voucher.getUserData();
			long t = System.currentTimeMillis();
			OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherSave) vecsaveclass.elementAt(i)).beforeSave(iSaveVO);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecsaveclass.elementAt(i).getClass() + ".BeforeSave time:" + (System.currentTimeMillis() - t) + "ms");
			result = OperationResultVO.appendResultVO(result, t_result1);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecsaveclass.elementAt(i).getClass() + ".BeforeSave times:" + (System.currentTimeMillis() - t) + "ms");
		}

		if (voucher.getPk_voucher() == null) {
			DealclassVO[] dealclassvos1 = dealclassdmo.queryByModulgroup("add");

			Vector vecaddclass = new Vector();
			Vector vecofferclass = new Vector();
			if (dealclassvos1 != null && dealclassvos1.length != 0)
				for (int m = 0; m < dealclassvos1.length; m++) {
					if (dealclassvos1[m].getModules() != null) {
						try {
							nc.bs.gl.pubinterface.IVoucherSave m_addclassall = (nc.bs.gl.pubinterface.IVoucherSave) NewObjectService.newInstance(dealclassvos1[m].getModules(), dealclassvos1[m]
									.getClassname());
							if (isneedcheck) {
								vecaddclass.addElement(m_addclassall);
							}
						} catch (FrameworkRuntimeException e) {
							// TODO: handle exception
							log.error(e);
						}
					}
					/*
					 * try { Class m_classadd =
					 * java.lang.Class.forName(dealclassvos1[m].getClassname());
					 * Object m_objectadd = m_classadd.newInstance();
					 * nc.bs.gl.pubinterface.IVoucherSave m_addclassall =
					 * (nc.bs.gl.pubinterface.IVoucherSave) m_objectadd;
					 * vecaddclass.addElement(m_addclassall); } catch
					 * (ClassNotFoundException e) { } catch
					 * (NoClassDefFoundError e) { }
					 */
				}
			for (int i = 0; i < vecaddclass.size(); i++) {
				VoucherSaveInterfaceVO iSaveVO = new VoucherSaveInterfaceVO();
				iSaveVO.pk_user = voucher.getPk_prepared();
				iSaveVO.voucher = voucher;
				iSaveVO.userdata = voucher.getUserData();
				long t = System.currentTimeMillis();
				OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherSave) vecaddclass.elementAt(i)).beforeSave(iSaveVO);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave time:" + (System.currentTimeMillis() - t) + "ms");
				result = OperationResultVO.appendResultVO(result, t_result1);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave times:" + (System.currentTimeMillis() - t) + "ms");
			}
			// 冲销接口
			if (voucher.getOffervoucher() != null) {
				DealclassVO[] offerclassvos = dealclassdmo.queryByModulgroup("offer");
				if (offerclassvos != null && offerclassvos.length != 0)
					for (int m = 0; m < offerclassvos.length; m++) {
						if (offerclassvos[m].getModules() != null) {
							try {
								nc.bs.gl.pubinterface.IVoucherSave m_addclassall = (nc.bs.gl.pubinterface.IVoucherSave) NewObjectService.newInstance(dealclassvos1[m].getModules(), dealclassvos1[m]
										.getClassname());
								if (isneedcheck) {
									vecofferclass.addElement(m_addclassall);
								}
							} catch (FrameworkRuntimeException e) {
								// TODO: handle exception
								log.error(e);
							}
						}

					}
			}
			for (int i = 0; i < vecofferclass.size(); i++) {
				VoucherOperateInterfaceVO iSaveVO = new VoucherOperateInterfaceVO();
				iSaveVO.pk_user = voucher.getPk_prepared();
				iSaveVO.pk_vouchers = new String[] { voucher.getOffervoucher() };
				iSaveVO.userdata = voucher.getUserData();
				long t = System.currentTimeMillis();
				OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherOffer) vecaddclass.elementAt(i)).beforeOffer(iSaveVO);

				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave time:" + (System.currentTimeMillis() - t) + "ms");
				result = OperationResultVO.appendResultVO(result, t_result1);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave times:" + (System.currentTimeMillis() - t) + "ms");
			}
			long tt = System.currentTimeMillis();

			voucher = insert(voucher);

			for (int i = 0; i < vecofferclass.size(); i++) {
				VoucherOperateInterfaceVO iSaveVO = new VoucherOperateInterfaceVO();
				iSaveVO.pk_user = voucher.getPk_prepared();
				iSaveVO.pk_vouchers = new String[] { voucher.getPk_voucher() };
				iSaveVO.userdata = voucher.getUserData();
				long t = System.currentTimeMillis();
				OperationResultVO[] t_result1 = ((nc.bs.gl.pubinterface.IVoucherOffer) vecaddclass.elementAt(i)).afterOffer(iSaveVO);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave time:" + (System.currentTimeMillis() - t) + "ms");
				result = OperationResultVO.appendResultVO(result, t_result1);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecaddclass.elementAt(i).getClass() + ".BeforeSave times:" + (System.currentTimeMillis() - t) + "ms");
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Insert time:" + (System.currentTimeMillis() - tt) + "ms");

			for (int m = vecaddclass.size(); m > 0; m--) {
				VoucherSaveInterfaceVO iSaveVO = new VoucherSaveInterfaceVO();
				iSaveVO.pk_user = voucher.getPk_prepared();
				iSaveVO.voucher = voucher;
				iSaveVO.userdata = voucher.getUserData();
				long t = System.currentTimeMillis();
				OperationResultVO[] t_result2 = ((nc.bs.gl.pubinterface.IVoucherSave) vecaddclass.elementAt(m - 1)).afterSave(iSaveVO);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info(
							"SaveVoucher time report::" + vecaddclass.elementAt(m - 1).getClass() + ".AfterSave time:" + (System.currentTimeMillis() - t) + "ms");
				result = OperationResultVO.appendResultVO(result, t_result2);
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info(
							"SaveVoucher time report::" + vecaddclass.elementAt(m - 1).getClass() + ".AfterSave times:" + (System.currentTimeMillis() - t) + "ms");
			}
		} else {
			long tt = System.currentTimeMillis();
			voucher = update(voucher);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Update time:" + (System.currentTimeMillis() - tt) + "ms");
		}

		for (int m = vecsaveclass.size(); m > 0; m--) {
			VoucherSaveInterfaceVO iSaveVO = new VoucherSaveInterfaceVO();
			iSaveVO.pk_user = voucher.getPk_prepared();
			iSaveVO.voucher = voucher;
			iSaveVO.userdata = voucher.getUserData();
			long t = System.currentTimeMillis();
			OperationResultVO[] t_result2 = ((nc.bs.gl.pubinterface.IVoucherSave) vecsaveclass.elementAt(m - 1)).afterSave(iSaveVO);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecsaveclass.elementAt(m - 1).getClass() + ".AfterSave time:" + (System.currentTimeMillis() - t) + "ms");
			result = OperationResultVO.appendResultVO(result, t_result2);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + vecsaveclass.elementAt(m - 1).getClass() + ".AfterSave times:" + (System.currentTimeMillis() - t) + "ms");
		}

		UFBoolean isVoucherTimeOrdered = new GlParaDMO().isVoucherTimeOrdered(voucher.getPk_glorgbook());
		if (isVoucherTimeOrdered != null && isVoucherTimeOrdered.booleanValue() && voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() != 2) {
			result = OperationResultVO.appendResultVO(result, new VoucherCheckBO().checkTimeOrdered(voucher));
		}

		OperationResultVO r_result = new OperationResultVO();
		r_result.m_intSuccess = 0;
		r_result.m_strDescription = null;
		r_result.m_strPK = voucher.getPk_voucher();
		r_result.m_userIdentical = voucher.clone();
		result = OperationResultVO.appendResultVO(new OperationResultVO[] { r_result }, result);

		if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
			Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Complete Saved time:" + (System.currentTimeMillis() - tb) + "ms");
		return result;
	}

	public OperationResultVO[] runtimeReconcile(VoucherVO voucher) {
		// TODO Auto-generated method stub
		OperationResultVO[] result = null;
		if (voucher.getIsmodelrecflag().booleanValue()) {
			return null;
		}

		if (GLParaDataCacheUseUap.getRunTimeReconcile(voucher.getPk_glorgbook()) == nc.vo.glcom.para.ParaMacro.RUNTIMERECONCILE_YES) {

			IPubReconcile pubreconcile = (IPubReconcile) NCLocator.getInstance().lookup(IPubReconcile.class.getName());
			PubReconcileInfoVO reconcileinfo = new PubReconcileInfoVO();
			reconcileinfo.setUserid(voucher.getPk_prepared());
			reconcileinfo.setReconcileDate(voucher.getPrepareddate());
			reconcileinfo.setPk_glorgbook(voucher.getPk_glorgbook());
			IReconcileExtend reconcilextend = NCLocator.getInstance().lookup(IReconcileExtend.class);

			try {
				boolean b = reconcilextend.checkCanReconByVoucherPK(voucher.getPk_voucher(), voucher.getPk_glorgbook());
				if (!b) {
					return null;
				}
				String log = pubreconcile.pubReconcileWithLog(voucher, reconcileinfo);
				if (log == null) {
					result = new OperationResultVO[1];
					result[0] = new OperationResultVO();
					result[0].m_strPK = voucher.getPk_voucher();
					result[0].m_intSuccess = 0;
					result[0].m_strDescription = "";
					result[0].m_userIdentical = voucher;
				} else {
					result = new OperationResultVO[1];
					result[0] = new OperationResultVO();
					result[0].m_strPK = voucher.getPk_voucher();
					result[0].m_intSuccess = 1;
					result[0].m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl5702","UPP2002gl5702-000020")/*@res "公有协同即时协同生成对方凭证出错:："*/ + log;
					result[0].m_userIdentical = voucher;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.debug(e.getMessage());
				throw new BusinessRuntimeException(e.getMessage());

			}
		}
		return result;
	}

	/**
	 * @author zhaozh
	 * 校验凭证号是否过大
	 * @param voucher
	 * @return
	 */
	private void checkMaxNum(VoucherVO voucher) {
		int realNum = voucher.getNo() == null ? 0 : voucher.getNo();
		Integer maxNum = null;
		int paraMaxnum = SystemParamConfig.getInstance().getMaxAllowVoucherNum();
		try {
			maxNum = GLPubProxy.getIRemoteVoucherNo().getMaxVoucherNumFromVouchertable(voucher);
			maxNum = maxNum == null ? 0 : maxNum;
		} catch (BusinessException e) {
			Logger.error("取最大凭证号错误:"+ this.getClass().getName());
			e.printStackTrace();
		}
		if(realNum > maxNum + paraMaxnum ){
			String err = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl5702","UPP2002gl5702-000021")/*@res "输入的凭证号"*/ + realNum + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl5702","UPP2002gl5702-000022")/*@res "大于此凭证类别在本年度期间的最大号"*/+ maxNum
			+ " " + SystemParamConfig.getInstance().getMaxAllowVoucherNum() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl5702","UPP2002gl5702-000023")/*@res "以上，不允许保存"*/;
			throw new GlBusinessException(err);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 17:54:14)
	 *
	 * @return nc.vo.gl.pubvoucher.OperationResultVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO voucher：凭证VO
	 *            isneedcheck：是否需要进行检查
	 */
	public OperationResultVO[] save(VoucherVO voucher, Boolean isneedcheck) throws BusinessException {
		long tb = System.currentTimeMillis();
		try {
			checkMaxNum(voucher);
			voucher = catOrgAndBook(voucher);
			voucher = catOppositesubj(voucher);
			voucher = catRegulationPeriod(voucher);
			voucher = catDetailPk_corp(voucher);

			//
			if (voucher.getFree7() != null && voucher.getFree7().startsWith("dap_save_Action")) {
				voucher.setFree7(null);
			}
			//

			OperationResultVO[] checkresult = null;
			OperationResultVO[] reconresult = null;
			if (isneedcheck != null && isneedcheck.booleanValue()) {
				// 检查凭证的合法性
				checkresult = new VoucherCheckBO().checkVoucher(voucher, new VoucherCheckConfigVO());
				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Check time:" + (System.currentTimeMillis() - tb) + "ms");
				if (checkresult != null) {
					StringBuffer strMsg = new StringBuffer();
					boolean errflag = false;
					for (int i = 0; i < checkresult.length; i++) {
						switch (checkresult[i].m_intSuccess) {
						case 0:
							break;
						case 1:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																	 * @res
																																	 * "警告:"
																																	 */+ checkresult[i].m_strDescription + "\n");
							break;
						case 2:
							errflag = true;
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																	 * @res
																																	 * "错误:"
																																	 */+ checkresult[i].m_strDescription + "\n");
							break;
						default:
							strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																	 * @res
																																	 * "信息:"
																																	 */+ checkresult[i].m_strDescription + "\n");
						}
					}
					if (strMsg.length() > 0 && errflag)
						throw new BusinessException(strMsg.toString());
				}
			} else {
				checkresult = checkCanSystemTempSave(voucher);
			}

			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Check and Adjust Result time:" + (System.currentTimeMillis() - tb) + "ms");

			OperationResultVO[] saveresult = saveVoucher(voucher, isneedcheck);

			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save time:" + (System.currentTimeMillis() - tb) + "ms");
			if (saveresult != null) {
				StringBuffer strMsg = new StringBuffer();
				boolean errflag = false;
				for (int i = 0; i < saveresult.length; i++) {
					switch (saveresult[i].m_intSuccess) {

					case 0:

						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ saveresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ saveresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ saveresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag) {
					throw new BusinessException(strMsg.toString());
				} else {
					// 凭证即时协同
					// int i
					reconresult = runtimeReconcile(voucher);
					//
				}
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save and Adjust Result time:" + (System.currentTimeMillis() - tb) + "ms");
			OperationResultVO[] result1 = OperationResultVO.appendResultVO(saveresult, checkresult);
			OperationResultVO[] results = OperationResultVO.appendResultVO(result1, reconresult);

			return results;
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
//			log.error(e);
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			GLKeyLock lock = null;
			boolean bLockSuccess = false;
			try {
				// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
				// getBeanHome(nc.bs.pub.lock.LockHome.class,
				// "nc.bs.pub.lock.LockBO");
				// if (home == null)
				// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
				lock = new GLKeyLock();
				for (int i = 0; i < 20; i++) {
					bLockSuccess = lock.lockKey("GL_V_N_C_L" + voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod(), voucher.getPk_prepared(),
							"gl_voucher");
					if (bLockSuccess)
						break;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				if (!bLockSuccess)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000561")/*
																																		 * @res
																																		 * "有其他用户在操作凭证表，请稍候再试。"
																																		 */);
				else {
					// VoucherExtendDMO dmo = new VoucherExtendDMO();
					// Integer iCount = dmo.getCountVoucherNo(voucher);
					// if (iCount.intValue() > 1) {
					// throw new BusinessException(new
					// VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
					// }
				}
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new BusinessException(e.getMessage(), e);
			} finally {
				try {
					if (bLockSuccess)
						lock.freeKey("GL_V_N_C_L" + voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod(), voucher.getPk_prepared(), "gl_voucher");
				} catch (Throwable e) {
					Log.getInstance(this.getClass().getName()).info(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000122")/*
																																								 * @res
																																								 * "释放锁异常:"
																																								 */+ "GL_V_N_C_L");
					e.printStackTrace();
				}

				// try
				// {
				// lock.remove();
				// }
				// catch (BusinessException e)
				// {
				// e.printStackTrace();
				// }
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save all done time:" + (System.currentTimeMillis() - tb) + "ms");
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 17:54:14)
	 *
	 * @return nc.vo.gl.pubvoucher.OperationResultVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO voucher：凭证VO
	 *            isneedcheck：是否需要进行检查
	 */
	public OperationResultVO[] save(VoucherVO voucher, VoucherCheckConfigVO configVO, Object userdata) throws BusinessException {
		long tb = System.currentTimeMillis();
		try {
			OperationResultVO[] checkresult = null;
			// 检查凭证的合法性
			checkresult = new VoucherCheckBO().checkVoucher(voucher, configVO);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Check time:" + (System.currentTimeMillis() - tb) + "ms");
			if (checkresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < checkresult.length; i++) {
					switch (checkresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ checkresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ checkresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Check and Adjust Result time:" + (System.currentTimeMillis() - tb) + "ms");
			voucher = catOrgAndBook(voucher);
			voucher = catOppositesubj(voucher);
			voucher = catRegulationPeriod(voucher);
			voucher = catRegulationPeriod(voucher);
			OperationResultVO[] saveresult = saveVoucher(voucher, true);
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save time:" + (System.currentTimeMillis() - tb) + "ms");
			if (saveresult != null) {
				StringBuffer strMsg = new StringBuffer(16 * 1024);
				boolean errflag = false;
				for (int i = 0; i < saveresult.length; i++) {
					switch (saveresult[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ saveresult[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ saveresult[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ saveresult[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save and Adjust Result time:" + (System.currentTimeMillis() - tb) + "ms");
			OperationResultVO[] results = OperationResultVO.appendResultVO(saveresult, checkresult);
			// VoucherExtendDMO dmo = new VoucherExtendDMO();
			// Integer iCount = dmo.getCountVoucherNo(voucher);
			// if (iCount.intValue() > 1)
			// {
			// throw new BusinessException(new
			// VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
			// }
			return results;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			GLKeyLock lock = null;
			boolean bLockSuccess = false;
			try {
				// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
				// getBeanHome(nc.bs.pub.lock.LockHome.class,
				// "nc.bs.pub.lock.LockBO");
				// if (home == null)
				// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
				lock = new GLKeyLock();
				for (int i = 0; i < 20; i++) {
					bLockSuccess = lock.lockKey("GL_V_N_C_L" + voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod(), voucher.getPk_prepared(),
							"gl_voucher");
					if (bLockSuccess)
						break;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				if (!bLockSuccess)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000561")/*
																																		 * @res
																																		 * "有其他用户在操作凭证表，请稍候再试。"
																																		 */);
				else {
					/*
					 * VoucherExtendDMO dmo = new VoucherExtendDMO(); Integer
					 * iCount = dmo.getCountVoucherNo(voucher); if
					 * (iCount.intValue() > 1) { throw new BusinessException(new
					 * VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist)); }
					 */
				}
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new BusinessException(e.getMessage(), e);
			} finally {
				try {
					if (bLockSuccess)
						lock.freeKey("GL_V_N_C_L" + voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod(), voucher.getPk_prepared(), "gl_voucher");
				} catch (Throwable e) {
					Log.getInstance(this.getClass().getName()).info(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000122")/*
																																								 * @res
																																								 * "释放锁异常:"
																																								 */+ "GL_V_N_C_L");
					e.printStackTrace();
				}

				// try
				// {
				// lock.remove();
				// }
				// catch (BusinessException e)
				// {
				// e.printStackTrace();
				// }
			}
			if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
				Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::nc.bs.gl.voucher.VoucherBO.Save all done time:" + (System.currentTimeMillis() - tb) + "ms");
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 17:54:14)
	 *
	 * @return nc.vo.gl.pubvoucher.OperationResultVO
	 * @param voucher
	 *            nc.vo.gl.pubvoucher.VoucherVO
	 */
	public OperationResultVO[] saveError(VoucherVO voucher) throws BusinessException {

		try {
			appendDetailHead(voucher);
			OperationResultVO[] result = new VoucherCheckBO().checkPreparedDate(voucher);
			if (result != null) {
				StringBuffer strMsg = new StringBuffer();
				boolean errflag = false;
				for (int i = 0; i < result.length; i++) {
					switch (result[i].m_intSuccess) {
					case 0:
						break;
					case 1:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000119")/*
																																 * @res
																																 * "警告:"
																																 */+ result[i].m_strDescription + "\n");
						break;
					case 2:
						errflag = true;
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000120")/*
																																 * @res
																																 * "错误:"
																																 */+ result[i].m_strDescription + "\n");
						break;
					default:
						strMsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002100556", "UPP2002100556-000121")/*
																																 * @res
																																 * "信息:"
																																 */+ result[i].m_strDescription + "\n");
					}
				}
				if (strMsg.length() > 0 && errflag)
					throw new BusinessException(strMsg.toString());
			}
			if (voucher.getPk_voucher() != null) {

				VoucherVO oldvoucher = new VoucherExtendDMO().findByPrimaryKey(voucher.getPk_voucher());
				checkError(oldvoucher, result);

				// 核销数据同步，当凭证标错或分录标错后，删除核销分录表中的相应数据。
				new nc.bs.gl.verifybegin.VerifydetailDMO().deleteByVoucherPk(voucher.getPk_voucher());
				//begin ncm zhaoypb 凭证标错信息为空保存时同步核销分录
				//PID:201110191821547309 CUS:协鑫硅材料
				if (voucher.m_errmessage == null || "".equals(voucher.m_errmessage.trim())) {
					VerifydetailVO[] vos = GLVouchersToCAVBalance.convertVO(voucher);
					new nc.bs.gl.verifybegin.VerifydetailDMO().insertDetails(vos);
				}
				//END NCM ZHAOYPB 2011-07-06

				if (oldvoucher == null)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000562")/*
																																		 * @res
																																		 * "凭证已删除！"
																																		 */);
				else {
					if (oldvoucher.getPk_checked() != null)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000563")/*
																																			 * @res
																																			 * "凭证已审核，无法标错！"
																																			 */);
					if (oldvoucher.getPk_manager() != null)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000564")/*
																																			 * @res
																																			 * "凭证已记账，无法标错！"
																																			 */);
				}
			}

			{
				long tt = System.currentTimeMillis();

				copy_deletedVoucher(voucher.getPk_voucher());

				GLKeyLock lock = null;
				boolean bLockSuccess = false;
				try {
					// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
					// getBeanHome(nc.bs.pub.lock.LockHome.class,
					// "nc.bs.pub.lock.LockBO");
					// if (home == null)
					// throw new
					// ClassNotFoundException("nc.bs.pub.lock.LockHome");
					lock = new GLKeyLock();
					for (int i = 0; i < 5; i++) {
						bLockSuccess = lock.lockKey(voucher.getPk_voucher() == null ? voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod() : voucher
								.getPk_voucher(), voucher.getPk_prepared(), "gl_voucher");
						if (bLockSuccess)
							break;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					if (!bLockSuccess)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																			 * @res
																																			 * "有其他用户在操作，请稍候再试。"
																																			 */);
					if (voucher.getNo() == null || voucher.getNo().intValue() <= 0)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000565")/*
																																			 * @res
																																			 * "错误或无效的凭证号："
																																			 */+ voucher.getNo());
					else
						// checkVoucherNo(voucher);
						checkVoucherNumber(voucher);
					// 凭证主表
					VoucherDMO dmo = new VoucherDMO();
					VoucherVO tmpvoucher = dmo.findByPrimaryKey(voucher.getPk_voucher());
					if (tmpvoucher == null)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000566")/*
																																			 * @res
																																			 * "正在修改的凭证已被别人删除。无法保存数据。"
																																			 */);
					else if (tmpvoucher.getDiscardflag() != null && tmpvoucher.getDiscardflag().equals("Y"))
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000567")/*
																																			 * @res
																																			 * "正在修改的凭证已被别人作废。无法保存数据。"
																																			 */);
					else if (tmpvoucher.getPk_checked() != null)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000568")/*
																																			 * @res
																																			 * "正在修改的凭证已被别人审核。无法保存数据。"
																																			 */);
					else if (tmpvoucher.getPk_manager() != null)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000569")/*
																																			 * @res
																																			 * "正在修改的凭证已被别人记账。无法保存数据。"
																																			 */);
					int iCount = dmo.update(voucher);
					if (iCount == 0)
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000566")/*
																																			 * @res
																																			 * "正在修改的凭证已被别人删除。无法保存数据。"
																																			 */);
				} finally {
					if (bLockSuccess)
						lock.freeKey(voucher.getPk_voucher() == null ? voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod() : voucher.getPk_voucher(),
								voucher.getPk_prepared(), "gl_voucher");
					// try
					// {
					// lock.remove();
					// }
					// catch (BusinessException e)
					// {
					// e.printStackTrace();
					// }
				}

				// 删除凭证相关子表的内容
				VchfreevalueDMO vfdmo = new VchfreevalueDMO();
				vfdmo.deleteByVoucherPK(voucher.getPk_voucher());
				DetailExtendDMO dmo1 = new DetailExtendDMO();
				DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
				SubjfreevalueDMO dmo4 = new SubjfreevalueDMO();
				dmo3.deleteByVoucherPK(voucher.getPk_voucher());
				dmo4.deleteByVoucherPK(voucher.getPk_voucher());
				dmo1.deleteByVoucherPK(voucher.getPk_voucher());

				// 插入相同pk的记录
				if (voucher.getFreevalue1() != null || voucher.getFreevalue2() != null || voucher.getFreevalue3() != null || voucher.getFreevalue4() != null || voucher.getFreevalue5() != null) {
					VchfreevalueVO vf = new VchfreevalueVO();
					vf.setFreevalue1(voucher.getFreevalue1());
					vf.setFreevalue2(voucher.getFreevalue2());
					vf.setFreevalue3(voucher.getFreevalue3());
					vf.setFreevalue4(voucher.getFreevalue4());
					vf.setFreevalue5(voucher.getFreevalue5());
					vf.setPk_voucher(voucher.getPk_voucher());
					vfdmo.insert(vf);
				}
				DetailVO[] details = voucher.getDetails();
				for (int i = 0; i < details.length; i++) {
					details[i].setPk_voucher(voucher.getPk_voucher());
				}

				String[] pks = dmo1.saveArray(details);

				Vector tempfreevalue = new Vector();
				for (int i = 0; i < details.length; i++) {
					details[i].setPk_detail(pks[i]);
					if (details[i].getFreevalue1() != null || details[i].getFreevalue2() != null || details[i].getFreevalue3() != null || details[i].getFreevalue4() != null
							|| details[i].getFreevalue5() != null || details[i].getFreevalue6() != null || details[i].getFreevalue7() != null || details[i].getFreevalue8() != null
							|| details[i].getFreevalue9() != null || details[i].getFreevalue10() != null || details[i].getFreevalue11() != null || details[i].getFreevalue12() != null
							|| details[i].getFreevalue13() != null || details[i].getFreevalue14() != null || details[i].getFreevalue15() != null || details[i].getFreevalue16() != null
							|| details[i].getFreevalue17() != null || details[i].getFreevalue18() != null || details[i].getFreevalue19() != null || details[i].getFreevalue20() != null
							|| details[i].getFreevalue21() != null || details[i].getFreevalue22() != null || details[i].getFreevalue23() != null || details[i].getFreevalue24() != null
							|| details[i].getFreevalue25() != null || details[i].getFreevalue26() != null || details[i].getFreevalue27() != null || details[i].getFreevalue28() != null
							|| details[i].getFreevalue29() != null || details[i].getFreevalue30() != null) {
						DtlfreevalueVO tempfree = new DtlfreevalueVO();
						tempfree.setPk_detail(details[i].getPk_detail());
						tempfree.setFreevalue1(details[i].getFreevalue1());
						tempfree.setFreevalue2(details[i].getFreevalue2());
						tempfree.setFreevalue3(details[i].getFreevalue3());
						tempfree.setFreevalue4(details[i].getFreevalue4());
						tempfree.setFreevalue5(details[i].getFreevalue5());
						tempfree.setFreevalue6(details[i].getFreevalue6());
						tempfree.setFreevalue7(details[i].getFreevalue7());
						tempfree.setFreevalue8(details[i].getFreevalue8());
						tempfree.setFreevalue9(details[i].getFreevalue9());
						tempfree.setFreevalue10(details[i].getFreevalue10());
						tempfree.setFreevalue11(details[i].getFreevalue11());
						tempfree.setFreevalue12(details[i].getFreevalue12());
						tempfree.setFreevalue13(details[i].getFreevalue13());
						tempfree.setFreevalue14(details[i].getFreevalue14());
						tempfree.setFreevalue15(details[i].getFreevalue15());
						tempfree.setFreevalue16(details[i].getFreevalue16());
						tempfree.setFreevalue17(details[i].getFreevalue17());
						tempfree.setFreevalue18(details[i].getFreevalue18());
						tempfree.setFreevalue19(details[i].getFreevalue19());
						tempfree.setFreevalue20(details[i].getFreevalue20());
						tempfree.setFreevalue21(details[i].getFreevalue21());
						tempfree.setFreevalue22(details[i].getFreevalue22());
						tempfree.setFreevalue23(details[i].getFreevalue23());
						tempfree.setFreevalue24(details[i].getFreevalue24());
						tempfree.setFreevalue25(details[i].getFreevalue25());
						tempfree.setFreevalue26(details[i].getFreevalue26());
						tempfree.setFreevalue27(details[i].getFreevalue27());
						tempfree.setFreevalue28(details[i].getFreevalue28());
						tempfree.setFreevalue29(details[i].getFreevalue29());
						tempfree.setFreevalue30(details[i].getFreevalue30());
						tempfreevalue.addElement(tempfree);
					}
				}
				DtlfreevalueVO[] detailfreevalues = new DtlfreevalueVO[tempfreevalue.size()];
				tempfreevalue.copyInto(detailfreevalues);
				if (detailfreevalues.length > 0) {
					dmo3.insertArray(detailfreevalues);
				}

				Vector subjfreevalue = new Vector();
				for (int i = 0; i < details.length; i++) {
					if (details[i].getSubjfreevalue() == null)
						continue;
					SubjfreevalueVO sf = details[i].getSubjfreevalue();
					if (sf.getSubjfreevalue1() != null || sf.getSubjfreevalue2() != null || sf.getSubjfreevalue3() != null || sf.getSubjfreevalue4() != null || sf.getSubjfreevalue5() != null
							|| sf.getSubjfreevalue6() != null || sf.getSubjfreevalue7() != null || sf.getSubjfreevalue8() != null || sf.getSubjfreevalue9() != null || sf.getSubjfreevalue10() != null
							|| sf.getSubjfreevalue11() != null || sf.getSubjfreevalue12() != null || sf.getSubjfreevalue13() != null || sf.getSubjfreevalue14() != null
							|| sf.getSubjfreevalue15() != null || sf.getSubjfreevalue16() != null || sf.getSubjfreevalue17() != null || sf.getSubjfreevalue18() != null
							|| sf.getSubjfreevalue19() != null || sf.getSubjfreevalue20() != null || sf.getSubjfreevalue21() != null || sf.getSubjfreevalue22() != null
							|| sf.getSubjfreevalue23() != null || sf.getSubjfreevalue24() != null || sf.getSubjfreevalue25() != null || sf.getSubjfreevalue26() != null
							|| sf.getSubjfreevalue27() != null || sf.getSubjfreevalue28() != null || sf.getSubjfreevalue29() != null || sf.getSubjfreevalue30() != null) {
						sf.setPk_accsubj(details[i].getPk_accsubj());
						sf.setPk_corp(details[i].getPk_corp());
						sf.setPk_detail(details[i].getPk_detail());
						subjfreevalue.addElement(sf);
					}
				}
				SubjfreevalueVO[] subjfreevalues = new SubjfreevalueVO[subjfreevalue.size()];
				subjfreevalue.copyInto(subjfreevalues);
				if (subjfreevalues.length > 0) {
					dmo4.insertArray(subjfreevalues);
				}

				voucher.setDetails(details);

				if (nc.vo.glcom.para.GlDebugFlag.$DEBUG)
					Log.getInstance(this.getClass().getName()).info("SaveVoucher time report::" + this.getClass() + "Update time:" + (System.currentTimeMillis() - tt) + "ms");
			}

			OperationResultVO r_result = new OperationResultVO();
			r_result.m_intSuccess = 0;
			r_result.m_strDescription = null;
			r_result.m_strPK = voucher.getPk_voucher();
			r_result.m_userIdentical = voucher.clone();
			result = OperationResultVO.appendResultVO(new OperationResultVO[] { r_result }, result);

			return result;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
	}

	private void checkError(VoucherVO oldvoucher, OperationResultVO[] result) throws Exception {
		// TODO Auto-generated method stub

		String[] pkdetails = getRecDetailPKsByVoucherPK(new String[] { oldvoucher.getPk_voucher() });
		if (pkdetails != null && pkdetails.length > 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55", "UPP2002gl55-000675")/*
																																 * @res
																																 * "有分录已经核销/对账/协同，不能标错。"
																																 */);
		} else {
			// 协同和对帐可以并行后，在这里加上是否对帐检查 added By liyongru For V506 at 20090615
			pkdetails = new CorpcontrastsubDMO().getContrastedDetailsByVoucherPKs(new String[] { oldvoucher.getPk_voucher() });
			if (pkdetails != null && pkdetails.length > 0) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55", "UPP2002gl55-000675")/*
																																	 * @res
																																	 * "有分录已经核销/对账/协同，不能标错。"
																																	 */);
			}
		}

	}

	public void reloadVoucherUser(String pk_glorgbook) throws BusinessException {
		VoucherUserDAO voucheruser = new VoucherUserDAO();
		voucheruser.reloadVoucherUser(pk_glorgbook, VoucherUserVO.User_Type_Prepare);
		voucheruser.reloadVoucherUser(pk_glorgbook, VoucherUserVO.User_Type_Check);
		voucheruser.reloadVoucherUser(pk_glorgbook, VoucherUserVO.User_Type_Sign);
		voucheruser.reloadVoucherUser(pk_glorgbook, VoucherUserVO.User_Type_Tally);

	}

	/**
	 * 用VO对象的属性值更新数据库。
	 *
	 * 由于凭证子表的数量是不一定的，依次判断该记录的新增的还是原有的该更新的还是原有的该删除的太影响效率，
	 * 所以，对凭证子表的更新操作采用先删除原有记录，然后插入相同记录的方式。
	 *
	 */
	private VoucherVO update(VoucherVO voucher) throws Exception {
		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		try {
			// nc.bs.pub.lock.LockHome home = (nc.bs.pub.lock.LockHome)
			// getBeanHome(nc.bs.pub.lock.LockHome.class,
			// "nc.bs.pub.lock.LockBO");
			// if (home == null)
			// throw new ClassNotFoundException("nc.bs.pub.lock.LockHome");
			lock = new GLKeyLock();
			for (int i = 0; i < 5; i++) {
				bLockSuccess = lock.lockKey(voucher.getPk_voucher() == null ? voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod() : voucher
						.getPk_voucher(), voucher.getPk_prepared(), "gl_voucher");
				if (bLockSuccess)
					break;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			if (!bLockSuccess)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000539")/*
																																	 * @res
																																	 * "有其他用户在操作，请稍候再试。"
																																	 */);

		if (voucher.getNo() == null || voucher.getNo().intValue() == 0) {
			// voucher = catVoucherNo(voucher);
			// getVoucherNumber(voucher);
			getVoucherNumberForSave(voucher);
			if(voucher.getPk_voucher() != null)
				updateVoucherNumber(voucher);
		} else {
			try {
				updateVoucherNumber(voucher);
			} catch (VoucherNoDuplicateException e) {
				throw new BusinessException(new VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
			} catch (Exception ex) {
				throw ex;
			}
		}

		copy_deletedVoucherNew(voucher.getPk_voucher());


			if (voucher.getNo() == null || voucher.getNo().intValue() <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000565")/*
																																	 * @res
																																	 * "错误或无效的凭证号："
																																	 */+ voucher.getNo());
			/*
			 * else checkVoucherNumber(voucher);
			 */
			// 凭证主表
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			VoucherVO tmpvoucher = dmo.queryByVoucherPk(voucher.getPk_voucher());
			if (tmpvoucher == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000566")/*
																																	 * @res
																																	 * "正在修改的凭证已被别人删除。无法保存数据。"
																																	 */);
			else if (tmpvoucher.getDiscardflag() != null && tmpvoucher.getDiscardflag().equals("Y"))
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000567")/*
																																	 * @res
																																	 * "正在修改的凭证已被别人作废。无法保存数据。"
																																	 */);
			else if (tmpvoucher.getPk_casher() != null && voucher.getErrmessage() == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000570")/*
																																	 * @res
																																	 * "正在修改的凭证已被别人签字。无法保存数据。"
																																	 */);
			else if (tmpvoucher.getPk_checked() != null) {

				if (new GlParaDMO().getVoucherFlowCTL(tmpvoucher.getPk_glorgbook()).intValue() == ParaMacro.VOUCHER_FLOWCONTROL_SIGNALCHECK)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000568")/*
																																		 * @res
																																		 * "正在修改的凭证已被别人审核。无法保存数据。"
																																		 */);
			} else if (tmpvoucher.getPk_manager() != null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000569")/*
																																	 * @res
																																	 * "正在修改的凭证已被别人记账。无法保存数据。"
																																	 */);
			int iCount = dmo.update(voucher);
			if (iCount == 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005", "UPP20021005-000566")/*
																																	 * @res
																																	 * "正在修改的凭证已被别人删除。无法保存数据。"
																																	 */);
		} finally {
			if (bLockSuccess)
				lock.freeKey(voucher.getPk_voucher() == null ? voucher.getPk_glorgbook() + voucher.getPk_vouchertype() + voucher.getYear() + voucher.getPeriod() : voucher.getPk_voucher(), voucher
						.getPk_prepared(), "gl_voucher");
			// try
			// {
			// lock.remove();
			// }
			// catch (BusinessException e)
			// {
			// e.printStackTrace();
			// }
		}

		// 删除凭证相关子表的内容
		VchfreevalueDMO vfdmo = new VchfreevalueDMO();
		vfdmo.deleteByVoucherPK(voucher.getPk_voucher());
		DetailExtendDMO dmo1 = new DetailExtendDMO();
		DtlfreevalueDMO dmo3 = new DtlfreevalueDMO();
		SubjfreevalueDMO dmo4 = new SubjfreevalueDMO();
		dmo3.deleteByVoucherPK(voucher.getPk_voucher());
		dmo4.deleteByVoucherPK(voucher.getPk_voucher());
		dmo1.deleteByVoucherPK(voucher.getPk_voucher());

		// 插入相同pk的记录
		if (voucher.getFreevalue1() != null || voucher.getFreevalue2() != null || voucher.getFreevalue3() != null || voucher.getFreevalue4() != null || voucher.getFreevalue5() != null) {
			VchfreevalueVO vf = new VchfreevalueVO();
			vf.setFreevalue1(voucher.getFreevalue1());
			vf.setFreevalue2(voucher.getFreevalue2());
			vf.setFreevalue3(voucher.getFreevalue3());
			vf.setFreevalue4(voucher.getFreevalue4());
			vf.setFreevalue5(voucher.getFreevalue5());
			vf.setPk_voucher(voucher.getPk_voucher());
			vfdmo.insert(vf);
		}
		DetailVO[] details = voucher.getDetails();
		for (int i = 0; i < details.length; i++) {
			details[i].setPk_voucher(voucher.getPk_voucher());
		}
		String[] pks = dmo1.saveArray(details);

		Vector tempfreevalue = new Vector();
		for (int i = 0; i < details.length; i++) {
			details[i].setPk_detail(pks[i]);
			if (details[i].getFreevalue1() != null || details[i].getFreevalue2() != null || details[i].getFreevalue3() != null || details[i].getFreevalue4() != null
					|| details[i].getFreevalue5() != null || details[i].getFreevalue6() != null || details[i].getFreevalue7() != null || details[i].getFreevalue8() != null
					|| details[i].getFreevalue9() != null || details[i].getFreevalue10() != null || details[i].getFreevalue11() != null || details[i].getFreevalue12() != null
					|| details[i].getFreevalue13() != null || details[i].getFreevalue14() != null || details[i].getFreevalue15() != null || details[i].getFreevalue16() != null
					|| details[i].getFreevalue17() != null || details[i].getFreevalue18() != null || details[i].getFreevalue19() != null || details[i].getFreevalue20() != null
					|| details[i].getFreevalue21() != null || details[i].getFreevalue22() != null || details[i].getFreevalue23() != null || details[i].getFreevalue24() != null
					|| details[i].getFreevalue25() != null || details[i].getFreevalue26() != null || details[i].getFreevalue27() != null || details[i].getFreevalue28() != null
					|| details[i].getFreevalue29() != null || details[i].getFreevalue30() != null) {
				DtlfreevalueVO tempfree = new DtlfreevalueVO();
				tempfree.setPk_detail(details[i].getPk_detail());
				tempfree.setFreevalue1(details[i].getFreevalue1());
				tempfree.setFreevalue2(details[i].getFreevalue2());
				tempfree.setFreevalue3(details[i].getFreevalue3());
				tempfree.setFreevalue4(details[i].getFreevalue4());
				tempfree.setFreevalue5(details[i].getFreevalue5());
				tempfree.setFreevalue6(details[i].getFreevalue6());
				tempfree.setFreevalue7(details[i].getFreevalue7());
				tempfree.setFreevalue8(details[i].getFreevalue8());
				tempfree.setFreevalue9(details[i].getFreevalue9());
				tempfree.setFreevalue10(details[i].getFreevalue10());
				tempfree.setFreevalue11(details[i].getFreevalue11());
				tempfree.setFreevalue12(details[i].getFreevalue12());
				tempfree.setFreevalue13(details[i].getFreevalue13());
				tempfree.setFreevalue14(details[i].getFreevalue14());
				tempfree.setFreevalue15(details[i].getFreevalue15());
				tempfree.setFreevalue16(details[i].getFreevalue16());
				tempfree.setFreevalue17(details[i].getFreevalue17());
				tempfree.setFreevalue18(details[i].getFreevalue18());
				tempfree.setFreevalue19(details[i].getFreevalue19());
				tempfree.setFreevalue20(details[i].getFreevalue20());
				tempfree.setFreevalue21(details[i].getFreevalue21());
				tempfree.setFreevalue22(details[i].getFreevalue22());
				tempfree.setFreevalue23(details[i].getFreevalue23());
				tempfree.setFreevalue24(details[i].getFreevalue24());
				tempfree.setFreevalue25(details[i].getFreevalue25());
				tempfree.setFreevalue26(details[i].getFreevalue26());
				tempfree.setFreevalue27(details[i].getFreevalue27());
				tempfree.setFreevalue28(details[i].getFreevalue28());
				tempfree.setFreevalue29(details[i].getFreevalue29());
				tempfree.setFreevalue30(details[i].getFreevalue30());
				tempfreevalue.addElement(tempfree);
			}
		}
		DtlfreevalueVO[] detailfreevalues = new DtlfreevalueVO[tempfreevalue.size()];
		tempfreevalue.copyInto(detailfreevalues);
		if (detailfreevalues.length > 0) {
			dmo3.insertArray(detailfreevalues);
		}

		Vector subjfreevalue = new Vector();
		for (int i = 0; i < details.length; i++) {
			if (details[i].getSubjfreevalue() == null)
				continue;
			SubjfreevalueVO sf = details[i].getSubjfreevalue();
			if (sf.getSubjfreevalue1() != null || sf.getSubjfreevalue2() != null || sf.getSubjfreevalue3() != null || sf.getSubjfreevalue4() != null || sf.getSubjfreevalue5() != null
					|| sf.getSubjfreevalue6() != null || sf.getSubjfreevalue7() != null || sf.getSubjfreevalue8() != null || sf.getSubjfreevalue9() != null || sf.getSubjfreevalue10() != null
					|| sf.getSubjfreevalue11() != null || sf.getSubjfreevalue12() != null || sf.getSubjfreevalue13() != null || sf.getSubjfreevalue14() != null || sf.getSubjfreevalue15() != null
					|| sf.getSubjfreevalue16() != null || sf.getSubjfreevalue17() != null || sf.getSubjfreevalue18() != null || sf.getSubjfreevalue19() != null || sf.getSubjfreevalue20() != null
					|| sf.getSubjfreevalue21() != null || sf.getSubjfreevalue22() != null || sf.getSubjfreevalue23() != null || sf.getSubjfreevalue24() != null || sf.getSubjfreevalue25() != null
					|| sf.getSubjfreevalue26() != null || sf.getSubjfreevalue27() != null || sf.getSubjfreevalue28() != null || sf.getSubjfreevalue29() != null || sf.getSubjfreevalue30() != null) {
				sf.setPk_accsubj(details[i].getPk_accsubj());
				sf.setPk_corp(details[i].getPk_corp());
				sf.setPk_detail(details[i].getPk_detail());
				subjfreevalue.addElement(sf);
			}
		}
		SubjfreevalueVO[] subjfreevalues = new SubjfreevalueVO[subjfreevalue.size()];
		subjfreevalue.copyInto(subjfreevalues);
		if (subjfreevalues.length > 0) {
			dmo4.insertArray(subjfreevalues);
		}

		voucher.setDetails(details);

		return voucher;
	}

	public void afterOperate(String fun_code, int opType, String pk1, String pk2, Object bd_docData) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void beforeOperate(String fun_code, int opType, String pk1, String pk2, Object bd_docData) throws AccperiodmonthUsedException, BusinessException {
		// TODO Auto-generated method stub
		if (fun_code != null && fun_code.equals("100404")) {
			if (opType == nc.itf.uap.bd.accperiod.IAccperiodConst.BDOPERATION_ACCPERIOD_DELETE_USEADJUST || opType == nc.itf.uap.bd.accperiod.IAccperiodConst.BDOPERATION_ACCPERIOD_UPDATE_USEADJUST) {
				AccperiodmonthVO[] adjustMonths = (AccperiodmonthVO[]) bd_docData;
				for (int i = 0; i < adjustMonths.length; i++) {
					AccountCalendar acc = AccountCalendar.getInstanceByAccperiod(adjustMonths[i].getPk_accperiod());
					boolean isexistregulsrvoucher = isExistRegulationVoucher(adjustMonths[i].getPk_accperiodscheme(), acc.getYearVO().getPeriodyear(), adjustMonths[i].getMonth());
					if (isexistregulsrvoucher) {
						throw new AccperiodmonthUsedException(adjustMonths[i]);
					}
				}
			}
		}

	}

	private void appendDetailHead(VoucherVO voucher) {
		DetailVO[] details = voucher.getDetails();
		for (DetailVO detailVO : details) {
			detailVO.setPrepareddate(voucher.getPrepareddate());
			detailVO.setYear(voucher.getYear());
			detailVO.setPeriod(voucher.getPeriod());
			detailVO.setFree6(voucher.getFree1());
			detailVO.setVoucherkind(voucher.getVoucherkind());
			detailVO.setPk_vouchertype(voucher.getPk_vouchertype());
			detailVO.setPk_manager(voucher.getPk_manager());
			detailVO.setDiscardflag(voucher.getDiscardflag());
			detailVO.setErrmessage2(voucher.getErrmessage());
			detailVO.setNo(voucher.getNo());
			detailVO.setPk_system(voucher.getPk_system());
			detailVO.setIsdifflag(voucher.getIsdifflag());
		}

	}

	public boolean updateVoucherDifflag(String[] pk_vouchers, UFBoolean direction) throws BusinessException {
		// TODO Auto-generated method stub
		boolean bflag = true;
		try {
			new VoucherExtendDMO().updateDifflag(pk_vouchers, direction);
		} catch (SystemException e) {
			bflag = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			bflag = false;
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			bflag = false;
			throw new BusinessException(e.getMessage());
		}
		return bflag;
	}

	/**
	 * @param voucher
	 * @return
	 * @throws BusinessException
	 */
	public String[] getVoucherNumber(VoucherVO voucher) throws BusinessException {

		try {
			GLParameterVO glparam = new GLParameterVO();

			GlParaDMO dmo = new GlParaDMO();
			glparam.Parameter_isvouchernoautofill = dmo.isVoucherNOAutoFill(voucher.getPk_glorgbook());
			if (glparam.Parameter_isvouchernoautofill != null && glparam.Parameter_isvouchernoautofill.booleanValue()) {
				if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() == 1)
					return getVoucherNumberNoUpdate(voucher, null);
				else {
					glparam.Parameter_startvoucherno = dmo.getStartVoucherNO(voucher.getPk_glorgbook());
					if (voucher.getFree9() == null)
						voucher.setFree9("automatchVoucherNO");
					return getVoucherNumberNoUpdate(voucher, glparam.Parameter_startvoucherno);
				}
			} else {
				return getVoucherNumberNoUpdate(voucher, null);
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new BusinessException(e);
		} finally {
			if (voucher.getFree9() != null && "automatchVoucherNO".equals(voucher.getFree9())) {
				voucher.setFree9(null);
			}
		}
	}

	/**
	 * @param voucher
	 * @return
	 * @throws BusinessException
	 */
	public String[] getVoucherNumberForSave(VoucherVO voucher) throws BusinessException {

		try {
			VoucherNoFetch fetch = new VoucherNoFetch();
			GLParameterVO glparam = new GLParameterVO();

			GlParaDMO dmo = new GlParaDMO();
			glparam.Parameter_isvouchernoautofill = dmo.isVoucherNOAutoFill(voucher.getPk_glorgbook());
			if (glparam.Parameter_isvouchernoautofill != null && glparam.Parameter_isvouchernoautofill.booleanValue()) {
				if (voucher.getVoucherkind() != null && voucher.getVoucherkind().intValue() == 1)
					return fetch.getVoucherNumber(voucher, null);
				else {
					glparam.Parameter_startvoucherno = dmo.getStartVoucherNO(voucher.getPk_glorgbook());
					if (voucher.getFree9() == null)
						voucher.setFree9("automatchVoucherNO");
					return fetch.getVoucherNumber(voucher, glparam.Parameter_startvoucherno);
				}
			} else {
				return fetch.getVoucherNumber(voucher, null);
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new BusinessException(e);
		} finally {
			if (voucher.getFree9() != null && "automatchVoucherNO".equals(voucher.getFree9())) {
				voucher.setFree9(null);
			}
		}
	}

	/**
	 * @param voucher
	 * @throws BusinessException
	 */
	public void checkVoucherNumber(VoucherVO voucher) throws BusinessException {
		// getVoucherNumber_RequiresNew(voucher, null, false);
		try {
			updateVoucherNumber(voucher);
		} catch (VoucherNoDuplicateException e) {
			throw new BusinessException(new VoucherCheckMessage().getVoucherMessage(VoucherCheckMessage.ErrMsgNOExist));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * @param voucher
	 * @throws Exception
	 */
	private void updateVoucherNumber(VoucherVO voucher) throws VoucherNoDuplicateException, Exception {

		if (voucher.getPk_voucher() != null && voucher.getPk_voucher().trim().length() > 0) {
			Object[] tmps = new VoucherDMO().getVoucherTypeByPk(voucher.getPk_voucher());
			String vt = (String) tmps[0];
			Integer no = (Integer) tmps[1];
			String pkglorgbook = (String) tmps[2];
			String year = (String) tmps[3];
			String period = (String) tmps[4];

			if (vt.compareTo(voucher.getPk_vouchertype()) != 0 || !pkglorgbook.equals(voucher.getPk_glorgbook()) || !year.equals(voucher.getYear()) || !period.equals(voucher.getPeriod())) {
				voucher.setNoFactorChanged(true);
			}
			if (vt.compareTo(voucher.getPk_vouchertype()) != 0 || no.compareTo(voucher.getNo()) != 0 || !pkglorgbook.equals(voucher.getPk_glorgbook()) || !year.equals(voucher.getYear())
					|| !period.equals(voucher.getPeriod())) {
				VoucherVO oldVoucher = new VoucherVO();
				oldVoucher.setPk_vouchertype(vt);
				oldVoucher.setNo(no);
				oldVoucher.setPk_glorgbook(pkglorgbook);
				oldVoucher.setYear(year);
				oldVoucher.setPeriod(period);
				voucher.setNoFeature(oldVoucher);
				new VoucherNoFetch().updateVoucherNumber(oldVoucher, voucher);
			}
		} else {
			new VoucherNoFetch().updateVoucherNumber(null, voucher);
		}

	}

	private String[] getVoucherNumberNoUpdate(VoucherVO voucher, Integer startNumber) throws BusinessException {
		Integer no = null;
		GLKeyLock lock = null;
		boolean bLockSuccess = false;
		String[] rslt = new String[2];
		String[] strs = null;
		try {
			// lock = new GLKeyLock();
			/*
			 * for (int i = 0; i < 5; i++) { bLockSuccess =
			 * lock.lockKey(voucher.getPk_voucher() == null ? "maxno_" +
			 * voucher.getPk_glorgbook() + voucher.getPk_vouchertype() +
			 * voucher.getYear() + voucher.getPeriod() : "maxno_" +
			 * voucher.getPk_voucher(), voucher.getPk_prepared(), "gl_voucher");
			 * if (bLockSuccess) break; try { Thread.sleep(100); } catch
			 * (InterruptedException e) { } } if (!bLockSuccess) throw new
			 * BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20021005",
			 * "UPP20021005-000539") @res "有其他用户在操作，请稍候再试。" );
			 */
			VoucherExtendDMO dmo = new VoucherExtendDMO();
			strs = dmo.getMaxVOucherNo(voucher, false);
			boolean isautoMatch = voucher.getFree9() != null && "automatchVoucherNO".equals(voucher.getFree9());

			if (strs[0] == null) {
				Integer num = dmo.getMaxVoucherNumFromVouchertable(voucher);
				if (num != null) {
					if (isautoMatch) {
						voucher.setNo(dmo.getCorrectVoucherNoAutoMatch(voucher, startNumber));
					} else {
						voucher.setNo(num + 1);
					}
				} else {
					if (isautoMatch) {
						voucher.setNo(startNumber == null ? new Integer(1) : startNumber);
					} else {
						voucher.setNo(1);
					}

				}
			} else {
				if (isautoMatch) {
					no = dmo.getNumberfromSub(strs[1], false);
					if (no != null) {
						voucher.setNo(no);
					} else {
						// strs = dmo.getMaxVOucherNo(voucher, true);
						no = Integer.valueOf(strs[0]);
						voucher.setNo(no + 1);
						no++;
					}
				} else {
					no = Integer.valueOf(strs[0]);
					Integer xno = dmo.getVoucherNoFromMaxsub(strs[1], no, false);
					if (xno != null) {
						// no = dmo.getNumberfromSub(strs[1], false);
						no = dmo.getCorrectVoucherNo(voucher);
						if (no != null) {
							voucher.setNo(no);
						} else {
							// strs = dmo.getMaxVOucherNo(voucher, true);
							no = Integer.valueOf(strs[0]);
							voucher.setNo(no + 1);
							no++;
						}
					} else {
						voucher.setNo(no + 1);
						no++;
					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			throw new BusinessException(e.getMessage());
		} finally {
			/*
			 * if (bLockSuccess) lock.freeKey(voucher.getPk_voucher() == null ?
			 * "maxno_" + voucher.getPk_glorgbook() +
			 * voucher.getPk_vouchertype() + voucher.getYear() +
			 * voucher.getPeriod() : "maxno_" + voucher.getPk_voucher(),
			 * voucher.getPk_prepared(), "gl_voucher");
			 */
		}

		return new String[] { strs[1], voucher.getNo().toString() };
	}

	/**
	 * @param voucher
	 * @throws BusinessException
	 */

}