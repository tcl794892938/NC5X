package nc.ui.dahuan.fkjhdy;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.dahuan.billref.RefdhhtSourceDLG;
import nc.ui.dahuan.queryref.RefDhhtQueryDLG;
import nc.ui.dap.dapquery.BillQueryVO;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class MyEventHandler extends AbstractMyEventHandler {

	private AggregatedValueObject oldaggvo = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		this.getButtonManager().getButton(IBillButton.Edit).setVisible(false);//设置’Edit‘按钮不可见
	}

	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		// 制单标识设为0
		this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("voperatorflag", "0");
		oldaggvo = this.getBillCardPanelWrapper().getBillVOFromUI();
	}
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		this.getBillUI().setUserObject(oldaggvo);

		// 校验单据合法
		ClientUICheckRule rule = new ClientUICheckRule();
		String billType = "FKJH";
		String actionName = "N_FKJH_WRITE";
		AggregatedValueObject vo = getBillUI().getVOFromUI();
		rule.runClass(this.getBillUI(), billType, actionName, vo, null);
		super.onBoSave();

	}

	public void onBillRef() throws Exception { 
		
		// 只有业务员才能制单
		String user = this._getOperator();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String sql = "select count(1) from dh_fkgx_d t " 
					+" where t.pk_dept_user='"+user+"' " 
//					+"  "
					+" and nvl(t.dr,0)=0 ";
		
		int flag = (Integer)query.executeQuery(sql, new ColumnProcessor());
		if(flag < 1){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有业务员才可制单");
			return;
		}

		nc.vo.trade.pub.HYBillVO[] aggvos = queryToDhht();
		ArrayList list = new ArrayList();

		if (aggvos == null || aggvos.length <= 0)
			return;

		for (int i = 0; i < aggvos.length; i++) {
			DhFkjhbillVO headvo = new DhFkjhbillVO();
			nc.vo.dahuan.ctbill.DhContractVO headvo1 = (DhContractVO) aggvos[i]
					.getParentVO();
			headvo.setPk_busitype(headvo1.getPk_busitype());
			headvo.setCtcode(headvo1.getCtcode());
			headvo.setJobcode(headvo1.getJobcode());
			if(null == headvo1.getVdef6() || "".equals(headvo1.getVdef6())){
				String invSQL = "select invname from bd_invbasdoc where pk_invbasdoc='"+headvo1.getCtname()+"'";
				String fkctname = (String)query.executeQuery(invSQL, new ColumnProcessor());
				headvo.setCtname(fkctname);
			}else{
				headvo.setCtname(headvo1.getVdef6());
			}
			
			headvo.setVyearmonth(this._getDate().toString().substring(0, 7));
			headvo.setVprojectname(headvo1.getCtname());
			headvo.setDbilldate(this._getDate());
			headvo.setDctjetotal(headvo1.getDctjetotal());
			headvo.setLjfkje(headvo1.getLjfkjhje());
			headvo.setPk_billtype("FKJH");
			headvo.setPk_corp(headvo1.getPk_corp());
			headvo.setPk_dept(headvo1.getPk_deptdoc());
			headvo.setPk_bank(headvo1.getPk_bank());
			headvo.setSay_no(headvo1.getSax_no());
			
			if(0 == headvo1.getHttype()){
				headvo.setPk_cust2(headvo1.getPk_cust1());
			}else{
				headvo.setPk_cust2(headvo1.getPk_cust2());
			}			
			
			headvo.setPk_fkfs(headvo1.getPk_skfs());
			headvo.setVbillstatus(new Integer(8));
			headvo.setVoperatorid(this._getOperator());
			headvo.setVsourcebillid(headvo1.getPk_contract());
			headvo.setVsourcebilltype(headvo1.getPk_billtype());
			headvo.setStatus(nc.vo.pub.VOStatus.NEW);
			list.add(headvo);
		}
		SuperVO[] vos = (DhFkjhbillVO[]) list.toArray(new DhFkjhbillVO[0]);
		SuperVO[] queryVos = vos;

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

	private nc.vo.trade.pub.HYBillVO[] queryToDhht() {

		// 用来展示参照数据的 参照界面(预留接口,后边添加自定义界面展示类)
		String referClassName = "";
		// 上游单据节点号
		String srcFunNode = "12H10101";//1201
		// 业务类型暂定为空
		String businessType = null;
		// 查询模板ID
		String strQueryTemplateId = "1001AA1000000002QB4B";
		// 公司pk_corp
		String m_sCorpID = this._getCorp().getPk_corp();
		// 当前日期
		String m_sLogDate = this._getDate().toString();
		// 操作人员
		String m_sUserID = this._getOperator();
		// 初始化查询对话框
		RefDhhtQueryDLG qcDLG = new RefDhhtQueryDLG(this.getBillUI(), null,
				m_sCorpID, srcFunNode, m_sUserID, null);

		if (qcDLG == null) {
			this.getBillUI().showErrorMessage("获取查询条件时失败,可能无法获取查询模板!");
			return null;
		}

		if (qcDLG.showModal() == UIDialog.ID_OK) {
			String whereStr = qcDLG.getWhereSQL();
			if (whereStr == null || whereStr.length() <= 0) {
				whereStr = " 1=1 ";
			} else {
				whereStr = " " + whereStr + " ";
			}
			
			whereStr +=	" and (dh_contract.pk_deptdoc in (select x.pk_deptdoc from dh_fkgx x, dh_fkgx_d y  where x.pk_fkgx = y.pk_fkgx "+
			" and nvl(x.dr, 0) = 0 and nvl(y.dr, 0) = 0 and y.pk_dept_user = '"+ this._getOperator() + "' union all select t.pk_deptdoc "+
			" from dh_fkgx t where nvl(t.dr,0)=0 and t.pk_user1='"+ this._getOperator() + "') or nvl(httype,0)=2 )  ";
			
			RefdhhtSourceDLG billReferUI = new RefdhhtSourceDLG(null,
					m_sCorpID, m_sUserID, srcFunNode, whereStr, "DHHT",
					businessType, strQueryTemplateId, null, null, null,
					this.getBillUI());

			// 放入查询条件的DLG
			billReferUI.setQueyDlg(qcDLG);
			// 加载模版
			billReferUI.addBillUI();
			// 加载数据
			billReferUI.loadHeadData();
			if (billReferUI.showModal() == UIDialog.ID_OK) {
				nc.vo.trade.pub.HYBillVO[] rtnVOs = (nc.vo.trade.pub.HYBillVO[]) billReferUI
						.getRetVos();
				return rtnVOs;
			}
		}
		return null;
	}

	private void setCheckManAndDate(AggregatedValueObject vo) throws Exception {
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
				getBillUI()._getDate());
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
				getBillUI()._getOperator());
	}

	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);

		if (IdhButton.LINK_BillQUERY == intBtn) {
			onViewCt();
		}
		
		if(IdhButton.FKZD == intBtn ){
			onFkZd();
		}
	}

	private void onFkZd() throws Exception {
		// TODO Auto-generated method stub
		this.onBoEdit();
	}

	protected void onJointCheck() throws Exception {
		nc.vo.pub.AggregatedValueObject aggvo = this.getBufferData()
				.getCurrentVO();
		if (aggvo != null) {
			DhFkjhbillVO voHeader = null;
			// 得到单据表头VO
			voHeader = (DhFkjhbillVO) aggvo.getParentVO();
			if (voHeader == null) {
				this.getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
								"UPP4008bill-000098")/* @res "没有要联查的单据！" */);
				return;
			}
			String sBillPK = null;
			String sBillTypeCode = null;
			String sBillCode = null;

			sBillPK = voHeader.getPrimaryKey();
			sBillTypeCode = voHeader.getPk_billtype();
			sBillCode = voHeader.getVbillno();
			// 如果sBillPK和sBillTypeCode为空，联查没有意义。
			if (sBillPK == null || sBillTypeCode == null) {
				this.getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
								"UPP4008bill-000099")/* @res "该行没有对应单据！" */);
				return;
			}
			nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
					this.getBillUI(), sBillTypeCode, sBillPK, this.getBillUI()
							.getBusinessType(), this._getOperator(), sBillCode);
			soureDlg.showModal();
		} else {
			this.getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000154")/* @res "没有联查的单据！" */);
		}
	}

	private void onViewCt() {
		String vsourcebillid = null;

		if (getBillManageUI().isListPanelSelected()) {
			int row = (int) getBufferData().getCurrentRow();
			vsourcebillid = (String) getBillManageUI().getBillListPanel()
					.getHeadBillModel().getValueAt(row, "vsourcebillid");
		} else {
			vsourcebillid = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getHeadItem("vsourcebillid")
					.getValueObject();
		}
		if (vsourcebillid == null || vsourcebillid.length() <= 0) {
			getBillUI().showErrorMessage("没有合同信息");
			return;
		}

		BillQueryVO linkdata = new BillQueryVO();
		linkdata.setBillID(vsourcebillid);

		SFClientUtil.openLinkedQueryDialog("1201", getBillUI(), linkdata);
	}

	
	public void onBoAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				int isselected = getBillManageUI().getBillListPanel()
						.getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData()
							.getVOByRowNo(row);

					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// 判断单据是否已审核
					String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
					if("1".equals(zdflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已审核");
						return;
					}
					
					newfkvo.setVoperatorflag("1");
					fkList.add(newfkvo);
				}
			}

		} else {
			
			// 判断部门人员有无维护
			AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
			if(modelVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
			if(fkVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			// 判断单据是否已审核
			String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
			if("1".equals(zdflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已审核");
				return;
			}
			
			newfkvo.setVoperatorflag("1");
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "制单完成");

	}

	@Override
	protected void onBoCancelAudit() throws Exception {

		List<DhFkjhbillVO> fkList = new ArrayList<DhFkjhbillVO>();
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		if (getBillManageUI().isListPanelSelected()) {
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				int isselected = getBillManageUI().getBillListPanel().getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData().getVOByRowNo(row);
					
					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
					
					// 判断单据是否已审核
					String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
					String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
					if("1".equals(zdflag)&&!"1".equals(shflag)){
						newfkvo.setVoperatorflag("0");
						fkList.add(fkVo);
					}else{
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已审核");
						return;
					}					
				}
			}

		} else {
			
			// 判断部门人员有无维护
			AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
			if(modelVo==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			// 判断单据是否已审核
			String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
			String shflag = newfkvo.getShrflag1()==null?"":newfkvo.getShrflag1();
			if("1".equals(zdflag)&&!"1".equals(shflag)){
				newfkvo.setVoperatorflag("0");
				fkList.add(fkVo);
			}else{
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已审核");
				return;
			}
		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "弃审完成");
	}

	@Override
	protected void onBoSelAll() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().selectAllTableRow();
	}

	@Override
	protected void onBoSelNone() throws Exception {
		getBillManageUI().getBillListPanel().getParentListPanel().cancelSelectAllTableRow();
	}
	
	
	
	
}