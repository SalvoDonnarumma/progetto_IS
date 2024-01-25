package view.ordini;

import java.io.IOException;  
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import gestioneordini.Ordine;
import storagelayer.IOrderDao;
import storagelayer.OrderDaoDataSource;
import view.ordini.*;

@WebServlet("/RemoveOrderServlet")
public class CancellaOrdine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IOrderDao orderDao = new OrderDaoDataSource(ds);
		try {
			Ordine toRemove = new Ordine();
			toRemove.setIdOrdine(Integer.parseInt(request.getParameter("idOrdine")));
			toRemove.setStato("RIMOSSO");
			orderDao.changeOrderState(toRemove);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
