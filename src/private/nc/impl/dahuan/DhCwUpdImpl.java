package nc.impl.dahuan;

import nc.bs.dao.BaseDAO;
import nc.itf.dahuan.pf.IdhCwUpd;
import nc.vo.dahuan.htupdate.HtUpdateVO;
import nc.vo.pub.AggregatedValueObject;

public class DhCwUpdImpl implements IdhCwUpd {

	public AggregatedValueObject updHtVo(AggregatedValueObject aggvo, int index) throws Exception {
		
		HtUpdateVO uvo = (HtUpdateVO)aggvo.getParentVO();
		
		BaseDAO dao = new BaseDAO();
		
		if(0==index){
			// 发货标志
			String sql = "update dh_contract t set t.is_delivery = "+uvo.getIs_delivery()+" where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
		}else if(1==index){
			// 累计收付款
			String sql = "update dh_contract t set t.ljfkjhje = "+uvo.getLjamt()+" where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
		}else if(2==index){
			// 合同盖章
			String sql1 = " delete from bd_jobbasfil b where b.pk_jobbasfil =  "+
						" (select m.pk_jobbasfil from bd_jobmngfil m where m.pk_jobmngfil = "+
						" (select t.pk_jobmandoc from dh_contract t where t.pk_contract = '"+uvo.getPk_contract()+"')) ";
			dao.executeUpdate(sql1);
			
			String sql2 = " delete from bd_jobmngfil m where m.pk_jobmngfil = "+
						" (select t.pk_jobmandoc from dh_contract t where t.pk_contract = '"+uvo.getPk_contract()+"') ";
			dao.executeUpdate(sql2);
			
			String sql = "update dh_contract t set t.is_seal = 0,t.pk_jobmandoc = null,t.is_pay = 0 where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
			
		}else if(3==index){
			// 归属部门
			String sql = "update dh_contract t set t.ht_dept = '"+uvo.getHt_dept()+"' where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
			
		}else if(4==index){
			// 银行信息
			String sql = "update dh_contract t set t.pk_bank='"+uvo.getBank_name()+"',t.sax_no='"+uvo.getSax_no()+"' where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
			
		}else if(5==index){
			//项目经理
			String sql="update dh_contract t set t.pk_xmjl='"+uvo.getPk_xmjl()+"' where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
		}else if(6==index){
			//项目预算
			String sql="update dh_contract t set t.xm_amount="+uvo.getBgtmny()+" where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
		}else if(7==index){
			//合同类型
			String sql="update dh_contract t set t.pk_cttype='"+uvo.getHttype()+"' where t.pk_contract='"+uvo.getPk_contract()+"' ";
			dao.executeUpdate(sql);
		}
		else{
			throw new Exception("数据库操作异常");
		}
		
		String pkUvo = dao.insertVO(uvo);
		uvo.setHtupd(pkUvo);
		aggvo.setParentVO(uvo);
		
		return aggvo;
	}
}
