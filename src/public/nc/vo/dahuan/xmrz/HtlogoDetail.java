package nc.vo.dahuan.xmrz;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class HtlogoDetail extends SuperVO{
	
	private String pk_id;
	private String pk_contract;
	private String pk_lookman;
	private UFDateTime ts;
	private Integer dr;

	@Override
	public String getPKFieldName() {
		return "pk_id";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "dh_htlogo_detail";
	}

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

	public String getPk_id() {
		return pk_id;
	}

	public void setPk_id(String pk_id) {
		this.pk_id = pk_id;
	}

	public String getPk_lookman() {
		return pk_lookman;
	}

	public void setPk_lookman(String pk_lookman) {
		this.pk_lookman = pk_lookman;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

}
