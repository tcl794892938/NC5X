package nc.ui.arap.actions.edit;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.ep.dj.CMPDJZBDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.arap.proxy.Proxy;
import nc.itf.cmp.IUpdateData;
import nc.itf.cmp.pub.ICMPBillPublic;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.arap.global.ResMessage;
import nc.vo.cmpbill.outer.CmpBillAccessableBusiVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJ_IAccessableBusiVO;
import nc.vo.fibill.outer.FiBillAccessableBusiVO;
import nc.vo.fibill.outer.FiBillAccessableBusiVOProxy;
import nc.vo.logging.Debug;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IWorkFlowStatus;

import org.apache.commons.lang.StringUtils;

public class SaveActionNor extends AbstractSaveAction {

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlDjEdit#doBsSaveAction(nc.vo.ep.dj.DJZBVO)
	 */
	protected DJZBVO doBsSaveAction(DJZBVO djzbvo) throws Exception {
		
		//设置单据主表信息by tcl 2016-11-02
		//		DJZBVO dj = null;
		//		dj=(DJZBVO)new FlowEngine().startService("ArapSaveDj", djzbvo);
		//		return dj;
		String serverTime = nc.ui.pub.ClientEnvironment.getServerTime().toString();
		String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate() + serverTime.substring(10);

		//update by songtao for ftspay 20080723
		if (((AbstractRuntime) getActionRunntimeV0()).getWorkflowlinkData() == null) {
			//内存泄漏： 由于新增保存 BillVOConvertAction.getConvertedBillVO() ： djzbvo.setm_OldVO(getDataBuffer().getCurrentDJZBVO());
//			djzbvo.setm_OldVO(null);
			Object result= nc.ui.pub.pf.PfUtilClient.processAction("SAVE", ((DJZBHeaderVO) djzbvo
					.getParentVO()).getDjlxbm(), currTime, djzbvo);
			ArrayList a1 = null;
			if (result instanceof ArrayList) {
				a1=(ArrayList)result;
			}else if (result instanceof ResMessage) {
				if ("sj".equals(((DJZBHeaderVO) djzbvo.getParentVO()).getDjdl())||"fj".equals(((DJZBHeaderVO) djzbvo
					.getParentVO()).getDjdl())||"hj".equals(((DJZBHeaderVO) djzbvo.getParentVO()).getDjdl())) {
					a1=new ArrayList();
					DJZBVO vo=((ResMessage)result).djzbvo;
					a1.add(vo.getParentVO().getPrimaryKey());
					ICMPBillPublic iCMPBillPublic=NCLocator.getInstance().lookup(ICMPBillPublic.class);
					vo=iCMPBillPublic.findArapBillByPK(vo.getParentVO().getPrimaryKey());
					a1.add(vo);
				}
			}
			
			// /long end1 = System.currentTimeMillis();
			// /nc.bs.logging.Log.getInstance(this.getClass()).warn("调用后台用时:"+(end1-end));
			
			if (a1 == null) {
				return null;
			} else {
				nc.vo.ep.dj.DJZBVO djzb=(nc.vo.ep.dj.DJZBVO) a1.get(1);
				djzb=this.getYSBuget(djzb,true);
				//return (nc.vo.ep.dj.DJZBVO) a1.get(1);
				getBillCardPanel().setBillValueVO(djzb);
				return djzb;
			}
		} else {
			((AbstractRuntime) getActionRunntimeV0()).getWorkflowlinkData().setDestBillvo(djzbvo);
			return djzbvo;
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.ep.dj.controller.ARAPDjCtlDjEdit#doBsEditAction(nc.vo.ep.dj.DJZBVO)
	 */
	protected DJZBVO doBsEditAction(DJZBVO djzbvo) throws Exception {
		DJZBHeaderVO headvo = (DJZBHeaderVO) djzbvo.getParentVO();
		String serverTime = nc.ui.pub.ClientEnvironment.getServerTime().toString();
		String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate() + serverTime.substring(10);
		headvo.setShr(null);
		headvo.setShrq(null);
		headvo.setSpzt(null);
		if (djzbvo.m_isQr) {
			ArrayList a1 = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", headvo.getDjlxbm(), currTime,
					djzbvo);
			//防止内存泄漏
			DJZBVO vo = (nc.vo.ep.dj.DJZBVO) a1.get(1);
			if(vo!=null){
				vo.setm_OldVO(null);
			}
			return (nc.vo.ep.dj.DJZBVO) a1.get(1);
		} else {
			int status = Proxy.getIPFWorkflowQry().queryWorkflowStatus(headvo.getXslxbm(), headvo.getDjlxbm(),
					headvo.getVouchid());
			if (status == IWorkFlowStatus.WORKFLOW_ON_PROCESS
					|| (status == IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW && !headvo.getLrr().equals(
							this.getDjSettingParam().getPk_user()))) {
				nc.vo.ep.dj.DJZBVO vo = (nc.vo.ep.dj.DJZBVO) nc.ui.pub.pf.PfUtilClient.processActionNoSendMessage(null, "EDIT", headvo
						.getDjlxbm(), currTime, djzbvo, null, null, null);
				vo=this.getYSBuget(vo,false);
				getBillCardPanel().setBillValueVO(vo);
				//防止内存泄漏
				vo.setm_OldVO(null);
				return vo;
			} else{
				Object result = nc.ui.pub.pf.PfUtilClient.processAction("EDIT", headvo.getDjlxbm(),currTime, djzbvo);
				if(result instanceof ArrayList){
					nc.vo.ep.dj.DJZBVO vo = (nc.vo.ep.dj.DJZBVO)((ArrayList) result).get(1);
					//防止内存泄漏
					if(vo!=null){
						vo.setm_OldVO(null);
					}
					return vo;
				}else if (result instanceof ResMessage) {
					if ("sj".equals(((DJZBHeaderVO) djzbvo.getParentVO()).getDjdl())||"fj".equals(((DJZBHeaderVO) djzbvo
							.getParentVO()).getDjdl())||"hj".equals(((DJZBHeaderVO) djzbvo.getParentVO()).getDjdl())) {
							DJZBVO vo=((ResMessage)result).djzbvo;
							ICMPBillPublic iCMPBillPublic=NCLocator.getInstance().lookup(ICMPBillPublic.class);
							vo=iCMPBillPublic.findArapBillByPK(vo.getParentVO().getPrimaryKey());
							//防止内存泄漏
							if(vo!=null){
								vo.setm_OldVO(null);
							}
							return vo;
					}
				}
				DJZBVO vo = result == null ?  null:(nc.vo.ep.dj.DJZBVO)result;
				//刷新旧VO
				if(null!=vo&&null!=vo.header&&!StringUtils.isEmpty(vo.header.getVouchid())){
					vo=this.getYSBuget(vo,false);
					getDataBuffer().refreshDJZBVO(vo.header.getVouchid(), vo);
					//getDataBuffer().
					getBillCardPanel().setBillValueVO(vo);
				}
				//防止内存泄漏
				if(null!=vo){
					vo.setm_OldVO(null);
				}
				return vo ;
			}
			
		}
	}
	
	/**
	 * @param selected
	 * @param djzb
	 */
	private FiBillAccessableBusiVOProxy getFiBillAccessableBusiVOProxy(DJZBItemVO selectedItem, DJZBHeaderVO head) {
		FiBillAccessableBusiVOProxy voProxy;
		FiBillAccessableBusiVO vo;
		String isyscode = null;
		if("ss".equals(head.getDjdl()) || "yt".equals(head.getDjdl())
				|| "sj".equals(head.getDjdl()) || "fj".equals(head.getDjdl()) || "hj".equals(head.getDjdl()))
		{
			vo = new CmpBillAccessableBusiVO();
			((CmpBillAccessableBusiVO)vo).setHead(head);
			((CmpBillAccessableBusiVO)vo).setItem(selectedItem);
			if ("ss".equals(head.getDjdl()))
				isyscode = "ss";
			else if ("yt".equals(head.getDjdl()))
				isyscode = "yt";
			else if ("sj".equals(head.getDjdl()) || "fj".equals(head.getDjdl()) || "hj".equals(head.getDjdl()))
				isyscode = "cmp";
		}
		else {
			vo = new DJ_IAccessableBusiVO();
			((DJ_IAccessableBusiVO)vo).setHead(head);
			((DJ_IAccessableBusiVO)vo).setItem(selectedItem);
			isyscode = "arap";
		}
		voProxy = new FiBillAccessableBusiVOProxy(vo, isyscode);
		voProxy.setLinkQuery(true);
		return voProxy;
	}
	
	//2017-04-06测试 by tcl
	/*private void checkData(DJZBVO vo)throws Exception{
		FiBillAccessableBusiVOProxy voProxy = null;
		DJZBItemVO ivo=new DJZBItemVO();
		DJZBHeaderVO hvo=new DJZBHeaderVO();
		hvo.setDjdl("fj");
		hvo.setDwbm("1024");
		hvo.setZyx1("0001A110000000000LM4");
		hvo.setDjlxbm("F5-01");
		hvo.setDjrq(new UFDate("2017-01-03"));
		ivo.setSzxmid("0001MM1000000001FV3L");
		voProxy = getFiBillAccessableBusiVOProxy(ivo, hvo);
		nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(
				new nc.vo.ntb.outer.IAccessableBusiVO[] { voProxy });
		System.out.println(vos);
	}*/
	
	
//	刷新前取数2016-11-07 by tcl
	protected DJZBVO getYSBuget(DJZBVO vo,boolean add) throws Exception{
		
		//this.checkData(vo);
		
		FiBillAccessableBusiVOProxy voProxy = null;
		//int row = getBillCardPanel().getBillTable().getSelectedRow();
		try {
			voProxy = getFiBillAccessableBusiVOProxy((DJZBItemVO) vo.getChildrenVO()[0], (DJZBHeaderVO) vo.getParentVO());
			
			nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(
					new nc.vo.ntb.outer.IAccessableBusiVO[] { voProxy });
			if(vos==null||vos.length<=0){
				Debug.debug("没有定义控制方案！");
				return vo;
			}
			for(NtbParamVO nvo:vos){
				
				if(nvo.getPlanname().startsWith("零预算")){
					Debug.debug("没有定义控制方案！");
					return vo;
				}
				
				String [] attrs=nvo.getBusiAttrs();
				HashMap<String, String> map=new HashMap<String, String>();
				for(String attr:attrs){
					map.put(attr, attr);
				}
				/*if(attrs.length==1&&attrs[0].equals("zb.dwbm")){//仅公司预算
					UFDouble yssmny=new UFDouble(nvo.getPlanData().doubleValue(),2);
					UFDouble zxsmny=new UFDouble(nvo.getRundata()[0].doubleValue(),2);
					UFDouble yesmny=new UFDouble(nvo.getBalance().doubleValue(),2);
					UFDouble wcbl=new UFDouble(0,2);
					if(yssmny.compareTo(new UFDouble(0))!=0){
						wcbl=new UFDouble(zxsmny.div(yssmny).multiply(100).doubleValue(),2);
					}
					String str="预算数:"+yssmny+"  实际数:"+zxsmny+"  剩余:"+yesmny+"  完成比:"+wcbl+"%";
					vo.getParentVO().setAttributeValue("zyx8", str);
					
					String sql="";
					if(add){
						sql="update cmp_busibill b set b.zyx8='"+str+"'" +
						//",b.ts='"+vo.getParentVO().getAttributeValue("ts")+"'" +
						" where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
					}
					String sql="update cmp_busibill b set b.zyx8='"+str+"'" +
							//",b.ts='"+vo.getParentVO().getAttributeValue("ts")+"'" +
							" where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
					
					IUpdateData ida=NCLocator.getInstance().lookup(IUpdateData.class);
					
					ida.doUpdateCmpData(sql);
					
				}*/
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
					vo.getParentVO().setAttributeValue("zyx7", str);
					
					String sql="update cmp_busibill b set b.zyx7='"+str+"'" +
							//",b.ts='"+vo.getParentVO().getAttributeValue("ts")+"'" +
							" where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
					
					String sql2="select ts from cmp_busibill b where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
					IUpdateData ida=NCLocator.getInstance().lookup(IUpdateData.class);
					
					ida.doUpdateCmpData(sql);
					
					IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
					String ts=(String)iQ.executeQuery(sql2, new ColumnProcessor());
					
					vo.getParentVO().setAttributeValue("ts", new UFDateTime(ts));
					
				}
				
			}
			
			
		} catch (Exception e) {
			Debug.debug("没有预算执行情况！");
		}
		
		return vo;
	}
	
}
