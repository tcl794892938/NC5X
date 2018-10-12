package nc.vo.dahuan.hkjh;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class HkcdBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HkcdDVO[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (HkcdVO)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children){
			super.setChildrenVO(null);
		}else{
			HkcdDVO[] childvos = Arrays.asList(children).toArray(new HkcdDVO[0]);
			super.setChildrenVO(childvos);
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HkcdVO)parent);
	}

	

}
