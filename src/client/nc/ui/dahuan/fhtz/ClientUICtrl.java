package nc.ui.dahuan.fhtz;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.fhtz.DhDeliveryDVO;
import nc.vo.dahuan.fhtz.DhDeliveryVO;
import nc.vo.dahuan.fhtz.MyBillVO;


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
                		IBillButton.Add,
                		IBillButton.Delete,
                		IBillButton.Save,
                		IBillButton.Cancel,
           	            IBillButton.Edit,
           	            IBillButton.Line,
           	            IBillButton.Audit,
           	            IBillButton.CancelAudit,
           	            IdhButton.FILEDOWNLOAD,
                		IBillButton.Print,
                		IBillButton.Refresh
                		};
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
	         	           	             IBillButton.Card,
	         	           	             IBillButton.Add,
	         	           	             IBillButton.Edit,
	         	           	         IBillButton.Audit,
	         	           	         IBillButton.CancelAudit,
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
		return "1234";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DhDeliveryVO.class.getName(),
			DhDeliveryDVO.class.getName()
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