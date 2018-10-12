package nc.ui.arap.bx.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.cmp.IUpdateData;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.arap.bx.BXBillMainPanel;
import nc.ui.arap.bx.BxParam;
import nc.ui.cmp.pub.AccountExceptionHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.vo.arap.util.StringUtils;
import nc.vo.cmp.exception.CmpAuthorizationException;
import nc.vo.cmp.exception.ErmException;
import nc.vo.cmpbill.outer.BugetAlarmBusinessException;
import nc.vo.cmpbill.outer.CmpFpBusinessException;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.MessageVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.logging.Debug;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * @author twei
 *
 *  ������ˣ�����ˡ�Action
 *
 * nc.ui.arap.bx.actions.AuditAction
 */
public class AuditAction extends BXDefaultAction {

	/**
	 * @param background������˻���
	 * @throws BusinessException
	 */
	public void unAudit( ) throws BusinessException {

		BXVO[] vos = getSelBxvosClone();

		if (vos.length < 1) {
			((BXBillMainPanel) this.getParent()).showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000135")/* @res "û�пɹ�����˵ĵ���,����ʧ��" */);
			return;
		}

		//��˽�����Ϣ
		MessageVO[] msgs = new MessageVO[vos.length];
		MessageVO[] msgReturn = new MessageVO[]{};
		List<BXVO> auditVOs=new ArrayList<BXVO>();

		for (int i = 0; i < vos.length; i++) {

			BXVO vo = vos[i];

			msgs[i] = checkUnShenhe(vo);

			if (!msgs[i].isSuccess()) {
				continue;
			}

			this.supplementInfoBeforeUnVerification(vo);

			auditVOs.add(vo);
		}

		if(auditVOs.size()>0){
			List<BXVO> resultVos=new ArrayList<BXVO>();

			for(BXVO bxvo:auditVOs){

				msgReturn = unAudit(msgReturn, bxvo);

				if(msgReturn==null)
					msgReturn = new MessageVO[]{new MessageVO(MessageVO.UNAUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000339")/*@res "�û�ȡ������"*/, false)};

				resultVos = combineMsgs(msgs, msgReturn,resultVos);

			}

			List<BXVO> updateVos=new ArrayList<BXVO>();
			for(BXVO vo:resultVos){
				BXHeaderVO parentVO = vo.getParentVO();
				BXVO cacheVONew = getVoCache().getVOByPk(parentVO.getPrimaryKey());
				cacheVONew.setParentVO(parentVO);
				updateVos.add(cacheVONew);
			}
			updateVoAndView(updateVos.toArray(new BXVO[]{}));
		}

		getMainPanel().viewLog(msgs);

	}



	private MessageVO[] unAudit(MessageVO[] msgReturn, BXVO bxvo) {
		BXHeaderVO head = bxvo.getParentVO();
		try {
			String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
			msgReturn= (MessageVO[]) PfUtilClient.processActionFlow(getMainPanel(), "UNAPPROVE"+getBxParam().getPk_user(), head.getDjlxbm(), currTime, bxvo, null);
		} catch (CmpAuthorizationException exp) {  
			AccountExceptionHandler handler = new AccountExceptionHandler(getMainPanel());
		    boolean pass = handler.handleException(exp);
		    if (pass) {
				List<String> authList = handler.getAccountList();
				bxvo.authList=authList;
				msgReturn=unAudit(msgReturn,bxvo);
		    }else{
				msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000340")/*@res "�˻���������"*/, false)};
			}
		}catch (Exception e) {
			msgReturn = new MessageVO[]{new MessageVO(MessageVO.UNAUDIT, bxvo, e.getMessage(), false)};
		}
		return msgReturn;
	}



	/**
	 * @param background ������˻���
	 * @throws BusinessException
	 */
	public void audit( ) throws BusinessException {

		BXVO[] vos = getSelBxvosClone();

		if (vos.length < 1) {
			getParent().showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000116")/*
			 * @res "û�пɹ���˵ĵ���,����ʧ��"
			 */);
		}

		// ��˽�����Ϣ
		MessageVO[] msgs = new MessageVO[vos.length];
		MessageVO[] msgReturn = new MessageVO[]{};
		List<BXVO> auditVOs=new ArrayList<BXVO>();

		for (int i = 0; i < vos.length; i++) {

			BXVO vo = vos[i];

			msgs[i] = checkShenhe(vo);

			if (!msgs[i].isSuccess()) {
				continue;
			}

			this.supplementInfoBeforeVerification(vo);

			auditVOs.add(vo);
		}
		List<BXVO> resultVos =new ArrayList<BXVO>();

		if(auditVOs.size()>0){

			for(BXVO bxvo:auditVOs){

				msgReturn = audit(msgReturn, bxvo);

				if(msgReturn==null)
					msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000339")/*@res "�û�ȡ������"*/, false)};

				resultVos = combineMsgs(msgs, msgReturn,resultVos);
			}

			List<BXVO> updateVos=new ArrayList<BXVO>();
			for(BXVO vo:resultVos){
				BXHeaderVO parentVO = vo.getParentVO();
				BXVO cacheVONew = getVoCache().getVOByPk(parentVO.getPrimaryKey());
				cacheVONew.setParentVO(parentVO);
				updateVos.add(cacheVONew);
			}
			//�޸�״̬
			for(BXVO vo:updateVos){
				if(vo.getParentVO().getSpzt()!=null&&vo.getParentVO().getSpzt().equals("1")){
					vo=this.setPrintSPInfomation(vo);
				}
			}
			updateVoAndView(updateVos.toArray(new BXVO[]{}));
		}

		getMainPanel().viewLog(msgs);

	}
	
//	�������������2016-12-08 by tcl
	protected BXVO setPrintSPInfomation(BXVO vo) throws BusinessException{
		
		try {
			IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String sqlsp="select * from pub_workflownote e where e.approvestatus=1 and e.billid='"+vo.getParentVO().getPrimaryKey()+"' order by ts ";
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
						"and t.pk_corp='"+vo.getParentVO().getPk_corp()+"' and l.role_code in('14','15')";
					Integer it=(Integer)iQ.executeQuery(sqlqx, new ColumnProcessor());
					if(it<=0){
						obj2=null;
					}
				}
			}
			
			String sql111="";
			String sql222="";
			
			String sql11111="";
			String sql22222="";
			String sql33333="";
			if(obj7==null||"".equals(obj7)){
				sql33333=" b.zyx14=null,";
			}else{
				sql33333=" b.zyx14='"+obj7+"',";
			}
			if(obj1==null||"".equals(obj1)){
				sql11111=" b.zyx19=null,b.zyx17=null,b.zyx15=null,";
			}else{
				sql11111=" b.zyx19='"+obj1+"',b.zyx17='"+obj3+"',b.zyx15='"+obj5+"',";
			}
			if(obj2==null||"".equals(obj2)){
				sql22222=" b.zyx20=null,b.zyx18=null,b.zyx16=null";
			}else{
				sql22222=" b.zyx20='"+obj2+"',b.zyx18='"+obj4+"',b.zyx16='"+obj6+"' ";
			}
			if(vo.getParentVO().getAttributeValue("djdl").equals("jk")){
				sql111="update er_jkzb b set "+sql33333+sql11111+sql22222+
				" where b.pk_jkbx='"+vo.getParentVO().getAttributeValue("pk_jkbx")+"'";
		
				sql222="select ts from er_jkzb b where b.pk_jkbx='"+vo.getParentVO().getAttributeValue("pk_jkbx")+"'";
			}else if(vo.getParentVO().getAttributeValue("djdl").equals("bx")){
				sql111="update er_bxzb b set "+sql33333+sql11111+sql22222+
				" where b.pk_jkbx='"+vo.getParentVO().getAttributeValue("pk_jkbx")+"'";
		
				sql222="select ts from er_bxzb b where b.pk_jkbx='"+vo.getParentVO().getAttributeValue("pk_jkbx")+"'";
			}
			
			IUpdateData ida=NCLocator.getInstance().lookup(IUpdateData.class);
	
			ida.doUpdateCmpData(sql111);
	
			String ts=(String)iQ.executeQuery(sql222, new ColumnProcessor());
	
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



	private MessageVO[] audit(MessageVO[] msgReturn, BXVO bxvo) {
		BXHeaderVO head = bxvo.getParentVO();

		try {
			String currTime = nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
			msgReturn= (MessageVO[]) PfUtilClient.processActionFlow((getMainPanel()), "APPROVE", head.getDjlxbm(), currTime, bxvo, null);

			//��ʾԤ�㣬�����Ƶ���ʾ��Ϣ
			if(!StringUtils.isNullWithTrim(msgReturn[0].getBxvo().getWarningMsg())){
				showWarningMsg(msgReturn[0].getBxvo().getWarningMsg());
				msgReturn[0].getBxvo().setWarningMsg(null);
			}

		} catch (CmpFpBusinessException e) {
			if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000341")/*@res " �Ƿ������ˣ�"*/)== MessageDialog.ID_YES){
				bxvo.setHasZjjhCheck(Boolean.TRUE);  //����飬ǿд�ʽ�ƻ�
				msgReturn=audit(msgReturn,bxvo);
			}else{
				msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000342")/*@res "�ʽ�ƻ�����ʧ��"*/, false)};
			}
		} catch (BugetAlarmBusinessException e) {
			if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000341")/*@res " �Ƿ������ˣ�"*/)== MessageDialog.ID_YES){
				bxvo.setHasNtbCheck(Boolean.TRUE);  //�����
				msgReturn=audit(msgReturn,bxvo);
			}else{
				msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000405")/*@res "Ԥ������ʧ��"*/, false)};
			}
		} catch (ErmException e) {
			if(MessageDialog.showYesNoDlg(getParent(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000049")/*@res "��ʾ"*/, e.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000341")/*@res " �Ƿ������ˣ�"*/)== MessageDialog.ID_YES){
				bxvo.setHasJkCheck(Boolean.TRUE);  //�����
				msgReturn=audit(msgReturn,bxvo);
			}else{
				msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000406")/*@res "�������ʧ��"*/, false)};
			}
		}catch (CmpAuthorizationException exp) {
			AccountExceptionHandler handler = new AccountExceptionHandler(getMainPanel());
		    boolean pass = handler.handleException(exp);
		    if (pass) {
				List<String> authList = handler.getAccountList();
				bxvo.authList=authList;
				msgReturn=audit(msgReturn,bxvo);
		    }else{
				msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000340")/*@res "�˻���������"*/, false)};
			}
		} catch (Exception e) {
			msgReturn = new MessageVO[]{new MessageVO(MessageVO.AUDIT, bxvo, e.getMessage(), false)};
		}
		return msgReturn;
	}

	/**
	 * @param bxvo
	 * @param background
	 * @return
	 *
	 * У�������Ϣ
	 */
	private MessageVO checkShenhe(BXVO bxvo ) {

		BxParam bxParam = getBxParam();

		BXHeaderVO head = null;

		head = (BXHeaderVO) (bxvo.getParentVO());

		MessageVO msgVO = new MessageVO(MessageVO.AUDIT, bxvo, "", true);


		if (!msgVO.isSuccess()) {
			return msgVO;
		}

		nc.vo.pub.lang.UFDate djrq = null;

		djrq = head.getDjrq();

		if (djrq.after(bxParam.getLoginDate())) {
			msgVO.setSuccess(false);
			msgVO.setMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2011","UPP2011-000000")/*@res "������ڲ������ڵ���¼������,�������"*/);
			return msgVO;
		}

		return msgVO;

	}

	/**
	 * @param vo
	 * @return
	 *
	 * vo�����Ϣ����
	 */
	private BXVO supplementInfoBeforeVerification(BXVO vo) {
		BxParam bxParam = getBxParam();
		BXHeaderVO head = (BXHeaderVO) vo.getParentVO();
		head.setShrq(bxParam.getLoginDate());
		head.setShr(bxParam.getPk_user());
		return vo;
	}

	/**
	 * @param vo
	 * @return
	 * vo�������Ϣ����
	 */
	private BXVO supplementInfoBeforeUnVerification(BXVO vo) {
		BxParam bxParam = getBxParam();
		BXHeaderVO head = (BXHeaderVO) vo.getParentVO();
//		head.setShrq(bxParam.getLoginDate());
//		head.setShr(bxParam.getPk_user());
		return vo;
	}

	/**
	 * @param bxvo
	 * @param background
	 * @return
	 *
	 * У�鷴�����Ϣ
	 */
	private MessageVO checkUnShenhe(BXVO bxvo ) {

		// ���ݱ�ͷVO
		BXHeaderVO head = bxvo.getParentVO();

		// ���鷵�ص���Ϣ
		MessageVO msgVO = new MessageVO(MessageVO.UNAUDIT, bxvo, "", true);

		if (null != head.getQcbz() && head.getQcbz().booleanValue()) {
			msgVO.setSuccess(false);
			msgVO.setMsg(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000091"))/* @res "�ڳ����ݲ��ܷ����" */;
			return msgVO;
		}

		return msgVO;
	}


}