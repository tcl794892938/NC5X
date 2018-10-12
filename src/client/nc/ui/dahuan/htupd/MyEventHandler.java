package nc.ui.dahuan.htupd;

import nc.bs.framework.common.NCLocator;
import nc.itf.dahuan.pf.IdhCwUpd;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends ManageEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		
		// 合同重复校验
		BillCardPanel card = this.getBillCardPanelWrapper().getBillCardPanel();
		UIRefPane uif = (UIRefPane)card.getHeadItem("pk_contract").getComponent();
		String htcode = uif.getRefCode();
		String sql1 = "select count(1) from dh_contract t where t.ctcode = '"+htcode+"' and nvl(t.dr,0)=0";
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		int cot = (Integer)iQ.executeQuery(sql1, new ColumnProcessor());
		if(0==cot){
			MessageDialog.showHintDlg(card, "提示", "合同已不存在");
			return;
		}else if(cot>1){
			MessageDialog.showHintDlg(card, "提示", "合同存在重复");
			return;
		}
		// 数据校验
		UIComboBox djlxBox = (UIComboBox)card.getHeadItem("djlx").getComponent();
		int index = djlxBox.getSelectedIndex();	
		if(2==index){
			// 盖章需判断
			String sql2 = "select count(1) from gl_freevalue v where exists(select 1 from bd_jobmngfil m " +
					" where exists(select 1 from dh_contract g where g.pk_contract = '"+uif.getRefPK()+"' and g.pk_jobmandoc = m.pk_jobmngfil) " +
					" and m.pk_jobbasfil=v.checkvalue) and nvl(v.dr,0)=0 ";
			int cot2 = (Integer)iQ.executeQuery(sql2, new ColumnProcessor());
			if(cot2>0){
				MessageDialog.showHintDlg(card, "提示", "该合同已做凭证，不可取消盖章");
				return;
			}
			
			String sql3 = "select count(1) from dh_fkjhbill t where t.ctcode = '"+htcode+"' and nvl(t.dr,0)=0 ";
			int cot3 = (Integer)iQ.executeQuery(sql3, new ColumnProcessor());
			if(cot3>0){
				MessageDialog.showHintDlg(card, "提示", "该合同已做付款单，不可取消盖章");
				return;
			}
			
			String sql4 = "select count(1) from htupd_v t where t.pk_contract = '"+uif.getRefPK()+"' ";
			int cot4 = (Integer)iQ.executeQuery(sql4, new ColumnProcessor());
			if(cot4>0){
				MessageDialog.showHintDlg(card, "提示", "该合同已做回款单，不可取消盖章");
				return;
			}
		}
		
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = checkVO.getParentVO();
				checkVO.setParentVO(null);
			} else {
				o = checkVO.getChildrenVO();
				checkVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (checkVO.getParentVO() == null
				&& (checkVO.getChildrenVO() == null || checkVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			IdhCwUpd idh = (IdhCwUpd)NCLocator.getInstance().lookup(IdhCwUpd.class.getName());
			checkVO = idh.updHtVo(checkVO, index);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				checkVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(checkVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(checkVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { checkVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), checkVO);

		// 设置保存后状态
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		
	}

	@Override
	protected void onBoQuery() throws Exception {
		
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
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
		
		String str2=strWhere.toString()+" and ts>'"+stdate+"'";

		SuperVO[] queryVos = queryHeadVOs(str2);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		
	}
	
	
	
	
  }