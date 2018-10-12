package nc.ui.dahuan.fksq;

import java.util.Hashtable;


public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.dahuan.fksq.DhFksqbillBVO[] bodyVOs1 =
                       (nc.vo.dahuan.fksq.DhFksqbillBVO[])queryByCondition(nc.vo.dahuan.fksq.DhFksqbillBVO.class,
                                                    getBodyCondition(nc.vo.dahuan.fksq.DhFksqbillBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dh_fksqbill_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dahuan.fksq.DhFksqbillBVO.class)
	   return "pk_fksqbill = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
