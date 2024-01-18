<% String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equals("Utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Prodotti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
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
	<script src="scripts/validate.js"></script>
	<script>
	function purchaseAlert(){
		alert("CAMBIO IN CORSO...se l'operazione è andata a buon fine sarai reindirizzato alla pagina di login!");
	}
	function disattivaErrore(){
		let error = document.getElementById("error");
		error.style.display = "none";
	}
	</script>
	<script type="text/javascript">
		function disattivaErrore(){
			let error = document.getElementById("error");
			error.style.display = "none";
		}
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
	<jsp:include page="../header.jsp" flush="true"/>
</head>
<body>
  <br>
  <br>
  <div class="container">
    <h1 class="title">Cambio password</h1>
    <form action="<%=request.getContextPath()%>/CambioPasswordAdminValidator" method="post">
      <label>Email</label>
      <input type="email" name="email" required onkeyup="disattivaErrore();" value="<%=request.getParameter("email")%>"
        	onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)" id="email">
      <span id="errorEmail" class="error-text"></span>
      
      <label for="oldPassword">Vecchia password</label>
      <input name="currentPassword" onChange="checkPassLength()"  id="newpassword" type="password" required pattern=".{12,}">
      <span style="color:red;" id="errorPass"></span>
      
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
     
      <button type="submit" onClick="purchaseAlert()" >Cambia Password</button>
    </form>
  </div>
</body>
</html>
