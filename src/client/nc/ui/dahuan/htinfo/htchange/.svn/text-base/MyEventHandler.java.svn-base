package nc.ui.dahuan.htinfo.htchange;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;


public class MyEventHandler extends AbstractMyEventHandler{
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	String condition = "";
	
	@Override 
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == IdhButton.FILEUPLOAD) {
			HtChangeEntity cvo = (HtChangeEntity)this.getBufferData().getCurrentVO().getParentVO();
			DocumentManagerHT.showDM(this.getBillUI(), "DHHT", cvo.getHtcode());
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		
		// 非空校验
		this.getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();

		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			
			BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
			
			Object conchangobj = card.getHeadItem("pk_conchange").getValueObject();
			
			String pkconchange = "";
			HtChangeEntity hcentity = (HtChangeEntity)billVO.getParentVO();
			HtChangeDtlEntity[] cdtl = (HtChangeDtlEntity[])billVO.getChildrenVO();
			
			// 统计金额
			UFDouble amountsum = new UFDouble("0.00");
			for(HtChangeDtlEntity dde : cdtl){
				amountsum = amountsum.add(dde.getPdu_amount());
			}
			
			String chsql = "select sum(nvl(dfkje,0)) dfkje from dh_fkjhbill where nvl(is_pay,0)=1 and ctcode = '"+hcentity.getHtcode()+"'";
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Map<String,Object>> mplit = (List<Map<String,Object>>)iQ.executeQuery(chsql, new MapListProcessor());
			if(null != mplit && mplit.size() > 0 ){
				
				UFDouble dfkje = new UFDouble(mplit.get(0).get("dfkje") == null ? "0.00":mplit.get(0).get("dfkje").toString());
				if(amountsum.compareTo(dfkje) < 0){
					MessageDialog.showHintDlg(this.getBillUI(), "提示", "合同变更后的金额小于已付款金额");
					return;
				}
			}
			
			hcentity.setHtamount(amountsum);
			if(null == conchangobj || "".equals(conchangobj)){
				hcentity.setAttributeValue("pk_conchange", null);
				pkconchange = HYPubBO_Client.insert(hcentity);
			}else{
				HYPubBO_Client.update(hcentity);
				pkconchange = conchangobj.toString();
			}
			
			hcentity.setPk_conchange(pkconchange);
			
			HYPubBO_Client.deleteByWhereClause(HtChangeDtlEntity.class, " pk_conchange = '"+pkconchange+"' ");
			
			
			HtChangeDtlEntity[] cdtlnew = new HtChangeDtlEntity[cdtl.length];
			for(int i=0;i<cdtl.length;i++){
				HtChangeDtlEntity ced = cdtl[i];
				ced.setPk_conchange(pkconchange);
				HYPubBO_Client.insert(ced);
				cdtlnew[i] = ced;
			}
			
			billVO.setParentVO(hcentity);
			billVO.setChildrenVO(cdtlnew);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// 设置保存后状态
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	
	}

	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		condition = strWhere.toString() + " and dept_manager = '"+this._getOperator()+"'";
		
		clearListVO();
	}

	private void clearListVO() throws Exception{
		SuperVO[] queryVos = queryHeadVOs(condition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		// 画面值
		HtChangeEntity htcentityui = (HtChangeEntity)this.getBufferData().getCurrentVO().getParentVO();
		String pkhtc = htcentityui.getPk_conchange();
		Integer uistatus = htcentityui.getHtstatus();
		// 数据库值
		HtChangeEntity htcentitydb = (HtChangeEntity)HYPubBO_Client.queryByPrimaryKey(HtChangeEntity.class, pkhtc);
		Integer dbstatus = htcentitydb.getHtstatus();
		
		if(uistatus.intValue() != dbstatus.intValue()){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "单据状态发生变化，请刷新后操作");
			return;
		}
		
		if(dbstatus.intValue() != 0){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有未提交的变更单才可删除");
			return;
		}
		
		HYPubBO_Client.deleteByWhereClause(HtChangeDtlEntity.class, " pk_conchange = '"+pkhtc+"' ");
		HYPubBO_Client.deleteByWhereClause(HtChangeEntity.class, " pk_conchange = '"+pkhtc+"' ");
		
		clearListVO();
	}

	@Override
	public void onBoAudit() throws Exception {
		// 提交
		HtChangeEntity htcentityui = (HtChangeEntity)this.getBufferData().getCurrentVO().getParentVO();
		htcentityui.setHtstatus(1);
		HYPubBO_Client.update(htcentityui);
		super.onBoRefresh();
	}

	@Override
	protected void onBoCancelAudit() throws Exception {
		// 驳回
		// 画面值
		HtChangeEntity htcentityui = (HtChangeEntity)this.getBufferData().getCurrentVO().getParentVO();
		String pkhtc = htcentityui.getPk_conchange();
		Integer uistatus = htcentityui.getHtstatus();
		// 数据库值
		HtChangeEntity htcentitydb = (HtChangeEntity)HYPubBO_Client.queryByPrimaryKey(HtChangeEntity.class, pkhtc);
		Integer dbstatus = htcentitydb.getHtstatus();
		if(uistatus.intValue() != dbstatus.intValue()){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "单据状态发生变化，请刷新后操作");
			return;
		}
		if(dbstatus.intValue() != 1){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有部门提交的变更单才可驳回");
			return;
		}
		htcentitydb.setHtstatus(0);
		HYPubBO_Client.update(htcentitydb);
		super.onBoRefresh();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		UIRefPane pduUif = (UIRefPane)card.getBodyItem("pk_pdu").getComponent();
		pduUif.getRefModel().addWherePart("  and bd_invbasdoc.pk_invcl <> '0001AA10000000000010'  ", true);
	}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		UIRefPane pduUif = (UIRefPane)card.getBodyItem("pk_pdu").getComponent();
		pduUif.getRefModel().addWherePart("  and bd_invbasdoc.pk_invcl <> '0001AA10000000000010'  ", true);
	}

	
	
}
	
	
