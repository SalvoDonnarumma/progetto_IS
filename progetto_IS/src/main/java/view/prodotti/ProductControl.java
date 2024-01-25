package view.prodotti;
import java.io.IOException;    
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import gestioneprodotti.Prodotto;
import gestioneprodotti.Taglie;
import storagelayer.IProductDao;
import storagelayer.ProductDaoDataSource;
/**
 * Servlet implementation class ProductControl
 */
public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		
		try {
			request.removeAttribute("products");
			request.setAttribute("products", productDao.doRetrieveAll(null));
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
