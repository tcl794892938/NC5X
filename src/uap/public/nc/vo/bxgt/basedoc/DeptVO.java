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
 * ��������:(2005-3-28)
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
 * ����addr��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getAddr() {
	return addr;
}
/**
 * ����canceldate��Getter����.
 *
 * ��������:(2005-3-28)
 * @return UFDate
 */
public UFDate getCanceldate() {
	return canceldate;
}
/**
 * ����canceled��Getter����.
 *
 * ��������:(2005-3-28)
 * @return UFBoolean
 */
public UFBoolean getCanceled() {
	return canceled;
}
/**
 * ����createdate��Getter����.
 *
 * ��������:(2005-3-28)
 * @return UFDate
 */
public UFDate getCreatedate() {
	return createdate;
}
/**
 * ����def1��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDef1() {
	return def1;
}
/**
 * ����def2��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDef2() {
	return def2;
}
/**
 * ����def3��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDef3() {
	return def3;
}
/**
 * ����def4��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDef4() {
	return def4;
}
/**
 * ����def5��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDef5() {
	return def5;
}
/**
 * ����deptattr��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDeptattr() {
	return deptattr;
}
/**
 * ����deptcode��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDeptcode() {
	return deptcode;
}
/**
 * ����deptduty��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDeptduty() {
	return deptduty;
}
/**
 * ����deptlevel��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDeptlevel() {
	return deptlevel;
}
/**
 * ����deptname��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getDeptname() {
	return deptname;
}
/**
 * ����depttype��Getter����.
 *
 * ��������:(2005-3-28)
 * @return Integer
 */
public Integer getDepttype() {
	return depttype;
}
/**
 * ����dr��Getter����.
 *
 * ��������:(2005-3-28)
 * @return Integer
 */
public Integer getDr() {
	return dr;
}
/**
 * ����hrcanceled��Getter����.
 *
 * ��������:(2005-3-28)
 * @return UFBoolean
 */
public UFBoolean getHrcanceled() {
	return hrcanceled;
}
/**
 * ����innercode��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getInnercode() {
	return innercode;
}
/**
 * ����maxinnercode��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getMaxinnercode() {
	return maxinnercode;
}
/**
 * ����memo��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getMemo() {
	return memo;
}
/**
 * ����orgtype��Getter����.
 *
 * ��������:(2005-3-28)
 * @return Integer
 */
public Integer getOrgtype() {
	return orgtype;
}
/**
 * ����phone��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPhone() {
	return phone;
}
/**
 * ����pk_calbody��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPk_calbody() {
	return pk_calbody;
}
/**
 * ����pk_corp��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPk_corp() {
	return pk_corp;
}
/**
 * ����pk_fathedept��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPk_fathedept() {
	return pk_fathedept;
}
/**
 * ����pk_psndoc��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * ����remcode��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getRemcode() {
	return remcode;
}
/**
 * ����resposition��Getter����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getResposition() {
	return resposition;
}
/**
 * ����ts��Getter����.
 *
 * ��������:(2005-3-28)
 * @return UFDateTime
 */
public UFDateTime getTs() {
	return ts;
}
/**
 * ����xtersysflag��Getter����.
 *
 * ��������:(2005-3-28)
 * @return Integer
 */
public Integer getXtersysflag() {
	return xtersysflag;
}
/**
 * ����addr��setter����.
 *
 * ��������:(2005-3-28)
 * @param newAddr String
 */
public void setAddr(String newAddr) {

	addr = newAddr;
}
/**
 * ����canceldate��setter����.
 *
 * ��������:(2005-3-28)
 * @param newCanceldate UFDate
 */
public void setCanceldate(UFDate newCanceldate) {

	canceldate = newCanceldate;
}
/**
 * ����canceled��setter����.
 *
 * ��������:(2005-3-28)
 * @param newCanceled UFBoolean
 */
public void setCanceled(UFBoolean newCanceled) {

	canceled = newCanceled;
}
/**
 * ����createdate��setter����.
 *
 * ��������:(2005-3-28)
 * @param newCreatedate UFDate
 */
public void setCreatedate(UFDate newCreatedate) {

	createdate = newCreatedate;
}
/**
 * ����def1��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDef1 String
 */
public void setDef1(String newDef1) {

	def1 = newDef1;
}
/**
 * ����def2��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDef2 String
 */
public void setDef2(String newDef2) {

	def2 = newDef2;
}
/**
 * ����def3��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDef3 String
 */
public void setDef3(String newDef3) {

	def3 = newDef3;
}
/**
 * ����def4��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDef4 String
 */
public void setDef4(String newDef4) {

	def4 = newDef4;
}
/**
 * ����def5��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDef5 String
 */
public void setDef5(String newDef5) {

	def5 = newDef5;
}
/**
 * ����deptattr��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDeptattr String
 */
public void setDeptattr(String newDeptattr) {

	deptattr = newDeptattr;
}
/**
 * ����deptcode��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDeptcode String
 */
public void setDeptcode(String newDeptcode) {

	deptcode = newDeptcode;
}
/**
 * ����deptduty��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDeptduty String
 */
public void setDeptduty(String newDeptduty) {

	deptduty = newDeptduty;
}
/**
 * ����deptlevel��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDeptlevel String
 */
public void setDeptlevel(String newDeptlevel) {

	deptlevel = newDeptlevel;
}
/**
 * ����deptname��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDeptname String
 */
