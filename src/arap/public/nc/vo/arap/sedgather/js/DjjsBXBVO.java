package nc.vo.arap.sedgather.js;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class DjjsBXBVO  extends SuperVO{
	  private String  bankmsg;
	  private String  bankrelated_code;
	  private String  billcode;
	  private UFDate  billdate;
	  private UFDateTime  billtime;
	  private  Integer busilineno;
	  private  Integer  checkcount;
	  private UFDate checkdate;
	  private String  code;
	  private String  def1;
	  private String  def10;
	  private String  def11;
	  private String  def12;
	  private String  def13;
	  private String  def14;
	  private String  def15;
	  private String  def16;
	  private String  def17;
	  private String  def18;
	  private String  def19;
	  private String  def2;
	  private String  def20;
	  private String  def3;
	  private String  def4;
	  private String  def5;
	  private String  def6;
	  private String  def7;
	  private String  def8;
	  private String  def9;
	  private  Integer  direction;
	  private  Integer dr;
	  private String editedflag;
	  private UFDate execdate;
	  private UFDateTime exectime;
	  private UFDate expectate_date;
	  private UFDouble fracrate;
	  private  Integer  fundformcode;
	  private  Integer  fundsflag;
	  private  Integer  groupno;
	  private String isacccord;
	  private String isbillrecord;
	  private UFDate  lastest_paydate;
	  private UFDouble localrate;
	  private String  memo;
	  private String  modifyflag;
	  private  Integer  notedirection;
	  private String  notenumber;
	  private String  oppaccount;
	  private String  oppbank;
	  private UFDouble  pay;
	  private String  paybillcode;
	  private UFDouble payfrac;
	  private UFDouble paylocal;
	  private String pk_account;
	  private String pk_assaccount;
	  private String pk_balatype;
	  private String pk_bank;
	  private String  pk_bill;
	  private String  pk_billbalatype;
	  private String  pk_billdetail;
	  private String  pk_billtype;
	  private String  pk_busitype;
	  private String  pk_cashflow;
	  private String pk_corp;
	  private String pk_costsubj;
	  private String  pk_cubasdoc;
	  private String  pk_currtype;
	  private String  pk_deptdoc;
	  private String  pk_detail;
	  private String pk_finorg;
	  private String  pk_innercorp;
	  private String  pk_invbasdoc;
	  private String	  pk_invcl;
	  private String	  pk_job;
	  private String  pk_jobphase;
	  private String  pk_mngaccount;
	  private String	  pk_notetype;
	  private String  pk_oppaccount;
	  private String  pk_oppbank;
	  private String  pk_paybill;
	  private String  pk_plansubj;
	  private String  pk_psndoc;
	  private String  pk_rescenter;
	  private String  pk_settlement;
	  private String	  pk_trader;
	  private  Integer  processtype;
	  private UFDouble  receive;
	  private UFDouble  receivefrac;
	  private UFDouble receivelocal;
	  private  Integer  settlelineno;
	  private  Integer  settlestatus;
	  private UFDate signdate;
	  private UFDateTime  signtime;
	  private String  systemcode;
	  private UFDate  tallydate;
	  private  Integer  tallystatus;
	  private String  tradername;
	  private  Integer  tradertype;
	  private  Integer  transtype;
	  private UFDateTime ts;
	  
	@Override
	public String getPKFieldName() {
		return "pk_detail";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "cmp_detail";
	}

	public String getBankmsg() {
		return bankmsg;
	}

	public void setBankmsg(String bankmsg) {
		this.bankmsg = bankmsg;
	}

	public String getBankrelated_code() {
		return bankrelated_code;
	}

	public void setBankrelated_code(String bankrelated_code) {
		this.bankrelated_code = bankrelated_code;
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public UFDate getBilldate() {
		return billdate;
	}

	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	public UFDateTime getBilltime() {
		return billtime;
	}

	public void setBilltime(UFDateTime billtime) {
		this.billtime = billtime;
	}

	public Integer getBusilineno() {
		return busilineno;
	}

	public void setBusilineno(Integer busilineno) {
		this.busilineno = busilineno;
	}

	public Integer getCheckcount() {
		return checkcount;
	}

	public void setCheckcount(Integer checkcount) {
		this.checkcount = checkcount;
	}

	public UFDate getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(UFDate checkdate) {
		this.checkdate = checkdate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
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

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getEditedflag() {
		return editedflag;
	}

	public void setEditedflag(String editedflag) {
		this.editedflag = editedflag;
	}

	public UFDate getExecdate() {
		return execdate;
	}

	public void setExecdate(UFDate execdate) {
		this.execdate = execdate;
	}

	public UFDateTime getExectime() {
		return exectime;
	}

	public void setExectime(UFDateTime exectime) {
		this.exectime = exectime;
	}

	public UFDate getExpectate_date() {
		return expectate_date;
	}

	public void setExpectate_date(UFDate expectate_date) {
		this.expectate_date = expectate_date;
	}

	public UFDouble getFracrate() {
		return fracrate;
	}

	public void setFracrate(UFDouble fracrate) {
		this.fracrate = fracrate;
	}

	public Integer getFundformcode() {
		return fundformcode;
	}

	public void setFundformcode(Integer fundformcode) {
		this.fundformcode = fundformcode;
	}

	public Integer getFundsflag() {
		return fundsflag;
	}

	public void setFundsflag(Integer fundsflag) {
		this.fundsflag = fundsflag;
	}

	public Integer getGroupno() {
		return groupno;
	}

	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
	}

	public String getIsacccord() {
		return isacccord;
	}

	public void setIsacccord(String isacccord) {
		this.isacccord = isacccord;
	}

	public String getIsbillrecord() {
		return isbillrecord;
	}

	public void setIsbillrecord(String isbillrecord) {
		this.isbillrecord = isbillrecord;
	}

	public UFDate getLastest_paydate() {
		return lastest_paydate;
	}

	public void setLastest_paydate(UFDate lastest_paydate) {
		this.lastest_paydate = lastest_paydate;
	}

	public UFDouble getLocalrate() {
		return localrate;
	}

	public void setLocalrate(UFDouble localrate) {
		this.localrate = localrate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getModifyflag() {
		return modifyflag;
	}

	public void setModifyflag(String modifyflag) {
		this.modifyflag = modifyflag;
	}

	public Integer getNotedirection() {
		return notedirection;
	}

	public void setNotedirection(Integer notedirection) {
		this.notedirection = notedirection;
	}

	public String getNotenumber() {
		return notenumber;
	}

	public void setNotenumber(String notenumber) {
		this.notenumber = notenumber;
	}

	public String getOppaccount() {
		return oppaccount;
	}

	public void setOppaccount(String oppaccount) {
		this.oppaccount = oppaccount;
	}

	public String getOppbank() {
		return oppbank;
	}

	public void setOppbank(String oppbank) {
		this.oppbank = oppbank;
	}

	public UFDouble getPay() {
		return pay;
	}

	public void setPay(UFDouble pay) {
		this.pay = pay;
	}

	public String getPaybillcode() {
		return paybillcode;
	}

	public void setPaybillcode(String paybillcode) {
		this.paybillcode = paybillcode;
	}

	public UFDouble getPayfrac() {
		return payfrac;
	}

	public void setPayfrac(UFDouble payfrac) {
		this.payfrac = payfrac;
	}

	public UFDouble getPaylocal() {
		return paylocal;
	}

	public void setPaylocal(UFDouble paylocal) {
		this.paylocal = paylocal;
	}

	public String getPk_account() {
		return pk_account;
	}

	public void setPk_account(String pk_account) {
		this.pk_account = pk_account;
	}

	public String getPk_assaccount() {
		return pk_assaccount;
	}

	public void setPk_assaccount(String pk_assaccount) {
		this.pk_assaccount = pk_assaccount;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getPk_bank() {
		return pk_bank;
	}

	public void setPk_bank(String pk_bank) {
		this.pk_bank = pk_bank;
	}

	public String getPk_bill() {
		return pk_bill;
	}

	public void setPk_bill(String pk_bill) {
		this.pk_bill = pk_bill;
	}

	public String getPk_billbalatype() {
		return pk_billbalatype;
	}

	public void setPk_billbalatype(String pk_billbalatype) {
		this.pk_billbalatype = pk_billbalatype;
	}

	public String getPk_billdetail() {
		return pk_billdetail;
	}

	public void setPk_billdetail(String pk_billdetail) {
		this.pk_billdetail = pk_billdetail;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_busitype() {
		return pk_busitype;
	}

	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
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

	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}

	public void setPk_cubasdoc(String pk_cubasdoc) {
		this.pk_cubasdoc = pk_cubasdoc;
	}

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_detail() {
		return pk_detail;
	}

	public void setPk_detail(String pk_detail) {
		this.pk_detail = pk_detail;
	}

	public String getPk_finorg() {
		return pk_finorg;
	}

	public void setPk_finorg(String pk_finorg) {
		this.pk_finorg = pk_finorg;
	}

	public String getPk_innercorp() {
		return pk_innercorp;
	}

	public void setPk_innercorp(String pk_innercorp) {
		this.pk_innercorp = pk_innercorp;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getPk_invcl() {
		return pk_invcl;
	}

	public void setPk_invcl(String pk_invcl) {
		this.pk_invcl = pk_invcl;
	}

	public String getPk_job() {
		return pk_job;
	}

	public void setPk_job(String pk_job) {
		this.pk_job = pk_job;
	}

	public String getPk_jobphase() {
		return pk_jobphase;
	}

	public void setPk_jobphase(String pk_jobphase) {
		this.pk_jobphase = pk_jobphase;
	}

	public String getPk_mngaccount() {
		return pk_mngaccount;
	}

	public void setPk_mngaccount(String pk_mngaccount) {
		this.pk_mngaccount = pk_mngaccount;
	}

	public String getPk_notetype() {
		return pk_notetype;
	}

	public void setPk_notetype(String pk_notetype) {
		this.pk_notetype = pk_notetype;
	}

	public String getPk_oppaccount() {
		return pk_oppaccount;
	}

	public void setPk_oppaccount(String pk_oppaccount) {
		this.pk_oppaccount = pk_oppaccount;
	}

	public String getPk_oppbank() {
		return pk_oppbank;
	}

	public void setPk_oppbank(String pk_oppbank) {
		this.pk_oppbank = pk_oppbank;
	}

	public String getPk_paybill() {
		return pk_paybill;
	}

	public void setPk_paybill(String pk_paybill) {
		this.pk_paybill = pk_paybill;
	}

	public String getPk_plansubj() {
		return pk_plansubj;
	}

	public void setPk_plansubj(String pk_plansubj) {
		this.pk_plansubj = pk_plansubj;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_rescenter() {
		return pk_rescenter;
	}

	public void setPk_rescenter(String pk_rescenter) {
		this.pk_rescenter = pk_rescenter;
	}

	public String getPk_settlement() {
		return pk_settlement;
	}

	public void setPk_settlement(String pk_settlement) {
		this.pk_settlement = pk_settlement;
	}

	public String getPk_trader() {
		return pk_trader;
	}

	public void setPk_trader(String pk_trader) {
		this.pk_trader = pk_trader;
	}

	public Integer getProcesstype() {
		return processtype;
	}

	public void setProcesstype(Integer processtype) {
		this.processtype = processtype;
	}

	public UFDouble getReceive() {
		return receive;
	}

	public void setReceive(UFDouble receive) {
		this.receive = receive;
	}

	public UFDouble getReceivefrac() {
		return receivefrac;
	}

	public void setReceivefrac(UFDouble receivefrac) {
		this.receivefrac = receivefrac;
	}

	public UFDouble getReceivelocal() {
		return receivelocal;
	}

	public void setReceivelocal(UFDouble receivelocal) {
		this.receivelocal = receivelocal;
	}

	public Integer getSettlelineno() {
		return settlelineno;
	}

	public void setSettlelineno(Integer settlelineno) {
		this.settlelineno = settlelineno;
	}

	public Integer getSettlestatus() {
		return settlestatus;
	}

	public void setSettlestatus(Integer settlestatus) {
		this.settlestatus = settlestatus;
	}

	public UFDate getSigndate() {
		return signdate;
	}

	public void setSigndate(UFDate signdate) {
		this.signdate = signdate;
	}

	public UFDateTime getSigntime() {
		return signtime;
	}

	public void setSigntime(UFDateTime signtime) {
		this.signtime = signtime;
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode;
	}

	public UFDate getTallydate() {
		return tallydate;
	}

	public void setTallydate(UFDate tallydate) {
		this.tallydate = tallydate;
	}

	public Integer getTallystatus() {
		return tallystatus;
	}

	public void setTallystatus(Integer tallystatus) {
		this.tallystatus = tallystatus;
	}

	public String getTradername() {
		return tradername;
	}

	public void setTradername(String tradername) {
		this.tradername = tradername;
	}

	public Integer getTradertype() {
		return tradertype;
	}

	public void setTradertype(Integer tradertype) {
		this.tradertype = tradertype;
	}

	public Integer getTranstype() {
		return transtype;
	}

	public void setTranstype(Integer transtype) {
		this.transtype = transtype;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

}
