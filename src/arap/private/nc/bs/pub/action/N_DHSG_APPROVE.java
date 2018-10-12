package nc.bs.pub.action;

import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.sedgather.AggSedGatherVO;
import nc.vo.arap.sedgather.SedGatherDVO;
import nc.vo.arap.sedgather.SedGatherHVO;
import nc.vo.arap.sedgather.sale.SaleBXBVO;
import nc.vo.arap.sedgather.sale.SaleBXXVO;
import nc.vo.arap.sedgather.sk.DjskBXBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���������󵥵�����
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2009-4-4)
 * @author ƽ̨�ű�����
 */
public class N_DHSG_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_X001_APPROVE ������ע�⡣
 */
public N_DHSG_APPROVE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####�����Ϊ����������������ʼ...���ܽ����޸�####
Object m_sysflowObj= procActionFlow(vo);
	if (m_sysflowObj!=null){
		return m_sysflowObj;
	}
	//####�����Ϊ�������������������...���ܽ����޸�####
	Object retObj  =runClass( "nc.bs.trade.comstatus.BillApprove", "approveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas); 
//	####�����Ϊ����������������ʼ...���ܽ����޸�####�Թ��ڵİ汾���ݽ��л�д
	Object inObject = getVo();
	
	AggSedGatherVO aggvo = (AggSedGatherVO)vo.m_preValueVo;
	BaseDAO dao = new BaseDAO();
	SedGatherHVO hvo = aggvo.getParentVO();
	SedGatherDVO dvo = aggvo.getChildrenVO()[0];
	
	// �������۶���--��ͷ
	String pkOrder = hvo.getPk_saleorder();
	
	String updordersql = "update so_sale t set t.pk_defdoc11 = 'Y' where t.csaleid = '"+pkOrder+"'";
	dao.executeUpdate(updordersql);
	
	//	 �������۶���--��ϸ
	String nnumsumsql = "select sum(nvl(nnumber,0)) from so_saleorder_b where csaleid = '"+pkOrder+"' and nvl(dr,0)=0 ";
	Object nnumObj = dao.executeQuery(nnumsumsql, new ColumnProcessor());
	UFDouble nnumUfd = new UFDouble(null==nnumObj?"0":nnumObj.toString());
	
	List<SaleBXBVO> bxblist = (List<SaleBXBVO>)dao.retrieveByClause(SaleBXBVO.class, " csaleid = '"+pkOrder+"' and nvl(dr,0)=0 ");
	
	UFDouble nnumber = dvo.getSaleout_nums().sub(hvo.getSale_nums());
	
	UFDouble synum = nnumber;
	
	UFDouble jehj = new UFDouble(0);
	
	for(int i=0;i<bxblist.size();i++){
		int lastindex = bxblist.size()-1;
		SaleBXBVO updbvo = bxblist.get(i);
		String pksaleb = updbvo.getCorder_bid();
		if(i==lastindex){
			// ����
			UFDouble nowuftb = updbvo.getNnumber().add(synum,4);
			// ����
			UFDouble kunsun = null==updbvo.getNpacknumber()||updbvo.getNpacknumber().compareTo(new UFDouble(0))==0
							  ? updbvo.getNpacknumber() : nowuftb.div(updbvo.getNnumber().div(updbvo.getNpacknumber()),4);
			UFDouble jeamt = updbvo.getNnetprice().multiply(nowuftb,4);
			
			jehj = jehj.add(jeamt);
			
			// ���¼�¼��
			String history = " insert into bx_sedgather_amt (corder_bid,nnumber,npacknumber,nmny) "
				        +" values ('"+pksaleb+"',"+updbvo.getNnumber()+","+updbvo.getNpacknumber()+","+updbvo.getNmny()+") ";
			dao.executeUpdate(history);
			
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
			
			
		}else{
			// ����
			UFDouble numfloor = nnumber.multiply(updbvo.getNnumber().div(nnumUfd),4);
			synum = synum.sub(numfloor);
			UFDouble nowuftb = updbvo.getNnumber().add(numfloor,4);
			
			// ����
			UFDouble kunsun = null==updbvo.getNpacknumber()||updbvo.getNpacknumber().compareTo(new UFDouble(0))==0
							  ? updbvo.getNpacknumber() : nowuftb.div(updbvo.getNnumber().div(updbvo.getNpacknumber()),4);
			UFDouble jeamt = updbvo.getNnetprice().multiply(nowuftb,4);
			
			jehj = jehj.add(jeamt);
			
			//	���¼�¼��
			String history = " insert into bx_sedgather_amt (corder_bid,nnumber,npacknumber,nmny) "
				        +" values ('"+pksaleb+"',"+updbvo.getNnumber()+","+updbvo.getNpacknumber()+","+updbvo.getNmny()+") ";
			dao.executeUpdate(history);
			
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
		}
	}
	
	// �������۶������ϼ�
	String updhjsale = "update so_sale t set t.nheadsummny = "+jehj+" where t.csaleid = '"+pkOrder+"'";
	dao.executeUpdate(updhjsale);
	
	// �����տ
	String pkSK = dvo.getPk_gathering();
	UFDouble saloutamt = dvo.getSaleout_amount();
	
	String updsksql = "update arap_djzb z set z.bbje="+saloutamt.toString()+",z.ybje="+saloutamt.toString()+",z.zyx1='Y' where z.vouchid = '"+pkSK+"'";
	dao.executeUpdate(updsksql);
	
	List<DjskBXBVO> djsklist = (List<DjskBXBVO>) dao.retrieveByClause(DjskBXBVO.class, " vouchid = '"+pkSK+"' and nvl(dr,0)=0");
	DjskBXBVO skvo = (DjskBXBVO)djsklist.get(0).clone();
	
	skvo.setTs(null);
	skvo.setFb_oid(null);
	
	UFDouble nmny = dvo.getSaleout_amount().sub(hvo.getSale_amount());
	skvo.setBbye(nmny);
	skvo.setDfbbje(nmny);
	skvo.setDfbbwsje(nmny);
	skvo.setYbye(nmny);
	skvo.setDfybje(nmny);
	skvo.setDfybwsje(nmny);
	skvo.setOccupationmny(nmny);
	
	String pkbsk = dao.insertVO(skvo);
	
	// ���㵥
	String settlsql = " update cmp_settlement t set t.local="+saloutamt.toString()+",t.primal= "+saloutamt.toString()+" where t.pk_settlement = (select distinct d.pk_settlement from cmp_detail d where d.pk_bill = '"+pkSK+"')";
	String detlsql = " update cmp_detail d set d.receivelocal="+saloutamt.toString()+",d.receive="+saloutamt.toString()+" where d.pk_bill = '"+pkSK+"'";
	dao.executeUpdate(settlsql);
	dao.executeUpdate(detlsql);

	// ���¼�¼������
//	String relationsql = " insert into bx_sedgather_exec(pk_bx_sedgather,pk_orderbid,pk_arapbid) "+
//						" values('"+hvo.getPk_sedagther()+"','"+pkbxb+"','"+pkbsk+"')";
//	
//	dao.executeUpdate(relationsql);
	
	// �ص�
	String headsql = "update so_sale set binvoicendflag = 'Y',boutendflag = 'Y',breceiptendflag = 'Y',fstatus=6 where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(headsql);
	String bodysql1 = "update so_saleorder_b set FROWSTATUS=6 where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(bodysql1);
	String bodysql2 = "update so_saleexecute set bifinventoryfinish='Y',bifinvoicefinish='Y',bifreceivefinish='Y',bsquareendflag='Y' where csaleid = '"+pkOrder+"'";
	dao.executeUpdate(bodysql2);
	
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####�����Ϊ����������������ʼ...���ܽ����޸�####\n	procActionFlow@@;\n	//####�����Ϊ�������������������...���ܽ����޸�####\n	Object retObj  =runClassCom@ \"nc.bs.trade.comstatus.BillApprove\", \"approveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@; \n	return retObj;\n";}
/*
* ��ע�����ýű�������HAS
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
