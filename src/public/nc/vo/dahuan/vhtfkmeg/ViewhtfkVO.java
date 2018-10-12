package nc.vo.dahuan.vhtfkmeg;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class ViewhtfkVO extends SuperVO {

	private String bill_status;
	private String bill_code;
	private String bill_flag;
	private String bill_vemo;
	private String bill_pkuser;
	private String bill_person;
	private UFDateTime bill_date;
	
	public String getBill_code() {
		return bill_code;
	}

	public void setBill_code(String bill_code) {
		this.bill_code = bill_code;
	}

	public UFDateTime getBill_date() {
		return bill_date;
	}

	public void setBill_date(UFDateTime bill_date) {
		this.bill_date = bill_date;
	}

	public String getBill_flag() {
		return bill_flag;
	}

	public void setBill_flag(String bill_flag) {
		this.bill_flag = bill_flag;
	}

	public String getBill_person() {
		return bill_person;
	}

	public void setBill_person(String bill_person) {
		this.bill_person = bill_person;
	}

	public String getBill_pkuser() {
		return bill_pkuser;
	}

	public void setBill_pkuser(String bill_pkuser) {
		this.bill_pkuser = bill_pkuser;
	}

	public String getBill_status() {
		return bill_status;
	}

	public void setBill_status(String bill_status) {
		this.bill_status = bill_status;
	}

	public String getBill_vemo() {
		return bill_vemo;
	}

	public void setBill_vemo(String bill_vemo) {
		this.bill_vemo = bill_vemo;
	}

	public ViewhtfkVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "v_htfkmessage";
	}

}
