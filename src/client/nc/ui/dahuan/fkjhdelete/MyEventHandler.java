package nc.ui.dahuan.fkjhdelete;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkjh.DhFkjhprintVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintDVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends CardEventHandler {


	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	public void onBoAdd(ButtonObject bo) throws Exception {
		
		super.onBoAdd(bo);
		
		this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
		getBillUI().updateButtons();
		
	}

	@Override
	protected void onBoDelete() throws Exception {
		
		BillCardPanel card=this.getBillCardPanelWrapper().getBillCardPanel();
		Object obj=card.getHeadItem("ctcode").getValueObject();
		
		if(obj==null||"".equals(obj)){
			
			MessageDialog.showHintDlg(getBillUI(), "提示", "录入的合同号不可为空！");
			return ;
			
		}
		
		String code=obj.toString();
		
		//校验合同是否存在
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql="select count(1) from dh_fkjhbill t where nvl(t.dr,0)=0 and nvl(t.sealflag,0)=0 " +
				" and t.ctcode='"+code+"' ";
		Integer it=(Integer)iQ.executeQuery(sql, new ColumnProcessor());
		if(it<1){
			MessageDialog.showHintDlg(getBillUI(), "提示", "该合同编号对应的付款计划单不存在！");
			return ;
		}
		if(it>1){
			MessageDialog.showHintDlg(getBillUI(), "提示", "该合同编号对应的付款计划单出现多条记录，请核查！");
			return ;
		}
		
		//获取打印号
		String sqlno="select print_no from dh_fkjhbill t where nvl(t.dr,0)=0 and nvl(t.sealflag,0)=0 and t.ctcode='"+code+"' ";
		Object objno=iQ.executeQuery(sqlno, new ColumnProcessor());//打印号
		
		//先删除当前付款单数据
		IVOPersistence ivp=NCLocator.getInstance().lookup(IVOPersistence.class);
		ivp.deleteByClause(DhFkjhbillVO.class, " nvl(dr,0)=0 and nvl(sealflag,0)=0 and ctcode='"+code+"' ");
		
		if(objno==null||"".equals(objno)){
			return ;
		}
		String print_no=objno.toString();//打印号
		
		String sql2="select * from dh_fkjhprint t where t.print_no='"+print_no+"' ";
		DhFkjhprintVO dvo=(DhFkjhprintVO)iQ.executeQuery(sql2,new BeanProcessor(DhFkjhprintVO.class));
		if(dvo!=null){
			//校验逻辑htamount
			for(int i=1;i<11;i++){
				Object htcode=dvo.getAttributeValue("htcode"+i);
				if(htcode!=null&&htcode.equals(code)){
					UFDouble objmny = dvo.getAttributeValue("htamount"+i)==null?new UFDouble(0):new UFDouble(dvo.getAttributeValue("htamount"+i).toString());
					dvo.setAttributeValue("htamount"+i, null);
					dvo.setAttributeValue("htcode"+i, null);
					dvo.setAttributeValue("say_content"+i, null);
					dvo.setAttributeValue("say_type"+i, null);
					dvo.setAmount_sum(dvo.getAmount_sum().sub(objmny));
					if(dvo.getAmount_sum().compareTo(new UFDouble(0))==0){
						HYPubBO_Client.delete(dvo);
					}else{
						HYPubBO_Client.update(dvo);
					}
					break; 
				}
			}
		}
		
		String sql3="select * from dh_fkbdprint t where t.print_no='"+print_no+"' ";
		DhFkbdprintVO hvo=(DhFkbdprintVO)iQ.executeQuery(sql3,new BeanProcessor(DhFkbdprintVO.class));
		String pk="***";
		if(hvo!=null){
			pk=hvo.getPk_fkbd();
			//校验逻辑htamount
			for(int i=1;i<11;i++){
				Object htcode=hvo.getAttributeValue("htcode"+i);
				if(htcode!=null&&htcode.equals(code)){
					UFDouble objmny = hvo.getAttributeValue("htamount"+i)==null?new UFDouble(0):new UFDouble(hvo.getAttributeValue("htamount"+i).toString());
					hvo.setAttributeValue("htamount"+i, null);
					hvo.setAttributeValue("htcode"+i, null);
					hvo.setAttributeValue("say_content"+i, null);
					hvo.setAttributeValue("say_type"+i, null);
					hvo.setAmount_sum(hvo.getAmount_sum().sub(objmny));
					if(hvo.getAmount_sum().compareTo(new UFDouble(0))==0){
						HYPubBO_Client.delete(hvo);
					}else{
						HYPubBO_Client.update(hvo);
					}
					break; 
				}
			}
		}
		
		//删除子表明细
		ivp.deleteByClause(DhFkbdprintDVO.class, " nvl(dr,0)=0 and pk_fkbd='"+pk+"' and htcode='"+code+"' ");
		MessageDialog.showHintDlg(getBillUI(), "提示", "删除成功！");
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		getBillUI().updateButtons();
	}
	

}