<%
	String isSomeoneLogged = (String) request.getSession().getAttribute("isAdmin");
	if( (isSomeoneLogged == null) || isSomeoneLogged.equalsIgnoreCase("utente") || isSomeoneLogged.equals("Gestore Ordini") || isSomeoneLogged.equals("Gestore Utenti")  ){
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
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .form-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        h2 {
            color: #333;
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 20px;
            text-align: left;
            text-transform: uppercase;
        }

        .form-group {
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }

        .form-group label {
            flex: 1;
            margin-right: 10px;
            font-weight: bold;
            color: #333;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            flex: 3;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f5f5f5;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 10px;
            resize: vertical;
        }

        .form-group select {
            width: 25%;
        }

        .form-group input[type="submit"],
        .form-group input[type="reset"] {
            flex: 3;
            height: 40px;
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

        .form-group input[type="submit"]:hover,
        .form-group input[type="reset"]:hover {
            border-color: #2691d9;
            transition: .5s;
            color: white;
            background-color: #51b0f0;
        }

        @media (max-width: 800px) {
            .form-container {
                width: 90%;
            }
        }
    </style>
	<script src="../scripts/validate.js"></script>
    <title>Aggiunta prodotto</title>
    <jsp:include page="../header.jsp" flush="true"/>
</head>
<body>    
    <div class="form-container">
        <h2>Inserisci Prodotto</h2>
        <form action="<%=request.getContextPath()%>/creaProdotto" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="insert">

            <div class="form-group">
                <label for="name">Name:</label>
                <input name="nome" type="text" maxlength="25" id="nome"
                onChange="validateProdottiForm(this, document.getElementById('errorName'), nameOrLastnameErrorMessage)">
            </div>
			<span id="errorName"> </span>
			
            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <select name="categoria" id="categoria-select" required>
                    <option value="Coltelli" selected>Coltelli</option>
                    <option value="Erogatori">Erogatori</option>
                    <option value="Guanti">Guanti</option>
                    <option value="Maschere">Maschere</option>
                    <option value="Mute">Mute</option>
                    <option value="Pinne">Pinne</option>
                    <option value="Torce">Torce</option>
                </select>
            </div>

            <div class="form-group">
                <label for="price">Prezzo:</label>
                <input name="price" style="width:20%;"  min="0" value="0" step="any" required id="price"
                onChange="validateProdottiForm(this, document.getElementById('errorPrice'), nameOrLastnameErrorMessage)">
            </div>
			<span id="errorPrice" style="align-items: center;"> </span>
			
            <div class="form-group">
                <label>Quantit&aacute;: </label><br> 
                <label>Taglia M: <input style="width:30%;" name="tagliaM" type="number" min="0" id="tagliaM"
                onChange="validateProdottiForm(this, document.getElementById('errorTaglie'), nameOrLastnameErrorMessage)"></label>
                <label>Taglia L: <input style="width:30%;" name="tagliaL" type="number"  id="tagliaL"
                onChange="validateProdottiForm(this, document.getElementById('errorTaglie'), nameOrLastnameErrorMessage)"></label>
                <label>Taglia XL: <input style="width:30%;" name="tagliaXL" type="number" min="0" id="tagliaXL"
                onChange="validateProdottiForm(this, document.getElementById('errorTaglie'), nameOrLastnameErrorMessage)"></label>
                <label>Taglia XXL: <input style="width:30%;" name="tagliaXXL" type="number" min="0" id="tagliaXXL"
                onChange="validateProdottiForm(this, document.getElementById('errorTaglie'), nameOrLastnameErrorMessage)"></label>
            </div>
			<span id="errorTaglie"></span>
			 
            <div class="form-group">
                <label for="description">Descrizione:</label>
                <textarea name="descrizione" maxlength="500" rows="10" required placeholder="Enter description" id="descrizione"
                onChange="validateProdottiForm(this, document.getElementById('errorDescrizione'), nameOrLastnameErrorMessage)"></textarea>
            </div>
			<span id="errorDescrizione"> </span>
			
            <div class="form-group">
                <label for="stats">Statistiche: </label>
                <textarea name="stats" maxlength="500" rows="10" required placeholder="Enter description" id="statistiche"
                onChange="validateProdottiForm(this, document.getElementById('errorStats'), nameOrLastnameErrorMessage)"></textarea>
            </div>
            <span id="errorStats"> </span>
            
            <div class="form-group">
                <label for="upload-input">File: </label>
                <input type="file" class="input_container" name="file" id="upload-input">
            </div>

            <div class="form-group">
                <input type="submit" value="Add">
                <input type="reset" value="Reset">
            </div>
        </form>
    </div>
</body>

</html>