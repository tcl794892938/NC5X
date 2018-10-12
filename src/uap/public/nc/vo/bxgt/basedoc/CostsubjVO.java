package nc.vo.bxgt.basedoc;

import nc.vo.pub.SuperVO;

public class CostsubjVO extends SuperVO {
	
	private String costcode;		
	private String costname;	
	private String creatorcorp;	
	private String def1;		
	private String def2;			
	private String def3;		
	private String def4;			
	private String def5;		
	private Integer dr;	
	private String free1;		
	private String free10;			
	private String free2;		
	private String free3;		
	private String free4;		
	private String free5;			
	private String free6;			
	private String free7;			
	private String free8;		
	private String free9;		
	private String incomeflag;		
	private String ioflag;
	private String isactiveappr;
	private String isbalancesubj;		
	private String isexpensepro;			
	private String memo;			
	private String mnecode;			
	private String outflag;		
	private String pk_balancesubj;			
	private String pk_cashflow;		
	private String pk_corp;		
	private String pk_costsubj;			
	private String pk_parent;		
	private String pk_source;			
	private String sealflag;		
	private String ts;			


	@Override
	public String getPKFieldName() {
		return "pk_costsubj";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_costsubj";
	}

	@Override
	public String getTableName() {
		return "bd_costsubj";
	}

	public String getCostcode() {
		return costcode;
	}

	public void setCostcode(String costcode) {
		this.costcode = costcode;
	}

	public String getCostname() {
		return costname;
	}

	public void setCostname(String costname) {
		this.costname = costname;
	}

	public String getCreatorcorp() {
		return creatorcorp;
	}

	public void setCreatorcorp(String creatorcorp) {
		this.creatorcorp = creatorcorp;
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

	public String getFree1() {
		return free1;
	}

	public void setFree1(String free1) {
		this.free1 = free1;
	}

	public String getFree10() {
		return free10;
	}

	public void setFree10(String free10) {
		this.free10 = free10;
	}

	public String getFree2() {
		return free2;
	}

	public void setFree2(String free2) {
		this.free2 = free2;
	}

	public String getFree3() {
		return free3;
	}

	public void setFree3(String free3) {
		this.free3 = free3;
	}

	public String getFree4() {
		return free4;
	}

	public void setFree4(String free4) {
		this.free4 = free4;
	}

	public String getFree5() {
		return free5;
	}

	public void setFree5(String free5) {
		this.free5 = free5;
	}

	public String getFree6() {
		return free6;
	}

	public void setFree6(String free6) {
		this.free6 = free6;
	}

	public String getFree7() {
		return free7;
	}

	public void setFree7(String free7) {
		this.free7 = free7;
	}

	public String getFree8() {
		return free8;
	}

	public void setFree8(String free8) {
		this.free8 = free8;
	}

	public String getFree9() {
		return free9;
	}

	public void setFree9(String free9) {
		this.free9 = free9;
	}

	public String getIncomeflag() {
		return incomeflag;
	}

	public void setIncomeflag(String incomeflag) {
		this.incomeflag = incomeflag;
	}

	public String getIoflag() {
		return ioflag;
	}

	public void setIoflag(String ioflag) {
		this.ioflag = ioflag;
	}

	public String getIsactiveappr() {
		return isactiveappr;
	}

	public void setIsactiveappr(String isactiveappr) {
		this.isactiveappr = isactiveappr;
	}

	public String getIsbalancesubj() {
		return isbalancesubj;
	}

	public void setIsbalancesubj(String isbalancesubj) {
		this.isbalancesubj = isbalancesubj;
	}

	public String getIsexpensepro() {
		return isexpensepro;
	}

	public void setIsexpensepro(String isexpensepro) {
		this.isexpensepro = isexpensepro;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMnecode() {
		return mnecode;
	}

	public void setMnecode(String mnecode) {
		this.mnecode = mnecode;
	}

	public String getOutflag() {
		return outflag;
	}

	public void setOutflag(String outflag) {
		this.outflag = outflag;
	}

	public String getPk_balancesubj() {
		return pk_balancesubj;
	}

	public void setPk_balancesubj(String pk_balancesubj) {
		this.pk_balancesubj = pk_balancesubj;
	}

	public String getPk_cashflow() {
		return pk_cashflow;
	}

	public void setPk_cashflow(String pk_cashflow) {
		this.pk_cashflow = pk_cashflow;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_costsubj() {
		return pk_costsubj;
	}

	public void setPk_costsubj(String pk_costsubj) {
		this.pk_costsubj = pk_costsubj;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getPk_source() {
		return pk_source;
	}

	public void setPk_source(String pk_source) {
		this.pk_source = pk_source;
	}

	public String getSealflag() {
		return sealflag;
	}

	public void setSealflag(String sealflag) {
		this.sealflag = sealflag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
