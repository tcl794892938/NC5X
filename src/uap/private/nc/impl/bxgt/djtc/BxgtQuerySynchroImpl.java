package nc.impl.bxgt.djtc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.scm.pub.CommonDataDMO;
import nc.itf.bxgt.billtype.IBxgtBillType;
import nc.itf.bxgt.djtc.IbxgtQuerySynchro;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ProcessorUtils;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.bxgt.pub.BFPubTool;
import nc.vo.arap.sedgather.js.DjjsBXBVO;
import nc.vo.arap.sedgather.js.DjjsBXHVO;
import nc.vo.arap.sedgather.sk.DjskBXBVO;
import nc.vo.arap.sedgather.sk.DjskBXHVO;
import nc.vo.bxgt.basedoc.CubasdocVO;
import nc.vo.bxgt.basedoc.CumandocVO;
import nc.vo.bxgt.ccp.BxgtAccountVO;
import nc.vo.bxgt.ccp.BxgtCcpBodyVo;
import nc.vo.bxgt.ccp.BxgtCcpHeadVO;
import nc.vo.bxgt.ccp.BxgtIabillBVO;
import nc.vo.bxgt.ccp.BxgtIabillHVO;
import nc.vo.bxgt.ccp.BxgtSpaceVo;
import nc.vo.bxgt.ccp.SaleOrderBVO;
import nc.vo.bxgt.ccp.SaleOrderExecuteVO;
import nc.vo.bxgt.ccp.SaleOrderHVO;
import nc.vo.bxgt.lockbill.ConfirmInfo;
import nc.vo.bxgt.lockbill.EditInfo;
import nc.vo.bxgt.lockbill.LockInfo;
import nc.vo.bxgt.lockbill.OrderSeqInfo;
import nc.vo.bxgt.lockbill.SynInfo;
import nc.vo.bxgt.menu.BxgtStepButton;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class BxgtQuerySynchroImpl implements IbxgtQuerySynchro {

	private static String pk_corp = "1001";

	// private static String pk_calbody="1003A110000000000FB2";

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOrder(
			String strWhere, String zt) throws Exception {
		// bwy 2015-3-27
		BaseDAO dao = null;
		// 销售订单查询语句
		String sql = "";
		// 存放界面销售订单vo的map
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replaceAll("dj.bill_date", "a.dbilldate");
		strWhere = strWhere.replaceFirst("dj.pk_cust", "a.ccustomerid");
		strWhere = strWhere.replaceFirst("dj.pk_hth", "a.vdef12");
		List list = new ArrayList();
		if ("外账套".equals(zt)) {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.csaleid=a.csaleid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.csaleid=a.csaleid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.csaleid=a.csaleid) pk_tb, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.csaleid=a.csaleid) pk_px, "
					+ " a.BCOOPTOPO,a.BFREECUSTFLAG,a.BINITFLAG,a.BINVOICENDFLAG,a.BOUTENDFLAG,a.BOVERDATE,a.BPAYENDFLAG,a.BPOCOOPTOME,"
					+ " a.BRECEIPTENDFLAG,a.BRETINVFLAG,a.BTRANSENDFLAG,b.user_name as CAPPROVEID,a.CBALTYPEID,c.businame as CBIZTYPE,"
					+ " d.bodyname as CCALBODYID,a.CCOOPPOHID,a.CCREDITNUM,a.CCUSTBANKID,f.custname as CCUSTOMERID,g.deptname as CDEPTID,"
					+ " h.psnname as CEMPLOYEEID,a.CFREECUSTID,i.user_name as COPERATORID,a.CRECEIPTAREAID,k.custname as CRECEIPTCORPID,"
					+ " k.custname as CRECEIPTCUSTOMERID,l.billtypename as CRECEIPTTYPE,m.vsalestruname as CSALECORPID,a.CSALEID,"
					+ " q.termname as CTERMPROTOCOLID,r.sendname as CTRANSMODEID,n.storname as CWAREHOUSEID,a.DAPPROVEDATE,a.DAUDITTIME,"
					+ " a.DBILLDATE,a.DBILLTIME,a.DMAKEDATE,a.DMODITIME,a.DR,a.EDITAUTHOR,a.EDITDATE,a.EDITIONNUM,a.FINVOICECLASS,a.FINVOICETYPE,"
					+ " a.FSTATUS,a.IBALANCEFLAG,a.IPRINTCOUNT,a.NACCOUNTPERIOD,a.NDISCOUNTRATE,a.NEVALUATECARRIAGE,a.NHEADSUMMNY,a.NPRECEIVEMNY,"
					+ " a.NPRECEIVERATE,a.NSUBSCRIPTION,a.PK_CORP,a.PK_DEFDOC1,a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,"
					+ " a.PK_DEFDOC15,a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,a.PK_DEFDOC3,a.PK_DEFDOC4,"
					+ " a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,a.TS,a.VACCOUNTYEAR,a.VDEF1,a.VDEF10,a.VDEF11,a.VDEF12,a.VDEF13,"
					+ " a.VDEF14,a.VDEF15,a.VDEF16,a.VDEF17,a.VDEF18,a.VDEF19,a.VDEF2,a.VDEF20,a.VDEF3,a.VDEF4,a.VDEF5,a.VDEF6,a.VDEF7,a.VDEF8,a.VDEF9,"
					+ " a.VEDITREASON,a.VNOTE,a.VRECEIPTCODE,p.addrname as VRECEIVEADDRESS,o.ccurrencytypeid,o.ntaxrate,o.nexchangeotobrate from so_sale a "
					+ " left join sm_user b on a.capproveid = b.cuserid left join bd_busitype c on a.cbiztype = c.pk_busitype left join bd_calbody d "
					+ " on a.ccalbodyid = d.pk_calbody left join bd_cumandoc e on a.ccustomerid = e.pk_cumandoc left join bd_cubasdoc f "
					+ " on e.pk_cubasdoc = f.pk_cubasdoc left join bd_deptdoc g on a.cdeptid = g.pk_deptdoc left join bd_psndoc h "
					+ " on a.cemployeeid = h.pk_psndoc left join sm_user i on a.coperatorid = i.cuserid left join bd_cumandoc j on "
					+ " a.creceiptcorpid = j.pk_cumandoc left join bd_cubasdoc k on j.pk_cubasdoc = k.pk_cubasdoc left join bd_billtype l "
					+ " on a.creceipttype = l.pk_billtypecode left join bd_salestru m on a.csalecorpid = m.CSALESTRUID left join bd_stordoc n "
					+ " on a.cwarehouseid = n.pk_stordoc left join (select distinct so_saleorder_b.csaleid,so_saleorder_b.ntaxrate,so_saleorder_b.nexchangeotobrate,"
					+ " bd_currtype.currtypename as ccurrencytypeid from so_saleorder_b left join bd_currtype on so_saleorder_b.ccurrencytypeid =bd_currtype.pk_currtype)"
					+ " o on o.csaleid = a.csaleid left join (select bd_custaddr.*,bd_cumandoc.pk_cumandoc from bd_custaddr LEFT OUTER JOIN bd_areacl "
					+ " ON bd_custaddr.pk_areacl = bd_areacl.pk_areacl LEFT OUTER JOIN bd_address ON bd_custaddr.pk_address = bd_address.pk_address "
					+ " INNER JOIN bd_cubasdoc ON bd_custaddr.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc LEFT OUTER JOIN bd_cumandoc "
					+ " ON bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc) p on p.pk_cumandoc = a.creceiptcorpid left join bd_payterm q "
					+ " on q.pk_payterm = a.ctermprotocolid left join bd_sendtype r on a.ctransmodeid = r.pk_sendtype where 1=1 and nvl(a.dr,0)=0 "
					+ " and a.fstatus in('2','6') and a.vdef8 = 'Y' and ( a.pk_defdoc11='Y' or( nvl(a.pk_defdoc11,'N') like 'N%' and exists( select 1 from so_saleorder_b sb where exists "
					+ "(select 1 from bd_invbasdoc vv where exists (select 1  from bd_invcl rr where nvl(rr.averagepurahead, 0) = 0 "
					+ " and rr.pk_invcl = vv.pk_invcl) and sb.cinvbasdocid = vv.pk_invbasdoc)and sb.csaleid=a.csaleid)) or a.ctransmodeid='0001V21000000000BVRZ')  "
					+ strWhere
					+ " and not exists(select 1 from bxgt_islock lk where lk.csaleid = a.csaleid) and "
					+ " exists(select 1 from bd_cumandoc bd where bd.def30='Y' and bd.pk_cumandoc = a.ccustomerid )";
			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);

			list = this.getResultListMap(sql);

		} else {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.csaleid=a.csaleid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.csaleid=a.csaleid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.csaleid=a.csaleid) pk_tb, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.csaleid=a.csaleid) pk_px, "
					+ " a.BCOOPTOPO,a.BFREECUSTFLAG,a.BINITFLAG,a.BINVOICENDFLAG,a.BOUTENDFLAG,a.BOVERDATE,a.BPAYENDFLAG,a.BPOCOOPTOME,a.BRECEIPTENDFLAG,"
					+ "a.BRETINVFLAG,a.BTRANSENDFLAG,b.user_name as CAPPROVEID,a.CBALTYPEID,c.businame as CBIZTYPE,d.bodyname as CCALBODYID,a.CCOOPPOHID,"
					+ "a.CCREDITNUM,a.CCUSTBANKID,f.custname as CCUSTOMERID,g.deptname as CDEPTID,h.psnname as CEMPLOYEEID,a.CFREECUSTID,i.user_name as COPERATORID,"
					+ "a.CRECEIPTAREAID,k.custname as CRECEIPTCORPID,k.custname as CRECEIPTCUSTOMERID,l.billtypename as CRECEIPTTYPE,m.vsalestruname as CSALECORPID,"
					+ "a.CSALEID,q.termname as CTERMPROTOCOLID,r.sendname as CTRANSMODEID,n.storname as CWAREHOUSEID,a.DAPPROVEDATE,a.DAUDITTIME,a.DBILLDATE,a.DBILLTIME,"
					+ "a.DMAKEDATE,a.DMODITIME,a.DR,a.EDITAUTHOR,a.EDITDATE,a.EDITIONNUM,a.FINVOICECLASS,a.FINVOICETYPE,a.FSTATUS,a.IBALANCEFLAG,a.IPRINTCOUNT,"
					+ "a.NACCOUNTPERIOD,a.NDISCOUNTRATE,a.NEVALUATECARRIAGE,a.NHEADSUMMNY,a.NPRECEIVEMNY,a.NPRECEIVERATE,a.NSUBSCRIPTION,a.PK_CORP,a.PK_DEFDOC1,"
					+ "a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,"
					+ "a.PK_DEFDOC2,a.PK_DEFDOC20,a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,a.TS,a.VACCOUNTYEAR,"
					+ "a.VDEF1,a.VDEF10,a.VDEF11,a.VDEF12,a.VDEF13,a.VDEF14,a.VDEF15,a.VDEF16,a.VDEF17,a.VDEF18,a.VDEF19,a.VDEF2,a.VDEF20,a.VDEF3,a.VDEF4,a.VDEF5,"
					+ "a.VDEF6,a.VDEF7,a.VDEF8,a.VDEF9,a.VEDITREASON,a.VNOTE,a.VRECEIPTCODE,p.addrname as VRECEIVEADDRESS,o.ccurrencytypeid,o.ntaxrate,"
					+ "o.nexchangeotobrate from so_sale a left join sm_user b on a.capproveid = b.cuserid left join bd_busitype c on a.cbiztype = c.pk_busitype "
					+ "left join bd_calbody d on a.ccalbodyid = d.pk_calbody left join bd_cumandoc e on a.ccustomerid = e.pk_cumandoc "
					+ "left join bd_cubasdoc f on e.pk_cubasdoc = f.pk_cubasdoc left join bd_deptdoc g on a.cdeptid = g.pk_deptdoc "
					+ "left join bd_psndoc h on a.cemployeeid = h.pk_psndoc left join sm_user i on a.coperatorid = i.cuserid "
					+ "left join bd_cumandoc j on a.creceiptcorpid = j.pk_cumandoc left join bd_cubasdoc k on j.pk_cubasdoc = k.pk_cubasdoc "
					+ "left join bd_billtype l on a.creceipttype = l.pk_billtypecode left join bd_salestru m on a.csalecorpid = m.CSALESTRUID "
					+ "left join bd_stordoc n on a.cwarehouseid = n.pk_stordoc left join (select distinct so_saleorder_b.csaleid,so_saleorder_b.ntaxrate,"
					+ "so_saleorder_b.nexchangeotobrate,bd_currtype.currtypename as ccurrencytypeid from so_saleorder_b left join bd_currtype "
					+ "on so_saleorder_b.ccurrencytypeid =bd_currtype.pk_currtype) o on o.csaleid = a.csaleid left join "
					+ "(select bd_custaddr.*,bd_cumandoc.pk_cumandoc from bd_custaddr LEFT OUTER JOIN bd_areacl ON bd_custaddr.pk_areacl = bd_areacl.pk_areacl "
					+ "LEFT OUTER JOIN bd_address ON bd_custaddr.pk_address = bd_address.pk_address INNER JOIN bd_cubasdoc ON "
					+ "bd_custaddr.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc LEFT OUTER JOIN bd_cumandoc ON bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc) p "
					+ "on p.pk_cumandoc = a.creceiptcorpid left join bd_payterm q on q.pk_payterm = a.ctermprotocolid left join bd_sendtype r "
					+ "on a.ctransmodeid = r.pk_sendtype where 1=1 and nvl(a.dr,0)=0 and a.fstatus in('2','6') "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);

			list = (ArrayList<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());

		}
		if (list == null || list.size() == 0) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			// 存放bean对象的map
			Map<String, Object> beanMap = (HashMap<String, Object>) list.get(i);
			voMap.put(i, beanMap);
		}
		return voMap;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQuerySaleOutOrder(
			String strWhere, String zt) throws Exception {
		// bwy 2015-3-27
		BaseDAO dao = null;
		// 销售出库单查询语句
		String sql = "";

		// 过滤A账套中销售出库单对应的销售订单已经存在B账套中的销售出库单的pk
		String[] cgeneralhids = filterCgeneralhid(strWhere);
		if (cgeneralhids == null || cgeneralhids.length <= 0)
			return null;
		// 存放界面销售出库单vo的map
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replaceAll("dj.bill_date", "dbilldate");
		List list = new ArrayList();
		if ("外账套".equals(zt)) {
			sql = "select(select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ "a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,b.user_name as CAUDITORID,"
					+ "c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,e.businame as CBIZTYPE,r.custname as CCUSTOMERID,"
					+ "a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,a.CINVENTORYID,"
					+ "a.CLASTMODIID,h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,a.COUTCALBODYID,"
					+ "a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,s.custname as CTRANCUSTID,"
					+ "l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as CWHSMANAGERID,a.DACCOUNTDATE,a.DAUDITDATE,"
					+ "a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,a.FREPLENISHFLAG,a.FSPECIALFLAG,a.IPRINTCOUNT,a.NDISCOUNTMNY,"
					+ "a.NNETMNY,m.bodyname as PK_CALBODY,n.unitname as PK_CORP,o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,"
					+ "a.PK_DEFDOC1,a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,"
					+ "a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,a.PK_DEFDOC3,"
					+ "a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,a.PK_MEASWARE,"
					+ "p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,a.TMAKETIME,a.TS,a.VBILLCODE,a.VDILIVERADDRESS,"
					+ "a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,a.VUSERDEF11,a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,"
					+ "a.VUSERDEF15,a.VUSERDEF16,a.VUSERDEF17,a.VUSERDEF18,a.VUSERDEF19,a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,"
					+ "a.VUSERDEF4,a.VUSERDEF5,a.VUSERDEF6,a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 from ic_general_h a left join "
					+ "sm_user b on a.cauditorid = b.cuserid left join bd_billtype c on a.cbilltypecode = c.pk_billtypecode "
					+ "left join bd_psndoc d on a.cbizid = d.pk_psndoc left join bd_busitype e on a.cbiztype = e.pk_busitype "
					+ "left join bd_rdcl f on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc "
					+ "left join sm_user h on a.coperatorid = h.cuserid left join bd_cumandoc i on a.cproviderid = i.pk_cumandoc "
					+ "left join bd_cubasdoc j on i.pk_cubasdoc = j.pk_cubasdoc left join sm_user k on a.cregister = k.cuserid "
					+ "left join bd_stordoc l on a.cwarehouseid = l.pk_stordoc left join bd_calbody m on a.pk_calbody = m.pk_calbody "
					+ "left join bd_corp n on a.pk_corp = n.pk_corp left join bd_cubasdoc o on a.pk_cubasdoc = o.pk_cubasdoc "
					+ "left join bd_corp p on a.pk_purcorp = p.pk_corp left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc "
					+ "left join bd_cubasdoc r on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,"
					+ "bd_cubasdoc.custname from dm_trancust inner join bd_delivorg on bd_delivorg.pk_delivorg = "
					+ "dm_trancust.pkdelivorg inner join bd_cumandoc on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc "
					+ "inner join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join bd_cumandoc "
					+ "apman on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas on "
					+ "apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join bd_psndoc t "
					+ "on a.cwhsmanagerid = t.pk_psndoc where 1=1 and cbilltypecode='4C' and nvl(a.dr,0)=0 and a.fbillflag=3 "
					+ strWhere
					+ " and not exists(select 1 from bxgt_islock lk where lk.cgeneralhid = a.cgeneralhid) and a.cgeneralhid in('";
			for (int i = 0; i < cgeneralhids.length; i++) {
				sql += cgeneralhids[i] + "','";
			}
			sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);
			list = this.getResultListMap(sql);

		} else {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ " a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,b.user_name as CAUDITORID,"
					+ "c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,e.businame as CBIZTYPE,r.custname as CCUSTOMERID,"
					+ "a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,"
					+ "a.CINVENTORYID,a.CLASTMODIID,h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,"
					+ "a.COUTCALBODYID,a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,"
					+ "s.custname as CTRANCUSTID,l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as CWHSMANAGERID,"
					+ "a.DACCOUNTDATE,a.DAUDITDATE,a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,a.FREPLENISHFLAG,a.FSPECIALFLAG,"
					+ "a.IPRINTCOUNT,a.NDISCOUNTMNY,a.NNETMNY,m.bodyname as PK_CALBODY,n.unitname as PK_CORP,"
					+ "o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,a.PK_DEFDOC1,a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,"
					+ "a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,"
					+ "a.PK_DEFDOC2,a.PK_DEFDOC20,a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,"
					+ "a.PK_DEFDOC8,a.PK_DEFDOC9,a.PK_MEASWARE,p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,"
					+ "a.TMAKETIME,a.TS,a.VBILLCODE,a.VDILIVERADDRESS,a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,"
					+ "a.VUSERDEF11,a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,a.VUSERDEF15,a.VUSERDEF16,a.VUSERDEF17,"
					+ "a.VUSERDEF18,a.VUSERDEF19,a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,a.VUSERDEF4,a.VUSERDEF5,a.VUSERDEF6,"
					+ "a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 from ic_general_h a left join sm_user b "
					+ "on a.cauditorid = b.cuserid left join bd_billtype c on a.cbilltypecode = c.pk_billtypecode "
					+ "left join bd_psndoc d on a.cbizid = d.pk_psndoc left join bd_busitype e on a.cbiztype = e.pk_busitype "
					+ "left join bd_rdcl f on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc "
					+ "left join sm_user h on a.coperatorid = h.cuserid left join bd_cumandoc i "
					+ "on a.cproviderid = i.pk_cumandoc left join bd_cubasdoc j on i.pk_cubasdoc = j.pk_cubasdoc "
					+ "left join sm_user k on a.cregister = k.cuserid left join bd_stordoc l on a.cwarehouseid = l.pk_stordoc "
					+ "left join bd_calbody m on a.pk_calbody = m.pk_calbody left join bd_corp n on a.pk_corp = n.pk_corp "
					+ "left join bd_cubasdoc o on a.pk_cubasdoc = o.pk_cubasdoc left join bd_corp p "
					+ "on a.pk_purcorp = p.pk_corp left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc "
					+ "left join bd_cubasdoc r on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,"
					+ "bd_cubasdoc.custname from dm_trancust inner join bd_delivorg on bd_delivorg.pk_delivorg = "
					+ "dm_trancust.pkdelivorg inner join bd_cumandoc on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc "
					+ "inner join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join "
					+ "bd_cumandoc apman on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas "
					+ "on apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join "
					+ "bd_psndoc t on a.cwhsmanagerid = t.pk_psndoc where 1=1 and a.cbilltypecode='4C' and a.fbillflag=3 and nvl(a.dr,0)=0 "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);

			list = (ArrayList<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		}
		if (list == null || list.size() == 0) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			// 存放bean对象的map
			Map<String, Object> beanMap = (HashMap<String, Object>) list.get(i);

			voMap.put(i, beanMap);
		}

		return voMap;
	}

	/**
	 * 过滤A账套中销售出库单对应的销售订单已经存在B账套中的销售出库单的pk
	 * 
	 * @param strWhere
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	private String[] filterCgeneralhid(String strWhere) throws Exception {
		strWhere = strWhere.replace("dj.bill_date", "h.dbilldate");
		String sql = "select distinct b.cfirstbillhid from ic_general_b b,ic_general_h h where h.cgeneralhid = b.cgeneralhid "
				+ strWhere + " and nvl(h.dr,0)=0 ";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		List list = this.getResultListMap(sql);
		String[] csaleids = null;
		if (list != null && list.size() > 0) {
			csaleids = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> csaleidMap = (HashMap<String, Object>) list
						.get(i);
				String csaleid = BFPubTool
						.getString_TrimZeroLenAsNull(csaleidMap
								.get("cfirstbillhid"));
				csaleids[i] = csaleid;
			}
		}
		String[] newCsaleids = getFitCsaleid(csaleids);
		String[] cgeneralhids = getFitCgeneralhid(newCsaleids);
		return cgeneralhids;
	}

	// 查询该销售订单a[]是否在B账套，返回在B账套的销售订单b[]
	private String[] getFitCsaleid(String[] csaleids) throws DAOException {
		if (csaleids == null || csaleids.length <= 0) {
			return null;
		}
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);

		int j = 0;// 循环标志
		int k = 0;
		String pk_sale = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < csaleids.length; i++) {
			pk_sale += "'" + csaleids[i] + "',";
			k++;
			if (k == 800) {
				pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
				String sql = "select csaleid from so_sale where nvl(dr,0)=0 and csaleid in("
						+ pk_sale + ")";
				ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
						sql, new ColumnListProcessor());
				for (String pk : llist) {
					list.add(pk);
				}
				pk_sale = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
			String sql = "select csaleid from so_sale where nvl(dr,0)=0 and csaleid in("
					+ pk_sale + ")";
			ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(sql,
					new ColumnListProcessor());
			for (String pk : llist) {
				list.add(pk);
			}
		}

		return list.toArray(new String[0]);
	}

	// 查询A账套销售出库单条件为对应销售订单是b[](根据b[]查询对应销售出库单主表pk)
	@SuppressWarnings("unchecked")
	private String[] getFitCgeneralhid(String[] csaleids) throws Exception {
		if (csaleids == null || csaleids.length <= 0) {
			return null;
		}
		int j = 0;// 循环标志
		int k = 0;
		String pk_sale = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < csaleids.length; i++) {
			pk_sale += "'" + csaleids[i] + "',";
			k++;
			if (k == 800) {
				pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
				String sql = "select distinct cgeneralhid from ic_general_b where nvl(dr,0)=0 and cfirstbillhid in("
						+ pk_sale + ")";
				ArrayList<String> llist = (ArrayList<String>) this
						.getResultListColumn(sql);
				for (String pk : llist) {
					list.add(pk);
				}
				pk_sale = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
			String sql = "select distinct cgeneralhid from ic_general_b where nvl(dr,0)=0 and cfirstbillhid in("
					+ pk_sale + ")";
			ArrayList<String> llist = (ArrayList<String>) this
					.getResultListColumn(sql);
			for (String pk : llist) {
				list.add(pk);
			}
		}
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		return list.toArray(new String[0]);
	}

	/**
	 * 过滤A账套中采购入库单对应的采购订单已经存在B账套中的采购入库单的pk
	 * 
	 * @param strWhere
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	private String[] filterCgeneralhidCG(String strWhere, int type)
			throws Exception {
		String sql = null;
		if (type == 1) {// 过滤采购入库
			strWhere = strWhere.replace("dj.bill_date", "h.dbilldate");
			sql = "select distinct b.cfirstbillhid from ic_general_b b,ic_general_h h where h.cgeneralhid = b.cgeneralhid "
					+ strWhere
					+ " and nvl(h.dr,0)=0 and b.cfirstbillhid is not null ";
		} else if (type == 2) {// 过滤采购发票
			strWhere = strWhere.replace("dj.bill_date", "h.dinvoicedate");
			sql = "select distinct b.corderid from po_invoice_b b,po_invoice h where h.cinvoiceid = b.cinvoiceid "
					+ strWhere + " and nvl(h.dr,0)=0 ";
		}
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		List<String> list = this.getResultListColumn(sql);
		if (list == null || list.size() <= 0) {
			return null;
		}
		String[] corderids = list.toArray(new String[0]);
		String[] newOrderids = this.getFitCorderid(corderids);
		String[] cAfterFilterids = this.getAfterFilter(newOrderids, type);
		return cAfterFilterids;
	}

	// 查询该采购订单a[]是否在B账套，返回在B账套的采购订单b[]
	private String[] getFitCorderid(String[] corderids) throws DAOException {
		if (corderids == null || corderids.length <= 0) {
			return null;
		}
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);

		int j = 0;// 循环标志
		int k = 0;
		String pk_sale = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < corderids.length; i++) {
			pk_sale += "'" + corderids[i] + "',";
			k++;
			if (k == 800) {
				pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
				String sql = "select corderid from po_order where nvl(dr,0)=0 and corderid in("
						+ pk_sale + ")";
				ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
						sql, new ColumnListProcessor());
				for (String pk : llist) {
					list.add(pk);
				}
				pk_sale = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
			String sql = "select corderid from po_order where nvl(dr,0)=0 and corderid in("
					+ pk_sale + ")";
			ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(sql,
					new ColumnListProcessor());
			for (String pk : llist) {
				list.add(pk);
			}
		}

		return list.toArray(new String[0]);
	}

	// 查询A账套满足条件的采购入库单或者采购发票
	@SuppressWarnings("unchecked")
	private String[] getAfterFilter(String[] corderids, int type)
			throws Exception {
		if (corderids == null || corderids.length <= 0) {
			return null;
		}

		int j = 0;// 循环标志
		int k = 0;
		String pk_sale = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < corderids.length; i++) {
			pk_sale += "'" + corderids[i] + "',";
			k++;
			if (k == 800) {
				pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
				String sql = "";
				if (type == 1) {
					sql = "select distinct b.cgeneralhid from ic_general_b b where nvl(b.dr,0)=0 and b.cfirstbillhid in("
							+ pk_sale + ") ";
				} else if (type == 2) {
					sql = "select distinct b.cinvoiceid from po_invoice_b b where nvl(b.dr,0)=0 and b.corderid in("
							+ pk_sale + ")";
				}
				ArrayList<String> llist = (ArrayList<String>) this
						.getResultListColumn(sql);
				for (String pk : llist) {
					list.add(pk);
				}
				pk_sale = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));
			String sql = "";
			if (type == 1) {
				sql = "select distinct b.cgeneralhid from ic_general_b b where nvl(b.dr,0)=0 and b.cfirstbillhid in("
						+ pk_sale + ") ";
			} else if (type == 2) {
				sql = "select distinct b.cinvoiceid from po_invoice_b b where nvl(b.dr,0)=0 and b.corderid in("
						+ pk_sale + ")";
			}
			ArrayList<String> llist = (ArrayList<String>) this
					.getResultListColumn(sql);
			for (String pk : llist) {
				list.add(pk);
			}
		}

		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		return list.toArray(new String[0]);
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseOrder(
			String strWhere, String zt) throws Exception {
		// 采购订单查询2015.5.18 xm
		BaseDAO dao = null;
		// 采购订单查询语句
		String sql = "";
		// 存放界面采购订单vo的map
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replace("dj.bill_date", "o.dorderdate");
		List list = new ArrayList();
		if ("外账套".equals(zt)) {
			sql = "SELECT (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.corderid=o.corderid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.corderid=o.corderid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.corderid=o.corderid) pk_tb, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.corderid=o.corderid) pk_px, "
					+ " o.bcooptoso,o.bislatest,o.bisreplenish,o.breturn,o.bsocooptome,o.caccountbankid,o.caccountyear,"
					+ " au.user_name AS cauditpsn,b.businame AS cbiztype,bd.deptname AS cdeptid,cu.user_name AS cemployeeid,"
					+ " cfu.user_name AS cfreecustid,bc.custshortname AS cgiveinvoicevendor,u.user_name AS coperator,o.corderid,"
					+ " bp.name AS cpurorganization,bco.custshortname AS creciever,bu.user_name AS crevisepsn,bpay.termname AS "
					+ " ctermprotocolid,bsend.sendname AS ctransmodeid,sujd.user_name AS cunfreeze,o.cvendorbaseid,bct.custshortname "
					+ " AS cvendormangid,o.dauditdate,o.dorderdate,o.dr,o.drevisiondate,o.forderstatus,o.iprintcount,o.nprepaymaxmny,"
					+ " o.nprepaymny,o.nversion,o.pk_corp,o.pk_defdoc1,o.pk_defdoc10,o.pk_defdoc11,o.pk_defdoc12,o.pk_defdoc13,"
					+ " o.pk_defdoc14,o.pk_defdoc15,o.pk_defdoc16,o.pk_defdoc17,o.pk_defdoc18,o.pk_defdoc19,o.pk_defdoc2,o.pk_defdoc20,"
					+ " o.pk_defdoc3,o.pk_defdoc4,o.pk_defdoc5,o.pk_defdoc6,o.pk_defdoc7,o.pk_defdoc8,o.pk_defdoc9,o.taudittime,"
					+ " o.tlastmaketime,o.tmaketime,o.trevisiontime,o.ts,o.vcoopordercode,o.vdef1,o.vdef10,o.vdef11,o.vdef12,"
					+ " o.vdef13,o.vdef14,o.vdef15,o.vdef16,o.vdef17,o.vdef18,o.vdef19,o.vdef2,o.vdef20,o.vdef3,o.vdef4,o.vdef5,"
					+ " o.vdef6,o.vdef7,o.vdef8,o.vdef9,o.vmemo,o.vordercode,c.unitname  AS pk_corp,zh.currtypename AS "
					+ " ccurrencytypeid FROM po_order o LEFT JOIN bd_corp c ON o.pk_corp = c.pk_corp LEFT JOIN sm_user u "
					+ " ON o.coperator = u.cuserid LEFT JOIN bd_busitype b ON o.cbiztype = b.pk_busitype LEFT JOIN sm_user au "
					+ " ON o.cauditpsn = au.cuserid LEFT JOIN bd_deptdoc bd ON o.cdeptid = bd.pk_deptdoc LEFT JOIN sm_user cu "
					+ " ON o.cemployeeid = cu.cuserid LEFT JOIN sm_user cfu ON o.cfreecustid = cfu.cuserid LEFT JOIN bd_cumandoc bcu "
					+ " ON o.cgiveinvoicevendor = bcu.pk_cumandoc LEFT JOIN bd_cubasdoc bc ON bc.pk_cubasdoc = bcu.pk_cubasdoc "
					+ " LEFT JOIN bd_purorg bp ON o.cpurorganization = bp.pk_purorg LEFT JOIN bd_cumandoc bcuo "
					+ " ON o.creciever = bcuo.pk_cumandoc LEFT JOIN bd_cubasdoc bco ON bco.pk_cubasdoc = bcuo.pk_cubasdoc "
					+ " LEFT JOIN sm_user bu ON o.crevisepsn = bu.cuserid LEFT JOIN bd_payterm bpay "
					+ " ON o.ctermprotocolid = bpay.pk_payterm LEFT JOIN bd_sendtype bsend ON o.ctransmodeid = bsend.pk_sendtype "
					+ " LEFT JOIN sm_user sujd ON o.cunfreeze = sujd.cuserid LEFT JOIN bd_cumandoc bcut "
					+ " ON o.cvendormangid = bcut.pk_cumandoc LEFT JOIN bd_cubasdoc bct ON bct.pk_cubasdoc = bcut.pk_cubasdoc "
					+ " LEFT JOIN (SELECT distinct poo.corderid,bcur.currtypename FROM bd_currtype bcur,po_order_b poo "
					+ " WHERE bcur.pk_currtype = poo.ccurrencytypeid) zh ON zh.corderid = o.corderid "
					+ " where 1=1 and nvl(o.dr,0)=0 and o.forderstatus=3 and o.vdef9='Y' "
					+ strWhere
					+ " and not exists(select 1 from bxgt_islock lk where lk.corderid = o.corderid) ";
			// + " and exists (select 1 from bd_cumandoc bd where bd.def30='Y'
			// and bd.pk_cumandoc=o.cvendormangid)";

			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);
			list = this.getResultListMap(sql);

		} else {
			sql = "SELECT(select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.corderid=o.corderid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.corderid=o.corderid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.corderid=o.corderid) pk_tb, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.corderid=o.corderid) pk_px, "
					+ " o.bcooptoso,o.bislatest,o.bisreplenish,o.breturn,o.bsocooptome,o.caccountbankid,o.caccountyear,"
					+ " au.user_name AS cauditpsn,b.businame AS cbiztype,bd.deptname AS cdeptid,cu.user_name AS cemployeeid,"
					+ " cfu.user_name AS cfreecustid,bc.custshortname AS cgiveinvoicevendor,u.user_name AS coperator,o.corderid,"
					+ " bp.name AS cpurorganization,bco.custshortname AS creciever,bu.user_name AS crevisepsn,bpay.termname AS "
					+ " ctermprotocolid,bsend.sendname AS ctransmodeid,sujd.user_name AS cunfreeze,o.cvendorbaseid,bct.custshortname "
					+ " AS cvendormangid,o.dauditdate,o.dorderdate,o.dr,o.drevisiondate,o.forderstatus,o.iprintcount,o.nprepaymaxmny,"
					+ " o.nprepaymny,o.nversion,o.pk_corp,o.pk_defdoc1,o.pk_defdoc10,o.pk_defdoc11,o.pk_defdoc12,o.pk_defdoc13,"
					+ " o.pk_defdoc14,o.pk_defdoc15,o.pk_defdoc16,o.pk_defdoc17,o.pk_defdoc18,o.pk_defdoc19,o.pk_defdoc2,"
					+ " o.pk_defdoc20,o.pk_defdoc3,o.pk_defdoc4,o.pk_defdoc5,o.pk_defdoc6,o.pk_defdoc7,o.pk_defdoc8,o.pk_defdoc9,"
					+ " o.taudittime,o.tlastmaketime,o.tmaketime,o.trevisiontime,o.ts,o.vcoopordercode,o.vdef1,o.vdef10,o.vdef11,"
					+ " o.vdef12,o.vdef13,o.vdef14,o.vdef15,o.vdef16,o.vdef17,o.vdef18,o.vdef19,o.vdef2,o.vdef20,o.vdef3,o.vdef4,"
					+ " o.vdef5,o.vdef6,o.vdef7,o.vdef8,o.vdef9,o.vmemo,o.vordercode,c.unitname  AS pk_corp,zh.currtypename AS "
					+ " ccurrencytypeid FROM po_order o LEFT JOIN bd_corp c ON o.pk_corp = c.pk_corp LEFT JOIN sm_user u "
					+ " ON o.coperator = u.cuserid LEFT JOIN bd_busitype b ON o.cbiztype = b.pk_busitype LEFT JOIN sm_user au "
					+ " ON o.cauditpsn = au.cuserid LEFT JOIN bd_deptdoc bd ON o.cdeptid = bd.pk_deptdoc LEFT JOIN sm_user cu "
					+ " ON o.cemployeeid = cu.cuserid LEFT JOIN sm_user cfu ON o.cfreecustid = cfu.cuserid LEFT JOIN bd_cumandoc bcu "
					+ " ON o.cgiveinvoicevendor = bcu.pk_cumandoc LEFT JOIN bd_cubasdoc bc ON bc.pk_cubasdoc = bcu.pk_cubasdoc "
					+ " LEFT JOIN bd_purorg bp ON o.cpurorganization = bp.pk_purorg LEFT JOIN bd_cumandoc bcuo "
					+ " ON o.creciever = bcuo.pk_cumandoc LEFT JOIN bd_cubasdoc bco ON bco.pk_cubasdoc = bcuo.pk_cubasdoc "
					+ " LEFT JOIN sm_user bu ON o.crevisepsn = bu.cuserid LEFT JOIN bd_payterm bpay "
					+ " ON o.ctermprotocolid = bpay.pk_payterm LEFT JOIN bd_sendtype bsend ON o.ctransmodeid = bsend.pk_sendtype "
					+ " LEFT JOIN sm_user sujd ON o.cunfreeze = sujd.cuserid LEFT JOIN bd_cumandoc bcut "
					+ " ON o.cvendormangid = bcut.pk_cumandoc LEFT JOIN bd_cubasdoc bct ON bct.pk_cubasdoc = bcut.pk_cubasdoc "
					+ " LEFT JOIN (SELECT distinct poo.corderid,bcur.currtypename FROM bd_currtype bcur,po_order_b poo "
					+ " WHERE bcur.pk_currtype = poo.ccurrencytypeid) zh ON zh.corderid = o.corderid "
					+ " where 1=1 and o.forderstatus=3  and nvl(o.dr,0)=0  "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);
			list = (ArrayList<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());

		}
		if (list == null || list.size() == 0) {
			return null;
		}

		for (int i = 0; i < list.size(); i++) {
			// 存放bean对象的map
			Map<String, Object> beanMap = (Map<String, Object>) list.get(i);
			voMap.put(i, beanMap);
		}

		return voMap;

	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInOrder(
			String strWhere, String zt) throws Exception {
		// 采购入库单 2015.5.18 xm
		BaseDAO dao = null;
		// 采购入库单查询sql
		String sql = "";
		// 过滤B帐套中存在数据才显示
		String[] cgeneralhids = this.filterCgeneralhidCG(strWhere, 1);
		if (cgeneralhids == null || cgeneralhids.length <= 0) {
			return null;
		}
		// 存放界面采购入库单vo的map
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replaceAll("dj.bill_date", "dbilldate");
		List list = new ArrayList();
		if ("外账套".equals(zt)) {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ "a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,"
					+ "b.user_name as CAUDITORID,c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,"
					+ "e.businame as CBIZTYPE,r.custname as CCUSTOMERID,a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,"
					+ "g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,a.CINVENTORYID,a.CLASTMODIID,"
					+ "h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,a.COUTCALBODYID,"
					+ "a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,"
					+ "s.custname as CTRANCUSTID,l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as "
					+ "CWHSMANAGERID,a.DACCOUNTDATE,a.DAUDITDATE,a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,"
					+ "a.FREPLENISHFLAG,a.FSPECIALFLAG,a.IPRINTCOUNT,a.NDISCOUNTMNY,a.NNETMNY,m.bodyname as "
					+ "PK_CALBODY,n.unitname as PK_CORP,o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,a.PK_DEFDOC1,"
					+ "a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,"
					+ "a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,"
					+ "a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,"
					+ "a.PK_MEASWARE,p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,a.TMAKETIME,"
					+ "a.TS,a.VBILLCODE,a.VDILIVERADDRESS,a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,"
					+ "a.VUSERDEF11,a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,a.VUSERDEF15,a.VUSERDEF16,"
					+ "a.VUSERDEF17,a.VUSERDEF18,a.VUSERDEF19,a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,a.VUSERDEF4,"
					+ "a.VUSERDEF5,a.VUSERDEF6,a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 from ic_general_h a "
					+ "left join sm_user b on a.cauditorid = b.cuserid left join bd_billtype c "
					+ "on a.cbilltypecode = c.pk_billtypecode left join bd_psndoc d on a.cbizid = d.pk_psndoc "
					+ "left join bd_busitype e on a.cbiztype = e.pk_busitype left join bd_rdcl f "
					+ "on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc "
					+ "left join sm_user h on a.coperatorid = h.cuserid left join bd_cumandoc i "
					+ "on a.cproviderid = i.pk_cumandoc left join bd_cubasdoc j on i.pk_cubasdoc = j.pk_cubasdoc "
					+ "left join sm_user k on a.cregister = k.cuserid left join bd_stordoc l "
					+ "on a.cwarehouseid = l.pk_stordoc left join bd_calbody m on a.pk_calbody = m.pk_calbody "
					+ "left join bd_corp n on a.pk_corp = n.pk_corp left join bd_cubasdoc o "
					+ "on a.pk_cubasdoc = o.pk_cubasdoc left join bd_corp p on a.pk_purcorp = p.pk_corp "
					+ "left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc left join bd_cubasdoc r "
					+ "on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,"
					+ "bd_cubasdoc.custname from dm_trancust inner join bd_delivorg on bd_delivorg.pk_delivorg = dm_trancust.pkdelivorg "
					+ "inner join bd_cumandoc on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc inner join bd_cubasdoc "
					+ "on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join bd_cumandoc apman "
					+ "on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas "
					+ "on apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join bd_psndoc t "
					+ "on a.cwhsmanagerid = t.pk_psndoc where 1=1  and cbilltypecode='45' and nvl(a.dr,0)=0 and a.fbillflag=3 "
					+ strWhere
					+ "and not exists(select 1 from bxgt_islock lk where lk.cgeneralhid = a.cgeneralhid) and a.cgeneralhid in('";

			for (int i = 0; i < cgeneralhids.length; i++) {
				sql += cgeneralhids[i] + "','";
			}
			sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);

			list = this.getResultListMap(sql);
		} else {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ "a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,b.user_name as CAUDITORID,"
					+ "c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,e.businame as CBIZTYPE,r.custname as CCUSTOMERID,"
					+ "a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,a.CINVENTORYID,"
					+ "a.CLASTMODIID,h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,a.COUTCALBODYID,"
					+ "a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,s.custname as CTRANCUSTID,"
					+ "l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as CWHSMANAGERID,a.DACCOUNTDATE,a.DAUDITDATE,"
					+ "a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,a.FREPLENISHFLAG,a.FSPECIALFLAG,a.IPRINTCOUNT,a.NDISCOUNTMNY,"
					+ "a.NNETMNY,m.bodyname as PK_CALBODY,n.unitname as PK_CORP,o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,a.PK_DEFDOC1,"
					+ "a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,a.PK_DEFDOC16,a.PK_DEFDOC17,"
					+ "a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,"
					+ "a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,a.PK_MEASWARE,p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,"
					+ "a.TMAKETIME,a.TS,a.VBILLCODE,a.VDILIVERADDRESS,a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,a.VUSERDEF11,"
					+ "a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,a.VUSERDEF15,a.VUSERDEF16,a.VUSERDEF17,a.VUSERDEF18,a.VUSERDEF19,"
					+ "a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,a.VUSERDEF4,a.VUSERDEF5,a.VUSERDEF6,a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 "
					+ "from ic_general_h a left join sm_user b on a.cauditorid = b.cuserid left join bd_billtype c on a.cbilltypecode = c.pk_billtypecode "
					+ "left join bd_psndoc d on a.cbizid = d.pk_psndoc left join bd_busitype e on a.cbiztype = e.pk_busitype left join bd_rdcl f "
					+ "on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc left join sm_user h "
					+ "on a.coperatorid = h.cuserid left join bd_cumandoc i on a.cproviderid = i.pk_cumandoc left join bd_cubasdoc j "
					+ "on i.pk_cubasdoc = j.pk_cubasdoc left join sm_user k on a.cregister = k.cuserid left join bd_stordoc l "
					+ "on a.cwarehouseid = l.pk_stordoc left join bd_calbody m on a.pk_calbody = m.pk_calbody left join bd_corp n "
					+ "on a.pk_corp = n.pk_corp left join bd_cubasdoc o on a.pk_cubasdoc = o.pk_cubasdoc left join bd_corp p "
					+ "on a.pk_purcorp = p.pk_corp left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc left join bd_cubasdoc r "
					+ "on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,bd_cubasdoc.custname from dm_trancust "
					+ "inner join bd_delivorg on bd_delivorg.pk_delivorg = dm_trancust.pkdelivorg inner join bd_cumandoc "
					+ "on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc inner join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc "
					+ "left outer join bd_cumandoc apman on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas "
					+ "on apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join bd_psndoc t "
					+ "on a.cwhsmanagerid = t.pk_psndoc where 1=1 and cbilltypecode='45' and nvl(a.dr,0)=0 and a.fbillflag=3 "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);
			list = (ArrayList<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		}

		if (list == null || list.size() == 0) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			// 存放bean对象的map
			Map<String, Object> beanMap = (HashMap<String, Object>) list.get(i);
			voMap.put(i, beanMap);
		}

		return voMap;

	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQueryPurchaseInvoiceOrder(
			String strWhere, String zt) throws Exception {
		// 采购发票 2015.5.18 xm
		BaseDAO dao = null;
		// 采购发票查询sql
		String sql = "";
		// 查询B帐存在的发票
		String[] invoiceids = this.filterCgeneralhidCG(strWhere, 2);
		if (invoiceids == null || invoiceids.length <= 0) {
			return null;
		}
		// 存放界面采购入库单vo的map
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replaceAll("dj.bill_date", "dinvoicedate");
		List list = new ArrayList();
		if ("外账套".equals(zt)) {
			sql = "SELECT (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cinvoiceid=i.cinvoiceid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cinvoiceid=i.cinvoiceid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cinvoiceid=i.cinvoiceid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cinvoiceid=i.cinvoiceid) pk_px,"
					+ " i.cinvoiceid, i.vinvoicecode,i.dinvoicedate,i.darrivedate, bbus.businame AS cbiztype,"
					+ " bcub.custname AS cvendormangid,bdep.deptname AS cdeptid,bpsn.psnname AS cemployeeid,"
					+ " decode(i.iinvoicetype, 0, '专用',1,'普通',2,'虚拟',3,'自定义') iinvoicetype,bpay.termname AS "
					+ " ctermprotocolid,decode(i.finitflag,0,'普通',1,'期初',2,'系统（虚拟）') finitflag,bcubo.custname "
					+ " AS cpayunit,bcal.bodyname AS cstoreorganization,i.vmemo FROM po_invoice i "
					+ " LEFT JOIN bd_busitype bbus ON bbus.pk_busitype = i.cbiztype LEFT JOIN bd_cumandoc bcum "
					+ " ON bcum.pk_cumandoc = i.cvendormangid LEFT JOIN bd_cubasdoc bcub ON bcub.pk_cubasdoc = bcum.pk_cubasdoc "
					+ " LEFT JOIN bd_deptdoc bdep ON bdep.pk_deptdoc = i.cdeptid LEFT JOIN bd_psndoc bpsn "
					+ " ON bpsn.pk_psndoc = i.cemployeeid LEFT JOIN bd_payterm bpay ON bpay.pk_payterm = i.ctermprotocolid "
					+ " LEFT JOIN bd_cumandoc bcumo ON bcumo.pk_cumandoc = i.cpayunit LEFT JOIN bd_cubasdoc bcubo "
					+ " ON bcubo.pk_cubasdoc = bcumo.pk_cubasdoc LEFT JOIN bd_calbody bcal ON bcal.pk_calbody = i.cstoreorganization "
					+ " where 1=1 and nvl(i.dr,0)=0 and i.ibillstatus=3 "
					+ strWhere
					+ "and not exists(select 1 from bxgt_islock lk where lk.cinvoiceid=i.cinvoiceid) and i.cinvoiceid in('";
			for (int i = 0; i < invoiceids.length; i++) {
				sql += invoiceids[i] + "','";
			}
			sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);
			list = this.getResultListMap(sql);
		} else {
			sql = "SELECT(select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cinvoiceid=i.cinvoiceid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cinvoiceid=i.cinvoiceid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cinvoiceid=i.cinvoiceid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cinvoiceid=i.cinvoiceid) pk_px,"
					+ " i.cinvoiceid, i.vinvoicecode,i.dinvoicedate,i.darrivedate, bbus.businame AS cbiztype,"
					+ " bcub.custname AS cvendormangid,bdep.deptname AS cdeptid,bpsn.psnname AS cemployeeid,"
					+ " decode(i.iinvoicetype, 0, '专用',1,'普通',2,'虚拟',3,'自定义') iinvoicetype,bpay.termname AS "
					+ " ctermprotocolid,decode(i.finitflag,0,'普通',1,'期初',2,'系统（虚拟）') finitflag,bcubo.custname "
					+ " AS cpayunit,bcal.bodyname AS cstoreorganization,i.vmemo FROM po_invoice i LEFT JOIN bd_busitype "
					+ " bbus ON bbus.pk_busitype = i.cbiztype LEFT JOIN bd_cumandoc bcum ON "
					+ " bcum.pk_cumandoc = i.cvendormangid LEFT JOIN bd_cubasdoc bcub ON bcub.pk_cubasdoc = bcum.pk_cubasdoc "
					+ " LEFT JOIN bd_deptdoc bdep ON bdep.pk_deptdoc = i.cdeptid LEFT JOIN bd_psndoc bpsn "
					+ " ON bpsn.pk_psndoc = i.cemployeeid LEFT JOIN bd_payterm bpay ON bpay.pk_payterm = i.ctermprotocolid "
					+ " LEFT JOIN bd_cumandoc bcumo ON bcumo.pk_cumandoc = i.cpayunit LEFT JOIN bd_cubasdoc bcubo "
					+ " ON bcubo.pk_cubasdoc = bcumo.pk_cubasdoc LEFT JOIN bd_calbody bcal ON bcal.pk_calbody = i.cstoreorganization "
					+ " where 1=1 and nvl(i.dr,0)=0 and i.ibillstatus=3 "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);
			list = (ArrayList<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		}

		if (list == null || list.size() == 0) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> beanMap = (Map<String, Object>) list.get(i);
			voMap.put(i, beanMap);
		}
		return voMap;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, Object>> bxgtQueryMatInOrder(
			String strWhere, String zt) throws Exception {
		// 材料出库单
		BaseDAO dao = null;
		// 材料出库单查询
		String sql = "";
		Map<Integer, Map<String, Object>> voMap = new HashMap<Integer, Map<String, Object>>();
		strWhere = strWhere.replaceAll("dj.bill_date", "dbilldate");
		List list = new ArrayList();
		if (zt.equals("外账套")) {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ "a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,"
					+ "b.user_name as CAUDITORID,c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,"
					+ "e.businame as CBIZTYPE,r.custname as CCUSTOMERID,a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,"
					+ "g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,a.CINVENTORYID,a.CLASTMODIID,"
					+ "h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,a.COUTCALBODYID,"
					+ "a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,"
					+ "s.custname as CTRANCUSTID,l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as "
					+ "CWHSMANAGERID,a.DACCOUNTDATE,a.DAUDITDATE,a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,"
					+ "a.FREPLENISHFLAG,a.FSPECIALFLAG,a.IPRINTCOUNT,a.NDISCOUNTMNY,a.NNETMNY,m.bodyname as "
					+ "PK_CALBODY,n.unitname as PK_CORP,o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,a.PK_DEFDOC1,"
					+ "a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,"
					+ "a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,"
					+ "a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,"
					+ "a.PK_MEASWARE,p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,a.TMAKETIME,"
					+ "a.TS,a.VBILLCODE,a.VDILIVERADDRESS,a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,"
					+ "a.VUSERDEF11,a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,a.VUSERDEF15,a.VUSERDEF16,"
					+ "a.VUSERDEF17,a.VUSERDEF18,a.VUSERDEF19,a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,a.VUSERDEF4,"
					+ "a.VUSERDEF5,a.VUSERDEF6,a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 from ic_general_h a "
					+ "left join sm_user b on a.cauditorid = b.cuserid left join bd_billtype c "
					+ "on a.cbilltypecode = c.pk_billtypecode left join bd_psndoc d on a.cbizid = d.pk_psndoc "
					+ "left join bd_busitype e on a.cbiztype = e.pk_busitype left join bd_rdcl f "
					+ "on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc "
					+ "left join sm_user h on a.coperatorid = h.cuserid left join bd_cumandoc i "
					+ "on a.cproviderid = i.pk_cumandoc left join bd_cubasdoc j on i.pk_cubasdoc = j.pk_cubasdoc "
					+ "left join sm_user k on a.cregister = k.cuserid left join bd_stordoc l "
					+ "on a.cwarehouseid = l.pk_stordoc left join bd_calbody m on a.pk_calbody = m.pk_calbody "
					+ "left join bd_corp n on a.pk_corp = n.pk_corp left join bd_cubasdoc o "
					+ "on a.pk_cubasdoc = o.pk_cubasdoc left join bd_corp p on a.pk_purcorp = p.pk_corp "
					+ "left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc left join bd_cubasdoc r "
					+ "on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,"
					+ "bd_cubasdoc.custname from dm_trancust inner join bd_delivorg on bd_delivorg.pk_delivorg = dm_trancust.pkdelivorg "
					+ "inner join bd_cumandoc on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc inner join bd_cubasdoc "
					+ "on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join bd_cumandoc apman "
					+ "on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas "
					+ "on apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join bd_psndoc t "
					+ "on a.cwhsmanagerid = t.pk_psndoc where 1=1  and cbilltypecode='4D' and nvl(a.dr,0)=0 and a.fbillflag=3 "
					+ strWhere
					+ "and not exists(select 1 from bxgt_islock lk where lk.cgeneralhid = a.cgeneralhid) ";
			// dao = new BaseDAO(BxgtStepButton.DESIGN_A);
			list = this.getResultListMap(sql);
		} else {
			sql = "select (select case when count(1)>0 then 'Y' else 'N' end from bxgt_islock k where k.cgeneralhid=a.cgeneralhid) pk_sd, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isconfirm m where m.cgeneralhid=a.cgeneralhid) pk_qr, "
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_issyn k where k.cgeneralhid=a.cgeneralhid) pk_tb,"
					+ " (select case when count(1)>0 then 'Y' else 'N' end from bxgt_isorderseq k where k.cgeneralhid=a.cgeneralhid) pk_px, "
					+ "a.BASSETCARD,a.BDELIVEDTORM,a.BDIRECTTRANFLAG,a.BOUTRETFLAG,a.BSALECOOPPUR,"
					+ "b.user_name as CAUDITORID,c.billtypename as CBILLTYPECODE,d.psnname as CBIZID,"
					+ "e.businame as CBIZTYPE,r.custname as CCUSTOMERID,a.CDILIVERTYPEID,f.rdname as CDISPATCHERID,"
					+ "g.deptname as CDPTID,a.CENDREPORTID,a.CGENERALHID,a.CINVENTORYID,a.CLASTMODIID,"
					+ "h.user_name as COPERATORID,a.COTHERCALBODYID,a.COTHERCORPID,a.COTHERWHID,a.COUTCALBODYID,"
					+ "a.COUTCORPID,j.custname as CPROVIDERID,k.user_name as CREGISTER,a.CSETTLEPATHID,"
					+ "s.custname as CTRANCUSTID,l.storname as CWAREHOUSEID,a.CWASTEWAREHOUSEID,t.psnname as "
					+ "CWHSMANAGERID,a.DACCOUNTDATE,a.DAUDITDATE,a.DBILLDATE,a.DR,a.FALLOCFLAG,a.FBILLFLAG,"
					+ "a.FREPLENISHFLAG,a.FSPECIALFLAG,a.IPRINTCOUNT,a.NDISCOUNTMNY,a.NNETMNY,m.bodyname as "
					+ "PK_CALBODY,n.unitname as PK_CORP,o.custname as PK_CUBASDOC,a.PK_CUBASDOCC,a.PK_DEFDOC1,"
					+ "a.PK_DEFDOC10,a.PK_DEFDOC11,a.PK_DEFDOC12,a.PK_DEFDOC13,a.PK_DEFDOC14,a.PK_DEFDOC15,"
					+ "a.PK_DEFDOC16,a.PK_DEFDOC17,a.PK_DEFDOC18,a.PK_DEFDOC19,a.PK_DEFDOC2,a.PK_DEFDOC20,"
					+ "a.PK_DEFDOC3,a.PK_DEFDOC4,a.PK_DEFDOC5,a.PK_DEFDOC6,a.PK_DEFDOC7,a.PK_DEFDOC8,a.PK_DEFDOC9,"
					+ "a.PK_MEASWARE,p.unitname as PK_PURCORP,a.TACCOUNTTIME,a.TLASTMODITIME,a.TMAKETIME,"
					+ "a.TS,a.VBILLCODE,a.VDILIVERADDRESS,a.VHEADNOTE2,a.VNOTE,a.VUSERDEF1,a.VUSERDEF10,"
					+ "a.VUSERDEF11,a.VUSERDEF12,a.VUSERDEF13,a.VUSERDEF14,a.VUSERDEF15,a.VUSERDEF16,"
					+ "a.VUSERDEF17,a.VUSERDEF18,a.VUSERDEF19,a.VUSERDEF2,a.VUSERDEF20,a.VUSERDEF3,a.VUSERDEF4,"
					+ "a.VUSERDEF5,a.VUSERDEF6,a.VUSERDEF7,a.VUSERDEF8,a.VUSERDEF9 from ic_general_h a "
					+ "left join sm_user b on a.cauditorid = b.cuserid left join bd_billtype c "
					+ "on a.cbilltypecode = c.pk_billtypecode left join bd_psndoc d on a.cbizid = d.pk_psndoc "
					+ "left join bd_busitype e on a.cbiztype = e.pk_busitype left join bd_rdcl f "
					+ "on a.cdispatcherid = f.pk_rdcl left join bd_deptdoc g on a.cdptid = g.pk_deptdoc "
					+ "left join sm_user h on a.coperatorid = h.cuserid left join bd_cumandoc i "
					+ "on a.cproviderid = i.pk_cumandoc left join bd_cubasdoc j on i.pk_cubasdoc = j.pk_cubasdoc "
					+ "left join sm_user k on a.cregister = k.cuserid left join bd_stordoc l "
					+ "on a.cwarehouseid = l.pk_stordoc left join bd_calbody m on a.pk_calbody = m.pk_calbody "
					+ "left join bd_corp n on a.pk_corp = n.pk_corp left join bd_cubasdoc o "
					+ "on a.pk_cubasdoc = o.pk_cubasdoc left join bd_corp p on a.pk_purcorp = p.pk_corp "
					+ "left join bd_cumandoc q on a.ccustomerid = q.pk_cumandoc left join bd_cubasdoc r "
					+ "on q.pk_cubasdoc = r.pk_cubasdoc left join (select dm_trancust.pk_trancust,"
					+ "bd_cubasdoc.custname from dm_trancust inner join bd_delivorg on bd_delivorg.pk_delivorg = dm_trancust.pkdelivorg "
					+ "inner join bd_cumandoc on bd_cumandoc.pk_cumandoc = dm_trancust.pkcusmandoc inner join bd_cubasdoc "
					+ "on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join bd_cumandoc apman "
					+ "on apman.pk_cumandoc = bd_cumandoc.pk_cusmandoc2 left outer join bd_cubasdoc apbas "
					+ "on apbas.pk_cubasdoc = apman.pk_cubasdoc ) s on a.ctrancustid = s.pk_trancust left join bd_psndoc t "
					+ "on a.cwhsmanagerid = t.pk_psndoc where 1=1  and cbilltypecode='4D' and nvl(a.dr,0)=0 and a.fbillflag=3 "
					+ strWhere;
			dao = new BaseDAO(BxgtStepButton.DESIGN_B);
			list = (List<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		}

		if (list == null || list.size() <= 0) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> beanMap = (Map<String, Object>) list.get(i);
			voMap.put(i, beanMap);
		}

		return voMap;
	}

	// 销售出库单附表-累计结算 同步
	public void bxgtSynchroAccount(String[] pkSaleOutOrders) throws Exception {

		// 根据销售出库单主表PK 查出所有子表PK
		String queryBodyPks = "select distinct cgeneralbid from ic_general_b where nvl(dr,0) =0 and cgeneralhid in ";

		String[] bodyPks = this.queryPks1ByPks2(pkSaleOutOrders, queryBodyPks);

		// 查询销售出库单附表-累计结算
		ArrayList<BxgtAccountVO> accountVoList = queryAccount(bodyPks);
		
		for(BxgtAccountVO vo : accountVoList){
			vo.setBsettleendflag("N");
			vo.setNzgyfmoney(new UFDouble(0));
			vo.setNzgyfprice(new UFDouble(0));
			vo.setNfeemny(new UFDouble(0));
			vo.setNoriginalcurmny(new UFDouble(0));
			vo.setNpmoney(new UFDouble(0));
			vo.setNpprice(new UFDouble(0));
			vo.setNsignnum(new UFDouble(0));
			vo.setNoriginaltaxpricemny(new UFDouble(0));
			vo.setNaccountnum1(new UFDouble(0));
			vo.setNaccountnum2(new UFDouble(0));
			vo.setNaccountmny(new UFDouble(0));
			vo.setNorgnettaxprice(new UFDouble(0));
			vo.setNoriginalnetprice(new UFDouble(0));
		}

		// B帐累计结算vo新增 SQL数组
		// String insertVOSQLs[] = makeInsertAccountVoSQLs(accountVoList);

		// 批量执行SQL类
		BaseDAO dmo = new BaseDAO(BxgtStepButton.DESIGN_B);
		if (accountVoList != null && accountVoList.size() > 0) {
			dmo
					.insertVOArrayWithPK(accountVoList
							.toArray(new BxgtAccountVO[0]));
		}
	}

	/**
	 * 查询销售出库单附表-累计结算
	 * 
	 * @param bodyPks
	 * @return
	 */
	private ArrayList<BxgtAccountVO> queryAccount(String[] bodyPks)
			throws Exception {
		String queryAccountSql = "select * from ic_general_bb3 where nvl(dr,0) = 0 and cgeneralbid in ";

		ArrayList<BxgtAccountVO> accountVoList = (ArrayList<BxgtAccountVO>) this
				.queryBeanPks1ByPks2(bodyPks, queryAccountSql,
						BxgtAccountVO.class);
		return accountVoList;
	}

	public void bxgtSynchroSpace(String[] pkSaleOutOrders) throws Exception {

		// 子表PK数组
		String[] bodyPks = null;
		// 根据销售出库单主表PK 查出所有子表PK
		String queryBodyPks = "select distinct cgeneralbid from ic_general_b where nvl(dr,0) = 0 and cgeneralhid in ";

		bodyPks = this.queryPks1ByPks2(pkSaleOutOrders, queryBodyPks);
		// 查询销售出库单附表-货位
		ArrayList<BxgtSpaceVo> spaceVoList = querySpace(bodyPks);

		BaseDAO dmo = new BaseDAO(BxgtStepButton.DESIGN_B);
		if (spaceVoList != null && spaceVoList.size() > 0) {
			dmo.insertVOArrayWithPK(spaceVoList.toArray(new BxgtSpaceVo[0]));
		}
	}

	/**
	 * 根据销售出库单子表pk查询A账对应的货位表
	 * 
	 * @param pks
	 * @return
	 * @throws DAOException
	 */
	private ArrayList<BxgtSpaceVo> querySpace(String[] pks) throws Exception {
		String querySpaceSql = "select * from ic_general_bb1 where nvl(dr,0) = 0 and cgeneralbid in ";
		ArrayList<BxgtSpaceVo> spaceVoList = (ArrayList<BxgtSpaceVo>) this
				.queryBeanPks1ByPks2(pks, querySpaceSql, BxgtSpaceVo.class);
		return spaceVoList;
	}

	/**
	 * 销售订单
	 */
	public boolean bxgtSynchroSaleOrder(String[] pkSaleOrders) throws Exception {
		// 2015-4-7 bwy
		// B帐销售订单表头vo新增 SQL数组
		// String insertHeadVOSQLs[] = null;
		// B帐销售订单表体vo新增 SQL数组
		// String insertBodyVOSQLs[] = null;

		// 拼接销售订单表头VO查询SQL
		String getHeadVoSQL = "select * from so_sale where nvl(dr,0) = 0 and csaleid in ";
		// 拼接销售订单子表VO查询SQL
		String getBodyVoSQL = "select * from so_saleorder_b where nvl(dr,0) = 0 and csaleid in ";
		// 拼接销售订单执行表VO查询SQL
		String getExecuteVoSQL = "select * from so_saleexecute where nvl(dr,0) = 0 and creceipttype='30' and csaleid in ";
		// 查询销售订单表头VO
		ArrayList<SaleOrderHVO> headVoList = (ArrayList<SaleOrderHVO>) this
				.queryBeanPks1ByPks2(pkSaleOrders, getHeadVoSQL,
						SaleOrderHVO.class);
		// 查询销售订单表体VO
		ArrayList<SaleOrderBVO> bodyVoList = (ArrayList<SaleOrderBVO>) this
				.queryBeanPks1ByPks2(pkSaleOrders, getBodyVoSQL,
						SaleOrderBVO.class);
		// 查询销售执行表
		ArrayList<SaleOrderExecuteVO> exeVoList = (ArrayList<SaleOrderExecuteVO>) this
				.queryBeanPks1ByPks2(pkSaleOrders, getExecuteVoSQL,
						SaleOrderExecuteVO.class);

		// 2015-5-5 根据销售订单查询下游单据（销售出库单）
		String[] pkSaleOuts = this.getCSaleOutByCsaleid(pkSaleOrders,
				BxgtStepButton.DESIGN_A);

		// 根据销售订单主键获得收款单主表主键
		// String[] vouchids = getSKpksB(pkSaleOrders, BxgtStepButton.DESIGN_A);

		// 插入数据前先删除
		this.deleteBill(pkSaleOrders, IBxgtBillType.SALE_ORDER);

		// 批量执行SQL类
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);

		// 批量插入销售订单表头VO到B帐
		dao.insertVOArrayWithPK(headVoList.toArray(new SaleOrderHVO[0]));
		// 批量插入销售订单表体VO到B帐
		dao.insertVOArrayWithPK(bodyVoList.toArray(new SaleOrderBVO[0]));
		// 插入销售执行表
		dao.insertVOArrayWithPK(exeVoList.toArray(new SaleOrderExecuteVO[0]));

		// 刷新开票客商值
		this.resetDatetoNull(pkSaleOrders, BxgtStepButton.N30);

		// 销售订单导入后同时生成产成品入库单
		bxgtSynchroProducIn(pkSaleOrders);
		if (pkSaleOuts != null && pkSaleOuts.length > 0) {
			// 同步该销售订单下游的销售出库单 2015-5-5 bwy
			bxgtSynchroSaleOutOrder(pkSaleOuts);
		}
		// 同步销售订单对应的收款单
		// bxgtSynchroSK(vouchids);

		// 同步AB帐套的同步状态
		this.synABSaleAllData(pkSaleOrders, pkSaleOuts);
		return true;
	}

	/**
	 * 销售出库
	 */
	public boolean bxgtSynchroSaleOutOrder(String[] pkSaleOutOrders)
			throws Exception {

		// 拼接销售出库单表头VO查询SQL
		String getHeadVoSQL = "select * from ic_general_h where nvl(dr,0) =0 and cgeneralhid in ";
		// 拼接销售出库单子表VO查询SQL
		String getBodyVoSQL = "select * from ic_general_b where nvl(dr,0) =0 and cgeneralhid in ";

		// 查询销售出库单表头VO
		ArrayList<BxgtCcpHeadVO> headVoList = (ArrayList<BxgtCcpHeadVO>) this
				.queryBeanPks1ByPks2(pkSaleOutOrders, getHeadVoSQL,
						BxgtCcpHeadVO.class);
		// 查询销售出库单表体VO
		ArrayList<BxgtCcpBodyVo> bodyVoList = (ArrayList<BxgtCcpBodyVo>) this
				.queryBeanPks1ByPks2(pkSaleOutOrders, getBodyVoSQL,
						BxgtCcpBodyVo.class);

		// 插入数据前先删除
		this.deleteBill(pkSaleOutOrders, IBxgtBillType.SALE_OUT);

		// 批量执行SQL类
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 批量插入销售出库单表头VO到B帐
		dao.insertVOArrayWithPK(headVoList.toArray(new BxgtCcpHeadVO[0]));
		// 批量插入销售出库单表体VO到B帐
		dao.insertVOArrayWithPK(bodyVoList.toArray(new BxgtCcpBodyVo[0]));

		// 2015-5-5 bwy 插入对应的销售出库单附表-货位
		bxgtSynchroSpace(pkSaleOutOrders);
		// 2015-5-5 bwy 插入对应的销售出库单附表-累计结算
		bxgtSynchroAccount(pkSaleOutOrders);
		// 插入到同步表中
		this.synSaleOutBill(pkSaleOutOrders);
		return true;
	}

	/**
	 * 采购订单
	 */
	public boolean bxgtSynchroPurchaseOrder(String[] pkPurchaseOrders)
			throws Exception {

		// 执行采购订单供应商检查
		this.checkSupplierByPurIn(pkPurchaseOrders);
		// 2015.5.18 xm
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);

		// B帐采购订单表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐采购订单表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 根据采购订单查询上游游单据（请购单）
		String[] pkPraybills = getPraybillPks(pkPurchaseOrders);

		// 找到采购订单上游单据请购单 获得B账中下游采购订单pks(防止有相同的请购单)
		String[] pkPurchaseOrdersB = null;

		if (pkPraybills != null && pkPraybills.length > 0) {
			// 同步采购订单上游请购单
			bxgtSynchroPraybill(pkPraybills);
			pkPurchaseOrdersB = getPuOrdersByPraybillB(pkPraybills);
		}
		// 拼接采购订单和他的兄弟
		List<String> list = new ArrayList<String>();
		for (String pkPurchaseOrder : pkPurchaseOrders) {
			list.add(pkPurchaseOrder);
		}
		if (pkPurchaseOrdersB != null && pkPurchaseOrdersB.length > 0) {
			for (String pkPurchaseOrder : pkPurchaseOrdersB) {
				list.add(pkPurchaseOrder);
			}
		}

		String[] purChaseOrders = list.toArray(new String[0]);

		// 拼接采购订单表头VO查询SQL
		String getHeadVoSQL = "select * from po_order  where corderid  in ('";
		// 拼接采购订单子表VO查询SQL
		String getBodyVoSQL = "select * from po_order_b where corderid  in ('";
		for (int i = 0; i < purChaseOrders.length; i++) {
			getHeadVoSQL += purChaseOrders[i] + "','";
			getBodyVoSQL += purChaseOrders[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";
		// 查询采购订单表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		// 查询采购订单表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);
		//修改累计发票金额为0 2016-09-22
		for(Map<String, Object> blist :bodyVoList){
			blist.put("naccuminvoicenum", 0);
		}
		// 拼接B帐表头VO新增数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PURCHASE_ORDER);
		// 拼接B帐表体VO新增数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PURCHASE_ORDER);
		// ------------------------------------------------------------------------------

		if (purChaseOrders != null && purChaseOrders.length > 0) {
			// 插入数据前先删除
			this.deletePurchaseBill(purChaseOrders,
					IBxgtBillType.PURCHASE_ORDER);
		}

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);

		// 批量插入采购订单表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入采购订单表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);

		// 清空自定义项
		this.resetDatetoNull(purChaseOrders, BxgtStepButton.N21);

		// 根据采购订单查询下游单据（采购入库单）
		String[] pkPurchaseIns = getPurchaseInByPurchaseOrder(purChaseOrders,
				BxgtStepButton.DESIGN_A);
		String[] pkinvoices = null;// 发票
		// 同步采购订单下游的采购入库单
		if (pkPurchaseIns != null && pkPurchaseIns.length > 0) {
			pkinvoices = bxgtSynchroPurchaseInOrder(pkPurchaseIns,
					purChaseOrders);
		}
		// 同步表
		this.synABOrderAllData(purChaseOrders, pkPurchaseIns, pkinvoices);
		// 修改公司
		this.checkAndChangeCorp(pkPraybills, purChaseOrders, pkPurchaseIns,
				null);

		return true;
	}

	/**
	 * 采购入库单同步
	 */
	public boolean bxgtSynchroPurInOrder(String[] pkPurchaseInOrders)
			throws Exception {
		// 采购入库单同步2015.5.19 xm
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// B帐采购入库单表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐采购入库单表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 拼接采购入库单表头VO查询SQL
		String getHeadVoSQL = "select * from ic_general_h where cgeneralhid in ('";
		// 拼接采购入库单子表VO查询SQL
		String getBodyVoSQL = "select * from ic_general_b where cgeneralhid in ('";
		for (int i = 0; i < pkPurchaseInOrders.length; i++) {
			getHeadVoSQL += pkPurchaseInOrders[i] + "','";
			getBodyVoSQL += pkPurchaseInOrders[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";

		// 查询采购入库单表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		// 查询采购入库单表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);
//		2016-09-22
		for(Map<String, Object> hlist:headVoList){
			hlist.put("cauditorid", null);
		}
		for(Map<String, Object> blist:bodyVoList){
			blist.put("isok", "N");
			blist.put("bzgflag", "N");
		}
		// 拼接B帐表头VO新增SQL数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PURCHASE_IN);
		// 拼接B帐表体VO新增SQL数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PURCHASE_IN);

		// 插入数据前先删除
		this.deletePurchaseBill(pkPurchaseInOrders, IBxgtBillType.PURCHASE_IN);

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		// 批量插入采购入库单表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入采购入库单表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);

		// 插入对应的采购入库单附表-货位
		bxgtSynchroSpace(pkPurchaseInOrders);
		// 插入对应的采购入库单附表-累计结算
		bxgtSynchroAccount(pkPurchaseInOrders);

		// 同步ia_bill
		this.sysIaBillByCgeneralhid(pkPurchaseInOrders);

		// 刷新数据库清空部分值
		this.resetDatetoNull(pkPurchaseInOrders, BxgtStepButton.N13);

		// 获得采购入库对应的采购发票pks
		// String[] pkPurchaseInvoices =
		// getPurchaseInvoicePks(pkPurchaseInOrders);
		// 同步采购入库对应的采购发票
		// if (pkPurchaseInvoices != null && pkPurchaseInvoices.length > 0) {
		// bxgtSynchroPurInvoiceOrder(pkPurchaseInvoices);
		// }
		// 同步表
		this.synSaleOutBill(pkPurchaseInOrders);
		// 刷新公司
		this.checkAndChangeCorp(null, null, pkPurchaseInOrders, null);
		return true;
	}

	/**
	 * 采购发票同步
	 */
	public boolean bxgtSynchroPurInvoiceOrder(String[] pkPurchaseInvoiceOrders)
			throws Exception {
		// 采购发票同步2015.5.19 xm
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// B帐采购发票表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐采购发票表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 拼接销售发票表头VO查询SQL
		String getHeadVoSQL = "select * from po_invoice where cinvoiceid in ('";
		// 拼接销售发票子表VO查询SQL
		String getBodyVoSQL = "select * from po_invoice_b where cinvoiceid in ('";
		for (int i = 0; i < pkPurchaseInvoiceOrders.length; i++) {
			getHeadVoSQL += pkPurchaseInvoiceOrders[i] + "','";
			getBodyVoSQL += pkPurchaseInvoiceOrders[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";
		// 查询销售发票表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		// 查询销售发票表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);

		// 拼接B帐表头VO新增SQL数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PURCHASE_INVOICE);
		// 拼接B帐表体VO新增SQL数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PURCHASE_INVOICE);

		// 插入数据前先删除
		this.deletePurchaseBill(pkPurchaseInvoiceOrders,
				IBxgtBillType.PURCHASE_INVOICE);

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);

		// 批量插入销售发票表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入销售发票表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);

		// 刷新数据 清空部分值
		this.resetDatetoNull(pkPurchaseInvoiceOrders, BxgtStepButton.N25);

		// 同步完修改发票税率
		this.batchInvoiceTaxRate(pkPurchaseInvoiceOrders, new UFDouble(17),
				BxgtStepButton.TBFP);
		// 同步表
		this.synInvoiceBill(pkPurchaseInvoiceOrders);
		// 修改公司
		this.checkAndChangeCorp(null, null, null, pkPurchaseInvoiceOrders);
		return true;
	}

	/**
	 * 材料出库同步
	 */
	public ArrayList<Object[]> bxgtSynMaterialOrder(String[] pkMaterials)
			throws Exception {

		String pk = "";
		for (String pkMaterial : pkMaterials) {
			pk += "'" + pkMaterial + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));
		String deleteh = "delete from ic_general_h where nvl(dr,0)=0 and cgeneralhid in("
				+ pk + ")";
		String deleteb = "delete from ic_general_b where nvl(dr,0)=0 and cgeneralhid in("
				+ pk + ")";
		String sql2 = deleteb.replaceFirst("delete", "select cgeneralbid");
		String deletebb = "delete from ic_general_bb1 where nvl(dr,0)=0 and cgeneralbid in("
				+ sql2 + ")";
		// 同步之前删除B的所有数据
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.executeUpdate(deletebb);
		dao2.executeUpdate(deleteb);
		dao2.executeUpdate(deleteh);

		// 插入数据之前校验库存可用量
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		String sql = "select max(dbilldate) from ic_general_h where nvl(dr,0)=0 and cgeneralhid in("
				+ pk + ")";
		String sqlm = "select distinct cinvbasid from ic_general_b where nvl(dr,0)=0 and cgeneralhid in("
				+ pk + ")";
		String billdate = (String) this.getResultObject(sql);

		ArrayList<String> metlist = (ArrayList<String>) this
				.getResultListColumn(sqlm);
		String pk_material = "";
		if (metlist.size() <= 0) {
			throw new BusinessException("选中的材料出库单据未录入表体存货信息，请核查！");
		}
		for (int i = 0; i < metlist.size(); i++) {
			pk_material += "'" + metlist.get(i) + "',";
		}
		pk_material = pk_material.substring(0, pk_material.lastIndexOf(","));
		// 查出B帐库存中有的物料
		String sqlm2 = "select distinct cinvbasid from ic_general_b where nvl(dr,0)=0 and dbizdate<='"
				+ billdate + "' and cinvbasid in(" + pk_material + ")";
		ArrayList<String> metlist2 = (ArrayList<String>) dao2.executeQuery(
				sqlm2, new ColumnListProcessor());
		String pk_met2 = "";
		if (metlist2 != null && metlist2.size() > 0) {
			for (int i = 0; i < metlist2.size(); i++) {
				pk_met2 += "'" + metlist2.get(i) + "',";
			}
			pk_met2 = pk_met2.substring(0, pk_met2.lastIndexOf(","));
		}
		if ("".equals(pk_met2)) {
			throw new BusinessException("库存中出库产品出错，请选择开票客商提供的物料");
		}
		// 处理
		ArrayList<Object[]> list = this.checkOnHandMaterial(pk, pk_met2,
				billdate);
		/*
		 * if (list != null && list.size() > 0) { return list; }
		 */

		// 查询出A帐套的主表，子表，货位表
		String sqlbody = "select * from ic_general_b where nvl(dr,0)=0 and cgeneralhid in("
				+ pk + ") and cinvbasid in(" + pk_met2 + ")";
		String sqlrep1 = sqlbody.replaceFirst("\\*", "distinct cgeneralhid");
		String sqlhead = "select * from ic_general_h where nvl(dr,0)=0 and cgeneralhid in("
				+ sqlrep1 + ")";
		String sqlrep2 = sqlbody.replaceFirst("\\*", "distinct cgeneralbid");
		String sqlhwei = "select * from ic_general_bb1 where nvl(dr,0)=0 and cgeneralbid in("
				+ sqlrep2 + ")";
		List<BxgtCcpHeadVO> hlist = (List<BxgtCcpHeadVO>) this
				.getResultBeanList(sqlhead, BxgtCcpHeadVO.class);
		List<BxgtCcpBodyVo> blist = (List<BxgtCcpBodyVo>) this
				.getResultBeanList(sqlbody, BxgtCcpBodyVo.class);
		List<BxgtSpaceVo> bblist = (List<BxgtSpaceVo>) this.getResultBeanList(
				sqlhwei, BxgtSpaceVo.class);
		if (hlist == null || hlist.size() <= 0 || blist == null
				|| blist.size() <= 0 || bblist == null || bblist.size() <= 0) {
			return null;
		}
		//2016-09-22
		BxgtCcpHeadVO[] hvos = hlist.toArray(new BxgtCcpHeadVO[0]);
		BxgtCcpBodyVo[] bvos = blist.toArray(new BxgtCcpBodyVo[0]);
		BxgtSpaceVo[] bbvos = bblist.toArray(new BxgtSpaceVo[0]);
		for(BxgtCcpHeadVO hvo:hvos){
			hvo.setCauditorid(null);
		}
		for(BxgtCcpBodyVo bvo:bvos){
			bvo.setIsok("N");
			bvo.setBzgflag("N");
		}
		// 插入数据
		dao2.insertVOArrayWithPK(hvos);
		dao2.insertVOArrayWithPK(bvos);
		dao2.insertVOArrayWithPK(bbvos);

		String[] pks = new String[hvos.length];
		for (int i = 0; i < hvos.length; i++) {
			pks[i] = hvos[i].getCgeneralhid();
		}

		// 同步ia_bill表
		this.sysIaBillByCgeneralhid(pks);
		// 刷新数据，清空部分值
		this.resetDatetoNull(pks, BxgtStepButton.N14);

		ArrayList<String> mpk = (ArrayList<String>) dao2.executeQuery(sqlrep1,
				new ColumnListProcessor());
		// 同步表
		this.synSaleOutBill(mpk.toArray(new String[0]));
		// 公司
		this.checkAndChangeCorp(null, null, mpk.toArray(new String[0]), null);
		return list;
	}

	/**
	 * A账根据销售订单主表pk获得销售出库单主表pk
	 * 
	 * @param csaleids
	 * @return
	 * @throws DAOException
	 */
	private String[] getCSaleOutByCsaleid(String[] csaleids, String design)
			throws Exception {
		// 获得销售出库单主表pk
		String sql = "select distinct cgeneralhid from ic_general_b where nvl(dr,0)=0 and cbodybilltypecode='4C' and cfirstbillhid in ";

		String[] cgeneralhids = null;
		if (design.equals(BxgtStepButton.DESIGN_A)) {
			cgeneralhids = this.queryPks1ByPks2(csaleids, sql);
		} else if (design.equals(BxgtStepButton.DESIGN_B)) {
			cgeneralhids = this.queryPks1ByPks2BZT(csaleids, sql);
		}

		return cgeneralhids;
	}

	/**
	 * 同步ia_bill数据
	 */
	private void sysIaBillByCgeneralhid(String[] cgeneralhids) throws Exception {

		if (cgeneralhids == null || cgeneralhids.length <= 0) {
			return;
		}

		String sql = "select distinct cbillid from ia_bill_b where cicbillid in ";
		String[] iabillpks = this.queryPks1ByPks2(cgeneralhids, sql);
		if (iabillpks == null || iabillpks.length <= 0) {
			return;
		}

		String sqlhead = "select * from ia_bill where nvl(dr,0)=0 and cbillid in ";
		String sqlbody = "select * from ia_bill_b where nvl(dr,0)=0 and cicbillid in ";
		ArrayList<BxgtIabillHVO> headvos = (ArrayList<BxgtIabillHVO>) this
				.queryBeanPks1ByPks2(iabillpks, sqlhead, BxgtIabillHVO.class);
		ArrayList<BxgtIabillBVO> bodyvos = (ArrayList<BxgtIabillBVO>) this
				.queryBeanPks1ByPks2(cgeneralhids, sqlbody, BxgtIabillBVO.class);

		if (bodyvos != null && bodyvos.size() > 0) {
			BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
			/*
			 * for(BxgtIabillHVO hvo:headvos){ hvo.setPk_corp(pk_corp);
			 * hvo.setCrdcenterid(pk_calbody);
			 * hvo.setCstockrdcenterid(pk_calbody); } for(BxgtIabillBVO
			 * bvo:bodyvos){ bvo.setCrdcenterid(pk_calbody); }
			 */
			dao.insertVOArrayWithPK(headvos.toArray(new BxgtIabillHVO[0]));
			dao.insertVOArrayWithPK(bodyvos.toArray(new BxgtIabillBVO[0]));
		}
	}

	/**
	 * B账根据销售订单主表pk获得销售出库单主表pk,产成品入库单
	 * 
	 * @throws DAOException
	 */
	private String[] getCgeneralhidByCsaleidB(String[] csaleids)
			throws Exception {
		// 获得销售出库单主表pk
		String sql = "select distinct cgeneralhid from ic_general_b where nvl(dr,0)=0 and "
				+ " cbodybilltypecode in('46','4C') and cfirstbillhid in ";

		String[] cgeneralhids = null;
		cgeneralhids = this.queryPks1ByPks2BZT(csaleids, sql);
		return cgeneralhids;
	}

	/**
	 * B账根据销售订单主键获取收款单主键
	 * 
	 * @param pk_saleouts
	 * @return
	 */
	private String[] getSKpksB(String[] pk_saleorders, String design)
			throws Exception {

		// 查询收款单主键SQL
		String querySKpkSQL = "select distinct vouchid from arap_djfb where nvl(dr,0) = 0 and djlxbm = 'D2' and ddlx in ";
		/*
		 * for (int i = 0; i < pk_saleorders.length; i++) { querySKpkSQL +=
		 * pk_saleorders[i] + "','"; } querySKpkSQL = querySKpkSQL.substring(0,
		 * querySKpkSQL.lastIndexOf(",")) + ") and nvl(dr,0) = 0 and djlxbm =
		 * 'D2'"; // 收款单主键集合
		 */String[] skPks = null;
		if (design.equals(BxgtStepButton.DESIGN_A)) {
			skPks = this.queryPks1ByPks2(pk_saleorders, querySKpkSQL);
		} else if (design.equals(BxgtStepButton.DESIGN_B)) {
			skPks = this.queryPks1ByPks2BZT(pk_saleorders, querySKpkSQL);
		}

		return skPks;
	}

	// bwy 2015-5-26 更改逻辑，销售订单导入时新增产成品入库单
	private void bxgtSynchroProducIn(String[] csaleid) throws Exception {
		// 产成品入库单新增
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 拼接销售订单表头VO查询SQL
		String getHeadVoSQL = "select * from so_sale where nvl(dr,0)=0 and csaleid in ";
		// 拼接销售订单子表VO查询SQL
		String getBodyVoSQL = "select * from so_saleorder_b where nvl(dr,0)=0 and csaleid in ";

		// 查询销售订单表头VO
		ArrayList<Map<String, Object>> saleHeadVoList = (ArrayList<Map<String, Object>>) this
				.queryMapPks1ByPks2(csaleid, getHeadVoSQL);
		// 查询销售订单表体VO
		ArrayList<Map<String, Object>> saleBodyVoList = (ArrayList<Map<String, Object>>) this
				.queryMapPks1ByPks2(csaleid, getBodyVoSQL);

		// GeneralBillVO billVo = new GeneralBillVO();
		BxgtCcpHeadVO[] headVos = new BxgtCcpHeadVO[saleHeadVoList.size()];
		BxgtCcpBodyVo[] bodyVos = new BxgtCcpBodyVo[saleBodyVoList.size()];

		// 获得产成品入库单的主键
		String[] billpks = this.getsynProductInBill(csaleid);
		if (billpks != null && billpks.length > 0) {
			this.deleteBill(billpks, IBxgtBillType.CCP_IN);
		}
		// 获取仓库
		String sql = "select pk_stordoc from bd_stordoc where nvl(dr,0)=0 and storname='成品库' and pk_corp='1001' ";
		Object pk_warehouse = dao2.executeQuery(sql, new ColumnProcessor());
		if (pk_warehouse == null) {
			throw new BusinessException("生成产成品入库单时未找到当前公司下名为成品库的仓库！");
		}

		HashMap<String, String> map = new HashMap<String, String>();// 表头主键
		HashMap<String, String> map1 = new HashMap<String, String>();// 库存组织
		HashMap<String, String> map2 = new HashMap<String, String>();// 时间
		// 插入表头
		String[] billcodes = null;
		if (saleHeadVoList != null && saleHeadVoList.size() > 0) {
			BillcodeGenerater bcg = new BillcodeGenerater();
			billcodes = bcg.getBatchBillCodes("46", null, null, saleHeadVoList
					.size());
		}
		for (int i = 0; i < saleHeadVoList.size(); i++) {
			BxgtCcpHeadVO headVo = new BxgtCcpHeadVO();
			headVo.setBassetcard("N");
			headVo.setDr(0);
			headVo.setCbilltypecode("46");
			headVo.setCbizid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"cemployeeid")));
			headVo.setCbiztype(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"cbiztype")));
			headVo.setCcustomerid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"ccustomerid")));
			headVo.setCdilivertypeid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"ctransmodeid")));
			headVo.setCdispatcherid("1002V2100000000010C3");// 收发类别 产成品入库
			headVo.setCdptid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"cdeptid")));
			headVo.setCoperatorid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"coperatorid")));
			headVo.setCproviderid(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"cproviderid")));
			headVo.setCregister(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"cproviderid"))); // 库房签字人
			// headVo.setCwarehouseid("1001V21000000002FAIT");
			UFDateTime dtime = new UFDateTime(saleHeadVoList.get(i).get(
					"dbilldate").toString());
			dtime = this.getRandomDateTime(dtime);
			headVo.setDbilldate(dtime.toString().substring(0, 10));// 单据日期
			headVo.setDaccountdate(dtime.toString().substring(0, 10)); // 库房签字日期
			headVo.setFbillflag(3);
			headVo.setTmaketime(dtime.toString());
			headVo.setTs(dtime.toString());
			headVo.setFreplenishflag("N");
			headVo.setPk_calbody(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"ccalbodyid")));
			headVo.setPk_corp(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"pk_corp")));
			headVo.setCwarehouseid(pk_warehouse.toString());// 默认设置为成品库
			headVo.setVuserdef10(BFPubTool
					.getString_TrimZeroLenAsNull(saleHeadVoList.get(i).get(
							"csaleid"))); // vdef10保存导入的销售订单的 pk
			// 单据号
			headVo.setVbillcode(billcodes[i]);
			String headPk = dao2.insertVO(headVo);
			headVo.setCgeneralhid(headPk);
			headVos[i] = headVo;
			// 存入map
			Object cbody = saleHeadVoList.get(i).get("ccalbodyid");
			if (cbody == null) {
				cbody = "";
			}
			map.put(saleHeadVoList.get(i).get("csaleid").toString(), headPk);
			map1.put(saleHeadVoList.get(i).get("csaleid").toString(), cbody
					.toString());
			map2.put(saleHeadVoList.get(i).get("csaleid").toString(), dtime
					.toString());
		}

		// 插入表体
		for (int i = 0; i < saleBodyVoList.size(); i++) {
			BxgtCcpBodyVo bodyVo = new BxgtCcpBodyVo();
			bodyVo.setBbarcodeclose("N");
			bodyVo.setBonroadflag("N");
			bodyVo.setBreturnprofit("N");
			bodyVo.setBsafeprice("N");
			bodyVo.setBsourcelargess("N");
			bodyVo.setBtoinzgflag("N");
			bodyVo.setBtoouttoiaflag("N");
			bodyVo.setBtooutzgflag("N");
			bodyVo.setBtou8rm("N");
			bodyVo.setBzgflag("N");
			bodyVo.setDr(0);
			bodyVo.setCastunitid(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"cpackunitid")));
			bodyVo.setCbodybilltypecode("46");
			bodyVo.setCbodywarehouseid(pk_warehouse.toString());
			// 插入主表主键
			bodyVo
					.setCgeneralhid(map.get(saleBodyVoList.get(i)
							.get("csaleid")));
			bodyVo.setCinvbasid(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"cinvbasdocid")));
			bodyVo.setCinventoryid(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"cinventoryid")));
			bodyVo.setCquoteunitid(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"cquoteunitid")));
			// bodyVo.setCquotecurrency("");币种
			bodyVo.setCreceieveid(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"creceiptcorpid")));
			bodyVo.setCrowno(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"crowno")));
			bodyVo.setDbizdate(map2.get(saleBodyVoList.get(i).get("csaleid"))
					.toString().substring(0, 10)); // 入库日期
			bodyVo.setDdeliverdate(map2.get(
					saleBodyVoList.get(i).get("csaleid")).toString().substring(
					0, 10));
			bodyVo.setFchecked(0);
			bodyVo.setFlargess("N");
			bodyVo.setIdesatype(0);
			bodyVo.setIsok("N");
			bodyVo.setTs(map2.get(saleBodyVoList.get(i).get("csaleid"))
					.toString());
			bodyVo.setNbarcodenum(UFDouble.ZERO_DBL);
			bodyVo
					.setNinassistnum(saleBodyVoList.get(i).get("npacknumber") == null ? null
							: new UFDouble(saleBodyVoList.get(i).get(
									"npacknumber").toString()));
			bodyVo.setNinnum(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("nnumber")));
			if (bodyVo.getNinassistnum() == null) {
				bodyVo.setHsl(null);
			} else {
				bodyVo.setHsl(bodyVo.getNinnum().div(bodyVo.getNinassistnum()));
			}

			bodyVo.setNmny(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList.get(
					i).get("noriginalcursummny")));
			bodyVo.setNprice(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("noriginalcurtaxprice")));
			bodyVo.setNquotemny(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("noriginalcurtaxnetprice")));
			bodyVo.setNquotentmny(BFPubTool
					.getUFDouble_NullAsZero(saleBodyVoList.get(i).get(
							"noriginalcurnetprice")));
			bodyVo
					.setNquotentprice(BFPubTool
							.getUFDouble_NullAsZero(saleBodyVoList.get(i).get(
									"nprice")));
			bodyVo.setNquoteprice(BFPubTool
					.getUFDouble_NullAsZero(saleBodyVoList.get(i).get(
							"norgqttaxprc")));
			bodyVo.setNquoteunitnum(BFPubTool
					.getUFDouble_NullAsZero(saleBodyVoList.get(i)
							.get("nnumber")));
			bodyVo.setNquoteunitrate(BFPubTool
					.getUFDouble_NullAsZero(saleBodyVoList.get(i).get(
							"nqtscalefactor")));
			bodyVo.setNsalemny(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("noriginalcurmny")));
			bodyVo.setNsaleprice(BFPubTool
					.getUFDouble_NullAsZero(saleBodyVoList.get(i).get(
							"noriginalcursummny")));
			bodyVo.setNshouldinnum(null);
			// bodyVo.setNshouldoutassistnum(BFPubTool
			// .getUFDouble_NullAsZero(saleBodyVoList.get(i).get("")));
			// bodyVo.setNshouldoutnum(BFPubTool
			// .getUFDouble_NullAsZero(saleBodyVoList.get(i).get("")));
			bodyVo.setNtaxmny(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("noriginalcursummny")));
			bodyVo.setNtaxprice(BFPubTool.getUFDouble_NullAsZero(saleBodyVoList
					.get(i).get("noriginalcurtaxprice")));
			bodyVo.setPk_bodycalbody(BFPubTool.getString_TrimZeroLenAsNull(map1
					.get(saleBodyVoList.get(i).get("csaleid"))));
			bodyVo.setPk_corp(BFPubTool
					.getString_TrimZeroLenAsNull(saleBodyVoList.get(i).get(
							"pk_corp")));

			bodyVo.setCfirstbillhid(saleBodyVoList.get(i).get("csaleid")
					.toString());
			bodyVo.setCsourcebillhid(saleBodyVoList.get(i).get("csaleid")
					.toString());
			bodyVo.setCfirsttype("30");
			bodyVo.setCfirstbillbid(saleBodyVoList.get(i).get("corder_bid")
					.toString());
			bodyVo.setCsourcebillbid(saleBodyVoList.get(i).get("corder_bid")
					.toString());
			bodyVo.setCsourcetype("30");
			bodyVos[i] = bodyVo;
		}

		String[] pk_bs = dao2.insertVOArray(bodyVos);
		for (int i = 0; i < bodyVos.length; i++) {
			bodyVos[i].setCgeneralbid(pk_bs[i]);
		}
		// 产成品入库单新增后插入单据附表-货位
		insertSpace(bodyVos);

		// 插入完生成ia_bill (by tcl)
		HashMap<String, String> mapkey = new HashMap<String, String>();// 表头主键
		HashMap<String, String> mapcode = new HashMap<String, String>();// 表头单据号
		HashMap<String, String> mapcode2 = new HashMap<String, String>();// 表头单据号ia_bill

		String[] billcodes2 = null;
		if (headVos != null && headVos.length > 0) {
			BillcodeGenerater bcg = new BillcodeGenerater();
			billcodes2 = bcg
					.getBatchBillCodes("I3", null, null, headVos.length);
		}
		for (int i = 0; i < headVos.length; i++) {
			BxgtIabillHVO hvo = new BxgtIabillHVO();
			hvo.setBauditedflag("N");
			hvo.setBdisableflag("N");
			hvo.setBestimateflag("N");
			hvo.setBoutestimate("N");
			hvo.setBwithdrawalflag("N");
			hvo.setCbilltypecode("I3");
			hvo.setClastoperatorid(headVos[i].getCoperatorid());
			hvo.setCoperatorid(headVos[i].getCoperatorid());
			hvo.setCrdcenterid(headVos[i].getPk_calbody());
			hvo.setCstockrdcenterid(headVos[i].getPk_calbody());
			hvo.setCsourcemodulename("IC");
			hvo.setCwarehouseid(headVos[i].getCwarehouseid());
			hvo.setDbilldate(headVos[i].getDbilldate());
			hvo.setDr(0);
			hvo.setFdispatchflag(0);
			hvo.setIdebtflag(-1);
			hvo.setPk_corp(headVos[i].getPk_corp());
			hvo.setTlastmaketime(headVos[i].getTmaketime());
			hvo.setTmaketime(headVos[i].getTmaketime());
			hvo.setTs(headVos[i].getTmaketime());
			hvo.setVbillcode(billcodes2[i]);

			String pkbill = dao2.insertVO(hvo);
			mapkey.put(headVos[i].getCgeneralhid(), pkbill);
			mapcode.put(headVos[i].getCgeneralhid(), headVos[i].getVbillcode());
			mapcode2.put(headVos[i].getCgeneralhid(), hvo.getVbillcode());
		}
		// 插入表体
		BxgtIabillBVO[] bvos = new BxgtIabillBVO[bodyVos.length];
		for (int i = 0; i < bodyVos.length; i++) {
			BxgtIabillBVO bvo = new BxgtIabillBVO();
			bvo.setBadjusteditemflag("N");
			bvo.setBauditbatchflag("N");
			bvo.setBlargessflag("N");
			bvo.setBretractflag("N");
			bvo.setBrtvouchflag("N");
			bvo.setBtransferincometax("N");
			bvo.setCastunitid(bodyVos[i].getCastunitid());
			bvo.setCbillid(mapkey.get(bodyVos[i].getCgeneralhid()));
			bvo.setCbilltypecode("I3");
			bvo.setCicbillcode(mapcode.get(bodyVos[i].getCgeneralhid()));
			bvo.setCicbillid(bodyVos[i].getCgeneralhid());
			bvo.setCicbilltype("46");
			bvo.setCicitemid(bodyVos[i].getCgeneralbid());
			bvo.setCinvbasid(bodyVos[i].getCinvbasid());
			bvo.setCinventoryid(bodyVos[i].getCinventoryid());
			bvo.setCrdcenterid(bodyVos[i].getPk_bodycalbody());
			bvo.setCsourcebillid(bodyVos[i].getCgeneralhid());
			bvo.setCsourcebillitemid(bodyVos[i].getCgeneralbid());
			bvo.setCsourcebilltypecode("46");
			bvo.setDbizdate(bodyVos[i].getDbizdate());
			bvo.setDr(0);
			bvo.setFcalcbizflag(0);
			bvo.setFdatagetmodelflag(5);
			bvo.setFoutadjustableflag("Y");
			bvo.setFolddatagetmodelflag(5);
			bvo.setFpricemodeflag(3);
			bvo.setIauditsequence(-1);
			bvo.setIrownumber(new Integer(bodyVos[i].getCrowno()) / 10);
			bvo.setNassistnum(bodyVos[i].getNinassistnum());
			bvo.setNchangerate(bodyVos[i].getHsl());
			bvo.setNnumber(bodyVos[i].getNinnum());
			bvo.setNsimulatemny(new UFDouble(0));
			bvo.setPk_corp(bodyVos[i].getPk_corp());
			bvo.setTs(bodyVos[i].getTs());
			bvo.setVbillcode(mapcode2.get(bodyVos[i].getCgeneralhid()));
			bvo.setVsourcebillcode(mapcode.get(bodyVos[i].getCgeneralhid()));
			bvo.setVsourcerowno(bodyVos[i].getCrowno());

			bvos[i] = bvo;
		}
		dao2.insertVOArray(bvos);

	}

	/**
	 * 产成品入库单新增时增加单据附表-货位
	 * 
	 * @param pk_saleOutOrders
	 */
	private void insertSpace(BxgtCcpBodyVo[] bodyVos) throws Exception {
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		BxgtSpaceVo[] voArray = null;
		if (bodyVos != null && bodyVos.length > 0) {
			voArray = new BxgtSpaceVo[bodyVos.length];
			for (int i = 0; i < bodyVos.length; i++) {
				BxgtSpaceVo vo = new BxgtSpaceVo();
				vo.setCgeneralbid(bodyVos[i].getCgeneralbid());
				vo.setDr(0);
				vo.setNingrossnum(bodyVos[i].getNingrossnum());
				vo.setNinspaceassistnum(bodyVos[i].getNinassistnum());
				vo.setNinspacenum(bodyVos[i].getNinnum());
				vo.setNoutgrossnum(bodyVos[i].getNoutgrossnum());
				vo.setNoutspaceassistnum(bodyVos[i].getNoutassistnum());
				vo.setNoutspacenum(bodyVos[i].getNoutnum());
				vo.setPk_corp(bodyVos[i].getPk_corp());
				vo.setTs(new UFDateTime(bodyVos[i].getTs()));
				voArray[i] = vo;
			}
		}
		dao.insertVOArray(voArray);
	}

	private String[] bxgtSynchroPurchaseInOrder(String[] pkPurchaseInOrders,
			String[] purChaseOrders) throws Exception {
		// 采购入库单同步2015.5.19 xm
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// B帐采购入库单表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐采购入库单表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 拼接采购入库单表头VO查询SQL
		String getHeadVoSQL = "select * from ic_general_h where cgeneralhid in ('";
		// 拼接采购入库单子表VO查询SQL
		String getBodyVoSQL = "select * from ic_general_b where cgeneralhid in ('";
		for (int i = 0; i < pkPurchaseInOrders.length; i++) {
			getHeadVoSQL += pkPurchaseInOrders[i] + "','";
			getBodyVoSQL += pkPurchaseInOrders[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";

		// 查询采购入库单表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		//2016-09-22
		for(Map<String, Object> hlist:headVoList){
			hlist.put("cauditorid", null);
		}
		// 查询采购入库单表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);
		for(Map<String, Object> blist:bodyVoList){
			blist.put("isok", "N");
			blist.put("bzgflag", "N");
		}
		// 拼接B帐表头VO新增SQL数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PURCHASE_IN);
		// 拼接B帐表体VO新增SQL数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PURCHASE_IN);

		// ------------------------------------------------------------------------------
		// B账根据采购订单找到采购入库单
		String[] pkPurchaseInsB = getPurchaseInPksB(purChaseOrders);
		if (pkPurchaseInsB != null && pkPurchaseInsB.length > 0) {
			// 插入数据前先删除
			this.deletePurchaseBill(pkPurchaseInsB, IBxgtBillType.PURCHASE_IN);
		}

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		// 批量插入采购入库单表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入采购入库单表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);

		// 插入对应的销售出库单附表-货位
		bxgtSynchroSpace(pkPurchaseInOrders);
		// 插入对应的采购入库单附表-累计结算
		bxgtSynchroAccount(pkPurchaseInOrders);

		// 获得采购入库对应的采购发票pks
		// String[] pkPurchaseInvoices =
		// getPurchaseInvoicePks(pkPurchaseInOrders);
		// 同步采购入库对应的采购发票
		// if (pkPurchaseInvoices != null && pkPurchaseInvoices.length > 0) {
		// bxgtSynchroPurchaseInvoiceOrder(pkPurchaseInvoices, purChaseOrders);
		// }

		// 插入ia_bill
		this.sysIaBillByCgeneralhid(pkPurchaseInOrders);

		return null;

	}

	private void bxgtSynchroPurchaseInvoiceOrder(
			String[] pkPurchaseInvoiceOrders, String[] purChaseOrders)
			throws Exception {
		// 采购发票同步2015.5.19 xm
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// B帐采购发票表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐采购发票表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 拼接销售发票表头VO查询SQL
		String getHeadVoSQL = "select * from po_invoice where cinvoiceid in ('";
		// 拼接销售发票子表VO查询SQL
		String getBodyVoSQL = "select * from po_invoice_b where cinvoiceid in ('";
		for (int i = 0; i < pkPurchaseInvoiceOrders.length; i++) {
			getHeadVoSQL += pkPurchaseInvoiceOrders[i] + "','";
			getBodyVoSQL += pkPurchaseInvoiceOrders[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";
		// 查询销售发票表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		// 查询销售发票表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);

		// 拼接B帐表头VO新增SQL数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PURCHASE_INVOICE);
		// 拼接B帐表体VO新增SQL数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PURCHASE_INVOICE);
		// ------------------------------------------------------------------------------

		// 根据B账采购订单获得B账采购发票
		String[] pkPurchaseInvoiceOrdersB = getPurInvoicePksByPurOrderPksB(purChaseOrders);
		if (pkPurchaseInvoiceOrdersB != null
				&& pkPurchaseInvoiceOrdersB.length > 0) {
			// 插入数据前先删除
			this.deletePurchaseBill(pkPurchaseInvoiceOrdersB,
					IBxgtBillType.PURCHASE_INVOICE);
		}

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);

		// 批量插入销售发票表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入销售发票表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);
	}

	/**
	 * 2015-4-8 bwy 拼接SQL时判断列的值是否为空
	 * 
	 * @param columnValue
	 * @return
	 */
	private Object checkNullColumn(Object columnValue) {
		if (columnValue == null) {
			return null;
		} else {
			columnValue = "'" + columnValue + "'";
			return columnValue;
		}
	}

	/**
	 * 2015-4-9 bwy 拼接B帐单据表头VO新增SQL数组
	 * 
	 * @param insertHeadVOSQLs
	 * @param headVoList
	 * @param billTypeFlag
	 * @return
	 */
	private String[] makeHeadVoInsertSQLs(String[] insertHeadVOSQLs,
			ArrayList<Map<String, Object>> headVoList, Integer billTypeFlag) {
		String insertHeadVOSql = "";
		if (headVoList != null && headVoList.size() > 0 && billTypeFlag != null) {
			// 主表所有的列名
			Object[] headColumnStr = null;
			insertHeadVOSQLs = new String[headVoList.size()];

			for (int i = 0; i < headVoList.size(); i++) {
				// 拼接B帐单据表头VO新增SQL===============START====================
				Map<String, Object> voMap = headVoList.get(i);
				if (1 == billTypeFlag)
					insertHeadVOSql = "insert into ic_general_h (";
				if (4 == billTypeFlag)
					insertHeadVOSql = "insert into so_saleinvoice (";
				if (2 == billTypeFlag)
					insertHeadVOSql = "insert into so_sale (";
				if (3 == billTypeFlag)
					insertHeadVOSql = "insert into so_salereceive (";
				if (6 == billTypeFlag)
					insertHeadVOSql = "insert into arap_djzb (";
				if (7 == billTypeFlag)
					insertHeadVOSql = "insert into cmp_settlement (";
				if (5 == billTypeFlag) {
					insertHeadVOSql = "insert into ic_general_h (";
					voMap.put("cbilltypecode", "46");
					String dbilldate = BFPubTool
							.getString_TrimZeroLenAsNull(voMap.get("dbilldate"));
					String tmaketime = BFPubTool
							.getString_TrimZeroLenAsNull(voMap.get("tmaketime"));
					String tlastmoditime = BFPubTool
							.getString_TrimZeroLenAsNull(voMap
									.get("tlastmoditime"));
					String cgeneralhid = BFPubTool
							.getString_TrimZeroLenAsNull(voMap
									.get("cgeneralhid"));
					voMap.put("dbilldate", aheadDate(dbilldate));// 单据日期
					voMap.put("tmaketime", aheadDate(tmaketime));// 制单时间
					voMap.put("tlastmoditime", aheadDate(tlastmoditime));// 最后修改时间
					voMap.put("cgeneralhid", makePk(cgeneralhid));
				}
				headColumnStr = voMap.keySet().toArray();
				// 循环拼接insert into tablename (列名，列名，。。。。)
				for (int j = 0; j < headColumnStr.length; j++) {
					insertHeadVOSql += headColumnStr[j] + ",";
					if (j == headColumnStr.length - 1) {
						insertHeadVOSql = insertHeadVOSql.substring(0,
								insertHeadVOSql.lastIndexOf(","))
								+ ") values (";
					}
				}
				// 循环拼接values(值，值，值。。。。。。)
				for (int j = 0; j < headColumnStr.length; j++) {
					// Map<String, Object> headVoMap = headVoList.get(i);
					insertHeadVOSql += checkNullColumn(voMap
							.get(headColumnStr[j]))
							+ ",";
				}
				insertHeadVOSql = insertHeadVOSql.substring(0, insertHeadVOSql
						.lastIndexOf(","))
						+ ")";
				// 拼接B帐单据表头VO新增SQL================END===================

				// 放入SQL数组
				insertHeadVOSQLs[i] = insertHeadVOSql;
			}
		}
		return insertHeadVOSQLs;
	}

	/**
	 * 2015-4-9 bwy 拼接B帐单据表体VO新增SQL数组
	 * 
	 * @param insertBodyVOSQLs
	 * @param bodyVoList
	 * @param billTypeFlag
	 * @return
	 */
	private String[] makeBodyVoInsertSQLs(String[] insertBodyVOSQLs,
			ArrayList<Map<String, Object>> bodyVoList, Integer billTypeFlag) {
		String insertBodyVOSql = "";
		if (bodyVoList != null && bodyVoList.size() > 0) {
			// 子表所有列名
			Object[] bodyColumnStr = null;
			insertBodyVOSQLs = new String[bodyVoList.size()];
			for (int i = 0; i < bodyVoList.size(); i++) {
				// 拼接B帐单据表体VO新增SQL===============START====================
				Map<String, Object> voMap = bodyVoList.get(i);
				if (1 == billTypeFlag)
					insertBodyVOSql = "insert into ic_general_b (";
				if (4 == billTypeFlag)
					insertBodyVOSql = "insert into so_saleinvoice_b (";
				if (2 == billTypeFlag)
					insertBodyVOSql = "insert into so_saleorder_b (";
				if (3 == billTypeFlag)
					insertBodyVOSql = "insert into so_salereceive_b (";
				if (6 == billTypeFlag)
					insertBodyVOSql = "insert into arap_djfb (";
				if (7 == billTypeFlag)
					insertBodyVOSql = "insert into cmp_detail (";
				if (5 == billTypeFlag) {
					String cgeneralhid = BFPubTool
							.getString_TrimZeroLenAsNull(voMap
									.get("cgeneralhid"));
					String cgeneralbid = BFPubTool
							.getString_TrimZeroLenAsNull(voMap
									.get("cgeneralbid"));
					insertBodyVOSql = "insert into ic_general_b (";
					voMap.put("cbodybilltypecode", "46");
					voMap.put("cgeneralhid", makePk(cgeneralhid));
					voMap.put("cgeneralbid", makePk(cgeneralbid));
				}
				bodyColumnStr = voMap.keySet().toArray();

				// 循环拼接insert into tablename (列名，列名，。。。。)
				for (int j = 0; j < bodyColumnStr.length; j++) {
					insertBodyVOSql += bodyColumnStr[j] + ",";
					if (j == bodyColumnStr.length - 1) {
						insertBodyVOSql = insertBodyVOSql.substring(0,
								insertBodyVOSql.lastIndexOf(","))
								+ ") values (";
					}
				}

				// 循环拼接values(值，值，值。。。。。。)
				for (int j = 0; j < bodyColumnStr.length; j++) {
					// Map<String, Object> bodyVoMap = bodyVoList.get(i);
					insertBodyVOSql += checkNullColumn(voMap
							.get(bodyColumnStr[j]))
							+ ",";
				}
				insertBodyVOSql = insertBodyVOSql.substring(0, insertBodyVOSql
						.lastIndexOf(","))
						+ ")";
				// 拼接B帐B帐单据表体VO新增SQL===============END====================

				// 放入SQL数组
				insertBodyVOSQLs[i] = insertBodyVOSql;
			}
		}
		return insertBodyVOSQLs;
	}

	/**
	 * 批改的销售订单
	 */
	public void bxgtBatchUpdateSaleOrder(String[] csaleids, int days)
			throws Exception {
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 批改销售订单SQL -- 暂改日期
		String updateSaleOrderSql = "update so_sale set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dbilltime = to_char(to_date(dbilltime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),dmakedate = to_char(to_date(dmakedate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dmoditime = to_char(to_date(dmoditime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),dapprovedate = to_char(to_date(dapprovedate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'), daudittime = to_char(to_date(daudittime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where csaleid in ";

		// 批改销售订单子表
		String updateSaleOrderBody = "update so_saleorder_b set dconsigndate = to_char(to_date(dconsigndate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ddeliverdate = to_char(to_date(ddeliverdate,'yyyy-MM-dd')+ "
				+ days
				+ ",'yyyy-MM-dd'),ts=to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+ "
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where csaleid in ";

		// 批改销售执行表
		String updateSaleExecuSql = "update so_saleexecute set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where creceipttype='30' and csaleid in ";

		// 批改所有出库单
		String updateSaleOutSql = "update ic_general_h set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),daccountdate = to_char(to_date(daccountdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taccounttime = to_char(to_date(taccounttime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmoditime = to_char(to_date(tlastmoditime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cgeneralhid in ";

		// 批改所有出库单子表
		String updateSaleOutBody = "update ic_general_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ddeliverdate = to_char(to_date(ddeliverdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cgeneralhid in ";

		// 批改出库关联表1
		String sqlbodys = "select distinct cgeneralbid from ic_general_b where nvl(dr,0)=0 and cgeneralhid in ";

		String updateSaleOutBody1 = "update ic_general_bb1 set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cgeneralbid in ";

		// 批改出库关联表2
		String updateSaleOutBody2 = "update ic_general_bb3 set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cgeneralhid in ";

		// 批改收款单主表
		String updateSkSql = "update arap_djzb set djrq = to_char(to_date(djrq,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'), effectdate = to_char(to_date(effectdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),shrq = to_char(to_date(shrq,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),sxrq = to_char(to_date(sxrq,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),yhqrrq = to_char(to_date(yhqrrq,'yyyy-MM-dd')+"
				+ days + ",'yyyy-MM-dd') where vouchid in ";

		// 批改收款单子表
		String updateSkBody = "update arap_djfb set billdate = to_char(to_date(billdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'), qxrq = to_char(to_date(qxrq,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where vouchid in ";

		// 批改结算信息主表
		String updateSettlementSql = "update cmp_settlement set busi_auditdate = to_char(to_date(busi_auditdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),busi_billdate = to_char(to_date(busi_billdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),lastupdatedate = to_char(to_date(lastupdatedate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),signdate = to_char(to_date(signdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where pk_busibill in ";

		// 批改结算信息表体
		String updateDetailSql = "update cmp_detail set billdate = to_char(to_date(billdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),signdate = to_char(to_date(signdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where pk_bill in ";

		// 获得销售出库单pk,产成品入库单
		String[] cgeneralhids = this.getCgeneralhidByCsaleidB(csaleids);

		// 批改ia_bill
		String sqliabill = "select distinct cbillid from ia_bill_b where nvl(dr,0)=0 and cicbillid in ";
		String[] iabills = this.queryPks1ByPks2BZT(cgeneralhids, sqliabill);

		String sqlia_h = "update ia_bill set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),tlastmaketime = to_char(to_date(tlastmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cbillid in ";

		String sqlia_b = "update ia_bill_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cicbillid in ";
		this.deleteOrUpdateBillByPksBZT(iabills, sqlia_h);
		this.deleteOrUpdateBillByPksBZT(cgeneralhids, sqlia_b);

		// 获得销售收款的Pk
		String[] vouchids = this.getSKpksB(csaleids, BxgtStepButton.DESIGN_B);

		this.deleteOrUpdateBillByPksBZT(csaleids, updateSaleOrderSql);
		this.deleteOrUpdateBillByPksBZT(csaleids, updateSaleOrderBody);
		this.deleteOrUpdateBillByPksBZT(csaleids, updateSaleExecuSql);

		/*
		 * for (int i = 0; i < csaleids.length; i++) { updateSaleOrderSql +=
		 * csaleids[i] + "','"; updateSaleOrderBody += csaleids[i] + "','";
		 * updateSaleExecuSql += csaleids[i] + "','"; } updateSaleOrderSql =
		 * updateSaleOrderSql.substring(0, updateSaleOrderSql .lastIndexOf(",")) +
		 * ")"; updateSaleOrderBody = updateSaleOrderBody.substring(0,
		 * updateSaleOrderBody.lastIndexOf(",")) + ")"; updateSaleExecuSql =
		 * updateSaleExecuSql.substring(0, updateSaleExecuSql .lastIndexOf(",")) +
		 * ")"; dao.executeUpdate(updateSaleOrderSql);
		 * dao.executeUpdate(updateSaleOrderBody);
		 * dao.executeUpdate(updateSaleExecuSql);
		 */

		if (cgeneralhids != null && cgeneralhids.length > 0) {
			this.deleteOrUpdateBillByPksBZT(cgeneralhids, updateSaleOutSql);
			this.deleteOrUpdateBillByPksBZT(cgeneralhids, updateSaleOutBody);
			String[] pksqlbodys = this.queryPks1ByPks2BZT(cgeneralhids,
					sqlbodys);
			this.deleteOrUpdateBillByPksBZT(pksqlbodys, updateSaleOutBody1);
			this.deleteOrUpdateBillByPksBZT(cgeneralhids, updateSaleOutBody2);
			/*
			 * for (int i = 0; i < cgeneralhids.length; i++) { updateSaleOutSql +=
			 * cgeneralhids[i] + "','"; updateSaleOutBody += cgeneralhids[i] +
			 * "','"; updateSaleOutBody1 += cgeneralhids[i] + "','";
			 * updateSaleOutBody2 += cgeneralhids[i] + "','"; } updateSaleOutSql =
			 * updateSaleOutSql.substring(0, updateSaleOutSql .lastIndexOf(",")) +
			 * ")"; updateSaleOutBody = updateSaleOutBody.substring(0,
			 * updateSaleOutBody.lastIndexOf(",")) + ")"; updateSaleOutBody1 =
			 * updateSaleOutBody1.substring(0,
			 * updateSaleOutBody1.lastIndexOf(",")) + "))"; updateSaleOutBody2 =
			 * updateSaleOutBody2.substring(0,
			 * updateSaleOutBody2.lastIndexOf(",")) + ")";
			 * dao.executeUpdate(updateSaleOutSql);
			 * dao.executeUpdate(updateSaleOutBody);
			 * dao.executeUpdate(updateSaleOutBody1);
			 * dao.executeUpdate(updateSaleOutBody2);
			 */
		}

		if (vouchids != null && vouchids.length > 0) {
			this.deleteOrUpdateBillByPksBZT(vouchids, updateSkSql);
			this.deleteOrUpdateBillByPksBZT(vouchids, updateSkBody);
			this.deleteOrUpdateBillByPksBZT(vouchids, updateSettlementSql);
			this.deleteOrUpdateBillByPksBZT(vouchids, updateDetailSql);

			/*
			 * for (int i = 0; i < vouchids.length; i++) { updateSkSql +=
			 * vouchids[i] + "','"; updateSkBody += vouchids[i] + "','";
			 * updateSettlementSql += vouchids[i] + "','"; updateDetailSql +=
			 * vouchids[i] + "','"; } updateSkSql = updateSkSql .substring(0,
			 * updateSkSql.lastIndexOf(",")) + ")"; updateSkBody =
			 * updateSkBody.substring(0, updateSkBody .lastIndexOf(",")) + ")";
			 * updateSettlementSql = updateSettlementSql.substring(0,
			 * updateSettlementSql.lastIndexOf(",")) + ")"; updateDetailSql =
			 * updateDetailSql.substring(0, updateDetailSql .lastIndexOf(",")) +
			 * ")";
			 * 
			 * dao.executeUpdate(updateSkSql); dao.executeUpdate(updateSkBody);
			 * dao.executeUpdate(updateSettlementSql);
			 * dao.executeUpdate(updateDetailSql);
			 */
		}

		// 批改完插入记录表bxgt_isedit,便于打印
		this.printAfterBatch(csaleids, vouchids, cgeneralhids, null,
				BxgtStepButton.SO_FLOW);

	}

	/**
	 * 2015-4-29 bwy 日期提前
	 * 
	 * @param date
	 * @return
	 */
	private String aheadDate(String dateStr) {
		SimpleDateFormat sdf = null;

		if (dateStr.length() == 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		if (dateStr.length() == 19) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}

		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date.setTime(date.getTime() + 30 * 24 * 3600000);
		String newDateStr = sdf.format(date);
		return newDateStr;
	}

	/**
	 * 拼接销售出库单附表-累计结算 新增voSQL数组
	 * 
	 * @param accountVoList
	 * @return
	 */
	/*
	 * private String[] makeInsertAccountVoSQLs( ArrayList<Map<String,
	 * Object>> accountVoList) {
	 * 
	 * String[] insertAccountVoSQLs = null; if (accountVoList != null &&
	 * accountVoList.size() > 0) { Object[] columnName = null;
	 * insertAccountVoSQLs = new String[accountVoList.size()]; for (int i = 0; i <
	 * accountVoList.size(); i++) { String insertAccountVoSql = "insert into
	 * ic_general_bb3 ("; Map<String, Object> accountVoMap =
	 * accountVoList.get(i); columnName = accountVoMap.keySet().toArray(); //
	 * 拼接(列名。。。。。) for (int j = 0; j < columnName.length; j++) {
	 * insertAccountVoSql += columnName[j] + ","; if (j == columnName.length -
	 * 1) { insertAccountVoSql = insertAccountVoSql.substring(0,
	 * insertAccountVoSql.lastIndexOf(",")) + ") values ("; } } //
	 * 循环拼接values(值，值，值。。。。。。) for (int j = 0; j < columnName.length; j++) { //
	 * Map<String, Object> bodyVoMap = bodyVoList.get(i); insertAccountVoSql +=
	 * checkNullColumn(accountVoMap .get(columnName[j])) + ","; }
	 * insertAccountVoSql = insertAccountVoSql.substring(0,
	 * insertAccountVoSql.lastIndexOf(",")) + ")"; insertAccountVoSQLs[i] =
	 * insertAccountVoSql; } } return insertAccountVoSQLs; }
	 */
	/**
	 * 拼接销售出库单附表-货位 新增voSQL数组
	 * 
	 * @param spaceVoList
	 * @return
	 */
	/*
	 * private String[] makeInsertSpaceVoSQLs( ArrayList<Map<String, Object>>
	 * spaceVoList) { String[] insertSpaceVoSQLs = null; if (spaceVoList != null &&
	 * spaceVoList.size() > 0) { Object[] columnName = null; insertSpaceVoSQLs =
	 * new String[spaceVoList.size()]; for (int i = 0; i < spaceVoList.size();
	 * i++) { String insertSpaceVoSql = "insert into ic_general_bb1 ("; Map<String,
	 * Object> spaceVoMap = spaceVoList.get(i); columnName =
	 * spaceVoMap.keySet().toArray(); // 拼接(列名。。。。。) for (int j = 0; j <
	 * columnName.length; j++) { insertSpaceVoSql += columnName[j] + ","; if (j ==
	 * columnName.length - 1) { insertSpaceVoSql = insertSpaceVoSql.substring(0,
	 * insertSpaceVoSql.lastIndexOf(",")) + ") values ("; } } //
	 * 循环拼接values(值，值，值。。。。。。) for (int j = 0; j < columnName.length; j++) { //
	 * Map<String, Object> bodyVoMap = bodyVoList.get(i); insertSpaceVoSql +=
	 * checkNullColumn(spaceVoMap .get(columnName[j])) + ","; } insertSpaceVoSql =
	 * insertSpaceVoSql.substring(0, insertSpaceVoSql.lastIndexOf(",")) + ")";
	 * insertSpaceVoSQLs[i] = insertSpaceVoSql; } } return insertSpaceVoSQLs; }
	 */
	/**
	 * 构造产成品入库单PK
	 * 
	 * @param pk
	 * @return
	 */
	private String makePk(String pk) {
		String newPk = "";
		newPk = pk.substring(0, 12) + "CCP" + pk.substring(15);
		return newPk;
	}

	/**
	 * 同步收款单
	 * 
	 * @param vouchids
	 * @throws Exception
	 */
	private void bxgtSynchroSK(String[] vouchids) throws Exception {

		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// 根据单据主表PK 查询所有的主表VO
		String queryHeadVosSql = "select * from arap_djzb where nvl(dr,0) = 0 and djlxbm='D2' and not exists(select 1 from bxgt_islock lk where lk.vouchid = arap_djzb.vouchid) and vouchid in ";
		ArrayList<DjskBXHVO> headVoList = (ArrayList<DjskBXHVO>) this
				.queryBeanPks1ByPks2(vouchids, queryHeadVosSql, DjskBXHVO.class);

		// 根据单据主表PK 查询所有的子表VO
		String queryBodyVosSql = "select * from arap_djfb where nvl(dr,0) = 0 and djlxbm='D2' and not exists(select 1 from bxgt_islock lk where lk.vouchid = arap_djfb.vouchid) and vouchid in ";
		ArrayList<DjskBXBVO> bodyVoList = (ArrayList<DjskBXBVO>) this
				.queryBeanPks1ByPks2(vouchids, queryBodyVosSql, DjskBXBVO.class);

		// 插入前删除
		this.deleteBill(vouchids, IBxgtBillType.BXGT_SK);

		// 根据收款单主键获得结算单主表主键
		String[] settlePks = getSettlePks(vouchids);
		// 批量同步收款单
		BaseDAO dmo = new BaseDAO(BxgtStepButton.DESIGN_B);
		dmo.insertVOArrayWithPK(headVoList.toArray(new DjskBXHVO[0]));
		dmo.insertVOArrayWithPK(bodyVoList.toArray(new DjskBXBVO[0]));
		// 批量同步对应结算单
		bxgtSynchroSetlle(settlePks);
	}

	/**
	 * 根据收款单主表主键获得结算单主表主键
	 * 
	 * @param vouchids
	 * @return
	 * @throws Exception
	 */
	private String[] getSettlePks(String[] vouchids) throws Exception {
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// 根据收款单主键查询出所有结算单主表主键
		String querySettlePkSQL = "select distinct pk_settlement from cmp_detail where nvl(dr,0) = 0 and pk_bill in ";
		// 结算单子表主键数组
		String[] settlePks = null;
		// 结算单子表主键集合
		settlePks = this.queryPks1ByPks2(vouchids, querySettlePkSQL);

		return settlePks;
	}

	/*
	 * private void bxgtSynchroSaleExecute(String[] csaleids) throws Exception {
	 * String querySaleexecuteSql = "select * from so_saleexecute where
	 * nvl(dr,0) = 0 and csaleid in "; ArrayList<Map<String, Object>>
	 * saleexecuteVoList = null; saleexecuteVoList = (ArrayList<Map<String,
	 * Object>>) this .queryMapPks1ByPks2(csaleids, querySaleexecuteSql); //
	 * 拼接插入B账销售执行表的SQL数组 String[] insertSaleexecuteSQLs =
	 * makeInsertSaleexecuteSQLs(saleexecuteVoList); // 批量执行SQL类 CommonDataDMO
	 * dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);
	 * dmo.execDatas(insertSaleexecuteSQLs); }
	 */

	/**
	 * 拼接插入B账销售执行表的SQL数组
	 * 
	 * @param saleexecuteVoList
	 * @return
	 */
	private String[] makeInsertSaleexecuteSQLs(
			ArrayList<Map<String, Object>> saleexecuteVoList) {
		String[] insertSaleexecuteSQLs = null;
		if (saleexecuteVoList != null && saleexecuteVoList.size() > 0) {
			Object[] columnName = null;
			insertSaleexecuteSQLs = new String[saleexecuteVoList.size()];
			for (int i = 0; i < saleexecuteVoList.size(); i++) {
				String insertSaleexecuteSql = "insert into so_saleexecute (";
				Map<String, Object> saleexecuteMap = saleexecuteVoList.get(i);
				columnName = saleexecuteMap.keySet().toArray();
				// 拼接(列名。。。。。)
				for (int j = 0; j < columnName.length; j++) {
					insertSaleexecuteSql += columnName[j] + ",";
					if (j == columnName.length - 1) {
						insertSaleexecuteSql = insertSaleexecuteSql.substring(
								0, insertSaleexecuteSql.lastIndexOf(","))
								+ ") values (";
					}
				}
				// 循环拼接values(值，值，值。。。。。。)
				for (int j = 0; j < columnName.length; j++) {
					// Map<String, Object> bodyVoMap = bodyVoList.get(i);
					insertSaleexecuteSql += checkNullColumn(saleexecuteMap
							.get(columnName[j]))
							+ ",";
				}
				insertSaleexecuteSql = insertSaleexecuteSql.substring(0,
						insertSaleexecuteSql.lastIndexOf(","))
						+ ")";
				insertSaleexecuteSQLs[i] = insertSaleexecuteSql;
			}
		}
		return insertSaleexecuteSQLs;
	}

	private void bxgtSynchroSetlle(String[] pk_settle) throws Exception {

		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// 根据单据主表PK 查询所有的主表VO
		String queryHeadVosSql = "select * from cmp_settlement where nvl(dr,0) = 0 and pk_settlement in ";

		ArrayList<DjjsBXHVO> headVoList = (ArrayList<DjjsBXHVO>) this
				.queryBeanPks1ByPks2(pk_settle, queryHeadVosSql,
						DjjsBXHVO.class);

		// 根据单据主表PK 查询所有的子表VO
		String queryBodyVosSql = "select * from cmp_detail where nvl(dr,0) = 0 and pk_settlement in ";
		ArrayList<DjjsBXBVO> bodyVoList = (ArrayList<DjjsBXBVO>) this
				.queryBeanPks1ByPks2(pk_settle, queryBodyVosSql,
						DjjsBXBVO.class);

		// 插入前删除
		this.deleteBill(pk_settle, IBxgtBillType.BXGT_JS);

		// 批量插入
		BaseDAO dmo = new BaseDAO(BxgtStepButton.DESIGN_B);
		dmo.insertVOArrayWithPK(headVoList.toArray(new DjjsBXHVO[0]));
		dmo.insertVOArrayWithPK(bodyVoList.toArray(new DjjsBXBVO[0]));
	}

	public String[] bxgtLockSaleOrders(String[] pkSaleOrders) throws Exception {

		String sql1 = "select distinct a.vreceiptcode from so_sale a where a.csaleid in ";
		String sql2 = " and not exists (select 1 from (select distinct cfirstbillhid from ic_general_b where cbodybilltypecode = '4C' and cfirstbillhid in ";
		String sql3 = " and nvl(dr, 0) = 0) c where c.cfirstbillhid = a.csaleid)";
		String[] billcodes = null;
		billcodes = this.queryBillcodeBySqlsBZT(pkSaleOrders, sql1, sql2, sql3);

		String[] csaleids = null;
		if (billcodes != null && billcodes.length > 0) {// 存在无入库的订单
			sql1 = sql1.replaceFirst("a.vreceiptcode", "a.csaleid");
			sql2 = sql2.replaceFirst("not exists", "exists");
			csaleids = this.queryBillcodeBySqlsBZT(pkSaleOrders, sql1, sql2,
					sql3);
		} else {
			csaleids = pkSaleOrders;
		}
		if (csaleids != null && csaleids.length > 0) {// 存在有下游的订单
			String[] pkSaleOuts = this.getCSaleOutByCsaleid(csaleids,
					BxgtStepButton.DESIGN_B);
			String[] vouchids = getSKpksB(csaleids, BxgtStepButton.DESIGN_B);

			lockBill(csaleids, pkSaleOuts, vouchids);
		}

		return billcodes;
	}

	/**
	 * 锁定单据
	 * 
	 * @param pk_saleorder
	 * @param pk_saleout
	 * @param vouchid
	 * @throws DAOException
	 */
	private void lockBill(String[] pk_saleorder, String[] pk_saleout,
			String[] vouchid) throws Exception {
		lockSaleOrder(pk_saleorder);
		lockSaleOut(pk_saleout);
		lockSK(vouchid);
	}

	/**
	 * 锁定销售订单
	 * 
	 * @param pk_saleOuts
	 * @param pk_saleOrders
	 * @param vouchids
	 * @throws DAOException
	 */
	private void lockSaleOrder(String[] pk_saleOrder) throws Exception {
		LockInfo[] bills = new LockInfo[pk_saleOrder.length];
		String[] sqls = new String[pk_saleOrder.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setCsaleid(pk_saleOrder[i]);
			bills[i] = bill;
			String sql = "insert into bxgt_islock(pk,csaleid)values(seq_info2.nextval,'"
					+ pk_saleOrder[i] + "')";
			sqls[i] = sql;
		}

		this.excuteUpdateJDBCs(sqls);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.insertVOArray(bills);
	}

	/**
	 * 锁定销售出库单
	 * 
	 * @param pk_saleOuts
	 * @param pk_saleOrders
	 * @param vouchids
	 * @throws DAOException
	 */
	private void lockSaleOut(String[] pk_saleOut) throws Exception {
		LockInfo[] bills = new LockInfo[pk_saleOut.length];
		String[] sqls = new String[pk_saleOut.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setCgeneralhid(pk_saleOut[i]);
			bills[i] = bill;
			String sql = "insert into bxgt_islock(pk,cgeneralhid)values(seq_info2.nextval,'"
					+ pk_saleOut[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.insertVOArray(bills);
	}

	/**
	 * 锁定收款单
	 * 
	 * @param pk_saleOuts
	 * @param pk_saleOrders
	 * @param vouchids
	 * @throws DAOException
	 */
	private void lockSK(String[] pk_sK) throws Exception {
		if (pk_sK == null || pk_sK.length <= 0) {
			return;
		}
		LockInfo[] bills = new LockInfo[pk_sK.length];
		String[] sqls = new String[pk_sK.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setVouchid(pk_sK[i]);
			bills[i] = bill;
			String sql = "insert into bxgt_islock(pk,vouchid)values(seq_info2.nextval,'"
					+ pk_sK[i] + "')";
			sqls[i] = sql;
		}

		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		this.excuteUpdateJDBCs(sqls);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.insertVOArray(bills);
	}

	public void deleteBill(String[] billPks, int billTypeFlag) throws Exception {
		String delHeadVoSql = "delete from ";
		String delBodyVoSql = "delete from ";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 货位
		String delHwSql = "delete from ic_general_bb1 where cgeneralbid in ";
		// 累计结算
		String delLjjsSql = "delete from ic_general_bb3 where cgeneralhid in ";
		// 销售执行表
		String delSaleExecuSql = "delete from so_saleexecute where csaleid in ";
		if (1 == billTypeFlag) {
			// 销售出库单
			delHeadVoSql += "ic_general_h where cgeneralhid in ";
			delBodyVoSql += "ic_general_b where cgeneralhid in ";

			// 获得销售出库单表体PK
			String[] saleOutBodyPk = getSaleOutBodyPks(billPks);

			// 删除货位信息
			this.deleteOrUpdateBillByPksBZT(saleOutBodyPk, delHwSql);
			// 删除累计结算信息
			this.deleteOrUpdateBillByPksBZT(billPks, delLjjsSql);
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);
		} else if (2 == billTypeFlag) {
			// 销售订单
			delHeadVoSql += "so_sale where csaleid in ";
			delBodyVoSql += "so_saleorder_b where csaleid in ";

			// 删除销售执行表
			this.deleteOrUpdateBillByPksBZT(billPks, delSaleExecuSql);
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);
		} else if (5 == billTypeFlag) {
			// 产成品入库单
			delHeadVoSql += "ic_general_h where cgeneralhid in ";
			delBodyVoSql += "ic_general_b where cgeneralhid in ";
			// 获得产成品入库单表体PK
			String[] producInBodyPk = getProduceInPks(billPks);
			// 删除货位信息
			this.deleteOrUpdateBillByPksBZT(producInBodyPk, delHwSql);
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);

			// 获得ia_bill表主键
			String sqlia = "select distinct cbillid from ia_bill_b where cicbillid in ";
			// 销售出库单表体pk数组
			String[] iaPks = null;
			iaPks = this.queryPks1ByPks2BZT(billPks, sqlia);
			// 删除ia_bill_b
			String del_ia_b = "delete from ia_bill_b where cicbillid in ";
			this.deleteOrUpdateBillByPksBZT(billPks, del_ia_b);
			// 删除ia_bill
			String del_ia = "delete from ia_bill where cbillid in ";
			this.deleteOrUpdateBillByPksBZT(iaPks, del_ia);
			/*
			 * delHeadVoSql = delHeadVoSql.substring(0, delHeadVoSql
			 * .lastIndexOf(",")) + ") and cbilltypecode ='46'"; delBodyVoSql =
			 * delBodyVoSql.substring(0, delBodyVoSql .lastIndexOf(",")) + ")
			 * and cbodybilltypecode ='46'"; delHwSql = delHwSql.substring(0,
			 * delHwSql.lastIndexOf(",")) + ")";
			 */

		} else if (6 == billTypeFlag) {
			// 收款单
			delHeadVoSql += "arap_djzb where djlxbm ='D2' and vouchid in ";
			delBodyVoSql += "arap_djfb where djlxbm ='D2' and vouchid in ";
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);
		} else if (7 == billTypeFlag) {
			// 结算单
			delHeadVoSql += "cmp_settlement where pk_settlement in ";
			delBodyVoSql += "cmp_detail where pk_settlement in ";
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);
		} else if (IBxgtBillType.SALE_OUT_ALL == billTypeFlag) {
			// 库存单
			delHeadVoSql += "ic_general_h where cgeneralhid in ";
			delBodyVoSql += "ic_general_b where cgeneralhid in ";

			// 获得销售出库单表体PK
			String[] saleOutBodyPk = getSaleOutBodyPks(billPks);

			// 删除货位信息
			this.deleteOrUpdateBillByPksBZT(saleOutBodyPk, delHwSql);
			// 删除累计结算信息
			this.deleteOrUpdateBillByPksBZT(billPks, delLjjsSql);
			// 删除子表
			this.deleteOrUpdateBillByPksBZT(billPks, delBodyVoSql);
			// 删除主表
			this.deleteOrUpdateBillByPksBZT(billPks, delHeadVoSql);
		}
	}

	/**
	 * 根据库存主表获取子表主键
	 * 
	 * @param pk_sale
	 * @return
	 * @throws DAOException
	 */
	private String[] getSaleOutBodyPks(String[] pk_saleout) throws Exception {
		String sql = "select distinct cgeneralbid from ic_general_b where nvl(dr,0)=0 and cgeneralhid in ";
		/*
		 * for (int i = 0; i < pk_saleout.length; i++) { sql += pk_saleout[i] +
		 * "','"; } sql = sql.substring(0, sql.lastIndexOf(",")) + ") and
		 * nvl(dr,0)=0 "; BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		 * ArrayList<Map<String, Object>> bodyPkList = (ArrayList<Map<String,
		 * Object>>) dao .executeQuery(sql, new MapListProcessor());
		 */
		// 销售出库单表体pk数组
		String[] bodyPks = null;
		bodyPks = this.queryPks1ByPks2BZT(pk_saleout, sql);
		/*
		 * if (bodyPkList != null && bodyPkList.size() > 0) { bodyPks = new
		 * String[bodyPkList.size()]; for (int i = 0; i < bodyPkList.size();
		 * i++) { String bodyPk = BFPubTool
		 * .getString_TrimZeroLenAsNull(bodyPkList.get(i).get( "cgeneralbid"));
		 * bodyPks[i] = bodyPk; } }
		 */
		return bodyPks;
	}

	/**
	 * 根据产成品入库单主表PK 获得产成品入库单表体pk
	 * 
	 * @param pk_sale
	 * @return
	 * @throws DAOException
	 */
	private String[] getProduceInPks(String[] pk_producIn) throws Exception {
		String sql = "select cgeneralbid from ic_general_b where cbodybilltypecode='46' and cgeneralhid in ";
		// 销售出库单表体pk数组
		String[] bodyPks = null;
		bodyPks = this.queryPks1ByPks2BZT(pk_producIn, sql);
		return bodyPks;
	}

	public String bxgtUnLockSaleOrders(String[] pkSaleOrders) throws Exception {
		String str = "";
		// 根据销售订单主键获得对应销售出库单主键
		String[] pkSaleOuts = this.getCSaleOutByCsaleid(pkSaleOrders,
				BxgtStepButton.DESIGN_B);
		// 根据销售订单主键获得对应收款单主键
		String[] vouchids = getSKpksB(pkSaleOrders, BxgtStepButton.DESIGN_B);
		if (pkSaleOuts != null && pkSaleOuts.length > 0) {
			unLockBill(pkSaleOrders, pkSaleOuts, vouchids);
		} else {
			str = "***";
		}
		return str;
	}

	private void unLockBill(String[] pkSaleOrders, String[] pkSaleOuts,
			String[] vouchids) throws Exception {
		unLockSaleOrder(pkSaleOrders);
		unLockSaleOut(pkSaleOuts);
		unLockSK(vouchids);
	}

	private void unLockSK(String[] vouchids) throws Exception {
		if (vouchids == null || vouchids.length <= 0) {
			return;
		}
		String sql = "delete from bxgt_islock where vouchid in('";
		for (int i = 0; i < vouchids.length; i++) {
			sql += vouchids[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);
	}

	private void unLockSaleOut(String[] pkSaleOuts) throws Exception {
		String sql = "delete from bxgt_islock where cgeneralhid in('";
		for (int i = 0; i < pkSaleOuts.length; i++) {
			sql += pkSaleOuts[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);
	}

	private void unLockSaleOrder(String[] pkSaleOrders) throws Exception {
		String sql = "delete from bxgt_islock where csaleid in('";
		for (int i = 0; i < pkSaleOrders.length; i++) {
			sql += pkSaleOrders[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);
	}

	/**
	 * 获得所需pk值
	 * 
	 * @author xm
	 * @date 2015.5.8
	 */
	public void getNeedPks() throws Exception {
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// 获得需要的pk值的集合（D2:收款单，有结算单标签）
		String sqlbegin = "select z.* from arap_djzb z where z.djlxbm = 'D0'and "
				+ " exists(select 1 from arap_djfb f where exists (select 1 from bd_cubasdoc c where exists "
				+ " (select 1 from bd_cumandoc cc where def30='Y' and cc.pk_cubasdoc=c.pk_cubasdoc ) "
				+ " and c.pk_cubasdoc = f.hbbm) and z.vouchid = f.vouchid) and nvl(dr, 0) = 0 and "
				+ "ts > (select max(lasttime) from bxgt_time) ";

		List<DjskBXHVO> list = (List<DjskBXHVO>) this.getResultBeanList(
				sqlbegin, DjskBXHVO.class);

		String sql1 = sqlbegin.replaceAll("z.\\*", "vouchid");
		String sql2 = "select * from arap_djfb where nvl(dr,0)=0 and vouchid in("
				+ sql1 + ")";

		List<DjskBXBVO> listb = (List<DjskBXBVO>) this.getResultBeanList(sql2,
				DjskBXBVO.class);

		if (list == null || list.size() <= 0) {
			throw new BusinessException("没有要同步的数据！");
		}

		DjskBXHVO[] vos = list.toArray(new DjskBXHVO[0]);
		DjskBXBVO[] vobs = listb.toArray(new DjskBXBVO[0]);

		// 取得所有数据的主键
		List<String> pks = new ArrayList<String>();
		for (int i = 0; i < vos.length; i++) {
			pks.add(vos[i].getVouchid());
		}
		// 删除预收款单
		this.deleteAdvanceReceive(pks.toArray(new String[0]));

		BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao1.insertVOArrayWithPK(vos);
		dao1.insertVOArrayWithPK(vobs);

		String sqltime = sqlbegin.replaceAll("z.\\*", "max(ts)");
		Object obj = this.getResultObject(sqltime);
		String sqlinsert = "insert into bxgt_time(lasttime) values('"
				+ obj.toString() + "')";
		this.excuteUpdateJDBC(sqlinsert);
		// 获得预收款单对应的结算单
		String sql3 = "select * from cmp_settlement where nvl(dr,0)=0 and pk_busibill in("
				+ sql1 + ")";
		List<DjjsBXHVO> listc = (List<DjjsBXHVO>) this.getResultBeanList(sql3,
				DjjsBXHVO.class);

		if (listc == null || listc.size() <= 0) {// 没有结算单就不同步
			return;
		}
		String sql4 = "select * from cmp_detail where nvl(dr,0)=0 and pk_bill in("
				+ sql1 + ")";
		List<DjjsBXBVO> listd = (List<DjjsBXBVO>) this.getResultBeanList(sql4,
				DjjsBXBVO.class);

		DjjsBXHVO[] dhvos = listc.toArray(new DjjsBXHVO[0]);
		DjjsBXBVO[] dbvos = listd.toArray(new DjjsBXBVO[0]);
		// 取得所有数据的主键
		List<String> pksets = new ArrayList<String>();
		for (int i = 0; i < dhvos.length; i++) {
			pksets.add(dhvos[i].getPk_settlement());
		}
		// 删除结算单
		this.deleteSettleOrder(pksets.toArray(new String[0]));

		dao1.insertVOArrayWithPK(dhvos);
		dao1.insertVOArrayWithPK(dbvos);
	}

	/**
	 * 预收款单删除
	 * 
	 * @author tcl
	 * @date 2015.5.11
	 */
	public void deleteAdvanceReceive(String[] vouchids) throws Exception {
		String vouchid = "";
		List<String> headlist = new ArrayList<String>();
		List<String> bodylist = new ArrayList<String>();
		CommonDataDMO daoB = new CommonDataDMO(BxgtStepButton.DESIGN_B);

		int j = 0;
		for (int i = 0; i < vouchids.length; i++) {
			vouchid += "'" + vouchids[i] + "',";
			j++;
			if (j == 100) {
				vouchid = vouchid.substring(0, vouchid.lastIndexOf(","));
				String sqlhead = "delete from arap_djzb where nvl(dr,0)=0 and vouchid in("
						+ vouchid + ")";
				String sqlbody = "delete from arap_djfb where nvl(dr,0)=0 and vouchid in("
						+ vouchid + ")";
				headlist.add(sqlhead);
				bodylist.add(sqlbody);
				vouchid = "";
				j = 0;// 初始化j
			}
		}
		if (j != 0) {
			vouchid = vouchid.substring(0, vouchid.lastIndexOf(","));
			String sqlhead = "delete from arap_djzb where nvl(dr,0)=0 and vouchid in("
					+ vouchid + ")";
			String sqlbody = "delete from arap_djfb where nvl(dr,0)=0 and vouchid in("
					+ vouchid + ")";
			headlist.add(sqlhead);
			bodylist.add(sqlbody);
		}
		// 执行SQL语句
		daoB.execDatas(headlist.toArray(new String[0]));
		daoB.execDatas(bodylist.toArray(new String[0]));
	}

	/**
	 * 结算单删除
	 * 
	 * @author xm
	 * @date 2015.5.11
	 */
	public void deleteSettleOrder(String[] pkSettles) throws Exception {
		String pkset = "";
		List<String> headlist = new ArrayList<String>();
		List<String> bodylist = new ArrayList<String>();
		CommonDataDMO dao = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		int j = 0;
		for (int i = 0; i < pkSettles.length; i++) {
			pkset += "'" + pkSettles[i] + "',";
			j++;
			if (j == 100) {
				pkset = pkset.substring(0, pkset.lastIndexOf(","));
				String sqlhead = "delete from cmp_settlement where nvl(dr,0)=0 and pk_settlement in("
						+ pkset + ")";
				String sqlbody = "delete from cmp_detail where nvl(dr,0)=0 and pk_settlement in("
						+ pkset + ")";
				headlist.add(sqlhead);
				bodylist.add(sqlbody);
				pkset = "";
				j = 0;
			}
		}
		if (j != 0) {
			pkset = pkset.substring(0, pkset.lastIndexOf(","));
			String sqlhead = "delete from cmp_settlement where nvl(dr,0)=0 and pk_settlement in("
					+ pkset + ")";
			String sqlbody = "delete from cmp_detail where nvl(dr,0)=0 and pk_settlement in("
					+ pkset + ")";
			headlist.add(sqlhead);
			bodylist.add(sqlbody);
		}
		dao.execDatas(headlist.toArray(new String[0]));
		dao.execDatas(bodylist.toArray(new String[0]));
	}

	/**
	 * 拼接B帐单据表头VO新增SQL数组
	 * 
	 * @author xm
	 * @date 2015.5.7
	 */
	private String[] makeHeadVOInsertSqls(String[] insertHeadVOSqls,
			ArrayList<Map<String, Object>> headVOList, Integer billTypeFlag) {
		String insertHeadVOSql = "";
		if (headVOList != null && headVOList.size() > 0 && billTypeFlag != null) {
			// 主表所有的列名
			Object[] headColumnStr = null;
			insertHeadVOSqls = new String[headVOList.size()];
			for (int i = 0; i < headVOList.size(); i++) {
				// 拼接B帐单据表头VO新增SQL===============START====================
				Map<String, Object> voMap = headVOList.get(i);
				if (9 == billTypeFlag)
					insertHeadVOSql = "insert into arap_djzb (";
				if (10 == billTypeFlag)
					insertHeadVOSql = "insert into cmp_settlement (";
				if (11 == billTypeFlag)
					insertHeadVOSql = "insert into po_order (";
				if (12 == billTypeFlag)
					insertHeadVOSql = "insert into ic_general_h (";
				if (13 == billTypeFlag)
					insertHeadVOSql = "insert into po_invoice (";
				if (14 == billTypeFlag)
					insertHeadVOSql = "insert into po_praybill (";
				headColumnStr = voMap.keySet().toArray();
				// 循环拼接insert into tablename (列名，列名，。。。。)
				for (int j = 0; j < headColumnStr.length; j++) {
					insertHeadVOSql += headColumnStr[j] + ",";
					if (j == headColumnStr.length - 1) {
						insertHeadVOSql = insertHeadVOSql.substring(0,
								insertHeadVOSql.lastIndexOf(","))
								+ ") values (";
					}
				}
				// 循环拼接values(值，值，值。。。。。。)
				for (int j = 0; j < headColumnStr.length; j++) {
					insertHeadVOSql += checkNullColumn(voMap
							.get(headColumnStr[j]))
							+ ",";
				}

				insertHeadVOSql = insertHeadVOSql.substring(0, insertHeadVOSql
						.lastIndexOf(","))
						+ ")";

				// 拼接B帐单据表头VO新增SQL================END===================
				// 放入SQL数组
				insertHeadVOSqls[i] = insertHeadVOSql;
			}

		}
		return insertHeadVOSqls;
	}

	/**
	 * 
	 * 拼接B帐单据表体VO新增SQL数组
	 * 
	 * @author xm
	 * @date 2015.5.7
	 */
	private String[] makeBodyVOInsertSqls(String[] insertBodyVOSqls,
			ArrayList<Map<String, Object>> bodyVOList, Integer billTypeFlag) {
		String insertBodyVOSql = "";
		if (bodyVOList != null && bodyVOList.size() > 0 && billTypeFlag != null) {
			// 子表所有列名
			Object[] bodyColumnStr = null;
			insertBodyVOSqls = new String[bodyVOList.size()];
			for (int i = 0; i < bodyVOList.size(); i++) {
				// 拼接B帐单据表体VO新增SQL===============START====================
				Map<String, Object> voMap = bodyVOList.get(i);
				if (9 == billTypeFlag)
					insertBodyVOSql = "insert into arap_djfb ( ";
				if (10 == billTypeFlag)
					insertBodyVOSql = "insert into cmp_detail ( ";
				if (11 == billTypeFlag)
					insertBodyVOSql = "insert into po_order_b (";
				if (12 == billTypeFlag)
					insertBodyVOSql = "insert into ic_general_b (";
				if (13 == billTypeFlag)
					insertBodyVOSql = "insert into po_invoice_b (";
				if (14 == billTypeFlag)
					insertBodyVOSql = "insert into po_praybill_b (";
				bodyColumnStr = voMap.keySet().toArray();
				// 循环拼接insert into tablename (列名，列名，列名。。。。)
				for (int j = 0; j < bodyColumnStr.length; j++) {
					insertBodyVOSql += bodyColumnStr[j] + ",";
					if (j == bodyColumnStr.length - 1) {
						insertBodyVOSql = insertBodyVOSql.substring(0,
								insertBodyVOSql.lastIndexOf(","))
								+ ") values (";
					}
				}
				// 循环拼接values(值，值，值。。。。。。)
				for (int j = 0; j < bodyColumnStr.length; j++) {
					insertBodyVOSql += checkNullColumn(voMap
							.get(bodyColumnStr[j]))
							+ ",";
				}
				insertBodyVOSql = insertBodyVOSql.substring(0, insertBodyVOSql
						.lastIndexOf(","))
						+ ")";
				// 拼接B帐B帐单据表体VO新增SQL===============END====================
				// 放入SQL数组
				insertBodyVOSqls[i] = insertBodyVOSql;
			}
		}
		return insertBodyVOSqls;
	}

	/**
	 * 字符串封装
	 * 
	 * @author xm
	 * @date 2015.5.7
	 */
	public String getStgObj(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 采购相关订单删除
	 * 
	 * @author xm
	 * @date 2015.5.18
	 */
	private void deletePurchaseBill(String[] billPks, int billTypeFlag)
			throws Exception {
		String delHeadVoSql = "delete from ";
		String delBodyVoSql = "delete from ";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 货位
		String delHwSql = "delete from ic_general_bb1 where cgeneralbid in('";
		// 累计结算
		String delLjjsSql = "delete from ic_general_bb3 where cgeneralbid in('";
		if (11 == billTypeFlag) {
			// 采购订单
			delHeadVoSql += "po_order where corderid  in('";
			delBodyVoSql += "po_order_b where corderid  in ('";
			for (int i = 0; i < billPks.length; i++) {
				delHeadVoSql += billPks[i] + "','";
				delBodyVoSql += billPks[i] + "','";
			}
			delHeadVoSql = delHeadVoSql.substring(0, delHeadVoSql
					.lastIndexOf(","))
					+ ")";
			delBodyVoSql = delBodyVoSql.substring(0, delBodyVoSql
					.lastIndexOf(","))
					+ ")";
			// 删除子表
			dao.executeUpdate(delBodyVoSql);
			// 删除主表
			dao.executeUpdate(delHeadVoSql);

		} else if (12 == billTypeFlag) {

			// 采购入库单
			delHeadVoSql += "ic_general_h where cgeneralhid in('";
			delBodyVoSql += "ic_general_b where cgeneralhid in ('";
			// 获得采购入库单表体PK
			String[] purchaseInBodyPk = getPurchaseInBodyPks(billPks);
			if (purchaseInBodyPk != null && purchaseInBodyPk.length > 0) {
				for (int i = 0; i < purchaseInBodyPk.length; i++) {
					delHwSql += purchaseInBodyPk[i] + "','";
				}
				delHwSql = delHwSql.substring(0, delHwSql.lastIndexOf(","))
						+ ")";

				// 删除货位信息
				dao.executeUpdate(delHwSql);
			}

			if (purchaseInBodyPk != null && purchaseInBodyPk.length > 0) {
				for (int i = 0; i < purchaseInBodyPk.length; i++) {
					delLjjsSql += purchaseInBodyPk[i] + "','";
				}
				delLjjsSql = delLjjsSql.substring(0, delLjjsSql
						.lastIndexOf(","))
						+ ")";
				// 删除累计结算信息
				dao.executeUpdate(delLjjsSql);
			}
			for (int i = 0; i < billPks.length; i++) {
				delHeadVoSql += billPks[i] + "','";
				delBodyVoSql += billPks[i] + "','";
			}
			delHeadVoSql = delHeadVoSql.substring(0, delHeadVoSql
					.lastIndexOf(","))
					+ ") and cbilltypecode ='45'";
			delBodyVoSql = delBodyVoSql.substring(0, delBodyVoSql
					.lastIndexOf(","))
					+ ") and cbodybilltypecode ='45'";

			// 删除子表
			dao.executeUpdate(delBodyVoSql);
			// 删除主表
			dao.executeUpdate(delHeadVoSql);

			// 获得ia_bill表主键
			String sqlia = "select distinct cbillid from ia_bill_b where cicbillid in ";
			String[] iaPks = null;
			iaPks = this.queryPks1ByPks2BZT(billPks, sqlia);
			// 删除ia_bill_b
			String del_ia_b = "delete from ia_bill_b where cicbillid in ";
			this.deleteOrUpdateBillByPksBZT(billPks, del_ia_b);
			// 删除ia_bill
			String del_ia = "delete from ia_bill where cbillid in ";
			this.deleteOrUpdateBillByPksBZT(iaPks, del_ia);

		} else if (13 == billTypeFlag) {
			// 采购发票
			delHeadVoSql += "po_invoice where cinvoiceid in('";
			delBodyVoSql += "po_invoice_b where cinvoiceid in ('";
			for (int i = 0; i < billPks.length; i++) {
				delHeadVoSql += billPks[i] + "','";
				delBodyVoSql += billPks[i] + "','";
			}
			delHeadVoSql = delHeadVoSql.substring(0, delHeadVoSql
					.lastIndexOf(","))
					+ ")";
			delBodyVoSql = delBodyVoSql.substring(0, delBodyVoSql
					.lastIndexOf(","))
					+ ")";
			// 删除子表
			dao.executeUpdate(delBodyVoSql);
			// 删除主表
			dao.executeUpdate(delHeadVoSql);

		} else if (14 == billTypeFlag) {
			// 请购单
			delHeadVoSql += "po_praybill where cpraybillid in('";
			delBodyVoSql += "po_praybill_b where cpraybillid in ('";
			for (int i = 0; i < billPks.length; i++) {
				delHeadVoSql += billPks[i] + "','";
				delBodyVoSql += billPks[i] + "','";
			}
			delHeadVoSql = delHeadVoSql.substring(0, delHeadVoSql
					.lastIndexOf(","))
					+ ")";
			delBodyVoSql = delBodyVoSql.substring(0, delBodyVoSql
					.lastIndexOf(","))
					+ ")";
			// 删除子表
			dao.executeUpdate(delBodyVoSql);
			// 删除主表
			dao.executeUpdate(delHeadVoSql);
		}

	}

	/**
	 * 根据采购入库单主表PK 获得采购入库单单表体pk
	 * 
	 * @author xm
	 * @date 2015.5.18
	 * @param pk_sale
	 * @return
	 * @throws DAOException
	 */
	private String[] getPurchaseInBodyPks(String[] pkPruchaseIn)
			throws Exception {
		String sql = "select distinct cgeneralbid from ic_general_b where "
				+ " cbodybilltypecode='45' and nvl(dr,0)=0 and cgeneralhid in ";
		// 采购入库单表体pk数组
		String[] bodyPks = null;
		bodyPks = this.queryPks1ByPks2BZT(pkPruchaseIn, sql);
		return bodyPks;
	}

	/**
	 * 根据采购订单查询下游单据（采购入库单）
	 * 
	 * @author xm
	 * @date 2015.5.18
	 * @param SourcePkHid
	 * @return
	 * @throws Exception
	 */
	private String[] getPurchaseInByPurchaseOrder(String[] SourcePkHids,
			String design) throws Exception {
		String sql = "select distinct a.cgeneralhid from ic_general_b a where a.cbodybilltypecode = '45' and a.csourcebillhid in ('";
		for (int i = 0; i < SourcePkHids.length; i++) {
			sql += SourcePkHids[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") and nvl(a.dr,0)=0 ";

		String[] pkPurchaseIns = null;
		ArrayList<Map<String, String>> pkList = null;
		if (design.equals(BxgtStepButton.DESIGN_A)) {
			pkList = (ArrayList<Map<String, String>>) this
					.getResultListMap(sql);
		} else {
			BaseDAO dao = new BaseDAO(design);
			pkList = (ArrayList<Map<String, String>>) dao.executeQuery(sql,
					new MapListProcessor());
		}

		if (pkList != null && pkList.size() > 0) {
			pkPurchaseIns = new String[pkList.size()];
			for (int i = 0; i < pkList.size(); i++) {
				Map<String, String> pkMap = pkList.get(i);
				pkPurchaseIns[i] = pkMap.get("cgeneralhid");
			}
		}
		return pkPurchaseIns;
	}

	/**
	 * 根据采购订单同步请购单
	 * 
	 * @author xm
	 * @date 2015.5.18
	 * @param pkPraybills
	 * @throws Exception
	 */
	private void bxgtSynchroPraybill(String[] pkPraybills) throws Exception {
		// 请购单同步
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		// B帐请购单表头vo新增 SQL数组
		String insertHeadVOSQLs[] = null;
		// B帐请购单表体vo新增 SQL数组
		String insertBodyVOSQLs[] = null;
		// 拼接请购销售出库单表头VO查询SQL
		String getHeadVoSQL = "select * from po_praybill where cpraybillid in ('";
		// 拼接请购单子表VO查询SQL
		String getBodyVoSQL = "select * from po_praybill_b where cpraybillid in ('";
		for (int i = 0; i < pkPraybills.length; i++) {
			getHeadVoSQL += pkPraybills[i] + "','";
			getBodyVoSQL += pkPraybills[i] + "','";
		}
		getHeadVoSQL = getHeadVoSQL.substring(0, getHeadVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0) = 0";
		getBodyVoSQL = getBodyVoSQL.substring(0, getBodyVoSQL.lastIndexOf(","))
				+ ") and nvl(dr,0)=0";
		// 查询请购单表头VO
		ArrayList<Map<String, Object>> headVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getHeadVoSQL);
		// 查询请购单表体VO
		ArrayList<Map<String, Object>> bodyVoList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(getBodyVoSQL);

		// 拼接B帐表头VO新增SQL数组
		insertHeadVOSQLs = makeHeadVOInsertSqls(insertHeadVOSQLs, headVoList,
				IBxgtBillType.PRAY_BILL);
		// 拼接B帐表体VO新增SQL数组
		insertBodyVOSQLs = makeBodyVOInsertSqls(insertBodyVOSQLs, bodyVoList,
				IBxgtBillType.PRAY_BILL);
		if (pkPraybills != null && pkPraybills.length > 0) {
			// 插入数据前先删除
			this.deletePurchaseBill(pkPraybills, IBxgtBillType.PRAY_BILL);
		}

		// 批量执行SQL类
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);

		// 批量插入请购单表头VO到B帐
		dmo.execDatas(insertHeadVOSQLs);
		// 批量插入请购单表体VO到B帐
		dmo.execDatas(insertBodyVOSQLs);

	}

	/**
	 * 根据采购订单pks获得相对应的询价请购单的pks
	 * 
	 * @author xm
	 * @date 2015.5.20
	 * @param PurchaseOrders
	 * @return
	 * @throws Exception
	 */
	private String[] getPraybillPks(String[] pkPurchaseOrders) throws Exception {
		String sql = "select p.cpraybillid from po_praybill p where p.cpraybillid in ( select distinct b.csourcebillid from po_order_b b where b.corderid in ('";
		for (int i = 0; i < pkPurchaseOrders.length; i++) {
			sql += pkPurchaseOrders[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + "))and nvl(dr,0) = 0";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		ArrayList<Map<String, Object>> pkList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(sql);
		// 请购单pks
		String[] pkPraybills = null;
		if (pkList != null && pkList.size() > 0) {
			pkPraybills = new String[pkList.size()];
			for (int i = 0; i < pkList.size(); i++) {
				String pkPraybill = BFPubTool
						.getString_TrimZeroLenAsNull(pkList.get(i).get(
								"cpraybillid"));
				pkPraybills[i] = pkPraybill;
			}
		}
		return pkPraybills;
	}

	/**
	 * 根据采购入库单pks获得相对应的采购发票的pks
	 * 
	 * @author xm
	 * @date 2015.5.20
	 * @param pkPurchaseIn
	 * @return
	 * @throws Exception
	 */
	private String[] getPurchaseInvoicePks(String[] pkPurchaseIns)
			throws Exception {
		String sql = "select cinvoiceid from po_invoice_b where cupsourcebillid in('";
		for (int i = 0; i < pkPurchaseIns.length; i++) {
			sql += pkPurchaseIns[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") and nvl(dr,0)=0";
		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_A);
		ArrayList<Map<String, Object>> pkList = (ArrayList<Map<String, Object>>) this
				.getResultListMap(sql);
		// 采购发票pks
		String[] pkPurchaseInvoices = null;
		if (pkList != null && pkList.size() > 0) {
			pkPurchaseInvoices = new String[pkList.size()];
			for (int i = 0; i < pkList.size(); i++) {
				String pkPurchaseInvoice = BFPubTool
						.getString_TrimZeroLenAsNull(pkList.get(i).get(
								"cinvoiceid"));
				pkPurchaseInvoices[i] = pkPurchaseInvoice;
			}
		}
		return pkPurchaseInvoices;

	}

	/**
	 * 采购订单锁定
	 * 
	 * @author xm
	 * @date 2015.5.20
	 */
	public String[] bxgtLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception {

		// 根据采购订单主键获得对应采购入库单主键
		/*
		 * String[] pkPurchaseIns = this
		 * .getSaleOutByOrderidSuodan(pkPurchaseOrders);
		 */
		// 获取无发票的订单号
		String sql1 = "select distinct a.vordercode from po_order a where a.corderid in ";
		String sql2 = " and not exists (select 1 from (select distinct cfirstbillhid from ic_general_b where cbodybilltypecode = '45' and cfirstbillhid in ";
		String sql3 = " and nvl(dr, 0) = 0) c where c.cfirstbillhid = a.corderid)";

		String[] billcodes = null;
		billcodes = this.queryBillcodeBySqlsBZT(pkPurchaseOrders, sql1, sql2,
				sql3);

		String[] corderids = null;
		if (billcodes != null && billcodes.length > 0) {// 存在无发票的订单
			sql1 = sql1.replaceFirst("a.vordercode", "a.corderid");
			sql2 = sql2.replaceFirst("not exists", "exists");
			corderids = this.queryBillcodeBySqlsBZT(pkPurchaseOrders, sql1,
					sql2, sql3);

		} else {
			corderids = pkPurchaseOrders;
		}
		if (corderids != null && corderids.length > 0) {// 不全是没有发票的数据，不锁单
			String[] pkPurchaseIns = this.getPurchaseInPksB(corderids);
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(corderids);
			this.lockPurchaseBill(corderids, pkPurchaseIns, pkPurchaseInvoices);
		}

		return billcodes;
	}

	/**
	 * 锁定采购相关单据
	 * 
	 * @author xm
	 * @date 2015.5.20
	 * @param pkPurchaseOrders
	 * @param pkPraybills
	 * @param pkPurchaseIns
	 * @param pkPurchaseInvoices
	 * @throws Exception
	 */
	private void lockPurchaseBill(String[] pkPurchaseOrders,
			String[] pkPurchaseIns, String[] pkPurchaseInvoices)
			throws Exception {
		lockPurchaseOrder(pkPurchaseOrders);
		lockPurchaseIn(pkPurchaseIns);
		lockPurchaseInvoice(pkPurchaseInvoices);
		// lockPraybill(pkPraybills);
	}

	/**
	 * 锁定采购订单
	 * 
	 * @author xm
	 * @date 2015.5.20
	 * @param pkPurchaseOrders
	 * @throws Exception
	 */
	private void lockPurchaseOrder(String[] pkPurchaseOrders) throws Exception {
		LockInfo[] bills = new LockInfo[pkPurchaseOrders.length];
		String[] sqls = new String[pkPurchaseOrders.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setCorderid(pkPurchaseOrders[i]);
			bills[i] = bill;

			String sql = "insert into bxgt_islock(pk,corderid)values(seq_info2.nextval,'"
					+ pkPurchaseOrders[i] + "')";
			sqls[i] = sql;
		}
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.insertVOArray(bills);
		this.excuteUpdateJDBCs(sqls);
	}

	/**
	 * 锁定采购入库单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseIns
	 * @throws Exception
	 */
	private void lockPurchaseIn(String[] pkPurchaseIns) throws Exception {
		LockInfo[] bills = new LockInfo[pkPurchaseIns.length];
		String[] sqls = new String[pkPurchaseIns.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setCgeneralhid(pkPurchaseIns[i]);
			bills[i] = bill;

			String sql = "insert into bxgt_islock(pk,cgeneralhid)values(seq_info2.nextval,'"
					+ pkPurchaseIns[i] + "')";
			sqls[i] = sql;
		}
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.insertVOArray(bills);
		this.excuteUpdateJDBCs(sqls);
	}

	/**
	 * 锁定采购发票
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseInvoices
	 * @throws Exception
	 */
	private void lockPurchaseInvoice(String[] pkPurchaseInvoices)
			throws Exception {
		if (pkPurchaseInvoices == null || pkPurchaseInvoices.length <= 0) {
			return;
		}
		LockInfo[] bills = new LockInfo[pkPurchaseInvoices.length];
		String[] sqls = new String[pkPurchaseInvoices.length];
		for (int i = 0; i < bills.length; i++) {
			LockInfo bill = new LockInfo();
			bill.setCinvoiceid(pkPurchaseInvoices[i]);
			bills[i] = bill;

			String sql = "insert into bxgt_islock(pk,cinvoiceid)values(seq_info2.nextval,'"
					+ pkPurchaseInvoices[i] + "')";
			sqls[i] = sql;
		}
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.insertVOArray(bills);
		this.excuteUpdateJDBCs(sqls);
	}

	/**
	 * 锁定请购单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPraybills
	 * @throws Exception
	 */
	/*
	 * private void lockPraybill(String[] pkPraybills) throws DAOException {
	 * LockInfo[] bills = new LockInfo[pkPraybills.length]; for (int i = 0; i <
	 * bills.length; i++) { LockInfo bill = new LockInfo();
	 * bill.setCpraybillid(pkPraybills[i]); bills[i] = bill; } BaseDAO dao = new
	 * BaseDAO("design_z"); dao.insertVOArray(bills); }
	 */

	/**
	 * B账根据采购订单pks获得采购入库单pks
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseOrders
	 * @return
	 * @throws Exception
	 */
	private String[] getPurchaseInPksB(String[] pkPurchaseOrders)
			throws Exception {
		// 获得采购订单pk
		String sql = "select distinct cgeneralhid from ic_general_b where csourcebillhid in('";
		for (int i = 0; i < pkPurchaseOrders.length; i++) {
			sql += pkPurchaseOrders[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") and nvl(dr,0)=0";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<Map<String, Object>> headPkList = (ArrayList<Map<String, Object>>) dao
				.executeQuery(sql, new MapListProcessor());
		String[] cgeneralhids = null;
		if (headPkList != null && headPkList.size() > 0) {
			cgeneralhids = new String[headPkList.size()];
			for (int i = 0; i < headPkList.size(); i++) {
				String cgeneralhid = BFPubTool
						.getString_TrimZeroLenAsNull(headPkList.get(i).get(
								"cgeneralhid"));
				cgeneralhids[i] = cgeneralhid;
			}
		}
		return cgeneralhids;

	}

	/**
	 * B账根据采购入库单订单pks获得采购发票pks
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkpurchaseIns
	 * @return
	 * @throws Exception
	 */
	private String[] getPurchaseInvoicePksB(String[] pkPurchaseIns)
			throws Exception {
		// 获得采购发票pk
		String sql = "select cinvoiceid from po_invoice_b where cupsourcebillid in('";
		for (int i = 0; i < pkPurchaseIns.length; i++) {
			sql += pkPurchaseIns[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") and nvl(dr,0)=0";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<Map<String, Object>> headPkList = (ArrayList<Map<String, Object>>) dao
				.executeQuery(sql, new MapListProcessor());
		String[] cinvoiceids = null;
		if (headPkList != null && headPkList.size() > 0) {
			cinvoiceids = new String[headPkList.size()];
			for (int i = 0; i < headPkList.size(); i++) {
				String cgeneralhid = BFPubTool
						.getString_TrimZeroLenAsNull(headPkList.get(i).get(
								"cinvoiceid"));
				cinvoiceids[i] = cgeneralhid;
			}
		}
		return cinvoiceids;
	}

	/**
	 * B账根据采购入库单订单pks获得请购单pks
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseOrders
	 * @return
	 * @throws Exception
	 */
	private String[] getPraybillPksB(String[] pkPurchaseOrders)
			throws Exception {
		// 获得请购单pk
		String sql = "select p.cpraybillid from po_praybill p where p.cpraybillid in ( select b.csourcebillid from po_order_b b where b.corderid in ('";
		for (int i = 0; i < pkPurchaseOrders.length; i++) {
			sql += pkPurchaseOrders[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")) and nvl(dr,0)=0";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<Map<String, Object>> headPkList = (ArrayList<Map<String, Object>>) dao
				.executeQuery(sql, new MapListProcessor());
		String[] cpraybillids = null;
		if (headPkList != null && headPkList.size() > 0) {
			cpraybillids = new String[headPkList.size()];
			for (int i = 0; i < headPkList.size(); i++) {
				String cgeneralhid = BFPubTool
						.getString_TrimZeroLenAsNull(headPkList.get(i).get(
								"cpraybillid"));
				cpraybillids[i] = cgeneralhid;
			}
		}
		return cpraybillids;
	}

	/**
	 * B账根据采购入库单订单pks获得采购发票pks
	 * 
	 * @author xm
	 * @date 2015.5.22
	 * @param pkPurchaseOrders
	 * @return
	 * @throws Exception
	 */
	private String[] getPurInvoicePksByPurOrderPksB(String[] pkPurchaseOrders)
			throws Exception {
		// 获得采购发票pk
		String sql = "select cinvoiceid from po_invoice_b where csourcebillid in('";
		for (int i = 0; i < pkPurchaseOrders.length; i++) {
			sql += pkPurchaseOrders[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") and nvl(dr,0)=0";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<Map<String, Object>> headPkList = (ArrayList<Map<String, Object>>) dao
				.executeQuery(sql, new MapListProcessor());
		String[] cinvoiceids = null;
		if (headPkList != null && headPkList.size() > 0) {
			cinvoiceids = new String[headPkList.size()];
			for (int i = 0; i < headPkList.size(); i++) {
				String cgeneralhid = BFPubTool
						.getString_TrimZeroLenAsNull(headPkList.get(i).get(
								"cinvoiceid"));
				cinvoiceids[i] = cgeneralhid;
			}
		}
		return cinvoiceids;
	}

	/**
	 * 根据B账请购单查询采购订单pk
	 * 
	 * @author xm
	 * @date 2015.5.22
	 * @param pkPraybills
	 * @return
	 * @throws Exception
	 */
	private String[] getPuOrdersByPraybillB(String[] pkPraybills)
			throws Exception {
		// 获得请购单对应B账中采购订单pk
		String sql = "select distinct b.corderid from po_order_b b where b.csourcebillid in ('";
		for (int i = 0; i < pkPraybills.length; i++) {
			sql += pkPraybills[i] + "','";
		}
		sql = sql.substring(0, sql.lastIndexOf(","))
				+ ") and nvl(b.dr,0)=0 and exists(select 1 from bxgt_islock k where k.corderid=b.corderid)";
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<Map<String, Object>> pkList = (ArrayList<Map<String, Object>>) dao
				.executeQuery(sql, new MapListProcessor());
		String[] corderid = null;
		if (pkList != null && pkList.size() > 0) {
			corderid = new String[pkList.size()];
			for (int i = 0; i < pkList.size(); i++) {
				String cgeneralhid = BFPubTool
						.getString_TrimZeroLenAsNull(pkList.get(i).get(
								"corderid"));
				corderid[i] = cgeneralhid;
			}
		}
		return corderid;
	}

	/**
	 * 解锁采购订单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 */
	public String bxgtUnLockPurchaseOrders(String[] pkPurchaseOrders)
			throws Exception {

		// 根据采购订单主键获得对应采购入库单主键
		String[] pkPurchaseIns = this.getPurchaseInPksB(pkPurchaseOrders);

		String[] pkPurchaseInvoices = this
				.getPurInvoicePksByPurOrderPksB(pkPurchaseOrders);

		String str = "";
		if (pkPurchaseIns != null && pkPurchaseIns.length > 0) {
			unLockPurchaseBill(pkPurchaseOrders, pkPurchaseIns,
					pkPurchaseInvoices);
		} else {
			str = "***";
		}
		return str;
	}

	/**
	 * 解锁采购相关单据
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseOrders
	 * @param pkPraybills
	 * @param pkPurchaseIns
	 * @param pkPurchaseInvoices
	 * @throws Exception
	 */
	private void unLockPurchaseBill(String[] pkPurchaseOrders,
			String[] pkPurchaseIns, String[] pkPurchaseInvoices)
			throws Exception {
		// unLockPraybill(pkPraybills);
		unLockPurchaseOrder(pkPurchaseOrders);
		unLockPurchaseIn(pkPurchaseIns);
		unLockPurchaseInvoice(pkPurchaseInvoices);
	}

	/**
	 * 解锁采购订单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseOrders
	 * @throws Exception
	 */
	private void unLockPurchaseOrder(String[] pkPurchaseOrders)
			throws Exception {
		String pk = "";
		for (String pkorder : pkPurchaseOrders) {
			pk += "'" + pkorder + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));
		String sql = "delete from bxgt_islock where corderid in(" + pk + ")";
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);

	}

	/**
	 * 解锁采购入库单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseIns
	 * @throws DAOException
	 */
	private void unLockPurchaseIn(String[] pkPurchaseIns) throws Exception {
		String pk = "";
		for (String pkorder : pkPurchaseIns) {
			pk += "'" + pkorder + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));
		String sql = "delete from bxgt_islock where cgeneralhid in(" + pk + ")";
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);
	}

	/**
	 * 解锁采购发票
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPurchaseInvoices
	 * @throws DAOException
	 */
	private void unLockPurchaseInvoice(String[] pkPurchaseInvoices)
			throws Exception {
		if (pkPurchaseInvoices == null || pkPurchaseInvoices.length <= 0) {
			return;
		}
		String pk = "";
		for (String pkorder : pkPurchaseInvoices) {
			pk += "'" + pkorder + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));
		String sql = "delete from bxgt_islock where cinvoiceid in(" + pk + ")";
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao2.executeUpdate(sql);
		this.excuteUpdateJDBC(sql);
	}

	/**
	 * 解锁请购单
	 * 
	 * @author xm
	 * @date 2015.5.21
	 * @param pkPraybills
	 * @throws DAOException
	 */
	/*
	 * private void unLockPraybill(String[] pkPraybills) throws DAOException {
	 * String sql = "delete from bxgt_islock where cpraybillid in('"; for (int i =
	 * 0; i < pkPraybills.length; i++) { sql += pkPraybills[i] + "','"; } sql =
	 * sql.substring(0, sql.lastIndexOf(",")) + ")"; BaseDAO dao = new
	 * BaseDAO("design_z"); dao.executeUpdate(sql); }
	 */

	/**
	 * 批改采购订单
	 * 
	 * @author xm
	 * @date 2015.5.22
	 */
	public ArrayList<Object[]> bxgtBatchUpdatePurchaseOrder(
			String[] pkPurchaseOrders, int days) throws Exception {
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 批改采购订单表头sql
		String updatePurchaseOrderSql = "update po_order set dauditdate = to_char(to_date(dauditdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dorderdate  = to_char(to_date(dorderdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),drevisiondate  = to_char(to_date(drevisiondate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taudittime = to_char(to_date(taudittime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmaketime  = to_char(to_date(tlastmaketime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tmaketime  = to_char(to_date(tmaketime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), trevisiontime  = to_char(to_date(trevisiontime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where corderid in ('";
		// 批改采购订单表体sql
		String updatePurchaseOrderBodySql = "update po_order_b set dplanarrvdate  = to_char(to_date(dplanarrvdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dclosedate  = to_char(to_date(dclosedate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dconfirmdate   = to_char(to_date(dconfirmdate  ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dcorrectdate  = to_char(to_date(dcorrectdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where  corderid in ('";
		// 批改采购入库单表头SQL
		String updatePurchaseInSql = "update ic_general_h set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),daccountdate = to_char(to_date(daccountdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dauditdate = to_char(to_date(dauditdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taccounttime = to_char(to_date(taccounttime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmoditime = to_char(to_date(tlastmoditime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where cbilltypecode ='45' and cgeneralhid in ('";
		// 批改采购入库单表体sql
		String updatePurchaseInBodySql = "update ic_general_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ddeliverdate  = to_char(to_date(ddeliverdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dfirstbilldate = to_char(to_date(dfirstbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),drequiredate = to_char(to_date(drequiredate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dstandbydate = to_char(to_date(dstandbydate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dvalidate  = to_char(to_date(dvalidate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dzgdate  = to_char(to_date(dzgdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),drequiretime  = to_char(to_date(drequiretime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where cbodybilltypecode='45' and cgeneralhid in ('";

		// 批改库位sql
		String updateKwSql = "update ic_general_bb1 set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where  cgeneralbid in ('";
		// 批改结算信息sql
		String updateSettleSql = "update ic_general_bb3 set dfchsdate = to_char(to_date(dfchsdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dtozgdate = to_char(to_date(dtozgdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dzgarapdate  = to_char(to_date(dzgarapdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where  cgeneralhid  in ('";
		// 批改采购发票表头SQL -- 暂改日期
		String updatePurchaseInvoiceSql = "update po_invoice set darrivedate  = to_char(to_date(darrivedate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dauditdate = to_char(to_date(dauditdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dinvoicedate  = to_char(to_date(dinvoicedate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taudittime  = to_char(to_date(taudittime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmaketime = to_char(to_date(tlastmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), tmaketime  = to_char(to_date(tmaketime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cinvoiceid in ('";
		// 批改采购发票表体sql
		String updatePurchaseInvoiceBodySql = "update po_invoice_b set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cinvoiceid in ('";
		// 批改请购单表头SQL -- 暂改日期
		String updatePraybillSql = "update po_praybill set dauditdate  = to_char(to_date(dauditdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dpraydate = to_char(to_date(dpraydate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),drevisedate = to_char(to_date(drevisedate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taudittime  = to_char(to_date(taudittime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmaketime = to_char(to_date(tlastmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tmaketime  = to_char(to_date(tmaketime ,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), trevisiontime = to_char(to_date(trevisiontime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cpraybillid in ('";
		// 批改请购单表体sql
		String updatePraybillBodySql = "update po_praybill_b set ddemanddate   = to_char(to_date(ddemanddate  ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),dsuggestdate  = to_char(to_date(dsuggestdate ,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cpraybillid in ('";

		// 获得采购入库单pk
		String[] pkPurchaseIns = this.getPurchaseInPksB(pkPurchaseOrders);

		// 批改ia_bill
		String sqliabill = "select distinct cbillid from ia_bill_b where nvl(dr,0)=0 and cicbillid in ";
		String[] iabills = this.queryPks1ByPks2BZT(pkPurchaseIns, sqliabill);

		String sqlia_h = "update ia_bill set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),tlastmaketime = to_char(to_date(tlastmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),vdef1 = to_char(to_date(vdef1,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cbillid in ";

		String sqlia_b = "update ia_bill_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cicbillid in ";
		this.deleteOrUpdateBillByPksBZT(iabills, sqlia_h);
		this.deleteOrUpdateBillByPksBZT(pkPurchaseIns, sqlia_b);

		// 获得采购入库单表体pk
		String[] pkPurchaseInBodys = this.getPurchaseInBodyPks(pkPurchaseIns);
		// 获得采购发票pk
		String[] pkPurchaseInvoices = this
				.getPurchaseInvoicePksB(pkPurchaseIns);
		// 获得请购单pk
		String[] pkPraybills = this.getPraybillPksB(pkPurchaseOrders);
		// 执行采购订单批改语句
		for (int i = 0; i < pkPurchaseOrders.length; i++) {
			updatePurchaseOrderSql += pkPurchaseOrders[i] + "','";
			updatePurchaseOrderBodySql += pkPurchaseOrders[i] + "','";
		}
		updatePurchaseOrderSql = updatePurchaseOrderSql.substring(0,
				updatePurchaseOrderSql.lastIndexOf(","))
				+ ")";
		updatePurchaseOrderBodySql = updatePurchaseOrderBodySql.substring(0,
				updatePurchaseOrderBodySql.lastIndexOf(","))
				+ ")";
		dao.executeUpdate(updatePurchaseOrderSql);
		dao.executeUpdate(updatePurchaseOrderBodySql);
		// 执行采购入库单批改语句
		if (pkPurchaseIns != null && pkPurchaseIns.length > 0) {
			for (int i = 0; i < pkPurchaseIns.length; i++) {
				updatePurchaseInSql += pkPurchaseIns[i] + "','";
				updatePurchaseInBodySql += pkPurchaseIns[i] + "','";
				updateSettleSql += pkPurchaseIns[i] + "','";
			}
			updatePurchaseInSql = updatePurchaseInSql.substring(0,
					updatePurchaseInSql.lastIndexOf(","))
					+ ")";
			updatePurchaseInBodySql = updatePurchaseInBodySql.substring(0,
					updatePurchaseInBodySql.lastIndexOf(","))
					+ ")";
			updateSettleSql = updateSettleSql.substring(0, updateSettleSql
					.lastIndexOf(","))
					+ ")";
			dao.executeUpdate(updatePurchaseInSql);
			dao.executeUpdate(updatePurchaseInBodySql);
			dao.executeUpdate(updateSettleSql);
		}
		// 执行库位批改语句
		if (pkPurchaseInBodys != null && pkPurchaseInBodys.length > 0) {
			for (int i = 0; i < pkPurchaseInBodys.length; i++) {
				updateKwSql += pkPurchaseInBodys[i] + "','";
			}
			updateKwSql = updateKwSql
					.substring(0, updateKwSql.lastIndexOf(","))
					+ ")";
			dao.executeUpdate(updateKwSql);
		}
		// 执行采购发票批改语句
		if (pkPurchaseInvoices != null && pkPurchaseInvoices.length > 0) {
			for (int i = 0; i < pkPurchaseInvoices.length; i++) {
				updatePurchaseInvoiceSql += pkPurchaseInvoices[i] + "','";
				updatePurchaseInvoiceBodySql += pkPurchaseInvoices[i] + "','";
			}
			updatePurchaseInvoiceSql = updatePurchaseInvoiceSql.substring(0,
					updatePurchaseInvoiceSql.lastIndexOf(","))
					+ ")";
			updatePurchaseInvoiceBodySql = updatePurchaseInvoiceBodySql
					.substring(0, updatePurchaseInvoiceBodySql.lastIndexOf(","))
					+ ")";
			dao.executeUpdate(updatePurchaseInvoiceSql);
			dao.executeUpdate(updatePurchaseInvoiceBodySql);
		}
		// 执行请购单批改语句
		if (pkPraybills != null && pkPraybills.length > 0) {
			for (int i = 0; i < pkPraybills.length; i++) {
				updatePraybillSql += pkPraybills[i] + "','";
				updatePraybillBodySql += pkPraybills[i] + "','";
			}
			updatePraybillSql = updatePraybillSql.substring(0,
					updatePraybillSql.lastIndexOf(","))
					+ ")";
			updatePraybillBodySql = updatePraybillBodySql.substring(0,
					updatePraybillBodySql.lastIndexOf(","))
					+ ")";
			dao.executeUpdate(updatePraybillSql);
			dao.executeUpdate(updatePraybillBodySql);
		}
		// 批改完记录到打印表
		this.printAfterBatch(pkPraybills, pkPurchaseOrders, pkPurchaseIns,
				pkPurchaseInvoices, BxgtStepButton.PU_FLOW);

		// 批改完校验库存量
		ArrayList<Object[]> list = this.batchCheckQueryOnHand(pkPurchaseIns);
		return list;
	}

	/**
	 * 批改材料出库
	 */
	public ArrayList<Object[]> bxgtBatchUpdateMaterialOrder(
			String[] pkMaterialOrders, int days) throws Exception {

		String pk = "";
		for (String pkMaterialOrder : pkMaterialOrders) {
			pk += "'" + pkMaterialOrder + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));
		// 主表
		String updateSaleOutSql = "update ic_general_h set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),daccountdate = to_char(to_date(daccountdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),taccounttime = to_char(to_date(taccounttime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tlastmoditime = to_char(to_date(tlastmoditime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'), tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where cgeneralhid in ("
				+ pk + ")";

		// 子表
		String updateSaleOutBody = "update ic_general_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ddeliverdate = to_char(to_date(ddeliverdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where cgeneralhid in ("
				+ pk + ")";

		// 批改关联表1
		String updateSaleOutBody1 = "update ic_general_bb1 set ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss') where cgeneralbid in(select cgeneralbid from ic_general_b where "
				+ " nvl(dr,0)=0 and cgeneralhid in (" + pk + ") )";

		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao.executeUpdate(updateSaleOutSql);
		dao.executeUpdate(updateSaleOutBody);
		dao.executeUpdate(updateSaleOutBody1);

		// 批改ia_bill
		String sqliabill = "select distinct cbillid from ia_bill_b where nvl(dr,0)=0 and cicbillid in ";
		String[] iabills = this.queryPks1ByPks2BZT(pkMaterialOrders, sqliabill);

		String sqlia_h = "update ia_bill set dbilldate = to_char(to_date(dbilldate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),tlastmaketime = to_char(to_date(tlastmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),tmaketime = to_char(to_date(tmaketime,'yyyy-MM-dd hh24:mi:ss')+"
				+ days
				+ ",'yyyy-MM-dd hh24:mi:ss'),vdef1 = to_char(to_date(vdef1,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cbillid in ";

		String sqlia_b = "update ia_bill_b set dbizdate = to_char(to_date(dbizdate,'yyyy-MM-dd')+"
				+ days
				+ ",'yyyy-MM-dd'),ts = to_char(to_date(ts,'yyyy-MM-dd hh24:mi:ss')+"
				+ days + ",'yyyy-MM-dd hh24:mi:ss') where cicbillid in ";
		this.deleteOrUpdateBillByPksBZT(iabills, sqlia_h);
		this.deleteOrUpdateBillByPksBZT(pkMaterialOrders, sqlia_b);

		// 批改完记录到打印表
		this.printAfterBatch(pkMaterialOrders, null, null, null,
				BxgtStepButton.CL_FLOW);

		// 批改完校验库存量
		ArrayList<Object[]> list = this.batchCheckQueryOnHand(pkMaterialOrders);
		return list;
	}

	/**
	 * 获得销售订单的下游出库单（锁定）
	 */
	private String[] getSaleOutBySaleidSuodan(String[] pk_sales)
			throws Exception {

		String pk = "";
		for (int i = 0; i < pk_sales.length; i++) {
			pk += "'" + pk_sales[i] + "',";
		}
		pk = pk.substring(0, pk.lastIndexOf(","));

		String sql = "select a.vreceiptcode from so_sale a where a.csaleid in ("
				+ pk
				+ ") "
				+ " and not exists (select 1 from (select distinct cfirstbillhid from ic_general_b "
				+ " where cbodybilltypecode = '4C' and cfirstbillhid in("
				+ pk
				+ ") and nvl(dr, 0) = 0) c where c.cfirstbillhid = a.csaleid)";

		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		ArrayList<String> list = (ArrayList<String>) dao.executeQuery(sql,
				new ColumnListProcessor());

		String billno = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				billno += "【" + list.get(i) + "】";
			}
			throw new BusinessException("B帐单据号为" + billno
					+ "的销售订单没有销售出库单，不能锁定！");
		}

		return this.getCSaleOutByCsaleid(pk_sales, BxgtStepButton.DESIGN_B);

	}

	/**
	 * 获得采购订单的下游入库单（锁定）
	 */
	/*
	 * private String[] getSaleOutByOrderidSuodan(String[] orderids) throws
	 * Exception {
	 * 
	 * String sql = "select distinct a.vordercode from po_order a where
	 * a.corderid in "; String sql1 = " and not exists (select 1 from (select
	 * distinct cfirstbillhid from ic_general_b where cbodybilltypecode = '45'
	 * and cfirstbillhid in "; String sql2 = " and nvl(dr, 0) = 0) c where
	 * c.cfirstbillhid = a.corderid)"; // BaseDAO dao = new
	 * BaseDAO(BxgtStepButton.DESIGN_B); String[] billcodes = null; billcodes =
	 * this.queryBillcodeBySqlsBZT(orderids,"", sql1, sql2, sql3); String billno =
	 * ""; if (list != null && list.size() > 0) { for (int i = 0; i <
	 * list.size(); i++) { billno += "【" + list.get(i) + "】"; } throw new
	 * BusinessException("B帐单据号为" + billno + "的采购订单没有采购入库单，不能锁定！"); }
	 * 
	 * return this.getPurchaseInPksB(orderids); }
	 */

	/**
	 * 获得采购订单的下游发票（锁定）(无发票的订单号)
	 */
	/*
	 * private String[] getInvoiceByOrderidSuodan(String[] orderids) throws
	 * Exception {
	 * 
	 * String sql1 = "select distinct a.vordercode from po_order a where
	 * a.corderid in "; String sql2 = " and not exists (select 1 from (select
	 * distinct csourcebillid from po_invoice_b where csourcebillid in "; String
	 * sql3 = " and nvl(dr, 0) = 0) c where c.csourcebillid = a.corderid)";
	 * 
	 * String[] billcodes = null; String sql = ""; billcodes = this
	 * .queryBillcodeBySqlsBZT(orderids, sql, sql1, sql2, sql3);
	 * 
	 * if (billcodes != null && billcodes.length > 0) {// 存在无发票的订单 sql = "select
	 * corderid from po_order where nvl(dr,0)=0 and "; }
	 * 
	 * return billcodes; }
	 */

	/**
	 * 获得产成品入库单主表主键集合
	 */

	private String[] getsynProductInBill(String[] salepks) throws Exception {

		// BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		String[] pksyn = null;
		String sql = "select distinct t.cgeneralhid from ic_general_b t where t.cbodybilltypecode='46' and "
				+ " nvl(dr,0)=0 and cfirstbillhid in ";
		pksyn = this.queryPks1ByPks2BZT(salepks, sql);
		return pksyn;
	}

	/**
	 * 锁定材料出库
	 */
	public String bxgtLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception {

		String str = "";
		if (pkMaterialOrders != null && pkMaterialOrders.length > 0) {
			this.lockSaleOut(pkMaterialOrders);
		} else {
			str = "***";
		}

		return str;
	}

	/**
	 * 解锁材料出库
	 */
	public String bxgtUnLockMaterialOrders(String[] pkMaterialOrders)
			throws Exception {
		String str = "";
		if (pkMaterialOrders != null && pkMaterialOrders.length > 0) {
			this.unLockSaleOut(pkMaterialOrders);
		} else {
			str = "***";
		}

		return str;
	}

	/**
	 * 同步AB帐套销售订单状态到同步表
	 */
	private void synABSaleAllData(String[] pk_sales, String[] pk_saleOuts)
			throws Exception {

		if (pk_sales == null || pk_sales.length <= 0) {
			return;
		}
		// 同步前删除
		String sql = "delete from bxgt_issyn where csaleid in ";
		this.deleteOrUpdateBillByPks(pk_sales, sql);
		this.deleteOrUpdateBillByPksBZT(pk_sales, sql);

		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		SynInfo[] bills = new SynInfo[pk_sales.length];
		String[] sqls = new String[pk_sales.length];
		for (int i = 0; i < bills.length; i++) {
			SynInfo bill = new SynInfo();
			bill.setCsaleid(pk_sales[i]);
			bills[i] = bill;

			String sql1 = "insert into bxgt_issyn(pk,csaleid)values(seq_info1.nextval,'"
					+ pk_sales[i] + "')";
			sqls[i] = sql1;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(bills);
		// 同步销售出库
		this.synSaleOutBill(pk_saleOuts);
	}

	/**
	 * 同步AB帐套销售出库，采购入库状态到同步表
	 * 
	 * @param pk_saleouts
	 * @throws Exception
	 */
	private void synSaleOutBill(String[] pk_saleouts) throws Exception {

		if (pk_saleouts == null || pk_saleouts.length <= 0) {
			return;
		}
		// 同步前删除
		String sql = "delete from bxgt_issyn where cgeneralhid in ";
		this.deleteOrUpdateBillByPks(pk_saleouts, sql);
		this.deleteOrUpdateBillByPksBZT(pk_saleouts, sql);

		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);// a
		SynInfo[] bills = new SynInfo[pk_saleouts.length];
		String[] sqls = new String[pk_saleouts.length];
		for (int i = 0; i < bills.length; i++) {
			SynInfo bill = new SynInfo();
			bill.setCgeneralhid(pk_saleouts[i]);
			bills[i] = bill;

			String sql1 = "insert into bxgt_issyn(pk,cgeneralhid)values(seq_info1.nextval,'"
					+ pk_saleouts[i] + "')";
			sqls[i] = sql1;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(bills);

	}

	/**
	 * 取消同步AB帐套销售订单状态到同步表
	 */
	private void unSynABSaleAllData(String[] pk_sales, String[] pk_saleOuts)
			throws Exception {

		if (pk_sales == null || pk_sales.length <= 0) {
			return;
		}
		// 同步前删除
		String sql = "delete from bxgt_issyn where csaleid in ";
		this.deleteOrUpdateBillByPksBZT(pk_sales, sql);
		this.deleteOrUpdateBillByPks(pk_sales, sql);

		// 删除出库
		this.unSynSaleOutBill(pk_saleOuts);
	}

	/**
	 * 取消同步AB帐套销售出库，采购入库状态到同步表
	 * 
	 * @param pk_saleouts
	 * @throws Exception
	 */
	private void unSynSaleOutBill(String[] pk_saleouts) throws Exception {

		if (pk_saleouts == null || pk_saleouts.length <= 0) {
			return;
		}
		// 同步前删除
		String sql = "delete from bxgt_issyn where cgeneralhid in ";

		this.deleteOrUpdateBillByPksBZT(pk_saleouts, sql);
		this.deleteOrUpdateBillByPks(pk_saleouts, sql);
	}

	/**
	 * 同步AB帐套采购订单流程
	 */
	private void synABOrderAllData(String[] pkPurOrders, String[] pkInOrders,
			String[] pkInvoices) throws Exception {

		if (pkPurOrders == null || pkPurOrders.length <= 0) {
			return;
		}

		// 同步前删除
		String pksale = "";
		for (String pk_sale : pkPurOrders) {
			pksale += "'" + pk_sale + "',";
		}
		pksale = pksale.substring(0, pksale.lastIndexOf(","));
		String sql = "delete from bxgt_issyn where corderid in(" + pksale + ")";

		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);// b
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);// a
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);

		SynInfo[] bills = new SynInfo[pkPurOrders.length];
		String[] sqls = new String[pkPurOrders.length];
		for (int i = 0; i < bills.length; i++) {
			SynInfo bill = new SynInfo();
			bill.setCorderid(pkPurOrders[i]);
			bills[i] = bill;

			String sql1 = "insert into bxgt_issyn(pk,corderid)values(seq_info1.nextval,'"
					+ pkPurOrders[i] + "')";
			sqls[i] = sql1;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(bills);
		// 同步采购入库
		this.synSaleOutBill(pkInOrders);
		this.synInvoiceBill(pkInvoices);

	}

	/**
	 * 同步AB帐套采购发票
	 */
	private void synInvoiceBill(String[] pkInvoices) throws Exception {

		if (pkInvoices == null || pkInvoices.length <= 0) {
			return;
		}
		// 同步前删除
		String pksale = "";
		for (String pk_sale : pkInvoices) {
			pksale += "'" + pk_sale + "',";
		}
		pksale = pksale.substring(0, pksale.lastIndexOf(","));
		String sql = "delete from bxgt_issyn where cinvoiceid in(" + pksale
				+ ")";

		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);// b
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);// a
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);

		SynInfo[] bills = new SynInfo[pkInvoices.length];
		String[] sqls = new String[pkInvoices.length];
		for (int i = 0; i < bills.length; i++) {
			SynInfo bill = new SynInfo();
			bill.setCinvoiceid(pkInvoices[i]);
			bills[i] = bill;

			String sql1 = "insert into bxgt_issyn(pk,cinvoiceid)values(seq_info1.nextval,'"
					+ pkInvoices[i] + "')";
			sqls[i] = sql1;
		}

		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(bills);

	}

	/**
	 * 取消同步AB帐套采购订单流程
	 */
	private void unSynABOrderAllData(String[] pkPurOrders, String[] pkInOrders,
			String[] pkInvoices) throws Exception {

		if (pkPurOrders == null || pkPurOrders.length <= 0) {
			return;
		}

		// 同步前删除
		String pksale = "";
		for (String pk_sale : pkPurOrders) {
			pksale += "'" + pk_sale + "',";
		}
		pksale = pksale.substring(0, pksale.lastIndexOf(","));
		String sql = "delete from bxgt_issyn where corderid in(" + pksale + ")";

		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);// a
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);

		// 同步采购入库
		this.unSynSaleOutBill(pkInOrders);
		this.unSynInvoiceBill(pkInvoices);
	}

	/**
	 * 取消同步AB帐套采购发票
	 */
	private void unSynInvoiceBill(String[] pkInvoices) throws Exception {

		if (pkInvoices == null || pkInvoices.length <= 0) {
			return;
		}
		// 同步前删除
		String pksale = "";
		for (String pk_sale : pkInvoices) {
			pksale += "'" + pk_sale + "',";
		}
		pksale = pksale.substring(0, pksale.lastIndexOf(","));
		String sql = "delete from bxgt_issyn where cinvoiceid in(" + pksale
				+ ")";

		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);// a
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);
	}

	/**
	 * 确认逻辑 by tcl
	 */
	public void confirmBill(String[] pks, String billtype) throws Exception {

		if (billtype.equals("销售订单")) {
			// 根据销售订单主键获得对应销售出库单主键
			String[] pkSaleOuts = this.getCSaleOutByCsaleid(pks,
					BxgtStepButton.DESIGN_B);
			// 根据销售订单主键获得对应收款单主键
			String[] vouchids = getSKpksB(pks, BxgtStepButton.DESIGN_B);
			if (pkSaleOuts == null || pkSaleOuts.length <= 0) {
				throw new BusinessException("B帐套销售订单下游数据不存在或被删除！");
			}
			confirmSaleOrder(pks, pkSaleOuts, vouchids);

		} else if (billtype.equals("采购订单")) {
			// 根据采购订单主键获得对应采购入库单主键
			String[] pkPurchaseIns = this.getPurchaseInPksB(pks);
			// 根据采购订单主键获得对应采购发票主键
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(pks);

			if (pkPurchaseIns == null || pkPurchaseIns.length <= 0) {
				throw new BusinessException("B帐套采购订单下游数据不存在或被删除！");
			}
			confirmPurOrder(pks, pkPurchaseIns, pkPurchaseInvoices);
		} else if (billtype.equals("材料出库")) {
			confirmMateOrder(pks);
		}
	}

	/**
	 * 向确认表中注入销售订单，销售出库单,收款单 by tcl
	 * 
	 * @throws Exception
	 */
	private void confirmSaleOrder(String[] saleOrders, String[] pkSaleOuts,
			String[] vouchids) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 确认销售订单
		ConfirmInfo[] conVOs = new ConfirmInfo[saleOrders.length];
		String[] sqls = new String[saleOrders.length];
		for (int i = 0; i < conVOs.length; i++) {
			ConfirmInfo conVo = new ConfirmInfo();
			conVo.setCsaleid(saleOrders[i]);
			conVOs[i] = conVo;

			String sql = "insert into bxgt_isconfirm(pk,csaleid)values(seq_info3.nextval,'"
					+ saleOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVOs);
		// 确认销售出库单
		ConfirmInfo[] conVOs2 = new ConfirmInfo[pkSaleOuts.length];
		String[] sqls2 = new String[pkSaleOuts.length];
		for (int i = 0; i < conVOs2.length; i++) {
			ConfirmInfo conVo = new ConfirmInfo();
			conVo.setCgeneralhid(pkSaleOuts[i]);
			conVOs2[i] = conVo;

			String sql = "insert into bxgt_isconfirm(pk,cgeneralhid)values(seq_info3.nextval,'"
					+ pkSaleOuts[i] + "')";
			sqls2[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls2);
		dao2.insertVOArray(conVOs2);
		// 确认收款单
		/*
		 * ConfirmInfo[] conVOs3 = new ConfirmInfo[vouchids.length]; String[]
		 * sqls3 = new String[vouchids.length]; for (int i = 0; i <
		 * conVOs3.length; i++) { ConfirmInfo conVo = new ConfirmInfo();
		 * conVo.setVouchid(vouchids[i]); conVOs3[i] = conVo;
		 * 
		 * String sql = "insert into
		 * bxgt_isconfirm(pk,vouchid)values(seq_info3.nextval,'" + vouchids[i] +
		 * "')"; sqls3[i] = sql; } this.excuteUpdateJDBCs(sqls3);
		 * dao2.insertVOArray(conVOs3);
		 */
	}

	/**
	 * 向确认表中注入采购订单，采购入库单，采购发票 by tcl
	 * 
	 * @throws Exception
	 */
	private void confirmPurOrder(String[] purOrders, String[] pkSaleOuts,
			String[] invoices) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 确认采购订单
		ConfirmInfo[] conVOs = new ConfirmInfo[purOrders.length];
		String[] sqls = new String[purOrders.length];
		for (int i = 0; i < conVOs.length; i++) {
			ConfirmInfo conVo = new ConfirmInfo();
			conVo.setCorderid(purOrders[i]);
			conVOs[i] = conVo;

			String sql = "insert into bxgt_isconfirm(pk,corderid)values(seq_info3.nextval,'"
					+ purOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVOs);
		// 确认采购入库单
		ConfirmInfo[] conVOs2 = new ConfirmInfo[pkSaleOuts.length];
		String[] sqls2 = new String[pkSaleOuts.length];
		for (int i = 0; i < conVOs2.length; i++) {
			ConfirmInfo conVo = new ConfirmInfo();
			conVo.setCgeneralhid(pkSaleOuts[i]);
			conVOs2[i] = conVo;

			String sql = "insert into bxgt_isconfirm(pk,cgeneralhid)values(seq_info3.nextval,'"
					+ pkSaleOuts[i] + "')";
			sqls2[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls2);
		dao2.insertVOArray(conVOs2);
		// 确认采购发票
		/*
		 * ConfirmInfo[] conVOs3 = new ConfirmInfo[invoices.length]; String[]
		 * sqls3 = new String[invoices.length]; for (int i = 0; i <
		 * conVOs3.length; i++) { ConfirmInfo conVo = new ConfirmInfo();
		 * conVo.setCinvoiceid(invoices[i]); conVOs3[i] = conVo;
		 * 
		 * String sql = "insert into
		 * bxgt_isconfirm(pk,cinvoiceid)values(seq_info3.nextval,'" +
		 * invoices[i] + "')"; sqls3[i] = sql; } this.excuteUpdateJDBCs(sqls3);
		 * dao2.insertVOArray(conVOs3);
		 */
	}

	/**
	 * 向确认表中插入材料出库
	 */
	private void confirmMateOrder(String[] mateOrders) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		ConfirmInfo[] conVos = new ConfirmInfo[mateOrders.length];
		String[] sqls = new String[mateOrders.length];
		for (int i = 0; i < conVos.length; i++) {
			ConfirmInfo conVo = new ConfirmInfo();
			conVo.setCgeneralhid(mateOrders[i]);
			conVos[i] = conVo;

			String sql = "insert into bxgt_isconfirm(pk,cgeneralhid)values(seq_info3.nextval,'"
					+ mateOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVos);
	}

	/**
	 * 取消逻辑 by tcl
	 */
	public void unConfirmBill(String[] pks, String billtype) throws Exception {

		if (billtype.equals("销售订单")) {
			// 根据销售订单主键获得对应销售出库单主键
			String[] pkSaleOuts = this.getCSaleOutByCsaleid(pks,
					BxgtStepButton.DESIGN_B);
			// 根据销售订单主键获得对应收款单主键
			String[] vouchids = getSKpksB(pks, BxgtStepButton.DESIGN_B);
			if (pkSaleOuts == null || pkSaleOuts.length <= 0) {
				throw new BusinessException("B帐套销售订单下游数据不存在或被删除！");
			}
			unConfirmSaleOrder(pks, pkSaleOuts, vouchids);

		} else if (billtype.equals("采购订单")) {
			// 根据采购订单主键获得对应采购入库单主键
			String[] pkPurchaseIns = this.getPurchaseInPksB(pks);
			// 根据采购订单主键获得对应采购发票主键
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(pks);

			if (pkPurchaseIns == null || pkPurchaseIns.length <= 0) {
				throw new BusinessException("B帐套采购订单下游数据不存在或被删除！");
			}
			unConfirmPurOrder(pks, pkPurchaseIns, pkPurchaseInvoices);
		} else if (billtype.equals("材料出库")) {
			unConfirmMateOrder(pks);
		}
	}

	/**
	 * 向确认表中删除销售订单，销售出库单,收款单 by tcl
	 * 
	 * @throws Exception
	 */
	private void unConfirmSaleOrder(String[] saleOrders, String[] pkSaleOuts,
			String[] vouchids) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		String pk2 = "";
		// String pk3 = "";
		for (String saleOrder : saleOrders) {
			pk1 += "'" + saleOrder + "',";
		}
		for (String pkSaleOut : pkSaleOuts) {
			pk2 += "'" + pkSaleOut + "',";
		}
		/*
		 * for (String vouchid : vouchids) { pk3 += "'" + vouchid + "',"; }
		 */
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		pk2 = pk2.substring(0, pk2.lastIndexOf(","));
		// pk3 = pk3.substring(0, pk3.lastIndexOf(","));
		String sql1 = "delete from bxgt_isconfirm where csaleid in(" + pk1
				+ ")";
		String sql2 = "delete from bxgt_isconfirm where cgeneralhid in(" + pk2
				+ ")";
		/*
		 * String sql3 = "delete from bxgt_isconfirm where vouchid in(" + pk3 +
		 * ")";
		 */

		this.excuteUpdateJDBC(sql1);
		this.excuteUpdateJDBC(sql2);
		// this.excuteUpdateJDBC(sql3);
		dao2.executeUpdate(sql1);
		dao2.executeUpdate(sql2);
		// dao2.executeUpdate(sql3);
	}

	/**
	 * 向确认表中删除采购订单，采购入库单，采购发票 by tcl
	 * 
	 * @throws Exception
	 */
	private void unConfirmPurOrder(String[] purOrders, String[] pkSaleOuts,
			String[] invoices) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		String pk2 = "";
		// String pk3 = "";
		for (String purOrder : purOrders) {
			pk1 += "'" + purOrder + "',";
		}
		for (String pkSaleOut : pkSaleOuts) {
			pk2 += "'" + pkSaleOut + "',";
		}
		/*
		 * for (String invoice : invoices) { pk3 += "'" + invoice + "',"; }
		 */
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		pk2 = pk2.substring(0, pk2.lastIndexOf(","));
		// pk3 = pk3.substring(0, pk3.lastIndexOf(","));
		String sql1 = "delete from bxgt_isconfirm where corderid in(" + pk1
				+ ")";
		String sql2 = "delete from bxgt_isconfirm where cgeneralhid in(" + pk2
				+ ")";
		/*
		 * String sql3 = "delete from bxgt_isconfirm where cinvoiceid in(" + pk3 +
		 * ")";
		 */

		this.excuteUpdateJDBC(sql1);
		this.excuteUpdateJDBC(sql2);
		// this.excuteUpdateJDBC(sql3);
		dao2.executeUpdate(sql1);
		dao2.executeUpdate(sql2);
		// dao2.executeUpdate(sql3);
	}

	/**
	 * 向确认表中删除材料出库
	 */
	private void unConfirmMateOrder(String[] mateOrders) throws Exception {
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		for (String mateOrder : mateOrders) {
			pk1 += "'" + mateOrder + "',";
		}
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		String sql = "delete from bxgt_isconfirm where cgeneralhid in(" + pk1
				+ ")";
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);
	}

	/**
	 * 删除单据
	 */
	public void deleteSaleOrOrder(String[] pks, String billtype)
			throws Exception {

		if (billtype.equals("销售订单")) {
			// 根据销售订单主键获得对应销售出库单，产成品入库单
			String[] pkSaleOuts = this.getCgeneralhidByCsaleidB(pks);
			if (pkSaleOuts != null && pkSaleOuts.length > 0) {
				this.deleteBill(pkSaleOuts, IBxgtBillType.SALE_OUT_ALL);
			}
			// 根据销售订单主键获得对应收款单主键
			String[] vouchids = getSKpksB(pks, BxgtStepButton.DESIGN_B);
			// 根据收款单获得结算单
			if (vouchids != null && vouchids.length > 0) {
				String[] settlePks = getSettlePks(vouchids);
				if (settlePks != null && settlePks.length > 0) {
					this.deleteBill(settlePks, IBxgtBillType.BXGT_JS);
				}

				this.deleteBill(vouchids, IBxgtBillType.BXGT_SK);
			}
			this.deleteBill(pks, IBxgtBillType.SALE_ORDER);
			this.unSynABSaleAllData(pks, pkSaleOuts);

		} else if (billtype.equals("采购订单")) {
			// 根据采购订单查询上游游单据（请购单）
			String[] pkPraybills = getPraybillPks(pks);
			// 找到采购订单上游单据请购单 获得B账中下游采购订单pks(防止有相同的请购单)
			String[] pkPurchaseOrdersB = null;
			// 根据采购订单主键获得对应采购入库单主键
			String[] pkPurchaseIns = this.getPurchaseInPksB(pks);
			// 根据采购订单主键获得对应采购发票主键
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(pks);
			if (pkPurchaseInvoices != null && pkPurchaseInvoices.length > 0) {
				this.deletePurchaseBill(pkPurchaseInvoices,
						IBxgtBillType.PURCHASE_INVOICE);
			}
			if (pkPurchaseIns != null && pkPurchaseIns.length > 0) {
				this.deletePurchaseBill(pkPurchaseIns,
						IBxgtBillType.PURCHASE_IN);
			}
			// 删除采购订单
			this.deletePurchaseBill(pks, IBxgtBillType.PURCHASE_ORDER);
			if (pkPraybills != null && pkPraybills.length > 0) {
				pkPurchaseOrdersB = getPuOrdersByPraybillB(pkPraybills);
			}
			if (pkPurchaseOrdersB != null && pkPurchaseOrdersB.length > 0) {
				this.deletePurchaseBill(pkPraybills, IBxgtBillType.PRAY_BILL);
			}
			// 取消同步表状态
			this.unSynABOrderAllData(pks, pkPurchaseIns, pkPurchaseInvoices);
		} else if (billtype.equals("材料出库")) {
			String pk = "";
			for (String pkMaterial : pks) {
				pk += "'" + pkMaterial + "',";
			}
			pk = pk.substring(0, pk.lastIndexOf(","));
			String deleteh = "delete from ic_general_h where nvl(dr,0)=0 and cgeneralhid in("
					+ pk + ")";
			String deleteb = "delete from ic_general_b where nvl(dr,0)=0 and cgeneralhid in("
					+ pk + ")";
			String sql2 = deleteb.replaceFirst("delete", "select cgeneralbid");
			String deletebb = "delete from ic_general_bb1 where nvl(dr,0)=0 and cgeneralbid in("
					+ sql2 + ")";
			// 同步之前删除B的所有数据
			BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
			dao2.executeUpdate(deletebb);
			dao2.executeUpdate(deleteb);
			dao2.executeUpdate(deleteh);

			// 取消同步表状态
			this.unSynSaleOutBill(pks);
		}
	}

	// 取随机时间
	private UFDateTime getRandomDateTime(UFDateTime time) {

		Random rm = new Random();
		int nextnum = 3 * 24 * 60 * 60 * 1000;
		int miao = rm.nextInt(nextnum);
		long newtime = time.getMillis() - miao;
		return new UFDateTime(newtime);
	}

	/**
	 * 采购订单同步供应商检查
	 */
	private void checkSupplierByPurIn(String[] pkPurIns) throws Exception {

		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);

		String pks = "";
		for (String pk : pkPurIns) {
			pks += "'" + pk + "',";
		}
		pks = pks.substring(0, pks.lastIndexOf(","));

		String sql = "select to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') from dual ";
		String billtime = (String) this.getResultObject(sql);

		// 供应商基本档案
		String sql16 = "select distinct cvendorbaseid from po_order where nvl(dr,0)=0 and corderid in("
				+ pks + ")";
		Object obj16 = this.getResultString(sql16);
		// int m_16 = 0;// 记录更新数量
		// int n_16 = 0;// 记录插入数量
		if (obj16 != null && !"".equals(obj16)) {
			// 校验B中存在的主键
			String sql16_1 = "select pk_cubasdoc from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
					+ obj16.toString() + ")";
			Object obj16_1 = dao2.executeQuery(sql16_1,
					new ColumnPKsProcessor());
			String sql16_2 = "";// 更新sql
			String sql16_3 = "";// 插入sql
			if (obj16_1 != null && !"".equals(obj16_1)) {// 有更新数据
				sql16_2 = "select * from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
						+ obj16_1.toString() + ")";
				sql16_3 = "select * from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
						+ obj16.toString()
						+ ")"
						+ " and pk_cubasdoc not in("
						+ obj16_1.toString() + ")";

			} else {
				sql16_3 = "select * from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
						+ obj16.toString() + ")";
			}
			if (!"".equals(sql16_2)) {// 有更新数据
				List<CubasdocVO> list16_2 = (List<CubasdocVO>) this
						.getResultBeanList(sql16_2, CubasdocVO.class);
				for (CubasdocVO vo : list16_2) {
					vo.setModifytime(null);
					vo.setModifier(null);
					vo.setCreatetime(billtime);
					vo.setTs(billtime.toString());
				}
				dao2.updateVOArray(list16_2.toArray(new CubasdocVO[0]));
			}

			List<CubasdocVO> list16_3 = (List<CubasdocVO>) this
					.getResultBeanList(sql16_3, CubasdocVO.class);
			if (list16_3 != null && list16_3.size() > 0) {
				for (CubasdocVO vo : list16_3) {
					vo.setModifytime(null);
					vo.setModifier(null);
					vo.setCreatetime(billtime.toString());
					vo.setTs(billtime.toString());
				}
				dao2.insertVOArrayWithPK(list16_3.toArray(new CubasdocVO[0]));
			}
		}

		// 供应商管理档案
		String sql17 = "select * from bd_cumandoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp
				+ "' and pk_cubasdoc in "
				+ "(select distinct t.cvendorbaseid from po_order t where t.corderid in("
				+ pks + ") )";
		List<CumandocVO> list17 = (List<CumandocVO>) this.getResultBeanList(
				sql17, CumandocVO.class);
		if (list17 != null && list17.size() > 0) {
			String pkcums[] = new String[list17.size()];
			for (int i = 0; i < list17.size(); i++) {
				list17.get(i).setCreatetime(billtime.toString());
				list17.get(i).setModifytime(null);
				list17.get(i).setModifier(null);
				// list17.get(i).setPk_corp(pk_corp);
				list17.get(i).setDevelopdate(
						billtime.toString().substring(0, 10));
				list17.get(i).setTs(billtime.toString());
				pkcums[i] = list17.get(i).getPrimaryKey();
			}
			// 插入前删除
			dao2.deleteByPKs(CumandocVO.class, pkcums);
			// 插入到B
			dao2.insertVOArrayWithPK(list17.toArray(new CumandocVO[0]));
		}

	}

	/**
	 * 校验材料库存可用量
	 */
	private ArrayList<Object[]> checkOnHandMaterial(String pks, String pk_mets,
			String billdate) throws Exception {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		ArrayList<Object[]> list = new ArrayList<Object[]>();// 存放最终数据
		// BaseDAO dao1 = new BaseDAO(BxgtStepButton.DESIGN_A);
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);

		HashMap<String, Object> spec = new HashMap<String, Object>();// 规格

		String sqla = "select sum(nvl(b.noutnum, 0)) outcl,s.storname,d.bodyname,e.invname,b.pk_corp,b.cinvbasid,e.invspec "
				+ " from ic_general_b b left join bd_stordoc s on b.cbodywarehouseid = s.pk_stordoc "
				+ " left join bd_calbody d on b.pk_bodycalbody = d.pk_calbody left join bd_invbasdoc e "
				+ " on b.cinvbasid = e.pk_invbasdoc where b.cinvbasid in("
				+ pk_mets
				+ ") and b.cgeneralhid in ("
				+ pks
				+ ") "
				+ " and nvl(b.dr, 0) = 0 group by s.storname, d.bodyname, e.invname, b.pk_corp,b.cinvbasid,e.invspec ";

		List<Map<String, Object>> dlist = (List<Map<String, Object>>) this
				.getResultListMap(sqla);

		if (dlist != null && dlist.size() > 0) {
			for (int i = 0; i < dlist.size(); i++) {
				Map<String, Object> map = dlist.get(i);
				String pk = map.get("pk_corp").toString() + "*|*"
						+ map.get("bodyname").toString() + "*|*"
						+ map.get("storname").toString() + "*|*"
						+ map.get("invname").toString() + "*|*"
						+ map.get("cinvbasid").toString();

				spec.put(map.get("cinvbasid").toString(), map.get("invspec"));
				map1.put(pk, map.get("outcl"));
			}
		}

		String sqlb = "select sum(nvl(b.ninnum,0))-sum(nvl(b.noutnum,0)) ckl,s.storname,d.bodyname,e.invname,b.pk_corp,b.cinvbasid,e.invspec "
				+ " from ic_general_b b left join bd_stordoc s on b.cbodywarehouseid=s.pk_stordoc "
				+ " left join bd_calbody d on b.pk_bodycalbody=d.pk_calbody left join bd_invbasdoc e "
				+ " on b.cinvbasid=e.pk_invbasdoc where b.cinvbasid in("
				+ pk_mets
				+ ") and  b.dbizdate<='"
				+ billdate
				+ "'  and nvl(b.dr,0)=0 group by s.storname,d.bodyname,e.invname,b.pk_corp,b.cinvbasid,e.invspec ";

		List<Map<String, Object>> elist = (List<Map<String, Object>>) dao2
				.executeQuery(sqlb, new MapListProcessor());

		if (elist != null && elist.size() > 0) {
			for (int i = 0; i < elist.size(); i++) {
				Map<String, Object> map = elist.get(i);
				String pk = map.get("pk_corp").toString() + "*|*"
						+ map.get("bodyname").toString() + "*|*"
						+ map.get("storname").toString() + "*|*"
						+ map.get("invname").toString() + "*|*"
						+ map.get("cinvbasid").toString();

				map2.put(pk, map.get("ckl"));
			}
		}

		// 对比数据
		for (String key : map1.keySet()) {
			if (BFPubTool.getUFDouble_NullAsZero(map1.get(key)).compareTo(
					BFPubTool.getUFDouble_NullAsZero(map2.get(key))) > 0) {// 出库大于库存
				String[] ss = key.split("\\*\\|\\*");
				Object[] str = new Object[ss.length + 2];
				for (int i = 0; i < ss.length - 1; i++) {
					str[i] = ss[i];
				}
				str[ss.length - 1] = spec.get(ss[4]);
				str[ss.length] = map1.get(key);
				str[ss.length + 1] = map2.get(key) == null ? 0 : map2.get(key);

				list.add(str);
			}
		}

		return list;
	}

	/**
	 * 库存批改查询库存量
	 */
	private ArrayList<Object[]> batchCheckQueryOnHand(String[] pkpurIns)
			throws Exception {

		ArrayList<Object[]> list = new ArrayList<Object[]>();// 存放最终数据
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		if (pkpurIns == null || pkpurIns.length <= 0) {
			return null;
		}
		String pks = "";
		for (String inpk : pkpurIns) {
			pks += "'" + inpk + "',";
		}
		pks = pks.substring(0, pks.lastIndexOf(","));

		String sql = "select max(dbilldate) from ic_general_h where nvl(dr,0)=0 and cgeneralhid in("
				+ pks + ")";

		String billdate = (String) dao.executeQuery(sql, new ColumnProcessor());

		String sqlm = "select distinct cinvbasid from ic_general_b where nvl(dr,0)=0 and cgeneralhid in("
				+ pks + ")";

		ArrayList<String> metlist = (ArrayList<String>) dao.executeQuery(sqlm,
				new ColumnListProcessor());
		String pk_material = "";
		for (int i = 0; i < metlist.size(); i++) {
			pk_material += "'" + metlist.get(i) + "',";
		}
		pk_material = pk_material.substring(0, pk_material.lastIndexOf(","));

		String sqlb = "select sum(nvl(b.ninnum,0))-sum(nvl(b.noutnum,0)) ckl,s.storname,d.bodyname,e.invname,b.pk_corp,b.cinvbasid,e.invspec "
				+ " from ic_general_b b left join bd_stordoc s on b.cbodywarehouseid=s.pk_stordoc "
				+ " left join bd_calbody d on b.pk_bodycalbody=d.pk_calbody left join bd_invbasdoc e "
				+ " on b.cinvbasid=e.pk_invbasdoc where b.cinvbasid in("
				+ pk_material
				+ ") and  b.dbizdate<='"
				+ billdate
				+ "' and nvl(b.dr,0)=0 group by s.storname,d.bodyname,e.invname,b.pk_corp,b.cinvbasid,e.invspec ";

		List<Map<String, Object>> elist = (List<Map<String, Object>>) dao
				.executeQuery(sqlb, new MapListProcessor());

		if (elist != null && elist.size() > 0) {
			for (int i = 0; i < elist.size(); i++) {
				Map<String, Object> map = elist.get(i);
				if (BFPubTool.getUFDouble_NullAsZero(map.get("ckl")).compareTo(
						new UFDouble(0)) < 0) {// 库存不足
					Object[] obj = new Object[6];
					obj[0] = map.get("pk_corp");
					obj[1] = map.get("bodyname");
					obj[2] = map.get("storname");
					obj[3] = map.get("invname");
					obj[4] = map.get("invspec");
					obj[5] = map.get("ckl");
					list.add(obj);
				}
			}
		}

		return list;
	}

	/**
	 * 发票税率更改
	 */
	public void batchInvoiceTaxRate(String[] pks, UFDouble taxRate, Integer it)
			throws Exception {
		String cinvoiceid = "";

		for (String pk : pks) {
			cinvoiceid += "'" + pk + "',";
		}
		cinvoiceid = cinvoiceid.substring(0, cinvoiceid.lastIndexOf(","));

		UFDouble sl = taxRate.div(new UFDouble(100));// 税率
		UFDouble hjs = new UFDouble(1).add(sl);// 合计税

		String sql = "";
		if (it == BxgtStepButton.SLXG) {
			sql = "update po_invoice_b b set b.ntaxrate=" + taxRate
					+ ",b.noriginalcurprice=b.norgnettaxprice/" + hjs
					+ ",b.nmoney=b.nsummny/" + hjs
					+ ",b.noriginalcurmny=b.nsummny/" + hjs
					+ ",b.noriginaltaxmny=b.nsummny*" + sl + "/" + hjs
					+ ",b.ntaxmny=b.nsummny*" + sl + "/" + hjs
					+ " where b.cinvoiceid in(" + cinvoiceid
					+ ") and nvl(b.dr,0)=0 ";
		} else if (it == BxgtStepButton.TBFP) {
			sql = "update po_invoice_b b set b.ntaxrate=" + taxRate
					+ ",b.noriginalcurprice=b.norgnettaxprice/" + hjs
					+ ",b.nmoney=b.nsummny/" + hjs
					+ ",b.noriginalcurmny=b.nsummny/" + hjs
					+ ",b.noriginaltaxmny=b.nsummny*" + sl + "/" + hjs
					+ ",b.ntaxmny=b.nsummny*" + sl + "/" + hjs
					+ " where b.cinvoiceid in(" + cinvoiceid
					+ ") and nvl(b.dr,0)=0 and b.ntaxrate=0 ";
		}

		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		dao.executeUpdate(sql);
	}

	/**
	 * 广东销售客户金额批改
	 */
	public void batchCustAndMoney(String[] pks, Object obj1, Object obj2)
			throws Exception {

		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		ArrayList<String> upsqls = new ArrayList<String>();

		String pk_sale = "";
		for (String pk : pks) {
			pk_sale += "'" + pk + "',";
		}
		pk_sale = pk_sale.substring(0, pk_sale.lastIndexOf(","));

		if (obj1 != null && !"".equals(obj1)) {
			String sql = "update so_sale set ccustomerid='" + obj1.toString()
					+ "' where csaleid in(" + pk_sale + ")";

			String sql2 = "update arap_djfb set hbbm=(select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
					+ obj1.toString()
					+ "'),"
					+ " ordercusmandoc=(select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
					+ obj1.toString()
					+ "') where ddlx in("
					+ pk_sale
					+ ") and djlxbm='D2' ";
			String sql3 = "update ic_general_h set ccustomerid='"
					+ obj1.toString()
					+ "',pk_cubasdocc=(select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
					+ obj1.toString()
					+ "') where cgeneralhid in(select distinct cgeneralhid from ic_general_b where csourcebillhid in("
					+ pk_sale + ") )";
			upsqls.add(sql);
			upsqls.add(sql2);
			upsqls.add(sql3);
		}
		if (obj2 != null && !"".equals(obj2)) {

			pk_sale = pk_sale.replaceAll("'", "");
			UFDouble newmny = new UFDouble(obj2.toString());
			// 取出原先的合计总值，后续要用
			String sql = "select a.csaleid,a.nheadsummny, sum(b.nnumber) nnumber from so_sale a "
					+ " left join so_saleorder_b b on a.csaleid=b.csaleid where a.csaleid ='"
					+ pk_sale
					+ "' "
					+ " and nvl(b.dr,0)=0 group by a.csaleid,a.nheadsummny ";
			Map<String, Object> map = (Map<String, Object>) dao.executeQuery(
					sql, new MapProcessor());
			UFDouble oldmny = new UFDouble(map.get("nheadsummny").toString());
			UFDouble oldnum = new UFDouble(map.get("nnumber").toString());
			UFDouble price = newmny.div(oldnum);
			// 销售订单主表
			String sql1 = "update so_sale set nheadsummny=" + newmny
					+ " where csaleid ='" + pk_sale + "' ";
			// 销售订单子表
			String sql2 = "update so_saleorder_b b set b.nmny=b.nnumber*"
					+ price + ",b.noriginalcurmny=b.nnumber*" + price + ","
					+ " b.noriginalcursummny=b.nnumber*" + price
					+ ",b.nsummny=b.nnumber*" + price + "," + " b.nnetprice="
					+ price + ",b.norgqtnetprc=" + price + ",b.norgqtprc="
					+ price + ",b.norgqttaxnetprc=" + price + ","
					+ " b.norgqttaxprc=" + price + ",b.noriginalcurnetprice="
					+ price + ",b.noriginalcurprice=" + price
					+ ",b.noriginalcurtaxnetprice=" + price + ","
					+ " b.noriginalcurtaxprice=" + price + ",b.nprice=" + price
					+ ",b.nqtnetprc=" + price + ",b.nqtprc=" + price + ","
					+ "b.nqttaxnetprc=" + price + ",b.nqttaxprc=" + price
					+ ",b.ntaxnetprice=" + price + ",b.ntaxprice=" + price
					+ " where b.csaleid='" + pk_sale + "' and nvl(b.dr,0)=0 ";
			// 收款单主表
			String sql3 = "update arap_djzb set bbje="
					+ newmny
					+ ",ybje="
					+ newmny
					+ " where vouchid =(select distinct vouchid from arap_djfb where ddlx='"
					+ pk_sale + "' and djlxbm='D2' and nvl(dr,0)=0 ) ";
			// 收款单子表
			String sql4 = "update arap_djfb set bbye=" + newmny + ",dfbbje="
					+ newmny + ",dfbbwsje=" + newmny + ",dfybje=" + newmny
					+ "," + "dfybwsje=" + newmny + ",occupationmny=" + newmny
					+ ",ybye=" + newmny + " where ddlx='" + pk_sale
					+ "' and djlxbm='D2' and nvl(dr,0)=0 and bbye=" + oldmny
					+ " ";
			// 销售出库子表
			String sql5 = "update ic_general_b b set b.nmny=b.noutnum*" + price
					+ ",b.nquotemny=b.noutnum*" + price
					+ ",b.nquotentmny=b.noutnum*" + price
					+ ",b.ntaxmny=b.noutnum*" + price
					+ ",b.nsalemny=b.noutnum*" + price + "," + "b.nprice="
					+ price + ",b.nquotentprice=" + price + ",b.nquoteprice="
					+ price + "," + "b.nsaleprice=" + price + ",b.ntaxprice="
					+ price
					+ " where b.cbodybilltypecode='4C' and nvl(b.dr,0)=0 "
					+ " and b.cfirstbillhid ='" + pk_sale + "' ";
			// 产成品入库和ia_bill不涉及金钱

			upsqls.add(sql1);
			upsqls.add(sql2);
			upsqls.add(sql3);
			upsqls.add(sql4);
			upsqls.add(sql5);
		}
		// 获取销售收款单
		String[] vouchids = this.getSKpksB(pks, BxgtStepButton.DESIGN_B);
		// 获取销售出库单
		String[] cgeneralhids = this.getCSaleOutByCsaleid(pks,
				BxgtStepButton.DESIGN_B);
		// 往打印表里面插入主键
		this.printAfterBatch(pks, vouchids, cgeneralhids, null,
				BxgtStepButton.GD_FLOW);

		dmo.execDatas(upsqls.toArray(new String[0]));

	}

	/**
	 * 基本档案同步逻辑
	 */
	public LinkedHashMap<String, Object> synBaseDoc() throws Exception {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		/*
		 * UFDateTime billtime = new UFDateTime(new Date()); UFDate billdate =
		 * new UFDate(billtime.toString().substring(0, 10)); BaseDAO dao1 = new
		 * BaseDAO(BxgtStepButton.DESIGN_A); BaseDAO dao2 = new
		 * BaseDAO(BxgtStepButton.DESIGN_B); // ①所有组织 // 销售组织 String sql1 =
		 * "select * from bd_salestru where nvl(dr,0)=0 and ts>(select
		 * max(lasttime) from bxgt_basetime) "; List<SalestruVO> list1 = (List<SalestruVO>)
		 * dao1.executeQuery(sql1, new BeanListProcessor(SalestruVO.class)); if
		 * (list1 != null && list1.size() > 0) { String pks[] = new
		 * String[list1.size()]; for (int i = 0; i < list1.size(); i++) {
		 * list1.get(i).setCreatedate(billdate); list1.get(i).setTs(billtime);
		 * pks[i] = list1.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(SalestruVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list1.toArray(new SalestruVO[0])); }
		 * map.put("销售组织", list1.size()); // 1 // 库存组织 String sql2 = "select *
		 * from bd_calbody where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<CalbodyVO> list2 = (List<CalbodyVO>)
		 * dao1.executeQuery(sql2, new BeanListProcessor(CalbodyVO.class)); if
		 * (list2 != null && list2.size() > 0) { String pks[] = new
		 * String[list2.size()]; for (int i = 0; i < list2.size(); i++) {
		 * list2.get(i).setCreatedate(billdate); list2.get(i).setTs(billtime);
		 * pks[i] = list2.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CalbodyVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list2.toArray(new CalbodyVO[0])); }
		 * map.put("库存组织", list2.size()); // 2 // 采购组织 String sql3 = "select *
		 * from bd_purorg where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<PurorgVO> list3 = (List<PurorgVO>)
		 * dao1.executeQuery(sql3, new BeanListProcessor(PurorgVO.class)); if
		 * (list3 != null && list3.size() > 0) { String pks[] = new
		 * String[list3.size()]; for (int i = 0; i < list3.size(); i++) {
		 * list3.get(i).setCreatedate(billdate); list3.get(i).setTs(billtime);
		 * pks[i] = list3.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(PurorgVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list3.toArray(new PurorgVO[0])); }
		 * map.put("采购组织", list3.size()); // 3 // 部门档案 String sql4 = "select *
		 * from bd_deptdoc where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<DeptVO> list4 = (List<DeptVO>)
		 * dao1.executeQuery(sql4, new BeanListProcessor(DeptVO.class)); if
		 * (list4 != null && list4.size() > 0) { String pks[] = new
		 * String[list4.size()]; for (int i = 0; i < list4.size(); i++) {
		 * list4.get(i).setCreatedate(billdate); list4.get(i).setTs(billtime);
		 * pks[i] = list4.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(DeptVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list4.toArray(new DeptVO[0])); }
		 * map.put("部门", list4.size()); // 4 // 公司档案 String sql5 = "select *
		 * from bd_corp where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<CorpVO> list5 = (List<CorpVO>)
		 * dao1.executeQuery(sql5, new BeanListProcessor(CorpVO.class)); if
		 * (list5 != null && list5.size() > 0) { String pks[] = new
		 * String[list5.size()]; for (int i = 0; i < list5.size(); i++) {
		 * list5.get(i).setCreatedate(billdate); list5.get(i).setTs(billtime);
		 * pks[i] = list5.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CorpVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list5.toArray(new CorpVO[0])); }
		 * map.put("公司", list5.size()); // 5 // 仓库档案 String sql6 = "select
		 * pk_stordoc from bd_stordoc where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; Object obj6 =
		 * dao1.executeQuery(sql6, new ColumnPKsProcessor()); int m_6 = 0;//
		 * 记录更新数量 int n_6 = 0;// 记录插入数量 if (obj6 != null && !"".equals(obj6)) { //
		 * 校验B中存在的主键 String sql6_1 = "select pk_stordoc from bd_stordoc where
		 * nvl(dr,0)=0 and pk_stordoc in(" + obj6.toString() + ")"; Object
		 * obj6_1 = dao2.executeQuery(sql6_1, new ColumnPKsProcessor()); String
		 * sql6_2 = "";// 更新sql String sql6_3 = "";// 插入sql if (obj6_1 != null &&
		 * !"".equals(obj6_1)) {// 有更新数据 sql6_2 = "select * from bd_stordoc
		 * where nvl(dr,0)=0 and pk_stordoc in(" + obj6_1.toString() + ")";
		 * sql6_3 = sql6.replaceFirst("pk_stordoc", "\\*") + " and pk_stordoc
		 * not in(" + obj6_1.toString() + ")"; } else { sql6_3 =
		 * sql6.replaceFirst("pk_stordoc", "\\*"); } if (!"".equals(sql6_2)) {//
		 * 有更新数据 List<StordocVO> list6_2 = (List<StordocVO>)
		 * dao1.executeQuery( sql6_2, new BeanListProcessor(StordocVO.class));
		 * dao2.updateVOArray(list6_2.toArray(new StordocVO[0])); m_6 =
		 * list6_2.size(); }
		 * 
		 * List<StordocVO> list6_3 = (List<StordocVO>) dao1.executeQuery(
		 * sql6_3, new BeanListProcessor(StordocVO.class)); n_6 =
		 * list6_3.size(); if (list6_3 != null && list6_3.size() > 0) {
		 * dao2.insertVOArrayWithPK(list6_3.toArray(new StordocVO[0])); } }
		 * map.put("仓库", m_6 + n_6); // 6 // 货位档案 String sql7 = "select * from
		 * bd_cargdoc where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<CargdocVO> list7 = (List<CargdocVO>)
		 * dao1.executeQuery(sql7, new BeanListProcessor(CargdocVO.class)); if
		 * (list7 != null && list7.size() > 0) { String pks[] = new
		 * String[list7.size()]; for (int i = 0; i < list7.size(); i++) { pks[i] =
		 * list7.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CargdocVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list7.toArray(new CargdocVO[0])); }
		 * map.put("货位", list7.size()); // 7 // 人员分类 String sql8 = "select
		 * pk_psncl from bd_psncl where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; Object obj8 =
		 * dao1.executeQuery(sql8, new ColumnPKsProcessor()); int m_8 = 0;//
		 * 记录更新数量 int n_8 = 0;// 记录插入数量 if (obj8 != null && !"".equals(obj8)) { //
		 * 校验B中存在的主键 String sql8_1 = "select pk_psncl from bd_psncl where
		 * nvl(dr,0)=0 and pk_psncl in(" + obj8.toString() + ")"; Object obj8_1 =
		 * dao2.executeQuery(sql8_1, new ColumnPKsProcessor()); String sql8_2 =
		 * "";// 更新sql String sql8_3 = "";// 插入sql if (obj8_1 != null &&
		 * !"".equals(obj8_1)) {// 有更新数据 sql8_2 = "select * from bd_psncl where
		 * nvl(dr,0)=0 and pk_psncl in(" + obj8_1.toString() + ")"; sql8_3 =
		 * sql8.replaceFirst("pk_psncl", "\\*") + " and pk_psncl not in(" +
		 * obj8_1.toString() + ")"; } else { sql8_3 =
		 * sql8.replaceFirst("pk_psncl", "\\*"); } if (!"".equals(sql8_2)) {//
		 * 有更新数据 List<PsnclVO> list8_2 = (List<PsnclVO>) dao1.executeQuery(
		 * sql8_2, new BeanListProcessor(PsnclVO.class));
		 * dao2.updateVOArray(list8_2.toArray(new PsnclVO[0])); m_8 =
		 * list8_2.size(); }
		 * 
		 * List<PsnclVO> list8_3 = (List<PsnclVO>) dao1.executeQuery(sql8_3,
		 * new BeanListProcessor(PsnclVO.class)); n_8 = list8_3.size(); if
		 * (list8_3 != null && list8_3.size() > 0) {
		 * dao2.insertVOArrayWithPK(list8_3.toArray(new PsnclVO[0])); } }
		 * map.put("人员分类", m_8 + n_8); // 8 // 人员档案 String sql9 = "select * from
		 * bd_psndoc where nvl(dr,0)=0 and ts > (select max(lasttime) from
		 * bxgt_basetime) "; List<PsndocVO> list9 = (List<PsndocVO>)
		 * dao1.executeQuery(sql9, new BeanListProcessor(PsndocVO.class)); if
		 * (list9 != null && list9.size() > 0) { String pks[] = new
		 * String[list9.size()]; for (int i = 0; i < list9.size(); i++) { pks[i] =
		 * list9.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(PsndocVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list9.toArray(new PsndocVO[0])); }
		 * map.put("人员档案", list9.size()); // 9 // 人员基本档案 String sql10 = "select *
		 * from bd_psnbasdoc where nvl(dr,0)=0 and ts > (select max(lasttime)
		 * from bxgt_basetime) "; List<PsnbasdocVO> list10 = (List<PsnbasdocVO>)
		 * dao1.executeQuery(sql10, new BeanListProcessor(PsnbasdocVO.class));
		 * if (list10 != null && list10.size() > 0) { String pks[] = new
		 * String[list10.size()]; for (int i = 0; i < list10.size(); i++) {
		 * pks[i] = list10.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(PsnbasdocVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list10.toArray(new PsnbasdocVO[0])); }
		 * map.put("人员基本档案", list10.size()); // 10 // 地区档案 String sql11 =
		 * "select * from bd_areacl where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<AreaclVO> list11 = (List<AreaclVO>)
		 * dao1.executeQuery(sql11, new BeanListProcessor(AreaclVO.class)); if
		 * (list11 != null && list11.size() > 0) { String pks[] = new
		 * String[list11.size()]; for (int i = 0; i < list11.size(); i++) {
		 * pks[i] = list11.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(AreaclVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list11.toArray(new AreaclVO[0])); }
		 * map.put("地区档案", list11.size()); // 11 // 币种档案 String sql12 = "select *
		 * from bd_currtype where nvl(dr,0)=0 and ts > (select max(lasttime)
		 * from bxgt_basetime) "; List<CurrtypeVO> list12 = (List<CurrtypeVO>)
		 * dao1.executeQuery(sql12, new BeanListProcessor(CurrtypeVO.class)); if
		 * (list12 != null && list12.size() > 0) { String pks[] = new
		 * String[list12.size()]; for (int i = 0; i < list12.size(); i++) {
		 * pks[i] = list12.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CurrtypeVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list12.toArray(new CurrtypeVO[0])); }
		 * map.put("币种档案", list12.size()); // 12 // 项目管理档案 String sql13 =
		 * "select * from bd_costsubj where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<CostsubjVO> list13 = (List<CostsubjVO>)
		 * dao1.executeQuery(sql13, new BeanListProcessor(CostsubjVO.class)); if
		 * (list13 != null && list13.size() > 0) { String pks[] = new
		 * String[list13.size()]; for (int i = 0; i < list13.size(); i++) {
		 * pks[i] = list13.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CostsubjVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list13.toArray(new CostsubjVO[0])); }
		 * map.put("项目管理档案", list13.size()); // 13 // 银行账户基本信息 String sql14 =
		 * "select * from bd_bankaccbas where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<BankaccbasVO> list14 =
		 * (List<BankaccbasVO>) dao1.executeQuery( sql14, new
		 * BeanListProcessor(BankaccbasVO.class)); if (list14 != null &&
		 * list14.size() > 0) { String pks[] = new String[list14.size()]; for
		 * (int i = 0; i < list14.size(); i++) {
		 * list14.get(i).setAccopendate(billdate);
		 * list14.get(i).setCreatetime(billtime); list14.get(i).setTs(billtime);
		 * pks[i] = list14.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(BankaccbasVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list14.toArray(new BankaccbasVO[0])); }
		 * map.put("银行账户基本信息", list14.size()); // 14 // 银行类别 String sql15 =
		 * "select * from bd_banktype where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<BankTypeVO> list15 = (List<BankTypeVO>)
		 * dao1.executeQuery(sql15, new BeanListProcessor(BankTypeVO.class)); if
		 * (list15 != null && list15.size() > 0) { String pks[] = new
		 * String[list15.size()]; for (int i = 0; i < list15.size(); i++) {
		 * list15.get(i).setCreatetime(billtime); list15.get(i).setTs(billtime);
		 * pks[i] = list15.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(BankTypeVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list15.toArray(new BankTypeVO[0])); }
		 * map.put("银行类别", list15.size()); // 15 // 客户基本档案 String sql16 =
		 * "select pk_cubasdoc from bd_cubasdoc where nvl(dr,0)=0 and ts >
		 * (select max(lasttime) from bxgt_basetime) "; Object obj16 =
		 * dao1.executeQuery(sql16, new ColumnPKsProcessor()); int m_16 = 0;//
		 * 记录更新数量 int n_16 = 0;// 记录插入数量 if (obj16 != null && !"".equals(obj16)) { //
		 * 校验B中存在的主键 String sql16_1 = "select pk_cubasdoc from bd_cubasdoc where
		 * nvl(dr,0)=0 and pk_cubasdoc in(" + obj16.toString() + ")"; Object
		 * obj16_1 = dao2.executeQuery(sql16_1, new ColumnPKsProcessor());
		 * String sql16_2 = "";// 更新sql String sql16_3 = "";// 插入sql if (obj16_1 !=
		 * null && !"".equals(obj16_1)) {// 有更新数据 sql16_2 = "select * from
		 * bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in(" +
		 * obj16_1.toString() + ")"; sql16_3 = sql16.replaceFirst("pk_cubasdoc",
		 * "\\*") + " and pk_cubasdoc not in(" + obj16_1.toString() + ")"; }
		 * else { sql16_3 = sql16.replaceFirst("pk_cubasdoc", "\\*"); } if
		 * (!"".equals(sql16_2)) {// 有更新数据 List<CubasdocVO> list16_2 = (List<CubasdocVO>)
		 * dao1 .executeQuery(sql16_2, new BeanListProcessor(
		 * CubasdocVO.class)); for (CubasdocVO vo : list16_2) {
		 * vo.setModifytime(billtime.toString());
		 * vo.setCreatetime(billtime.toString()); vo.setTs(billtime.toString()); }
		 * dao2.updateVOArray(list16_2.toArray(new CubasdocVO[0])); m_8 =
		 * list16_2.size(); }
		 * 
		 * List<CubasdocVO> list16_3 = (List<CubasdocVO>) dao1.executeQuery(
		 * sql16_3, new BeanListProcessor(CubasdocVO.class)); n_8 =
		 * list16_3.size(); if (list16_3 != null && list16_3.size() > 0) { for
		 * (CubasdocVO vo : list16_3) { vo.setCreatetime(billtime.toString());
		 * vo.setTs(billtime.toString()); }
		 * dao2.insertVOArrayWithPK(list16_3.toArray(new CubasdocVO[0])); } }
		 * map.put("客户基本档案", m_16 + n_16); // 16 // 客商管理档案 String sql17 =
		 * "select * from bd_cumandoc where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<CumandocVO> list17 = (List<CumandocVO>)
		 * dao1.executeQuery(sql17, new BeanListProcessor(CumandocVO.class)); if
		 * (list17 != null && list17.size() > 0) { String pks[] = new
		 * String[list17.size()]; for (int i = 0; i < list17.size(); i++) {
		 * list17.get(i).setCreatetime(billtime.toString());
		 * list17.get(i).setModifytime(billtime.toString());
		 * list17.get(i).setDevelopdate(billdate.toString());
		 * list17.get(i).setTs(billtime.toString()); pks[i] =
		 * list17.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(CumandocVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list17.toArray(new CumandocVO[0])); }
		 * map.put("客商管理档案", list17.size()); // 17 // 单位基本档案 String sql18 =
		 * "select pk_measdoc from bd_measdoc where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; Object obj18 =
		 * dao1.executeQuery(sql18, new ColumnPKsProcessor()); int m_18 = 0;//
		 * 记录更新数量 int n_18 = 0;// 记录插入数量 if (obj18 != null && !"".equals(obj18)) { //
		 * 校验B中存在的主键 String sql18_1 = "select pk_measdoc from bd_measdoc where
		 * nvl(dr,0)=0 and pk_measdoc in(" + obj18.toString() + ")"; Object
		 * obj18_1 = dao2.executeQuery(sql18_1, new ColumnPKsProcessor());
		 * String sql18_2 = "";// 更新sql String sql18_3 = "";// 插入sql if (obj18_1 !=
		 * null && !"".equals(obj18_1)) {// 有更新数据 sql18_2 = "select * from
		 * bd_measdoc where nvl(dr,0)=0 and pk_measdoc in(" + obj18_1.toString() +
		 * ")"; sql18_3 = sql18.replaceFirst("pk_measdoc", "\\*") + " and
		 * pk_measdoc not in(" + obj18_1.toString() + ")"; } else { sql18_3 =
		 * sql18.replaceFirst("pk_measdoc", "\\*"); } if (!"".equals(sql18_2))
		 * {// 有更新数据 List<MeasdocVO> list18_2 = (List<MeasdocVO>)
		 * dao1.executeQuery( sql18_2, new BeanListProcessor(MeasdocVO.class));
		 * dao2.updateVOArray(list18_2.toArray(new MeasdocVO[0])); m_18 =
		 * list18_2.size(); }
		 * 
		 * List<MeasdocVO> list18_3 = (List<MeasdocVO>) dao1.executeQuery(
		 * sql18_3, new BeanListProcessor(MeasdocVO.class)); n_18 =
		 * list18_3.size(); if (list18_3 != null && list18_3.size() > 0) {
		 * dao2.insertVOArrayWithPK(list18_3.toArray(new MeasdocVO[0])); } }
		 * map.put("计量单位档案", m_18 + n_18); // 18 // 存货分类档案 String sql19 =
		 * "select pk_invcl from bd_invcl where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; Object obj19 =
		 * dao1.executeQuery(sql19, new ColumnPKsProcessor()); int m_19 = 0;//
		 * 记录更新数量 int n_19 = 0;// 记录插入数量 if (obj19 != null && !"".equals(obj19)) { //
		 * 校验B中存在的主键 String sql19_1 = "select pk_invcl from bd_invcl where
		 * nvl(dr,0)=0 and pk_invcl in(" + obj19.toString() + ")"; Object
		 * obj19_1 = dao2.executeQuery(sql19_1, new ColumnPKsProcessor());
		 * String sql19_2 = "";// 更新sql String sql19_3 = "";// 插入sql if (obj19_1 !=
		 * null && !"".equals(obj19_1)) {// 有更新数据 sql19_2 = "select * from
		 * bd_invcl where nvl(dr,0)=0 and pk_invcl in(" + obj19_1.toString() +
		 * ")"; sql19_3 = sql19.replaceFirst("pk_invcl", "\\*") + " and pk_invcl
		 * not in(" + obj19_1.toString() + ")"; } else { sql19_3 =
		 * sql19.replaceFirst("pk_invcl", "\\*"); } if (!"".equals(sql19_2)) {//
		 * 有更新数据 List<InvclVO> list19_2 = (List<InvclVO>) dao1.executeQuery(
		 * sql19_2, new BeanListProcessor(InvclVO.class));
		 * dao2.updateVOArray(list19_2.toArray(new InvclVO[0])); m_19 =
		 * list19_2.size(); }
		 * 
		 * List<InvclVO> list19_3 = (List<InvclVO>) dao1.executeQuery(sql19_3,
		 * new BeanListProcessor(InvclVO.class)); n_19 = list19_3.size(); if
		 * (list19_3 != null && list19_3.size() > 0) {
		 * dao2.insertVOArrayWithPK(list19_3.toArray(new InvclVO[0])); } }
		 * map.put("存货分类档案", m_19 + n_19); // 19 // 存货基本档案 String sql20 =
		 * "select pk_invbasdoc from bd_invbasdoc where nvl(dr,0)=0 and ts >
		 * (select max(lasttime) from bxgt_basetime) "; Object obj20 =
		 * dao1.executeQuery(sql20, new ColumnPKsProcessor()); int m_20 = 0;//
		 * 记录更新数量 int n_20 = 0;// 记录插入数量 if (obj20 != null && !"".equals(obj20)) { //
		 * 校验B中存在的主键 String sql20_1 = "select pk_invbasdoc from bd_invbasdoc
		 * where nvl(dr,0)=0 and pk_invbasdoc in(" + obj20.toString() + ")";
		 * Object obj20_1 = dao2.executeQuery(sql20_1, new
		 * ColumnPKsProcessor()); String sql20_2 = "";// 更新sql String sql20_3 =
		 * "";// 插入sql if (obj20_1 != null && !"".equals(obj20_1)) {// 有更新数据
		 * sql20_2 = "select * from bd_invbasdoc where nvl(dr,0)=0 and
		 * pk_invbasdoc in(" + obj20_1.toString() + ")"; sql20_3 =
		 * sql20.replaceFirst("pk_invbasdoc", "\\*") + " and pk_invbasdoc not
		 * in(" + obj20_1.toString() + ")"; } else { sql20_3 =
		 * sql20.replaceFirst("pk_invbasdoc", "\\*"); } if (!"".equals(sql20_2))
		 * {// 有更新数据 List<InvbasdocVO> list20_2 = (List<InvbasdocVO>) dao1
		 * .executeQuery(sql20_2, new BeanListProcessor( InvbasdocVO.class));
		 * for (InvbasdocVO vo : list20_2) { vo.setCreatetime(billtime);
		 * vo.setModifier(null); vo.setModifytime(null); vo.setTs(billtime); }
		 * dao2.updateVOArray(list20_2.toArray(new InvbasdocVO[0])); m_20 =
		 * list20_2.size(); }
		 * 
		 * List<InvbasdocVO> list20_3 = (List<InvbasdocVO>) dao1.executeQuery(
		 * sql20_3, new BeanListProcessor(InvbasdocVO.class)); n_20 =
		 * list20_3.size(); if (list20_3 != null && list20_3.size() > 0) { for
		 * (InvbasdocVO vo : list20_3) { vo.setCreatetime(billtime);
		 * vo.setModifier(null); vo.setModifytime(null); vo.setTs(billtime); }
		 * dao2.insertVOArrayWithPK(list20_3.toArray(new InvbasdocVO[0])); } }
		 * map.put("存货基本档案", m_20 + n_20); // 20 // 存货管理档案 String sql21 =
		 * "select * from bd_invmandoc where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<InvmandocVO> list21 =
		 * (List<InvmandocVO>) dao1.executeQuery(sql21, new
		 * BeanListProcessor(InvmandocVO.class)); if (list21 != null &&
		 * list21.size() > 0) { String pks[] = new String[list21.size()]; for
		 * (int i = 0; i < list21.size(); i++) {
		 * list21.get(i).setCreatetime(billtime);
		 * list21.get(i).setModifytime(null); list21.get(i).setModifier(null);
		 * list21.get(i).setTs(billtime); pks[i] =
		 * list21.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(InvmandocVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list21.toArray(new InvmandocVO[0])); }
		 * map.put("存货管理档案", list21.size()); // 21 // 存货生产档案 String sql22 =
		 * "select * from bd_produce where nvl(dr,0)=0 and ts > (select
		 * max(lasttime) from bxgt_basetime) "; List<ProduceVO> list22 = (List<ProduceVO>)
		 * dao1.executeQuery(sql22, new BeanListProcessor(ProduceVO.class)); if
		 * (list22 != null && list22.size() > 0) { String pks[] = new
		 * String[list22.size()]; for (int i = 0; i < list22.size(); i++) {
		 * list22.get(i).setCreatetime(billtime.toString());
		 * list22.get(i).setModifytime(null); list22.get(i).setModifier(null);
		 * list22.get(i).setTs(billtime.toString()); pks[i] =
		 * list22.get(i).getPrimaryKey(); } // 插入前删除
		 * dao2.deleteByPKs(ProduceVO.class, pks); // 插入到B
		 * dao2.insertVOArrayWithPK(list22.toArray(new ProduceVO[0])); }
		 * map.put("存货生产档案", list22.size()); // 22 // 更新记录时间 String sqlinsert =
		 * "insert into bxgt_basetime(lasttime) values" + " ((select
		 * to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||'' from dual)) ";
		 * dao1.executeUpdate(sqlinsert);
		 */

		return map;
	}

	/**
	 * 获取pk,只为查询b帐是否存在
	 * 
	 * @author tcl
	 */
	private class ColumnPKsProcessor implements ResultSetProcessor {

		public Object handleResultSet(ResultSet rs) throws SQLException {
			String pks = "";
			while (rs.next()) {
				pks += "'" + rs.getString(1) + "',";
			}
			if (!"".equals(pks)) {
				return pks.substring(0, pks.lastIndexOf(","));
			}
			return pks;
		}
	}

	public void updateBasedoc(String lastts, String currts) throws Exception {
		String billdate = currts.substring(0, 10);
		CommonDataDMO dmo = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		ArrayList<String> sqllist = new ArrayList<String>();

		// 参照档案
		String sqldef1 = "update bd_defdef set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "'";
		sqllist.add(sqldef1);

		String sqldef2 = "update bd_defdoc set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "'";
		sqllist.add(sqldef2);

		String sqldef3 = "update bd_defdoclist set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "'";
		sqllist.add(sqldef3);

		String sqldef4 = "update bd_defquote set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "'";
		sqllist.add(sqldef4);

		// 基本档案
		String sql1 = "update bd_salestru set createdate=null,sealdate=null,ts='"
				+ currts + "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql1);// 销售组织

		String sql2 = "update bd_calbody set createdate='" + billdate
				+ "',ts='" + currts + "' where nvl(dr,0)=0 and ts>='" + lastts
				+ "' ";
		sqllist.add(sql2);// 库存组织

		String sql3 = "update bd_purorg set createdate='" + billdate + "',ts='"
				+ currts + "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql3);// 采购组织

		String sql4 = "update bd_deptdoc set createdate='" + billdate
				+ "',ts='" + currts + "' where nvl(dr,0)=0 and ts>='" + lastts
				+ "' ";
		sqllist.add(sql4);// 部门

		String sql5 = "update bd_corp set createdate=null,sealeddate=null,ts='"
				+ currts + "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql5);// 公司

		String sql6 = "update bd_stordoc set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql6);// 仓库

		String sql7 = "update bd_cargdoc set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql7);// 货位

		String sql8 = "update bd_psncl set createdate=null,ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql8);// 人员分类

		String sql9 = "update bd_psndoc set sealdate=null,ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql9);// 人员信息

		String sql10 = "update bd_psnbasdoc set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql10);// 人员基本

		String sql11 = "update bd_areacl set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql11);// 地区分类

		String sql12 = "update bd_currtype set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql12);// 币种

		String sql13 = "update bd_costsubj set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql13);// 项目管理

		String sql14 = "update bd_bankaccbas set modifier=null,modifytime=null, accopendate='"
				+ billdate
				+ "',createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql14);// 银行账户

		String sql15 = "update bd_banktype set modifier=null,modifytime=null, createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='"
				+ lastts + "' ";
		sqllist.add(sql15);// 银行类别

		String sql16 = "update bd_cubasdoc set modifier=null,modifytime=null, createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='"
				+ lastts + "' ";
		sqllist.add(sql16);// 客户基本档案

		String sql17 = "update bd_cumandoc set modifier=null,modifytime=null,developdate='"
				+ billdate
				+ "', createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql17);// 客商管理档案

		String sql18 = "update bd_measdoc set ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql18);// 计量单位

		String sql19 = "update bd_invcl set sealdate=null,ts='" + currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql19);// 存货分类

		String sql20 = "update bd_invbasdoc set modifier=null,modifytime=null, createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='"
				+ lastts + "' ";
		sqllist.add(sql20);// 存货基本档案

		String sql21 = "update bd_invmandoc set modifier=null,modifytime=null,sealdate='"
				+ billdate
				+ "', createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql21);// 存货管理档案

		String sql22 = "update bd_produce set modifier=null,modifytime=null,sealdate='"
				+ billdate
				+ "', createtime='"
				+ currts
				+ "', ts='"
				+ currts
				+ "' where nvl(dr,0)=0 and ts>='" + lastts + "' ";
		sqllist.add(sql22);// 生产档案

		dmo.execDatas(sqllist.toArray(new String[0]));

	}

	/**
	 * 同步开票客商
	 */
	public int synCustBasedoc(String[] custcodes, String billtime)
			throws Exception {

		if (custcodes == null) {// 表示一键同步，需要先处理
			int g = this.synCustBasedocAuto(billtime);
			return g;
		}
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String ccode = "";
		int j = 0;
		ArrayList<String> list = new ArrayList<String>();
		// 对custcodes分组
		for (int i = 0; i < custcodes.length; i++) {
			ccode += "'" + custcodes[i] + "',";
			j++;
			if (j == 100) {
				String str = ccode.substring(0, ccode.lastIndexOf(","));
				list.add(str);
				j = 0;
				ccode = "";
			}
		}
		if (j != 0) {
			String str = ccode.substring(0, ccode.lastIndexOf(","));
			list.add(str);
		}

		int m = 0;
		int n = 0;

		// 对每组里面的数据进行同步
		for (int i = 0; i < list.size(); i++) {
			String pks = list.get(i);
			// 先校验B中是否存在的编码
			String sql = "select custcode from bd_cubasdoc where nvl(dr,0)=0 and custcode in("
					+ pks + ")";
			Object obj = dao2.executeQuery(sql, new ColumnPKsProcessor());
			String sql_1 = "";
			String sql_2 = "";
			if (obj != null && !"".equals(obj.toString())) {// 有更新数据
				sql_1 = "select * from bd_cubasdoc where nvl(dr,0)=0 and custcode in("
						+ obj.toString() + ")";
				sql_2 = sql.replaceFirst("custcode", "\\*")
						+ " and custcode not in(" + obj.toString() + ")";
			} else {
				sql_2 = sql.replaceFirst("custcode", "\\*");
			}

			if (!"".equals(sql_1)) {
				List<CubasdocVO> list1 = (List<CubasdocVO>) this
						.getResultBeanList(sql_1, CubasdocVO.class);
				m = m + list1.size();// 更新数量
				for (CubasdocVO vo : list1) {
					vo.setModifier(null);
					vo.setModifytime(null);
					vo.setCreatetime(billtime);
					vo.setTs(billtime);
				}
				dao2.updateVOArray(list1.toArray(new CubasdocVO[0]));
			}
			List<CubasdocVO> list2 = (List<CubasdocVO>) this.getResultBeanList(
					sql_2, CubasdocVO.class);
			n = n + list2.size();// 插入数量
			if (list2 != null && list2.size() > 0) {
				for (CubasdocVO vo : list2) {
					vo.setModifier(null);
					vo.setModifytime(null);
					vo.setCreatetime(billtime);
					vo.setTs(billtime);
				}
				dao2.insertVOArrayWithPK(list2.toArray(new CubasdocVO[0]));
			}

			// 管理档案
			String sqlman = "select * from bd_cumandoc where nvl(dr,0)=0 and pk_cubasdoc "
					+ " in(select pk_cubasdoc from bd_cubasdoc where nvl(dr,0)=0 and custcode in("
					+ pks + "))";
			List<CumandocVO> list3 = (List<CumandocVO>) this.getResultBeanList(
					sqlman, CumandocVO.class);

			if (list3 != null && list3.size() > 0) {
				String[] pkmans = new String[list3.size()];
				for (int k = 0; k < list3.size(); k++) {
					list3.get(k).setCreatetime(billtime);
					list3.get(k).setModifytime(null);
					list3.get(k).setModifier(null);
					list3.get(k).setDevelopdate(billtime.substring(0, 10));
					list3.get(k).setTs(billtime.toString());
					pkmans[k] = list3.get(k).getPrimaryKey();
				}
				dao2.deleteByPKs(CumandocVO.class, pkmans);// 删除

				dao2.insertVOArrayWithPK(list3.toArray(new CumandocVO[0]));
			}

		}

		// 对每组数据进行标记
		// CommonDataDMO dmo1 = new CommonDataDMO(BxgtStepButton.DESIGN_A);
		CommonDataDMO dmo2 = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		String[] sqllist = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			String codes = list.get(i);
			String sql = "update bd_cumandoc set def30='Y' where pk_cubasdoc in"
					+ " (select pk_cubasdoc from bd_cubasdoc where nvl(dr,0)=0 and custcode in("
					+ codes + "))";
			sqllist[i] = sql;
		}

		this.excuteUpdateJDBCs(sqllist);
		dmo2.execDatas(sqllist);

		return m + n;
	}

	private int synCustBasedocAuto(String billtime) throws Exception {

		String[] pkms = null;
		String sqlspt = "select distinct t.pk_cubasdoc from bd_cubasdoc t left join bd_cumandoc s "
				+ " on t.pk_cubasdoc=s.pk_cubasdoc where nvl(t.dr,0)=0 and nvl(s.dr,0)=0 "
				+ " and nvl(s.def30,'N')='N' and nvl(t.fax1,'N')='Y'";// 暂用def4代替开票客商
		ArrayList<String> listpk = (ArrayList<String>) this
				.getResultListColumn(sqlspt);
		if (listpk != null && listpk.size() > 0) {
			pkms = listpk.toArray(new String[0]);
		} else {
			return 0;
		}
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String ccode = "";
		int j = 0;
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < pkms.length; i++) {
			ccode += "'" + pkms[i] + "',";
			j++;
			if (j == 100) {
				String str = ccode.substring(0, ccode.lastIndexOf(","));
				list.add(str);
				j = 0;
				ccode = "";
			}
		}
		if (j != 0) {
			String str = ccode.substring(0, ccode.lastIndexOf(","));
			list.add(str);
		}

		int m = 0;
		int n = 0;

		// 对每组里面的数据进行同步
		for (int i = 0; i < list.size(); i++) {
			String pks = list.get(i);
			// 先校验B中是否存在的编码
			String sql = "select pk_cubasdoc from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
					+ pks + ")";
			Object obj = dao2.executeQuery(sql, new ColumnPKsProcessor());
			String sql_1 = "";
			String sql_2 = "";
			if (obj != null && !"".equals(obj.toString())) {// 有更新数据
				sql_1 = "select * from bd_cubasdoc where nvl(dr,0)=0 and pk_cubasdoc in("
						+ obj.toString() + ")";
				sql_2 = sql.replaceFirst("pk_cubasdoc", "\\*")
						+ " and pk_cubasdoc not in(" + obj.toString() + ")";
			} else {
				sql_2 = sql.replaceFirst("pk_cubasdoc", "\\*");
			}

			if (!"".equals(sql_1)) {
				List<CubasdocVO> list1 = (List<CubasdocVO>) this
						.getResultBeanList(sql_1, CubasdocVO.class);
				m = m + list1.size();// 更新数量
				for (CubasdocVO vo : list1) {
					vo.setModifier(null);
					vo.setModifytime(null);
					vo.setCreatetime(billtime);
					vo.setTs(billtime);
				}
				dao2.updateVOArray(list1.toArray(new CubasdocVO[0]));
			}
			List<CubasdocVO> list2 = (List<CubasdocVO>) this.getResultBeanList(
					sql_2, CubasdocVO.class);
			n = n + list2.size();// 插入数量
			if (list2 != null && list2.size() > 0) {
				for (CubasdocVO vo : list2) {
					vo.setModifier(null);
					vo.setModifytime(null);
					vo.setCreatetime(billtime);
					vo.setTs(billtime);
				}
				dao2.insertVOArrayWithPK(list2.toArray(new CubasdocVO[0]));
			}

			// 管理档案
			String sqlman = "select * from bd_cumandoc where nvl(dr,0)=0 and pk_cubasdoc in ("
					+ pks + ")";
			List<CumandocVO> list3 = (List<CumandocVO>) this.getResultBeanList(
					sqlman, CumandocVO.class);

			if (list3 != null && list3.size() > 0) {
				String[] pkmans = new String[list3.size()];
				for (int k = 0; k < list3.size(); k++) {
					if (list3.get(k).getCustflag() == null
							|| list3.get(k).getCustflag().equals("")) {
						list3.get(k).setCustflag(" ");
					}
					list3.get(k).setCreatetime(billtime);
					list3.get(k).setModifytime(null);
					list3.get(k).setModifier(null);
					list3.get(k).setDevelopdate(billtime.substring(0, 10));
					list3.get(k).setTs(billtime.toString());
					pkmans[k] = list3.get(k).getPrimaryKey();
				}
				dao2.deleteByPKs(CumandocVO.class, pkmans);// 删除

				dao2.insertVOArrayWithPK(list3.toArray(new CumandocVO[0]));
			}

		}

		// 对每组数据进行标记
		// CommonDataDMO dmo1 = new CommonDataDMO(BxgtStepButton.DESIGN_A);
		CommonDataDMO dmo2 = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		String[] sqllist = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			String pks = list.get(i);
			String sql = "update bd_cumandoc set def30='Y' where pk_cubasdoc in ("
					+ pks + ") ";
			sqllist[i] = sql;
		}

		this.excuteUpdateJDBCs(sqllist);
		dmo2.execDatas(sqllist);

		return m + n;
	}

	/**
	 * 刷新采购流程公司
	 */
	private void checkAndChangeCorp(String[] pks1, String[] pks2,
			String[] pks3, String[] pks4) throws Exception {/*
															 * 
															 * CommonDataDMO dao =
															 * new
															 * CommonDataDMO(BxgtStepButton.DESIGN_B);
															 * 
															 * List<String>
															 * sqls = new
															 * ArrayList<String>(); //
															 * 请购单 if (pks1 !=
															 * null &&
															 * pks1.length > 0) {
															 * String str = "";
															 * for (String pk1 :
															 * pks1) { str +=
															 * "'" + pk1 + "',"; }
															 * str =
															 * str.substring(0,
															 * str.lastIndexOf(","));
															 * 
															 * String sql1 =
															 * "update
															 * po_praybill set
															 * pk_corp='" +
															 * pk_corp + "'
															 * where cpraybillid
															 * in(" + str + ")";
															 * String sql2 =
															 * "update
															 * po_praybill_b set
															 * pk_corp='" +
															 * pk_corp + "'
															 * where cpraybillid
															 * in(" + str + ")";
															 * sqls.add(sql1);
															 * sqls.add(sql2); } //
															 * 采购订单 if (pks2 !=
															 * null &&
															 * pks2.length > 0) {
															 * String str = "";
															 * for (String pk2 :
															 * pks2) { str +=
															 * "'" + pk2 + "',"; }
															 * str =
															 * str.substring(0,
															 * str.lastIndexOf(","));
															 * 
															 * String sql1 =
															 * "update po_order
															 * set pk_corp='" +
															 * pk_corp + "'
															 * where corderid
															 * in(" + str + ")";
															 * String sql2 =
															 * "update
															 * po_order_b set
															 * pk_corp='" +
															 * pk_corp + "'
															 * where corderid
															 * in(" + str + ")";
															 * sqls.add(sql1);
															 * sqls.add(sql2); } //
															 * 库存 if (pks3 !=
															 * null &&
															 * pks3.length > 0) {
															 * String str = "";
															 * for (String pk3 :
															 * pks3) { str +=
															 * "'" + pk3 + "',"; }
															 * str =
															 * str.substring(0,
															 * str.lastIndexOf(","));
															 * 
															 * String sql1 =
															 * "update
															 * ic_general_h set
															 * pk_corp='" +
															 * pk_corp +
															 * "',pk_calbody='"+pk_calbody
															 * +"' where
															 * cgeneralhid in(" +
															 * str + ")"; String
															 * sql2 = "update
															 * ic_general_b set
															 * pk_corp='" +
															 * pk_corp +
															 * "',pk_bodycalbody='"+pk_calbody
															 * +"' where
															 * cgeneralhid in(" +
															 * str + ")"; String
															 * sql3 = "update
															 * ic_general_bb3
															 * set pk_corp='" +
															 * pk_corp + "'
															 * where cgeneralhid
															 * in(" + str + ")";
															 * String sql4 =
															 * "update
															 * ic_general_bb1
															 * set pk_corp='" +
															 * pk_corp + "'
															 * where cgeneralbid
															 * in" + "(select
															 * cgeneralbid from
															 * ic_general_b
															 * where nvl(dr,0)=0
															 * and cgeneralhid
															 * in(" + str +
															 * "))";
															 * sqls.add(sql1);
															 * sqls.add(sql2);
															 * sqls.add(sql3);
															 * sqls.add(sql4); } //
															 * 发票 if (pks4 !=
															 * null &&
															 * pks4.length > 0) {
															 * String str = "";
															 * for (String pk4 :
															 * pks4) { str +=
															 * "'" + pk4 + "',"; }
															 * str =
															 * str.substring(0,
															 * str.lastIndexOf(","));
															 * 
															 * String sql1 =
															 * "update
															 * po_invoice set
															 * pk_corp='" +
															 * pk_corp + "'
															 * where cinvoiceid
															 * in(" + str + ")";
															 * String sql2 =
															 * "update
															 * po_invoice_b set
															 * pk_corp='" +
															 * pk_corp + "'
															 * where cinvoiceid
															 * in(" + str + ")";
															 * sqls.add(sql1);
															 * sqls.add(sql2); }
															 * 
															 * dao.execDatas(sqls.toArray(new
															 * String[0]));
															 */
	}

	/**
	 * 批改各流程打印
	 */
	private void printAfterBatch(String[] pks1, String[] pks2, String[] pks3,
			String[] pks4, int type) throws BusinessException {

		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		if (type == BxgtStepButton.SO_FLOW || type == BxgtStepButton.GD_FLOW) {// 销售流程(广东)

			if (pks1 != null && pks1.length > 0) {// 订单
				EditInfo[] eds = new EditInfo[pks1.length];
				for (int i = 0; i < pks1.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCsaleid(pks1[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

			if (pks2 != null && pks2.length > 0) {// 收款
				EditInfo[] eds = new EditInfo[pks2.length];
				for (int i = 0; i < pks2.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setVouchid(pks2[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

			if (pks3 != null && pks3.length > 0) {// 库存
				EditInfo[] eds = new EditInfo[pks3.length];
				for (int i = 0; i < pks3.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCgeneralhid(pks3[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

		} else if (type == BxgtStepButton.PU_FLOW) {// 采购流程

			if (pks1 != null && pks1.length > 0) {// 请购单
				EditInfo[] eds = new EditInfo[pks1.length];
				for (int i = 0; i < pks1.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCpraybillid(pks1[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

			if (pks2 != null && pks2.length > 0) {// 订单
				EditInfo[] eds = new EditInfo[pks2.length];
				for (int i = 0; i < pks2.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCorderid(pks2[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

			if (pks3 != null && pks3.length > 0) {// 库存
				EditInfo[] eds = new EditInfo[pks3.length];
				for (int i = 0; i < pks3.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCgeneralhid(pks3[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

			if (pks4 != null && pks4.length > 0) {// 发票
				EditInfo[] eds = new EditInfo[pks4.length];
				for (int i = 0; i < pks4.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCinvoiceid(pks4[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}

		} else if (type == BxgtStepButton.CL_FLOW) { // 材料出库

			if (pks1 != null && pks1.length > 0) {// 出库
				EditInfo[] eds = new EditInfo[pks1.length];
				for (int i = 0; i < pks1.length; i++) {
					EditInfo edi = new EditInfo();
					edi.setCgeneralhid(pks1[i]);
					eds[i] = edi;
				}
				dao.insertVOArray(eds);
			}
		}
	}

	/**
	 * 重新清空表的字段，自定义项
	 * 
	 * @param pks
	 * @param it
	 * @throws Exception
	 */
	private void resetDatetoNull(String[] pks, Integer it) throws Exception {

		String sql = "";
		if (it == BxgtStepButton.N30) {
			sql = "update so_sale set vdef8=null,vdef18=null,pk_defdoc2=null,cemployeeid=null,iprintcount=null where csaleid in ";
		} else if (it == BxgtStepButton.N12) {
			sql = "update ic_general_h set iprintcount=null,vuserdef8=null where cgeneralhid in ";
		} else if (it == BxgtStepButton.N21) {
			sql = "update po_order set iprintcount=null,vmemo=null,vdef2=null,vdef9=null where corderid in ";
		} else if (it == BxgtStepButton.N13) {
			sql = "update ic_general_h set iprintcount=null,vnote=null,vuserdef2=null where cgeneralhid in ";
		} else if (it == BxgtStepButton.N25) {
			sql = "update po_invoice set iprintcount=null,vmemo=null,vdef2=null where cinvoiceid in ";
		} else if (it == BxgtStepButton.N14) {
			sql = "update ic_general_h set iprintcount=null,vnote=null,vuserdef2=null where cgeneralhid in ";
		}

		this.deleteOrUpdateBillByPksBZT(pks, sql);
	}

	/** *************************************************************************** */
	/** *************************************************************************** */
	private Object getResultBeanList(String sql, Class type) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();
		ResultSet rs = pst.executeQuery(sql);

		return ProcessorUtils.toBeanList(rs, type);
	}

	private List getResultListMap(String sql) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();
		ResultSet rs = pst.executeQuery(sql);

		List results = new ArrayList();
		while (rs.next()) {
			results.add(ProcessorUtils.toMap(rs));
		}
		rs.close();
		pst.close();
		con.close();

		return results;
	}

	private List getResultListColumn(String sql) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		int columnIndex = 1;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();
		ResultSet rs = pst.executeQuery(sql);

		List<String> results = new ArrayList<String>();
		while (rs.next()) {
			results.add(rs.getObject(columnIndex) == null ? "" : rs.getObject(
					columnIndex).toString());
		}
		rs.close();
		pst.close();
		con.close();

		return results;
	}

	private Object getResultObject(String sql) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();
		ResultSet rs = pst.executeQuery(sql);
		if (rs.next()) {
			return rs.getObject(1);
		} else {
			return null;
		}
	}

	private String getResultString(String sql) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();
		ResultSet rs = pst.executeQuery(sql);
		String pks = "";
		while (rs.next()) {
			pks += "'" + rs.getString(1) + "',";
		}
		if (!"".equals(pks)) {
			return pks.substring(0, pks.lastIndexOf(","));
		}
		return pks;
	}

	private void excuteUpdateJDBCs(String[] sqls) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();

		for (String sql : sqls) {
			pst.addBatch(sql);
		}
		pst.executeBatch();

		pst.close();
		con.close();
	}

	private void excuteUpdateJDBC(String sql) throws Exception {

		String[] str = BxgtStepButton.getConnect();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(str[0], str[1], str[2]);
		Statement pst = con.createStatement();

		pst.executeUpdate(sql);

		pst.close();
		con.close();
	}

	private String[] queryPks1ByPks2(String[] pks, String sql) throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}

		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				ArrayList<String> llist = (ArrayList<String>) this
						.getResultListColumn(sql_st);
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			ArrayList<String> llist = (ArrayList<String>) this
					.getResultListColumn(sql_st);
			list.addAll(llist);
		}
		return list.toArray(new String[0]);
	}

	private List queryMapPks1ByPks2(String[] pks, String sql) throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				List llist = this.getResultListMap(sql_st);
				list.addAll((List<Map<String, Object>>) llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			List llist = this.getResultListMap(sql_st);
			list.addAll((List<Map<String, Object>>) llist);
		}
		return list;
	}

	private List queryBeanPks1ByPks2(String[] pks, String sql, Class classType)
			throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				List<SuperVO> llist = (List<SuperVO>) this.getResultBeanList(
						sql_st, classType);
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			List<SuperVO> llist = (List<SuperVO>) this.getResultBeanList(
					sql_st, classType);
			list.addAll(llist);
		}
		return list;
	}

	private String[] queryPks1ByPks2BZT(String[] pks, String sql)
			throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
						sql_st, new ColumnListProcessor());
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
					sql_st, new ColumnListProcessor());
			list.addAll(llist);
		}
		return list.toArray(new String[0]);
	}

	private String[] queryBillcodeBySqlsBZT(String[] pks, String sql1,
			String sql2, String sql3) throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql1 + "(" + pk + ")" + sql2 + "(" + pk + ")"
						+ sql3;
				ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
						sql_st, new ColumnListProcessor());
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql1 + "(" + pk + ")" + sql2 + "(" + pk + ")"
					+ sql3;
			ArrayList<String> llist = (ArrayList<String>) dao.executeQuery(
					sql_st, new ColumnListProcessor());
			list.addAll(llist);
		}
		return list.toArray(new String[0]);
	}

	// B帐查询
	private List queryMapPks1ByPks2BZT(String[] pks, String sql)
			throws Exception {

		if (pks == null || pks.length <= 0) {
			return null;
		}
		BaseDAO dao = new BaseDAO(BxgtStepButton.DESIGN_B);
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				List<Map<String, Object>> llist = (List<Map<String, Object>>) dao
						.executeQuery(sql_st, new MapListProcessor());
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			List<Map<String, Object>> llist = (List<Map<String, Object>>) dao
					.executeQuery(sql_st, new MapListProcessor());
			list.addAll(llist);
		}
		return list;
	}

	private void deleteOrUpdateBillByPks(String[] pks, String sql)
			throws Exception {

		if (pks == null || pks.length <= 0) {
			return;
		}

		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<String> sqls = new ArrayList<String>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				sqls.add(sql_st);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			sqls.add(sql_st);
		}

		this.excuteUpdateJDBCs(sqls.toArray(new String[0]));
	}

	private void deleteOrUpdateBillByPksBZT(String[] pks, String sql)
			throws Exception {

		if (pks == null || pks.length <= 0) {
			return;
		}
		CommonDataDMO dao = new CommonDataDMO(BxgtStepButton.DESIGN_B);
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<String> sqls = new ArrayList<String>();
		for (int i = 800 * j + 0; i < pks.length; i++) {
			pk += "'" + pks[i] + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				sqls.add(sql_st);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			sqls.add(sql_st);
		}

		dao.execDatas(sqls.toArray(new String[0]));
	}

	/**
	 * 排序
	 */
	public void orderSeqBill(String[] pks, String billtype) throws Exception {

		if (billtype.equals("销售订单")) {
			// 根据销售订单主键获得对应销售出库单主键
			String[] pkSaleOuts = this.getCSaleOutByCsaleid(pks,
					BxgtStepButton.DESIGN_B);
			// 根据销售订单主键获得对应收款单主键
			String[] vouchids = getSKpksB(pks, BxgtStepButton.DESIGN_B);
			if (pkSaleOuts == null || pkSaleOuts.length <= 0) {
				throw new BusinessException("B帐套销售订单下游数据不存在或被删除！");
			}
			orderSeqSaleOrder(pks, pkSaleOuts, vouchids);

		} else if (billtype.equals("采购订单")) {
			// 根据采购订单主键获得对应采购入库单主键
			String[] pkPurchaseIns = this.getPurchaseInPksB(pks);
			// 根据采购订单主键获得对应采购发票主键
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(pks);

			if (pkPurchaseIns == null || pkPurchaseIns.length <= 0) {
				throw new BusinessException("B帐套采购订单下游数据不存在或被删除！");
			}
			orderSeqPurOrder(pks, pkPurchaseIns, pkPurchaseInvoices);
		} else if (billtype.equals("材料出库")) {
			orderSeqMateOrder(pks);
		}
	}

	private void orderSeqSaleOrder(String[] saleOrders, String[] pkSaleOuts,
			String[] vouchids) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 排序销售订单
		OrderSeqInfo[] conVOs = new OrderSeqInfo[saleOrders.length];
		String[] sqls = new String[saleOrders.length];
		for (int i = 0; i < conVOs.length; i++) {
			OrderSeqInfo conVo = new OrderSeqInfo();
			conVo.setCsaleid(saleOrders[i]);
			conVOs[i] = conVo;

			String sql = "insert into bxgt_isorderseq(pk,csaleid)values(seq_info4.nextval,'"
					+ saleOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVOs);
		// 排序销售出库单
		OrderSeqInfo[] conVOs2 = new OrderSeqInfo[pkSaleOuts.length];
		String[] sqls2 = new String[pkSaleOuts.length];
		for (int i = 0; i < conVOs2.length; i++) {
			OrderSeqInfo conVo = new OrderSeqInfo();
			conVo.setCgeneralhid(pkSaleOuts[i]);
			conVOs2[i] = conVo;

			String sql = "insert into bxgt_isorderseq(pk,cgeneralhid)values(seq_info4.nextval,'"
					+ pkSaleOuts[i] + "')";
			sqls2[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls2);
		dao2.insertVOArray(conVOs2);
		// 排序收款单
		/*
		 * OrderSeqInfo[] conVOs3 = new OrderSeqInfo[vouchids.length]; String[]
		 * sqls3 = new String[vouchids.length]; for (int i = 0; i <
		 * conVOs3.length; i++) { OrderSeqInfo conVo = new OrderSeqInfo();
		 * conVo.setVouchid(vouchids[i]); conVOs3[i] = conVo;
		 * 
		 * String sql = "insert into
		 * bxgt_isorderseq(pk,vouchid)values(seq_info4.nextval,'" + vouchids[i] +
		 * "')"; sqls3[i] = sql; } this.excuteUpdateJDBCs(sqls3);
		 * dao2.insertVOArray(conVOs3);
		 */
	}

	private void orderSeqPurOrder(String[] purOrders, String[] pkSaleOuts,
			String[] invoices) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		// 排序采购订单
		OrderSeqInfo[] conVOs = new OrderSeqInfo[purOrders.length];
		String[] sqls = new String[purOrders.length];
		for (int i = 0; i < conVOs.length; i++) {
			OrderSeqInfo conVo = new OrderSeqInfo();
			conVo.setCorderid(purOrders[i]);
			conVOs[i] = conVo;

			String sql = "insert into bxgt_isorderseq(pk,corderid)values(seq_info4.nextval,'"
					+ purOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVOs);
		// 排序采购入库单
		OrderSeqInfo[] conVOs2 = new OrderSeqInfo[pkSaleOuts.length];
		String[] sqls2 = new String[pkSaleOuts.length];
		for (int i = 0; i < conVOs2.length; i++) {
			OrderSeqInfo conVo = new OrderSeqInfo();
			conVo.setCgeneralhid(pkSaleOuts[i]);
			conVOs2[i] = conVo;

			String sql = "insert into bxgt_isorderseq(pk,cgeneralhid)values(seq_info4.nextval,'"
					+ pkSaleOuts[i] + "')";
			sqls2[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls2);
		dao2.insertVOArray(conVOs2);
		// 排序采购发票
		/*
		 * OrderSeqInfo[] conVOs3 = new OrderSeqInfo[invoices.length]; String[]
		 * sqls3 = new String[invoices.length]; for (int i = 0; i <
		 * conVOs3.length; i++) { OrderSeqInfo conVo = new OrderSeqInfo();
		 * conVo.setCinvoiceid(invoices[i]); conVOs3[i] = conVo;
		 * 
		 * String sql = "insert into
		 * bxgt_isorderseq(pk,cinvoiceid)values(seq_info4.nextval,'" +
		 * invoices[i] + "')"; sqls3[i] = sql; } this.excuteUpdateJDBCs(sqls3);
		 * dao2.insertVOArray(conVOs3);
		 */
	}

	private void orderSeqMateOrder(String[] mateOrders) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		OrderSeqInfo[] conVos = new OrderSeqInfo[mateOrders.length];
		String[] sqls = new String[mateOrders.length];
		for (int i = 0; i < conVos.length; i++) {
			OrderSeqInfo conVo = new OrderSeqInfo();
			conVo.setCgeneralhid(mateOrders[i]);
			conVos[i] = conVo;

			String sql = "insert into bxgt_isorderseq(pk,cgeneralhid)values(seq_info4.nextval,'"
					+ mateOrders[i] + "')";
			sqls[i] = sql;
		}
		this.excuteUpdateJDBCs(sqls);
		dao2.insertVOArray(conVos);
	}

	public void unOrderSeqBill(String[] pks, String billtype) throws Exception {

		if (billtype.equals("销售订单")) {
			// 根据销售订单主键获得对应销售出库单主键
			String[] pkSaleOuts = this.getCSaleOutByCsaleid(pks,
					BxgtStepButton.DESIGN_B);
			// 根据销售订单主键获得对应收款单主键
			String[] vouchids = getSKpksB(pks, BxgtStepButton.DESIGN_B);
			if (pkSaleOuts == null || pkSaleOuts.length <= 0) {
				throw new BusinessException("B帐套销售订单下游数据不存在或被删除！");
			}
			unOrderSeqSaleOrder(pks, pkSaleOuts, vouchids);

		} else if (billtype.equals("采购订单")) {
			// 根据采购订单主键获得对应采购入库单主键
			String[] pkPurchaseIns = this.getPurchaseInPksB(pks);
			// 根据采购订单主键获得对应采购发票主键
			String[] pkPurchaseInvoices = this
					.getPurInvoicePksByPurOrderPksB(pks);

			if (pkPurchaseIns == null || pkPurchaseIns.length <= 0) {
				throw new BusinessException("B帐套采购订单下游数据不存在或被删除！");
			}
			unOrderSeqPurOrder(pks, pkPurchaseIns, pkPurchaseInvoices);
		} else if (billtype.equals("材料出库")) {
			unOrderSeqMateOrder(pks);
		}
	}

	private void unOrderSeqSaleOrder(String[] saleOrders, String[] pkSaleOuts,
			String[] vouchids) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		String pk2 = "";
		// String pk3 = "";
		for (String saleOrder : saleOrders) {
			pk1 += "'" + saleOrder + "',";
		}
		for (String pkSaleOut : pkSaleOuts) {
			pk2 += "'" + pkSaleOut + "',";
		}
		/*
		 * for (String vouchid : vouchids) { pk3 += "'" + vouchid + "',"; }
		 */
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		pk2 = pk2.substring(0, pk2.lastIndexOf(","));
		// pk3 = pk3.substring(0, pk3.lastIndexOf(","));
		String sql1 = "delete from bxgt_isorderseq where csaleid in(" + pk1
				+ ")";
		String sql2 = "delete from bxgt_isorderseq where cgeneralhid in(" + pk2
				+ ")";
		/*
		 * String sql3 = "delete from bxgt_isorderseq where vouchid in(" + pk3 +
		 * ")";
		 */

		this.excuteUpdateJDBC(sql1);
		this.excuteUpdateJDBC(sql2);
		// this.excuteUpdateJDBC(sql3);
		dao2.executeUpdate(sql1);
		dao2.executeUpdate(sql2);
		// dao2.executeUpdate(sql3);
	}

	/**
	 * 向确认表中删除采购订单，采购入库单，采购发票 by tcl
	 * 
	 * @throws Exception
	 */
	private void unOrderSeqPurOrder(String[] purOrders, String[] pkSaleOuts,
			String[] invoices) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		String pk2 = "";
		// String pk3 = "";
		for (String purOrder : purOrders) {
			pk1 += "'" + purOrder + "',";
		}
		for (String pkSaleOut : pkSaleOuts) {
			pk2 += "'" + pkSaleOut + "',";
		}
		/*
		 * for (String invoice : invoices) { pk3 += "'" + invoice + "',"; }
		 */
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		pk2 = pk2.substring(0, pk2.lastIndexOf(","));
		// pk3 = pk3.substring(0, pk3.lastIndexOf(","));
		String sql1 = "delete from bxgt_isorderseq where corderid in(" + pk1
				+ ")";
		String sql2 = "delete from bxgt_isorderseq where cgeneralhid in(" + pk2
				+ ")";
		/*
		 * String sql3 = "delete from bxgt_isorderseq where cinvoiceid in(" +
		 * pk3 + ")";
		 */

		this.excuteUpdateJDBC(sql1);
		this.excuteUpdateJDBC(sql2);
		// this.excuteUpdateJDBC(sql3);
		dao2.executeUpdate(sql1);
		dao2.executeUpdate(sql2);
		// dao2.executeUpdate(sql3);
	}

	/**
	 * 向确认表中删除材料出库
	 */
	private void unOrderSeqMateOrder(String[] mateOrders) throws Exception {
		BaseDAO dao2 = new BaseDAO(BxgtStepButton.DESIGN_B);
		String pk1 = "";
		for (String mateOrder : mateOrders) {
			pk1 += "'" + mateOrder + "',";
		}
		pk1 = pk1.substring(0, pk1.lastIndexOf(","));
		String sql = "delete from bxgt_isorderseq where cgeneralhid in(" + pk1
				+ ")";
		this.excuteUpdateJDBC(sql);
		dao2.executeUpdate(sql);
	}

}
