/*
 * �������� 2005-8-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.report.base;

public class QueryQcyjAction extends AbstractActionAlwaysAvailable {

	public QueryQcyjAction(ReportUIBase reportUIBase) {
		super(reportUIBase);
	}

	public QueryQcyjAction() {
		super();
	}

	public void execute() throws Exception {
		getReportUIBase().onClearItemyj();
		getReportUIBase().showHintMessage("����ѽ���ɹ�");
	}

}