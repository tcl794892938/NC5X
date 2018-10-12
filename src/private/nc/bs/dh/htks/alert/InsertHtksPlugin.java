package nc.bs.dh.htks.alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin2;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dahuan.report.AlerthtksVO;
import nc.vo.dahuan.report.HtmxReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pa.Key;

public class InsertHtksPlugin implements IBusinessPlugin2 {

	public int getImplmentsType() {

		return IMPLEMENT_RETURNMESSAGE;
	}

	public Key[] getKeys() {

		return null;
	}

	public String getTypeDescription() {

		return null;
	}

	public String getTypeName() {

		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {

		return null;
	}

	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {

		return null;
	}

	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {

		return null;
	}

	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {

		return false;
	}

	public IAlertMessage[] implementReturnFormatMsg(Key[] keys,
			Object currEnvVO, UFDate clientLoginDate) throws BusinessException {

		return null;
	}

	public String[] implementReturnMessage(Key[] keys, Object currEnvVO,
			UFDate clientLoginDate) throws BusinessException {

		String pk_corp = null;
		String Condition = null;
		for (int i = 0; i < keys.length; i++) {
			if ("pk_corp".equals(keys[i].getName())) {
				pk_corp = (String) keys[i].getValue();
			}

			if ("Condition".equals(keys[i].getName())) {
				Condition = "jobname like  '%" + (String) keys[i].getValue()
						+ "%' ";
			}
		}

		if (pk_corp != null || !"0001".equals(pk_corp) || !("").equals(pk_corp)) {
			Condition += " and gl_detail.pk_corp = '" + pk_corp + "'";
		}

		String delSql = " delete from dh_htalert ";
		new BaseDAO().executeUpdate(delSql);

		AlerthtksVO[] itemvos1 = queryAlertHttj();

		AlerthtksVO[] itemvos2 = queryAlertHtzx();

		SuperVO[] superVOs = new SuperVO[itemvos1.length + itemvos2.length];

		System.arraycopy(itemvos1, 0, superVOs, 0, itemvos1.length);
		System.arraycopy(itemvos2, 0, superVOs, itemvos1.length,
				itemvos2.length);

		new BaseDAO().insertVOArray(superVOs);

		return null;
	}

	private AlerthtksVO[] queryAlertHttj() throws BusinessException {

		StringBuffer sql1 = new StringBuffer();
		sql1.append("	select TEMQ_CT.jobname jobname,TEMQ_CT.pk_custdoc, ");
		sql1.append("   TEMQ_CT.def1 def1, ");
		sql1.append("   TEMQ_CT.xsj xsj,   ");
		sql1.append("   TEMQ_CT.cgj cgj,   ");
		sql1
				.append("   TEMQ_CT.ce ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.jobcode ,TEMQ_CT.pk_corp ");
		sql1.append("   from  TEMQ_CT   ");
		sql1
				.append("  group by TEMQ_CT.jobcode ,TEMQ_CT.jobname, TEMQ_CT.pk_custdoc, ");
		sql1.append("       TEMQ_CT.def1, ");
		sql1.append("       TEMQ_CT.xsj,  ");
		sql1.append("       TEMQ_CT.cgj,  ");
		sql1
				.append("       TEMQ_CT.ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.pk_corp   ");
		sql1.append(" order by jobname asc ");

		IUAPQueryBS uapbs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		ArrayList list = (ArrayList) uapbs.executeQuery(sql1.toString(),
				new ArrayListProcessor());
		Object clistobj;
		Object[] vobjs;
		String tmp;
		String[] msgs = null;
		ArrayList<HtmxReportVO> expensehtmxVoList = new ArrayList<HtmxReportVO>();
		HtmxReportVO expensehtmxVo = null;
		HashMap htmap = new HashMap();
		HashMap htnamemap = new HashMap();

		if (list.size() != 0) {
			clistobj = list.get(0);
			vobjs = (Object[]) clistobj;
			for (int i = 0; i < list.size(); i++) {
				clistobj = list.get(i);
				vobjs = (Object[]) clistobj;
				expensehtmxVo = new HtmxReportVO();
				tmp = vobjs[0] == null ? " " : vobjs[0].toString();
				expensehtmxVo.setJobname(tmp);
				tmp = vobjs[1] == null ? " " : vobjs[1].toString();
				expensehtmxVo.setCustname(tmp);
				tmp = vobjs[2] == null ? " " : vobjs[2].toString();
				expensehtmxVo.setDef1(tmp);
				tmp = vobjs[3] == null ? " " : vobjs[3].toString();
				expensehtmxVo.setXsj(new UFDouble(tmp));
				tmp = vobjs[4] == null ? " " : vobjs[4].toString();
				expensehtmxVo.setCgj(new UFDouble(tmp));
				tmp = vobjs[5] == null ? " " : vobjs[5].toString();
				expensehtmxVo.setCe(new UFDouble(tmp));
				tmp = vobjs[6] == null ? " " : vobjs[6].toString();
				expensehtmxVo.setDeptname(tmp);
				tmp = vobjs[7] == null ? " " : vobjs[7].toString();
				expensehtmxVo.setJobcode(tmp);
				tmp = vobjs[8] == null ? " " : vobjs[8].toString();
				expensehtmxVo.setPk_corp(tmp);

				if (expensehtmxVo.getJobname().endsWith("-00")) {
					String name1 = expensehtmxVo.getDef1() == null ? ""
							: expensehtmxVo.getDef1();
					String name2 = expensehtmxVo.getCustname() == null ? ""
							: expensehtmxVo.getCustname();
					String name3 = expensehtmxVo.getDeptname() == null ? ""
							: expensehtmxVo.getDeptname();
					String pk_corp = expensehtmxVo.getPk_corp() == null ? ""
							: expensehtmxVo.getPk_corp();
					String[] objs = new String[] { name1, name2, name3, pk_corp };
					htnamemap.put(expensehtmxVo.getJobname(), objs);
				}
				expensehtmxVoList.add(expensehtmxVo);
			}

			HtmxReportVO[] itemvos = (HtmxReportVO[]) expensehtmxVoList
					.toArray(new HtmxReportVO[0]);

			HashMap htcodemap = new HashMap();
			for (int i = 0; i < itemvos.length; i++) {
				String tmp1 = itemvos[i].getJobname();
				int htlen = tmp1.indexOf("-");
				String htzname = tmp1.substring(0, htlen);
				if (htmap.containsKey(htzname)) {
					UFDouble[] objs = (UFDouble[]) htmap.get(htzname);
					UFDouble xsj = objs[0].add(itemvos[i].getXsj()); // 销售金额
					UFDouble cgj = objs[1].add(itemvos[i].getCgj()); // 采购金额
					UFDouble ce = objs[2].add(itemvos[i].getCe());// 毛利
					objs = new UFDouble[] { xsj, cgj, ce };
					htmap.put(htzname, objs);
				} else {
					UFDouble xsj = itemvos[i].getXsj(); // 销售金额
					UFDouble cgj = itemvos[i].getCgj(); // 采购金额
					UFDouble ce = itemvos[i].getCe(); // 毛利
					UFDouble[] objs = new UFDouble[] { xsj, cgj, ce };
					htmap.put(htzname, objs);
				}

				htcodemap.put(itemvos[i].getJobname(), itemvos[i].getJobcode());
			}

			Iterator iter = htmap.entrySet().iterator();
			ArrayList listvo = new ArrayList();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				UFDouble[] objs = (UFDouble[]) entry.getValue();
				AlerthtksVO itemvo = new AlerthtksVO();
				String key1 = key + "-00";
				itemvo.setJobcode(key1);
				if (htnamemap.get(key1) != null) {
					String[] names = (String[]) htnamemap.get(key1);
					itemvo.setJobname(names[0]);
					itemvo.setCustname(names[1]);
					itemvo.setDeptname(names[2]);
					itemvo.setPk_corp(names[3]);
				}
				itemvo.setXsj(new UFDouble(objs[0].toString(), 2));
				itemvo.setCgj(new UFDouble(objs[1].toString(), 2));
				UFDouble ce = new UFDouble(objs[2].toString(), 2);
				if (ce.doubleValue() > 0)
					continue;
				String xmcode = (String) htcodemap.get(key1);
				itemvo.setXmcode(xmcode);
				itemvo.setCe(ce);
				itemvo.setAlertype(0);
				listvo.add(itemvo);
			}
			AlerthtksVO[] itemvos1 = (AlerthtksVO[]) listvo
					.toArray(new AlerthtksVO[0]);
			return itemvos1;
		}
		return null;

	}

