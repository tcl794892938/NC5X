package nc.vo.dahuan.xmrz;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class HtlogoBVO extends SuperVO{
	
	  public String pk_logo_d;   
	  public UFDate logo_date;  
	  public String content ;   
	  public Integer vfstatus ;
	  public UFDateTime ts;
	  public Integer dr    ;     
	  public String logo_person;
	  public String pk_contract;
	  public Integer rzlx;
	  
	  public static final String PK_LOGO_D = "pk_logo_d";
	  public static final String LOGO_DATE = "logo_date";
	  public static final String CONTENT = "content";
	  public static final String VFSTATUS = "vfstatus";
	  public static final String TS = "ts";
	  public static final String DR = "dr";
	  public static final String LOGO_PERSON = "logo_person";
	  public static final String PK_CONTRACT = "pk_contract";

	  
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDate getLogo_date() {
		return logo_date;
	}

	public void setLogo_date(UFDate logo_date) {
		this.logo_date = logo_date;
	}

	public String getLogo_person() {
		return logo_person;
	}

	public void setLogo_person(String logo_person) {
		this.logo_person = logo_person;
	}

	public String getPk_contract() {
		return pk_contract;
	}

	public void setPk_contract(String pk_contract) {
		this.pk_contract = pk_contract;
	}

	public String getPk_logo_d() {
		return pk_logo_d;
	}

	public void setPk_logo_d(String pk_logo_d) {
		this.pk_logo_d = pk_logo_d;
	}

	public Integer getVfstatus() {
		return vfstatus;
	}

	public void setVfstatus(Integer vfstatus) {
		this.vfstatus = vfstatus;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_logo_d";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		//û�и���
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dh_htlogo_d";
	}

	public Integer getRzlx() {
		return rzlx;
	}

	public void setRzlx(Integer rzlx) {
		this.rzlx = rzlx;
	}

}