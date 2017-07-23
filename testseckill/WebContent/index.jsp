<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>商品大秒杀</h1>
	<form id="form" action="${pageContext.request.contextPath}/doseckill" method="post">
		<input type="hidden" name="proid" value="01" id="proid">
		<input type="button" value="点我秒杀" id="btn">
	</form>
	
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#btn").click(function(){
			var url=$("#form").attr("action");
			$.post(url,$("#form").serialize(),function(data){
				if(data=="false"){
					alert("抢光了");
					$("#btn").attr("disabled",true);
				}
			});			
		});
	});
</script>
</html>