package nc.ui.demo.tree.tree03;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.job.IJobtypePrivate;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.CanAuditCorpBtnVO;
import nc.ui.bfriend.button.CanAuditDeptBtnVO;
import nc.ui.bfriend.button.ExceptFHSKBtnVO;
import nc.ui.bfriend.button.ExceptHTBtnVO;
import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.bfriend.button.ImportHttkBtnVO;
import nc.ui.bfriend.button.RetCommitBtnVO;
import nc.ui.bfriend.button.SealBtnVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.MultiChildBillTreeManageUI;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;

public class MultiChildTreeCardUI extends MultiChildBillTreeManageUI implements
		ILinkQuery {

	private static final long serialVersionUID = 1L;

	private IJobtypePrivate pfserver = null;

	protected AbstractManageController createController() {
		return new MultiChildTreeCardController();
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	public MultiChildTreeCardUI() {
		super();
		modifyRootNodeShowName("合同");
		this.getBillCardPanel().setAutoExecHeadEditFormula(true); // 设置表头编辑公式可执行
		this.getButtonManager().getButton(IdhButton.YCHT).setEnabled(true);
		this.getButtonManager().getButton(IdhButton.FHSK).setEnabled(true);
	}

	protected IVOTreeData createTableTreeData() {
		return new MultiChildTreeCardData();
	}

	protected IVOTreeData createTreeData() {

		return new MultiChildTreeCardData();
	}

	public void updateTreeData() {
		this.getBillTree().setModel(getBillTreeModel(getCreateTreeData()));

	}

	protected UITree getBillTree() {
		return super.getBillTree();
	}

	protected ManageEventHandler createEventHandler() {
		return new MultiChildTreeCardEventHandler(this, getUIControl());
	}

	public String getRefBillType() {

		return null;
	}

	protected void initSelfData() {
		
		// 修改按钮
		ButtonObject editBtn = (ButtonObject)this.getButtonManager().getButton(IBillButton.Edit);
		((ButtonVO)editBtn.getData()).setExtendStatus(new int[]{ 2,5 });
		
		// 提交按钮
		ButtonObject commitBtn = (ButtonObject)this.getButtonManager().getButton(IBillButton.Commit);
		((ButtonVO)commitBtn.getData()).setExtendStatus(new int[]{ 2 });
		
		// 驳回按钮
		ButtonObject retBtn = (ButtonObject)this.getButtonManager().getButton(IdhButton.RET_COMMIT);
		((ButtonVO)retBtn.getData()).setExtendStatus(new int[]{ 3 });
		
		ButtonObject impBtn = (ButtonObject)this.getButtonManager().getButton(IBillButton.ImportBill);
		((ButtonVO)impBtn.getData()).setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		
		getBillListPanel().getHeadTable().setCellSelectionEnabled(false);//取消单元格
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true);//设置行选择
		
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	public void setDefaultData() throws Exception {

		String strCorp = getClientEnvironment().getCorporation().getPk_corp();
		VOTreeNode node = getBillTreeSelectNode();
		JobtypeVO typevo = queryJobtype();

		if (strCorp == null || strCorp.equalsIgnoreCase(""))
			getBillCardPanel().setHeadItem("pk_corp", "0001");
		else
			getBillCardPanel().setHeadItem("pk_corp", strCorp);
		getBillCardPanel().setHeadItem("pk_billtype", "DHHT");
		getBillCardPanel().setTailItem("dbilldate", this._getDate());
		getBillCardPanel().setTailItem("voperatorid", this._getOperator());
		getBillCardPanel().setHeadItem("jobtype", typevo.getPk_jobtype());

		if (node != null) {
			String jobcode = node.getCode();
			String ctcode = node.getData().getAttributeValue("ctcode")
					.toString();

			getBillCardPanel().setHeadItem("jobcode", jobcode);
			getBillCardPanel().setHeadItem("ctcode", ctcode);
		}
		getBillCardPanel().setHeadItem("vbillstatus", 8);
		getBillCardPanel().getHeadItem("vbillstatus").setEdit(false);
		getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
		
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		String tcsql = " select count(1) from sm_user_role f where f.cuserid='"+this._getOperator()+"' and f.pk_corp='"+strCorp+"' " +
			" and f.pk_role=(select r.pk_role from sm_role r where r.role_code='DHCBFY') ";
		int ttc = (Integer)query.executeQuery(tcsql, new ColumnProcessor());
		
		if(ttc != 0){
			getBillCardPanel().getHeadItem("ht_dept").setEdit(true);
		}else{
			
			String sql = "select a.pk_deptdoc from dh_fkgx a,dh_fkgx_d b where a.pk_fkgx=b.pk_fkgx and nvl(a.dr,0)=0"
		 		+ " and nvl(b.dr,0)=0 and b.pk_dept_user='"+this._getOperator()+"' and a.pk_corp='"+strCorp+"'";
			String pkDept = (String)query.executeQuery(sql, new ColumnProcessor());
			getBillCardPanel().setHeadItem("pk_deptdoc", pkDept);
			
			String deptqry = "select deptname from bd_deptdoc where pk_deptdoc='"+pkDept+"'";
			String htDept = (String)query.executeQuery(deptqry, new ColumnProcessor());
			if(htDept.contains("自动化")){
				getBillCardPanel().getHeadItem("ht_dept").setEdit(true);
				UIRefPane deptRef = (UIRefPane)this.getBillCardPanel().getHeadItem("ht_dept").getComponent();
				deptRef.getRefModel().addWherePart(" and pk_fathedept = '1001AA10000000000J3Y' and pk_deptdoc <> '"+pkDept+"'");//经营部门
			}else{
				getBillCardPanel().getHeadItem("ht_dept").setEdit(true);
				getBillCardPanel().setHeadItem("ht_dept", pkDept);
			}
			//新加逻辑2017-11-17 by tcl
			Object obj=getBillCardPanel().getHeadItem("ctcode").getValueObject();
			if(obj!=null&&!"".equals(obj)){
				
				String code=obj.toString().substring(0, 1);
				String sqlnew="select a.pk_deptdoc,c.deptname from dh_fkgx a,dh_fkgx_d b,bd_deptdoc c where a.pk_fkgx=b.pk_fkgx " +
						"and a.pk_deptdoc=c.pk_deptdoc and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and " +
						"b.pk_dept_user='"+this._getOperator()+"' and a.pk_corp='"+strCorp+"'";
				
				List<Map<String, Object>> maplsit=(List<Map<String, Object>>)query.executeQuery(sqlnew, new MapListProcessor());
				if(maplsit==null||maplsit.size()<=0){
					return ;
				}
				
				for(Map<String, Object> map:maplsit){
					
					Object dept=map.get("deptname");
					String deptstr=dept==null?"":dept.toString();
					if(deptstr.startsWith(code)){
						getBillCardPanel().setHeadItem("pk_deptdoc", map.get("pk_deptdoc"));
						getBillCardPanel().setHeadItem("ht_dept", map.get("pk_deptdoc"));
						return ;
					}
				}
			}
			//end
		}
	}
	

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			DhContractVO patVO = (DhContractVO)vo.getParentVO();
			int status = patVO.getVbillstatus();
			if(8==status){
				return 2;
			}
			if(3==status){
				return 3;
			}
			if(2==status){
				return 4;
			}
			if(1==status){
				return 5;
			}
			return 6;
		}
		return 1;
	}

	private JobtypeVO queryJobtype() {

		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
		try {
			JobtypeVO[] typevos = getJobtypePrivate().queryAllJobtypeVOs(
					pk_corp, false);
			for (int i = 0; i < typevos.length; i++) {
				if (typevos[i].getJobtypecode().equalsIgnoreCase("01")) {
					return typevos[i];
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void afterEdit(BillEditEvent e){
		super.afterEdit(e);
		
		// 项目经理根据销售合同自动带出
		if("ctcode".equals(e.getKey())||"httype".equals(e.getKey())){
			BillCardPanel card = this.getBillCardPanel();
			String ctcode = card.getHeadItem("ctcode").getValueObject()==null ? "" : card.getHeadItem("ctcode").getValueObject().toString();
			String httype = card.getHeadItem("httype").getValueObject()==null ? "2" : card.getHeadItem("httype").getValueObject().toString();
			
			String pkcorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			if("httype".equals(e.getKey())){
				card.setHeadItem("xm_amount", null);
			}
			try {
				Object ctname=card.getHeadItem("ctname").getValueObject();
				String name=ctname==null?"":ctname.toString();
				String typesql="select invname from bd_invbasdoc where pk_invbasdoc='"+name+"' and nvl(dr,0)=0";
				IUAPQueryBS iQuery=NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Object obj = iQuery.executeQuery(typesql, new ColumnProcessor());
				String str=obj==null?"":obj.toString();
				Boolean fag=(str.equals("现场费用"))||(str.equals("施工成本"))||(str.equals("加工成本"));
				if(httype.equals("2")&&fag){
					
					card.getHeadItem("xm_amount").setEnabled(true);
				}else{
					card.getHeadItem("xm_amount").setEnabled(false);
				}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(!"".equals(ctcode) && "1".equals(httype)){
				try{
					if(ctcode.indexOf("-")>0){
						String xsctcode = ctcode.substring(0, ctcode.indexOf("-")+1)+"00";
						IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
						
						List<DhContractVO> dhclit = (List<DhContractVO>)query.retrieveByClause(DhContractVO.class, 
																								" ctcode = '"+xsctcode+"' and nvl(dr,0)=0 and pk_corp = '"+pkcorp+"' ");
						
						if(null == dhclit || dhclit.size() <= 0){
							MessageDialog.showErrorDlg(this, "警告", "请先维护销售合同");
							card.setHeadItem("ctcode", xsctcode);
							card.setHeadItem("httype", "2");
						}else if(dhclit.size()>1){
							
						}else{
							DhContractVO cvo = dhclit.get(0);
							card.setHeadItem("vdef6", cvo.getVdef6());
							UIRefPane xmjlref = (UIRefPane)card.getHeadItem("pk_xmjl").getComponent();
							xmjlref.setPK(cvo.getPk_xmjl());
							card.setHeadItem("pk_fuzong", cvo.getPk_fuzong());
						}	
					}else{
						card.setHeadItem("vdef6", null);
						card.setHeadItem("pk_xmjl", null);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	
		if (e.getKey().equalsIgnoreCase("pk_cust1")) {
			UIRefPane pk_cust1 = (UIRefPane) this.getBillCardPanel()
					.getHeadItem("pk_cust1").getComponent();
			this.getBillCardPanel().setHeadItem("vdef1", pk_cust1.getRefPK());
			
			this.getBillCardPanel().setHeadItem("pk_bank", null);
			this.getBillCardPanel().setHeadItem("sax_no", null);
			
//			String accsql = "select t.account,c.bankdocname from bd_bankaccbas t,bd_bankdoc c where t.pk_bankdoc = c.pk_bankdoc "+
//							" and nvl(t.dr,0)=0 and nvl(c.dr,0)=0 and t.pk_bankaccbas in (select k.pk_accbank from bd_cumandoc m, " +
//							" bd_custbank k where m.pk_cumandoc='"+pk_cust1.getRefPK()+"' and m.pk_cubasdoc=k.pk_cubasdoc and nvl(k.dr,0)=0) ";
			
			String accsql = "select t.def1,t.def2 from bd_cumandoc t where t.pk_cumandoc = '"+pk_cust1.getRefPK()+"'";
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			try{
				List<Map<String,String>> ml = (List<Map<String,String>>)iQ.executeQuery(accsql, new MapListProcessor());
				if(null != ml && ml.size()>0 ){
					Map<String,String> mm = ml.get(0);
					this.getBillCardPanel().setHeadItem("pk_bank", mm.get("def1"));
					this.getBillCardPanel().setHeadItem("sax_no",  mm.get("def2"));
				}
			}catch(Exception e2){
				e2.printStackTrace();
			}
		} else if (e.getKey().equalsIgnoreCase("pk_cust2")) {
			UIRefPane pk_cust2 = (UIRefPane) this.getBillCardPanel()
					.getHeadItem("pk_cust2").getComponent();
			this.getBillCardPanel().setHeadItem("vdef2", pk_cust2.getRefPK());
			
			this.getBillCardPanel().setHeadItem("pk_bank", null);
			this.getBillCardPanel().setHeadItem("sax_no", null);
			
//			String accsql = "select t.account,c.bankdocname from bd_bankaccbas t,bd_bankdoc c where t.pk_bankdoc = c.pk_bankdoc "+
//			" and nvl(t.dr,0)=0 and nvl(c.dr,0)=0 and t.pk_bankaccbas in (select k.pk_accbank from bd_cumandoc m, " +
//			" bd_custbank k where m.pk_cumandoc='"+pk_cust2.getRefPK()+"' and m.pk_cubasdoc=k.pk_cubasdoc and nvl(k.dr,0)=0) ";
			
			String accsql = "select t.def1,t.def2 from bd_cumandoc t where t.pk_cumandoc = '"+pk_cust2.getRefPK()+"'";
			
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			try{
			List<Map<String,String>> ml = (List<Map<String,String>>)iQ.executeQuery(accsql, new MapListProcessor());
			if(null != ml && ml.size()>0 ){
				Map<String,String> mm = ml.get(0);
				this.getBillCardPanel().setHeadItem("pk_bank", mm.get("def1"));
				this.getBillCardPanel().setHeadItem("sax_no",  mm.get("def2"));
			}
			}catch(Exception e2){
			e2.printStackTrace();
			}
		}
		
		if("ctname".equals(e.getKey())){
			BillModel model = this.getBillCardPanel().getBillModel();
			model.clearBodyData();
			getBillCardPanel().setHeadItem("xm_amount", null);
			if(null != e.getValue() && !"".equals(e.getValue())){			
				model.addLine();
				UIRefPane invref = (UIRefPane)e.getSource();
				String pk = invref.getRefPK();
				String code = invref.getRefCode();
				
				String sql = "select t.invspec || t.invtype vggxh,t.pk_measdoc pk_danw," +
						       "(select d.measname from bd_measdoc d where t.pk_measdoc = d.pk_measdoc) dwname " +
						       "from bd_invbasdoc t where t.pk_invbasdoc = '"+pk+"'";
				IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				try{
					List<Map<String,String>> maplit = (List<Map<String,String>>)query.executeQuery(sql, new MapListProcessor());
					Map<String,String> mp = maplit.get(0);
					
					DefaultConstEnum invCE = new DefaultConstEnum(pk,code);
					
					model.setValueAt(pk, 0, "pk_invbasdoc");
					model.setValueAt(invCE, 0, "invcode");
					model.setValueAt(invref.getRefName(), 0, "invname");
					model.setValueAt(mp.get("vggxh"), 0, "vggxh");
					model.setValueAt(mp.get("dwname"), 0, "pk_danw");
					model.setValueAt(mp.get("dwname"), 0, "dwname");
					
					Object ctname=getBillCardPanel().getHeadItem("ctname").getValueObject();
					String name=ctname==null?"":ctname.toString();
					String typesql="select invname from bd_invbasdoc where pk_invbasdoc='"+name+"' and nvl(dr,0)=0";
					IUAPQueryBS iQuery=NCLocator.getInstance().lookup(IUAPQueryBS.class);
					Object obj = iQuery.executeQuery(typesql, new ColumnProcessor());
					String str=obj==null?"":obj.toString();
					Boolean fag=(str.equals("现场费用"))||(str.equals("施工成本"))||(str.equals("加工成本"));
					String httype = getBillCardPanel().getHeadItem("httype").getValueObject()==null ? "2" : getBillCardPanel().getHeadItem("httype").getValueObject().toString();
					if(httype.equals("2")&&fag){
						
						getBillCardPanel().getHeadItem("xm_amount").setEnabled(true);
					}else{
						getBillCardPanel().getHeadItem("xm_amount").setEnabled(false);
					}
				}catch(Exception e3){
					e3.printStackTrace();
				}
			}
		}
		
		// 汇率 金额处理
		if("currenty_rate".equals(e.getKey())){
			// 汇率
			UFDouble currenty_rate = new UFDouble(e.getValue()==null?"0.00":e.getValue().toString());
			BillModel bmodel = this.getBillCardPanel().getBillModel();
			for(int i=0;i<bmodel.getRowCount();i++){
				// 数量
				UFDouble nnumber = new UFDouble(bmodel.getValueAt(i, "nnumber")==null?"0.00":bmodel.getValueAt(i, "nnumber").toString());
				// 单价(外币)
				UFDouble currenty_amount = new UFDouble(bmodel.getValueAt(i, "currenty_amount")==null?"0.00":bmodel.getValueAt(i, "currenty_amount").toString());
				// 总额(外币)
				UFDouble waibi_amount = currenty_amount.multiply(nnumber, 2);
				bmodel.setValueAt(waibi_amount, i, "curr_amount_sum");
				// 单价(人民币)
				UFDouble rmb_price = currenty_amount.multiply(currenty_rate, 2);
				bmodel.setValueAt(rmb_price, i, "djprice");
				// 总额(人民币)
				UFDouble rmb_amount = rmb_price.multiply(nnumber, 2);
				bmodel.setValueAt(rmb_amount, i, "djetotal");
			}
		}
		if("nnumber".equals(e.getKey()) || "currenty_amount".equals(e.getKey())){
			int row = e.getRow();
			BillCardPanel card = this.getBillCardPanel();
			// 汇率
			UFDouble currenty_rate = new UFDouble(card.getHeadItem("currenty_rate").getValueObject()==null?"0.00":card.getHeadItem("currenty_rate").getValueObject().toString());
			BillModel bmodel = this.getBillCardPanel().getBillModel();
			// 数量
			UFDouble nnumber = new UFDouble(bmodel.getValueAt(row, "nnumber")==null?"0.00":bmodel.getValueAt(row, "nnumber").toString());
			// 单价(外币)
			UFDouble currenty_amount = new UFDouble(bmodel.getValueAt(row, "currenty_amount")==null?"0.00":bmodel.getValueAt(row, "currenty_amount").toString());
			// 总额(外币)
			UFDouble waibi_amount = currenty_amount.multiply(nnumber, 2);
			bmodel.setValueAt(waibi_amount, row, "curr_amount_sum");
			// 单价(人民币)
			UFDouble rmb_price = currenty_amount.multiply(currenty_rate, 2);
			bmodel.setValueAt(rmb_price, row, "djprice");
			// 总额
			UFDouble rmb_amount = rmb_price.multiply(nnumber, 2);
			bmodel.setValueAt(rmb_amount, row, "djetotal");
			
		}
		if("pk_fzr".equals(e.getKey())){
			BillCardPanel card = this.getBillCardPanel();
			UIRefPane pk_httype = (UIRefPane) card.getHeadItem("pk_cttype").getComponent();
			if(pk_httype.getRefCode()==null){
				MessageDialog.showHintDlg(card, "提示", "请先维护合同类型！");
				return ;
			}
			if(!pk_httype.getRefCode().equals("002")){
				return ;
			}
			UIRefPane pk_fzr = (UIRefPane) this.getBillCardPanel()
			.getHeadItem("pk_fzr").getComponent();
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			
			String sql="select t.cuserid from sm_user t where t.user_name='"+pk_fzr.getRefName()+"' ";
			Object obj=null;
			try {
				obj=iQ.executeQuery(sql, new ColumnProcessor());
			} catch (BusinessException e1) {
			}
			if(obj!=null){
				card.setHeadItem("pk_ysid", obj.toString());
			}
			card.setHeadItem("pk_xmjl", pk_fzr.getRefPK());
			
		}
		
		//2017-11-17 by tcl
		if("httype".equals(e.getKey())&&!_getCorp().getPk_corp().equals("1002")){
			
			Object obj=getBillCardPanel().getHeadItem("httype").getValueObject();
			Integer it=(obj==null||"".equals(obj))?2:Integer.valueOf(obj.toString());
			Object dept=getBillCardPanel().getHeadItem("ht_dept").getValueObject();
			if(it==1){
				String sql="select a.pk_deptdoc from dh_fkgx a,dh_fkgx_d b,bd_deptdoc c where a.pk_fkgx=b.pk_fkgx " +
						"and a.pk_deptdoc=c.pk_deptdoc and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and " +
						"b.pk_dept_user='"+this._getOperator()+"' and a.pk_corp='"+_getCorp().getPk_corp()+"' " +
						"and c.deptname='工程管理部' and c.canceled='N'";
				IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Object pk_dept=null;
				try {
					pk_dept=iQ.executeQuery(sql, new ColumnProcessor());
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				if(pk_dept==null||"".equals(pk_dept)){
					MessageDialog.showHintDlg(this, "提示", "采购合同需工程部制单,请重新选择合同属性！");
					getBillCardPanel().setHeadItem("pk_deptdoc", null);
				}else{
					getBillCardPanel().setHeadItem("pk_deptdoc", pk_dept);
				}
			}else{
				getBillCardPanel().setHeadItem("pk_deptdoc", dept);
			}
			
		}
		
	}

	private IJobtypePrivate getJobtypePrivate() {
		if (pfserver == null) {
			pfserver = (IJobtypePrivate) NCLocator.getInstance().lookup(
					IJobtypePrivate.class.getName());
			return pfserver;
		}
		return pfserver;
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	protected void initPrivateButton() {
		ImportHttkBtnVO BtnVO = new ImportHttkBtnVO();
		addPrivateButton(BtnVO.getButtonVO());
		
		RetCommitBtnVO BtnVO2 = new RetCommitBtnVO();
		addPrivateButton(BtnVO2.getButtonVO());
		
		SealBtnVO BtnVO3 = new SealBtnVO();
		addPrivateButton(BtnVO3.getButtonVO());
		
		CanAuditDeptBtnVO BtnVO4 = new CanAuditDeptBtnVO();
		addPrivateButton(BtnVO4.getButtonVO());
		
		CanAuditCorpBtnVO BtnVO5 = new CanAuditCorpBtnVO();
		addPrivateButton(BtnVO5.getButtonVO());
		
		FileUpLoadBtnVO BtnVO7 = new FileUpLoadBtnVO();
		ButtonVO btn7=BtnVO7.getButtonVO();
		btn7.setBtnName("合同文本");
		addPrivateButton(btn7);
		
		ExceptHTBtnVO btn=new ExceptHTBtnVO();
		addPrivateButton(btn.getButtonVO());
		
		ExceptFHSKBtnVO btn2=new ExceptFHSKBtnVO();
		addPrivateButton(btn2.getButtonVO());
		
		ButtonVO impBtn = ButtonVOFactory.getInstance().build(IBillButton.ImportBill);
		impBtn.setBtnName("合同引入");
		addPrivateButton(impBtn);
	}
	
	
	
}
