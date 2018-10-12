package nc.vo.dahuan.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class AlertDzVO extends SuperVO {

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

	private UFDouble sjsk;

	private UFDouble sjfk;

	private UFDouble dzks;

	private UFDouble dzkslv;

	public UFDouble getDzkslv() {
		return this.dzkslv;
	}

	public void setDzkslv(UFDouble dzkslv) {
		this.dzkslv = dzkslv;
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

	public UFDouble getDzks() {
		return dzks;
	}

	public void setDzks(UFDouble dzks) {
		this.dzks = dzks;
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

	public UFDouble getSjfk() {
		return sjfk;
	}

	public void setSjfk(UFDouble sjfk) {
		this.sjfk = sjfk;
	}

	public UFDouble getSjsk() {
		return sjsk;
	}

	public void setSjsk(UFDouble sjsk) {
		this.sjsk = sjsk;
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

}
