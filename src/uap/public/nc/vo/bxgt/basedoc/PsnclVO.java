package nc.vo.bxgt.basedoc;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class PsnclVO extends SuperVO {
	
	public String pk_psncl;
	public String psnclasscode;
	public String psnclassname;
	public String pk_psncl1;
	public String pk_corp;
	public String createdate; //创建日期
	public String memo; //备注
	public Integer psnclscope;
	public UFDate sealflag; //封存标志
	public UFDateTime ts;
	public Integer dr;

	@Override
	public String getPKFieldName() {
		
		return "pk_psncl";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_psncl";
	}

	@Override
	public String getTableName() {
		return "bd_psncl";
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_psncl() {
		return pk_psncl;
	}

	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	public String getPk_psncl1() {
		return pk_psncl1;
	}

	public void setPk_psncl1(String pk_psncl1) {
		this.pk_psncl1 = pk_psncl1;
	}

	public String getPsnclasscode() {
		return psnclasscode;
	}

	public void setPsnclasscode(String psnclasscode) {
		this.psnclasscode = psnclasscode;
	}

	public String getPsnclassname() {
		return psnclassname;
	}

	public void setPsnclassname(String psnclassname) {
		this.psnclassname = psnclassname;
	}

	public Integer getPsnclscope() {
		return psnclscope;
	}

	public void setPsnclscope(Integer psnclscope) {
		this.psnclscope = psnclscope;
	}

	public UFDate getSealflag() {
		return sealflag;
	}

	public void setSealflag(UFDate sealflag) {
		this.sealflag = sealflag;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

}
