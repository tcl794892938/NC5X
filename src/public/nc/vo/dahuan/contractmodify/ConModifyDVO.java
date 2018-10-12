package nc.vo.dahuan.contractmodify;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ConModifyDVO extends SuperVO {

	public String pk_contractmodify_d;
	public String pk_contractmodify;
	public String pk_invbasdoc;
	public String invcode;
	public String invname;
	public String stylemodel;
	public String meaname;
    public UFDouble nums;
    public UFDouble fc_price;
    public UFDouble price;
    public UFDouble fc_amount;
    public UFDouble amount;
    public UFDate delivery_date;
    public String vemo;
    public UFDateTime ts;
    public Integer dr=0;
	
	public UFDouble getAmount() {
		return amount;
	}

	public void setAmount(UFDouble amount) {
		this.amount = amount;
	}

	public UFDate getDelivery_date() {
		return delivery_date;
	}

	public void setDelivery_date(UFDate delivery_date) {
		this.delivery_date = delivery_date;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getFc_amount() {
		return fc_amount;
	}

	public void setFc_amount(UFDouble fc_amount) {
		this.fc_amount = fc_amount;
	}

	public UFDouble getFc_price() {
		return fc_price;
	}

	public void setFc_price(UFDouble fc_price) {
		this.fc_price = fc_price;
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	public String getMeaname() {
		return meaname;
	}

	public void setMeaname(String meaname) {
		this.meaname = meaname;
	}

	public UFDouble getNums() {
		return nums;
	}

	public void setNums(UFDouble nums) {
		this.nums = nums;
	}

	public String getPk_contractmodify() {
		return pk_contractmodify;
	}

	public void setPk_contractmodify(String pk_contractmodify) {
		this.pk_contractmodify = pk_contractmodify;
	}

	public String getPk_contractmodify_d() {
		return pk_contractmodify_d;
	}

	public void setPk_contractmodify_d(String pk_contractmodify_d) {
		this.pk_contractmodify_d = pk_contractmodify_d;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public UFDouble getPrice() {
		return price;
	}

	public void setPrice(UFDouble price) {
		this.price = price;
	}

	public String getStylemodel() {
		return stylemodel;
	}

	public void setStylemodel(String stylemodel) {
		this.stylemodel = stylemodel;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getVemo() {
		return vemo;
	}

	public void setVemo(String vemo) {
		this.vemo = vemo;
	}

	public ConModifyDVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		return "pk_contractmodify_d";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_contractmodify";
	}

	@Override
	public String getTableName() {
		return "dh_contractmodify_d";
	}

}
