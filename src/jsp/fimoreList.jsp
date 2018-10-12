<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="gbk"%>
<%@ page import="java.util.List,nc.vo.pub.msg.MessageVO,java.lang.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0">

 <style type="text/css">
      .out  {  background-color:#ffffff;   cursor:default;   }    
	  .click   {   background-color:#E8E8E8;   color:#ffffff;   cursor:default;   } 
        </style>

<title>财务事务列表</title>
<%

	MessageVO msgvo1 = new MessageVO();
	            msgvo1.setPrimaryKey("1001AA1000000002QMWX");
				msgvo1.setPk_billtype("DHHT");
				msgvo1.setPk_srcbilltype("1111111111111");
				msgvo1.setBillNO("DHHT1310150013");
				msgvo1.setBillPK("1001AA1000000002QMWO");
				msgvo1.setBusiTypePK("1001AA1000000002Q9KP");
				msgvo1.setMessageNote("合同信息审批");
				msgvo1.setTitle("合同信息审批");
				msgvo1.setCorpPK("1001");
				msgvo1.setActionTypeCode("SAVE");
				msgvo1.setSenderCode("fk");
				msgvo1.setSenderName("fk");
				msgvo1.setCheckerCode("xwb");
				msgvo1.setCheckerName("徐为斌");
	MessageVO[] list = new MessageVO[]{msgvo1};
	//List doneList = (List)request.getAttribute("doneList");
	//Object obj = request.getAttribute("todototal");
	String usercode = "XWB";//(String)request.getSession().getAttribute("usercode");
	String pwd= "1";//(String)request.getSession().getAttribute("pwd");
	int todototal =  1;//obj!=null ? Integer.parseInt(obj.toString()) : 0;
	int count = list!=null ? list.length : 0;
	int doneCount = 1;//doneList!=null ? doneList.size() : 0;
	String enip=  "http://127.0.0.1";//(String)request.getAttribute("enip");
	String accountcode= "001" ;//(String)request.getAttribute("accountcode");
	String cuserid= "0001AA10000000000N79" ;//(String)request.getAttribute("cuserid");
	
	//Object obj_defaultcount = request.getAttribute("defaultcount");
	int defaultcount = 1;//obj_defaultcount!=null? Integer.parseInt(obj_defaultcount.toString()) : 0;
	
%>
</head>
<script type="text/javascript">
	
    var xhr ;	//XML访问对象
	function createXMLHttpRequest() {
	//	if(window.XMLHttpRequest) { 	//Mozilla 浏览器
	//		xhr = new XMLHttpRequest();
	 // 	}else if (window.ActiveXObject) { // IE5、IE6浏览器
		    xhr = new ActiveXObject("Microsoft.XMLHTTP");
	//  	}
	}

	function sendRequest(url){  
	createXMLHttpRequest();   
	xhr.open("GET",url,true);  
	xhr.setRequestHeader("Content-Type","text/html;charset=utf-8");    
	xhr.send();  
 
}  

	function openNCNode2(messageinfo,pk_corp,usercode,pwd,enip,accountcode,obj){
	try
	{
		var myDate = new Date();	
		var key="111111"; //myDate.getYear()+''+myDate.getMonth()+''+myDate.getDate()+''+myDate.getHours()+''+myDate.getMinutes()+''+myDate.getSeconds()+''+myDate.getMilliseconds();	
		var year = myDate.getFullYear();
		var month = myDate.getMonth() + 1;
		month = month < 10 ? ("0" + month) : month;
		var dt = myDate.getDate();
		dt = dt < 10 ? ("0" + dt) : dt;
		var today = year + "-" + month + "-" + dt;
		var url=enip+'/service/RegisterServlet?key='+key+'&workdate='+today+'&language=simpchn&usercode='+usercode+'&pwd='+pwd+'&accountcode='+accountcode+'&pkcorp='+pk_corp;	
		sendRequest(url);
			
		//execNCAppletFunction("nc.ui.mk.webcall.PortalInNCClient","openMsgPanel",messageinfo,key,enip);
		execNCAppletFunction("nc.ui.sm.webcall.OpenNCNode","openNode","10080806",key,enip);
		
	}
	catch(error)
	{
		alert(error);
	}	
	clicked(obj);
	}


	function execNCAppletFunction(className,methodName,argStr,key,enip)
{
	
   	var ncFrame = document.getElementById("123"); 
	//此处注意gateUrl的key参数是根据单点登录情况动态形成的
	var gateUrl = "http://127.0.0.1/login.jsp?key=111111";
   //	if(ncFrame == null)
   	//	ncFrame=initNCFrame(gateUrl);
		ncFrame=initNCFrame2(key,enip);
   //	showProgressPopup();
	waitLoadNCApplet(className,methodName,argStr);
}

	function waitLoadNCApplet(className,methodName,argStr)
	{
	var applet =null;

	try{
		var ncFrame =document.getElementById("123");
		if(ncFrame != null)
		applet =ncFrame.contentWindow.document.applets["NCApplet"];
		//log("ncFrame=" + ncFrame + " applet=" + applet);
	}
	catch(error){
		//hideProgressPopup();
		//showErrorDialog("get applet error:" + error.name + ":" + error.message);
		alert(error);
		return;
	}
	
	if(applet == null)
	{
		setTimeout("waitLoadNCApplet('"+ className +"','" + methodName + "','" + argStr + "')",1000);
		return;
	}
	exec(className, methodName,argStr);
}


function exec(className,methodName,argStr)
{
	 try{
		 var ncFrame =document.getElementById("123");
    	var applet = ncFrame.contentWindow.document.applets["NCApplet"];
   		if(applet!=null){
		 	applet.callNC(className,methodName,argStr);
		}
    		
    }catch(error)
    {
    	//hideProgressPopup();
        //showErrorDialog(error.name + "  " + error);
		alert(error);
    }
	
}
	
	function initNCFrame2(key,enip)
	{
	var frameID = "123";
	var frame = frameID;
		frame = document.createElement("iframe");
		frame.id = frameID;
		frame.style.position = "relative";
		frame.style.left = "0";
		frame.style.top = "0";
		frame.style.width = 1;
		frame.style.height = 1;
		frame.frameBorder = 0;
		frame.width = 0;
		frame.height = 0;
		frame.src =enip+'/login.jsp?key='+key;
		document.body.appendChild(frame);

	return frame;
	}
	function openNCNode3(messageinfo,pk_corp,usercode,pwd,enip,accountcode,msgpk,userpk){
	try
	{
		var myDate = new Date();	
		var key=myDate.getYear()+''+myDate.getMonth()+''+myDate.getDate()+''+myDate.getHours()+''+myDate.getMinutes()+''+myDate.getSeconds()+''+myDate.getMilliseconds();	
		var year = myDate.getFullYear();
		var month = myDate.getMonth() + 1;
		month = month < 10 ? ("0" + month) : month;
		var dt = myDate.getDate();
		dt = dt < 10 ? ("0" + dt) : dt;
		var today = year + "-" + month + "-" + dt;
		var url=enip+'/service/RegisterServlet?key='+key+'&workdate='+today+'&language=simpchn&usercode='+usercode+'&pwd='+pwd+'&accountcode='+accountcode+'&pkcorp='+pk_corp;	
		sendRequest(url);
		execNCAppletFunction3("nc.ui.mk.webcall.PortalInNCClient","openMsgPanel2",messageinfo,key,enip,msgpk,userpk);
		//execNCAppletFunction("nc.ui.sm.webcall.OpenNCNode","openNode","10080806",ramdom);
	}
	catch(error)
	{
		alert(error);
	}	
	}
	
	
	function execNCAppletFunction3(className,methodName,argStr,key,enip,msgpk,userpk)
{
	
   	var ncFrame = document.getElementById("123"); 
	
   	if(ncFrame == null)  	
		ncFrame=initNCFrame2(key,enip); 
	waitLoadNCApplet3(className,methodName,argStr,msgpk,userpk);
}

	function waitLoadNCApplet3(className,methodName,argStr,msgpk,userpk)
	{
	var applet =null;
	try{
		var ncFrame =document.getElementById("123");
		if(ncFrame != null)
		applet =ncFrame.contentWindow.document.applets["NCApplet"];
		//log("ncFrame=" + ncFrame + " applet=" + applet);
	}
	catch(error){
		//hideProgressPopup();
		//showErrorDialog("get applet error:" + error.name + ":" + error.message);
		alert(error);
		return;
	}	
	if(applet == null)
	{
		setTimeout("waitLoadNCApplet('"+ className +"','" + methodName + "','" + argStr + "')",1000);
	}
	exec3(className,methodName,argStr,msgpk,userpk);
}


function exec3(className,methodName,argStr,msgpk,userpk)
{
	 try	{
		 var ncFrame =document.getElementById("123");
    	var applet = ncFrame.contentWindow.document.applets["NCApplet"];
   		if(applet!=null){
    	//hideProgressPopup();
		 	applet.callNC(className,methodName,msgpk+";"+userpk);
		}
    		
    }catch(error)
    {
    	//hideProgressPopup();
        //showErrorDialog(error.name + "  " + error);
		alert(error);
    	//showMessageDialog("由于NC与Portal不在同一个域中，因此此功能失效！");
    }
	
}
	function clicked(obj){  
    var mytable = document.getElementById("table")  
    for(var i=0;i<mytable.rows.length;i++){  
        if(obj == mytable.rows[i].cells[1] || obj==mytable.rows[i].cells[4]){  	
	 mytable.rows[i].cells[0].className='click';
	 mytable.rows[i].cells[1].className='click';
 	 mytable.rows[i].cells[2].className='click';
	 mytable.rows[i].cells[3].className='click';
	 mytable.rows[i].cells[4].className='click';
        } 
    }  
 }  
		
	function clearNCFrame()
	{
	var frameID = "123";
	var frame = frameID;
	if(frame != null)
	{
		//document.bodye.removeChild(frame);
	}
	}
	;

	
	
</script>
<body   onload="clearNCFrame()"  >
	<table align="center" border="1" borderColor=#E8E8E8 width="850" cellspacing="0" cellpadding="0" style="border-collapse:collapse;font-size:13px;text-align:left;" bordercolor="threeddarkshadow" id="table">
		<tr height="30">
			<td align="center" width="50">&nbsp;</td>
			<td align="center" width="45%"><b>主题</b></td>
			<td align="center" width="100"><b>发送人</b></td>
			<td align="center" width="120"><b>发送时间</b></td>
			<td align="center" width="80"><b>操作</b></td>
		</tr>
		<%
			if(list!=null && list.length>0){
				String key= (String)request.getAttribute("key");
				for(int i=0;i<count;i++){
					if(i==defaultcount)
					{
					break;
					}
					MessageVO msgvo = list[i];
				//	String mssageinfo=org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(msgvo);			
				StringBuffer mssageinfo = new StringBuffer();
				mssageinfo.append("test").append(";");//1
				mssageinfo.append(msgvo.getPrimaryKey()).append(";");
				mssageinfo.append(msgvo.getPk_billtype()).append(";");
				mssageinfo.append(msgvo.getPk_srcbilltype()).append(";");
				mssageinfo.append(msgvo.getBillNO()).append(";");
				mssageinfo.append(msgvo.getBillPK()).append(";");//6
				mssageinfo.append(msgvo.getBusiTypePK()).append(";");
				mssageinfo.append(msgvo.getCorpPK()).append(";");
				mssageinfo.append(msgvo.getActionTypeCode()).append(";");
				mssageinfo.append(msgvo.getSenderCode()).append(";");
				mssageinfo.append(msgvo.getSenderName()).append(";");//11
				mssageinfo.append(msgvo.getCheckerCode()).append(";");//12
				mssageinfo.append(msgvo.getCheckerName()).append(";");//13

				if (null == msgvo.isCheck()) {//N
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.isCheck().toString()).append(";");
				}
				if (null == msgvo.getCheckNote()) {//
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getCheckNote().toString()).append(";");
				}
				if (null == msgvo.getSendDateTime()) {
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getSendDateTime().toString()).append(";");
				}
				if (null == msgvo.getDealDateTime()) {
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getDealDateTime().toString()).append(";");
				}
 				if (null == msgvo.getMessageNote()) {
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getMessageNote().toString()).append(";");
				}
				if (null == msgvo.getTitle()) {
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getTitle().toString()).append(";");
				}
				if (null == msgvo.getMailAddress()) {
					mssageinfo.append("").append(";");
				} else {
					mssageinfo.append(msgvo.getMailAddress().toString()).append(";");
				}
				mssageinfo.append(String.valueOf(msgvo.getMsgType())).append(";");
				mssageinfo.append(String.valueOf(msgvo.getPriority()));

				String strmessage=mssageinfo.toString();

				String pk_corp=msgvo.getCorpPK();
		%>
					<tr height="30">
						<td align="center"><b><%=i+1%></b></td>
						<td onclick="openNCNode2('<%=strmessage%>','<%=pk_corp%>','<%=usercode%>','<%=pwd%>','<%=enip%>','<%=accountcode%>',this);"><b style="font-weight:normal;cursor:pointer;color:#0000FF;" 
								onmouseover="this.style.color='#FF0000'" 
								onmouseout="this.style.color='#0000FF'" >
								<%=msgvo.getTitle()%>
							</b>
						</td>
						<td><%=msgvo.getSenderName()!=null ? msgvo.getSenderName() :"" %></td>
						<td><%=msgvo.getSendDateTime() %></td>
						<td align="center" style="cursor:pointer;" 
								onmouseover="this.style.color='#FF0000'" 
								onmouseout="this.style.color='#000000'" 
							onclick="openNCNode2('<%=strmessage%>','<%=pk_corp%>','<%=usercode%>','<%=pwd%>','<%=enip%>','<%=accountcode%>',this);">查看</td>
					</tr>		
		<%			
				}
			}
		%>
	</table>
	
</body>
</html>