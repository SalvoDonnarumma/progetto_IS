package gestionecarta;

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
import gestioneutenti.Utente;

/**
 * Servlet implementation class salvaCarta
 */
@WebServlet("/salvaCarta")
public class salvaCarta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public salvaCarta() {
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
		
		String proprietario = request.getParameter("nome");
		String numero_carta = request.getParameter("numero_carta");
		String data = request.getParameter("data");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("modifypaymentcard.jsp");
		List<String> errors = new ArrayList<>();
		
		if( proprietario == null || proprietario.trim().isEmpty()) {
			System.out.println(proprietario);
			errors.add("Il campo username non può essere vuoto!");
			request.setAttribute("errors", errors);
        	dispatcher.forward(request, response);
        	return;
		}
		
		if( numero_carta == null || !CardValidator.isValidFormat(numero_carta)) {
			System.out.println(numero_carta);
			errors.add("Il campo numero carta non rispetta il formato richiesto!");
			request.setAttribute("errors", errors);
        	dispatcher.forward(request, response);
        	return;
		}	
		
		
		String mm, aa;
		if( data == null || data.trim().isEmpty()) {
			System.out.println(data);
			errors.add("Il campo data non rispetta il formato richiesto!");
			request.setAttribute("errors", errors);
        	dispatcher.forward(request, response);
        	return;
		} else {
			mm = data.substring(0, 2);
			aa = data.substring(3, 7);
			
			if( !CardValidator.isValidMonth(convertiNumeroInMese(mm)) ) {
				System.out.println(mm);
				errors.add("Il campo data non rispetta il formato richiesto!");
				request.setAttribute("errors", errors);
	        	dispatcher.forward(request, response);
	        	return;
			}
			
			if( !CardValidator.isYearNotExpired(aa) ) {
				System.out.println(aa);
				errors.add("Il campo  data non rispetta il formato richiesto!");
				request.setAttribute("errors", errors);
	        	dispatcher.forward(request, response);
	        	return;
			}
		}
		
		errors.clear();
		Utente logged = (Utente) request.getSession().getAttribute("logged");
		Carta carta =  new Carta();
		carta.setIdCarta(logged.getId());
		carta.setData_scadenza(mm+"/"+aa);
		carta.setNumero_carta(numero_carta);
		carta.setProprietario(proprietario);
		try {
			if(cartaDao.recuperaCarta(logged)!= null) {
				cartaDao.cancellaCarta(carta);
			}
			cartaDao.salvaCarta(carta);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		logged.setCarta(carta);
		request.getSession().removeAttribute("logged");
		request.getSession().setAttribute("logged", logged);
		dispatcher = null;	
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
	
	private static String convertiNumeroInMese(String numeroMese) {
        int numero = Integer.parseInt(numeroMese);

        if (numero < 1 || numero > 12) {
            return "Numero mese non valido";
        }

        // Array con i nomi dei mesi
        String[] nomiMesi = {
                "Gennaio", "Febbraio", "Marzo", "Aprile",
                "Maggio", "Giugno", "Luglio", "Agosto",
                "Settembre", "Ottobre", "Novembre", "Dicembre"
        };

        return nomiMesi[numero - 1];
    }

}
