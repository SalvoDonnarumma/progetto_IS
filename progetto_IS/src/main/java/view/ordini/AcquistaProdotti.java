package view.ordini;

import java.io.IOException; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import gestionecarrello.Carrello;
import gestioneprodotti.Prodotto;
import storagelayer.IProductDao;
import storagelayer.ProductDaoDataSource;

/**
 * Servlet implementation class AcquistaProdotti
 */
@WebServlet("/AcquistaProdotti")
public class AcquistaProdotti extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcquistaProdotti() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Collection <Prodotto> products; //creo una lista di prodotti
		Carrello cart = (Carrello) request.getSession().getAttribute("cart");
		products= cart.getAllProduct();
		request.getSession().setAttribute("products", products);
		request.getSession().setAttribute("products", products);
		
		
		List<String>sizes = new ArrayList<>();
		List<Integer>qnts = new ArrayList<>();
		int i;
		for (i = 0; i < products.size(); i++) {
		    sizes.add(request.getParameter("sz" + i));
		    System.out.println("Size: " + request.getParameter("sz" + i));
		    System.out.println("Qnt: " + request.getParameter("qnt" + i));
		    qnts.add(Integer.parseInt(request.getParameter("qnt" + i)));
		}
		
		request.getSession().removeAttribute("sizes");
		request.getSession().setAttribute("sizes", sizes);
		request.getSession().removeAttribute("qnts");
		request.getSession().setAttribute("qnts", qnts);

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
