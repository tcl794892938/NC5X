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
  public abstract class AbstractClientUI extends nc.ui.trade.list.BillListUI implements  ILinkQuery{

	private List<IUserDefButtonCommand> bos = null;
	 
	protected IListController createController() {
		return new ClientUICtrl();
	}
	
	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	/**
	  * 注册自定义按钮
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
	 * 注册前台校验类
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
