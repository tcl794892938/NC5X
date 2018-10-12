package nc.bs.arap.impl;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.arap.itf.IUpdateMidData;
import nc.vo.pu.tempbill.TempbillHeaderVO;
import nc.vo.pub.BusinessException;

public class UpdateMidDataImpl implements IUpdateMidData {

	public String updateNCSystemData(String pk_bill) throws BusinessException {
		
		BaseDAO dao=new BaseDAO();
		
		
		String sql="select * from temp_bill where pk_tempbill='"+pk_bill+"'";
		
		TempbillHeaderVO vo=(TempbillHeaderVO)dao.executeQuery(sql, new BeanProcessor(TempbillHeaderVO.class));
		
		if(vo==null){
			return "���������м���Ѿ�ɾ����";
		}
		
		String sql2="";
		Integer it=0;
		if(vo.getVbilltype().equals("21")){//�ɹ�����
			
			if(vo.getVbillstatus()==1){//��������
				it=2;
			}
			if(vo.getVbillstatus()==2){//������ͨ��
				it=0;
			}
			if(vo.getVbillstatus()==3){//����ͨ��
				it=3;
			}
			sql2="update po_order set approver=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
					"dr=0,forderstatus="+it+",taudittime='"+vo.getApprovedate()+"',vdef10='"+vo.getApprovenote()+"' where pk_order='"+vo.getPk_bill()+"'";
		}
		
		if(vo.getVbilltype().equals("20")){//�빺��
			
			if(vo.getVbillstatus()==1){//��������
				it=2;
			}
			if(vo.getVbillstatus()==2){//������ͨ��
				it=0;
			}
			if(vo.getVbillstatus()==3){//����ͨ��
				it=3;
			}
			sql2="update po_praybill set approver=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
					"dr=0,fbillstatus="+it+",taudittime='"+vo.getApprovedate()+"',vdef10='"+vo.getApprovenote()+"' where pk_praybill='"+vo.getPk_bill()+"'";
		}
		
		if(vo.getVbilltype().equals("F3")){//�������뵥
			
			Integer it1=0;//����״̬
			Integer it2=0;//����״̬
			Integer it3=0;//��Ч״̬
			if(vo.getVbillstatus()==1){//��������
				it1=2;
				it2=2;
				it3=0;
			}
			if(vo.getVbillstatus()==2){//������ͨ��
				it1=-1;
				it2=-1;
				it3=0;
			}
			if(vo.getVbillstatus()==3){//����ͨ��
				it1=1;
				it2=1;
				it3=10;
			}
			
			if(it3==10){
				sql2="update ap_paybill set approver=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
						"effectuser=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
						"dr=0,approvestatus="+it1+",billstatus="+it2+",effectstatus="+it3+",effectdate='"+vo.getApprovedate()+"'," +
						"approvedate='"+vo.getApprovedate()+"',def10='"+vo.getApprovenote()+"' where pk_paybill='"+vo.getPk_bill()+"'";
			}else{
				sql2="update ap_paybill set approver=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
						"dr=0,approvestatus="+it1+",billstatus="+it2+",effectstatus="+it3+"," +
						"approvedate='"+vo.getApprovedate()+"',def10='"+vo.getApprovenote()+"' where pk_paybill='"+vo.getPk_bill()+"'";
			}
		}
		
		if(vo.getVbilltype().equals("4455")){//�������뵥
			
			if(vo.getVbillstatus()==1){//��������
				it=5;
			}
			if(vo.getVbillstatus()==2){//������ͨ��
				it=2;
			}
			if(vo.getVbillstatus()==3){//����ͨ��
				it=4;
			}
			sql2="update ic_sapply_h set approver=(select cuserid from sm_user where user_code='"+vo.getApprover()+"')," +
					"dr=0,fbillflag="+it+",taudittime='"+vo.getApprovedate()+"',vdef10='"+vo.getApprovenote()+"' where cgeneralhid='"+vo.getPk_bill()+"'";
		}
		
		if("".equals(sql2)){
			return "�м�������ͻ�״̬����ȷ��";
		}
		
		try {
			dao.executeUpdate(sql2);
		} catch (Exception e) {
			return "����NCϵͳ�����쳣��";
		}
		
		return "���³ɹ���";
	}

}
