package nc.ui.dahuan.fkjh;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkjh.MyBillVO;


public class ClientUICtrl extends AbstractManageController implements
		ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Return,		
				IBillButton.Refbill,
				IBillButton.Save,
				IBillButton.Cancel,
				
				IBillButton.Edit,
				IdhButton.FKZD,
				IBillButton.Delete,
				IBillButton.Audit,
				IBillButton.CancelAudit,
						
			    IBillButton.Refresh,
			    IdhButton.FILEUPLOAD,
			    IdhButton.FILEDOWNLOAD
			    
			    
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Refbill,
				IBillButton.Edit,
				IdhButton.FKZD,
				IBillButton.Delete, 
				IBillButton.Audit,
				IBillButton.CancelAudit,
			    IBillButton.Refresh, 
				IBillButton.SelAll,
				IBillButton.SelNone,
				IBillButton.ExportBill,
				IdhButton.FILEUPLOAD,
				IdhButton.FILEDOWNLOAD
		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "FKJH";
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				DhFkjhbillVO.class.getName(), DhFkjhbillVO.class.getName() };
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
		return "pk_fkjhbill";
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

	/**
	 * 是否单表
	 * 
	 * @return boolean true:单表体，false:单表头
	 */
	public boolean isSingleDetail() {
		return false; // 单表头
	}
}
