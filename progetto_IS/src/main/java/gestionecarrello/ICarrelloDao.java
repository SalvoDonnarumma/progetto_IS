package gestionecarrello;

import java.sql.SQLException;

import checking.CheckException;
import gestioneutenti.Utente;

public interface ICarrelloDao {
	public void salvaCarrello(Carrello carrello, Utente utente) throws SQLException;
	public Carrello recuperaCarrello(Utente utente) throws SQLException, CheckException;
	public boolean carrelloEsistente(Utente utente) throws SQLException;
	public void eliminaCarrello(Utente utente) throws SQLException;
}
