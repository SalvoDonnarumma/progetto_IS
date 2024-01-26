package gestioneordini;

import java.io.IOException; 
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OttieniStoricoOrdini")
public class OttieniStoricoOrdini extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IOrderDao orderDao = new OrderDaoDataSource(ds);
		Collection <Ordine> orders= new LinkedList<>(); //creo una lista di ordini
		
		String idUtente = request.getParameter("idUtente");
		if( idUtente == null ) {
			try {
				orders = orderDao.doRetrieveAllOrders(null);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		
		Gson json = new Gson();
		PrintWriter out = response.getWriter();
		out.write(json.toJson(orders));	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
