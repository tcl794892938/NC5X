/*
 * 创建日期 2005-8-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
		getReportUIBase().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000031")/*@res "查询成功"*/);
	}

}