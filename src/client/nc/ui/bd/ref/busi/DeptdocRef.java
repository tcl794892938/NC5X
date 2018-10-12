package nc.ui.bd.ref.busi;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class DeptdocRef extends AbstractRefModel {
	
	String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	/**
	 * 默认显示字段中的显示字段数----表示显示前几个字段
	 */
	public int getDefaultFieldCount() {
		return 2;
	}

	/**
	 * 显示字段列表
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return new String[] { "deptcode","deptname" };
	}

	// 隐藏字段
	public String[] getHiddenFieldCode() {
		return new String[] { "pk_deptdoc" };
	}

	/**
	 * 显示字段中文名
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { "部门编号", "部门名称" };
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_deptdoc";
	}

	/**
	 * 参照标题
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return "部门档案（权限）";
	}

	/**
	 * 参照数据库表或者视图名
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return "bd_deptdoc";
	}

	/**
	 * 查询条件
	 * 
	 * @return java.lang.String
	 */
	public String getWherePart() {
		return "  pk_corp ='" + getPk_corp() + "'  and pk_deptdoc in  (select pk_deptdoc from v_deptperonal where v_deptperonal.pk_user = '"+userid+"' ) ";
	}

}
