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

/**
 * Servlet implementation class OttieniDettagliProdotto
 */
@WebServlet("/OttieniDettagliProdotto")
public class OttieniDettagliProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OttieniDettagliProdotto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IProductDao productDao = new ProductDaoDataSource(ds);
		
		int id = Integer.parseInt(request.getParameter("id"));
		Prodotto id_p = new Prodotto();
		id_p.setCode(id);
		request.removeAttribute("product");
		try {
			request.setAttribute("product", productDao.doRetrieveByKey(id_p));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = null;
		String to = request.getParameter("to");
		if( to == null)
			dispatcher = getServletContext().getRequestDispatcher("/singleproduct.jsp");
		else if(to.equals("modify"))
			dispatcher = getServletContext().getRequestDispatcher("/admin/modifyproduct.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
