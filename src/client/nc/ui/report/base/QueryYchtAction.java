/*
 * 创建日期 2005-8-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
		getReportUIBase().showHintMessage("查询异常合同成功");
	}

}