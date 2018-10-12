package nc.ui.dahuan.fkjhywdy;

import nc.ui.pub.print.IDataSource;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;

public class FkjhClientUIPRTS implements IDataSource {
	
	private static final long serialVersionUID = 1L;
	private String m_sModuleName= "";
	private DhFkbdprintVO fkvo;
	
	public FkjhClientUIPRTS(String m_sModuleName, DhFkbdprintVO fkvo){
		super();
		this.m_sModuleName = m_sModuleName;
		this.fkvo = fkvo;
	}

	public String[] getAllDataItemExpress() {
		String[] itemexpresss = new String[]{
			"h_print_no","h_print_date","h_say_bank","h_say_corp","h_say_no","h_say_content","h_say_type","h_amount_sum",
			"h_corp_name","h_dept_name","h_dept_saleman","h_dept_master","h_fzname","h_htamount1","h_htamount10","h_htamount2",
			"h_htamount3","h_htamount4","h_htamount5","h_htamount6","h_htamount7","h_htamount8","h_htamount9","h_htcode1",
			"h_htcode10","h_htcode2","h_htcode3","h_htcode4","h_htcode5","h_htcode6","h_htcode7","h_htcode8",
			"h_htcode9","h_pk_fkbd","h_pk_fkprint","h_say_content1","h_say_content2","h_say_content3","h_say_content4","h_say_content5",
			"h_say_type1","h_say_type2","h_say_type3","h_say_type4","h_say_type5","h_cwname"
		};
		return itemexpresss;
	}

	public String[] getAllDataItemNames() {
		String[] itemnames = new String[]{
				"打印编号","打印日期","开户银行","付款单位","银行账号","付款内容","付款形式","付款总额",
				"公司","经办部门",	"经办人",	"部门主任","分管经理","合同金额1","合同金额10","合同金额2",
				"合同金额3","合同金额4","合同金额5","合同金额6","合同金额7","合同金额8","合同金额9","合同号1",
				"合同号10","合同号2","合同号3","合同号4","合同号5","合同号6","合同号7","合同号8",
				"合同号9","主键","打印主键标示","say_content1","say_content2","say_content3","say_content4","say_content5",
				"say_type1","say_type2","say_type3","say_type4","say_type5","cwname"
			};
			return itemnames;
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		
		if(itemExpress.startsWith("h_")){
			
			String value = fkvo.getAttributeValue(itemExpress.substring(2))==null?"":fkvo.getAttributeValue(itemExpress.substring(2)).toString();
			
			return new String[]{value};
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

	public DhFkbdprintVO getFkvo() {
		return fkvo;
	}

	public void setFkvo(DhFkbdprintVO fkvo) {
		this.fkvo = fkvo;
	}

}
