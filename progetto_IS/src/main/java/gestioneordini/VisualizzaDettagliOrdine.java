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

import checking.CheckException;

/**
 * Servlet implementation class visualizzaDettagliOrdine
 */
@WebServlet("/visualizzaDettagliOrdine")
public class VisualizzaDettagliOrdine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idOrdine = request.getParameter("id");
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IOrderDao orderDao = new OrderDaoDataSource(ds);
		Collection <ProdottoOrdinato> order= new LinkedList<>(); //creo una lista di ordini
		
		try {
			Ordine o = new Ordine();
			o.setIdOrdine(Integer.parseInt(idOrdine));
			order = orderDao.doRetrieveById(o);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
			
		System.out.println("Prodotti: "+order);
		Gson json = new Gson();
		PrintWriter out = response.getWriter();
		out.write(json.toJson(order));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
