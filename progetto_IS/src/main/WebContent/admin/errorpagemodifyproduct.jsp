<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Errore Modifica Prodotto</title>
    <script>
        function goBack() {
            window.history.back();
        }
    </script>
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
        <h1>Errore Modifica o Inserimento Prodotto</h1>
        <p>I dati inseriti per la modifica/inserimento del prodotto sono errati. Si prega di controllare e riprovare.</p>
   		 <p>Torna indietro e correggi i dati</p> <br>
   		 <p><a href="./admin/ProductView.jsp">Torna al Catalogo</a></p>
    </div>
</body>
</html>

