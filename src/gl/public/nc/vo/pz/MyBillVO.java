package nc.vo.pz;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class MyBillVO extends HYBillVO{
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (PZVO[]) super.getChildrenVO();
		
	}
	
	public CircularlyAccessibleValueObject getParentVO() {
		return (RQVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new PZVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((RQVO)parent);
	}

}