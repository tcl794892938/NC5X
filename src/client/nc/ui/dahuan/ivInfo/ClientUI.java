package nc.ui.dahuan.ivInfo;

import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.dahuan.ivInfo.PdutVO;
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
		
		ButtonManager btm = this.getButtonManager();
		
		// 修改
		((ButtonVO)btm.getButton(IBillButton.Edit).getData()).setExtendStatus(new int[]{2,7});
		// 提交
		((ButtonVO)btm.getButton(IBillButton.Commit).getData()).setExtendStatus(new int[]{2,7});
		// 撤销
		((ButtonVO)btm.getButton(IBillButton.CancelAudit).getData()).setExtendStatus(new int[]{6});
		// 工程部审批
		((ButtonVO)btm.getButton(IBillButton.Audit).getData()).setExtendStatus(new int[]{6});
		// 工程部驳回
		((ButtonVO)btm.getButton(IdhButton.RET_COMMIT).getData()).setExtendStatus(new int[]{6});
		// 不同意
		((ButtonVO)btm.getButton(IdhButton.NOAGREE).getData()).setExtendStatus(new int[]{3});
		// 同意
		((ButtonVO)btm.getButton(IdhButton.AGREE).getData()).setExtendStatus(new int[]{3});
		// 财务确认
		((ButtonVO)btm.getButton(IdhButton.CWQR).getData()).setExtendStatus(new int[]{4});
		
	}

	public void setDefaultData() throws Exception {
		UIRefPane ivuif = (UIRefPane)this.getBillCardPanel().getHeadItem("pkivmandoc").getComponent();
		String conf = " and bd_invbasdoc.pk_invcl <> '0001AA10000000000010' and exists (select 1 from bd_invmandoc  " +
					"  where nvl(bd_invmandoc.sealflag,'N')='N' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc) ";
		ivuif.getRefModel().addWherePart(conf);
		
	}

	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			PdutVO pvo = (PdutVO)vo.getParentVO();
			int flag = pvo.getDhpdu_flag();
			if(1==flag){
				// 已提交
				return 6;
			}else if(2==flag){
				// 被驳回
				return 2;
			}else if(3==flag){
				// 工程部审核通过
				return 3;
			}else if(4==flag){
				// 副总同意
				return 4;
			}else if(5==flag){
				// 财务确认
				return 5;
			}else{
				return 7;
			}
		}
		return 1;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if("pkivmandoc".equals(e.getKey())){
			try{
				BillCardPanel card = this.getBillCardPanel();
				UIRefPane ivuif = (UIRefPane)card.getHeadItem("pkivmandoc").getComponent();
				InvbasdocVO inb = (InvbasdocVO)HYPubBO_Client.queryByPrimaryKey(InvbasdocVO.class, ivuif.getRefPK());
				card.setHeadItem("pdu_type", inb.getPk_invcl());
				card.setHeadItem("pdu_no", inb.getInvcode());
				card.setHeadItem("pdu_name", inb.getInvname());
				card.setHeadItem("pk_dnaw", inb.getPk_measdoc());
				
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}

	
	
}
