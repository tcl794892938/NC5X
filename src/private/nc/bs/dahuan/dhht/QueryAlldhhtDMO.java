package nc.bs.dahuan.dhht;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.scm.pub.redun.IRedunSource;
import nc.itf.dahuan.pf.IdhServer;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.bfriend.pub.BfStringUtil;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

public class QueryAlldhhtDMO extends DataManageObject implements IQueryData,
		IQueryData2, IRedunSource {

	public QueryAlldhhtDMO() throws NamingException {
		super();

	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {

		return null;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(
			String where, Object objIsWaitAudit) {
		String strSql = " select  dh_contract.*  from dh_contract where  isnull(dh_contract.dr,0) = 0 and dh_contract.vbillstatus = 1 ";

		if (where != null && where.trim().length() > 0)
			strSql = strSql + " AND " + where;

		strSql = strSql
				+ " order by  dh_contract.vbillno,dh_contract.dbilldate ";
		DhContractVO[] billHs = null;
		ArrayList list = new ArrayList();
		 IdhServer pfserver =  NCLocator.getInstance().lookup(IdhServer.class);

		try {
			list = (ArrayList) getQuerypf().executeQuery(strSql,
					new BeanListProcessor(DhContractVO.class));
			if (list.size() > 0) {
				billHs = (DhContractVO[]) list.toArray(new DhContractVO[0]);
			}
			
			/*********************************查询合同实际付款金额wanglong************************/
			
			  if(billHs==null)
				  return null;
				String[] htcodes = new String[billHs.length];
				for (int i = 0; i < billHs.length; i++) {
					htcodes[i] = billHs[i].getCtcode();
				}
				String strwhere = BfStringUtil.getWherePartByKeys("TEMQ_CT.jobname ", htcodes, false);
				HtzxReportVO[] htzxvos = pfserver.queryHtzxVo(strwhere);
				HashMap htzxmap = new HashMap();
				for (int i = 0; i < htzxvos.length; i++) {
					htzxmap.put(htzxvos[i].getJobname(), htzxvos[i]);
				}
				
				ArrayList listvos = new ArrayList();
				for (int i = 0; i < billHs.length; i++) {
					HtzxReportVO htzxvo = (HtzxReportVO) htzxmap.get(billHs[i].getCtcode());
					UFDouble ufsjfk = new UFDouble(0.00);
					if(htzxvo!=null)
					  ufsjfk = htzxvo.getSjfk();
					UFDouble ufhtje = billHs[i].getDctjetotal()==null?new UFDouble(0.00):billHs[i].getDctjetotal();
					if(ufhtje.doubleValue() > ufsjfk.doubleValue()){
						billHs[i].setLjsjfkje(ufsjfk);
						listvos.add(billHs[i]);
					}
				}
				
				/*********************************end查询合同实际付款金额************************/
				
				
				if (listvos.size() > 0) {
					billHs = (DhContractVO[]) listvos.toArray(new DhContractVO[0]);
				}else{
					billHs = null;
				}
				
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return billHs;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(
			String where) throws BusinessException {
		return queryAllHeadData(where, null);
	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key,
			String whereString) throws BusinessException {

		return null;
	}

	public Object[] queryAllBillDatas(String sheadsql, String sbodysql)
			throws BusinessException {
		if (sheadsql == null || sheadsql.trim().length() <= 0)
			return null;

		// 创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		try {
			String sheadsqlnew = sheadsql;

			if (sbodysql != null && sbodysql.trim().length() > 0)
				if (sbodysql.trim().startsWith("and")
						|| sbodysql.trim().startsWith("AND"))
					sheadsqlnew += " " + sbodysql;
				else
					sheadsqlnew += " and " + sbodysql;

			CircularlyAccessibleValueObject[] headvos = queryAllHeadData(sheadsqlnew);

			if (headvos == null || headvos.length <= 0)
				return null;

			Object[] retobjs = new Object[3];
			retobjs[0] = headvos;
			if (headvos != null && headvos.length > 5000) {
				CircularlyAccessibleValueObject[] bakvos = headvos;
				headvos = new DhContractVO[5000];
				System.arraycopy(bakvos, 0, headvos, 0, headvos.length);
				retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
			}
			timer
					.addExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询--表头查询]用时：>>>>>>>>>>>");

			sbodysql = sbodysql + sheadsql;

			DhContractBVO voaItem[] = queryAllBodyDataByWhere(sbodysql, null);

			retobjs[1] = voaItem;

			timer
					.addExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询--表体查询]用时：>>>>>>>>>>>");

			// 输出各个业务操作占用时间分布
			timer
					.showAllExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询]结束：>>>>>>>>>>>");

			return retobjs;

		} catch (Exception ee) {
			nc.vo.scm.pub.SCMEnv.out(ee.getMessage());
			throw new BusinessException(ee.getMessage());
		}

	}

	public DhContractBVO[] queryAllBodyDataByWhere(String swhere, String orderby)
			throws Exception {

		String strSql = " select  dh_contract_b.*  from  dh_contract inner join dh_contract_b on dh_contract.pk_contract = dh_contract_b.pk_contract where isnull(dh_contract_b.dr,0) = 0 ";

		if (swhere != null && swhere.trim().length() > 0)
			strSql = strSql + " and " + swhere;

		strSql = strSql
				+ " order by  dh_contract.vbillno,dh_contract.dbilldate ";
		DhContractBVO[] billBs = null;
		ArrayList<DhContractBVO> list = new ArrayList<DhContractBVO>();

		try {
			list = (ArrayList) getQuerypf().executeQuery(strSql,
					new BeanListProcessor(DhContractBVO.class));
			if (list.size() > 0) {
				billBs = (DhContractBVO[]) list.toArray(new DhContractBVO[0]);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return billBs;

	}

	private IUAPQueryBS getQuerypf() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	public HYBillVO queryAllDataById(String id) {

		String strSql = " select  dh_contract.*  from dh_contract where pk_contract = '"
				+ id
				+ "' and isnull(dh_contract.dr,0) = 0 and dh_contract.vbillstatus = 1 ";

		strSql = strSql
				+ " order by  dh_contract.vbillno,dh_contract.dbilldate ";

		String strbodySql = " select  dh_contract_b.*  from  dh_contract inner join dh_contract_b on dh_contract.pk_contract = dh_contract_b.pk_contract where  dh_contract.pk_contract = '"
				+ id + "' and isnull(dh_contract_b.dr,0) = 0 ";

		DhContractVO[] billHs = null;
		DhContractBVO[] billbodyHs = null;
		ArrayList list = new ArrayList();
		ArrayList bodylist = new ArrayList();
		HYBillVO aggvo = new HYBillVO();
		try {
			list = (ArrayList) getQuerypf().executeQuery(strSql,
					new BeanListProcessor(DhContractVO.class));

			if (list.size() > 0) {
				bodylist = (ArrayList) getQuerypf().executeQuery(strbodySql,
						new BeanListProcessor(DhContractBVO.class));

				billHs = (DhContractVO[]) list.toArray(new DhContractVO[0]);

				DhContractVO headvo = billHs[0];
				aggvo.setParentVO(headvo);
				if (bodylist.size() > 0) {
					billbodyHs = (DhContractBVO[]) bodylist
							.toArray(new DhContractBVO[0]);
					aggvo.setChildrenVO(billbodyHs);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return aggvo;

	}

}
