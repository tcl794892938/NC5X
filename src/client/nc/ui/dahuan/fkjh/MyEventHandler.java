package nc.ui.dahuan.fkjh;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.dahuan.billref.RefdhhtSourceDLG;
import nc.ui.dahuan.exceltools.ExcelUtils;
import nc.ui.dahuan.queryref.RefDhhtQueryDLG;
import nc.ui.dap.dapquery.BillQueryVO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends AbstractMyEventHandler {

	private AggregatedValueObject oldaggvo = null;
	public String strCondition = "";
	public int qryStatus = 1;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		this.getButtonManager().getButton(IBillButton.Edit).setVisible(false);//设置’Edit‘按钮不可见
	}

	protected void onBoEdit() throws Exception {
		//制单时，参照合同，默认带出副总

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		
		if (getBillManageUI().isListPanelSelected()) {
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBufferData().updateView();
		}
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		// 制单标识设为0
		this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("voperatorflag", "0");
		oldaggvo = this.getBillCardPanelWrapper().getBillVOFromUI();
		
		
		
		BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
		
		Object obj1=card.getHeadItem("dctjetotal").getValueObject();
		Object obj2=card.getHeadItem("ljfkje").getValueObject();
		UFDouble ud1=obj1==null?new UFDouble(0):new UFDouble(obj1.toString());
		UFDouble ud2=obj2==null?new UFDouble(0):new UFDouble(obj2.toString());
		if(ud2.compareTo(ud1)>=0){
			getBillUI().showErrorMessage("数据加载失败，累计已付款大于等于合同金额，不可制单！");
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			return ;
		}
		//取得当前面版的 pk_contract 而在付款计划表中dh_fkjhbill，对应的合同PK 应该是vsourcebillid
	    String id  = (String)card.getHeadItem("vsourcebillid").getValueObject();
	    
	    //取得表头对应字段的值
//	    card.getHeadItem("");
	    //往表头对应字段塞值
//	    card.setHeadItem(strKey, Value);
	    //取得表体对应字段的值
//	    card.getBillModel(tableCode);
	    //往表体对应字段塞值
	    
	    
		String corp = this._getCorp().getPrimaryKey();
		String sql1 = " select t.pk_fuzong from dh_contract t where t.pk_contract = '"+id+"' and pk_corp='"+corp+"'";
		
		try {
			String fz1 = (String)query.executeQuery(sql1, new ColumnProcessor());
			this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("shrid2").setValue(fz1);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		this.getBillUI().setUserObject(oldaggvo);

		
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		
		// 同合同号一个月内只能制单一次
		Object ctcode = card.getHeadItem("ctcode").getValueObject();
		if(null==ctcode||"".equals(ctcode)){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请维护合同编号");
			return;
		}
		
		//String yrmth = this._getDate().toString().substring(0, 7);
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		// 判断修改或是新增
		Object pkfkjhobj = card.getHeadItem("pk_fkjhbill").getValueObject();
		if(null == pkfkjhobj || "".equals(pkfkjhobj)){
			/*List<DhFkjhbillVO> fklits = (List<DhFkjhbillVO>)query.retrieveByClause(DhFkjhbillVO.class, 
					" ctcode='"+ctcode+"' and nvl(dr,0)=0 and vyearmonth='"+yrmth+"' ");
			
			if(null!=fklits && fklits.size()>0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "一个合同在一个月之内只能有一张付款单");
				return;
			}*/
			
			List<DhFkjhbillVO> fklits2 = (List<DhFkjhbillVO>)query.retrieveByClause(DhFkjhbillVO.class, 
					" ctcode='"+ctcode+"' and nvl(dr,0)=0 and nvl(sealflag,0)=0 ");
			
			if(null!=fklits2 && fklits2.size()>0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该合同存在未走完流程的付款单");
				return;
			}
		}
		
		
		//保存校验
		int yseflag = 0;
		
		// 合同预付款
		Object iffobja = card.getHeadItem("iffknr1").getValueObject();
		boolean ifflaga = new UFBoolean(iffobja.toString()).booleanValue();
		if(ifflaga){
			card.setHeadItem("fknr", "合同预付款");
			yseflag++;
		}
		// 合同交货款
		Object iffobjb = card.getHeadItem("iffknr2").getValueObject();
		boolean ifflagb = new UFBoolean(iffobjb.toString()).booleanValue();
		if(ifflagb){
			card.setHeadItem("fknr", "合同交货款");
			yseflag++;
		}
		// 合同尾款
		Object iffobjc = card.getHeadItem("iffknr3").getValueObject();
		boolean ifflagc = new UFBoolean(iffobjc.toString()).booleanValue();
		if(ifflagc){
			card.setHeadItem("fknr", "合同尾款");
			yseflag++;
		}
		// 其他付款
		Object iffobjd = card.getHeadItem("iffknr4").getValueObject();
		boolean ifflagd = new UFBoolean(iffobjd.toString()).booleanValue();
		if(ifflagd){
			card.setHeadItem("fknr", "其他付款");
			yseflag++;
		}
		// 全额付款
		Object iffobje = card.getHeadItem("iffknr5").getValueObject();		
		boolean ifflage = new UFBoolean(iffobje.toString()).booleanValue();
		if(ifflage){
			card.setHeadItem("fknr", "全额付款");
			yseflag++;
		}
		// 合同交货款
		Object iffobjf = card.getHeadItem("iffknr6").getValueObject();
		boolean ifflagf = new UFBoolean(iffobjf.toString()).booleanValue();
		if(ifflagf){
			card.setHeadItem("fknr", "合同交货款");
			yseflag++;
		}
		
		if(yseflag == 0){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择付款方式");
			return;
		}else if(yseflag > 1){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "只能选择一种付款方式");
			return;
		}		
		
		// 校验单据合法
//		ClientUICheckRule rule = new ClientUICheckRule();
//		String billType = "FKJH";
//		String actionName = "N_FKJH_WRITE";
		AggregatedValueObject vo = getBillUI().getVOFromUI();
		
		//付款计划金额，不可以大于合同金额 wanglong 2014-05-23
		UFDouble  ufhtje = vo.getParentVO().getAttributeValue("dctjetotal")==null?new UFDouble("0.00"):new UFDouble(vo.getParentVO().getAttributeValue("dctjetotal").toString());
		UFDouble  ufbcfkje = vo.getParentVO().getAttributeValue("dfkje")==null?new UFDouble("0.00"):new UFDouble(vo.getParentVO().getAttributeValue("dfkje").toString());
		UFDouble  ufljyfje = vo.getParentVO().getAttributeValue("ljfkje")==null?new UFDouble("0.00"):new UFDouble(vo.getParentVO().getAttributeValue("ljfkje").toString());
		UFDouble  ufkyfkje = new UFDouble(ufhtje.sub(ufljyfje).toString(),2);
		
//		String sql = " select nvl(k.dctjetotal,0)-nvl(k.ljfkjhje,0)-sum(nvl(f.dfkje,0)) syamt from (select t.ctcode,t.dctjetotal,t.ljfkjhje from dh_contract t "+
//		" where t.ctcode = '"+ctcode+"' and nvl(t.dr,0)=0) k left join dh_fkjhbill f on f.ctcode=k.ctcode and nvl(f.is_pay,0)=0 " +
//		" group by k.dctjetotal,k.ljfkjhje ";
//		
//		List<Map<String,Object>> sylist = (List<Map<String,Object>>)query.executeQuery(sql, new MapListProcessor());
//		
//		UFDouble ufkyfkje = new UFDouble(sylist.get(0).get("syamt").toString());
		
		
		if(ufkyfkje.doubleValue() < ufbcfkje.doubleValue()){
			this.getBillUI().showErrorMessage("计划付款金额["+ufbcfkje+"]大于合同可付款金额["+ufkyfkje+"]");
			return;
		}
		
		
		
		System.out.println(vo.getParentVO().getAttributeValue("pk_billtype"));
//		
//		rule.runClass(this.getBillUI(), billType, actionName, vo, null);
		
		
		super.onBoSave();

	}

	public void onBillRef() throws Exception { 
		
		// 只有业务员才能制单 //2016-04-11修改为所有人可以参照制单
		String user = this._getOperator();
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String pk_corp=_getCorp().getPk_corp();
		
		Object gc=null;//工程管理部pk
		if(!pk_corp.equals("1002")){
			
//			查询工程部pk
			String sql2="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
				"and pk_corp='"+pk_corp+"' and deptname='工程管理部' and canceled='N'";
			
			gc=query.executeQuery(sql2, new ColumnProcessor());
			
			if(gc==null){
				MessageDialog.showHintDlg(getBillUI(), "提示", "请先维护部门工程管理部");
				return ;
			}
			
			String sql3="select count(1) from dh_fkgx_d where nvl(dr,0)=0 and pk_dept_user='"+user+"' " +
					"and pk_fkgx=(select pk_fkgx from dh_fkgx x where nvl(x.dr,0)=0 " +
					"and pk_deptdoc='"+gc+"' and pk_corp='"+pk_corp+"')";
			Integer it2=(Integer)query.executeQuery(sql3, new ColumnProcessor());
			
			if(it2 < 1){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有工程部业务员才可制单");
				return;
			}
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
			
			String invSQL = "select invname from bd_invbasdoc where pk_invbasdoc='"+headvo1.getCtname()+"'";
			String fkctname = (String)query.executeQuery(invSQL, new ColumnProcessor());
			headvo.setCtname(fkctname);
			
			headvo.setVyearmonth(this._getDate().toString().substring(0, 7));
			headvo.setVprojectname(headvo1.getVdef6());
			headvo.setDbilldate(this._getDate());
			headvo.setDctjetotal(headvo1.getDctjetotal());
			headvo.setLjfkje(headvo1.getLjsjfkje());
			headvo.setSjspje(headvo1.getSjspje());  //by tcl
			headvo.setPk_billtype("FKJH");
			headvo.setPk_bank(headvo1.getPk_bank());
			headvo.setSay_no(headvo1.getSax_no());
			headvo.setPk_corp(headvo1.getPk_corp());
			
			headvo.setPk_dept(headvo1.getPk_deptdoc());
			headvo.setFk_dept(headvo1.getHt_dept());
			/*if(gc!=null){
				headvo.setPk_dept(gc.toString());
				headvo.setFk_dept(gc.toString());
			}*/
			
			headvo.setHttype(headvo1.getHttype());
			headvo.setPk_cttype(headvo1.getPk_cttype());
			
			//往画面塞值，其实在这里就可以把副总带出来，不用再制单的时候才塞值
//			headvo.setVsourcebillid(headvo1.getPk_contract());
			
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
			
			headvo.setPk_currenty(headvo1.getPk_currenty());
			headvo.setCurrenty_rate(headvo1.getCurrenty_rate());
			
			
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
		String srcFunNode = "12H103011";//1201
		// 业务类型暂定为空
		String businessType = null;
		// 查询模板ID
		String strQueryTemplateId = "1001AA1000000002QB4B";//0001AA1000000000HZOG  1001AA1000000002QB4B
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
			ClientEnvironment ce = ClientEnvironment.getInstance();
			String pk_corp=ce.getCorporation().getPrimaryKey();
			whereStr=whereStr.replaceFirst("#Sys_CurrCorp#", pk_corp);
			if (whereStr == null || whereStr.length() <= 0) {
				whereStr = " 1=1 ";
			} else {
				whereStr = " " + whereStr + " ";
			}
		
			//String yrmth = this._getDate().toString().substring(0, 7);
			
			//去除相同月份的合同
			/*whereStr += " and not exists (select 1 from dh_fkjhbill where dh_fkjhbill.vyearmonth='"+yrmth+"' "
					+" and nvl(dh_fkjhbill.dr,0)=0 and dh_fkjhbill.ctcode=dh_contract.ctcode) ";*/
			
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
		
		if(IdhButton.FILEUPLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "付款单额度管理");
		}
		
		if(IdhButton.FILEDOWNLOAD == intBtn){
			DocumentManagerHT.showDM(this.getBillUI(), "FKJH", "付款单附件");
		}
	}

	private void onFkZd() throws Exception {
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
			
			DhFkjhbillVO[] fbillVO = (DhFkjhbillVO[])getBillManageUI().getBillListPanel().getHeadBillModel()
																.getBodySelectedVOs(DhFkjhbillVO.class.getName());
			if(null == fbillVO || fbillVO.length == 0){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
				return;
			}
			
			int num = getBillManageUI().getBillListPanel().getHeadBillModel().getRowCount();
			for (int row = 0; row < num; row++) {
				int isselected = getBillManageUI().getBillListPanel()
						.getHeadBillModel().getRowState(row);
				if (isselected == 4) {
					AggregatedValueObject modelVo = getBufferData()
							.getVOByRowNo(row);

					DhFkjhbillVO fkVo = (DhFkjhbillVO)modelVo.getParentVO();
					if(fkVo==null){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "请选择单据");
						return;
					}
					String aa = fkVo.getPrimaryKey();
					if(aa==null){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "请维护该单据");
						return;
					}
					DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
				
					
					// 判断单据是否已审核
					String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
					if("1".equals(zdflag)){
						MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已提交");
						return;
					}
					
					newfkvo.setVoperatorflag("1");
					newfkvo.setIs_single(0);
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
			String aa = fkVo.getPrimaryKey();
			if(aa==null){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "请维护该单据");
				return;
			}
			
			DhFkjhbillVO newfkvo = (DhFkjhbillVO)query.retrieveByPK(DhFkjhbillVO.class, fkVo.getPrimaryKey());
			
			
			// 判断单据是否已审核
			String zdflag = newfkvo.getVoperatorflag()==null?"":newfkvo.getVoperatorflag();
			if("1".equals(zdflag)){
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已提交");
				return;
			}
			
			newfkvo.setVoperatorflag("1");
			newfkvo.setIs_single(0);
			fkList.add(newfkvo);

		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "提交完成");
		
		if (getBillManageUI().isListPanelSelected()) {
			if(qryStatus == 2){
				clearListPanelVO();
			}else{
				getBufferData().clear();
				// 增加数据到Buffer
				addDataToBuffer(fkList.toArray(new DhFkjhbillVO[0]));

				updateBuffer();
			}
		}else{
			super.onBoRefresh();
		}

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
						fkList.add(newfkvo);
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
				fkList.add(newfkvo);
			}else{
				MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已审核");
				return;
			}
			
		}
		
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVOList(fkList);
		MessageDialog.showHintDlg(getBillManageUI(), "提示", "单据已驳回");
		
		if (getBillManageUI().isListPanelSelected()) {
			if(qryStatus == 2){
				clearListPanelVO();
			}else{
				getBufferData().clear();
				// 增加数据到Buffer
				addDataToBuffer(fkList.toArray(new DhFkjhbillVO[0]));

				updateBuffer();
			}
		}else{
			super.onBoRefresh();
		}
		
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

		String pkuser = this._getOperator();
		String pkcorp = this._getCorp().getPrimaryKey();
		
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

