package nc.ui.arap.bx.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.logging.Log;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.FIBException;
import nc.ui.arap.bx.actions.BXDefaultAction;
import nc.ui.arap.eventagent.EventTypeConst;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BxFinItemVO;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author twei
 * 
 * nc.ui.arap.bx.listeners.BxListHeadEditListener
 */
public class BxListHeadEditListener extends BXDefaultAction {

	public void afterEdit() {

		EventObject event = (EventObject) getMainPanel().getAttribute(EventTypeConst.TEMPLATE_EDIT_EVENT);

		if (event instanceof BillEditEvent) {

			BillEditEvent e = (BillEditEvent) event;
			String key = e.getKey();
			
			boolean oldSelected=getVoCache().getSelectedVOs().size()==0;

			if (key.equals(BXHeaderVO.SELECTED)) {

				int row = e.getRow();
				String djoid = getBillListPanel().getHeadBillModel().getValueAt(row, BXHeaderVO.PK_JKBX).toString();
				BXVO vo = getVoCache().getVOByPk(djoid);
				if (vo != null) {
					if(((Boolean) e.getValue()).booleanValue())
						vo.getParentVO().setSelected(UFBoolean.TRUE);
					else
						vo.getParentVO().setSelected(UFBoolean.FALSE);
				}
				
				boolean newSelected=getVoCache().getSelectedVOs().size()==0;
				
				if(newSelected!=oldSelected){
					getActionRunntimeV0().updateButtonStatus();
				}else{
					getMainPanel().refreshBtnStatus(new String[]{"审核","反审核"});/*-=notranslate=-*/
				}
			}
		}
	}
	
	public void bodyRowChange(){
		
		EventObject event = (EventObject) getMainPanel().getAttribute(EventTypeConst.TEMPLATE_EDIT_EVENT);
		BillEditEvent  e=null;
		
		if (event instanceof BillEditEvent){
			e=(BillEditEvent)event;
		}else{
			return ;
		}

		if(e.getPos()==BillItem.BODY){
			return;
		}
		
		if(getMainPanel().getBillListPanel().getBodyBillModel().getBodyItems()==null || getMainPanel().getBillListPanel().getBodyBillModel().getBodyItems().length==0){
			return ;
		}
		
		String[] bodyTableCodes = getMainPanel().getBillListPanel().getBillListData().getBodyTableCodes();
		if(bodyTableCodes!=null){
			for(String table:bodyTableCodes){
				getMainPanel().getBillListPanel().setBodyValueVO(table,new BXBusItemVO[]{});
			}
		}
		
		int row = e.getRow();
		
		if(row<0)
			return ;
		
		Object pkjkbx = getMainPanel().getBillListPanel().getHeadBillModel().getValueAt(row, BXHeaderVO.PK_JKBX);
	
		if(pkjkbx==null)
			return;
		
		BXVO zbvo = getMainPanel().getCache().getVOByPk(pkjkbx.toString());
		//根据表头VO的信息，查询出表体其他页签的信息，获得完整的聚合VO
		if( (zbvo.getChildrenVO()==null || zbvo.getChildrenVO().length==0 ) && !zbvo.isChildrenFetched()){  //初始化表体vo
			try {
				zbvo=retrieveChidren(zbvo);
			} catch (BusinessException e1) {
				throw new BusinessRuntimeException(e1.getMessage(),e1);
			}
		}
		
		//哈希表map<页签编码，表体VO列表>，建立页签与所属表体数据的联系
		Map<String,List> map=new HashMap<String, List>();
		
		//处理业务页签信息
		BXBusItemVO[] items=zbvo.getBxBusItemVOS();
		if(items!=null){
			for (int i = 0; i < items.length; i++) {
				if(map.containsKey(items[i].getTablecode())){
					List<BXBusItemVO> list = map.get(items[i].getTablecode());
					list.add(items[i]);
					map.put(items[i].getTablecode(),list);
				}else{
					List<BXBusItemVO> list=new ArrayList<BXBusItemVO>();
					list.add(items[i]);
					map.put(items[i].getTablecode(), list);
				}
			}
		}

		//处理财务页签信息
		BxFinItemVO[] childrenVO = zbvo.getChildrenVO();
		if(childrenVO!=null && childrenVO.length!=0){
			List<BxFinItemVO> finitems=new ArrayList<BxFinItemVO>();
			for(BxFinItemVO vo:childrenVO){
				finitems.add(vo);
			}
			map.put(BXConstans.FIN_PAGE,finitems);
		}
		
		//处理借款报销页签信息
		BxcontrastVO[] bxchildVo=zbvo.getContrastVO();
		if(bxchildVo!=null && bxchildVo.length!=0){
			List<BxcontrastVO> finitems=new ArrayList<BxcontrastVO>();
			for(BxcontrastVO vo:bxchildVo){
				finitems.add(vo);
			}
			map.put(BXConstans.CONST_PAGE,finitems);
		}
		
		Set<String> tables = map.keySet();
		
		for (String table:tables) {
			if(getMainPanel().getBillListPanel().getBodyBillModel(table).getBodyItems()!=null){
				BillItem[] bodyItems = getMainPanel().getBillListPanel().getBodyBillModel(table).getBodyItems();
				for(BillItem item:bodyItems){
					if(item.getKey().equals(BXBusItemVO.AMOUNT) || (item.getIDColName()!=null && item.getIDColName().equals(BXBusItemVO.AMOUNT))){
						String bzbm = zbvo.getParentVO().getBzbm();
						if(bzbm!=null){
							int precision=2;
							try {
								precision = Currency.getCurrDigit(bzbm);
							} catch (FIBException e1) {
								Log.getInstance(this.getClass()).error(e1);
							}
							item.setDecimalDigits(precision);
						}
					}
				}
				CircularlyAccessibleValueObject[] voArray = (CircularlyAccessibleValueObject[]) map.get(table).toArray(new CircularlyAccessibleValueObject[]{});
				getMainPanel().getBillListPanel().setBodyValueVO(table,voArray);
				if(voArray!=null && voArray.length!=0)
					getMainPanel().getBillListPanel().getBodyBillModel(table).execLoadFormula();
					//getMainPanel().getBillListPanel().getHeadBillModel().execLoadFormula();
			}
		}
		
	}
	/**
	 * 运行表体公式
	 * 参数：行号，字段编码，页签编码
	 * @see nc.ui.arap.bx.actions.AddRowAction
	 * @return void
	 */
	private void execBodyFormula(int rownum,String key,String tableCode){
		BillItem item = getBillListPanel().getBillListData().getBodyItem(tableCode,key);
		if(item!=null){
			String[] formulas = item.getEditFormulas();
			BillModel bm = getBillListPanel().getBodyBillModel(tableCode);
			if (bm != null)
				bm.execFormulas(rownum, formulas);
		}
	}
}
