package nc.ui.dahuan.fhtz;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bfriend.button.FileUpLoadBtnVO;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.ctbill.DhContractBVO;
import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fhtz.DhDeliveryDVO;
import nc.vo.dahuan.fhtz.DhDeliveryVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	
		ButtonVO editvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Edit).getData();
		editvo.setExtendStatus(new int[]{ 3 });
		
		ButtonVO auditvo = (ButtonVO)this.getButtonManager().getButton(IBillButton.Audit).getData();
		auditvo.setExtendStatus(new int[]{ 3 });
	}

	public void setDefaultData() throws Exception {
		
//		UIRefPane fmuif = (UIRefPane)this.getBillCardPanel().getHeadItem("fromaddress").getComponent();
//		fmuif.getRefModel().addWherePart(" and bd_psnbasdoc.pk_corp='"+this._getCorp().getPrimaryKey()+"' ", true);
//		
//		UIRefPane apuif = (UIRefPane)this.getBillCardPanel().getHeadItem("approveid").getComponent();
//		apuif.getRefModel().addWherePart(" and bd_psnbasdoc.pk_corp='"+this._getCorp().getPrimaryKey()+"' ", true);
		
		UIRefPane conuif = (UIRefPane)this.getBillCardPanel().getHeadItem("pk_contract").getComponent();
		String condition = " and temq_contract.httype = 1 and exists (select 1 from dh_contract cn where exists " +
						" ( select 1 from v_deptperonal vd where vd.pk_corp='"+this._getCorp().getPrimaryKey()+"'" +
						" and vd.pk_user='"+this._getOperator()+"' and (vd.pk_deptdoc = cn.pk_deptdoc or vd.pk_deptdoc = cn.ht_dept )) and cn.pk_contract = temq_contract.pk_contract ) ";
		conuif.getRefModel().addWherePart(condition, true);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);		
		BillCardPanel card = this.getBillCardPanel();
		if("pk_contract".equals(e.getKey())){
			
			card.setHeadItem("xmcode", null);
			card.setHeadItem("htcode", null);
			card.setHeadItem("htname", null);
			card.setHeadItem("toaddress", null);
			card.setHeadItem("delunit", null);
			card.setHeadItem("relationid", null);
			
			UIRefPane uif = (UIRefPane)e.getSource();
			String pkcon = uif.getRefPK();
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			try{
				DhContractVO dcvo = (DhContractVO)iQ.retrieveByPK(DhContractVO.class, pkcon);
				card.setHeadItem("xmcode", dcvo.getJobcode());
				card.setHeadItem("htcode", dcvo.getCtcode());
				card.setHeadItem("htname", dcvo.getCtname());
				
				card.setHeadItem("relationid", dcvo.getRelationid());
				
				// 制单部门和归属部门
				card.setHeadItem("vdef8", dcvo.getPk_deptdoc());
				card.setHeadItem("vdef9", dcvo.getHt_dept());
				
				// 发货单位
				UIRefPane fhdwuif = (UIRefPane)card.getHeadItem("toaddress").getComponent();
				fhdwuif.setPK(dcvo.getPk_cust2());
				
				if(dcvo.getCtcode().indexOf("-")>0){
					String xsctcode = dcvo.getCtcode().substring(0, dcvo.getCtcode().indexOf("-")+1)+"00";
					IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
					
					List<DhContractVO> dhclit = (List<DhContractVO>)query.retrieveByClause(DhContractVO.class, 
																							" ctcode = '"+xsctcode+"' and nvl(dr,0)=0 ");
					
					if(null != dhclit && dhclit.size() == 1){
						DhContractVO cvo = dhclit.get(0);
						UIRefPane shdwref = (UIRefPane)card.getHeadItem("delunit").getComponent();
						shdwref.setPK(cvo.getPk_cust1());
					}	
				}
				
				BillModel bmodel = card.getBillModel();
				
				bmodel.clearBodyData();
				
				// 表体数据带出
				List<DhContractBVO> blists = (List<DhContractBVO>)iQ.retrieveByClause(DhContractBVO.class," pk_contract = '"+pkcon+"' ");
				if(null != blists && blists.size()>0){
					
					DhDeliveryDVO[] ddvos = new DhDeliveryDVO[blists.size()];
					
					for(int j=0;j<blists.size();j++){
						DhContractBVO bvo = blists.get(j);
						DhDeliveryDVO dlvo = new DhDeliveryDVO();
						dlvo.setPk_product(bvo.getPk_invbasdoc());
						dlvo.setPduname(bvo.getInvname());
						dlvo.setPdunewname(bvo.getInvname());
						dlvo.setStylemodel(bvo.getVggxh());
						dlvo.setPdunum(bvo.getNnumber());
						dlvo.setPduamount(bvo.getDjetotal());
						
						ddvos[j]=dlvo;
					}
					
					bmodel.setBodyDataVO(ddvos);
				}
				
				
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		
		if("vdef2".equals(e.getKey())){//by tcl
			//Object vdefobj = e.getValue();
			//card.setHeadItem("vdef1", vdefobj);
		}
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			DhDeliveryVO dvo = (DhDeliveryVO)vo.getParentVO();
			int isdel = dvo.getIsdelivery()==null ?0:dvo.getIsdelivery();
			if(isdel == 1){
				return 2;
			}else{
				return 3;
			}
		}
		return 1;
	}

	@Override
	protected void initPrivateButton() {
		
		ButtonVO btn=new FileUpLoadBtnVO().getButtonVO4();
		btn.setBtnName("运单回执附件");
		addPrivateButton(btn);
		super.initPrivateButton();
	}
	
	
	
}
