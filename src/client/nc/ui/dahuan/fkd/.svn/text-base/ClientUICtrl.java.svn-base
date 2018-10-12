package nc.ui.dahuan.fkd;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.fkd.DhFkbillBVO;
import nc.vo.dahuan.fkd.DhFkbillVO;
import nc.vo.dahuan.fkd.MyBillVO;


public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] {

		IBillButton.Busitype, IBillButton.Query, IBillButton.Refbill,
				IBillButton.Edit, IBillButton.Line, IBillButton.Brow,
				IBillButton.ApproveInfo, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Del, IBillButton.Delete, IBillButton.Return,
				IBillButton.Card, IBillButton.Refresh };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Busitype, IBillButton.Query,
				IBillButton.Refbill,IBillButton.Edit,
				IBillButton.Line, IBillButton.Brow, IBillButton.ApproveInfo,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Del,
				IBillButton.Delete, IBillButton.Return, IBillButton.Card,
				IBillButton.Refresh

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "fkdj";
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				DhFkbillVO.class.getName(), DhFkbillBVO.class.getName() };
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
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
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}

}
