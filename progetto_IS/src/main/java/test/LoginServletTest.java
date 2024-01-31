package test;

import java.io.IOException;   
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarrello.ICarrelloDao;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestionecarta.ICartaDaoData;
import gestionegestioneutenti.GestoreDaoDataSource;
import gestionegestioneutenti.IGestoreDao;
import gestioneutenti.Utente;

@WebServlet("/LoginServletTest")
public class LoginServletTest extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
			String username = request.getParameter("email");
			String password = request.getParameter("password");
			String jspName = request.getParameter("jspName");
			List<String> errors = new ArrayList<>();
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("login.jsp");

        	if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
    	        errors.add("Il campo username e password non possono essere vuoti!");
    	        request.setAttribute("errors", errors);
    	        dispatcherToLoginPage.forward(request, response);
    	        return;
    	    }

			
			Utente match= new Utente(); //Caso utente trovato 
			match.setEmail(username);
			match.setPassword(password);
			match.setRuolo("Utente");
				
			/*
			if (match == null) {
		        errors.add("Username o password non validi!");
		        request.setAttribute("errors", errors);
		        dispatcherToLoginPage.forward(request, response);
		        return;
		    } */
			
			String result = match.getRuolo();
			if( result.equals("Gestore Ordini")) { //sono state usate credenziali di admin
				request.getSession().setAttribute("isAdmin", "Gestore Ordini"); //inserisco il token nella sessione
				request.getSession().setAttribute("logged", match);
				response.sendRedirect("store.jsp");	
			}	else if( result.equals("Gestore Utenti")) {
					request.getSession().setAttribute("isAdmin", "Gestore Utenti"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");	
				} else if( result.equals("Gestore Prodotti")) {
					request.getSession().setAttribute("isAdmin", "Gestore Prodotti"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");	
				} else if( result.equals("Utente") && result!=null ) { //sono state usate credenziali di un utente
					//Recupero il carello salvato in maniera persistente
					//request.getSession().setAttribute("cart", carrello);
					request.getSession().setAttribute("isAdmin", "Utente"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");
			}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	private static final long serialVersionUID = 1L;

	public void doPostNonCorretta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("email");
	    String password = request.getParameter("password");
	    String jspName = request.getParameter("jspName");
	    List<String> errors = new ArrayList<>();
	    RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("login.jsp");

	    if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
	        errors.add("Il campo username e password non possono essere vuoti!");
	        request.setAttribute("errors", errors);
	        dispatcherToLoginPage.forward(request, response);
	        return;
	    }

	    Utente match = null; // Non è stato trovato nessun utente

	    if (match == null) {
	        errors.add("Username o password non validi!");
	        request.setAttribute("errors", errors);
	        dispatcherToLoginPage.forward(request, response);
	        return;
	    } 
	}
}
