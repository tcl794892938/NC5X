package nc.vo.dahuan.hkjh;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class HkqtBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HkqtDVO[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (HkqtVO)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children){
			super.setChildrenVO(null);
		}else{
			HkqtDVO[] childvos = Arrays.asList(children).toArray(new HkqtDVO[0]);
			super.setChildrenVO(childvos);
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HkqtVO)parent);
	}

	

}
