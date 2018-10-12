package nc.ui.arap.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.cmp.utils.StringUtil;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.prv.IArapTBBillPrivate;
import nc.itf.cmp.IUpdateData;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.arap.actions.check.CheckAuditVO;
import nc.ui.arap.actions.check.CheckBillStatus;
import nc.ui.arap.actions.check.CheckCurrentMonth;
import nc.ui.arap.actions.search.MapKeyGather;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.arap.pubdj.AdjustVoAfterQuery;
import nc.ui.arap.pubdj.ArapDjBillCardPanel;
import nc.ui.cmp.pub.AccountExceptionHandler;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.ShenheLog;
import nc.ui.ep.dj.SqYz;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.util.VOCompress;
import nc.vo.cmp.exception.CmpAuthorizationException;
import nc.vo.cmpbill.outer.BugetAlarmBusinessException;
import nc.vo.cmpbill.outer.CmpFpBusinessException;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.DJZBVOUtility;
import nc.vo.ep.dj.ShenheException;
import nc.vo.fibill.outer.FiBillAccessableBusiVOProxy;
import nc.vo.logging.Debug;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IPfRetBackCheckInfo;

public class AuditAction extends DefaultAction {
	private ARAPDjSettingParam arapDjSettingParam = null;

	private ArapDjBillCardPanel arapDjBillCardPanel = null;

	private BillModel billModel = null;


	/**
	 * ��� �������ڣ�(2001-5-22 9:12:31)
	 *
	 * @return boolean ����:
	 * @throws BusinessException
	 */
	public DJZBVO[] audit() throws BusinessException {
		DJZBVO[] result=null;
		if (this.getActionRunntimeV0().getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) // �б�
		{
			result = shenhe_LB();
		} else // ����
		{
			result = shenhe_Dj();

		}
		return result;
	}

	// 1.updateViewAfterVerifyCardMode ��Ϊpublic ,��ʾΪһ��action 2 ����ֵΪDJZBVO 3.������Ч��action����ֵҲΪDJZBVO
	public DJZBVO[] shenhe_Dj() throws BusinessException {
		// �����Ϣ
		String strResMessage = "";

		int ShenheCount = 0; // �Ѿ���˳ɹ��ĵ���

		// vo =
		// this.getDjDataBuffer().getDJZBVO(getArapDjPanel1().getM_Vouchid());
		nc.vo.ep.dj.DJZBVO vo = getDataBuffer().getCurrentDJZBVO();
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) (vo.getParentVO());
        nc.vo.ep.dj.DJZBHeaderVO oldhead =(nc.vo.ep.dj.DJZBHeaderVO)head.clone();
       
		try {
			this.supplementInfoBeforeVerification(vo, true);
		} catch (Exception e) {
			getParent().showHintMessage(e.getMessage());
			Log.getInstance(this.getClass()).debug(e, this.getClass(), e.getMessage());
			throw new BusinessException(e.getCause()==null?e.getMessage():e.getCause().getMessage());

		}
		ResMessage resmessage = CheckAuditVO.checkShenhe(vo, getArapDjPanel().getDjSettingParam(), getDataBuffer()
				.getCurrentDjdl());
		if (resmessage.isSuccess == false) {
            head.setShrq(oldhead.getShrq());
            //�����
            head.setShr(oldhead.getShr());

			strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000117", null,
					new String[] { head.getDjbh() })
					+ "\n" + resmessage.strMessage;
			getParent().showHintMessage(strResMessage);
			Log.getInstance(this.getClass()).debug(strResMessage);
			throw new BusinessException(strResMessage);

		}
		vo = setBudgetCheckToTrue(vo);
		resmessage = shenheByDjvo(vo); // ��˵���vo
		if (resmessage == null) {
			resmessage = new ResMessage();
			resmessage.isSuccess = true;
			resmessage.djzbvo=vo;
			resmessage.intValue = ResMessage.$SHENHE_SUCCESS;
		}
		vo=resmessage.djzbvo;
		if (((DJZBHeaderVO)vo.getParentVO()).getBzbm()==null && vo.getChildren() != null && vo.getChildren().length > 0) {
	        DJZBItemVO item=((DJZBItemVO[])vo.getChildrenVO())[0];
	        ((DJZBHeaderVO)vo.getParentVO()).setBzbm(item.getBzbm());
		}
		getDataBuffer().setCurrentDJZBVO(vo);
		//��ոü�ֵ,�������ֵ
		if (null != ((AbstractRuntime) this.getParent()).getAttribute(MapKeyGather.auditAction_ResMessages)) {
			((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages, null);
		}
		((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages,
				new ResMessage[] { resmessage });
		if (resmessage.intValue == ResMessage.$SHENHE_SUCCESS) // ��˳ɹ�
		{
            this.supplementInfoAfterVerify(vo, resmessage, 1,oldhead);
            
        	//ˢ��djzjVO�������� ��  ����ڴ������ ������ʧ�ܡ�����ˢ���ˣ���̨û�в����µ�DJZBVO
			if(null!= resmessage.djzbvo&& null!= resmessage.djzbvo.header){
				getDataBuffer().refreshDJZBVO(resmessage.djzbvo.header.getPrimaryKey(), resmessage.djzbvo);
			}
			ShenheCount++;

		} else //ʧ��
		{
            this.supplementInfoAfterVerify(vo, null, 2,oldhead);
			strResMessage = resmessage.strMessage;
		}

		if (ShenheCount < 1) {
			Log.getInstance(this.getClass()).debug(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000118")/*
					 * @res
					 * "����û����˵���,"
					 */
							+ strResMessage);
			getParent().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000119")/*
			 * @res
			 * "������ϣ�����û����˵���"
			 */);
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000118")/*
			 * @res "����û����˵���,"
			 */
					+ strResMessage);

		}

		//		// ���ع�ʽ
		//		try {
		//			getBillListPanel().getHeadBillModel().execFormulas(
		//					new String[] { "shrmc->getColvalue(sm_user,user_name,cUserId,shr)" });
		//		} catch (Exception e) {
		//			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
		//																											 * @res
		//																											 * "���ع�ʽ����:"
		//																											 */
		//					+ e);
		//		}

		// ((AbstractRuntime)getParent()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000120")/*@res
		// "������,"*/ + resmessage.strMessage);
		if (resmessage.strMessage != null && resmessage.strMessage.trim().length() > 0) {
			//TODO_liaobx
			if(resmessage.isSuccess == false){
				MessageDialog.showHintDlg((getRunEnvironment()), nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000121")/* @res "����" */, resmessage.strMessage);
			}

		}
		
		//ˢ�¾�VO
		if(null!=vo&&null!=vo.header&&!StringUtils.isEmpty(vo.header.getVouchid())){
			if(vo.header.getSpzt()!=null&&vo.header.getSpzt().equals("1")){
				vo=this.setPrintSPInfomation(vo);
			}
			getDataBuffer().refreshDJZBVO(vo.header.getVouchid(), vo);
			//getBillCardPanel().setBillValueVO(vo);
		}
		// this.m_parent.endPressBtn(bo);
		//��ֹ�ڴ�й©
		if(null!=vo){
			vo.setm_OldVO(null);
		}
		return new DJZBVO[]{vo};
		
	}
	
