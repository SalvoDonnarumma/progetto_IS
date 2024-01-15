package view.prodotti;
import java.io.IOException;    
import java.sql.SQLException;
import java.util.List;

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
@WebServlet("/modificaProdotto")
public class ModificaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		String action = request.getParameter("action");	
		if(action.equals("read")) {
			int id = Integer.parseInt(request.getParameter("id"));
			request.removeAttribute("product");
			Prodotto p = new Prodotto();
			p.setCode(id);
			try {
				request.setAttribute("product", productDao.doRetrieveByKey(p));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RequestDispatcher dispatcher = null;	
			dispatcher = getServletContext().getRequestDispatcher("/admin/modifyproduct.jsp");		
			dispatcher.forward(request, response);
		} else if(action.equals("modify")) {
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
					int code = Integer.parseInt(request.getParameter("id"));
					productDao.doUpdate(code, bean);
					
					Taglie sizes = new Taglie();			
					sizes.setQuantitaM(quantityM);
					sizes.setQuantitaL(quantityL);
					sizes.setQuantitaXL(quantityXL);
					sizes.setQuantitaXXL(quantityXXL);
					bean.setTaglie(sizes);
					productDao.doUpdateSizes(code, sizes);
				
					RequestDispatcher dispatcher = null;	
					dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");	
					dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
	}
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
