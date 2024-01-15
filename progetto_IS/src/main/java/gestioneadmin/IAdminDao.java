package gestioneadmin;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import gestioneutenti.Utente;

public interface IAdminDao {

	void doSaveAdmin(Utente admin) throws SQLException;
	
	void doDeleteAdmin(Utente user) throws SQLException;

	boolean changePassAdmin(Utente new_pass) throws SQLException;
	
	boolean validateOldPassword(Utente old, Utente new_pass) throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public Collection<Utente> doRetrieveUtentiSorted(String sort) throws SQLException;

	public Utente loginAdmin(Utente match) throws SQLException;
}

