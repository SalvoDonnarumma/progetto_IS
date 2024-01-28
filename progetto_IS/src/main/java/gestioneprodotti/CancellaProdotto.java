package gestioneprodotti;
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
 * Servlet implementation class ProductControl
 */
@WebServlet("/cancellaProdotto")
public class CancellaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Prodotto p = new Prodotto();
			p.setCode(id);
			productDao.doDelete(p);			
		} catch (SQLException | CheckException e) {
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
