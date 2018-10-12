package nc.ui.pf.changedir;

import nc.bs.pf.change.ConversionPolicyEnum;
import nc.vo.pf.change.UserDefineFunction;
/**
 * ����FKSQTOfkdj��VO�����ࡣ
 *
 * @author ����ƽ̨�Զ�������VO������ 2013-10-13
 * @since 5.5
 */
public class CHGFKSQTOfkdj extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGFKSQTOfkdj Ĭ�Ϲ���
 */
public CHGFKSQTOfkdj() {
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


public ConversionPolicyEnum getConversionPolicy() {
	return ConversionPolicyEnum.BILLITEM_BILLITEM;
}
/**
 * ���ӳ�����͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getField() {
	return new String[] {
		"H_cmodifyid->H_cmodifyid",
		"H_dapprovedate->H_dapprovedate",
		"H_dbilldate->H_dbilldate",
		"H_pk_billtype->H_pk_billtype",
		"H_pk_busitype->H_pk_busitype",
		"H_pk_corp->H_pk_corp",
		"H_pk_cust2->H_pk_cust2",
		"H_tmodifytime->H_tmodifytime",
		"H_vapproveid->H_vapproveid",
		"H_vapprovenote->H_vapprovenote",
		"H_vbillno->H_vbillno",
		"H_vbillstatus->H_vbillstatus",
		"H_vlastbillid->H_vlastbillid",
		"H_vlastbilltype->H_vlastbilltype",
		"H_voperatorid->H_voperatorid",
		"H_vsourcebillid->H_vsourcebillid",
		"H_vsourcebilltype->H_vsourcebilltype",
		"B_vdef1->B_vdef1",
		"B_vdef10->B_vdef10",
		"B_vdef2->B_vdef2",
		"B_vdef3->B_vdef3",
		"B_vdef4->B_vdef4",
		"B_vdef5->B_vdef5",
		"B_vdef6->B_vdef6",
		"B_vdef7->B_vdef7",
		"B_vdef8->B_vdef8",
		"B_vdef9->B_vdef9",
		"B_vmen->B_vmen"
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
	return new String[] {
		      "H_dbilldate->SYSDATE",
		      "H_voperatorid->SYSOPERATOR",
		      "H_pk_billtype->\"fkdj\"",
		      "H_vbillstatus->8",
		      "H_vsourcebillid->H_pk_fksqbill",
		      "B_vsourcebillrowid->B_pk_fksqbill_b"
		  };
}
/**
 * �����û��Զ��庯��
 */
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
