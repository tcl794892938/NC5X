package nc.impl.ws.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.ws.service.DOMUtils;
import nc.itf.ws.service.Pzcxtest;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.logging.Debug;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pz.PZVO;

import org.w3c.dom.Document;

public class MobileServiceImpl implements Pzcxtest {


	public String qryPZ(String s) throws Exception {
		
		
		String[] ssss=s.split(";");
		String year=ssss[0];
		String month=ssss[1];
		String code=ssss[2];
		
		String sql1="select cuserid from sm_user where user_code='"+code+"' and nvl(dr,0)=0";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj1=iQ.executeQuery(sql1, new ColumnProcessor());
		if(obj1==null){
			throw new Exception("���û������ڣ�");
		}
		
		String sql2="select no from gl_voucher where pk_prepared='"+obj1.toString()+"' and nvl(dr,0)=0 and " +
				" year='"+year+"' and period='"+month+"' order by no";
		List<Object> oobj1=(List<Object>)iQ.executeQuery(sql2, new ColumnListProcessor());
		
		LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
		
		for(Object obj : oobj1){
			map.put(obj.toString(), obj);
		}
		
		String sql3="select b.* from rq a left join pzcx b on a.pk_rq=b.pk_rq  where nvl(a.dr,0)=0 and nvl(b.dr,0)=0 " +
				" and a.styear='"+year+"' and a.stmonth='"+month+"' and b.code='"+code+"'";
		PZVO  pzvo=(PZVO) iQ.executeQuery(sql3, new BeanProcessor(PZVO.class));
		if(pzvo==null){
			throw new Exception("���û���ƾ֤���䣡");
		}
		Map<String, Object> nmap=new HashMap<String, Object>();
		
		if(pzvo.getQj1()!=null && !pzvo.getQj1().equals("")){
			String s1=pzvo.getQj1().substring(0,pzvo.getQj1().indexOf('-') );
			String s2=pzvo.getQj1().substring(pzvo.getQj1().indexOf('-')+1,pzvo.getQj1().length());
			int a=Integer.parseInt(s1);
			int b=Integer.parseInt(s2);
			for(Integer i=a;i<=b;i++){
				nmap.put(i.toString(), i);
			}
			
		}
		if(pzvo.getQj2()!=null && !pzvo.getQj2().equals("")){
			String s1=pzvo.getQj2().substring(0,pzvo.getQj2().indexOf('-') );
			String s2=pzvo.getQj2().substring(pzvo.getQj2().indexOf('-')+1,pzvo.getQj2().length());
			int a=Integer.parseInt(s1);
			int b=Integer.parseInt(s2);
			for(Integer i=a;i<=b;i++){
				nmap.put(i.toString(), i);
			}
			
		}
		if(pzvo.getQj3()!=null && !pzvo.getQj3().equals("")){
			String s1=pzvo.getQj3().substring(0,pzvo.getQj3().indexOf('-') );
			String s2=pzvo.getQj3().substring(pzvo.getQj3().indexOf('-')+1,pzvo.getQj3().length());
			int a=Integer.parseInt(s1);
			int b=Integer.parseInt(s2);
			for(Integer i=a;i<=b;i++){
				nmap.put(i.toString(), i);
			}
			
		}
		
		
		
		for(String key : map.keySet()){
			if(nmap.containsKey(key)){
				nmap.remove(key);
			}
		}
		
		List<Integer> list=new ArrayList<Integer>();
		for(String key : nmap.keySet()){
			list.add(Integer.parseInt(key));
		}
		
		Integer[] a=list.toArray(new Integer[0]);
		
		Arrays.sort(a);
		String pzqj="";
		for(int i=0;i<a.length;i++){
			pzqj+=a[i]+";";
		}
		
		System.out.println(a);
		return pzqj;
	}

