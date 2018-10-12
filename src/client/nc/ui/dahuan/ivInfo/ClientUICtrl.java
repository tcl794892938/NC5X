package nc.ui.dahuan.ivInfo;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.ivInfo.PdutVO;
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
 * @author authorName
 * @version tempProject version
 */

public class ClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                		IBillButton.Return,
                		IBillButton.Save,
                		IBillButton.Cancel,
                		IBillButton.Add,
                		IBillButton.Delete,
                		IBillButton.Edit,
                		IBillButton.Commit,
                		IBillButton.CancelAudit,
                		IdhButton.RET_COMMIT,
                		IBillButton.Audit,
                		IdhButton.NOAGREE,
                		IdhButton.AGREE,
                		IdhButton.CWQR,
                		IBillButton.Refresh
                		};
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
					IBillButton.Card,
            		IBillButton.Query,
            		IBillButton.Add,
            		IBillButton.Edit,
            		IBillButton.Commit,
            		IBillButton.CancelAudit,
            		IdhButton.RET_COMMIT,
            		IBillButton.Audit,
            		IdhButton.NOAGREE,
            		IdhButton.AGREE,
            		IdhButton.CWQR,
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
		return "DHPDUT";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				PdutVO.class.getName(),
				PdutVO.class.getName()
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
		return false;
	}

	public boolean isSingleDetail() {
		return false;
	}
	
}