//	�������������2016-12-08 by tcl
	protected DJZBVO setPrintSPInfomation(DJZBVO vo) throws BusinessException{
		
		try {
			IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String sqlsp="select * from pub_workflownote e where e.approvestatus=1 and e.billid='"+vo.header.getPrimaryKey()+"' order by ts ";
			List<Map<String, Object>> maplist=(List<Map<String, Object>>)iQ.executeQuery(sqlsp, new MapListProcessor());
			if(maplist==null||maplist.size()<=0){
				Debug.debug("û��������Ϣ��");
			}
			Object obj1=null;//��һ���������
			Object obj2=null;
			Object obj3=null;//���ʱ��
			Object obj4=null;
			Object obj5=null;//�������
			Object obj6=null;
			Object obj7=maplist.get(maplist.size()-1).get("checknote");
			for(int i=0;i<maplist.size();i++){
				
				if(i==0){
					Object objt=maplist.get(i).get("dealdate");
					Object objr=maplist.get(i).get("checknote");
					Object obju=maplist.get(i).get("checkman");
					obj1=obju==null?null:obju.toString();
					obj3=objt==null?null:objt.toString();
					obj5=objr==null?null:objr.toString();
				}
				
				if(i==1){
					Object obj=maplist.get(i).get("checkman");
					obj2=obj==null?null:obj.toString();
					
					Object objt=maplist.get(i).get("dealdate");
					Object objr=maplist.get(i).get("checknote");
					obj4=objt==null?null:objt.toString();
					obj6=objr==null?null:objr.toString();
					//��ѯ
					String sqlqx="select count(1) from sm_user r left join sm_user_role t on t.cuserid=r.cuserid " +
						"left join sm_role l on l.pk_role=t.pk_role where r.cuserid='"+obj2+"' " +
						"and t.pk_corp='"+vo.header.getDwbm()+"' and l.role_code in('14','15')";
					Integer it=(Integer)iQ.executeQuery(sqlqx, new ColumnProcessor());
					if(it<=0){
						obj2=null;
					}
				}
			}
			String sql111="";
			String sql222="";
			String sql333="";
			if(obj7==null||"".equals(obj7)){
				sql333=" b.zyx14=null,";
			}else{
				sql333=" b.zyx14='"+obj7+"',";
			}
			if(obj1==null||"".equals(obj1)){
				sql111=" b.zyx19=null,b.zyx17=null,b.zyx15=null,";
			}else{
				sql111=" b.zyx19='"+obj1+"',b.zyx17='"+obj3+"',b.zyx15='"+obj5+"',";
			}
			if(obj2==null||"".equals(obj2)){
				sql222=" b.zyx20=null,b.zyx18=null,b.zyx16=null";
			}else{
				sql222=" b.zyx20='"+obj2+"',b.zyx18='"+obj4+"',b.zyx16='"+obj6+"' ";
			}
			
			String sql="update cmp_busibill b set "+sql333+sql111+sql222+
			//",b.ts='"+vo.getParentVO().getAttributeValue("ts")+"'" +
			" where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
	
			String sql2="select ts from cmp_busibill b where b.vouchid='"+vo.getParentVO().getAttributeValue("vouchid")+"'";
			IUpdateData ida=NCLocator.getInstance().lookup(IUpdateData.class);
	
			ida.doUpdateCmpData(sql);
	
			String ts=(String)iQ.executeQuery(sql2, new ColumnProcessor());
	
			vo.getParentVO().setAttributeValue("zyx14", obj7);
			vo.getParentVO().setAttributeValue("zyx15", obj5);
			vo.getParentVO().setAttributeValue("zyx16", obj6);
			vo.getParentVO().setAttributeValue("zyx17", obj3);
			vo.getParentVO().setAttributeValue("zyx18", obj4);
			vo.getParentVO().setAttributeValue("zyx19", obj1);
			vo.getParentVO().setAttributeValue("zyx20", obj2);
			vo.getParentVO().setAttributeValue("ts", new UFDateTime(ts));
				
		} catch (Exception e) {
			Debug.debug("û��Ԥ��ִ�������");
		}
		
		return vo;
	}

	private DJZBVO supplementInfoBeforeVerification(DJZBVO vo, boolean bSupItems) throws Exception {
		arapDjSettingParam = getDjSettingParam();
		DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
		DJZBItemVO[] items = (DJZBItemVO[]) vo.getChildrenVO();
		if ((bSupItems == true) && (items == null || items.length == 0)) {
			// ��ѯ���ݱ���
			try {
				if ("yt".equals(head.getDjdl()))
					items = getIArapTBBillPrivate().querTbItemsByHPK(head.getPrimaryKey());
				else if ("ss".equals(head.getDjdl()))
					items = nc.impl.arap.proxy.Proxy.getIArapItemPrivate().querySsItems(head.getPrimaryKey());
				else
					items = ((AbstractRuntime)this.getActionRunntimeV0()).getProxy().getIArapBillPrivate().queryDjzbitems(head.getPrimaryKey());
			} catch (Exception e) {
				Log.getInstance(this.getClass()).debug(e, this.getClass(), e.getMessage());
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000148")/*
				 * @res
				 * "���鵥�ݱ���ʧ��,���ʧ��"
				 */);
			}
			vo.setChildrenVO(items);
		}

		// �������
		head.setShrq(arapDjSettingParam.getLoginDate());
		// ������
		head.setShkjnd(arapDjSettingParam.getAccountYear());
		// ����ڼ�
		head.setShkjqj(arapDjSettingParam.getAccountMonth());
		// �����
		head.setShr(arapDjSettingParam.getPk_user());
		// head.m_CTIsUsed = arapDjSettingParam.getCTIsUsed().booleanValue();
		// head.m_POIsUsed = arapDjSettingParam.getPOIsUsed().booleanValue();

		// ����Эͬ���Ʊ�־
		if (head.getPzglh().intValue() == 0 || head.getPzglh().intValue() == 1 || head.getPzglh().intValue() == 2) {
			ResMessage res_xt = nc.ui.arap.global.PubData.getXtflag(head.getDwbm(), head.getPzglh().intValue());
			if (res_xt == null || !res_xt.isSuccess) {

				Logger.debug(res_xt.strMessage);
				// return false;
			}
			head.setXtflag(res_xt.strMessage);
		}
		if (head.getPzglh().intValue() == 1)
			head.m_IsContract = arapDjSettingParam.getIsContract().booleanValue();

		head.setSsflag(arapDjSettingParam.getSSflag());
		// �����������ײ����Ƿ����Ԥ�����
		// ���µ������Ϳ���Ԥ��
		if (head.getDjdl().equals("ys") || head.getDjdl().equals("yf") || head.getDjdl().equals("sk")
				|| head.getDjdl().equals("fk") || head.getDjdl().equals("sj") || head.getDjdl().equals("fj")
				|| head.getDjdl().equals("hj") || head.getDjdl().equals("ss")) {
			if (arapDjSettingParam.getHyflag() == null) {
				Log.getInstance(this.getClass()).debug(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000127"));
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000127")/*
				 * @res
				 * "\nȡԤ����Ƴ���"
				 */);
			}
			head.setHyflag(arapDjSettingParam.getHyflag());
		}

