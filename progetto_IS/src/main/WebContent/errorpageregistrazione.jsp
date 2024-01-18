<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore Registrazione Utente</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 70vh;
        }

        .error-container {
            text-align: center;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #e44d26;
        }

        p {
            color: #333;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Errore Registrazione Utente</h1>
        <p>I dati inseriti per la registrazione utente sono errati. Si prega di controllare e riprovare.</p>
        <p><a href="./registrazione.jsp">Riprova la registrazione</a></p>
    </div>
</body>
</html>
    