package storagelayer;

import java.sql.SQLException;

import checking.CheckException;
import gestionecarta.Carta;
import gestioneutenti.Utente;

public interface ICartaDaoData {
	public boolean salvaCarta(Carta carta) throws SQLException, CheckException;
	public Carta recuperaCarta(Utente utente) throws SQLException, CheckException;
	public boolean cancellaCarta(Carta carta) throws SQLException, CheckException;
}
