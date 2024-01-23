package view.utenti;

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
import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;
import gestioneutenti.UtenteRegistrazioneValidator;


/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class RegistrazioneUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IUserDao userDao = null;		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		userDao = new UserDaoDataSource(ds);
	
		String username = request.getParameter("email");
		String name = request.getParameter("firstname");
		String surname = request.getParameter("lastname");
		String password = request.getParameter("password");
		String confpassword = request.getParameter("conf_password");
		String telefono = request.getParameter("phone");
		
		RequestDispatcher dispatcherToErrorPage = request.getRequestDispatcher("/errorpageregistrazione.jsp");
		
		List<String> errors = new ArrayList<>();
		/* controllo se le due password combaciano, in caso contrario blocco la registrazione 
		 * e segnalo all'utente tramite un alert (presente nel .js della registrazione)*/
		String registrazione = "registrazione.jsp";
		
		if( !UtenteRegistrazioneValidator.isValidEmail(username) )
			dispatcherToErrorPage.forward(request, response);
		
		if( !UtenteRegistrazioneValidator.isValidNome(name) )
			dispatcherToErrorPage.forward(request, response);
		
		if( !UtenteRegistrazioneValidator.isValidNome(surname) )
			dispatcherToErrorPage.forward(request, response);
		
		if( !UtenteRegistrazioneValidator.isValidTelefono(telefono) )
			dispatcherToErrorPage.forward(request, response);
		
        if ( !password.equals(confpassword) ) {  //controllo se password e conferma password sono uguali
        	request.setAttribute("errors", errors);
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher(registrazione);
        	dispatcherToLoginPage.forward(request, response);
        	return;
        }
        
        if( password.length()<12 ) {
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher(registrazione);
        	dispatcherToLoginPage.forward(request, response);
			return;
		}
		
        /* Quando un utente si deve registrare, controllo se l'email che vuole usare è
         * già presente e salvata nel database */
        Boolean result = false;
        try {
			List<String> emails = userDao.getAllEmails();
			for( String email : emails) 
				if(email.equals(username))
					result = true;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        if( result ) {
        	errors.add("L'email che hai inserito non è disponibile!");
        	request.setAttribute("errors", errors);
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher(registrazione);
        	dispatcherToLoginPage.forward(request, response);
			return;
		}
        
		Utente user= new Utente();
		Integer idUtente = null;
		try {
			user.setEmail(username);
			user.setPassword(password);
			user.setNome(name);
			user.setCognome(surname);
			user.setTelefono(telefono);		
			user.setRuolo("Utente");
			userDao.doSaveUser(user);
			idUtente = userDao.getLastCode();
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		user.setId(idUtente);
		request.getSession().setAttribute("isAdmin", "utente"); //inserisco il token nella sessione
		request.getSession().setAttribute("logged", user);
		response.sendRedirect("store.jsp");	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
