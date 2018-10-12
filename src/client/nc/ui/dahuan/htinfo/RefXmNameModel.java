package nc.ui.dahuan.htinfo;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefXmNameModel extends AbstractRefModel {

	public RefXmNameModel() {

		setRefNodeName("项目名称");
		setRefTitle("项目名称");
		setFieldCode(new String[] {" distinct v.vdef6"});
		setFieldName(new String[] {"项目名称"});
		setHiddenFieldCode(new String[] { "v.vdef6" });
		setPkFieldCode("v.vdef6");

		setOrderPart(" v.vdef6 ");
		
		setTableName("dh_contract v ");
		
		addWherePart(getConfString());
		
		setRefCodeField("v.vdef6");
		setRefNameField("v.vdef6");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return " and v.vapproveid = '"+pkUser+"' and nvl(v.dr,0)=0 ";
	}
	
	
	
	
}
