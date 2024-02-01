package unitTesting;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarta.Carta;
import gestionegestioneutenti.GestoreDaoDataSource;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class GestoreUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private GestoreDaoDataSource gestoreDaoData;
	private PreparedStatement preparedStatement;
	
	@BeforeEach
	public void setUp() throws Exception {
	    ds = Mockito.mock(DataSource.class);
	    connection = mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
	    preparedStatement = mock(PreparedStatement.class);
	    Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	    gestoreDaoData = new GestoreDaoDataSource(ds);
	}
	
	@Test
	@DisplayName("TCU1_2_1 registrazioneNuovoGestore")
	public void registrazioneNuovoGestore() throws SQLException, CheckException{ 
		ds = Mockito.mock(DataSource.class);
	    connection = mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
	    PreparedStatement preparedStatement = mock(PreparedStatement.class);
	    Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	    gestoreDaoData = new GestoreDaoDataSource(ds);
	    
        Utente utente = new Utente(1, "salvatoredonnarumma@gmail.com", "passwordLunga123", null,"Donnarumma", "320-1234567", "Gestore Utenti",null);        
        gestoreDaoData.doSaveGestore(utente);
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
    public void changePasswordTestCorretto() throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        gestoreDaoData = new GestoreDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Utente utente = new Utente();
        utente.setId(1);
        String pass = "jojo123456789";
        utente.setPassword(pass);    
        gestoreDaoData.changePassGestore(utente);       
        
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
        gestoreDaoData = new GestoreDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Utente utente = new Utente();
        utente.setEmail("esempioemail@gmail.com");;    
        gestoreDaoData.doDeleteGestore(utente);       
        
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
        gestoreDaoData = new GestoreDaoDataSource(ds);

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
        java.util.Collection<Utente> result = gestoreDaoData.doRetrieveUtentiSorted(null);
        
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
        gestoreDaoData = new GestoreDaoDataSource(ds);

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
        java.util.Collection<Utente> result = gestoreDaoData.doRetrieveUtentiSorted("email");
        
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
        gestoreDaoData = new GestoreDaoDataSource(ds);

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
        java.util.Collection<Utente> result = gestoreDaoData.doRetrieveUtentiSorted("cognome");
        
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
        gestoreDaoData = new GestoreDaoDataSource(ds);
        

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
        java.util.Collection<Utente> result = gestoreDaoData.doRetrieveUtentiSorted("");
        
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
