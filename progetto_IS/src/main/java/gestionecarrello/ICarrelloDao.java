package gestionecarrello;

import java.sql.SQLException;

import gestioneutenti.Utente;

public interface ICarrelloDao {
	public void salvaCarrello(Carrello carrello, Utente utente) throws SQLException;
	public Carrello recuperaCarrello(Utente utente) throws SQLException;
	public boolean carrelloEsistente(Utente utente) throws SQLException;
	public void eliminaCarrello(Utente utente) throws SQLException;
}
