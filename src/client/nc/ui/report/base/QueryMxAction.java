/*
 * �������� 2005-8-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.report.base;

/**
 * @author dengjt
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class QueryMxAction extends AbstractActionAlwaysAvailable
{

	/**
	 * @param reportUIBase
	 */
	public QueryMxAction(ReportUIBase reportUIBase)
	{
		super(reportUIBase);
	}

	/**
	 *
	 */
	public QueryMxAction()
	{
		super();
	}

	/*
	 * ���� Javadoc��
	 *
	 * @see nc.ui.report.base.actions.IButtonActionAndState#execute()
	 */
	public void execute() throws Exception
	{
		getReportUIBase().onQueryMx();
		//TODO i18n
		getReportUIBase().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000031")/*@res "��ѯ�ɹ�"*/);
	}

}