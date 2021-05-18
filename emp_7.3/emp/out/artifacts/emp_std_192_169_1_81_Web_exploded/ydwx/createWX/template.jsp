<%@ page language="java" import="java.util.*,com.montnets.emp.netnews.entity.Images" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.netnews.daoImpl.*" %>
<%@ page import="java.sql.SQLException" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%!
	Wx_ueditorDaoImpl dataAccess=new Wx_ueditorDaoImpl();//存取数据用
 %>
<%! //定义函数 getImage 根据传入的类别，页码，返回Images的List
	//每一页6幅图像
	public ArrayList<Images> getImages(int section,int page){
		Map<Long,String> imageMap=new HashMap<Long,String>();
		ArrayList<Images>	imageList=new ArrayList<Images>();
		try{
			imageMap=dataAccess.getPageImage(section,page);
		}
		catch(Exception e){
			EmpExecutionContext.error(e,"获取页面图片异常！");
		}
		for(Long i:imageMap.keySet()){
			Images image=new Images();
			image.setId(i);
			image.setName("模板"+i);
			image.setUrl(imageMap.get(i));
			imageList.add(image);
		}
		return imageList;
	}
 %>

<%!
	//定义函数pageCount传入类别，返回页数
	public int pageCount(int section){
		int total=0;
		total=dataAccess.getSectionSize(section);
		if(total==0)return 1;
		else if(total%6==0)return total/6;
		else return total/6+1;
	}
 %>

  <%!//取得默认的模板类别
  	 public Long getDefaultSection(){
  	 	return dataAccess.getMinSection();
  	 }
   %>
<div id="templateContainer" >
<%
	String section=request.getParameter("section");
	String pageNum=request.getParameter("pageNum");
	if(section==null||section=="")
	{
		section=Long.toString(getDefaultSection());
	}
	if(pageNum==null||pageNum=="")
	{
		pageNum="1";
	}
	ArrayList<Images> list=getImages(Integer.parseInt(section),Integer.parseInt(pageNum));
	session.setAttribute("images",list);
%>
<div align="center">
<%
	int totalPageNum = pageCount(Integer.parseInt(section));
	if(totalPageNum>1){
		out.println("<font size='2'>");
		out.print("共"+totalPageNum+"页 当前第"+pageNum+"页");
		out.println("</font>");
	}
 	if(Integer.parseInt(pageNum)==1 && totalPageNum>1)
 	{
 		out.println(MessageUtils.extractMessage("ydwx","ydwx_common_btn_shangyiye",request));
 	}
 	else if(Integer.parseInt(pageNum)!=1 && totalPageNum>1) 
 	{
		out.println("<a  href='javascript:showPreviousPage()'>"+MessageUtils.extractMessage("ydwx","ydwx_common_btn_shangyiye",request)+"</a>");
 	}
 %>
 
 <%
 	if(Integer.parseInt(pageNum)==totalPageNum && totalPageNum>1)
 		out.println( MessageUtils.extractMessage("ydwx","ydwx_common_btn_xiayiye",request));
 	else if( totalPageNum>1)
 		out.println("<a  href='javascript:showNextPage()'>"+MessageUtils.extractMessage("ydwx","ydwx_common_btn_xiayiye",request)+"</a>");
  %>
</div>
<div style="margin-left:20px">
<table CellSpacing=10 align='left'>
<%
	out.println("<tr>");
	for(int i=0;i<list.size();i++){
		out.println("<td align='left'>");
		out.println("<a href=\"javascript:templateApply('"+list.get(i).getId()+"')\">");
		out.println("<img src='"+request.getContextPath()+ list.get(i).getUrl() +"' style='width: 100px;height: 100px;' message="+"\"<img width='229' height='386' src='"+request.getContextPath()+"/ydwx/images/templateImage"+"/"+list.get(i).getId()+".jpg"+"'/>\">");
		out.println("</a>");
		out.println("<br/>");
		out.println("<a href=\"javascript:templateApply('"+list.get(i).getId()+"')\">");
		out.println("<font size='2'>");
		/*预览*/
		out.println(MessageUtils.extractMessage("ydwx","ydwx_common_yulan",request));
		out.println("</font>");
		out.println("</a>");
		out.println("&nbsp;&nbsp;");
		out.println("<a href=\"javascript:templateApplyConfirm('"+list.get(i).getId()+"')\">");
		out.println("<font size='2'>");
		/*应用*/
		out.println(MessageUtils.extractMessage("ydwx","ydwx_common_yingyong",request));
		out.println("</font>");
		out.println("</a>");
		out.println("</td>");
		if((i+1)%2==0){
			out.println("</tr><tr>");
		}
	}
	out.println("</tr>");	
 %>
</table>
</div>

</div>
