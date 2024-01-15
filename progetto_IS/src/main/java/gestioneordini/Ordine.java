package gestioneordini;
import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.List;

public class Ordine implements Serializable{
	
	public Ordine() {
		orders = new ArrayList<>();
	}

	List<ProdottoOrdinato> orders;
	String idUtente;
	String data;
	String stato;
	Integer idOrdine;
	Double prezzototale;
	String indirizzo;
	String data_consegna;

	public String getData_consegna() {
		return data_consegna;
	}


	public void setData_consegna(String data_consegna) {
		this.data_consegna = data_consegna;
	}


	public void addOrder(ProdottoOrdinato product) {
		orders.add(product);
	}
	
	
	public List<ProdottoOrdinato> getOrders() {
		return orders;
	}

	public void setOrders(List<ProdottoOrdinato> orders) {
		this.orders = orders;
	}

	public String getEmailUtente() {
		return idUtente;
	}

	public void setEmailUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Integer getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(Integer idOrdine) {
		this.idOrdine = idOrdine;
	}

	public Double getPrezzototale() {
		return prezzototale;
	}


	public void setPrezzototale(Double prezzototale) {
		this.prezzototale = prezzototale;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}	
}
