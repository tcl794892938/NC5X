package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.uap.bd.job.IJobmanagedoc;
import nc.vo.bd.b38.JobbasfilVO;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.demo.contract.MultiBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 备注：加工参数的审批 单据动作执行中的动态执行类的动态执行类。
 * 
 * 创建日期：(2010-11-2)
 * 
 * @author 平台脚本生成
 */
public class N_DHHT_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_JGCS_APPROVE 构造子注解。
	 */
	public N_DHHT_APPROVE() {
		super();
	}

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####该组件为单动作审批处理开始...不能进行修改####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}
			//####该组件为单动作工作流处理结束...不能进行修改####
			Object retObj  =runClass( "nc.bs.trade.comstatus.BillApprove", "approveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas); 
			
			nc.vo.demo.contract.MultiBillVO aggvo= (MultiBillVO) retObj;
			int vbillstatus = new Integer(aggvo.getParentVO().getAttributeValue("vbillstatus").toString());
			if(vbillstatus==1){
			
			IJobmanagedoc pfserver = (IJobmanagedoc)NCLocator.getInstance().lookup(IJobmanagedoc.class.getName());
			JobmngfilVO jobmngfil = new JobmngfilVO();
			JobbasfilVO basvo = new JobbasfilVO();
			DhContractVO headvo =  (DhContractVO) vo.m_preValueVo.getParentVO();
			basvo.setJobcode(headvo.getJobcode());
			basvo.setJobname(headvo.getCtcode());
			basvo.setPk_corp(headvo.getPk_corp());
			basvo.setPk_jobtype("0001AA100000000013ZG");
			basvo.setBegindate(headvo.getDstartdate());
			basvo.setEnddate(headvo.getDjhdate());
			basvo.setFinishedflag(new UFBoolean("N"));
			basvo.setSealflag("N");
			jobmngfil.setJobBasicInfo(basvo);
			jobmngfil.setDef1(headvo.getCtname());
			jobmngfil.setDef2(headvo.getDsaletotal()==null?"":headvo.getDsaletotal().toString());
			jobmngfil.setDef3(headvo.getDcaigtotal()==null?"":headvo.getDcaigtotal().toString());
			
			jobmngfil.setPk_corp(headvo.getPk_corp());
			jobmngfil.setPk_custdoc(headvo.getPk_cust1());
			jobmngfil.setPk_deptdoc(headvo.getPk_deptdoc());
			jobmngfil.setPk_psndoc(headvo.getPk_fzr());
			jobmngfil.setPk_vendoc(headvo.getPk_cust2());
			jobmngfil.setSealflag(new UFBoolean("N"));
			
			jobmngfil.setPubflag(new UFBoolean("N"));
			jobmngfil.setMemo(headvo.getVmen());
			pfserver.insertJobmngfilVOWithBasedoc(jobmngfil, vo.m_coId);
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
