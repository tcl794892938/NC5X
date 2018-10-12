package nc.ui.dahuan.htinfo.htchange;

import java.util.Hashtable;

import nc.vo.dahuan.htinfo.htchange.HtChangeDtlEntity;


/**
 *
 *�����ҵ�������
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
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		if(bodyClass == HtChangeDtlEntity.class){
			return " pk_conchange = '"+key+"' and isnull(dr,0)=0 ";
		}
       		
	 return null;
       } 
}
