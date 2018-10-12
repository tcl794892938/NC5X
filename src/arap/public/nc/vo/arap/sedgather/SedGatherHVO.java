package nc.vo.arap.sedgather;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class SedGatherHVO extends SuperVO {

	private String pk_sedagther;
	private String pk_corp;
	private Integer vbillstatus;
	private String vbillno;
	private String pk_busitype;
	private String pk_billtype;
	private String voperatorid;
	private String vapproveid;
	private UFDate voperatordate;
	private UFDate dapprovedate;
	private String vapprovenote;	
	private UFDateTime ts;
	private Integer dr=0;
	
	private String pk_saleorder;
	private String salebillno;
	private UFDate salebilldate;
	private String pk_cust;
	private UFDouble sale_nums;
	private UFDouble sale_amount;
	
	
	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
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

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_cust() {
		return pk_cust;
	}

	public void setPk_cust(String pk_cust) {
		this.pk_cust = pk_cust;
	}

	public String getPk_saleorder() {
		return pk_saleorder;
	}

	public void setPk_saleorder(String pk_saleorder) {
		this.pk_saleorder = pk_saleorder;
	}

	public String getPk_sedagther() {
		return pk_sedagther;
	}

	public void setPk_sedagther(String pk_sedagther) {
		this.pk_sedagther = pk_sedagther;
	}

	public UFDouble getSale_amount() {
		return sale_amount;
	}

	public void setSale_amount(UFDouble sale_amount) {
		this.sale_amount = sale_amount;
	}

	public UFDouble getSale_nums() {
		return sale_nums;
	}

	public void setSale_nums(UFDouble sale_nums) {
		this.sale_nums = sale_nums;
	}

	public UFDate getSalebilldate() {
		return salebilldate;
	}

	public void setSalebilldate(UFDate salebilldate) {
		this.salebilldate = salebilldate;
	}

	public String getSalebillno() {
		return salebillno;
	}

	public void setSalebillno(String salebillno) {
		this.salebillno = salebillno;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getVapproveid() {
		return vapproveid;
	}

	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}

	public String getVapprovenote() {
		return vapprovenote;
	}

	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public UFDate getVoperatordate() {
		return voperatordate;
	}

	public void setVoperatordate(UFDate voperatordate) {
		this.voperatordate = voperatordate;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	@Override
	public String getPKFieldName() {
		return "pk_sedagther";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "bx_sedgather";
	}

}
