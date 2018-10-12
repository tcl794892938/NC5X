package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.uap.bd.job.IJobmanagedoc;
import nc.itf.uap.bd.job.IJobmanagedocQuery;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.demo.contract.MultiBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ��ע���ӹ����������� ���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 * 
 * �������ڣ�(2010-11-2)
 * 
 * @author ƽ̨�ű�����
 */
public class N_DHHT_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_JGCS_UNAPPROVE ������ע�⡣
	 */
	public N_DHHT_UNAPPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####�����Ϊ������������ʼ...���ܽ����޸�####
			boolean isFinishToGoing = procUnApproveFlow(vo);
			Object retObj = runClass("nc.bs.trade.comstatus.BillUnApprove",
					"unApproveBill", "nc.vo.pub.AggregatedValueObject:01", vo,
					m_keyHas, m_methodReturnHas);

			IJobmanagedocQuery pfserver1 = (IJobmanagedocQuery) NCLocator
					.getInstance().lookup(IJobmanagedocQuery.class.getName());
			IJobmanagedoc pfserver2 = (IJobmanagedoc) NCLocator.getInstance()
					.lookup(IJobmanagedoc.class.getName());
			nc.vo.demo.contract.MultiBillVO aggvo = (MultiBillVO) retObj;
			String jobcode = (String) aggvo.getParentVO().getAttributeValue(
					"jobcode");
			String condition = " jobcode = '" + jobcode + "'";
			JobmngfilVO[] jobvos = (JobmngfilVO[]) pfserver1
					.queryJobmngfilVOsByCondition("0001AA100000000013ZG", vo.m_coId, condition);
			if (jobvos != null && jobvos.length > 0) {
				nc.vo.bd.b39.JobmngfilVO jobmngfil = jobvos[0];
				pfserver2.deleteJobmngfilVO(jobmngfil);
			}
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####�����Ϊ������������ʼ...���ܽ����޸�####\n	procUnApproveFlow@@;\n	//####�����Ϊ���������������...���ܽ����޸�####\n";
	}

	/*
	 * ��ע�����ýű�������HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
