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
import checking.DBException;

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
			Prodotto p = new Prodotto();
			p = productDao.doRetrieveByKey(id_p);
			p.setTaglie(productDao.getSizesByKey(p));
			request.setAttribute("product", p);
		} catch (SQLException | CheckException e) {
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
