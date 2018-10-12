package nc.ui.dahuan.projclear.fzr;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.projclear.ProjClearBillVO;
import nc.vo.dahuan.projclear.ProjectClearDVO;
import nc.vo.dahuan.projclear.ProjectClearVO;

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Return,
				IBillButton.CancelAudit,
				IBillButton.Audit,
				IBillButton.Refresh,
				IBillButton.Refbill
		};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "projclear";
	}

	public String[] getBillVoName() {
		return new String[]{
				ProjClearBillVO.class.getName(),
				ProjectClearVO.class.getName(),
				ProjectClearDVO.class.getName()
		};
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
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.CancelAudit,
				IBillButton.Audit,
				IBillButton.Refresh,
				IBillButton.Refbill
		};
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
