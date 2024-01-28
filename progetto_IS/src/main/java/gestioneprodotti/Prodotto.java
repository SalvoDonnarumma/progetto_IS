package gestioneprodotti;

import java.io.Serializable;
import java.util.Objects;
public class Prodotto implements Serializable {

	@Override
	public int hashCode() {
		return Objects.hash(categoria, code, descrizione, imagePath, name, price, stats, taglie);
	}

	private static final long serialVersionUID = 1L;
	
	int code;
	String name;
	String descrizione;
	String categoria;
	Double price;
	String stats;
	Taglie taglie;
	String imagePath;

	public Prodotto() {
		code = -1;
		name = "";
		descrizione = "";
		taglie = new Taglie();
	}
	
	public Prodotto(int code, String name, String descrizione, String categoria, Double price, String stats,
			Taglie taglie, String imagePath) {
		super();
		this.code = code;
		this.name = name;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.price = price;
		this.stats = stats;
		this.taglie = taglie;
		this.imagePath = imagePath;
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
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prodotto other = (Prodotto) obj;
		return Objects.equals(categoria, other.categoria) && code == other.code
				&& Objects.equals(descrizione, other.descrizione) && Objects.equals(imagePath, other.imagePath)
				&& Objects.equals(name, other.name) && Objects.equals(price, other.price)
				&& Objects.equals(stats, other.stats) && Objects.equals(taglie, other.taglie);
	}
}

