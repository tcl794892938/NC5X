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
 * <b> ǰ̨У���� </b><br>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * 
 * @author author
 * @version tempProject 1.0
 */

public class ClientUICheckRule extends BeforeActionCHK implements ICheckRules,
		IUniqueRules, ICheckRules2 {

	/**
	 * ���ر�ͷΨһ���򣬽����ں�̨��顣
	 */
	public IUniqueRule[] getHeadUniqueRules() {

		IUniqueRule[] unique = new IUniqueRule[] { new UniqueRule(
				"���º�ͬ����ƻ��Ѿ�����,�����ظ�", new String[] { "vyearmonth",
						"vsourcebillid" }) };
		return unique;
	}

	/**
	 * ���ر���Ψһ���򣬽�����ǰ̨��顣
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		IUniqueRule[] unique = null;

		return unique;
	}

	/**
	 * �����������ࡣ ���VOChecker��������Ҫ�󣬿����ô�������顣
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}

	/**
	 * �Ƿ��������Ϊ��
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
//				throw new BusinessException("��ͬ�ƻ����������ͬ��");

		}

	}

}
