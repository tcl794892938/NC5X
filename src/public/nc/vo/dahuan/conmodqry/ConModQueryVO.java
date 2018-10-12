package nc.vo.dahuan.conmodqry;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ConModQueryVO extends SuperVO {

	public String parent_contractid;
	public Integer modify_type;
	public Integer modify_status;
	public Integer httype;
	public String contractname;
	public String projectname;
	public String parent_custname;
    public UFDouble rate;
    public String parent_corpid;
    public String zdr;
    public UFDate zddate;
    public String zzr;
    public UFDate zzdate;
    public UFDouble old_fc_summny;
    public UFDouble fc_summny;
    public UFDouble old_budget;
    public UFDouble budget;
    public UFDouble old_summny;
    public UFDouble summny;
    public String old_managerid;
    public String managerid;
    public String old_contractorid;
    public String contractorid;
    public String bgxx;
	public String pk_contractmodify;
	
	
    
	public String getPk_contractmodify() {
		return pk_contractmodify;
	}

	public void setPk_contractmodify(String pk_contractmodify) {
		this.pk_contractmodify = pk_contractmodify;
	}

	public Integer getHttype() {
		return httype;
	}

	public void setHttype(Integer httype) {
		this.httype = httype;
	}

	public String getBgxx() {
		return bgxx;
	}

	public void setBgxx(String bgxx) {
		this.bgxx = bgxx;
	}

	public UFDouble getBudget() {
		return budget;
	}

	public void setBudget(UFDouble budget) {
		this.budget = budget;
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
		return "parent_contractid";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "vbg_contract";
	}

}
