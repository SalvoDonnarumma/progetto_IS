package gestionegestioneutenti;

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

import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

/**
 * Servlet implementation class CreaNuovoGestoreValidator
 */
@WebServlet("/CreaNuovoAdminValidator")
public class CreaNuovoGestoreValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreaNuovoGestoreValidator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IGestoreDao gestoreDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		gestoreDao = new GestoreDaoDataSource(ds);
		IUserDao userDao = new UserDaoDataSource(ds);
		
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String cognome = request.getParameter("cognome");
		String telefono = request.getParameter("phone");
		String ruolo = request.getParameter("ruolo");
			
		Utente bean = new Utente();
		bean.setEmail(email);
		bean.setPassword(password);
		bean.setCognome(cognome);
		bean.setTelefono(telefono);
		bean.setRuolo(ruolo);
					
		Boolean result = false;
		List<String> errors = new ArrayList<>();
		try {
			List<String> emails = userDao.getAllEmails();
			for( String emaildb : emails) 
			if(emaildb.equals(email))
					result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			} 			        
		if( result ) {
			errors.add("L'email che hai inserito non è disponibile!");
			request.setAttribute("errors", errors);
			RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("/admin/insertAmm.jsp");
			dispatcherToLoginPage.forward(request, response);
			return;
		}			
			
		RequestDispatcher dispatcher = null;		
        dispatcher = getServletContext().getRequestDispatcher("/CreaNuovoGestore");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
