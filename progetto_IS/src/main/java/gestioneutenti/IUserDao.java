package gestioneutenti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import checking.CheckException;

public interface IUserDao {

	public Integer doSaveUser(Utente user) throws SQLException, CheckException;
	public Utente doRetrieveByKey(Utente user) throws SQLException, CheckException;
	public Utente login(Utente user) throws SQLException, CheckException;
	public  Boolean validateOldPassword(Utente oldPassHash, Utente passToBeMatch) throws SQLException;
	boolean changePass(String pass, Utente user) throws SQLException, CheckException;
	public int getLastCode() throws SQLException;
	public Utente doRetrieveByEmail(Utente user) throws SQLException, CheckException;
	
	//Funzionalità Gestore Utenti
	void doSaveGestore(Utente admin) throws SQLException, CheckException;	
	int doDeleteGestore(Utente user) throws SQLException, CheckException;
	boolean changePassGestore(Utente new_pass) throws SQLException, CheckException;
	public List<String> getAllEmails() throws SQLException;
	public ArrayList<Utente> doRetrieveUtentiSorted(String sort) throws SQLException, CheckException;
	public Utente loginGestore(Utente match) throws SQLException;
}