//		2017-12-14自动化权限(只有自动化部的查询所有包含电气的合同)
		String sqlss="select count(1) from v_deptperonal v where v.pk_user='"+pkuser+"' and pk_corp='"+pkcorp+"' and dept_name='自动化部' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer it=(Integer)iQ.executeQuery(sqlss, new ColumnProcessor());
		String str="";
		if(it>0){
			str=" or dh_fkjhbill.ctcode like '%电气%' ";
		}
		
		strCondition = strWhere.toString()+	
						" and (dh_fkjhbill.pk_dept in (select v.pk_deptdoc from v_deptperonal v " +
						" where v.pk_user = '"+pkuser+"' and v.pk_corp = '"+pkcorp+"') "+
						" or dh_fkjhbill.fk_dept in (select v.pk_deptdoc from v_deptperonal v " +
						" where v.pk_user = '"+pkuser+"' and v.pk_corp = '"+pkcorp+"') "+str+") ";
		
		clearListPanelVO();
		
		qryStatus = 2;
		
	}
	
	public void clearListPanelVO() throws Exception{
		
		SuperVO[] queryVos = queryHeadVOs(strCondition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	@Override
	protected void onBoExport() throws Exception {

		ClientUI cui = (ClientUI)this.getBillUI();
		BillModel bmodel = cui.getBillListPanel().getHeadBillModel();
		
		UITable btable = cui.getBillListPanel().getHeadTable();
		
		int rows = bmodel.getRowCount();
		int cols = btable.getColumnCount();
		
		List<Object[]> lists = new ArrayList<Object[]>();
		String[] headColsCN = new String[]{
				"合同编号","合同名称","项目名称","付款单位","付款方式",
				"合同金额","累计已付款","计划付款金额","付款内容","付款日期"
		};
		
		for(int i=0;i<rows;i++){
			
			Object[] objs = new Object[cols];
			
			for(int j=0;j<cols;j++){
				objs[j] = btable.getValueAt(i, j);
			}
			
			lists.add(objs);
		}
		
		ExcelUtils.doExport("付款单", lists, headColsCN, cui);
	
	}
	
	
	
}