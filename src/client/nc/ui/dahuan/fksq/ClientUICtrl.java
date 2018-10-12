package nc.ui.dahuan.fksq;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.fksq.DhFksqbillBVO;
import nc.vo.dahuan.fksq.DhFksqbillVO;
import nc.vo.dahuan.fksq.MyBillVO;

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Busitype, IBillButton.Query,
				 IBillButton.Refbill, IBillButton.Edit, IBillButton.Line,
				IBillButton.Brow, IBillButton.ApproveInfo, IBillButton.Save,
				IBillButton.Cancel, IBillButton.Del, IBillButton.Delete,
				IBillButton.Return, IBillButton.Card, IBillButton.Commit,
				IBillButton.Audit, IBillButton.CancelAudit , IBillButton.Print};

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Busitype, IBillButton.Query,
				IBillButton.Refbill, IBillButton.Edit, IBillButton.Line,
				IBillButton.Brow, IBillButton.ApproveInfo, IBillButton.Save,
				IBillButton.Cancel, IBillButton.Del, IBillButton.Delete,
				IBillButton.Return, IBillButton.Card,IBillButton.Print

		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "FKSQ";
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				DhFksqbillVO.class.getName(), DhFksqbillBVO.class.getName() };
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
		return false;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}

}
