package gestioneprodotti;

import java.io.Serializable;
public class Prodotto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	int code;
	String name;
	String descrizione;
	String categoria;
	Double price;
	String stats;
	Taglie taglie;

	public Prodotto() {
		code = -1;
		name = "";
		descrizione = "";
		taglie = new Taglie();
	}
	
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getCategoria() {
		return this.categoria;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getNome() {
		return name;
	}

	public void setNome(String name) {
		this.name = name;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String description) {
		this.descrizione = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Prodotto [code=" + code + ", name=" + name + ", categoria=" + categoria
				+ ", price=" + price + ", taglie=" + taglie + "]";
	}


	public String getStats() {
		return stats;
	}


	public void setStats(String stats2) {
		this.stats = stats2;
	}


	public Taglie getTaglie() {
		return taglie;
	}


	public void setTaglie(Taglie taglie) {
		this.taglie = taglie;
	}
}
