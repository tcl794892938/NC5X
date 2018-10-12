package nc.ui.dahuan.fkjhdy;

import nc.ui.bfriend.button.LinkBtnVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;



 public class ClientUI extends AbstractClientUI {
       
   
	private static final long serialVersionUID = 1L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
		//获取当前面板的所有列，并设置为多选框，这样就会对所选择的列进行合计
		getBillListPanel().setMultiSelect(true);
		
		
//		getBillListPanel().getParentListPanel().getFixRowTable().setValueAt(aValue, row, column);
		
//		getBillListPanel().getParentListPanel().setTotalRowShow(true);//表头
		
		
		
		// 制单
		ButtonObject edit = (ButtonObject)this.getButtonManager().getButton(IBillButton.Edit);
		((ButtonVO)edit.getData()).setExtendStatus(new int[]{ 4 });
		
		// 删除
		ButtonObject delete = (ButtonObject)this.getButtonManager().getButton(IBillButton.Delete);
		((ButtonVO)delete.getData()).setExtendStatus(new int[]{ 4 });
		
		// 审核
		ButtonObject audit = (ButtonObject)this.getButtonManager().getButton(IBillButton.Audit);
		((ButtonVO)audit.getData()).setExtendStatus(new int[]{ 4 });
		
		// 弃审
		ButtonObject cancel = (ButtonObject)this.getButtonManager().getButton(IBillButton.CancelAudit);
		((ButtonVO)cancel.getData()).setExtendStatus(new int[]{ 3 });
		
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String[] itemkeys = new String[]{
				fileDef.getField_Corp(),
				fileDef.getField_Operator(),
				fileDef.getField_Billtype(),
				fileDef.getField_BillStatus()
				};
		Object[] values = new Object[]{
				pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype,
				new Integer(IBillStatus.FREE).toString()
				};
		
		for(int i = 0; i < itemkeys.length; i++){
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if(item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if(item != null)
				item.setValue(values[i]);
		}
	}



	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if(e.getKey().equalsIgnoreCase("dfkbl")){   
		    String formula = "dfkje->dfkbl*dctjetotal/100";
		    this.getBillCardPanel().execHeadFormula(formula);
		}else if(e.getKey().equalsIgnoreCase("dfkje")){
			String formula = "dfkbl->dfkje*100/dctjetotal";
			this.getBillCardPanel().execHeadFormula(formula);
		}
		
		
	}
	
	 protected void initPrivateButton() {
			super.initPrivateButton();
			LinkBtnVO btn = new LinkBtnVO();
			addPrivateButton(btn.getButtonVO());
		}

	 //按钮控制
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			DhFkjhbillVO fkVO = (DhFkjhbillVO)vo.getParentVO();
			// 制单标识
			String zdflag = fkVO.getVoperatorflag();
			// 审核标识1
			String shflag = fkVO.getShrflag1();
			if("1".equals(zdflag)){
				if("1".equals(shflag)){
					return 2;
				}else{
					return 3;
				}
			}
			return 4;
		}
		return 1;
	}


	
}
