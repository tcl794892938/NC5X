package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.bs.pf.change.ConversionPolicyEnum;
import nc.bs.logging.Logger;
/**
 * ����FKJHTOFKSQ��VO�����ࡣ
 *
 * @author ����ƽ̨�Զ�������VO������ 2013-11-1
 * @since 5.5
 */
public class CHGFKJHTOFKSQ extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGFKJHTOFKSQ Ĭ�Ϲ���
 */
public CHGFKJHTOFKSQ() {
	super();
}
/**
 * ��ý��������� ȫ����
 * @return java.lang.String
 */
public String getAfterClassName() {
	return null;
}
/**
 * ��ý��������� ȫ����
 * @return java.lang.String
 */
public String getOtherClassName() {
	return null;
}
/**
 * ���ؽ�������ö��ConversionPolicyEnum��Ĭ��Ϊ������Ŀ-������Ŀ
 * @return ConversionPolicyEnum
 * @since 5.5
 */
public ConversionPolicyEnum getConversionPolicy() {
	return ConversionPolicyEnum.BILLITEM_BILLITEM;
}
/**
 * ���ӳ�����͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getField() {
	return new String[] {
		"H_ctcode->H_ctcode",
		"H_dapprovedate->H_dapprovedate",
		"H_dbilldate->H_dbilldate",
		"H_dctjetotal->H_dctjetotal",
		"H_dfkbl->H_dfkbl",
		"H_dfkje->H_dfkje",
		"H_pk_billtype->H_pk_billtype",
		"H_pk_busitype->H_pk_busitype",
		"H_pk_corp->H_pk_corp",
		"H_pk_cust2->H_pk_cust2",
		"H_vapproveid->H_vapproveid",
		"H_vapprovenote->H_vapprovenote",
		"H_vbillno->H_vbillno",
		"H_vbillstatus->H_vbillstatus",
		"H_vfkcontext->H_vfkcontext",
		"H_voperatorid->H_voperatorid",
		"H_vprojectname->H_vprojectname",
		"H_vsourcebillid->H_vsourcebillid",
		"H_vsourcebilltype->H_vsourcebilltype"
	};
}
/**
 * ��ø�ֵ���͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getAssign() {
	return null;
}
/**
 * ��ù�ʽ���͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getFormulas() {
	return null;
}
/**
 * �����û��Զ��庯��
 */
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
