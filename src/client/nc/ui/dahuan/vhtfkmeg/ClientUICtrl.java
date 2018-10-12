package nc.ui.dahuan.vhtfkmeg;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.vhtfkmeg.ViewhtfkVO;
import nc.vo.trade.pub.HYBillVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author author
 * @version tempProject version
 */

public class ClientUICtrl implements IListController,ISingleController {
	
	public int[] getListButtonAry() {		
	
        return new int[]{
			IBillButton.Refresh
        };
	
	}

	public String getBillType() {
		return "HTFK";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				ViewhtfkVO.class.getName(),
				ViewhtfkVO.class.getName()
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
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

	public boolean isSingleDetail() {
		return false;
	}
	
}
