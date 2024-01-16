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
@WebServlet("/cambioPasswordAdmin")
public class CambioPasswordAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IAdminDao adminDao = null;
		IUserDao userDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		adminDao = new AdminDaoDataSource(ds);
		userDao = new UserDaoDataSource(ds);
		
		try {
			String oldPass = request.getParameter("currentPassword");
			String newPass = request.getParameter("newPassword");
			String confPass = request.getParameter("confirmPassword");
			String email = request.getParameter("email");
			System.out.println("Email: "+email);
			System.out.println("Pass1: "+newPass);
			System.out.println("Pass2: "+confPass);
			System.out.println("CurrentPass: "+oldPass);
			Utente bean = new Utente();
			bean.setEmail(email);
			
			List<String> errors = new ArrayList<>();
		    RequestDispatcher dispatcherChangePassPage = request.getRequestDispatcher("admin/changepassadmin.jsp");

		    if( !newPass.equals(confPass) ) {
				errors.add("La password nuova e la password di conferma non corrispondono!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
		    Utente old = userDao.doRetrieveByEmail(bean);
		    bean.setPassword(oldPass);
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
					
			old.setPassword(confPass);
			adminDao.changePassAdmin(old);			
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
