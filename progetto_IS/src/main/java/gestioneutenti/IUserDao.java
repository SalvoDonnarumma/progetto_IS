package gestioneutenti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import checking.CheckException;

public interface IUserDao {

	public Integer doSaveUser(Utente user) throws SQLException, CheckException;
	
	public Utente doRetrieveByKey(Utente user) throws SQLException, CheckException;
	
	public Utente login(Utente user) throws SQLException, CheckException;
	
	public  Boolean validateOldPassword(String oldPassHash, String passToBeMatch);

	boolean changePass(String pass, Utente user) throws SQLException, CheckException;
		
	public int getLastCode() throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public Utente doRetrieveByEmail(Utente user) throws SQLException, CheckException;
}
