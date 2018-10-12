package nc.ui.arap.bx.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.pub.IBxUIControl;
import nc.itf.cmp.IUpdateData;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.bx.BXBillMainPanel;
import nc.ui.arap.engine.IActionRuntime;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.arap.bx.util.BXParamConstant;
import nc.vo.arap.bx.util.BXStatusConst;
import nc.vo.arap.util.StringUtils;
import nc.vo.cmp.exception.ErmException;
import nc.vo.cmpbill.outer.BugetAlarmBusinessException;
import nc.vo.cmpbill.outer.CmpFpBusinessException;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BxFinItemVO;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.ep.bx.MessageVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.er.exception.CrossControlMsgException;
import nc.vo.erm.control.YsControlVO;
import nc.vo.erm.util.ErVOUtils;
import nc.vo.erm.util.VOUtils;
import nc.vo.fibill.outer.FiBillAccessableBusiVO;
import nc.vo.fibill.outer.FiBillAccessableBusiVOProxy;
import nc.vo.logging.Debug;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * nc.ui.arap.bx.actions.SaveAction
 *
 * @author twei
 *
 * 保存单据
 */

public class SaveAction extends BXDefaultAction {


	public SaveAction() {
	}
	public SaveAction(IActionRuntime panel) {
		setActionRunntimeV0(panel);
	}

	/**
	 * @return 是否需要进行冲借款操作.
	 * @throws Exception
	 */
	public boolean save() throws Exception {

		removeNullItem();

		BXVO bxvo = getBillValueVO();

		getCardPanel().dataNotNullValidate();

		boolean pass = getCardPanel().getBillData().execValidateFormulas();

		if(pass)
			pass=getCardPanel().getBillData().execBodyValidateFormulas();

		if(!pass){
			return false;
		}

		DjLXVO djlxvo = getVoCache().getCurrentDjlx();

		if ((bxvo.getContrastVO()==null ||bxvo.getContrastVO().length==0)  && bxvo.getParentVO().getDjdl().equals(BXConstans.BX_DJDL)) {
			boolean para = false;
			//目前产品的实现是负数金额的单据保存也会根据ER7校验，导致负数金额单据保存不成功,现在把其置成false,不让其起作用
			try {
				para = SysInit.getParaBoolean(ClientEnvironment.getInstance().getCorporation().getPk_corp(), BXParamConstant.PARAM_IS_FORCE_CONTRAST).booleanValue();
			} catch (java.lang.Throwable ivjExc) {
			}
			if(bxvo.getParentVO().getYbje()!=null && bxvo.getParentVO().getYbje().doubleValue()>=0){
				if(para){
					if (getBxUIControl().getJKD(bxvo.getParentVO(),ClientEnvironment.getInstance().getDate(),null).size() != 0) {
						MessageDialog.showErrorDlg(this.getParent(), null,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011V57","UPP2011V57-000002")/*@res "本人有未清的借款单，必须进行冲借款操作!"*/);
							return true;
					}
				}else if(djlxvo.getIscontrast()!=null && djlxvo.getIscontrast().booleanValue()){
					if(bxvo.getParentVO().getYbje()!=null && bxvo.getParentVO().getYbje().doubleValue() == 0){
						if ((bxvo.getContrastVO()==null ||bxvo.getContrastVO().length==0)  && bxvo.getParentVO().getDjdl().equals(BXConstans.BX_DJDL)) {
							getMainPanel().showWarningMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000018")/*@res "还款类单据必须进行冲借款操作"*/);
							return true;
						}
					}

					if (getBxUIControl().getJKD(bxvo.getParentVO(),ClientEnvironment.getInstance().getDate(),null).size() != 0) {
						int result = getMainPanel().showYesNoMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000019")/*@res "报销人有未两清的借款单,是否进行冲借款操作?"*/);
						if (result == UIDialog.ID_YES)
							return true;
					}
				}
			}
		}

		saveBXVO(bxvo);

		return false;
	}
	private void removeNullItem() {
		String[] bodyTableCodes = getCardPanel().getBillData().getBodyTableCodes();
		for(String tableCode:bodyTableCodes){
			//财务页签处理
			if(tableCode.equals(BXConstans.FIN_PAGE))
				continue;
			BillScrollPane bodyPanel = getBxBillCardPanel().getBodyPanel(tableCode);
			BillItem[] bodyItems = bodyPanel.getTableModel().getBodyItems();
			int rowCount = bodyPanel.getTableModel().getRowCount();
			for (int i = rowCount-1; i >0; i--) {
				boolean isnull=true;
				for(BillItem item:bodyItems){
					if(item.isShow()){
						if(bodyPanel.getTableModel().getValueAt(i, item.getKey())!=null){
							isnull=false;
							break;
						}
					}
				}
				if(isnull){
					bodyPanel.delLine(new int[]{i});
				}
			}
		}
	}

	private IBxUIControl getBxUIControl(){
		return NCLocator.getInstance().lookup(IBxUIControl.class);
	}

	/**
	 * 暂存单据//常用单据的保存
	 *
	 * @throws Exception
	 */
	public void tempSave() throws Exception {

		BXVO bxvo = getBillValueVO();
		if (getBxParam().isInit()) { // 常用单据

			if (bxvo.getParentVO().getPk_corp() == null) {
				bxvo.getParentVO().setDwbm(BXConstans.GROUP_CODE);
			}

			String pk_corp = bxvo.getParentVO().getPk_corp();
			String djlxbm = bxvo.getParentVO().getDjlxbm();

			List<BXHeaderVO> vos = getInitBillHeader(pk_corp, djlxbm);

			if (vos != null && vos.size() > 0 && !VOUtils.simpleEquals(vos.get(0).getPk_jkbx(), bxvo.getParentVO().getPk_jkbx())) {
				int result = getMainPanel().showYesNoMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000020")/*@res "该单据类型的常用单据在当前公司已经存在,是否进行覆盖?"*/);
				if (result == UIDialog.ID_YES) {
					BXHeaderVO headerVO = vos.get(0);
					headerVO.setInit(true);
					getIBXBillPublic().deleteBills(new BXVO[]{new BXVO(headerVO)});
					getVoCache().removeVO(new BXVO(headerVO));
				} else {
					return;
				}
			}
		}

		ErVOUtils.clearContrastInfo(bxvo);
		bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_TempSaved);
		bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_NO);

