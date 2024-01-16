<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OctoPlus Login</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/login-style.css">

<jsp:include page="header.jsp" flush="true"/>
<script src="scripts/validate.js"></script>
<script>
function disattivaErrore(){
	let error = document.getElementById("error");
	error.style.display = "none";
}
</script>
</head>
<body>
<div class="center">
 <h1>OctoLogin</h1>
   <form action="Login" method="post">
     <div class="txt_field email-field">
    <input type="email" name="email" required onkeyup="disattivaErrore();"
        onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)" id="email">
    <span id="errorEmail" class="error-text"></span>
    <label>Email</label>
</div>
    <div class="txt_field email-field"> 
       <input type="password" name="password" required> <label>Password</label>
    </div>
   <input type="hidden" name="jspName" value="loginutenti">
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
    
    <div class="pass"><a href="changepass2.jsp">Password dimenticata?</a></div>
    <input type="submit" value="Login">
                      
    Non sei un membro? 
    <a href="registrazione.jsp"> <b> Registrami </b></a>

    <br>
  </form> 
</div>
 <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>