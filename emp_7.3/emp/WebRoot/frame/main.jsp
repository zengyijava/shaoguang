<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
    String tkn = request.getParameter("tkn");
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
        <style type="text/css">
            #load-bg{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                z-index:99999;
                -moz-opacity: 0.4;
                opacity:.40;
                filter: alpha(opacity=40);
                background:#eee url('<%=path %>/common/img/loading-bg.gif') no-repeat center
            }
        </style>
	</head>
	<body >
        <form action="" method="post">
            <input type="hidden" name="tkn" id="tkn" value=""/>
        </form>
        <div id="load-bg"></div>
        <script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript">
            var path = "<%=path%>";
            var tkn = "<%=tkn%>";
	        $(function(){
                $.ajax({
                    url:path+"/init.htm",
                    method:"POST",
                    async:true,
                    data:{
                        tkn:tkn,
                        isAsync:"yes"
                    },
                    beforeSend:function(){
                        $('#load-bg').show();
                    },
                    success:function(result) {
                        if (result == "outOfLogin") {
                            location.href = path + "/common/logoutEmp.jsp";
                        }else if(result == 'false'){
                            location.href = path + "/common/logoutEmp.jsp";
                        }else{
                            result = result || 'frame3.0';
                            var arrs = result.split("&");
                            var $form = $('form');
                            $('#tkn').val(arrs[1]);
                            $('#load-bg').hide();
                            $form.attr('action', path + "/frame/"+arrs[0]+"/main.jsp");
                            $form.submit();
                        }

                    }
                });
	        })
        </script>
	</body>
</html>
