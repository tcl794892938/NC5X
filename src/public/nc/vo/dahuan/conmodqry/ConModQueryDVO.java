package nc.vo.dahuan.conmodqry;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ConModQueryDVO extends SuperVO {

	public String child_contractid;
	public String parent_contractid;
    public UFDouble old_fc_summny;
    public UFDouble fc_summny;
    public UFDouble old_summny;
    public UFDouble summny;
    public String old_managerid;
    public String managerid;
    public String old_contractorid;
    public String contractorid;
    public UFDouble old_budget;
    public UFDouble budget;
    public String zdr;
    public UFDate zddate;
    public String zzr;
    public UFDate zzdate;
	
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

	public String getContractorid() {
		return contractorid;
	}

	public void setContractorid(String contractorid) {
		this.contractorid = contractorid;
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

	public UFDouble getSummny() {
		return summny;
	}

	public void setSummny(UFDouble summny) {
		this.summny = summny;
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

	@Override
	public String getPKFieldName() {
		return "child_contractid";
	}

	@Override
	public String getParentPKFieldName() {
		return "parent_contractid";
	}

	@Override
	public String getTableName() {
		return "vbg_contract_b";
	}

}
