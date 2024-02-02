package gestioneordini;
import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.List;
import java.util.Objects;

public class Ordine implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2756102178026823980L;

	public Ordine() {
		orders = new ArrayList<>();
	}

	public Ordine(List<ProdottoOrdinato> orders, String idUtente, String data, String stato, Integer idOrdine,
			Double prezzototale, String indirizzo, String data_consegna) {
		super();
		this.orders = orders;
		this.idUtente = idUtente;
		this.data = data;
		this.stato = stato;
		this.idOrdine = idOrdine;
		this.prezzototale = prezzototale;
		this.indirizzo = indirizzo;
		this.data_consegna = data_consegna;
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

	@Override
	public int hashCode() {
		return Objects.hash(data, data_consegna, idOrdine, idUtente, indirizzo, orders, prezzototale, stato);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ordine other = (Ordine) obj;
		return Objects.equals(data, other.data) && Objects.equals(data_consegna, other.data_consegna)
				&& Objects.equals(idOrdine, other.idOrdine) && Objects.equals(idUtente, other.idUtente)
				&& Objects.equals(indirizzo, other.indirizzo) && Objects.equals(orders, other.orders)
				&& Objects.equals(prezzototale, other.prezzototale) && Objects.equals(stato, other.stato);
	}

	@Override
	public String toString() {
		return "Ordine [orders=" + orders + ", idUtente=" + idUtente + ", data=" + data + ", stato=" + stato
				+ ", idOrdine=" + idOrdine + ", prezzototale=" + prezzototale + ", indirizzo=" + indirizzo
				+ ", data_consegna=" + data_consegna + "]";
	}	
	
	
}
