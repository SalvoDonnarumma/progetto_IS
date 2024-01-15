package gestioneprodotti;
import java.io.Serializable;

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
	
}
