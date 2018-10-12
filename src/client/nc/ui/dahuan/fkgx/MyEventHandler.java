package nc.ui.dahuan.fkgx;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.dahuan.fkgx.DhFkgxDVO;
import nc.vo.dahuan.fkgx.DhFkgxVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

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
	protected void onBoLineAdd() throws Exception {
		// 确认表头信息完整
		BillCardPanel panel = this.getBillCardPanelWrapper().getBillCardPanel();
		Object pkDept = panel.getHeadItem(DhFkgxVO.PK_DEPTDOC).getValueObject();
		Object pkUser = panel.getHeadItem(DhFkgxVO.PK_USER1).getValueObject();
		
		if(null == pkDept || null == pkUser || "".equals(pkDept) || "".equals(pkUser)){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "请维护好部门和部门主管信息");
			return;
		}
		
		super.onBoLineAdd();
	}
	
	

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		// 部门不可修改
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		card.getHeadItem("pk_deptdoc").setEdit(false);
	}

	@Override
	protected void onBoSave() throws Exception {
		// 数据校验
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();

		// 画面所有字段必填，并校验部门有无重复
		Object pkUser = card.getHeadItem("pk_user1").getValueObject();
		if(null == pkUser || "".equals(pkUser)){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "负责人必填");
			return;
		}
		Object pkDept = card.getHeadItem("pk_deptdoc").getValueObject();
		if(null == pkDept || "".equals(pkDept)){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "部门信息必填");
			return;
		}
		int rows = card.getBillModel().getRowCount();
		if(rows <= 0){
			MessageDialog.showHintDlg(this.getBillUI(), "提示", "部门主管级别需维护所属员工信息");
			return;
		}

		Object fkgxPK = card.getHeadItem("pk_fkgx").getValueObject();
		// 判断是修改保存还是新增保存，若是新增保存需校验部门的唯一性
		if(null == fkgxPK || "".equals(fkgxPK)){
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			SQLParameter param = new SQLParameter();
			String sql = "select count(1) from dh_fkgx t where t.pk_deptdoc=? and nvl(t.dr,0)=0 and t.pk_corp=?";
			param.clearParams();
			param.addParam(pkDept);
			param.addParam(_getCorp().getPrimaryKey());
			Integer count = (Integer)query.executeQuery(sql,param, new ColumnProcessor());
			if(count>0){
				MessageDialog.showHintDlg(this.getBillUI(), "提示", "该部门信息已存在");
				return;
			}
		}
		

		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据清空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			// 主表更新
			DhFkgxVO mvo = (DhFkgxVO)billVO.getParentVO();
			String hy="";
			if(null == mvo.getPk_fkgx() || "".equals(mvo.getPk_fkgx())){
				hy = (String)HYPubBO_Client.insert(mvo);//返回主键值
				
			}else{
				hy = mvo.getPk_fkgx();
				HYPubBO_Client.update(mvo);
			}
			
			// 子表更新
			DhFkgxDVO[] dvos = (DhFkgxDVO[])billVO.getChildrenVO();
			DhFkgxDVO[] nvos = new DhFkgxDVO[dvos.length];
			for(int i=0;i<dvos.length;i++){
				DhFkgxDVO dvo = dvos[i];
				String pkDDuser = dvo.getPk_dept_user();
				if(!"".equals(pkDDuser) && null != pkDDuser){
					dvo.setPk_fkgx(hy);
					dvo.setPk_fkgx_d(null);
					nvos[i]=dvo;
				}
			}
			HYPubBO_Client.deleteByWhereClause(DhFkgxDVO.class, " pk_fkgx = '"+mvo.getPk_fkgx()+"'");
			HYPubBO_Client.insertAry(nvos);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// 设置保存后状态
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	
	}
	
		
}