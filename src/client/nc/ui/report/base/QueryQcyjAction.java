/*
 * 创建日期 2005-8-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
		getReportUIBase().showHintMessage("清空已结算成功");
	}

}