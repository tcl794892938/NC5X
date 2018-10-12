package nc.vo.bxgt.ccp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 产成品入库表体VO
 * 
 * @author bwy
 * 
 */
public class BxgtCcpBodyVo extends SuperVO {

	private String bbarcodeclose;

	private String bonroadflag;

	private String breturnprofit;

	private String bsafeprice;

	private String bsourcelargess;

	private String bsupplyflag;

	private String btoinzgflag;

	private String btoouttoiaflag;

	private String btooutzgflag;

	private String btou8rm;

	private String bzgflag;

	private String bzgyfflag;

	private String castunitid;

	private String cbodybilltypecode;

	private String cbodywarehouseid;

	private String ccheckstateid;

	private String ccorrespondbid;

	private String ccorrespondcode;

	private String ccorrespondhid;

	private String ccorrespondtype;

	private String ccostobject;

	private String cfirstbillbid;

	private String cfirstbillhid;

	private String cfirsttype;

	private String cfreezeid;

	private String cgeneralbid;

	private String cgeneralhid;

	private String cinvbasid;

	private String cinventoryid;

	private String corder_bb1id;

	private String cparentid;

	private String cprojectid;

	private String cprojectphaseid;

	private String cquotecurrency;

	private String cquoteunitid;

	private String creceieveid;

	private String creceiveareaid;

	private String creceivepointid;

	private String crowno;

	private String cselastunitid;

	private String csignwastbid;

	private String csignwasthid;

	private String csignwasttype;

	private String csourcebillbid;

	private String csourcebillhid;

	private String csourcetype;

	private String csourcewastbid;

	private String csourcewasthid;

	private String csourcewasttype;

	private String csrc2billbid;

	private String csrc2billhid;

	private String csrc2billtype;

	private String csumid;

	private String cvendorid;

	private String cworkcenterid;

	private String cworksiteid;

	private String cwp;

	private String dbizdate;

	private String ddeliverdate;

	private String dfirstbilldate;

	private Integer dr;

	private String drequiredate;

	private String drequiretime;

	private String dstandbydate;

	private String dvalidate;

	private String dzgdate;

	private Integer fbillrowflag;

	private Integer fchecked;

	private String flargess;

	private String ftoouttransflag;

	private UFDouble hsl;

	private Integer idesatype;

	private String isok;

	private UFDouble naccumtonum;

	private UFDouble naccumwastnum;

	private UFDouble nbarcodenum;

	private UFDouble ncorrespondastnum;

	private UFDouble ncorrespondgrsnum;

	private UFDouble ncorrespondnum;

	private UFDouble ncountnum;

	private Integer nfeesettletimes;

	private UFDouble ninassistnum;

	private UFDouble ningrossnum;

	private UFDouble ninnum;

	private UFDouble nkdnum;

	private UFDouble nmny;

	private UFDouble nneedinassistnum;

	private UFDouble noutassistnum;

	private UFDouble noutgrossnum;

	private UFDouble noutnum;

	private UFDouble nplannedmny;

	private UFDouble nplannedprice;

	private UFDouble nprice;

	private Integer npricesettlebill;

	private UFDouble nquotemny;

	private UFDouble nquotentmny;

	private UFDouble nquotentprice;

	private UFDouble nquoteprice;

	private UFDouble nquoteunitnum;

	private UFDouble nquoteunitrate;

	private UFDouble nreplenishedastnum;

	private UFDouble nreplenishednum;

	private UFDouble nretastnum;

	private UFDouble nretgrossnum;

	private UFDouble nretnum;

	private UFDouble nsalemny;

	private UFDouble nsaleprice;

	private UFDouble nshouldinnum;

	private UFDouble nshouldoutassistnum;

	private UFDouble nshouldoutnum;

	private UFDouble ntarenum;

	private UFDouble ntaxmny;

	private UFDouble ntaxprice;

	private UFDouble ntranoutastnum;

	private UFDouble ntranoutnum;

	private String pk_bodycalbody;

	private String pk_corp;

	private String pk_creqwareid;

	private String pk_cubasdoc;

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

	private String pk_invoicecorp;

	private String pk_measware;

