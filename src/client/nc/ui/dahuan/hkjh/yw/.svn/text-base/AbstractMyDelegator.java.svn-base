package nc.ui.dahuan.hkjh.yw;

import java.util.Hashtable;

import nc.vo.dahuan.hkjh.HkdhDVO;


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
	   
	  String condition = getBodyCondition(HkdhDVO.class,key);
	  HkdhDVO[] dvos = (HkdhDVO[])queryByCondition(HkdhDVO.class, condition);
	  
	  if(null != dvos && dvos.length > 0){
		  dataHashTable.put("dh_hkdh_d", dvos);
	  }
	   
	              	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		if(bodyClass == HkdhDVO.class){
			return " pk_hkdh = '"+key+"' and isnull(dr,0)=0 ";
		}
       		
	 return null;
       } 
	
}
