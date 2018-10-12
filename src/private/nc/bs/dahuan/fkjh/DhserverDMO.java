package nc.bs.dahuan.fkjh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nc.bs.bd.b00.SubjQryDAO;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SuperDMO;
import nc.bs.uap.bd.BDException;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.job.IJobmanagedoc;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.JdbcTransaction;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.bd.b38.JobbasfilVO;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.bfriend.pub.BfStringUtil;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkjh.DhFkjhprintVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintDVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.dahuan.htinfo.htquery.HtConLogoDtlEntity;
import nc.vo.dahuan.htinfo.htquery.HtConLogoEntity;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.IErrorDict;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.bdinfo.BdinfoUtil;
import nc.vo.util.tree.LevelTool;

public class DhserverDMO extends DataManageObject implements IdhServer {

	BaseDAO m_baseDAO = null;
	private LevelTool m_levelTool = null;

	public DhserverDMO() throws NamingException {
		super();
	}

	// ��д��ͬ���ۼƼƻ�����
	public void UpdateDhhtToLjfkjhje(DhFkjhbillVO[] itemvos)
			throws BusinessException {

		JdbcTransaction jdbcTransaction = null;
		String updateSQl = "update dh_contract set ljfkjhje = (select sum(nvl(dfkje,0)) from dh_fkjhbill where dh_fkjhbill.vsourcebillid = ? and nvl(dh_fkjhbill.dr,0) = 0 ) where dh_contract.pk_contract = ?";

		SQLParameter param = new SQLParameter();
		try {
			jdbcTransaction = PersistenceManager.getInstance().getJdbcSession()
					.createTransaction();
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		jdbcTransaction.startTransaction();
		try {
			for (int i = 0; i < itemvos.length; i++) {
				param.clearParams();
				param.addParam(itemvos[i].getVsourcebillid());
				param.addParam(itemvos[i].getVsourcebillid());
				jdbcTransaction.addBatch(updateSQl, param);
			}

			jdbcTransaction.executeBatch();//
			jdbcTransaction.commitTransaction();
		} catch (DbException e) {
			// �쳣���Ե�
			jdbcTransaction.rollbackTransaction();
			throw new BusinessException(e.getMessage());
		} finally {
			jdbcTransaction.close();
		}

	}

	public void AuditDhhtbmzg(Map<String, List<DhContractVO>> map,String user,UFDate date) throws Exception {
		
		BaseDAO dao = new BaseDAO();
		
		String sql = "update dh_contract set vbillstatus=?,vapproveid=?,dapprovedate=?,vdef4=? where pk_contract=?";
		
		SQLParameter param = new SQLParameter();
		
		// ��Ҫ������˵ĺ�ͬ��
		List<DhContractVO> listFZ = map.get("aaa");
		if(null!=listFZ&&listFZ.size()>0){
			for(DhContractVO dhvo : listFZ){
				
				param.clearParams();
				
				// ����״̬��Ϊ����������
				param.addParam(2);
				// �����
				param.addParam(user);
				// ���ʱ��
				param.addParam(date);
				//��˽��
				param.addParam("1");
				// ��������ֵ
				param.addParam(dhvo.getPrimaryKey());
				
				// �������ݿ�
				dao.executeUpdate(sql, param);
			}
		}
		
		// ���踱����˵ĺ�ͬ��
		List<DhContractVO> listBM = map.get("bbb");
		if(null!=listBM&&listBM.size()>0){
			
			for(DhContractVO headvo : listBM){				
				param.clearParams();
				
				// ����״̬��Ϊ����������
				param.addParam(2);
				// �����
				param.addParam(user);
				// ���ʱ��
				param.addParam(date);	
//				��˽��
				param.addParam("1");
				// ��������ֵ
				param.addParam(headvo.getPrimaryKey());
				
				// �������ݿ�
				dao.executeUpdate(sql, param);
			}
		}
		
	}
	
	

	public void SealDhht(nc.vo.dahuan.cttreebill.DhContractVO headvo, String user, UFDate date) throws Exception {
		
		BaseDAO dao = new BaseDAO();
		
		JobbasfilVO basvo = new JobbasfilVO();
		basvo.setJobcode(headvo.getJobcode());
		basvo.setJobname(headvo.getCtcode());
		basvo.setPk_corp(headvo.getPk_corp());
		basvo.setPk_jobtype("0001AA100000000013ZG");
		basvo.setBegindate(headvo.getDstartdate());
//		basvo.setEnddate(headvo.getDjhdate());
//		basvo.setFinishedflag(new UFBoolean("N"));
		basvo.setSealflag("N");
		
		JobmngfilVO jobmngfil = new JobmngfilVO();
		jobmngfil.setJobBasicInfo(basvo);
		
		String invsql = "select t.invname from bd_invbasdoc t where t.pk_invbasdoc='"+headvo.getCtname()+"'";
		String invname = (String)dao.executeQuery(invsql, new ColumnProcessor());
		jobmngfil.setDef1(invname);
		
		jobmngfil.setDef10(headvo.getVdef6());

		
		jobmngfil.setDef2(headvo.getDsaletotal()==null?"":headvo.getDsaletotal().toString());
		jobmngfil.setDef3(headvo.getDcaigtotal()==null?"":headvo.getDcaigtotal().toString());
		
		//�Ѻ�ͬ���ʹ���def11�ֶ���
		jobmngfil.setDef11(headvo.getPk_cttype());
		
		jobmngfil.setPk_corp(headvo.getPk_corp());
		jobmngfil.setPk_custdoc(headvo.getPk_cust1());
		jobmngfil.setPk_deptdoc(headvo.getPk_deptdoc());
		
		// ������Ա�������ղ�ͬ
		jobmngfil.setPk_psndoc(headvo.getPk_fzr());
		
//		String psnsql = "select b.pk_psndoc from bd_psndoc b where nvl(b.dr,0)=0 and b.pk_psnbasdoc='"+headvo.getPk_fzr()+"' and b.pk_corp = '"+headvo.getPk_corp()+"'";
//		List<Map<String,String>> psnlit = (List<Map<String,String>>)dao.executeQuery(psnsql, new MapListProcessor());
//		if(null != psnlit && psnlit.size()>0){
//			Map<String,String> psnmap = psnlit.get(0);
//			jobmngfil.setPk_psndoc(psnmap.get("pk_psndoc"));
//		}
		
		jobmngfil.setPk_vendoc(headvo.getPk_cust2());
		jobmngfil.setSealflag(new UFBoolean("N"));		
		jobmngfil.setPubflag(new UFBoolean("N"));
		jobmngfil.setMemo(headvo.getVmen());
		
		IJobmanagedoc pfserver = NCLocator.getInstance().lookup(IJobmanagedoc.class);
		String sk = pfserver.insertJobmngfilVOWithBasedoc(jobmngfil, headvo.getPk_corp());
		
		String pkjobmng = sk.substring(sk.indexOf(",")+1);
		
		// ���ĸ��±�ʶ
		String sql = "update dh_contract set is_seal=1,pk_jobmandoc='"+pkjobmng+"' where pk_contract='"+headvo.getPrimaryKey()+"'";
		dao.executeUpdate(sql);
		
	}

	public void CancelAuditDhhtbmzg(Map<String, List<DhContractVO>> map) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void BoundPrint(DhFkjhbillVO[] vos,String pkcon,String content,String user,UFDate date) throws Exception {
		BaseDAO dao = new BaseDAO();
		
		String pkcorp = vos[0].getPk_corp()==null ? "" :vos[0].getPk_corp();
		//String pkdept = vos[0].getFk_dept()==null ? "" :vos[0].getFk_dept();
		String pkdept = vos[0].getPk_dept()==null ? "" :vos[0].getPk_dept();
		String voperid = vos[0].getVoperatorid()==null ? "" :vos[0].getVoperatorid();
		String shrid = vos[0].getShrid1()==null ? "" :vos[0].getShrid1();
		String pkcust = vos[0].getPk_cust2()==null ? "" :vos[0].getPk_cust2();
		String pkbank = vos[0].getPk_bank()==null ? "" :vos[0].getPk_bank();
		String nsay = vos[0].getSay_no()==null ? "" :vos[0].getSay_no();	
		String pkfz = vos[0].getShrid2()==null ? "" :vos[0].getShrid2();
		String pk_fkjhbill = vos[0].getPk_fkjhbill()==null ? "" :vos[0].getPk_fkjhbill();
		String pk_cwid = vos[0].getCwid()==null?"":vos[0].getCwid();
		
		DhFkjhprintVO pvo = new DhFkjhprintVO();
		DhFkbdprintVO bdvo = new DhFkbdprintVO();
		DhFkbdprintDVO[] bdvos = new DhFkbdprintDVO[vos.length];
		
		String corpSQL = "select t.unitname from bd_corp t where t.pk_corp = '"+pkcorp+"'";
		String corpname = (String)dao.executeQuery(corpSQL, new ColumnProcessor());
		pvo.setCorp_name(corpname);
		bdvo.setCorp_name(corpname);
		
		String depetSQL = "select t.deptname from bd_deptdoc t where t.pk_deptdoc = '"+pkdept+"'";
		String deptname = (String)dao.executeQuery(depetSQL, new ColumnProcessor());
		pvo.setDept_name(deptname);
		bdvo.setDept_name(deptname);
		
		String userSQL = "select t.user_name from sm_user t where t.cuserid = '"+voperid+"'";
		String username = (String)dao.executeQuery(userSQL, new ColumnProcessor());
		pvo.setDept_saleman(username);
		bdvo.setDept_saleman(username);
		
		String masterSQL = "select t.user_name from sm_user t where t.cuserid = '"+shrid+"'";
		String mastername = (String)dao.executeQuery(masterSQL, new ColumnProcessor());
		pvo.setDept_master(mastername);
		bdvo.setDept_master(mastername);
		
		String fzSQL = "select t.user_name from sm_user t where t.cuserid = '"+pkfz+"'";
		String fzname = (String)dao.executeQuery(fzSQL, new ColumnProcessor());
		pvo.setFzname(fzname);
		bdvo.setFzname(fzname);
		
		String cuSQL = "select c.custname from bd_cubasdoc c,bd_cumandoc t " +
							"where c.pk_cubasdoc = t.pk_cubasdoc and t.pk_cumandoc='"+pkcust+"'";
		String cusname = (String)dao.executeQuery(cuSQL, new ColumnProcessor());
		pvo.setSay_corp(cusname);
		bdvo.setSay_corp(cusname);
		
		
		// ����
		String cwSQL = "select t.user_name from sm_user t where t.cuserid = '"+pk_cwid+"'";
		String cwName = (String)dao.executeQuery(cwSQL, new ColumnProcessor());
		pvo.setCwname(cwName);
		bdvo.setCwname(cwName);
		
		pvo.setSay_bank(pkbank);
		bdvo.setSay_bank(pkbank);
		
		pvo.setSay_no(nsay);
		bdvo.setSay_no(nsay);
		
		pvo.setPrint_date(date);
		bdvo.setPrint_date(date);
		
//		bdvo.setPk_fkjhbill(pk_fkjhbill);
		
		
		UFDouble jebg = new UFDouble();
		UFDouble jetotal = new UFDouble();
		for(int i=0;i<vos.length;i++){
			DhFkjhbillVO fkvo = vos[i];
			jetotal = jetotal.add(fkvo.getDfkje(), 2);	
			String saycon = "";
			if(fkvo.getIffknr1().booleanValue()){
				saycon = "��ͬԤ����";
			}else if(fkvo.getIffknr2().booleanValue()){
				saycon = "��ͬ������";
			}else if(fkvo.getIffknr3().booleanValue()){
				saycon = "��ͬβ��";
			}else if(fkvo.getIffknr4().booleanValue()){
				saycon = "��������";
			}else if(fkvo.getIffknr5().booleanValue()){
				saycon = "ȫ���";
			}else if(fkvo.getIffknr6().booleanValue()){
				saycon = "��ͬ���Կ�";
			}else{
				saycon = "";
			}
			
			String balSQL = "select t.balanname from bd_balatype t where t.pk_balatype='"+fkvo.getPk_fkfs()+"'";
			String saytype = (String)dao.executeQuery(balSQL, new ColumnProcessor());
			
			if(i==0){
				pvo.setHtcode1(fkvo.getCtcode());
				pvo.setHtamount1(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content1(saycon);
				pvo.setSay_type1(saytype);
				bdvo.setHtcode1(fkvo.getCtcode());
				bdvo.setHtamount1(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content1(saycon);
				bdvo.setSay_type1(saytype);
			}else if(i==1){
				pvo.setHtcode2(fkvo.getCtcode());
				pvo.setHtamount2(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content2(saycon);
				pvo.setSay_type2(saytype);
				bdvo.setHtcode2(fkvo.getCtcode());
				bdvo.setHtamount2(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content2(saycon);
				bdvo.setSay_type2(saytype);
			}else if(i==2){
				pvo.setHtcode3(fkvo.getCtcode());
				pvo.setHtamount3(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content3(saycon);
				pvo.setSay_type3(saytype);
				bdvo.setHtcode3(fkvo.getCtcode());
				bdvo.setHtamount3(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content3(saycon);
				bdvo.setSay_type3(saytype);
			}else if(i==3){
				pvo.setHtcode4(fkvo.getCtcode());
				pvo.setHtamount4(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content4(saycon);
				pvo.setSay_type4(saytype);
				bdvo.setHtcode4(fkvo.getCtcode());
				bdvo.setHtamount4(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content4(saycon);
				bdvo.setSay_type4(saytype);
			}else if(i==4){
				pvo.setHtcode5(fkvo.getCtcode());
				pvo.setHtamount5(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content5(saycon);
				pvo.setSay_type5(saytype);
				bdvo.setHtcode5(fkvo.getCtcode());
				bdvo.setHtamount5(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content5(saycon);
				bdvo.setSay_type5(saytype);
			}else if(i==5){
				pvo.setHtcode6(fkvo.getCtcode());
				pvo.setHtamount6(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content6(saycon);
				pvo.setSay_type6(saytype);
				bdvo.setHtcode6(fkvo.getCtcode());
				bdvo.setHtamount6(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content6(saycon);
				bdvo.setSay_type6(saytype);
			}else if(i==6){
				pvo.setHtcode7(fkvo.getCtcode());
				pvo.setHtamount7(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content7(saycon);
				pvo.setSay_type7(saytype);
				bdvo.setHtcode7(fkvo.getCtcode());
				bdvo.setHtamount7(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content7(saycon);
				bdvo.setSay_type7(saytype);
			}else if(i==7){
				pvo.setHtcode8(fkvo.getCtcode());
				pvo.setHtamount8(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content8(saycon);
				pvo.setSay_type8(saytype);
				bdvo.setHtcode8(fkvo.getCtcode());
				bdvo.setHtamount8(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content8(saycon);
				bdvo.setSay_type8(saytype);
			}else if(i==8){
				pvo.setHtcode9(fkvo.getCtcode());
				pvo.setHtamount9(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content9(saycon);
				pvo.setSay_type9(saytype);
				bdvo.setHtcode9(fkvo.getCtcode());
				bdvo.setHtamount9(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content9(saycon);
				bdvo.setSay_type9(saytype);
			}else if(i==9){
				pvo.setHtcode10(fkvo.getCtcode());
				pvo.setHtamount10(fkvo.getDfkje().add(jebg,2));
				pvo.setSay_content10(saycon);
				pvo.setSay_type10(saytype);
				bdvo.setHtcode10(fkvo.getCtcode());
				bdvo.setHtamount10(fkvo.getDfkje().add(jebg,2));
				bdvo.setSay_content10(saycon);
				bdvo.setSay_type10(saytype);
			}
			DhFkbdprintDVO bdpvo = new DhFkbdprintDVO();
			bdpvo.setHtcode(fkvo.getCtcode());
			bdpvo.setHtamount(fkvo.getDfkje().add(jebg,2));
			bdpvo.setSay_content(saycon);
			bdpvo.setSay_type(saytype);
			bdvos[i] = bdpvo;
		}
		
		pvo.setAmount_sum(jetotal);
		bdvo.setAmount_sum(jetotal);
		
		String sql="select to_char(sysdate,'yyyy-mm-dd') from dual";
		String data=(String)dao.executeQuery(sql, new ColumnProcessor());
		
		String ptno = data.toString().substring(0, 4)+data.toString().substring(5, 7);
		String ptnoSQL = "select max(t.print_no) from dh_fkjhprint t where t.print_no like '"+ptno+"%'";
		Object ptobj = dao.executeQuery(ptnoSQL,new ColumnProcessor());
		if(null == ptobj || "".equals(ptobj)){
			ptno += "0001";
		}else{
			ptno = String.valueOf(Integer.valueOf(ptobj.toString()).intValue()+1);
		}
		
		pvo.setPrint_no(ptno);
		bdvo.setPrint_no(ptno);
		
		String primkey = dao.insertVO(pvo);
		bdvo.setPk_fkprint(primkey);
		String pkbd = dao.insertVO(bdvo);
		
		DhFkbdprintDVO[] dvonw = new DhFkbdprintDVO[vos.length];
		int num = 0;
		for(DhFkbdprintDVO fkbd : bdvos){
			fkbd.setPk_fkbd(pkbd);
			dvonw[num] = fkbd;
			num++ ;
		}
		dao.insertVOArray(dvonw);
		
		// ����ѡ�����ݵĴ�ӡ��ʶ
		String updSQL = "update dh_fkjhbill t set t.is_print = 2," +
												" t.shrid3 = '"+user+"'," +
												" t.shrdate3 = '"+date+"', " +
												" t.pk_fkjhprint = '"+primkey+"'," +
												" t.print_no = '"+ptno+"'" +
												" where t.pk_fkjhbill in ("+pkcon+")";
		
		
		dao.executeUpdate(updSQL);
		
	}
	
	// ��ѯ��ͬ�Ƿ��и���wanglong
	public int queryfilesystem(String htcode) throws BusinessException {
		String querySql = "  select count(*) count from sm_pub_filesystem   where sm_pub_filesystem.path  like '"
				+ htcode + "/%' and sm_pub_filesystem.isfolder = 'n'";

		Map filemap = (Map) new BaseDAO().executeQuery(querySql,
				new MapProcessor());
		int count = Integer.valueOf(filemap.get("count").toString());
		return count;

	}

	public HtzxReportVO[] queryHtzxVo(String strwhere) throws BusinessException {
		StringBuffer sql = new StringBuffer();

		sql.append("select TEMQ_CT.jobname as jobname,                                                                                      ");
		sql.append("       TEMQ_CT.def1 as def1,                                                                                            ");
		sql.append("       TEMQ_CT.xsj as xsj,                                                                                              ");
		sql.append("       TEMQ_CT.cgj as cgj,                                                                                              ");
		sql.append("       TEMQ_CT.ce as ce,                                                                                                ");
		sql.append("       TEMQ_dept.deptname as deptname,                                                                                  ");
		sql.append("       TEMQ_cust.custname as custname,                                                                                  ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql.append("              gl_detail.localcreditamount                                                                               ");
		sql.append("             else                                                                                                       ");
		sql.append("              0                                                                                                         ");
		sql.append("           end) as sjsk,                                                                                                ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                                     ");
		sql.append("              gl_detail.localdebitamount                                                                                ");
		sql.append("             else                                                                                                       ");
		sql.append("              0                                                                                                         ");
		sql.append("           end) as sjfk,                                                                                                ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' or                                       ");
		sql.append("                  bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql.append("              0                                                                                                         ");
		sql.append("             else                                                                                                       ");
		sql.append("              gl_detail.localdebitamount                                                                                ");
		sql.append("           end) as sjfy,                                                                                                ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql.append("              gl_detail.localcreditamount                                                                               ");
		sql.append("             else                                                                                                       ");
		sql.append("              0                                                                                                         ");
		sql.append("           end) - sum(case                                                                                              ");
		sql.append("                        when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                          ");
		sql.append("                         gl_detail.localdebitamount                                                                     ");
		sql.append("                        else                                                                                            ");
		sql.append("                         0                                                                                              ");
		sql.append("                      end) - sum(case                                                                                   ");
		sql.append("                                   when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' or                 ");
		sql.append("                                        bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then               ");
		sql.append("                                    0                                                                                   ");
		sql.append("                                   else                                                                                 ");
		sql.append("                                    gl_detail.localdebitamount                                                          ");
		sql.append("                                 end) as sjml,                                                                          ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '1122' or bd_accsubj.subjcode = '2203' then                                     ");
		sql.append("              gl_detail.localdebitamount                                                                                ");
		sql.append("             else                                                                                                       ");
		sql.append("              0                                                                                                         ");
		sql.append("           end) as sjkp,                                                                                                ");
		sql.append("       sum(case                                                                                                         ");
		sql.append("             when bd_accsubj.subjcode = '2202' or bd_accsubj.subjcode = '1123' then                                     ");
		sql.append("              gl_detail.localcreditamount                                                                               ");
		sql.append("             else                                                                                                       ");
		sql.append("              0                                                                                                         ");
		sql.append("           end) as sjsp,                                                                                                ");
		sql.append("                                                                                                                        ");
		sql.append("                 bd_accsubj.subjcode || bd_accsubj.subjname    as subjname                                              ");
		sql.append("                                                                                                                        ");
		sql.append("                                                                                                                        ");
		sql.append("  from gl_detail                                                                                                        ");
		sql.append(" inner join gl_voucher                                                                                                  ");
		sql.append("    on gl_detail.pk_voucher = gl_voucher.pk_voucher                                                                     ");
		sql.append(" inner join gl_freevalue                                                                                                ");
		sql.append("    on gl_detail.assid = gl_freevalue.freevalueid                                                                       ");
		sql.append(" inner join bd_accsubj                                                                                                  ");
		sql.append("    on gl_detail.pk_accsubj = bd_accsubj.pk_accsubj                                                                     ");
		sql.append(" inner join bd_corp                                                                                                     ");
		sql.append("    on gl_voucher.pk_corp = bd_corp.pk_corp                                                                             ");
		sql.append(" right outer join  TEMQ_CT                                                       ");
		sql.append("    on gl_freevalue.checkvalue = TEMQ_CT.pk_jobbasfil                                                                   ");
		sql.append("  left outer join (select gl_freevalue.valuename   as deptname,                                                         ");
		sql.append("                          gl_freevalue.freevalueid as freevalueid                                                       ");
		sql.append("                     from gl_freevalue                                                                                  ");
		sql.append("                    where (gl_freevalue.checktype = '00010000000000000002' and                                          ");
		sql.append("                          gl_freevalue.dr = 0)) TEMQ_dept                                                               ");
		sql.append("    on gl_detail.assid = TEMQ_dept.freevalueid                                                                          ");
		sql.append("  left outer join (select gl_freevalue.valuename   as custname,                                                         ");
		sql.append("                          gl_freevalue.freevalueid as freevalueid                                                       ");
		sql.append("                     from gl_freevalue                                                                                  ");
		sql.append("                    where (gl_freevalue.checktype = '00010000000000000073' and                                          ");
		sql.append("                          gl_freevalue.dr = 0)) TEMQ_cust                                                               ");
		sql.append("    on gl_detail.assid = TEMQ_cust.freevalueid                                                                          ");
		sql.append(" where (gl_detail.dr = 0 and gl_voucher.dr = 0 and                                                                      ");
		sql.append("       gl_voucher.discardflag = 'N' and gl_voucher.errmessage is null and                                               ");
		sql.append("       gl_voucher.period <> '00')                                                                                       ");
		sql.append("   and (   (bd_accsubj.subjcode in ('1122', '2203', '2202', '1123')) or                                                 ");
		sql.append("       (bd_accsubj.subjcode like '6602%' and                                                                              ");

		sql.append("       gl_detail.localdebitamount <> 0))                                                                                ");
		sql.append(" and " + strwhere);
		sql.append("   and (gl_freevalue.checktype = '0001A11000000000CGMX')                                                                ");
		sql.append(" group by TEMQ_CT.jobname,                                                                                              ");
		sql.append("          TEMQ_CT.def1,                                                                                                 ");
		sql.append("          TEMQ_CT.xsj,                                                                                                  ");
		sql.append("          TEMQ_CT.cgj,                                                                                                  ");
		sql.append("          TEMQ_CT.ce,                                                                                                   ");
		sql.append("          TEMQ_dept.deptname,                                                                                           ");
		sql.append("          TEMQ_cust.custname,                                                                                           ");
		sql.append("          bd_accsubj.subjcode || bd_accsubj.subjname                                                                    ");
		sql.append(" order by jobname asc, deptname asc, custname asc                                                                       ");

		IUAPQueryBS uapbs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
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

				if (expensehtzxVo.getDef1().equalsIgnoreCase("���ǲɹ�")) {
					expensehtzxVo.setCgj(expensehtzxVo.getSjfk());
					expensehtzxVo.setCe(expensehtzxVo.getSjfk().multiply(-1));
				}

				if (expensehtzxVo.getJobname().endsWith("-����")) {
					expensehtzxVo.setCe(expensehtzxVo.getSjfy().multiply(-1));
				}

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

						if (!subjname.startsWith("6602"))
							listvo.add(itemvotmp);

						itemvo.setJobname(itemvotmp.getJobname());
						itemvo.setDef1(itemvotmp.getDef1());
						UFDouble xsj = itemvo.getXsj() == null ? new UFDouble(
								0.00) : itemvo.getXsj();
						UFDouble xsj1 = itemvotmp.getXsj() == null ? new UFDouble(
								0.00) : itemvotmp.getXsj();
						itemvo.setXsj(xsj.add(xsj1));

						UFDouble cgj = itemvo.getCgj() == null ? new UFDouble(
								0.00) : itemvo.getCgj();
						UFDouble cgj1 = itemvotmp.getCgj() == null ? new UFDouble(
								0.00) : itemvotmp.getCgj();
						itemvo.setCgj(cgj.add(cgj1));

						UFDouble ce = itemvo.getCe() == null ? new UFDouble(
								0.00) : itemvo.getCe();
						UFDouble ce1 = itemvotmp.getCe() == null ? new UFDouble(
								0.00) : itemvotmp.getCe();

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
								0.00) : itemvo.getSjsk();
						UFDouble sjsk1 = itemvotmp.getSjsk() == null ? new UFDouble(
								0.00) : itemvotmp.getSjsk();
						itemvo.setSjsk(sjsk.add(sjsk1));

						UFDouble sjfk = itemvo.getSjfk() == null ? new UFDouble(
								0.00) : itemvo.getSjfk();
						UFDouble sjfk1 = itemvotmp.getSjfk() == null ? new UFDouble(
								0.00) : itemvotmp.getSjfk();

						itemvo.setSjfk(sjfk.add(sjfk1));

						UFDouble sjfy = itemvo.getSjfy() == null ? new UFDouble(
								0.00) : itemvo.getSjfy();
						UFDouble sjfy1 = itemvotmp.getSjfy() == null ? new UFDouble(
								0.00) : itemvotmp.getSjfy();

						itemvo.setSjfy(sjfy.add(sjfy1));

						UFDouble sjml = itemvo.getSjml() == null ? new UFDouble(
								0.00) : itemvo.getSjml();
						UFDouble sjml1 = itemvotmp.getSjml() == null ? new UFDouble(
								0.00) : itemvotmp.getSjml();
						itemvo.setSjml(sjml.add(sjml1));

						UFDouble sjkp = itemvo.getSjkp() == null ? new UFDouble(
								0.00) : itemvo.getSjkp();
						UFDouble sjkp1 = itemvotmp.getSjkp() == null ? new UFDouble(
								0.00) : itemvotmp.getSjkp();
						itemvo.setSjkp(sjkp.add(sjkp1));

						UFDouble sjsp = itemvo.getSjsp() == null ? new UFDouble(
								0.00) : itemvo.getSjsp();
						UFDouble sjsp1 = itemvotmp.getSjsp() == null ? new UFDouble(
								0.00) : itemvotmp.getSjsp();

						itemvo.setSjsp(sjsp.add(sjsp1));

						String subjname1 = itemvo.getSubjname() == null ? ""
								: itemvo.getSubjname();

						String subjname2 = itemvotmp.getSubjname() == null ? ""
								: itemvotmp.getSubjname();

						itemvo.setSubjname(subjname1 + " " + subjname2);

					}

					listvo.add(itemvo);
				} else {
					listvo.add(val.get(0));
				}
			}

			HtzxReportVO[] itemvos = (HtzxReportVO[]) listvo
					.toArray(new HtzxReportVO[0]);

			for (int i = 0; i < itemvos.length; i++) {
				String tmp1 = itemvos[i].getJobname();
				int htlen = tmp1.indexOf("-");
				String htzname = tmp1.substring(0, htlen);
				itemvos[i].setHtzname(htzname);
				String subjN = itemvos[i].getSubjname();
				if (subjN.trim().startsWith("6602")) {
					itemvos[i].setCgj(itemvos[i].getSjfy());
				}
			}
			return itemvos;
		}
		return null;
	}

	public DhContractVO[] queryDhhtVO(String[] htcodes)
			throws BusinessException {
		String condition = BfStringUtil.getWherePartByKeys("ctcode", htcodes,
				false);
		DhContractVO[] aggvos = (DhContractVO[]) new SuperDMO()
				.queryByWhereClause(DhContractVO.class, condition);
		return aggvos;

	}

	// �����ͬ������� wanglong
	public void checkInsertbasedoc(JobbasfilVO jobbasfil)
			throws BusinessException {
		// ��鼯���Ƿ�����������Ŀ����
		checkInsertAvailable(jobbasfil);
		// ���ȼ�����ĺϷ���
		checkCodeRepeat(jobbasfil);

		// �������͵ĺϷ���
		JobtypeVO jobtype = queryAndCheckJobtype(jobbasfil);
		LevelTool lt = getLevelToolFromType(jobtype);

		// ��֤��������ϼ�
		int level = lt.getLevelNum(jobbasfil.getJobcode());
		int controlLevel = jobtype.getControlnum() == null ? 0 : jobtype
				.getControlnum().intValue();
		checkJobbasfilLevel(jobbasfil, lt, level, controlLevel);

		checkSuperCancelBeforeInsert(jobbasfil, jobtype);
		checkSuperDocsBeforeInsert(jobbasfil, jobtype.getPrimaryKey(), lt);

		businessCheckBeforeSave(jobbasfil);
	}
	
	/**
	 * ��ѯ���Ų���"��Ŀ�����Ƿ������¼���λ����"
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private boolean isInputAvailable() throws BusinessException {
		ISysInitQry initQuery = (ISysInitQry) NCLocator.getInstance().lookup(
				ISysInitQry.class.getName());
		UFBoolean isInsertAvailable = initQuery.getParaBoolean("0001", "BD005");
		return isInsertAvailable == null ? true : isInsertAvailable
				.booleanValue();
	}

	/**
	 * ��鼯���Ƿ�����������Ŀ����
	 */
	private void checkInsertAvailable(JobbasfilVO jobbasfil)
			throws BusinessException {
		if (!"0001".equals(jobbasfil.getPk_corp())) {
			if (!isInputAvailable()) {
				throw new BDException(NCLangResOnserver.getInstance()
						.getStrByID("10081406", "UPP10081406-000035")/*
																	 * @res
																	 * "��˾���������"
																	 */);
			}
		}
	}

	private void checkCodeRepeat(JobbasfilVO vo) throws BusinessException {
		if (vo == null)
			return;

		String pk_corp = vo.getPk_corp();
		SubjQryDAO sqdmo = new SubjQryDAO();
		boolean isUsedByAss = sqdmo.isJobUsedAsAss(pk_corp).booleanValue();

		String where = " jobcode='" + vo.getJobcode() + "' ";
		if (!"0001".equals(pk_corp)) {
			where += "and (pk_corp = '0001' or pk_corp = '" + pk_corp + "') ";
		}
		if (!isUsedByAss) {
			where += "and pk_jobtype = '" + vo.getPk_jobtype() + "' ";
		}
		if (vo.getPrimaryKey() != null && vo.getPrimaryKey().length() != 0) {
			where += "and pk_jobbasfil <> '" + vo.getPrimaryKey() + "' ";
		}
		String sql = "select count(*) from bd_jobbasfil where " + where;
		Object obj = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		boolean isExist = obj == null ? false : ((Integer) obj).intValue() > 0;
		if (isExist) {
			throw new BusinessException(
					MultiLangTrans.getTransStr(
							"MC1",
							new String[] {
									addSquareBrackets(NCLangRes4VoTransl
											.getNCLangRes()
											.getStrByID("10081404",
													"UC000-0003279")/* @res "����" */),
									null }),
					IErrorDict.ERR_BUSI_BILL_CODE_ALREADYEXIST); // ���벻���ظ�
		}
	}

	private JobtypeVO queryAndCheckJobtype(JobbasfilVO jobbasfil)
			throws BusinessException, BDException {
		JobtypeVO jottype = (JobtypeVO) getBaseDAO().retrieveByPK(
				JobtypeVO.class, jobbasfil.getPk_jobtype());
		if (jottype == null) {
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MC7",
									new String[] {
											NCLangResOnserver
													.getInstance()
													.getStrByID("10081404",
															"UPP10081404-000007")/*
																				 * @
																				 * res
																				 * "��Ŀ����"
																				 */,
											null })); // ��Ŀ���Ͳ�����
		}
		return jottype;
	}

	private LevelTool getLevelToolFromType(JobtypeVO jobtype)
			throws BusinessException {
		String rule = jobtype.getJobclclass().trim();
		LevelTool lt = getLevelTool();
		lt.checkString(rule);
		return lt;
	}

	private LevelTool getLevelTool() {
		if (m_levelTool == null) {
			m_levelTool = new LevelTool(20, 200);
		}
		return m_levelTool;
	}

	private void checkJobbasfilLevel(JobbasfilVO jobbasfil, LevelTool lt,
			int level, int controlLevel) throws BusinessException {
		// ���������ȷ!
		if (level <= 0) {
			int len = lt.gethowlength(1);
			if (len != jobbasfil.getJobcode().length()) {
				throw new BDException(BDMsg.MSG_ERROR_CODE_RULE());
			}
		}
		// ����ǹ�˾����,�伶�α�����ڼ��ſ��Ƽ���
		if (!"0001".equalsIgnoreCase(jobbasfil.getPk_corp())) {
			if (level <= controlLevel) {
				throw new BDException(
						NCLangResOnserver.getInstance().getStrByID("10081406",
								"UPP10081406-000048")/* @res "���ſ��Ƽ���" */
								+ ": "
								+ controlLevel
								+ "\n"
								+ MultiLangTrans.getTransStr(
										"MP1",
										new String[] { NCLangRes4VoTransl
												.getNCLangRes().getStrByID(
														"10081406",
														"UC001-0000002") /*
																		 * @res
																		 * "����"
																		 */})); // ���ſ��Ƽ���X,��������
			}
		}
		// ��֤�Ƿ�����ϼ� ��Ŀ
		else if (level > 1) {
			int plen = lt.gethowlength(level - 1);
			JobbasfilVO pvo = findByTypeAndCode(jobbasfil.getPk_jobtype(),
					jobbasfil.getJobcode().substring(0, plen));
			// modified by jingli ȥ��������(����ѱ���Ϊaa�ĸ�Ϊaabb)
			if (pvo != null
					&& pvo.getPrimaryKey().equals(jobbasfil.getPrimaryKey())) {
				pvo = null;
			}
			if (pvo == null) {
				throw new BDException(MultiLangTrans.getTransStr(
						"MC7",
						new String[] {
								NCLangResOnserver.getInstance().getStrByID(
										"10081404", "UPP10081404-000008")/*
																		 * @res
																		 * "�ϼ�"
																		 */,
								null })); // �ϼ�������
			}
		}
	}

	private JobbasfilVO findByTypeAndCode(String pk_jobtype, String jobcode)
			throws BusinessException {
		String where = "pk_jobtype = '" + pk_jobtype + "' and  jobcode = '"
				+ jobcode + "'";
		Collection c = getBaseDAO().retrieveByClause(JobbasfilVO.class, where);
		JobbasfilVO[] vos = c == null || c.size() == 0 ? null
				: (JobbasfilVO[]) c.toArray(new JobbasfilVO[c.size()]);
		return vos == null ? null : vos[0];
	}

	/**
	 * ���ָ�����������ڹ�˾pk_corp�е��ϼ��Ƿ��Ѿ����
	 * 
	 * @param jobbasfil
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	private boolean isSuperCancel(JobbasfilVO jobbasfil, JobtypeVO jobtype,
			String pk_corp) throws BusinessException {
		if (jobtype == null
				|| !jobbasfil.getPk_jobtype().equals(jobtype.getPrimaryKey()))
			jobtype = (JobtypeVO) getBaseDAO().retrieveByPK(JobtypeVO.class,
					jobbasfil.getPk_jobtype());
		LevelTool lt = getLevelToolFromType(jobtype);
		String parentcode = lt.getParentLevel(jobbasfil.getJobcode());
		if (parentcode == null || parentcode.trim().length() == 0)
			return false;
		String sql = "select bd_jobmngfil.sealflag from bd_jobmngfil,bd_jobbasfil where bd_jobbasfil.pk_jobbasfil = bd_jobmngfil.pk_jobbasfil and bd_jobbasfil.jobcode = '"
				+ parentcode
				+ "' and bd_jobbasfil.pk_jobtype = '"
				+ jobtype.getPrimaryKey()
				+ "' and bd_jobmngfil.pk_corp = '"
				+ pk_corp + "' ";
		String isSuperCancel = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return new UFBoolean(isSuperCancel).booleanValue();
	}

	private BaseDAO getBaseDAO() {
		if (m_baseDAO == null) {
			m_baseDAO = new BaseDAO();
		}
		return m_baseDAO;
	}

	private void checkSuperCancelBeforeInsert(JobbasfilVO jobbasfil,
			JobtypeVO jobtype) throws BusinessException, BDException {
		if (!"0001".equals(jobbasfil.getPk_corp())
				&& isSuperCancel(jobbasfil, jobtype, jobbasfil.getPk_corp())) {
			// ��˾���ӵ���Ŀ��������,����ϼ����������,����������
			String errMsg = NCLangResOnserver.getInstance().getStrByID(
					"10081404", "UC000-0000404")/* @res "��˾" */
					+ BdinfoUtil.genBdinfoContextString(IBdinfoConst.CORP,
							"%c %n", jobbasfil.getPk_corp())
					+ ": "
					+ jobbasfil.getJobname()
					+ " "
					+ MultiLangTrans
							.getTransStr(
									"MO4",
									new String[] {
											NCLangResOnserver
													.getInstance()
													.getStrByID("10081404",
															"UPP10081404-000006")/*
																				 * @
																				 * res
																				 * "���"
																				 */,
											NCLangResOnserver.getInstance()
													.getStrByID("10081404",
															"UC001-0000002")
									/* @res "����" */}); // �ϼ��ѷ��,���������¼�
			throw new BDException(new BdinfoUtil().process(errMsg));
		}
	}

	private void checkSuperDocsBeforeInsert(JobbasfilVO jobbasfil,
			String pk_jobtype, LevelTool lt) throws BusinessException {
		if (jobbasfil == null)
			return;

		String jobcode = jobbasfil.getJobcode();
		String parentcode = lt.getParentLevel(jobcode);
		HashMap<String, JobbasfilVO> changedVOMap = new HashMap<String, JobbasfilVO>();
		while (parentcode != null && parentcode.trim().length() > 0) {
			checkSuperDocBeforeInsert(jobbasfil, parentcode, pk_jobtype,
					changedVOMap);
			parentcode = lt.getParentLevel(parentcode);
		}
		// Ϊ�ϼ���д���ֶ�(��ʼ����,�깤����,Ԥ���깤����)
		if (changedVOMap.size() > 0) {
			JobbasfilVO[] changedVOs = (JobbasfilVO[]) changedVOMap.values()
					.toArray(new JobbasfilVO[changedVOMap.values().size()]);
			getBaseDAO().updateVOArray(changedVOs);
		}
	}

	private void checkSuperDocBeforeInsert(JobbasfilVO jobbasfil,
			String parentCode, String pk_jobtype, HashMap changedVOMap)
			throws BusinessException {
		JobbasfilVO[] vos = queryJobbasfilVOsByCondition(pk_jobtype, null,
				"jobcode = '" + parentCode + "' " + "and (pk_corp = '"
						+ jobbasfil.getPk_corp() + "' or pk_corp = '0001') ");
		if (vos == null || vos.length == 0)
			return;
		JobbasfilVO superJobbasfil = vos[0];
		// �ϼ��ѷ��,���������¼�
		if (superJobbasfil.getSealflag() != null
				&& new UFBoolean(superJobbasfil.getSealflag().trim())
						.booleanValue()) {
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MO4",
									new String[] {
											NCLangResOnserver
													.getInstance()
													.getStrByID("10081404",
															"UPP10081404-000006")/*
																				 * @
																				 * res
																				 * "���"
																				 */,
											NCLangResOnserver.getInstance()
													.getStrByID("10081404",
															"UC001-0000002") /*
																			 * @res
																			 * "����"
																			 */})); // �ϼ��ѷ��,���������¼�
		}
		// �ϼ����깤,���������¼�
		if ((superJobbasfil.getEnddate() != null && superJobbasfil.getEnddate()
				.toString().trim().length() > 0)
				|| (superJobbasfil.getFinishedflag() != null && superJobbasfil
						.getFinishedflag().booleanValue())) {
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MO4",
									new String[] {
											NCLangResOnserver
													.getInstance()
													.getStrByID("10081404",
															"UPP10081404-000005")/*
																				 * @
																				 * res
																				 * "�깤"
																				 */,
											NCLangResOnserver.getInstance()
													.getStrByID("10081404",
															"UC001-0000002") /*
																			 * @res
																			 * "����"
																			 */})); // �ϼ����깤,���������¼�
		}
		// ��ʼ���ڲ��������ϼ���ʼ����
		if (jobbasfil.getBegindate() != null) {
			if (superJobbasfil.getBegindate() != null) {
				if (jobbasfil.getBegindate().before(
						superJobbasfil.getBegindate()))
					throw new BDException(MultiLangTrans.getTransStr(
							"MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0001892")/*
																	 * @res
																	 * "��ʼ����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081404",
													"UPP10081404-000002")/*
																		 * @res
																		 * "�ϼ���ʼ����"
																		 */) })); // ��ʼ���ڲ��������ϼ���ʼ����
			} else {
				getBaseVOFromMap(changedVOMap, superJobbasfil).setBegindate(
						jobbasfil.getBegindate());
			}
		}
		// Ԥ���깤���ڲ��������ϼ�Ԥ���깤����
		if (jobbasfil.getForecastenddate() != null) {
			if (superJobbasfil.getForecastenddate() != null) {
				if (jobbasfil.getForecastenddate().after(
						superJobbasfil.getForecastenddate()))
					throw new BDException(MultiLangTrans.getTransStr(
							"MC14",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0004223")/*
																	 * @res
																	 * "Ԥ���깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081404",
													"UPP10081404-000003")/*
																		 * @res
																		 * "�ϼ�Ԥ���깤����"
																		 */) })); // Ԥ���깤���ڲ��������ϼ�Ԥ���깤����
			}
			// else {
			// getBaseVOFromMap(changedVOMap,superJobbasfil).setForecastenddate(jobbasfil.getForecastenddate());
			// }
		}
		// �깤���ڲ��������ϼ��깤����
		if (jobbasfil.getEnddate() != null) {
			if (superJobbasfil.getEnddate() != null) {
				if (jobbasfil.getEnddate().after(superJobbasfil.getEnddate()))
					throw new BDException(MultiLangTrans.getTransStr(
							"MC14",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0001510")/*
																	 * @res
																	 * "�깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance().getStrByID(
													"10081404",
													"UPP10081404-000004")/*
																		 * @res
																		 * "�ϼ��깤����"
																		 */) })); // �깤���ڲ��������ϼ��깤����
			}
			// else {
			// getBaseVOFromMap(changedVOMap,superJobbasfil).setEnddate(jobbasfil.getEnddate());
			// }
		}
	}

