package nc.itf.dahuan.pf;

import java.util.List;
import java.util.Map;

import nc.vo.dahuan.ctbill.DhContractVO;
import nc.vo.dahuan.fkjh.DhFkjhbillVO;
import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;
import nc.vo.dahuan.htinfo.htchange.HtChangeEntity;
import nc.vo.dahuan.report.HtzxReportVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IdhServer {

	//处理计划单据回写 
	public void UpdateDhhtToLjfkjhje(DhFkjhbillVO[] itemvos) throws BusinessException;
	
	// 处理合同盖章
	public void SealDhht(nc.vo.dahuan.cttreebill.DhContractVO headVO,String user,UFDate date) throws Exception;
	// 处理合同审核
	public void AuditDhhtbmzg(Map<String,List<DhContractVO>> map,String user,UFDate date) throws Exception;
	// 处理合同弃审
	public void CancelAuditDhhtbmzg(Map<String,List<DhContractVO>> map) throws Exception;
	
	// 绑定打印
	public void BoundPrint(DhFkjhbillVO[] vos,String pkcon,String content,String user,UFDate date) throws Exception;
	
	//	查询文件是否上传
	public int queryfilesystem(String htcode) throws BusinessException ;
	
	//查询合同的执行情况数据
	public  HtzxReportVO[] queryHtzxVo(String strwhere) throws BusinessException;
	
	//根据合同号查询信息
	public DhContractVO[] queryDhhtVO(String[] htcodes) throws BusinessException;
	
	
	public void checkJobaseData(nc.vo.dahuan.cttreebill.DhContractVO headvo) throws BusinessException ;
	
	
	// 合同变更审核
	public void changeContractData(HtChangeEntity htc,HtChangeDtlEntity[] htcdtls,UFDate nowdate) throws Exception;
	
	
}
