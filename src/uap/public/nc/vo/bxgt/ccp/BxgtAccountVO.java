package nc.vo.bxgt.ccp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class BxgtAccountVO extends SuperVO {

	private String bendsettle1;		
	private String bfchsflag;	
	private String bsettleendflag;		
	private String btoaccountflag;			
	private String btranendflag;	
	private String bzgarapflag;	
	private String caccountunitid;		
	private String cfeeid;			
	private String cgeneralbb3;			
	private String cgeneralbid;		
	private String cgeneralhid;			
	private String coperatorid;		
	private String cpk1;			
	private String cpk2;			
	private String csettlerid;			
	private String csettlerid2;		
	private String currencytypeid;		
	private String dfchsdate;	
	private Integer dr=0;
	private String dtozgdate;			
	private String dzgarapdate;		
	private Integer fzgarrule;	
	private UFDouble naccountmny;		
	private UFDouble naccountnum1;			
	private UFDouble naccountnum2;		
	private UFDouble naccumoutbacknum;		
	private UFDouble naccumoutsignnum;			
	private UFDouble naccumwashnum;			
	private UFDouble ndmsignnum;		
	private UFDouble nexchangeotobrate;			
	private UFDouble nfeemny;		
	private UFDouble nmaterialmoney;		
	private UFDouble norgnettaxprice;			
	private UFDouble noriginalcurmny;			
	private UFDouble noriginalnetprice;		
	private UFDouble noriginaltaxpricemny;			
	private UFDouble norisignsummny;		
	private UFDouble norisigntaxsummny;			
	private UFDouble npmoney;		
	private UFDouble npprice;		
	private UFDouble nrsvnum1;			
	private UFDouble nrsvnum2;			
	private UFDouble nrushnum;		
	private UFDouble nsaleinsettlenum;		
	private UFDouble nsettlemny1;		
	private UFDouble nsettlenum1;			
	private UFDouble nsignnum;			
	private UFDouble nsignsummny;			
	private UFDouble nsigntaxsummny;			
	private UFDouble nsoinvoicetoarnum;		
	private UFDouble ntoaccountmny;			
	private UFDouble ntoaccountnum;			
	private UFDouble ntoarnum;			
	private UFDouble ntotaltrannum;		
	private UFDouble nzgmny1;		
	private UFDouble nzgmny2;			
	private UFDouble nzgprice1;		
	private UFDouble nzgprice2;	
	private UFDouble nzgyfmoney;			
	private UFDouble nzgyfprice;			
	private String pk_corp;			
	private String ts;		
	private String vrsv1;		
	private String vrsv2;		
	private String vsettledbillcode;		
	private String vsettledbillcode2;		

	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralbb3";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_bb3";
	}

	public String getBendsettle1() {
		return bendsettle1;
	}

	public void setBendsettle1(String bendsettle1) {
		this.bendsettle1 = bendsettle1;
	}

	public String getBfchsflag() {
		return bfchsflag;
	}

	public void setBfchsflag(String bfchsflag) {
		this.bfchsflag = bfchsflag;
	}

	public String getBsettleendflag() {
		return bsettleendflag;
	}

	public void setBsettleendflag(String bsettleendflag) {
		this.bsettleendflag = bsettleendflag;
	}

	public String getBtoaccountflag() {
		return btoaccountflag;
	}

	public void setBtoaccountflag(String btoaccountflag) {
		this.btoaccountflag = btoaccountflag;
	}

	public String getBtranendflag() {
		return btranendflag;
	}

	public void setBtranendflag(String btranendflag) {
		this.btranendflag = btranendflag;
	}

	public String getBzgarapflag() {
		return bzgarapflag;
	}

	public void setBzgarapflag(String bzgarapflag) {
		this.bzgarapflag = bzgarapflag;
	}

	public String getCaccountunitid() {
		return caccountunitid;
	}

	public void setCaccountunitid(String caccountunitid) {
		this.caccountunitid = caccountunitid;
	}

	public String getCfeeid() {
		return cfeeid;
	}

	public void setCfeeid(String cfeeid) {
		this.cfeeid = cfeeid;
	}

	public String getCgeneralbb3() {
		return cgeneralbb3;
	}

	public void setCgeneralbb3(String cgeneralbb3) {
		this.cgeneralbb3 = cgeneralbb3;
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

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getCpk1() {
		return cpk1;
	}

	public void setCpk1(String cpk1) {
		this.cpk1 = cpk1;
	}

	public String getCpk2() {
		return cpk2;
	}

	public void setCpk2(String cpk2) {
		this.cpk2 = cpk2;
	}

	public String getCsettlerid() {
		return csettlerid;
	}

	public void setCsettlerid(String csettlerid) {
		this.csettlerid = csettlerid;
	}

	public String getCsettlerid2() {
		return csettlerid2;
	}

	public void setCsettlerid2(String csettlerid2) {
		this.csettlerid2 = csettlerid2;
	}

	public String getCurrencytypeid() {
		return currencytypeid;
	}

	public void setCurrencytypeid(String currencytypeid) {
		this.currencytypeid = currencytypeid;
	}

	public String getDfchsdate() {
		return dfchsdate;
	}

	public void setDfchsdate(String dfchsdate) {
		this.dfchsdate = dfchsdate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getDtozgdate() {
		return dtozgdate;
	}

	public void setDtozgdate(String dtozgdate) {
		this.dtozgdate = dtozgdate;
	}

	public String getDzgarapdate() {
		return dzgarapdate;
	}

	public void setDzgarapdate(String dzgarapdate) {
		this.dzgarapdate = dzgarapdate;
	}

	public Integer getFzgarrule() {
		return fzgarrule;
	}

	public void setFzgarrule(Integer fzgarrule) {
		this.fzgarrule = fzgarrule;
	}

	public UFDouble getNaccountmny() {
		return naccountmny;
	}

	public void setNaccountmny(UFDouble naccountmny) {
		this.naccountmny = naccountmny;
	}

	public UFDouble getNaccountnum1() {
		return naccountnum1;
	}

	public void setNaccountnum1(UFDouble naccountnum1) {
		this.naccountnum1 = naccountnum1;
	}

	public UFDouble getNaccountnum2() {
		return naccountnum2;
	}

	public void setNaccountnum2(UFDouble naccountnum2) {
		this.naccountnum2 = naccountnum2;
	}

	public UFDouble getNaccumoutbacknum() {
		return naccumoutbacknum;
	}

	public void setNaccumoutbacknum(UFDouble naccumoutbacknum) {
		this.naccumoutbacknum = naccumoutbacknum;
	}

	public UFDouble getNaccumoutsignnum() {
		return naccumoutsignnum;
	}

	public void setNaccumoutsignnum(UFDouble naccumoutsignnum) {
		this.naccumoutsignnum = naccumoutsignnum;
	}

	public UFDouble getNaccumwashnum() {
		return naccumwashnum;
	}

	public void setNaccumwashnum(UFDouble naccumwashnum) {
		this.naccumwashnum = naccumwashnum;
	}

	public UFDouble getNdmsignnum() {
		return ndmsignnum;
	}

	public void setNdmsignnum(UFDouble ndmsignnum) {
		this.ndmsignnum = ndmsignnum;
	}

	public UFDouble getNexchangeotobrate() {
		return nexchangeotobrate;
	}

	public void setNexchangeotobrate(UFDouble nexchangeotobrate) {
		this.nexchangeotobrate = nexchangeotobrate;
	}

	public UFDouble getNfeemny() {
		return nfeemny;
	}

	public void setNfeemny(UFDouble nfeemny) {
		this.nfeemny = nfeemny;
	}

	public UFDouble getNmaterialmoney() {
		return nmaterialmoney;
	}

	public void setNmaterialmoney(UFDouble nmaterialmoney) {
		this.nmaterialmoney = nmaterialmoney;
	}

	public UFDouble getNorgnettaxprice() {
		return norgnettaxprice;
	}

	public void setNorgnettaxprice(UFDouble norgnettaxprice) {
		this.norgnettaxprice = norgnettaxprice;
	}

	public UFDouble getNoriginalcurmny() {
		return noriginalcurmny;
	}

	public void setNoriginalcurmny(UFDouble noriginalcurmny) {
		this.noriginalcurmny = noriginalcurmny;
	}

	public UFDouble getNoriginalnetprice() {
		return noriginalnetprice;
	}

	public void setNoriginalnetprice(UFDouble noriginalnetprice) {
		this.noriginalnetprice = noriginalnetprice;
	}

	public UFDouble getNoriginaltaxpricemny() {
		return noriginaltaxpricemny;
	}

	public void setNoriginaltaxpricemny(UFDouble noriginaltaxpricemny) {
		this.noriginaltaxpricemny = noriginaltaxpricemny;
	}

	public UFDouble getNorisignsummny() {
		return norisignsummny;
	}

	public void setNorisignsummny(UFDouble norisignsummny) {
		this.norisignsummny = norisignsummny;
	}

	public UFDouble getNorisigntaxsummny() {
		return norisigntaxsummny;
	}

	public void setNorisigntaxsummny(UFDouble norisigntaxsummny) {
		this.norisigntaxsummny = norisigntaxsummny;
	}

	public UFDouble getNpmoney() {
		return npmoney;
	}

	public void setNpmoney(UFDouble npmoney) {
		this.npmoney = npmoney;
	}

	public UFDouble getNpprice() {
		return npprice;
	}

	public void setNpprice(UFDouble npprice) {
		this.npprice = npprice;
	}

	public UFDouble getNrsvnum1() {
		return nrsvnum1;
	}

	public void setNrsvnum1(UFDouble nrsvnum1) {
		this.nrsvnum1 = nrsvnum1;
	}

	public UFDouble getNrsvnum2() {
		return nrsvnum2;
	}

	public void setNrsvnum2(UFDouble nrsvnum2) {
		this.nrsvnum2 = nrsvnum2;
	}

	public UFDouble getNrushnum() {
		return nrushnum;
	}

	public void setNrushnum(UFDouble nrushnum) {
		this.nrushnum = nrushnum;
	}

	public UFDouble getNsaleinsettlenum() {
		return nsaleinsettlenum;
	}

	public void setNsaleinsettlenum(UFDouble nsaleinsettlenum) {
		this.nsaleinsettlenum = nsaleinsettlenum;
	}

	public UFDouble getNsettlemny1() {
		return nsettlemny1;
	}

	public void setNsettlemny1(UFDouble nsettlemny1) {
		this.nsettlemny1 = nsettlemny1;
	}

	public UFDouble getNsettlenum1() {
		return nsettlenum1;
	}

	public void setNsettlenum1(UFDouble nsettlenum1) {
		this.nsettlenum1 = nsettlenum1;
	}

	public UFDouble getNsignnum() {
		return nsignnum;
	}

	public void setNsignnum(UFDouble nsignnum) {
		this.nsignnum = nsignnum;
	}

	public UFDouble getNsignsummny() {
		return nsignsummny;
	}

	public void setNsignsummny(UFDouble nsignsummny) {
		this.nsignsummny = nsignsummny;
	}

	public UFDouble getNsigntaxsummny() {
		return nsigntaxsummny;
	}

	public void setNsigntaxsummny(UFDouble nsigntaxsummny) {
		this.nsigntaxsummny = nsigntaxsummny;
	}

	public UFDouble getNsoinvoicetoarnum() {
		return nsoinvoicetoarnum;
	}

	public void setNsoinvoicetoarnum(UFDouble nsoinvoicetoarnum) {
		this.nsoinvoicetoarnum = nsoinvoicetoarnum;
	}

	public UFDouble getNtoaccountmny() {
		return ntoaccountmny;
	}

	public void setNtoaccountmny(UFDouble ntoaccountmny) {
		this.ntoaccountmny = ntoaccountmny;
	}

	public UFDouble getNtoaccountnum() {
		return ntoaccountnum;
	}

	public void setNtoaccountnum(UFDouble ntoaccountnum) {
		this.ntoaccountnum = ntoaccountnum;
	}

	public UFDouble getNtoarnum() {
		return ntoarnum;
	}

	public void setNtoarnum(UFDouble ntoarnum) {
		this.ntoarnum = ntoarnum;
	}

	public UFDouble getNtotaltrannum() {
		return ntotaltrannum;
	}

	public void setNtotaltrannum(UFDouble ntotaltrannum) {
		this.ntotaltrannum = ntotaltrannum;
	}

	public UFDouble getNzgmny1() {
		return nzgmny1;
	}

	public void setNzgmny1(UFDouble nzgmny1) {
		this.nzgmny1 = nzgmny1;
	}

	public UFDouble getNzgmny2() {
		return nzgmny2;
	}

	public void setNzgmny2(UFDouble nzgmny2) {
		this.nzgmny2 = nzgmny2;
	}

	public UFDouble getNzgprice1() {
		return nzgprice1;
	}

	public void setNzgprice1(UFDouble nzgprice1) {
		this.nzgprice1 = nzgprice1;
	}

	public UFDouble getNzgprice2() {
		return nzgprice2;
	}

	public void setNzgprice2(UFDouble nzgprice2) {
		this.nzgprice2 = nzgprice2;
	}

	public UFDouble getNzgyfmoney() {
		return nzgyfmoney;
	}

	public void setNzgyfmoney(UFDouble nzgyfmoney) {
		this.nzgyfmoney = nzgyfmoney;
	}

	public UFDouble getNzgyfprice() {
		return nzgyfprice;
	}

	public void setNzgyfprice(UFDouble nzgyfprice) {
		this.nzgyfprice = nzgyfprice;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVrsv1() {
		return vrsv1;
	}

	public void setVrsv1(String vrsv1) {
		this.vrsv1 = vrsv1;
	}

	public String getVrsv2() {
		return vrsv2;
	}

	public void setVrsv2(String vrsv2) {
		this.vrsv2 = vrsv2;
	}

	public String getVsettledbillcode() {
		return vsettledbillcode;
	}

	public void setVsettledbillcode(String vsettledbillcode) {
		this.vsettledbillcode = vsettledbillcode;
	}

	public String getVsettledbillcode2() {
		return vsettledbillcode2;
	}

	public void setVsettledbillcode2(String vsettledbillcode2) {
		this.vsettledbillcode2 = vsettledbillcode2;
	}

}
