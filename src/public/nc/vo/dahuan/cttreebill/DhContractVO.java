/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.dahuan.cttreebill;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2013-10-6
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class DhContractVO extends SuperVO {

	public String pk_corp;
	public String vdef4;
	public String pk_cust1;
	public String pk_fzr;
	public String pk_cttype;
	public String vdef7;
	public String ctcode;
	public String voperatorid;
	public String pk_skfs;
	public String vapprovenote;
	public String pk_billtype;
	public Integer vbillstatus;
	public String vmen;
	public String vdef2;
	public String vsourcebilltype;
	public String tmodifytime;
	public String pk_busitype;
	public String vdef1;
	public String vjhaddress;
	public UFDate dstartdate;
	public UFDouble dctjetotal;
	public UFDate dbilldate;
	public String vbillno;
	public String pk_contract;
	public UFDate djhdate;
	public String pk_deptdoc;
	public String vlastbilltype;
	public String pk_jobmandoc;
	public String vdef3;
	public String vdef9;
	public UFDate dapprovedate;
	public String vdef8;
	public String pk_cust2;
	public String vlastbillid;
	public String vapproveid;
	public String vdef6;
	public UFDouble dcaigtotal;
	public String vdef10;
	public String vsourcebillid;
	public String vdef5;
	public UFDouble dsaletotal;
	public String ctname;
	public String cmodifyid;
	public String jobtype;
	public String custname1;
	public String custname2;
	public String htaddress;
	public Integer httype;
	public UFDate htrq;
	public String jobcode;
	public UFDouble ljfkjhje;
	public UFDouble ljsjfkje;
	public String ctname3;
	public String ctcode19;
	public String ctname15;
	public String ctname11;
	public String ctcode18;
	public String ctcode3;
	public String ctname10;
	public String ctname17;
	public String ctname13;
	public String ctcode6;
	public String ctname9;
	public String ctname20;
	public String ctname2;
	public String ctcode17;
	public String ctname18;
	public String ctcode5;
	public String ctcode11;
	public String ctname1;
	public String ctname19;
	public String ctcode4;
	public String ctcode12;
	public String ctname4;
	public String ctcode7;
	public String ctcode15;
	public String ctname6;
	public String pk_httk;
	public String ctname16;
	public String ctname14;
	public String ctcode2;
	public String ctcode13;
	public String ctname7;
	public String ctcode20;
	public String ctcode14;
	public String ctcode10;
	public String ctcode1;
	public String ctcode8;
	public String ctname8;
	public String ctname12;
	public String ctcode16;
	public String ctname5;
	public String ctcode9;
	public UFDateTime ts;
	public Integer dr;
	public Integer is_delivery = 0;
	public Integer is_seal = 0;
	public Integer is_pay = 0;
	public String pk_bank;
	public String pk_xmjl;
	public String sax_no;
	public String pk_ysid;
	public String ys_flag = "0";
	public String pk_fuzong;
	public String fuzong_flag = "0";
	public Integer is_conexe = 0;
	public UFDouble xm_amount;
	public String ht_dept;
	public Integer ht_changenum = 0;
	public String ret_vemo;
	//
	public UFDouble ljskje;
	public UFDouble sjspje;
	public UFDouble sjfkbl; //已付款比例  by tcl
	
	public UFDouble currenty_rate;
    public String pk_currenty;
    public UFDouble curr_amount;
    
    public String relationid;
    public Integer is_relation;

	public static final String PK_CORP = "pk_corp";
	public static final String VDEF4 = "vdef4";
	public static final String PK_CUST1 = "pk_cust1";
	public static final String PK_FZR = "pk_fzr";
	public static final String PK_CTTYPE = "pk_cttype";
	public static final String VDEF7 = "vdef7";
	public static final String CTCODE = "ctcode";
	public static final String VOPERATORID = "voperatorid";
	public static final String PK_SKFS = "pk_skfs";
	public static final String VAPPROVENOTE = "vapprovenote";
	public static final String PK_BILLTYPE = "pk_billtype";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String VMEN = "vmen";
	public static final String VDEF2 = "vdef2";
	public static final String VSOURCEBILLTYPE = "vsourcebilltype";
	public static final String TMODIFYTIME = "tmodifytime";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String VDEF1 = "vdef1";
	public static final String VJHADDRESS = "vjhaddress";
	public static final String DSTARTDATE = "dstartdate";
	public static final String DCTJETOTAL = "dctjetotal";
	public static final String DBILLDATE = "dbilldate";
	public static final String VBILLNO = "vbillno";
	public static final String PK_CONTRACT = "pk_contract";
	public static final String DJHDATE = "djhdate";
	public static final String PK_DEPTDOC = "pk_deptdoc";
	public static final String VLASTBILLTYPE = "vlastbilltype";
	public static final String PK_JOBMANDOC = "pk_jobmandoc";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF9 = "vdef9";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String VDEF8 = "vdef8";
	public static final String PK_CUST2 = "pk_cust2";
	public static final String VLASTBILLID = "vlastbillid";
	public static final String VAPPROVEID = "vapproveid";
	public static final String VDEF6 = "vdef6";
	public static final String DCAIGTOTAL = "dcaigtotal";
	public static final String VDEF10 = "vdef10";
	public static final String VSOURCEBILLID = "vsourcebillid";
	public static final String VDEF5 = "vdef5";
	public static final String DSALETOTAL = "dsaletotal";
	public static final String CTNAME = "ctname";
	public static final String CMODIFYID = "cmodifyid";
	public static final String IS_PAY = "is_pay";
	public static final String IS_DELIVERY = "is_delivery";
	public static final String IS_SEAL = "is_seal";
	public static final String PK_BANK = "pk_bank";
	public static final String PK_XMJL = "pk_xmjl";
	public static final String SAX_NO = "sax_no";
	public static final String PK_YSID = "pk_ysid";
	public static final String YS_FLAG = "ys_flag";
	public static final String PK_FUZONG = "pk_fuzong";
	public static final String FUZONG_FLAG = "fuzong_flag";

	public static final String IS_CONEXE = "is_conexe";
	public static final String XM_AMOUNT = "xm_amount";
	public static final String HT_DEPT = "ht_dept";
	public static final String HT_CHANGENUM = "ht_changenum";

	
	
	public Integer getIs_relation() {
		return is_relation;
	}

	public void setIs_relation(Integer is_relation) {
		this.is_relation = is_relation;
	}

	public String getRelationid() {
		return relationid;
	}

	public void setRelationid(String relationid) {
		this.relationid = relationid;
	}

	public UFDouble getCurr_amount() {
		return curr_amount;
	}

	public void setCurr_amount(UFDouble curr_amount) {
		this.curr_amount = curr_amount;
	}

	public UFDouble getCurrenty_rate() {
		return currenty_rate;
	}

	public void setCurrenty_rate(UFDouble currenty_rate) {
		this.currenty_rate = currenty_rate;
	}

	public String getPk_currenty() {
		return pk_currenty;
	}

	public void setPk_currenty(String pk_currenty) {
		this.pk_currenty = pk_currenty;
	}

	public UFDouble getLjskje() {
		return ljskje;
	}

	public void setLjskje(UFDouble ljskje) {
		this.ljskje = ljskje;
	}

	public String getRet_vemo() {
		return ret_vemo;
	}

	public void setRet_vemo(String ret_vemo) {
		this.ret_vemo = ret_vemo;
	}

	public Integer getHt_changenum() {
		return ht_changenum;
	}

	public void setHt_changenum(Integer ht_changenum) {
		this.ht_changenum = ht_changenum;
	}

	public String getHt_dept() {
		return ht_dept;
	}

	public void setHt_dept(String ht_dept) {
		this.ht_dept = ht_dept;
	}

	public UFDouble getXm_amount() {
		return xm_amount;
	}

	public void setXm_amount(UFDouble xm_amount) {
		this.xm_amount = xm_amount;
	}

	public Integer getIs_conexe() {
		return is_conexe;
	}

	public void setIs_conexe(Integer is_conexe) {
		this.is_conexe = is_conexe;
	}

	public String getFuzong_flag() {
		return fuzong_flag;
	}

	public void setFuzong_flag(String fuzong_flag) {
		this.fuzong_flag = fuzong_flag;
	}

	public String getPk_fuzong() {
		return pk_fuzong;
	}

	public void setPk_fuzong(String pk_fuzong) {
		this.pk_fuzong = pk_fuzong;
	}

	public String getPk_ysid() {
		return pk_ysid;
	}

	public void setPk_ysid(String pk_ysid) {
		this.pk_ysid = pk_ysid;
	}

	public String getYs_flag() {
		return ys_flag;
	}

	public void setYs_flag(String ys_flag) {
		this.ys_flag = ys_flag;
	}

	public String getPk_bank() {
		return pk_bank;
	}

	public void setPk_bank(String pk_bank) {
		this.pk_bank = pk_bank;
	}

	public String getPk_xmjl() {
		return pk_xmjl;
	}

	public void setPk_xmjl(String pk_xmjl) {
		this.pk_xmjl = pk_xmjl;
	}

	public String getSax_no() {
		return sax_no;
	}

	public void setSax_no(String sax_no) {
		this.sax_no = sax_no;
	}

	public String getCtcode1() {
		return ctcode1;
	}

	public void setCtcode1(String ctcode1) {
		this.ctcode1 = ctcode1;
	}

			public String getCtcode10() {
				return ctcode10;
	}

			public void setCtcode10(String ctcode10) {
				this.ctcode10 = ctcode10;
	}

			public String getCtcode11() {
				return ctcode11;
	}

			public void setCtcode11(String ctcode11) {
				this.ctcode11 = ctcode11;
	}

			public String getCtcode12() {
				return ctcode12;
	}

			public void setCtcode12(String ctcode12) {
				this.ctcode12 = ctcode12;
	}

			public String getCtcode13() {
				return ctcode13;
	}

			public void setCtcode13(String ctcode13) {
				this.ctcode13 = ctcode13;
	}

			public String getCtcode14() {
				return ctcode14;
	}

			public void setCtcode14(String ctcode14) {
				this.ctcode14 = ctcode14;
	}

			public String getCtcode15() {
				return ctcode15;
	}

			public void setCtcode15(String ctcode15) {
				this.ctcode15 = ctcode15;
	}

			public String getCtcode16() {
				return ctcode16;
	}

			public void setCtcode16(String ctcode16) {
				this.ctcode16 = ctcode16;
	}

			public String getCtcode17() {
				return ctcode17;
	}

			public void setCtcode17(String ctcode17) {
				this.ctcode17 = ctcode17;
	}

			public String getCtcode18() {
				return ctcode18;
	}

			public void setCtcode18(String ctcode18) {
				this.ctcode18 = ctcode18;
	}

			public String getCtcode19() {
				return ctcode19;
	}

			public void setCtcode19(String ctcode19) {
				this.ctcode19 = ctcode19;
	}

			public String getCtcode2() {
				return ctcode2;
	}

			public void setCtcode2(String ctcode2) {
				this.ctcode2 = ctcode2;
	}

			public String getCtcode20() {
				return ctcode20;
	}

			public void setCtcode20(String ctcode20) {
				this.ctcode20 = ctcode20;
	}

			public String getCtcode3() {
				return ctcode3;
	}

			public void setCtcode3(String ctcode3) {
				this.ctcode3 = ctcode3;
	}

			public String getCtcode4() {
				return ctcode4;
	}

			public void setCtcode4(String ctcode4) {
				this.ctcode4 = ctcode4;
	}

			public String getCtcode5() {
				return ctcode5;
	}

			public void setCtcode5(String ctcode5) {
				this.ctcode5 = ctcode5;
	}

			public String getCtcode6() {
				return ctcode6;
	}

			public void setCtcode6(String ctcode6) {
				this.ctcode6 = ctcode6;
	}

			public String getCtcode7() {
				return ctcode7;
	}

			public void setCtcode7(String ctcode7) {
				this.ctcode7 = ctcode7;
	}

			public String getCtcode8() {
				return ctcode8;
	}

			public void setCtcode8(String ctcode8) {
				this.ctcode8 = ctcode8;
	}

			public String getCtcode9() {
				return ctcode9;
	}

			public void setCtcode9(String ctcode9) {
				this.ctcode9 = ctcode9;
	}

			public String getCtname1() {
				return ctname1;
	}

			public void setCtname1(String ctname1) {
				this.ctname1 = ctname1;
	}

			public String getCtname10() {
				return ctname10;
	}

			public void setCtname10(String ctname10) {
				this.ctname10 = ctname10;
	}

			public String getCtname11() {
				return ctname11;
	}

			public void setCtname11(String ctname11) {
				this.ctname11 = ctname11;
	}

			public String getCtname12() {
				return ctname12;
	}

			public void setCtname12(String ctname12) {
				this.ctname12 = ctname12;
	}

			public String getCtname13() {
				return ctname13;
	}

			public void setCtname13(String ctname13) {
				this.ctname13 = ctname13;
	}

			public String getCtname14() {
				return ctname14;
	}

			public void setCtname14(String ctname14) {
				this.ctname14 = ctname14;
	}

			public String getCtname15() {
				return ctname15;
	}

			public void setCtname15(String ctname15) {
				this.ctname15 = ctname15;
	}

	public String getCtname16() {
		return ctname16;
	}

	public void setCtname16(String ctname16) {
		this.ctname16 = ctname16;
	}

			public String getCtname17() {
				return ctname17;
	}

			public void setCtname17(String ctname17) {
				this.ctname17 = ctname17;
	}

			public String getCtname18() {
				return ctname18;
	}

			public void setCtname18(String ctname18) {
				this.ctname18 = ctname18;
	}

			public String getCtname19() {
				return ctname19;
	}

			public void setCtname19(String ctname19) {
				this.ctname19 = ctname19;
	}

			public String getCtname2() {
				return ctname2;
	}

			public void setCtname2(String ctname2) {
				this.ctname2 = ctname2;
	}

			public String getCtname20() {
				return ctname20;
	}

			public void setCtname20(String ctname20) {
				this.ctname20 = ctname20;
	}

			public String getCtname3() {
				return ctname3;
	}

			public void setCtname3(String ctname3) {
				this.ctname3 = ctname3;
	}

			public String getCtname4() {
				return ctname4;
	}

			public void setCtname4(String ctname4) {
				this.ctname4 = ctname4;
	}

			public String getCtname5() {
				return ctname5;
	}

			public void setCtname5(String ctname5) {
				this.ctname5 = ctname5;
	}

			public String getCtname6() {
				return ctname6;
	}

			public void setCtname6(String ctname6) {
				this.ctname6 = ctname6;
	}

			public String getCtname7() {
				return ctname7;
	}

			public void setCtname7(String ctname7) {
				this.ctname7 = ctname7;
	}

			public String getCtname8() {
				return ctname8;
	}

			public void setCtname8(String ctname8) {
				this.ctname8 = ctname8;
	}

			public String getCtname9() {
				return ctname9;
	}

			public void setCtname9(String ctname9) {
				this.ctname9 = ctname9;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getIs_delivery() {
		return is_delivery;
	}

	public void setIs_delivery(Integer is_delivery) {
		this.is_delivery = is_delivery;
	}

	public Integer getIs_pay() {
		return is_pay;
	}

	public void setIs_pay(Integer is_pay) {
		this.is_pay = is_pay;
	}

	public Integer getIs_seal() {
		return is_seal;
	}

	public void setIs_seal(Integer is_seal) {
		this.is_seal = is_seal;
	}

			public String getPk_httk() {
				return pk_httk;
			}

			public void setPk_httk(String pk_httk) {
				this.pk_httk = pk_httk;
			}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	/**
	 * 属性pk_corp的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性pk_corp的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_corp String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 属性vdef4的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}

	/**
	 * 属性vdef4的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef4 String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	}

	/**
	 * 属性pk_cust1的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_cust1() {
		return pk_cust1;
	}

	/**
	 * 属性pk_cust1的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_cust1 String
	 */
	public void setPk_cust1(String newPk_cust1) {

		pk_cust1 = newPk_cust1;
	}

	/**
	 * 属性pk_fzr的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_fzr() {
		return pk_fzr;
	}

	/**
	 * 属性pk_fzr的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_fzr String
	 */
	public void setPk_fzr(String newPk_fzr) {

		pk_fzr = newPk_fzr;
	}

	/**
	 * 属性pk_cttype的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_cttype() {
		return pk_cttype;
	}

	/**
	 * 属性pk_cttype的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_cttype String
	 */
	public void setPk_cttype(String newPk_cttype) {

		pk_cttype = newPk_cttype;
	}

	/**
	 * 属性vdef7的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef7() {
		return vdef7;
	}

	/**
	 * 属性vdef7的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef7 String
	 */
	public void setVdef7(String newVdef7) {

		vdef7 = newVdef7;
	}

	/**
	 * 属性ctcode的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getCtcode() {
		return ctcode;
	}

	/**
	 * 属性ctcode的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newCtcode String
	 */
	public void setCtcode(String newCtcode) {

		ctcode = newCtcode;
	}

	/**
	 * 属性voperatorid的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * 属性voperatorid的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVoperatorid String
	 */
	public void setVoperatorid(String newVoperatorid) {

		voperatorid = newVoperatorid;
	}

	/**
	 * 属性pk_skfs的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_skfs() {
		return pk_skfs;
	}

	/**
	 * 属性pk_skfs的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_skfs String
	 */
	public void setPk_skfs(String newPk_skfs) {

		pk_skfs = newPk_skfs;
	}

	/**
	 * 属性vapprovenote的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVapprovenote() {
		return vapprovenote;
	}

	/**
	 * 属性vapprovenote的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVapprovenote String
	 */
	public void setVapprovenote(String newVapprovenote) {

		vapprovenote = newVapprovenote;
	}

	/**
	 * 属性pk_billtype的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 属性pk_billtype的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_billtype String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * 属性vbillstatus的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return Integer
	 */
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVbillstatus Integer
	 */
	public void setVbillstatus(Integer newVbillstatus) {

		vbillstatus = newVbillstatus;
	}

	/**
	 * 属性vmen的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVmen() {
		return vmen;
	}

	/**
	 * 属性vmen的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVmen String
	 */
	public void setVmen(String newVmen) {

		vmen = newVmen;
	}

	/**
	 * 属性vdef2的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}

	/**
	 * 属性vdef2的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef2 String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	}

	/**
	 * 属性vsourcebilltype的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVsourcebilltype() {
		return vsourcebilltype;
	}

	/**
	 * 属性vsourcebilltype的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVsourcebilltype String
	 */
	public void setVsourcebilltype(String newVsourcebilltype) {

		vsourcebilltype = newVsourcebilltype;
	}

	/**
	 * 属性tmodifytime的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDateTime
	 */
	public String getTmodifytime() {
		return tmodifytime;
	}

	/**
	 * 属性tmodifytime的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newTmodifytime UFDateTime
	 */
	public void setTmodifytime(String newTmodifytime) {

		tmodifytime = newTmodifytime;
	}

	/**
	 * 属性pk_busitype的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_busitype() {
		return pk_busitype;
	}

	/**
	 * 属性pk_busitype的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_busitype String
	 */
	public void setPk_busitype(String newPk_busitype) {

		pk_busitype = newPk_busitype;
	}

	/**
	 * 属性vdef1的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}

	/**
	 * 属性vdef1的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef1 String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	}

	/**
	 * 属性vjhaddress的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVjhaddress() {
		return vjhaddress;
	}

	/**
	 * 属性vjhaddress的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVjhaddress String
	 */
	public void setVjhaddress(String newVjhaddress) {

		vjhaddress = newVjhaddress;
	}

	/**
	 * 属性dstartdate的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDate
	 */
	public UFDate getDstartdate() {
		return dstartdate;
	}

	/**
	 * 属性dstartdate的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDstartdate UFDate
	 */
	public void setDstartdate(UFDate newDstartdate) {

		dstartdate = newDstartdate;
	}

	/**
	 * 属性dctjetotal的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDouble
	 */
	public UFDouble getDctjetotal() {
		return dctjetotal;
	}

	/**
	 * 属性dctjetotal的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDctjetotal UFDouble
	 */
	public void setDctjetotal(UFDouble newDctjetotal) {

		dctjetotal = newDctjetotal;
	}

	/**
	 * 属性dbilldate的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDate
	 */
	public UFDate getDbilldate() {
		return dbilldate;
	}

	/**
	 * 属性dbilldate的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDbilldate UFDate
	 */
	public void setDbilldate(UFDate newDbilldate) {

		dbilldate = newDbilldate;
	}

	/**
	 * 属性vbillno的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVbillno() {
		return vbillno;
	}

	/**
	 * 属性vbillno的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVbillno String
	 */
	public void setVbillno(String newVbillno) {

		vbillno = newVbillno;
	}

	/**
	 * 属性pk_contract的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_contract() {
		return pk_contract;
	}

	/**
	 * 属性pk_contract的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_contract String
	 */
	public void setPk_contract(String newPk_contract) {

		pk_contract = newPk_contract;
	}

	/**
	 * 属性djhdate的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDate
	 */
	public UFDate getDjhdate() {
		return djhdate;
	}

	/**
	 * 属性djhdate的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDjhdate UFDate
	 */
	public void setDjhdate(UFDate newDjhdate) {

		djhdate = newDjhdate;
	}

	/**
	 * 属性pk_deptdoc的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	/**
	 * 属性pk_deptdoc的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_deptdoc String
	 */
	public void setPk_deptdoc(String newPk_deptdoc) {

		pk_deptdoc = newPk_deptdoc;
	}

	/**
	 * 属性vlastbilltype的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVlastbilltype() {
		return vlastbilltype;
	}

	/**
	 * 属性vlastbilltype的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVlastbilltype String
	 */
	public void setVlastbilltype(String newVlastbilltype) {

		vlastbilltype = newVlastbilltype;
	}

	/**
	 * 属性pk_jobmandoc的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_jobmandoc() {
		return pk_jobmandoc;
	}

	/**
	 * 属性pk_jobmandoc的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_jobmandoc String
	 */
	public void setPk_jobmandoc(String newPk_jobmandoc) {

		pk_jobmandoc = newPk_jobmandoc;
	}

	/**
	 * 属性vdef3的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}

	/**
	 * 属性vdef3的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef3 String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	}

	/**
	 * 属性vdef9的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef9() {
		return vdef9;
	}

	/**
	 * 属性vdef9的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef9 String
	 */
	public void setVdef9(String newVdef9) {

		vdef9 = newVdef9;
	}

	/**
	 * 属性dapprovedate的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDate
	 */
	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	/**
	 * 属性dapprovedate的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDapprovedate UFDate
	 */
	public void setDapprovedate(UFDate newDapprovedate) {

		dapprovedate = newDapprovedate;
	}

	/**
	 * 属性vdef8的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef8() {
		return vdef8;
	}

	/**
	 * 属性vdef8的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef8 String
	 */
	public void setVdef8(String newVdef8) {

		vdef8 = newVdef8;
	}

	/**
	 * 属性pk_cust2的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPk_cust2() {
		return pk_cust2;
	}

	/**
	 * 属性pk_cust2的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newPk_cust2 String
	 */
	public void setPk_cust2(String newPk_cust2) {

		pk_cust2 = newPk_cust2;
	}

	/**
	 * 属性vlastbillid的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVlastbillid() {
		return vlastbillid;
	}

	/**
	 * 属性vlastbillid的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVlastbillid String
	 */
	public void setVlastbillid(String newVlastbillid) {

		vlastbillid = newVlastbillid;
	}

	/**
	 * 属性vapproveid的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVapproveid() {
		return vapproveid;
	}

	/**
	 * 属性vapproveid的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVapproveid String
	 */
	public void setVapproveid(String newVapproveid) {

		vapproveid = newVapproveid;
	}

	/**
	 * 属性vdef6的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef6() {
		return vdef6;
	}

	/**
	 * 属性vdef6的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef6 String
	 */
	public void setVdef6(String newVdef6) {

		vdef6 = newVdef6;
	}

	/**
	 * 属性dcaigtotal的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDouble
	 */
	public UFDouble getDcaigtotal() {
		return dcaigtotal;
	}

	/**
	 * 属性dcaigtotal的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDcaigtotal UFDouble
	 */
	public void setDcaigtotal(UFDouble newDcaigtotal) {

		dcaigtotal = newDcaigtotal;
	}

	/**
	 * 属性vdef10的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef10() {
		return vdef10;
	}

	/**
	 * 属性vdef10的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef10 String
	 */
	public void setVdef10(String newVdef10) {

		vdef10 = newVdef10;
	}

	/**
	 * 属性vsourcebillid的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVsourcebillid() {
		return vsourcebillid;
	}

	/**
	 * 属性vsourcebillid的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVsourcebillid String
	 */
	public void setVsourcebillid(String newVsourcebillid) {

		vsourcebillid = newVsourcebillid;
	}

	/**
	 * 属性vdef5的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}

	/**
	 * 属性vdef5的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newVdef5 String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	}

	/**
	 * 属性dsaletotal的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return UFDouble
	 */
	public UFDouble getDsaletotal() {
		return dsaletotal;
	}

	/**
	 * 属性dsaletotal的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newDsaletotal UFDouble
	 */
	public void setDsaletotal(UFDouble newDsaletotal) {

		dsaletotal = newDsaletotal;
	}

	/**
	 * 属性ctname的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getCtname() {
		return ctname;
	}

	/**
	 * 属性ctname的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newCtname String
	 */
	public void setCtname(String newCtname) {

		ctname = newCtname;
	}

	/**
	 * 属性cmodifyid的Getter方法.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getCmodifyid() {
		return cmodifyid;
	}

	/**
	 * 属性cmodifyid的Setter方法.
	 * 
	 * 创建日期:2013-10-6
	   * @param newCmodifyid String
	 */
	public void setCmodifyid(String newCmodifyid) {

		cmodifyid = newCmodifyid;
	}

       
	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2013-10-6
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_contract == null) {
			errFields.add(new String("pk_contract"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

       
	/**
	  * <p>取得父VO主键字段.
	 * <p>
	 * 创建日期:2013-10-6
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_contract";

	}

	/**
	  * <p>取得表主键.
	 * <p>
	 * 创建日期:2013-10-6
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_contract";
	}

	/**
      * <p>返回表名称.
	 * <p>
	 * 创建日期:2013-10-6
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "dh_contract";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2013-10-6
	 */
	public DhContractVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2013-10-6
	 * @param newPk_contract 主键值
	 */
	public DhContractVO(String newPk_contract) {

		// 为主键字段赋值:
		pk_contract = newPk_contract;

	}

     
	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2013-10-6
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_contract;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2013-10-6
	  * @param newPk_contract  String    
	 */
	public void setPrimaryKey(String newPk_contract) {

		pk_contract = newPk_contract;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2013-10-6
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "dh_contract";

	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getCustname1() {
		return custname1;
	}

	public void setCustname1(String custname1) {
		this.custname1 = custname1;
	}

	public String getCustname2() {
		return custname2;
	}

	public void setCustname2(String custname2) {
		this.custname2 = custname2;
	}

	public String getHtaddress() {
		return htaddress;
	}

	public void setHtaddress(String htaddress) {
		this.htaddress = htaddress;
	}

	public UFDate getHtrq() {
		return htrq;
	}

	public void setHtrq(UFDate htrq) {
		this.htrq = htrq;
	}

	public Integer getHttype() {
		return httype;
	}

	public void setHttype(Integer httype) {
		this.httype = httype;
	}

	public String getJobcode() {
		return jobcode;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public UFDouble getLjfkjhje() {
		return ljfkjhje;
	}

	public void setLjfkjhje(UFDouble ljfkjhje) {
		this.ljfkjhje = ljfkjhje;
	}

	public UFDouble getLjsjfkje() {
		return ljsjfkje;
	}

	public void setLjsjfkje(UFDouble ljsjfkje) {
		this.ljsjfkje = ljsjfkje;
	}

	public UFDouble getSjspje() {
		return sjspje;
	}

	public void setSjspje(UFDouble sjspje) {
		this.sjspje = sjspje;
	}

	public UFDouble getSjfkbl() {
		return sjfkbl;
	}

	public void setSjfkbl(UFDouble sjfkbl) {
		this.sjfkbl = sjfkbl;
	}

}
