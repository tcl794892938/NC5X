package nc.vo.dahuan.hkjh;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class MyBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HkdhDVO[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (HkdhVO)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children){
			super.setChildrenVO(null);
		}else{
			HkdhDVO[] childvos = Arrays.asList(children).toArray(new HkdhDVO[0]);
			super.setChildrenVO(childvos);
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HkdhVO)parent);
	}

	

}
