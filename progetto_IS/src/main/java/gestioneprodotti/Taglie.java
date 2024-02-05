package gestioneprodotti;
import java.io.Serializable;
import java.util.Objects;

public class Taglie implements Serializable {
	private static final long serialVersionUID = -146315416856526905L;

	public Taglie() {
		super();
		this.idProdotto = -1;
		this.quantitaM = -1;
		this.quantitaL = -1;
		this.quantitaXL = -1;
		this.quantitaXXL = -1;
	}

	public Taglie(Integer idProdotto, Integer quantitaM, Integer quantitaL, Integer quantitaXL, Integer quantitaXXL) {
		super();
		this.idProdotto = idProdotto;
		this.quantitaM = quantitaM;
		this.quantitaL = quantitaL;
		this.quantitaXL = quantitaXL;
		this.quantitaXXL = quantitaXXL;
	}



	Integer idProdotto;
	Integer quantitaM; 
	Integer quantitaL;
	Integer quantitaXL; 
	Integer quantitaXXL;

	public Integer getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public Integer getQuantitaM() {
		return quantitaM;
	}

	public void setQuantitaM(Integer quantitaM) {
		this.quantitaM = quantitaM;
	}

	public Integer getQuantitaL() {
		return quantitaL;
	}

	public void setQuantitaL(Integer quantitaL) {
		this.quantitaL = quantitaL;
	}

	public Integer getQuantitaXL() {
		return quantitaXL;
	}

	public void setQuantitaXL(Integer quantitaXL) {
		this.quantitaXL = quantitaXL;
	}

	public Integer getQuantitaXXL() {
		return quantitaXXL;
	}

	public void setQuantitaXXL(Integer quantitaXXL) {
		this.quantitaXXL = quantitaXXL;
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
		return Objects.equals(idProdotto, other.idProdotto) && Objects.equals(quantitaL, other.quantitaL)
				&& Objects.equals(quantitaM, other.quantitaM) && Objects.equals(quantitaXL, other.quantitaXL)
				&& Objects.equals(quantitaXXL, other.quantitaXXL);
	}

	@Override
	public String toString() {
		return "Taglie disponibili:" + " quantitaM=" + quantitaM + ", quantitaL=" + quantitaL
				+ ", quantitaXL=" + quantitaXL + ", quantitaXXL=" + quantitaXXL + "]";
	}
 
}
