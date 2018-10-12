package nc.ui.st.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class YsYearRef extends AbstractRefModel {
	
	public YsYearRef() {
		super();
	}

	public String[] getFieldCode() {
		return new String[] { "ycode","yname","pk_year"};
	}

	public String[] getFieldName() {
		return new String[] { "编码","年度" ,"主键"};
	}

	public String getPkFieldCode() {
		return "pk_year";
	}

	public String[] getHiddenFieldCode() {
		return new String[]{"pk_year"};
	}

	public String getTableName() {
		return " ys_year";
	}

	public java.lang.String getRefNodeName() {
		return "年度";
	}

}
