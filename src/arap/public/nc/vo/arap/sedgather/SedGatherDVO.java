package nc.vo.arap.sedgather;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class SedGatherDVO extends SuperVO {

	private String pk_sedagther;
	private String pk_sedagther_d;
	private UFDateTime ts;
	private Integer dr=0;
	
	private String pk_saleout;
	private String saleout_no;
	private UFDate saleout_date;
	private UFDouble saleout_nums;
	private UFDouble saleout_amount;
	
	private String pk_gathering;
	private String gather_no;
	private UFDate gather_date;
	private UFDouble gather_amount;
	
	
	
	public UFDate getGather_date() {
		return gather_date;
	}

	public void setGather_date(UFDate gather_date) {
		this.gather_date = gather_date;
	}

	public String getGather_no() {
		return gather_no;
	}

	public void setGather_no(String gather_no) {
		this.gather_no = gather_no;
	}

	public UFDate getSaleout_date() {
		return saleout_date;
	}

	public void setSaleout_date(UFDate saleout_date) {
		this.saleout_date = saleout_date;
	}

	public String getSaleout_no() {
		return saleout_no;
	}

	public void setSaleout_no(String saleout_no) {
		this.saleout_no = saleout_no;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getGather_amount() {
		return gather_amount;
	}

	public void setGather_amount(UFDouble gather_amount) {
		this.gather_amount = gather_amount;
	}

	public String getPk_gathering() {
		return pk_gathering;
	}

	public void setPk_gathering(String pk_gathering) {
		this.pk_gathering = pk_gathering;
	}

	public String getPk_saleout() {
		return pk_saleout;
	}

	public void setPk_saleout(String pk_saleout) {
		this.pk_saleout = pk_saleout;
	}

	public String getPk_sedagther() {
		return pk_sedagther;
	}

	public void setPk_sedagther(String pk_sedagther) {
		this.pk_sedagther = pk_sedagther;
	}

	public String getPk_sedagther_d() {
		return pk_sedagther_d;
	}

	public void setPk_sedagther_d(String pk_sedagther_d) {
		this.pk_sedagther_d = pk_sedagther_d;
	}

	public UFDouble getSaleout_amount() {
		return saleout_amount;
	}

	public void setSaleout_amount(UFDouble saleout_amount) {
		this.saleout_amount = saleout_amount;
	}

	public UFDouble getSaleout_nums() {
		return saleout_nums;
	}

	public void setSaleout_nums(UFDouble saleout_nums) {
		this.saleout_nums = saleout_nums;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_sedagther_d";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_sedagther";
	}

	@Override
	public String getTableName() {
		return "bx_sedgather_d";
	}

}
