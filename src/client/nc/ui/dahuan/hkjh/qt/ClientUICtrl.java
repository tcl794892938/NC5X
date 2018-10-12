package nc.ui.dahuan.hkjh.qt;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.hkjh.HkqtBillVO;
import nc.vo.dahuan.hkjh.HkqtDVO;
import nc.vo.dahuan.hkjh.HkqtVO;


public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Return,		
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Delete,
				IBillButton.Commit,
				IBillButton.CancelAudit,
				IBillButton.Audit,
			    IBillButton.Refresh	  
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Commit,
				IBillButton.CancelAudit,
				IBillButton.Audit,
			    IBillButton.Refresh
		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "HKQT";
	}

	public String[] getBillVoName() {
		return new String[] { 
				HkqtBillVO.class.getName(),
				HkqtVO.class.getName(),
				HkqtDVO.class.getName()
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
