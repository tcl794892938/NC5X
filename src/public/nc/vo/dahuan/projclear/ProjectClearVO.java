package nc.vo.dahuan.projclear;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ProjectClearVO extends SuperVO {

	public String pk_projectclear;
	public Integer pc_status = 0;
	public Integer pc_type;
	public String zdr;
	public UFDate zddate;
	public String zdvemo;
	public String gcr;
	public UFDate gcdate;
	public String gcstatus;
	public String gcvemo;
	public String fzr;
	public UFDate fzdate;
	public String fzstatus;
	public String fzvemo;
	public String fzr2;
	public UFDate fzdate2;
	public String fzstatus2;
	public String fzvemo2;
	public String cwr;
	public UFDate cwdate;
	public String cwvemo;
	public String salecontractid;
	public String salectcode;
	public String salectname;
	public String customerid;
	public String projectname;
	public UFDouble saleamount;
	public UFDouble retamount;
	public UFDouble overamount;
	public UFDouble lossamount;
	public UFDouble hxamount;
	public Integer dr = 0;
	public UFDateTime ts;
	
	
	public UFDouble getHxamount() {
		return hxamount;
	}

	public void setHxamount(UFDouble hxamount) {
		this.hxamount = hxamount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public UFDate getCwdate() {
		return cwdate;
	}

	public void setCwdate(UFDate cwdate) {
		this.cwdate = cwdate;
	}

	public String getCwr() {
		return cwr;
	}

	public void setCwr(String cwr) {
		this.cwr = cwr;
	}

	public String getCwvemo() {
		return cwvemo;
	}

	public void setCwvemo(String cwvemo) {
		this.cwvemo = cwvemo;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDate getFzdate() {
		return fzdate;
	}

	public void setFzdate(UFDate fzdate) {
		this.fzdate = fzdate;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getFzvemo() {
		return fzvemo;
	}

	public void setFzvemo(String fzvemo) {
		this.fzvemo = fzvemo;
	}

	public UFDate getGcdate() {
		return gcdate;
	}

	public void setGcdate(UFDate gcdate) {
		this.gcdate = gcdate;
	}

	public String getGcr() {
		return gcr;
	}

	public void setGcr(String gcr) {
		this.gcr = gcr;
	}

	public String getGcvemo() {
		return gcvemo;
	}

	public void setGcvemo(String gcvemo) {
		this.gcvemo = gcvemo;
	}

	public UFDouble getLossamount() {
		return lossamount;
	}

	public void setLossamount(UFDouble lossamount) {
		this.lossamount = lossamount;
	}

	public UFDouble getOveramount() {
		return overamount;
	}

	public void setOveramount(UFDouble overamount) {
		this.overamount = overamount;
	}

	public Integer getPc_status() {
		return pc_status;
	}

	public void setPc_status(Integer pc_status) {
		this.pc_status = pc_status;
	}

	public Integer getPc_type() {
		return pc_type;
	}

	public void setPc_type(Integer pc_type) {
		this.pc_type = pc_type;
	}

	public String getPk_projectclear() {
		return pk_projectclear;
	}

	public void setPk_projectclear(String pk_projectclear) {
		this.pk_projectclear = pk_projectclear;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public UFDouble getRetamount() {
		return retamount;
	}

	public void setRetamount(UFDouble retamount) {
		this.retamount = retamount;
	}

	public UFDouble getSaleamount() {
		return saleamount;
	}

	public void setSaleamount(UFDouble saleamount) {
		this.saleamount = saleamount;
	}

	public String getSalecontractid() {
		return salecontractid;
	}

	public void setSalecontractid(String salecontractid) {
		this.salecontractid = salecontractid;
	}

	public String getSalectcode() {
		return salectcode;
	}

	public void setSalectcode(String salectcode) {
		this.salectcode = salectcode;
	}

	public String getSalectname() {
		return salectname;
	}

	public void setSalectname(String salectname) {
		this.salectname = salectname;
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

	public String getZdvemo() {
		return zdvemo;
	}

	public void setZdvemo(String zdvemo) {
		this.zdvemo = zdvemo;
	}

	@Override
	public String getPKFieldName() {
		return "pk_projectclear";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "dh_projectclear";
	}

	public UFDate getFzdate2() {
		return fzdate2;
	}

	public void setFzdate2(UFDate fzdate2) {
		this.fzdate2 = fzdate2;
	}

	public String getFzr2() {
		return fzr2;
	}

	public void setFzr2(String fzr2) {
		this.fzr2 = fzr2;
	}

	public String getFzstatus2() {
		return fzstatus2;
	}

	public void setFzstatus2(String fzstatus2) {
		this.fzstatus2 = fzstatus2;
	}

	public String getFzvemo2() {
		return fzvemo2;
	}

	public void setFzvemo2(String fzvemo2) {
		this.fzvemo2 = fzvemo2;
	}

	public void setFzstatus(String fzstatus) {
		this.fzstatus = fzstatus;
	}

	public void setGcstatus(String gcstatus) {
		this.gcstatus = gcstatus;
	}

	public String getFzstatus() {
		return fzstatus;
	}

	public String getGcstatus() {
		return gcstatus;
	}

}
