package nc.ui.dahuan.htzjl;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.dahuan.htfz.MultiBillVO;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.ctbill.DhContractBVO;
import nc.vo.dahuan.ctbill.DhContractB1VO;
import nc.ui.trade.button.IBillButton;


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

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                                                               IBillButton.Return,
                                                               IBillButton.Audit,
                                                               IdhButton.RET_COMMIT,
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
	           	         	           	             IdhButton.FILEUPLOAD
                                              
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "DHHT";
	}

	public String[] getBillVoName() {
		return new String[]{
			MultiBillVO.class.getName(),
			DhContractVO.class.getName(),
			DhContractBVO.class.getName(),
			DhContractB1VO.class.getName()
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
