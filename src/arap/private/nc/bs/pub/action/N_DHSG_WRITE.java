package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���������󵥵ı���
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2009-4-4)
 * @author ƽ̨�ű�����
 */
public class N_DHSG_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_X001_WRITE ������ע�⡣
 */
public N_DHSG_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
	Object retObj  =null;
	//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	//����˵��:�������淽��
	// ���ɵ��ݺ� tenancyno
	nc.bs.pub.billcodemanage.BillcodeGenerater gene  = new nc.bs.pub.billcodemanage.BillcodeGenerater ();
	if ( nc.vo.jcom.lang.StringUtil.isEmpty((String)vo.m_preValueVo.getParentVO().getAttributeValue ("vbillno"))) {
		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);
		vo.m_preValueVo.getParentVO().setAttributeValue("vbillno",billno);
	}
	retObj  =runClass( "nc.bs.trade.comsave.BillSave", "saveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
	//#################################################
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
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj  =null;\n	//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	//����˵��:�������淽��\n	// ���ɵ��ݺ�\n	nc.bs.pub.billcodemanage.BillcodeGenerater gene  =\n	new nc.bs.pub.billcodemanage.BillcodeGenerater ();\n	if ( nc.vo.jcom.lang.StringUtil.isEmpty ( ( (String)vo.m_preValueVo.getParentVO ().getAttributeValue ( \"vbillno\")))) {\n		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);\n		vo.m_preValueVo.getParentVO ().setAttributeValue ( \"vbillno\",billno);\n	}\n	retObj  =runClassCom@ \"nc.bs.trade.comsave.BillSave\", \"saveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//#################################################\n	return retObj;\n";}
/*
* ��ע�����ýű�������HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
