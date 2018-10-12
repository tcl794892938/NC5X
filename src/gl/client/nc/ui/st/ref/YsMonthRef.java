package nc.ui.st.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class YsMonthRef extends AbstractRefModel {
	
	public YsMonthRef() {
		super();
	}

	public String[] getFieldCode() {
		return new String[] { "mcode","mname","djmonth"};
	}

	public String[] getFieldName() {
		return new String[] { "�·ݱ���","�·�����" ,"����"};
	}

	public String getPkFieldCode() {
		return "djmonth";
	}

	public String[] getHiddenFieldCode() {
		return new String[]{"djmonth"};
	}

	public String getTableName() {
		return " ys_month";
	}

	public java.lang.String getRefNodeName() {
		return "�·�";
	}

}
