<%  String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equalsIgnoreCase("utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
	return;
	}
	Prodotto bean = (Prodotto) request.getAttribute("product"); 
 	Taglie sizes = (Taglie) bean.getTaglie();
 	System.out.println(bean);%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="it">

<%@ page import="java.util.*,gestioneprodotti.Prodotto, gestioneprodotti.Taglie"%>
<head>
<meta charset="ISO-8859-1">
<title>Modifica prodotto</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/modifyproduct.css">

<script>
function addValuesToLink(){
	var link = document.getElementById("link");	
	console.log(link.href+="&categoria=");
	console.log(link.href+=document.getElementById("category").value);
}
</script>
<jsp:include page="../header.jsp" flush="true"/>
</head>
<body>
	<div class="big_container">
	<br>
		<div class="col-25">  
	     	<div class="box1">
	     	<h3 style="color: grey;">Vecchio Prodotto:</h3> <br>
	     	
	     	<img src="<%=bean.getImagePath()%>" alt="Immagine del prodotto" onerror="this.src='./images/nophoto.png'" style="width:30%">
	     	
			<p class="name">Nome: <%=bean.getNome()%> </p> 
			
			<p class="categoria">Categoria:<%=bean.getCategoria()%> </p>  <br> 
			
			<p class="description">Descrizione: <%=bean.getDescrizione()%> <p><br>
			
			<p>Prezzo: <%=bean.getPrice()%> &euro; </p> <br>
			<p>Quantit&aacute; taglie M disponibili: <%=bean.getTaglie().getQuantitaM() %> </p><br>
			<p>Quantit&aacute; taglie L disponibili: <%=bean.getTaglie().getQuantitaL() %></p><br>
			<p>Quantit&aacute; taglie XL disponibili: <%=bean.getTaglie().getQuantitaXL() %></p><br>
			<p>Quantit&aacute; taglie XLL disponibili:<%=bean.getTaglie().getQuantitaXXL() %></p><br>
			<p>Statistiche:  <%= bean.getStats()%> <br>
		 			
		 	</div>
	    </div>
	 <br>
	 <br> 	
	<br>
		<div class="row">
		  <div class="col-75"> 
	    	<div class="container">
	    	<h2> Modifiche al prodotto: </h2> <br>
	      	<form action="modificaProdotto?id=<%=bean.getCode()%>" method="post" onClick="addValuesToLink();" enctype="multipart/form-data">
				<input type="hidden" name="action" value="insert"> 
				<h4>
					<label> Nome prodotto: 
						<input name="nome" type="text" maxlength="25" required placeholder="enter name" value=<%=bean.getNome()%>><br> 
					</label>
				</h4>	
				<h4>
					<label>
					Seleziona categoria: 
						<select name="categoria" id="categoria-select" onchange="searchAndFilter()">
						    <option value="<%=bean.getCategoria()%>" selected><%=bean.getCategoria()%></option>
						    <option value="Coltelli">Coltelli</option>
						    <option value="Erogatori">Erogatori</option>
						    <option value="Guanti">Guanti</option>
						    <option value="Maschere">Maschere</option>
						    <option value="Mute">Mute</option>
						    <option value="Pinne">Pinne</option>
						    <option value="Torce">Torce</option>
		  				</select>
 					</label>
				</h4>
				<br>
				
				<h4>
					<label>
						Descrizione: <br>
						<textarea cols="100" name="descrizione" maxlength="1000" rows="10" required>
						<%=bean.getDescrizione()%>
						</textarea><br>
					</label>
				</h4>			
				<h4>
					<label>
						Prezzo: 
						<input style="width:8%;" name="price" type="number"  min="0" step="any" value=<%=bean.getPrice()%>  required>&euro;
					</label>
				</h4>			
				<br>
				<h4>
					<label>
						Quantit&aacute;: <br>
						<h5>
						Taglia M: <input style="width:7%;" name="tagliaM" type="number" min="0" value=<%=bean.getTaglie().getQuantitaM()%> required ><br>
						Taglia L: <input style="width:7%;" name="tagliaL" type="number" min="0" value=<%=bean.getTaglie().getQuantitaL()%> required><br>
						Taglia XL: <input style="width:7%;" name="tagliaXL" type="number" min="0" value=<%=bean.getTaglie().getQuantitaXL()%> required><br>
						Taglia XXL: <input style="width:7%;" name="tagliaXXL" type="number" min="0" value=<%=bean.getTaglie().getQuantitaXXL()%> required><br>
						</h5>
					</label>
				</h4>

				<h4>
					<label>
						Statistiche: <br>
						<textarea cols="100" name="stats" maxlength="1000" rows="10" required placeholder="enter description">
						<%= bean.getStats() %>
						</textarea><br>
					</label>
					
               	 	<label for="upload-input">File: </label>
                	<input type="file" class="input_container" name="file" id="upload-input">
				</h4>
				<br>
				<br>
				<div class="buttons">
				<input onClick="addValuesToLink();" class="submitButton" type="submit" value="Modifica">
				<input type="reset" class="resetButton"  value="Reset">
				</div>
			</form>
	    	</div>
	  	</div>
		</div>
	</div>
</body>
</html>