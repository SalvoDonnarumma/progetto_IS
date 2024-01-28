package gestioneordini;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import checking.CheckException;
import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;

/**
 * Servlet implementation class AcquistaProdotto
 */
@WebServlet("/AcquistaProdotto")
public class AcquistaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcquistaProdotto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		request.getSession().setAttribute("products", null);
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IProductDao productDao = new ProductDaoDataSource(ds);;
		Collection <Prodotto> products = new LinkedList<>(); //creo una lista di prodotti
		Prodotto p = new Prodotto();
		p.setCode(id);
		try {
			p = productDao.doRetrieveByKey(p);
			p.setTaglie(productDao.getSizesByKey(p));
			products.add(productDao.doRetrieveByKey(p));
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		 //inserisco solo un prodotto nella lista
		/* in questo modo nella pagina di acquisto (anche se singolo) si lavora sempre con 
		 * un lista, mi permette di avere un acquisto omogoneo di uno o più prodotti*/
		request.getSession().setAttribute("products", products);
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
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
