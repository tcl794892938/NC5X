package nc.ui.dahuan.fkallquery;

import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;



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
		
		getBillListPanel().getHeadTable().setCellSelectionEnabled(false);//ȡ����Ԫ��
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true);//������ѡ��
	}

	public void setDefaultData() throws Exception {}
	
}