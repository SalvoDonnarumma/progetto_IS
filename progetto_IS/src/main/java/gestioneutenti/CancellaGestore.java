package gestioneutenti;

import java.io.IOException;    
import java.sql.SQLException;
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
@WebServlet("/CancellaAdmin")
public class CancellaGestore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IUserDao gestoreDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		gestoreDao = new UserDaoDataSource(ds);

		Utente admin = new Utente();
		try {
			String email = request.getParameter("email");
			admin.setEmail(email);
			gestoreDao.doDeleteGestore(admin);				
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