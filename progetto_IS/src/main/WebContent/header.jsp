<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="gestioneutenti.Utente"%>
<html lang="it">
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles//header.css">
<link rel="stylesheet" href="https://unpkg.com/boxicons@latest/css/boxicons.min.css">
<title> Header </title>
<style>
    
.icon {
	display: inline-black;
	margin: 2px;
	justify-content: flex-end;
}

.icon{
	color: #121212;
	font-size: 20px;
	margin-left: 15px;
	border-radius: 50%; /*rende circolare l'icona*/
	padding: 10px; /* grandezza cerchio */
    background-color: #fff;
	text-decoration: none;
}

.icon:hover{
	opacity: 0.7;
	background-color: #121212; /* Aggiungi il colore di sfondo desiderato quando ci passi sopra */
  	color: #fff; /* Cambia il colore del testo quando ci passi sopra */
}

.button {
  border: none;
  color: white;
  padding: 16px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 12px;
  margin: 4px 2px;
  transition-duration: 0.4s;
  cursor: pointer;
}

.button1 {
  background-color: white; 
  color: black; 
  border: 2px solid #04AA6D;
}

.button1:hover {
  background-color: #04AA6D;
  color: white;
}

.button2 {
  background-color: white; 
  color: black; 
  border: 2px solid #008CBA;
}

.button2:hover {
  background-color: #008CBA;
  color: white;
}

.button {
  border: none;
  color: white;
  padding: 16px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  transition-duration: 0.4s;
  cursor: pointer;
}

.button1 {
  background-color: white; 
  color: black; 
  border: 2px solid #03b1fc;
}

.button1:hover {
  background-color: #008CBA;
  color: white;
}

.button2 {
  background-color: white; 
  color: black; 
  border: 2px solid #03b1fc;
}

.button2:hover {
  background-color: #008CBA;
  color: white;
}

</style>
</head>
<body>
<header>
	<img src="<%=request.getContextPath()%>/images/octopus.png" class="logo" alt="logo sito">
	<span class="hfont">OctoPlus</span> 
		 <ul class="links-nav">
			<%
			String isAdmin = (String) request.getSession().getAttribute("isAdmin");
			HttpSession session2 = request.getSession(false);
			if( session2 == null )
				System.out.println("Nessuna sessione attiva");
			else
				System.out.println("sessione attiva: "+session2);
			
			System.out.println("----");
			String inStore =request.getParameter("fromStore");
			System.out.println(isAdmin);
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String path = httpServletRequest.getServletPath();
			System.out.println(path);
			if( isAdmin == null ){ //sezione utente ospite
					if (path.contains("/store.jsp")) {%>																							
<%						}
			%>	
				<!--  <li class="item"><a href="<%=request.getContextPath()%>/index.jsp"><i class='bx bx-home-heart icon'></i></a></li> -->
				<!--  <li class="item"><a href="<%=request.getContextPath()%>/login.jsp"><i class='bx bx-log-in-circle icon'></i></a>	</li> --> 
				<li class="item"><a href="<%=request.getContextPath()%>/login.jsp"> <button class="button button1">ACCEDI</button> </a> </li>
				<li class="item"><a href="<%=request.getContextPath()%>/registrazione.jsp"> <button class="button button2">REGISTRATI</button> </a> </li>
				<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a>	</li>
				<li class="item"><a href="<%=request.getContextPath()%>/cart.jsp"><i class='bx bx-cart icon'></i></a> </li>	
				<%
			} else if( isAdmin.equals("Gestore Ordini") ){ //sezione admin		
					if( path.contains("/admin") ) { %>		
							<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a></li>
							<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a></li>																															
<%					} else {	
				%>	
					<li class="item"><a href="<%=request.getContextPath()%>/admin/OrderView.jsp"><i class='bx bx-library icon'></i></a></li>
					<li class="item"><a href="<%=request.getContextPath()%>/userprofile.jsp"><i class='bx bxs-user icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a>	</li>
		<%				}
					} else if( isAdmin.equals("Gestore Prodotti") ){ //sezione admin
						if( path.contains("/admin") ) { %>	
								<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a></li>
								<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a></li>																																		
<%							} else {	
			%>	
					<li class="item"><a href="<%=request.getContextPath()%>/admin/ProductView.jsp"><i class='bx bx-library icon'></i></a></li>
					<li class="item"><a href="<%=request.getContextPath()%>/userprofile.jsp"><i class='bx bxs-user icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a>	</li>
	
	<%		}} else if( isAdmin.equals("Gestore Utenti") ){ //sezione admin
						if( path.contains("/admin") ) { %>		
							<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a></li>
							<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a></li>																																
<%						}	else {
	%>
					<li class="item"><a href="<%=request.getContextPath()%>/admin/UserView.jsp"><i class='bx bx-library icon'></i></a></li>
					<li class="item"><a href="<%=request.getContextPath()%>/admin/userprofile.jsp"><i class='bx bxs-user icon'></i></a> </li>
					<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a>	</li>
<%							}
					}
				else		{ //sezione utente normale 
					 if( path.contains("/store.jsp") ) { %>	
					 		<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a></li>
							<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a></li>	
<%					} else { %>
					<li class="item"><a href="<%=request.getContextPath()%>/userprofile.jsp"><i class='bx bxs-user icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/Logout"><i class='bx bx-log-out-circle icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/store.jsp"><i class='bx bx-home-heart icon'></i></a>	</li>
					<li class="item"><a href="<%=request.getContextPath()%>/cart.jsp"><i class='bx bx-cart icon'></i></a> </li>	
					<%  }
							} %>	
			</ul>
	
		<button class="hamburger">
					<div class="bar"></div>
		</button>
		<!-- Menu hamburger -->
		<nav class ="mobile-nav" aria-label="menuHamburger">
		<div class="nav-container">
		<%  if(isAdmin == null)  {%>
				<a href="<%=request.getContextPath()%>/login.jsp">&nbsp;&nbsp;Login </a>
		<%} else if(isAdmin.equals("Gestore Ordini")) {//sezione admin %>	
			 <a href="<%=request.getContextPath()%>/admin/OrderView.jsp">&nbsp;&nbsp;Sezione <br>&nbsp;&nbsp;Admin</a>
			 <a href="<%=request.getContextPath()%>/userprofile.jsp">&nbsp;&nbsp;Profilo </a>
			 <a href="<%=request.getContextPath()%>/Logout">&nbsp;&nbsp;Esci </a>
		<%} else if(isAdmin.equals("Utente")) {%>
			 <a href="<%=request.getContextPath()%>/userprofile.jsp">&nbsp;&nbsp;Profilo </a>
			 <a href="<%=request.getContextPath()%>/Logout">&nbsp;&nbsp;Esci </a>
		<%} %>	
			<a href="<%=request.getContextPath()%>/cart.jsp">&nbsp;&nbsp;Carrello</a>
		</div>
	</nav>
	<script>
		const menu_btn = document.querySelector('.hamburger');
		const mobile_menu = document.querySelector('.mobile-nav');
		menu_btn.addEventListener('click', function(){
			menu_btn.classList.toggle('is-active');
			mobile_menu.classList.toggle('is-active');
		});	
	</script>
</header>
</body>
</html>