package nc.ui.dahuan.ctInfo;

import java.lang.reflect.Field;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bfriend.button.IdhButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.ctInfo.CubasVO;
import nc.vo.dahuan.ctInfo.CumanVO;
import nc.vo.dahuan.ctInfo.CustVO;
import nc.vo.dahuan.cttreebill.DhContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.filesystem.NCFileNode;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == IdhButton.RET_COMMIT) {
			onBoRetCommit();
		}
		if (intBtn == IdhButton.CWQR) {
			onBoCwQr();
		}
		if (intBtn == IdhButton.AGREE) {
			onBoAgree();
		}
		if (intBtn == IdhButton.NOAGREE) {
			onBoNoAgree();
		}
		if(intBtn == IdhButton.FILEDOWNLOAD){
			onBoFujian();
		}
		if(intBtn == IdhButton.CWBH){
			onBoCwBh();
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		// 判断查询
		String cotsql = "select count(1) from sm_user_role u where u.pk_corp = '"
				+ _getCorp().getPrimaryKey()
				+ "' and u.cuserid = '"
				+ _getOperator()
				+ "' "
				+ " and u.pk_role in (select r.pk_role from sm_role r where r.role_code in ('DH00','DH01','DH08','CS2') and nvl(r.dr,0)=0) and nvl(u.dr,0)=0 ";
		IUAPQueryBS iQ = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int cot = (Integer) iQ.executeQuery(cotsql, new ColumnProcessor());
		if (cot < 1) {
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString()
					+ " and dhcust_flag<>5 ");

			getBufferData().clear();
			// 增加数据到Buffer
			addDataToBuffer(queryVos);

			updateBuffer();
		} else {

			int month = this._getDate().getMonth();
			int year = this._getDate().getYear();
			String stdate = "";
			if (month > 6) {

				int stmonth = month - 6;
				if (stmonth > 9) {
					stdate = year + "-" + stmonth + "-01";
				} else {
					stdate = year + "-0" + stmonth + "-01";
				}
			} else {
				int styear = year - 1;
				int stmonth = 6 + month;
				if (stmonth > 9) {
					stdate = styear + "-" + stmonth + "-01";
				} else {
					stdate = styear + "-0" + stmonth + "-01";
				}
			}
			
			//判断日期字段2016-10-20
			String where="";
			if(strWhere.toString().indexOf("voperdate")==-1){//制单日期
				where=strWhere.toString()+ " and ((dhcust_flag<>5) or (dhcust_flag=5 and voperdate>'"+ stdate + "'))";
			}else{
				where=strWhere.toString();
			}

			SuperVO[] queryVos = queryHeadVOs(where);

			getBufferData().clear();
			// 增加数据到Buffer
			addDataToBuffer(queryVos);

			updateBuffer();
		}
	}

	@Override
	protected void onBoSave() throws Exception {

		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		BillData data = card.getBillData();
		if (data != null) {
			data.dataNotNullValidate();
		}

		String pkCorp = this._getCorp().getPrimaryKey();

		// 重复校验
		int dcflag = (Integer) card.getHeadItem("dj_status").getValueObject();
		Object pkcubobj = card.getHeadItem("pk_cubasdoc").getValueObject();
		String cuname = (String) card.getHeadItem("dhcust_name")
				.getValueObject();
		if (1 == dcflag) {
			if (null == pkcubobj || "".equals(pkcubobj)) {
				MessageDialog
						.showHintDlg(this.getBillUI(), "提示", "请维护要变更的客商信息");
				return;
			}
			String cfsql = "select count(1) from bd_cubasdoc t where t.pk_cubasdoc<>'"
					+ pkcubobj.toString()
					+ "' and t.custname = '"
					+ cuname
					+ "' and t.pk_corp = '" + pkCorp + "' ";
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			int retcf = (Integer) iQ.executeQuery(cfsql, new ColumnProcessor());
			if (retcf != 0) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "客商已存在");
				return;
			}
		} else if (0 == dcflag) {
			String cfsql = "select count(1) from bd_cubasdoc t where t.custname = '"
					+ cuname + "' and t.pk_corp = '" + pkCorp + "' ";
			IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			int retcf = (Integer) iQ.executeQuery(cfsql, new ColumnProcessor());
			if (retcf != 0) {
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "客商已存在");
				return;
			}
		}

		super.onBoSave();
	}

	@Override
	protected void onBoEdit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			if (this._getOperator().equals(ctvo.getVoperid())) {
				super.onBoEdit();
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示",
						"只有该单据的制单人才可执行此操作");
			}
		}
	}

	// 提交
	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			if (this._getOperator().equals(ctvo.getVoperid())) {
				CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
						CustVO.class, ctvo.getPk_dhcust());
				
				//判断是否有变更附件
				if(ctvo.getDj_status()==1){//变更状态
					String rootPath=ctvo.getDhcust_name();
					FileManageUI ui = new FileManageUI(rootPath);
					Class  cla=FileManageUI.class;
					Field   fields[] =cla.getDeclaredFields(); 
					Field.setAccessible(fields, true);
					for(int i=0;i<fields.length;i++){
						if(fields[i].getName().equals("rootNode")){
							NCFileNode filen=(NCFileNode)fields[i].get(ui);
							if(!filen.children().hasMoreElements()){
								MessageDialog.showHintDlg(this.getBillUI(), "提示",
								"请先上传变更附件！");
								return ;
							}
						}
					}
				}
				nctvo.setDhcust_flag(1);
				nctvo.setGcbid(null);
				nctvo.setGcbdate(null);
				nctvo.setFuzongid(null);
				nctvo.setVappdate(null);
				HYPubBO_Client.update(nctvo);
				super.onBoRefresh();
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示",
						"只有该单据的制单人才可执行此操作");
			}
		}
	}

	// 撤销
	@Override
	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			if (this._getOperator().equals(ctvo.getVoperid())) {
				CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
						CustVO.class, ctvo.getPk_dhcust());
				nctvo.setDhcust_flag(0);
				HYPubBO_Client.update(nctvo);
				super.onBoRefresh();
			} else {
				MessageDialog.showHintDlg(this.getBillUI(), "提示",
						"只有该单据的制单人才可执行此操作");
			}
		}
	}

	// 工程部审核
	@Override
	public void onBoAudit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDhcust_flag(3);
			nctvo.setGcbid(this._getOperator());
			nctvo.setGcbdate(this._getDate());
			nctvo.setFuzongid(null);
			nctvo.setVappdate(null);
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}

	// 工程部驳回
	public void onBoRetCommit() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDhcust_flag(2);

			CanAudMemoDialog cmg = new CanAudMemoDialog(this.getBillUI());
			if (cmg.showCanAudMemoDialog(nctvo)) {
				nctvo.setGcbid(this._getOperator());
				nctvo.setGcbdate(this._getDate());
				nctvo.setFuzongid(null);
				nctvo.setVappdate(null);
				HYPubBO_Client.update(nctvo);
			}

			super.onBoRefresh();
		}
	}

	/*// 解封
	public void onJieFeng() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDj_status(3);
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}*/

	// 不同意
	public void onBoNoAgree() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDhcust_flag(2);

			CanAudMemoDialog cmg = new CanAudMemoDialog(this.getBillUI());
			if (cmg.showCanAudMemoDialog(nctvo)) {
				nctvo.setFuzongid(this._getOperator());
				nctvo.setVappdate(this._getDate());
				HYPubBO_Client.update(nctvo);
			}
			super.onBoRefresh();
		}
	}

	// 副总同意
	public void onBoAgree() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDhcust_flag(4);
			nctvo.setFuzongid(this._getOperator());
			nctvo.setVappdate(this._getDate());
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}

	// 财务确认
	public void onBoCwQr() throws Exception {
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());

			if (1 == nctvo.getDhcust_flag()) {
				// 变更
				CumanVO cmvo = (CumanVO) HYPubBO_Client.queryByPrimaryKey(
						CumanVO.class, nctvo.getPkcumandoc());
				cmvo.setDef1(nctvo.getDhcust_bank());
				cmvo.setDef2(nctvo.getDhcust_saxno());
				HYPubBO_Client.update(cmvo);

				CubasVO cbvo = (CubasVO) HYPubBO_Client.queryByPrimaryKey(
						CubasVO.class, nctvo.getPk_cubasdoc());
				cbvo.setCustname(nctvo.getDhcust_name());
				cbvo.setCustshortname(nctvo.getDhcust_shortname());
				cbvo.setPk_areacl(nctvo.getDhcust_area());
				cbvo.setTaxpayerid(nctvo.getDhcust_sayno());
				cbvo.setPhone1(nctvo.getDhcust_tel());
				cbvo.setZipcode(nctvo.getDhcust_pos());
				cbvo.setConaddr(nctvo.getCust_address());
				cbvo.setRegisterfund(nctvo.getZuce_amount());
				HYPubBO_Client.update(cbvo);
				
				//变更合同的主体客商，更新开户行和银行帐号
				String sql="serlect * from  dh_contract t "+
						" where nvl(dr,0)=0 and (pk_cust1='"+nctvo.getPkcumandoc()+"' or pk_cust2='"+nctvo.getPkcumandoc()+"') " +
						" and t.dctjetotal>t.ljfkjhje ";
				IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
				List<DhContractVO> listvo=(List<DhContractVO>)iQ.executeQuery(sql, new BeanListProcessor(DhContractVO.class));
				if(listvo!=null&&listvo.size()>0){
					for(DhContractVO dvo:listvo){
						dvo.setPk_bank(nctvo.getDhcust_bank());
						dvo.setSax_no(nctvo.getDhcust_saxno());
					}
					HYPubBO_Client.updateAry(listvo.toArray(new DhContractVO[0]));
				}
				
			} else if (2 == nctvo.getDhcust_flag()) {
				// 注销
				CumanVO cmvo = (CumanVO) HYPubBO_Client.queryByPrimaryKey(
						CumanVO.class, nctvo.getPkcumandoc());
				cmvo.setSealflag("Y");
				HYPubBO_Client.update(cmvo);
			} else {
				// 新增
				String pkcorp = this._getCorp().getPrimaryKey();

				String cksql = "select count(1) from bd_cumandoc m where (m.sealflag is null or m.sealflag = 'N') and nvl(m.dr, 0) = 0 "
						+ " and (m.pk_corp = '"
						+ pkcorp
						+ "' or m.pk_corp = '0001') and exists (select 1 from bd_cubasdoc b where nvl(b.dr, 0) = 0 "
						+ " and b.pk_cubasdoc = m.pk_cubasdoc and b.custname = '"
						+ nctvo.getDhcust_name() + "') ";
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(
						IUAPQueryBS.class);
				int cot = (Integer) iQ.executeQuery(cksql,
						new ColumnProcessor());
				if (cot == 0) {
					MessageDialog.showHintDlg(this.getBillUI(), "提示",
							"请手工维护新增的客商信息");
					return;
				}
			}
			nctvo.setDhcust_flag(5);
			nctvo.setCaiwuid(this._getOperator());
			nctvo.setSuredate(this._getDate());
			HYPubBO_Client.update(nctvo);
			super.onBoRefresh();
		}
	}
	
	protected void onBoCwBh() throws Exception{
		
		AggregatedValueObject aggvo = this.getBufferData().getCurrentVO();
		if (null != aggvo) {
			CustVO ctvo = (CustVO) aggvo.getParentVO();
			CustVO nctvo = (CustVO) HYPubBO_Client.queryByPrimaryKey(
					CustVO.class, ctvo.getPk_dhcust());
			nctvo.setDhcust_flag(2);
			
			CanAudMemoDialog cmg = new CanAudMemoDialog(this.getBillUI());
			if (cmg.showCanAudMemoDialog(nctvo)) {
				//nctvo.setFuzongid(this._getOperator());
				//nctvo.setVappdate(this._getDate());
				HYPubBO_Client.update(nctvo);
			}
			
			super.onBoRefresh();
		}
	
	}

	@Override
	protected void onBoDelete() throws Exception {
		
		CustVO vo=(CustVO)getBufferData().getCurrentVO().getParentVO();
		if(vo==null){
			MessageDialog.showHintDlg(getBillUI(), "提示", "数据异常！请选择单据！");
			return ;
		}
		
		if(vo.getDhcust_flag()!=0){
			MessageDialog.showHintDlg(getBillUI(),"提示", "单据状态需为未提交！");
			return ;
		}
		
		super.onBoDelete();
	}
	
	protected void onBoFujian()throws Exception{
		
		CustVO vo=(CustVO)getBufferData().getCurrentVO().getParentVO();
		if(vo==null){
			MessageDialog.showHintDlg(getBillUI(), "提示", "数据异常！请选择单据！");
			return ;
		}
		
		if(vo.getDj_status()!=1){
			MessageDialog.showHintDlg(getBillUI(),"提示", "单据属性需为变更！");
			return ;
		}
		
		DocumentManagerHT.showDM(this.getBillUI(), null, vo.getDhcust_name());
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		
		String user=_getOperator();
		String pk_corp=_getCorp().getPk_corp();
		IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		if(!pk_corp.equals("1002")){
//			查询工程部pk
			String sql2="select pk_deptdoc from bd_deptdoc where nvl(dr,0)=0 " +
				"and pk_corp='"+pk_corp+"' and deptname='工程管理部' and canceled='N'";
			
			Object gc=query.executeQuery(sql2, new ColumnProcessor());
			
			if(gc==null){
				MessageDialog.showHintDlg(getBillUI(), "提示", "请先维护部门工程管理部");
				return ;
			}
			
			String sql3="select count(1) from dh_fkgx_d where nvl(dr,0)=0 and pk_dept_user='"+user+"' " +
					"and pk_fkgx=(select pk_fkgx from dh_fkgx x where nvl(x.dr,0)=0 " +
					"and pk_deptdoc='"+gc+"' and pk_corp='"+pk_corp+"')";
			Integer it2=(Integer)query.executeQuery(sql3, new ColumnProcessor());
			
			if(it2 < 1){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "只有工程部业务员才可制单");
				return;
			}
		}
		super.onBoAdd(bo);
	}
	
	
	
}