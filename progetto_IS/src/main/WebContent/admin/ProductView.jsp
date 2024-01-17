 <%@page import="gestioneutenti.Utente"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equals("Utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
		return;
	}
	Collection<?> products = (Collection<?>) request.getAttribute("products");
	request.getSession().setAttribute("fromStore", Boolean.FALSE);
	if(products == null) {
		response.sendRedirect(request.getContextPath()+"/product");	
		return;
	}
	Prodotto product = (Prodotto) request.getAttribute("product");
%>
<!DOCTYPE html>
<html lang="it">
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,gestioneprodotti.Prodotto"%>

<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/productviewstyle.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script type="text/javascript">
		function confirmDelete(){
			alert("Cancellazione del prodotto in corso...");
		}
	</script>
	<title> Lista Prodotti </title>
	<jsp:include page="../header.jsp" flush="true"/>
</head>
<body>
	<br>	
	<br> 
	<a href="./admin/insertProduct.jsp" class="edit-link-insert"> INSERISCI UN NUOVO PRODOTTO </a>
	<br>
	<br>
	<table border="1" title="Tabella prodotti">
	<caption>Tabella prodotti</caption>
		<tr>
			<th>IdProdotto</th>
			<th>Categoria<a href="OrdinaProdotti?sort=categoria" class="no-border-link">Ordina</a></th>
			<th>Nome <a href="OrdinaProdotti?sort=nome" class="no-border-link">Ordina</a></th>
			<th>Prezzo</th>
			<th>Descrizione</th>
			<th>Foto</th>
			<th>Statistiche</th>
			<th>Taglie Disponibili</th>
			<th>Azioni</th>
		</tr>
		<%
		if (products != null && products.size() != 0) {
				Iterator<?> it = products.iterator();
				while (it.hasNext()) {
					Prodotto bean = (Prodotto) it.next();
		%>
		<tr>
			<td><%=bean.getCode()%></td>
			<td><%=bean.getCategoria()%></td>
			<td><%=bean.getNome()%></td>
			<td><%=bean.getPrice()%></td>
			<td><%=bean.getDescrizione()%></td>
			<td> <img src="./getPicture?id=<%=bean.getCode()%>" onerror="this.src='./images/nophoto.png'" alt="Immagine del prodotto" style="width:100px;height:100px"> </td>
			<td><%=bean.getStats()%></td>
			<td><p> <%=bean.getTaglie()%> </p> </td>
			<td>
				<a id="link" href="cancellaProdotto?fromStore=false&action=delete&id=<%=bean.getCode()%>" onClick="confirmDelete();" class="delete-link">Cancella</a>
					<br>	<br>
				<a href="OttieniDettagliProdotto?to=modify&id=<%=bean.getCode()%>" class="edit-link">Modifica</a>
					<br>
		</td>
		</tr>
		<%
		}
			} else {
		%>
		<tr>
			<td colspan="6">No products available</td>
		</tr>
		<%
		}
		%>
	</table>
	
	<%
		if (product != null) {
		%>
	<table border="1" title="Tabella prodotti">
	<caption>Tabella prodotti</caption>
		<tr>
			<th>Code</th>
			<th>Name</th>
			<th>Description</th>
			<th>Price</th>
		</tr>
		<tr>
			<td><%=product.getCode()%></td>
			<td><%=product.getNome()%></td>
			<td><%=product.getDescrizione()%></td>
			<td><%=product.getPrice()%></td>
		</tr>
	</table>
	<%
	}
	%>

<br>

<br>
<h1>Upload photo:</h1>
	<div class="UploadPhoto">
<form action="UpdatePhoto" enctype="multipart/form-data" method="post">
	Nome file caricato:
	<select name="id">
<%
if(products != null && products.size() > 0) {
		Iterator<?> it = products.iterator(); 
		while(it.hasNext()) {
	Prodotto item = (Prodotto)it.next();
%>	
		<option value="<%=item.getCode()%>"> cod: <%=item.getCode()%> nome: <%=item.getNome()%></option>
<%
		}
	}	
%>		
	</select>
	<br>
	<input class="file" type="file" name="talkPhoto" value="" maxlength="255">	
	<br>		
	<input type="submit" class="submit" value="Upload">      <input type="reset">
	<br>
</form>
	</div>
</body>
</html>