package nc.vo.dahuan.hkjh;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class HkwhBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HkwhDVO[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (HkwhVO)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children){
			super.setChildrenVO(null);
		}else{
			HkwhDVO[] childvos = Arrays.asList(children).toArray(new HkwhDVO[0]);
			super.setChildrenVO(childvos);
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HkwhVO)parent);
	}

	

}
