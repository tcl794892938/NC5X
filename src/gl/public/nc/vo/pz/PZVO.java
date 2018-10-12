package nc.vo.pz;

import nc.vo.pub.SuperVO;

public class PZVO extends SuperVO{

	private String pk_pzcx;
	private String code;
	private String name;
	private String qj1;
	private String qj2;
	private String qj3;
	private String year;
	private String month;
	private String ts;
	private Integer dr;
	private String pk_rq;
	
	
	public String getPk_rq() {
		return pk_rq;
	}

	public void setPk_rq(String pk_rq) {
		this.pk_rq = pk_rq;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_pzcx() {
		return pk_pzcx;
	}

	public void setPk_pzcx(String pk_pzcx) {
		this.pk_pzcx = pk_pzcx;
	}

	public String getQj1() {
		return qj1;
	}

	public void setQj1(String qj1) {
		this.qj1 = qj1;
	}

	public String getQj2() {
		return qj2;
	}

	public void setQj2(String qj2) {
		this.qj2 = qj2;
	}

	public String getQj3() {
		return qj3;
	}

	public void setQj3(String qj3) {
		this.qj3 = qj3;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_pzcx";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_rq";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "pzcx";
	}
	
}
