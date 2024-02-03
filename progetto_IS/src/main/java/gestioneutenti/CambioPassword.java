package gestioneutenti;

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


/**
 * Servlet implementation class AdminControl
 */
@WebServlet("/cambioPassword")
public class CambioPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IUserDao userDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		userDao = new UserDaoDataSource(ds);
		
		try {
			String oldPass = request.getParameter("currentPassword");
			String newPass = request.getParameter("newPassword");
			String confPass = request.getParameter("confirmPassword");
			String email = request.getParameter("email");
			Utente bean = (Utente) request.getSession().getAttribute("logged");
			
			System.out.println("Email: "+email);
			System.out.println("New pass: "+newPass);
			System.out.println("New pass2: "+confPass);
			System.out.println("Old pass: "+oldPass);
			List<String> errors = new ArrayList<>();
			RequestDispatcher dispatcherChangePassPage = null;
			Boolean no_email = false;
			
			if(bean == null || bean.getRuolo().equals("Gestore Utenti")) {
				dispatcherChangePassPage = request.getRequestDispatcher("changepass2.jsp");
			}else
				dispatcherChangePassPage = request.getRequestDispatcher("changepass.jsp");
		    
		    if( !newPass.equals(confPass) ) {
				errors.add("La password nuova e la password di conferma non corrispondono!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
		    if( bean == null ) { //sto chiamando il cambio password dalla pagina di login/l'utente non è loggato
		    	no_email = true;
		    	Utente id = new Utente();
		    	id.setEmail(email);
		    	try {
		    		bean = userDao.doRetrieveByEmail(id);
		    	} catch( CheckException e) {
		    		e.printStackTrace();
		    	}
		    	System.out.println("Bean: "+bean);
		    	if(bean == null) { //l'email inserita non esiste
		    		errors.add("L'email inserita non esiste!");
					request.setAttribute("errors", errors);
					dispatcherChangePassPage.forward(request, response);
					return;
		    	}
		    }
		    
		    Utente pass_to_match = new Utente();
		    pass_to_match.setPassword(oldPass);
		    if(!no_email) {
		    	if( !userDao.validateOldPassword(bean, pass_to_match) ) {
		    		errors.add("La vecchia password inserita non &egrave; valida!");
		    		request.setAttribute("errors", errors);
		    		dispatcherChangePassPage.forward(request, response);
		    		return;
		    	}
		    }
					
			if( newPass.length()<12 ) {
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
			userDao.changePass(confPass, bean);			
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = null;	
		dispatcher = getServletContext().getRequestDispatcher("/Logout");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
