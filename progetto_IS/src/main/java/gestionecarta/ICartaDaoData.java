package gestionecarta;

import java.sql.SQLException;

import checking.CheckException;
import gestioneutenti.Utente;

public interface ICartaDaoData {
	public boolean salvaCarta(Carta carta) throws SQLException, CheckException;
	public Carta recuperaCarta(Utente utente) throws SQLException;
	public boolean cancellaCarta(Carta carta) throws SQLException, CheckException;
}
