package nc.vo.arap.sedgather;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class AggSedGatherVO extends HYBillVO {

	@Override
	public SedGatherDVO[] getChildrenVO() {
		return (SedGatherDVO[])super.getChildrenVO();
	}

	@Override
	public SedGatherHVO getParentVO() {
		return (SedGatherHVO)super.getParentVO();
	}

}