	private String pk_packsort;

	private String pk_reqcorp;

	private String pk_reqstoorg;

	private String pk_returnreason;

	private String ts;

	private String vbatchcode;

	private String vbilltypeu8rm;

	private String vbodynote2;

	private String vcorrespondrowno;

	private String vfirstbillcode;

	private String vfirstrowno;

	private String vfree1;

	private String vfree10;

	private String vfree2;

	private String vfree3;

	private String vfree4;

	private String vfree5;

	private String vfree6;

	private String vfree7;

	private String vfree8;

	private String vfree9;

	private String vnotebody;

	private String vproductbatch;

	private String vreceiveaddress;

	private String vsignwastcode;

	private String vsignwastrowno;

	private String vsourcebillcode;

	private String vsourcerowno;

	private String vsourcewastcode;

	private String vsourcewastrowno;

	private String vsrc2billcode;

	private String vsrc2billrowno;

	private String vtransfercode;

	private String vuserdef1;

	private String vuserdef10;

	private String vuserdef11;

	private String vuserdef12;

	private String vuserdef13;

	private String vuserdef14;

	private String vuserdef15;

	private String vuserdef16;

	private String vuserdef17;

	private String vuserdef18;

	private String vuserdef19;

	private String vuserdef2;

	private String vuserdef20;

	private String vuserdef3;

	private String vuserdef4;

	private String vuserdef5;

	private String vuserdef6;

	private String vuserdef7;

	private String vuserdef8;

	private String vuserdef9;

	private String vvehiclecode;

	public String getBbarcodeclose() {
		return bbarcodeclose;
	}

	public void setBbarcodeclose(String bbarcodeclose) {
		this.bbarcodeclose = bbarcodeclose;
	}

	public String getBonroadflag() {
		return bonroadflag;
	}

	public void setBonroadflag(String bonroadflag) {
		this.bonroadflag = bonroadflag;
	}

	public String getBreturnprofit() {
		return breturnprofit;
	}

	public void setBreturnprofit(String breturnprofit) {
		this.breturnprofit = breturnprofit;
	}

	public String getBsafeprice() {
		return bsafeprice;
	}

	public void setBsafeprice(String bsafeprice) {
		this.bsafeprice = bsafeprice;
	}

	public String getBsourcelargess() {
		return bsourcelargess;
	}

	public void setBsourcelargess(String bsourcelargess) {
		this.bsourcelargess = bsourcelargess;
	}

	public String getBsupplyflag() {
		return bsupplyflag;
	}

	public void setBsupplyflag(String bsupplyflag) {
		this.bsupplyflag = bsupplyflag;
	}

	public String getBtoinzgflag() {
		return btoinzgflag;
	}

	public void setBtoinzgflag(String btoinzgflag) {
		this.btoinzgflag = btoinzgflag;
	}

	public String getBtoouttoiaflag() {
		return btoouttoiaflag;
	}

	public void setBtoouttoiaflag(String btoouttoiaflag) {
		this.btoouttoiaflag = btoouttoiaflag;
	}

	public String getBtooutzgflag() {
		return btooutzgflag;
	}

	public void setBtooutzgflag(String btooutzgflag) {
		this.btooutzgflag = btooutzgflag;
	}

	public String getBtou8rm() {
		return btou8rm;
	}

	public void setBtou8rm(String btou8rm) {
		this.btou8rm = btou8rm;
	}

	public String getBzgflag() {
		return bzgflag;
	}

	public void setBzgflag(String bzgflag) {
		this.bzgflag = bzgflag;
	}

	public String getBzgyfflag() {
		return bzgyfflag;
	}

	public void setBzgyfflag(String bzgyfflag) {
		this.bzgyfflag = bzgyfflag;
	}

