package nc.ui.dahuan.fkjhzjl;

import nc.ui.pub.print.IDataSource;

public class FkjhClientUIPRTS implements IDataSource {
	
	private static final long serialVersionUID = 1L;
	private String m_sModuleName= "";
	private String pkFkjh = "";
	
	public FkjhClientUIPRTS(String m_sModuleName, String pkFkjh){
		super();
		this.m_sModuleName = m_sModuleName;
		this.pkFkjh = pkFkjh;
	}

	public String[] getAllDataItemExpress() {
		return new String[]{"h_pk_fkjhbill"};
	}

	public String[] getAllDataItemNames() {
		return new String[]{"Ö÷¼ü"};
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		if("h_pk_fkjhbill".equals(itemExpress)){
			return new String[]{pkFkjh};
		}
		return null;
	}

	public String getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNumber(String itemExpress) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getM_sModuleName() {
		return m_sModuleName;
	}

	public void setM_sModuleName(String moduleName) {
		m_sModuleName = moduleName;
	}

	public String getPkFkjh() {
		return pkFkjh;
	}

	public void setPkFkjh(String pkFkjh) {
		this.pkFkjh = pkFkjh;
	}

}
