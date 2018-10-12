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
 * ��Ŀ�������ĺ�̨����ʵ��
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

		// ��ѯ��Ŀ�׶ζ�Ӧ����Ŀ��������
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
															"UPP10081404-000001")/* ��Ŀ�������� */,
											null })); // ��Ŀ��������������

		int pubAccount = 0;
		HashSet uniqeSet = new HashSet();
		for (int i = 0; i < vos.length; i++) {
			JobobjphaVO jobobjphaVO = vos[i];
			if (uniqeSet.contains(jobobjphaVO.getPk_jobphase())) {
				throw new BDException(MultiLangTrans.getTransStr("MC1",
						new String[] { NCLangResOnserver.getInstance()
								.getStrByID("10081406", "UPP10081406-000019") /*
																				 * @res
																				 * "�׶�"
																				 */})); // �׶β����ظ�
			} else {
				uniqeSet.add(jobobjphaVO.getPk_jobphase());
			}
			if (jobobjphaVO.getPubflag() != null
					&& jobobjphaVO.getPubflag().booleanValue()) {
				pubAccount++; // ������Ŀ����
			} else {
				if (jobobjphaVO.getBegindate() == null
						|| jobobjphaVO.getBegindate().toString().trim()
								.length() == 0) {
					throw new BDException(MultiLangTrans.getTransStr("MC3",
							new String[] { addSquareBrackets(NCLangResOnserver
									.getInstance().getStrByID("10081406",
											"UC000-0001892")/*
															 * @res "��ʼ����"
															 */) })); // ��ʼ���ڲ���Ϊ��
				}
				if (jobobjphaVO.getPrefinisheddate() == null
						|| jobobjphaVO.getPrefinisheddate().toString().trim()
								.length() == 0) {
					throw new BDException(MultiLangTrans.getTransStr("MC3",
							new String[] { addSquareBrackets(NCLangResOnserver
									.getInstance().getStrByID("10081406",
											"UC000-0004223")/*
															 * @res "Ԥ���깤����"
															 */) })); // Ԥ���깤���ڲ���Ϊ��
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
																	 * "Ԥ���깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001892")/*
																	 * @res
																	 * "��ʼ����"
																	 */) })); // Ԥ���깤���ڲ������ڿ�ʼ����
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
																	 * "�깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081406",
													"UC000-0001892")/*
																	 * @res
																	 * "��ʼ����"
																	 */) })); // �깤���ڲ������ڿ�ʼ����
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
																	 * "��ʼ����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000017")/*
																			 * @res
																			 * "��Ŀ��ʼ����"
																			 */) })); // ��ʼ���ڲ���������Ŀ��ʼ����
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
																	 * "Ԥ���깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000018")/*
																			 * @res
																			 * "��ĿԤ���깤����"
																			 */) })); // Ԥ���깤���ڲ���������ĿԤ���깤����
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
																	 * "Ԥ���깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081406",
													"UPP10081406-000017")/*
																			 * @res
																			 * "��Ŀ��ʼ����"
																			 */) })); // Ԥ���깤���ڲ���������Ŀ��ʼ����
				}
			}
		}

		// ��鹫���׶��Ƿ�ֻ��һ��
		if (pubAccount > 1) {
			throw new BDException(MultiLangTrans.getTransStr("MC2",
					new String[] {
							NCLangResOnserver.getInstance().getStrByID(
									"10081406", "UPP10081406-000055")/*
																		 * @res
																		 * "�����׶θ���"
																		 */, "1" }));
		} else if (pubAccount == 0) {
			throw new BDException(NCLangResOnserver.getInstance().getStrByID(
					"10081406", "UPP10081406-000056")/* @res "�������ù����׶�" */);
		}
	}

	private void checkPubflagBeforeUpdate(JobmngfilVO jobmngfil)
			throws BusinessException, BDException {
		// �ж��Ƿ��޸��˹������
		if (jobmngfil.getIsPubChanged() != null
				&& jobmngfil.getIsPubChanged().booleanValue()) {
			IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance()
					.lookup(IReferenceCheck.class.getName());
			if (ref.isReferenced(BD_JOBMNGFIL, jobmngfil.getPrimaryKey()))
				throw new BDException(MultiLangTrans.getTransStr("MC8",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UC000-0004165")/*
																	 * @res "��Ŀ"
																	 */, null })
						+ ","
						+ MultiLangTrans.getTransStr("MP1",
								new String[] { NCLangRes4VoTransl
										.getNCLangRes().getStrByID("10081404",
												"UPP10081404-000041") /*
																		 * @res
																		 * "�޸Ĺ�����Ŀ���"
																		 */})); // ��Ŀ�Ѿ�������,�����޸Ĺ�����Ŀ���
		}
		// �жϹ�����Ŀ����Ŀ
		if (jobmngfil.getPubflag().booleanValue()) {
			if (isExistPubJobInCorp(jobmngfil.getPk_corp(), jobmngfil
					.getJobBasicInfo().getPk_jobtype(), jobmngfil
					.getPrimaryKey())) {
				throw new BDException(MultiLangTrans.getTransStr("MC6",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UPP10081404-000040")/*
																			 * @res
																			 * "������Ŀ"
																			 */, null })); // ������Ŀ�Ѿ�����
			}
		}
		// ���ǹ�����Ŀ��ɾ����·��
		if (jobmngfil.getPubflag().booleanValue()) {
			deleteJobobjphaByJobmngfilPk(jobmngfil.getPrimaryKey());
		}
	}

	@SuppressWarnings("unchecked")
	private void checkSealflag(JobmngfilVO oldVO, JobmngfilVO newVO)
			throws BusinessException {
		// ���ԭ��δ���,������ʱ���˷��,���������¼�������
		if (newVO != null && newVO.getSealflag() != null
				&& newVO.getSealflag().booleanValue()) {
			if (oldVO != null
					&& (oldVO.getSealflag() == null || !oldVO.getSealflag()
							.booleanValue())) {
				// ��ѯ�¼�������
				JobmngfilVO[] subdocs = querySubJobmngfilVOs(newVO);
				if (subdocs == null || subdocs.length == 0)
					return;
				ArrayList subdocPkList = (ArrayList) VOUtil.extractFieldValues(
						subdocs, "pk_jobmngfil", null);

				final BaseDAO baseDAO = new BaseDAO();
				// �����¼�����������־
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
		// ���ԭ���ѷ��,���ڽ���,�����ϼ��Ƿ��ѽ��
		else if (newVO != null
				&& (newVO.getSealflag() == null || !newVO.getSealflag()
						.booleanValue())) {
			if (oldVO != null && oldVO.getSealflag() != null
					&& oldVO.getSealflag().booleanValue()) {
				// ��ѯ�ϼ����
				JobmngfilVO parent = querySuperJobmngfilVO(newVO);
				// ����ϼ��������ѱ����,���״�
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
																	"UPP10081406-000068")/* ��� */,
													NCLangResOnserver
															.getInstance()
															.getStrByID(
																	"10081406",
																	"UPP10081406-000069") /* "������" */}));
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
				throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // �����ѱ���������Ա����!
			}

			// �汾У��(У����������İ汾)
			VOPersistenceUtil.validateVersion(jobmngfil.getJobBasicInfo());

			// ����Ϸ��Լ���,�ڱ�ĵط��Ƿ�������
			if (jobmngfil.getJobBasicInfo().getPk_corp().equals("0001")) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_DATA_DELETE_FAIL()/*
																			 * @res
																			 * "����ɾ��ʧ��!"
																			 */
						+ "\n"
						+ MultiLangTrans.getTransStr("MP1", NCLangResOnserver
								.getInstance().getStrByID("common",
										"UC001-0000039") /*
															 * @res "ɾ��"
															 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000072") /*
																	 * @res "����"
																	 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"10081404", "UPT10081404-000001") /*
																			 * @res
																			 * "����"
																			 */
								+ NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000053") /*
																	 * @res "����"
																	 */, null)/*
											 * @res "����ɾ�����ŷ�������"
											 */);
			}

			// �ж���Ŀ�������Ƿ�UAPƽ̨�����������������ã�
			// �����Ӧ�Ļ�������(�ڹ��������ڵĹ�˾��)�Ƿ�������������.
			IReferenceCheck ref = (IReferenceCheck) NCLocator.getInstance()
					.lookup(IReferenceCheck.class.getName());
			if (ref.isReferenced(BD_JOBMNGFIL, jobmngfil.getPrimaryKey())
					|| ref.isBasePkReferencedInCorp(BD_JOBMNGFIL, jobmngfil
							.getJobBasicInfo().getPrimaryKey(), jobmngfil
							.getPk_corp())) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE() /* @res "�����Ѿ������ã�����ɾ��!" */);
			}

			// �ж���Ŀ�������Ƿ�����ҵ������������
			JobBillQuery billQuery = new JobBillQuery();
			if (billQuery
					.isJobmngfilReferencedByBill(jobmngfil.getPrimaryKey())) {
				throw new BDException(nc.vo.bd.BDMsg.MSG_REF_NOT_DELETE() /*
																			 * @res
																			 * "�����Ѿ������ã�����ɾ��!"
																			 */
						+ "\n"
						+ MultiLangTrans.getTransStr("MC8", new String[] {
								NCLangResOnserver.getInstance().getStrByID(
										"common", "UC001-0000053")/*
																	 * @res "����"
																	 */,
								NCLangResOnserver.getInstance().getStrByID(
										"common", "UPP10081406-000030") /*
																		 * @res
																		 * "����"
																		 */})/*
								 * @res "���ݱ�������������."
								 */);
			}


			// �ж��Ƿ�����¼�����Ϊ��������ɾ��
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
				// ˵����ǰ��Ŀ�����������¼��ڵ�
				throw new BusinessException(MultiLangTrans
						.getTransStr("MO2", new String[] { NCLangResOnserver
								.getInstance().getStrByID("10081406",
										"UC001-0000039") /* @res "ɾ��" */})); /*
																				 * @res
																				 * "�Ѵ����¼��ڵ�,����ɾ��"
																				 */
			}
			// The end for validation

			// �����¼�ǰ֪ͨ
			BDOperateServ bdOperaServ = new BDOperateServ();
			bdOperaServ
					.beforeOperate(JOB_MNG_NODE_CODE,
							IBDOperate.BDOPERATION_DEL, jobmngfil
									.getJobBasicInfo().getPrimaryKey(),
							jobmngfil.getPk_corp(), jobmngfil.getPrimaryKey());

			// ɾ���׶�
			if (jobmngfil.getJobObjectPhase() != null) {
				deleteJobobjphaByJobmngfilPk(jobmngfil.getPrimaryKey());
			}

			// ɾ��������
			BDDelLog delLog = new BDDelLog();
			delLog.delPKs(BD_JOBMNGFIL, new String[] { jobmngfil
					.getPrimaryKey() });

			baseDAO.deleteVO(jobmngfil);
			CacheProxy.fireDataDeleted(BD_JOBMNGFIL, jobmngfil
					.getPrimaryKey());

			// ɾ����������
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

		// ���鹫����Ŀ�ĸ���
		if (jobmngfil.getPubflag().booleanValue()) {
			if (isExistPubJobInCorp(pk_corp, jobmngfil.getJobBasicInfo()
					.getPk_jobtype(), null)) {
				throw new BDException(MultiLangTrans.getTransStr("MC6",
						new String[] {
								NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"10081404", "UPP10081404-000040")/*
																			 * @res
																			 * "������Ŀ"
																			 */, null })); // ������Ŀ�Ѿ�����
			}
		}

		// ����һ��������
		JobbasfilVO jobBDVO = jobmngfil.getJobBasicInfo();
		// ͬ���������������ķ���־���������ķ���־���ࣩ
		jobBDVO.setSealflag(jobmngfil.getSealflag());
		String pk_jobbasfil = getIJobbasedoc().insertJobbasfilVO(jobBDVO);

		// ������ǰ֪ͨ
		BDOperateServ bdOperServ = new BDOperateServ();
		bdOperServ.beforeOperate(JOB_MNG_NODE_CODE,
				IBDOperate.BDOPERATION_INSERT, null, null, jobmngfil);

		// ���������Ĺ�����
		jobmngfil.setPk_jobbasfil(pk_jobbasfil);
		jobmngfil.setPk_corp(pk_corp);
		String pk_jobmngfil = new BaseDAO().insertVO(jobmngfil);

		// �����¼���֪ͨ
		bdOperServ.afterOperate(JOB_MNG_NODE_CODE,
				IBDOperate.BDOPERATION_INSERT, null, null, jobmngfil);
		// ���ͻ���֪ͨ
		CacheProxy.fireDataInserted(BD_JOBMNGFIL, pk_jobmngfil);

		// ������Ŀ·�� (�Ƶ�insertJobmngfilVO�����д���Ϊ�˼���һ��������Ŀ·�ߵ��ٲ�ѯ)

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
		// ��ѯ������
		JobmngfilVO[] jobmngfils = queryAllJobmngfilVOsSimpleByJobtype(
				pk_jobtype, pk_corp);
		if (jobmngfils == null)
			return null;

		// ��ѯ��������
		HashMap jobbasfilPk_jobbasfil_map = queryJobbasfilVOsByJobmngfilVOs(jobmngfils);

		// ��ѯ��Ŀ·��
		ArrayList jobobjphaList = queryJobobjphaVOListByJobmngfilVOs(jobmngfils);
		Map<String, List<JobobjphaVO>> jobobjphaVOsMap = handleJobobjphaVOList2Map(jobobjphaList);

		// Ϊ���������û�����������Ŀ·��
		for (int i = 0; i < jobmngfils.length; i++) {
			JobmngfilVO jobmngfilVO = jobmngfils[i];
			// ���û�������
			jobmngfilVO.setJobBasicInfo((JobbasfilVO) jobbasfilPk_jobbasfil_map
					.get(jobmngfilVO.getPk_jobbasfil()));
			// ȡ��Ŀ·��
			if (jobobjphaVOsMap != null
					&& jobobjphaVOsMap.get(jobmngfilVO.getPrimaryKey()) != null) {
				jobmngfilVO.setJobObjectPhase(jobobjphaVOsMap.get(
						jobmngfilVO.getPrimaryKey())
						.toArray(new JobobjphaVO[0]));
			}
		}

		// ��������������Ϊ����������
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
	 * ������Ŀ������������ѯ��������
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
		// ��ѯ������
		jobmngfils = queryJobmngfilVOsSimpleByCondition(pk_jobtype, pk_corp,
				condition);
		if (jobmngfils == null)
			return null;

		// ��ѯ��������
		HashMap jobbasfilPk_jobbasfil_map = queryJobbasfilVOsByJobmngfilVOs(jobmngfils);

		// ��ѯ��Ŀ·��(�����������ƺ͸���������)
		ArrayList jobobjphaList = queryJobobjphaVOListByJobmngfilVOs(jobmngfils);
		Map<String, List<JobobjphaVO>> jobobjphaVOsMap = handleJobobjphaVOList2Map(jobobjphaList);

		// Ϊ���������û�����������Ŀ·��
		for (int i = 0; i < jobmngfils.length; i++) {
			JobmngfilVO jobmngfilVO = jobmngfils[i];
			// ���û�������
			jobmngfilVO.setJobBasicInfo((JobbasfilVO) jobbasfilPk_jobbasfil_map
					.get(jobmngfilVO.getPk_jobbasfil()));
			// ȡ��Ŀ·��
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
		// ��ȷ��дbd_jobmngfil.dr����ֹ�������飨������dr��ʱ�����ֲ���ȷ�����
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
	 * ������Ŀ������������ѯ��Ŀ·��(�����������ƺ͸���������)
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
		// // // ��ѯ��������
		// // HashMap deptPk_deptname_map =
		// queryDeptnameForJobobjphaVOs(jobobjphas);
		// // // ��ѯ����������
		// // HashMap psnPk_psnname_map =
		// queryPsnnameForJobobjphaVOs(jobobjphas);
		// // ���ݹ�������ѯ��Ŀ·�߶�Ӧ����Ŀ�׶�����
		// // HashMap phasePk_phasename_map =
		// queryPhasenameForJobobjphaVOs(jobobjphas);
		// // Ϊ��Ŀ·�����ò������ƺ͸���������
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
	 * ������Ŀ������������ѯ��Ŀ·��(�������������ƺ͸���������)
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
			// �����ǰ�������ǹ�����Ŀ,�򷵻�null
			if (jobmngfil.getPubflag().booleanValue())
				return null;
			// �����ǰ������������Ŀ���Ͳ��ֽ׶�,�򷵻�null
			if (!isJobtypePhasedByJobmngfilPk(pk_jobmngfil))
				return null;
			// ���ҹ�������Ӧ����Ŀ·��
			ArrayList jobobjphaList = queryJobobjphaVOListSimpleByJobmngfilVOs(new JobmngfilVO[] { jobmngfil });
			// ���ҹ�����Ŀ·��
			if (jobobjphaList != null && jobobjphaList.size() > 0) {
				for (Iterator iter = jobobjphaList.iterator(); iter.hasNext();) {
					JobobjphaVO jobobjpha = (JobobjphaVO) iter.next();
					if (jobobjpha.getPubflag().booleanValue())
						return jobobjpha.getPrimaryKey();
				}
			}
		}

		// �����Ŀ������Ϊ��,��ָ������Ŀ�ֽ׶�,���Ҳ���������Ŀ·��,���׳�����
		throw new BDException(NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"10081404", "UPP10081404-000042")/* @res "�Ƿ�����" */);
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
					throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // �����ѱ���������Ա����!
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
			// ���δ��ɾ���Ľ׶�VO����
			checkJobobjphaVOs(notDeletedVOs);

			// ����׶��Ƿ�ʹ��
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

			// ��ѯ�µ���Ŀ·��VO����
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
				throw new BDException(nc.vo.bd.BDMsg.MSG_LOCKED()); // �����ѱ���������Ա����!
			}

			// �汾У�飨У���������VO�İ汾��
			VOPersistenceUtil.validateVersion(jobmngfil.getJobBasicInfo());

			// ��鹫����Ŀ
			checkPubflagBeforeUpdate(jobmngfil);

			BaseDAO baseDAO = new BaseDAO();

			// ���ʱ,Ҫͬʱ����¼�������
			JobmngfilVO oldVO = (JobmngfilVO) baseDAO.retrieveByPK(
					JobmngfilVO.class, jobmngfil.getPrimaryKey());
			checkSealflag(oldVO, jobmngfil);

			// ������������
			JobbasfilVO jobBDVO = jobmngfil.getJobBasicInfo();
			if (!jobmngfil.getJobBasicInfo().getPk_corp().equals("0001")) {
				// ��ԷǷ���������ֱ�Ӹ���

			} else {
				// ��Է�������������ͬ���������������ķ���־�����������ķ���־����, ������ʵ�����壬��Ϊ����ǰ���ݣ�
				jobBDVO.setSealflag(jobmngfil.getSealflag());
			}
			getIJobbasedoc().updateJobbasfilVO(jobBDVO);

			// ���¹�����
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
		// ����
		new PfBusinessLock().lock(new JobMngLockData(
				new JobmngfilVO[] { jobmngfilVO }), null);

		String pk_basic_mng = insertJobmngfilVOWithBasedoc(jobmngfilVO,
				jobmngfilVO.getPk_corp());

		/*
		 * pk_basic_mng ������Pk����ϣ� "pk_jobbasfil, pk_jobmngfil"
		 */
		int commaLocation = pk_basic_mng.indexOf(",");
		String pk_jobbasfil = pk_basic_mng.substring(0, commaLocation);
		String pk_jobmngfil = pk_basic_mng.substring(commaLocation + 1);

		// ������Ŀ·��
		JobobjphaVO[] phaVOs = jobmngfilVO.getJobObjectPhase();
		if (phaVOs != null && phaVOs.length > 0) {
			for (JobobjphaVO vo : phaVOs) {
				vo.setPk_jobmngfil(pk_jobmngfil);
			}
		}
		phaVOs = getJobPhaseService().saveJobobjphaVOs(phaVOs);

		// ���¼������������Ŀ������VO
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
		// �жϵ�ǰ��Ŀ����������ǰ���깤��,����桱�ֶ��Ƿ��б仯
		String querySQL = "select nvl(finishedflag,'N'), sealflag from bd_jobbasfil where pk_jobbasfil = (select pk_jobbasfil from bd_jobmngfil where pk_jobmngfil = '"
				+ jobmngfilVO.getPrimaryKey() + "')"; // by tcl
		Object[] flags = (Object[]) baseDAO.executeQuery(querySQL,
				new ArrayProcessor());
		UFBoolean oldFinishedFlag = new UFBoolean(flags[0].toString());
		UFBoolean newFinishedFlag = jobmngfilVO.getJobBasicInfo()
				.getFinishedflag();
		UFBoolean oldSealFlag = new UFBoolean(flags[1].toString());
		UFBoolean newSealFlag = jobmngfilVO.getSealflag();

		// ���µ�ǰ��Ŀ�������Լ���Ӧ����Ŀ������������Ŀ·��
		// LiFIXME ��������Ӧ���ǣ�������Ŀ��������ͬʱ���»�����������Ŀ·��(�����ǵ���)
		updateJobmngfilVO(jobmngfilVO);
		JobobjphaVO[] phaseVOs = getJobPhaseService().saveJobobjphaVOs(
				jobmngfilVO.getJobObjectPhase());

		// ��ѯ�����صı����µ���Ŀ������
		if ((!oldFinishedFlag.booleanValue() && newFinishedFlag.booleanValue())
				|| (!oldSealFlag.booleanValue() && newSealFlag.booleanValue())) {
			// �����ǰ������ǰû���깤�������ڱ���Ϊ�깤��
			// ���ߣ������ǰ������ǰû����棬�����ڱ�����ˣ�
			// ����������ĿҲ��Ҫ����Ϊ�깤���棬������Ҫ��������������м���

			JobmngfilVO[] jobMngVOs = null;
			String newJobCode = jobmngfilVO.getJobCode();
			String condition = "bd_jobbasfil.jobcode like '" + newJobCode
					+ "%'";
			jobMngVOs = queryJobmngfilVOsByCondition(jobmngfilVO
					.getJobBasicInfo().getPk_jobtype(), jobmngfilVO
					.getPk_corp(), condition);
			return jobMngVOs;
		}

		// ֻ���ص�ǰ�����µ���Ŀ������
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
