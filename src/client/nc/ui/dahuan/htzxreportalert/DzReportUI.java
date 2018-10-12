package nc.ui.dahuan.htzxreportalert;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.FramePanel;
import nc.ui.report.base.ReportUIBase;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.dahuan.report.AlertDzVO;
import nc.vo.pub.lang.UFDouble;

public class DzReportUI extends ReportUIBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DzReportUI() {
		super();

	}

	public DzReportUI(FramePanel fp) {
		super(fp);
	}
	
	
	IUAPQueryBS uapbs = NCLocator.getInstance().lookup(
			IUAPQueryBS.class);

	protected void onQuery() throws Exception {

		if (getQryDlg().showModal() == QueryDLG.ID_OK) {
			String strCondition = getQryDlg().getWhereSQL()==null?"":" and " +getQryDlg().getWhereSQL();
            String querySql = "select bd_corp.unitname,dh_htalertbase.JOBCODE,dh_htalertbase.JOBNAME,dh_htalertbase.DEF10, dh_htalertbase.CUSTNAME,	dh_htalertbase.DEPTNAME,	dh_htalertbase.SJSK,"+
                  " nvl(dh_htalertbase.SJFK,0.00)+nvl(dh_htalertbase.SJFY,0.00) SJFK,"+
                  " nvl(dh_htalertbase.SJSK,0.00)-nvl(dh_htalertbase.SJFK,0.00)-nvl(dh_htalertbase.SJFY,0.00) dzks "+
                  " from dh_htalertbase,bd_corp where dh_htalertbase.pk_corp = bd_corp.pk_corp "+
                  " and (dh_htalertbase.ALERTYPE = 1) and (nvl(dh_htalertbase.SJSK,0.00)-nvl(dh_htalertbase.SJFK,0.00)-nvl(dh_htalertbase.SJFY,0.00) < 0) "+strCondition;
			
			ArrayList list = (ArrayList) uapbs.executeQuery(querySql.toString(),
					new ArrayListProcessor());
        	Object[] vobjs;
        	Object clistobj;
        	String tmp;
        	ArrayList<AlertDzVO> ListVO = new ArrayList<AlertDzVO>();
            for (int i = 0; i < list.size(); i++) {
            	clistobj = list.get(i);
				vobjs = (Object[]) clistobj;
				AlertDzVO dzvo = new AlertDzVO();
				tmp = vobjs[0] == null ? " " : vobjs[0].toString();
				dzvo.setUnitname(tmp);
				tmp = vobjs[1] == null ? " " : vobjs[1].toString();
				dzvo.setJobcode(tmp);
				tmp = vobjs[2] == null ? " " : vobjs[2].toString();
				dzvo.setJobname(tmp);
				tmp = vobjs[3] == null ? " " : vobjs[3].toString();
				dzvo.setProjetname(tmp);
				tmp = vobjs[4] == null ? " " : vobjs[4].toString();
				dzvo.setCustname(tmp);
				tmp = vobjs[5] == null ? " " : vobjs[5].toString();
				dzvo.setDeptname(tmp);
				tmp = vobjs[6] == null ? " " : vobjs[6].toString();
				dzvo.setSjsk(new UFDouble(tmp));
				tmp = vobjs[7] == null ? " " : vobjs[7].toString();
				dzvo.setSjfk(new UFDouble(tmp));
				tmp = vobjs[8] == null ? " " : vobjs[8].toString();
				dzvo.setDzks(new UFDouble(tmp));
				if ((dzvo.getSjsk() != null) &&(dzvo.getSjsk().doubleValue() != 0)) {
					 UFDouble dzkslv = dzvo.getDzks().div(dzvo.getSjsk())
			            .multiply(100);
			          dzvo.setDzkslv(dzkslv);
			    } else {
			        dzvo.setDzkslv(new UFDouble(100));
			    }
				
				ListVO.add(dzvo);
		
            }
            AlertDzVO[] itemvos = ListVO.toArray(new AlertDzVO[0]);
            setBodyDataVO(itemvos, false);
            
            this.getReportBase().getBillModel().execLoadFormula();

		}

	}

	
	protected void onClearItemyj() throws Exception {
		
		
	}

	
	protected void onQueryFh() throws Exception {
		
		
	}

	
	protected void onQueryFpMx() throws Exception {
		
		
	}

	
	protected void onQueryMx() throws Exception {
		
		
	}

	
	protected void onQueryYcht() throws Exception {
		
		
	}

	@Override
	protected void onQueryZb() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onQueryYCPZ() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
