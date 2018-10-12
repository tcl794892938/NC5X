package nc.ui.dahuan.fkjhjlqs;

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
		
		// 部门主管添加过滤条件
		String user = _getOperator();
		String pkCorp = _getCorp().getPrimaryKey();
		
		//副总弃审时，部门审核标志shrflag1=1，副总审核标志shrflag2=1，财务预审标志cwflag为1，并且工程部未打印is_print = 0
		condition = strWhere.toString() 
						+ " and nvl(dh_fkjhbill.shrflag1,0) = 1 " 
						+ " and nvl(dh_fkjhbill.cwflag,0) = 1 " 
						+ " and nvl(dh_fkjhbill.shrflag2,0) = 1 " 
						+ " and nvl(dh_fkjhbill.is_print,0)=0 "
						+ " and dh_fkjhbill.shrid2 = '"+user+"'" 
						+ " order by dh_fkjhbill.pk_cust2";

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

	@Override
	protected void onBoCancelAudit() throws Exception {
			
			// 判断部门人员有无维护
			AggregatedValueObject modelVO = getBufferData().getCurrentVO();
			if(modelVO==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
		
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVO.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)iQ.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			// 判断单据是否副总已审核且工程部未打印
			String fzsh = newfkvo.getShrflag2()==null?"":newfkvo.getShrflag2();
			int gcdy = newfkvo.getIs_print()==null ? 0 : newfkvo.getIs_print();
			if("0".equals(fzsh)||"".equals(fzsh)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据未被副总审核");
				return;
			}
			if(gcdy == 1){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已被工程部打印");
				return;
			}
			
			CanAudMemoDialog cadg = new CanAudMemoDialog(getBillManageUI());
			boolean flag = cadg.showCanAudMemoDialog(newfkvo);
			
			if(flag){
				newfkvo.setShrflag2("0");
				newfkvo.setShrflag1("0");
				newfkvo.setCwflag("0");
				newfkvo.setVoperatorflag("0");
				newfkvo.setShrdate2(null);		
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
				rtvo.setRet_address("副总付款弃审");			
				HYPubBO_Client.insert(rtvo);
				
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "副总弃审完成");
				setListValue();
			}
	}

		
}