//		String strPKCorp = head.getDwbm();
//		// get the bill type pk
//		String strDjlxoid = head.getYwbm();
		return vo;
	}

	private ResMessage shenheByDjvo(DJZBVO djzbvo) throws BusinessException {
		// ���ݱ�ͷ
		nc.vo.ep.dj.DJZBHeaderVO head = (DJZBHeaderVO) djzbvo.getParentVO();
		if ("yt".equals(head.getDjdl())) {
			return shenheByDjvo_TB(djzbvo);
		}
		// ��˽�����Ϣ
		ResMessage resmessage = new ResMessage();
		resmessage.djzbvo=djzbvo;
		resmessage.isSuccess = true;
		// �����������ײ����Ƿ����Ԥ�����
		// String serverTime = nc.ui.pub.ClientEnvironment.getServerTime().toString();
		String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();// +
		// serverTime.substring(10);
		DJZBItemVO[] items=(DJZBItemVO[])djzbvo.getChildrenVO();
		VOCompress.objectReference(djzbvo);
		djzbvo.setSettlementAccountList(null);
		
		while (true) {
			try {
				// Object reObje = nc.ui.pub.pf.PfUtilClient.processActionFlow((getRunEnvironment()),
				// 		"APPROVE", head.getDjlxbm(), currTime, djzbvo, null);
				HashMap hmPfExParams = new HashMap();
				hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
				DJZBVO transferVO=new DJZBVO();
				transferVO.setParentVO(head);
				// ADD BY Lianggr 2010-10-22 BEGIN
				transferVO.budgetCheck = djzbvo.budgetCheck;
				// Ԥ�����У����Ҫ����VO
				transferVO.setChildren(djzbvo.getChildren());
				// ADD BY Lianggr 2010-10-22 END
				List<Object> userObjlist= new ArrayList<Object>();
				userObjlist.add(djzbvo.hasZjjhCheck);
				userObjlist.add(djzbvo.budgetCheck);
				userObjlist.add(djzbvo.getSettlementAccountList());
				userObjlist.add(djzbvo.getParentVO().getAttributeValue("ts"));
				String actionName="APPROVE"+ClientEnvironment.getInstance().getUser().getPrimaryKey();
				Object reObje = nc.ui.pub.pf.PfUtilClient.runAction((getRunEnvironment()), actionName, head.getDjlxbm(), currTime, transferVO, userObjlist, null, null,  hmPfExParams);
				
				if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {
					resmessage.isSuccess = false;
					resmessage.intValue = -123456;
					resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPP2006030102-000129")/*
					 * @res "��������ʧ��"
					 */;
				} else {
//					Object obj=(ResMessage)reObje;
					if (reObje instanceof Object[]) {
						if (((Object[])reObje)[0] instanceof ResMessage) {
							resmessage = (ResMessage) ((Object[])reObje)[0];
							if (resmessage.djzbvo != null){
								resmessage.strMessage = resmessage.strMessage + resmessage.djzbvo.m_Resmessage.strMessage;
								resmessage.djzbvo.setChildrenVO(items);
							}
						} 
					}
					else {
						if (reObje instanceof ResMessage) {
							resmessage=(ResMessage)reObje;
							resmessage.djzbvo.setChildren(items);
							resmessage.m_Ts = ((nc.vo.ep.dj.DJZBHeaderVO) (resmessage.djzbvo.getParentVO())).getts()
							.toString();
						}
						resmessage.intValue = ResMessage.$SHENHE_SUCCESS;
						if(reObje instanceof List && ((List)reObje).size() > 0){
							resmessage.djzbvo = ((ResMessage)((List)reObje).get(0)).djzbvo;
							resmessage.djzbvo.setChildrenVO(items);
							resmessage.m_Ts = ((nc.vo.ep.dj.DJZBHeaderVO) (resmessage.djzbvo.getParentVO())).getts()
									.toString();
						}
						if (reObje instanceof nc.vo.ep.dj.DJZBVO) {
							resmessage.djzbvo = (nc.vo.ep.dj.DJZBVO) reObje;
							resmessage.djzbvo.setChildrenVO(items);
							resmessage.m_Ts = ((nc.vo.ep.dj.DJZBHeaderVO) (resmessage.djzbvo.getParentVO())).getts()
									.toString();
						} else if (reObje instanceof nc.bs.pub.compiler.IWorkFlowRet) {
							resmessage.djzbvo = (nc.vo.ep.dj.DJZBVO) (((nc.bs.pub.compiler.IWorkFlowRet) reObje).m_inVo);
							resmessage.djzbvo.setChildrenVO(items);
							resmessage.m_Ts = ((nc.vo.ep.dj.DJZBHeaderVO) (resmessage.djzbvo.getParentVO())).getts()
									.toString();
						}
					}
				}
				break;
			} catch (Exception e) {
				Logger.debug(e.getMessage());
				if (e instanceof ArapBusinessException) // Զ���쳣Ϊ�����Ҫ��Ȩ�쳣
				{
					ArapBusinessException se = (ArapBusinessException) e;
					return se.m_ResMessage;

				} else if (e instanceof ShenheException) {
					ShenheException se = (ShenheException) e;
					return se.m_ResMessage;

				} else if (e instanceof CmpAuthorizationException) {
					AccountExceptionHandler handler = new AccountExceptionHandler(this.getParent());
					boolean ispass = handler.handleException((CmpAuthorizationException)e);
					if (ispass == false) {
						if (resmessage.strMessage!=null) {
							resmessage.strMessage = resmessage.strMessage + "\n";
						}
						if (null!=e.getMessage()) {
							resmessage.strMessage+=e.getMessage();
						}else {
							resmessage.strMessage+=nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000213")/*@res " �˻������޶�!"*/;
						}
						resmessage.isSuccess = false;
						resmessage.intValue = ResMessage.$SYSERROR;
						break;
					}else {
						//��������Ҫ���������˻���Ϣд��vo
						List<String> authList = handler.getAccountList();
						djzbvo.setSettlementAccountList(authList);
					}
				} else if (e instanceof java.rmi.RemoteException) // Զ���쳣
				{
					java.rmi.RemoteException re = (java.rmi.RemoteException) e;
					if ((re.detail) instanceof ArapBusinessException) // Զ���쳣Ϊ�����Ҫ��Ȩ�쳣
					{
						ArapBusinessException se = (ArapBusinessException) re.detail;
						return se.m_ResMessage;

					} else if ((re.detail) instanceof ShenheException) {
						ShenheException se = (ShenheException) re.detail;
						return se.m_ResMessage;

					} else {
						resmessage.strMessage = re.detail.getMessage();
						resmessage.isSuccess = false;
						return resmessage;
					}
				}else {
					if (e.getCause()!=null && e.getCause() instanceof BugetAlarmBusinessException) {
						if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000214")/*@res " �Ƿ����������"*/)
								== MessageDialog.ID_YES)
						{
							djzbvo.budgetCheck = Boolean.FALSE;  //����飬ǿдԤ��
							return shenheByDjvo(djzbvo);
						}
					}
					else if(e.getCause()!=null&&e.getCause() instanceof CmpFpBusinessException)  //�ʽ�ƻ���ʾ����
					{
						if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000214")/*@res " �Ƿ����������"*/)
								== MessageDialog.ID_YES)
						{
							djzbvo.hasZjjhCheck = Boolean.FALSE;  //����飬ǿд�ʽ�ƻ�
							return shenheByDjvo(djzbvo);
						}
					}
					Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000130")/*
							 * @res
							 * "��˵��ݳ���: "
							 */
									+ e);
					resmessage.strMessage = resmessage.strMessage + "\n" + (e.getCause()==null?e.getMessage():(e.getCause().getMessage()==null?e.getMessage():e.getCause().getMessage()));
					resmessage.isSuccess = false;
					resmessage.intValue = ResMessage.$SYSERROR;
					return resmessage;
				}

			}
		}
		return resmessage;

	}

	private ResMessage shenheByDjvo_TB(nc.vo.ep.dj.DJZBVO djzbvo) throws BusinessException {

		// ���ݱ�ͷ
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) djzbvo.getParentVO();

		// ��˽�����Ϣ
		ResMessage resmessage = new ResMessage();
		resmessage.isSuccess = true;

		// �������VO

		resmessage = checkShenhe(djzbvo);
		resmessage.djzbvo = djzbvo;  //û�����������ִ��this.supplementInfoAfterVerify(vo, resmessage, 1,oldhead);�׿�

		if (!resmessage.isSuccess) {
			getParent().showHintMessage(resmessage.strMessage);
			throw new BusinessException(resmessage.strMessage);

		}

		// �����������ײ����Ƿ����Ԥ�����
		ResMessage res_hycz = new ResMessage();
		res_hycz.isSuccess = false;

		// �����������ײ����Ƿ����Ԥ�����

		try {
			// �޸����ݿ�(���)
			new nc.ui.ep.dj.Arap_ProcAction().runClass(getParent(), head.getDjlxbm(), "APPROVE", djzbvo, null);
			resmessage = getIArapTBBillPrivate().auditTb(djzbvo);

		} catch (Exception e) {

			if (e instanceof ArapBusinessException) // Զ���쳣Ϊ�����Ҫ��Ȩ�쳣
			{
				ArapBusinessException se = (ArapBusinessException) e;
				return se.m_ResMessage;

			} else if (e instanceof java.rmi.RemoteException) // Զ���쳣
			{
				java.rmi.RemoteException re = (java.rmi.RemoteException) e;
				if ((re.detail) instanceof nc.vo.ep.dj.ShenheException) // Զ���쳣Ϊ�����Ҫ��Ȩ�쳣
				{
					nc.vo.ep.dj.ShenheException se = (nc.vo.ep.dj.ShenheException) re.detail;
					if (se.m_ResMessage.intValue != ResMessage.$REQUEST_SQ)
						return se.m_ResMessage;
					djzbvo = se.m_ResMessage.djzbvo;
					return sq(djzbvo, se.m_ResMessage, true); // ������Ȩ

				}
				if ((re.detail) instanceof ArapBusinessException) // Զ���쳣Ϊ�����Ҫ��Ȩ�쳣
				{
					ArapBusinessException se = (ArapBusinessException) re.detail;
					return se.m_ResMessage;

				} else {
					resmessage.strMessage = re.detail.getMessage();
					resmessage.isSuccess = false;
					return resmessage;
				}
			}
			else
				if (e.getCause()!=null && e.getCause() instanceof BugetAlarmBusinessException) {
					if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000214")/*@res " �Ƿ����������"*/)
							== MessageDialog.ID_YES)
					{
						djzbvo.budgetCheck = Boolean.FALSE;  //����飬ǿдԤ��
						return shenheByDjvo_TB(djzbvo);
					}
				}
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001043")/*
			 * @res
			 * "��˵��ݳ���: "
			 */
					+ e);
			resmessage.strMessage = resmessage.strMessage + "\n" + e.getCause()==null?e.getMessage():e.getCause().getMessage();
			resmessage.isSuccess = false;
			resmessage.intValue = ResMessage.$SYSERROR;
			return resmessage;
		}
		if (resmessage.isSuccess) {
			resmessage.strMessage = "";
		}
		resmessage.djzbvo = djzbvo;  //û�����������ִ��this.supplementInfoAfterVerify(vo, resmessage, 1,oldhead);�׿�

		return resmessage;

	}

    private DJZBVO supplementInfoAfterVerify(DJZBVO djzbvo, ResMessage res, int intSucceed,DJZBHeaderVO oldhead) {

		DJZBHeaderVO head = (DJZBHeaderVO) djzbvo.getParentVO();
		if ("yt".equalsIgnoreCase(head.getDjdl()) && intSucceed == 1) {
			head.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Verified));
			head.setSpzt(DJZBVOConsts.m_strStatusVerifying);
		} else {
			// if operation succeed
			if (intSucceed == 1) {
//				DJZBVO voFromServer = (DJZBVO) res.djzbvo;
				// �޸�ȫ�ֱ���еĵ���״̬
				int checkState = res.djzbvo.getCheckState();

				// String spzt = "����ͨ��";
				String spzt = DJZBVOConsts.m_strStatusVerifiedPass;

				if (checkState == nc.vo.pub.pf.IPfRetCheckInfo.PASSING) {
//					head.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Verified));
				}
				if (checkState == nc.vo.pub.pf.IPfRetCheckInfo.NOPASS) {
					// spzt = "����δͨ��";
					spzt = DJZBVOConsts.m_strStatusVerifiedNoPass;

				} else if (checkState == nc.vo.pub.pf.IPfRetCheckInfo.GOINGON) {
					// spzt = "������";
					spzt = DJZBVOConsts.m_strStatusVerifying;
				} else if (checkState == IPfRetBackCheckInfo.NOSTATE) {
					spzt = DJZBVOConsts.m_strStatusVerifiedNoPass;
					head.setShr(null);
					head.setShrq(null);
					return djzbvo;
				}
				if(!StringUtil.isEmpty(res.m_Ts)){
					head.setTs(new nc.vo.pub.lang.UFDateTime(res.m_Ts));
				}

				head.setSpzt(spzt);

//				if (head.getIsQr().equalsIgnoreCase("Y")) {
//					head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_NO));
//				} else {
//					if (!voFromServer.getCanBeCommissioned() || DJZBVOConsts.FromFTS.equals(head.getLybz())) {
//						head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_VALID));// ���
//						head.setSxr(head.getShr());
//						head.setSxkjnd(head.getShkjnd());
//						head.setSxkjqj(head.getShkjqj());
//						head.setSxrq(head.getShrq());
//					} else
//						// head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_NO));
//						head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_VALID_ARAP));
//				}

			} else if (intSucceed == 4) {
//				DJZBVO voFromServer = (DJZBVO) res.djzbvo;
				// �޸�ȫ�ֱ���еĵ���״̬
				// String spzt = "����ͨ��";
				if(!StringUtil.isEmpty(res.m_Ts)){
					head.setTs(new nc.vo.pub.lang.UFDateTime(res.m_Ts));
				}
				head.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Verified));
				head.setSpzt(DJZBVOConsts.m_strStatusVerifiedPass);
