package nc.ui.st.pz;

import java.util.Hashtable;

import nc.vo.pz.PZVO;


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
	                         
          PZVO[] bodyVOs1 =
                       (PZVO[])queryByCondition(PZVO.class,
                                                    getBodyCondition(PZVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("pzcx",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == PZVO.class)
	   return " pk_rq = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
