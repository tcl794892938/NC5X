package nc.vo.bxgt.ccp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class BxgtIabillHVO extends SuperVO {
	
	private String bauditedflag;			
	private String bdisableflag;			
	private String bestimateflag;		
	private String boutestimate;			
	private String bwithdrawalflag;			
	private String caccountmonth;		
	private String caccountyear;		
	private String cagentid;			
	private String cbillid;		
	private String cbilltypecode;			
	private String cbiztypeid;		
	private String cbusinessbillid;			
	private String ccustomvendorbasid;		
	private String ccustomvendorid;		
	private String cdeptid;		
	private String cdispatchid;			
	private String cemployeeid;			
	private String clastoperatorid;		
	private String coperatorid;		
	private String cothercalbodyid;		
	private String cothercorpid;			
	private String coutcalbodyid;		
	private String coutcorpid;			
	private String crdcenterid;			
	private String csourcemodulename;			
	private String cstockrdcenterid;			
	private String cwarehouseid;			
	private String cwarehousemanagerid;			
	private String dbilldate;		
	private String dcheckdate;			
	private Integer dr;		
	private Integer fallocflag;		
	private Integer fdispatchflag;			
	private Integer idebtflag;		
	private Integer iprintcount;		
	private UFDouble ncost;			
	private String pk_corp;			
	private String pk_defdoc1;			
	private String pk_defdoc10;			
	private String pk_defdoc11;			
	private String pk_defdoc12;		
	private String pk_defdoc13;		
	private String pk_defdoc14;		
	private String pk_defdoc15;			
	private String pk_defdoc16;			
	private String pk_defdoc17;			
	private String pk_defdoc18;		
	private String pk_defdoc19;			
	private String pk_defdoc2;			
	private String pk_defdoc20;			
	private String pk_defdoc3;			
	private String pk_defdoc4;			
	private String pk_defdoc5;			
	private String pk_defdoc6;		
	private String pk_defdoc7;		
	private String pk_defdoc8;		
	private String pk_defdoc9;		
	private String tlastmaketime;			
	private String tmaketime;			
	private String ts;		
	private String vadjpricefilecode;		
	private String vbillcode;			
	private String vcheckbillcode;		
	private String vdef1;			
	private String vdef10;		
	private String vdef11;		
	private String vdef12;		
	private String vdef13;		
	private String vdef14;			
	private String vdef15;			
	private String vdef16;			
	private String vdef17;		
	private String vdef18;		
	private String vdef19;		
	private String vdef2;			
	private String vdef20;			
	private String vdef3;			
	private String vdef4;		
	private String vdef5;		
	private String vdef6;		
	private String vdef7;		
	private String vdef8;		
	private String vdef9;			
	private String vnote;		


	@Override
	public String getPKFieldName() {
		return "cbillid";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "ia_bill";
	}

	public String getBauditedflag() {
		return bauditedflag;
	}

	public void setBauditedflag(String bauditedflag) {
		this.bauditedflag = bauditedflag;
	}

	public String getBdisableflag() {
		return bdisableflag;
	}

	public void setBdisableflag(String bdisableflag) {
		this.bdisableflag = bdisableflag;
	}

	public String getBestimateflag() {
		return bestimateflag;
	}

	public void setBestimateflag(String bestimateflag) {
		this.bestimateflag = bestimateflag;
	}

	public String getBoutestimate() {
		return boutestimate;
	}

	public void setBoutestimate(String boutestimate) {
		this.boutestimate = boutestimate;
	}

	public String getBwithdrawalflag() {
		return bwithdrawalflag;
	}

	public void setBwithdrawalflag(String bwithdrawalflag) {
		this.bwithdrawalflag = bwithdrawalflag;
	}

	public String getCaccountmonth() {
		return caccountmonth;
	}

	public void setCaccountmonth(String caccountmonth) {
		this.caccountmonth = caccountmonth;
	}

	public String getCaccountyear() {
		return caccountyear;
	}

	public void setCaccountyear(String caccountyear) {
		this.caccountyear = caccountyear;
	}

	public String getCagentid() {
		return cagentid;
	}

	public void setCagentid(String cagentid) {
		this.cagentid = cagentid;
	}

	public String getCbillid() {
		return cbillid;
	}

	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}

	public String getCbilltypecode() {
		return cbilltypecode;
	}

	public void setCbilltypecode(String cbilltypecode) {
		this.cbilltypecode = cbilltypecode;
	}

	public String getCbiztypeid() {
		return cbiztypeid;
	}

	public void setCbiztypeid(String cbiztypeid) {
		this.cbiztypeid = cbiztypeid;
	}

	public String getCbusinessbillid() {
		return cbusinessbillid;
	}

	public void setCbusinessbillid(String cbusinessbillid) {
		this.cbusinessbillid = cbusinessbillid;
	}

	public String getCcustomvendorbasid() {
		return ccustomvendorbasid;
	}

	public void setCcustomvendorbasid(String ccustomvendorbasid) {
		this.ccustomvendorbasid = ccustomvendorbasid;
	}

	public String getCcustomvendorid() {
		return ccustomvendorid;
	}

	public void setCcustomvendorid(String ccustomvendorid) {
		this.ccustomvendorid = ccustomvendorid;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public String getCdispatchid() {
		return cdispatchid;
	}

	public void setCdispatchid(String cdispatchid) {
		this.cdispatchid = cdispatchid;
	}

	public String getCemployeeid() {
		return cemployeeid;
	}

	public void setCemployeeid(String cemployeeid) {
		this.cemployeeid = cemployeeid;
	}

	public String getClastoperatorid() {
		return clastoperatorid;
	}

	public void setClastoperatorid(String clastoperatorid) {
		this.clastoperatorid = clastoperatorid;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getCothercalbodyid() {
		return cothercalbodyid;
	}

	public void setCothercalbodyid(String cothercalbodyid) {
		this.cothercalbodyid = cothercalbodyid;
	}

	public String getCothercorpid() {
		return cothercorpid;
	}

	public void setCothercorpid(String cothercorpid) {
		this.cothercorpid = cothercorpid;
	}

	public String getCoutcalbodyid() {
		return coutcalbodyid;
	}

	public void setCoutcalbodyid(String coutcalbodyid) {
		this.coutcalbodyid = coutcalbodyid;
	}

	public String getCoutcorpid() {
		return coutcorpid;
	}

	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}

	public String getCrdcenterid() {
		return crdcenterid;
	}

	public void setCrdcenterid(String crdcenterid) {
		this.crdcenterid = crdcenterid;
	}

	public String getCsourcemodulename() {
		return csourcemodulename;
	}

	public void setCsourcemodulename(String csourcemodulename) {
		this.csourcemodulename = csourcemodulename;
	}

	public String getCstockrdcenterid() {
		return cstockrdcenterid;
	}

	public void setCstockrdcenterid(String cstockrdcenterid) {
		this.cstockrdcenterid = cstockrdcenterid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getCwarehousemanagerid() {
		return cwarehousemanagerid;
	}

	public void setCwarehousemanagerid(String cwarehousemanagerid) {
		this.cwarehousemanagerid = cwarehousemanagerid;
	}

	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getDcheckdate() {
		return dcheckdate;
	}

	public void setDcheckdate(String dcheckdate) {
		this.dcheckdate = dcheckdate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getFallocflag() {
		return fallocflag;
	}

	public void setFallocflag(Integer fallocflag) {
		this.fallocflag = fallocflag;
	}

	public Integer getFdispatchflag() {
		return fdispatchflag;
	}

	public void setFdispatchflag(Integer fdispatchflag) {
		this.fdispatchflag = fdispatchflag;
	}

	public Integer getIdebtflag() {
		return idebtflag;
	}

	public void setIdebtflag(Integer idebtflag) {
		this.idebtflag = idebtflag;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public UFDouble getNcost() {
		return ncost;
	}

	public void setNcost(UFDouble ncost) {
		this.ncost = ncost;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_defdoc1() {
		return pk_defdoc1;
	}

	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}

	public String getPk_defdoc10() {
		return pk_defdoc10;
	}

	public void setPk_defdoc10(String pk_defdoc10) {
		this.pk_defdoc10 = pk_defdoc10;
	}

	public String getPk_defdoc11() {
		return pk_defdoc11;
	}

	public void setPk_defdoc11(String pk_defdoc11) {
		this.pk_defdoc11 = pk_defdoc11;
	}

	public String getPk_defdoc12() {
		return pk_defdoc12;
	}

	public void setPk_defdoc12(String pk_defdoc12) {
		this.pk_defdoc12 = pk_defdoc12;
	}

	public String getPk_defdoc13() {
		return pk_defdoc13;
	}

	public void setPk_defdoc13(String pk_defdoc13) {
		this.pk_defdoc13 = pk_defdoc13;
	}

	public String getPk_defdoc14() {
		return pk_defdoc14;
	}

	public void setPk_defdoc14(String pk_defdoc14) {
		this.pk_defdoc14 = pk_defdoc14;
	}

	public String getPk_defdoc15() {
		return pk_defdoc15;
	}

	public void setPk_defdoc15(String pk_defdoc15) {
		this.pk_defdoc15 = pk_defdoc15;
	}

	public String getPk_defdoc16() {
		return pk_defdoc16;
	}

	public void setPk_defdoc16(String pk_defdoc16) {
		this.pk_defdoc16 = pk_defdoc16;
	}

	public String getPk_defdoc17() {
		return pk_defdoc17;
	}

	public void setPk_defdoc17(String pk_defdoc17) {
		this.pk_defdoc17 = pk_defdoc17;
	}

	public String getPk_defdoc18() {
		return pk_defdoc18;
	}

	public void setPk_defdoc18(String pk_defdoc18) {
		this.pk_defdoc18 = pk_defdoc18;
	}

	public String getPk_defdoc19() {
		return pk_defdoc19;
	}

	public void setPk_defdoc19(String pk_defdoc19) {
		this.pk_defdoc19 = pk_defdoc19;
	}

	public String getPk_defdoc2() {
		return pk_defdoc2;
	}

	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}

	public String getPk_defdoc20() {
		return pk_defdoc20;
	}

	public void setPk_defdoc20(String pk_defdoc20) {
		this.pk_defdoc20 = pk_defdoc20;
	}

	public String getPk_defdoc3() {
		return pk_defdoc3;
	}

	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}

	public String getPk_defdoc4() {
		return pk_defdoc4;
	}

	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}

	public String getPk_defdoc5() {
		return pk_defdoc5;
	}

	public void setPk_defdoc5(String pk_defdoc5) {
		this.pk_defdoc5 = pk_defdoc5;
	}

	public String getPk_defdoc6() {
		return pk_defdoc6;
	}

	public void setPk_defdoc6(String pk_defdoc6) {
		this.pk_defdoc6 = pk_defdoc6;
	}

	public String getPk_defdoc7() {
		return pk_defdoc7;
	}

	public void setPk_defdoc7(String pk_defdoc7) {
		this.pk_defdoc7 = pk_defdoc7;
	}

	public String getPk_defdoc8() {
		return pk_defdoc8;
	}

	public void setPk_defdoc8(String pk_defdoc8) {
		this.pk_defdoc8 = pk_defdoc8;
	}

	public String getPk_defdoc9() {
		return pk_defdoc9;
	}

	public void setPk_defdoc9(String pk_defdoc9) {
		this.pk_defdoc9 = pk_defdoc9;
	}

	public String getTlastmaketime() {
		return tlastmaketime;
	}

	public void setTlastmaketime(String tlastmaketime) {
		this.tlastmaketime = tlastmaketime;
	}

	public String getTmaketime() {
		return tmaketime;
	}

	public void setTmaketime(String tmaketime) {
		this.tmaketime = tmaketime;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVadjpricefilecode() {
		return vadjpricefilecode;
	}

	public void setVadjpricefilecode(String vadjpricefilecode) {
		this.vadjpricefilecode = vadjpricefilecode;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getVcheckbillcode() {
		return vcheckbillcode;
	}

	public void setVcheckbillcode(String vcheckbillcode) {
		this.vcheckbillcode = vcheckbillcode;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef10() {
		return vdef10;
	}

	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}

	public String getVdef11() {
		return vdef11;
	}

	public void setVdef11(String vdef11) {
		this.vdef11 = vdef11;
	}

	public String getVdef12() {
		return vdef12;
	}

	public void setVdef12(String vdef12) {
		this.vdef12 = vdef12;
	}

	public String getVdef13() {
		return vdef13;
	}

	public void setVdef13(String vdef13) {
		this.vdef13 = vdef13;
	}

	public String getVdef14() {
		return vdef14;
	}

	public void setVdef14(String vdef14) {
		this.vdef14 = vdef14;
	}

	public String getVdef15() {
		return vdef15;
	}

	public void setVdef15(String vdef15) {
		this.vdef15 = vdef15;
	}

	public String getVdef16() {
		return vdef16;
	}

	public void setVdef16(String vdef16) {
		this.vdef16 = vdef16;
	}

	public String getVdef17() {
		return vdef17;
	}

	public void setVdef17(String vdef17) {
		this.vdef17 = vdef17;
	}

	public String getVdef18() {
		return vdef18;
	}

	public void setVdef18(String vdef18) {
		this.vdef18 = vdef18;
	}

	public String getVdef19() {
		return vdef19;
	}

	public void setVdef19(String vdef19) {
		this.vdef19 = vdef19;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef20() {
		return vdef20;
	}

	public void setVdef20(String vdef20) {
		this.vdef20 = vdef20;
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

	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}

	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}

	public String getVdef8() {
		return vdef8;
	}

	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}

	public String getVdef9() {
		return vdef9;
	}

	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

}
