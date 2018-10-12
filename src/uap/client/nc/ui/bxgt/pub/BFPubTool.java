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
	 * �жϴ�����ַ����Ƿ�Ϊ��
	 * 
	 * @param String
	 *            value
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * ���ܣ�����һ�������ֵ�õ�UFBoolean��ֵ�����Ϊ�գ������û�ָ����FALSE��TRUE
	 * 
	 * @param��Object
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
	 * ���ܣ�����һ�������ֵ�õ�UFDate��ֵ�����Ϊ�գ����ؿ�
	 * 
	 * @param ��Object
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
	 * ���ܣ�����һ�������ֵ�õ�UFDouble��ֵ�����Ϊ�գ�������
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
	 * ���ܣ�����һ��double�õ�UFDouble��ֵ
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
	 * ���ܣ�����һ�������ֵ�õ�UFDouble��ֵ���ռ����ؿգ��㼴������
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
	 * ���ܣ�����һ��double��ֵ�õ�UFDouble��ֵ�����Ϊ�㣬���ؿ�
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
	 * ���ܣ�����һ�������ֵ�õ�UFDouble��ֵ�����Ϊ�㣬���ؿ�
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
	 * ���ܣ�����һ�������ֵ�õ�String��ֵ�����Ϊ�մ������ؿ� �÷�����Ҫ������setAttrobuteValue()
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
	 * ���ܣ�����һ�������ֵ�õ�String��ֵ�����Ϊ�մ������ؿ� �÷�����Ҫ������setAttrobuteValue()
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
	 * ����: ���ݴ�����ַ������鷵�� SQL IN �ĸ�ʽ�ַ���
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
	 * ����: ���ݴ�����ַ������鷵�� SQL IN �ĸ�ʽ�ַ���
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
	 * ����: ���ݴ�����ַ������鷵�� ���ֶ����Ƶ� SQL IN ��ʽ�ַ���,����ָ����ʼ�ͽ������� Ϊ��ֹ IN
	 * ����254����������,ÿ��ANDֻ��200��,����200ʱ������֯�������ORƴ�� ע��:
	 * �˷���ֻ������ǰ̨ƴ��SQLʱ��,������SQL���,���IN����������,�ᵼ��Ч�ʱȽϵ�, ���Դ�����������ѯ����Ǻ�̨������ʱ����
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
	 * ����: ���ݴ�����ַ������鷵�� ���ֶ����Ƶ� SQL IN ��ʽ�ַ���,����ָ����ʼ�ͽ������� Ϊ��ֹ IN
	 * ����254����������,ÿ��ANDֻ��200��,����200ʱ������֯�������ORƴ�� ע��:
	 * �˷���ֻ������ǰ̨ƴ��SQLʱ��,������SQL���,���IN����������,�ᵼ��Ч�ʱȽϵ�, ���Դ�����������ѯ����Ǻ�̨������ʱ����
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
