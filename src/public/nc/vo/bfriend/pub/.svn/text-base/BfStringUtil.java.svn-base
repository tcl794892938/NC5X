package nc.vo.bfriend.pub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;

/**
 * 字符串公共类,包括了一些字符串处理方法、校验方法等
 * 
 * @author laihongwei
 * @copyright 北京时代益友科技有限公司
 * 
 */
public class BfStringUtil {

	/**
	 * 检验是否合法的身份证号
	 * 
	 * @param idcard
	 * @throws BusinessException
	 */
	public static void checkValidIDCard(String idcard) throws BusinessException {
		if (StringUtil.isEmpty(idcard))
			return;

		if (idcard.length() == 15 || idcard.length() == 18) {
			if (idcard
					.matches("[0-8]\\d\\d\\d\\d\\d\\d\\d[0|1][1-9][0-3]\\d\\d\\d\\d")
					|| idcard
							.matches("[0-8]\\d\\d\\d\\d\\d[1-2][0|9]\\d\\d[0|1][1-9][0-3]\\d\\d\\d\\d[0-9a-zA-Z]")) {
			} else {
				throw new BusinessException("身份证号码不正确！");
			}
		} else {
			throw new BusinessException("身份证号码应该是15或18位！");
		}
	}

	/**
	 * 检验是否合法的固定电话号码
	 * 
	 * @param ihomephone
	 * @throws BusinessException
	 */
	public static void checkValidHomePhone(String ihomephone)
			throws BusinessException {
		if (StringUtil.isEmpty(ihomephone))
			return;

		if (ihomephone.matches("\\d*\\-?\\d*\\-?\\d*")) {
		} else {
			throw new BusinessException("电话号码只能含有数字、'-'");
		}

	}

	/**
	 * 检验是否合法的手机号
	 * 
	 * @param mobile
	 * @throws BusinessException
	 */
	public static void checkValidMobile(String mobile) throws BusinessException {
		if (StringUtil.isEmpty(mobile))
			return;

		if (mobile.length() > 0 && (mobile.length() == 11)) {
			if (mobile.matches("[1][3|5]\\d{9}")) {
			} else {
				throw new BusinessException("手机号码不正确！");
			}
		} else {
			throw new BusinessException("手机号码位数错误！");
		}
	}

	/**
	 * 根据一个数组去拼成 in 的语句
	 * 
	 * @param fld
	 * @param arsKey
	 * @param bNullAll
	 * @return
	 */
	public static String getWherePartByKeys(String fld, String[] arsKey,
			boolean bNullAll) {
		final int MAX = 200;
		if (arsKey == null || arsKey.length == 0) {
			return bNullAll ? " 1 = 1 " : "1>2";
		}
		if (arsKey.length == 1)
			return fld + "='" + arsKey[0] + "'";
		if (arsKey.length <= MAX) {
			String sTmp = fld + " in (";
			for (int i = 0; i < arsKey.length; i++) {
				if (i == arsKey.length - 1) {
					sTmp += "'" + arsKey[i] + "')";
					break;
				}
				sTmp += "'" + arsKey[i] + "',";
			}
			return sTmp;
		}
		int ipos = 0;
		int itimes = arsKey.length / MAX;
		int mode = arsKey.length % MAX;
		String where = null;
		for (int i = 0; i < itimes; i++) {
			if (where == null)
				where = " ( " + fld + " in ( ";
			else
				where += " or " + fld + " in (";
			for (int j = 0; j < MAX; j++) {
				if (j == MAX - 1) {
					where += "'" + arsKey[ipos + j] + "')";
					break;
				}
				where += "'" + arsKey[ipos + j] + "',";
			}
			ipos += MAX;
		}
		if (mode == 0)
			where += " )";
		else {
			where += " or " + fld + " in (";
			for (int k = 0; k < mode; k++) {
				if (k == mode - 1) {
					where += "'" + arsKey[ipos + k] + "'))";
					break;
				}
				where += "'" + arsKey[ipos + k] + "',";
			}
		}
		return where;
	}

	/**
	 * 用正则表达式精确替代字符串 比如，对于字符串 i love this game because i am a gamer 如果要把 am 替换成
	 * was , 是不会把 game 中的 am 也误替换
	 * 
	 * @param source
	 * @param strReplace
	 * @param strReplaced
	 * @return
	 */
	public static String replaceAllString(String source, String strReplace,
			String strReplaced) {
		if (StringUtil.isEmpty(source) || StringUtil.isEmpty(strReplace)
				|| strReplaced == null)
			return source;

		String regex = "\\b" + strReplaced + "\\b";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source); // get a matcher object
		return m.replaceAll(strReplace);
	}

}
