package view.gestore;

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
import gestioneutenti.Utente;
import storagelayer.GestoreDaoDataSource;
import storagelayer.IGestoreDao;
import storagelayer.IUserDao;
import storagelayer.UserDaoDataSource;

/**
 * Servlet implementation class AdminControl
 */
@WebServlet("/cambioPasswordAdmin")
public class CambioPasswordGestore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IGestoreDao gestoreDao = null;
		IUserDao userDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		gestoreDao = new GestoreDaoDataSource(ds);
		userDao = new UserDaoDataSource(ds);
		
		try {
			String confPass = request.getParameter("confirmPassword");
			String email = request.getParameter("email");
			Utente bean = new Utente();
			bean.setEmail(email);			
		    Utente old = userDao.doRetrieveByEmail(bean);		
			old.setPassword(confPass);
			gestoreDao.changePassGestore(old);			
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
