package nc.bs.dh.htks.alert;

import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.dahuan.report.HtmxReportVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class HtkszxAlertMsg implements IAlertMessage {

	private static final long serialVersionUID = 1L;
	SuperVO[] m_epxensevos = null;

	public HtkszxAlertMsg(SuperVO[] epxensevos) {
		super();
		this.m_epxensevos = epxensevos;
	}

	public String[] getBodyFields() {
		return new String[] { "合同编号","合同名称","客户名称", "销售金额", "采购金额", "合同执行亏损" };
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
			content[i][3] = new UFDouble(tmp.toString(),2);
			UFDouble  tmp1= itemvo.getCgj().add(itemvo.getSjfy())==null?new UFDouble("0.00")
			:itemvo.getCgj().add(itemvo.getSjfy());
			content[i][4] = new UFDouble(tmp1.toString(),2);
			UFDouble  tmp3= tmp.sub(tmp1);
			content[i][5] = new UFDouble(tmp3.toString(),2);
		}

		return content;
	}

	public float[] getBodyWidths() {

		return new float[] { 0.1F, 0.3F, 0.3F, 0.2F, 0.2F, 0.2F, 0.2F };
	}

	public String[] getBottom() {

		return null;
	}

	public String getTitle() {

		return "合同执行预警";
	}

	public String[] getTop() {

		return null;
	}

}
