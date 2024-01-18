<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore di Validazione della Carta di Credito</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            margin: 50px;
        }

        .error-message {
            background-color: #ffcccc;
            padding: 20px;
            border: 1px solid #e74c3c;
            border-radius: 5px;
            color: #c0392b;
        }
    </style>
</head>
<body>
    <div class="error-message">
        <h2>Errore di durante la Validazione Del Pagamento</h2>
        <p>Uno dei campi per la validazione della carta di credito è scritto male. Per favore, verifica i dati inseriti.</p>
        <% String action = (String) request.getParameter("action");
        if(action.equals("purchaseOne")) { 	%>
        <p><a href="purchase.jsp?action=purchaseOne&&qnt0=<%=request.getParameter("qnt")%>&sz0=<%=request.getParameter("sz")%>" >Torna indietro e correggi i dati</a></p>
        <%} else { %>
        	<a href="purchase.jsp?action=purchaseAll&tot=<%=request.getParameter("tot")%>"> Torna indietro e correggi i dati </a>
        <%} %>
    </div>
</body>
</html>
