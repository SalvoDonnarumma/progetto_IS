<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OctoPlus Login</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/registrazione.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="scripts/validate.js"></script>
<script type="text/javascript">
function checkPasswords(){
	var pass = document.getElementById("password");
	var conf_pass = document.getElementById("conf_password");
	console.log(pass);
	console.log(conf_pass);
	if( pass.value != conf_pass.value ){
		alert("Le passwords inserite non combaciano!");
	}	
	alertLength();
}	

function checkPassLength(){
	var pass = document.getElementById("password");
	if( pass.value.length < 12){
		$("#errorPass").empty();
		$("#errorPass").append("la password deve essere lunga almeno 12 caratteri!");
	} else 
		$("#errorPass").empty();
	if(  pass.value.length == 0 )
		$("#errorPass").empty();
}

function alertLength(){
	var pass = document.getElementById("password");
	if( pass.value.length < 12)
		alert("La password deve essere lunga almeno 12 caratteri!");
}

function disattivaErrore(){
	let error = document.getElementById("error");
	error.style.display = "none";
}

</script>
<jsp:include page="header.jsp" flush="true"/>
</head>
<body>
<div class="center">
 <h1>Benvenuto!</h1>
 
 	<form action="SignUp" method="post">
 		<div class="txt_field email-field"> <!-- Nome -->
 			<input type="text" name="firstname" id="firstname" required pattern="^[A-Za-z]+$" 
 			 onkeyup="disattivaErrore();"
			 onChange="validateFormElem(this, document.getElementById('errorName'), nameOrLastnameErrorMessage)">
			 <span id="errorName"> </span> <label>Nome</label>
		</div>
		
		<div class="txt_field email-field">
			<input type="text" name="lastname" id="lastname" required pattern="^[A-Za-z]+$" 
			 onChange="validateFormElem(this, document.getElementById('errorLastname'), nameOrLastnameErrorMessage)">
			<span id="errorLastname"></span> <label>Cognome</label>
		</div>
		
		<div class="txt_field email-field">
			<input type="email" name="email" required 
			onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)"	id="email">
			<span id="errorEmail"></span> <label>Email</label>
		</div>
		
		<div class="txt_field email-field"> 
 			<input type= "password" onChange="checkPassLength()" name="password" id="password" required> <label>Password</label>
		</div>
		<span style="color:red;" id="errorPass"></span>
		
		<div class="txt_field email-field"> 
 			<input type= "password" name="conf_password" id="conf_password" required> <label>Conferma Password</label>
		</div>
		<span id="errorPhone0"></span>
		
		<div id="phones" class="txt_field email-field">
					<input type="tel" name="phone"
						id="phone0" required
						pattern="^([0-9]{3}-[0-9]{7})$"
						onchange="validateFormElem(this, document.getElementById('errorPhone0'), phoneErrorMessage)">
				<label>Numero telefono [###-#######]</label>
				<br>
				<span id="errorPhone0"></span>
		</div>
		
		
		<% 
		List<String> errors = (List<String>) request.getAttribute("errors");
		if (errors != null){
			for (String error: errors){ %>
				<h3 id="error" style="color:red"><%=error %></h3>
			<%
			}
		}
		%>
		
		<input type= "submit" onClick="checkPasswords()" value="Registrami" >
		
		Sei già registrato? 
		<a href="login.jsp">  <b> Accedi </b> </a>			
	</form> 		
</div>
 <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
