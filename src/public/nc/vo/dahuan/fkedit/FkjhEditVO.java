package nc.vo.dahuan.fkedit;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class FkjhEditVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_fkjhedit;
	  private String ctcode;
	  private String print_no;
	  private UFDate dbilldate;
	  private Integer dr;
	  UFDouble oldmny;
	  UFDouble mny;
	  private String pk_customer;

	@Override
	public String getPKFieldName() {
		
		return "pk_fkjhedit";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		
		return "dh_fkjhedit";
	}

	public String getCtcode() {
		return ctcode;
	}

	public void setCtcode(String ctcode) {
		this.ctcode = ctcode;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getMny() {
		return mny;
	}

	public void setMny(UFDouble mny) {
		this.mny = mny;
	}

	public UFDouble getOldmny() {
		return oldmny;
	}

	public void setOldmny(UFDouble oldmny) {
		this.oldmny = oldmny;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getPk_fkjhedit() {
		return pk_fkjhedit;
	}

	public void setPk_fkjhedit(String pk_fkjhedit) {
		this.pk_fkjhedit = pk_fkjhedit;
	}

	public String getPrint_no() {
		return print_no;
	}

	public void setPrint_no(String print_no) {
		this.print_no = print_no;
	}

}
