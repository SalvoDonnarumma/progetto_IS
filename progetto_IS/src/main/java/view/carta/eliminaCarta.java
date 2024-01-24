package view.carta;

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
import gestionecarta.CartaDaoDataSource;
import gestionecarta.ICartaDaoData;
import gestioneutenti.Utente;

/**
 * Servlet implementation class eliminaCarta
 */
@WebServlet("/eliminaCarta")
public class eliminaCarta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public eliminaCarta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ICartaDaoData cartaDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		cartaDao = new CartaDaoDataSource(ds);
		
		System.out.println("Esecuzione della servlet eliminaCarta");
		
		Utente logged = (Utente) request.getSession().getAttribute("logged");
		try {
			cartaDao.cancellaCarta(logged.getCarta());
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		logged.setCarta(null);
		request.getSession().removeAttribute("logged");
		request.getSession().setAttribute("logged", logged);
		RequestDispatcher dispatcher = null;	
		dispatcher = getServletContext().getRequestDispatcher("/modifypaymentcard.jsp");
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