	private HashMap queryAlertHttjxs() throws BusinessException {

		StringBuffer sql1 = new StringBuffer();
		sql1.append("	select TEMQ_CT.jobname jobname,TEMQ_CT.pk_custdoc, ");
		sql1.append("   TEMQ_CT.def1 def1, ");
		sql1.append("   TEMQ_CT.xsj xsj,   ");
		sql1.append("   TEMQ_CT.cgj cgj,   ");
		sql1
				.append("   TEMQ_CT.ce ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.jobcode ,TEMQ_CT.pk_corp ");
		sql1.append("   from  TEMQ_CT  where TEMQ_CT.jobname like '%-00' ");
		sql1
				.append("  group by TEMQ_CT.jobcode ,TEMQ_CT.jobname, TEMQ_CT.pk_custdoc, ");
		sql1.append("       TEMQ_CT.def1, ");
		sql1.append("       TEMQ_CT.xsj,  ");
		sql1.append("       TEMQ_CT.cgj,  ");
		sql1
				.append("       TEMQ_CT.ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.pk_corp   ");
		sql1.append(" order by jobname asc ");

		IUAPQueryBS uapbs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		ArrayList list = (ArrayList) uapbs.executeQuery(sql1.toString(),
				new ArrayListProcessor());
		Object clistobj;
		Object[] vobjs;
		String tmp;
		HtmxReportVO expensehtmxVo = null;
		HashMap htnamemap = new HashMap();

		if (list.size() != 0) {
			clistobj = list.get(0);
			vobjs = (Object[]) clistobj;
			for (int i = 0; i < list.size(); i++) {
				clistobj = list.get(i);
				vobjs = (Object[]) clistobj;
				expensehtmxVo = new HtmxReportVO();
				tmp = vobjs[0] == null ? " " : vobjs[0].toString();
				expensehtmxVo.setJobname(tmp);
				tmp = vobjs[1] == null ? " " : vobjs[1].toString();
				expensehtmxVo.setCustname(tmp);
				tmp = vobjs[2] == null ? " " : vobjs[2].toString();
				expensehtmxVo.setDef1(tmp);
				tmp = vobjs[3] == null ? " " : vobjs[3].toString();
				expensehtmxVo.setXsj(new UFDouble(tmp));
				tmp = vobjs[4] == null ? " " : vobjs[4].toString();
				expensehtmxVo.setCgj(new UFDouble(tmp));
				tmp = vobjs[5] == null ? " " : vobjs[5].toString();
				expensehtmxVo.setCe(new UFDouble(tmp));
				tmp = vobjs[6] == null ? " " : vobjs[6].toString();
				expensehtmxVo.setDeptname(tmp);
				tmp = vobjs[7] == null ? " " : vobjs[7].toString();
				expensehtmxVo.setJobcode(tmp);
				tmp = vobjs[8] == null ? " " : vobjs[8].toString();
				expensehtmxVo.setPk_corp(tmp);

				if (expensehtmxVo.getJobname().endsWith("-00")) {
					String name1 = expensehtmxVo.getDef1() == null ? ""
							: expensehtmxVo.getDef1();
					String name2 = expensehtmxVo.getCustname() == null ? ""
							: expensehtmxVo.getCustname();
					String name3 = expensehtmxVo.getDeptname() == null ? ""
							: expensehtmxVo.getDeptname();
					String pk_corp = expensehtmxVo.getPk_corp() == null ? ""
							: expensehtmxVo.getPk_corp();
					String jobcode = expensehtmxVo.getJobcode() == null ? ""
							: expensehtmxVo.getJobcode();
					String[] objs = new String[] { name1, name2, name3,
							pk_corp, jobcode };
					htnamemap.put(expensehtmxVo.getJobname(), objs);
				}
			}
		}
		return htnamemap;

	}

