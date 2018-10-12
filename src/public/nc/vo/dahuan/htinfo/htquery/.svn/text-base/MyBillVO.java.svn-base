package nc.vo.dahuan.htinfo.htquery;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class MyBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HtConLogoDtlEntity[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (HtConLogoEntity)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children || children.length == 0){
			super.setChildrenVO(null);
		}else{
			super.setChildrenVO((CircularlyAccessibleValueObject[])Arrays.asList(children).toArray(new HtConLogoDtlEntity[0]));
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HtConLogoEntity)parent);
	}



}