		saveBXVOBack(bxvo,true);

	}

	/**
	 * @param bxvo 要保存的单据聚合VO
	 * @param isTempSave 是否暂存
	 * @throws BusinessException
	 *
	 * 后台保存单据
	 */
	private void saveBXVOBack(BXVO bxvo,boolean isTempSave) throws BusinessException {

		BXBillMainPanel mainPanel = getMainPanel();

		BXVO[] bxvos;
		Integer djzt = bxvo.getParentVO().getDjzt();//单据状态
		try {
			if(StringUtils.isNullWithTrim(bxvo.getParentVO().getPk_jkbx())){
				if(isTempSave){
					if(djzt!=BXStatusConst.DJZT_TempSaved){
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000363")/*@res "非暂存态单据不能暂存！"*/);
					}
					bxvos = getIBXBillPublic().tempSave(new BXVO[] { bxvo });
				}else if (bxvo.getParentVO().getQcbz().booleanValue()) {
					bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_Sign);
					bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_VALID);
					bxvos = getIBXBillPublic().save(new BXVO[] { bxvo });
				}else{
					bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_Saved);//将暂存单据由暂存状态转为保存状态
					bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_NO);
					bxvos = getIBXBillPublic().save(new BXVO[] { bxvo });
				}
			}else{
				if(isTempSave){
					bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_TempSaved);
					bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_NO);
				}else if (bxvo.getParentVO().getQcbz().booleanValue()) {
					bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_Sign);//将暂存单据由暂存状态转为保存状态
					bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_VALID);
				} else {
					bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_Saved);//将暂存单据由暂存状态转为保存状态
					bxvo.getParentVO().setSxbz(BXStatusConst.SXBZ_NO);
				}
				bxvo.setBxoldvo(getVoCache().getVOByPk(bxvo.getParentVO().getPk_jkbx()));
				bxvos = getIBXBillPublic().update(new BXVO[] { bxvo });
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(NCLangRes.getInstance().getStrByID("20060610", "UPP20060610-000006")/* @res "保存失败" */+ "\n" + e.getMessage());
		}

		getVoCache().addVO(bxvos[0]);
		mainPanel.updateView();
	}

	/**
	 * @param bxvo
	 * @throws Exception
	 *
	 * 保存单据， 走审批流
	 */
	private void saveBXVO(BXVO bxvo) throws Exception {

		BXBillMainPanel mainPanel = getMainPanel();

		BXVO[] bxvos = null;

		String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();

		//期初单据走后台保存
		if(bxvo.getParentVO().getQcbz().booleanValue()){
			saveBXVOBack(bxvo,false);
		}else{

			try {

				if (StringUtils.isNullWithTrim(bxvo.getParentVO().getPk_jkbx())) {
					Object re_result = nc.ui.pub.pf.PfUtilClient.processAction("SAVE", bxvo.getParentVO().getDjlxbm(), currTime, bxvo);

					bxvos = getResultBXVO(bxvos, re_result);
					this.getYSBuget(bxvos[0]);//by tcl

				} else {
					//将暂存单据由暂存状态转为保存状态
					if(bxvo.getParentVO().getDjzt()==BXStatusConst.DJZT_TempSaved)
						bxvo.getParentVO().setDjzt(BXStatusConst.DJZT_Saved);
					bxvo.setBxoldvo(getVoCache().getVOByPk(bxvo.getParentVO().getPk_jkbx())); //设置修改前的VO
					Object re_result = nc.ui.pub.pf.PfUtilClient.processAction("EDIT", bxvo.getParentVO().getDjlxbm(), currTime, bxvo);

					bxvos = getResultBXVO(bxvos, re_result);
					this.getYSBuget(bxvos[0]);//by tcl
				}

				if(bxvos==null || bxvos[0]==null){
					return;
				}

				//显示预算，借款控制的提示信息
				if(!StringUtils.isNullWithTrim(bxvos[0].getWarningMsg())){
					showWarningMsg(bxvos[0].getWarningMsg());
					bxvos[0].setWarningMsg(null);
				}

			}catch (CmpFpBusinessException e) {
				if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "提示"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000364")/*@res " 是否继续保存？"*/)== MessageDialog.ID_YES){
					bxvo.setHasZjjhCheck(Boolean.TRUE);  //不检查，强写资金计划
					saveBXVO(bxvo);
					return;
				}else{
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000342")/*@res "资金计划申请失败"*/,e);
				}
			}catch (BugetAlarmBusinessException e) {
				if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "提示"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000364")/*@res " 是否继续保存？"*/)== MessageDialog.ID_YES){
					bxvo.setHasNtbCheck(Boolean.TRUE);  //不检查
					saveBXVO(bxvo);
					return;
				}else{
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000405")/*@res "预算申请失败"*/,e);
				}
			}catch (ErmException e) {
				if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "提示"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000364")/*@res " 是否继续保存？"*/)== MessageDialog.ID_YES){
					bxvo.setHasJkCheck(Boolean.TRUE);  //不检查
					saveBXVO(bxvo);
					return;
				}else{
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000406")/*@res "借款申请失败"*/,e);
				}
			}catch (CrossControlMsgException e) {
				if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "提示"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000364")/*@res " 是否继续保存？"*/)== MessageDialog.ID_YES){
					bxvo.setHasCrossCheck(Boolean.TRUE);  //不检查
					saveBXVO(bxvo);
					return;
				}else{
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000417")/*@res "交叉校验失败"*/,e);
				}
			}catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(NCLangRes.getInstance().getStrByID("20060610", "UPP20060610-000006")/* @res "保存失败" */+ "\n" + e.getMessage());
			}
			getVoCache().addVO(bxvos[0]);
			mainPanel.updateView();
		}
	}
	private BXVO[] getResultBXVO(BXVO[] bxvos, Object re_result) {
		if(re_result instanceof BXVO[]) {
			bxvos = (BXVO[]) re_result;
		}else if(re_result instanceof MessageVO[]){
			MessageVO[] msgs = (MessageVO[]) re_result;
			bxvos=new BXVO[msgs.length];
			int i=0;
			for(MessageVO msg:msgs){
				bxvos[i++]=msg.getBxvo();
			}
		}
		return bxvos;
	}

	public void prepareVO(BXVO bxvo) throws ValidationException {
		BXHeaderVO parentVO = bxvo.getParentVO();

		prepareContrast(bxvo);
		prepareHeader(parentVO,bxvo.getContrastVO());
	}

	/**
	 * 初始化冲借款对照VO
	 *
	 * @param bxvo
	 */
	private void prepareContrast(BXVO bxvo) {

		if(getBxBillCardPanel().isContrast()){
			List<BxcontrastVO> contrasts = getBxBillCardPanel().getContrasts();
			bxvo.setContrastVO(contrasts.toArray(new BxcontrastVO[]{}));
			bxvo.setContrastUpdate(true);
		}else{
			bxvo.setContrastUpdate(false);
			if(getVoCache().getVOByPk(bxvo.getParentVO().getPk_jkbx())!=null){
				bxvo.setContrastVO(getVoCache().getVOByPk(bxvo.getParentVO().getPk_jkbx()).getContrastVO());
			}
		}
	}

	/**
	 * @param parentVO
	 * @param bxcontrastVOs
	 * @throws ValidationException
	 *
	 * 初始化单据表头
	 */
	private void prepareHeader(BXHeaderVO parentVO, BxcontrastVO[] bxcontrastVOs) throws ValidationException {

		// 设置是否常用单据/期初单据
		parentVO.setInit(getBxParam().isInit());
		parentVO.setQcbz(new UFBoolean(getBxParam().getIsQc()));

		//设置最后修改人
		parentVO.setEnduser(getBxParam().getPk_user());
	}
	
	private void checkData(BXVO bxvo)throws Exception{
		
		List<FiBillAccessableBusiVOProxy> voProxys = new ArrayList<FiBillAccessableBusiVOProxy>();
		
		BXVO bvo=new BXVO();
		BXHeaderVO hvo=new BXHeaderVO();
		/*BXBusItemVO bvo2=new BXBusItemVO();
		bvo2.setSzxmid("0001MM1000000001FV3L");
		
		BxcontrastVO bvo3=new BxcontrastVO();
		bvo3.setSzxmid("0001MM1000000001FV3L");*/
		
		/*BxFinItemVO bvo4=new BxFinItemVO();
		bvo4.setSzxmid("0001MM1000000001FV3L");*/
		
		/*bvo.setBxBusItemVOS(new BXBusItemVO[]{bvo2});
		bvo.setContrastVO(new BxcontrastVO[]{bvo3});*/
		//bvo.setChildrenVO(new BxFinItemVO[]{bvo4});
		//hvo.setDwbm("1024");
		hvo.setDjdl("bx");
		hvo.setDjlxbm("264X-01");
		hvo.setSzxmid("0001MM1000000001FV3L");
		hvo.setZyx1("0001A110000000000LM4");
		hvo.setDjrq(new UFDate("2017-01-02"));
		//hvo.setPk_corp("1024");
		//hvo.setContrastenddate(new UFDate("3000-01-01"));
		hvo.setFydwbm("1024");
		bvo.setParentVO(hvo);
		BXHeaderVO[] items = ErVOUtils.prepareBxvoItemToHeaderClone(bvo);
		for(BXHeaderVO item:items){
			YsControlVO vo = new YsControlVO();
			vo.setItems(new BXHeaderVO[]{item});
			voProxys.add(getFiBillAccessableBusiVOProxy(vo));
		}
		
		nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(voProxys.toArray(new nc.vo.ntb.outer.IAccessableBusiVO[]{}));
		
		System.out.println(vos);
	}
	
	/**
	 * by tcl
	 */
	private BXVO getYSBuget(BXVO bxvo)throws Exception{
		this.checkData(bxvo);
		List<FiBillAccessableBusiVOProxy> voProxys = new ArrayList<FiBillAccessableBusiVOProxy>();
		BXHeaderVO[] items = ErVOUtils.prepareBxvoItemToHeaderClone(bxvo);

		for(BXHeaderVO item:items){
			YsControlVO vo = new YsControlVO();
			vo.setItems(new BXHeaderVO[]{item});
			voProxys.add(getFiBillAccessableBusiVOProxy(vo));
		}
		
		try {
			nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(voProxys.toArray(new nc.vo.ntb.outer.IAccessableBusiVO[]{}));

			if(vos==null||vos.length<=0){
				Debug.debug("没有定义控制方案！");
				return bxvo;
			}
			
			for(NtbParamVO nvo:vos){
				
				if(nvo.getPlanname().startsWith("零预算")){
					Debug.debug("没有定义控制方案！");
					return bxvo;
				}
				
				String [] attrs=nvo.getBusiAttrs();
				HashMap<String, String> map=new HashMap<String, String>();
				for(String attr:attrs){
					map.put(attr, attr);
				}
				
				if((attrs.length==2&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid"))||
						(attrs.length==3&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid")&&map.containsKey("zb.zyx1"))){//仅公司+项目预算
					UFDouble yssmny=new UFDouble(nvo.getPlanData().doubleValue(),2);
					UFDouble zxsmny=new UFDouble(nvo.getRundata()[0].doubleValue(),2);
					UFDouble yesmny=new UFDouble(nvo.getBalance().doubleValue(),2);
					UFDouble wcbl=new UFDouble(0,2);
					if(yssmny.compareTo(new UFDouble(0))!=0){
						wcbl=new UFDouble(zxsmny.div(yssmny).multiply(100).doubleValue(),2);
					}
					String str="预算数:"+yssmny+"  实际数:"+zxsmny+"  剩余:"+yesmny+"  完成比:"+wcbl+"%";
					bxvo.getParentVO().setAttributeValue("zyx7", str);
					
					String sql="";
					String sql2="";
					if(bxvo.getParentVO().getAttributeValue("djdl").equals("jk")){
						
						sql="update er_jkzb b set b.zyx7='"+str+"'" +
						//",b.ts='"+bxvo.getParentVO().getAttributeValue("ts")+"'" +
						" where b.pk_jkbx='"+bxvo.getParentVO().getAttributeValue("pk_jkbx")+"'";
				
						sql2="select ts from er_jkzb b where b.pk_jkbx='"+bxvo.getParentVO().getAttributeValue("pk_jkbx")+"'";
						
					}else if(bxvo.getParentVO().getAttributeValue("djdl").equals("bx")){
						sql="update er_bxzb b set b.zyx7='"+str+"'" +
						//",b.ts='"+bxvo.getParentVO().getAttributeValue("ts")+"'" +
						" where b.pk_jkbx='"+bxvo.getParentVO().getAttributeValue("pk_jkbx")+"'";
				
						sql2="select ts from er_bxzb b where b.pk_jkbx='"+bxvo.getParentVO().getAttributeValue("pk_jkbx")+"'";
					}
					
					IUpdateData ida=NCLocator.getInstance().lookup(IUpdateData.class);
					
					ida.doUpdateCmpData(sql);
					
					IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
					String ts=(String)iQ.executeQuery(sql2, new ColumnProcessor());
					
					bxvo.getParentVO().setAttributeValue("ts", new UFDateTime(ts));
				}
				
			}
		} catch (Exception e) {
			Debug.debug("没有预算执行情况！");
		}
		
		return bxvo;
	}
	
	private FiBillAccessableBusiVOProxy getFiBillAccessableBusiVOProxy(FiBillAccessableBusiVO vo) {
		FiBillAccessableBusiVOProxy voProxy;
		voProxy = new FiBillAccessableBusiVOProxy(vo, BXConstans.ERM_PRODUCT_CODE_Lower);
		voProxy.setLinkQuery(true);
		return voProxy;
	}

}