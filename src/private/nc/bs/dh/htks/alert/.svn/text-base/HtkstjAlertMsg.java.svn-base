package nc.bs.dh.htks.alert;

import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.dahuan.report.HtmxReportVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class HtkstjAlertMsg implements IAlertMessage {

	private static final long serialVersionUID = 1L;
	SuperVO[] m_epxensevos = null;

	public HtkstjAlertMsg(SuperVO[] epxensevos) {
		super();
		this.m_epxensevos = epxensevos;
	}

	public String[] getBodyFields() {

		return new String[] { "合同编号","合同名称","客户名称", "销售金额", "采购金额", "合同统计亏损" };
	}

	public Object[][] getBodyValue() {
		int size = m_epxensevos.length;
		Object[][] content = new Object[size][6];
		for (int i = 0; i < m_epxensevos.length; i++) {
			HtmxReportVO itemvo = (HtmxReportVO) m_epxensevos[i];
			content[i][0] = itemvo.getJobname();
			content[i][1] = itemvo.getDef1();
			content[i][2] = itemvo.getCustname();
			UFDouble tmp = itemvo.getXsj()==null?new UFDouble("0.00"):itemvo.getXsj();
			content[i][3] = new UFDouble(tmp.toString(), 2);
			         tmp = itemvo.getCgj()==null?new UFDouble("0.00"):itemvo.getCgj();
			content[i][4] = new UFDouble(tmp.toString(),2);
			         tmp = itemvo.getCe()==null?new UFDouble("0.00"):itemvo.getCe();
			content[i][5] = new UFDouble(tmp.toString(),2);
		}
		return content;
	}

	public float[] getBodyWidths() {

		return new float[] { 0.3F, 0.3F, 0.5F, 0.5F, 0.5F, 0.5F };
	}

	public String[] getBottom() {
		return null;
	}

	public String getTitle() {

		return "合同统计预警";
	}

	public String[] getTop() {

		return null;
	}

}
