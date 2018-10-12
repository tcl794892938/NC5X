package nc.ui.dahuan.htinfo.htchange;

import java.util.Hashtable;

import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;


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
	   
	   HtChangeDtlEntity[] htcdtls = (HtChangeDtlEntity[])queryByCondition(HtChangeDtlEntity.class, getBodyCondition(HtChangeDtlEntity.class,key));
	   if(null != htcdtls && htcdtls.length >0 ){
		   dataHashTable.put("dh_conchange_d", htcdtls);
	   }
	              	   return dataHashTable;
		
	}
	
	
/**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		if(bodyClass == HtChangeDtlEntity.class){
			return " pk_conchange = '"+key+"' and isnull(dr,0)=0 ";
		}
       		
	 return null;
       } 
}
