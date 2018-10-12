package nc.vo.bxgt.basedoc;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class ProduceVO extends SuperVO {
	
	private String abcfl;		
	private String abcfundeg;			
	private String abcgrosspft;			
	private String abcpurchase;			
	private String abssales;			
	private UFDouble accquiretime;		
	private UFDouble aheadbatch;			
	private UFDouble aheadcoff;		
	private UFDouble batchincrnum;		
	private UFDouble batchnum;	
	private Integer batchperiodnum;		
	private String batchrule;	
	private UFDouble blgdsczkxs;		
	private Integer bomtype;	
	private String cgzz;	
	private String chkfreeflag;	
	private UFDouble chngamount;		
	private UFDouble ckcb;			
	private UFDouble cksj;			
	private String combineflag;		
	private UFDouble confirmtime;		
	private String converseflag;			
	private String createtime;		
	private String creator;		
	private UFDouble currentamount;		
	private UFDouble datumofsend;			
	private String def1;			
	private String def2;		
	private String def3;		
	private String def4;			
	private String def5;			
	private Integer dr;		
	private UFDouble ecobatch;		
	private UFDouble endahead;		
	private Integer fcpclgsfa;		
	private String fgys;		
	private UFDouble fixedahead;		
	private String fixperiodbegin;		
	private UFDouble flanlennum;		
	private Integer flanmadenum;		
	private UFDouble flanwidenum;		
	private String gfwlbm;			
	private UFDouble grosswtnum;		
	private String iscancalculatedinvcost;		
	private String iscostbyorder;		
	private String iscreatesonprodorder;		
	private String isctlbyfixperiod;		
	private String isctlbyprimarycode;	
	private String isctlbysecondarycode;	
	private String isctoutput;	
	private String iselementcheck;		
	private String isfatherofbom;			
	private String isfxjz;	
	private String issend;	
	private String issendbydatum;		
	private String isused;		
	private String isuseroute;	
	private String iswholesetsend;	
	private UFDouble jhj;		
	private Integer jyrhzdyw;	
	private Integer lowlevelcode;	
	private UFDouble lowstocknum;			
	private Integer materclass;		
	private Integer materstate;			
	private String matertype;		
	private UFDouble maxstornum;	
	private UFDouble minbatchnum;		
	private UFDouble minmulnum;	
	private String modifier;			
	private String modifytime;		
	private UFDouble nbzyj;		
	private UFDouble netwtnum;		
	private UFDouble nyzbmxs;		
	private Integer outnumhistorydays;		
	private String outtype;		
	private Integer pchscscd;	
	private String pk_calbody;			
	private String pk_ckjlcid;			
	private String pk_corp;			
	private String pk_deptdoc3;			
	private String pk_invbasdoc;		
	private String pk_invmandoc;		
	private String pk_produce;			
	private String pk_psndoc3;		
	private String pk_psndoc4;			
	private String pk_rkjlcid;			
	private String pk_sealuser;			
	private String pk_stordoc;			
	private UFDouble prevahead;		
	private Integer pricemethod;		
	private String primaryflag;			
	private UFDouble primnessnum;			
	private Integer producemethod;			
	private UFDouble rationwtnum;			
	private UFDouble realusableamount;			
	private Integer roadtype;		
	private UFDouble roundingnum;			
	private UFDouble safetystocknum;			
	private String scheattr;		
	private UFDouble scpl;		
	private Integer scscddms;	
	private String scxybzsfzk;	
	private String sealdate;		
	private String sealflag;			
	private String sfbj;			
	private String sfcbdx;			
	private String sffzfw;	
	private String sfpchs;	
	private String sfscx;		
	private String sfzb;	
	private String sfzzcp;		
	private String stockbycheck;	
	private Integer stocklowerdays;		
	private Integer stockupperdays;			
	private UFDouble sumahead;		
	private String supplytype;		
	private String ts;		
	private String usableamount;	
	private String usableamountbyfree;		
	private String virtualflag;			
	private UFDouble wasterrate;		
	private UFDouble wggdsczkxs;			
	private Integer wghxcl;		
	private String ybgys;		
	private String zbczjyxm;			
	private UFDouble zbxs;		
	private UFDouble zdhd;			
	private String zgys;		


	@Override
	public String getPKFieldName() {
		return "pk_produce";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_produce";
	}

	@Override
	public String getTableName() {
		return "bd_produce";
	}

	public String getAbcfl() {
		return abcfl;
	}

	public void setAbcfl(String abcfl) {
		this.abcfl = abcfl;
	}

	public String getAbcfundeg() {
		return abcfundeg;
	}

	public void setAbcfundeg(String abcfundeg) {
		this.abcfundeg = abcfundeg;
	}

	public String getAbcgrosspft() {
		return abcgrosspft;
	}

	public void setAbcgrosspft(String abcgrosspft) {
		this.abcgrosspft = abcgrosspft;
	}

	public String getAbcpurchase() {
		return abcpurchase;
	}

	public void setAbcpurchase(String abcpurchase) {
		this.abcpurchase = abcpurchase;
	}

	public String getAbssales() {
		return abssales;
	}

	public void setAbssales(String abssales) {
		this.abssales = abssales;
	}

	public UFDouble getAccquiretime() {
		return accquiretime;
	}

	public void setAccquiretime(UFDouble accquiretime) {
		this.accquiretime = accquiretime;
	}

	public UFDouble getAheadbatch() {
		return aheadbatch;
	}

	public void setAheadbatch(UFDouble aheadbatch) {
		this.aheadbatch = aheadbatch;
	}

	public UFDouble getAheadcoff() {
		return aheadcoff;
	}

	public void setAheadcoff(UFDouble aheadcoff) {
		this.aheadcoff = aheadcoff;
	}

	public UFDouble getBatchincrnum() {
		return batchincrnum;
	}

	public void setBatchincrnum(UFDouble batchincrnum) {
		this.batchincrnum = batchincrnum;
	}

	public UFDouble getBatchnum() {
		return batchnum;
	}

	public void setBatchnum(UFDouble batchnum) {
		this.batchnum = batchnum;
	}

	public Integer getBatchperiodnum() {
		return batchperiodnum;
	}

	public void setBatchperiodnum(Integer batchperiodnum) {
		this.batchperiodnum = batchperiodnum;
	}

	public String getBatchrule() {
		return batchrule;
	}

	public void setBatchrule(String batchrule) {
		this.batchrule = batchrule;
	}

	public UFDouble getBlgdsczkxs() {
		return blgdsczkxs;
	}

	public void setBlgdsczkxs(UFDouble blgdsczkxs) {
		this.blgdsczkxs = blgdsczkxs;
	}

	public Integer getBomtype() {
		return bomtype;
	}

	public void setBomtype(Integer bomtype) {
		this.bomtype = bomtype;
	}

	public String getCgzz() {
		return cgzz;
	}

	public void setCgzz(String cgzz) {
		this.cgzz = cgzz;
	}

	public String getChkfreeflag() {
		return chkfreeflag;
	}

	public void setChkfreeflag(String chkfreeflag) {
		this.chkfreeflag = chkfreeflag;
	}

	public UFDouble getChngamount() {
		return chngamount;
	}

	public void setChngamount(UFDouble chngamount) {
		this.chngamount = chngamount;
	}

	public UFDouble getCkcb() {
		return ckcb;
	}

	public void setCkcb(UFDouble ckcb) {
		this.ckcb = ckcb;
	}

	public UFDouble getCksj() {
		return cksj;
	}

	public void setCksj(UFDouble cksj) {
		this.cksj = cksj;
	}

	public String getCombineflag() {
		return combineflag;
	}

	public void setCombineflag(String combineflag) {
		this.combineflag = combineflag;
	}

	public UFDouble getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(UFDouble confirmtime) {
		this.confirmtime = confirmtime;
	}

	public String getConverseflag() {
		return converseflag;
	}

	public void setConverseflag(String converseflag) {
		this.converseflag = converseflag;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public UFDouble getCurrentamount() {
		return currentamount;
	}

	public void setCurrentamount(UFDouble currentamount) {
		this.currentamount = currentamount;
	}

	public UFDouble getDatumofsend() {
		return datumofsend;
	}

	public void setDatumofsend(UFDouble datumofsend) {
		this.datumofsend = datumofsend;
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

	public UFDouble getEcobatch() {
		return ecobatch;
	}

	public void setEcobatch(UFDouble ecobatch) {
		this.ecobatch = ecobatch;
	}

	public UFDouble getEndahead() {
		return endahead;
	}

	public void setEndahead(UFDouble endahead) {
		this.endahead = endahead;
	}

	public Integer getFcpclgsfa() {
		return fcpclgsfa;
	}

	public void setFcpclgsfa(Integer fcpclgsfa) {
		this.fcpclgsfa = fcpclgsfa;
	}

	public String getFgys() {
		return fgys;
	}

	public void setFgys(String fgys) {
		this.fgys = fgys;
	}

	public UFDouble getFixedahead() {
		return fixedahead;
	}

	public void setFixedahead(UFDouble fixedahead) {
		this.fixedahead = fixedahead;
	}

	public String getFixperiodbegin() {
		return fixperiodbegin;
	}

	public void setFixperiodbegin(String fixperiodbegin) {
		this.fixperiodbegin = fixperiodbegin;
	}

	public UFDouble getFlanlennum() {
		return flanlennum;
	}

	public void setFlanlennum(UFDouble flanlennum) {
		this.flanlennum = flanlennum;
	}

	public Integer getFlanmadenum() {
		return flanmadenum;
	}

	public void setFlanmadenum(Integer flanmadenum) {
		this.flanmadenum = flanmadenum;
	}

	public UFDouble getFlanwidenum() {
		return flanwidenum;
	}

	public void setFlanwidenum(UFDouble flanwidenum) {
		this.flanwidenum = flanwidenum;
	}

	public String getGfwlbm() {
		return gfwlbm;
	}

	public void setGfwlbm(String gfwlbm) {
		this.gfwlbm = gfwlbm;
	}

	public UFDouble getGrosswtnum() {
		return grosswtnum;
	}

	public void setGrosswtnum(UFDouble grosswtnum) {
		this.grosswtnum = grosswtnum;
	}

	public String getIscancalculatedinvcost() {
		return iscancalculatedinvcost;
	}

	public void setIscancalculatedinvcost(String iscancalculatedinvcost) {
		this.iscancalculatedinvcost = iscancalculatedinvcost;
	}

	public String getIscostbyorder() {
		return iscostbyorder;
	}

	public void setIscostbyorder(String iscostbyorder) {
		this.iscostbyorder = iscostbyorder;
	}

	public String getIscreatesonprodorder() {
		return iscreatesonprodorder;
	}

	public void setIscreatesonprodorder(String iscreatesonprodorder) {
		this.iscreatesonprodorder = iscreatesonprodorder;
	}

	public String getIsctlbyfixperiod() {
		return isctlbyfixperiod;
	}

	public void setIsctlbyfixperiod(String isctlbyfixperiod) {
		this.isctlbyfixperiod = isctlbyfixperiod;
	}

	public String getIsctlbyprimarycode() {
		return isctlbyprimarycode;
	}

	public void setIsctlbyprimarycode(String isctlbyprimarycode) {
		this.isctlbyprimarycode = isctlbyprimarycode;
	}

	public String getIsctlbysecondarycode() {
		return isctlbysecondarycode;
	}

	public void setIsctlbysecondarycode(String isctlbysecondarycode) {
		this.isctlbysecondarycode = isctlbysecondarycode;
	}

	public String getIsctoutput() {
		return isctoutput;
	}

	public void setIsctoutput(String isctoutput) {
		this.isctoutput = isctoutput;
	}

	public String getIselementcheck() {
		return iselementcheck;
	}

	public void setIselementcheck(String iselementcheck) {
		this.iselementcheck = iselementcheck;
	}

	public String getIsfatherofbom() {
		return isfatherofbom;
	}

	public void setIsfatherofbom(String isfatherofbom) {
		this.isfatherofbom = isfatherofbom;
	}

	public String getIsfxjz() {
		return isfxjz;
	}

	public void setIsfxjz(String isfxjz) {
		this.isfxjz = isfxjz;
	}

	public String getIssend() {
		return issend;
	}

	public void setIssend(String issend) {
		this.issend = issend;
	}

	public String getIssendbydatum() {
		return issendbydatum;
	}

	public void setIssendbydatum(String issendbydatum) {
		this.issendbydatum = issendbydatum;
	}

	public String getIsused() {
		return isused;
	}

	public void setIsused(String isused) {
		this.isused = isused;
	}

	public String getIsuseroute() {
		return isuseroute;
	}

	public void setIsuseroute(String isuseroute) {
		this.isuseroute = isuseroute;
	}

	public String getIswholesetsend() {
		return iswholesetsend;
	}

	public void setIswholesetsend(String iswholesetsend) {
		this.iswholesetsend = iswholesetsend;
	}

	public UFDouble getJhj() {
		return jhj;
	}

	public void setJhj(UFDouble jhj) {
		this.jhj = jhj;
	}

	public Integer getJyrhzdyw() {
		return jyrhzdyw;
	}

	public void setJyrhzdyw(Integer jyrhzdyw) {
		this.jyrhzdyw = jyrhzdyw;
	}

	public Integer getLowlevelcode() {
		return lowlevelcode;
	}

	public void setLowlevelcode(Integer lowlevelcode) {
		this.lowlevelcode = lowlevelcode;
	}

	public UFDouble getLowstocknum() {
		return lowstocknum;
	}

	public void setLowstocknum(UFDouble lowstocknum) {
		this.lowstocknum = lowstocknum;
	}

	public Integer getMaterclass() {
		return materclass;
	}

	public void setMaterclass(Integer materclass) {
		this.materclass = materclass;
	}

	public Integer getMaterstate() {
		return materstate;
	}

	public void setMaterstate(Integer materstate) {
		this.materstate = materstate;
	}

	public String getMatertype() {
		return matertype;
	}

	public void setMatertype(String matertype) {
		this.matertype = matertype;
	}

	public UFDouble getMaxstornum() {
		return maxstornum;
	}

	public void setMaxstornum(UFDouble maxstornum) {
		this.maxstornum = maxstornum;
	}

	public UFDouble getMinbatchnum() {
		return minbatchnum;
	}

	public void setMinbatchnum(UFDouble minbatchnum) {
		this.minbatchnum = minbatchnum;
	}

	public UFDouble getMinmulnum() {
		return minmulnum;
	}

	public void setMinmulnum(UFDouble minmulnum) {
		this.minmulnum = minmulnum;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public UFDouble getNbzyj() {
		return nbzyj;
	}

	public void setNbzyj(UFDouble nbzyj) {
		this.nbzyj = nbzyj;
	}

	public UFDouble getNetwtnum() {
		return netwtnum;
	}

	public void setNetwtnum(UFDouble netwtnum) {
		this.netwtnum = netwtnum;
	}

	public UFDouble getNyzbmxs() {
		return nyzbmxs;
	}

	public void setNyzbmxs(UFDouble nyzbmxs) {
		this.nyzbmxs = nyzbmxs;
	}

	public Integer getOutnumhistorydays() {
		return outnumhistorydays;
	}

	public void setOutnumhistorydays(Integer outnumhistorydays) {
		this.outnumhistorydays = outnumhistorydays;
	}

	public String getOuttype() {
		return outtype;
	}

	public void setOuttype(String outtype) {
		this.outtype = outtype;
	}

	public Integer getPchscscd() {
		return pchscscd;
	}

	public void setPchscscd(Integer pchscscd) {
		this.pchscscd = pchscscd;
	}

	public String getPk_calbody() {
		return pk_calbody;
	}

	public void setPk_calbody(String pk_calbody) {
		this.pk_calbody = pk_calbody;
	}

	public String getPk_ckjlcid() {
		return pk_ckjlcid;
	}

	public void setPk_ckjlcid(String pk_ckjlcid) {
		this.pk_ckjlcid = pk_ckjlcid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_deptdoc3() {
		return pk_deptdoc3;
	}

	public void setPk_deptdoc3(String pk_deptdoc3) {
		this.pk_deptdoc3 = pk_deptdoc3;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}

	public String getPk_produce() {
		return pk_produce;
	}

	public void setPk_produce(String pk_produce) {
		this.pk_produce = pk_produce;
	}

	public String getPk_psndoc3() {
		return pk_psndoc3;
	}

	public void setPk_psndoc3(String pk_psndoc3) {
		this.pk_psndoc3 = pk_psndoc3;
	}

	public String getPk_psndoc4() {
		return pk_psndoc4;
	}

	public void setPk_psndoc4(String pk_psndoc4) {
		this.pk_psndoc4 = pk_psndoc4;
	}

	public String getPk_rkjlcid() {
		return pk_rkjlcid;
	}

	public void setPk_rkjlcid(String pk_rkjlcid) {
		this.pk_rkjlcid = pk_rkjlcid;
	}

	public String getPk_sealuser() {
		return pk_sealuser;
	}

	public void setPk_sealuser(String pk_sealuser) {
		this.pk_sealuser = pk_sealuser;
	}

	public String getPk_stordoc() {
		return pk_stordoc;
	}

	public void setPk_stordoc(String pk_stordoc) {
		this.pk_stordoc = pk_stordoc;
	}

	public UFDouble getPrevahead() {
		return prevahead;
	}

	public void setPrevahead(UFDouble prevahead) {
		this.prevahead = prevahead;
	}

	public Integer getPricemethod() {
		return pricemethod;
	}

	public void setPricemethod(Integer pricemethod) {
		this.pricemethod = pricemethod;
	}

	public String getPrimaryflag() {
		return primaryflag;
	}

	public void setPrimaryflag(String primaryflag) {
		this.primaryflag = primaryflag;
	}

	public UFDouble getPrimnessnum() {
		return primnessnum;
	}

	public void setPrimnessnum(UFDouble primnessnum) {
		this.primnessnum = primnessnum;
	}

	public Integer getProducemethod() {
		return producemethod;
	}

	public void setProducemethod(Integer producemethod) {
		this.producemethod = producemethod;
	}

	public UFDouble getRationwtnum() {
		return rationwtnum;
	}

	public void setRationwtnum(UFDouble rationwtnum) {
		this.rationwtnum = rationwtnum;
	}

	public UFDouble getRealusableamount() {
		return realusableamount;
	}

	public void setRealusableamount(UFDouble realusableamount) {
		this.realusableamount = realusableamount;
	}

	public Integer getRoadtype() {
		return roadtype;
	}

	public void setRoadtype(Integer roadtype) {
		this.roadtype = roadtype;
	}

	public UFDouble getRoundingnum() {
		return roundingnum;
	}

	public void setRoundingnum(UFDouble roundingnum) {
		this.roundingnum = roundingnum;
	}

	public UFDouble getSafetystocknum() {
		return safetystocknum;
	}

	public void setSafetystocknum(UFDouble safetystocknum) {
		this.safetystocknum = safetystocknum;
	}

	public String getScheattr() {
		return scheattr;
	}

	public void setScheattr(String scheattr) {
		this.scheattr = scheattr;
	}

	public UFDouble getScpl() {
		return scpl;
	}

	public void setScpl(UFDouble scpl) {
		this.scpl = scpl;
	}

	public Integer getScscddms() {
		return scscddms;
	}

	public void setScscddms(Integer scscddms) {
		this.scscddms = scscddms;
	}

	public String getScxybzsfzk() {
		return scxybzsfzk;
	}

	public void setScxybzsfzk(String scxybzsfzk) {
		this.scxybzsfzk = scxybzsfzk;
	}

	public String getSealdate() {
		return sealdate;
	}

	public void setSealdate(String sealdate) {
		this.sealdate = sealdate;
	}

	public String getSealflag() {
		return sealflag;
	}

	public void setSealflag(String sealflag) {
		this.sealflag = sealflag;
	}

	public String getSfbj() {
		return sfbj;
	}

	public void setSfbj(String sfbj) {
		this.sfbj = sfbj;
	}

	public String getSfcbdx() {
		return sfcbdx;
	}

	public void setSfcbdx(String sfcbdx) {
		this.sfcbdx = sfcbdx;
	}

	public String getSffzfw() {
		return sffzfw;
	}

	public void setSffzfw(String sffzfw) {
		this.sffzfw = sffzfw;
	}

	public String getSfpchs() {
		return sfpchs;
	}

	public void setSfpchs(String sfpchs) {
		this.sfpchs = sfpchs;
	}

	public String getSfscx() {
		return sfscx;
	}

	public void setSfscx(String sfscx) {
		this.sfscx = sfscx;
	}

	public String getSfzb() {
		return sfzb;
	}

	public void setSfzb(String sfzb) {
		this.sfzb = sfzb;
	}

	public String getSfzzcp() {
		return sfzzcp;
	}

	public void setSfzzcp(String sfzzcp) {
		this.sfzzcp = sfzzcp;
	}

	public String getStockbycheck() {
		return stockbycheck;
	}

	public void setStockbycheck(String stockbycheck) {
		this.stockbycheck = stockbycheck;
	}

	public Integer getStocklowerdays() {
		return stocklowerdays;
	}

	public void setStocklowerdays(Integer stocklowerdays) {
		this.stocklowerdays = stocklowerdays;
	}

	public Integer getStockupperdays() {
		return stockupperdays;
	}

	public void setStockupperdays(Integer stockupperdays) {
		this.stockupperdays = stockupperdays;
	}

	public UFDouble getSumahead() {
		return sumahead;
	}

	public void setSumahead(UFDouble sumahead) {
		this.sumahead = sumahead;
	}

	public String getSupplytype() {
		return supplytype;
	}

	public void setSupplytype(String supplytype) {
		this.supplytype = supplytype;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getUsableamount() {
		return usableamount;
	}

	public void setUsableamount(String usableamount) {
		this.usableamount = usableamount;
	}

	public String getUsableamountbyfree() {
		return usableamountbyfree;
	}

	public void setUsableamountbyfree(String usableamountbyfree) {
		this.usableamountbyfree = usableamountbyfree;
	}

	public String getVirtualflag() {
		return virtualflag;
	}

	public void setVirtualflag(String virtualflag) {
		this.virtualflag = virtualflag;
	}

	public UFDouble getWasterrate() {
		return wasterrate;
	}

	public void setWasterrate(UFDouble wasterrate) {
		this.wasterrate = wasterrate;
	}

	public UFDouble getWggdsczkxs() {
		return wggdsczkxs;
	}

	public void setWggdsczkxs(UFDouble wggdsczkxs) {
		this.wggdsczkxs = wggdsczkxs;
	}

	public Integer getWghxcl() {
		return wghxcl;
	}

	public void setWghxcl(Integer wghxcl) {
		this.wghxcl = wghxcl;
	}

	public String getYbgys() {
		return ybgys;
	}

	public void setYbgys(String ybgys) {
		this.ybgys = ybgys;
	}

	public String getZbczjyxm() {
		return zbczjyxm;
	}

	public void setZbczjyxm(String zbczjyxm) {
		this.zbczjyxm = zbczjyxm;
	}

	public UFDouble getZbxs() {
		return zbxs;
	}

	public void setZbxs(UFDouble zbxs) {
		this.zbxs = zbxs;
	}

	public UFDouble getZdhd() {
		return zdhd;
	}

	public void setZdhd(UFDouble zdhd) {
		this.zdhd = zdhd;
	}

	public String getZgys() {
		return zgys;
	}

	public void setZgys(String zgys) {
		this.zgys = zgys;
	}

}
