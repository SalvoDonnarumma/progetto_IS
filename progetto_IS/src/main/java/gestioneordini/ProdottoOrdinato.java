package gestioneordini;
import gestioneprodotti.Prodotto;

public class ProdottoOrdinato extends Prodotto {
	@Override
	public String toString() {
		return "ProdottoOrdinato [qnt=" + qnt + ", sz=" + sz + "]"+super.toString();
	}

	private static final long serialVersionUID = 981173534141524876L;
	public ProdottoOrdinato() {
		super();
		qnt=0;
	}
	
	int qnt;
	String sz;
	
	public int getQnt() {
		return qnt;
	}
	public void setQnt(int qnt) {
		this.qnt = qnt;
	}
	
	public String getSz() {
		return sz;
	}
	
	public void setSz(String sz) {
		this.sz = sz;
	}
}
