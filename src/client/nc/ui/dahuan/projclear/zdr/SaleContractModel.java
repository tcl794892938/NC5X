package nc.ui.dahuan.projclear.zdr;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class SaleContractModel extends AbstractRefModel {

	public SaleContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode"});
		setFieldName(new String[] {"合同编号"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("dh_contract v ");
		
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String pkUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		
		setWherePart(" v.httype = 0 and v.pk_corp = '"+pkCorp+"' and nvl(v.dr,0)=0 " +
				" and v.jobcode>'09' and v.ctcode not like'%宝%' and v.ctcode not like'%备%' " +
				"and exists (select 1 " +
				"from v_deptperonal h " +
				"where h.pk_corp = '"+pkCorp+"'" +
				" and h.pk_user = '"+pkUser+"'" +
				" and h.pk_deptdoc = v.ht_dept) " +
				
				" and v.dctjetotal>v.ljfkjhje" +
				//">(select nvl(sum(f.hxamount),0) from " + 
			      //"dh_projectclear f where f.salecontractid=v.pk_contract and nvl(f.dr,0)=0) " + 
				
				" and exists (select 1 from v_dh_tcl1 bb where bb.pk_contract=v.pk_contract " +
				"and bb.localcreditamount<>nvl(v.dctjetotal, 0)) ");
		getWherePart();
		setRefCodeField("v.ctcode");
		setRefNameField("v.ctcode");
		setCacheEnabled(false);
	
	}
	
}