//				if (head.getIsQr().equalsIgnoreCase("Y")) {
//					head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_NO));
//				} else {
//					if (!voFromServer.getCanBeCommissioned() || DJZBVOConsts.FromFTS.equals(head.getLybz())) {
//						head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_VALID));// ���
//						head.setSxr(head.getShr());
//						head.setSxkjnd(head.getShkjnd());
//						head.setSxkjqj(head.getShkjqj());
//						head.setSxrq(head.getShrq());
//					} else
//						// head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_NO));
//						head.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_VALID_ARAP));
//				}

			} else if (intSucceed == 2) {
				head.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Saved));
				// �������
	            head.setShrq(oldhead.getShrq());
	            //������
	            head.setShkjnd(oldhead.getShkjnd());
	            //����ڼ�
	            head.setShkjqj(oldhead.getShkjqj());
	            //�����
	            head.setShr(oldhead.getShr());

			}
		}
		try {
			AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(djzbvo);
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}
		this.getDataBuffer().refreshDJZBVO(head.getVouchid(), djzbvo);
		return djzbvo;
	}

	//��˺���½���״̬ ��abstactRuntime��ȡresmessage
	public void updateViewAfterVerifyCardMode(DJZBVO[] djzbvos) throws BusinessException {
		
		arapDjBillCardPanel = getArapDjPanel().getBillCardPanelDj();
		DJZBHeaderVO head = (DJZBHeaderVO) djzbvos[0].getParentVO();
		ResMessage[] resmessage = (ResMessage[]) ((AbstractRuntime) this.getParent())
				.getAttribute(MapKeyGather.auditAction_ResMessages);
		// if operation succeed
		if (resmessage[0].intValue == ResMessage.$SHENHE_SUCCESS) {
			// �޸Ľ����ϵĵ���״̬
			arapDjBillCardPanel.setHeadItem("djzt", head.getDjzt());
			arapDjBillCardPanel.setTailItem("shr", head.getShr());
			arapDjBillCardPanel.setHeadItem("shrq", head.getShrq());
			arapDjBillCardPanel.setHeadItem("shkjnd", head.getShkjnd());
			arapDjBillCardPanel.setHeadItem("shkjqj", head.getShkjqj());

			arapDjBillCardPanel.setHeadItem("sxbz", head.getSxbz());// �޸Ľ���״̬,����flow��
			arapDjBillCardPanel.setHeadItem("sxbzmc", head.getAttributeValue("sxbzmc"));// �޸Ľ���״̬,����flow��
			arapDjBillCardPanel.setHeadItem("sxr", head.getSxr());
			arapDjBillCardPanel.setHeadItem("sxkjnd", head.getSxkjnd());
			arapDjBillCardPanel.setHeadItem("sxkjqj", head.getSxkjqj());
			arapDjBillCardPanel.setHeadItem("sxrq", head.getSxrq());

			arapDjBillCardPanel.setHeadItem("yhqrr", head.getYhqrr());// �޸Ľ���״̬,����flow��
			arapDjBillCardPanel.setHeadItem("yhqrrq", head.getYhqrrq());
			arapDjBillCardPanel.setHeadItem("yhqrkjnd", head.getYhqrkjnd());
			arapDjBillCardPanel.setHeadItem("yhqrkjqj", head.getYhqrkjqj());

			arapDjBillCardPanel.setHeadItem("ts", head.getts());
			// getArapDjPanel().getBillCardPanelDj().setHeadItem("zyx20",
			// head.getSpzt());
			arapDjBillCardPanel.setHeadItem("spzt", DJZBVOUtility.getUIStringVerifyingStatus(head.getSpzt()));
			if(null!=getBillListPanel()&&null!=getBillListPanel().getHeadBillModel()){
				billModel = getBillListPanel().getHeadBillModel();
				String strCurrentPK = getDataBuffer().getCurrentDJZBVOPK();
				int row = getBillListPanel().getHeadTable().getSelectedRow();
				if (row == -1) {
					row = (getRunEnvironment()).getHeadRowInexByPK(strCurrentPK);
				} else if (!strCurrentPK.equals(billModel.getValueAt(row, "vouchid"))) {
					for (int i = 0; i < billModel.getRowCount(); i++) {
						if (strCurrentPK.equals(billModel.getValueAt(row, "vouchid"))) {
							row = i;
							break;
						}
					}
				}
				// �б��еı�ͷ��Ϣ
				billModel.setValueAt(head.getDjzt(), row, "djzt");
				billModel.setValueAt(DJZBVOUtility.getDjztmc(head.getDjzt()), row, "djzt_mc");
	
				billModel.setValueAt(head.getShr(), row, "shr");
				billModel.setValueAt(head.getShrq(), row, "shrq");
				billModel.setValueAt(head.getAttributeValue("spzt"), row, "spzt");
				billModel.setValueAt(head.getts(), row, "ts");
				//���ع�ʽ
				try {
					getBillListPanel().getHeadBillModel().execFormulas(
							new String[] { "shrmc->getColvalue(sm_user,user_name,cUserId,shr)" });
				} catch (Exception e) {
					Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
					 * @res
					 * "���ع�ʽ����:"
					 */
							+ e);
				}
			}
			//TODO Test syn
			try{
				Log.getInstance(this.getClass()).error("************"+head.toString()+"****************");
				Log.getInstance(this.getClass()).error("************"+head.getts().toString()+"****************");
			}catch(Exception e){
			}
		}
