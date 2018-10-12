package nc.ui.dahuan.fkjhzg;

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
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		// 付款计划单中
		// 部门主管添加过滤条件
		//部门在财务预审之前进行审核，则部门审核标志shrflag1为0 制单标识voperatorflag = 1
		
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		condition = strWhere.toString() 
						+ " and nvl(dh_fkjhbill.voperatorflag,0) = 1 and  nvl(dh_fkjhbill.shrflag1,0)=0 "
						+ " and dh_fkjhbill.pk_dept in (select v.pk_deptdoc from v_deptperonal v " 
						+ " where v.pk_user = '"+user+"' and v.pk_corp = '"+pkCorp+"')"; 
		
		
		setListValue();
		
	}
	
	private void setListValue() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	// 全选
	@Override
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	// 全消
	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}
	
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
	
	//审核
	@Override
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
			for (int row = 0; row < num; row++) {
				// 单据是否勾选
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
										
					//判断单据是否已提交且未审核
					// 业务员提交，部门主任未审核Shrflag1=0
					// 
					String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					if("0".equals(zdflag)||"".equals(zdflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据未提交");
						return;
					}
					if("1".equals(shflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被部门主管审核");
						return;
					}
					
					newfkvo.setShrflag1("1");
					newfkvo.setShrdate1(date);
					newfkvo.setShrid1(user);
					fkList.add(newfkvo);
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
			
			// 判断单据是否已提交且未审核
			// 业务员提交，部门主任未审核Shrflag1=0
			// 
			String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			if("0".equals(zdflag)||"".equals(zdflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据未提交");
				return;
			}
			if("1".equals(shflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被部门主管审核");
				return;
			}
			
			newfkvo.setShrflag1("1");
			newfkvo.setShrdate1(date);
			newfkvo.setShrid1(user);
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "部门审核完成");
		setListValue();
	}	
		
//	拒绝
	@Override
	protected void onBoCancelAudit() throws Exception {
		
			// 判断部门人员有无维护
			AggregatedValueObject modelVO = getBufferData().getCurrentVOClone();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
//			 判断单据是否已提交且未审核
			//部门已审核，财务没审核
			String cwflag = newfkvo.getCwflag()==null?"":newfkvo.getCwflag();
			
			if("1".equals(cwflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被财务审核");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog(newfkvo);
			
			if(flag){
				newfkvo.setShrflag1("0");
				newfkvo.setCwflag("0");
				newfkvo.setVoperatorflag("0");
				newfkvo.setIs_print(0);
				newfkvo.setShrdate1(null);
				newfkvo.setCwdate(null);
				newfkvo.setShrid1(null);
				newfkvo.setCwid(null);
				newfkvo.setRet_date(this._getDate());
				newfkvo.setRet_user(this._getOperator());
				NCLocator.getInstance().lookup(IVOPersistence.class).updateVO(newfkvo);
				
				RetRecordVO rtvo = new RetRecordVO();
				rtvo.setRet_user(this._getOperator());
				rtvo.setRet_date(this._getDate());
				rtvo.setRet_vemo(newfkvo.getQishenyuanyin());
				rtvo.setRet_type(1);
				rtvo.setPk_fkjhbill(newfkvo.getPk_fkjhbill());
				rtvo.setRet_address("部门付款审核");			
				HYPubBO_Client.insert(rtvo);
				
				
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "部门驳回完成");
				setListValue();
			}
	}
}