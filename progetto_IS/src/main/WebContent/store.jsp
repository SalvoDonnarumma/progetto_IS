<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        
<!DOCTYPE html>
<html lang="it">
<%@ page import="java.util.*"%>
<head>
<script src="<%=request.getContextPath()%>/scripts/store.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js">
</script>
<script>
	$(document).ready(function(){
		dynamicStore("<%=request.getContextPath()%>/StoreServlet");
	});	
</script>
<meta charset="ISO-8859-1">
	<title>OCTOPLUS</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/store.css">	
	<link rel="stylesheet" href="https://unpkg.com/boxicons@latest/css/boxicons.min.css">
	<jsp:include page="header.jsp" flush="true"/>
</head>
<body> 	
 	<nav class="topnav">
		<div class="dropdown">
		  <label for="categoria-select">Categoria:</label>
		  <select id="categoria-select" onchange="searchAndFilter()">
		    <option value="">Tutte</option>
		    <option value="Coltelli">Coltelli</option>
		    <option value="Erogatori">Erogatori</option>
		    <option value="Guanti">Guanti</option>
		    <option value="Maschere">Maschere</option>
		    <option value="Mute">Mute</option>
		    <option value="Pinne">Pinne</option>
		    <option value="Torce">Torce</option>
		  </select>
		</div>
	  <div class="dropdown">
	    <label for="prezzo-select">Prezzo:</label>
	    <select id="prezzo-select" onchange="searchAndFilter()">
	      <option value="">Tutti</option>
	      <option value="0-50">0 - 50</option>
	      <option value="50-150">50 - 150</option>
	      <option value="150-250">150 - 250</option>
	      <option value="250-400">250 - 400</option>
	      <option value="400-500">400 - 500</option>
	      <option value="500-1000000"> 500+ </option>
	    </select>
	  </div>
	  <form action="#" method="get" class="search-form">
    			<input type="text" id="search-input" onkeyup="searchAndFilter()" placeholder="Cerca..." class="search-input">
     			<button type="submit" onClick="searchAndFilter()" class="search-button"><i class='bx bx-search'></i></button>
  	  </form>
	</nav>
 	<!-- shop start -->
 	<section class="shop" id="shop">
 		<div class="container" id="prodotti"> 		
 		</div>		
	</section>
	<br>
	<br>
	<ul class="pagination" id="pagination"></ul>
	<br>
	<br>
	<footer>
	<jsp:include page="footer.jsp" flush="true"/>		
	</footer>
</body>
</html>
