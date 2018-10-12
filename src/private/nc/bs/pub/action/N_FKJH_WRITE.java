package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;

public class N_FKJH_WRITE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	public N_FKJH_WRITE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {

			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			Object retObj = null;
			// ####重要说明：生成的业务组件方法尽量不要进行修改####
			// 方法说明:公共保存方法
			// 生成单据号
			Object inCurObject = getVo();
			Object inPreObject = getUserObj();
			nc.vo.dahuan.fkjh.MyBillVO inCurVO = null;
			nc.vo.dahuan.fkjh.MyBillVO inPreVO = null;
			
			//计划付款合计-合同金额 = 小于零
			DhFkjhbillVO headvotmp =  (DhFkjhbillVO) getVo().getParentVO();
			String billpk =headvotmp.getPk_fkjhbill();
			String vsourcebillid =  getVo().getParentVO().getAttributeValue("vsourcebillid") ==null?"":getVo().getParentVO().getAttributeValue("vsourcebillid") .toString();
			String querySql = "";
			if(billpk==null){
			     querySql = " select sum(aa.dfkje) ljjhfkje from dh_fkjhbill aa where  aa.vsourcebillid = '"+vsourcebillid+"' and nvl(dr,0) = 0 ";
			}else {
				
				 querySql = " select sum(aa.dfkje) ljjhfkje from dh_fkjhbill aa where aa.pk_fkjhbill != '"+billpk+"' and aa.vsourcebillid = '"+vsourcebillid+"' and nvl(dr,0) = 0";
			}
			
			 ArrayList list =  (ArrayList) new BaseDAO().executeQuery(querySql, new ArrayListProcessor());
			if(list.size()> 0){
				Object[] obj = (Object[]) list.get(0);
				if(obj!=null){
				 UFDouble ljjhfkje = new UFDouble(obj[0]==null?"":obj[0].toString());
				 ljjhfkje = ljjhfkje.add(headvotmp.getDfkje());
				 if(headvotmp.getDctjetotal().doubleValue()<ljjhfkje.doubleValue())
					 throw new BusinessException("累计计划付款["+ljjhfkje+"]大于合同金额["+headvotmp.getDctjetotal()+"],不可以申请计划!");
				 
				}
			}
			 
			nc.bs.pub.billcodemanage.BillcodeGenerater gene = new nc.bs.pub.billcodemanage.BillcodeGenerater();
			if (nc.vo.jcom.lang.StringUtil.isEmpty(((String) vo.m_preValueVo
					.getParentVO().getAttributeValue("vbillno")))) {
				String billno = gene.getBillCode(vo.m_billType, vo.m_coId,
						null, null);
				vo.m_preValueVo.getParentVO().setAttributeValue("vbillno",
						billno);
			}

			retObj = runClass("nc.bs.trade.comsave.BillSave", "saveBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			
			if (inCurObject != null){
				inCurVO = (nc.vo.dahuan.fkjh.MyBillVO) inCurObject;
				DhFkjhbillVO headvo = (DhFkjhbillVO) inCurVO.getParentVO();
				inCurVO.setChildrenVO(new DhFkjhbillVO[]{headvo});
			}

			if (inPreObject != null
					&& inCurVO.getParentVO().getPrimaryKey() != null){
				inPreVO = (nc.vo.dahuan.fkjh.MyBillVO) inPreObject;
				DhFkjhbillVO headvo = (DhFkjhbillVO) inPreVO.getParentVO();
				inPreVO.setChildrenVO(new DhFkjhbillVO[]{headvo});
			
			}else {
				inPreVO = null;
			}
			
			
			setParameter("INCURVO", inCurVO);
			setParameter("INPREVO", inPreVO);
			runClass(
					"nc.bs.dahuan.rewrite.DhhtRewriteDMO",
					"reWriteSaleNewBatch",
					"&INCURVO:nc.vo.dahuan.fkjh.MyBillVO,&INPREVO:nc.vo.dahuan.fkjh.MyBillVO",
					vo, m_keyHas, m_methodReturnHas);
			
			
			
			
			// #################################################
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
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	Object retObj  =null;\n	//####重要说明：生成的业务组件方法尽量不要进行修改####\n	//方法说明:公共保存方法\n	// 生成单据号\n	nc.bs.pub.billcodemanage.BillcodeGenerater gene  =\n	new nc.bs.pub.billcodemanage.BillcodeGenerater ();\n	if ( nc.vo.jcom.lang.StringUtil.isEmpty ( ( (String)vo.m_preValueVo.getParentVO ().getAttributeValue ( \"vbillno\")))) {\n		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);\n		vo.m_preValueVo.getParentVO ().setAttributeValue ( \"vbillno\",billno);\n	}\n	retObj  =runClassCom@ \"nc.bs.trade.comsave.BillSave\", \"saveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//#################################################\n	return retObj;\n";
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
