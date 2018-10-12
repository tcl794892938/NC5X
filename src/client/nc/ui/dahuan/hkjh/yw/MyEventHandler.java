package nc.ui.dahuan.hkjh.yw;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dahuan.exceltools.ExcelUtils;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.hkjh.HkdhDVO;
import nc.vo.dahuan.hkjh.HkdhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	public void onBillRef() throws Exception {
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		
		HkdhVO hvo = (HkdhVO)aggvo.getParentVO();
		String pkHK = hvo.getPk_hkdh();
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		UFDate ufdate = ClientEnvironment.getServerTime().getDate();
		
		HuiKuanDialog hkd = new HuiKuanDialog(this.getBillUI());
		hkd.showHuiKuanDialog(hvo, pkCorp, pkUser, ufdate);
					
		this.onBoRefresh();
			      
	}

	@Override
	protected void onBoEdit() throws Exception {
		ClientUI cui = (ClientUI)this.getBillUI();
		
		BillModel bodyModel = null;
		UITable bodyTable = null;
		
		if(cui.isListPanelSelected()){
			// 列表画面处理
			bodyModel = cui.getBillListPanel().getBodyBillModel();
			bodyTable = cui.getBillListPanel().getBodyTable();
			
		}else{
			// 卡片画面处理
			bodyModel = cui.getBillCardPanel().getBillModel();
			bodyTable = cui.getBillCardPanel().getBillTable();
			
		}
		
		String message = checkEdit(bodyModel, bodyTable);
		
		if(!"".equals(message)){
			MessageDialog.showHintDlg(cui, "提示", message);
			return;
		}else{
			this.onBoRefresh();
		}
		
		
	}
	
	private String checkEdit(BillModel bodyModel,UITable bodyTable) throws Exception{
		// 选择行
		int[] rows = bodyTable.getSelectedRows();
		if(null == rows || rows.length == 0){
			return "请在表体中选择要维护的行";
		}else{
			HkdhDVO[] hk_dtlvos = new HkdhDVO[rows.length];
			String message = "";
			for(int i=0;i<rows.length;i++){
				int row = rows[i];
				HkdhDVO dvo = (HkdhDVO)bodyModel.getBodyValueRowVO(row, HkdhDVO.class.getName());
				hk_dtlvos[i]=dvo;
				// 当前制单人校验
				if(null == dvo.getVoperid() || !this._getOperator().equals(dvo.getVoperid())){
					int merow = row+1;
					message +=" ["+merow+"] ";
				}
			}
			if(!"".equals(message)){
				return "表体中第"+message+"行制单人与当前登录人不符，不可修改";
			}else{
				
				EditDialog hkd = new EditDialog(this.getBillUI());
				hkd.showEditDialog(hk_dtlvos,this._getDate());
				
				return "";
			}
		}
	}

	@Override
	protected void onBoDelete() throws Exception {
		ClientUI cui = (ClientUI)this.getBillUI();
		
		BillModel bodyModel = null;
		UITable bodyTable = null;
		
		if(cui.isListPanelSelected()){
			// 列表画面处理
			bodyModel = cui.getBillListPanel().getBodyBillModel();
			bodyTable = cui.getBillListPanel().getBodyTable();
			
		}else{
			// 卡片画面处理
			bodyModel = cui.getBillCardPanel().getBillModel();
			bodyTable = cui.getBillCardPanel().getBillTable();
			
		}
		
		String message = checkDelete(bodyModel, bodyTable);
		
		if(!"".equals(message)){
			MessageDialog.showHintDlg(cui, "提示", message);
			return;
		}else{
			this.onBoRefresh();
		}
	}
	
	private String checkDelete(BillModel bodyModel,UITable bodyTable) throws Exception{
		// 选择行
		int[] rows = bodyTable.getSelectedRows();
		if(null == rows || rows.length == 0){
			return "请在表体中选择要维护的行";
		}else{
			HkdhDVO[] hk_dtlvos = new HkdhDVO[rows.length];
			String messageError = "";
			String messageCorrect = "";
			for(int i=0;i<rows.length;i++){
				int row = rows[i];
				HkdhDVO dvo = (HkdhDVO)bodyModel.getBodyValueRowVO(row, HkdhDVO.class.getName());
				hk_dtlvos[i]=dvo;
				// 当前制单人校验
				int merow = row+1;
				if(null == dvo.getVoperid() || !this._getOperator().equals(dvo.getVoperid())){
					messageError +=" ["+merow+"] ";
				}else{
					messageCorrect +=" ["+merow+"] ";
				}
			}
			if(!"".equals(messageError)){
				return "表体中第"+messageError+"行制单人与当前登录人不符，不可删除";
			}else{
				
				int ret = MessageDialog.showOkCancelDlg(this.getBillUI(), "选择", "是否确定删除第"+messageCorrect+"行的数据？");
				if(ret == MessageDialog.ID_OK){
					for(HkdhDVO dvo : hk_dtlvos){
						
						/************************************** 更新合同档案主表  start  *********************************************/
						DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, dvo.getPk_contract());
						UFDouble ljfkjhje = ctvo.getLjfkjhje()==null?new UFDouble("0.00"):ctvo.getLjfkjhje();
						ctvo.setLjfkjhje(ljfkjhje.sub(dvo.getCt_amount(), 2));
						HYPubBO_Client.update(ctvo);
						/************************************** 更新合同档案主表  end   *********************************************/
						
						HYPubBO_Client.delete(dvo);
					}
					
					HkdhDVO[] checkvos = (HkdhDVO[])HYPubBO_Client.queryByCondition(HkdhDVO.class, " pk_hkdh = '"+hk_dtlvos[0].getPk_hkdh()+"' ");
					if(null==checkvos || checkvos.length==0){
						HkdhVO headvo = (HkdhVO)HYPubBO_Client.queryByPrimaryKey(HkdhVO.class, hk_dtlvos[0].getPk_hkdh());
						headvo.setSure_flag(0);
						HYPubBO_Client.update(headvo);
					}
					
				}
				return "";
			}
		}
	}

	@Override
	protected void onBoExport() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		HkdhVO dhvo = (HkdhVO)aggvo.getParentVO();
		
		//回款单位
		String sql = "select b.custname from bd_cubasdoc b where b.pk_cubasdoc =(select m.pk_cubasdoc from bd_cumandoc m where m.pk_cumandoc = '"+dhvo.getPk_cust()+"')";
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String custname = (String)iQ.executeQuery(sql, new ColumnProcessor());
		
		String[] headCN = new String[]{
				"单据号",dhvo.getHkbillno(),
				"回款单位",custname,
				"总额",dhvo.getRmb_amount().toString()
		};
		
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] obj = new Object[7];
		list.add(obj);
		Object[] obj2 = new Object[]{
				"合同号","合同名称","项目名称","已分配金额","分配人","分配部门","分配日期"
		};
		list.add(obj2);
		
		BillModel bmodel = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int rows = bmodel.getRowCount();
		UFDouble hjamt = new UFDouble("0.00");
		
		for(int i=0;i<rows;i++){
			Object[] nobj = new Object[]{
				bmodel.getValueAt(i, "ctcode"),
				bmodel.getValueAt(i, "ctname"),
				bmodel.getValueAt(i, "xmname"),
				bmodel.getValueAt(i, "ct_amount"),
				bmodel.getValueAt(i, "vopername"),
				bmodel.getValueAt(i, "deptname"),
				bmodel.getValueAt(i, "dbilldate"),
			};
			list.add(nobj);
			hjamt = bmodel.getValueAt(i, "ct_amount")==null?hjamt:hjamt.add(new UFDouble(bmodel.getValueAt(i, "ct_amount").toString()),2);
		}
		
		Object[] xjobj = new Object[]{
			"小计",null,null,hjamt,null,null,null
		};
		list.add(xjobj);
		
		ExcelUtils.doExport("回款单", list, headCN, this.getBillUI());
		
		
	}

	@Override
	protected void onBoQuery() throws Exception {

		
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String corpid = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return;
		
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";
		
		
		String sql = " select distinct dh_hkdh.* from dh_hkdh left join dh_hkdh_d on dh_hkdh.pk_hkdh = dh_hkdh_d.pk_hkdh and nvl(dh_hkdh_d.dr, 0) = 0 "+
			"  where nvl(dh_hkdh.dr, 0) = 0 and nvl(dh_hkdh.seal_flag,0)=0 and "+strWhere;
		
		List<HkdhVO> queryVos = (List<HkdhVO>)iQ.executeQuery(sql, new BeanListProcessor(HkdhVO.class));
		
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos.toArray(new HkdhVO[0]));
	
		updateBuffer();

	
	}

	// 封存
	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
			return;
		}
		HkdhVO hvo = (HkdhVO)aggvo.getParentVO();
		int sf = hvo.getSure_flag();
		if(1 == sf){
			hvo.setSeal_flag(1);
			hvo.setSealdate(this._getDate());
			hvo.setSealid(this._getOperator());
			HYPubBO_Client.update(hvo);
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "回款单已封存");
			this.onBoRefresh();
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请先确认单据");
			return;
		}
	}

	
	
}