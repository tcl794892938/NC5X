package nc.ui.dahuan.fhtz;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fhtz.DhDeliveryVO;
import nc.vo.dahuan.fhtz.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	

	@Override
	protected void onBoDelete() throws Exception {
		
		MyBillVO vo = (MyBillVO)this.getBufferData().getCurrentVO();
		if(vo==null){
			MessageDialog.showHintDlg(getBillUI(), "提示", "数据不存在！");
			return ;
		}
		
		DhDeliveryVO hvo = (DhDeliveryVO)vo.getParentVO();
		if(hvo.getIsdelivery()==1){
			MessageDialog.showHintDlg(getBillUI(), "提示", "发货单已确认，不可删除！");
			return ;
		}
		IVOPersistence ip=NCLocator.getInstance().lookup(IVOPersistence.class);
		ip.deleteByPK(DhDeliveryVO.class, hvo.getPrimaryKey());
		
		super.onBoRefresh();
		
	}



	@Override
	protected void onBoSave() throws Exception {
		
		BillData data = this.getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(data != null){
			data.dataNotNullValidate();
		}		
		
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		Object vbobj = card.getHeadItem("vbillno").getValueObject();
		String pkCon = card.getHeadItem("pk_contract").getValueObject().toString();
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if(null == vbobj || "".equals(vbobj)){
			String pkCorp = this._getCorp().getPrimaryKey();
			String strYear = this._getDate().getYear()+"";
			String strMonth = this._getDate().getStrMonth();
			String strDay = this._getDate().getStrDay();
			
			String qmsql = "select v.dept_code||to_char(sysdate,'yyyymm') qm_no from v_deptperonal v where v.pk_user='"+this._getOperator()+"'";
			
			List<Map<String,String>> qmml = (List<Map<String,String>>)iQ.executeQuery(qmsql, new MapListProcessor());
			if(null != qmml && qmml.size()>0){
				String billno = qmml.get(0).get("qm_no");

				String sql = "select max(vbillno) from dh_delivery where vbillno like '"+billno+"%'";
				
				Object billnoobj = iQ.executeQuery(sql, new ColumnProcessor());
				if(null == billnoobj || "".equals(billnoobj)){
					billno += "001";
				}else{
					String strNo = billnoobj.toString().substring(billnoobj.toString().length()-3, billnoobj.toString().length());
					String intNo = String.valueOf(Integer.parseInt(strNo)+1);
					for(int i = intNo.length();i<3;i++){
						billno += "0";
					}
					billno += intNo;
				}
				
				card.setHeadItem("vbillno", billno);
				
				UFDouble useAmt = new UFDouble("0.00");
				UFDouble sumAmt = new UFDouble("0.00");
				
				String qmtsql = "select sum(nvl(b.pduamount,0)) pduamt from dh_delivery a left join dh_delivery_d b " +
						" on a.pk_dhdelivery = b.pk_dhdelivery and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 "+
						"  where a.pk_contract = '"+pkCon+"' ";
				List<Map<String,Object>> lm = (List<Map<String,Object>>)iQ.executeQuery(qmtsql, new MapListProcessor());
				if(null != lm && lm.size() > 0){
					useAmt = new UFDouble(lm.get(0).get("pduamt")==null?"0.00":lm.get(0).get("pduamt").toString());
				}
				
				String ctsql = "select v.dctjetotal from v_dhdelivery v where v.pk_contract='"+pkCon+"'";
				List<Map<String,Object>> lv = (List<Map<String,Object>>)iQ.executeQuery(ctsql, new MapListProcessor());
				if(null != lv && lv.size() > 0){
					sumAmt = new UFDouble(lv.get(0).get("dctjetotal")==null?"0.00":lv.get(0).get("dctjetotal").toString());
				}
				
				UFDouble unAmt = sumAmt.sub(useAmt);
				
				UFDouble syAmt = new UFDouble("0.00");
				BillModel bmd = card.getBillModel();
				for(int i=0;i<bmd.getRowCount();i++){
					UFDouble mdAmt = new UFDouble(bmd.getValueAt(i, "pduamount")==null?"0.00":bmd.getValueAt(i, "pduamount").toString());
					syAmt = syAmt.add(mdAmt);
					
					// 产品具体名称赋值
					Object pduobj = bmd.getValueAt(i, "pdunewname");
					if(null == pduobj || "".equals(pduobj)){
						bmd.setValueAt(bmd.getValueAt(i, "pduname"), i, "pdunewname");
					}
					
				}
				
				if(unAmt.compareTo(syAmt)<0){
					MessageDialog.showHintDlg(this.getBillUI(), "提示", "累计发货金额已超出合同金额，不可保存");
					card.setHeadItem("vbillno", null);//清空单据号
					return;
				}
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "当前用户未分配部门，请先维护该用户的部门信息");
				return;
			}
		}else{
			
			String pkDel = card.getHeadItem("pk_dhdelivery").getValueObject().toString();
			
			UFDouble useAmt = new UFDouble("0.00");
			UFDouble sumAmt = new UFDouble("0.00");
			
			String qmtsql = "select sum(nvl(b.pduamount,0)) pduamt from dh_delivery a left join dh_delivery_d b " +
							" on a.pk_dhdelivery = b.pk_dhdelivery and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 "+
							" where a.pk_contract = '"+pkCon+"'  and a.pk_dhdelivery <> '"+pkDel+"' ";
			
			List<Map<String,Object>> lm = (List<Map<String,Object>>)iQ.executeQuery(qmtsql, new MapListProcessor());
			if(null != lm && lm.size() > 0){
				useAmt = new UFDouble(lm.get(0).get("pduamt")==null?"0.00":lm.get(0).get("pduamt").toString());
			}
			
			String ctsql = "select v.dctjetotal from v_dhdelivery v where v.pk_contract='"+pkCon+"'";
			List<Map<String,Object>> lv = (List<Map<String,Object>>)iQ.executeQuery(ctsql, new MapListProcessor());
			if(null != lv && lv.size() > 0){
				sumAmt = new UFDouble(lv.get(0).get("dctjetotal")==null?"0.00":lv.get(0).get("dctjetotal").toString());
			}
			
			UFDouble unAmt = sumAmt.sub(useAmt);
			
			UFDouble syAmt = new UFDouble("0.00");
			BillModel bmd = card.getBillModel();
			for(int i=0;i<bmd.getRowCount();i++){
				UFDouble mdAmt = new UFDouble(bmd.getValueAt(i, "pduamount")==null?"0.00":bmd.getValueAt(i, "pduamount").toString());
				syAmt = syAmt.add(mdAmt);
				// 产品具体名称赋值
				Object pduobj = bmd.getValueAt(i, "pdunewname");
				if(null == pduobj || "".equals(pduobj)){
					bmd.setValueAt(bmd.getValueAt(i, "pduname"), i, "pdunewname");
				}
			}
			
			if(unAmt.compareTo(syAmt)<0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "累计发货金额已超出合同金额，不可保存");
				return;
			}
		}
		super.onBoSave();
	}



	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
		if(intBtn == IdhButton.FILEDOWNLOAD){
			onBoFujian();
		}
		super.onBoElse(intBtn);
		
	}	


	@Override
	public void onBoAudit() throws Exception {
		UFDate data=ClientEnvironment.getInstance().getDate();
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhDeliveryVO dvo = (DhDeliveryVO)aggvo.getParentVO();
			
			if(null == dvo.getRelationid() || "".equals(dvo.getRelationid())){
				dvo.setIsdelivery(1);
				dvo.setVdef3(data.toString());
				dvo.setAuditperson(this._getOperator());
				HYPubBO_Client.update(dvo);
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "发货确认完成");
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请通知子公司确认发货");
			}
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
		}	
		super.onBoRefresh();
	}
	
	



	@Override
	protected void onBoCancelAudit() throws Exception {

		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if(null != aggvo){
			DhDeliveryVO dvo = (DhDeliveryVO)aggvo.getParentVO();
			
			//判断附件
//			判断是否有变更附件
			String rootPath=dvo.getVbillno();
			FileManageUI ui = new FileManageUI(rootPath);
			Class cla=FileManageUI.class;
			Field fields[] =cla.getDeclaredFields(); 
			Field.setAccessible(fields, true);
			for(int i=0;i<fields.length;i++){
				if(fields[i].getName().equals("rootNode")){
					NCFileNode filen=(NCFileNode)fields[i].get(ui);
					if(!filen.children().hasMoreElements()){
						MessageDialog.showHintDlg(this.getBillUI(), "提示","请先上传变更附件！");
						return ;
					}
				}
			}
			
			if(null == dvo.getRelationid() || "".equals(dvo.getRelationid())){
				dvo.setYdstatus(1);
				HYPubBO_Client.update(dvo);
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "回执确认完成");
			}else{
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "请通知子公司确认回执");
			}
		}else{
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请选择单据");
		}	
		super.onBoRefresh();
	
	}



	@Override
	protected void onBoPrint() throws Exception {
		super.onBoPrint();
		DhDeliveryVO dvo = (DhDeliveryVO)this.getBufferData().getCurrentVO().getParentVO();
		dvo.setIsprint(1);
		HYPubBO_Client.update(dvo);
		super.onBoRefresh();
	}



	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		ClientEnvironment env = ClientEnvironment.getInstance();
		String crp = env.getCorporation().getPrimaryKey();
		String usr = env.getUser().getPrimaryKey();
		
		int month = this._getDate().getMonth();
		int year = this._getDate().getYear();
		String str=this._getDate().toString().substring(8);
		String stdate = "";
		if(month>6){
			
			int stmonth = month-6;
			if(stmonth>9){
				stdate = year+"-"+stmonth+"-"+str;
			}else{
				stdate = year+"-0"+stmonth+"-"+str;
			}
		}else{
			int styear = year-1;
			int stmonth = 6+month;
			if(stmonth>9){
				stdate = styear+"-"+stmonth+"-"+str;
			}else{
				stdate = styear+"-0"+stmonth+"-"+str;
			}
		}
		
		String othcondition="";
		if(strWhere.toString().indexOf("deldate")==-1){//没有日期 默认查询6个月
			
			othcondition = strWhere.toString()+" and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
			" where v_deptperonal.pk_user = '"+usr+"' and v_deptperonal.pk_corp = '"+crp+"' " +
			" and (v_deptperonal.pk_deptdoc=dh_delivery.vdef8 or " +
			" v_deptperonal.pk_deptdoc=dh_delivery.vdef9)) and deldate>'"+stdate+"'";
		}else{
			othcondition = strWhere.toString()+" and exists (select v_deptperonal.pk_deptdoc from v_deptperonal " +
			" where v_deptperonal.pk_user = '"+usr+"' and v_deptperonal.pk_corp = '"+crp+"' " +
			" and (v_deptperonal.pk_deptdoc=dh_delivery.vdef8 or " +
			" v_deptperonal.pk_deptdoc=dh_delivery.vdef9)) ";
		}
		
		
		SuperVO[] queryVos = queryHeadVOs(othcondition);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		
		if(((ClientUI)getBillUI()).isListPanelSelected()){
			//int column=((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().getBodyColByKey("vbillno");
			((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().sortByColumn("vbillno", true);
		}
		
		
	}	
	
	protected void onBoFujian()throws Exception{
		
		DhDeliveryVO vo=(DhDeliveryVO)getBufferData().getCurrentVO().getParentVO();
		if(vo==null){
			MessageDialog.showHintDlg(getBillUI(), "提示", "数据异常！请选择单据！");
			return ;
		}
		
		DocumentManagerHT.showDM(this.getBillUI(), "1234", vo.getVbillno());
	}
	
	
}