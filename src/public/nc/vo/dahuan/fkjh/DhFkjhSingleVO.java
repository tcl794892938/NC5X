package nc.vo.dahuan.fkjh;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class DhFkjhSingleVO extends SuperVO {

	private String pk_fkjhsingle;
	private String pk_fkjh;
	private UFDouble single_amount;
	private Integer dr=0;
	private UFDateTime ts;
	
	
	
	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_fkjh() {
		return pk_fkjh;
	}

	public void setPk_fkjh(String pk_fkjh) {
		this.pk_fkjh = pk_fkjh;
	}

	public String getPk_fkjhsingle() {
		return pk_fkjhsingle;
	}

	public void setPk_fkjhsingle(String pk_fkjhsingle) {
		this.pk_fkjhsingle = pk_fkjhsingle;
	}

	public UFDouble getSingle_amount() {
		return single_amount;
	}

	public void setSingle_amount(UFDouble single_amount) {
		this.single_amount = single_amount;
	}

	public DhFkjhSingleVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return "pk_fkjhsingle";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "dh_fkjhsingle";
	}

}
