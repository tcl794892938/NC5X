/*
 * �������� 2005-8-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.report.base;

public class QueryYCPZAction extends AbstractActionAlwaysAvailable {

	public QueryYCPZAction(ReportUIBase reportUIBase) {
		super(reportUIBase);
	}

	public QueryYCPZAction() {
		super();
	}

	public void execute() throws Exception {
		getReportUIBase().onQueryYCPZ();
		getReportUIBase().showHintMessage("�쳣ƾ֤������ͬ�Ų�ѯ�ɹ���");
	}

}