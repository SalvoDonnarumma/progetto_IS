 <%
 Utente bean = (Utente) request.getSession().getAttribute("logged"); //quando una persona si logga salvo i suoi dati nella sessione
 	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equals("Utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}
 %>
<!DOCTYPE html>
<html lang="it">
<%@ page import="java.util.*,gestioneprodotti.Prodotto,storagelayer.ProductDaoDataSource,gestioneutenti.Utente"%>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Lista Acquisti</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/listproduct.css">
   <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="scripts/dynamicproducts.js"></script>
  <script>
	$(document).ready(function(){
		dynamicOrderView("<%=request.getContextPath()%>/visualizzaDettagliOrdine?id=<%=request.getParameter("id")%>");
	});	
  </script> 
  <jsp:include page="header.jsp" flush="true"/>
</head>
<body>
  <br>
  <br>
  <div id="container_products" class="container_products">
  </div>
  <br>
  <div class="center">
  	<div class="links">
 
    <a class="redirect" href="<%=request.getContextPath()%>/userprofile.jsp">Torna al profilo</a>
  
    <a  class="redirect" href="store.jsp" class="modify-btn">Torna al catalogo</a>
  
</div>
  </div>
  <br>
  <br>
  <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