	public String getCastunitid() {
		return castunitid;
	}

	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}

	public String getCbodybilltypecode() {
		return cbodybilltypecode;
	}

	public void setCbodybilltypecode(String cbodybilltypecode) {
		this.cbodybilltypecode = cbodybilltypecode;
	}

	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}

	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}

	public String getCcheckstateid() {
		return ccheckstateid;
	}

	public void setCcheckstateid(String ccheckstateid) {
		this.ccheckstateid = ccheckstateid;
	}

	public String getCcorrespondbid() {
		return ccorrespondbid;
	}

	public void setCcorrespondbid(String ccorrespondbid) {
		this.ccorrespondbid = ccorrespondbid;
	}

	public String getCcorrespondcode() {
		return ccorrespondcode;
	}

	public void setCcorrespondcode(String ccorrespondcode) {
		this.ccorrespondcode = ccorrespondcode;
	}

	public String getCcorrespondhid() {
		return ccorrespondhid;
	}

	public void setCcorrespondhid(String ccorrespondhid) {
		this.ccorrespondhid = ccorrespondhid;
	}

	public String getCcorrespondtype() {
		return ccorrespondtype;
	}

	public void setCcorrespondtype(String ccorrespondtype) {
		this.ccorrespondtype = ccorrespondtype;
	}

	public String getCcostobject() {
		return ccostobject;
	}

	public void setCcostobject(String ccostobject) {
		this.ccostobject = ccostobject;
	}

	public String getCfirstbillbid() {
		return cfirstbillbid;
	}

	public void setCfirstbillbid(String cfirstbillbid) {
		this.cfirstbillbid = cfirstbillbid;
	}

	public String getCfirstbillhid() {
		return cfirstbillhid;
	}

	public void setCfirstbillhid(String cfirstbillhid) {
		this.cfirstbillhid = cfirstbillhid;
	}

	public String getCfirsttype() {
		return cfirsttype;
	}

	public void setCfirsttype(String cfirsttype) {
		this.cfirsttype = cfirsttype;
	}

	public String getCfreezeid() {
		return cfreezeid;
	}

	public void setCfreezeid(String cfreezeid) {
		this.cfreezeid = cfreezeid;
	}

	public String getCgeneralbid() {
		return cgeneralbid;
	}

	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	public String getCgeneralhid() {
		return cgeneralhid;
	}

	public void setCgeneralhid(String cgeneralhid) {
		this.cgeneralhid = cgeneralhid;
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

	public String getCorder_bb1id() {
		return corder_bb1id;
	}

	public void setCorder_bb1id(String corder_bb1id) {
		this.corder_bb1id = corder_bb1id;
	}

	public String getCparentid() {
		return cparentid;
	}

	public void setCparentid(String cparentid) {
		this.cparentid = cparentid;
	}

	public String getCprojectid() {
		return cprojectid;
	}

	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}

	public String getCprojectphaseid() {
		return cprojectphaseid;
	}

	public void setCprojectphaseid(String cprojectphaseid) {
		this.cprojectphaseid = cprojectphaseid;
	}

	public String getCquotecurrency() {
		return cquotecurrency;
	}

	public void setCquotecurrency(String cquotecurrency) {
		this.cquotecurrency = cquotecurrency;
	}

	public String getCquoteunitid() {
		return cquoteunitid;
	}

	public void setCquoteunitid(String cquoteunitid) {
		this.cquoteunitid = cquoteunitid;
	}

	public String getCreceieveid() {
		return creceieveid;
	}

	public void setCreceieveid(String creceieveid) {
		this.creceieveid = creceieveid;
	}

	public String getCreceiveareaid() {
		return creceiveareaid;
	}

	public void setCreceiveareaid(String creceiveareaid) {
		this.creceiveareaid = creceiveareaid;
	}

	public String getCreceivepointid() {
		return creceivepointid;
	}

	public void setCreceivepointid(String creceivepointid) {
		this.creceivepointid = creceivepointid;
	}

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCselastunitid() {
		return cselastunitid;
	}

	public void setCselastunitid(String cselastunitid) {
		this.cselastunitid = cselastunitid;
	}

	public String getCsignwastbid() {
		return csignwastbid;
	}

	public void setCsignwastbid(String csignwastbid) {
		this.csignwastbid = csignwastbid;
	}

	public String getCsignwasthid() {
		return csignwasthid;
	}

	public void setCsignwasthid(String csignwasthid) {
		this.csignwasthid = csignwasthid;
	}

	public String getCsignwasttype() {
		return csignwasttype;
	}

	public void setCsignwasttype(String csignwasttype) {
		this.csignwasttype = csignwasttype;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	public String getCsourcewastbid() {
		return csourcewastbid;
	}

	public void setCsourcewastbid(String csourcewastbid) {
		this.csourcewastbid = csourcewastbid;
	}

	public String getCsourcewasthid() {
		return csourcewasthid;
	}

	public void setCsourcewasthid(String csourcewasthid) {
		this.csourcewasthid = csourcewasthid;
	}

	public String getCsourcewasttype() {
		return csourcewasttype;
	}

	public void setCsourcewasttype(String csourcewasttype) {
		this.csourcewasttype = csourcewasttype;
	}

	public String getCsrc2billbid() {
		return csrc2billbid;
	}

	public void setCsrc2billbid(String csrc2billbid) {
		this.csrc2billbid = csrc2billbid;
	}

	public String getCsrc2billhid() {
		return csrc2billhid;
	}

	public void setCsrc2billhid(String csrc2billhid) {
		this.csrc2billhid = csrc2billhid;
	}

	public String getCsrc2billtype() {
		return csrc2billtype;
	}

	public void setCsrc2billtype(String csrc2billtype) {
		this.csrc2billtype = csrc2billtype;
	}

	public String getCsumid() {
		return csumid;
	}

	public void setCsumid(String csumid) {
		this.csumid = csumid;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getCworkcenterid() {
		return cworkcenterid;
	}

	public void setCworkcenterid(String cworkcenterid) {
		this.cworkcenterid = cworkcenterid;
	}

	public String getCworksiteid() {
		return cworksiteid;
	}

	public void setCworksiteid(String cworksiteid) {
		this.cworksiteid = cworksiteid;
	}

	public String getCwp() {
		return cwp;
	}

	public void setCwp(String cwp) {
		this.cwp = cwp;
	}

	public String getDbizdate() {
		return dbizdate;
	}

	public void setDbizdate(String dbizdate) {
		this.dbizdate = dbizdate;
	}

	public String getDdeliverdate() {
		return ddeliverdate;
	}

	public void setDdeliverdate(String ddeliverdate) {
		this.ddeliverdate = ddeliverdate;
	}

	public String getDfirstbilldate() {
		return dfirstbilldate;
	}

	public void setDfirstbilldate(String dfirstbilldate) {
		this.dfirstbilldate = dfirstbilldate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getDrequiredate() {
		return drequiredate;
	}

	public void setDrequiredate(String drequiredate) {
		this.drequiredate = drequiredate;
	}

	public String getDrequiretime() {
		return drequiretime;
	}

	public void setDrequiretime(String drequiretime) {
		this.drequiretime = drequiretime;
	}

	public String getDstandbydate() {
		return dstandbydate;
	}

	public void setDstandbydate(String dstandbydate) {
		this.dstandbydate = dstandbydate;
	}

	public String getDvalidate() {
		return dvalidate;
	}

	public void setDvalidate(String dvalidate) {
		this.dvalidate = dvalidate;
	}

	public String getDzgdate() {
		return dzgdate;
	}

	public void setDzgdate(String dzgdate) {
		this.dzgdate = dzgdate;
	}

	public Integer getFbillrowflag() {
		return fbillrowflag;
	}

	public void setFbillrowflag(Integer fbillrowflag) {
		this.fbillrowflag = fbillrowflag;
	}

	public Integer getFchecked() {
		return fchecked;
	}

	public void setFchecked(Integer fchecked) {
		this.fchecked = fchecked;
	}

	public String getFlargess() {
		return flargess;
	}

	public void setFlargess(String flargess) {
		this.flargess = flargess;
	}

	public String getFtoouttransflag() {
		return ftoouttransflag;
	}

	public void setFtoouttransflag(String ftoouttransflag) {
		this.ftoouttransflag = ftoouttransflag;
	}

	public UFDouble getHsl() {
		return hsl;
	}

	public void setHsl(UFDouble hsl) {
		this.hsl = hsl;
	}

	public Integer getIdesatype() {
		return idesatype;
	}

	public void setIdesatype(Integer idesatype) {
		this.idesatype = idesatype;
	}

	public String getIsok() {
		return isok;
	}

	public void setIsok(String isok) {
		this.isok = isok;
	}

	public UFDouble getNaccumtonum() {
		return naccumtonum;
	}

	public void setNaccumtonum(UFDouble naccumtonum) {
		this.naccumtonum = naccumtonum;
	}

	public UFDouble getNaccumwastnum() {
		return naccumwastnum;
	}

	public void setNaccumwastnum(UFDouble naccumwastnum) {
		this.naccumwastnum = naccumwastnum;
	}

	public UFDouble getNbarcodenum() {
		return nbarcodenum;
	}

	public void setNbarcodenum(UFDouble nbarcodenum) {
		this.nbarcodenum = nbarcodenum;
	}

	public UFDouble getNcorrespondastnum() {
		return ncorrespondastnum;
	}

	public void setNcorrespondastnum(UFDouble ncorrespondastnum) {
		this.ncorrespondastnum = ncorrespondastnum;
	}

	public UFDouble getNcorrespondgrsnum() {
		return ncorrespondgrsnum;
	}

	public void setNcorrespondgrsnum(UFDouble ncorrespondgrsnum) {
		this.ncorrespondgrsnum = ncorrespondgrsnum;
	}

	public UFDouble getNcorrespondnum() {
		return ncorrespondnum;
	}

	public void setNcorrespondnum(UFDouble ncorrespondnum) {
		this.ncorrespondnum = ncorrespondnum;
	}

	public UFDouble getNcountnum() {
		return ncountnum;
	}

	public void setNcountnum(UFDouble ncountnum) {
		this.ncountnum = ncountnum;
	}

	public Integer getNfeesettletimes() {
		return nfeesettletimes;
	}

	public void setNfeesettletimes(Integer nfeesettletimes) {
		this.nfeesettletimes = nfeesettletimes;
	}

	public UFDouble getNinassistnum() {
		return ninassistnum;
	}

	public void setNinassistnum(UFDouble ninassistnum) {
		this.ninassistnum = ninassistnum;
	}

	public UFDouble getNingrossnum() {
		return ningrossnum;
	}

	public void setNingrossnum(UFDouble ningrossnum) {
		this.ningrossnum = ningrossnum;
	}

	public UFDouble getNinnum() {
		return ninnum;
	}

	public void setNinnum(UFDouble ninnum) {
		this.ninnum = ninnum;
	}

	public UFDouble getNkdnum() {
		return nkdnum;
	}

	public void setNkdnum(UFDouble nkdnum) {
		this.nkdnum = nkdnum;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public UFDouble getNneedinassistnum() {
		return nneedinassistnum;
	}

	public void setNneedinassistnum(UFDouble nneedinassistnum) {
		this.nneedinassistnum = nneedinassistnum;
	}

	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	public void setNoutassistnum(UFDouble noutassistnum) {
		this.noutassistnum = noutassistnum;
	}

	public UFDouble getNoutgrossnum() {
		return noutgrossnum;
	}

	public void setNoutgrossnum(UFDouble noutgrossnum) {
		this.noutgrossnum = noutgrossnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNplannedmny() {
		return nplannedmny;
	}

	public void setNplannedmny(UFDouble nplannedmny) {
		this.nplannedmny = nplannedmny;
	}

	public UFDouble getNplannedprice() {
		return nplannedprice;
	}

	public void setNplannedprice(UFDouble nplannedprice) {
		this.nplannedprice = nplannedprice;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public Integer getNpricesettlebill() {
		return npricesettlebill;
	}

	public void setNpricesettlebill(Integer npricesettlebill) {
		this.npricesettlebill = npricesettlebill;
	}

	public UFDouble getNquotemny() {
		return nquotemny;
	}

	public void setNquotemny(UFDouble nquotemny) {
		this.nquotemny = nquotemny;
	}

	public UFDouble getNquotentmny() {
		return nquotentmny;
	}

	public void setNquotentmny(UFDouble nquotentmny) {
		this.nquotentmny = nquotentmny;
	}

	public UFDouble getNquotentprice() {
		return nquotentprice;
	}

	public void setNquotentprice(UFDouble nquotentprice) {
		this.nquotentprice = nquotentprice;
	}

	public UFDouble getNquoteprice() {
		return nquoteprice;
	}

	public void setNquoteprice(UFDouble nquoteprice) {
		this.nquoteprice = nquoteprice;
	}

	public UFDouble getNquoteunitnum() {
		return nquoteunitnum;
	}

	public void setNquoteunitnum(UFDouble nquoteunitnum) {
		this.nquoteunitnum = nquoteunitnum;
	}

	public UFDouble getNquoteunitrate() {
		return nquoteunitrate;
	}

	public void setNquoteunitrate(UFDouble nquoteunitrate) {
		this.nquoteunitrate = nquoteunitrate;
	}

	public UFDouble getNreplenishedastnum() {
		return nreplenishedastnum;
	}

	public void setNreplenishedastnum(UFDouble nreplenishedastnum) {
		this.nreplenishedastnum = nreplenishedastnum;
	}

	public UFDouble getNreplenishednum() {
		return nreplenishednum;
	}

	public void setNreplenishednum(UFDouble nreplenishednum) {
		this.nreplenishednum = nreplenishednum;
	}

	public UFDouble getNretastnum() {
		return nretastnum;
	}

	public void setNretastnum(UFDouble nretastnum) {
		this.nretastnum = nretastnum;
	}

	public UFDouble getNretgrossnum() {
		return nretgrossnum;
	}

	public void setNretgrossnum(UFDouble nretgrossnum) {
		this.nretgrossnum = nretgrossnum;
	}

	public UFDouble getNretnum() {
		return nretnum;
	}

	public void setNretnum(UFDouble nretnum) {
		this.nretnum = nretnum;
	}

	public UFDouble getNsalemny() {
		return nsalemny;
	}

	public void setNsalemny(UFDouble nsalemny) {
		this.nsalemny = nsalemny;
	}

	public UFDouble getNsaleprice() {
		return nsaleprice;
	}

	public void setNsaleprice(UFDouble nsaleprice) {
		this.nsaleprice = nsaleprice;
	}

	public UFDouble getNshouldinnum() {
		return nshouldinnum;
	}

	public void setNshouldinnum(UFDouble nshouldinnum) {
		this.nshouldinnum = nshouldinnum;
	}

	public UFDouble getNshouldoutassistnum() {
		return nshouldoutassistnum;
	}

	public void setNshouldoutassistnum(UFDouble nshouldoutassistnum) {
		this.nshouldoutassistnum = nshouldoutassistnum;
	}

	public UFDouble getNshouldoutnum() {
		return nshouldoutnum;
	}

	public void setNshouldoutnum(UFDouble nshouldoutnum) {
		this.nshouldoutnum = nshouldoutnum;
	}

	public UFDouble getNtarenum() {
		return ntarenum;
	}

	public void setNtarenum(UFDouble ntarenum) {
		this.ntarenum = ntarenum;
	}

	public UFDouble getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(UFDouble ntaxmny) {
		this.ntaxmny = ntaxmny;
	}

	public UFDouble getNtaxprice() {
		return ntaxprice;
	}

	public void setNtaxprice(UFDouble ntaxprice) {
		this.ntaxprice = ntaxprice;
	}

	public UFDouble getNtranoutastnum() {
		return ntranoutastnum;
	}

	public void setNtranoutastnum(UFDouble ntranoutastnum) {
		this.ntranoutastnum = ntranoutastnum;
	}

	public UFDouble getNtranoutnum() {
		return ntranoutnum;
	}

	public void setNtranoutnum(UFDouble ntranoutnum) {
		this.ntranoutnum = ntranoutnum;
	}

	public String getPk_bodycalbody() {
		return pk_bodycalbody;
	}

	public void setPk_bodycalbody(String pk_bodycalbody) {
		this.pk_bodycalbody = pk_bodycalbody;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_creqwareid() {
		return pk_creqwareid;
	}

	public void setPk_creqwareid(String pk_creqwareid) {
		this.pk_creqwareid = pk_creqwareid;
	}

	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}

	public void setPk_cubasdoc(String pk_cubasdoc) {
		this.pk_cubasdoc = pk_cubasdoc;
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

	public String getPk_invoicecorp() {
		return pk_invoicecorp;
	}

	public void setPk_invoicecorp(String pk_invoicecorp) {
		this.pk_invoicecorp = pk_invoicecorp;
	}

	public String getPk_measware() {
		return pk_measware;
	}

	public void setPk_measware(String pk_measware) {
		this.pk_measware = pk_measware;
	}

	public String getPk_packsort() {
		return pk_packsort;
	}

	public void setPk_packsort(String pk_packsort) {
		this.pk_packsort = pk_packsort;
	}

	public String getPk_reqcorp() {
		return pk_reqcorp;
	}

	public void setPk_reqcorp(String pk_reqcorp) {
		this.pk_reqcorp = pk_reqcorp;
	}

	public String getPk_reqstoorg() {
		return pk_reqstoorg;
	}

	public void setPk_reqstoorg(String pk_reqstoorg) {
		this.pk_reqstoorg = pk_reqstoorg;
	}

	public String getPk_returnreason() {
		return pk_returnreason;
	}

	public void setPk_returnreason(String pk_returnreason) {
		this.pk_returnreason = pk_returnreason;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getVbilltypeu8rm() {
		return vbilltypeu8rm;
	}

	public void setVbilltypeu8rm(String vbilltypeu8rm) {
		this.vbilltypeu8rm = vbilltypeu8rm;
	}

	public String getVbodynote2() {
		return vbodynote2;
	}

	public void setVbodynote2(String vbodynote2) {
		this.vbodynote2 = vbodynote2;
	}

	public String getVcorrespondrowno() {
		return vcorrespondrowno;
	}

	public void setVcorrespondrowno(String vcorrespondrowno) {
		this.vcorrespondrowno = vcorrespondrowno;
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

	public String getVfree10() {
		return vfree10;
	}

	public void setVfree10(String vfree10) {
		this.vfree10 = vfree10;
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

	public String getVfree6() {
		return vfree6;
	}

	public void setVfree6(String vfree6) {
		this.vfree6 = vfree6;
	}

	public String getVfree7() {
		return vfree7;
	}

	public void setVfree7(String vfree7) {
		this.vfree7 = vfree7;
	}

	public String getVfree8() {
		return vfree8;
	}

	public void setVfree8(String vfree8) {
		this.vfree8 = vfree8;
	}

	public String getVfree9() {
		return vfree9;
	}

	public void setVfree9(String vfree9) {
		this.vfree9 = vfree9;
	}

	public String getVnotebody() {
		return vnotebody;
	}

	public void setVnotebody(String vnotebody) {
		this.vnotebody = vnotebody;
	}

	public String getVproductbatch() {
		return vproductbatch;
	}

	public void setVproductbatch(String vproductbatch) {
		this.vproductbatch = vproductbatch;
	}

	public String getVreceiveaddress() {
		return vreceiveaddress;
	}

	public void setVreceiveaddress(String vreceiveaddress) {
		this.vreceiveaddress = vreceiveaddress;
	}

	public String getVsignwastcode() {
		return vsignwastcode;
	}

	public void setVsignwastcode(String vsignwastcode) {
		this.vsignwastcode = vsignwastcode;
	}

	public String getVsignwastrowno() {
		return vsignwastrowno;
	}

	public void setVsignwastrowno(String vsignwastrowno) {
		this.vsignwastrowno = vsignwastrowno;
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

	public String getVsourcewastcode() {
		return vsourcewastcode;
	}

	public void setVsourcewastcode(String vsourcewastcode) {
		this.vsourcewastcode = vsourcewastcode;
	}

	public String getVsourcewastrowno() {
		return vsourcewastrowno;
	}

	public void setVsourcewastrowno(String vsourcewastrowno) {
		this.vsourcewastrowno = vsourcewastrowno;
	}

	public String getVsrc2billcode() {
		return vsrc2billcode;
	}

	public void setVsrc2billcode(String vsrc2billcode) {
		this.vsrc2billcode = vsrc2billcode;
	}

	public String getVsrc2billrowno() {
		return vsrc2billrowno;
	}

	public void setVsrc2billrowno(String vsrc2billrowno) {
		this.vsrc2billrowno = vsrc2billrowno;
	}

	public String getVtransfercode() {
		return vtransfercode;
	}

	public void setVtransfercode(String vtransfercode) {
		this.vtransfercode = vtransfercode;
	}

	public String getVuserdef1() {
		return vuserdef1;
	}

	public void setVuserdef1(String vuserdef1) {
		this.vuserdef1 = vuserdef1;
	}

	public String getVuserdef10() {
		return vuserdef10;
	}

	public void setVuserdef10(String vuserdef10) {
		this.vuserdef10 = vuserdef10;
	}

	public String getVuserdef11() {
		return vuserdef11;
	}

	public void setVuserdef11(String vuserdef11) {
		this.vuserdef11 = vuserdef11;
	}

	public String getVuserdef12() {
		return vuserdef12;
	}

	public void setVuserdef12(String vuserdef12) {
		this.vuserdef12 = vuserdef12;
	}

	public String getVuserdef13() {
		return vuserdef13;
	}

	public void setVuserdef13(String vuserdef13) {
		this.vuserdef13 = vuserdef13;
	}

	public String getVuserdef14() {
		return vuserdef14;
	}

	public void setVuserdef14(String vuserdef14) {
		this.vuserdef14 = vuserdef14;
	}

	public String getVuserdef15() {
		return vuserdef15;
	}

	public void setVuserdef15(String vuserdef15) {
		this.vuserdef15 = vuserdef15;
	}

	public String getVuserdef16() {
		return vuserdef16;
	}

	public void setVuserdef16(String vuserdef16) {
		this.vuserdef16 = vuserdef16;
	}

	public String getVuserdef17() {
		return vuserdef17;
	}

	public void setVuserdef17(String vuserdef17) {
		this.vuserdef17 = vuserdef17;
	}

	public String getVuserdef18() {
		return vuserdef18;
	}

	public void setVuserdef18(String vuserdef18) {
		this.vuserdef18 = vuserdef18;
	}

	public String getVuserdef19() {
		return vuserdef19;
	}

	public void setVuserdef19(String vuserdef19) {
		this.vuserdef19 = vuserdef19;
	}

	public String getVuserdef2() {
		return vuserdef2;
	}

	public void setVuserdef2(String vuserdef2) {
		this.vuserdef2 = vuserdef2;
	}

	public String getVuserdef20() {
		return vuserdef20;
	}

	public void setVuserdef20(String vuserdef20) {
		this.vuserdef20 = vuserdef20;
	}

	public String getVuserdef3() {
		return vuserdef3;
	}

	public void setVuserdef3(String vuserdef3) {
		this.vuserdef3 = vuserdef3;
	}

	public String getVuserdef4() {
		return vuserdef4;
	}

	public void setVuserdef4(String vuserdef4) {
		this.vuserdef4 = vuserdef4;
	}

	public String getVuserdef5() {
		return vuserdef5;
	}

	public void setVuserdef5(String vuserdef5) {
		this.vuserdef5 = vuserdef5;
	}

	public String getVuserdef6() {
		return vuserdef6;
	}

	public void setVuserdef6(String vuserdef6) {
		this.vuserdef6 = vuserdef6;
	}

	public String getVuserdef7() {
		return vuserdef7;
	}

	public void setVuserdef7(String vuserdef7) {
		this.vuserdef7 = vuserdef7;
	}

	public String getVuserdef8() {
		return vuserdef8;
	}

	public void setVuserdef8(String vuserdef8) {
		this.vuserdef8 = vuserdef8;
	}

	public String getVuserdef9() {
		return vuserdef9;
	}

	public void setVuserdef9(String vuserdef9) {
		this.vuserdef9 = vuserdef9;
	}

	public String getVvehiclecode() {
		return vvehiclecode;
	}

	public void setVvehiclecode(String vvehiclecode) {
		this.vvehiclecode = vvehiclecode;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralbid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralhid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_b";
	}

}
