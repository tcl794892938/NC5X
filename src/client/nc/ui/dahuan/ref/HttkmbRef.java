package nc.ui.dahuan.ref;

import nc.ui.bd.ref.AbstractRefModel;


public class HttkmbRef extends AbstractRefModel {

	/**
	 * 合同条款模板参照
	 */
	public HttkmbRef() {
		
	}

	
	public String[] getFieldCode() {
		return (new String[] { 
				   "vbillno",
			       "tkmbname"
				  }
		        );
	}

	
	public int getDefaultFieldCount() {

		return 2;
	}

	
	public String[] getFieldName() {
		return (new String[] { "合同条款模板编码","合同条款名" });
	}

	
	public String[] getHiddenFieldCode() {
		return (new String[] { "pk_httk" });
	}


	
	public String getRefNameField() {
		return "tkmbname";
	}

	
	public String getPkFieldCode() {
		return "pk_httk";
	}

	
	public String getRefTitle() {
		return "合同条款模板参照";
	}

	
	public String getTableName() {
		return "dh_httk";
	}

	
	public String getWherePart() {
		String oldPart = super.getWherePart();
		if (oldPart == null || oldPart.trim().length() == 0)
			return " nvl(dh_httk.dr,0)=0 ";
		else
			return " nvl(dh_httk.dr,0)=0  and (" + oldPart + ")";
	}

}
