package nc.ui.dahuan.vhtfkmeg;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.dahuan.vhtfkmeg.ViewhtfkVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


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
       
	 
	 public ClientUI()  throws Exception {
		 super();
		 
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<ViewhtfkVO>  vhflit = (List<ViewhtfkVO>)iQ.retrieveByClause(ViewhtfkVO.class, " bill_pkuser = '"+this._getOperator()+"' ");
		 
		this.setListHeadData(vhflit.toArray(new ViewhtfkVO[0]));
	 }

	protected ListEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}
	
		
	 protected List<IUserDefButtonCommand> creatUserButtons(){
		 
		  List<IUserDefButtonCommand> bos = new ArrayList<IUserDefButtonCommand>();
		  return bos;
		}
	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initBillData(String strWhere) throws Exception {
	}

	@Override
	public String getRefBillType() {
		return null;
	}


}
