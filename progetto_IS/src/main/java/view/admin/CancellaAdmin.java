package view.admin;

import java.io.IOException;    
import java.sql.SQLException;
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
@WebServlet("/CancellaAdmin")
public class CancellaAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IAdminDao adminDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		adminDao = new AdminDaoDataSource(ds);

		Utente admin = new Utente();
		try {
			String email = request.getParameter("email");
			admin.setEmail(email);
			adminDao.doDeleteAdmin(admin);				
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
