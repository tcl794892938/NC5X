package nc.ui.dahuan.fkgx;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
       
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
		getBillListPanel().setMultiSelect(true);
	}

	public void setDefaultData() throws Exception {
		UIRefPane deptRef = (UIRefPane)this.getBillCardPanel().getHeadItem("pk_deptdoc").getComponent();
		deptRef.getRefModel().addWherePart(" and canceled = 'N' ");//经营部门
		
		
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
		// 表头数据发生变化，表体清空
		if("pk_deptdoc".equals(e.getKey()) || "pk_user1".equals(e.getKey())){
			this.getBillCardPanel().getBillModel().clearBodyData();
		}
		
		// 部门设置发生改变时，画面数据全部清空，并检验该部门维护信息的唯一性
		if("pk_deptdoc".equals(e.getKey())){
			// 数据清理
			this.getBillCardPanel().setHeadItem("pk_user1", null);
		}
		
		// 处理下属主键
		if("deptusername".equals(e.getKey())){
//			 数据校验
			BillCardPanel card = this.getBillCardPanel();
			Object fkgxPK = card.getHeadItem("pk_fkgx").getValueObject();
			DefaultConstEnum ud = (DefaultConstEnum)e.getValue();
			String udPK = ud.getValue().toString();
			int row = e.getRow();
			this.getBillCardPanel().getBillModel().setValueAt(fkgxPK, row, "pk_fkgx");
			this.getBillCardPanel().getBillModel().setValueAt(udPK, row, "pk_dept_user");
		}
	}
	
	


}
