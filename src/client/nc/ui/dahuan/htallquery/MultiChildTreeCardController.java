package nc.ui.dahuan.htallquery;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.dahuan.cttreebill.DhContractB1VO;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.demo.contract.MultiBillVO;

public class MultiChildTreeCardController extends AbstractTreeManageController {

	public String[] getCardBodyHideCol() {

		return null;
	}
	
	public int[] getListButtonAry() {

		return new int[] { 
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Refresh,
				IdhButton.FILEUPLOAD,
				IdhButton.YCHT,
				IdhButton.FHSK,
				IdhButton.YCHTDC,
				IdhButton.FHSKDC
				};
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Return,
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

		return "DHTC";
	}

	public String[] getBillVoName() {

		return new String[] { MultiBillVO.class.getName(),
				DhContractVO.class.getName(), DhContractBVO.class.getName(),
				DhContractB1VO.class.getName() };
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

		return "pk_contract";
	}

	public Boolean isEditInGoing() throws Exception {

		return false;
	}

	public boolean isExistBillStatus() {

		return true;
	}

	public boolean isLoadCardFormula() {

		return true;
	}

	/**
	 * 如果允许应用程序自动维护树的结构的话， 新增单据会使树增加一个节点， 删除单据会使树减少一个节点
	 */
	public boolean isAutoManageTree() {

		return true;
	}

	public boolean isChildTree() {

		return false;
	}

	public boolean isTableTree() {

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
