package nc.ui.report.base;

public class QueryZbAction extends AbstractActionAlwaysAvailable {
	
	public QueryZbAction(ReportUIBase reportUIBase) {
		super(reportUIBase);
	}

	public QueryZbAction() {
		super();
	}

	public void execute() throws Exception {

		getReportUIBase().onQueryZb();
		getReportUIBase().showHintMessage("小计总包合同");
	}

}
