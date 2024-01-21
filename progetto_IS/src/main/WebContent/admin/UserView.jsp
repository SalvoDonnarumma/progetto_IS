<%@page import="gestioneutenti.Utente"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equalsIgnoreCase("utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Prodotti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}
	System.out.println("Benvenuto nella pagina UserView");
	Collection<?> users = (Collection<?>) request.getAttribute("users");
	request.getSession().setAttribute("fromStore", Boolean.FALSE);
	if(users == null) {
		response.sendRedirect(request.getContextPath()+"/OttieniElencoUtenti");	
		return;
	}
	Utente user = (Utente) request.getSession().getAttribute("logged");
%>

<!DOCTYPE html>
<html lang="it">
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,gestioneprodotti.Prodotto"%>

<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/productviewstyle.css">
	<title> Lista Utenti </title>
<script src="scripts/validate.js"></script>
<script type="text/javascript">
	function confirmAlert(){
		alert("Eliminazione utente in corso...");
	}
</script>
<jsp:include page="../header.jsp" flush="true"/>
</head>

<body>
	<br>
	<br>
	<a href="admin/insertAmm.jsp" class="edit-link-insert"> INSERISCI NUOVO ADMIN </a>
	<br>
	<table border="1" title="Tabella utenti">
	
	<caption >Tabella utenti</caption>
		<tr>
			<th> Email <a href="OrdinaUtenti?&sort=email" class="no-border-link">Ordina</a></th>
			<th> Nome</th>
			<th> Cognome <a href="OrdinaUtenti?sort=cognome" class="no-border-link">Ordina</a></th>
			<th> Numero telefono </th>
			<th> Ruolo </th>
			<th> Azioni </th>
		</tr>
		<%
		if (users != null && users.size() != 0) {
				Iterator<?> it = users.iterator();
				while (it.hasNext()) {
					Utente bean = (Utente) it.next();
		%>
		<tr>
			<td><%=bean.getEmail()%></td>
			<td><%=bean.getNome()%></td>
			<td><%=bean.getCognome()%></td>
			<% if(bean.getTelefono()== null){%>
			<td>	Non disponibile </td>
			<%} else {%>
			<td><%=bean.getTelefono()%></td>
			<%} %>
			<td><%=bean.getRuolo()%></td>
			<td><a onClick="confirmAlert();" href="CancellaAdmin?email=<%=bean.getEmail()%>" class="edit-link"> &nbsp;Cancella&nbsp;utente&nbsp;&nbsp;&nbsp; </a><br>
			<br>
			<% if( bean.getRuolo().contains("Gestore") ) {%>
		 		<a href="admin/changepassadmin.jsp?email=<%=bean.getEmail()%>" class="edit-link"> Cambia password </a>
				<br>
				</td>
		</tr>
		<%     }
				}
			} else {
		%>
		<tr>
			<td colspan="6">No users available</td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<br>
	<br>
	<br>
</body>
</html>