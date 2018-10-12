package nc.bs.dh.httk;

import nc.bs.dao.BaseDAO;
import nc.itf.dahuan.pf.IdhtkServer;
import nc.vo.dh.httk.DhHttkVO;
import nc.vo.pub.BusinessException;

public class QueryhttkDMO implements IdhtkServer {

	public DhHttkVO queryhttkAggvo(String pk) throws BusinessException {

		return (DhHttkVO) new BaseDAO().retrieveByPK(DhHttkVO.class, pk);
	}

}
