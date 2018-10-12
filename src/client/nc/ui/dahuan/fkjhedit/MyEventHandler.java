package nc.ui.dahuan.fkjhedit;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dahuan.fkedit.FkjhEditVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.fkjh.DhFkjhprintVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintDVO;
import nc.vo.dahuan.fkprintquery.DhFkbdprintVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class MyEventHandler extends CardEventHandler {


	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {
		
		BillCardPanel card=this.getBillCardPanelWrapper().getBillCardPanel();
		Object obj=card.getHeadItem("ctcode").getValueObject();
		Object obj2=card.getHeadItem("mny").getValueObject();
		Object obj3=card.getHeadItem("print_no").getValueObject();
		Object obj4=card.getHeadItem("oldmny").getValueObject();
		if(obj==null||"".equals(obj)||obj2==null||"".equals(obj2)||obj3==null||"".equals(obj3)){
			MessageDialog.showHintDlg(getBillUI(), "提示", "录入的合同号,打印号和金额不可为空！");
			return ;
		}
		String code=obj.toString();//合同号
		String print_no=obj3.toString();//打印号
		UFDouble mny=new UFDouble(obj2.toString());
		UFDouble oldmny=new UFDouble(obj4.toString());
		//校验合同是否存在
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql="select count(1) from dh_fkjhbill t where nvl(t.dr,0)=0 and nvl(t.sealflag,0)=0 " +
				" and t.ctcode='"+code+"' and t.print_no='"+print_no+"' ";
		Integer it=(Integer)iQ.executeQuery(sql, new ColumnProcessor());
		if(it<1){
			MessageDialog.showHintDlg(getBillUI(), "提示", "该合同编号和打印号对应的付款计划单！");
			return ;
		}
		if(it>1){
			MessageDialog.showHintDlg(getBillUI(), "提示", "该合同编号与打印号对应的付款计划单出现多条记录，请核查！");
			return ;
		}
		
		String sql1="select * from dh_fkjhbill t where nvl(t.dr,0)=0 and nvl(t.sealflag,0)=0 " +
				" and t.ctcode='"+code+"' and t.print_no='"+print_no+"'";
		DhFkjhbillVO vo=(DhFkjhbillVO)iQ.executeQuery(sql1, new BeanProcessor(DhFkjhbillVO.class));
		vo.setCurrenty_amount(mny);
		vo.setDfkje(mny);
		vo.setDfkbl(mny.div(vo.getDctjetotal()));
		HYPubBO_Client.update(vo);
		
		String sql2="select * from dh_fkjhprint t where t.print_no='"+print_no+"' ";
		DhFkjhprintVO dvo=(DhFkjhprintVO)iQ.executeQuery(sql2,new BeanProcessor(DhFkjhprintVO.class));
		if(dvo!=null){
			//校验逻辑htamount
			for(int i=1;i<11;i++){
				Object htcode=dvo.getAttributeValue("htcode"+i);
				if(htcode!=null&&htcode.equals(code)){
					dvo.setAttributeValue("htamount"+i, mny);
					dvo.setAmount_sum(dvo.getAmount_sum().add(mny).sub(oldmny));
					HYPubBO_Client.update(dvo);
					break; 
				}
			}
		}
		
		String sql3="select * from dh_fkbdprint t where t.print_no='"+print_no+"' ";
		DhFkbdprintVO hvo=(DhFkbdprintVO)iQ.executeQuery(sql3,new BeanProcessor(DhFkbdprintVO.class));
		String pk="";
		if(hvo!=null){
			pk=hvo.getPk_fkbd();
			//校验逻辑htamount
			for(int i=1;i<11;i++){
				Object htcode=hvo.getAttributeValue("htcode"+i);
				if(htcode!=null&&htcode.equals(code)){
					hvo.setAttributeValue("htamount"+i, mny);
					hvo.setAmount_sum(hvo.getAmount_sum().add(mny).sub(oldmny));
					HYPubBO_Client.update(hvo);
					break; 
				}
			}
		}
		
		if(!"".equals(pk)){
			String sql4="select * from dh_fkbdprint_d r where r.pk_fkbd='"+pk+"' and r.htcode='"+code+"'";
			DhFkbdprintDVO fvo=(DhFkbdprintDVO)iQ.executeQuery(sql4,new BeanProcessor(DhFkbdprintDVO.class));
			if(fvo!=null){
				fvo.setHtamount(mny);
				HYPubBO_Client.update(fvo);
			}
		}
		
		//插入记录表
		FkjhEditVO fvo=new FkjhEditVO();
		fvo.setCtcode(code);
		fvo.setPrint_no(print_no);
		fvo.setDbilldate(new UFDate(card.getHeadItem("dbilldate").getValueObject().toString()));
		fvo.setMny(mny);
		fvo.setOldmny(oldmny);
		fvo.setPk_customer(card.getHeadItem("pk_customer").getValueObject().toString());
		HYPubBO_Client.insert(fvo);
		
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	}

	public void onBoAdd(ButtonObject bo) throws Exception {
		
		super.onBoAdd(bo);
	}

	@Override
	protected void onBoQuery() throws Exception {

		String sql="select * from dh_fkjhedit ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<FkjhEditVO> list=(List<FkjhEditVO>)iQ.executeQuery(sql, new BeanListProcessor(FkjhEditVO.class));
		if(list==null||list.size()<=0){
			MessageDialog.showHintDlg(getBillUI(), "提示", "无数据！");
			return ;
		}
		FkjhEditVO[] fvos=list.toArray(new FkjhEditVO[0]);
		EditDlg dlg=new EditDlg(getBillUI());
		dlg.showEditInfo(fvos);
	}

	

}