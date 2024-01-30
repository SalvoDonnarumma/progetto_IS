package gestioneutenti;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarrello.ICarrelloDao;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		ICarrelloDao carrelloDaoDataSource = new CarrelloDaoDataSource(ds);
		Utente inSession = (Utente) request.getSession().getAttribute("logged");
		Carrello carrello = (Carrello) request.getSession().getAttribute("cart");
		try {
			if(inSession != null && carrello != null) {
				carrello.setIdcarrello(inSession.getId());
				carrelloDaoDataSource.salvaCarrello(carrello);
			}
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() + "/login.jsp");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}
