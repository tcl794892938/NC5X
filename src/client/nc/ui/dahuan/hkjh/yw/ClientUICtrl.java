package nc.ui.dahuan.hkjh.yw;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.dahuan.hkjh.MyBillVO;


public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Return,		
				IBillButton.Refbill,
				IBillButton.Audit,
				IBillButton.Delete,
			    IBillButton.Refresh,
			    IBillButton.ExportBill
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Refbill,
				IBillButton.Audit,
				IBillButton.Delete,
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
		return "HKFP";
	}

	public String[] getBillVoName() {
		return new String[] { 
				MyBillVO.class.getName(),
				HkdhVO.class.getName(), 
				HkdhDVO.class.getName() };
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