package nc.vo.dahuan.contractmodify;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ConModfiyVO extends SuperVO {

	public String pk_contractmodify;
	public Integer modify_type;
	public Integer modify_status;
	public String parent_contractid;
	public String child_contractid;
	public String contractname;
	public String projectname;
	public String parent_custname;
	public String child_custname;
	public UFDouble rate;
	public UFDouble fc_summny;
	public UFDouble old_fc_summny;
	public UFDouble summny;
	public UFDouble old_summny;
	public String managerid;
	public String old_managerid;
	public String contractorid;
	public String old_contractorid;
	public UFDouble budget;
	public UFDouble old_budget;
	public String parent_corpid;
	public String child_corpid;
	public String zdr;
	public UFDate zddate;
	public String shr;
	public UFDate shdate;
	public String zzr;
	public UFDate zzdate;
	public String refusalvemo;
	public Integer dr=0;
	public UFDateTime ts;
	
	public UFDouble getBudget() {
		return budget;
	}

	public void setBudget(UFDouble budget) {
		this.budget = budget;
	}

	public String getChild_contractid() {
		return child_contractid;
	}

	public void setChild_contractid(String child_contractid) {
		this.child_contractid = child_contractid;
	}

	public String getChild_corpid() {
		return child_corpid;
	}

	public void setChild_corpid(String child_corpid) {
		this.child_corpid = child_corpid;
	}

	public String getChild_custname() {
		return child_custname;
	}

	public void setChild_custname(String child_custname) {
		this.child_custname = child_custname;
	}

	public String getContractname() {
		return contractname;
	}

	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	public String getContractorid() {
		return contractorid;
	}

	public void setContractorid(String contractorid) {
		this.contractorid = contractorid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getFc_summny() {
		return fc_summny;
	}

	public void setFc_summny(UFDouble fc_summny) {
		this.fc_summny = fc_summny;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public Integer getModify_status() {
		return modify_status;
	}

	public void setModify_status(Integer modify_status) {
		this.modify_status = modify_status;
	}

	public Integer getModify_type() {
		return modify_type;
	}

	public void setModify_type(Integer modify_type) {
		this.modify_type = modify_type;
	}

	public UFDouble getOld_budget() {
		return old_budget;
	}

	public void setOld_budget(UFDouble old_budget) {
		this.old_budget = old_budget;
	}

	public String getOld_contractorid() {
		return old_contractorid;
	}

	public void setOld_contractorid(String old_contractorid) {
		this.old_contractorid = old_contractorid;
	}

	public UFDouble getOld_fc_summny() {
		return old_fc_summny;
	}

	public void setOld_fc_summny(UFDouble old_fc_summny) {
		this.old_fc_summny = old_fc_summny;
	}

	public String getOld_managerid() {
		return old_managerid;
	}

	public void setOld_managerid(String old_managerid) {
		this.old_managerid = old_managerid;
	}

	public UFDouble getOld_summny() {
		return old_summny;
	}

	public void setOld_summny(UFDouble old_summny) {
		this.old_summny = old_summny;
	}

	public String getParent_contractid() {
		return parent_contractid;
	}

	public void setParent_contractid(String parent_contractid) {
		this.parent_contractid = parent_contractid;
	}

	public String getParent_corpid() {
		return parent_corpid;
	}

	public void setParent_corpid(String parent_corpid) {
		this.parent_corpid = parent_corpid;
	}

	public String getParent_custname() {
		return parent_custname;
	}

	public void setParent_custname(String parent_custname) {
		this.parent_custname = parent_custname;
	}

	public String getPk_contractmodify() {
		return pk_contractmodify;
	}

	public void setPk_contractmodify(String pk_contractmodify) {
		this.pk_contractmodify = pk_contractmodify;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public UFDouble getRate() {
		return rate;
	}

	public void setRate(UFDouble rate) {
		this.rate = rate;
	}

	public String getRefusalvemo() {
		return refusalvemo;
	}

	public void setRefusalvemo(String refusalvemo) {
		this.refusalvemo = refusalvemo;
	}

	public UFDate getShdate() {
		return shdate;
	}

	public void setShdate(UFDate shdate) {
		this.shdate = shdate;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public UFDouble getSummny() {
		return summny;
	}

	public void setSummny(UFDouble summny) {
		this.summny = summny;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public UFDate getZddate() {
		return zddate;
	}

	public void setZddate(UFDate zddate) {
		this.zddate = zddate;
	}

	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}

	public UFDate getZzdate() {
		return zzdate;
	}

	public void setZzdate(UFDate zzdate) {
		this.zzdate = zzdate;
	}

	public String getZzr() {
		return zzr;
	}

	public void setZzr(String zzr) {
		this.zzr = zzr;
	}

	public ConModfiyVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return "pk_contractmodify";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "dh_contractmodify";
	}

}
