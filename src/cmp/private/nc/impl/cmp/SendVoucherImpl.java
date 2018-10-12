package nc.impl.cmp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.impl.arap.proxy.Proxy;
import nc.itf.cmp.ISendVoucher;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.xmlchange.DOMUtils;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.cmpbill.outer.CmpBillAccessableBusiVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJ_IAccessableBusiVO;
import nc.vo.erm.control.YsControlVO;
import nc.vo.erm.util.ErVOUtils;
import nc.vo.fibill.outer.FiBillAccessableBusiVO;
import nc.vo.fibill.outer.FiBillAccessableBusiVOProxy;
import nc.vo.logging.Debug;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.w3c.dom.Document;

public class SendVoucherImpl implements ISendVoucher {

	public String doSendVoucherToNCSystem(String xml) throws BusinessException {
		
		
		String str="";
		PostFile ps=new PostFile();
		Document doc=DOMUtils.parseXMLDocument(xml);
		String corp=doc.getDocumentElement().getAttribute("receiver");
		String sql1="select defaultvalue from pub_sysinittemp where initcode='TCL01' ";
		String sql2="select defaultvalue from pub_sysinittemp where initcode='TCL02' ";
		BaseDAO dao=new BaseDAO("anju");
		Object obj1=dao.executeQuery(sql1, new ColumnProcessor());
		Object obj2=dao.executeQuery(sql2, new ColumnProcessor());
		if(obj1==null||"".equals(obj1)||obj2==null||"".equals(obj2)){
			return "请先配置集团参数TCL01,TCL02，用于获取数据库连接信息！";
		}
		String url=obj1+"/service/XChangeServlet?account="+obj2+"&receiver="+corp+"&langcode=simpchn";
		try {
			str=ps.sendDocument(doc, url, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}

	public String getZJZFYSBugetFromNCSystem(String args) throws BusinessException {
		
		String[] str1=args.split(";");
		if(str1==null||str1.length!=4){
			return "tcl001参数错误，确认参数类型str1[日期];str2[收支项目编码];str3[工程辅助项目编码];str4[公司编码]";
		}
		
		BaseDAO dao=new BaseDAO("anju");
		UFDate data=null;
		try {
			data=new UFDate(str1[0]);
		} catch (Exception e) {
			return "tcl001日期格式不正确[0000-00-00]!";
		}
		
		String sqlcorp="select pk_corp from bd_corp where unitcode='"+str1[3]+"'";
		Object corp=dao.executeQuery(sqlcorp, new ColumnProcessor());
		if(corp==null||"".equals(corp)){
			return "tcl001公司编码不存在，请重新传递！";
		}
		String sql1="select pk_costsubj from bd_costsubj where pk_corp='0001' and costcode='"+str1[1]+"'";//集团分配收支项目
		Object obj=dao.executeQuery(sql1, new ColumnProcessor());
		if(obj==null||"".equals(obj)){
			return "tcl001收支项目编码不存在，请重新传递！";
		}
		String sql2="select pk_jobbasfil from bd_jobbasfil where jobcode='"+str1[2]+"'";
		Object obj2=dao.executeQuery(sql2, new ColumnProcessor());
		if(obj2==null||"".equals(obj2)){
			return "tcl001工程辅助项目编码不存在，请重新传递！";
		}
		
		FiBillAccessableBusiVOProxy voProxy = null;
		DJZBItemVO ivo=new DJZBItemVO();
		DJZBHeaderVO hvo=new DJZBHeaderVO();
		hvo.setDjdl("fj");
		hvo.setDwbm(corp.toString());
		hvo.setDjlxbm("F5-01");
		hvo.setDjrq(data);
		hvo.setSzxmid(obj.toString());
		ivo.setSzxmid(obj.toString());
		hvo.setZyx1(obj2.toString());
		
		UFDouble ud1=new UFDouble(0);
		UFDouble ud2=new UFDouble(0);
		UFDouble ud3=new UFDouble(0);
		try {
			voProxy = getFiBillAccessableBusiVOProxy(ivo, hvo);
			nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(
					new nc.vo.ntb.outer.IAccessableBusiVO[] { voProxy });
			if(vos==null||vos.length<=0){
				Debug.debug("没有定义控制方案！");
				return "tcl001没有定义控制方案！";
			}
			for(NtbParamVO nvo:vos){
				
				if(nvo.getPlanname().startsWith("零预算")){
					Debug.debug("没有定义控制方案！");
					return "tcl001没有定义控制方案！";
				}
				
				String [] attrs=nvo.getBusiAttrs();
				HashMap<String, String> map=new HashMap<String, String>();
				for(String attr:attrs){
					map.put(attr, attr);
				}
				
				if((attrs.length==2&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid"))||
						(attrs.length==3&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid")&&map.containsKey("zb.zyx1"))){//仅公司+项目预算
					ud1=new UFDouble(nvo.getPlanData().doubleValue(),2);
					ud2=new UFDouble(nvo.getRundata()[0].doubleValue(),2);
					ud3=new UFDouble(nvo.getBalance().doubleValue(),2);
				}
			}
		} catch (Exception e) {
			
			return "tcl001没有预算执行情况！"+e.getMessage();
		}
		
		return ud1.toString()+";"+ud2.toString()+";"+ud3.toString();
	}
	
	public String getFYBXYSBugetFromNCSystem(String args) throws BusinessException {
		
		String[] str1=args.split(";");
		if(str1==null||str1.length!=4){
			return "tcl002参数错误，确认参数类型str1[日期];str2[收支项目编码];str3[工程辅助项目编码];str4[公司编码]";
		}
		
		BaseDAO dao=new BaseDAO("anju");
		UFDate data=null;
		try {
			data=new UFDate(str1[0]);
		} catch (Exception e) {
			return "tcl002日期格式不正确[0000-00-00]!";
		}
		
		String sqlcorp="select pk_corp from bd_corp where unitcode='"+str1[3]+"'";
		Object corp=dao.executeQuery(sqlcorp, new ColumnProcessor());
		if(corp==null||"".equals(corp)){
			return "tcl002公司编码不存在，请重新传递！";
		}
		String sql1="select pk_costsubj from bd_costsubj where pk_corp='0001' and costcode='"+str1[1]+"'";//集团分配收支项目
		Object obj=dao.executeQuery(sql1, new ColumnProcessor());
		if(obj==null||"".equals(obj)){
			return "tcl002收支项目编码不存在，请重新传递！";
		}
		String sql2="select pk_jobbasfil from bd_jobbasfil where jobcode='"+str1[2]+"'";
		Object obj2=dao.executeQuery(sql2, new ColumnProcessor());
		if(obj2==null||"".equals(obj2)){
			return "tcl002工程辅助项目编码不存在，请重新传递！";
		}
		
		List<FiBillAccessableBusiVOProxy> voProxys = new ArrayList<FiBillAccessableBusiVOProxy>();
		
		BXVO bvo=new BXVO();
		BXHeaderVO hvo=new BXHeaderVO();
		hvo.setFydwbm(corp.toString());
		hvo.setDjdl("bx");
		hvo.setDjlxbm("264X-01");
		hvo.setSzxmid(obj.toString());
		hvo.setZyx1(obj2.toString());
		hvo.setDjrq(data);
		hvo.setPk_corp(corp.toString());
		bvo.setParentVO(hvo);
		BXHeaderVO[] items = ErVOUtils.prepareBxvoItemToHeaderClone(bvo);
		
		UFDouble ud1=new UFDouble(0);
		UFDouble ud2=new UFDouble(0);
		UFDouble ud3=new UFDouble(0);
		try {
			for(BXHeaderVO item:items){
				YsControlVO vo = new YsControlVO();
				vo.setItems(new BXHeaderVO[]{item});
				voProxys.add(getFiBillAccessableBusiVOProxy(vo));
			}
			
			nc.vo.ntb.outer.NtbParamVO[] vos = Proxy.getILinkQuery().getLinkDatas(voProxys.toArray(new nc.vo.ntb.outer.IAccessableBusiVO[]{}));
			
			if(vos==null||vos.length<=0){
				Debug.debug("没有定义控制方案！");
				return "tcl002没有定义控制方案！";
			}
			
			for(NtbParamVO nvo:vos){
				
				if(nvo.getPlanname().startsWith("零预算")){
					Debug.debug("没有定义控制方案！");
					return "tcl002没有定义控制方案！";
				}
				
				String [] attrs=nvo.getBusiAttrs();
				HashMap<String, String> map=new HashMap<String, String>();
				for(String attr:attrs){
					map.put(attr, attr);
				}
				
				if((attrs.length==2&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid"))||
						(attrs.length==3&&map.containsKey("zb.dwbm")&&map.containsKey("fb.szxmid")&&map.containsKey("zb.zyx1"))){//仅公司+项目预算
					ud1=new UFDouble(nvo.getPlanData().doubleValue(),2);
					ud2=new UFDouble(nvo.getRundata()[0].doubleValue(),2);
					ud3=new UFDouble(nvo.getBalance().doubleValue(),2);
				}	
			}
			
		} catch (Exception e) {
			return "tcl002没有预算执行情况！";
		}
		
		return ud1.toString()+";"+ud2.toString()+";"+ud3.toString();
	}
	
	private FiBillAccessableBusiVOProxy getFiBillAccessableBusiVOProxy(DJZBItemVO selectedItem, DJZBHeaderVO head) {
		FiBillAccessableBusiVOProxy voProxy;
		FiBillAccessableBusiVO vo;
		String isyscode = null;
		if("ss".equals(head.getDjdl()) || "yt".equals(head.getDjdl())
				|| "sj".equals(head.getDjdl()) || "fj".equals(head.getDjdl()) || "hj".equals(head.getDjdl()))
		{
			vo = new CmpBillAccessableBusiVO();
			((CmpBillAccessableBusiVO)vo).setHead(head);
			((CmpBillAccessableBusiVO)vo).setItem(selectedItem);
			if ("ss".equals(head.getDjdl()))
				isyscode = "ss";
			else if ("yt".equals(head.getDjdl()))
				isyscode = "yt";
			else if ("sj".equals(head.getDjdl()) || "fj".equals(head.getDjdl()) || "hj".equals(head.getDjdl()))
				isyscode = "cmp";
		}
		else {
			vo = new DJ_IAccessableBusiVO();
			((DJ_IAccessableBusiVO)vo).setHead(head);
			((DJ_IAccessableBusiVO)vo).setItem(selectedItem);
			isyscode = "arap";
		}
		voProxy = new FiBillAccessableBusiVOProxy(vo, isyscode);
		voProxy.setLinkQuery(true);
		return voProxy;
	}
	
	private FiBillAccessableBusiVOProxy getFiBillAccessableBusiVOProxy(FiBillAccessableBusiVO vo) {
		FiBillAccessableBusiVOProxy voProxy;
		voProxy = new FiBillAccessableBusiVOProxy(vo, BXConstans.ERM_PRODUCT_CODE_Lower);
		voProxy.setLinkQuery(true);
		return voProxy;
	}

}
