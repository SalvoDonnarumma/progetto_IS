package gestioneprodotti;

import java.io.Serializable;
import java.util.Objects;
public class Prodotto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer code;
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
		price = null;
		stats = null;
		imagePath = null;
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
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
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
		return "Prodotto [code=" + code + ", name=" + name  + ", categoria=" + categoria
				+ ", price=" + price + ", taglie=" + taglie + ", imagePath=" + imagePath + "]";
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
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Prodotto prodotto = (Prodotto) obj;
	    return getCode() == prodotto.getCode() &&
	           Double.compare(prodotto.getPrice(), getPrice()) == 0 &&
	           Objects.equals(getNome(), prodotto.getNome()) &&
	           Objects.equals(getCategoria(), prodotto.getCategoria()) &&
	           Objects.equals(getDescrizione(), prodotto.getDescrizione()) &&
	           Objects.equals(getStats(), prodotto.getStats()) &&
	           Objects.equals(getTaglie(), prodotto.getTaglie()) &&
	           Objects.equals(getImagePath(), prodotto.getImagePath());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(
	            getCode(),
	            getNome(),
	            getCategoria(),
	            getDescrizione(),
	            getPrice(),
	            getStats(),
	            getTaglie(),
	            getImagePath()
	    );
	}


}

