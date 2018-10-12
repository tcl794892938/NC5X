package nc.vo.dahuan.projclear;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ProjectClearDVO extends SuperVO {
	
	public String pk_projectclear_d;
	public String pk_projectclear;
	public String purcontractid;
	public String purctcode;
	public String purctname;
	public String supplierid;
	public UFDouble puramount;
	public UFDouble payamount;
	public UFDouble jyamount;
	public UFDouble doveramount;
	public Integer dr = 0;
	public UFDateTime ts;
	public String purvemo;
	
	
	public UFDouble getJyamount() {
		return jyamount;
	}

	public void setJyamount(UFDouble jyamount) {
		this.jyamount = jyamount;
	}

	public String getPurvemo() {
		return purvemo;
	}

	public void setPurvemo(String purvemo) {
		this.purvemo = purvemo;
	}

	public UFDouble getDoveramount() {
		return doveramount;
	}

	public void setDoveramount(UFDouble doveramount) {
		this.doveramount = doveramount;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getPayamount() {
		return payamount;
	}

	public void setPayamount(UFDouble payamount) {
		this.payamount = payamount;
	}

	public String getPk_projectclear() {
		return pk_projectclear;
	}

	public void setPk_projectclear(String pk_projectclear) {
		this.pk_projectclear = pk_projectclear;
	}

	public String getPk_projectclear_d() {
		return pk_projectclear_d;
	}

	public void setPk_projectclear_d(String pk_projectclear_d) {
		this.pk_projectclear_d = pk_projectclear_d;
	}

	public UFDouble getPuramount() {
		return puramount;
	}

	public void setPuramount(UFDouble puramount) {
		this.puramount = puramount;
	}

	public String getPurcontractid() {
		return purcontractid;
	}

	public void setPurcontractid(String purcontractid) {
		this.purcontractid = purcontractid;
	}

	public String getPurctcode() {
		return purctcode;
	}

	public void setPurctcode(String purctcode) {
		this.purctcode = purctcode;
	}

	public String getPurctname() {
		return purctname;
	}

	public void setPurctname(String purctname) {
		this.purctname = purctname;
	}

	public String getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_projectclear_d";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_projectclear";
	}

	@Override
	public String getTableName() {
		return "dh_projectclear_d";
	}

}
