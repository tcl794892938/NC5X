package nc.vo.bxgt.ccp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class BxgtIabillBVO extends SuperVO {
	
	private String badjusteditemflag;		
	private String bauditbatchflag;	
	private String blargessflag;		
	private String bretractflag;		
	private String brtvouchflag;	
	private String btransferincometax;		
	private String cadjustbillid;			
	private String cadjustbillitemid;		
	private String castunitid;		
	private String cauditorid;		
	private String cbill_bid;			
	private String cbillid;		
	private String cbilltypecode;		
	private String ccalcbillid;			
	private String ccalcbillitemid;		
	private String ccrspbillitemid;		
	private String ccsaleadviceitemid;		
	private String cfacardid;			
	private String cfadeviceid;		
	private String cfirstbillid;			
	private String cfirstbillitemid;		
	private String cfirstbilltypecode;			
	private String cicbillcode;		
	private String cicbillid;			
	private String cicbilltype;			
	private String cicitemid;			
	private String cinbillitemid;			
	private String cinvbasid;		
	private String cinventoryid;		
	private String cprojectid;		
	private String cprojectphase;		
	private String crdcenterid;		
	private String csaleadviceid;			
	private String csaleaudititemid;		
	private String csourcebillid;			
	private String csourcebillitemid;		
	private String csourcebilltypecode;		
	private String csumrtvouchid;		
	private String cvendorbasid;		
	private String cvendorid;			
	private String cvoucherid;		
	private String cwp;		
	private String dauditdate;		
	private String dbizdate;			
	private UFDouble ddrawrate;	
	private Integer dr;	
	private Integer fcalcbizflag;		
	private Integer fdatagetmodelflag;		
	private Integer folddatagetmodelflag;			
	private String foutadjustableflag;		
	private Integer fpricemodeflag;			
	private Integer iauditsequence;		
	private Integer irownumber;		
	private UFDouble nadjustnum;		
	private UFDouble nassistnum;			
	private UFDouble nchangerate;			
	private UFDouble ndrawsummny;			
	private UFDouble nexpaybacktax;		
	private UFDouble nfactor1;			
	private UFDouble nfactor2;			
	private UFDouble nfactor3;		
	private UFDouble nfactor4;		
	private UFDouble nfactor5;			
	private UFDouble nfactor6;		
	private UFDouble nfactor7;		
	private UFDouble nfactor8;		
	private UFDouble nincometax;			
	private UFDouble ninvarymny;		
	private UFDouble nmoney;			
	private UFDouble nnumber;			
	private UFDouble noriginalprice;		
	private UFDouble noutvarymny;			
	private UFDouble nplanedmny;		
	private UFDouble nplanedprice;			
	private UFDouble nprice;			
	private UFDouble nreasonalwastmny;			
	private UFDouble nreasonalwastnum;			
	private UFDouble nreasonalwastprice;			
	private UFDouble nsettledretractnum;			
	private UFDouble nsettledsendnum;			
	private UFDouble nsimulatemny;		
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
	private String ts;		
	private String vbatch;
	private String vbillcode;		
	private String vbomcode;			
	private String vcalcbilltype;			
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
	private String vfirstbillcode;			
	private String vfirstrowno;			
	private String vfree1;			
	private String vfree2;			
	private String vfree3;		
	private String vfree4;			
	private String vfree5;			
	private String vproducebatch;			
	private String vsourcebillcode;		
	private String vsourcerowno;			


	@Override
	public String getPKFieldName() {
		return "cbill_bid";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "ia_bill_b";
	}

	public String getBadjusteditemflag() {
		return badjusteditemflag;
	}

	public void setBadjusteditemflag(String badjusteditemflag) {
		this.badjusteditemflag = badjusteditemflag;
	}

	public String getBauditbatchflag() {
		return bauditbatchflag;
	}

	public void setBauditbatchflag(String bauditbatchflag) {
		this.bauditbatchflag = bauditbatchflag;
	}

	public String getBlargessflag() {
		return blargessflag;
	}

	public void setBlargessflag(String blargessflag) {
		this.blargessflag = blargessflag;
	}

	public String getBretractflag() {
		return bretractflag;
	}

	public void setBretractflag(String bretractflag) {
		this.bretractflag = bretractflag;
	}

	public String getBrtvouchflag() {
		return brtvouchflag;
	}

	public void setBrtvouchflag(String brtvouchflag) {
		this.brtvouchflag = brtvouchflag;
	}

	public String getBtransferincometax() {
		return btransferincometax;
	}

	public void setBtransferincometax(String btransferincometax) {
		this.btransferincometax = btransferincometax;
	}

	public String getCadjustbillid() {
		return cadjustbillid;
	}

	public void setCadjustbillid(String cadjustbillid) {
		this.cadjustbillid = cadjustbillid;
	}

	public String getCadjustbillitemid() {
		return cadjustbillitemid;
	}

	public void setCadjustbillitemid(String cadjustbillitemid) {
		this.cadjustbillitemid = cadjustbillitemid;
	}

	public String getCastunitid() {
		return castunitid;
	}

	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}

	public String getCauditorid() {
		return cauditorid;
	}

	public void setCauditorid(String cauditorid) {
		this.cauditorid = cauditorid;
	}

	public String getCbill_bid() {
		return cbill_bid;
	}

	public void setCbill_bid(String cbill_bid) {
		this.cbill_bid = cbill_bid;
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

	public String getCcalcbillid() {
		return ccalcbillid;
	}

	public void setCcalcbillid(String ccalcbillid) {
		this.ccalcbillid = ccalcbillid;
	}

	public String getCcalcbillitemid() {
		return ccalcbillitemid;
	}

	public void setCcalcbillitemid(String ccalcbillitemid) {
		this.ccalcbillitemid = ccalcbillitemid;
	}

	public String getCcrspbillitemid() {
		return ccrspbillitemid;
	}

	public void setCcrspbillitemid(String ccrspbillitemid) {
		this.ccrspbillitemid = ccrspbillitemid;
	}

	public String getCcsaleadviceitemid() {
		return ccsaleadviceitemid;
	}

	public void setCcsaleadviceitemid(String ccsaleadviceitemid) {
		this.ccsaleadviceitemid = ccsaleadviceitemid;
	}

	public String getCfacardid() {
		return cfacardid;
	}

	public void setCfacardid(String cfacardid) {
		this.cfacardid = cfacardid;
	}

	public String getCfadeviceid() {
		return cfadeviceid;
	}

	public void setCfadeviceid(String cfadeviceid) {
		this.cfadeviceid = cfadeviceid;
	}

	public String getCfirstbillid() {
		return cfirstbillid;
	}

	public void setCfirstbillid(String cfirstbillid) {
		this.cfirstbillid = cfirstbillid;
	}

	public String getCfirstbillitemid() {
		return cfirstbillitemid;
	}

	public void setCfirstbillitemid(String cfirstbillitemid) {
		this.cfirstbillitemid = cfirstbillitemid;
	}

	public String getCfirstbilltypecode() {
		return cfirstbilltypecode;
	}

	public void setCfirstbilltypecode(String cfirstbilltypecode) {
		this.cfirstbilltypecode = cfirstbilltypecode;
	}

	public String getCicbillcode() {
		return cicbillcode;
	}

	public void setCicbillcode(String cicbillcode) {
		this.cicbillcode = cicbillcode;
	}

	public String getCicbillid() {
		return cicbillid;
	}

	public void setCicbillid(String cicbillid) {
		this.cicbillid = cicbillid;
	}

	public String getCicbilltype() {
		return cicbilltype;
	}

	public void setCicbilltype(String cicbilltype) {
		this.cicbilltype = cicbilltype;
	}

	public String getCicitemid() {
		return cicitemid;
	}

	public void setCicitemid(String cicitemid) {
		this.cicitemid = cicitemid;
	}

	public String getCinbillitemid() {
		return cinbillitemid;
	}

	public void setCinbillitemid(String cinbillitemid) {
		this.cinbillitemid = cinbillitemid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getCprojectid() {
		return cprojectid;
	}

	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}

	public String getCprojectphase() {
		return cprojectphase;
	}

	public void setCprojectphase(String cprojectphase) {
		this.cprojectphase = cprojectphase;
	}

	public String getCrdcenterid() {
		return crdcenterid;
	}

	public void setCrdcenterid(String crdcenterid) {
		this.crdcenterid = crdcenterid;
	}

	public String getCsaleadviceid() {
		return csaleadviceid;
	}

	public void setCsaleadviceid(String csaleadviceid) {
		this.csaleadviceid = csaleadviceid;
	}

	public String getCsaleaudititemid() {
		return csaleaudititemid;
	}

	public void setCsaleaudititemid(String csaleaudititemid) {
		this.csaleaudititemid = csaleaudititemid;
	}

	public String getCsourcebillid() {
		return csourcebillid;
	}

	public void setCsourcebillid(String csourcebillid) {
		this.csourcebillid = csourcebillid;
	}

	public String getCsourcebillitemid() {
		return csourcebillitemid;
	}

	public void setCsourcebillitemid(String csourcebillitemid) {
		this.csourcebillitemid = csourcebillitemid;
	}

	public String getCsourcebilltypecode() {
		return csourcebilltypecode;
	}

	public void setCsourcebilltypecode(String csourcebilltypecode) {
		this.csourcebilltypecode = csourcebilltypecode;
	}

	public String getCsumrtvouchid() {
		return csumrtvouchid;
	}

	public void setCsumrtvouchid(String csumrtvouchid) {
		this.csumrtvouchid = csumrtvouchid;
	}

	public String getCvendorbasid() {
		return cvendorbasid;
	}

	public void setCvendorbasid(String cvendorbasid) {
		this.cvendorbasid = cvendorbasid;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getCvoucherid() {
		return cvoucherid;
	}

	public void setCvoucherid(String cvoucherid) {
		this.cvoucherid = cvoucherid;
	}

	public String getCwp() {
		return cwp;
	}

	public void setCwp(String cwp) {
		this.cwp = cwp;
	}

	public String getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(String dauditdate) {
		this.dauditdate = dauditdate;
	}

	public String getDbizdate() {
		return dbizdate;
	}

	public void setDbizdate(String dbizdate) {
		this.dbizdate = dbizdate;
	}

	public UFDouble getDdrawrate() {
		return ddrawrate;
	}

	public void setDdrawrate(UFDouble ddrawrate) {
		this.ddrawrate = ddrawrate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getFcalcbizflag() {
		return fcalcbizflag;
	}

	public void setFcalcbizflag(Integer fcalcbizflag) {
		this.fcalcbizflag = fcalcbizflag;
	}

	public Integer getFdatagetmodelflag() {
		return fdatagetmodelflag;
	}

	public void setFdatagetmodelflag(Integer fdatagetmodelflag) {
		this.fdatagetmodelflag = fdatagetmodelflag;
	}

	public Integer getFolddatagetmodelflag() {
		return folddatagetmodelflag;
	}

	public void setFolddatagetmodelflag(Integer folddatagetmodelflag) {
		this.folddatagetmodelflag = folddatagetmodelflag;
	}

	public String getFoutadjustableflag() {
		return foutadjustableflag;
	}

	public void setFoutadjustableflag(String foutadjustableflag) {
		this.foutadjustableflag = foutadjustableflag;
	}

	public Integer getFpricemodeflag() {
		return fpricemodeflag;
	}

	public void setFpricemodeflag(Integer fpricemodeflag) {
		this.fpricemodeflag = fpricemodeflag;
	}

	public Integer getIauditsequence() {
		return iauditsequence;
	}

	public void setIauditsequence(Integer iauditsequence) {
		this.iauditsequence = iauditsequence;
	}

	public Integer getIrownumber() {
		return irownumber;
	}

	public void setIrownumber(Integer irownumber) {
		this.irownumber = irownumber;
	}

	public UFDouble getNadjustnum() {
		return nadjustnum;
	}

	public void setNadjustnum(UFDouble nadjustnum) {
		this.nadjustnum = nadjustnum;
	}

	public UFDouble getNassistnum() {
		return nassistnum;
	}

	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}

	public UFDouble getNchangerate() {
		return nchangerate;
	}

	public void setNchangerate(UFDouble nchangerate) {
		this.nchangerate = nchangerate;
	}

	public UFDouble getNdrawsummny() {
		return ndrawsummny;
	}

	public void setNdrawsummny(UFDouble ndrawsummny) {
		this.ndrawsummny = ndrawsummny;
	}

	public UFDouble getNexpaybacktax() {
		return nexpaybacktax;
	}

	public void setNexpaybacktax(UFDouble nexpaybacktax) {
		this.nexpaybacktax = nexpaybacktax;
	}

	public UFDouble getNfactor1() {
		return nfactor1;
	}

	public void setNfactor1(UFDouble nfactor1) {
		this.nfactor1 = nfactor1;
	}

	public UFDouble getNfactor2() {
		return nfactor2;
	}

	public void setNfactor2(UFDouble nfactor2) {
		this.nfactor2 = nfactor2;
	}

	public UFDouble getNfactor3() {
		return nfactor3;
	}

	public void setNfactor3(UFDouble nfactor3) {
		this.nfactor3 = nfactor3;
	}

	public UFDouble getNfactor4() {
		return nfactor4;
	}

	public void setNfactor4(UFDouble nfactor4) {
		this.nfactor4 = nfactor4;
	}

	public UFDouble getNfactor5() {
		return nfactor5;
	}

	public void setNfactor5(UFDouble nfactor5) {
		this.nfactor5 = nfactor5;
	}

	public UFDouble getNfactor6() {
		return nfactor6;
	}

	public void setNfactor6(UFDouble nfactor6) {
		this.nfactor6 = nfactor6;
	}

	public UFDouble getNfactor7() {
		return nfactor7;
	}

	public void setNfactor7(UFDouble nfactor7) {
		this.nfactor7 = nfactor7;
	}

	public UFDouble getNfactor8() {
		return nfactor8;
	}

	public void setNfactor8(UFDouble nfactor8) {
		this.nfactor8 = nfactor8;
	}

	public UFDouble getNincometax() {
		return nincometax;
	}

	public void setNincometax(UFDouble nincometax) {
		this.nincometax = nincometax;
	}

	public UFDouble getNinvarymny() {
		return ninvarymny;
	}

	public void setNinvarymny(UFDouble ninvarymny) {
		this.ninvarymny = ninvarymny;
	}

	public UFDouble getNmoney() {
		return nmoney;
	}

	public void setNmoney(UFDouble nmoney) {
		this.nmoney = nmoney;
	}

	public UFDouble getNnumber() {
		return nnumber;
	}

	public void setNnumber(UFDouble nnumber) {
		this.nnumber = nnumber;
	}

	public UFDouble getNoriginalprice() {
		return noriginalprice;
	}

	public void setNoriginalprice(UFDouble noriginalprice) {
		this.noriginalprice = noriginalprice;
	}

	public UFDouble getNoutvarymny() {
		return noutvarymny;
	}

	public void setNoutvarymny(UFDouble noutvarymny) {
		this.noutvarymny = noutvarymny;
	}

	public UFDouble getNplanedmny() {
		return nplanedmny;
	}

	public void setNplanedmny(UFDouble nplanedmny) {
		this.nplanedmny = nplanedmny;
	}

	public UFDouble getNplanedprice() {
		return nplanedprice;
	}

	public void setNplanedprice(UFDouble nplanedprice) {
		this.nplanedprice = nplanedprice;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public UFDouble getNreasonalwastmny() {
		return nreasonalwastmny;
	}

	public void setNreasonalwastmny(UFDouble nreasonalwastmny) {
		this.nreasonalwastmny = nreasonalwastmny;
	}

	public UFDouble getNreasonalwastnum() {
		return nreasonalwastnum;
	}

	public void setNreasonalwastnum(UFDouble nreasonalwastnum) {
		this.nreasonalwastnum = nreasonalwastnum;
	}

	public UFDouble getNreasonalwastprice() {
		return nreasonalwastprice;
	}

	public void setNreasonalwastprice(UFDouble nreasonalwastprice) {
		this.nreasonalwastprice = nreasonalwastprice;
	}

	public UFDouble getNsettledretractnum() {
		return nsettledretractnum;
	}

	public void setNsettledretractnum(UFDouble nsettledretractnum) {
		this.nsettledretractnum = nsettledretractnum;
	}

	public UFDouble getNsettledsendnum() {
		return nsettledsendnum;
	}

	public void setNsettledsendnum(UFDouble nsettledsendnum) {
		this.nsettledsendnum = nsettledsendnum;
	}

	public UFDouble getNsimulatemny() {
		return nsimulatemny;
	}

	public void setNsimulatemny(UFDouble nsimulatemny) {
		this.nsimulatemny = nsimulatemny;
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

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVbatch() {
		return vbatch;
	}

	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getVbomcode() {
		return vbomcode;
	}

	public void setVbomcode(String vbomcode) {
		this.vbomcode = vbomcode;
	}

	public String getVcalcbilltype() {
		return vcalcbilltype;
	}

	public void setVcalcbilltype(String vcalcbilltype) {
		this.vcalcbilltype = vcalcbilltype;
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

	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getVfirstrowno() {
		return vfirstrowno;
	}

	public void setVfirstrowno(String vfirstrowno) {
		this.vfirstrowno = vfirstrowno;
	}

	public String getVfree1() {
		return vfree1;
	}

	public void setVfree1(String vfree1) {
		this.vfree1 = vfree1;
	}

	public String getVfree2() {
		return vfree2;
	}

	public void setVfree2(String vfree2) {
		this.vfree2 = vfree2;
	}

	public String getVfree3() {
		return vfree3;
	}

	public void setVfree3(String vfree3) {
		this.vfree3 = vfree3;
	}

	public String getVfree4() {
		return vfree4;
	}

	public void setVfree4(String vfree4) {
		this.vfree4 = vfree4;
	}

	public String getVfree5() {
		return vfree5;
	}

	public void setVfree5(String vfree5) {
		this.vfree5 = vfree5;
	}

	public String getVproducebatch() {
		return vproducebatch;
	}

	public void setVproducebatch(String vproducebatch) {
		this.vproducebatch = vproducebatch;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public String getVsourcerowno() {
		return vsourcerowno;
	}

	public void setVsourcerowno(String vsourcerowno) {
		this.vsourcerowno = vsourcerowno;
	}

}
