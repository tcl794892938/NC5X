package nc.vo.arap.sedgather.js;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class DjjsBXHVO extends SuperVO{
	//	����
	private String pk_settlement;
	//¼����
	private String pk_operator;

	private Integer effectstatus;
	//ҵ�񵥾�����
	private String pk_busibill;
	//����ϵͳ
	private String systemcode;
	//����ҵ������
	private String pk_busitype;
	//ҵ�񵥵�������
	private String pk_tradetype;
	//ҵ�񵥾ݱ��
	private String billcode;
	//ҵ�񵥾�¼����
	private String pk_billoperator;
	//ҵ�񵥾�����
	private UFDate busi_billdate;
	//ҵ�񵥾��������
	private UFDate busi_auditdate;
	//ҵ�񵥾������
	private String pk_auditor;
	//ҵ�񵥾�״̬
	private Integer busistatus;
	//ҵ�񵥾��Ƿ�����Ч
	private String isbusieffect;
	//ǩ��ȷ������
	private UFDate signdate;
	//ǩ��ȷ����
	private String pk_signer;
	//�����
	private String setlleno;
	//��������
	private UFDate setlledate;
	//����ȷ����
	private String pk_executor;
	//����״̬
	private Integer settlestatus;
	//���㵥�Ƿ���Ч
	private String issettleeffect ;
	//��˾
	private String pk_corp;
	//�Ƿ�ҵ�����
	private String isbusicontrol;
	//�Ƿ����ƽ̨�ɴ���
	private String iscmcandeal;
	//������֯
	private String pk_finorg;
	//֧����ʽ
	private Integer payway;
	//�Ƿ��Զ�ǩ��
	private String isautosign;
	//�Ƿ��Զ�֧��
	private String isautopay;
	//�ո����뵥
	private String isapplybill;
	//ί���ո��������
	private String fts_billtype;
	//ί���ո������
	private String pk_ftsbill;
	//�Ƿ��Զ�����
	private String isautogenerate;
	//����������
	private UFDate lastupdatedate;
	//��������
	private String lastupdater;

	private Integer arithmetic;

	private String ispay;

	private Integer direction;

	private UFDateTime ts;

	private Integer dr;

	private String def1;

	private String def2;

	private String def3;

	private String def4;

	private String def5;

	private String def6;

	private String def7;

	private String def8;

	private String def9;

	private String def10;

	private String def11;

	private String def12;

	private String def13;

	private String def14;

	private String def15;

	private String def16;

	private String def17;

	private String def18;

	private String def19;

	private String def20;

	private Integer settletype;
	//����״̬
	private Integer aduitstatus ;

	private UFDouble primal;
	
	private UFDouble local;
	
	

	public Integer getAduitstatus() {
		return aduitstatus;
	}

	public void setAduitstatus(Integer aduitstatus) {
		this.aduitstatus = aduitstatus;
	}

	public Integer getArithmetic() {
		return arithmetic;
	}

	public void setArithmetic(Integer arithmetic) {
		this.arithmetic = arithmetic;
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public UFDate getBusi_auditdate() {
		return busi_auditdate;
	}

	public void setBusi_auditdate(UFDate busi_auditdate) {
		this.busi_auditdate = busi_auditdate;
	}

	public UFDate getBusi_billdate() {
		return busi_billdate;
	}

	public void setBusi_billdate(UFDate busi_billdate) {
		this.busi_billdate = busi_billdate;
	}

	public Integer getBusistatus() {
		return busistatus;
	}

	public void setBusistatus(Integer busistatus) {
		this.busistatus = busistatus;
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

	public Integer getEffectstatus() {
		return effectstatus;
	}

	public void setEffectstatus(Integer effectstatus) {
		this.effectstatus = effectstatus;
	}

	public String getFts_billtype() {
		return fts_billtype;
	}

	public void setFts_billtype(String fts_billtype) {
		this.fts_billtype = fts_billtype;
	}

	public String getIsapplybill() {
		return isapplybill;
	}

	public void setIsapplybill(String isapplybill) {
		this.isapplybill = isapplybill;
	}

	public String getIsautogenerate() {
		return isautogenerate;
	}

	public void setIsautogenerate(String isautogenerate) {
		this.isautogenerate = isautogenerate;
	}

	public String getIsautopay() {
		return isautopay;
	}

	public void setIsautopay(String isautopay) {
		this.isautopay = isautopay;
	}

	public String getIsautosign() {
		return isautosign;
	}

	public void setIsautosign(String isautosign) {
		this.isautosign = isautosign;
	}

	public String getIsbusicontrol() {
		return isbusicontrol;
	}

	public void setIsbusicontrol(String isbusicontrol) {
		this.isbusicontrol = isbusicontrol;
	}

	public String getIsbusieffect() {
		return isbusieffect;
	}

	public void setIsbusieffect(String isbusieffect) {
		this.isbusieffect = isbusieffect;
	}

	public String getIscmcandeal() {
		return iscmcandeal;
	}

	public void setIscmcandeal(String iscmcandeal) {
		this.iscmcandeal = iscmcandeal;
	}

	public String getIspay() {
		return ispay;
	}

	public void setIspay(String ispay) {
		this.ispay = ispay;
	}

	public String getIssettleeffect() {
		return issettleeffect;
	}

	public void setIssettleeffect(String issettleeffect) {
		this.issettleeffect = issettleeffect;
	}

	public UFDate getLastupdatedate() {
		return lastupdatedate;
	}

	public void setLastupdatedate(UFDate lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public String getLastupdater() {
		return lastupdater;
	}

	public void setLastupdater(String lastupdater) {
		this.lastupdater = lastupdater;
	}

	public UFDouble getLocal() {
		return local;
	}

	public void setLocal(UFDouble local) {
		this.local = local;
	}

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	public String getPk_auditor() {
		return pk_auditor;
	}

	public void setPk_auditor(String pk_auditor) {
		this.pk_auditor = pk_auditor;
	}

	public String getPk_billoperator() {
		return pk_billoperator;
	}

	public void setPk_billoperator(String pk_billoperator) {
		this.pk_billoperator = pk_billoperator;
	}

	public String getPk_busibill() {
		return pk_busibill;
	}

	public void setPk_busibill(String pk_busibill) {
		this.pk_busibill = pk_busibill;
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

	public String getPk_executor() {
		return pk_executor;
	}

	public void setPk_executor(String pk_executor) {
		this.pk_executor = pk_executor;
	}

	public String getPk_finorg() {
		return pk_finorg;
	}

	public void setPk_finorg(String pk_finorg) {
		this.pk_finorg = pk_finorg;
	}

	public String getPk_ftsbill() {
		return pk_ftsbill;
	}

	public void setPk_ftsbill(String pk_ftsbill) {
		this.pk_ftsbill = pk_ftsbill;
	}

	public String getPk_operator() {
		return pk_operator;
	}

	public void setPk_operator(String pk_operator) {
		this.pk_operator = pk_operator;
	}

	public String getPk_settlement() {
		return pk_settlement;
	}

	public void setPk_settlement(String pk_settlement) {
		this.pk_settlement = pk_settlement;
	}

	public String getPk_signer() {
		return pk_signer;
	}

	public void setPk_signer(String pk_signer) {
		this.pk_signer = pk_signer;
	}

	public String getPk_tradetype() {
		return pk_tradetype;
	}

	public void setPk_tradetype(String pk_tradetype) {
		this.pk_tradetype = pk_tradetype;
	}

	public UFDouble getPrimal() {
		return primal;
	}

	public void setPrimal(UFDouble primal) {
		this.primal = primal;
	}

	public UFDate getSetlledate() {
		return setlledate;
	}

	public void setSetlledate(UFDate setlledate) {
		this.setlledate = setlledate;
	}

	public String getSetlleno() {
		return setlleno;
	}

	public void setSetlleno(String setlleno) {
		this.setlleno = setlleno;
	}

	public Integer getSettlestatus() {
		return settlestatus;
	}

	public void setSettlestatus(Integer settlestatus) {
		this.settlestatus = settlestatus;
	}

	public Integer getSettletype() {
		return settletype;
	}

	public void setSettletype(Integer settletype) {
		this.settletype = settletype;
	}

	public UFDate getSigndate() {
		return signdate;
	}

	public void setSigndate(UFDate signdate) {
		this.signdate = signdate;
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_settlement";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "cmp_settlement";
	}
	

}
