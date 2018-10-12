package nc.ui.dahuan.fhtz;

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
	                         
           nc.vo.dahuan.fhtz.DhDeliveryDVO[] bodyVOs1 =
                       (nc.vo.dahuan.fhtz.DhDeliveryDVO[])queryByCondition(nc.vo.dahuan.fhtz.DhDeliveryDVO.class,
                                                    getBodyCondition(nc.vo.dahuan.fhtz.DhDeliveryDVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dh_delivery_d",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dahuan.fhtz.DhDeliveryDVO.class)
	   return "pk_dhdelivery = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
