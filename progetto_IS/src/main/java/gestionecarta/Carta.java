package gestionecarta;

public class Carta {
	int idCarta;
	String proprietario;
	String numero_carta;
	String data_scadenza;
	
	public Carta() {
		idCarta = 0;
		proprietario = null;
		numero_carta = null;
		data_scadenza = null;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int idCarta) {
		this.idCarta = idCarta;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	public String getNumero_carta() {
		return numero_carta;
	}

	public void setNumero_carta(String numero_carta) {
		this.numero_carta = numero_carta;
	}

	public String getData_scadenza() {
		return data_scadenza;
	}

	public void setData_scadenza(String data_scadenza) {
		this.data_scadenza = data_scadenza;
	}

	@Override
	public String toString() {
		return "Carta [idCarta=" + idCarta + ", proprietario=" + proprietario + ", numero_carta=" + numero_carta
				+ ", data_scadenza=" + data_scadenza + "]";
	}

}
