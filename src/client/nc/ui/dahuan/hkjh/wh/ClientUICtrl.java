package nc.ui.dahuan.hkjh.wh;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.hkjh.HkwhBillVO;
import nc.vo.dahuan.hkjh.HkwhDVO;
import nc.vo.dahuan.hkjh.HkwhVO;


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
				IBillButton.Delete,
				IBillButton.Audit,
				IBillButton.CancelAudit,
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
				IBillButton.Audit,
				IBillButton.CancelAudit,
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
		return "HKWH";
	}

	public String[] getBillVoName() {
		return new String[] { 
				HkwhBillVO.class.getName(),
				HkwhVO.class.getName(),
				HkwhDVO.class.getName()
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
