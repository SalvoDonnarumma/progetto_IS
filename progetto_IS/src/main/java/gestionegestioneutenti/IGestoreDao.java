package gestionegestioneutenti;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import gestioneutenti.Utente;

public interface IGestoreDao {

	void doSaveGestore(Utente admin) throws SQLException;
	
	void doDeleteGestore(Utente user) throws SQLException;

	boolean changePassGestore(Utente new_pass) throws SQLException;
	
	boolean validateOldPassword(Utente old, Utente new_pass) throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public Collection<Utente> doRetrieveUtentiSorted(String sort) throws SQLException;

	public Utente loginGestore(Utente match) throws SQLException;
}

