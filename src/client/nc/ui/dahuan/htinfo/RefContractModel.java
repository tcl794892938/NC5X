package nc.ui.dahuan.htinfo;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","s.invname"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName(" dh_contract v left join bd_invbasdoc s on s.pk_invbasdoc = v.ctname ");
		
		addWherePart(getConfString());
		
		
		setRefCodeField("v.ctcode");
		setRefNameField("s.invname");
		setCacheEnabled(false);
	
	}
	
	private String getConfString(){
		
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		return " and v.vapproveid = '"+pkUser+"' and nvl(v.dr,0)=0 ";
	}
	
	
	
	
}
