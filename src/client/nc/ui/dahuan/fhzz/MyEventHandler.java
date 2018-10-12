package nc.ui.dahuan.fhzz;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.fhzz.DhDelZZVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	public String strQuery = "";

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		// ���Ȳ�ѯ�Ƿ��о���������������Ľ�ɫ
		String ztcsql = " select count(1) from sm_user_role f where f.cuserid='"+this._getOperator()+"' and f.pk_corp='"+this._getCorp().getPk_corp()+"' " +
				" and f.pk_role in(select r.pk_role from sm_role r where r.role_code in('CS2','DH00','DH01','DH08')) ";
		int zcot = (Integer)iQ.executeQuery(ztcsql, new ColumnProcessor());
		
		strQuery = "select v.pk_contract,v.httype,v.ctcode htcode,v.ctname htname,v.jobcode xmcode,v.jobname xmname,v.amount_sum dey_amount,v.isdel, "
			+ " v.dctjetotal dhjetotal,v.sy_amount dhsyje,v.custname from v_dhdelivery v left join bd_jobmngfil m " 
			+ " on v.pk_jobmandoc=m.pk_jobmngfil where nvl(m.dr,0)=0 and nvl(m.sealflag,'N')='N' and "; //���˷�� by tcl
		
		if(zcot<1){
		
			String tcsql = " select count(1) from sm_user_role f where f.cuserid='"+this._getOperator()+"' and f.pk_corp='"+this._getCorp().getPk_corp()+"' " +
					" and f.pk_role=(select r.pk_role from sm_role r where r.role_code='DHFH') ";
			int cot = (Integer)iQ.executeQuery(tcsql, new ColumnProcessor());
	
				
				if(cot<1){
					strQuery += " v.ishttype=1 and ";
				}
				
			strQuery += " exists (select cn.pk_contract from dh_contract cn where cn.pk_contract=v.pk_contract and exists (select 1 from v_deptperonal vd "
				+ " where vd.pk_corp='"+this._getCorp().getPrimaryKey()+"' and vd.pk_user='"+this._getOperator()+"' and (vd.pk_deptdoc = cn.pk_deptdoc or vd.pk_deptdoc =cn.ht_dept))) " +
						" and v.is_delivery<>1 and v.ctname<>'���ǲɹ�'  " +
						" and (v.ishttype=1 or (v.ishttype=0 and v.is_delivery=0)) and " + strWhere +" order by v.ctcode "; //ȥ�����ǲɹ� by tcl
		}else{
			
			int month = this._getDate().getMonth();
			int year = this._getDate().getYear();
			String stdate = "";
			if(month>6){
				
				int stmonth = month-6;
				if(stmonth>9){
					stdate = year+"-"+stmonth+"-01";
				}else{
					stdate = year+"-0"+stmonth+"-01";
				}
			}else{
				int styear = year-1;
				int stmonth = 6+month;
				if(stmonth>9){
					stdate = styear+"-"+stmonth+"-01";
				}else{
					stdate = styear+"-0"+stmonth+"-01";
				}
			}
			strQuery += strWhere +" and ((v.is_delivery<>1) or (v.is_delivery=1 and v.stdate > '"+stdate+"')) " +
					" and ((v.ishttype=1 or v.ishttype=0) and v.is_delivery=0) and v.ctname<>'���ǲɹ�' order by v.ctcode ";//ȥ�����ǲɹ� by tcl
			
		}
		refreshListPanel();
	}


	public void refreshListPanel() throws Exception{
		
		if("".equals(strQuery)){
			return;
		}
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		List<DhDelZZVO> delzzlit = (List<DhDelZZVO>)iQ.executeQuery(strQuery,new BeanListProcessor(DhDelZZVO.class));
		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(delzzlit.toArray(new DhDelZZVO[0]));

		updateBuffer();
	}
	
	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf) throws Exception {

		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	
	}



	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == IdhButton.DELIVERYQRY){
			deliveryQry();
		}
		
	}	

	public void deliveryQry() throws Exception{
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhDelZZVO zzvo = (DhDelZZVO)aggvo.getParentVO();
			String pkCon = zzvo.getPk_contract();
			DeliveryDialog ddg = new DeliveryDialog(this.getBillUI());
			ddg.showDeliveryDialog(pkCon);
		}
	
	}

	@Override
	protected void onBoRefresh() throws Exception {
		refreshListPanel();
	}

	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhDelZZVO zzvo = (DhDelZZVO)aggvo.getParentVO();
			String pkCon = zzvo.getPk_contract();
			if(null != pkCon && !"".equals(pkCon)){
				
				String chsql = "select count(1) from dh_delivery where nvl(dr,0)=0 and nvl(isdelivery,0)=0 and pk_contract = '"+pkCon+"'";
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				int count = (Integer)iQ.executeQuery(chsql, new ColumnProcessor());
				if(count > 0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬ����δȷ�ϵķ����������ɷ�����ֹ����");
					return;
				}	
				
				String htcode=zzvo.getHtcode();
				htcode=htcode.substring(0, htcode.length()-3);
				String sql="select nvl(sum(v.sy_amount),0) sy_amount from v_dhdelivery v where ishttype=1 and is_delivery=0 " +
						" and v.ctcode like '"+htcode+"%' and v.ctname<>'���ǲɹ�' " ;
				Object obj=iQ.executeQuery(sql, new ColumnProcessor());
				UFDouble syje=new UFDouble(obj.toString());
				if(syje.compareTo(new UFDouble(0))>0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬδȫ����������������Ŀ��ɲ�����");
					return ;
				}
				DhContractVO dvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkCon);
				dvo.setIs_delivery(1);
				HYPubBO_Client.update(dvo);
				
				if(null != dvo.getRelationid() && !"".equals(dvo.getRelationid())){
					DhContractVO rvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, dvo.getRelationid());
					rvo.setIs_delivery(1);
					HYPubBO_Client.update(rvo);
				}
				
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬ��ȫ������");
				super.onBoReturn();
				refreshListPanel();
			}else{
				MessageDialog.showErrorDlg(this.getBillUI(), "����", "�ú�ͬ�Ѳ�����");
			}
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
		}
	}


	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhDelZZVO zzvo = (DhDelZZVO)aggvo.getParentVO();
			String pkCon = zzvo.getPk_contract();
			if(null != pkCon && !"".equals(pkCon)){
				
				String chsql = "select count(1) from dh_delivery where nvl(dr,0)=0 and nvl(isdelivery,0)=0 and pk_contract = '"+pkCon+"'";
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				int count = (Integer)iQ.executeQuery(chsql, new ColumnProcessor());
				if(count > 0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬ����δȷ�ϵķ����������ɷ�����ֹ����");
					return;
				}				
				if(zzvo.getDhsyje().compareTo(new UFDouble(0))>0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬ����ʣ���Ϊ0��������ֹ������");
					return ;
				}
				DhContractVO dvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkCon);
				
				dvo.setIs_delivery(1);
				HYPubBO_Client.update(dvo);
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�ú�ͬ����ֹ����");
				super.onBoReturn();
				refreshListPanel();
			}else{
				MessageDialog.showErrorDlg(this.getBillUI(), "����", "�ú�ͬ�Ѳ�����");
			}
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
		}
	}	
	
}