package view.admin;

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
import gestioneadmin.AdminDaoDataSource;
import gestioneadmin.IAdminDao;
import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

/**
 * Servlet implementation class AdminControl
 */
@WebServlet("/CreaNuovoAdmin")
public class CreaNuovoAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IAdminDao adminDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		adminDao = new AdminDaoDataSource(ds);
		IUserDao userDao = new UserDaoDataSource(ds);
		
		try {
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
				adminDao.doSaveAdmin(bean);			
		} catch (SQLException e) {
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
