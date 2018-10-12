package nc.ui.dahuan.fkgx;

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
	                         
           nc.vo.dahuan.fkgx.DhFkgxDVO[] bodyVOs1 =
                       (nc.vo.dahuan.fkgx.DhFkgxDVO[])queryByCondition(nc.vo.dahuan.fkgx.DhFkgxDVO.class,
                                                    getBodyCondition(nc.vo.dahuan.fkgx.DhFkgxDVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dh_fkgx_d",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dahuan.fkgx.DhFkgxDVO.class)
	   return "pk_fkgx = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
