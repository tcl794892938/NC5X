package nc.ui.st.pz;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends AbstractClientUI {
	
	public ClientUI(){
		
	}

	public Integer ioperate = 0;
	

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}
	
	
	
	
	
	@Override
	public String getNodeKey() {
		//return "H1H305";
		return super.getNodeKey();
	}

	

	protected void initSelfData() {
		
	}

	public void setDefaultData() throws Exception {
		
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if(e.getKey().equals("code")){
			Object obj1=getBillCardPanel().getHeadItem("styear").getValueObject();
			Object obj2=getBillCardPanel().getHeadItem("stmonth").getValueObject();
			String sqla="select count(*) from pzcx a left join rq b on a.pk_rq=b.pk_rq  where b.styear='"+obj1.toString()+"' and b.stmonth='"+obj2.toString()+"'" +
					" and a.code='"+e.getValue()+"'";
			try {
				Object obj3=iQ.executeQuery(sqla, new ColumnProcessor());
				int a=obj3==null?0:Integer.parseInt(obj3.toString());
				if(a>0){
					MessageDialog.showHintDlg(getBillCardPanel(), "提示", "编码不符合唯一性！");
					return;
				}
				
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			
			
			
			String sql="select user_name from sm_user where nvl(dr,0)=0 and user_code='"+e.getValue()+"'";
			try {
				Object obj=iQ.executeQuery(sql, new ColumnProcessor());
				getBillCardPanel().getBillModel().setValueAt(obj, e.getRow(), "name");
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getKey().equals("qj1")|| e.getKey().equals("qj2")|| e.getKey().equals("qj3")){
			
		}
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		return super.beforeEdit(e);
	}

	
	
	
}
