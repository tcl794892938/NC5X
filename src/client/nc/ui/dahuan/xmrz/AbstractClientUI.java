package nc.ui.dahuan.xmrz;

import nc.ui.bfriend.button.CgjlBtnVO;
import nc.ui.bfriend.button.ConReadBtnVO;
import nc.ui.bfriend.button.JhztBtnVO;
import nc.ui.bfriend.button.KsztBtnVO;
import nc.ui.bfriend.button.QtztBtnVO;
import nc.ui.bfriend.button.SjjlBtnVo;
import nc.ui.bfriend.button.TjjlBtnVO;
import nc.ui.bfriend.button.TsztBtnVO;
import nc.ui.bfriend.button.WgztBtnVO;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;




/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
  public abstract class AbstractClientUI extends nc.ui.trade.manage.BillManageUI implements  ILinkQuery{

	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dahuan.fkjhcw.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if( listButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}		
		if( hasCommit ){
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}
		
		if( hasAudit ){
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
				.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}
		
		if( hasCancelAudit ){
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);	
		}
		
		SjjlBtnVo btnvo= new SjjlBtnVo();
	    addPrivateButton(btnvo.getButtonVO());
	    
		JhztBtnVO btnv1= new JhztBtnVO();
	    addPrivateButton(btnv1.getButtonVO());
	    
	    TjjlBtnVO btnv2= new TjjlBtnVO();
	    addPrivateButton(btnv2.getButtonVO());
	    
	    KsztBtnVO btnvo3= new KsztBtnVO();
	    addPrivateButton(btnvo3.getButtonVO());
	    
	    TsztBtnVO btnvo4= new TsztBtnVO();
	    addPrivateButton(btnvo4.getButtonVO());
	    
	    WgztBtnVO btnvo5= new WgztBtnVO();
	    addPrivateButton(btnvo5.getButtonVO());
	    
	    CgjlBtnVO btnv6= new CgjlBtnVO();
	    addPrivateButton(btnv6.getButtonVO());
	    
	    QtztBtnVO btnvo7= new QtztBtnVO();
	    addPrivateButton(btnvo7.getButtonVO());
	    
	    ConReadBtnVO btnvo8=new ConReadBtnVO();
	    addPrivateButton(btnvo8.getButtonVO());

	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}