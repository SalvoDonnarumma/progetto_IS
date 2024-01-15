<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
 //quando una persona si logga salvo i suoi dati nella sessione, in questo modo posso prelevarli con l'operazione duale
  	Utente bean = (Utente) request.getSession().getAttribute("logged"); 
 	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equals("Utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}
 %>
<!DOCTYPE html>
<html lang="it">
<%@ page import="java.util.*,gestioneutenti.Utente"%>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cambio password</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/changepass.css">
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script type="text/javascript">
		function checkPassLength(){
			var pass = document.getElementById("newpassword");
			if( pass.value.length < 12){
				$("#errorPass").empty();
				$("#errorPass").append("la password deve essere lunga almeno 12 caratteri!");
			} else {
				$("#errorPass").empty();
			}
			
			if(  pass.value.length == 0 )
				$("#errorPass").empty();
		}
		
		function alertLength(){
			var pass = document.getElementById("newpassword");
			if( pass.value.length < 12 )
				alert("La password deve essere lunga almeno 12 caratteri!");
		}
	</script>
	<jsp:include page="./header.jsp" flush="true"/>
</head>
<body>
  <br>
  <br>
  <div class="container">
    <h1 class="title">Cambio password</h1>
    <!-- la servlet si chiama UserControl ma è mappata con /AdminControl -->
    <form action="cambioPassword?cgpass&email=<%=bean.getEmail()%>" method="post">
      <label for="currentPassword">Password attuale</label>
      <input name="currentPassword" type="password" id="currentPassword" required>
      
      <label for="newPassword">Nuova password</label>
      <input name="newPassword" onChange="checkPassLength()"  id="newpassword" type="password" required pattern=".{12,}">
      <span style="color:red;" id="errorPass"></span>
      
      <label for="confirmPassword">Conferma nuova password</label>
      <input name="confirmPassword" type="password" required>
      
      <%
     	 List<String> errors = (List<String>) request.getAttribute("errors");
			if (errors != null){
				for (String error: errors){ %>
				<label style="color: red">	<%=error %> </label>
				<br>		
			<%
			}
		}
      %>
     
      <button type="submit" onClick="alertLength()" >Cambia Password</button>
    </form>
  </div>
</body>
</html>
