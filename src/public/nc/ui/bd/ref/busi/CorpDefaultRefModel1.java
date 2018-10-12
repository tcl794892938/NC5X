package nc.ui.bd.ref.busi;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.bd.ref.RefIconConfigVO;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��˾Ŀ¼,Ȩ�޹�˾Ŀ¼
 * <p>
 * <strong>�ṩ�ߣ�</strong>UAP
 * <p>
 * <strong>ʹ���ߣ�</strong>
 * 
 * <p>
 * <strong>���״̬��</strong>��ϸ���
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class CorpDefaultRefModel1 extends AbstractRefTreeModel {

	public CorpDefaultRefModel1(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO �Զ����ɹ��캯�����
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *��������������Ӧ����

		setFieldCode(new String[] { "bd_corp.unitcode", "bd_corp.unitname" });
		setHiddenFieldCode(new String[] { "bd_corp.pk_corp",
				"bd_corp.fathercorp", "showorder" });

		setFatherField("bd_corp.fathercorp");
		setChildField("bd_corp.pk_corp");
		setPkFieldCode("bd_corp.pk_corp");

		setStrPatch("distinct");

		setRefTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000004")/*
								 * @res "��˾Ŀ¼"
								 */);
		setStrPatch("distinct");

		//
		if (refNodeName.indexOf("Ȩ��") < 0) {
			setTableName("bd_corp");
			setWherePart(" ishasaccount='Y' and bd_corp.unitcode like '1%' and sealeddate is null ");

			// Ȩ�޹�˾Ŀ¼
		} else {
			setTableName("bd_corp INNER JOIN sm_user_role ON bd_corp.pk_corp = sm_user_role.pk_corp ");
			setWherePart("bd_corp.ishasaccount='Y'and sm_user_role.cuserid ='"
					+ getPk_user() + "' and bd_corp.unitcode like '1%' ");

		}
		RefIconConfigVO iconcfgVO = new RefIconConfigVO();
		iconcfgVO.setIconKey("��.��˾Ŀ¼");
		setIconCfgVO(iconcfgVO);
		resetFieldName();
		setOrderPart("showorder, bd_corp.unitcode");
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		// ��½��������Ե����⡣
		setFieldName(null);
		resetFieldName();
		// ÿ�η���
		setRefTitle(null);
		return super.getRefTitle();

	}

	protected Vector getConvertedData(boolean isDataFromCache, Vector v,
			boolean isDefConverted) {
		Vector vecData = super.getConvertedData(isDataFromCache, v,
				isDefConverted);
		// ������¼��˳�򣬰�showorder����������㷨��showorderΪnull�ļ�¼����������
		if (vecData != null) {
			for (int i = 0; i < vecData.size(); i++) {
				Vector record = (Vector) vecData.get(0);
				if (record.get(getFieldIndex("showorder")) == null) {
					//
					if (record.get(getFieldIndex(getRefCodeField())) == IRefConst.GROUPCORP) {
						continue;
					}
					vecData.remove(0);
					vecData.add(record);
				} else {
					// showorder == null ����ǰ�棬���Ķ����ź���
					break;
				}
			}
		}
		return vecData;
	}

}
