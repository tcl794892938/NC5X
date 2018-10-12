package nc.bs.dh.htks.alert;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dahuan.report.HtmxReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pa.Key;

public class HtkszxPlugin implements IBusinessPlugin {

	public int getImplmentsType() {

		return IMPLEMENT_RETURNFORMATMSG;
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

		String pk_corp = "";
		String Condition = null;
		Condition = " and pk_corp = '"+pk_corp+ "' ";
		for (int i = 0; i < keys.length; i++) {
			if ("pk_corp".equals(keys[i].getName())) {
				pk_corp = (String) keys[i].getValue();
				Condition = " and pk_corp = '"+pk_corp+ "' ";
			}

			if ("Condition".equals(keys[i].getName())&& keys[i].getValue()!=null) {
				Condition = " and deptname = '"+(String) keys[i].getValue()+ "' ";
			}
			
			if ("xmcode".equals(keys[i].getName())&& keys[i].getValue()!=null) {
				Condition = " and xmcode  >= '"+(String) keys[i].getValue()+ "' ";
			}
		}

		if(Condition==null)
			Condition = " and 1=1";
		
		HtmxReportVO[] itemvos1 = queryAlertHtzx(Condition);

		return new HtkszxAlertMsg(itemvos1);
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

		return null;
	}

	private HtmxReportVO[] queryAlertHtzx(String Condition)
			throws BusinessException {

		StringBuffer sql1 = new StringBuffer();
		sql1.append("	select aa.jobcode,aa.jobname,aa.custname, aa.xsj,aa.cgj,aa.ce,aa.sjsk,aa.sjfk,aa.sjfy ,aa.sjml from dh_htalert aa where aa.alertype = 1 and (isnull(aa.xsj,0.00)-isnull(aa.cgj,0.00)-isnull(aa.sjfy,0.00)) < 0 "
				+ Condition+ " order by aa.xmcode ");

		IUAPQueryBS uapbs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		ArrayList list = (ArrayList) uapbs.executeQuery(sql1.toString(),
				new ArrayListProcessor());
		Object clistobj;
		Object[] vobjs;
		String tmp;
		ArrayList<HtmxReportVO> ExpenseHTzxVOList = new ArrayList<HtmxReportVO>();
		HtmxReportVO expensehtzxVo = null;
		if (list.size() != 0) {
			clistobj = list.get(0);
			vobjs = (Object[]) clistobj;
			for (int i = 0; i < list.size(); i++) {
				clistobj = list.get(i);
				vobjs = (Object[]) clistobj;
				expensehtzxVo = new HtmxReportVO();
				tmp = vobjs[0] == null ? " " : vobjs[0].toString();
				expensehtzxVo.setJobname(tmp);
				tmp = vobjs[1] == null ? " " : vobjs[1].toString();
				expensehtzxVo.setDef1(tmp);
				tmp = vobjs[2] == null ? " " : vobjs[2].toString();
				expensehtzxVo.setCustname(tmp);
				tmp = vobjs[3] == null ? " " : vobjs[3].toString();
				expensehtzxVo.setXsj(new UFDouble(tmp));
				tmp = vobjs[4] == null ? " " : vobjs[4].toString();
				expensehtzxVo.setCgj(new UFDouble(tmp));
				tmp = vobjs[5] == null ? " " : vobjs[5].toString();
				expensehtzxVo.setCe(new UFDouble(tmp));
				tmp = vobjs[6] == null ? " " : vobjs[6].toString();
				expensehtzxVo.setSjsk(new UFDouble(tmp));
				tmp = vobjs[7] == null ? " " : vobjs[7].toString();
				expensehtzxVo.setSjfk(new UFDouble(tmp));
				tmp = vobjs[8] == null ? " " : vobjs[8].toString();
				expensehtzxVo.setSjfy(new UFDouble(tmp));
				tmp = vobjs[9] == null ? " " : vobjs[9].toString();
				expensehtzxVo.setSjml(new UFDouble(tmp));
				
				ExpenseHTzxVOList.add(expensehtzxVo);
			}

			HtmxReportVO[] itemvos = (HtmxReportVO[]) ExpenseHTzxVOList
					.toArray(new HtmxReportVO[0]);

			return itemvos;
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
