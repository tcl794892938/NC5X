package nc.ui.dahuan.htinfo.htchange;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.dahuan.cttreebill.DhContractBVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
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
		// 行操作按钮更新
		ButtonObject lineBtn = this.getButtonManager().getButton(IBillButton.Line);
		ButtonObject lineAddBtn = this.getButtonManager().getButton(IBillButton.AddLine);
		ButtonObject lineDelBtn = this.getButtonManager().getButton(IBillButton.DelLine);
		lineBtn.removeAllChildren();
		lineBtn.addChildButton(lineAddBtn);
		lineBtn.addChildButton(lineDelBtn);
		
		ButtonObject editObj = this.getButtonManager().getButton(IBillButton.Edit);
		ButtonVO editVO = (ButtonVO)editObj.getData();
		editVO.setExtendStatus(new int[]{4});
		
		ButtonObject deleteObj = this.getButtonManager().getButton(IBillButton.Delete);
		ButtonVO deleteVO = (ButtonVO)deleteObj.getData();
		deleteVO.setExtendStatus(new int[]{4});
		
		ButtonObject auditObj = this.getButtonManager().getButton(IBillButton.Audit);
		ButtonVO auditVO = (ButtonVO)auditObj.getData();
		auditVO.setExtendStatus(new int[]{4});
		
		ButtonObject cancelObj = this.getButtonManager().getButton(IBillButton.CancelAudit);
		ButtonVO cancelVO = (ButtonVO)cancelObj.getData();
		cancelVO.setExtendStatus(new int[]{2});
	}

	public void setDefaultData() throws Exception {
		// 过滤合同
		UIRefPane refp = (UIRefPane)this.getBillCardPanel().getHeadItem("pk_contract").getComponent();
		refp.getRefModel().addWherePart(" and vapproveid = '"+this._getOperator()+"' ");
	}

	
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		if(null != vo){
			HtChangeEntity htc = (HtChangeEntity)vo.getParentVO();
			if(null != htc){
				int status = htc.getHtstatus();
				if(1 == status){
					return 2;
				}else if( 2 == status ){
					return 3;
				}else{
					return 4;
				}
			}
			return 1;
		}
		return 1;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);//先把选择的合同编号塞到面板中
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		//比较是否获得的是 pk_contract
		if("pk_contract".equals(e.getKey())){
			UIRefPane refp = (UIRefPane)this.getBillCardPanel().getHeadItem("pk_contract").getComponent();
			//获得 合同的主键值
			String pkcon = refp.getRefPK();
			
			try {
				// 校验合同是否存在未变更的单子
				String chsql = "select count(1) from dh_conchange t where t.pk_contract = '"+pkcon+"' and nvl(t.htstatus,0)<>2 ";
				
				Integer chret = (Integer)iQ.executeQuery(chsql, new ColumnProcessor());
				if(chret.intValue() != 0){
					MessageDialog.showHintDlg(this, "提示", "该合同存在未审核确认的变更单，不可再新增此变更单");
					BillItem[] items = this.getBillCardPanel().getHeadItems();
					for(BillItem item : items){
						if(!"htstatus".equals(item.getKey())){
							this.getBillCardPanel().setHeadItem(item.getKey(), null);
						}
					}
					this.getBillCardPanel().getBillModel().clearBodyData();
					return;
				}
				
				// 合同主表信息
				DhContractVO vo = (DhContractVO)HYPubBO_Client.queryByPrimaryKey(DhContractVO.class, pkcon);
				
				String htcode = vo.getCtcode();
				
				String ctname = vo.getCtname();
				String invsql = "select t.invname from bd_invbasdoc t where t.pk_invbasdoc='"+ctname+"'";
				String htpduname = (String)iQ.executeQuery(invsql, new ColumnProcessor());
				
				String ctpe = vo.getPk_cttype();
				String ctsql = "select docname from bd_defdoc where pk_defdoc ='"+ctpe+"'";
				String htctpe = (String)iQ.executeQuery(ctsql, new ColumnProcessor());
				
				String htprojname = vo.getVdef6();
				
				Integer type = vo.getHttype();
				String httype = "";
				String pkcust = "";
				if(type.intValue() == 0){
					httype = "销售合同";
					pkcust = vo.getPk_cust1();
				}else if(type.intValue() == 1){
					httype = "采购合同";
					pkcust = vo.getPk_cust2();
				}else{
				}
				String custsql = "select c.custname from bd_cubasdoc c where c.pk_cubasdoc = " +
									" (select d.pk_cubasdoc from bd_cumandoc d where d.pk_cumandoc = '"+pkcust+"')";
				String htcust = (String)iQ.executeQuery(custsql, new ColumnProcessor());
				
				String htbank = vo.getPk_bank();
				String htsaxno  = vo.getSax_no();
				UFDouble htamount = vo.getDctjetotal();
				
				String jsfs = vo.getPk_skfs();
				String jssql = "select balanname from bd_balatype where pk_balatype = '"+jsfs+"'";
				String htstyle = (String)iQ.executeQuery(jssql, new ColumnProcessor());
				
				String htcontractor = vo.getPk_fzr();
				String htmanager = vo.getPk_xmjl();
				String dept_manager = vo.getVapproveid();
				String corp_manager = vo.getPk_fuzong();
				UFDouble ys = vo.getXm_amount();//项目预算
				
				BillCardPanel card = this.getBillCardPanel();
				card.setHeadItem("htcode", htcode);
				card.setHeadItem("htpduname", htpduname);
				card.setHeadItem("htctpe",htctpe );
				card.setHeadItem("htprojname",htprojname );
				card.setHeadItem("httype", httype);
				card.setHeadItem("htcust", htcust);
				card.setHeadItem("htbank", htbank);
				card.setHeadItem("htsaxno", htsaxno);
				card.setHeadItem("htamount",htamount );
				card.setHeadItem("htstyle",htstyle );
				card.setHeadItem("htcontractor", htcontractor);
				card.setHeadItem("htmanager",htmanager );
				card.setHeadItem("xmaccount", ys);
				card.setTailItem("corp_manager", corp_manager);
				
				// 合同子表信息
				DhContractBVO[] dcvos = (DhContractBVO[])HYPubBO_Client.queryByCondition(DhContractBVO.class, " pk_contract = '"+pkcon+"'");
				HtChangeDtlEntity[] htcvos = new HtChangeDtlEntity[dcvos.length];
				BillModel bd = card.getBillModel();
				bd.clearBodyData();
				for(int i=0;i<dcvos.length;i++){
					DhContractBVO bvo = dcvos[i];
					bd.addLine();
					
					DefaultConstEnum pkpdu = new DefaultConstEnum(bvo.getPk_invbasdoc(),bvo.getInvcode());
					
					bd.setValueAt(pkpdu, i, "pk_pdu");
					
					//往表体对应字段塞值
//					bd.setValueAt(aValue, row, strKey),
					
					card.setCellEditable(i, "pk_pdu", false);
					
					bd.setValueAt(bvo.getInvcode(), i, "pdu_no");
					card.setCellEditable(i, "pdu_no", false);
					
					bd.setValueAt(bvo.getInvname(), i, "pdu_name");
					card.setCellEditable(i, "pdu_name", false);
					
					bd.setValueAt(bvo.getVggxh(), i, "pdu_stylemodel");
					card.setCellEditable(i, "pdu_stylemodel", false);
					
					bd.setValueAt(bvo.getPk_danw(), i, "meadoc_name");
					card.setCellEditable(i, "meadoc_name", false);
					
					bd.setValueAt(bvo.getNnumber(), i, "pdu_num");
					card.setCellEditable(i, "pdu_num", false);
					
					bd.setValueAt(bvo.getDjprice(), i, "pdu_piece");
					card.setCellEditable(i, "pdu_piece", false);
					
					bd.setValueAt(bvo.getDjetotal(), i, "pdu_amount");
					card.setCellEditable(i, "pdu_amount", false);
					
					bd.setValueAt(bvo.getDghsj(), i, "delivery_date");
					card.setCellEditable(i, "delivery_date", false);
					
					bd.setValueAt(bvo.getVmen(), i, "vemo");
					card.setCellEditable(i, "vemo", false);
					
				}
				
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		
		if("pk_pdu".equals(e.getKey())){
			try{
				DefaultConstEnum pduenct = (DefaultConstEnum)e.getValue();
				String rowsql = "select b.invcode,b.invname,(select m.measname from bd_measdoc m where m.pk_measdoc = b.pk_measdoc) meaname," +
							" b.invspec,b.invtype from bd_invbasdoc b where b.pk_invbasdoc = '"+pduenct.getValue()+"'";
				List<Map<String,String>> mplit = (List<Map<String,String>>)iQ.executeQuery(rowsql, new MapListProcessor());
				if(null != mplit && mplit.size() > 0){
					Map<String,String> mp = mplit.get(0);
					BillModel bd = this.getBillCardPanel().getBillModel();
					bd.setValueAt(getValueNull(mp.get("invcode")), e.getRow(), "pdu_no");
					bd.setValueAt(getValueNull(mp.get("invname")), e.getRow(), "pdu_name");
					bd.setValueAt(getValueNull(mp.get("invspec"))+getValueNull(mp.get("invtype")), e.getRow(), "pdu_stylemodel");
					bd.setValueAt(getValueNull(mp.get("meaname")), e.getRow(), "meadoc_name");
				}
			}catch(Exception e2){
				e2.printStackTrace();
			}			
		}
		
		if("pdu_num".equals(e.getKey()) || "pdu_piece".equals(e.getKey())){
			BillModel bd = this.getBillCardPanel().getBillModel();
			UFDouble pdunum = new UFDouble(getValueNull(bd.getValueAt(e.getRow(), "pdu_num")));
			UFDouble pdupiece = new UFDouble(getValueNull(bd.getValueAt(e.getRow(), "pdu_piece")));
			bd.setValueAt(pdunum.multiply(pdupiece), e.getRow(), "pdu_amount");
		}
		
	}
	
	public String getValueNull(Object obj){
		if(null == obj){
			return "";
		}else{
			return obj.toString();
		}
	}

}
