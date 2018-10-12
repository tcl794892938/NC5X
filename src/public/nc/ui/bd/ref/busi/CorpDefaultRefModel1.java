package nc.ui.bd.ref.busi;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.bd.ref.RefIconConfigVO;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 公司目录,权限公司目录
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class CorpDefaultRefModel1 extends AbstractRefTreeModel {

	public CorpDefaultRefModel1(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO 自动生成构造函数存根
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *根据需求设置相应参数

		setFieldCode(new String[] { "bd_corp.unitcode", "bd_corp.unitname" });
		setHiddenFieldCode(new String[] { "bd_corp.pk_corp",
				"bd_corp.fathercorp", "showorder" });

		setFatherField("bd_corp.fathercorp");
		setChildField("bd_corp.pk_corp");
		setPkFieldCode("bd_corp.pk_corp");

		setStrPatch("distinct");

		setRefTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000004")/*
								 * @res "公司目录"
								 */);
		setStrPatch("distinct");

		//
		if (refNodeName.indexOf("权限") < 0) {
			setTableName("bd_corp");
			setWherePart(" ishasaccount='Y' and bd_corp.unitcode like '1%' and sealeddate is null ");

			// 权限公司目录
		} else {
			setTableName("bd_corp INNER JOIN sm_user_role ON bd_corp.pk_corp = sm_user_role.pk_corp ");
			setWherePart("bd_corp.ishasaccount='Y'and sm_user_role.cuserid ='"
					+ getPk_user() + "' and bd_corp.unitcode like '1%' ");

		}
		RefIconConfigVO iconcfgVO = new RefIconConfigVO();
		iconcfgVO.setIconKey("树.公司目录");
		setIconCfgVO(iconcfgVO);
		resetFieldName();
		setOrderPart("showorder, bd_corp.unitcode");
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		// 登陆界面多语言的问题。
		setFieldName(null);
		resetFieldName();
		// 每次翻译
		setRefTitle(null);
		return super.getRefTitle();

	}

	protected Vector getConvertedData(boolean isDataFromCache, Vector v,
			boolean isDefConverted) {
		Vector vecData = super.getConvertedData(isDataFromCache, v,
				isDefConverted);
		// 调整记录的顺序，按showorder排序，下面的算法把showorder为null的记录调整到后面
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
					// showorder == null 都在前面，其后的都已排好序
					break;
				}
			}
		}
		return vecData;
	}

}
