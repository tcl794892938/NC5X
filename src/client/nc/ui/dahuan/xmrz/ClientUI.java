package nc.ui.dahuan.xmrz;

import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.trade.business.HYPubBO;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
		
		this.getBillListPanel().getHeadTable().setCellSelectionEnabled(false);
		this.getBillListPanel().getHeadTable().setRowSelectionAllowed(true);
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		
		// ��ͬ�������
		if("htcode".equals(e.getKey())){
			BillCardPanel card = this.getBillCardPanel();
			String[] items = new String[]{"htname","pk_contract"};
			String[] values = new String[2];
			String htcode = e.getValue() == null ? "" : e.getValue().toString();
			DhContractVO[] dvo = null;
			try {
				dvo = (DhContractVO[])HYPubBO_Client.queryByCondition(DhContractVO.class, " ctcode = '"+htcode+"'");
			} catch (UifException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(dvo.length == 1){
				
			}else{
				MessageDialog.showHintDlg(this, "��ʾ", "��ͬ������");
				card.setHeadItem("htcode", null);
			}
		}
	}
	
	

	private void setItemValue(String[] items,String[] values,BillCardPanel card){
		for(int i=0 ;i<items.length;i++){
			card.setHeadItem(items[i], null);
			card.setHeadItem(items[i], values[i]);
		}
	}

}
