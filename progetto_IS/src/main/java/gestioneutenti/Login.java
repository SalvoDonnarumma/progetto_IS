package gestioneutenti;
import java.io.IOException;    
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarrello.ICarrelloDao;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestionecarta.ICartaDaoData;

@WebServlet("/Login")
public class Login extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
			IUserDao userDao = null;	
			ICarrelloDao carrelloDao = null;
			DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
			userDao = new UserDaoDataSource(ds);
			carrelloDao = new CarrelloDaoDataSource(ds);
			
			String username = request.getParameter("email");
			String password = request.getParameter("password");
			String jspName = request.getParameter("jspName");
			List<String> errors = new ArrayList<>();
        	RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("login.jsp");

			if(username == null || username.trim().isEmpty()) {
				errors.add("");
			}
            if(password == null || password.trim().isEmpty()) {
            	errors.add("");
			}
            if (!errors.isEmpty()) {
            	request.setAttribute("errors", errors);
            	dispatcherToLoginPage.forward(request, response);
            	return;
            }
			
			Utente match= new Utente();
			match.setEmail(username);
			match.setPassword(password);
			try {
				if(jspName.equals("loginutenti"))
					match=userDao.login(match);
				else if(jspName.equals("loginadmin"))
					match=userDao.loginGestore(match);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (CheckException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if( match==null ) {
				errors.add("Username o password non validi!");
				request.setAttribute("errors", errors);
				dispatcherToLoginPage.forward(request, response);
				return;
			} 
					
			ICartaDaoData cartaDao = null;
			cartaDao = new CartaDaoDataSource(ds);	
			try {
				Carta carta = cartaDao.recuperaCarta(match);
				match.setCarta(carta);
			} catch (SQLException | CheckException e1) {
				e1.printStackTrace();
			}
			
			String result = match.getRuolo();
			if( result.equals("Gestore Ordini")) { //sono state usate credenziali di admin
				request.getSession().setAttribute("isAdmin", "Gestore Ordini"); //inserisco il token nella sessione
				request.getSession().setAttribute("logged", match);
				response.sendRedirect("store.jsp");	
			}	else if( result.equals("Gestore Utenti")) {
					request.getSession().setAttribute("isAdmin", "Gestore Utenti"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");	
				} else if( result.equals("Gestore Prodotti")) {
					request.getSession().setAttribute("isAdmin", "Gestore Prodotti"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");	
				} else if( result.equals("Utente") && result!=null ) { //sono state usate credenziali di un utente
					//Recupero il carello salvato in maniera persistente
					Carrello carrello = new Carrello();
					try {
						carrello = carrelloDao.recuperaCarrello(match);
					} catch (SQLException | CheckException e) {
						e.printStackTrace();
					}
					request.getSession().setAttribute("cart", carrello);
					request.getSession().setAttribute("isAdmin", "utente"); //inserisco il token nella sessione
					request.getSession().setAttribute("logged", match);
					response.sendRedirect("store.jsp");
			}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	private static final long serialVersionUID = 1L;


}
