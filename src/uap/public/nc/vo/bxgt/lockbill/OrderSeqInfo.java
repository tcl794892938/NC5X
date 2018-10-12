package nc.vo.bxgt.lockbill;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class OrderSeqInfo extends SuperVO {

	private String pk;

	private String csaleid;

	private String cgeneralhid;

	private String vouchid;
	
	private String corderid;
	
	private String cinvoiceid;

	private Integer dr;

	private UFDateTime ts;

	private String vdef1;

	private String vdef2;

	private String vdef3;

	private String vdef4;

	private String vdef5;

	public String getCgeneralhid() {
		return cgeneralhid;
	}

	public void setCgeneralhid(String cgeneralhid) {
		this.cgeneralhid = cgeneralhid;
	}

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public String getVouchid() {
		return vouchid;
	}

	public void setVouchid(String vouchid) {
		this.vouchid = vouchid;
	}

	public String getCinvoiceid() {
		return cinvoiceid;
	}

	public void setCinvoiceid(String cinvoiceid) {
		this.cinvoiceid = cinvoiceid;
	}

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}


	@Override
	public String getPKFieldName() {
		return "pk";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "bxgt_isorderseq";
	}

}
