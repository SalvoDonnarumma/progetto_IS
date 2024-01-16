package gestioneadmin;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import gestioneutenti.Utente;

public class AdminDaoDataSource implements IAdminDao {

	private DataSource ds = null;

	public AdminDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public void doSaveAdmin(Utente admin) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO utente (email, password, nome, cognome, telefono, ruolo) VALUES (?, ?, ?, ?, ?, ?)";
		
		admin.setPassword(toHash(admin.getPassword()));
		try {
			connection = ds.getConnection();
			
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, admin.getEmail());
			preparedStatement.setString(2, admin.getPassword());
			preparedStatement.setString(3, admin.getNome());
			preparedStatement.setString(4, admin.getCognome());
			preparedStatement.setString(5, admin.getTelefono());
			preparedStatement.setString(6, admin.getRuolo());
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
        }
        return hashString;
    }
	
	@Override
	public void doDeleteAdmin(Utente admin) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "DELETE FROM Utente WHERE email = ?";
		
		admin.setPassword(toHash(admin.getPassword()));
		try {
			connection = ds.getConnection();
			
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, admin.getEmail());
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
		
	}

	@Override
	public boolean changePassAdmin(Utente new_user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Integer idUtente = new_user.getId();
		String deleteSQL = "UPDATE utente SET password = ? WHERE idUtente = ?";
		int result = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, this.toHash(new_user.getPassword()));
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
	public boolean validateOldPassword(Utente new_pass, Utente old) throws SQLException {
		String hashedpassToBeMatch = this.toHash(new_pass.getPassword());
		if( hashedpassToBeMatch == null || hashedpassToBeMatch.equals("null"))
			return false;
		return 
			hashedpassToBeMatch.equals(old.getPassword());
	}

	
	public List<String> getAllEmails() throws SQLException{
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


	@Override
	public Collection<Utente> doRetrieveUtentiSorted(String sort) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Utente> users = new LinkedList<>();

		String selectSQL = null;
		if(sort == null)
		 selectSQL = "SELECT * FROM utente" ;
		else if(sort.equals("email"))
			selectSQL = "SELECT * FROM utente ORDER BY email" ;
		else if(sort.equals("cognome"))
			selectSQL = "SELECT * FROM utente ORDER BY cognome" ;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

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

	@Override
	public Utente loginAdmin(Utente user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String selectSQLUser = "SELECT * FROM utente WHERE ruolo LIKE '%Gestore%'";
		
		String password = user.getPassword();
		String email = user.getEmail();
		
		String hashPassword = toHash(password);
		String emailToBeMatch = null;
		String hashPasswordToBeMatch = null;

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQLUser);
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

}
