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

import gestioneprodotti.IProductDao;
import gestioneprodotti.ProductDaoDataSource;
/**
 * Servlet implementation class ProductControl
 */
public class OrdinaProdotti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		
		String sort = request.getParameter("sort");
			try {
				if (sort == null) {
					request.removeAttribute("products");
					request.setAttribute("products", productDao.doRetrieveAll(sort));
				} else if (sort.equals("categoria")) {
					request.removeAttribute("products");
					request.setAttribute("products", productDao.sortByCategoria(sort));
				} else if (sort.equals("nome")) {
					request.removeAttribute("products");
					request.setAttribute("products", productDao.sortByName(sort));
				}
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