public void setDeptname(String newDeptname) {

	deptname = newDeptname;
}
/**
 * ����depttype��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDepttype Integer
 */
public void setDepttype(Integer newDepttype) {

	depttype = newDepttype;
}
/**
 * ����dr��setter����.
 *
 * ��������:(2005-3-28)
 * @param newDr Integer
 */
public void setDr(Integer newDr) {

	dr = newDr;
}
/**
 * ����hrcanceled��setter����.
 *
 * ��������:(2005-3-28)
 * @param newHrcanceled UFBoolean
 */
public void setHrcanceled(UFBoolean newHrcanceled) {

	hrcanceled = newHrcanceled;
}
/**
 * ����innercode��setter����.
 *
 * ��������:(2005-3-28)
 * @param newInnercode String
 */
public void setInnercode(String newInnercode) {

	innercode = newInnercode;
}
/**
 * ����maxinnercode��setter����.
 *
 * ��������:(2005-3-28)
 * @param newMaxinnercode String
 */
public void setMaxinnercode(String newMaxinnercode) {

	maxinnercode = newMaxinnercode;
}
/**
 * ����memo��setter����.
 *
 * ��������:(2005-3-28)
 * @param newMemo String
 */
public void setMemo(String newMemo) {

	memo = newMemo;
}
/**
 * ����orgtype��setter����.
 *
 * ��������:(2005-3-28)
 * @param newOrgtype Integer
 */
public void setOrgtype(Integer newOrgtype) {

	orgtype = newOrgtype;
}
/**
 * ����phone��setter����.
 *
 * ��������:(2005-3-28)
 * @param newPhone String
 */
public void setPhone(String newPhone) {

	phone = newPhone;
}
/**
 * ����pk_calbody��setter����.
 *
 * ��������:(2005-3-28)
 * @param newPk_calbody String
 */
public void setPk_calbody(String newPk_calbody) {

	pk_calbody = newPk_calbody;
}
/**
 * ����pk_corp��setter����.
 *
 * ��������:(2005-3-28)
 * @param newPk_corp String
 */
public void setPk_corp(String newPk_corp) {

	pk_corp = newPk_corp;
}
/**
 * ����pk_fathedept��setter����.
 *
 * ��������:(2005-3-28)
 * @param newPk_fathedept String
 */
public void setPk_fathedept(String newPk_fathedept) {

	pk_fathedept = newPk_fathedept;
}
/**
 * ����pk_psndoc��setter����.
 *
 * ��������:(2005-3-28)
 * @param newPk_psndoc String
 */
public void setPk_psndoc(String newPk_psndoc) {

	pk_psndoc = newPk_psndoc;
}
/**
 * ����remcode��setter����.
 *
 * ��������:(2005-3-28)
 * @param newRemcode String
 */
public void setRemcode(String newRemcode) {

	remcode = newRemcode;
}
/**
 * ����resposition��setter����.
 *
 * ��������:(2005-3-28)
 * @param newResposition String
 */
public void setResposition(String newResposition) {

	resposition = newResposition;
}
/**
 * ����ts��setter����.
 *
 * ��������:(2005-3-28)
 * @param newTs UFDateTime
 */
public void setTs(UFDateTime newTs) {

	ts = newTs;
}
/**
 * ����xtersysflag��setter����.
 *
 * ��������:(2005-3-28)
 * @param newXtersysflag Integer
 */
public void setXtersysflag(Integer newXtersysflag) {

	xtersysflag = newXtersysflag;
}
/**
 * ��֤���������֮��������߼���ȷ��.
 *
 * ��������:(2005-3-28)
 * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
 *     ValidationException,�Դ�����н���.
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
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
	message.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202","UPP10082202-000018")/*@res "�����ֶβ���Ϊ��:"*/);
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
 * <p>ȡ�ø�VO�����ֶ�.
 * <p>
 * ��������:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getParentPKFieldName() {

	return  null;
}
/**
 * <p>ȡ�ñ�����.
 * <p>
 * ��������:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getPKFieldName() {

	return  "pk_deptdoc";
}
/**
 * <p>���ر�����.
 * <p>
 * ��������:(2005-3-28)
 * @return java.lang.String
 */
public java.lang.String getTableName() {

	return "bd_deptdoc";
}
/**
 * ʹ�������ֶν��г�ʼ���Ĺ�����.
 *
 * ��������:(2005-3-28)
 */
public DeptVO() {
	super();
}
/**
 * ʹ���������г�ʼ���Ĺ�����.
 *
 * ��������:(2005-3-28)
 * @param Pk_deptdoc ����ֵ
 */
public DeptVO(String newPk_deptdoc) {
	super();

	// Ϊ�����ֶθ�ֵ:
	pk_deptdoc = newPk_deptdoc;
}
/**
 * ���ض����ʶ,����Ψһ��λ����.
 *
 * ��������:(2005-3-28)
 * @return String
 */
public String getPrimaryKey() {

	return pk_deptdoc;
}
/**
 * ���ö����ʶ,����Ψһ��λ����.
 *
 * ��������:(2005-3-28)
 * @param pk_deptdoc String
 */
public void setPrimaryKey(String newPk_deptdoc) {

	pk_deptdoc = newPk_deptdoc;
}
/**
 * ������ֵ�������ʾ����.
 *
 * ��������:(2005-3-28)
 * @return java.lang.String ������ֵ�������ʾ����.
 */
public String getEntityName() {

	return "Deptdoc";
}

	/* ���� Javadoc��
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