	private JobbasfilVO getBaseVOFromMap(HashMap<String, JobbasfilVO> map,
			JobbasfilVO vo) {
		if (map.containsKey(vo.getPrimaryKey())) {
			return (JobbasfilVO) map.get(vo.getPrimaryKey());
		} else {
			map.put(vo.getPrimaryKey(), vo);
			return vo;
		}
	}

	public JobbasfilVO[] queryJobbasfilVOsByCondition(String pk_jobtype,
			String pk_corp, String condition) throws BusinessException {
		String where = "1=1 ";
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where += "and pk_corp = '" + pk_corp + "' ";
		}
		if (pk_jobtype != null && pk_jobtype.trim().length() > 0) {
			where += "and pk_jobtype = '" + pk_jobtype + "' ";
		}
		if (condition != null && condition.trim().length() > 0) {
			where += "and (" + condition + ") ";
		}
		String order = "jobcode ";
		Collection c = getBaseDAO().retrieveByClause(JobbasfilVO.class, where,
				order);
		JobbasfilVO[] jobbasfils = c == null || c.size() == 0 ? null
				: (JobbasfilVO[]) c.toArray(new JobbasfilVO[c.size()]);
		return jobbasfils;
	}

	private void businessCheckBeforeSave(JobbasfilVO jobbasfil)
			throws BusinessException {
		StringBuilder nullFields = new StringBuilder();
		if (jobbasfil.getJobcode() == null
				|| jobbasfil.getJobcode().trim().length() == 0)
			nullFields.append(addSquareBrackets(NCLangResOnserver.getInstance()
					.getStrByID("10081404", "UC000-0004176")/* @res "��Ŀ����" */));
		if (jobbasfil.getJobname() == null
				|| jobbasfil.getJobname().trim().length() == 0)
			nullFields.append(addSquareBrackets(NCLangResOnserver.getInstance()
					.getStrByID("10081404", "UC000-0004171")/* @res "��Ŀ����" */));
		if (nullFields.length() > 0)
			throw new BDException(MultiLangTrans.getTransStr("MC3",
					new String[] { nullFields.toString() }));
		UFDate beginDate = jobbasfil.getBegindate();
		UFDate endDate = jobbasfil.getEnddate();
		UFDate forcastEndDate = jobbasfil.getForecastenddate();
		UFBoolean endFlag = jobbasfil.getFinishedflag();

		if (endDate != null && beginDate == null)
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MC6",
									new String[] {
											addSquareBrackets(NCLangResOnserver
													.getInstance().getStrByID(
															"10081404",
															"UC000-0001510")/*
																			 * @res
																			 * "�깤����"
																			 */),
											null })
							+ ","
							+ MultiLangTrans
									.getTransStr(
											"MC3",
											new String[] { addSquareBrackets(NCLangResOnserver
													.getInstance().getStrByID(
															"10081404",
															"UC000-0001892")/*
																			 * @res
																			 * "��ʼ����"
																			 */) })); // �깤�����Ѿ����ڣ���ʼ���ڲ���Ϊ��

		if (forcastEndDate != null && beginDate == null)
			throw new BDException(
					MultiLangTrans
							.getTransStr(
									"MC6",
									new String[] {
											addSquareBrackets(NCLangResOnserver
													.getInstance().getStrByID(
															"10081404",
															"UC000-0004223")/*
																			 * @res
																			 * "Ԥ���깤����"
																			 */),
											null })
							+ ","
							+ MultiLangTrans
									.getTransStr(
											"MC3",
											new String[] { addSquareBrackets(NCLangResOnserver
													.getInstance().getStrByID(
															"10081404",
															"UC000-0001892")/*
																			 * @res
																			 * "��ʼ����"
																			 */) })); // Ԥ���깤�����Ѿ����ڣ���ʼ���ڲ���Ϊ��

		if (forcastEndDate != null && beginDate != null
				&& forcastEndDate.before(beginDate))
			throw new BDException(
					MultiLangTrans.getTransStr(
							"MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0004223")/*
																	 * @res
																	 * "Ԥ���깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0001892")/*
																	 * @res
																	 * "��ʼ����"
																	 */) })); // Ԥ���깤���ڲ������ڿ�ʼ����

		if (endDate != null && beginDate != null && endDate.before(beginDate))
			throw new BDException(
					MultiLangTrans.getTransStr(
							"MC13",
							new String[] {
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0001510")/*
																	 * @res
																	 * "�깤����"
																	 */),
									addSquareBrackets(NCLangResOnserver
											.getInstance()
											.getStrByID("10081404",
													"UC000-0001892")/*
																	 * @res
																	 * "��ʼ����"
																	 */) })); // �깤���ڲ������ڿ�ʼ����

		if (endDate != null && (endFlag == null || !endFlag.booleanValue()))
			jobbasfil.setFinishedflag(UFBoolean.TRUE);

		if (endFlag != null && endFlag.booleanValue() && endDate == null)
			throw new BDException(
					NCLangResOnserver.getInstance().getStrByID("10081404",
							"UPP10081404-000033")/* @res "��Ŀ���깤" */
							+ ","
							+ MultiLangTrans
									.getTransStr(
											"MC3",
											new String[] { addSquareBrackets(NCLangResOnserver
													.getInstance().getStrByID(
															"10081404",
															"UC000-0001510")/*
																			 * @res
																			 * "�깤����"
																			 */) })/* �깤���ڲ���Ϊ�� */);
	}

	private String addSquareBrackets(String fieldName) {
		if (fieldName != null && fieldName.trim().length() > 0)
			return " [" + fieldName + "] ";
		else
			return fieldName;
	}

	public void checkJobaseData(nc.vo.dahuan.cttreebill.DhContractVO headvo)
			throws BusinessException {

		JobbasfilVO basvo = new JobbasfilVO();
		basvo.setJobcode(headvo.getJobcode());
		basvo.setJobname(headvo.getCtcode());
		basvo.setPk_corp(headvo.getPk_corp());
		basvo.setPk_jobtype("0001AA100000000013ZG");
		basvo.setBegindate(headvo.getDstartdate());
		basvo.setEnddate(headvo.getDjhdate());
		basvo.setFinishedflag(new UFBoolean("N"));
		basvo.setSealflag("N");
		checkInsertbasedoc(basvo);
	}

	/**
	 * ��ͬ���
	 * */
	public void changeContractData(HtChangeEntity htc,HtChangeDtlEntity[] htcdtls,UFDate nowdate) throws Exception {

		BaseDAO dao = new BaseDAO();
		
		// ���º�ͬ����
		nc.vo.dahuan.cttreebill.DhContractVO dvo = (nc.vo.dahuan.cttreebill.DhContractVO)
												dao.retrieveByPK(nc.vo.dahuan.cttreebill.DhContractVO.class, htc.getPk_contract());
		List<nc.vo.dahuan.cttreebill.DhContractBVO> dcvolit = (List<nc.vo.dahuan.cttreebill.DhContractBVO>)
												dao.retrieveByClause(nc.vo.dahuan.cttreebill.DhContractBVO.class, " pk_contract = '"+dvo.getPk_contract()+"' ");
		
		// �洢�ɵĺ�ͬ����
		HtConLogoEntity logen = new HtConLogoEntity();
		logen.setHttype(htc.getHttype());
		logen.setCttype(htc.getHtctpe());
		logen.setJobcode(dvo.getJobcode());
		logen.setJobname(htc.getHtprojname());
		logen.setHtcode(htc.getHtcode());
		logen.setHtname(htc.getHtpduname());
		logen.setCustname(htc.getHtcust());
		logen.setHtamount(dvo.getDctjetotal());
		logen.setBank_name(htc.getHtbank());
		logen.setSax_no(htc.getHtsaxno());
		logen.setHtstyle(htc.getHtstyle());
		logen.setVoperdate(dvo.getDbilldate());
		logen.setVemo(dvo.getVmen());
		logen.setHtdate(dvo.getHtrq());
		logen.setHtexedate(dvo.getDstartdate());
		logen.setHtaddress(dvo.getHtaddress());
		logen.setHtdeladdress(dvo.getVjhaddress());
		logen.setHtdeldate(dvo.getDjhdate());
		
		String deptsql = "select t.deptname from bd_deptdoc t where t.pk_deptdoc='"+dvo.getHt_dept()+"'";
		String htdept = (String)dao.executeQuery(deptsql, new ColumnProcessor());
		logen.setBelong_dept(htdept);
		
		deptsql = "select t.deptname from bd_deptdoc t where t.pk_deptdoc='"+dvo.getPk_deptdoc()+"'";
		String pkdept = (String)dao.executeQuery(deptsql, new ColumnProcessor());
		logen.setAdding_dept(pkdept);
		
		String psnsql = "select t.psnname from bd_psnbasdoc t where t.pk_psnbasdoc='"+dvo.getPk_xmjl()+"'";
		String xmjl = (String)dao.executeQuery(psnsql, new ColumnProcessor());
		logen.setHtmanager(xmjl);
		
		psnsql = "select t.psnname from bd_psnbasdoc t where t.pk_psnbasdoc='"+dvo.getPk_fzr()+"'";
		String fzr = (String)dao.executeQuery(psnsql, new ColumnProcessor());
		logen.setHtcontractor(fzr);
		
		String corpsql = "select t.unitname from bd_corp t where t.pk_corp='"+dvo.getPk_corp()+"'";
		String corpname = (String)dao.executeQuery(corpsql, new ColumnProcessor());
		logen.setCorp_name(corpname);
		
		String usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getPk_fuzong()+"'";
		String fzname = (String)dao.executeQuery(usersql, new ColumnProcessor());
		logen.setCorp_manager(fzname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getVapproveid()+"'";
		String zgname = (String)dao.executeQuery(usersql, new ColumnProcessor());
		logen.setDept_manager(zgname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getPk_ysid()+"'";
		String ysname = (String)dao.executeQuery(usersql, new ColumnProcessor());
		logen.setDept_contractor(ysname);
		
		usersql = "select t.user_name from sm_user t where t.cuserid='"+dvo.getVoperatorid()+"'";
		String vpname = (String)dao.executeQuery(usersql, new ColumnProcessor());
		logen.setDept_contractor(vpname);
		
		String pklogo = dao.insertVO(logen);
		
		for(nc.vo.dahuan.cttreebill.DhContractBVO ctvo : dcvolit){
			HtConLogoDtlEntity logdd = new HtConLogoDtlEntity();
			logdd.setPk_conlogo(pklogo);
			logdd.setPk_pdu(ctvo.getPk_invbasdoc());
			logdd.setPdu_no(ctvo.getInvcode());
			logdd.setPdu_name(ctvo.getInvname());
			logdd.setPdu_stylemodel(ctvo.getVggxh());
			logdd.setMeadoc_name(ctvo.getPk_danw());
			logdd.setPdu_num(ctvo.getNnumber());
			logdd.setPdu_piece(ctvo.getDjprice());
			logdd.setPdu_amount(ctvo.getDjetotal());
			logdd.setDelivery_date(ctvo.getDghsj());
			logdd.setVemo(ctvo.getVmen());
			dao.insertVO(logdd);
		}
		
		
		// ��ͬ����Ŀ������ͬǩԼ��		
		int httype = dvo.getHttype().intValue();
		if(0==httype){
			dvo.setDsaletotal(htc.getHtamount());
		}else if(1==httype){
			dvo.setDcaigtotal(htc.getHtamount());
		}else{
			// ���ͬ������
		}
		dvo.setDctjetotal(htc.getHtamount());
		dvo.setPk_xmjl(htc.getHtmanager());
		dvo.setPk_fzr(htc.getHtcontractor());
		if(dvo.getHt_changenum()==null){
			dvo.setHt_changenum(1);
		}else{
			int chnum = dvo.getHt_changenum().intValue() + 1;
			dvo.setHt_changenum(chnum);
		}
		
		dao.updateVO(dvo);
		
		dao.deleteByClause(nc.vo.dahuan.cttreebill.DhContractBVO.class, " pk_contract = '"+dvo.getPk_contract()+"' ");
		
		for(HtChangeDtlEntity htcdd : htcdtls){
			nc.vo.dahuan.cttreebill.DhContractBVO bvo = new nc.vo.dahuan.cttreebill.DhContractBVO();
			bvo.setPk_contract(dvo.getPk_contract());
			bvo.setInvcode(htcdd.getPdu_no());
			bvo.setPk_invbasdoc(htcdd.getPk_pdu());
			bvo.setVggxh(htcdd.getPdu_stylemodel());
			bvo.setNnumber(htcdd.getPdu_num());
			bvo.setDjprice(htcdd.getPdu_piece());
			bvo.setDjetotal(htcdd.getPdu_amount());
			bvo.setDghsj(htcdd.getDelivery_date());
			bvo.setVmen(htcdd.getVemo());
			bvo.setInvname(htcdd.getPdu_name());
			bvo.setPk_danw(htcdd.getMeadoc_name());
			dao.insertVO(bvo);
		}
		
		// ������Ŀ����
		List<JobbasfilVO> jobbaslit = (List<JobbasfilVO>)dao.retrieveByClause(JobbasfilVO.class, " jobcode = '"+dvo.getJobcode()+"'");
		if(null != jobbaslit && jobbaslit.size() > 0){
			JobbasfilVO jobbas = jobbaslit.get(0);
			
			List<JobmngfilVO> jobmnglit = (List<JobmngfilVO>)dao.retrieveByClause(JobmngfilVO.class, " pk_jobbasfil = '"+jobbas.getPrimaryKey()+"'");
			if(null != jobmnglit && jobmnglit.size() > 0){
				JobmngfilVO jobmng = jobmnglit.get(0);
				
				// ������Ա�������ղ�ͬ
				String psndocsql = "select b.pk_psndoc from bd_psndoc b where nvl(b.dr,0)=0 and b.pk_psnbasdoc='"+dvo.getPk_fzr()+"' and b.pk_corp = '"+dvo.getPk_corp()+"'";
				List<Map<String,String>> psnlit = (List<Map<String,String>>)dao.executeQuery(psndocsql, new MapListProcessor());
				if(null != psnlit && psnlit.size()>0){
					Map<String,String> psnmap = psnlit.get(0);
					jobmng.setPk_psndoc(psnmap.get("pk_psndoc"));
				}
				
				if(0==httype){
					jobmng.setDef2(htc.getHtamount().toString());
				}else if(1==httype){
					jobmng.setDef3(htc.getHtamount().toString());
				}else{
					// ���ͬ������
				}
			}
		}
		
		// ���º�ͬ�����
		htc.setHtstatus(2);
		htc.setSure_date(nowdate);
		dao.updateVO(htc);
	}

	
	
}
