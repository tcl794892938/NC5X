/*
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
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
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
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
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode " ;
				
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<Object[]> list=(ArrayList<Object[]>)iQ.executeQuery(sql, new ArrayListProcessor());
			if(list==null||list.size()<=0){
				MessageDialog.showHintDlg(getBillUI(), "��ʾ", "δ�����쳣��ͬ����ֹ������");
				return ;
			}
			//����excel
			String str="�쳣��ͬ�����ļ�";
			this.doExport(list,str);
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����ɹ���");
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
				"and nvl(t.dctjetotal,0)<>0 and nvl(t.ljfkjhje,0)=0 and t.jobcode >='09' and nvl(t.dr,0)=0 " + //09��֮��
				"and t.dstartdate<=to_char((select sysdate-365 from dual),'yyyy-mm-dd hh24:mi:ss') order by t.ctcode " ;
			IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList<Object[]> list=(ArrayList<Object[]>)iQ.executeQuery(sql, new ArrayListProcessor());
			if(list==null||list.size()<=0){
				MessageDialog.showHintDlg(getBillUI(), "��ʾ", "δ����δ�ո����ͬ����ֹ������");
				return ;
			}
			//����excel
			String str="�ѷ���δ�ո�����ļ�";
			this.doExport(list,str);
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�����ɹ���");
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
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}
	
	//��ѯ by tcl
	protected void onBoQuery() throws Exception {
		
		super.onBoQuery();
		this.getBillTreeManageUI().getBillListPanel().getHeadBillModel().sortByColumn("jobcode", true);//������Ŀ������������
		//��ĿԤ��
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=model.getRowCount();
		for(int i=0;i<row;i++){
			Object obj=model.getValueAt(i, "httype");
			if(obj!=null&&obj.equals("���ۺ�ͬ")){
				model.setValueAt(null, i, "xm_amount");
			}
		}
	}
	
	
	
	@Override
	protected void onBoRefresh() throws Exception {
		super.onBoRefresh();
		//�޸��߼���ĿԤ��
		BillModel model=this.getBillTreeManageUI().getBillListPanel().getHeadBillModel();
		int row=this.getBillTreeManageUI().getBillListPanel().getHeadTable().getSelectedRow();
		Object obj=model.getValueAt(row, "httype");
		if(row > 0 && obj!=null&&obj.equals("���ۺ�ͬ")){
			model.setValueAt(null, row, "xm_amount");
		}
		
	}

	//daochu
	private void doExport(ArrayList<Object[]> list,String str)throws Exception{
		
		String[] headColsCN = new String[]{"��ͬ���","��Ŀ����","�ͻ�����","��Ӧ������","���۽��","�ɹ����"};
		// �û�ѡ��·��
		String path = this.getChooseFilePath(getBillUI(), str);
		// �жϴ�����ļ����Ƿ�Ϊ��
		if (StringUtils.isEmpty(path)) {
			// ���Ϊ�գ��Ͳ�����ִ����
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ȡ������������ʧ�ܣ�");
			return;

		}
		// �жϴ�����ļ����Ƿ���.xls��β
		if (!path.endsWith(".xls")) {
			// ���������.xls��β���͸��ļ�����������.xls��չ��
			path = path + ".xls";
		}
		
		//����һ�������
		IOUtils util = new IOUtils(path, false, true);
		// ����excel���������
		ExcelTools excelTools = new ExcelTools();
		// ����һ��sheet
		excelTools.createSheet("��������");
		// �жϲ�ѯ���������Ƿ�Ϊ��
		if (CollectionUtils.isEmpty(list)) {
			// ����һ��
			excelTools.createRow(0);
			// ����һ����Ԫ��
			short ct = 0;
			excelTools.createCell(ct);
			// ���Ϊ�գ���ֱ����excel�ļ���д�롰�����ݣ���
			excelTools.setValue("�����ݣ�");

		} else {
			// ����һ��
			excelTools.createRow(0);
			// �ж���ͷ�Ƿ�Ϊ��
			for (short i = 0; i < headColsCN.length; i++) {
				// ����һ����Ԫ��
				excelTools.createCell(i);
				// ��ֵд����Ԫ��
				excelTools.setValue(headColsCN[i]);

			}
			// ����Object����
			Object[] array = null;

			for (int i = 0; i < list.size(); i++) {
				// ����list
				array = (Object[]) list.get(i);

				if (null != array) {
					// ����һ��
					excelTools.createRow(i + 1);

					for (short j = 0; j < array.length; j++) {
						// ����һ����Ԫ��
						excelTools.createCell(j);
						// ��ֵд����Ԫ��
						excelTools.setValue(array[j]);

					}
				}
			}
		}
		// ��excelд��������
		excelTools.writeExcel(util.getOutputStream());
		// �չ���
		util.closeStream();
	}
	
	public String getChooseFilePath(Container parent, String defaultFileName) {
		// �½�һ���ļ�ѡ���
		JFileChooser fileChooser = new JFileChooser();
		// ����Ĭ���ļ���
		fileChooser.setSelectedFile(new File(defaultFileName));
		// �򿪱����
		int retVal = fileChooser.showSaveDialog(parent);
		// ���巵�ر���
		String path = null;
		// �ж��Ƿ��
		if (retVal == JFileChooser.APPROVE_OPTION) {
			// ȷ�ϴ򿪣���ȡѡ���·��
			path = fileChooser.getSelectedFile().getPath();

		}
		// ����·��
		return path;
	}

}