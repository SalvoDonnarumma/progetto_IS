package gestionecarrello;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import checking.CheckException;
import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;

/**
 * Servlet implementation class AggiungiAlCarrello
 */
@WebServlet("/AggiungiAlCarrello")
public class AggiungiAlCarrello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AggiungiAlCarrello() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		IProductDao productDao = new ProductDaoDataSource(ds);
        
		String id= request.getParameter("idProdotto");
        String quantita = request.getParameter("quantita");

        Carrello cart = (Carrello) session.getAttribute("cart");
		
		if(  id!=null && !id.equalsIgnoreCase("null") ) {
				Integer idi = Integer.parseInt(request.getParameter("idProdotto"));
				Prodotto product = new Prodotto();
				try {
					product.setCode(idi);
					product = productDao.doRetrieveByKey(product);
					product.setTaglie(productDao.getSizesByKey(product));
				} catch (SQLException | CheckException e1) {
					e1.printStackTrace();
				}

				if( cart == null ) {
					cart = new Carrello();
					session.setAttribute("cart", cart);
				}
				
				cart.addProduct(product); //aggiungo un prodotto solo se non è già presente nel carrello
				
				session.removeAttribute("cart");
				session.setAttribute("cart", cart);
		}	
		if(cart == null) {
			cart = new Carrello();
			request.getSession().setAttribute("cart", cart);
		}

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"numeroProdottiCarrello\": " + cart.getSize() + "}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
