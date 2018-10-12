package nc.vo.dahuan.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class AlerthtksVO extends SuperVO {

	
	

	private String jobcode;
	private UFDouble xsj; // ���۽��
	private UFDouble cgj; // �ɹ����
	private UFDouble ce; // ë��
	private UFDouble sjsk; // ʵ���տ�
	private UFDouble sjfk; // ʵ�ʸ���
	private UFDouble sjml; // ʵ��ë��
	private UFDouble sjfy; // ʵ�ʷ���
	private UFDouble sjkp; // ʵ�ʿ�Ʊ
	private UFDouble sjsp; // ʵ����Ʊ
	private Integer alertype;// 0 ��ͬͳ�� ��1��ִͬ�У�2��֧
	private String pk_htalert;
	private String jobname;
	private String custname;
	private String deptname;
	private String pk_corp;
	private String xmcode;
	private String projectname;
	
	
	
	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getXmcode() {
		return xmcode;
	}

	public void setXmcode(String xmcode) {
		this.xmcode = xmcode; 
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
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

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getJobcode() {
		return jobcode;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public String getPk_htalert() {
		return pk_htalert;
	}

	public void setPk_htalert(String pk_htalert) {
		this.pk_htalert = pk_htalert;
	}

	public UFDouble getXsj() {
		return xsj;
	}

	public void setXsj(UFDouble xsj) {
		this.xsj = xsj;
	}

	public UFDouble getCgj() {
		return cgj;
	}

	public void setCgj(UFDouble cgj) {
		this.cgj = cgj;
	}

	public UFDouble getCe() {
		return ce;
	}

	public void setCe(UFDouble ce) {
		this.ce = ce;
	}

	public UFDouble getSjsk() {
		return sjsk;
	}

	public void setSjsk(UFDouble sjsk) {
		this.sjsk = sjsk;
	}

	public UFDouble getSjfk() {
		return sjfk;
	}

	public void setSjfk(UFDouble sjfk) {
		this.sjfk = sjfk;
	}

	public UFDouble getSjml() {
		return sjml;
	}

	public void setSjml(UFDouble sjml) {
		this.sjml = sjml;
	}

	public UFDouble getSjfy() {
		return sjfy;
	}

	public void setSjfy(UFDouble sjfy) {
		this.sjfy = sjfy;
	}

	public UFDouble getSjkp() {
		return sjkp;
	}

	public void setSjkp(UFDouble sjkp) {
		this.sjkp = sjkp;
	}

	public UFDouble getSjsp() {
		return sjsp;
	}

	public void setSjsp(UFDouble sjsp) {
		this.sjsp = sjsp;
	}

	public Integer getAlertype() {
		return alertype;
	}

	public void setAlertype(Integer alertype) {
		this.alertype = alertype;
	}

	public String getPKFieldName() {

		return "pk_htalert";
	}

	public String getParentPKFieldName() {

		return null;
	}

	public String getTableName() {

		return "dh_htalert";
	}

	
	/**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2013-10-8
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_htalert;
	   
	   }

    /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *pk_htalert
	  * ��������:2013-10-8
	  * @param newPk_fksqbill  String    
	  */
	 public void setPrimaryKey(String newPk_htalert) {
				
		 pk_htalert = newPk_htalert; 
				
	 } 
}
