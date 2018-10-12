package nc.ui.bd.ref.busi;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class DeptdocRef extends AbstractRefModel {
	
	String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	/**
	 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
	 */
	public int getDefaultFieldCount() {
		return 2;
	}

	/**
	 * ��ʾ�ֶ��б�
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return new String[] { "deptcode","deptname" };
	}

	// �����ֶ�
	public String[] getHiddenFieldCode() {
		return new String[] { "pk_deptdoc" };
	}

	/**
	 * ��ʾ�ֶ�������
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { "���ű��", "��������" };
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_deptdoc";
	}

	/**
	 * ���ձ���
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return "���ŵ�����Ȩ�ޣ�";
	}

	/**
	 * �������ݿ�������ͼ��
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return "bd_deptdoc";
	}

	/**
	 * ��ѯ����
	 * 
	 * @return java.lang.String
	 */
	public String getWherePart() {
		return "  pk_corp ='" + getPk_corp() + "'  and pk_deptdoc in  (select pk_deptdoc from v_deptperonal where v_deptperonal.pk_user = '"+userid+"' ) ";
	}

}
