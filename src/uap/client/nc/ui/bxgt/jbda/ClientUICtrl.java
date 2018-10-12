package nc.ui.bxgt.jbda;

import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class ClientUICtrl implements ICardController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] { IBxgtButton.BASE_SYN, IBxgtButton.CUST_DOWN,
				IBxgtButton.CUST_SYN,IBxgtButton.AUTO_SYN};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "12H203";
	}

	public String[] getBillVoName() {
		return new String[] { HYBillVO.class.getName(),
				CircularlyAccessibleValueObject.class.getName(),
				CircularlyAccessibleValueObject.class.getName() };
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

}
