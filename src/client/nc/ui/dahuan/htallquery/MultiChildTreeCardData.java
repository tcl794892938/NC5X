package nc.ui.dahuan.htallquery;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.job.IJobtypePrivate;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * 树状数据的封装类
 * 
 * 树的数据类封装了树的构造规则，如何取得数据等。按 ID 构造的树， 其数据类必须实现 IVOTreeDataByID 接口，而按编码构造的树，其数据类
 * 必须实现IVOTreeDataByCode接口。
 * 
 * @author LINQI
 * 
 */
public class MultiChildTreeCardData implements IVOTreeDataByCode {

	private SuperVO[] vos = null;
	
	private IJobtypePrivate pfserver = null;

	public String getCodeFieldName() {
		return "jobcode";
	}

	
	
	public String getCodeRule() {
		JobtypeVO  typevo =  queryJobtype();
		if(typevo.getJobtypecode().equalsIgnoreCase("01"))
			return typevo.getJobclclass();
		
		return "/3/2/3/3/3/3/";
	}

	
	private JobtypeVO queryJobtype(){
		
		String pk_corp =ClientEnvironment.getInstance().getCorporation()
		.getPk_corp();
		try {
			JobtypeVO[] typevos =  getJobtypePrivate().queryAllJobtypeVOs(pk_corp, false);
			for (int i = 0; i < typevos.length; i++) {
				if(typevos[i].getJobtypecode().equalsIgnoreCase("01")){
				 return typevos[i];
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private IJobtypePrivate getJobtypePrivate(){
		if(pfserver==null){
			 pfserver = (IJobtypePrivate)NCLocator.getInstance().lookup(IJobtypePrivate.class.getName());
			 return pfserver;
		}
		 return pfserver;
	}
	
	
	
	
	
	
	
	public String getShowFieldName() {
		return "jobcode+ctcode";
	}

	public SuperVO[] getTreeVO() {
		return queryTreeData();
	}

	
	private SuperVO[] queryTreeData() {

		try {
			IUAPQueryBS iQuery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
			
			String[] retStr = new String[]{"pk_contract", "pk_corp", "jobcode","ctcode"};
			
			// 公司过滤
			String pkcorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			
			List<DhContractVO> list = (List<DhContractVO>) iQuery.retrieveByClause(DhContractVO.class," dh_contract.pk_corp = '"+pkcorp+"' and nvl(dh_contract.dr,0)=0 order by jobcode ",retStr);
			
			vos = (DhContractVO[]) list.toArray(new DhContractVO[0]);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(null, null, e.getMessage());
		} catch (BusinessRuntimeException e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(null, null, e.getMessage());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showUnknownErrorDlg(null, e);
		}
		return vos;
	}

}
