package nc.impl.cmp;

import nc.bs.dao.BaseDAO;
import nc.itf.cmp.IUpdateData;
import nc.vo.pub.BusinessException;

public class UpdateDataImpl implements IUpdateData {

	public void doUpdateCmpData(String sql) throws BusinessException {
		
		BaseDAO dao=new BaseDAO();
		
		dao.executeUpdate(sql);

	}

}
