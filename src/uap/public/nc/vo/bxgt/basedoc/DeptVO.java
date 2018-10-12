package nc.vo.bxgt.basedoc;
import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
/**
 *
 *
 * 创建日期:(2005-3-28)
 * @author:
 */
public class DeptVO extends SuperVO {

	public String pk_deptdoc;
	public String addr;
	public UFDate canceldate;
	public UFBoolean canceled;
	public UFDate createdate;
	public String def1;
	public String def2;
	public String def3;
	public String def4;
	public String def5;
	public String deptattr;
	public String deptcode;
	public String deptduty;
	public String deptlevel;
	public String deptname;
	public Integer depttype;
	public Integer dr;
	public UFBoolean hrcanceled;
	public String innercode;
	public UFBoolean isuseretail;
	public String maxinnercode;
	public String memo;
	public Integer orgtype;
	public String phone;
	public String pk_calbody;
	public String pk_corp;
	public String pk_fathedept;
	public String pk_psndoc;
	public String remcode;
	public String resposition;
	public UFDateTime ts;
	public Integer xtersysflag;
/**
 * 属性addr的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getAddr() {
	return addr;
}
/**
 * 属性canceldate的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return UFDate
 */
public UFDate getCanceldate() {
	return canceldate;
}
/**
 * 属性canceled的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return UFBoolean
 */
public UFBoolean getCanceled() {
	return canceled;
}
/**
 * 属性createdate的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return UFDate
 */
public UFDate getCreatedate() {
	return createdate;
}
/**
 * 属性def1的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDef1() {
	return def1;
}
/**
 * 属性def2的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDef2() {
	return def2;
}
/**
 * 属性def3的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDef3() {
	return def3;
}
/**
 * 属性def4的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDef4() {
	return def4;
}
/**
 * 属性def5的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDef5() {
	return def5;
}
/**
 * 属性deptattr的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDeptattr() {
	return deptattr;
}
/**
 * 属性deptcode的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDeptcode() {
	return deptcode;
}
/**
 * 属性deptduty的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDeptduty() {
	return deptduty;
}
/**
 * 属性deptlevel的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDeptlevel() {
	return deptlevel;
}
/**
 * 属性deptname的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getDeptname() {
	return deptname;
}
/**
 * 属性depttype的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return Integer
 */
public Integer getDepttype() {
	return depttype;
}
/**
 * 属性dr的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return Integer
 */
public Integer getDr() {
	return dr;
}
/**
 * 属性hrcanceled的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return UFBoolean
 */
public UFBoolean getHrcanceled() {
	return hrcanceled;
}
/**
 * 属性innercode的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getInnercode() {
	return innercode;
}
/**
 * 属性maxinnercode的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getMaxinnercode() {
	return maxinnercode;
}
/**
 * 属性memo的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getMemo() {
	return memo;
}
/**
 * 属性orgtype的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return Integer
 */
public Integer getOrgtype() {
	return orgtype;
}
/**
 * 属性phone的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPhone() {
	return phone;
}
/**
 * 属性pk_calbody的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPk_calbody() {
	return pk_calbody;
}
/**
 * 属性pk_corp的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPk_corp() {
	return pk_corp;
}
/**
 * 属性pk_fathedept的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPk_fathedept() {
	return pk_fathedept;
}
/**
 * 属性pk_psndoc的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * 属性remcode的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getRemcode() {
	return remcode;
}
/**
 * 属性resposition的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getResposition() {
	return resposition;
}
/**
 * 属性ts的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return UFDateTime
 */
public UFDateTime getTs() {
	return ts;
}
/**
 * 属性xtersysflag的Getter方法.
 *
 * 创建日期:(2005-3-28)
 * @return Integer
 */
public Integer getXtersysflag() {
	return xtersysflag;
}
/**
 * 属性addr的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newAddr String
 */
public void setAddr(String newAddr) {

	addr = newAddr;
}
/**
 * 属性canceldate的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newCanceldate UFDate
 */
public void setCanceldate(UFDate newCanceldate) {

	canceldate = newCanceldate;
}
/**
 * 属性canceled的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newCanceled UFBoolean
 */
public void setCanceled(UFBoolean newCanceled) {

	canceled = newCanceled;
}
/**
 * 属性createdate的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newCreatedate UFDate
 */
public void setCreatedate(UFDate newCreatedate) {

	createdate = newCreatedate;
}
/**
 * 属性def1的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDef1 String
 */
public void setDef1(String newDef1) {

	def1 = newDef1;
}
/**
 * 属性def2的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDef2 String
 */
public void setDef2(String newDef2) {

	def2 = newDef2;
}
/**
 * 属性def3的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDef3 String
 */
public void setDef3(String newDef3) {

	def3 = newDef3;
}
/**
 * 属性def4的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDef4 String
 */
public void setDef4(String newDef4) {

	def4 = newDef4;
}
/**
 * 属性def5的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDef5 String
 */
public void setDef5(String newDef5) {

	def5 = newDef5;
}
/**
 * 属性deptattr的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDeptattr String
 */
public void setDeptattr(String newDeptattr) {

	deptattr = newDeptattr;
}
/**
 * 属性deptcode的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDeptcode String
 */
public void setDeptcode(String newDeptcode) {

	deptcode = newDeptcode;
}
/**
 * 属性deptduty的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDeptduty String
 */
public void setDeptduty(String newDeptduty) {

	deptduty = newDeptduty;
}
/**
 * 属性deptlevel的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDeptlevel String
 */
public void setDeptlevel(String newDeptlevel) {

	deptlevel = newDeptlevel;
}
/**
 * 属性deptname的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDeptname String
 */
public void setDeptname(String newDeptname) {

	deptname = newDeptname;
}
/**
 * 属性depttype的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDepttype Integer
 */
public void setDepttype(Integer newDepttype) {

	depttype = newDepttype;
}
/**
 * 属性dr的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newDr Integer
 */
public void setDr(Integer newDr) {

	dr = newDr;
}
/**
 * 属性hrcanceled的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newHrcanceled UFBoolean
 */
public void setHrcanceled(UFBoolean newHrcanceled) {

	hrcanceled = newHrcanceled;
}
/**
 * 属性innercode的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newInnercode String
 */
public void setInnercode(String newInnercode) {

	innercode = newInnercode;
}
/**
 * 属性maxinnercode的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newMaxinnercode String
 */
public void setMaxinnercode(String newMaxinnercode) {

	maxinnercode = newMaxinnercode;
}
/**
 * 属性memo的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newMemo String
 */
public void setMemo(String newMemo) {

	memo = newMemo;
}
/**
 * 属性orgtype的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newOrgtype Integer
 */
public void setOrgtype(Integer newOrgtype) {

	orgtype = newOrgtype;
}
/**
 * 属性phone的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newPhone String
 */
public void setPhone(String newPhone) {

	phone = newPhone;
}
/**
 * 属性pk_calbody的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newPk_calbody String
 */
public void setPk_calbody(String newPk_calbody) {

	pk_calbody = newPk_calbody;
}
/**
 * 属性pk_corp的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newPk_corp String
 */
public void setPk_corp(String newPk_corp) {

	pk_corp = newPk_corp;
}
/**
 * 属性pk_fathedept的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newPk_fathedept String
 */
public void setPk_fathedept(String newPk_fathedept) {

	pk_fathedept = newPk_fathedept;
}
/**
 * 属性pk_psndoc的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newPk_psndoc String
 */
public void setPk_psndoc(String newPk_psndoc) {

	pk_psndoc = newPk_psndoc;
}
/**
 * 属性remcode的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newRemcode String
 */
public void setRemcode(String newRemcode) {

	remcode = newRemcode;
}
/**
 * 属性resposition的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newResposition String
 */
public void setResposition(String newResposition) {

	resposition = newResposition;
}
/**
 * 属性ts的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newTs UFDateTime
 */
public void setTs(UFDateTime newTs) {

	ts = newTs;
}
/**
 * 属性xtersysflag的setter方法.
 *
 * 创建日期:(2005-3-28)
 * @param newXtersysflag Integer
 */
public void setXtersysflag(Integer newXtersysflag) {

	xtersysflag = newXtersysflag;
}
/**
 * 验证对象各属性之间的数据逻辑正确性.
 *
 * 创建日期:(2005-3-28)
 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
 *     ValidationException,对错误进行解释.
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	if (canceled == null) {
		errFields.add(new String("canceled"));
	}
	if (deptattr == null) {
		errFields.add(new String("deptattr"));
	}
	if (deptcode == null) {
		errFields.add(new String("deptcode"));
	}
	if (deptname == null) {
		errFields.add(new String("deptname"));
	}
	if (hrcanceled == null) {
		errFields.add(new String("hrcanceled"));
	}
	if (pk_corp == null) {
		errFields.add(new String("pk_corp"));
	}
	if (pk_deptdoc == null) {
		errFields.add(new String("pk_deptdoc"));
	}
	// construct the exception message:
	StringBuffer message = new StringBuffer();
	message.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202","UPP10082202-000018")/*@res "下列字段不能为空:"*/);
	if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append(",");
			message.append(temp[i]);
		}
		// throw the exception:
		throw new NullFieldException(message.toString());
	}
}
/**
 * <p>取得父VO主键字段.
 * <p>
 * 创建日期:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getParentPKFieldName() {

	return  null;
}
/**
 * <p>取得表主键.
 * <p>
 * 创建日期:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getPKFieldName() {

	return  "pk_deptdoc";
}
/**
 * <p>返回表名称.
 * <p>
 * 创建日期:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getTableName() {

	return "bd_deptdoc";
}
/**
 * 使用主键字段进行初始化的构造子.
 *
 * 创建日期:(2005-3-28)
 */
public DeptVO() {
	super();
}
/**
 * 使用主键进行初始化的构造子.
 *
 * 创建日期:(2005-3-28)
 * @param Pk_deptdoc 主键值
 */
public DeptVO(String newPk_deptdoc) {
	super();

	// 为主键字段赋值:
	pk_deptdoc = newPk_deptdoc;
}
/**
 * 返回对象标识,用来唯一定位对象.
 *
 * 创建日期:(2005-3-28)
 * @return String
 */
public String getPrimaryKey() {

	return pk_deptdoc;
}
/**
 * 设置对象标识,用来唯一定位对象.
 *
 * 创建日期:(2005-3-28)
 * @param pk_deptdoc String
 */
public void setPrimaryKey(String newPk_deptdoc) {

	pk_deptdoc = newPk_deptdoc;
}
/**
 * 返回数值对象的显示名称.
 *
 * 创建日期:(2005-3-28)
 * @return java.lang.String 返回数值对象的显示名称.
 */
public String getEntityName() {

	return "Deptdoc";
}

	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getDeptname();
	}
	public UFBoolean getIsuseretail() {
		return isuseretail;
	}
	public void setIsuseretail(UFBoolean isuseretail) {
		this.isuseretail = isuseretail;
	}
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}
	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}
}