package nc.ui.dahuan.jcInfo.invInfo;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.jcInfo.invInfo.BgInvEntity;
import nc.vo.pub.AggregatedValueObject;

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
	
	@Override
	protected void onBoSave() throws Exception {
		// �ǿ�У��
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		BillData data = card.getBillData();
		if(data != null)
			data.dataNotNullValidate();
		
		// Ψһ��У��
		UIRefPane invuif = (UIRefPane)card.getHeadItem("pk_invbasdoc").getComponent();
		String pkoginv = invuif.getRefPK();
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		if(null == pkoginv || "".equals(pkoginv)){			
			// �±�š�������
			String invno = (String)card.getHeadItem("inv_no").getValueObject();
			String invname = (String)card.getHeadItem("inv_name").getValueObject();
			
			String nosql = "select count(1) from bd_invbasdoc t where t.invcode='"+invno+"'";
			String namesql = "select count(1) from bd_invbasdoc t where t.invname='"+invname+"'";
			
			int nocount = (Integer)iQ.executeQuery(nosql, new ColumnProcessor());
			int namecount = (Integer)iQ.executeQuery(namesql, new ColumnProcessor());
			
			if(nocount > 0){
				if(namecount > 0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[������][�������]�Ѵ���");
					return;
				}else{
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[������]�Ѵ���");
					return;
				}
			}else{
				if(namecount > 0){
					MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[�������]�Ѵ���");
					return;
				}
			}
		}else{
			// �±�š�������
			String invno = (String)card.getHeadItem("inv_no").getValueObject();
			String invname = (String)card.getHeadItem("inv_name").getValueObject();
			
			// ԭ��š�ԭ����
			String oginvno = (String)card.getHeadItem("og_invno").getValueObject();
			String oginvname = (String)card.getHeadItem("og_invname").getValueObject();
			
			if(invno.equals(oginvno)){
				if(invname.equals(oginvname)){
					
				}else{
					String namesql = "select count(1) from bd_invbasdoc t where t.invname='"+invname+"'";
					int namecount = (Integer)iQ.executeQuery(namesql, new ColumnProcessor());
					if(namecount > 0){
						MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[�������]�Ѵ���");
						return;
					}
				}
			}else{
				if(invname.equals(oginvname)){
					String nosql = "select count(1) from bd_invbasdoc t where t.invcode='"+invno+"'";
					int nocount = (Integer)iQ.executeQuery(nosql, new ColumnProcessor());
					if(nocount > 0){
						MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[������]�Ѵ���");
						return;
					}
				}else{
					String nosql = "select count(1) from bd_invbasdoc t where t.invcode='"+invno+"'";
					String namesql = "select count(1) from bd_invbasdoc t where t.invname='"+invname+"'";
					
					int nocount = (Integer)iQ.executeQuery(nosql, new ColumnProcessor());
					int namecount = (Integer)iQ.executeQuery(namesql, new ColumnProcessor());
					
					if(nocount > 0){
						if(namecount > 0){
							MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[������][�������]�Ѵ���");
							return;
						}else{
							MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[������]�Ѵ���");
							return;
						}
					}else{
						if(namecount > 0){
							MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "[�������]�Ѵ���");
							return;
						}
					}
				}
			}
		}
		
		// ����
		super.onBoSave();
	}

	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			BgInvEntity bginv = (BgInvEntity)aggvo.getParentVO();
			if(null != bginv){
				bginv.setBg_status(1);
				HYPubBO_Client.update(bginv);
				super.onBoRefresh();
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
			}
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��ѡ�񵥾�");
		}
	}

		
	
}