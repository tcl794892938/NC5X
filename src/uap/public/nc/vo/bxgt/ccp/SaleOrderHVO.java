package nc.vo.bxgt.ccp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class SaleOrderHVO extends SuperVO {
	
	private String bcooptopo;
	private String bfreecustflag;	
	private String binitflag;	
	private String binvoicendflag;
	private String boutendflag;	
	private String boverdate;		
	private String bpayendflag;	
	private String bpocooptome;	
	private String breceiptendflag;		
	private String bretinvflag;		
	private String btransendflag;
	private String capproveid;		
	private String cbaltypeid;		
	private String cbiztype;		
	private String ccalbodyid;		
	private String ccooppohid;		
	private String ccreditnum;		
	private String ccustbankid;		
	private String ccustomerid;		
	private String cdeptid;		
	private String cemployeeid;		
	private String cfreecustid;		
	private String coperatorid;		
	private String creceiptareaid;		
	private String creceiptcorpid;		
	private String creceiptcustomerid;		
	private String creceipttype;	
	private String csalecorpid;		
	private String csaleid;	
	private String ctermprotocolid;		
	private String ctransmodeid;		
	private String cwarehouseid;		
	private String dapprovedate;		
	private String daudittime;		
	private String dbilldate;		
	private String dbilltime;		
	private String dmakedate;		
	private String dmoditime;		
	private Integer dr=0;
	private String editauthor;		
	private String editdate;		
	private String editionnum;			
	private Integer finvoiceclass;		
	private Integer finvoicetype;		
	private Integer fstatus;		
	private Integer ibalanceflag;		
	private Integer iprintcount;		
	private UFDouble naccountperiod;			
	private UFDouble ndiscountrate;		
	private UFDouble nevaluatecarriage;		
	private UFDouble nheadsummny;			
	private UFDouble npreceivemny;		
	private UFDouble npreceiverate;		
	private UFDouble nsubscription;		
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
	private String vaccountyear;		
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
	private String veditreason;		
	private String vnote;			
	private String vreceiptcode;			
	private String vreceiveaddress;			


	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "csaleid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "so_sale";
	}

	public String getBcooptopo() {
		return bcooptopo;
	}

	public void setBcooptopo(String bcooptopo) {
		this.bcooptopo = bcooptopo;
	}

	public String getBfreecustflag() {
		return bfreecustflag;
	}

	public void setBfreecustflag(String bfreecustflag) {
		this.bfreecustflag = bfreecustflag;
	}

	public String getBinitflag() {
		return binitflag;
	}

	public void setBinitflag(String binitflag) {
		this.binitflag = binitflag;
	}

	public String getBinvoicendflag() {
		return binvoicendflag;
	}

	public void setBinvoicendflag(String binvoicendflag) {
		this.binvoicendflag = binvoicendflag;
	}

	public String getBoutendflag() {
		return boutendflag;
	}

	public void setBoutendflag(String boutendflag) {
		this.boutendflag = boutendflag;
	}

	public String getBoverdate() {
		return boverdate;
	}

	public void setBoverdate(String boverdate) {
		this.boverdate = boverdate;
	}

	public String getBpayendflag() {
		return bpayendflag;
	}

	public void setBpayendflag(String bpayendflag) {
		this.bpayendflag = bpayendflag;
	}

	public String getBpocooptome() {
		return bpocooptome;
	}

	public void setBpocooptome(String bpocooptome) {
		this.bpocooptome = bpocooptome;
	}

	public String getBreceiptendflag() {
		return breceiptendflag;
	}

	public void setBreceiptendflag(String breceiptendflag) {
		this.breceiptendflag = breceiptendflag;
	}

	public String getBretinvflag() {
		return bretinvflag;
	}

	public void setBretinvflag(String bretinvflag) {
		this.bretinvflag = bretinvflag;
	}

	public String getBtransendflag() {
		return btransendflag;
	}

	public void setBtransendflag(String btransendflag) {
		this.btransendflag = btransendflag;
	}

	public String getCapproveid() {
		return capproveid;
	}

	public void setCapproveid(String capproveid) {
		this.capproveid = capproveid;
	}

	public String getCbaltypeid() {
		return cbaltypeid;
	}

	public void setCbaltypeid(String cbaltypeid) {
		this.cbaltypeid = cbaltypeid;
	}

	public String getCbiztype() {
		return cbiztype;
	}

	public void setCbiztype(String cbiztype) {
		this.cbiztype = cbiztype;
	}

	public String getCcalbodyid() {
		return ccalbodyid;
	}

	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}

	public String getCcooppohid() {
		return ccooppohid;
	}

	public void setCcooppohid(String ccooppohid) {
		this.ccooppohid = ccooppohid;
	}

	public String getCcreditnum() {
		return ccreditnum;
	}

	public void setCcreditnum(String ccreditnum) {
		this.ccreditnum = ccreditnum;
	}

	public String getCcustbankid() {
		return ccustbankid;
	}

	public void setCcustbankid(String ccustbankid) {
		this.ccustbankid = ccustbankid;
	}

	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public String getCemployeeid() {
		return cemployeeid;
	}

	public void setCemployeeid(String cemployeeid) {
		this.cemployeeid = cemployeeid;
	}

	public String getCfreecustid() {
		return cfreecustid;
	}

	public void setCfreecustid(String cfreecustid) {
		this.cfreecustid = cfreecustid;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getCreceiptareaid() {
		return creceiptareaid;
	}

	public void setCreceiptareaid(String creceiptareaid) {
		this.creceiptareaid = creceiptareaid;
	}

	public String getCreceiptcorpid() {
		return creceiptcorpid;
	}

	public void setCreceiptcorpid(String creceiptcorpid) {
		this.creceiptcorpid = creceiptcorpid;
	}

	public String getCreceiptcustomerid() {
		return creceiptcustomerid;
	}

	public void setCreceiptcustomerid(String creceiptcustomerid) {
		this.creceiptcustomerid = creceiptcustomerid;
	}

	public String getCreceipttype() {
		return creceipttype;
	}

	public void setCreceipttype(String creceipttype) {
		this.creceipttype = creceipttype;
	}

	public String getCsalecorpid() {
		return csalecorpid;
	}

	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}

	public String getCtermprotocolid() {
		return ctermprotocolid;
	}

	public void setCtermprotocolid(String ctermprotocolid) {
		this.ctermprotocolid = ctermprotocolid;
	}

	public String getCtransmodeid() {
		return ctransmodeid;
	}

	public void setCtransmodeid(String ctransmodeid) {
		this.ctransmodeid = ctransmodeid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getDapprovedate() {
		return dapprovedate;
	}

	public void setDapprovedate(String dapprovedate) {
		this.dapprovedate = dapprovedate;
	}

	public String getDaudittime() {
		return daudittime;
	}

	public void setDaudittime(String daudittime) {
		this.daudittime = daudittime;
	}

	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getDbilltime() {
		return dbilltime;
	}

	public void setDbilltime(String dbilltime) {
		this.dbilltime = dbilltime;
	}

	public String getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(String dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getDmoditime() {
		return dmoditime;
	}

	public void setDmoditime(String dmoditime) {
		this.dmoditime = dmoditime;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getEditauthor() {
		return editauthor;
	}

	public void setEditauthor(String editauthor) {
		this.editauthor = editauthor;
	}

	public String getEditdate() {
		return editdate;
	}

	public void setEditdate(String editdate) {
		this.editdate = editdate;
	}

	public String getEditionnum() {
		return editionnum;
	}

	public void setEditionnum(String editionnum) {
		this.editionnum = editionnum;
	}

	public Integer getFinvoiceclass() {
		return finvoiceclass;
	}

	public void setFinvoiceclass(Integer finvoiceclass) {
		this.finvoiceclass = finvoiceclass;
	}

	public Integer getFinvoicetype() {
		return finvoicetype;
	}

	public void setFinvoicetype(Integer finvoicetype) {
		this.finvoicetype = finvoicetype;
	}

	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	public Integer getIbalanceflag() {
		return ibalanceflag;
	}

	public void setIbalanceflag(Integer ibalanceflag) {
		this.ibalanceflag = ibalanceflag;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public UFDouble getNaccountperiod() {
		return naccountperiod;
	}

	public void setNaccountperiod(UFDouble naccountperiod) {
		this.naccountperiod = naccountperiod;
	}

	public UFDouble getNdiscountrate() {
		return ndiscountrate;
	}

	public void setNdiscountrate(UFDouble ndiscountrate) {
		this.ndiscountrate = ndiscountrate;
	}

	public UFDouble getNevaluatecarriage() {
		return nevaluatecarriage;
	}

	public void setNevaluatecarriage(UFDouble nevaluatecarriage) {
		this.nevaluatecarriage = nevaluatecarriage;
	}

	public UFDouble getNheadsummny() {
		return nheadsummny;
	}

	public void setNheadsummny(UFDouble nheadsummny) {
		this.nheadsummny = nheadsummny;
	}

	public UFDouble getNpreceivemny() {
		return npreceivemny;
	}

	public void setNpreceivemny(UFDouble npreceivemny) {
		this.npreceivemny = npreceivemny;
	}

	public UFDouble getNpreceiverate() {
		return npreceiverate;
	}

	public void setNpreceiverate(UFDouble npreceiverate) {
		this.npreceiverate = npreceiverate;
	}

	public UFDouble getNsubscription() {
		return nsubscription;
	}

	public void setNsubscription(UFDouble nsubscription) {
		this.nsubscription = nsubscription;
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

	public String getVaccountyear() {
		return vaccountyear;
	}

	public void setVaccountyear(String vaccountyear) {
		this.vaccountyear = vaccountyear;
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

	public String getVeditreason() {
		return veditreason;
	}

	public void setVeditreason(String veditreason) {
		this.veditreason = veditreason;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	public String getVreceiptcode() {
		return vreceiptcode;
	}

	public void setVreceiptcode(String vreceiptcode) {
		this.vreceiptcode = vreceiptcode;
	}

	public String getVreceiveaddress() {
		return vreceiveaddress;
	}

	public void setVreceiveaddress(String vreceiveaddress) {
		this.vreceiveaddress = vreceiveaddress;
	}

}
