<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
 	Utente bean = (Utente) request.getSession().getAttribute("logged"); //quando una persona si logga salvo i suoi dati nella sessione
   	String isAdmin = (String) request.getSession().getAttribute("isAdmin");
  	if( bean == null ){
  		response.sendRedirect("./login.jsp");		
  		return;
  	}
 %>
<!DOCTYPE html>
<html lang="it">
<%@ page import="java.util.*,gestioneprodotti.Prodotto,gestioneprodotti.ProductDaoDataSource,gestioneutenti.Utente"%>
<head>
    <title>Pagina Utente</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/profile.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  	<script src="<%=request.getContextPath()%>/scripts/orders.js"></script>
  	<script>
		$(document).ready(function(){
			dynamicOrdersUser("<%=request.getContextPath()%>/OttieniOrdiniUtente?idUtente=<%=bean.getEmail()%>");
		});	
  </script> 
  <jsp:include page="./header.jsp" flush="true"/>
</head>
<body>
	<br>
  	<br>
    <div class="container_orders">
        <h1 class="title" >Area personale</h1>
        
        <div class="user-info">
            <h1>Informazioni personali</h1>
            <ul>
                <li><strong>Nome:</strong> <%=bean.getNome() %></li>
                <li><strong>Cognome:</strong> <%=bean.getCognome() %></li>
                <li><strong>Email:</strong> <%=bean.getEmail() %> 
                <div class="password-change"> 
            		<p> <a href="./changepass.jsp" class="no-border-link">Cambia password</a></p>
            		<p> <a href="./modifypaymentcard.jsp" class="no-border-link">Modifica dati carta di pagamento</a></p>
       			 </div>
       			</li>
                <li><strong>Numero di telefono:</strong> <%=bean.getTelefono() %></li>
            </ul>
        </div>
          
        <div class="orders" id="orders">   
        </div>
        <div class="links">
        	<div class="torna">  
        	    <p> <a href="<%=request.getContextPath()%>/store.jsp" class="no-border-link">Torna allo store</a></p>
        	</div>
        	<div class="logout">  
            	<p> <a href="Logout" class="no-border-link">Logout</a></p>
        	</div>
        </div>
    </div>
    
    <br>
  	<br>
	<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
