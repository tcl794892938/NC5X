package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���������󵥵��޸�
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2009-4-4)
 * @author ƽ̨�ű�����
 */
public class N_DHSG_EDIT extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_X001_EDIT ������ע�⡣
 */
public N_DHSG_EDIT() {
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
	Object retObj =null;
	if ( true)
	return null;
	throw new BusinessException ( "");
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
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj =null;\n	if ( true)\n	return null;\n	throw new BusinessException ( \"\");\n";}
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