//		getArapDjPanel().setDj(djzbvos[0]);
		
	}

	private ResMessage sq(nc.vo.ep.dj.DJZBVO djzbvo, ResMessage resmessage, boolean isShenhe) throws BusinessException {

		// ���ݱ���vo
		nc.vo.ep.dj.DJZBItemVO[] items = (nc.vo.ep.dj.DJZBItemVO[]) djzbvo.getChildrenVO();

		if (resmessage.intValue == 9999) // ��Ȩ
		{
			// ���¿�ʼ��֤����

			// ���¿�ʼ��֤����
			SqYz pd = new SqYz(getParent());
			pd.setM_pk_accid(items[resmessage.itemRow].getAccountid()); // �����˻�����
			pd.setM_AccidName(resmessage.accidname); // �����˻�����
			pd.showModal();

			if (pd.getM_Flag() == -1) {
				resmessage.strMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000131", null,
						new String[] { String.valueOf(resmessage.itemRow + 1) });
				resmessage.intValue = ResMessage.$CANCEL_SQ;
			} else if (pd.getM_Flag() == -99) {
				resmessage.strMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000132", null,
						new String[] { String.valueOf(resmessage.itemRow + 1) });
				resmessage.intValue = ResMessage.$SYSERROR;

			} else if (pd.getM_Flag() == 1 && pd.isM_IsPass()) // ��Ȩ��֤ͨ��
			{
				// ����(��)������ŵ���

				items[resmessage.itemRow].setIsSqed(ArapConstant.UFBOOLEAN_TRUE);
				djzbvo.setChildrenVO(items);

				if (isShenhe) { // ���
					resmessage = shenheByDjvo(djzbvo); // �ݹ�
				}
				// �ݹ�

			} else {
				resmessage.strMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000133", null,
						new String[] { String.valueOf(resmessage.itemRow + 1) });
				resmessage.intValue = ResMessage.$FALSE_SQ;
			}
			pd.destroy(); // del��Ȩ����

		}

		return resmessage;
	}

	private ResMessage checkShenhe(nc.vo.ep.dj.DJZBVO djzbvo) throws BusinessException {
		arapDjSettingParam = getDjSettingParam();
		// ���ݱ�ͷVO
		nc.vo.ep.dj.DJZBHeaderVO head = null;
		// nc.vo.ep.dj.DJZBHeaderVO head_hsb = null;

		head = (nc.vo.ep.dj.DJZBHeaderVO) (djzbvo.getParentVO());

		// ���鷵�ص���Ϣ
		ResMessage resmessage = new ResMessage();

		resmessage.isSuccess = true;

		if (!head.getDwbm().equals(arapDjSettingParam.getPk_corp())) {
			resmessage.isSuccess = false;
			resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000069")/*
			 * @res
			 * "ֻ�ܺ�̨����˱���λ�ĵ���"
			 */;
			return resmessage;
		}

		// if(true)
		// return resmessage;

		/***********************************************************************
		 * ���鱾���Ѿ����˲������**********************
		 */

		resmessage = CheckCurrentMonth.CheckCurrentMonthIsClosed(resmessage, djzbvo);
		/*********************************************************
		 */
		// ����״̬
		Integer oldDjzt = null;

		// ¼����
		String lrr = "";

		// ��������
		nc.vo.pub.lang.UFDate djrq = null;

		if (head.getSpzt() != null && head.getSpzt().equals(DJZBVOConsts.m_strStatusVerifiedNoPass)) {
			resmessage.isSuccess = false;
			resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000072")/*
			 * @res
			 * "��������δͨ��,����ʧ��"
			 */;
			return resmessage;
		}

		lrr = head.getLrr();
		// ϵͳ��Դ�����۹�����տ,ys�����ܹ�˾������¼������������Ƿ����Ϊͬһ�ˡ����ƣ���¼�����������������ͬ��
		if (head.getLybz() != null && head.getLybz().intValue() == 3
				&& (head.getDjdl().equals("sk") || head.getDjdl().equals("ys")))
			;
		else {
			if ((lrr != null) && lrr.equals(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey()))
			// ����¼����=��¼��
			{
				ResMessage res = nc.ui.arap.global.PubData.isEveryShhe(arapDjSettingParam.getPk_corp(), head.getPzglh()
						.intValue());
				if (res.intValue < 0) {
					resmessage.isSuccess = false;
					resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPP2006030102-000073")/*
					 * @res
					 * "ȡ���ײ���:¼������������Ƿ����Ϊͬһ��
					 * ����"
					 */;
					return resmessage;
				} else {
					if (!res.isSuccess) {
						resmessage.isSuccess = false;
						resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
								"UPP2006030102-000074")/*
						 * @res "¼����������˲�����ͬһ��"
						 */; // cf
						// test
						return resmessage;
					}
				}
			}
		}

		oldDjzt = head.getDjzt();
		djrq = head.getDjrq();

		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000075")/*
		 * @res
		 * "check����״̬:"
		 */
				+ oldDjzt);

		String strDjdl = getDataBuffer().getCurrentDjdl();

		resmessage = CheckBillStatus
				.checkBillStatus(resmessage, getArapDjPanel().getDjSettingParam(), strDjdl, oldDjzt);

		if (djrq.after(arapDjSettingParam.getLoginDate())) {
			resmessage.strMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000087")/*
			 * @res
			 * "������ڲ���С�ڵ���¼������,�������"
			 */;
			resmessage.isSuccess = false;
		}

		if (!resmessage.isSuccess)
			return resmessage;

		return resmessage;
	}

	// ˼·ͬshenhe_LB_s
	public DJZBVO[] shenhe_LB() throws BusinessException {
		arapDjSettingParam = getDjSettingParam();
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000115")/*@res
		// "��ʼ���..."*/);
		List<DJZBVO>  selectedVos =this.getListAllSelectedVOs();
		if (selectedVos.size() < 1) {
			ExceptionHandler.createException(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000116")/*
			 * @res
			 * "û�пɹ���˵ĵ���,����ʧ��"
			 */);
		}
		// �����Ϣ
		String strResMessage = "";
		// ��˽�����Ϣ
		ResMessage resmessage = new ResMessage();

		int ShenheCount = 0; // �Ѿ���˳ɹ��ĵ���

		Vector<String> v = new Vector<String>(); // �����Ϣ
		Vector<String> errmsgs = new Vector<String>(); // ������Ϣ
		Vector<ResMessage> resMessageVec=new Vector<ResMessage>();
		for (DJZBVO vo : selectedVos) {
			// get the index of the selected vo on the list panel

			nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) (vo.getParentVO());
			strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000117", null,
					new String[] { head.getDjbh() });
            nc.vo.ep.dj.DJZBHeaderVO oldhead =(nc.vo.ep.dj.DJZBHeaderVO)head.clone();
			resmessage = CheckAuditVO.checkShenhe(vo, getArapDjPanel().getDjSettingParam(), getDataBuffer()
					.getCurrentDjdl());
			if (resmessage.isSuccess == false) {
				strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000124", null,
						new String[] { head.getDjbh() })
						+ "\n" + resmessage.strMessage;
				v.addElement(strResMessage);
				errmsgs.addElement(strResMessage);
				continue;
			}
			try {
				this.supplementInfoBeforeVerification(vo, false);
			} catch (Exception e) {
				Log.getInstance(this.getClass()).debug(e, this.getClass(), e.getMessage());
				throw new BusinessException(e);
			}
			vo = setBudgetCheckToTrue(vo);
			resmessage = shenheByDjvo(vo); // ��˵���vo
			if (resmessage == null) {
				resmessage = new ResMessage();
				resmessage.isSuccess = true;
				resmessage.djzbvo=vo;
				resmessage.intValue = ResMessage.$SHENHE_SUCCESS;
			}

			if (resmessage.intValue == ResMessage.$SHENHE_SUCCESS) // �ɹ�
			{
				vo=resmessage.djzbvo;
                this.supplementInfoAfterVerify(vo, resmessage, 1,oldhead);

				int idx = (getRunEnvironment()).getHeadRowInexByPK(head.getVouchid());
				if (idx != -1)
					this.updateViewAfterVerifyListMode(vo, idx, 1);
				strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000125", null,
						new String[] { head.getDjbh() })
						+ resmessage.strMessage;
				
				//ˢ��djzjVO�������� ��  ����ڴ������ ������ʧ�ܡ�����ˢ���ˣ���̨û�в����µ�DJZBVO
				if(null!= resmessage.djzbvo&& null!= resmessage.djzbvo.header){
					getDataBuffer().refreshDJZBVO(resmessage.djzbvo.header.getPrimaryKey(), resmessage.djzbvo);
				}
				ShenheCount++;
				resMessageVec.add(resmessage);
			} else // ʧ��
			{
                this.supplementInfoAfterVerify(vo, null, 2,oldhead);

				strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000124", null,
						new String[] { head.getDjbh() })
						+ "\n" + resmessage.strMessage;
				errmsgs.addElement(strResMessage);
			}
			v.addElement(strResMessage);
		}
		ResMessage[] resmessages = new ResMessage[resMessageVec.size()];
		resMessageVec.copyInto(resmessages);
		//�洢ResMessage[]
		if (null != ((AbstractRuntime) this.getParent()).getAttribute(MapKeyGather.auditAction_ResMessages)) {
			((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages, null);
		}
		((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages, resmessages);

		if (v.size() > 0) {
			// if (showOkCancelMessage("�Ƿ�鿴�����־?") == MessageDialog.ID_OK) {
			// ������ʾ�����־
			//�����ɹ���ֻ��һ�����ݣ� ��������Ϣ��
			if(!(resmessage.intValue == ResMessage.$SHENHE_SUCCESS && 1==selectedVos.size())){
			
				ShenheLog f = new ShenheLog(getParent());
				Double w = new Double((getParent().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
				Double h = new Double((getParent().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
				f.setLocation(w.intValue(), h.intValue());
				//ֻ��ʾ�����Ϣ��
				if(v.size()>100){
					int errlen=errmsgs.size();
					if(0==errlen){
						errmsgs.add( NCLangRes.getInstance().getStrByID(
								"2006030102", "UPP2006030102-001117", null,
								new String[] { }/*û�д�����־�� ȫ���ɹ�*/));
						f.f_setText(errmsgs);
					}else if(errlen>100){
						Vector<String> printmsgs = new Vector<String>(); // ������Ϣ
						for (int i = 0; i < 100; i++) {
							printmsgs.add(errmsgs.get(i));
						}
						printmsgs.add(0,NCLangRes.getInstance().getStrByID(
								"2006030102", "UPP2006030102-001116" , null,
								new String[] { }/*����100�еĲ���ֻ��ʾ������Ϣ(�ܹ����������Ϊ*/)+errmsgs.size()+")");

						f.f_setText(printmsgs);
					}else {
						f.f_setText(errmsgs);
					}
				}else{
					f.f_setText(v);
				}
				f.showModal();
			}
			
			
		}

		if (ShenheCount < 1) {
			ExceptionHandler.createException(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000119")/*
			 * @res
			 * "������ϣ�����û����˵���"
			 */);
		}

		// ���ع�ʽ
		try {
			getBillListPanel().getHeadBillModel().execFormulas(
					new String[] { "shrmc->getColvalue(sm_user,user_name,cUserId,shr)" ,
							"yhqrrmc->getColvalue(sm_user,user_name,cUserId,yhqrr)" });
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
			 * @res
			 * "���ع�ʽ����:"
			 */
					+ e);
		}
		// setButtonsState(0);
		// getParent().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000126")/*@res
		// "������"*/);
		// this.m_parent.endPressBtn(bo);
		return selectedVos.toArray(new DJZBVO[]{});

	}

	private DjPanel getRunEnvironment() {

		return (DjPanel) getActionRunntimeV0();

	}

	private void updateViewAfterVerifyListMode(DJZBVO djzbvo, int voIndex, int intSucceed) {
		billModel = getBillListPanel().getHeadBillModel();
		arapDjSettingParam = getDjSettingParam();
		DJZBHeaderVO head = (DJZBHeaderVO) djzbvo.getParentVO();
		// if operation succeed
		if (intSucceed == 1) {

			// �޸Ľ����ϵĵ���״̬
			billModel.setValueAt(head.getDjzt(), voIndex, "djzt");
			billModel.setValueAt(DJZBVOUtility.getDjztmc(head.getDjzt()), voIndex, "djzt_mc");
			billModel.setValueAt(head.getAttributeValue("zzzt_mc"), voIndex, "zzzt_mc");
////			billModel.setValueAt(head.getDjzt(), voIndex, "sxbz");
//			billModel.setValueAt(DJZBVOUtility.getDjztmc(head.getDjzt()), voIndex, "djzt_mc");
			billModel.setValueAt(head.getShrq(), voIndex, "shrq");
//			String strUserPK = arapDjSettingParam.getPk_user();
			billModel.setValueAt(head.getShr(), voIndex, "shr");
			billModel.setValueAt(DJZBVOUtility.getUIStringVerifyingStatus(head.getSpzt()), voIndex, "spzt");
			billModel.setValueAt(head.getts(), voIndex, "ts");
			billModel.setValueAt(head.getYhqrr(), voIndex, "yhqrr");
//			billModel.setValueAt(head.getYhqrrq(), voIndex, "yhqrrq");
			if (head.getSxbz()!=null) {
				billModel.setValueAt(DJZBVOUtility.getUIStringDjSxbzStatus(head.getSxbz()), voIndex, "sxbzmc");
			}
		}
	}


	public boolean cancel() {
		return true;
	}

	// 1.����ֵ DJZBVO[] 2.AbstractRuntime�д�� ResMessage[]��keyֵΪDJZBVO[]��pkֵ���Ժ���ȡ��ʱͬ��ƴ��key����ȡ���Զ�����ʶͬһ�����֣�
	//���������̶�key�������趨ʹ��ǰ��գ���ÿ�ζ�������    3.�������ı���
	public DJZBVO[] shenhe_LB_S() throws BusinessException {
		// m_djPanel.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000115")/*@res
		// "��ʼ���..."*/);
		List<DJZBVO> selectedVos = this.getListAllSelectedVOs();
		if (selectedVos.size() < 1) {
			ExceptionHandler.createException(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000116")/*
			 * @res
			 * "û�пɹ���˵ĵ���,����ʧ��"
			 */);

		}

		// �����Ϣ
		String strResMessage = "";

		ResMessage[] res_s = null;

		java.util.Vector<DJZBVO> djs_v = new java.util.Vector<DJZBVO>();

		nc.vo.ep.dj.DJZBVO[] djs = null;

		java.util.Vector<String> v = new java.util.Vector<String>(); // �����Ϣ
        Map<String,DJZBHeaderVO> old=new HashMap<String,DJZBHeaderVO>();

		//		java.util.Vector vAllSelectedDJPK = getRunEnvironment().getAllSelectedDJPK();
		//		java.util.Vector vAllSelectedIndex = getRunEnvironment().getAllSelectedDJIndex();
		//		��ոü�ֵ,�������ֵ

		for (DJZBVO vo : selectedVos) {
			nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) (vo.getParentVO());
			int voIndex = getRunEnvironment().getHeadRowInexByPK(head.getVouchid());
            old.put(head.getVouchid(),  (nc.vo.ep.dj.DJZBHeaderVO) head.clone());

			strResMessage = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000117", null,
					new String[] { head.getDjbh() });
			// ���µ������Ϳ���Ԥ��
			ResMessage res = checkShenhe(vo);

			// ����������
			if (!res.isSuccess) {
				v.addElement(strResMessage + "\n" + res.strMessage);
				continue;
			}
			// ���µ������Ϳ���Ԥ��
			try {
				this.supplementInfoBeforeVerification(vo, false);
			} catch (Exception e) {
				Log.getInstance(this.getClass()).debug(e, this.getClass(), e.getMessage());
				v.addElement(strResMessage + e.getMessage());
				continue;
			}

			vo.listIndex = voIndex;
			head.listIndex = voIndex;
			// refactoring:wangqiang
			// vo.vouchid = djzboid;
			djs_v.addElement(vo);

		}
		// /////////////////////////////////////
		if (djs_v.size() > 0) {
			djs = new nc.vo.ep.dj.DJZBVO[djs_v.size()];
			djs_v.copyInto(djs);
			try {
				VOCompress.objectReference(djs);
				res_s = ((AbstractRuntime)this.getActionRunntimeV0()).getProxy().getIArapBillPrivate().auditABills2(djs);
			} catch (Exception e) {
				Log.getInstance(this.getClass()).debug(e, this.getClass(), e.getMessage());
				ExceptionHandler.createException(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000128")/*
				 * @res
				 * "���ʧ��"
				 */);
			}

		}
		for (int i = 0; res_s != null && i < res_s.length; i++) {
			v.addElement(res_s[i].strMessage);
			DJZBVO vo = getDataBuffer().getDJZBVO(res_s[i].vouchid);
			if (vo == null)
				vo = getDataBuffer().getListSelectedVO(res_s[i].vouchid);
			if (res_s[i].isSuccess) // ��˳ɹ�
			{
				// �޸�ȫ�ֱ���еĵ���״̬
            	this.supplementInfoAfterVerify(vo, res_s[i], 4,old.get(res_s[i].vouchid));
                int idx=this.getHeadRowInexByPK(  res_s[i].vouchid);
                if(idx>-1)
                	this.updateViewAfterVerifyListMode(vo, idx, 1);
            } else {
                this.supplementInfoAfterVerify(vo, null, 2,old.get(res_s[i].vouchid));
            }

		}
		//�洢ResMessage[]
		if (null != ((AbstractRuntime) this.getParent()).getAttribute(MapKeyGather.auditAction_ResMessages)) {
			((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages, null);
		}
		((AbstractRuntime) this.getParent()).setAttribute(MapKeyGather.auditAction_ResMessages, res_s);

		if (v.size() > 0) {

			ShenheLog f = new ShenheLog(getRunEnvironment());
			Double w = new Double((getRunEnvironment().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
			Double h = new Double((getRunEnvironment().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
			f.setLocation(w.intValue(), h.intValue());
			f.f_setText(v);
			f.showModal();
		}

		try {
			getRunEnvironment().getBillListPanel1().getHeadBillModel().execFormulas(
					new String[] { "shrmc->getColvalue(sm_user,user_name,cUserId,shr)" });
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
			 * @res
			 * "���ع�ʽ����:"
			 */
					+ e);
		}
		return djs;
	}

	private IArapTBBillPrivate getIArapTBBillPrivate() throws ComponentException {
		return ((IArapTBBillPrivate) NCLocator.getInstance().lookup(IArapTBBillPrivate.class.getName()));
	}
	
	
	/**
	 * ����ϵͳ�ڵ��ã��޸ġ��Ƿ���Ҫ���Ԥ�㡱���Ե�ֵΪ��Ҫ���Ԥ��
	 * @param djzbvo
	 * @return
	 */
	private DJZBVO setBudgetCheckToTrue(DJZBVO djzbvo) {
		djzbvo.budgetCheck = true;
		return djzbvo;
	}
}