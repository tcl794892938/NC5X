package nc.ui.arap.sedgather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.sedgather.sale.SaleBXBVO;
import nc.vo.arap.sedgather.sk.DjskBXBVO;

public class SKDataSource implements IDataSource {
	
	public List<SaleBXBVO> blist;
	public Map<String,String> namemaps;
	public List<String> strlist;
	public String pk_sale;
	
	public SKDataSource(String pksale,String nodecode) {
		super();
		IUAPQueryBS iQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try{
			pk_sale = pksale;
			blist = (List<SaleBXBVO>)iQ.retrieveByClause(SaleBXBVO.class, " csaleid = '"+pk_sale+"' and nvl(dr,0) = 0 ");
			
			String sql = "select vvarexpress,vvarname from pub_print_dataitem where vnodecode = '"+nodecode+"' and nvl(dr,0)=0";
			List<Map<String,String>> listmap = (List<Map<String,String>>)iQ.executeQuery(sql, new MapListProcessor());
			namemaps = new HashMap<String, String>();
			if(null != listmap && listmap.size() > 0 ){
				for(Map<String,String> map : listmap){
					namemaps.put(map.get("vvarexpress"), map.get("vvarname"));
				}
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}

	public String[] getAllDataItemExpress() {

		strlist = new ArrayList<String>();
		
		if(null != pk_sale && !"".equals(pk_sale)){
			
			strlist.add("h_csaleid");
			
			if(null != blist && blist.size() > 0){
				SaleBXBVO bvo = blist.get(0);
				String[] b_attrs = bvo.getAttributeNames();
				for(String b_attr : b_attrs){
					strlist.add("b_"+b_attr);
				}
				
			}
			
			return strlist.toArray(new String[0]);
			
		}
		
		return null;
	}

	public String[] getAllDataItemNames() {
		if(null != namemaps && namemaps.size() > 0){
			String[] itemnames = new String[strlist.size()];
			
			for(int i=0;i<strlist.size();i++){
				String itemexpress = strlist.get(i);
				itemnames[i] = namemaps.get(itemexpress);
			}
			
			return itemnames;
		}
		return null;
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		
		if(null != itemExpress){
			String attr = itemExpress.substring(2);
			
			if("h_csaleid".equals(itemExpress)){
				
				return new String[]{pk_sale};
				
			}else if(itemExpress.startsWith("b_")){
				String[] values = new String[blist.size()];
				for(int i=0;i<blist.size();i++){
					SaleBXBVO bvo = blist.get(i);
					String bvalue = bvo.getAttributeValue(attr)==null ? "" :bvo.getAttributeValue(attr).toString();
					values[i]=bvalue;
				}
				return values;
			}
		}
		
		return null;
	}

	public String getModuleName() {
		return null;
	}

	public boolean isNumber(String itemExpress) {
		return false;
	}

}
