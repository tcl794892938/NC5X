package nc.ui.dahuan.htfz;

import java.util.Hashtable;


/**
 *
 *抽象的业务代理类
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.dahuan.ctbill.DhContractBVO[] bodyVOs1 =
                       (nc.vo.dahuan.ctbill.DhContractBVO[])queryByCondition(nc.vo.dahuan.ctbill.DhContractBVO.class,
                                                    getBodyCondition(nc.vo.dahuan.ctbill.DhContractBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dh_contract_b",bodyVOs1);
            } 
	         
	              
           nc.vo.dahuan.ctbill.DhContractB1VO[] bodyVOs2 =
                       (nc.vo.dahuan.ctbill.DhContractB1VO[])queryByCondition(nc.vo.dahuan.ctbill.DhContractB1VO.class,
                                                    getBodyCondition(nc.vo.dahuan.ctbill.DhContractB1VO.class,key));   
            if(bodyVOs2 != null && bodyVOs2.length > 0){
                          
              dataHashTable.put("dh_contract_b1",bodyVOs2);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dahuan.ctbill.DhContractBVO.class)
	   return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";
       	 if(bodyClass == nc.vo.dahuan.ctbill.DhContractB1VO.class)
	   return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
