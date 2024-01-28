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
		return "Prodotto [code=" + code + ", name=" + name + ", descrizione=" + descrizione + ", categoria=" + categoria
				+ ", price=" + price + ", stats=" + stats + ", taglie=" + taglie + ", imagePath=" + imagePath + "]";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prodotto prodotto = (Prodotto) o;
        return Objects.equals(code, prodotto.code) &&
                Objects.equals(name, prodotto.name) &&
                Objects.equals(descrizione, prodotto.descrizione) &&
                Objects.equals(categoria, prodotto.categoria) &&
                Objects.equals(price, prodotto.price) &&
                Objects.equals(stats, prodotto.stats) &&
                Objects.equals(taglie, prodotto.taglie) &&
                Objects.equals(imagePath, prodotto.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, descrizione, categoria, price, stats, taglie, imagePath);
    }

}

