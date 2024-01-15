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
import gestioneutenti.Utente;

/**
 * Servlet implementation class AdminControl
 */
@WebServlet("/CambioPasswordAdmin")
public class CambioPasswordAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IAdminDao adminDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		adminDao = new AdminDaoDataSource(ds);
		
		try {
			String oldPass = request.getParameter("currentPassword");
			String newPass = request.getParameter("newPassword");
			String confPass = request.getParameter("confirmPassword");
			Utente bean = (Utente) request.getSession().getAttribute("logged");
					
			List<String> errors = new ArrayList<>();
		    RequestDispatcher dispatcherChangePassPage = request.getRequestDispatcher("changepass.jsp");

		    if( !newPass.equals(confPass) ) {
				errors.add("La password nuova e la password di conferma non corrispondono!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
		    Utente old = new Utente();
		    old.setPassword(oldPass);
			if( !adminDao.validateOldPassword(bean, old)) {
				errors.add("La vecchia password inserita non &egrave; valida!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
			if( newPass.length()<12 ) {
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
			bean.setPassword(confPass);
			adminDao.changePassAdmin(bean);			
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
