package nc.ui.demo.tree.tree03;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.dahuan.cttreebill.DhContractB1VO;
import nc.vo.dahuan.cttreebill.DhContractB2VO;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.demo.contract.MultiBillVO;

public class MultiChildTreeCardController extends AbstractTreeManageController {

	public String[] getCardBodyHideCol() {

		return null;
	}
	
	public int[] getListButtonAry() {

		return new int[] { 
				IBillButton.Busitype,
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,			
				IBillButton.Commit,
				IdhButton.RET_COMMIT,
				IdhButton.SEAL,
				IBillButton.Refresh,
				IdhButton.FILEUPLOAD,
				IBillButton.ImportBill,
				IdhButton.YCHT,
				IdhButton.FHSK
				};
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Busitype,
				IBillButton.Return,
				IBillButton.Add,
				IBillButton.Save,
				IBillButton.Cancel, 
				IBillButton.Edit,
				IBillButton.Line,				
				IBillButton.Commit,
				IdhButton.RET_COMMIT,
				IdhButton.SEAL,
				IBillButton.Refresh,
				IdhButton.FILEUPLOAD,
				IBillButton.ImportBill
				};
	}

	public boolean isShowCardRowNo() {

		return true;
	}

	public boolean isShowCardTotal() {

		return true;
	}

	public String getBillType() {

		return "DHHT";
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
	 * �������Ӧ�ó����Զ�ά�����Ľṹ�Ļ��� �������ݻ�ʹ������һ���ڵ㣬 ɾ�����ݻ�ʹ������һ���ڵ�
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