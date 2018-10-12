package nc.ui.dahuan.htzxreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dbcache.intf.IDBCacheBS;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.historyxs.HistoryxsUI;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.report.base.IButtonActionAndState;
import nc.ui.report.base.QueryFpMxAction;
import nc.ui.report.base.QueryMxAction;
import nc.ui.report.base.ReportUIBase;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.dahuan.report.HtmxReportVO;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.trade.pub.HYBillVO;

public class HTZXLYReportUI extends ReportUIBase {

	private static final long serialVersionUID = 1L;

	private IReportCtl m_uiCtl;

	public HTZXLYReportUI() {
		super();

	}

	public HTZXLYReportUI(FramePanel fp) {
		super(fp);
	}

	public boolean getisCondPanlshow() {

		return false;

	}

	protected void onQuery() throws Exception {
		
		
		//������ʶ��ӯ����
		String zdy_fhbs="";
		String zdy_yll="";

		if (getQryDlg().showModal() == QueryDLG.ID_OK) {
			String lastCustomQueryCondition = getQryDlg().getWhereSQL();
			if (lastCustomQueryCondition == null) {
				lastCustomQueryCondition = " 1 = 1 ";
			}
			
			if(lastCustomQueryCondition.indexOf("zdy.yll")!=-1){//��������ӯ����
				int t=lastCustomQueryCondition.indexOf("zdy.yll");
				int t2=lastCustomQueryCondition.substring(t).indexOf(")");
				zdy_yll=lastCustomQueryCondition.substring(t).substring(0, t2);
				lastCustomQueryCondition = lastCustomQueryCondition.substring(0,t-5)+lastCustomQueryCondition.substring(t+t2+1);
			}
			
			if(lastCustomQueryCondition.indexOf("zdy.fhbz")!=-1){ //��������������ʶ
				int t=lastCustomQueryCondition.indexOf("zdy.fhbz");
				int t2=lastCustomQueryCondition.substring(t).indexOf(")");
				zdy_fhbs=lastCustomQueryCondition.substring(t).substring(0, t2);
				lastCustomQueryCondition = lastCustomQueryCondition.substring(0,t-5)+lastCustomQueryCondition.substring(t+t2+1);
			}
			


			String node = getQryDlg().getCurFunCode();
			if (node.equalsIgnoreCase("12H20301")) {
				int num = lastCustomQueryCondition.indexOf("pk_deptdoc");
				if (num <= 0) {
					this.showErrorMessage("��ѡ������Ϣ��ѯ!");
					return;
				}
			}

			nc.vo.pub.query.ConditionVO[] condvos = getQryDlg()
					.getConditionVO();
			for (int i = 0; i < condvos.length; i++) {
				if (condvos[i].getFieldCode().equalsIgnoreCase(
						"TEMQ_CT.pk_deptdoc1")) {
					String pk_dept = condvos[i].getValue();
					if (pk_dept.equalsIgnoreCase("�Զ�����")) {
						lastCustomQueryCondition = lastCustomQueryCondition
								.replaceAll("TEMQ_CT.pk_deptdoc1",
										"TEMQ_CT.pk_deptdoc");
					}

				}

			}

			StringBuffer sql = new StringBuffer();
			sql
					.append(" select TEMQ_CT.jobname as jobname,TEMQ_CT.def1 as def1,TEMQ_CT.xsj as xsj,TEMQ_CT.cgj,TEMQ_CT.ce as ce,                            ");
			sql
					.append(" TEMQ_dept.deptname as deptname,TEMQ_cust.custname as custname,sum(case when bd_accsubj.subjcode = '1122' or            ");
			sql
					.append(" bd_accsubj.subjcode = '2203' then gl_detail.localcreditamount else 0 end) as sjsk,                                     ");
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
					.append("                  bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203'  then                                     ");
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
					.append("                                        bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203'  then               ");
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
					.append(" bd_accsubj.subjcode || bd_accsubj.subjname    as subjname , TEMQ_CT.jobcode as jobcode,TEMQ_CT.is_delivery ,TEMQ_CT.def10 ,TEMQ_CT.unitname    ");
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
					.append("  left outer join (select bd_deptdoc.deptname   as deptname,                                                         ");
			sql
					.append("                          gl_freevalue.freevalueid as freevalueid                                                       ");
			sql
					.append("                     from gl_freevalue,bd_deptdoc                                                                                  ");
			sql
					.append("                    where (gl_freevalue.checktype = '00010000000000000002' and gl_freevalue.dr = 0                  ");
			sql
					.append("                           and bd_deptdoc.pk_deptdoc = gl_freevalue.checkvalue )) TEMQ_dept                                                               ");
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
					.append("       (bd_accsubj.subjcode like '6602%' and                                                                              ");

			sql
					.append("       gl_detail.localdebitamount <> 0))                                                                                ");
			sql.append(" and " + lastCustomQueryCondition);
			sql
					.append("   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                                                ");
			sql
					.append(" group by  TEMQ_CT.jobcode,TEMQ_CT.jobname,                                                                                              ");
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
					.append("          bd_accsubj.subjcode || bd_accsubj.subjname,TEMQ_CT.is_delivery ,TEMQ_CT.def10     ,TEMQ_CT.def12  ,TEMQ_CT.unitname                                        ");
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
			HashMap listjsmap = new HashMap();

			String htname = "";

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

					if (expensehtzxVo.getDef1().equalsIgnoreCase("���ǲɹ�")) {
						expensehtzxVo.setCgj(expensehtzxVo.getSjfk());
						expensehtzxVo.setCe(expensehtzxVo.getSjfk()
								.multiply(-1));
					}

					if (expensehtzxVo.getJobname().endsWith("-����")) {
						expensehtzxVo.setCe(expensehtzxVo.getSjfy()
								.multiply(-1));
					}

					if (expensehtzxVo.getJobname().endsWith("-�ɱ�")) {
						expensehtzxVo.setCgj(expensehtzxVo.getSjfy());
						expensehtzxVo.setCe(expensehtzxVo.getSjfy()
								.multiply(-1));
					}

					expensehtzxVo.setSubjname(tmp);

					tmp = vobjs[14] == null ? " " : vobjs[14].toString();
					expensehtzxVo.setJobcode(tmp);

					int isdel = vobjs[15] == null ? 0 : (Integer) vobjs[15];
					if (1 == isdel) {
						expensehtzxVo.setIffh("�ѷ���");
					} else {
						expensehtzxVo.setIffh("δ����");
					}

					String projectname = vobjs[16] == null ? " " : vobjs[16]
							.toString();

					expensehtzxVo.setProjectname(projectname);

					String unitname = vobjs[17] == null ? " " : vobjs[17]
							.toString();

					expensehtzxVo.setUnitname(unitname);

					ExpenseHTZXVOList.add(expensehtzxVo);

				}

				HashMap listmap = new HashMap();

				List<String> pxlit = new ArrayList<String>();

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
						pxlit.add(htcode);
					}
				}

				ArrayList listvo = new ArrayList();

				UFDouble allXsj = new UFDouble(0.00);
				UFDouble allCgj = new UFDouble(0.00);
				UFDouble allCe = new UFDouble(0.00);
				UFDouble allSjsk = new UFDouble(0.00);
				UFDouble allSjfk = new UFDouble(0.00);
				UFDouble allSjfy = new UFDouble(0.00);
				UFDouble allSjml = new UFDouble(0.00);
				UFDouble allSjkp = new UFDouble(0.00);
				UFDouble allSjsp = new UFDouble(0.00);

				for (String mapcode : pxlit) {
					ArrayList val = (ArrayList) listmap.get(mapcode);
					if (val.size() > 1) {
						HtzxReportVO itemvo = new HtzxReportVO();
						for (int i = 0; i < val.size(); i++) {
							HtzxReportVO itemvotmp = (HtzxReportVO) val.get(i);
							String subjname = itemvotmp.getSubjname();
							String ctname = itemvotmp.getDef1();

							if (!ctname.equalsIgnoreCase("���ǲɹ�")
									&& !itemvotmp.getJobname().endsWith("-����")
									&& !itemvotmp.getJobname().endsWith("-�ɱ�")) {
								listvo.add(itemvotmp);
								continue;
							}
							itemvo.setJobname(itemvotmp.getJobname());
							itemvo.setUnitname(itemvotmp.getUnitname());
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

							String Deptname = itemvo.getDeptname() == null ? ""
									: itemvo.getDeptname();
							String Deptname1 = itemvotmp.getDeptname() == null ? ""
									: itemvotmp.getDeptname();
							itemvo.setDeptname(Deptname + " " + Deptname1);

							String custname = itemvo.getCustname() == null ? ""
									: itemvo.getCustname();
							String custname1 = itemvotmp.getCustname() == null ? ""
									: itemvotmp.getCustname();
							itemvo.setCustname(custname + " " + custname1);

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

							String subjname1 = itemvo.getSubjname() == null ? ""
									: itemvo.getSubjname();
							String subjname2 = itemvotmp.getSubjname() == null ? ""
									: itemvotmp.getSubjname();
							itemvo.setSubjname(subjname1 + " " + subjname2);

							itemvo.setIffh(itemvotmp.getIffh());

						}
						if (itemvo.getJobname() != null) {
							allXsj = itemvo.getXsj().add(allXsj);
							allCgj = itemvo.getCgj().add(allCgj);
							allCe = itemvo.getCe().add(allCe);
							allSjsk = itemvo.getSjsk().add(allSjsk);
							allSjfk = itemvo.getSjfk().add(allSjfk);
							allSjfy = itemvo.getSjfy().add(allSjfy);
							allSjml = itemvo.getSjml().add(allSjml);
							allSjkp = itemvo.getSjkp().add(allSjkp);
							allSjsp = itemvo.getSjsp().add(allSjsp);

							listvo.add(itemvo);
						}
					} else {

						HtzxReportVO itemvoly = (HtzxReportVO) val.get(0);

						allXsj = itemvoly.getXsj().add(allXsj);
						allCgj = itemvoly.getCgj().add(allCgj);
						allCe = itemvoly.getCe().add(allCe);
						allSjsk = itemvoly.getSjsk().add(allSjsk);
						allSjfk = itemvoly.getSjfk().add(allSjfk);
						allSjfy = itemvoly.getSjfy().add(allSjfy);
						allSjml = itemvoly.getSjml().add(allSjml);
						allSjkp = itemvoly.getSjkp().add(allSjkp);
						allSjsp = itemvoly.getSjsp().add(allSjsp);

						listvo.add(itemvoly);
					}
				}

				HtzxReportVO[] itemvos = (HtzxReportVO[]) listvo
						.toArray(new HtzxReportVO[0]);

				ArrayList listcodes = new ArrayList();
				for (int i = 0; i < itemvos.length; i++) {
					String tmp1 = itemvos[i].getJobname();
					int htlen = tmp1.indexOf("-");
					String htzname = tmp1.substring(0, htlen);
					itemvos[i].setHtzname(htzname);
					String subjN = itemvos[i].getSubjname();
					if (subjN.trim().startsWith("6602")) {
						itemvos[i].setCgj(itemvos[i].getSjfy());
					}

					listcodes.add(tmp1);
				}

				HashMap htzx = new HashMap();
				for (int i = 0; i < itemvos.length; i++) {
					String htzname = itemvos[i].getHtzname();
					if (!htzx.containsKey(htzname)) {
						ArrayList listtmp = new ArrayList();
						listtmp.add(itemvos[i]);
						htzx.put(htzname, listtmp);
					} else {
						ArrayList listtmp1 = (ArrayList) htzx.get(htzname);
						listtmp1.add(itemvos[i]);
						htzx.put(htzname, listtmp1);
					}
				}
				
				HashMap htxsnames =  queryAlertHttjxs();

				Iterator iter = htzx.entrySet().iterator();
				ArrayList listvos = new ArrayList();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String htzname = (String) entry.getKey();
					ArrayList listitems = (ArrayList) htzx.get(htzname);
					HtzxReportVO itemvo = new HtzxReportVO();
					for (int i = 0; i < listitems.size(); i++) {
						HtzxReportVO tmpvo = (HtzxReportVO) listitems.get(i);
						String jobname = tmpvo.getJobname();
						if (jobname.endsWith("-00")) {
							itemvo = tmpvo;
						} else {
							
							if(itemvo.getJobname()==null){
								String[] names = (String[]) htxsnames.get(htzname+"-00");
								if (names != null) {
									itemvo.setJobcode(names[4]);
									itemvo.setDef1(names[0]);
									itemvo.setJobname(htzname+"-00");
									itemvo.setCustname(names[1]);
									itemvo.setDeptname(names[2]);
									itemvo.setHtzname(htzname);
									itemvo.setProjectname(names[5]);
									
								}else{
									itemvo.setJobname(htzname+"-00");
								}
							}
							
							UFDouble xsje = itemvo.getXsj() == null ? new UFDouble(
									0.00)
									: itemvo.getXsj();
							UFDouble cgje = itemvo.getCgj() == null ? new UFDouble(
									0.00)
									: itemvo.getCgj();
							UFDouble chml = itemvo.getCe() == null ? new UFDouble(
									0.00)
									: itemvo.getCe();
							UFDouble xsje1 = tmpvo.getXsj() == null ? new UFDouble(
									0.00)
									: tmpvo.getXsj();
							UFDouble cgje2 = tmpvo.getCgj() == null ? new UFDouble(
									0.00)
									: tmpvo.getCgj();
							UFDouble chml3 = tmpvo.getCe() == null ? new UFDouble(
									0.00)
									: tmpvo.getCe();
							itemvo.setXsj(xsje.add(xsje1));
							itemvo.setCgj(cgje.add(cgje2));
							itemvo.setCe(chml.add(chml3));
							
							UFDouble html = new UFDouble((chml.add(chml3)).div(xsje.add(xsje1)).multiply(100).toString(),2);
							
							itemvo.setHtml(html);
							
						}
					}

					listvos.add(itemvo);
				}
				
				//��listvos������ by tcl
				
				listvos= this.doFilterData(zdy_fhbs, zdy_yll, listvos);

				HtzxReportVO[] newitemvos = (HtzxReportVO[]) listvos
						.toArray(new HtzxReportVO[0]);
				

				setBodyDataVO(newitemvos, false);

			} else {
				setBodyDataVO(null, false);

			}
		}

	}

	protected void onClearItemyj() throws Exception {
		HtzxReportVO[] itemvos = (HtzxReportVO[]) this.getReportBase()
				.getBodyDataVO();
		ArrayList list = new ArrayList();
		String jobname = "";

		if (itemvos == null || itemvos.length <= 0) {
			this.showErrorMessage("��������Ϊ�գ������Թ��ˣ�");
			return;
		}

		for (int j = 0; j < itemvos.length; j++) {
			jobname = itemvos[j].getJobname() == null ? "" : itemvos[j]
					.getJobname();
			if (jobname.endsWith("-00")) {
				UFDouble nxsje = itemvos[j].getXsj();
				UFDouble nsjsk = itemvos[j].getSjsk();
				UFDouble nsjkp = itemvos[j].getSjkp();
				if (nxsje.doubleValue() == nsjsk.doubleValue()
						&& nsjsk.doubleValue() == nsjkp.doubleValue()) {

				} else {
					list.add(itemvos[j]);
				}
			} else {
				UFDouble ncgje = itemvos[j].getCgj();
				UFDouble nsjfk = itemvos[j].getSjfk();
				UFDouble nsjsp = itemvos[j].getSjsp();
				if (ncgje.doubleValue() == nsjfk.doubleValue()
						&& nsjfk.doubleValue() == nsjsp.doubleValue()) {

				} else {
					list.add(itemvos[j]);
				}
			}
		}
		HtzxReportVO[] newitemvos = (HtzxReportVO[]) list
				.toArray(new HtzxReportVO[0]);

		setBodyDataVO(newitemvos, false);

	}

	protected void onQueryYcht() throws Exception {
		HtzxReportVO[] itemvos = (HtzxReportVO[]) this.getReportBase()
				.getBodyDataVO();
		ArrayList list = new ArrayList();
		String jobname = "";

		if (itemvos == null || itemvos.length <= 0) {
			this.showErrorMessage("��������Ϊ�գ���������ʾ��");
			return;
		}

		for (int j = 0; j < itemvos.length; j++) {
			jobname = itemvos[j].getJobname() == null ? "" : itemvos[j]
					.getJobname();
			if (!jobname.endsWith("-����")) {
				if (jobname.endsWith("-00")) {
					UFDouble nxsje = itemvos[j].getXsj();
					UFDouble nsjsk = itemvos[j].getSjsk();
					UFDouble nsjkp = itemvos[j].getSjkp();
					if (nsjsk.doubleValue() == 0 && nsjkp.doubleValue() == 0) {
						list.add(itemvos[j]);
					}
				} else {
					UFDouble ncgje = itemvos[j].getCgj();
					UFDouble nsjfk = itemvos[j].getSjfk();
					UFDouble nsjsp = itemvos[j].getSjsp();
					if (nsjfk.doubleValue() == 0
							&& nsjsp.doubleValue() == 0
							&& (itemvos[j].getIffh() == null || itemvos[j]
									.getIffh().equalsIgnoreCase("N"))) {
						list.add(itemvos[j]);
					}
				}
			}
		}
		HtzxReportVO[] newitemvos = (HtzxReportVO[]) list
				.toArray(new HtzxReportVO[0]);

		setBodyDataVO(newitemvos, false);

	}

	protected void setPrivateButtons() {
		super.setPrivateButtons();
		ButtonObject m_boQuerymx = new ButtonObject("�ո�������ϸ", "�ո�������ϸ", 0,
				"�ո�������ϸ");
		ButtonObject m_boQueryfpmx = new ButtonObject("��Ʊ������ϸ", "��Ʊ������ϸ", 0,
				"��Ʊ������ϸ");
		//ButtonObject m_boQueryll = new ButtonObject("����", "����", 0,"����");
		IButtonActionAndState action = new QueryMxAction(this);
		IButtonActionAndState action3 = new QueryFpMxAction(this);
		//IButtonActionAndState action3 = new QueryFpMxAction(this);
		registerButton(m_boQuerymx, action, -1);
		registerButton(m_boQueryfpmx, action3, -1);
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
		HtzxReportVO selectvo = (HtzxReportVO) this.getReportBase()
				.getBillModel().getBodyValueRowVO(row,
						HtzxReportVO.class.getName());
		if (selectvo == null) {
			this.showErrorMessage("����ѡ��Ҫ�鿴��ϸ������!");
			return;
		}

		String jobname = selectvo.getJobname();
		String QueryMxSql = "select TEMQ_CT.jobname as jobname,                                                      "
				+ "       TEMQ_CT.def1 as def1,                                                            "
				+ "       TEMQ_dept.deptname as deptname,                                                  "
				+ "       TEMQ_cust.custname as custname,                                                  "
				+ "       sum(gl_detail.localcreditamount) as sjsk,                                        "
				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              gl_detail.localdebitamount                                                "
				+ "             else                                                                       "
				+ "              0                                                                         "
				+ "           end) as sjfk,                                                                "
				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              0                                                                         "
				+ "             else                                                                       "
				+ "              gl_detail.localdebitamount                                                "
				+ "           end) as sjfy,                                                                "

				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then     "
				+ "             gl_detail.localcreditamount                                            "
				+ "             else                                                                       "
				+ "              0                                                                         "
				+ "           end) as sjkp,                                                                "

				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              0                                                                         "
				+ "             else                                                                       "
				+ "              gl_detail.localdebitamount                                              "
				+ "           end) as sjsp,                                                                "

				+ "       sum(gl_detail.localcreditamount) - sum(gl_detail.localdebitamount) as sjml,      "
				+ "       bd_accsubj.subjcode || bd_accsubj.subjname as subjname,                          "
				+ "       sum(gl_detail.localdebitamount + gl_detail.localcreditamount) as fs,             "
				+ "       gl_detail.explanation as explanation,                                            "
				+ "       gl_voucher.prepareddate as prepareddate,                                         "
				+ "       gl_voucher.no as no,                                                             "
				+ "       gl_voucher.period as period                                                      "
				+ "  from gl_detail                                                                        "
				+ " inner join gl_voucher                                                                  "
				+ "    on gl_detail.pk_voucher = gl_voucher.pk_voucher                                     "
				+ " inner join gl_freevalue                                                                "
				+ "    on gl_detail.assid = gl_freevalue.freevalueid                                       "
				+ " inner join bd_accsubj                                                                  "
				+ "    on gl_detail.pk_accsubj = bd_accsubj.pk_accsubj                                     "
				+ " inner join bd_corp                                                                     "
				+ "    on gl_voucher.pk_corp = bd_corp.pk_corp                                             "
				+ " right outer join (select bd_jobbasfil.jobname as jobname,                              "
				+ "                          bd_jobmngfil.def1 as def1,                                    "
				+ "                          bd_jobbasfil.jobcode as jobcode,                              "
				+ "                          (select bd_cubasdoc.custname                                  "
				+ "                             from bd_cubasdoc                                           "
				+ "                            where bd_cubasdoc.pk_cubasdoc =                             "
				+ "                                  (select bd_cumandoc.pk_cubasdoc                       "
				+ "                                     from bd_cumandoc                                   "
				+ "                                    where bd_cumandoc.pk_cumandoc =                     "
				+ "                                          bd_jobmngfil.pk_custdoc)) as pk_custdoc,      "
				+ "                          (select bd_cubasdoc.custname                                  "
				+ "                             from bd_cubasdoc                                           "
				+ "                            where bd_cubasdoc.pk_cubasdoc =                             "
				+ "                                  (select bd_cumandoc.pk_cubasdoc                       "
				+ "                                     from bd_cumandoc                                   "
				+ "                                    where bd_cumandoc.pk_cumandoc =                     "
				+ "                                          bd_jobmngfil.pk_vendoc)) as pk_vendoc,        "
				+ "                          nvl(bd_jobmngfil.def2, 0) * 1 as xsj,                         "
				+ "                          nvl(bd_jobmngfil.def3, 0) * 1 as cgj,                         "
				+ "                          nvl(bd_jobmngfil.def2, 0) -                                   "
				+ "                          nvl(bd_jobmngfil.def3, 0) as ce,                              "
				+ "                          bd_jobbasfil.begindate as begindate,                          "
				+ "                          (select bd_deptdoc.deptname                                   "
				+ "                             from bd_deptdoc                                            "
				+ "                            where bd_deptdoc.pk_deptdoc =                               "
				+ "                                  bd_jobmngfil.pk_deptdoc) as pk_deptdoc,               "
				+ "                          (select bd_psndoc.psnname                                     "
				+ "                             from bd_psndoc                                             "
				+ "                            where bd_psndoc.pk_psndoc =                                 "
				+ "                                  bd_jobmngfil.pk_psndoc) as pk_psndoc,                 "
				+ "                          bd_jobmngfil.def4 as def4,                                    "
				+ "                          bd_jobmngfil.pk_jobmngfil as pk_jobmngfil,                    "
				+ "                          case                                                          "
				+ "                            when length(jobcode) <= 10 then                             "
				+ "                             jobname                                                    "
				+ "                            else                                                        "
				+ "                             (substr(jobname,0,length(jobname) - 3)) || '-00'           "
				+ "                          end as zht,                                                   "
				+ "                          bd_jobmngfil.sealflag as fc,                                  "
				+ "                          bd_jobmngfil.pk_jobbasfil as pk_jobbasfil                     "
				+ "                     from bd_jobbasfil                                                  "
				+ "                    inner join bd_jobmngfil                                             "
				+ "                       on bd_jobbasfil.pk_jobbasfil =                                   "
				+ "                          bd_jobmngfil.pk_jobbasfil                                     "
				+ "                    where (bd_jobbasfil.dr = 0 and bd_jobmngfil.dr = 0 and              "
				+ "                          bd_jobbasfil.pk_jobtype = '0001AA100000000013ZG')             "
				+ "                    order by jobname asc, pk_deptdoc asc) TEMQ_CT                       "
				+ "    on gl_freevalue.checkvalue = TEMQ_CT.pk_jobbasfil                                   "
				+ "  left outer join (select gl_freevalue.valuename   as deptname,                         "
				+ "                          gl_freevalue.freevalueid as freevalueid                       "
				+ "                     from gl_freevalue                                                  "
				+ "                    where (gl_freevalue.checktype = '00010000000000000002' and          "
				+ "                          gl_freevalue.dr = 0)) TEMQ_dept                               "
				+ "    on gl_detail.assid = TEMQ_dept.freevalueid                                          "
				+ "  left outer join (select gl_freevalue.valuename   as custname,                         "
				+ "                          gl_freevalue.freevalueid as freevalueid                       "
				+ "                     from gl_freevalue                                                  "
				+ "                    where (gl_freevalue.checktype = '00010000000000000073' and          "
				+ "                          gl_freevalue.dr = 0)) TEMQ_cust                               "
				+ "    on gl_detail.assid = TEMQ_cust.freevalueid                                          "
				+ " where (gl_freevalue.dr = 0 and gl_detail.dr = 0 and gl_voucher.dr = 0 and              "
				+ "       gl_voucher.discardflag = 'N' and gl_voucher.errmessage is null and               "
				+ "       gl_voucher.period <> '00' and gl_detail.explanation <> '�ڳ�')                   "
				+ "   and ((bd_accsubj.subjcode in ('1122', '2203') and                                    "
				+ "       gl_detail.localcreditamount <> 0) or                                             "
				+ "       ((bd_accsubj.subjcode in ('2202',                                                "
				+ "                                 '1123','50011201') or bd_accsubj.subjcode like '6602%') and       "
				+ "       gl_detail.localdebitamount <> 0))                                                "
				+ "   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                "
				+ "   and TEMQ_CT.jobname like '"
				+ jobname
				+ "'"
				+ " group by TEMQ_CT.jobname,                                                              "
				+ "          TEMQ_CT.def1,                                                                 "
				+ "          TEMQ_dept.deptname,                                                           "
				+ "          TEMQ_cust.custname,                                                           "
				+ "          bd_accsubj.subjcode || bd_accsubj.subjname,                                   "
				+ "          gl_detail.explanation,                                                        "
				+ "          gl_voucher.prepareddate,                                                      "
				+ "          gl_voucher.no,                                                                "
				+ "          gl_voucher.period                                                             "
				+ " order by jobname asc                                                                   ";

		IDBCacheBS service = (IDBCacheBS) NCLocator.getInstance().lookup(
				IDBCacheBS.class.getName());

		int num = (int) (Math.random() * 9000000 + 1000000);

		HtzxReportVO[] childvo = ((ArrayList<HtzxReportVO>) service
				.runSQLQuery(QueryMxSql.toString().toString(),
						new BeanListProcessor(HtzxReportVO.class)))
				.toArray(new HtzxReportVO[0]);

		String strBillType = "htzxmx";
		String strCorp = "0001";
		String strOperator = getClientEnvironment().getInstance().getUser()
				.getPrimaryKey();
		String strNodekey = null;

		if (childvo != null && childvo.length > 0) {
			HYBillVO aggvo = new HYBillVO();
			aggvo.setParentVO(null);
			aggvo.setChildrenVO(childvo);
			HistoryxsUI historyui = new HistoryxsUI(this, strBillType, strCorp,
					strOperator, strNodekey);
			historyui.getBillCardPanel().getBillModel().setEnabled(false);
			historyui.setVO(aggvo);
			historyui.showModal();
		}
	}

	protected void onQueryFpMx() throws Exception {

		int row = this.getReportBase().getBillTable().getSelectedRow();
		HtzxReportVO selectvo = (HtzxReportVO) this.getReportBase()
				.getBillModel().getBodyValueRowVO(row,
						HtzxReportVO.class.getName());
		if (selectvo == null) {
			this.showErrorMessage("����ѡ��Ҫ�鿴��ϸ������!");
			return;
		}

		String jobname = selectvo.getJobname();
		String QueryMxSql = "select TEMQ_CT.jobname as jobname,                                                      "
				+ "       TEMQ_CT.def1 as def1,                                                            "
				+ "       TEMQ_dept.deptname as deptname,                                                  "
				+ "       TEMQ_cust.custname as custname,                                                  "
				+ "       sum(gl_detail.localcreditamount) as sjsk,                                        "
				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              gl_detail.localdebitamount                                                "
				+ "             else                                                                       "
				+ "              0                                                                         "
				+ "           end) as sjfk,                                                                "
				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              0                                                                         "
				+ "             else                                                                       "
				+ "              gl_detail.localdebitamount                                                "
				+ "           end) as sjfy,                                                                "

				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then     "
				+ "             gl_detail.localdebitamount                                             "
				+ "             else                                                                       "
				+ "              0                                                                         "
				+ "           end) as sjkp,                                                                "

				+ "       sum(case                                                                         "
				+ "             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then     "
				+ "              gl_detail.localcreditamount                                                                    "
				+ "             else                                                                       "
				+ "             0                                "
				+ "           end) as sjsp,                                                                "

				+ "       sum(gl_detail.localcreditamount) - sum(gl_detail.localdebitamount) as sjml,      "
				+ "       bd_accsubj.subjcode || bd_accsubj.subjname as subjname,                          "
				+ "       sum(gl_detail.localdebitamount + gl_detail.localcreditamount) as fs,             "
				+ "       gl_detail.explanation as explanation,                                            "
				+ "       gl_voucher.prepareddate as prepareddate,                                         "
				+ "       gl_voucher.no as no,                                                             "
				+ "       gl_voucher.period as period                                                      "
				+ "  from gl_detail                                                                        "
				+ " inner join gl_voucher                                                                  "
				+ "    on gl_detail.pk_voucher = gl_voucher.pk_voucher                                     "
				+ " inner join gl_freevalue                                                                "
				+ "    on gl_detail.assid = gl_freevalue.freevalueid                                       "
				+ " inner join bd_accsubj                                                                  "
				+ "    on gl_detail.pk_accsubj = bd_accsubj.pk_accsubj                                     "
				+ " inner join bd_corp                                                                     "
				+ "    on gl_voucher.pk_corp = bd_corp.pk_corp                                             "
				+ " right outer join (select bd_jobbasfil.jobname as jobname,                              "
				+ "                          bd_jobmngfil.def1 as def1,                                    "
				+ "                          bd_jobbasfil.jobcode as jobcode,                              "
				+ "                          (select bd_cubasdoc.custname                                  "
				+ "                             from bd_cubasdoc                                           "
				+ "                            where bd_cubasdoc.pk_cubasdoc =                             "
				+ "                                  (select bd_cumandoc.pk_cubasdoc                       "
				+ "                                     from bd_cumandoc                                   "
				+ "                                    where bd_cumandoc.pk_cumandoc =                     "
				+ "                                          bd_jobmngfil.pk_custdoc)) as pk_custdoc,      "
				+ "                          (select bd_cubasdoc.custname                                  "
				+ "                             from bd_cubasdoc                                           "
				+ "                            where bd_cubasdoc.pk_cubasdoc =                             "
				+ "                                  (select bd_cumandoc.pk_cubasdoc                       "
				+ "                                     from bd_cumandoc                                   "
				+ "                                    where bd_cumandoc.pk_cumandoc =                     "
				+ "                                          bd_jobmngfil.pk_vendoc)) as pk_vendoc,        "
				+ "                          nvl(bd_jobmngfil.def2, 0) * 1 as xsj,                         "
				+ "                          nvl(bd_jobmngfil.def3, 0) * 1 as cgj,                         "
				+ "                          nvl(bd_jobmngfil.def2, 0) -                                   "
				+ "                          nvl(bd_jobmngfil.def3, 0) as ce,                              "
				+ "                          bd_jobbasfil.begindate as begindate,                          "
				+ "                          (select bd_deptdoc.deptname                                   "
				+ "                             from bd_deptdoc                                            "
				+ "                            where bd_deptdoc.pk_deptdoc =                               "
				+ "                                  bd_jobmngfil.pk_deptdoc) as pk_deptdoc,               "
				+ "                          (select bd_psndoc.psnname                                     "
				+ "                             from bd_psndoc                                             "
				+ "                            where bd_psndoc.pk_psndoc =                                 "
				+ "                                  bd_jobmngfil.pk_psndoc) as pk_psndoc,                 "
				+ "                          bd_jobmngfil.def4 as def4,                                    "
				+ "                          bd_jobmngfil.pk_jobmngfil as pk_jobmngfil,                    "
				+ "                          case                                                          "
				+ "                            when length(jobcode) <= 10 then                             "
				+ "                             jobname                                                    "
				+ "                            else                                                        "
				+ "                             (substr(jobname,0,length(jobname) - 3)) || '-00'           "
				+ "                          end as zht,                                                   "
				+ "                          bd_jobmngfil.sealflag as fc,                                  "
				+ "                          bd_jobmngfil.pk_jobbasfil as pk_jobbasfil                     "
				+ "                     from bd_jobbasfil                                                  "
				+ "                    inner join bd_jobmngfil                                             "
				+ "                       on bd_jobbasfil.pk_jobbasfil =                                   "
				+ "                          bd_jobmngfil.pk_jobbasfil                                     "
				+ "                    where (bd_jobbasfil.dr = 0 and bd_jobmngfil.dr = 0 and              "
				+ "                          bd_jobbasfil.pk_jobtype = '0001AA100000000013ZG')             "
				+ "                    order by jobname asc, pk_deptdoc asc) TEMQ_CT                       "
				+ "    on gl_freevalue.checkvalue = TEMQ_CT.pk_jobbasfil                                   "
				+ "  left outer join (select gl_freevalue.valuename   as deptname,                         "
				+ "                          gl_freevalue.freevalueid as freevalueid                       "
				+ "                     from gl_freevalue                                                  "
				+ "                    where (gl_freevalue.checktype = '00010000000000000002' and          "
				+ "                          gl_freevalue.dr = 0)) TEMQ_dept                               "
				+ "    on gl_detail.assid = TEMQ_dept.freevalueid                                          "
				+ "  left outer join (select gl_freevalue.valuename   as custname,                         "
				+ "                          gl_freevalue.freevalueid as freevalueid                       "
				+ "                     from gl_freevalue                                                  "
				+ "                    where (gl_freevalue.checktype = '00010000000000000073' and          "
				+ "                          gl_freevalue.dr = 0)) TEMQ_cust                               "
				+ "    on gl_detail.assid = TEMQ_cust.freevalueid                                          "
				+ " where (gl_freevalue.dr = 0 and gl_detail.dr = 0 and gl_voucher.dr = 0 and              "
				+ "       gl_voucher.discardflag = 'N' and gl_voucher.errmessage is null and               "
				+ "       gl_voucher.period <> '00' and gl_detail.explanation <> '�ڳ�')                   "
				+ "   and ((bd_accsubj.subjcode in ('1122', '2203') and                                    "
				+ "       gl_detail.localdebitamount <> 0) or                                             "
				+ "       ((bd_accsubj.subjcode in ('2202',                                                "
				+ "                                 '1123') ) and       "
				+ "       gl_detail.localcreditamount  <> 0))                                                "
				+ "   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                "
				+ "   and TEMQ_CT.jobname like '"
				+ jobname
				+ "'"
				+ " group by TEMQ_CT.jobname,                                                              "
				+ "          TEMQ_CT.def1,                                                                 "
				+ "          TEMQ_dept.deptname,                                                           "
				+ "          TEMQ_cust.custname,                                                           "
				+ "          bd_accsubj.subjcode || bd_accsubj.subjname,                                   "
				+ "          gl_detail.explanation,                                                        "
				+ "          gl_voucher.prepareddate,                                                      "
				+ "          gl_voucher.no,                                                                "
				+ "          gl_voucher.period                                                             "
				+ " order by jobname asc                                                                   ";

		IDBCacheBS service = (IDBCacheBS) NCLocator.getInstance().lookup(
				IDBCacheBS.class.getName());

		int num = (int) (Math.random() * 9000000 + 1000000);

		HtzxReportVO[] childvo = ((ArrayList<HtzxReportVO>) service
				.runSQLQuery(QueryMxSql.toString().toString(),
						new BeanListProcessor(HtzxReportVO.class)))
				.toArray(new HtzxReportVO[0]);

		String strBillType = "htzxfp";
		String strCorp = "0001";
		String strOperator = getClientEnvironment().getInstance().getUser()
				.getPrimaryKey();
		String strNodekey = null;

		if (childvo != null && childvo.length > 0) {
			HYBillVO aggvo = new HYBillVO();
			aggvo.setParentVO(null);
			aggvo.setChildrenVO(childvo);
			HistoryxsUI historyui = new HistoryxsUI(this, strBillType, strCorp,
					strOperator, strNodekey);
			historyui.getBillCardPanel().getBillModel().setEnabled(false);
			historyui.setVO(aggvo);
			historyui.showModal();
		}
	}

	
	
	@Override
	protected void onQueryFh() throws Exception {
		
		
		int row = this.getReportBase().getBillTable().getSelectedRow();
		HtzxReportVO selectvo = (HtzxReportVO) this.getReportBase()
				.getBillModel()
				.getBodyValueRowVO(row, HtzxReportVO.class.getName());
		if (selectvo == null) {
			this.showErrorMessage("����ѡ��Ҫ�鿴��ϸ������!");
			return;
		}
		String jobnamecb = selectvo.getJobcode();
		nc.ui.pub.msg.PfLinkData pflinkdata = new PfLinkData();
		pflinkdata.setBillID("");
		pflinkdata.setUserObject(jobnamecb);
		pflinkdata.setSourceBillID("");
		// /�ر��Ѿ��򿪵Ĵ��� ,�Ա�������´򿪽��м�װ���� start
		java.util.List openModules = ClientEnvironment.getInstance()
				.getOpenModules();
		java.util.Iterator it = openModules.iterator();
		while (it.hasNext()) {
			IFuncWindow window = (IFuncWindow) it.next();
			if (window.getFuncPanel().getModuleCode().equals("12H20601")) {
				window.closeWindow();
				openModules = ClientEnvironment.getInstance().getOpenModules();
				it = openModules.iterator();
				break;
			}
		}
		// /�ر��Ѿ��򿪵Ĵ��� ,�Ա�������´򿪽��м�װ���� end
		FuncRegisterVO frVO = SFClientUtil.findFRVOFromMenuTree("12H20601");
		
		FuncNodeStarter.openDialog(frVO,  ILinkType.LINK_TYPE_MAINTAIN, pflinkdata, this, false, false);
		
	}
	
	private HashMap queryAlertHttjxs() throws BusinessException {

		StringBuffer sql1 = new StringBuffer();
		sql1.append("	select TEMQ_CT.jobname jobname,TEMQ_CT.pk_custdoc, ");
		sql1.append("   TEMQ_CT.def1 def1, ");
		sql1.append("   TEMQ_CT.xsj xsj,   ");
		sql1.append("   TEMQ_CT.cgj cgj,   ");
		sql1
				.append("   TEMQ_CT.ce ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.jobcode ,TEMQ_CT.pk_corp,TEMQ_CT.def10 ");
		sql1.append("   from  TEMQ_CT  where TEMQ_CT.jobname like '%-00' ");
		sql1
				.append("  group by TEMQ_CT.jobcode ,TEMQ_CT.jobname, TEMQ_CT.pk_custdoc, ");
		sql1.append("       TEMQ_CT.def1, ");
		sql1.append("       TEMQ_CT.xsj,  ");
		sql1.append("       TEMQ_CT.cgj,  ");
		sql1
				.append("       TEMQ_CT.ce ,TEMQ_CT.pk_deptdoc ,TEMQ_CT.pk_corp ,TEMQ_CT.def10  ");
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
				
				tmp = vobjs[9] == null ? " " : vobjs[9].toString();
				expensehtmxVo.setProjectname(tmp);

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
					
					String projectname = expensehtmxVo.getProjectname() == null ? ""
							: expensehtmxVo.getProjectname();
					String[] objs = new String[] { name1, name2, name3,
							pk_corp, jobcode,projectname };
					htnamemap.put(expensehtmxVo.getJobname(), objs);
				}
			}
		}
		return htnamemap;

	}
	
	private ArrayList doFilterData(String zdy_fhbs,String zdy_yll,ArrayList list) throws Exception{
		
		if(!"".endsWith(zdy_yll)){
			UFDouble dd=new UFDouble(zdy_yll.substring(zdy_fhbs.lastIndexOf("=")+1).trim());
			//�ж�����ȡ���ڵ��ڻ���С�ڵ���
			if(zdy_yll.indexOf(">=")!=-1){
				for(int i=0;i<list.size();i++){
					HtzxReportVO  vo=(HtzxReportVO)list.get(i);
					UFDouble hsl=vo.getHtml()==null?new UFDouble(0):vo.getHtml();
					if(hsl.compareTo(dd)<0){
						list.remove(i);
						i--;
					}
				}
			}else{
				for(int i=0;i<list.size();i++){
					HtzxReportVO  vo=(HtzxReportVO)list.get(i);
					UFDouble hsl=vo.getHtml()==null?new UFDouble(0):vo.getHtml();
					if(hsl.compareTo(dd)>0){
						list.remove(i);
						i--;
					}
				}
			}
		}
		
		if(!"".endsWith(zdy_fhbs)){
			Integer it=new Integer(zdy_fhbs.substring(zdy_fhbs.lastIndexOf("=")+1).trim());
			if(it==0){ //δ����
				for(int i=0;i<list.size();i++){
					HtzxReportVO  vo=(HtzxReportVO)list.get(i);
					String str= vo.getIffh()==null?"�ѷ���":vo.getIffh();
					if(!str.equals("δ����")){
						list.remove(i);
						i--;
					}
				}
			}else{ //�ѷ���
				for(int i=0;i<list.size();i++){
					HtzxReportVO  vo=(HtzxReportVO)list.get(i);
					String str= vo.getIffh()==null?"δ����":vo.getIffh();
					if(!str.equals("�ѷ���")){
						list.remove(i);
						i--;
					}
				}
			}
		}
		return list;
		
	}

	@Override
	protected void onQueryZb() throws Exception {
		
	}

	@Override
	protected void onQueryYCPZ() throws Exception {
		// TODO Auto-generated method stub
		
	}
}