package gestioneutenti;

import java.nio.charset.StandardCharsets;   
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import gestioneutenti.Utente;
import javax.sql.DataSource;

public class UserDaoDataSource implements IUserDao {

	private DataSource ds = null;

	public UserDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	public int getLastCode() throws SQLException {
		Connection connection = null;
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
	public synchronized String doSaveUser(Utente user) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO utente (email, password, nome, cognome, telefono, ruolo) VALUES (?, ?, ?, ?, ?, ?)";
		
		user.setPassword(toHash(user.getPassword()));
		try {
			connection = ds.getConnection();
			
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getTelefono());
			preparedStatement.setString(6, user.getRuolo());
		
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		
		return user.getEmail();
	}
	
	@Override
	public synchronized Collection<Utente> doRetrieveAllUsers(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Utente> users = new LinkedList<>();

		String selectSQL = "SELECT * FROM utente" ;

		if (order != null && !order.equals("")) {
			selectSQL += "ORDER BY ?";
		}

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			if (order != null && !order.equals("")) {
				preparedStatement.setString(1, order);
			}
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Utente bean = new Utente();

				bean.setId(rs.getInt("idutente"));
				bean.setEmail(rs.getString("email"));
				bean.setNome(rs.getString("nome"));
				bean.setCognome(rs.getString("cognome"));
				bean.setPassword(rs.getString("password"));
				bean.setTelefono(rs.getString("telefono"));
				bean.setRuolo(rs.getString("ruolo"));
				users.add(bean);
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
		return users;
	}
	
	private String toHash(String password) {
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
	
	@SuppressWarnings("null")
	public synchronized Boolean comparePass(String oldPassHash, String passToBeMatch) {
		String hashedpassToBeMatch = this.toHash(passToBeMatch);
		if( hashedpassToBeMatch == null || hashedpassToBeMatch.equals("null"))
			return false;
		return hashedpassToBeMatch.equals(oldPassHash);
	}
	
	public synchronized Utente login(Utente user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String selectSQLUser = "SELECT * FROM utente WHERE ruolo = ?";
		
		String password = user.getPassword();
		String email = user.getEmail();
		
		String hashPassword = toHash(password);
		String emailToBeMatch = null;
		String hashPasswordToBeMatch = null;

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
					return user;
				}
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
		return null;
	}
	
	@Override
	public boolean changePass(String pass, Utente user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Integer idUtente = user.getId();
		String deleteSQL = "UPDATE utente SET password = ? WHERE idUtente = ?";
		int result = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, this.toHash(pass));
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
	public Utente doRetrieveByKey(Utente user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Utente bean = new Utente();

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
	public Utente doRetrieveByEmail(Utente user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Utente bean = new Utente();

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
		Connection connection = null;
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
