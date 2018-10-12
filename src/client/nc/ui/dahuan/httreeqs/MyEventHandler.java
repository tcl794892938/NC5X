package nc.ui.dahuan.httreeqs;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.retrecord.RetRecordVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	private BillManageUI getBillManageUI(){
		return (BillManageUI)this.getBillUI();
	}

	@Override
	protected void onBoCard() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = getBillManageUI().getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}

	public String condition = "";
	
	// 查询过滤条件
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		String user = this._getOperator();
		String corpid = this._getCorp().getPrimaryKey();
		
		//部门弃审有两种情况 nvl(fuzong_flag,0) = 0, vbillstatus = 1或者 2 的时候弃审
		//1、预审以后，ys_flag=1,vbillstatus由8变为3,然后部门审核过后，vbillstatus由3变为2，
		//	这个时候，不需要进行进行副总审批，nvl(fuzong_flag,0) = 0 ，vbillstatus由2变为1，可以弃审
		//2、vbillstatus由3变为2后，且需要副总审批，当副总还未审核的时候，nvl(fuzong_flag,0) = 0，vbillstatus = 2，可以弃审
		//当副总审批过后，fuzong_flag=1,且vbillstatus由2变为1，则不能弃审
		//并且当前登录用户为部门主管 vapproveid
		//nvl(vdef4,0) =1 部门主管已经审核
		
//		销售合同都走部门审核
		//采购合同如果是溧阳公司，需要走，其他公司直接财务审核节点
		String httype="";
		if(!_getCorp().getPk_corp().equals("1002")){//不是溧阳公司，需要过滤采购合同
			httype+= " and httype = 0 ";
		}
		condition = strWhere.toString() + " and nvl(vbillstatus,0) =2 and pk_corp='"+corpid+"' " 
											   + " and vapproveid = '"+user+"' "
											   + " and nvl(ys_flag,0) = 1 and pk_ysid is not null "
											   + " and nvl(is_seal,0) =0 "
											   + " and nvl(is_pay,0) = 0 "
											   + " and nvl(fuzong_flag,0) = 0 and nvl(vdef4,0)=1 "+httype;
							//fuzong_flag 有两个状态 0代表未审核，1代表审核

		setListVO();
	}
	
	private void setListVO() throws Exception{
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

	// 弃审
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		DhContractVO dhcVO = (DhContractVO)aggvo.getParentVO();
		if(null == dhcVO){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}		

		/*int ret = checkBillStatus(dhcVO);
		if(1!=ret){
			MessageDialog.showErrorDlg(this.getBillUI(), "警告", "单据已被处理过，不可再审核");
			return;
		}*/

		CanAudMemoDialog cadg = new CanAudMemoDialog(this.getBillUI());
		boolean flag = cadg.showCanAudMemoDialog(dhcVO);

		if(flag){
			dhcVO.setVbillstatus(8);
			dhcVO.setYs_flag("0");
			dhcVO.setVdef4("0");
			dhcVO.setDapprovedate(null);
			dhcVO.setCmodifyid(this._getOperator());
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String dtsql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
			String tmdate = (String)iQ.executeQuery(dtsql, new ColumnProcessor());
			
			dhcVO.setTmodifytime(tmdate);
			HYPubBO_Client.update(dhcVO);
			
			RetRecordVO rtvo = new RetRecordVO();
			rtvo.setRet_user(this._getOperator());
			rtvo.setRet_date(this._getDate());
			rtvo.setRet_vemo(dhcVO.getRet_vemo());
			rtvo.setRet_type(0);
			rtvo.setPk_contract(dhcVO.getPk_contract());
			rtvo.setRet_address("部门合同弃审");			
			HYPubBO_Client.insert(rtvo);
			
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "弃审已完成");
			// 界面刷新
			setListVO();
		}
	}
	
	/**
	 * 单据状态校验
	 * @return 1、单据可以审核；2、单据状态发生改变；3、单据不是提交态
	 * */
	private int checkBillStatus(DhContractVO vo) throws Exception{
		
		// SQL条件拼接：根据选择单据的主键和单据状态判断符合条件的单据数量是否和选择的数量一致		
		String sql = " select count(1) from dh_contract where " +
						" pk_contract='"+vo.getPrimaryKey()+"' and vbillstatus='"+vo.getVbillstatus()+"'";
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		int value = (Integer)query.executeQuery(sql, new ColumnProcessor());
		
		// 校验结果
		if(value != 1){
			return 2;
			//2表示单据状态发生改变
		}
		
		return 1;
		//1表示单据可以审核
	}
	

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == IdhButton.FILEUPLOAD){
			DhContractVO dvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", dvo.getCtcode());
		}
	}
		
	
}