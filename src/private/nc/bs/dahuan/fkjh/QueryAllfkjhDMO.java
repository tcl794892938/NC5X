package nc.bs.dahuan.fkjh;

import java.util.ArrayList;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.scm.pub.redun.IRedunSource;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class QueryAllfkjhDMO extends DataManageObject implements IQueryData,
		IQueryData2, IRedunSource {

	public QueryAllfkjhDMO() throws NamingException {
		super();

	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {

		return null;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(
			String where, Object objIsWaitAudit) {
		String strSql = " select  dh_fkjhbill.*  from dh_fkjhbill where isnull(dh_fkjhbill.dr,0) = 0 and dh_fkjhbill.vbillstatus = 1 ";

		if (where != null && where.trim().length() > 0)
			strSql = strSql + " AND " + where;

		strSql = strSql
				+ " order by  dh_fkjhbill.vbillno,dh_fkjhbill.dbilldate ";
		DhFkjhbillVO[] billHs = null;
		ArrayList list = new ArrayList();

		try {
			list = (ArrayList) getQuerypf().executeQuery(strSql,
					new BeanListProcessor(DhFkjhbillVO.class));
			if (list.size() > 0) {
				billHs = (DhFkjhbillVO[]) list.toArray(new DhFkjhbillVO[0]);
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
				headvos = new DhFkjhbillVO[5000];
				System.arraycopy(bakvos, 0, headvos, 0, headvos.length);
				retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
			}
			timer
					.addExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询--表头查询]用时：>>>>>>>>>>>");

			DhFkjhbillVO voaItem[] = queryAllBodyDataByWhere(sbodysql, null);

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

	public DhFkjhbillVO[] queryAllBodyDataByWhere(String swhere, String orderby)
			throws Exception {

		String strSql = " select  dh_fkjhbill.*  from dh_fkjhbill  where  isnull(dh_fkjhbill.dr,0) = 0 ";

		if (swhere != null && swhere.trim().length() > 0)
			strSql = strSql + swhere;

		strSql = strSql
				+ " order by  dh_fkjhbill.vbillno,dh_fkjhbill.dbilldate ";
		DhFkjhbillVO[] billBs = null;
		ArrayList<DhFkjhbillVO> list = new ArrayList<DhFkjhbillVO>();

		try {
			list = (ArrayList) getQuerypf().executeQuery(strSql,
					new BeanListProcessor(DhFkjhbillVO.class));
			if (list.size() > 0) {
				billBs = (DhFkjhbillVO[]) list.toArray(new DhFkjhbillVO[0]);
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

}
