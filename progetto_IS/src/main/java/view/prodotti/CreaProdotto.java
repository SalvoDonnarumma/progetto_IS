package view.prodotti;
import java.io.IOException;   
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;

/**
 * Servlet implementation class ProductControl
 */
@WebServlet("/creaProdotto")
public class CreaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		try { 
			String categoria = request.getParameter("categoria");
			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			Double price = Double.parseDouble(request.getParameter("price"));
			int quantityM = Integer.parseInt(request.getParameter("tagliaM"));
			int quantityL = Integer.parseInt(request.getParameter("tagliaL"));
			int quantityXL = Integer.parseInt(request.getParameter("tagliaXL"));
			int quantityXXL = Integer.parseInt(request.getParameter("tagliaXXL"));
			String stats = request.getParameter("stats");
			Prodotto bean = new Prodotto();
			bean.setNome(nome);
			bean.setDescrizione(descrizione);
			bean.setCategoria(categoria);
			bean.setStats(stats);
			bean.setPrice(price);
			productDao.doSave(bean);
			Taglie sizes = new Taglie();			
			int code = productDao.doRetrieveByName(bean);
			sizes.setQuantitaM(quantityM);
			sizes.setQuantitaL(quantityL);
			sizes.setQuantitaXL(quantityXL);
			sizes.setQuantitaXXL(quantityXXL);
			bean.setTaglie(sizes);
			productDao.setSizesByKey(code, sizes);												
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");
					
		dispatcher.forward(request, response);
	}
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
