/*
 * �������� 2005-8-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.report.base;

public class QueryYchtAction extends AbstractActionAlwaysAvailable {

	public QueryYchtAction(ReportUIBase reportUIBase) {
		super(reportUIBase);
	}

	public QueryYchtAction() {
		super();
	}

	public void execute() throws Exception {
		getReportUIBase().onQueryYcht();
		getReportUIBase().showHintMessage("��ѯ�쳣��ͬ�ɹ�");
	}

}