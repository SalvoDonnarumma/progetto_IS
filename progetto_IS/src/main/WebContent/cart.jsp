<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
 	Collection<?> products = (Collection<?>) request.getSession().getAttribute("products");
%>
<!DOCTYPE html>
<html lang="it">
<%@ page import="java.util.*,gestioneordini.Carrello,gestioneprodotti.ProductDaoDataSource"%>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Carrello - OctoPlus</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/cart.css">
  <link rel="stylesheet"
  href="https://unpkg.com/boxicons@latest/css/boxicons.min.css">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="scripts/cart.js"></script>
  
<script>
	$(document).ready(function(){
		var id = '<%= request.getParameter("id") %>';
		var encodedId = encodeURIComponent(id);
		dynamicCart("<%= request.getContextPath() %>/AggiungiProdottoAlCarrello?id=" + encodedId);
	});	
</script>    
<jsp:include page="header.jsp" flush="true"/>
</head>
<body>
	<br>
  <div class="container ">
    <table title="Carrello">
    <caption class="title"> Carrello prodotti </caption>
      <thead>
        <tr>
          <th>Prodotto</th>
          <th class="d-none">Categoria</th>
          <th>Quantità</th>
          <th>Taglia</th>
          <th class="d-none">Prezzo unitario</th>
          <th class="d-none">Subtotale</th>
          <th class="d-none">Elimina prodotto</th>
          <th class="d-temp">Elimina</th>
        </tr>
      </thead>
      <tbody id="cart">
    
      </tbody>
    </table>         

    <div class="checkout" id="checkout">
    </div>
  </div>

  <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
