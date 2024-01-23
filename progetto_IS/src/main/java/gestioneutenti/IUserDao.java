package gestioneutenti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import checking.CheckException;

public interface IUserDao {

	public String doSaveUser(Utente user) throws SQLException, CheckException;
	
	public ArrayList<Utente> doRetrieveAllUsers(String order) throws SQLException;
	
	public Utente doRetrieveByKey(Utente user) throws SQLException, CheckException;
	
	public Utente login(Utente user) throws SQLException;
	
	public  Boolean validateOldPassword(String oldPassHash, String passToBeMatch);

	boolean changePass(String pass, Utente user) throws SQLException, CheckException;
		
	public int getLastCode() throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public Utente doRetrieveByEmail(Utente user) throws SQLException, CheckException;
}
