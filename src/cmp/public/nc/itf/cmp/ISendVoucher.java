package nc.itf.cmp;

import nc.vo.pub.BusinessException;

public interface ISendVoucher {
	
	public String doSendVoucherToNCSystem(String xml)throws BusinessException;
	
	public String getZJZFYSBugetFromNCSystem(String args)throws BusinessException;
	
	public String getFYBXYSBugetFromNCSystem(String args)throws BusinessException;

}
