/*
 * 创建日期 2005-8-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
		getReportUIBase().showHintMessage("异常凭证关联合同号查询成功！");
	}

}