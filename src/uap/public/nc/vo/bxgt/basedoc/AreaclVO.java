package nc.vo.bxgt.basedoc;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class AreaclVO extends SuperVO {

	private String pk_areacl;
	private String pk_fatherarea;
	private String areaclname;
	private String pk_corp;
	private String areaclcode;

	private String mnecode;
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;
	public Integer dr;
	public UFDateTime ts;
	@Override
	public String getPKFieldName() {
		return "pk_areacl";
	}
	@Override
	public String getParentPKFieldName() {
		return "pk_areacl";
	}
	@Override
	public String getTableName() {
		return "bd_areacl";
	}
	public String getAreaclcode() {
		return areaclcode;
	}
	public void setAreaclcode(String areaclcode) {
		this.areaclcode = areaclcode;
	}
	public String getAreaclname() {
		return areaclname;
	}
	public void setAreaclname(String areaclname) {
		this.areaclname = areaclname;
	}
	public String getDef1() {
		return def1;
	}
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	public String getDef2() {
		return def2;
	}
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	public String getDef3() {
		return def3;
	}
	public void setDef3(String def3) {
		this.def3 = def3;
	}
	public String getDef4() {
		return def4;
	}
	public void setDef4(String def4) {
		this.def4 = def4;
	}
	public String getDef5() {
		return def5;
	}
	public void setDef5(String def5) {
		this.def5 = def5;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getMnecode() {
		return mnecode;
	}
	public void setMnecode(String mnecode) {
		this.mnecode = mnecode;
	}
	public String getPk_areacl() {
		return pk_areacl;
	}
	public void setPk_areacl(String pk_areacl) {
		this.pk_areacl = pk_areacl;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public String getPk_fatherarea() {
		return pk_fatherarea;
	}
	public void setPk_fatherarea(String pk_fatherarea) {
		this.pk_fatherarea = pk_fatherarea;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
}