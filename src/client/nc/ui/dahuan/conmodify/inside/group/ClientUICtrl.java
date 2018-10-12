package nc.ui.dahuan.conmodify.inside.group;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.contractmodify.ConModfiyVO;
import nc.vo.dahuan.contractmodify.ConModifyBillVO;
import nc.vo.dahuan.contractmodify.ConModifyDVO;

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
				IdhButton.FILEUPLOAD
			
		};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "conmodfiy";
	}

	public String[] getBillVoName() {
		return new String[]{
				ConModifyBillVO.class.getName(),
				ConModfiyVO.class.getName(),
				ConModifyDVO.class.getName()
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
		return false;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Card,
				IBillButton.Query,
				IBillButton.CancelAudit,
				IBillButton.Audit,
				IBillButton.Refresh,
				IdhButton.FILEUPLOAD
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
