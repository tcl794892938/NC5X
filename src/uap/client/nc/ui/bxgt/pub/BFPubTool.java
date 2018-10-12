package nc.ui.bxgt.pub;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class BFPubTool {

	public static UFDouble ZERO = new UFDouble(0.0);

	/**
	 * 判断传入的字符串是否为空
	 * 
	 * @param String
	 *            value
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * 功能：根据一个对象的值得到UFBoolean的值，如果为空，返回用户指定的FALSE或TRUE
	 * 
	 * @param：Object
	 * @return UFBoolean
	 */
	public static UFBoolean getUFBoolean_NullAs(Object value,
			UFBoolean bDefaultValue) {
		if (value == null || value.toString().trim().equals("")) {
			return bDefaultValue;
		} else if (value instanceof UFBoolean) {
			return (UFBoolean) value;
		} else {
			return new UFBoolean(value.toString().trim());
		}
	}

	/**
	 * 功能：根据一个对象的值得到UFDate的值，如果为空，返回空
	 * 
	 * @param ：Object
	 * @return UFDate
	 */
	public static UFDate getUFDate(Object value) {
		if (value == null || value.toString().trim().equals("")) {
			return null;
		} else if (value instanceof UFDate) {
			return (UFDate) value;
		} else {
			return new UFDate(value.toString().trim());
		}
	}

	/**
	 * 功能：根据一个对象的值得到UFDouble的值，如果为空，返回零
	 * 
	 * @param Object
	 * @return UFDouble
	 */
	public static UFDouble getUFDouble_NullAsZero(Object value) {
		if (value == null || value.toString().trim().equals("")) {
			return ZERO;
		} else if (value instanceof UFDouble) {
			return (UFDouble) value;
		} else if (value instanceof BigDecimal) {
			return new UFDouble((BigDecimal) value);
		} else {
			return new UFDouble(value.toString().trim());
		}
	}

	/**
	 * 功能：根据一个double得到UFDouble的值
	 * 
	 * @param double
	 * @return UFDouble
	 */
	public static UFDouble getUFDouble_ValueAsValue(double dValue) {
		if (dValue == 0) {
			return ZERO;
		} else {
			return new UFDouble(dValue);
		}
	}

	/**
	 * 功能：根据一个对象的值得到UFDouble的值，空即返回空，零即返回零
	 * 
	 * @param Object
	 * @return UFDouble
	 */
	public static UFDouble getUFDouble_ValueAsValue(Object value) {
		if (value == null || value.toString().trim().equals("")) {
			return null;
		} else if (value instanceof UFDouble) {
			return (UFDouble) value;
		} else if (value instanceof BigDecimal) {
			return new UFDouble((BigDecimal) value);
		} else {
			return new UFDouble(value.toString().trim());
		}

	}

	/**
	 * 功能：根据一个double的值得到UFDouble的值，如果为零，返回空
	 * 
	 * @param Object
	 * @return UFDouble
	 */
	public static UFDouble getUFDouble_ZeroAsNull(double dValue) {
		if (dValue == 0) {
			return null;
		} else {
			return new UFDouble(dValue);
		}
	}

	/**
	 * 功能：根据一个对象的值得到UFDouble的值，如果为零，返回空
	 * 
	 * @param Object
	 * @return UFDouble
	 */
	public static UFDouble getUFDouble_ZeroAsNull(Object value) {
		UFDouble dValue = getUFDouble_NullAsZero(value);
		if (dValue.compareTo(ZERO) == 0) {
			return null;
		}
		return dValue;
	}

	/**
	 * 功能：根据一个对象的值得到String的值，如果为空串，返回空 该方法主要可用于setAttrobuteValue()
	 * 
	 * @param Object
	 * @return String
	 */
	public static String getString_TrimZeroLenAsNull(Object value) {
		if (value == null || value.toString().trim().length() == 0) {
			return null;
		}
		return value.toString().trim();
	}

	/**
	 * 功能：根据一个对象的值得到String的值，如果为空串，返回空 该方法主要可用于setAttrobuteValue()
	 * 
	 * @param Object
	 * @return String
	 */
	public static String getString_TrimZeroLenAs(Object value, String str) {
		if (value == null || value.toString().trim().length() == 0) {
			return str;
		}
		return value.toString().trim();
	}

	/**
	 * 功能: 根据传入的字符串数组返回 SQL IN 的格式字符串
	 * 
	 * @param Collection
	 * @return String
	 * @example 'abc','def','ghi'
	 */
	public static String getInStr(Collection coll) {
		String inStr = null;
		if (coll != null && coll.size() > 0) {
			StringBuilder sb = new StringBuilder();
			String[] values = (String[]) coll.toArray(new String[0]);
			for (int i = 0; i < values.length; i++) {
				sb.append("'").append(values[i]).append("'");
				if (i < values.length - 1)
					sb.append(",");
			}
			inStr = sb.toString();
		}
		return inStr;
	}

	/**
	 * 功能: 根据传入的字符串数组返回 SQL IN 的格式字符串
	 * 
	 * @param Collection
	 * @return String
	 * @example 'abc','def','ghi'
	 */
	public static String getInStr(String[] strs) {
		String inStr = null;
		if (strs != null && strs.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < strs.length; i++) {
				sb.append("'").append(strs[i]).append("'");
				if (i < strs.length - 1)
					sb.append(",");
			}
			inStr = sb.toString();
		}
		return inStr;
	}
	
	
	public static String getRefStr(String[] strs) {
		String inStr = null;
		if (strs != null && strs.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < strs.length; i++) {
				sb.append(strs[i]);
				if (i < strs.length - 1)
					sb.append(",");
			}
			inStr = sb.toString();
		}
		return inStr;
	}

	/**
	 * 功能: 根据传入的字符串数组返回 带字段名称的 SQL IN 格式字符串,可以指定开始和结束索引 为防止 IN
	 * 超过254个条件限制,每个AND只放200个,超过200时重新组织语句块采用OR拼接 注意:
	 * 此方法只适用于前台拼接SQL时用,此类型SQL语句,如果IN的条件过多,会导致效率比较低, 所以大批量条件查询最好是后台采用临时表方案
	 * 
	 * @param sFieldName
	 * @param alValue
	 * @param start
	 * @param num
	 * @return String
	 */
	public static String getInSqlWithOutAnd(String sFieldName,
			ArrayList alValue, int start, int num) {
		if (sFieldName == null || sFieldName.trim().length() == 0
				|| alValue == null || start < 0 || num < 0
				|| alValue.size() < start + num)
			return null;
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append("  ( ").append(sFieldName).append(" IN ( ");
		int end = start + num;
		for (int i = start; i < end; i++) {
			if (alValue.get(i) != null
					&& alValue.get(i).toString().trim().length() > 0) {
				sbSQL.append("'").append(alValue.get(i)).append("'");
				if (i != alValue.size() - 1 && (i <= 0 || i % 200 != 0))
					sbSQL.append(",");
			} else {
				return null;
			}
			if (i > 0 && i % 200 == 0)
				sbSQL.append(" ) OR ").append(sFieldName).append(" IN ( ");
		}

		sbSQL.append(" ) )");
		return sbSQL.toString();
	}

	/**
	 * 功能: 根据传入的字符串数组返回 带字段名称的 SQL IN 格式字符串,可以指定开始和结束索引 为防止 IN
	 * 超过254个条件限制,每个AND只放200个,超过200时重新组织语句块采用OR拼接 注意:
	 * 此方法只适用于前台拼接SQL时用,此类型SQL语句,如果IN的条件过多,会导致效率比较低, 所以大批量条件查询最好是后台采用临时表方案
	 * 
	 * @param sFieldName
	 * @param saValue
	 * @param start
	 * @param num
	 * @return String
	 */
	public static String getInSqlWithOutAnd(String sFieldName,
			String saValue[], int start, int num) {
		if (sFieldName == null || sFieldName.trim().length() == 0
				|| saValue == null || start < 0 || num < 0
				|| saValue.length < start + num)
			return null;
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append("  ( ").append(sFieldName).append(" IN ( ");
		int end = start + num;
		for (int i = start; i < end; i++) {
			if (saValue[i] != null && saValue[i].trim().length() > 0) {
				sbSQL.append("'").append(saValue[i]).append("'");
				if (i != saValue.length - 1 && (i <= 0 || i % 200 != 0))
					sbSQL.append(",");
			} else {
				return null;
			}
			if (i > 0 && i % 200 == 0)
				sbSQL.append(" ) OR ").append(sFieldName).append(" IN ( ");
		}

		sbSQL.append(" ) )");
		return sbSQL.toString();
	}

	public static String getInSqlWithOutAnd(String sFieldName, Object oValue[],
			int start, int num) {
		if (sFieldName == null || sFieldName.trim().length() == 0
				|| oValue == null || start < 0 || num < 0
				|| oValue.length < start + num)
			return null;
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append("  ( ").append(sFieldName).append(" IN ( ");
		int end = start + num;
		for (int i = start; i < end; i++) {
			String sValue = getString_TrimZeroLenAsNull(oValue[i]);
			if (sValue != null) {
				sbSQL.append("'").append(sValue).append("'");
				if (i != oValue.length - 1 && (i <= 0 || i % 200 != 0))
					sbSQL.append(",");
			} else {
				return null;
			}
			if (i > 0 && i % 200 == 0)
				sbSQL.append(" ) OR ").append(sFieldName).append(" IN ( ");
		}

		sbSQL.append(" ) )");
		return sbSQL.toString();
	}
}
