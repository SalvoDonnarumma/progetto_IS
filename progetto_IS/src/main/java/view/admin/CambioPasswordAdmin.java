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
			String confPass = request.getParameter("confirmPassword");
			String email = request.getParameter("email");
			Utente bean = new Utente();
			bean.setEmail(email);			
		    Utente old = userDao.doRetrieveByEmail(bean);		
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
