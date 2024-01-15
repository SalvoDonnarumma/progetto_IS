<%
	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equals("Utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
		response.sendRedirect(request.getContextPath()+"/login.jsp");	
	return;
	}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html lang="it">
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>

<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/style.css">
	<title> Inserimento nuovo prodotto </title>
<style>
@charset "ISO-8859-1";

/* Impostazioni dei link */
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

/* Stili per il form */
form {
	width: 40%;
	margin: 0 auto;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	background-color: #D9DCD6;
	padding: 25px;
	text-align: center;
}

/* Stili per le sezioni del modulo */
.InsertProdotto, .UploadPhoto, .InsertAmministratore {
	width: 50%;
	border: 1px solid black;
	border-radius: 5px;
	padding: 20px;
	margin: 0 auto;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	background-color: #f2f2f2;
}

/* Stile per gli input del modulo */
form input[type=text], form input[type=number], form input[type=password],
	form input[type=date], form input[type=range], form input[type=email],
	form input[type=url], form input[type=time], form input[list] {
	width: 100%;
	padding: 10px;
	font-size: 16px;
	border: none;
	border-radius: 5px;
	background-color: #f5f5f5;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	margin-bottom: 10px;
	margin-right: 30px;
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
input[type="submit"], input[type="reset"] {
	width: 50%;
	height: 25%;
	border: 1px solid;
	background: #2691d9;
	border-radius: 10px;
	font-size: 18px;
	color: #e9f4fb;
	font-weight: 700;
	cursor: pointer;
	outline: none;
	position: relative;
}

input[type="submit"]:hover, input[type="reset"]:hover {
	border-color: #2691d9;
	transition: .5s;
	color: white;
	background-color: #51b0f0;
	width: 60%;
}

/* Stili per le sezioni del modulo */
.InsertProdotto, .UploadPhoto, .InsertAmministratore {
	width: 50%;
	border: 1px solid black;
	padding: 20px;
	margin: 0 auto;
}
/* Stili per il titolo del form */
.form-title {
	font-size: 28px;
	font-weight: bold;
	color: #000;
	margin-bottom: 20px;
	text-align: left;
	text-transform: uppercase;
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

@media ( max-width : 800px) {
	form {
	width: 90%;
	margin: 0 auto;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	background-color: #daf0ef;
	padding: 25px;
	}
}
.form-group {
    margin-bottom: 10px;
    display: flex;
    align-items: center;
  }
  
  .form-group_ {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    
  }

  .form-group label {
    flex: 1;
    margin-right: 10px;
  }

  .form-group textarea {
    flex: 3;
  }
</style>

<title>Aggiunta prodotto</title>
<jsp:include page="../header.jsp" flush="true"/>
</head>
<body>	
	<h2 class="form-title">Inserisci Prodotto</h2>
	
	<form action="<%=request.getContextPath()%>/creaProdotto" method="post">
  <input type="hidden" name="action" value="insert">

  <div class="form-group">
    <label for="name">Name:</label>
    <input name="nome" type="text" maxlength="25" required placeholder="enter name">
  </div>

  <div class="form-group_">
    <label for="categoria">Categoria:</label>
    <select style="width:25%;" name="categoria" id="categoria-select" onchange="searchAndFilter()" required>
						    <option value="Coltelli">Coltelli</option>
						    <option value="Erogatori">Erogatori</option>
						    <option value="Guanti">Guanti</option>
						    <option value="Maschere">Maschere</option>
						    <option value="Mute">Mute</option>
						    <option value="Pinne">Pinne</option>
						    <option value="Torce">Torce</option>
	</select>
  </div>
  
  <div class="form-group_">
  <label for="price">Price:</label><br> 
		<input name="price" style="width:20%;" type="number" min="0" value="0" step="any" required><br>
  </div>
  
  <div class="form-group">
  <label>
	Quantit&aacute;: <br>
			Taglia M: <input style="width:15%;" name="tagliaM" type="number" min="0" ><br>
			Taglia L: <input style="width:15%;" name="tagliaL" type="number" min="0"><br>
			Taglia XL: <input style="width:15%;" name="tagliaXL" type="number" min="0"><br>
			Taglia XXL: <input style="width:15%;" name="tagliaXXL" type="number" min="0"><br>
		</label>
  </div>

  <div class="form-group">
    <label for="description">Description:</label>
    <textarea name="descrizione" maxlength="500" rows="10" required placeholder="enter description"></textarea>
  </div>

  <div class="form-group">
    <label for="Stats">Statistiche: </label>
    <textarea name="stats" maxlength="500" rows="10" required placeholder="enter description"></textarea>
  </div>

  <div class="form-group">
    <input class="submit" type="submit" value="Add">
    <input type="reset" value="Reset">
  </div>
</form>
	</form>
	
</body>
</html>