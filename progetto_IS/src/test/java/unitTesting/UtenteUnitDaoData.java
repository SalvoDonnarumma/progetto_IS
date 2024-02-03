package unitTesting;

import java.sql.Connection;   
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class UtenteUnitDaoData {
	private DataSource ds;
	private UserDaoDataSource userDaoData;
	private Connection connection;
	
	@BeforeEach
    public void setUp() throws Exception {
		ds = Mockito.mock(DataSource.class);
	    connection = mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
	    PreparedStatement preparedStatement = mock(PreparedStatement.class);
	    Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	    userDaoData = new UserDaoDataSource(ds);
    }
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestCorretto")
	public void doRetrieveByEmailTestCorretto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    // Configura il mock per ritornare la connessione mockata quando getConnection() è chiamato sul DataSource
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);
        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        // Configura il mock per ritornare un risultato simulato quando executeQuery() è chiamato
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		
        // Configura il mock per ritornare valori simulati quando chiamati i metodi del ResultSet
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword");
        Mockito.when(resultSet.getString(Mockito.eq("nome"))).thenReturn("Salvatore");
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Utente");
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        Utente result = userDaoData.doRetrieveByEmail(email);
        
        //Verifico che il risultato sia quello atteso
        assertEquals(1, result.getId());
        assertEquals("salvatoredonnarumma@gmail.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals("Salvatore", result.getNome());
        assertEquals("Donnarumma", result.getCognome());
        assertEquals("320-1234567", result.getTelefono());
        assertEquals("Utente", result.getRuolo());
        
     // Verifica che il metodo setString sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        // Verifica che il metodo executeQuery sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(1)).next();
        // Verifica che i metodi getString siano stati chiamati correttamente sul resultSet
        Mockito.verify(resultSet, times(1)).getInt("idutente");
        Mockito.verify(resultSet, times(1)).getString("email");
        Mockito.verify(resultSet, times(1)).getString("password");
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
        Mockito.verify(resultSet, times(1)).getString("telefono");
        Mockito.verify(resultSet, times(1)).getString("ruolo");
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByKeyTestCorretto")
	public void doRetrieveByKeyTestCorretto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword");
        Mockito.when(resultSet.getString(Mockito.eq("nome"))).thenReturn("Salvatore");
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Utente");
        
        Utente user = Mockito.mock(Utente.class);
        Mockito.when(user.getId()).thenReturn(1);   
        Utente result = userDaoData.doRetrieveByKey(user);
        
        //Verifico che il risultato sia quello atteso
        assertEquals(1, result.getId());
        assertEquals("salvatoredonnarumma@gmail.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals("Salvatore", result.getNome());
        assertEquals("Donnarumma", result.getCognome());
        assertEquals("320-1234567", result.getTelefono());
        assertEquals("Utente", result.getRuolo());

        Mockito.verify(preparedStatement, times(1)).setInt(eq(1), eq(1));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        
        Mockito.verify(resultSet, times(1)).getInt("idutente");
        Mockito.verify(resultSet, times(1)).getString("email");
        Mockito.verify(resultSet, times(1)).getString("password");
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
        Mockito.verify(resultSet, times(1)).getString("telefono");
        Mockito.verify(resultSet, times(1)).getString("ruolo");
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestNonPresente")
	public void doRetrieveByEmailTestNonPresente() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    // Configura il mock per ritornare la connessione mockata quando getConnection() è chiamato sul DataSource
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);
        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        // Configura il mock per ritornare un risultato simulato quando executeQuery() è chiamato
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		
     // Configura il mock per ritornare valori simulati quando chiamati i metodi del ResultSet
        Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        Utente result = userDaoData.doRetrieveByEmail(email);
        assertNull(result);
        
        Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByKeyTestNonPresente")
	public void doRetrieveByKeyNonPresente() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);
        
        Utente user = Mockito.mock(Utente.class);
        Mockito.when(user.getId()).thenReturn(1);   
        Utente result = userDaoData.doRetrieveByKey(user);
        assertNull(result);
        
        Mockito.verify(preparedStatement, times(1)).setInt(eq(1), eq(1));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        
        resultSet.close();
	}
	
	@Test
    @DisplayName("TCU1_1_3 doSaveUserTestSalva")
    public void doSaveUserTestSalva() throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Carta carta = Mockito.mock(Carta.class);
        Utente utente = new Utente(1, "salvatoredonnarumma@gmail.com", "passwordLunga123", "Salvatore","Donnarumma", "320-1234567", "Utente", carta);
        userDaoData.doSaveUser(utente);       
        
        Mockito.verify(preparedStatement, times(1)).setString(1, utente.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, UserDaoDataSource.toHash("passwordLunga123"));
        Mockito.verify(preparedStatement, times(1)).setString(3, utente.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, utente.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, utente.getTelefono());
        Mockito.verify(preparedStatement, times(1)).setString(6, utente.getRuolo());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
	
	
	@Test
    @DisplayName("TCU1_1_4 doSaveUserTestChangePassword")
    public void changePasswordTestCorretto() throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Carta carta = Mockito.mock(Carta.class);
        Utente utente = new Utente();
        utente.setId(1);
        utente.setEmail("salvatoredonnarumma@gmail.com");
        String pass = "jojo123456789";
        userDaoData.changePass(pass, utente);       
        
        Mockito.verify(preparedStatement, times(1)).setString(1, UserDaoDataSource.toHash("jojo123456789"));
        Mockito.verify(preparedStatement, times(1)).setInt(2, utente.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
	
	
	//Test funzionalità Gestore Utenti
	@Test
	@DisplayName("TCU1_2_1 registrazioneNuovoGestore")
	public void registrazioneNuovoGestore() throws SQLException, CheckException{ 
		ds = Mockito.mock(DataSource.class);
	    connection = mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
	    PreparedStatement preparedStatement = mock(PreparedStatement.class);
	    Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	    userDaoData = new UserDaoDataSource(ds);
	    
        Utente utente = new Utente(1, "salvatoredonnarumma@gmail.com", "passwordLunga123", null,"Donnarumma", "320-1234567", "Gestore Utenti",null);        
        userDaoData.doSaveGestore(utente);
        Mockito.verify(preparedStatement, times(1)).setString(1, utente.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, UserDaoDataSource.toHash("passwordLunga123"));
        Mockito.verify(preparedStatement, times(1)).setString(3, utente.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, utente.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, utente.getTelefono());
        Mockito.verify(preparedStatement, times(1)).setString(6, utente.getRuolo());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
    @DisplayName("TCU1_1_4 changePasswordTestCorretto")
    public void changePasswordGestoreTestCorretto() throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Utente utente = new Utente();
        utente.setId(1);
        String pass = "jojo123456789";
        utente.setPassword(pass);    
        userDaoData.changePassGestore(utente);       
        
        Mockito.verify(preparedStatement, times(1)).setString(1, UserDaoDataSource.toHash("jojo123456789"));
        Mockito.verify(preparedStatement, times(1)).setInt(2, utente.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
	
	@Test
    @DisplayName("TCU1_1_4 doDeleteTestCorretto")
    public void doDeleteTestCorretto() throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        userDaoData = new UserDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Utente utente = new Utente();
        utente.setEmail("esempioemail@gmail.com");;    
        userDaoData.doDeleteGestore(utente);       
        
        Mockito.verify(ds, times(1)).getConnection();
		Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).setString(1, "esempioemail@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
	
	@Test
	@DisplayName("TCU1_1_2 doRetrieveAllUserTestNull")
	public void doRetrieveUtentiSortedCorretto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com", "orlandotomeo@gmal.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword", "hashedPassword");       
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma", "Tomeo");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567", "351-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Gestore Utenti", "Gestore Utenti");
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        java.util.Collection<Utente> result = userDaoData.doRetrieveUtentiSorted(null);
        
        //Verifico che il risultato sia quello atteso
        assertEquals(2, result.size());
  
        // Verifica che il metodo setString sia stato chiamato correttamente sul preparedStatement
        // Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        // Verifica che il metodo executeQuery sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("idutente");
        Mockito.verify(resultSet, times(2)).getString("email");
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("cognome");
        Mockito.verify(resultSet, times(2)).getString("telefono");
        Mockito.verify(resultSet, times(2)).getString("ruolo");
        resultSet.close();
	}
	@Test
	@DisplayName("TCU1_1_2 doRetrieveAllUserTestNull")
	public void doRetrieveUtentiSortedEmailCorretto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com", "orlandotomeo@gmal.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword", "hashedPassword");
        
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma", "Tomeo");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567", "351-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Gestore Utenti", "Gestore Utenti");
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        java.util.Collection<Utente> result = userDaoData.doRetrieveUtentiSorted("email");
        
        //Verifico che il risultato sia quello atteso
        assertEquals(2, result.size());
  
     // Verifica che il metodo setString sia stato chiamato correttamente sul preparedStatement
     // Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        // Verifica che il metodo executeQuery sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("idutente");
        Mockito.verify(resultSet, times(2)).getString("email");
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("cognome");
        Mockito.verify(resultSet, times(2)).getString("telefono");
        Mockito.verify(resultSet, times(2)).getString("ruolo");
        resultSet.close();
	}
	@Test
	@DisplayName("TCU1_1_2 doRetrieveAllUserTestNull")
	public void doRetrieveUtentiSortedCognomeCorretto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com", "orlandotomeo@gmal.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword", "hashedPassword");
        
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma", "Tomeo");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567", "351-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Gestore Utenti", "Gestore Utenti");
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        java.util.Collection<Utente> result = userDaoData.doRetrieveUtentiSorted("cognome");
        
        //Verifico che il risultato sia quello atteso
        assertEquals(2, result.size());
  
     // Verifica che il metodo setString sia stato chiamato correttamente sul preparedStatement
     // Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        // Verifica che il metodo executeQuery sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("idutente");
        Mockito.verify(resultSet, times(2)).getString("email");
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("cognome");
        Mockito.verify(resultSet, times(2)).getString("telefono");
        Mockito.verify(resultSet, times(2)).getString("ruolo");
        resultSet.close();
	}
	@Test
	@DisplayName("TCU1_1_2 doRetrieveAllUserTestVuoto")
	public void doRetrieveUtentiSortedVuoto() throws SQLException, CheckException {
	    DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        userDaoData = new UserDaoDataSource(ds);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
        Mockito.when(resultSet.getInt(Mockito.eq("idutente"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("email"))).thenReturn("salvatoredonnarumma@gmail.com", "orlandotomeo@gmal.com");
        Mockito.when(resultSet.getString(Mockito.eq("password"))).thenReturn("hashedPassword", "hashedPassword");
        Mockito.when(resultSet.getString(Mockito.eq("nome"))).thenReturn("Salvatore", "Orlando");
        Mockito.when(resultSet.getString(Mockito.eq("cognome"))).thenReturn("Donnarumma", "Tomeo");
        Mockito.when(resultSet.getString(Mockito.eq("telefono"))).thenReturn("320-1234567", "351-1234567");
        Mockito.when(resultSet.getString(Mockito.eq("ruolo"))).thenReturn("Utente", "Utente");
        
        Utente email = Mockito.mock(Utente.class);
        Mockito.when(email.getEmail()).thenReturn("salvatoredonnarumma@gmail.com");   
        java.util.Collection<Utente> result = userDaoData.doRetrieveUtentiSorted("");
        
        //Verifico che il risultato sia quello atteso
        assertEquals(2, result.size());
  
     // Verifica che il metodo setString sia stato chiamato correttamente sul preparedStatement
     // Mockito.verify(preparedStatement, times(1)).setString(eq(1), eq("salvatoredonnarumma@gmail.com"));
        // Verifica che il metodo executeQuery sia stato chiamato correttamente sul preparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("idutente");
        Mockito.verify(resultSet, times(2)).getString("email");
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("cognome");
        Mockito.verify(resultSet, times(2)).getString("telefono");
        Mockito.verify(resultSet, times(2)).getString("ruolo");
        resultSet.close();
	}
}
