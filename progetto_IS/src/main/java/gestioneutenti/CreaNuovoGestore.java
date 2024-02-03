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
		IUserDao gestoreDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		gestoreDao = new UserDaoDataSource(ds);
		
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
					
		try {
			gestoreDao.doSaveGestore(bean);			
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
