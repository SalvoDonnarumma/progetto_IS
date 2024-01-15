<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>   
<!DOCTYPE html>
<html lang="it">
<head>
	<meta charset="ISO-8859-1">
	<title>Homepage</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/style.css">
	<jsp:include page="header.jsp" flush="true"/>
</head>
<body>
	<section class="box">
		<video src="images/scuba-diving-248.mp4" autoplay muted loop> </video>
		<h1> OctoPlus </h1>
		<h4> Cerca gli articoli di cui hai bisogno </h4>
		<br>
		<a href="store.jsp" class="boxBtn"> Entra nello store </a>
	</section>
	
	<jsp:include page="footer.jsp" flush="true"/>	
</body>
</html>