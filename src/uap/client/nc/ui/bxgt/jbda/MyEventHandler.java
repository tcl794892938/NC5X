package nc.ui.bxgt.jbda;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.bxgt.djtc.IbxgtQuerySynchro;
import nc.ui.bxgt.button.IBxgtButton;
import nc.ui.bxgt.pub.ExcelUtils;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class MyEventHandler extends CardEventHandler {

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == IBxgtButton.BASE_SYN) {
			this.onBoSynBase();
		}
		else if(intBtn==IBxgtButton.CUST_DOWN){
			this.onDownExcel();
		}
		else if(intBtn == IBxgtButton.CUST_SYN){
			this.onBoSynCust();
		}
		else if(intBtn == IBxgtButton.AUTO_SYN){
			this.onAutoSyn();
		}
	}

	public void onBoSynBase() throws Exception {
		
		IbxgtQuerySynchro ibqs=NCLocator.getInstance().lookup(IbxgtQuerySynchro.class);
		
		LinkedHashMap<String, Object> map=ibqs.synBaseDoc();
		
		BillCardPanel card=this.getBillCardPanelWrapper().getBillCardPanel();
		BillModel bmodel=card.getBillModel();
		bmodel.clearBodyData();//������
		
		int i=0;
		for(String key:map.keySet()){
			bmodel.addLine();
			Object obj=map.get(key);
			bmodel.setValueAt(key, i, "pk_base");
			bmodel.setValueAt(obj, i, "count");
			i++;
		}
		
		MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "ͬ����ɣ�");
		return ;

	}
	
	public void onDownExcel()throws Exception{
		
		ExcelUtils.doExportExcel(this.getBillUI(),"���ۿ⻧ģ��");
	}
	
	public void onBoSynCust()throws Exception{
		
		Object[][] exlimp =ExcelUtils.doImport(this.getBillUI());
		
		if(null==exlimp){
			return ;
		}
		//У������Ƿ�Ϊ��,�ӵ�n�п�ʼУ��
		int j=0;
		for(int i=0;i<exlimp.length;i++){
			Object[] obj=exlimp[i];
			if(obj[1]!=null&&obj[1].equals("���̱���")){
				j=i;
				break;
			}
		}
		String str="";
		for(int i=j+1;i<exlimp.length;i++){
			Object[] obj=exlimp[i];
			if(obj[1]==null||"".equals(obj[1].toString().trim())){
				str+="��"+(i+1)+"��";
			}
		}
		if(!"".equals(str)){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "��"+str+"�пͻ����벻��Ϊ�գ�");
			return ;
		}
		
		//�����ݿ����
		ArrayList<String> list=new ArrayList<String>();
		for(int i=3;i<exlimp.length;i++){
			Object[] obj=exlimp[i];
			String code=getStgObj(obj[1]);
			list.add(code);
		}
		
		String billtime=ClientEnvironment.getServerTime().toString();
		IbxgtQuerySynchro ibqs=NCLocator.getInstance().lookup(IbxgtQuerySynchro.class);
		if(list.size()>0){
			Integer it=ibqs.synCustBasedoc(list.toArray(new String[0]), billtime);
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ͬ��"+it+"��������");
			return ;
		}
		else{
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "û�з���Ҫͬ���Ŀͻ���");
			return ;
		}
		
	}
	
	public void onAutoSyn()throws Exception{
		
		String billtime=ClientEnvironment.getServerTime().toString();
		IbxgtQuerySynchro ibqs=NCLocator.getInstance().lookup(IbxgtQuerySynchro.class);
		Integer it=ibqs.synCustBasedoc(null, billtime);
		if(it==0){
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "δ������Ҫͬ���Ŀ�Ʊ���̣�");
			return ;
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "����ͬ��"+it+"��������");
			return ;
		}
	} 
	
	//�ַ�����װ
	public String getStgObj(Object obj){
		return obj==null?"":obj.toString();
	}

}
