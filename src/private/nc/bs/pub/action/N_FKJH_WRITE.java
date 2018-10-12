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
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {

			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			Object retObj = null;
			// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
			// ����˵��:�������淽��
			// ���ɵ��ݺ�
			Object inCurObject = getVo();
			Object inPreObject = getUserObj();
			nc.vo.dahuan.fkjh.MyBillVO inCurVO = null;
			nc.vo.dahuan.fkjh.MyBillVO inPreVO = null;
			
			//�ƻ�����ϼ�-��ͬ��� = С����
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
					 throw new BusinessException("�ۼƼƻ�����["+ljjhfkje+"]���ں�ͬ���["+headvotmp.getDctjetotal()+"],����������ƻ�!");
				 
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
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj  =null;\n	//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	//����˵��:�������淽��\n	// ���ɵ��ݺ�\n	nc.bs.pub.billcodemanage.BillcodeGenerater gene  =\n	new nc.bs.pub.billcodemanage.BillcodeGenerater ();\n	if ( nc.vo.jcom.lang.StringUtil.isEmpty ( ( (String)vo.m_preValueVo.getParentVO ().getAttributeValue ( \"vbillno\")))) {\n		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);\n		vo.m_preValueVo.getParentVO ().setAttributeValue ( \"vbillno\",billno);\n	}\n	retObj  =runClassCom@ \"nc.bs.trade.comsave.BillSave\", \"saveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//#################################################\n	return retObj;\n";
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
