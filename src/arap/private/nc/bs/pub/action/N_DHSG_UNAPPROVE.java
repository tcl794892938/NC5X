package nc.bs.pub.action;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.arap.sedgather.AggSedGatherVO;
import nc.vo.arap.sedgather.SedGatherDVO;
import nc.vo.arap.sedgather.SedGatherHVO;
import nc.vo.arap.sedgather.sale.SaleBXBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：材料需求单的弃审
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2009-4-4)
 * @author 平台脚本生成
 */
public class N_DHSG_UNAPPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_X001_UNAPPROVE 构造子注解。
 */
public N_DHSG_UNAPPROVE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
	procUnApproveFlow (vo); 
	Object retObj =runClass( "nc.bs.trade.comstatus.BillUnApprove", "unApproveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
//	####该组件为单动作工作流处理开始...不能进行修改####对过期的版本数据进行回写
	
	AggSedGatherVO aggvo = (AggSedGatherVO)vo.m_preValueVo;
	BaseDAO dao = new BaseDAO();
	SedGatherHVO hvo = aggvo.getParentVO();
	SedGatherDVO dvo = aggvo.getChildrenVO()[0];
	
	// 更新销售订单-表头
	
	String pkOrder = hvo.getPk_saleorder();
	
	String updordersql = "update so_sale t set t.pk_defdoc11 = 'N' where t.csaleid = '"+pkOrder+"'";
	dao.executeUpdate(updordersql);
	
	// 删除销售订单明细
	
	String deletexsmx = "delete from so_saleorder_b b where b.corder_bid = " +
			" (select e.pk_orderbid from bx_sedgather_exec e where e.pk_bx_sedgather='"+hvo.getPk_sedagther()+"')";
	dao.executeUpdate(deletexsmx);
	
	// 更新销售订单明细
	List<SaleBXBVO> bxblist = (List<SaleBXBVO>)dao.retrieveByClause(SaleBXBVO.class, " csaleid = '"+pkOrder+"' and nvl(dr,0)=0 ");
	
	// 销售订单金额合计
	UFDouble jehj = new UFDouble(0);
	
	for(int i=0;i<bxblist.size();i++){
		SaleBXBVO updbvo = bxblist.get(i);
		String pksaleb = updbvo.getCorder_bid();
		
		String hissql = "select corder_bid,nnumber,npacknumber,nmny from bx_sedgather_amt where corder_bid = '"+pksaleb+"' ";
		List<Map<String,Object>> hislist = (List<Map<String,Object>>)dao.executeQuery(hissql, new MapListProcessor());
		if(null != hislist && hislist.size()>0){
			Map<String,Object> hismap = hislist.get(0);
			
			// 数量
			UFDouble nowuftb = new UFDouble(null == hismap.get("nnumber") ? "0.00" : hismap.get("nnumber").toString());
			// 捆数
			UFDouble kunsun = new UFDouble(null == hismap.get("npacknumber") ? "0.00" : hismap.get("npacknumber").toString());
			// 金额
			UFDouble jeamt = new UFDouble(null == hismap.get("nmny") ? "0.00" : hismap.get("nmny").toString());
			
			jehj = jehj.add(jeamt);
			
			String bsql = "update so_saleorder_b set " +
						" nnumber="+nowuftb+"," +
						" nquoteunitnum="+nowuftb+"," +
						" npacknumber="+kunsun+"," +
						" nmny="+jeamt+"," +
						" noriginalcurmny="+jeamt+"," +
						" noriginalcursummny="+jeamt+"," +
						" nsummny="+jeamt+" " +
						" where corder_bid = '"+pksaleb+"'";
			dao.executeUpdate(bsql);
			
			// 删除记录
			String delhissql = "delete from bx_sedgather_amt where corder_bid = '"+pksaleb+"' ";
			dao.executeUpdate(delhissql);
		}
		
	}
	
	// 更新销售订单合计
	String updhjsale = "update so_sale t set t.nheadsummny = "+jehj+" where t.csaleid = '"+pkOrder+"'";
	dao.executeUpdate(updhjsale);
	
	// 删除销售订单执行
	String deletexsexec = "delete from so_saleexecute b where b.csale_bid = ( " +
			" select e.pk_orderbid from bx_sedgather_exec e where e.pk_bx_sedgather='"+hvo.getPk_sedagther()+"')";
	dao.executeUpdate(deletexsexec);
	
	// 更新收款单表头
	String pkSK = dvo.getPk_gathering();
	UFDouble gatheramount = dvo.getGather_amount();
	
	String updsksql = "update arap_djzb z set z.bbje="+gatheramount.toString()+",z.ybje="+gatheramount.toString()+",z.zyx1='N' where z.vouchid = '"+pkSK+"'";
	dao.executeUpdate(updsksql);
	
	// 删除收款单明细
	
	String deleteskfb = " delete from arap_djfb b where b.fb_oid = ( " +
			" select e.pk_arapbid from bx_sedgather_exec e where e.pk_bx_sedgather='"+hvo.getPk_sedagther()+"')";
	dao.executeUpdate(deleteskfb);
	
	// 更新结算单表头
	String settlsql = " update cmp_settlement t set t.local="+gatheramount.toString()+",t.primal= "+gatheramount.toString()+" where t.pk_settlement = (select distinct d.pk_settlement from cmp_detail d where d.pk_bill = '"+pkSK+"')";
	dao.executeUpdate(settlsql);
	
	// 更新结算单明细
	String detlsql = " update cmp_detail d set d.receivelocal="+gatheramount.toString()+",d.receive="+gatheramount.toString()+" where d.pk_bill = '"+pkSK+"'";
	dao.executeUpdate(detlsql);
	
	// 删除记录关联表
	String relationsql = " delete from bx_sedgather_exec where pk_bx_sedgather='"+hvo.getPk_sedagther()+"' ";
	dao.executeUpdate(relationsql);
	
	// 关单
	String headsql = "update so_sale set binvoicendflag = 'N',boutendflag = 'N',breceiptendflag = 'N',fstatus=2 where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(headsql);
	String bodysql1 = "update so_saleorder_b set frowstatus=2 where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(bodysql1);
	String bodysql2 = "update so_saleexecute set bifinventoryfinish='N',bifinvoicefinish='N',bifreceivefinish='N',bsquareendflag='N' where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(bodysql2);
	
	return getVo();
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	procUnApproveFlow (vo); \n	Object retObj =runClassCom@ \"nc.bs.trade.comstatus.BillUnApprove\", \"unApproveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	return retObj;\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
