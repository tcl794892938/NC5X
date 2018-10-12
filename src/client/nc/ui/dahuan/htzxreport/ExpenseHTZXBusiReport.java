package nc.ui.dahuan.htzxreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bs.dbcache.intf.IDBCacheBS;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.historyxs.HistoryxsUI;
import nc.ui.report.base.IButtonActionAndState;
import nc.ui.report.base.QueryMxAction;
import nc.ui.report.base.ReportUIBase;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

public class ExpenseHTZXBusiReport extends ReportUIBase {

	private IReportCtl m_uiCtl;

	public ExpenseHTZXBusiReport() {
		super();

	}

	public ExpenseHTZXBusiReport(FramePanel fp) {
		super(fp);
	}

	public boolean getisCondPanlshow() {

		return false;

	}

	protected void onQuery() throws Exception {
		if (getQryDlg().showModal() == QueryDLG.ID_OK) {
			String lastCustomQueryCondition = getQryDlg().getWhereSQL();
			if (lastCustomQueryCondition == null) {
				lastCustomQueryCondition = " 1 = 1 ";
			}
			String pk_corp = getClientEnvironment().getCorporation().pk_corp;
			if (pk_corp != null || !"0001".equals(pk_corp)
					|| !("").equals(pk_corp)) {
				lastCustomQueryCondition += " and gl_detail.pk_corp = '"+pk_corp+"'";
			}
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
					.append("           end) as sjfy,                                                                                                ");
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
					.append("                      end) - sum(case                                                                                   ");
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
					.append("                                 end) as sjml,                                                                          ");
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
					.append("                 bd_accsubj.subjcode || bd_accsubj.subjname    as subjname                                              ");
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
					.append("   and (   (bd_accsubj.subjcode in ('1122', '2203', '2202', '1123')) or                                                 ");
			sql
					.append("       (bd_accsubj.subjcode like '6602%' and                                                                              ");
			
			sql
					.append("       gl_detail.localdebitamount <> 0))                                                                                ");
			sql.append(" and " + lastCustomQueryCondition);
			sql
					.append("   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                                                ");
			sql
					.append(" group by TEMQ_CT.jobname,                                                                                              ");
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
					.append("          bd_accsubj.subjcode || bd_accsubj.subjname                                                                    ");
			sql
					.append(" order by jobname asc, deptname asc, custname asc                                                                       ");

			IUAPQueryBS uapbs = NCLocator.getInstance().lookup(
					IUAPQueryBS.class);
			ArrayList list = (ArrayList) uapbs.executeQuery(sql.toString(),
					new ArrayListProcessor());
			Object clistobj;
			Object[] vobjs;
			String tmp;
			ArrayList<HtzxReportVO> ExpenseHTZXVOList = new ArrayList<HtzxReportVO>();
			HtzxReportVO expensehtzxVo = null;
			if (list.size() != 0) {
				clistobj = list.get(0);
				vobjs = (Object[]) clistobj;
				for (int i = 0; i < list.size(); i++) {
					clistobj = list.get(i);
					vobjs = (Object[]) clistobj;
					expensehtzxVo = new HtzxReportVO();
					tmp = vobjs[0] == null ? " " : vobjs[0].toString();
					expensehtzxVo.setJobname(tmp);
					
					tmp = vobjs[1] == null ? " " : vobjs[1].toString();
					expensehtzxVo.setDef1(tmp);
					tmp = vobjs[2] == null ? " " : vobjs[2].toString();
					expensehtzxVo.setXsj(new UFDouble(tmp));
					tmp = vobjs[3] == null ? " " : vobjs[3].toString();
					expensehtzxVo.setCgj(new UFDouble(tmp));
					tmp = vobjs[4] == null ? " " : vobjs[4].toString();
					expensehtzxVo.setCe(new UFDouble(tmp));
					tmp = vobjs[5] == null ? " " : vobjs[5].toString();
					expensehtzxVo.setDeptname(tmp);
					tmp = vobjs[6] == null ? " " : vobjs[6].toString();
					expensehtzxVo.setCustname(tmp);
					tmp = vobjs[7] == null ? " " : vobjs[7].toString();
					expensehtzxVo.setSjsk(new UFDouble(tmp));
					tmp = vobjs[8] == null ? " " : vobjs[8].toString();
					expensehtzxVo.setSjfk(new UFDouble(tmp));
					tmp = vobjs[9] == null ? " " : vobjs[9].toString();
					expensehtzxVo.setSjfy(new UFDouble(tmp));
					tmp = vobjs[10] == null ? " " : vobjs[10].toString();
					expensehtzxVo.setSjml(new UFDouble(tmp));
					tmp = vobjs[11] == null ? " " : vobjs[11].toString();
					expensehtzxVo.setSjkp(new UFDouble(tmp));
					tmp = vobjs[12] == null ? " " : vobjs[12].toString();
					expensehtzxVo.setSjsp(new UFDouble(tmp));
					tmp = vobjs[13] == null ? " " : vobjs[13].toString();
					expensehtzxVo.setSubjname(tmp);
					ExpenseHTZXVOList.add(expensehtzxVo);

				}

				HashMap listmap = new HashMap();
				for (int i = 0; i < ExpenseHTZXVOList.size(); i++) {
					HtzxReportVO itemvo = ExpenseHTZXVOList.get(i);
					String htcode = itemvo.getJobname();
					if (listmap.containsKey(htcode)) {
						ArrayList listitem = (ArrayList) listmap.get(htcode);
						listitem.add(itemvo);
						listmap.put(htcode, listitem);
					} else {
						ArrayList listitem = new ArrayList();
						listitem.add(itemvo);
						listmap.put(htcode, listitem);
					}
				}

				ArrayList listvo = new ArrayList();

				Iterator iter = listmap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					ArrayList val = (ArrayList) entry.getValue();
					if (val.size() > 1) {
						HtzxReportVO itemvo = new HtzxReportVO();
						for (int i = 0; i < val.size(); i++) {
							HtzxReportVO itemvotmp = (HtzxReportVO) val.get(i);
							String subjname = itemvotmp.getSubjname();
							
							if(!subjname.startsWith("6602"))
								listvo.add(itemvotmp);
						
							itemvo.setJobname(itemvotmp.getJobname());
							itemvo.setDef1(itemvotmp.getDef1());
							UFDouble xsj = itemvo.getXsj() == null ? new UFDouble(
									0.00)
									: itemvo.getXsj();
							UFDouble xsj1 = itemvotmp.getXsj() == null ? new UFDouble(
									0.00)
									: itemvotmp.getXsj();
							itemvo.setXsj(xsj.add(xsj1));

							UFDouble cgj = itemvo.getCgj() == null ? new UFDouble(
									0.00)
									: itemvo.getCgj();
							UFDouble cgj1 = itemvotmp.getCgj() == null ? new UFDouble(
									0.00)
									: itemvotmp.getCgj();
							itemvo.setCgj(cgj.add(cgj1));

							UFDouble ce = itemvo.getCe() == null ? new UFDouble(
									0.00)
									: itemvo.getCe();
							UFDouble ce1 = itemvotmp.getCe() == null ? new UFDouble(
									0.00)
									: itemvotmp.getCe();

							itemvo.setCe(ce.add(ce1));
							
							String Deptname = itemvo.getDeptname()==null?"": itemvo.getDeptname();
							String Deptname1 = itemvotmp.getDeptname()==null?"": itemvotmp.getDeptname();
							
							itemvo.setDeptname(Deptname + " "+ Deptname1);
							
							String custname = itemvo.getCustname()==null?"": itemvo.getCustname();
							String custname1 = itemvotmp.getCustname()==null?"": itemvotmp.getCustname();
							itemvo.setCustname(custname + " "+ custname1);

							UFDouble sjsk = itemvo.getSjsk() == null ? new UFDouble(
									0.00)
									: itemvo.getSjsk();
							UFDouble sjsk1 = itemvotmp.getSjsk() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjsk();
							itemvo.setSjsk(sjsk.add(sjsk1));

							UFDouble sjfk = itemvo.getSjfk() == null ? new UFDouble(
									0.00)
									: itemvo.getSjfk();
							UFDouble sjfk1 = itemvotmp.getSjfk() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjfk();

							itemvo.setSjfk(sjfk.add(sjfk1));

							UFDouble sjfy = itemvo.getSjfy() == null ? new UFDouble(
									0.00)
									: itemvo.getSjfy();
							UFDouble sjfy1 = itemvotmp.getSjfy() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjfy();

							itemvo.setSjfy(sjfy.add(sjfy1));

							UFDouble sjml = itemvo.getSjml() == null ? new UFDouble(
									0.00)
									: itemvo.getSjml();
							UFDouble sjml1 = itemvotmp.getSjml() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjml();
							itemvo.setSjml(sjml.add(sjml1));

							UFDouble sjkp = itemvo.getSjkp() == null ? new UFDouble(
									0.00)
									: itemvo.getSjkp();
							UFDouble sjkp1 = itemvotmp.getSjkp() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjkp();
							itemvo.setSjkp(sjkp.add(sjkp1));

							UFDouble sjsp = itemvo.getSjsp() == null ? new UFDouble(
									0.00)
									: itemvo.getSjsp();
							UFDouble sjsp1 = itemvotmp.getSjsp() == null ? new UFDouble(
									0.00)
									: itemvotmp.getSjsp();

							itemvo.setSjsp(sjsp.add(sjsp1));
							
							String subjname1 = itemvo.getSubjname()==null?"":itemvo.getSubjname();
							
							String subjname2 = itemvotmp.getSubjname()==null?"":itemvotmp.getSubjname();
							
							itemvo.setSubjname(subjname1 + " "+ subjname2);

						}

						listvo.add(itemvo);
					} else {
						listvo.add(val.get(0));
					}
				}

				HtzxReportVO[] itemvos = (HtzxReportVO[]) listvo
						.toArray(new HtzxReportVO[0]);
				
				for (int i = 0; i < itemvos.length; i++) {
					String tmp1 =  itemvos[i].getJobname();
					int htlen = tmp1.indexOf("-");
					String htzname = tmp1.substring(0, htlen);
					itemvos[i].setHtzname(htzname);
					String subjN = itemvos[i].getSubjname();
					if(subjN.trim().startsWith("6602")){
						itemvos[i].setCgj(itemvos[i].getSjfy());
					}
				}
				

				setBodyDataVO(itemvos, false);

			}else{
				setBodyDataVO(null, false);

			}
		}

	}
	
	

	protected void setPrivateButtons() {
		super.setPrivateButtons();
		ButtonObject m_boQuerymx = new ButtonObject("������ϸ", "������ϸ",0,"������ϸ");
		IButtonActionAndState action = new QueryMxAction(this);
		registerButton(m_boQuerymx, action, -1);
		
	}

	public boolean isshowTitlePanel() {
		return false;
	}

	public boolean isshowMenuPanel() {
		return true;
	}

	public IReportCtl getUIControl() {
		if (m_uiCtl == null)
			m_uiCtl = createIReportCtl();
		return m_uiCtl;
	}


	protected void onQueryMx() throws Exception {
		
		int row = this.getReportBase().getBillTable().getSelectedRow();
		HtzxReportVO selectvo = (HtzxReportVO) this.getReportBase().getBillModel().getBodyValueRowVO(row, HtzxReportVO.class.getName()); 
		String jobname = selectvo.getJobname();
		
		String QueryMxSql = "select TEMQ_CT.jobname as jobname,                                                      "
			+"       TEMQ_CT.def1 as def1,                                                            "
			+"       TEMQ_dept.deptname as deptname,                                                  "
			+"       TEMQ_cust.custname as custname,                                                  "
			+"       sum(gl_detail.localcreditamount) as sjsk,                                        "
			+"       sum(case                                                                         "
			+"             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
			+"              gl_detail.localdebitamount                                                "
			+"             else                                                                       "
			+"              0                                                                         "
			+"           end) as sjfk,                                                                "
			+"       sum(case                                                                         "
			+"             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
			+"              0                                                                         "
			+"             else                                                                       "
			+"              gl_detail.localdebitamount                                                "
			+"           end) as sjfy,                                                                "
			+"       sum(gl_detail.localcreditamount) - sum(gl_detail.localdebitamount) as sjml,      "
			+"       bd_accsubj.subjcode || bd_accsubj.subjname as subjname,                          "
			+"       sum(gl_detail.localdebitamount + gl_detail.localcreditamount) as fs,             "
			+"       gl_detail.explanation as explanation,                                            "
			+"       gl_voucher.prepareddate as prepareddate,                                         "
			+"       gl_voucher.no as no,                                                             "
			+"       gl_voucher.period as period                                                      "
			+"  from gl_detail                                                                        "
			+" inner join gl_voucher                                                                  "
			+"    on gl_detail.pk_voucher = gl_voucher.pk_voucher                                     "
			+" inner join gl_freevalue                                                                "
			+"    on gl_detail.assid = gl_freevalue.freevalueid                                       "
			+" inner join bd_accsubj                                                                  "
			+"    on gl_detail.pk_accsubj = bd_accsubj.pk_accsubj                                     "
			+" inner join bd_corp                                                                     "
			+"    on gl_voucher.pk_corp = bd_corp.pk_corp                                             "
			+" right outer join (select bd_jobbasfil.jobname as jobname,                              "
			+"                          bd_jobmngfil.def1 as def1,                                    "
			+"                          bd_jobbasfil.jobcode as jobcode,                              "
			+"                          (select bd_cubasdoc.custname                                  "
			+"                             from bd_cubasdoc                                           "
			+"                            where bd_cubasdoc.pk_cubasdoc =                             "
			+"                                  (select bd_cumandoc.pk_cubasdoc                       "
			+"                                     from bd_cumandoc                                   "
			+"                                    where bd_cumandoc.pk_cumandoc =                     "
			+"                                          bd_jobmngfil.pk_custdoc)) as pk_custdoc,      "
			+"                          (select bd_cubasdoc.custname                                  "
			+"                             from bd_cubasdoc                                           "
			+"                            where bd_cubasdoc.pk_cubasdoc =                             "
			+"                                  (select bd_cumandoc.pk_cubasdoc                       "
			+"                                     from bd_cumandoc                                   "
			+"                                    where bd_cumandoc.pk_cumandoc =                     "
			+"                                          bd_jobmngfil.pk_vendoc)) as pk_vendoc,        "
			+"                          nvl(bd_jobmngfil.def2, 0) * 1 as xsj,                         "
			+"                          nvl(bd_jobmngfil.def3, 0) * 1 as cgj,                         "
			+"                          nvl(bd_jobmngfil.def2, 0) -                                   "
			+"                          nvl(bd_jobmngfil.def3, 0) as ce,                              "
			+"                          bd_jobbasfil.begindate as begindate,                          "
			+"                          (select bd_deptdoc.deptname                                   "
			+"                             from bd_deptdoc                                            "
			+"                            where bd_deptdoc.pk_deptdoc =                               "
			+"                                  bd_jobmngfil.pk_deptdoc) as pk_deptdoc,               "
			+"                          (select bd_psndoc.psnname                                     "
			+"                             from bd_psndoc                                             "
			+"                            where bd_psndoc.pk_psndoc =                                 "
			+"                                  bd_jobmngfil.pk_psndoc) as pk_psndoc,                 "
			+"                          bd_jobmngfil.def4 as def4,                                    "
			+"                          bd_jobmngfil.pk_jobmngfil as pk_jobmngfil,                    "
			+"                          case                                                          "
			+"                            when length(jobcode) <= 10 then                             "
			+"                             jobname                                                    "
			+"                            else                                                        "
			+"                             (substr(jobname,0,length(jobname) - 3)) || '-00'           "
			+"                          end as zht,                                                   "
			+"                          bd_jobmngfil.sealflag as fc,                                  "
			+"                          bd_jobmngfil.pk_jobbasfil as pk_jobbasfil                     "
			+"                     from bd_jobbasfil                                                  "
			+"                    inner join bd_jobmngfil                                             "
			+"                       on bd_jobbasfil.pk_jobbasfil =                                   "
			+"                          bd_jobmngfil.pk_jobbasfil                                     "
			+"                    where (bd_jobbasfil.dr = 0 and bd_jobmngfil.dr = 0 and              "
			+"                          bd_jobbasfil.pk_jobtype = '0001AA100000000013ZG')             "
			+"                    order by jobname asc, pk_deptdoc asc) TEMQ_CT                       "
			+"    on gl_freevalue.checkvalue = TEMQ_CT.pk_jobbasfil                                   "
			+"  left outer join (select gl_freevalue.valuename   as deptname,                         "
			+"                          gl_freevalue.freevalueid as freevalueid                       "
			+"                     from gl_freevalue                                                  "
			+"                    where (gl_freevalue.checktype = '00010000000000000002' and          "
			+"                          gl_freevalue.dr = 0)) TEMQ_dept                               "
			+"    on gl_detail.assid = TEMQ_dept.freevalueid                                          "
			+"  left outer join (select gl_freevalue.valuename   as custname,                         "
			+"                          gl_freevalue.freevalueid as freevalueid                       "
			+"                     from gl_freevalue                                                  "
			+"                    where (gl_freevalue.checktype = '00010000000000000073' and          "
			+"                          gl_freevalue.dr = 0)) TEMQ_cust                               "
			+"    on gl_detail.assid = TEMQ_cust.freevalueid                                          "
			+" where (gl_freevalue.dr = 0 and gl_detail.dr = 0 and gl_voucher.dr = 0 and              "
			+"       gl_voucher.discardflag = 'N' and gl_voucher.errmessage is null and               "
			+"       gl_voucher.period <> '00' and gl_detail.explanation <> '�ڳ�')                   "
			+"   and ((bd_accsubj.subjcode in ('1122', '2203') and                                    "
			+"       gl_detail.localcreditamount <> 0) or                                             "
			+"       ((bd_accsubj.subjcode in ('2202',                                                 "
			+"                                 '1123') or bd_accsubj.subjcode like '6602%') and       "
			+"       gl_detail.localdebitamount <> 0))                                                "
			+"   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                "
			+"   and TEMQ_CT.jobname like '"+jobname+"'"
			+" group by TEMQ_CT.jobname,                                                              "
			+"          TEMQ_CT.def1,                                                                 "
			+"          TEMQ_dept.deptname,                                                           "
			+"          TEMQ_cust.custname,                                                           "
			+"          bd_accsubj.subjcode || bd_accsubj.subjname,                                   "
			+"          gl_detail.explanation,                                                        "
			+"          gl_voucher.prepareddate,                                                      "
			+"          gl_voucher.no,                                                                "
			+"          gl_voucher.period                                                             "
			+" order by jobname asc                                                                   ";
		
	
		
		IDBCacheBS service = (IDBCacheBS) NCLocator.getInstance().lookup(
				IDBCacheBS.class.getName());
		HtzxReportVO[] childvo = ((ArrayList<HtzxReportVO>) service.runSQLQuery(QueryMxSql.toString()
				.toString(), new BeanListProcessor(HtzxReportVO.class)))
				.toArray(new HtzxReportVO[0]);
		
		       String strBillType= "htzxmx";
		       String strCorp= "0001";
		       String strOperator= getClientEnvironment().getInstance().getUser().getPrimaryKey();
		       String strNodekey= null;

				if (childvo != null && childvo.length > 0) {
					HYBillVO aggvo = new HYBillVO();
					aggvo.setParentVO(null);
					aggvo.setChildrenVO(childvo);
					HistoryxsUI historyui = new HistoryxsUI(this,
							strBillType, strCorp, strOperator, strNodekey);
					historyui.getBillCardPanel().getBillModel().setEnabled(
							false);
					historyui.setVO(aggvo);
					historyui.showModal();
				}
				  }

	@Override
	protected void onClearItemyj() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onQueryYcht() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onQueryFpMx() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onQueryFh() throws Exception {
		
		
	}

	@Override
	protected void onQueryZb() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onQueryYCPZ() throws Exception {
		// TODO Auto-generated method stub
		
	}
}