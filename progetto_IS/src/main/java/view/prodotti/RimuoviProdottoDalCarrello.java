package view.prodotti;

import java.io.IOException;  
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gestioneordini.Carrello;
import gestioneprodotti.Prodotto;


/**
 * Servlet implementation class RemoveProductFromCart
 */
@WebServlet("/RemoveProductFromCart")
public class RimuoviProdottoDalCarrello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Carrello cart = (Carrello) session.getAttribute("cart");
		Prodotto removeP = new Prodotto();
		removeP.setCode(Integer.parseInt(request.getParameter("id")));
		cart.removeProduct(removeP);
		request.getSession().removeAttribute("cart");
		request.getSession().setAttribute("cart", cart);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
