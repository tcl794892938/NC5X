package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.dahuan.dhht.QueryAlldhhtDMO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.compiler.BatchWorkflowRet;
import nc.itf.uap.bd.job.IJobmanagedoc;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.vo.bd.b38.JobbasfilVO;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fksq.DhFksqbillVO;
import nc.vo.dahuan.fksq.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 备注：加工参数的审批 单据动作执行中的动态执行类的动态执行类。
 * 
 * 创建日期：(2010-11-2)
 * 
 * @author 平台脚本生成
 */
public class N_FKJH_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_JGCS_APPROVE 构造子注解。
	 */
	public N_FKJH_APPROVE() {
		super();
	}

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			Hashtable m_sysHasNoPassAndGonging = procFlowBacth(vo);
			BatchWorkflowRet bwr = new BatchWorkflowRet();
			bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);
			bwr.setUserObj(getVos());
			return new Object[] { bwr };
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####该组件为单动作审批处理开始...不能进行修改####\n	procActionFlow@@;\n	//####该组件为单动作审批处理结束...不能进行修改####\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
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
