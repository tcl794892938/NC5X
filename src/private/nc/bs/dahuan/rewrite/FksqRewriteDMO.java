package nc.bs.dahuan.rewrite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.NamingException;

import nc.bs.pub.DataManageObject;
import nc.vo.dahuan.fkd.DhFkbillBVO;
import nc.vo.dahuan.fkd.DhFkbillVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;

public class FksqRewriteDMO extends DataManageObject {

	public FksqRewriteDMO() throws NamingException {
		super();
	}

	public java.util.Hashtable getAnyValue(String tableName, String fieldName,
			String key, String[] values) throws SQLException {
		if (tableName == null || fieldName == null || key == null
				|| values == null || values.length <= 0)
			return null;

		Hashtable table = new Hashtable();
		ArrayList ids = new ArrayList();
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null && !values[i].equals("")
					&& !ids.contains(values[i])) {
				ids.add(values[i]);
			}
		}
		StringBuffer sWhere = new StringBuffer();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int onenum = 200;

		try {

			con = getConnection();

			for (int i = 0, count = (ids.size() % onenum == 0 ? ids.size()
					/ onenum : ids.size() / onenum + 1); i < count; i++) {

				String sql = " select " + key + ", " + fieldName + " from "
						+ tableName + " where " + key + " in ( ";
				sWhere.setLength(0);
				for (int j = 0, count1 = ids.size() - i * onenum > onenum ? onenum
						: ids.size() - i * onenum; j < count1; j++) {
					if (j > 0) {
						sWhere.append(" , ");
					}
					sWhere.append("'" + ids.get(j + i * onenum).toString()
							+ "'");
				}
				sWhere.append(" ) ");

				sql += sWhere.toString();
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				//
				String keyValue = null;
				String fieldValue = null;
				while (rs.next()) {
					keyValue = rs.getString(1);
					if (keyValue == null || keyValue.trim().equals(""))
						continue;
					fieldValue = rs.getString(2);
					if (fieldValue == null || fieldValue.trim().equals(""))
						continue;
					if (!table.containsKey(keyValue))
						table.put(keyValue, fieldValue);
				}
				rs.close();
				stmt.close();

			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return table;

	}

	/**
	 * 由于现在回写后可以修改原单据，所以要将修改后的差值回写给外系统。 功能： 参数： 返回： 例外： 日期：(2002-04-30 14:16:35)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void reWriteSaleNewBatch(nc.vo.dahuan.fkd.MyBillVO newVO,
			nc.vo.dahuan.fkd.MyBillVO oldVO)
			throws nc.vo.pub.BusinessException {

		if (oldVO != null
				&& (oldVO.getChildrenVO() == null || oldVO.getChildrenVO().length == 0))
			return;

		// 增加应发数量
		String[] sNumFields = new String[] { "dfkje" };
		nc.vo.dahuan.fkd.MyBillVO vot = null;

		String sCorpID = null;
		HashMap<String, DhFkbillBVO> hsnewvo = new HashMap<String, DhFkbillBVO>();
		HashMap<String, String> hsfirstrowidtorowno = new HashMap<String, String>();
		if (newVO != null) {
			vot = new nc.vo.dahuan.fkd.MyBillVO();
			vot.setParentVO((DhFkbillVO) newVO.getParentVO().clone());
			DhFkbillBVO[] itemvos = (DhFkbillBVO[]) newVO.getChildrenVO();
			if (itemvos != null) {
				for (int i = 0; i < itemvos.length; i++) {
					if (itemvos[i].getVsourcebillrowid() != null) {
						if (itemvos[i].getStatus() == VOStatus.NEW)
							hsnewvo.put(itemvos[i].getVsourcebillrowid(),
									itemvos[i]);
					}
				}
			}
		} else {
			vot = new nc.vo.dahuan.fkd.MyBillVO();
			if (oldVO == null || oldVO.getChildrenVO() == null
					|| oldVO.getChildrenVO().length <= 0)
				return;
			vot.setParentVO((DhFkbillVO) oldVO.getParentVO().clone());
		}
		sCorpID = vot.getParentVO().getAttributeValue("pk_corp").toString();

		if (oldVO != null && oldVO.getChildrenVO() != null
				&& oldVO.getChildrenVO().length > 0) {
			DhFkbillBVO[] itemvos = (DhFkbillBVO[]) oldVO.getChildrenVO();
			for (DhFkbillBVO itemvo : itemvos) {
				if (itemvo.getVsourcebillrowid() != null)
					hsfirstrowidtorowno.put(itemvo.getVsourcebillrowid(), itemvo
							.getCtcode());
			}
		}

		try {

			String[] fieldkeys = new String[] { "vsourcebillrowid",
					"vsourcebilltype" };
			// 处理应发数量
			nc.vo.dahuan.fkd.MyBillVO[] genvos = processNshouldoutnum(newVO,
					oldVO, fieldkeys, sNumFields);
			if (genvos == null)
				return;
			DhFkbillBVO[] voItems = getCombinedItems(genvos[0], genvos[1],
					fieldkeys, sNumFields);

			vot.setChildrenVO(voItems);

			if (voItems == null || voItems.length < 1) {
				return;

			}
			setOutNum(vot);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

	}

	private nc.vo.dahuan.fkd.MyBillVO[] processNshouldoutnum(
			nc.vo.dahuan.fkd.MyBillVO newVO, nc.vo.dahuan.fkd.MyBillVO oldVO,
			String[] fields, String[] snumfields) {
		nc.vo.dahuan.fkd.MyBillVO[] gvos = new nc.vo.dahuan.fkd.MyBillVO[2];
		UFDouble nnum = null, d0 = new UFDouble(0);
		if (newVO != null) {
			DhFkbillBVO[] newitemvos = (DhFkbillBVO[]) newVO.getChildrenVO();
			gvos[0] = new nc.vo.dahuan.fkd.MyBillVO();
			gvos[0].setParentVO(newVO.getParentVO());
			if (newitemvos != null && newitemvos.length > 0) {
				DhFkbillBVO[] newitemvos_c = new DhFkbillBVO[newitemvos.length];
				for (int i = 0, loop = newitemvos_c.length; i < loop; i++) {
					newitemvos_c[i] = new DhFkbillBVO();
					newitemvos_c[i].setStatus(newitemvos[i].getStatus());
					newitemvos_c[i].setCtcode(newitemvos[i].getCtcode());
					for (int k = 0, loopk = fields.length; k < loopk; k++)
						newitemvos_c[i].setAttributeValue(fields[k],
								newitemvos[i].getAttributeValue(fields[k]));
					for (int k = 0, loopk = snumfields.length; k < loopk; k++)
						newitemvos_c[i].setAttributeValue(snumfields[k],
								newitemvos[i].getAttributeValue(snumfields[k]));
					// 修改应发数量
					nnum = newitemvos_c[i].getDfkje();
				}
				gvos[0].setChildrenVO(newitemvos_c);
			}
		}

		if (oldVO != null) {
			DhFkbillBVO[] olditemvos = (DhFkbillBVO[]) oldVO.getChildrenVO();
			gvos[1] = new nc.vo.dahuan.fkd.MyBillVO();
			gvos[1].setParentVO(oldVO.getParentVO());
			if (olditemvos != null && olditemvos.length > 0) {
				DhFkbillBVO[] olditemvos_c = new DhFkbillBVO[olditemvos.length];
				for (int i = 0, loop = olditemvos_c.length; i < loop; i++) {
					olditemvos_c[i] = new DhFkbillBVO();
					olditemvos_c[i].setStatus(olditemvos[i].getStatus());
					olditemvos_c[i].setCtcode(olditemvos[i].getCtcode());
					for (int k = 0, loopk = fields.length; k < loopk; k++)
						olditemvos_c[i].setAttributeValue(fields[k],
								olditemvos[i].getAttributeValue(fields[k]));
					for (int k = 0, loopk = snumfields.length; k < loopk; k++)
						olditemvos_c[i].setAttributeValue(snumfields[k],
								olditemvos[i].getAttributeValue(snumfields[k]));
					// 修改应发数量
					nnum = olditemvos_c[i].getDfkje();
				}
				gvos[1].setChildrenVO(olditemvos_c);
			}
		}

		return gvos;
	}

	private DhFkbillBVO[] getCombinedItems(nc.vo.dahuan.fkd.MyBillVO newVO,
			nc.vo.dahuan.fkd.MyBillVO oldVO, String[] sGroupFields,
			String[] sNumFields) throws Exception {
		DhFkbillBVO[] voItems = null;
		ArrayList alvo = new ArrayList();
		if (newVO != null) {
			voItems = (DhFkbillBVO[]) newVO.getChildrenVO();
			for (int i = 0; i < voItems.length; i++) {
				if (voItems[i].getStatus() == nc.vo.pub.VOStatus.DELETED)
					continue;

				for (int j = 0; j < sGroupFields.length; j++) {
					if (voItems[i].getAttributeValue(sGroupFields[j]) != null) {
						alvo.add(voItems[i]);
						break;
					}
				}
			}
		}

		if (oldVO != null) {// &&oldVO.isQtyFilled()){
			voItems = (DhFkbillBVO[]) oldVO.getChildrenVO();
			for (int i = 0; i < voItems.length; i++) {
				DhFkbillBVO voItem = new DhFkbillBVO();// (DhFkbillBVO)voItems[i].clone();
				UFDouble neg = new UFDouble(-1.0);
				boolean isHasvalue = false;
				for (int j = 0; j < sGroupFields.length; j++) {
					if (voItems[i].getAttributeValue(sGroupFields[j]) != null) {
						voItem.setAttributeValue(sGroupFields[j], voItems[i]
								.getAttributeValue(sGroupFields[j]));
						isHasvalue = true;
					}

				}
				if (isHasvalue) {
					alvo.add(voItem);

					for (int j = 0; j < sNumFields.length; j++) {
						if (voItems[i].getAttributeValue(sNumFields[j]) != null)
							voItem.setAttributeValue(sNumFields[j],
									((UFDouble) voItems[i]
											.getAttributeValue(sNumFields[j]))
											.multiply(neg));
						else
							voItem.setAttributeValue(sNumFields[j],
									GenMethod.ZERO);

					}

				}
			}

		}
		DhFkbillBVO[] voRes = null;

		if (alvo.size() > 0) {

			DhFkbillBVO[] voItemstmp = new DhFkbillBVO[alvo.size()];
			alvo.toArray(voItemstmp);
			nc.vo.scm.merge.DefaultVOMerger m = new nc.vo.scm.merge.DefaultVOMerger();
			m.setMergeAttrs(sGroupFields, sNumFields, null, null, null);
			voRes = (DhFkbillBVO[]) m.mergeByGroupOnly(voItemstmp);

		}
		return voRes;
	}

	public void setOutNum(AggregatedValueObject outBillVO)
			throws nc.vo.pub.BusinessException {

		if (outBillVO == null || outBillVO.getParentVO() == null)
			return;

		String csourcebiibid = null;

		UFDouble noutnum;

		CircularlyAccessibleValueObject[] billbody = outBillVO.getChildrenVO();
		if (billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;
		Object id;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			// 订单表体id
			id = billbody[i].getAttributeValue("vsourcebillid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
		}// end for
		String SQLUpdateReback = "update dh_fksqbill set ljsfkje= isnull(ljsfkje,0) + ? where pk_fksqbill = ? ";

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = prepareStatement(con, SQLUpdateReback);
			for (int i = 0; i < outBillVO.getChildrenVO().length; i++) {
				CircularlyAccessibleValueObject bodyVO = outBillVO
						.getChildrenVO()[i];
				if (bodyVO.getAttributeValue("vsourcebillrowid") != null) {
					// 数量
					noutnum = (bodyVO.getAttributeValue("dfkje") == null ? new UFDouble(
							"0")
							: new UFDouble(bodyVO.getAttributeValue("dfkje")
									.toString()));
					csourcebiibid = bodyVO.getAttributeValue("vsourcebillrowid")
							.toString();
					stmt.setBigDecimal(1, noutnum.toBigDecimal());
					stmt.setString(2, csourcebiibid);
					executeUpdate(stmt);
					// 关闭连接
				}
			}

			executeBatch(stmt);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {

			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

}
