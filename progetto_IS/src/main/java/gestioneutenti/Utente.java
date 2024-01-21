package gestioneutenti;

import java.io.Serializable;

import gestionecarta.Carta;

public class Utente implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer id;
	String email;
	String password;
	String nome;
	String cognome;
	String telefono;
	String ruolo;
	Carta carta;

	public Utente() {
		id=0;
		email="";
		password="";
		nome="";
		cognome="";
		telefono="";
		ruolo="";
		carta = null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	public String getRuolo() {
		return this.ruolo;
	}

	
	public Carta getCarta() {
		return carta;
	}

	public void setCarta(Carta carta) {
		this.carta = carta;
	}

	@Override
	public String toString() {
		return "UserBean [id=" + id + ", email=" + email + " nome=" + nome + ", cognome="
				+ cognome + ", telefono=" + telefono + ", ruolo=" + ruolo + "]";
	}

}
