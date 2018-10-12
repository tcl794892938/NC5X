package nc.ui.dahuan.vhtfkmeg;

import java.util.List;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

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
  public abstract class AbstractClientUI extends nc.ui.trade.list.BillListUI implements  ILinkQuery{

	private List<IUserDefButtonCommand> bos = null;
	 
	protected IListController createController() {
		return new ClientUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	/**
	  * ע���Զ��尴ť
	  */
	 protected void initPrivateButton() {
		 
	 }
	 
	 public List<IUserDefButtonCommand> getUserButtons() {
		  if(bos == null)
			  bos = creatUserButtons();
		  return bos;
	 }
	 
	 protected abstract List<IUserDefButtonCommand> creatUserButtons();
	
		
		
	
	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return null;
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
    	
	protected void saveOnClosing() throws Exception {

		ButtonObject bo = getButtonManager().getButton(IBillButton.Save);
		getListEventHandler().onButton(bo);
		
	}
}
