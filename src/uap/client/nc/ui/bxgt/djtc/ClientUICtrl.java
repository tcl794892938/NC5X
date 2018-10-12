package nc.ui.bxgt.djtc;

import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class ClientUICtrl implements ICardController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] { IBillButton.Query, IBxgtButton.SYNCHRONOUS,
				IBxgtButton.BATCH_EDIT, IBxgtButton.LOCK_GROUP,
				IBxgtButton.MARK_GROUP, IBxgtButton.ORDERGROUP,
				IBxgtButton.DELBILL, IBxgtButton.CUST_MNY,
				IBxgtButton.TAX_RATE, IBxgtButton.PRE_PAYMENT };
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "12H201";
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
