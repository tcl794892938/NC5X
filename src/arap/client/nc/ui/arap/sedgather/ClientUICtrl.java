package nc.ui.arap.sedgather;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.arap.sedgather.AggSedGatherVO;
import nc.vo.arap.sedgather.SedGatherDVO;
import nc.vo.arap.sedgather.SedGatherHVO;

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Commit,
				IBillButton.Audit,
				IBillButton.CancelAudit,
				IBillButton.ApproveInfo,
				IBillButton.Return,
				IBillButton.Print
		};
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "DHSG";
	}

	public String[] getBillVoName() {
		return new String[]{
				AggSedGatherVO.class.getName(),
				SedGatherHVO.class.getName(),
				SedGatherDVO.class.getName()
		};
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

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Commit,
				IBillButton.Audit,
				IBillButton.CancelAudit,
				IBillButton.ApproveInfo,
				IBillButton.Card
		};
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
