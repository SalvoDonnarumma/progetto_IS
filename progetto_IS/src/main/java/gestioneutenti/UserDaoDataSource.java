package gestioneutenti;

import java.nio.charset.StandardCharsets;   
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import checking.CheckException;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestionecarta.ICartaDaoData;

public class UserDaoDataSource implements IUserDao {

	private DataSource ds = null;
	private Connection connection = null;
	
	public UserDaoDataSource(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		}catch( SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getLastCode() throws SQLException {
		PreparedStatement preparedStatement = null;
		
		String getLast = "SELECT * from utente order by idUtente DESC LIMIT 1";
		int idUtente = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(getLast);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) { 
				idUtente = rs.getInt("idutente");
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}	
		return idUtente;
	}
	
	@Override
	public synchronized Integer doSaveUser(Utente user) throws SQLException, CheckException {
		PreparedStatement preparedStatement = null;
		
		if(user.getId() == null)
			throw new CheckException("Id non valido!");
		if(user.getEmail() == null || user.getEmail() == "" || !UtenteRegistrazioneValidator.isValidEmail(user.getEmail()))
			throw new CheckException("Id non valido!");
		if(user.getPassword() == null || user.getPassword() == "" || user.getPassword().length()<12)
			throw new CheckException("Id non valido!");
		if(user.getNome()== null || user.getNome() == "" || !UtenteRegistrazioneValidator.isValidNome(user.getNome()))
			throw new CheckException("Id non valido!");
		if(user.getCognome() == null || user.getCognome() == "" || !UtenteRegistrazioneValidator.isValidNome(user.getCognome()))
			throw new CheckException("Id non valido!");
		if(user.getTelefono() == null || user.getTelefono() == "" || !UtenteRegistrazioneValidator.isValidTelefono(user.getTelefono()))
			throw new CheckException("Id non valido!");
		if(user.getRuolo() == null || user.getRuolo() == "")
			throw new CheckException("Id non valido!");
		if( !user.getRuolo().equals("Utente") && !user.getRuolo().equals("Gestore Ordini") && !user.getRuolo().equals("Gestore Prodotti") && !user.getRuolo().equals("Gestori Utenti"))
			throw new CheckException();
			
		String insertSQL = "INSERT INTO utente (email, password, nome, cognome, telefono, ruolo) VALUES (?, ?, ?, ?, ?, ?)";
		
		Integer res = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, toHash(user.getPassword()));
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getTelefono());
			preparedStatement.setString(6, user.getRuolo());
		
			res = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		
		return res;
	}
	
	static public String toHash(String password) {
        String hashString = null;
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashString = "";
            for (int i = 0; i < hash.length; i++) {
                hashString += Integer.toHexString( 
                                  (hash[i] & 0xFF) | 0x100 
                              ).toLowerCase().substring(1,3);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashString;
    }
	
	public synchronized Boolean validateOldPassword(String oldPassHash, String passToBeMatch) {
		String hashedpassToBeMatch = this.toHash(passToBeMatch);
		if( hashedpassToBeMatch == null || hashedpassToBeMatch.equals("null"))
			return false;
		return hashedpassToBeMatch.equals(oldPassHash);
	}
	
	public synchronized Utente login(Utente user) throws SQLException, CheckException {
		PreparedStatement preparedStatement = null;
			
		if(user == null)
			throw new CheckException();
		
		if(user.getEmail() == null || user.getPassword() == null)
			throw new CheckException();
		
		if(user.getEmail().equals("") || user.getPassword().equals(""))
			throw new CheckException();
		
		String selectSQLUser = "SELECT * FROM utente WHERE ruolo = ?";
		
		String password = user.getPassword();
		String email = user.getEmail();
		
		String hashPassword = toHash(password);
		String emailToBeMatch = null;
		String hashPasswordToBeMatch = null;
		ICartaDaoData cardDao = new CartaDaoDataSource(ds);
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQLUser);
			preparedStatement.setString(1, "Utente");
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				emailToBeMatch = rs.getString("email"); //prelevo tutte le email dal db
				hashPasswordToBeMatch = rs.getString("password"); //prelevo tutte le password dal db		
				if(emailToBeMatch.equals(email) && hashPasswordToBeMatch.equals(hashPassword)) {
						user.setId(rs.getInt("idutente"));
						user.setEmail(rs.getString("email"));
						user.setPassword(rs.getString("password"));
						user.setCognome(rs.getString("cognome"));
						user.setTelefono(rs.getString("telefono"));
						user.setNome(rs.getString("nome"));
						user.setRuolo(rs.getString("ruolo"));
						Carta carta = cardDao.recuperaCarta(user);
						user.setCarta(carta);
					return user;
				}
			}
		} catch (CheckException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return null;
	}
	
	@Override
	public boolean changePass(String pass, Utente user) throws SQLException, CheckException {
		PreparedStatement preparedStatement = null;
		
		if( user == null )
			throw new CheckException("cambio pass non valido");
		
		if( user.getEmail() == null || user.getEmail().equals(""))
			throw new CheckException("cambio pass non valido");
		
		if( pass == null || pass.equals("") )
			throw new CheckException("cambio pass non valido");
		
		
		Integer idUtente = user.getId();
		String deleteSQL = "UPDATE utente SET password = ? WHERE idUtente = ?";
		if(pass == null || pass == "")
			throw new CheckException("Password non valida!");
		int result = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, toHash(pass));
			preparedStatement.setInt(2, idUtente);

			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return (result != 0);
	}
	

	@Override
	public Utente doRetrieveByKey(Utente user) throws SQLException, CheckException {
		PreparedStatement preparedStatement = null;

		Utente bean = new Utente();

		if(user == null)
			throw new CheckException("Utente non valido!");
		if(user.getId() == null)
			throw new CheckException("Id non valido!");
		String selectSQL = "SELECT * FROM utente WHERE idUtente = ?" ;
		int idUtente = user.getId();	

		try {
			connection = ds.getConnection();
		
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, idUtente);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(!rs.next())
				return null;
			bean.setId(rs.getInt("idutente"));
			bean.setEmail(rs.getString("email"));
			bean.setNome(rs.getString("nome"));
			bean.setCognome(rs.getString("cognome"));
			bean.setPassword(rs.getString("password"));
			bean.setTelefono(rs.getString("telefono"));
			bean.setRuolo(rs.getString("ruolo"));

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return bean;
	}

	@Override
	public Utente doRetrieveByEmail(Utente user) throws SQLException, CheckException {
		PreparedStatement preparedStatement = null;

		if( user == null )
			throw new CheckException("Utente non valido");
			
		Utente bean = new Utente();
		if(user.getEmail() == null || user.getEmail().equals(""))
			throw new CheckException("Email non valida!");
		String selectSQL = "SELECT * FROM utente WHERE email= ?" ;
		String idUtente = user.getEmail();	

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, idUtente);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(!rs.next())
				return null;
			bean.setId(rs.getInt("idutente"));
			bean.setEmail(rs.getString("email"));
			bean.setNome(rs.getString("nome"));
			bean.setCognome(rs.getString("cognome"));
			bean.setPassword(rs.getString("password"));
			bean.setTelefono(rs.getString("telefono"));
			bean.setRuolo(rs.getString("ruolo"));

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return bean;
	}
	
	@Override
	public List<String> getAllEmails() throws SQLException {
		PreparedStatement preparedStatement = null;	
		String selectSQL = "SELECT email FROM utente";
		List<String> emails = new ArrayList<>();
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				emails.add(rs.getString("email")); 
			}	
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return emails;
	}
}
