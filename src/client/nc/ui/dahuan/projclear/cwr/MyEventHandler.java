package nc.ui.dahuan.projclear.cwr;

import nc.ui.dahuan.projclear.DocumentManagerHT;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.projclear.ProjectClearDVO;
import nc.vo.dahuan.projclear.ProjectClearVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.datapower.BtnPowerVO;

public class MyEventHandler extends ManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and pc_status=8 ");

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		pcvo.setPc_status(6);
		
		pcvo.setCwr(this._getOperator());
		pcvo.setCwdate(this._getDate());
		pcvo.setCwvemo("��ȷ��");
		
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ȷ�����");
		this.onBoRefresh();
		
		//��̨�����ۼƸ�����Ⱥ�����ƾ֤��
		String conid=pcvo.getSalecontractid();
		/*String sql="select sum(nvl(d.localcreditamount, 0)) localcreditamount" +
				" from (select (select m.pk_jobbasfil from bd_jobmngfil m where m.pk_jobmngfil = t.pk_jobmandoc) pk_jobbasfil,t.pk_corp,t.vdef6, " +
				" t.ctcode,t.ctname,t.pk_cust1,t.dctjetotal,t.pk_fuzong from dh_contract t where t.pk_contract = '"+conid+"') v," +
				" gl_freevalue f,gl_detail d,bd_accsubj a,gl_voucher g where v.pk_jobbasfil = f.checkvalue and nvl(f.dr, 0) = 0 " +
				" and f.checktype = '0001A11000000000CGMX' and d.assid = f.freevalueid and nvl(d.dr, 0) = 0  and d.explanation <> '�ڳ�' " +
				" and d.pk_accsubj = a.pk_accsubj  and nvl(a.dr, 0) = 0 and d.pk_voucher = g.pk_voucher and nvl(g.dr, 0) = 0 " +
				" and g.discardflag = 'N' and ((a.subjcode in ('1122', '2203') and nvl(d.localcreditamount, 0) <> 0) or " +
				" ((a.subjcode in ('2202', '1123') or a.subjcode like '6602%') and nvl(d.localdebitamount, 0) <> 0)) and g.discardflag = 'N'" +
				" and g.errmessage is null and g.period <> '00' and g.pk_corp = v.pk_corp ";*/
		/*String sql="select nvl(r.ljfkjhje,0) ljfkjhje from dh_contract r where r.pk_contract='"+conid+"' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj2=iQ.executeQuery(sql, new ColumnProcessor());
		UFDouble ljfkmny=obj2==null?new UFDouble(0):new UFDouble(obj2.toString());*/
		UFDouble hxamount=pcvo.getHxamount();//ȡ�������
		
		/************************************** ���º�ͬ��������  start  *********************************************/
		DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conid);
		UFDouble ud1=ctvo.getLjfkjhje()==null?new UFDouble(0):ctvo.getLjfkjhje();
		UFDouble ud2=hxamount==null?new UFDouble(0):hxamount;
		ctvo.setLjfkjhje(ud1.add(ud2, 2));
		HYPubBO_Client.update(ctvo);
		
		/************************************** ���º�ͬ�����ӱ�  start  *********************************************/
		ProjectClearDVO[] dvos=(ProjectClearDVO[])aggvo.getChildrenVO();
		if(dvos!=null&&dvos.length>0){
			
			for(ProjectClearDVO dvo : dvos){
				
				String bconid=dvo.getPurcontractid();
				UFDouble dmny=dvo.getDoveramount()==null?new UFDouble(0):dvo.getDoveramount();
				if(dmny.compareTo(new UFDouble(0))==0){
					continue ;
				}
				DhContractVO ctvo2 = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, bconid);
				UFDouble ud3=ctvo2.getLjfkjhje()==null?new UFDouble(0):ctvo2.getLjfkjhje();
				ctvo2.setLjfkjhje(ud3.add(dmny,2));
				HYPubBO_Client.update(ctvo2);
			}
			
		}
	}
	
	@Override
	public void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		pcvo.setPc_status(0);
		
		pcvo.setCwr(this._getOperator());
		pcvo.setCwdate(this._getDate());
		pcvo.setCwvemo("�Ѳ���");
		
		HYPubBO_Client.update(pcvo);
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "�������");
		this.onBoRefresh();
		
		//��д��ͬ(��ѯ��ͬ���ۼ��ո���)
		String conid=pcvo.getSalecontractid();
		/*String sql="select sum(nvl(d.localcreditamount, 0)) localcreditamount" +
				" from (select (select m.pk_jobbasfil from bd_jobmngfil m where m.pk_jobmngfil = t.pk_jobmandoc) pk_jobbasfil,t.pk_corp,t.vdef6, " +
				" t.ctcode,t.ctname,t.pk_cust1,t.dctjetotal,t.pk_fuzong from dh_contract t where t.pk_contract = '"+conid+"') v," +
				" gl_freevalue f,gl_detail d,bd_accsubj a,gl_voucher g where v.pk_jobbasfil = f.checkvalue and nvl(f.dr, 0) = 0 " +
				" and f.checktype = '0001A11000000000CGMX' and d.assid = f.freevalueid and nvl(d.dr, 0) = 0  and d.explanation <> '�ڳ�' " +
				" and d.pk_accsubj = a.pk_accsubj  and nvl(a.dr, 0) = 0 and d.pk_voucher = g.pk_voucher and nvl(g.dr, 0) = 0 " +
				" and g.discardflag = 'N' and ((a.subjcode in ('1122', '2203') and nvl(d.localcreditamount, 0) <> 0) or " +
				" ((a.subjcode in ('2202', '1123') or a.subjcode like '6602%') and nvl(d.localdebitamount, 0) <> 0)) and g.discardflag = 'N'" +
				" and g.errmessage is null and g.period <> '00' and g.pk_corp = v.pk_corp ";*/
		/*String sql="select nvl(r.ljfkjhje,0) ljfkjhje from dh_contract r where r.pk_contract='"+conid+"' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj2=iQ.executeQuery(sql, new ColumnProcessor());
		UFDouble ljfkmny=obj2==null?new UFDouble(0):new UFDouble(obj2.toString());*/
		UFDouble hxamount=pcvo.getHxamount();//ȡ�������
		
		/************************************** ���º�ͬ��������  start  *********************************************/
		DhContractVO ctvo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, conid);
		UFDouble ud1=ctvo.getLjfkjhje()==null?new UFDouble(0):ctvo.getLjfkjhje();
		UFDouble ud2=hxamount==null?new UFDouble(0):hxamount;
		ctvo.setLjfkjhje(ud1.sub(ud2,2));
		HYPubBO_Client.update(ctvo);
		
		/************************************** ���º�ͬ�����ӱ�  start  *********************************************/
		ProjectClearDVO[] dvos=(ProjectClearDVO[])aggvo.getChildrenVO();
		if(dvos!=null&&dvos.length>0){
			
			for(ProjectClearDVO dvo : dvos){
				
				String bconid=dvo.getPurcontractid();
				UFDouble dmny=dvo.getDoveramount()==null?new UFDouble(0):dvo.getDoveramount();
				if(dmny.compareTo(new UFDouble(0))==0){
					continue ;
				}
				DhContractVO ctvo2 = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, bconid);
				UFDouble ud3=ctvo2.getLjfkjhje()==null?new UFDouble(0):ctvo2.getLjfkjhje();
				ctvo2.setLjfkjhje(ud3.sub(dmny,2));
				HYPubBO_Client.update(ctvo2);
			}
			
		}
	}




	@Override
	public void onBillRef() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null == aggvo){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			return;
		}
		
		ProjectClearVO pcvo = (ProjectClearVO)aggvo.getParentVO();
		String htz = pcvo.getSalectcode().substring(0, pcvo.getSalectcode().length()-3);
		BtnPowerVO powerVO = new BtnPowerVO(htz,"false","false","true");
		DocumentManagerHT.showDM(this.getBillUI(), "XMQS", htz, powerVO);
	}
	
}
