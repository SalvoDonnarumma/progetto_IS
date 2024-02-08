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
@WebServlet("/CreaNuovoAdmin")
public class CreaNuovoGestore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IUserDao userDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		userDao = new UserDaoDataSource(ds);
		
		String username = request.getParameter("email");
		String password = request.getParameter("password");
		String cognome = request.getParameter("cognome");
		String telefono = request.getParameter("phone");
		String ruolo = request.getParameter("ruolo");
			
		Utente bean = new Utente();
		bean.setEmail(username);
		bean.setPassword(password);
		bean.setCognome(cognome);
		bean.setTelefono(telefono);
		bean.setRuolo(ruolo);
			
		List<String> errors = new ArrayList<>();
		Boolean result = false;
		
        try {
			List<String> emails = userDao.getAllEmails();
			for( String email : emails) 
				if(email.equals(username))
					result = true;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        String registrazione = "admin/insertAmm.jsp";
        if( result ) {
        	errors.add("L'email che hai inserito non è disponibile!");
        	request.setAttribute("errors", errors);
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher(registrazione);
        	dispatcherToLoginPage.forward(request, response);
			return;
		}
		try {
			userDao.doSaveGestore(bean);			
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
			
		RequestDispatcher dispatcher = null;		
        dispatcher = getServletContext().getRequestDispatcher("/admin/UserView.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
