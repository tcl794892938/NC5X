/*
 * �������� 2005-8-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.report.base;


public class QueryFpMxAction extends AbstractActionAlwaysAvailable
{


	public QueryFpMxAction(ReportUIBase reportUIBase)
	{
		super(reportUIBase);
	}

	public QueryFpMxAction()
	{
		super();
	}


	public void execute() throws Exception
	{
		getReportUIBase().onQueryFpMx();
		//TODO i18n
		getReportUIBase().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000031")/*@res "��ѯ�ɹ�"*/);
	}

}