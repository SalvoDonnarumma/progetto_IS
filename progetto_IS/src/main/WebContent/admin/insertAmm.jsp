<%	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equalsIgnoreCase("utente")|| isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Prodotti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="it">
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>
	<head>
	<title> Inserimento nuovo Gestore </title>
<style>
	a {
	color: blue;
	font-weight: bold;
	text-decoration: none;
	}
	
	a:visited {
		color: blue;
	}
	
	a:hover {
		color: red;
	}
	
	a:active {
		color: blue;
	}
	
	/* Impostazioni del corpo della pagina */
	body {
	
	 	background-color: rgb(0, 0, 0);
		background-image: linear-gradient(135deg, #008080, #16425B, #9BA2FF);
		background: linear-gradient(to bottom right, #16425B, #9BA2FF);
		min-height: 100vh;
		margin: 0;
	}
	
	/* Stili per il form */
	form {
		width: 700px;
		margin: 70px auto 0;
		border: 1px solid #ccc;
		border-radius: 5px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		background-color: #D9DCD6;
		padding: 25px;
	}
	
	/* Stili per i pulsanti all'interno del form */
	form input[type="submit"],
	form input[type="reset"] {
		display: inline-block;
		margin-top: -15px;
		float: right;
		
	}
	
	/* Stili per le sezioni del modulo */
	.InsertProdotto,
	.UploadPhoto,
	.InsertAmministratore {
		width: 50%;
		border: none;
		padding: 20px;
		margin: 0 auto;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		background-color: #D9DCD6;
	}
	.InsertAmministratore form {
		border: none;
		background-color: transparent;
	}
	
	
	
	/* Stile per gli input del modulo */
	form input[type=text],
	form input[type=number],
	form input[type=password],
	form input[type=date],
	form input[type=range],
	form input[type=email],
	form input[type=url],
	form input[type=time],
	form input[list] {
		width: 100%;
		padding: 10px;
		font-size: 16px;
		border: none;
		border-radius: 5px;
		background-color: #f5f5f5;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		margin-bottom: 10px;
		margin-right: 30px;
		font-family: "Arial", sans-serif; 
	    color: #606C38;
	}
	form textarea {
		width: 100%;
		padding: 10px;
		font-size: 16px;
		border: none;
		border-radius: 5px;
		background-color: #f5f5f5;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
		margin-bottom: 10px;
		resize: vertical; /*fa solo su e giù*/
		margin-right: 30px;
		
	}
	
	/* Stile per i pulsanti di invio e reset */
	input[type="submit"],
	input[type="reset"] {
		width: 15%;
		height: 25%;
		border: 1px solid;
		background: #85756E;
		border-radius: 10px;
		font-size: 18px;
		color: #D9DCD6;
		font-weight: 700;
		cursor: pointer;
		outline: none;
		position: relative;
	}
	
	input[type="submit"]:hover,
	input[type="reset"]:hover {
		border-color: #BC2C1A;
		transition: .5s;
		color: #D9DCD6;
		background-color: #BC2C1A;
	}
	
	/* Stili per le sezioni del modulo */
	.InsertProdotto,
	.UploadPhoto,
	.InsertAmministratore {
		width: 50%;
		border: 1px solid black;
		padding: 20px;
		margin: 0 auto;
	}
	/* Stili per il titolo del form */
	.InsertAmministratore {
		width: 50%;
		max-width: 750px;
		border: 1px solid black;
		padding: 20px;
		margin: 0 auto;
	}
	.form-title {
	  font-size: 32px;
	  font-weight: bold;
	  color: #BC2C1A;
	  margin-bottom: 40px;
	  text-align: left;
	  text-transform: uppercase;
	  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);  /* Aggiungi l'ombra al testo */
	  font-family: "Arial", sans-serif;  /* Cambia il carattere del testo */
	}
	
/* Stili per la linea decorativa sotto il titolo */
	.form-title::after {
	  content: "";
	  display: block;
	  width: 100px;
	  height: 3px;
	  background-color: #853411;
	  margin: 10px 0;
	}
	 label {
	    color: #BC2C1A; /* Colore del testo dell'etichetta */
	  }
	  
	  input[name="email"] {
	    font-family: "Arial", sans-serif; 
	    color: #BC2C1A; 
	  }	
	  
@media ( max-width : 800px) {
	input[type="submit"],
	input[type="reset"] {
		width: 30%;
		height: 25%;
		border: 1px solid;
		background: #85756E;
		border-radius: 10px;
		font-size: 18px;
		color: #D9DCD6;
		font-weight: 700;
		cursor: pointer;
		outline: none;
		position: relative;
	}
	
	input[type="submit"]:hover,
	input[type="reset"]:hover {
		border-color: #BC2C1A;
		transition: .5s;
		color: #D9DCD6;
		background-color: #BC2C1A;
	}
	
	form {
		width: 90%;
	}
}
</style>

<script src="<%=request.getContextPath()%>/scripts/validate.js"></script>
<script> 
function disattivaErrore(){
	let error = document.getElementById("error");
	error.style.display = "none";
}
</script>
<jsp:include page="../header.jsp" flush="true"/>
</head>
<body>
	<h2 class="form-title">Inserisci Gestore</h2>
	
	<form action="<%=request.getContextPath()%>/CreaNuovoAdmin" method="post">
		<input type="hidden" name="action" value="insert"> 
		
		<label for="name">Email </label>
		<input type="email" name="email" required placeholder="Inserisci email" onkeyup="disattivaErrore();"
		onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)"	id="email">
		<span id="errorEmail"></span> 
		
		<label for="cognome">Cognome </label>
		<input type="text" name="cognome" id="cognome" required pattern="^[A-Za-z]+$" placeholder="Inserisci il cognome"
		onchange="validateFormElem(this, document.getElementById('errorLastname'), nameOrLastnameErrorMessage)">
		<span id="errorLastname"></span>
		
		<div class="txt_field"> 
			<label>Password</label>
 			<input type= "password" name="password" required pattern=".{12,}"
 			placeholder="Inserisci la password" onchange="validateFormElem(this, document.getElementById('errorPass'), lengthPassErrorMessage)">
			<span id="errorPass"></span>
		</div>
		
		<div id="phones" class="txt_field">
			<label>Numero telefono</label>
					<input type="tel" name="phone"
						id="phone0" required
						pattern="^([0-9]{3}-[0-9]{7})$"
						onchange="validateFormElem(this, document.getElementById('errorPhone0'), phoneErrorMessage)"
						placeholder="[###-#######]">
				<br>
				<span id="errorPhone0"></span>	
		</div>
		<br>
		<label for="ruolo">Seleziona il ruolo:</label>
    		<select name="ruolo" id="ruolo">
        		<option value="Gestore Utenti">Gestore Utenti</option>
        		<option value="Gestore Prodotti">Gestore Prodotti</option>
       			<option value="Gestore Ordini">Gestore Ordini</option>
    		</select>
    	<br>
		<% 
		List<String> errors = (List<String>) request.getAttribute("errors");
		if (errors != null){
			for (String error: errors){ %>
				<h3 id="error" style="color:red"><%=error %></h3>
			<%
			}
		}
		%>
	
		<input type="submit" value="Add"><input type="reset" value="Reset">
	</form>
	
	
</body>
</html>