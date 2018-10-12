package nc.ui.dahuan.htbg;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.dahuan.xmrz.MyBillVO;
import nc.vo.dahuan.xmrz.HtlogoVO;
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

public class ClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                		IBillButton.Return,   IdhButton.SJJL,                                       
                											   IdhButton.JHZT,
                                                               IdhButton.TJJL,
                                                               IdhButton.KSZT,
                                                               IdhButton.TSZT,
                                                               IdhButton.WGZT,
                                                               IdhButton.CGJL,
                                                               IdhButton.QTZT
                                                               
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,                                         
                                         IBillButton.Card,
                                         IdhButton.SJJL,
                                         IdhButton.JHZT,
                                         IdhButton.TJJL,
                                         IdhButton.KSZT,
                                         IdhButton.TSZT,
                                         IdhButton.WGZT,
                                         IdhButton.CGJL,
                                         IdhButton.QTZT
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "1235";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			HtlogoVO.class.getName(),
			HtlogoVO.class.getName()
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
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}
	
	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return false; //单表头
	}
}
