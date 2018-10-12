/*
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.dahuan.htallquery;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.demo.tree.tree03.DocumentManagerHT;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.query.INormalQuery;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubtools.ExcelTools;
import nc.vo.pubtools.IOUtils;
import nc.vo.scm.constant.ic.BillMode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class MultiChildTreeCardEventHandler extends TreeManageEventHandler {

	IdhServer pfserver = (IdhServer)NCLocator.getInstance().lookup(IdhServer.class);
	IUAPQueryBS iQuery = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	public MultiChildTreeCardEventHandler(BillManageUI billUI,
			ICardController control) {
		super(billUI, control);

	}

	public boolean isAllowAddNode(TableTreeNode node) {

		return super.isAllowAddNode(node);
	}

	public boolean isAllowDelNode(TableTreeNode node) {

		return super.isAllowDelNode(node);
	}

	@Override
	protected void onBoCard() throws Exception {
		((BillManageUI)this.getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		BillCardPanel card = ((BillManageUI)this.getBillUI()).getBillCardPanel();
		BillCardLayout layout = (BillCardLayout)card.getLayout();
		layout.setHeadScale(60);
		layout.layoutContainer(card);
		getBufferData().updateView();
	}
	
	private void refashTreeVO(){
		BillTreeManageUI treeUI = (BillTreeManageUI) getBillTreeManageUI();

		AggregatedValueObject aggvo = this.getBillTreeManageUI()
				.getBufferData().getCurrentVO();
		DhContractVO headvo = (DhContractVO) aggvo.getParentVO();
		treeUI.getBillTreeData().insertNodeToTree(headvo);
		VOTreeNode selectnode = new VOTreeNode("");
		selectnode.setNodeID(headvo.getPk_contract());
		selectnode.setCode(headvo.getCtcode());

		selectnode.setData(aggvo.getParentVO());
		setSelectionPath(headvo);
		this.onTreeSelected(selectnode);
	}

	public void setSelectionPath(DhContractVO headvo) {
		TreePath selectTreePath = null;
		MultiChildTreeCardUI treeUI = (MultiChildTreeCardUI) getBillTreeManageUI();

		DefaultTreeModel treeModel = (DefaultTreeModel) treeUI.getBillTree()
				.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();

		Enumeration en = root.preorderEnumeration();
		while (en.hasMoreElements()) {
			TableTreeNode treeNode = (TableTreeNode) en.nextElement();
			if ("root".equalsIgnoreCase(treeNode.getNodeID().toString().trim()))
				continue;
			VOTreeNode voTreeNode = (VOTreeNode) treeNode;
			CircularlyAccessibleValueObject deptdocVO = voTreeNode.getData();
			if (headvo.getCtcode().equalsIgnoreCase(
					deptdocVO.getAttributeValue("ctcode").toString())) {
				TreeNode[] treeNodes = treeModel.getPathToRoot(voTreeNode);
				selectTreePath = new TreePath(treeNodes);
				break;
			}
		}

		if (selectTreePath != null) {
			treeUI.getBillTree().expandPath(selectTreePath);
			treeUI.getBillTree().setSelectionPath(selectTreePath);
		}
	}

	public void onTreeSelected(VOTreeNode selectnode) {
		super.onTreeSelected(selectnode);
	}

	protected void setAddNewOperate(boolean isAdding,
			AggregatedValueObject billVO) throws Exception {

		super.setAddNewOperate(isAdding, billVO);
	}
	
	
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == IdhButton.FILEUPLOAD) {
			DhContractVO cvo = (DhContractVO)this.getBufferData().getCurrentVO().getParentVO();
			
			String pkCorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			
			if("1001".equals(pkCorp)){
				DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getCtcode());
			}else{
				if(null == cvo.getRelationid() || "".equals(cvo.getRelationid())){
					DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getCtcode());
				}else{
					IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					String sql = "select ctcode from dh_contract where pk_contract = '"+cvo.getRelationid()+"'";
					String relcode = (String)iQ.executeQuery(sql, new ColumnProcessor());
					DocumentManagerHT.showDM(this.getBillUI(), "DHHT", relcode);
				}
			}
		}else if(intBtn == IdhButton.YCHT){
			String pkUser = this._getOperator();
			String pkCorp = this._getCorp().getPrimaryKey();
			
			String sql="select t.* from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=0 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09年之后
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode " ;
				
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DhContractVO> list=(List<DhContractVO>)iQ.executeQuery(sql, new BeanListProcessor(DhContractVO.class));
			
			getBufferData().clear();
			addDataToBuffer(list.toArray(new DhContractVO[0]));
			updateBuffer();
		}else if(intBtn == IdhButton.FHSK){
			String pkUser = this._getOperator();
			String pkCorp = this._getCorp().getPrimaryKey();
			String sql="select t.* from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=1 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09年之后
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode  " ;
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DhContractVO> list=(List<DhContractVO>)iQ.executeQuery(sql, new BeanListProcessor(DhContractVO.class));
			
			getBufferData().clear();
			addDataToBuffer(list.toArray(new DhContractVO[0]));
			updateBuffer();
		}else if(intBtn == IdhButton.YCHTDC){
			String pkCorp = this._getCorp().getPrimaryKey();
			
			String sql="select t.ctcode,t.vdef6, (select s.custname from bd_cubasdoc s where s.pk_cubasdoc in " +
					"(select m.pk_cubasdoc from bd_cumandoc m where m.pk_cumandoc=t.pk_cust1)) custname1, " +
					"(select s.custname from bd_cubasdoc s where s.pk_cubasdoc in(select m.pk_cubasdoc " +
					"from bd_cumandoc m where m.pk_cumandoc=t.pk_cust2)) custname2,t.dsaletotal,t.dcaigtotal from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=0 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09年之后
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode " ;
				
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<Object[]> list=(ArrayList<Object[]>)iQ.executeQuery(sql, new ArrayListProcessor());
			if(list==null||list.size()<=0){
				MessageDialog.showHintDlg(getBillUI(), "提示", "未发现异常合同，终止导出！");
				return ;
			}
			//导出excel
			String str="异常合同导出文件";
			this.doExport(list,str);
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "导出成功！");
		}else if(intBtn == IdhButton.FHSKDC){
			String pkCorp = this._getCorp().getPrimaryKey();
			String sql="select t.ctcode,t.vdef6,(select s.custname from bd_cubasdoc s where s.pk_cubasdoc in" +
					"(select m.pk_cubasdoc from bd_cumandoc m where m.pk_cumandoc=t.pk_cust1)) custname1, " +
					"(select s.custname from bd_cubasdoc s where s.pk_cubasdoc in(select " +
					"m.pk_cubasdoc from bd_cumandoc m where m.pk_cumandoc=t.pk_cust2)) custname2,t.dsaletotal,t.dcaigtotal from dh_contract t " +
				"left join bd_invbasdoc d on t.ctname=d.pk_invbasdoc " +
				"left join bd_jobmngfil s on t.pk_jobmandoc=s.pk_jobmngfil " +
				"where nvl(s.dr,0)=0 and nvl(d.dr,0)=0 and  nvl(s.sealflag,'N')='N' and t.vbillstatus=1 " +
				"and nvl(t.is_delivery,0)=1 and t.httype in(0,1) and t.pk_corp='"+pkCorp+"' " +
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09年之后
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode " ;
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<Object[]> list=(ArrayList<Object[]>)iQ.executeQuery(sql, new ArrayListProcessor());
			if(list==null||list.size()<=0){
				MessageDialog.showHintDlg(getBillUI(), "提示", "未发现未收付款合同，终止导出！");
				return ;
			}
			//导出excel
			String str="已发货未收付款导出文件";
			this.doExport(list,str);
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "导出成功！");
		}
	}
	
	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf) throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0){
			strWhere = "1=1";
		}else{
			strWhere = strWhere.replaceAll("dh_contract.jobcode like '%", "dh_contract.jobcode like '");
		}
		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}
	
	//查询 by tcl
	protected void onBoQuery() throws Exception {
		
		super.onBoQuery();
		this.getBillTreeManageUI().getBillListPanel().getHeadBillModel().sortByColumn("jobcode", true);//按照项目编码升序排序
		//项目预算
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=model.getRowCount();
		for(int i=0;i<row;i++){
			Object obj=model.getValueAt(i, "httype");
			if(obj!=null&&obj.equals("销售合同")){
				model.setValueAt(null, i, "xm_amount");
			}
		}
	}
	
	
	
	@Override
	protected void onBoRefresh() throws Exception {
		super.onBoRefresh();
		//修改逻辑项目预算
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=this.getBillTreeManageUI().getBillListPanel().getHeadTable().getSelectedRow();
		Object obj=model.getValueAt(row, "httype");
		if(row > 0 && obj!=null&&obj.equals("销售合同")){
			model.setValueAt(null, row, "xm_amount");
		}
		
	}

	//daochu
	private void doExport(ArrayList<Object[]> list,String str)throws Exception{
		
		String[] headColsCN = new String[]{"合同编号","项目名称","客户名称","供应商名称","销售金额","采购金额"};
		// 用户选择路径
		String path = this.getChooseFilePath(getBillUI(), str);
		// 判断传入的文件名是否为空
		if (StringUtils.isEmpty(path)) {
			// 如果为空，就不往下执行了
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "已取消操作，导出失败！");
			return;

		}
		// 判断传入的文件名是否以.xls结尾
		if (!path.endsWith(".xls")) {
			// 如果不是以.xls结尾，就给文件名变量加上.xls扩展名
			path = path + ".xls";
		}
		
		//构造一个输出流
		IOUtils util = new IOUtils(path, false, true);
		// 构造excel工具类对象
		ExcelTools excelTools = new ExcelTools();
		// 创建一个sheet
		excelTools.createSheet("导出数据");
		// 判断查询出的数据是否为空
		if (CollectionUtils.isEmpty(list)) {
			// 创建一行
			excelTools.createRow(0);
			// 创建一个单元格
			short ct = 0;
			excelTools.createCell(ct);
			// 如果为空，就直接向excel文件中写入“无数据！”
			excelTools.setValue("无数据！");

		} else {
			// 创建一行
			excelTools.createRow(0);
			// 判断列头是否为空
			for (short i = 0; i < headColsCN.length; i++) {
				// 创建一个单元格
				excelTools.createCell(i);
				// 将值写到单元格
				excelTools.setValue(headColsCN[i]);

			}
			// 定义Object数组
			Object[] array = null;

			for (int i = 0; i < list.size(); i++) {
				// 迭代list
				array = (Object[]) list.get(i);

				if (null != array) {
					// 创建一行
					excelTools.createRow(i + 1);

					for (short j = 0; j < array.length; j++) {
						// 创建一个单元格
						excelTools.createCell(j);
						// 将值写到单元格
						excelTools.setValue(array[j]);

					}
				}
			}
		}
		// 将excel写到磁盘上
		excelTools.writeExcel(util.getOutputStream());
		// 闭关流
		util.closeStream();
	}
	
	public String getChooseFilePath(Container parent, String defaultFileName) {
		// 新建一个文件选择框
		JFileChooser fileChooser = new JFileChooser();
		// 设置默认文件名
		fileChooser.setSelectedFile(new File(defaultFileName));
		// 打开保存框
		int retVal = fileChooser.showSaveDialog(parent);
		// 定义返回变量
		String path = null;
		// 判断是否打开
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// 确认打开，获取选择的路径
			path = fileChooser.getSelectedFile().getPath();

		}
		// 返回路径
		return path;
	}

}