	private AlerthtksVO[] queryAlertHtzx() throws BusinessException {

		// 合同执行情况
		StringBuffer sql = new StringBuffer();

		sql
				.append("select TEMQ_CT.jobname as jobname,                                                                                      ");
		sql
				.append("       TEMQ_CT.def1 as def1,                                                                                            ");
		sql
				.append("       TEMQ_CT.xsj as xsj,                                                                                              ");
		sql
				.append("       TEMQ_CT.cgj as cgj,                                                                                              ");
		sql
				.append("       TEMQ_CT.ce as ce,                                                                                                ");
		sql
				.append("       TEMQ_dept.deptname as deptname,                                                                                  ");
		sql
				.append("       TEMQ_cust.custname as custname,                                                                                  ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql
				.append("              gl_detail.localcreditamount                                                                               ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("           end) as sjsk,                                                                                                ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                                     ");
		sql
				.append("              gl_detail.localdebitamount                                                                                ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("           end) as sjfk,                                                                                                ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' or                                       ");
		sql
				.append("                  bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              gl_detail.localdebitamount                                                                                ");
		sql
				.append("           end)+To_number(max(nvl(TEMQ_CT.def12,0.00))) as sjfy,                                                                                                ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql
				.append("              gl_detail.localcreditamount                                                                               ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("           end) - sum(case                                                                                              ");
		sql
				.append("                        when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                          ");
		sql
				.append("                         gl_detail.localdebitamount                                                                     ");
		sql
				.append("                        else                                                                                            ");
		sql
				.append("                         0                                                                                              ");
		sql
				.append("                      end) - (sum(case                                                                                   ");
		sql
				.append("                                   when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' or                 ");
		sql
				.append("                                        bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then               ");
		sql
				.append("                                    0                                                                                   ");
		sql
				.append("                                   else                                                                                 ");
		sql
				.append("                                    gl_detail.localdebitamount                                                          ");
		sql
				.append("                                 end)+To_number(max(nvl(TEMQ_CT.def12,0.00)))) as sjml,                                                                          ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql
				.append("              gl_detail.localdebitamount                                                                                ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("           end) as sjkp,                                                                                                ");
		sql
				.append("       sum(case                                                                                                         ");
		sql
				.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                                     ");
		sql
				.append("              gl_detail.localcreditamount                                                                               ");
		sql
				.append("             else                                                                                                       ");
		sql
				.append("              0                                                                                                         ");
		sql
				.append("           end) as sjsp,                                                                                                ");
		sql
				.append("                                                                                                                        ");
		sql
				.append("                 bd_accsubj.subjcode || bd_accsubj.subjname    as subjname  ,TEMQ_CT.jobcode  as  jobcode  ,TEMQ_CT.pk_corp as pk_corp  , TEMQ_CT.def10 as def10        ");
		sql
				.append("                                                                                                                        ");
		sql
				.append("                                                                                                                        ");
		sql
				.append("  from gl_detail                                                                                                        ");
		sql
				.append(" inner join gl_voucher                                                                                                  ");
		sql
				.append("    on gl_detail.pk_voucher = gl_voucher.pk_voucher                                                                     ");
		sql
				.append(" inner join gl_freevalue                                                                                                ");
		sql
				.append("    on gl_detail.assid = gl_freevalue.freevalueid                                                                       ");
		sql
				.append(" inner join bd_accsubj                                                                                                  ");
		sql
				.append("    on gl_detail.pk_accsubj = bd_accsubj.pk_accsubj                                                                     ");
		sql
				.append(" inner join bd_corp                                                                                                     ");
		sql
				.append("    on gl_voucher.pk_corp = bd_corp.pk_corp                                                                             ");
		sql
				.append(" right outer join  TEMQ_CT                                                       ");
		sql
				.append("    on gl_freevalue.checkvalue = TEMQ_CT.pk_jobbasfil                                                                   ");
		sql
				.append("  left outer join (select gl_freevalue.valuename   as deptname,                                                         ");
		sql
				.append("                          gl_freevalue.freevalueid as freevalueid                                                       ");
		sql
				.append("                     from gl_freevalue                                                                                  ");
		sql
				.append("                    where (gl_freevalue.checktype = '00010000000000000002' and                                          ");
		sql
				.append("                          gl_freevalue.dr = 0)) TEMQ_dept                                                               ");
		sql
				.append("    on gl_detail.assid = TEMQ_dept.freevalueid                                                                          ");
		sql
				.append("  left outer join (select gl_freevalue.valuename   as custname,                                                         ");
		sql
				.append("                          gl_freevalue.freevalueid as freevalueid                                                       ");
		sql
				.append("                     from gl_freevalue                                                                                  ");
		sql
				.append("                    where (gl_freevalue.checktype = '00010000000000000073' and                                          ");
		sql
				.append("                          gl_freevalue.dr = 0)) TEMQ_cust                                                               ");
		sql
				.append("    on gl_detail.assid = TEMQ_cust.freevalueid                                                                          ");
		sql
				.append(" where (gl_detail.dr = 0 and gl_voucher.dr = 0 and                                                                      ");
		sql
				.append("       gl_voucher.discardflag = 'N' and gl_voucher.errmessage is null and                                               ");
		sql
				.append("       gl_voucher.period <> '00')                                                                                       ");
		sql
				.append("   and (   (bd_accsubj.subjcode in ('1122', '2203', '2202', '1123','50011201','500113')) or                                                 ");
		sql
				.append("       (    bd_accsubj.subjcode like '6602%'                                                                                ");
		sql
				.append("                                  and                                                                                   ");
		sql
				.append("       gl_detail.localdebitamount <> 0))                                                                                ");
		sql
				.append("   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                                         ");
		sql
				.append(" group by TEMQ_CT.jobcode,TEMQ_CT.jobname,                                                                              ");
		sql
				.append("          TEMQ_CT.def1,                                                                                                 ");
		sql
				.append("          TEMQ_CT.xsj,                                                                                                  ");
		sql
				.append("          TEMQ_CT.cgj,                                                                                                  ");
		sql
				.append("          TEMQ_CT.ce,                                                                                                   ");
		sql
				.append("          TEMQ_dept.deptname,                                                                                           ");
		sql
				.append("          TEMQ_cust.custname,                                                                                           ");
		sql
				.append("          bd_accsubj.subjcode || bd_accsubj.subjname ,TEMQ_CT.pk_corp ,TEMQ_CT.def10                                                                 ");
		sql
				.append(" order by jobname asc, deptname asc, custname asc                                                                       ");

		IUAPQueryBS uapbs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		ArrayList list = (ArrayList) uapbs.executeQuery(sql.toString(),
				new ArrayListProcessor());
		Object clistobj;
		Object[] vobjs;
		String tmp;
		String[] msgs = null;
		ArrayList<HtmxReportVO> expensehtmxVoList = new ArrayList<HtmxReportVO>();
		HtmxReportVO expensehtmxVo = null;
		HashMap htmap = new HashMap();
		HashMap htnamemap = new HashMap();
		if (list.size() != 0) {
			clistobj = list.get(0);
			vobjs = (Object[]) clistobj;
			for (int i = 0; i < list.size(); i++) {
				clistobj = list.get(i);
				vobjs = (Object[]) clistobj;
				expensehtmxVo = new HtmxReportVO();
				tmp = vobjs[0] == null ? " " : vobjs[0].toString();
				expensehtmxVo.setJobname(tmp);
				tmp = vobjs[1] == null ? " " : vobjs[1].toString();
				expensehtmxVo.setDef1(tmp);
				tmp = vobjs[2] == null ? " " : vobjs[2].toString();
				expensehtmxVo.setXsj(new UFDouble(tmp));
				tmp = vobjs[3] == null ? " " : vobjs[3].toString();
				expensehtmxVo.setCgj(new UFDouble(tmp));
				tmp = vobjs[4] == null ? " " : vobjs[4].toString();
				expensehtmxVo.setCe(new UFDouble(tmp));
				tmp = vobjs[5] == null ? " " : vobjs[5].toString();
				expensehtmxVo.setDeptname(tmp);
				tmp = vobjs[6] == null ? " " : vobjs[6].toString();
				expensehtmxVo.setCustname(tmp);
				tmp = vobjs[7] == null ? " " : vobjs[7].toString();
				expensehtmxVo.setSjsk(new UFDouble(tmp));
				tmp = vobjs[8] == null ? " " : vobjs[8].toString();
				expensehtmxVo.setSjfk(new UFDouble(tmp));
				tmp = vobjs[9] == null ? " " : vobjs[9].toString();
				expensehtmxVo.setSjfy(new UFDouble(tmp));
				tmp = vobjs[10] == null ? " " : vobjs[10].toString();
				expensehtmxVo.setSjml(new UFDouble(tmp));
				tmp = vobjs[11] == null ? " " : vobjs[11].toString();
				expensehtmxVo.setSjkp(new UFDouble(tmp));
				tmp = vobjs[12] == null ? " " : vobjs[12].toString();
				expensehtmxVo.setSjsp(new UFDouble(tmp));
				tmp = vobjs[13] == null ? " " : vobjs[13].toString();
				expensehtmxVo.setSubjname(tmp);

				tmp = vobjs[14] == null ? " " : vobjs[14].toString();
				expensehtmxVo.setXmcode(tmp);

				tmp = vobjs[15] == null ? " " : vobjs[15].toString();
				expensehtmxVo.setPk_corp(tmp);

				tmp = vobjs[16] == null ? " " : vobjs[16].toString();
				expensehtmxVo.setProjectname(tmp);

				if (expensehtmxVo.getJobname().endsWith("-00")) {
					String[] objs = new String[] { expensehtmxVo.getDef1(),
							expensehtmxVo.getCustname(),
							expensehtmxVo.getDeptname(),
							expensehtmxVo.getPk_corp() };
					htnamemap.put(expensehtmxVo.getJobname(), objs);
				}

				expensehtmxVoList.add(expensehtmxVo);
			}

			HtmxReportVO[] itemvos = (HtmxReportVO[]) expensehtmxVoList
					.toArray(new HtmxReportVO[0]);

			// HashMap htcodemap = new HashMap();
			for (int i = 0; i < itemvos.length; i++) {
				String tmp1 = itemvos[i].getJobname();
				int htlen = tmp1.indexOf("-");
				String htzname = tmp1.substring(0, htlen);
				if (htmap.containsKey(htzname)) {
					UFDouble[] objs = (UFDouble[]) htmap.get(htzname);
					UFDouble xsj = objs[0].add(itemvos[i].getXsj()); // 销售金额
					UFDouble cgj = new UFDouble(0.00);
					if (!itemvos[i].getDef1().equalsIgnoreCase("零星采购"))
						cgj = objs[1].add(itemvos[i].getCgj()); // 采购金额
					else
						cgj = objs[1].add(itemvos[i].getSjfk()); // 采购金额

					UFDouble ce = objs[2].add(itemvos[i].getCe()); // 毛利
					UFDouble sjsk = objs[3].add(itemvos[i].getSjsk()); // 实际收款
					UFDouble sjfk = objs[4].add(itemvos[i].getSjfk()); // 实际付款
					UFDouble sjml = objs[5].add(itemvos[i].getSjml()); // 实际毛利
					UFDouble sjfy = objs[6].add(itemvos[i].getSjfy()); // 实际费用
					UFDouble sjkp = objs[7].add(itemvos[i].getSjkp()); // 实际开票
					UFDouble sjsp = objs[8].add(itemvos[i].getSjsp()); // 实际收票
					objs = new UFDouble[] { xsj, cgj, ce, sjsk, sjfk, sjml,
							sjfy, sjkp, sjsp };

					htmap.put(htzname, objs);

				} else {
					UFDouble xsj = itemvos[i].getXsj(); // 销售金额
					UFDouble cgj = new UFDouble(0.00); // 采购金额

					if (!itemvos[i].getDef1().equalsIgnoreCase("零星采购"))
						cgj = itemvos[i].getCgj(); // 采购金额
					else
						cgj = itemvos[i].getSjfk(); // 采购金额

					UFDouble ce = itemvos[i].getCe(); // 毛利
					UFDouble sjsk = itemvos[i].getSjsk(); // 实际收款
					UFDouble sjfk = itemvos[i].getSjfk(); // 实际付款
					UFDouble sjml = itemvos[i].getSjml(); // 实际毛利
					UFDouble sjfy = itemvos[i].getSjfy(); // 实际费用
					UFDouble sjkp = itemvos[i].getSjkp(); // 实际开票
					UFDouble sjsp = itemvos[i].getSjsp(); // 实际收票
					UFDouble[] objs = new UFDouble[] { xsj, cgj, ce, sjsk,
							sjfk, sjml, sjfy, sjkp, sjsp };
					htmap.put(htzname, objs);
				}

			}

			HashMap htxsnames = queryAlertHttjxs();

			Iterator iter = htmap.entrySet().iterator();
			ArrayList listvo = new ArrayList();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				UFDouble[] objs = (UFDouble[]) entry.getValue();
				AlerthtksVO itemvo = new AlerthtksVO();
				String key1 = key + "-00";
				itemvo.setJobcode(key1);

				String[] names = (String[]) htxsnames.get(key1);
				if (names != null) {
					itemvo.setJobname(names[0]);
					itemvo.setCustname(names[1]);
					itemvo.setDeptname(names[2]);
					itemvo.setPk_corp(names[3]);
					itemvo.setXmcode(names[4]);
				}else{
					
				}

				itemvo.setXsj(new UFDouble(objs[0].toString(), 2));
				itemvo.setCgj(new UFDouble(objs[1].toString(), 2));
				UFDouble ce = new UFDouble(objs[2].toString(), 2);
				UFDouble sjfy = new UFDouble(objs[6].toString(), 2);
				UFDouble sjml = new UFDouble(objs[5].toString(), 2);
				itemvo.setCe(ce);
				itemvo.setSjsk(new UFDouble(objs[3].toString(), 2));
				itemvo.setSjfk(new UFDouble(objs[4].toString(), 2));
				itemvo.setSjfy(sjfy);
				itemvo.setSjml(sjml);
				itemvo.setAlertype(1);

				if (itemvo.getXsj().sub(itemvo.getCgj()).sub(sjfy)
						.doubleValue() < 0
						|| sjml.doubleValue() < 0)
					listvo.add(itemvo);
			}

			AlerthtksVO[] itemvos1 = (AlerthtksVO[]) listvo
					.toArray(new AlerthtksVO[0]);

			return itemvos1;
		}
		return null;

	}

	public Object implementReturnObject(Key[] keys, Object currEnvVO,
			UFDate clientLoginDate) throws BusinessException {
		return null;
	}

	public boolean implementWriteFile(Key[] keys, String fileName,
			Object currEnvVO, UFDate clientLoginDate) throws BusinessException {
		return false;
	}

}
