package nc.ui.dahuan.htupd;

import nc.ui.bd.ref.AbstractRefModel;

public class RefContractModel extends AbstractRefModel {

	public RefContractModel() {

		setRefNodeName("合同");
		setRefTitle("合同");
		setFieldCode(new String[] {"v.ctcode","(select c.invname from bd_invbasdoc c where c.pk_invbasdoc = v.ctname)"});
		setFieldName(new String[] {"合同编号","合同名称"});
		setHiddenFieldCode(new String[] { "v.pk_contract" });
		setPkFieldCode("v.pk_contract");

		setOrderPart(" v.ctcode ");
		
		setTableName("dh_contract v ");
		
		setWherePart(" nvl(v.dr,0) = 0 and nvl(v.is_seal,0) = 1 ");
		
		setRefCodeField("v.ctcode");
		setRefNameField("v.ctcode");
		setCacheEnabled(false);
	
	}
	
}