	public String doSendVoucherToNCSystem(String xml) throws Exception {
		Debug.debug("111");
		String str="";
		PostFile ps=new PostFile();
		Document doc=DOMUtils.parseXMLDocument(xml);
		Debug.debug("112");
		String corp=doc.getDocumentElement().getAttribute("receiver");
		String sql1="select defaultvalue from pub_sysinittemp where initcode='TCL01' ";
		String sql2="select defaultvalue from pub_sysinittemp where initcode='TCL02' ";
		BaseDAO dao=new BaseDAO("design");
		Object obj1=dao.executeQuery(sql1, new ColumnProcessor());
		Object obj2=dao.executeQuery(sql2, new ColumnProcessor());
		//obj1="http://218.2.111.162:9988";
		//obj2="01";
		if(obj1==null||"".equals(obj1)||obj2==null||"".equals(obj2)){
			return "�������ü��Ų���TCL01,TCL02�����ڻ�ȡ���ݿ�������Ϣ��";
		}
		Debug.debug("113");
		String url=obj1+"/service/XChangeServlet?account="+obj2+"&receiver="+corp+"&langcode=simpchn";
		try {
			str=ps.sendDocument(doc, url, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			Debug.debug("114");
		}
		
		return str;
	}
	
	public String DeletePZ (String s)throws Exception{
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql1="select distinct billpk from xx_idcontrastpk where pk_docid='"+s+"' and nvl(dr,0)=0";
		Object obj1=iQ.executeQuery(sql1, new ColumnProcessor());
		if(obj1==null || "".equals(obj1)){
			return "��id�����ڣ�";
		}
//		BaseDAO dao=new BaseDAO("design");
//		
//		dao.deleteByClause(DetailVO.class," pk_voucher='"+obj1.toString()+"'");
//		dao.deleteByClause(VoucherVO.class," pk_voucher='"+obj1.toString()+"'");
		
		Connection con = null;// ����һ�����ݿ�����
	    PreparedStatement pre = null;// ����Ԥ����������һ�㶼�������������Statement
	   // ResultSet result = null;// ����һ�����������

		 Class.forName("oracle.jdbc.driver.OracleDriver");// ����Oracle��������
	        System.out.println("��ʼ�����������ݿ⣡");
	        
	        String sqla="select defaultvalue from pub_sysinittemp where initcode='CWF01' ";
			String sqlb="select defaultvalue from pub_sysinittemp where initcode='CWF02' ";
			String sqlc="select defaultvalue from pub_sysinittemp where initcode='CWF03' ";
			String sqld="select defaultvalue from pub_sysinittemp where initcode='CWF04' ";
			BaseDAO dao=new BaseDAO("design");
			Object obja=dao.executeQuery(sqla, new ColumnProcessor());
			Object objb=dao.executeQuery(sqlb, new ColumnProcessor());
			Object objc=dao.executeQuery(sqlc, new ColumnProcessor());
			Object objd=dao.executeQuery(sqld, new ColumnProcessor());
			if(obja==null||"".equals(obja)||objb==null||"".equals(objb)||objc==null||"".equals(objc)||objd==null||"".equals(objd)){
				return "�������ü��Ų���CWF01,CWF02��CWF03,CWF04,���ڻ�ȡ���ݿ�������Ϣ��";
			}
	        
	        String url = "jdbc:oracle:" + "thin:@"+obja+":1521:"+objb;// 127.0.0.1�Ǳ�����ַ��XE�Ǿ����Oracle��Ĭ�����ݿ���
	        String user = objc.toString();// �û���,ϵͳĬ�ϵ��˻���
	        String password = objd.toString();// �㰲װʱѡ���õ�����
	        con = DriverManager.getConnection(url, user, password);// ��ȡ����
	        System.out.println("���ӳɹ���");
	        String sql = "delete  from gl_voucher where pk_voucher=?";// Ԥ������䣬�������������
	        pre = con.prepareStatement(sql);// ʵ����Ԥ�������
	        pre.setString(1, obj1.toString());// ���ò�����ǰ���1��ʾ�����������������Ǳ�������������
	        pre.executeUpdate();// ִ�в�ѯ��ע�������в���Ҫ�ټӲ���
	        
	        String sql2= "delete  from gl_detail where pk_voucher=?";
	        pre = con.prepareStatement(sql2);// ʵ����Ԥ�������
	        pre.setString(1, obj1.toString());// ���ò�����ǰ���1��ʾ�����������������Ǳ�������������
	        pre.executeUpdate();// ִ�в�ѯ��ע�������в���Ҫ�ټӲ���

		
		return "ɾ���ɹ���";
	}

}
