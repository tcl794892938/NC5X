package nc.vo.dahuan.retrecord;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class RetRecordVO extends SuperVO {

	private String pk_ret;
	private String ret_user;
	private UFDate ret_date;
	private String ret_vemo;
	private Integer ret_type;
	private String pk_contract;
	private String pk_fkjhbill;
	private String ret_address;
	private Integer dr=0;
	private UFDateTime ts;
	
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_contract() {
		return pk_contract;
	}

	public void setPk_contract(String pk_contract) {
		this.pk_contract = pk_contract;
	}

	public String getPk_fkjhbill() {
		return pk_fkjhbill;
	}

	public void setPk_fkjhbill(String pk_fkjhbill) {
		this.pk_fkjhbill = pk_fkjhbill;
	}

	public String getPk_ret() {
		return pk_ret;
	}

	public void setPk_ret(String pk_ret) {
		this.pk_ret = pk_ret;
	}

	public String getRet_address() {
		return ret_address;
	}

	public void setRet_address(String ret_address) {
		this.ret_address = ret_address;
	}

	public UFDate getRet_date() {
		return ret_date;
	}

	public void setRet_date(UFDate ret_date) {
		this.ret_date = ret_date;
	}

	public Integer getRet_type() {
		return ret_type;
	}

	public void setRet_type(Integer ret_type) {
		this.ret_type = ret_type;
	}

	public String getRet_user() {
		return ret_user;
	}

	public void setRet_user(String ret_user) {
		this.ret_user = ret_user;
	}

	public String getRet_vemo() {
		return ret_vemo;
	}

	public void setRet_vemo(String ret_vemo) {
		this.ret_vemo = ret_vemo;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public RetRecordVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return "pk_ret";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "dh_retrecord";
	}

}
