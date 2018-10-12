package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.SuperDMO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.dahuan.fkjh.MyBillVO;
import nc.vo.dahuan.fksq.DhFksqbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ��ע���ӹ����������� ���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 * 
 * �������ڣ�(2010-11-2)
 * 
 * @author ƽ̨�ű�����
 */
public class N_FKJH_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_JGCS_UNAPPROVE ������ע�⡣
	 */
	public N_FKJH_UNAPPROVE() {
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
			
			//���󵥾ݣ��ж��Ƿ�
//			//���ε��ݴ��ڲ�������ʵ�ʸ���Ͳ�����ɾ��
//			nc.vo.dahuan.fkjh.MyBillVO aggvo =  (MyBillVO) getVo();
//			String condition = " vsourcebillid = '"+aggvo.getParentVO().getPrimaryKey()+"'";
//			DhFksqbillVO[] queryVOs =  (DhFksqbillVO[]) new SuperDMO().queryByWhereClause(DhFksqbillVO.class, condition);
//			UFDouble ljsfkje = queryVOs[0].getLjsfkje()==null?new UFDouble(0.00):queryVOs[0].getLjsfkje();
//			if(ljsfkje.doubleValue()>0){
//				throw new BusinessException ("�������뵥�Ѿ�ʵ�ʸ������������");
//			}
//			
			Object retObj =runClass( "nc.bs.trade.comstatus.BillUnApprove", "unApproveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
//			 new SuperDMO().deleteByWhereClause(DhFksqbillVO.class, condition);
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
