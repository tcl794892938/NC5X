package nc.vo.arap.sedgather.sale;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class SaleBXBVO extends SuperVO {
	
	private String bbindflag;
	private String bdericttrans;
	private String blargessflag;
	private String boosflag;
	private String breturnprofit;
	private String bsafeprice;
	private String bsupplyflag;
	private String cadvisecalbodyid;
	private String cbatchid;
	private String cbodywarehouseid;
	private String cbomorderid;
	private String cchantypeid;
	private String cconsigncorpid;
	private String ccurrencytypeid;
	private String cfactoryid;
	private String cfreezeid;
	private String cinvbasdocid;
	private String cinventoryid;
	private String cinventoryid1;
	private String clargessrowno;
	private String coperatorid;
	private String corder_bid;
	private String cpackunitid;
	private String cpricecalproc;
	private String cpriceitemid;
	private String cpriceitemtable;
	private String cpricepolicyid;
	private String cprolineid;
	private String cproviderid;
	private String cquoteunitid;
	private String crecaddrnode;
	private String creccalbodyid;
	private String creceiptareaid;
	private String creceiptcorpid;
	private String creceipttype;
	private String crecwareid;
	private String crowno;
	private String csaleid;
	private String csourcebillbodyid;
	private String csourcebillid;
	private String ct_managebid;
	private String ct_manageid;
	private String cunitid;
	private String dconsigndate;
	private String ddeliverdate;
	private Integer dr=0;
	private Integer fbatchstatus;
	private String frownote;
	private Integer frowstatus;
	private UFDouble nastretprofnum;
	private UFDouble nasttaldcnum;
	private UFDouble ndiscountmny;
	private UFDouble ndiscountrate;
	private UFDouble nexchangeotobrate;
	private UFDouble nitemdiscountrate;
	private UFDouble nmny;
	private UFDouble nnetprice;
	private UFDouble nnumber;
	private UFDouble norgqtnetprc;
	private UFDouble norgqtprc;
	private UFDouble norgqttaxnetprc;
	private UFDouble norgqttaxprc;
	private UFDouble noriginalcurdiscountmny;
	private UFDouble noriginalcurmny;
	private UFDouble noriginalcurnetprice;
	private UFDouble noriginalcurprice;
	private UFDouble noriginalcursummny;
	private UFDouble noriginalcurtaxmny;
	private UFDouble noriginalcurtaxnetprice;
	private UFDouble noriginalcurtaxprice;
	private UFDouble npacknumber;
	private UFDouble nprice;
	private UFDouble nqtnetprc;
	private UFDouble nqtorgnetprc;
	private UFDouble nqtorgprc;
	private UFDouble nqtorgtaxnetprc;
	private UFDouble nqtorgtaxprc;
	private UFDouble nqtprc;
	private UFDouble nqttaxnetprc;
	private UFDouble nqttaxprc;
	private UFDouble nquoteunitnum;
	private UFDouble nquoteunitrate;
	private UFDouble nretprofmny;
	private UFDouble nretprofnum;
	private UFDouble nreturntaxrate;
	private UFDouble nsummny;
	private UFDouble ntaldcmny;
	private UFDouble ntaldcnum;
	private UFDouble ntaxmny;
	private UFDouble ntaxnetprice;
	private UFDouble ntaxprice;
	private UFDouble ntaxrate;
	private String pk_corp;
	private String tconsigntime;
	private String tdelivertime;
	private UFDateTime ts;
	private String veditreason;
	private String vreceiveaddress;


	public String getBbindflag() {
		return bbindflag;
	}

	public void setBbindflag(String bbindflag) {
		this.bbindflag = bbindflag;
	}

	public String getBdericttrans() {
		return bdericttrans;
	}

	public void setBdericttrans(String bdericttrans) {
		this.bdericttrans = bdericttrans;
	}

	public String getBlargessflag() {
		return blargessflag;
	}

	public void setBlargessflag(String blargessflag) {
		this.blargessflag = blargessflag;
	}

	public String getBoosflag() {
		return boosflag;
	}

	public void setBoosflag(String boosflag) {
		this.boosflag = boosflag;
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

	public String getBsupplyflag() {
		return bsupplyflag;
	}

	public void setBsupplyflag(String bsupplyflag) {
		this.bsupplyflag = bsupplyflag;
	}

	public String getCadvisecalbodyid() {
		return cadvisecalbodyid;
	}

	public void setCadvisecalbodyid(String cadvisecalbodyid) {
		this.cadvisecalbodyid = cadvisecalbodyid;
	}

	public String getCbatchid() {
		return cbatchid;
	}

	public void setCbatchid(String cbatchid) {
		this.cbatchid = cbatchid;
	}

	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}

	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}

	public String getCbomorderid() {
		return cbomorderid;
	}

	public void setCbomorderid(String cbomorderid) {
		this.cbomorderid = cbomorderid;
	}

	public String getCchantypeid() {
		return cchantypeid;
	}

	public void setCchantypeid(String cchantypeid) {
		this.cchantypeid = cchantypeid;
	}

	public String getCconsigncorpid() {
		return cconsigncorpid;
	}

	public void setCconsigncorpid(String cconsigncorpid) {
		this.cconsigncorpid = cconsigncorpid;
	}

	public String getCcurrencytypeid() {
		return ccurrencytypeid;
	}

	public void setCcurrencytypeid(String ccurrencytypeid) {
		this.ccurrencytypeid = ccurrencytypeid;
	}

	public String getCfactoryid() {
		return cfactoryid;
	}

	public void setCfactoryid(String cfactoryid) {
		this.cfactoryid = cfactoryid;
	}

	public String getCfreezeid() {
		return cfreezeid;
	}

	public void setCfreezeid(String cfreezeid) {
		this.cfreezeid = cfreezeid;
	}

	public String getCinvbasdocid() {
		return cinvbasdocid;
	}

	public void setCinvbasdocid(String cinvbasdocid) {
		this.cinvbasdocid = cinvbasdocid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getCinventoryid1() {
		return cinventoryid1;
	}

	public void setCinventoryid1(String cinventoryid1) {
		this.cinventoryid1 = cinventoryid1;
	}

	public String getClargessrowno() {
		return clargessrowno;
	}

	public void setClargessrowno(String clargessrowno) {
		this.clargessrowno = clargessrowno;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getCorder_bid() {
		return corder_bid;
	}

	public void setCorder_bid(String corder_bid) {
		this.corder_bid = corder_bid;
	}

	public String getCpackunitid() {
		return cpackunitid;
	}

	public void setCpackunitid(String cpackunitid) {
		this.cpackunitid = cpackunitid;
	}

	public String getCpricecalproc() {
		return cpricecalproc;
	}

	public void setCpricecalproc(String cpricecalproc) {
		this.cpricecalproc = cpricecalproc;
	}

	public String getCpriceitemid() {
		return cpriceitemid;
	}

	public void setCpriceitemid(String cpriceitemid) {
		this.cpriceitemid = cpriceitemid;
	}

	public String getCpriceitemtable() {
		return cpriceitemtable;
	}

	public void setCpriceitemtable(String cpriceitemtable) {
		this.cpriceitemtable = cpriceitemtable;
	}

	public String getCpricepolicyid() {
		return cpricepolicyid;
	}

	public void setCpricepolicyid(String cpricepolicyid) {
		this.cpricepolicyid = cpricepolicyid;
	}

	public String getCprolineid() {
		return cprolineid;
	}

	public void setCprolineid(String cprolineid) {
		this.cprolineid = cprolineid;
	}

	public String getCproviderid() {
		return cproviderid;
	}

	public void setCproviderid(String cproviderid) {
		this.cproviderid = cproviderid;
	}

	public String getCquoteunitid() {
		return cquoteunitid;
	}

	public void setCquoteunitid(String cquoteunitid) {
		this.cquoteunitid = cquoteunitid;
	}

	public String getCrecaddrnode() {
		return crecaddrnode;
	}

	public void setCrecaddrnode(String crecaddrnode) {
		this.crecaddrnode = crecaddrnode;
	}

	public String getCreccalbodyid() {
		return creccalbodyid;
	}

	public void setCreccalbodyid(String creccalbodyid) {
		this.creccalbodyid = creccalbodyid;
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

	public String getCreceipttype() {
		return creceipttype;
	}

	public void setCreceipttype(String creceipttype) {
		this.creceipttype = creceipttype;
	}

	public String getCrecwareid() {
		return crecwareid;
	}

	public void setCrecwareid(String crecwareid) {
		this.crecwareid = crecwareid;
	}

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}

	public String getCsourcebillbodyid() {
		return csourcebillbodyid;
	}

	public void setCsourcebillbodyid(String csourcebillbodyid) {
		this.csourcebillbodyid = csourcebillbodyid;
	}

	public String getCsourcebillid() {
		return csourcebillid;
	}

	public void setCsourcebillid(String csourcebillid) {
		this.csourcebillid = csourcebillid;
	}

	public String getCt_managebid() {
		return ct_managebid;
	}

	public void setCt_managebid(String ct_managebid) {
		this.ct_managebid = ct_managebid;
	}

	public String getCt_manageid() {
		return ct_manageid;
	}

	public void setCt_manageid(String ct_manageid) {
		this.ct_manageid = ct_manageid;
	}

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getDconsigndate() {
		return dconsigndate;
	}

	public void setDconsigndate(String dconsigndate) {
		this.dconsigndate = dconsigndate;
	}

	public String getDdeliverdate() {
		return ddeliverdate;
	}

	public void setDdeliverdate(String ddeliverdate) {
		this.ddeliverdate = ddeliverdate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getFbatchstatus() {
		return fbatchstatus;
	}

	public void setFbatchstatus(Integer fbatchstatus) {
		this.fbatchstatus = fbatchstatus;
	}

	public String getFrownote() {
		return frownote;
	}

	public void setFrownote(String frownote) {
		this.frownote = frownote;
	}

	public Integer getFrowstatus() {
		return frowstatus;
	}

	public void setFrowstatus(Integer frowstatus) {
		this.frowstatus = frowstatus;
	}

	public UFDouble getNastretprofnum() {
		return nastretprofnum;
	}

	public void setNastretprofnum(UFDouble nastretprofnum) {
		this.nastretprofnum = nastretprofnum;
	}

	public UFDouble getNasttaldcnum() {
		return nasttaldcnum;
	}

	public void setNasttaldcnum(UFDouble nasttaldcnum) {
		this.nasttaldcnum = nasttaldcnum;
	}

	public UFDouble getNdiscountmny() {
		return ndiscountmny;
	}

	public void setNdiscountmny(UFDouble ndiscountmny) {
		this.ndiscountmny = ndiscountmny;
	}

	public UFDouble getNdiscountrate() {
		return ndiscountrate;
	}

	public void setNdiscountrate(UFDouble ndiscountrate) {
		this.ndiscountrate = ndiscountrate;
	}

	public UFDouble getNexchangeotobrate() {
		return nexchangeotobrate;
	}

	public void setNexchangeotobrate(UFDouble nexchangeotobrate) {
		this.nexchangeotobrate = nexchangeotobrate;
	}

	public UFDouble getNitemdiscountrate() {
		return nitemdiscountrate;
	}

	public void setNitemdiscountrate(UFDouble nitemdiscountrate) {
		this.nitemdiscountrate = nitemdiscountrate;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public UFDouble getNnetprice() {
		return nnetprice;
	}

	public void setNnetprice(UFDouble nnetprice) {
		this.nnetprice = nnetprice;
	}

	public UFDouble getNnumber() {
		return nnumber;
	}

	public void setNnumber(UFDouble nnumber) {
		this.nnumber = nnumber;
	}

	public UFDouble getNorgqtnetprc() {
		return norgqtnetprc;
	}

	public void setNorgqtnetprc(UFDouble norgqtnetprc) {
		this.norgqtnetprc = norgqtnetprc;
	}

	public UFDouble getNorgqtprc() {
		return norgqtprc;
	}

	public void setNorgqtprc(UFDouble norgqtprc) {
		this.norgqtprc = norgqtprc;
	}

	public UFDouble getNorgqttaxnetprc() {
		return norgqttaxnetprc;
	}

	public void setNorgqttaxnetprc(UFDouble norgqttaxnetprc) {
		this.norgqttaxnetprc = norgqttaxnetprc;
	}

	public UFDouble getNorgqttaxprc() {
		return norgqttaxprc;
	}

	public void setNorgqttaxprc(UFDouble norgqttaxprc) {
		this.norgqttaxprc = norgqttaxprc;
	}

	public UFDouble getNoriginalcurdiscountmny() {
		return noriginalcurdiscountmny;
	}

	public void setNoriginalcurdiscountmny(UFDouble noriginalcurdiscountmny) {
		this.noriginalcurdiscountmny = noriginalcurdiscountmny;
	}

	public UFDouble getNoriginalcurmny() {
		return noriginalcurmny;
	}

	public void setNoriginalcurmny(UFDouble noriginalcurmny) {
		this.noriginalcurmny = noriginalcurmny;
	}

	public UFDouble getNoriginalcurnetprice() {
		return noriginalcurnetprice;
	}

	public void setNoriginalcurnetprice(UFDouble noriginalcurnetprice) {
		this.noriginalcurnetprice = noriginalcurnetprice;
	}

	public UFDouble getNoriginalcurprice() {
		return noriginalcurprice;
	}

	public void setNoriginalcurprice(UFDouble noriginalcurprice) {
		this.noriginalcurprice = noriginalcurprice;
	}

	public UFDouble getNoriginalcursummny() {
		return noriginalcursummny;
	}

	public void setNoriginalcursummny(UFDouble noriginalcursummny) {
		this.noriginalcursummny = noriginalcursummny;
	}

	public UFDouble getNoriginalcurtaxmny() {
		return noriginalcurtaxmny;
	}

	public void setNoriginalcurtaxmny(UFDouble noriginalcurtaxmny) {
		this.noriginalcurtaxmny = noriginalcurtaxmny;
	}

	public UFDouble getNoriginalcurtaxnetprice() {
		return noriginalcurtaxnetprice;
	}

	public void setNoriginalcurtaxnetprice(UFDouble noriginalcurtaxnetprice) {
		this.noriginalcurtaxnetprice = noriginalcurtaxnetprice;
	}

	public UFDouble getNoriginalcurtaxprice() {
		return noriginalcurtaxprice;
	}

	public void setNoriginalcurtaxprice(UFDouble noriginalcurtaxprice) {
		this.noriginalcurtaxprice = noriginalcurtaxprice;
	}

	public UFDouble getNpacknumber() {
		return npacknumber;
	}

	public void setNpacknumber(UFDouble npacknumber) {
		this.npacknumber = npacknumber;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public UFDouble getNqtnetprc() {
		return nqtnetprc;
	}

	public void setNqtnetprc(UFDouble nqtnetprc) {
		this.nqtnetprc = nqtnetprc;
	}

	public UFDouble getNqtorgnetprc() {
		return nqtorgnetprc;
	}

	public void setNqtorgnetprc(UFDouble nqtorgnetprc) {
		this.nqtorgnetprc = nqtorgnetprc;
	}

	public UFDouble getNqtorgprc() {
		return nqtorgprc;
	}

	public void setNqtorgprc(UFDouble nqtorgprc) {
		this.nqtorgprc = nqtorgprc;
	}

	public UFDouble getNqtorgtaxnetprc() {
		return nqtorgtaxnetprc;
	}

	public void setNqtorgtaxnetprc(UFDouble nqtorgtaxnetprc) {
		this.nqtorgtaxnetprc = nqtorgtaxnetprc;
	}

	public UFDouble getNqtorgtaxprc() {
		return nqtorgtaxprc;
	}

	public void setNqtorgtaxprc(UFDouble nqtorgtaxprc) {
		this.nqtorgtaxprc = nqtorgtaxprc;
	}

	public UFDouble getNqtprc() {
		return nqtprc;
	}

	public void setNqtprc(UFDouble nqtprc) {
		this.nqtprc = nqtprc;
	}

	public UFDouble getNqttaxnetprc() {
		return nqttaxnetprc;
	}

	public void setNqttaxnetprc(UFDouble nqttaxnetprc) {
		this.nqttaxnetprc = nqttaxnetprc;
	}

	public UFDouble getNqttaxprc() {
		return nqttaxprc;
	}

	public void setNqttaxprc(UFDouble nqttaxprc) {
		this.nqttaxprc = nqttaxprc;
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

	public UFDouble getNretprofmny() {
		return nretprofmny;
	}

	public void setNretprofmny(UFDouble nretprofmny) {
		this.nretprofmny = nretprofmny;
	}

	public UFDouble getNretprofnum() {
		return nretprofnum;
	}

	public void setNretprofnum(UFDouble nretprofnum) {
		this.nretprofnum = nretprofnum;
	}

	public UFDouble getNreturntaxrate() {
		return nreturntaxrate;
	}

	public void setNreturntaxrate(UFDouble nreturntaxrate) {
		this.nreturntaxrate = nreturntaxrate;
	}

	public UFDouble getNsummny() {
		return nsummny;
	}

	public void setNsummny(UFDouble nsummny) {
		this.nsummny = nsummny;
	}

	public UFDouble getNtaldcmny() {
		return ntaldcmny;
	}

	public void setNtaldcmny(UFDouble ntaldcmny) {
		this.ntaldcmny = ntaldcmny;
	}

	public UFDouble getNtaldcnum() {
		return ntaldcnum;
	}

	public void setNtaldcnum(UFDouble ntaldcnum) {
		this.ntaldcnum = ntaldcnum;
	}

	public UFDouble getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(UFDouble ntaxmny) {
		this.ntaxmny = ntaxmny;
	}

	public UFDouble getNtaxnetprice() {
		return ntaxnetprice;
	}

	public void setNtaxnetprice(UFDouble ntaxnetprice) {
		this.ntaxnetprice = ntaxnetprice;
	}

	public UFDouble getNtaxprice() {
		return ntaxprice;
	}

	public void setNtaxprice(UFDouble ntaxprice) {
		this.ntaxprice = ntaxprice;
	}

	public UFDouble getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(UFDouble ntaxrate) {
		this.ntaxrate = ntaxrate;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getTconsigntime() {
		return tconsigntime;
	}

	public void setTconsigntime(String tconsigntime) {
		this.tconsigntime = tconsigntime;
	}

	public String getTdelivertime() {
		return tdelivertime;
	}

	public void setTdelivertime(String tdelivertime) {
		this.tdelivertime = tdelivertime;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getVeditreason() {
		return veditreason;
	}

	public void setVeditreason(String veditreason) {
		this.veditreason = veditreason;
	}

	public String getVreceiveaddress() {
		return vreceiveaddress;
	}

	public void setVreceiveaddress(String vreceiveaddress) {
		this.vreceiveaddress = vreceiveaddress;
	}

	@Override
	public String getPKFieldName() {
		return "corder_bid";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "so_saleorder_b";
	}

}
