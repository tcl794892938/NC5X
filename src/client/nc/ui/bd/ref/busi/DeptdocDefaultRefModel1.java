package nc.ui.bd.ref.busi;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 部门档案,部门档案HR
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
public class DeptdocDefaultRefModel1 extends AbstractRefTreeModel {

	public DeptdocDefaultRefModel1(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO 自动生成构造函数存根
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *根据需求设置相应参数
		
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		setFieldCode(new String[] { "deptcode", "deptname", "remcode" });
		setFieldName(new String[] {
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0004073")/* @res "部门编码" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0004069")/* @res "部门名称" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
						"UC000-0000703") /* @res "助记码" */});
		setHiddenFieldCode(new String[] { "bd_deptdoc.remcode", "pk_deptdoc",
				"pk_fathedept", "showorder" });
		setPkFieldCode("pk_deptdoc");
		setRefCodeField("deptcode");
		setRefNameField("deptname");
		setRefQueryDlgClaseName("nc.ui.bd.deptdoc.DeptdocQryUI");
		setTableName("bd_deptdoc");
		setFatherField("pk_fathedept");
		setChildField("pk_deptdoc");
//		setSealedWherePart("canceled <>'Y' and hrcanceled<>'Y' ");
		setWherePart("( pk_corp='" + getPk_corp() + "') and exists(select 'X' from v_deptperonal where v_deptperonal.pk_user = '"+userid+"') ");
		setOrderPart(" showorder,deptcode ");
		setMnecode(new String[] { "bd_deptdoc.remcode", "deptname" });
		setRefTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000005")/* @res "部门档案" */);
		
		resetFieldName();
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
					vecData.remove(0);
					vecData.add(record);
				} else {
					break;
				}
			}
		}
		return vecData;
	}
	
	
	
	
	
	
}
