package nc.ui.dahuan.vhtfkmeg;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import nc.ui.trade.bocommand.MetaDataPrintBoCommand;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.dahuan.vhtfkmeg.ViewhtfkVO;
import nc.vo.pub.BusinessException;
import nc.vo.trade.button.ButtonVO;

/** 
  *
  *该类用来进行按钮事件处理, 扩展按钮的事件响应应该实现IUIButtonCommand并进行注册
  *@author author
  *@version tempProject version
  */
  
public class MyEventHandler 
                                          extends ListEventHandler{
	
	private Map<Integer, IButtonCommand> commands = new HashMap<Integer, IButtonCommand>(); 
	
	public MyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);
		initBoCommand();
	}

	protected void addBoCommand(int intBtn,IButtonCommand command){
		commands.put(intBtn, command);
	}
	
	private IButtonCommand getButtonCommand(int intBtn){
		return commands.get(intBtn);
	}
	
	private void preBoCommand(){
		addBoCommand(IBillButton.Print, new MetaDataPrintBoCommand(getBillUI(),getUIController()));
	}
	
	protected void initBoCommand(){ 
	
		preBoCommand();
		
		List<IUserDefButtonCommand> bos = ((ClientUI)getBillUI()).getUserButtons();
		if(bos != null) {
		   for(IUserDefButtonCommand cmd : bos)
			   addBoCommand(cmd.getButtonVO().getBtnNo(), cmd);
		}
	} 

	
	 @Override
	 public void onButton(ButtonObject bo) {
	 
	  	  Object boData = null;
		  if (bo.getData() != null) {
		  	  boData = bo.getData();
		  }else if(bo.getParent() != null && bo.getParent().getData() != null) {
		   	  boData = bo.getParent().getData();
		  }
		  
		  int intBtn = -1;
		  if(boData != null && boData instanceof ButtonVO)
		   	  intBtn = ((ButtonVO) boData).getBtnNo();
		  
		  IButtonCommand command = getButtonCommand(intBtn);
		  if (command != null)
		  	 onBoCommand(command,bo);
		  else
		   	 super.onButton(bo);
	 }
	 
	 private void onBoCommand(IButtonCommand command, ButtonObject bo){
	 
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		}
		try {
			command.execute(bo);
		} catch (BusinessException ex) {
			onBusinessException(ex);
		} catch (SQLException ex) {
			getBillUI().showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			Logger.error(e.getMessage());
		}
	 }

	@Override
	protected void onBoRefresh() throws Exception {
		this.getBufferData().clear();
		
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<ViewhtfkVO>  vhflit = (List<ViewhtfkVO>)iQ.retrieveByClause(ViewhtfkVO.class, " bill_pkuser = '"+this._getOperator()+"' ");
		
		addDataToBuffer(vhflit.toArray(new ViewhtfkVO[0]));
		
		updateBuffer();
	}
	 
	 
}