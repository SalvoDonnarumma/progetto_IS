package gestionegestioneutenti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import checking.CheckException;
import gestioneutenti.Utente;

public interface IGestoreDao {

	void doSaveGestore(Utente admin) throws SQLException, CheckException;
	
	int doDeleteGestore(Utente user) throws SQLException, CheckException;

	boolean changePassGestore(Utente new_pass) throws SQLException, CheckException;
	
	boolean validateOldPassword(Utente old, Utente new_pass) throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public ArrayList<Utente> doRetrieveUtentiSorted(String sort) throws SQLException, CheckException;

	public Utente loginGestore(Utente match) throws SQLException;
}

