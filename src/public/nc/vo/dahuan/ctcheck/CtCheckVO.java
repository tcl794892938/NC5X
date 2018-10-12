package nc.vo.dahuan.ctcheck;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class CtCheckVO extends SuperVO {

	public String ctcode;
	public String ctname;
	public String xmname;
	public String deptname;
	public String custname;
	public String prepareddate;
	public Integer no;
	public UFDouble syje;
	public UFDouble yszk;
	public UFDouble dctjetotal;
	public UFDate htrq;
	public UFDate djhdate;
	
	public Integer xj_no;//小计合计
	
	public UFDouble getDctjetotal() {
		return dctjetotal;
	}

	public void setDctjetotal(UFDouble dctjetotal) {
		this.dctjetotal = dctjetotal;
	}

	public UFDate getDjhdate() {
		return djhdate;
	}

	public void setDjhdate(UFDate djhdate) {
		this.djhdate = djhdate;
	}

	public UFDate getHtrq() {
		return htrq;
	}

	public void setHtrq(UFDate htrq) {
		this.htrq = htrq;
	}

	public UFDouble getYszk() {
		return yszk;
	}

	public void setYszk(UFDouble yszk) {
		this.yszk = yszk;
	}

	public String getXmname() {
		return xmname;
	}

	public void setXmname(String xmname) {
		this.xmname = xmname;
	}

	public UFDouble getSyje() {
		return syje;
	}

	public void setSyje(UFDouble syje) {
		this.syje = syje;
	}

	public String getCtcode() {
		return ctcode;
	}

	public void setCtcode(String ctcode) {
		this.ctcode = ctcode;
	}

	public String getCtname() {
		return ctname;
	}

	public void setCtname(String ctname) {
		this.ctname = ctname;
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

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getPrepareddate() {
		return prepareddate;
	}

	public void setPrepareddate(String prepareddate) {
		this.prepareddate = prepareddate;
	}

	public CtCheckVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return "ctcode";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "v_concheck";
	}

	public Integer getXj_no() {
		return xj_no;
	}

	public void setXj_no(Integer xj_no) {
		this.xj_no = xj_no;
	}

}
