package nc.vo.dahuan.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class AlertZxVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitname;

	private String jobcode;

	private String jobname;

	private String projetname;

	private String custname;

	private String deptname;

	private UFDouble xsj;

	private UFDouble cgj;

	private UFDouble ce;

	private UFDouble kslv;

	public UFDouble getKslv() {
		return this.kslv;
	}

	public UFDouble getCe() {
		return ce;
	}

	public void setCe(UFDouble ce) {
		this.ce = ce;
	}

	public UFDouble getCgj() {
		return cgj;
	}

	public void setCgj(UFDouble cgj) {
		this.cgj = cgj;
	}

	public UFDouble getXsj() {
		return xsj;
	}

	public void setXsj(UFDouble xsj) {
		this.xsj = xsj;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getJobcode() {
		return jobcode;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getProjetname() {
		return projetname;
	}

	public void setProjetname(String projetname) {
		this.projetname = projetname;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setKslv(UFDouble kslv) {
		this.kslv = kslv;
	}

}
