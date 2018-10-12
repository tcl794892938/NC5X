package nc.ui.dahuan.fkjhdy;

import java.awt.Container;

import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;
import nc.vo.trade.checkrule.VOChecker;

/**
 * <b> 前台校验类 </b><br>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject 1.0
 */

public class ClientUICheckRule extends BeforeActionCHK implements ICheckRules,
		IUniqueRules, ICheckRules2 {

	/**
	 * 返回表头唯一规则，仅用于后台检查。
	 */
	public IUniqueRule[] getHeadUniqueRules() {

		IUniqueRule[] unique = new IUniqueRule[] { new UniqueRule(
				"本月合同付款计划已经存在,不能重复", new String[] { "vyearmonth",
						"vsourcebillid" }) };
		return unique;
	}

	/**
	 * 返回表体唯一规则，仅用于前台检查。
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		IUniqueRule[] unique = null;

		return unique;
	}

	/**
	 * 返回特殊检查类。 如果VOChecker不能满足要求，可以用此类来检查。
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}

	/**
	 * 是否允许表体为空
	 */
	public boolean isAllowEmptyBody(String tablecode) {
		return true;
	}

	public ICheckRule[] getHeadCheckRules() {
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		return null;
	}

	public String[] getHeadIntegerField() {
		return null;
	}

	public String[] getHeadUFDoubleField() {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return null;
	}

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		if (actionName.equals(IPFACTION.SAVE)) {
			if (!VOChecker.check(vo, this))
				throw new nc.vo.pub.BusinessException(VOChecker
						.getErrorMessage());
//
//			SuperVO headvo = (SuperVO) vo.getParentVO();
//			UFDouble dctjetotal = (UFDouble) (headvo
//					.getAttributeValue("dctjetotal") == null ? new UFDouble(
//					0.00) : headvo.getAttributeValue("dctjetotal"));
//			UFDouble ljfkje = (UFDouble) (headvo.getAttributeValue("ljfkje") == null ? new UFDouble(
//					0.00)
//					: headvo.getAttributeValue("ljfkje"));
//			UFDouble dfkje = (UFDouble) (headvo.getAttributeValue("dfkje") == null ? new UFDouble(
//					0.00)
//					: headvo.getAttributeValue("dfkje"));
//			if (dctjetotal.sub(ljfkje).doubleValue() < dfkje.doubleValue())
//				throw new BusinessException("合同计划付款金额超过合同金额！");

		}

	}

}
