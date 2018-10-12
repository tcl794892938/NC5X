package nc.vo.dahuan.projclear;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class ProjClearBillVO extends HYBillVO {

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (ProjectClearDVO[])super.getChildrenVO();
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return (ProjectClearVO)super.getParentVO();
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if(null == children || children.length == 0){
			super.setChildrenVO(null);
		}else{
			super.setChildrenVO(Arrays.asList(children).toArray(new ProjectClearDVO[0]));
		}
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((ProjectClearVO)parent);
	}

}
