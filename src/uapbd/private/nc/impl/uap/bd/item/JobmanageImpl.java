package nc.impl.uap.bd.item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.uap.bd.BDException;
import nc.bs.uap.lock.PKLock;
import nc.bs.uif2.VOPersistenceUtil;
import nc.itf.uap.bd.item.IJobbasedoc;
import nc.itf.uap.bd.item.IJobmanagedoc;
import nc.itf.uap.bd.item.IJobmanagedocQuery;
import nc.itf.uap.bd.item.IJobphase;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.bd.service.IBDOperate;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;
import nc.vo.trade.voutils.IFilter;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uapbd.itembasedoc.JobbasfilVO;
import nc.vo.uapbd.itemmngdoc.JobmngfilVO;
import nc.vo.uapbd.itemmngdoc.JobobjphaVO;
import nc.vo.util.tree.LevelTool;

/**
 * 项目管理档案的后台操作实现
 * 
 * @author lixa1
 * @created on 2008.8.15
 * @since NC5.5
 * 
 */
public class JobmanageImpl implements IJobmanagedoc, IJobmanagedocQuery {

	private static final String BD_JOBMNGFIL = "bd_jobmngfil";
	private static final String BD_JOBBASFIL = "bd_jobbasfil";
	private static final String JOB_MNG_NODE_CODE = "10081406";
	private static final String JOB_BAS_NODE_CODE = "10081404";

	public JobmanageImpl() {
		super();
	}

	private String addSquareBrackets(String fieldName) {
		if (fieldName != null && fieldName.trim().length() > 0)
			return " [" + fieldName + "] ";
		else
			return fieldName;
	}

