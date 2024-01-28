package gestioneprodotti;
import java.io.Serializable;
import java.util.Objects;

public class Taglie implements Serializable {
	private static final long serialVersionUID = -146315416856526905L;

	public Taglie() {
		super();
		this.idProdotto = 0;
		this.quantitaM = 0;
		this.quantitaL = 0;
		this.quantitaXL = 0;
		this.quantitaXXL = 0;
	}
	
	public Taglie(int idProdotto, int quantitaM, int quantitaL, int quantitaXL, int quantitaXXL) {
		super();
		this.idProdotto = idProdotto;
		this.quantitaM = quantitaM;
		this.quantitaL = quantitaL;
		this.quantitaXL = quantitaXL;
		this.quantitaXXL = quantitaXXL;
	}



	int idProdotto;
	int quantitaM; 
	int quantitaL;
	int quantitaXL; 
	int quantitaXXL;
	
	public int getIdProdotto() {
		return idProdotto;
	}
	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}
	public int getQuantitaM() {
		return quantitaM;
	}
	public void setQuantitaM(int quantitaM) {
		this.quantitaM = quantitaM;
	}
	public int getQuantitaL() {
		return quantitaL;
	}
	public void setQuantitaL(int quantitaL) {
		this.quantitaL = quantitaL;
	}
	public int getQuantitaXL() {
		return quantitaXL;
	}
	public void setQuantitaXL(int quantitaXL) {
		this.quantitaXL = quantitaXL;
	}
	public int getQuantitaXXL() {
		return quantitaXXL;
	}
	public void setQuantitaXXL(int quantitaXXL) {
		this.quantitaXXL = quantitaXXL;
	}
	@Override
	public String toString() {
		return "taglia M=" + quantitaM + "  taglia L=" + quantitaL
				+ " taglia XL=" + quantitaXL + " taglia XXL=" + quantitaXXL;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idProdotto, quantitaL, quantitaM, quantitaXL, quantitaXXL);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Taglie other = (Taglie) obj;
		return idProdotto == other.idProdotto && quantitaL == other.quantitaL && quantitaM == other.quantitaM
				&& quantitaXL == other.quantitaXL && quantitaXXL == other.quantitaXXL;
	}
	
}
