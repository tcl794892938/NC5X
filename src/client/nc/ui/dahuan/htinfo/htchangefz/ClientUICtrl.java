package nc.ui.dahuan.htinfo.htchangefz;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.dahuan.htinfo.htchange.MyBillVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author authorName
 * @version tempProject version
 */

public class ClientUICtrl extends AbstractManageController {
	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {                         
		return new int[]{
				IBillButton.Return,
				IBillButton.Audit,
				IBillButton.CancelAudit,
				IBillButton.Refresh,
				IdhButton.FILEUPLOAD
		};  
	}
	
	public int[] getListButtonAry() {		
		return new int[]{
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Audit,
				IBillButton.SelAll,
				IBillButton.SelNone,
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
		return "HTCHAG";
	}

	public String[] getBillVoName() {
		return new String[]{
				MyBillVO.class.getName(),
				HtChangeEntity.class.getName(),
				HtChangeDtlEntity.class.getName()
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