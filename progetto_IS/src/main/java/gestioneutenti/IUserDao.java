package gestioneutenti;

import java.sql.SQLException;  
import java.util.Collection;
import java.util.List;

public interface IUserDao {

	public String doSaveUser(Utente user) throws SQLException;
	
	public Collection<Utente> doRetrieveAllUsers(String order) throws SQLException;
	
	public Utente doRetrieveByKey(Utente user) throws SQLException;
	
	public Utente login(Utente user) throws SQLException;
	
	public  Boolean validateOldPassword(String oldPassHash, String passToBeMatch);

	boolean changePass(String pass, Utente user) throws SQLException;
		
	public int getLastCode() throws SQLException;
	
	public List<String> getAllEmails() throws SQLException;

	public Utente doRetrieveByEmail(Utente user) throws SQLException;
}
