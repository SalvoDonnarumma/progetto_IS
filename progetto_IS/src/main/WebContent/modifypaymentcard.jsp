<%
 	Utente bean = (Utente) request.getSession().getAttribute("logged"); //quando una persona si logga salvo i suoi dati nella sessione
  	if( bean == null ){
  		response.sendRedirect("./login.jsp");		
  		return;
  	}
  	Utente logged = (Utente) request.getSession().getAttribute("logged");
	Carta carta = logged.getCarta(); 	
 %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.util.*,gestioneprodotti.Prodotto,storagelayer.ProductDaoDataSource,gestioneutenti.Utente, gestionecarta.Carta"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifica dati pagamento</title>
    <style>
body {
    font-family: Arial, sans-serif;
    background: linear-gradient(217deg, rgba(26, 82, 118, .8), rgba(26, 82, 118, 0) 70.71%),
      linear-gradient(127deg, rgba(19, 126, 166, .8), rgba(19, 126, 166, 0) 70.71%),
      linear-gradient(336deg, rgba(2, 27, 70, .8), rgba(2, 27, 70, 0) 70.71%);
    margin: 0;
    padding: 0;
    align-items: center;
    justify-content: center;
    height: 100vh;
}

form {
    margin-left: 40%;
    align-items: center;
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    text-align: center; /* Centra il contenuto all'interno del form */
    display: flex; /* Utilizza un layout flessibile */
    flex-direction: column; /* Colonna per allineare i pulsanti verticalmente */
}

label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
}

.form_payment{
	margin-top:30px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

input,
select {
    width: 100%;
    padding: 8px;
    margin-bottom: 16px;
    box-sizing: border-box;
    border: 1px solid #ccc;
    border-radius: 4px;
    transition: all 0.3s ease;
}

input:hover,
select:hover {
    border-color: #4caf50;
}

.extra-form{
	margin-top: -20px;
}

button {
    background-color: #4caf50;
    color: #fff;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
}

button:hover {
    background-color: #357a38;
    transform: scale(1.1);
}       
    </style>
     <jsp:include page="./header.jsp" flush="true"/>
</head>
<body>
    <form class="form_payment" action="salvaCarta" method="post">
        <label for="cardNumber">Numero carta:</label>
        <% if( carta != null) {%>
			<input type="text" id="cardNumber" name="numero_carta" value="<%=carta.getNumero_carta()%>" required>
		<%} else { %>        
        	<input type="text" id="cardNumber" name="numero_carta" placeholder="Enter card number" required>
		<%} %>
		
		
        <label for="cardHolder">Proprietario Carta:</label>
        <% if( carta != null) {%>
			 <input type="text" id="cardHolder" name="nome" value="<%=carta.getProprietario() %>" required>
		<%} else { %>        
        	 <input type="text" id="cardHolder" name="nome" placeholder="Enter card holder name" required>
		<%} %>

        <label for="expiryDate">Expiry Date:</label>
        <% if( carta != null) {%>
			 <input type="text" id="expirationdate" name="data" value="<%=carta.getData_scadenza()%>" required>
		<%} else { %>        
        	 <input type="text" id="expirationdate" name="data" placeholder="01/2024" required>
		<%} %>
        

        <button type="submit">Modica o inserisci i dati</button>
        <div class="errors" style="color: red;">
    	<% 
    	  List<String> errors = (List<String>) request.getAttribute("errors");
      		if (errors != null){
        	for (String error: errors){ %>
         	<h3 id="error" style="color:red"><%=error %></h3>   
        <%
        }
      }
    %>	
    </div>
    </form>
    <% if( carta != null) {%>
    <form action="eliminaCarta" class="extra-form">
    	<button id="eliminaCartaButton">Elimina Carta</button>
    	<br>
    	</form>
    	<%} %>
    <br>

<script>
    document.getElementById("eliminaCartaButton").addEventListener("click", function() {
        // Chiamata AJAX per chiamare la servlet eliminaCarta
        alert("Eliminazione Dati Carta...");
    });
</script>
</body>
</html>
