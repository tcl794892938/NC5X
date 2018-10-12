package nc.ui.demo.tree.tree03;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.job.IJobtypePrivate;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.vo.bd.b36.JobtypeVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * ��״���ݵķ�װ��
 * 
 * �����������װ�����Ĺ���������ȡ�����ݵȡ��� ID ��������� �����������ʵ�� IVOTreeDataByID �ӿڣ��������빹���������������
 * ����ʵ��IVOTreeDataByCode�ӿڡ�
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
			
			String corpgl = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			String userdl = ClientEnvironment.getInstance().getUser().getPrimaryKey();
			
//			2017-12-14�Զ���Ȩ��(ֻ���Զ������Ĳ�ѯ���а��������ĺ�ͬ)
			String sqlss="select count(1) from v_deptperonal v where v.pk_user='"+userdl+"' and pk_corp='"+corpgl+"' and dept_name='�Զ�����' ";
			IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Integer it=(Integer)iQ.executeQuery(sqlss, new ColumnProcessor());
			String str="";
			if(it>0){
				str=" or dh_contract.ctcode like '%����%' ";
			}
			
			// v_deptperonal������Ա��ϵ��ͼ
			String condition = " nvl(dh_contract.dr,0)=0  and pk_corp='" + corpgl + "' " +
					" and (exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
					" where v_deptperonal.pk_user = '"+userdl+"' and v_deptperonal.pk_corp = '"+corpgl+"' " +
					" and (v_deptperonal.pk_deptdoc=dh_contract.pk_deptdoc or " +
					" v_deptperonal.pk_deptdoc=dh_contract.ht_dept))  " +
					" or nvl(httype,0) = 2 "+str+") order by jobcode ";
			
			String[] retStr = new String[]{"pk_contract", "pk_corp", "jobcode","ctcode"};
			
			List<DhContractVO> list = (List<DhContractVO>) iQuery.retrieveByClause(DhContractVO.class,condition,retStr);
			
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