	@SuppressWarnings("unchecked")
	private void checkJobobjphaVOs(JobobjphaVO[] vos) throws BusinessException {
		if (vos == null || vos.length == 0)
			return;

		// 查询项目阶段对应的项目基本档案
		String where = "pk_jobbasfil in (select pk_jobbasfil from bd_jobmngfil where pk_jobmngfil = '"
				+ vos[0].getPk_jobmngfil() + "') ";
		Collection c = new BaseDAO().retrieveByClause(JobbasfilVO.class, where);
		JobbasfilVO jobbasfil = c.size() == 0 ? null : (JobbasfilVO) c
				.iterator().next();
		if (jobbasfil == null)
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MC7",
									new String[] {
											NCLangResOnserver
													.getInstance()
													.getStrByID("10081404",
															"UPP10081404-000001")/* 项目基本档案 */,
											null })); // 项目基本档案不存在

		int pubAccount = 0;
		HashSet uniqeSet = new HashSet();
		for (int i = 0; i < vos.length; i++) {
			JobobjphaVO jobobjphaVO = vos[i];
			if (uniqeSet.contains(jobobjphaVO.getPk_jobphase())) {
				throw new BDException(MultiLangTrans.getTransStr("MC1",
						new String[] { NCLangResOnserver.getInstance()
								.getStrByID("10081406", "UPP10081406-000019") /*
																				 * @res
																				 * "阶段"
																				 */})); // 阶段不能重复
			} else {
				uniqeSet.add(jobobjphaVO.getPk_jobphase());
			}
			if (jobobjphaVO.getPubflag() != null
					&& jobobjphaVO.getPubflag().booleanValue()) {
				pubAccount++; // 公共项目计数
			} else {
				if (jobobjphaVO.getBegindate() == null
						|| jobobjphaVO.getBegindate().toString().trim()
								.length() == 0) {
					throw new BDException(MultiLangTrans.getTransStr("MC3",
							new String[] { addSquareBrackets(NCLangResOnserver
									.getInstance().getStrByID("10081406",
											"UC000-0001892")/*
															 * @res "开始日期"
															 */) })); // 开始日期不能为空
				}
				if (jobobjphaVO.getPrefinisheddate() == null
						|| jobobjphaVO.getPrefinisheddate().toString().trim()
								.length() == 0) {
					throw new BDException(MultiLangTrans.getTransStr("MC3",
							new String[] { addSquareBrackets(NCLangResOnserver
									.getInstance().getStrByID("10081406",
											"UC000-0004223")/*
															 * @res "预计完工日期"
															 */) })); // 预计完工日期不能为空
				}
				if (jobobjphaVO.getPrefinisheddate().before(
						jobobjphaVO.getBegindate())) {
					throw new BDException(MultiLangTrans.getTransStr("MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0004223")/*
																	 * @res
																	 * "预计完工日期"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001892")/*
																	 * @res
																	 * "开始日期"
																	 */) })); // 预计完工日期不能早于开始日期
				}
				if (jobobjphaVO.getEnddate() != null
						&& jobobjphaVO.getEnddate().before(
								jobobjphaVO.getBegindate())) {
					throw new BDException(MultiLangTrans.getTransStr("MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001510")/*
																	 * @res
																	 * "完工日期"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001892")/*
																	 * @res
																	 * "开始日期"
																	 */) })); // 完工日期不能早于开始日期
				}
				if (jobbasfil.getBegindate() != null
						&& jobobjphaVO.getBegindate().before(
								jobbasfil.getBegindate())) {
					throw new BDException(MultiLangTrans.getTransStr("MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001892")/*
																	 * @res
																	 * "开始日期"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000017")/*
																			 * @res
																			 * "项目开始日期"
																			 */) })); // 开始日期不能早于项目开始日期
				}
				if (jobbasfil.getForecastenddate() != null
						&& jobobjphaVO.getPrefinisheddate().after(
								jobbasfil.getForecastenddate())) {
					throw new BDException(MultiLangTrans.getTransStr("MC14",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0004223")/*
																	 * @res
																	 * "预计完工日期"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000018")/*
																			 * @res
																			 * "项目预计完工日期"
																			 */) })); // 预计完工日期不能晚于项目预计完工日期
				}
				if (jobbasfil.getBegindate() != null
						&& jobobjphaVO.getPrefinisheddate().before(
								jobbasfil.getBegindate())) {
					throw new BDException(MultiLangTrans.getTransStr("MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0004223")/*
																	 * @res
																	 * "预计完工日期"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000017")/*
																			 * @res
																			 * "项目开始日期"
																			 */) })); // 预计完工日期不能早于项目开始日期
				}
			}
		}

		// 检查公共阶段是否只有一个
		if (pubAccount > 1) {
			throw new BDException(MultiLangTrans.getTransStr("MC2",
					new String[] {
							NCLangResOnserver.getInstance().getStrByID(
									"10081406", "UPP10081406-000055")/*
																		 * @res
																		 * "公共阶段个数"
																		 */, "1" }));
		} else if (pubAccount == 0) {
			throw new BDException(NCLangResOnserver.getInstance().getStrByID(
					"10081406", "UPP10081406-000056")/* @res "必须设置公共阶段" */);
		}
	}

	private void checkPubflagBeforeUpdate(JobmngfilVO jobmngfil)
			throws BusinessException, BDException {
		// 判断是否修改了公共标记
		if (jobmngfil.getIsPubChanged() != null
				&& jobmngfil.getIsPubChanged().booleanValue()) {
			IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance()
					.lookup(IReferenceCheck.class.getName());
			if (ref.isReferenced(BD_JOBMNGFIL, jobmngfil.getPrimaryKey()))
				throw new BDException(MultiLangTrans.getTransStr("MC8",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UC000-0004165")/*
																	 * @res "项目"
																	 */, null })
						+ ","
						+ MultiLangTrans.getTransStr("MP1",
								new String[] { NCLangRes4VoTransl
										.getNCLangRes().getStrByID("10081404",
												"UPP10081404-000041") /*
																		 * @res
																		 * "修改公共项目标记"
																		 */})); // 项目已经被引用,不能修改公共项目标记
		}
		// 判断公共项目的数目
		if (jobmngfil.getPubflag().booleanValue()) {
			if (isExistPubJobInCorp(jobmngfil.getPk_corp(), jobmngfil
					.getJobBasicInfo().getPk_jobtype(), jobmngfil
					.getPrimaryKey())) {
				throw new BDException(MultiLangTrans.getTransStr("MC6",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UPP10081404-000040")/*
																			 * @res
																			 * "公共项目"
																			 */, null })); // 公共项目已经存在
			}
		}
		// 若是公共项目，删除其路线
		if (jobmngfil.getPubflag().booleanValue()) {
			deleteJobobjphaByJobmngfilPk(jobmngfil.getPrimaryKey());
		}
	}

	@SuppressWarnings("unchecked")
	private void checkSealflag(JobmngfilVO oldVO, JobmngfilVO newVO)
			throws BusinessException {
		// 如果原来未封存,而更新时做了封存,则封存所有下级管理档案
		if (newVO != null && newVO.getSealflag() != null
				&& newVO.getSealflag().booleanValue()) {
			if (oldVO != null
					&& (oldVO.getSealflag() == null || !oldVO.getSealflag()
							.booleanValue())) {
				// 查询下级管理档案
				JobmngfilVO[] subdocs = querySubJobmngfilVOs(newVO);
				if (subdocs == null || subdocs.length == 0)
					return;
				ArrayList subdocPkList = (ArrayList) VOUtil.extractFieldValues(
						subdocs, "pk_jobmngfil", null);

				final BaseDAO baseDAO = new BaseDAO();
				// 更新下级管理档案封存标志
				InSqlBatchCaller caller = new InSqlBatchCaller(subdocPkList);
				try {
					caller.execute(new IInSqlBatchCallBack() {
						String sql = "update bd_jobmngfil set sealflag = 'Y' where pk_jobmngfil in ";

						public Object doWithInSql(String inSql)
								throws BusinessException, SQLException {
							baseDAO.executeUpdate(sql + inSql);
							CacheProxy.fireDataUpdated(BD_JOBMNGFIL, null);
							return null;
						}
					});
				} catch (SQLException e) {
					Logger.error(e.getMessage(), e);
					throw new BDException(e.getMessage());
				}
			}
		}
		// 如果原来已封存,现在解封存,需检查上级是否已解封
		else if (newVO != null
				&& (newVO.getSealflag() == null || !newVO.getSealflag()
						.booleanValue())) {
			if (oldVO != null && oldVO.getSealflag() != null
					&& oldVO.getSealflag().booleanValue()) {
				// 查询上级结点
				JobmngfilVO parent = querySuperJobmngfilVO(newVO);
				// 如果上级存在且已被封存,则抛错
				if (parent == null || parent.getSealflag() == null
						|| !parent.getSealflag().booleanValue())
					return;
				else
					throw new BDException(
							MultiLangTrans
									.getTransStr(
											"MO4",
											new String[] {
													NCLangResOnserver
															.getInstance()
															.getStrByID(
																	"10081406",
																	"UPP10081406-000068")/* 封存 */,
													NCLangResOnserver
															.getInstance()
															.getStrByID(
																	"10081406",
																	"UPP10081406-000069") /* "解除封存" */}));
			}
		}
	}

	public String deleteJobmngfilVO(JobmngfilVO jobmngfil)
			throws BusinessException {
		boolean isLock = false;
		try {
			isLock = PKLock.getInstance().acquireLock(
					jobmngfil.getPrimaryKey(), getUserId(), null);
			if (!isLock) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // 数据已被其他操作员锁定!
			}

			// 版本校验(校验基本档案的版本)
			VOPersistenceUtil.validateVersion(jobmngfil.getJobBasicInfo());

			// 加入合法性检验,在别的地方是否已引用
			if (jobmngfil.getJobBasicInfo().getPk_corp().equals("0001")) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_DATA_DELETE_FAIL()/*
																			 * @res
																			 * "数据删除失败!"
																			 */
						+ "\n"
						+ MultiLangTrans.getTransStr("MP1", NCLangResOnserver
								.getInstance().getStrByID("common",
										"UC001-0000039") /*
															 * @res "删除"
															 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000072") /*
																	 * @res "集团"
																	 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"10081404", "UPT10081404-000001") /*
																			 * @res
																			 * "分配"
																			 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000053") /*
																	 * @res "数据"
																	 */, null)/*
											 * @res "不能删除集团分配数据"
											 */);
			}

			// 判断项目管理档案是否被UAP平台、基本档案数据引用，
			// 或其对应的基本档案(在管理档案所在的公司内)是否被其他档案引用.
			IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance()
					.lookup(IReferenceCheck.class.getName());
			if (ref.isReferenced(BD_JOBMNGFIL, jobmngfil.getPrimaryKey())
					|| ref.isBasePkReferencedInCorp(BD_JOBMNGFIL, jobmngfil
							.getJobBasicInfo().getPrimaryKey(), jobmngfil
							.getPk_corp())) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE() /* @res "数据已经被引用，不能删除!" */);
			}

			// 判断项目管理档案是否被其他业务组数据引用
			JobBillQuery billQuery = new JobBillQuery();
			if (billQuery
					.isJobmngfilReferencedByBill(jobmngfil.getPrimaryKey())) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE() /*
																			 * @res
																			 * "数据已经被引用，不能删除!"
																			 */
						+ "\n"
						+ MultiLangTrans.getTransStr("MC8", new String[] {
								NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000053")/*
																	 * @res "数据"
																	 */,
								NCLangResOnserver.getInstance().getStrByID(
										"common", "UPP10081406-000030") /*
																		 * @res
																		 * "单据"
																		 */})/*
								 * @res "数据被其他单据引用."
								 */);
			}


			// 判断是否存在下级，若为是则不允许删除
			String sql = "select basic.jobcode from bd_jobbasfil basic, bd_jobmngfil manager where manager.pk_jobmngfil = '"
					+ jobmngfil.getPrimaryKey()
					+ "' and manager.pk_jobbasfil = basic.pk_jobbasfil";
			BaseDAO baseDAO = new BaseDAO();
			String results = (String) baseDAO.executeQuery(sql,
					new ColumnProcessor());
			String hasSubLevel = "select count(*) from bd_jobbasfil where jobcode like '"
					+ results
					+ "%' and pk_jobtype = '"
					+ jobmngfil.getJobBasicInfo().getPk_jobtype()
					+ "' and (pk_corp = '0001' or pk_corp = '"
					+ jobmngfil.getPk_corp() + "')";
			Integer count = (Integer) baseDAO.executeQuery(hasSubLevel,
					new ColumnProcessor());
			if (count.intValue() > 1) {
				// 说明当前项目管理档案具有下级节点
				throw new BusinessException(MultiLangTrans
						.getTransStr("MO2", new String[] { NCLangResOnserver
								.getInstance().getStrByID("10081406",
										"UC001-0000039") /* @res "删除" */})); /*
																				 * @res
																				 * "已存在下级节点,不能删除"
																				 */
			}
			// The end for validation

			// 发送事件前通知
			BDOperateServ bdOperaServ = new BDOperateServ();
			bdOperaServ
					.beforeOperate(JOB_MNG_NODE_CODE,
							IBDOperate.BDOPERATION_DEL, jobmngfil
									.getJobBasicInfo().getPrimaryKey(),
							jobmngfil.getPk_corp(), jobmngfil.getPrimaryKey());

			// 删除阶段
			if (jobmngfil.getJobObjectPhase() != null) {
				deleteJobobjphaByJobmngfilPk(jobmngfil.getPrimaryKey());
			}

			// 删除管理档案
			BDDelLog delLog = new BDDelLog();
			delLog.delPKs(BD_JOBMNGFIL, new String[] { jobmngfil
					.getPrimaryKey() });

			baseDAO.deleteVO(jobmngfil);
			CacheProxy.fireDataDeleted(BD_JOBMNGFIL, jobmngfil
					.getPrimaryKey());

			// 删除基础档案
			if (jobmngfil.getPk_corp().equals(
					jobmngfil.getJobBasicInfo().getPk_corp())) {
				if (ref.isReferenced(BD_JOBBASFIL, jobmngfil
						.getJobBasicInfo().getPrimaryKey()))
					throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE());
				
				bdOperaServ.beforeOperate(JOB_BAS_NODE_CODE,
						IBDOperate.BDOPERATION_DEL, jobmngfil.getJobBasicInfo()
								.getPrimaryKey(), null, null);
				CacheProxy.fireDataDeleted(BD_JOBBASFIL, jobmngfil
						.getJobBasicInfo().getPrimaryKey());
				delLog.delPKs(BD_JOBBASFIL, new String[] { jobmngfil
						.getJobBasicInfo().getPrimaryKey() });
				baseDAO.deleteVO(jobmngfil.getJobBasicInfo());
				bdOperaServ.afterOperate(JOB_BAS_NODE_CODE,
						IBDOperate.BDOPERATION_DEL, jobmngfil.getJobBasicInfo()
								.getPrimaryKey(), null, null);
			}

			bdOperaServ
					.afterOperate(JOB_MNG_NODE_CODE,
							IBDOperate.BDOPERATION_DEL, jobmngfil
									.getJobBasicInfo().getPrimaryKey(),
							jobmngfil.getPk_corp(), jobmngfil.getPrimaryKey());

		} finally {
			if (isLock) {
				PKLock.getInstance().releaseLock(jobmngfil.getPrimaryKey(),
						getUserId(), null);
			}
		}
		return null;

	}

	private void deleteJobobjphaByJobmngfilPk(String pk_jobmngfil)
			throws BusinessException {
		if (pk_jobmngfil == null || pk_jobmngfil.trim().length() == 0)
			return;
		String where = "pk_jobmngfil = '" + pk_jobmngfil + "' ";
		CacheProxy.fireDataDeletedByWhereClause("bd_jobobjpha", "pk_jobobjpha",
				where);
		new BaseDAO().deleteByClause(JobobjphaVO.class, where);
		new BDDelLog().delByWhereClause("bd_jobobjpha", "pk_jobobjpha", where);

	}

	private IJobbasedoc getIJobbasedoc() throws BusinessException {
		return (IJobbasedoc) NCLocator.getInstance().lookup(
				IJobbasedoc.class.getName());
	}

	private String getUserId() throws BusinessException {
		return InvocationInfoProxy.getInstance().getUserCode();
	}

	public String insertJobmngfilVOWithBasedoc(JobmngfilVO jobmngfil,
			String pk_corp) throws BusinessException {
		if (jobmngfil == null) {
			return null;
		}

		// 检验公共项目的个数
		if (jobmngfil.getPubflag().booleanValue()) {
			if (isExistPubJobInCorp(pk_corp, jobmngfil.getJobBasicInfo()
					.getPk_jobtype(), null)) {
				throw new BDException(MultiLangTrans.getTransStr("MC6",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UPP10081404-000040")/*
																			 * @res
																			 * "公共项目"
																			 */, null })); // 公共项目已经存在
			}
		}

		// 新增一基础档案
		JobbasfilVO jobBDVO = jobmngfil.getJobBasicInfo();
		// 同步基本、管理档案的封存标志（管理档案的封存标志冗余）
		jobBDVO.setSealflag(jobmngfil.getSealflag());
		String pk_jobbasfil = getIJobbasedoc().insertJobbasfilVO(jobBDVO);

		// 发送事前通知
		BDOperateServ bdOperServ = new BDOperateServ();
		bdOperServ.beforeOperate(JOB_MNG_NODE_CODE,
				IBDOperate.BDOPERATION_INSERT, null, null, jobmngfil);

		// 保存新增的管理档案
		jobmngfil.setPk_jobbasfil(pk_jobbasfil);
		jobmngfil.setPk_corp(pk_corp);
		String pk_jobmngfil = new BaseDAO().insertVO(jobmngfil);

		// 发送事件后通知
		bdOperServ.afterOperate(JOB_MNG_NODE_CODE,
				IBDOperate.BDOPERATION_INSERT, null, null, jobmngfil);
		// 发送缓存通知
		CacheProxy.fireDataInserted(BD_JOBMNGFIL, pk_jobmngfil);

		// 保存项目路线 (移到insertJobmngfilVO方法中处理，为了减少一次新增项目路线的再查询)

		String sPK = pk_jobbasfil + "," + pk_jobmngfil;
		return sPK;
	}

	private boolean isExistPubJobInCorp(String pk_corp, String pk_jobtype,
			String pk_jobmngfil) throws BusinessException {
		String sql = "select count(*) from bd_jobmngfil where pubflag='Y' ";
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			sql += "and pk_corp = '" + pk_corp + "' ";
		}
		if (pk_jobtype != null && pk_jobtype.trim().length() > 0) {
			sql += "and pk_jobbasfil in (select pk_jobbasfil from bd_jobbasfil where pk_jobtype = '"
					+ pk_jobtype + "') ";
		}
		if (pk_jobmngfil != null && pk_jobmngfil.trim().length() > 0) {
			sql += "and pk_jobmngfil <> '" + pk_jobmngfil + "' ";
		}
		Object obj = new BaseDAO().executeQuery(sql, new ColumnProcessor());
		return obj == null ? false : ((Integer) obj).intValue() > 0;
	}

	private boolean isJobtypePhasedByJobmngfilPk(String pk_jobmngfil)
			throws BusinessException {
		String sql = "select jobphaseflag from bd_jobtype,bd_jobbasfil,bd_jobmngfil "
				+ "where bd_jobtype.pk_jobtype = bd_jobbasfil.pk_jobtype and "
				+ "bd_jobbasfil.pk_jobbasfil = bd_jobmngfil.pk_jobbasfil and "
				+ "bd_jobmngfil.pk_jobmngfil = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_jobmngfil);
		Object obj = new BaseDAO().executeQuery(sql, param,
				new ColumnProcessor());
		return obj == null ? false : new UFBoolean((String) obj).booleanValue();
	}

	@SuppressWarnings("unchecked")
	public JobmngfilVO[] queryAllJobmngfilVOsByJobtype(String pk_jobtype,
			String pk_corp) throws BusinessException {
		// 查询管理档案
		JobmngfilVO[] jobmngfils = queryAllJobmngfilVOsSimpleByJobtype(
				pk_jobtype, pk_corp);
		if (jobmngfils == null)
			return null;

		// 查询基本档案
		HashMap jobbasfilPk_jobbasfil_map = queryJobbasfilVOsByJobmngfilVOs(jobmngfils);

		// 查询项目路线
		ArrayList jobobjphaList = queryJobobjphaVOListByJobmngfilVOs(jobmngfils);
		Map<String, List<JobobjphaVO>> jobobjphaVOsMap = handleJobobjphaVOList2Map(jobobjphaList);

		// 为管理档案设置基本档案和项目路线
		for (int i = 0; i < jobmngfils.length; i++) {
			JobmngfilVO jobmngfilVO = jobmngfils[i];
			// 设置基本档案
			jobmngfilVO.setJobBasicInfo((JobbasfilVO) jobbasfilPk_jobbasfil_map
					.get(jobmngfilVO.getPk_jobbasfil()));
			// 取项目路线
			if (jobobjphaVOsMap != null
					&& jobobjphaVOsMap.get(jobmngfilVO.getPrimaryKey()) != null) {
				jobmngfilVO.setJobObjectPhase(jobobjphaVOsMap.get(
						jobmngfilVO.getPrimaryKey())
						.toArray(new JobobjphaVO[0]));
			}
		}

		// 按基本档案编码为管理档案排序
		sortJobmngfilVOsByJobcode(jobmngfils);
		return jobmngfils;
	}

	@SuppressWarnings("unchecked")
	private JobmngfilVO[] queryAllJobmngfilVOsSimpleByJobtype(
			String pk_jobtype, String pk_corp) throws BusinessException {
		String where = "1=1 ";
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where += "and pk_corp = '" + pk_corp + "' ";
		}
		if (pk_jobtype != null && pk_jobtype.trim().length() > 0) {
			where += "and pk_jobbasfil in (select pk_jobbasfil from bd_jobbasfil "
					+ "where pk_jobtype = '" + pk_jobtype + "') ";
		}
		Collection c = new BaseDAO().retrieveByClause(JobmngfilVO.class, where);
		return c == null || c.size() == 0 ? null : (JobmngfilVO[]) c
				.toArray(new JobmngfilVO[c.size()]);
	}

	/**
	 * 根据项目管理档案主键查询基本档案
	 * 
	 * @param jobmngfils
	 * @param baseDAO
	 * @return
	 * @throws BusinessException
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	private HashMap queryJobbasfilVOsByJobmngfilVOs(JobmngfilVO[] jobmngfils)
			throws BusinessException, BDException {
		final BaseDAO baseDAO = new BaseDAO();
		ArrayList jobbasfilPkList = (ArrayList) VOUtil.extractFieldValues(
				jobmngfils, "pk_jobbasfil", null);
		InSqlBatchCaller baseCaller = new InSqlBatchCaller(jobbasfilPkList);
		ArrayList jobbasfilList = null;
		HashMap jobbasfilPk_jobbasfil_map = new HashMap();
		try {
			jobbasfilList = (ArrayList) baseCaller
					.execute(new IInSqlBatchCallBack() {
						ArrayList rs = new ArrayList();

						public Object doWithInSql(String inSql)
								throws BusinessException, SQLException {
							String where = "pk_jobbasfil in " + inSql;
							Collection c = baseDAO.retrieveByClause(
									JobbasfilVO.class, where);
							rs.addAll(c);
							return rs;
						}
					});
		} catch (BusinessException e1) {
			throw e1;
		} catch (SQLException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new BDException(e1.getMessage());
		}
		if (jobbasfilList != null && jobbasfilList.size() > 0) {
			for (Iterator iter = jobbasfilList.iterator(); iter.hasNext();) {
				JobbasfilVO jobbasfil = (JobbasfilVO) iter.next();
				jobbasfilPk_jobbasfil_map.put(jobbasfil.getPrimaryKey(),
						jobbasfil);
			}
		}
		return jobbasfilPk_jobbasfil_map;
	}

	@SuppressWarnings("unchecked")
	public HashMap queryJobmngfilPkJobtypePkMap(final String[] jobmngfilPks)
			throws BusinessException {
		HashMap jobmngfilPk_jobtypePk_map = new HashMap();
		if (jobmngfilPks == null || jobmngfilPks.length == 0)
			return jobmngfilPk_jobtypePk_map;

		final BaseDAO baseDAO = new BaseDAO();
		InSqlBatchCaller caller = new InSqlBatchCaller(jobmngfilPks);
		try {
			jobmngfilPk_jobtypePk_map = (HashMap) caller
					.execute(new IInSqlBatchCallBack() {
						HashMap hm = new HashMap();

						public Object doWithInSql(String inSql)
								throws BusinessException, SQLException {
							String sql = "select pk_jobtype,pk_jobmngfil from bd_jobbasfil,bd_jobmngfil "
									+ "where bd_jobbasfil.pk_jobbasfil = bd_jobmngfil.pk_jobbasfil and "
									+ "pk_jobmngfil in " + inSql;
							baseDAO.executeQuery(sql, new ResultSetProcessor() {
								private static final long serialVersionUID = -2044031941435505552L;

								public Object handleResultSet(ResultSet rs)
										throws SQLException {
									while (rs.next()) {
										String pk_jobtype = rs.getString(1);
										String pk_jobmngfil = rs.getString(2);
										hm.put(pk_jobmngfil, pk_jobtype);
									}
									return hm;
								}

							});
							return hm;
						}
					});
		} catch (BusinessException e1) {
			throw e1;
		} catch (SQLException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new BDException(e1.getMessage());
		}
		return jobmngfilPk_jobtypePk_map;
	}

	@SuppressWarnings("unchecked")
	public JobmngfilVO[] queryJobmngfilVOsByCondition(String pk_jobtype,
			String pk_corp, String condition) throws BusinessException {
		JobmngfilVO[] jobmngfils = null;
		// 查询管理档案
		jobmngfils = queryJobmngfilVOsSimpleByCondition(pk_jobtype, pk_corp,
				condition);
		if (jobmngfils == null)
			return null;

		// 查询基本档案
		HashMap jobbasfilPk_jobbasfil_map = queryJobbasfilVOsByJobmngfilVOs(jobmngfils);

		// 查询项目路线(包括部门名称和负责人名称)
		ArrayList jobobjphaList = queryJobobjphaVOListByJobmngfilVOs(jobmngfils);
		Map<String, List<JobobjphaVO>> jobobjphaVOsMap = handleJobobjphaVOList2Map(jobobjphaList);

		// 为管理档案设置基本档案和项目路线
		for (int i = 0; i < jobmngfils.length; i++) {
			JobmngfilVO jobmngfilVO = jobmngfils[i];
			// 设置基本档案
			jobmngfilVO.setJobBasicInfo((JobbasfilVO) jobbasfilPk_jobbasfil_map
					.get(jobmngfilVO.getPk_jobbasfil()));
			// 取项目路线
			if (jobobjphaVOsMap != null
					&& jobobjphaVOsMap.get(jobmngfilVO.getPrimaryKey()) != null) {
				jobmngfilVO.setJobObjectPhase(jobobjphaVOsMap.get(
						jobmngfilVO.getPrimaryKey())
						.toArray(new JobobjphaVO[0]));
			}
		}
		return jobmngfils;
	}

	@SuppressWarnings("unchecked")
	private JobmngfilVO[] queryJobmngfilVOsSimpleByCondition(String pk_jobtype,
			String pk_corp, String condition) throws BusinessException {
		String select = "select ";
		JobmngfilVO jobmngfilVO = new JobmngfilVO();
		String[] jobmngfilFields = jobmngfilVO.getAttributeNames();
		for (int i = 0; i < jobmngfilFields.length; i++) {
			String jobmngfilField = jobmngfilFields[i];
			if (!jobmngfilField.equals("ispubchanged")
					&& !jobmngfilField.equals("jobbasicinfo")
					&& !jobmngfilField.equals("jobobjectphase")) {
				if (i != 0)
					select += ",";
				select += "bd_jobmngfil." + jobmngfilField + " ";
			}
		}
		String from = "from bd_jobbasfil,bd_jobmngfil ";
		String where = "where bd_jobbasfil.pk_jobbasfil=bd_jobmngfil.pk_jobbasfil ";
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where += "and bd_jobmngfil.pk_corp = '" + pk_corp.trim() + "' ";
		}
		if (pk_jobtype != null && pk_jobtype.trim().length() > 0) {
			where += "and bd_jobbasfil.pk_jobtype = '" + pk_jobtype.trim()
					+ "' ";
		}
		if (condition != null && condition.trim().length() > 0) {
			where += "and (" + condition + ") ";
		}

		String order = "order by bd_jobbasfil.jobcode ";
		String sql = select + from + where + order;
		// 明确书写bd_jobmngfil.dr，防止两表联查（都含有dr）时，出现不明确的情况
		sql = sql.replace("isnull(dr,0)", "isnull(bd_jobmngfil.dr,0)");
		List resultList = (List) new BaseDAO().executeQuery(sql,
				new BeanListProcessor(JobmngfilVO.class));
		return resultList == null || resultList.size() == 0 ? null
				: (JobmngfilVO[]) resultList.toArray(new JobmngfilVO[resultList
						.size()]);
	}

	@SuppressWarnings("unchecked")
	private Map handleJobobjphaVOList2Map(ArrayList<JobobjphaVO> jobobjphaVOList) {
		if (jobobjphaVOList == null || jobobjphaVOList.size() == 0)
			return null;

		Map<String, List<JobobjphaVO>> pk2PhasesMap = new HashMap<String, List<JobobjphaVO>>();
		for (int i = 0; i < jobobjphaVOList.size(); i++) {
			JobobjphaVO currentPhase = (JobobjphaVO) jobobjphaVOList.get(i);
			if (pk2PhasesMap.containsKey(currentPhase.getPk_jobmngfil())) {
				pk2PhasesMap.get(currentPhase.getPk_jobmngfil()).add(
						currentPhase);
			} else {
				List<JobobjphaVO> newPhaseList = new ArrayList<JobobjphaVO>();
				newPhaseList.add(currentPhase);
				pk2PhasesMap.put(currentPhase.getPk_jobmngfil(), newPhaseList);
			}
		}
		return pk2PhasesMap;
	}

	/**
	 * 根据项目管理档案主键查询项目路线(包括部门名称和负责人名称)
	 * 
	 * @param jobmngfils
	 * @return
	 * @throws BusinessException
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	private ArrayList queryJobobjphaVOListByJobmngfilVOs(
			JobmngfilVO[] jobmngfils) throws BusinessException, BDException {
		ArrayList jobobjphaList = queryJobobjphaVOListSimpleByJobmngfilVOs(jobmngfils);

		// if (jobobjphaList != null && jobobjphaList.size() > 0) {
		// JobobjphaVO[] jobobjphas = (JobobjphaVO[]) jobobjphaList
		// .toArray(new JobobjphaVO[jobobjphaList.size()]);
		// // // 查询部门名称
		// // HashMap deptPk_deptname_map =
		// queryDeptnameForJobobjphaVOs(jobobjphas);
		// // // 查询负责人名称
		// // HashMap psnPk_psnname_map =
		// queryPsnnameForJobobjphaVOs(jobobjphas);
		// // 根据管理档案查询项目路线对应的项目阶段名称
		// // HashMap phasePk_phasename_map =
		// queryPhasenameForJobobjphaVOs(jobobjphas);
		// // 为项目路线设置部门名称和负责人名称
		// for (int i = 0; i < jobobjphas.length; i++) {
		// JobobjphaVO jobobjphaVO = jobobjphas[i];
		// // jobobjphaVO.setdeptdoc((String) deptPk_deptname_map
		// // .get(jobobjphaVO.getPk_deptdoc()));
		// // jobobjphaVO.setpsndoc((String) psnPk_psnname_map
		// // .get(jobobjphaVO.getPk_psndoc()));
		// // jobobjphaVO.setjobphase((String) phasePk_phasename_map
		// // .get(jobobjphaVO.getPk_jobphase()));
		// }
		// }
		return jobobjphaList;
	}

	/**
	 * 根据项目管理档案主键查询项目路线(不包括部门名称和负责人名称)
	 * 
	 * @param jobmngfils
	 * @param baseDAO
	 * @return
	 * @throws BusinessException
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	private ArrayList queryJobobjphaVOListSimpleByJobmngfilVOs(
			JobmngfilVO[] jobmngfils) throws BusinessException, BDException {
		final BaseDAO baseDAO = new BaseDAO();
		ArrayList jobmngfilPkList = (ArrayList) VOUtil.extractFieldValues(
				jobmngfils, "pk_jobmngfil", null);
		InSqlBatchCaller phaCaller = new InSqlBatchCaller(jobmngfilPkList);
		ArrayList jobobjphaList = null;
		try {
			jobobjphaList = (ArrayList) phaCaller
					.execute(new IInSqlBatchCallBack() {
						ArrayList rs = new ArrayList();

						public Object doWithInSql(String inSql)
								throws BusinessException, SQLException {
							String where = "pk_jobmngfil in " + inSql;
							String orderBy = " pk_jobmngfil, inumber";
							Collection c = baseDAO.retrieveByClause(
									JobobjphaVO.class, where, orderBy);
							rs.addAll(c);
							return rs;
						}
					});
		} catch (BusinessException e1) {
			throw e1;
		} catch (SQLException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new BDException(e1.getMessage());
		}
		return jobobjphaList;
	}

	@SuppressWarnings("unchecked")
	public String queryPubJobobjphaPk(String pk_jobmngfil)
			throws BusinessException {
		JobmngfilVO jobmngfil = (JobmngfilVO) new BaseDAO().retrieveByPK(
				JobmngfilVO.class, pk_jobmngfil);
		if (jobmngfil != null) {
			// 如果当前管理档案是公共项目,则返回null
			if (jobmngfil.getPubflag().booleanValue())
				return null;
			// 如果当前管理档案所属项目类型不分阶段,则返回null
			if (!isJobtypePhasedByJobmngfilPk(pk_jobmngfil))
				return null;
			// 查找管理档案对应的项目路线
			ArrayList jobobjphaList = queryJobobjphaVOListSimpleByJobmngfilVOs(new JobmngfilVO[] { jobmngfil });
			// 查找公共项目路线
			if (jobobjphaList != null && jobobjphaList.size() > 0) {
				for (Iterator iter = jobobjphaList.iterator(); iter.hasNext();) {
					JobobjphaVO jobobjpha = (JobobjphaVO) iter.next();
					if (jobobjpha.getPubflag().booleanValue())
						return jobobjpha.getPrimaryKey();
				}
			}
		}

		// 如果项目管理档案为空,或指定了项目分阶段,又找不到公共项目路线,则抛出错误
		throw new BDException(NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"10081404", "UPP10081404-000042")/* @res "非法单据" */);
	}

	@SuppressWarnings("unchecked")
	private JobmngfilVO[] querySubJobmngfilVOs(JobmngfilVO jobmngfil)
			throws BusinessException {
		if (jobmngfil == null)
			return null;

		BaseDAO baseDAO = new BaseDAO();
		JobbasfilVO basefil = jobmngfil.getJobBasicInfo() == null ? (JobbasfilVO) baseDAO
				.retrieveByPK(JobbasfilVO.class, jobmngfil.getPk_jobbasfil())
				: jobmngfil.getJobBasicInfo();
		String jobcode = basefil == null ? null : basefil.getJobcode();
		if (jobcode == null || jobcode.trim().length() == 0)
			return null;
		String where = "pk_corp = '" + jobmngfil.getPk_corp()
				+ "' and pk_jobbasfil in "
				+ "(select pk_jobbasfil from bd_jobbasfil where pk_jobtype = '"
				+ basefil.getPk_jobtype() + "' and jobcode like '" + jobcode
				+ "_%')";
		Collection c = baseDAO.retrieveByClause(JobmngfilVO.class, where);
		return c == null || c.size() == 0 ? null : (JobmngfilVO[]) c
				.toArray(new JobmngfilVO[c.size()]);
	}

	@SuppressWarnings("unchecked")
	private JobmngfilVO querySuperJobmngfilVO(JobmngfilVO jobmngfil)
			throws BusinessException {
		if (jobmngfil == null)
			return null;

		BaseDAO baseDAO = new BaseDAO();

		JobbasfilVO basefil = jobmngfil.getJobBasicInfo() == null ? (JobbasfilVO) baseDAO
				.retrieveByPK(JobbasfilVO.class, jobmngfil.getPk_jobbasfil())
				: jobmngfil.getJobBasicInfo();
		if (basefil == null)
			return null;

		JobtypeVO jobtype = (JobtypeVO) baseDAO.retrieveByPK(JobtypeVO.class,
				basefil.getPk_jobtype());
		if (jobtype == null)
			return null;

		String codeRule = jobtype.getJobclclass();
		String jobcode = basefil.getJobcode();
		LevelTool lt = new LevelTool(10, 40);
		lt.checkString(codeRule);
		String parentCode = lt.getParentLevel(jobcode);
		if (parentCode == null || parentCode.trim().length() == 0)
			return null;
		;

		String where = "pk_corp = '" + jobmngfil.getPk_corp()
				+ "' and pk_jobbasfil in "
				+ "(select pk_jobbasfil from bd_jobbasfil where pk_jobtype = '"
				+ basefil.getPk_jobtype() + "' and jobcode = '" + parentCode
				+ "')";
		Collection c = baseDAO.retrieveByClause(JobmngfilVO.class, where);
		return c == null || c.size() == 0 ? null : (JobmngfilVO) c.iterator()
				.next();
	}

	@SuppressWarnings("unchecked")
	public JobobjphaVO[] saveJobobjphaVOs(JobobjphaVO[] jobobjphas)
			throws BusinessException {
		if (jobobjphas == null || jobobjphas.length == 0)
			return null;

		JobobjphaVO[] newJobobjphas = null;

		boolean isLock = false;
		String[] keys = null;
		try {
			List keyList = VOUtil.extractFieldValues(jobobjphas,
					"pk_jobobjpha", new IFilter() {

						public boolean accept(Object o) {
							if (o != null && o instanceof JobobjphaVO) {
								String pk = ((JobobjphaVO) o).getPrimaryKey();
								if (pk != null && pk.trim().length() > 0)
									return true;
							}
							return false;
						}

					});
			keys = (String[]) keyList.toArray(new String[keyList.size()]);
			if (keys != null && keys.length > 0) {
				isLock = PKLock.getInstance().acquireBatchLock(keys,
						getUserId(), null);
				if (!isLock) {
					throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // 数据已被其他操作员锁定!
				}
			}
			JobobjphaVO[] notDeletedVOs = (JobobjphaVO[]) VOUtil.filter(
					jobobjphas, new IFilter() {
						public boolean accept(Object o) {
							if (o != null
									&& o instanceof JobobjphaVO
									&& ((JobobjphaVO) o).getStatus() != VOStatus.DELETED)
								return true;
							else
								return false;
						}
					});
			// 检查未被删除的阶段VO数组
			checkJobobjphaVOs(notDeletedVOs);

			// 检验阶段是否被使用
			IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance()
					.lookup(IReferenceCheck.class.getName());

			BaseDAO baseDAO = new BaseDAO();

			for (int i = 0; i < jobobjphas.length; i++) {
				if (jobobjphas[i].getStatus() == VOStatus.DELETED
						&& ref.isReferenced("bd_jobobjpha", jobobjphas[i]
								.getPrimaryKey())) {
					throw new BDException(BDMsg.MSG_REF_NOT_DELETE());
				}

				if (jobobjphas[i].getStatus() == VOStatus.DELETED) {
					baseDAO.deleteVO(jobobjphas[i]);
					CacheProxy.fireDataDeleted("bd_jobobjpha", jobobjphas[i]
							.getPrimaryKey());
					new BDDelLog().delPKs("bd_jobobjpha",
							new String[] { jobobjphas[i].getPrimaryKey() });
				} else if (jobobjphas[i].getStatus() == VOStatus.NEW) {
					String pk_jobobjpha = baseDAO.insertVO(jobobjphas[i]);
					CacheProxy.fireDataInserted("bd_jobobjpha", pk_jobobjpha);
				} else if (jobobjphas[i].getStatus() == VOStatus.UPDATED) {
					baseDAO.updateVO(jobobjphas[i]);
					CacheProxy.fireDataUpdated("bd_jobobjpha", jobobjphas[i]
							.getPrimaryKey());
				}
			}

			// 查询新的项目路线VO数组
			if (jobobjphas[0].getPk_jobmngfil() == null
					|| jobobjphas[0].getPk_jobmngfil().trim().length() == 0) {
				return null;
			} else {
				JobmngfilVO jobmngfil = (JobmngfilVO) baseDAO.retrieveByPK(
						JobmngfilVO.class, jobobjphas[0].getPk_jobmngfil());
				ArrayList newJobobjphaList = queryJobobjphaVOListByJobmngfilVOs(new JobmngfilVO[] { jobmngfil });
				newJobobjphas = newJobobjphaList == null
						|| newJobobjphaList.size() == 0 ? null
						: (JobobjphaVO[]) newJobobjphaList
								.toArray(new JobobjphaVO[newJobobjphaList
										.size()]);
			}
		} finally {
			if (isLock) {
				PKLock.getInstance().releaseBatchLock(keys, getUserId(), null);
			}
		}
		return newJobobjphas;
	}

	@SuppressWarnings("unchecked")
	private void sortJobmngfilVOsByJobcode(JobmngfilVO[] jobmngfils) {
		Arrays.sort(jobmngfils, new Comparator<JobmngfilVO>() {

			public int compare(JobmngfilVO o1, JobmngfilVO o2) {
				JobbasfilVO base1 = o1.getJobBasicInfo();
				JobbasfilVO base2 = o2.getJobBasicInfo();
				return base1.getJobcode().compareTo(base2.getJobcode());
			}
		});
	}

	public String updateJobmngfilVO(JobmngfilVO jobmngfil)
			throws BusinessException {
		String result = null;
		boolean isLock = false;
		try {
			isLock = PKLock.getInstance().acquireLock(
					jobmngfil.getPrimaryKey(), getUserId(), null);
			if (!isLock) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // 数据已被其他操作员锁定!
			}

			// 版本校验（校验基本档案VO的版本）
			VOPersistenceUtil.validateVersion(jobmngfil.getJobBasicInfo());

			// 检查公共项目
			checkPubflagBeforeUpdate(jobmngfil);

			BaseDAO baseDAO = new BaseDAO();

			// 封存时,要同时封存下级管理档案
			JobmngfilVO oldVO = (JobmngfilVO) baseDAO.retrieveByPK(
					JobmngfilVO.class, jobmngfil.getPrimaryKey());
			checkSealflag(oldVO, jobmngfil);

			// 基本档案更新
			JobbasfilVO jobBDVO = jobmngfil.getJobBasicInfo();
			if (!jobmngfil.getJobBasicInfo().getPk_corp().equals("0001")) {
				// 针对非分配的情况，直接更新

			} else {
				// 针对分配的情况：需先同步基本、管理档案的封存标志（基本档案的封存标志冗余, 且已无实际意义，仅为了向前兼容）
				jobBDVO.setSealflag(jobmngfil.getSealflag());
			}
			getIJobbasedoc().updateJobbasfilVO(jobBDVO);

			// 更新管理档案
			baseDAO.updateVO(jobmngfil);
			CacheProxy.fireDataUpdated(BD_JOBMNGFIL, jobmngfil
					.getPrimaryKey());
		} finally {
			if (isLock) {
				PKLock.getInstance().releaseLock(jobmngfil.getPrimaryKey(),
						getUserId(), null);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Object insertJobmngfilVO(JobmngfilVO jobmngfilVO)
			throws BusinessException {
		// 加锁
		new PfBusinessLock().lock(new JobMngLockData(
				new JobmngfilVO[] { jobmngfilVO }), null);

		String pk_basic_mng = insertJobmngfilVOWithBasedoc(jobmngfilVO,
				jobmngfilVO.getPk_corp());

		/*
		 * pk_basic_mng 是两个Pk的组合： "pk_jobbasfil, pk_jobmngfil"
		 */
		int commaLocation = pk_basic_mng.indexOf(",");
		String pk_jobbasfil = pk_basic_mng.substring(0, commaLocation);
		String pk_jobmngfil = pk_basic_mng.substring(commaLocation + 1);

		// 保存项目路线
		JobobjphaVO[] phaVOs = jobmngfilVO.getJobObjectPhase();
		if (phaVOs != null && phaVOs.length > 0) {
			for (JobobjphaVO vo : phaVOs) {
				vo.setPk_jobmngfil(pk_jobmngfil);
			}
		}
		phaVOs = getJobPhaseService().saveJobobjphaVOs(phaVOs);

		// 重新检索出插入的项目管理档案VO
		BaseDAO baseDAO = new BaseDAO();
		JobmngfilVO jobMngVO = (JobmngfilVO) baseDAO.retrieveByPK(
				JobmngfilVO.class, pk_jobmngfil);
		JobbasfilVO jobVO = (JobbasfilVO) baseDAO.retrieveByPK(
				JobbasfilVO.class, pk_jobbasfil);
		jobMngVO.setJobBasicInfo(jobVO);
		jobMngVO.setJobObjectPhase(phaVOs);

		return jobMngVO;
	}

	@SuppressWarnings("unchecked")
	public Object updateJobmngfilVOWithBasedoc(JobmngfilVO jobmngfilVO)
			throws BusinessException {
		BaseDAO baseDAO = new BaseDAO();
		// 判断当前项目管理档案更新前后“完工”,“封存”字段是否有变化
		String querySQL = "select nvl(finishedflag,'N'), sealflag from bd_jobbasfil where pk_jobbasfil = (select pk_jobbasfil from bd_jobmngfil where pk_jobmngfil = '"
				+ jobmngfilVO.getPrimaryKey() + "')"; // by tcl
		Object[] flags = (Object[]) baseDAO.executeQuery(querySQL,
				new ArrayProcessor());
		UFBoolean oldFinishedFlag = new UFBoolean(flags[0].toString());
		UFBoolean newFinishedFlag = jobmngfilVO.getJobBasicInfo()
				.getFinishedflag();
		UFBoolean oldSealFlag = new UFBoolean(flags[1].toString());
		UFBoolean newSealFlag = jobmngfilVO.getSealflag();

		// 更新当前项目管理档案以及对应的项目基本档案，项目路线
		// LiFIXME 合理的设计应该是：更新项目管理档案的同时更新基本档案、项目路线(而不是单做)
		updateJobmngfilVO(jobmngfilVO);
		JobobjphaVO[] phaseVOs = getJobPhaseService().saveJobobjphaVOs(
				jobmngfilVO.getJobObjectPhase());

		// 查询待返回的被更新的项目管理档案
		if ((!oldFinishedFlag.booleanValue() && newFinishedFlag.booleanValue())
				|| (!oldSealFlag.booleanValue() && newSealFlag.booleanValue())) {
			// 如果当前档案以前没被完工，而现在被置为完工，
			// 或者，如果当前档案以前没被封存，而现在被封存了，
			// 则所有子项目也均要被置为完工或封存，所以需要检索出变更的所有集合

			JobmngfilVO[] jobMngVOs = null;
			String newJobCode = jobmngfilVO.getJobCode();
			String condition = "bd_jobbasfil.jobcode like '" + newJobCode
					+ "%'";
			jobMngVOs = queryJobmngfilVOsByCondition(jobmngfilVO
					.getJobBasicInfo().getPk_jobtype(), jobmngfilVO
					.getPk_corp(), condition);
			return jobMngVOs;
		}

		// 只返回当前被更新的项目管理档案
		JobmngfilVO jobMngVO = (JobmngfilVO) baseDAO.retrieveByPK(
				JobmngfilVO.class, jobmngfilVO.getPrimaryKey());
		JobbasfilVO jobVO = (JobbasfilVO) baseDAO.retrieveByPK(
				JobbasfilVO.class, jobMngVO.getPk_jobbasfil());
		jobMngVO.setJobBasicInfo(jobVO);
		jobMngVO.setJobObjectPhase(phaseVOs);

		return jobMngVO;
	}

	private IJobphase getJobPhaseService() {
		return (NCLocator.getInstance().lookup(IJobphase.class));
	}

}
