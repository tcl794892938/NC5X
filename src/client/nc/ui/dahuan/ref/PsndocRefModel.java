package nc.ui.dahuan.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class PsndocRefModel extends AbstractRefModel {

	public PsndocRefModel() {
		setRefNodeName("人员档案");
		setRefTitle("人员档案");
		setFieldCode(new String[] {"psncode","psnname"});
		setFieldName(new String[] {"人员编号","人员姓名"});
		setHiddenFieldCode(new String[] { "pk_psndoc" });
		setPkFieldCode("pk_psndoc");

		setOrderPart(" psncode ");
		
		String pkcorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
		setWherePart(" bd_psndoc.sealdate is null and bd_psndoc.pk_corp = '"+pkcorp+"' " +
				" and bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc " +
				" where bd_deptdoc.deptname = '公共部门' and bd_deptdoc.pk_corp='"+pkcorp+"') ",true);
		
		setTableName("bd_psndoc");
		setRefCodeField("psncode");
		setRefNameField("psnname");
		setCacheEnabled(false);
		
		
	}
}
