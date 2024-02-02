package gestioneordini;

import java.io.IOException;   
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import gestionecarta.CardValidator;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestionecarta.ICartaDaoData;
import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneutenti.Utente;

/**
 * Servlet implementation class OrderControl
 */
@WebServlet("/creaOrdine")
public class CreaOrdine extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IProductDao productDao = null;
		IOrderDao orderDao = null;
	
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		orderDao = new OrderDaoDataSource(ds);
		
		List<String> errors = new ArrayList<>();
		String action = request.getParameter("action");
		Utente bean = (Utente) request.getSession().getAttribute("logged");
		if( bean == null ){
			response.sendRedirect("./login.jsp");		
			return;
		}
		String email = bean.getEmail();
		String indirizzo = request.getParameter("indirizzo");
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		String dateConsegna = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		
		String nome_carta = request.getParameter("nome");
		String numero_carta = request.getParameter("numero_carta");
		String mese_scadenza = request.getParameter("mese_scadenza");
		String anno_scadenza = request.getParameter("anno_scadenza");
		String cvv = request.getParameter("cvv");
		
		String salvaDati = request.getParameter("salvaDati");
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/errorpage.jsp?fromCart=false&qnt="+request.getParameter("qnt")+"&sz="+request.getParameter("sz"));
		if(nome_carta == null) {
			System.out.println("Nome carta è vuoto");
			dispatcher.forward(request, response);
		}
		if(numero_carta != null) {
			if(!CardValidator.isValidFormat(numero_carta)) {
				System.out.println("Numero di carta non è valido");
				dispatcher.forward(request, response);
			}
		}else {
			System.out.println("Campo numero carta vuoto");
			dispatcher.forward(request, response);
		}
		
		if(mese_scadenza != null) {
			if(!CardValidator.isValidMonth(mese_scadenza)) {
				System.out.println("Mese di scadenza non valido");
				dispatcher.forward(request, response);
			}
		}else {
			System.out.println("Mese scadenza vuoto");
			dispatcher.forward(request, response);
		}
		
		if(anno_scadenza != null) {
			if(!CardValidator.isYearNotExpired(anno_scadenza)) {
				System.out.println("Campo anno scadenza non valido");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo anno scadenza vuoto");
			dispatcher.forward(request, response);
		}
		
		if(cvv != null) {
			if(!CardValidator.isValidCVV(cvv)) {
				System.out.println("Campo anno scadenza vuoto");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo cvv vuoto");
			dispatcher.forward(request, response);
		}
		System.out.println("Carta Valida");
		
		dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/errorpageutente.jsp?fromCart=false&qnt="+request.getParameter("qnt")+"&sz="+request.getParameter("sz"));
		String nome_utente= request.getParameter("nome_utente");
		String city = request.getParameter("city");
		String stato = request.getParameter("stato");
		String cap = request.getParameter("cap");
		
		if(nome_utente != null) {
			if(!UtenteCheckoutValidator.isValidNome(nome_utente)) {
				System.out.println("Nome utente non valido");
				dispatcher.forward(request, response);
			}
		}else {
			System.out.println("Nome utente vuoto "+nome_utente);
			dispatcher.forward(request, response);
		}
		
		if(indirizzo != null) {
			if(!UtenteCheckoutValidator.isValidIndirizzo(indirizzo)) {
				System.out.println("Campo indirizzo non valido");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo indirizzo vuoto");
			dispatcher.forward(request, response);
		}
		
		if(city != null) {
			if(!UtenteCheckoutValidator.isValidCitta(city)) {
				System.out.println("Campo città non valido");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo città vuoto");
			dispatcher.forward(request, response);
		}
		
		if(stato != null) {
			if(!UtenteCheckoutValidator.isValidStato(stato)) {
				System.out.println("Campo stato non valido");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo stato vuoto "+stato);
			dispatcher.forward(request, response);
		}
		
		if(cap != null) {
			if(!UtenteCheckoutValidator.isValidCAP(cap)) {
				System.out.println("Campo cap non valido");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("Campo cap vuoto");
			dispatcher.forward(request, response);
		}
		System.out.println("Dati utente validi");
				
		if (action.equalsIgnoreCase("purchaseOne")) {	
					int quantity = Integer.parseInt(request.getParameter("qnt"));
					String size = request.getParameter("sz");
					int id = Integer.parseInt(request.getParameter("id"));
					System.out.println("Codice id da acquistare: "+id);
					ProdottoOrdinato product = new ProdottoOrdinato();
					try {
						product.setCode(id);
						product = orderDao.doRetrieveByKeyO(product);
					} catch (SQLException | CheckException e1) {
						e1.printStackTrace();
					}
					String error = "La quantita' "+size+" richiesta eccede quella disponibile!";
					if( size.equalsIgnoreCase("M") ) {
						if(quantity > product.getTaglie().getQuantitaM() ) {
							errors.add(error);
							System.out.println("Quantità richiesta: "+quantity+" , quantità disponibile: "+product.getTaglie().getQuantitaM());
						}
					} else if ( size.equalsIgnoreCase("L") ) {
						if(quantity > product.getTaglie().getQuantitaL() )
							errors.add(error);
							} else if( size.equalsIgnoreCase("XL")) {
								if ( quantity > product.getTaglie().getQuantitaXL()) 
									errors.add(error);
							} else if( size.equalsIgnoreCase("XXL")) {
								if ( quantity > product.getTaglie().getQuantitaXXL()) 
									errors.add(error);
							}
					RequestDispatcher dispatcherToProductPage = request.getRequestDispatcher("./singleproduct.jsp?id="+product.getCode());
					if (!errors.isEmpty()) {
		            	request.setAttribute("errors", errors);
		            	request.setAttribute("product", product);
		            	dispatcherToProductPage.forward(request, response);
		            	return;
		            }
					
					product.setQnt(quantity);
					product.setSz(size);
					try {
						productDao.decreaseSize(product.getTaglie(), quantity, size, product.getCode());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Ordine order = new Ordine();
					order.addOrder(product);
					order.setEmailUtente(email);
					order.setStato("IN ELABORAZIONE");
					order.setData(dateTime);
					order.setIndirizzo(indirizzo);

					try {
						orderDao.doSave(order);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					if( salvaDati != null && salvaDati.equals("on")) { //Devo salvare la carta
						ICartaDaoData cardDao = new CartaDaoDataSource(ds);
						Carta carta = new Carta();
						carta.setIdCarta(bean.getId());
						String mm = convertiMeseInNumero(mese_scadenza); 
						carta.setData_scadenza(mm+"/"+anno_scadenza);
						carta.setNumero_carta(numero_carta);
						carta.setProprietario(nome_carta);
						try {
							cardDao.salvaCarta(carta);
						} catch (SQLException | CheckException e) {
							e.printStackTrace();
						}
						
						request.getSession().removeAttribute("logged");
						request.getSession().setAttribute("logged", bean);
						bean.setCarta(carta);
					}
					errors.clear();
				} else if( action.equalsIgnoreCase("purchaseAll")) { 
					Carrello cart = (Carrello) request.getSession().getAttribute("cart");
					
					/* prendo le taglie dei singoli prodotti messi nel carrello */
					@SuppressWarnings("unchecked")
					List<String>sizes = (List<String>) request.getSession().getAttribute("sizes");
					
					/* prendo le quantità dei singoli prodotti messi nel carrello */
					@SuppressWarnings("unchecked")
					List<Integer>qnts = (List<Integer>) request.getSession().getAttribute("qnts");
					
					/* devo controllare se ogni prodotto è disponibile per l'acquisto
					 * sulla quantità e taglia richiesta */
					ProdottoOrdinato prodondb = null;
					Prodotto p = null;
					String error = "La quantita' richiesta di un prodotto presente nel carrello eccedere quella disponibile!";
					for(int i=0; i<cart.getSize(); i++) {
						Prodotto id = new Prodotto();
						id.setCode(i);
						p = cart.getProduct(id);
						System.out.println("Prodotto nel carrello: "+p);
						try {
							prodondb = orderDao.doRetrieveByKeyO(p);
							System.out.println(prodondb);
						} catch (SQLException | CheckException e) {
							e.printStackTrace();
						}

						if( sizes.get(i).equalsIgnoreCase("M") ) {
							if(qnts.get(i) > prodondb.getTaglie().getQuantitaM() )
								errors.add(error);
						} else if ( sizes.get(i).equalsIgnoreCase("L") ) {
							if( qnts.get(i) > prodondb.getTaglie().getQuantitaL() )
								errors.add(error);
								} else if( sizes.get(i).equalsIgnoreCase("XL") ) {
									if ( qnts.get(i) > prodondb.getTaglie().getQuantitaXL() ) 
										errors.add(error);
								} else if( sizes.get(i).equalsIgnoreCase("XXL") ) {
									if ( qnts.get(i) > prodondb.getTaglie().getQuantitaXXL() ) 
										errors.add(error);
								}
						if(!errors.isEmpty())
							break;
					}
					
					/*Se qualche prodotto non è disponibiile all'acquisto, rimando alla pagina di checkout con la visualizzazione
					 * dell'errore relativo all'acquisto */
					RequestDispatcher dispatcherToProductPage = request.getRequestDispatcher("./singleproduct.jsp?id="+prodondb.getCode());
					if (!errors.isEmpty()) {
		            	request.setAttribute("errors", errors);
		            	request.setAttribute("product", prodondb);
		            	dispatcherToProductPage.forward(request, response);
		            	return;
		            }
					
					/*Superato il controllo della disponibilità dei prodotti, possono creare l'ordine*/
					Ordine order = new Ordine();
					p = null;
					for(int i = 0; i<cart.getSize(); i++) {
						Prodotto id = new Prodotto();
						id.setCode(i);
						p = cart.getProduct(id);
						try {
							prodondb = orderDao.doRetrieveByKeyO(p);
							/* salvo il prodotto insieme alla quantità con cui
							 * lo si vuol comprare, quantità che verrà salvata nel db */
							prodondb.setQnt(qnts.get(i));
							prodondb.setSz(sizes.get(i));
						
							/* decremento le quantità di determinate taglie dei prodotti acquistati */
							productDao.decreaseSize(prodondb.getTaglie(), qnts.get(i), sizes.get(i), prodondb.getCode());
							/* aggiungo tutti i prodotti nella lista dell'ordine */
							System.out.println("***prodotto"+i+" :"+prodondb);
							order.addOrder(prodondb);
						} catch (SQLException | CheckException e) {
							e.printStackTrace();
						}					
					}
					
					order.setEmailUtente(email);
					order.setStato("IN ELABORAZIONE");
					order.setData(dateTime);
					order.setIndirizzo(indirizzo);
					order.setData_consegna(dateConsegna);
					
					Double ptot = Double.parseDouble(request.getParameter("tot"));
					try {
						order.setPrezzototale(ptot);
						orderDao.doSave(order);
					} catch (SQLException e) {
						e.printStackTrace();
					}
		
					/* svuoto il carrello */
					Carrello newcart = new Carrello();
					request.getSession().removeAttribute("cart");
					request.getSession().setAttribute("cart", newcart);
					
					if( salvaDati != null && salvaDati.equals("on")) { //Devo salvare la carta
						ICartaDaoData cardDao = new CartaDaoDataSource(ds);
						Carta carta = new Carta();
						carta.setIdCarta(bean.getId());
						String mm = convertiMeseInNumero(mese_scadenza); 
						carta.setData_scadenza(mm+"/"+anno_scadenza);
						carta.setNumero_carta(numero_carta);
						carta.setProprietario(nome_carta);
						try {
							cardDao.salvaCarta(carta);
						} catch (SQLException | CheckException e) {
							e.printStackTrace();
						}
						
						request.getSession().removeAttribute("logged");
						request.getSession().setAttribute("logged", bean);
						bean.setCarta(carta);
					}
				}
							
		String fromStore = request.getParameter("fromStore");
		
		dispatcher = null;
		
		if(  fromStore.equalsIgnoreCase("get2")) {
			dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
		} else if ( Boolean.parseBoolean(fromStore) )    
					dispatcher = getServletContext().getRequestDispatcher("/store.jsp");
         		else
         			dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private static String convertiMeseInNumero(String nomeMese) {
        String[] nomiMesi = {
                "Gennaio", "Febbraio", "Marzo", "Aprile",
                "Maggio", "Giugno", "Luglio", "Agosto",
                "Settembre", "Ottobre", "Novembre", "Dicembre"
        };

        for (int i = 0; i < nomiMesi.length; i++) {
            if (nomiMesi[i].equalsIgnoreCase(nomeMese)) {
                return (i + 1 < 10) ? "0" + (i + 1) : String.valueOf(i + 1);
            }
        }

        return "Mese non valido";
    }
}
