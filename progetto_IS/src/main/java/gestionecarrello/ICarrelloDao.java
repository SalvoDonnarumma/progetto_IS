package gestionecarrello;

import java.sql.SQLException; 
import checking.CheckException;
import gestioneutenti.Utente;

public interface ICarrelloDao {
	public Carrello recuperaCarrello(Utente utente) throws SQLException, CheckException;
	public void eliminaCarrello(Carrello utente) throws SQLException, CheckException;
	public void salvaCarrello(Carrello carrello) throws SQLException, CheckException;
}
