package nc.ui.dahuan.jcInfo.custAudit;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dahuan.jcInfo.custInfo.BgCustEntity;
import nc.vo.trade.pub.HYBillVO;


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

public class ClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                		IBillButton.Return,
                		IBillButton.CancelAudit,
	         	        IBillButton.Audit,
	         	        IBillButton.Refresh
                        };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
	         	           	             IBillButton.Card,
	         	           	             IBillButton.CancelAudit,
	           	         	           	 IBillButton.Audit,
	           	         	           	 IBillButton.Refresh,
	           		         	        IBillButton.SelAll,
	           		         	        IBillButton.SelNone
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "DHCUST";
	}

	public String[] getBillVoName() {
		return new String[]{
			HYBillVO.class.getName(),
			BgCustEntity.class.getName(),
			BgCustEntity.class.getName()
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
	
	/**
	 * �Ƿ񵥱�
	 * @return boolean true:�����壬false:����ͷ
	 */ 
	public boolean isSingleDetail() {
		return false; //����ͷ
	}
}