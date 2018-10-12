package nc.ui.dahuan.fkjhcwcs;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{
	  
	  public String condition = "";

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		//财务预审在部门审核之后，则财务审核标志cwflag=0；并且部门审核标志shrflag1为1
		condition = strWhere.toString() 
							+ "  and nvl(dh_fkjhbill.cwflag,0) = 0 and nvl(dh_fkjhbill.shrflag1,0) = 1 ";
		setListValue();
		
	}
	
	public void setListValue() throws Exception{
		
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);
    
		updateBuffer();
	}
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	
	
	// 分单
	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhFkjhbillVO fkvo = (DhFkjhbillVO)aggvo.getParentVO();
			DhFkjhbillVO nfkvo = (DhFkjhbillVO)HYPubBO_Client.queryByPrimaryKey(DhFkjhbillVO.class, fkvo.getPk_fkjhbill());
			new FKSingleDialog(this.getBillUI()).showFKSingleDialog(nfkvo);
			this.onBoRefresh();
		}
	}

	@Override
	//财务预审
	public void onBoAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// 当前操作人
		String user = this._getOperator();
		// 当前时间
		UFDate date = this.getBillUI()._getServerTime().getDate();
		
		// 判断卡片画面还是列表画面
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			
			String slgmeg = "";
			
			for (int row = 0; row < num; row++) {
				// 单据是否勾选
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// 判断单据是否已提交且未审核
					//部门已审核，财务没审核
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
					
					if("0".equals(shflag)||"".equals(shflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据没有被部门审核");
						return;
					}
					if("1".equals(cwflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被财务审核");
						return;
					}
					
					if(0==newfkvo.getIs_single()){
						slgmeg += "["+row+"]";
					}
					
					newfkvo.setCwflag("1");
					newfkvo.setCwdate(date);
					newfkvo.setCwid(user);
					fkList.add(newfkvo);
				}
			}
			
			if(!"".equals(slgmeg)){
				int slgret = MessageDialog.showOkCancelDlg(getBillManageUI(), "选择", "第"+slgmeg+"行付款单未做过分单操作，是否确定复核？");
				if(!(MessageDialog.ID_OK==slgret)){
					return;
				}
			}

		} else {
			
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			//判断单据是否已提交且未审核
			//部门已审核，财务没审核
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			
			if("0".equals(shflag)||"".equals(shflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据没有被部门审核");
				return;
			}
			if("1".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被财务审核");
				return;
			}
			
			if(0==newfkvo.getIs_single()){
				int slgret = MessageDialog.showOkCancelDlg(getBillManageUI(), "选择", "当前付款单未做过分单操作，是否确定复核？");
				if(!(MessageDialog.ID_OK==slgret)){
					return;
				}
			}
			
			newfkvo.setCwflag("1");
			newfkvo.setCwdate(date);
			newfkvo.setCwid(user);
			fkList.add(newfkvo);

		}
		if(null != fkList && fkList.size()>0){
			NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
			MessageDialog.showHintDlg(getBillManageUI(), "提示", "财务审核完成");
			setListValue();
		}else{
			MessageDialog.showHintDlg(getBillManageUI(), "提示", "请勾选单据");
		}
	}

	
	//拒绝
	@Override
	protected void onBoCancelAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		// 判断卡片画面还是列表画面
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			int[] rowsels = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRows();
			if(0==rowsels.length){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog();
			String meg = cadg.value;
			if(!flag){
				return;
			}
			
			for (int row = 0; row < num; row++) {
				// 单据是否勾选
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
					
					if("0".equals(shflag)||"".equals(shflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据没有被部门审核");
						return;
					}
					if("1".equals(cwflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被财务审核");
						return;
					}
					
					newfkvo.setShrflag1("0");
					newfkvo.setCwflag("0");
					newfkvo.setVoperatorflag("0");
					newfkvo.setShrdate1(null);
					newfkvo.setCwdate(null);
					newfkvo.setShrid1(null);
					newfkvo.setCwid(null);
					newfkvo.setRet_date(this._getDate());
					newfkvo.setRet_user(this._getOperator());
					newfkvo.setQishenyuanyin(meg);
					
					RetRecordVO rtvo = new RetRecordVO();
					rtvo.setRet_user(this._getOperator());
					rtvo.setRet_date(this._getDate());
					rtvo.setRet_vemo(meg);
					rtvo.setRet_type(1);
					rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
					rtvo.setRet_address("财务付款复核");			
					HYPubBO_Client.insert(rtvo);
					
					fkList.add(newfkvo);
				}
			}

		} else {
			
			// 判断部门人员有无维护
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			
			if("0".equals(shflag)||"".equals(shflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据没有被部门审核");
				return;
			}
			if("1".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被财务审核");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog();
			String meg = cadg.value;
			if(!flag){
				return;
			}
			
			newfkvo.setShrflag1("0");
			newfkvo.setCwflag("0");
			newfkvo.setVoperatorflag("0");
			newfkvo.setShrdate1(null);
			newfkvo.setCwdate(null);
			newfkvo.setShrid1(null);
			newfkvo.setCwid(null);
			newfkvo.setRet_date(this._getDate());
			newfkvo.setRet_user(this._getOperator());
			newfkvo.setQishenyuanyin(meg);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(meg);
			rtvo.setRet_type(1);
			rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
			rtvo.setRet_address("财务付款复核");			
			HYPubBO_Client.insert(rtvo);
			
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "财务驳回完成");
		setListValue();
	}
}