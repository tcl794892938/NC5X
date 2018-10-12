package nc.vo.dahuan.contractmodify;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class ConModifyBillVO extends HYBillVO {

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((ConModfiyVO)parent);
	}
	
	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (ConModfiyVO)super.getParentVO();
	}
	
	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children || children.length == 0){
			super.setChildrenVO(null);
		}else{
			super.setChildrenVO(Arrays.asList(children).toArray(new ConModifyDVO[0]));
		}
	}
	
	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (ConModifyDVO[])super.getChildrenVO();
	}

}
