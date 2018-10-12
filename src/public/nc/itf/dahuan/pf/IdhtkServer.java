package nc.itf.dahuan.pf;

import nc.vo.dh.httk.DhHttkVO;
import nc.vo.pub.BusinessException;

public interface IdhtkServer {

	//查询合同条款VO
	public DhHttkVO queryhttkAggvo(String pk) throws BusinessException;
	
	
}
