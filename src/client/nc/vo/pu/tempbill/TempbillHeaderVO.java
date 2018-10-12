package nc.vo.pu.tempbill;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class TempbillHeaderVO extends SuperVO {

	private static final long serialVersionUID = 8740732187859047462L;
	private String pk_tempbill;
	private String pk_bill;
	private String pk_org;
	private String vbillno;
	private String dbilldate;
	private String vbilltype;
	private String pk_customer;
	private String pk_psndoc;
	private String psncode;
	private String pk_deptdoc;
	private String vmemo;
	private Integer rowno;
	private String pk_currtype;
	private UFDouble totalmny;
	private String is_ndht="N";
	private String is_thbu="N";
	private String pk_bank;
	private String bankaccount;
	private String pk_paytype;
	private Integer printno;
	private String useunit;
	private String is_dsh="N";
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private Integer vbillstatus;
	private String approver;
	private String approvedate;
	private String approvenote;
	private Integer dr=0;
	private String ts;
	private String billtypename;
	
	@Override
	public String getPrimaryKey() {
		return pk_tempbill;
	}

	@Override
	public String getTableName() {
		return "temp_bill";
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_tempbill";
	}

	public String getPk_bill() {
		return pk_bill;
	}

	public void setPk_bill(String pk_bill) {
		this.pk_bill = pk_bill;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getVbilltype() {
		return vbilltype;
	}

	public void setVbilltype(String vbilltype) {
		this.vbilltype = vbilltype;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public UFDouble getTotalmny() {
		return totalmny;
	}

	public void setTotalmny(UFDouble totalmny) {
		this.totalmny = totalmny;
	}

	public String getIs_ndht() {
		return is_ndht;
	}

	public void setIs_ndht(String is_ndht) {
		this.is_ndht = is_ndht;
	}

	public String getIs_thbu() {
		return is_thbu;
	}

	public void setIs_thbu(String is_thbu) {
		this.is_thbu = is_thbu;
	}

	public String getPk_bank() {
		return pk_bank;
	}

	public void setPk_bank(String pk_bank) {
		this.pk_bank = pk_bank;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getPk_paytype() {
		return pk_paytype;
	}

	public void setPk_paytype(String pk_paytype) {
		this.pk_paytype = pk_paytype;
	}

	public Integer getPrintno() {
		return printno;
	}

	public void setPrintno(Integer printno) {
		this.printno = printno;
	}

	public String getUseunit() {
		return useunit;
	}

	public void setUseunit(String useunit) {
		this.useunit = useunit;
	}

	public String getIs_dsh() {
		return is_dsh;
	}

	public void setIs_dsh(String is_dsh) {
		this.is_dsh = is_dsh;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
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

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApprovedate() {
		return approvedate;
	}

	public void setApprovedate(String approvedate) {
		this.approvedate = approvedate;
	}

	public String getApprovenote() {
		return approvenote;
	}

	public void setApprovenote(String approvenote) {
		this.approvenote = approvenote;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getBilltypename() {
		return billtypename;
	}

	public void setBilltypename(String billtypename) {
		this.billtypename = billtypename;
	}

	public String getPk_tempbill() {
		return pk_tempbill;
	}

	public void setPk_tempbill(String pk_tempbill) {
		this.pk_tempbill = pk_tempbill;
	}

	public String getPsncode() {
		return psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}
