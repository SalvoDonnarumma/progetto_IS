package unitTesting;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
import gestionecarta.CartaDaoDataSource;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class CartaUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private CartaDaoDataSource cartaDaoData;
	
	@BeforeEach
	public void setUp() throws Exception {
	    ds = Mockito.mock(DataSource.class);
	    connection = mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
	    PreparedStatement preparedStatement = mock(PreparedStatement.class);
	    Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	    cartaDaoData = new CartaDaoDataSource(ds);
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestCorretto")
	public void salvaCartaTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(1, "Giorno Giovanni", "1111-2222-3333-4444", "05/2030");
		cartaDaoData.salvaCarta(carta);
		
		Mockito.verify(preparedStatement, times(1)).setInt(1, carta.getIdCarta());
        Mockito.verify(preparedStatement, times(1)).setString(2, carta.getProprietario());
        Mockito.verify(preparedStatement, times(1)).setString(3, carta.getNumero_carta());
        Mockito.verify(preparedStatement, times(1)).setString(4, carta.getData_scadenza());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestIdNonValido")
	public void salvaCartaTestIdNonValido() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(0, "Giorno Giovanni", "1111-2222-3333-4444", "05/2030");
		
		assertThrows( CheckException.class, ()->{ cartaDaoData.salvaCarta(carta);} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestProprietarioNonValido")
	public void salvaCartaTestNomeNonValido() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(1, "", "1111-2222-3333-4444", "05/2030");
		
		assertThrows( CheckException.class, ()->{ cartaDaoData.salvaCarta(carta);} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestNumeroCartaNonValido")
	public void salvaCartaTestNumeroCartaNonValido() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(0, "Giorno Giovanni", "1111-2222-3333-44e4", "05/2030");
		
		assertThrows( CheckException.class, ()->{ cartaDaoData.salvaCarta(carta);} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestDataNonValido")
	public void salvaCartaTestDataNonValido() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(0, "Giorno Giovanni", "1111-2222-3333-4444", null);
		
		assertThrows( CheckException.class, ()->{ cartaDaoData.salvaCarta(carta);} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 cancellaCartaTestCorretto")
	public void cancellaCartaTestDataNonValido() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        Carta carta = new Carta();
        carta.setIdCarta(1);
        cartaDaoData.cancellaCarta(carta);
	}
	
	@Test
	@DisplayName("TCU1_2_1 cancellaCartaTestNonValido")
	public void cancellaCartaTestCartaNull() throws SQLException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        assertThrows( CheckException.class, ()->{ cartaDaoData.cancellaCarta(null);} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 cancellaCartaTestNonValido")
	public void cancellaCartaTestCartaVuota() throws SQLException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        assertThrows( CheckException.class, ()->{ cartaDaoData.cancellaCarta(new Carta());} );
	}
	
	@Test
	@DisplayName("TCU1_2_1 recuperaTestCorretto")
	public void recuperaCartaTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        cartaDaoData = new CartaDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next())
        .thenReturn(true);
        Mockito.when(resultSet.getInt(Mockito.eq("idcarta"))).thenReturn(1);
        Mockito.when(resultSet.getString(Mockito.eq("proprietario"))).thenReturn("Donnarumma Salvatore");
        Mockito.when(resultSet.getString(Mockito.eq("data_scadenza"))).thenReturn("08/29");
        Mockito.when(resultSet.getString(Mockito.eq("numero_carta"))).thenReturn("1111-2222-3333-4444");
       
        Utente mockUser = Mockito.mock(Utente.class);
        when(mockUser.getId()).thenReturn(1);
        Carta result = cartaDaoData.recuperaCarta(mockUser);
        
        assertEquals(1, result.getIdCarta());
        assertEquals("Donnarumma Salvatore", result.getProprietario());
        assertEquals("08/29", result.getData_scadenza());
        assertEquals("1111-2222-3333-4444", result.getNumero_carta());
		
		Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
		Mockito.verify(preparedStatement, times(1)).executeQuery();
		Mockito.verify(resultSet, times(1)).next();
		
        Mockito.verify(resultSet, times(1)).getInt("idcarta");
        Mockito.verify(resultSet, times(1)).getString("proprietario");
        Mockito.verify(resultSet, times(1)).getString("data_scadenza");
        Mockito.verify(resultSet, times(1)).getString("numero_carta");
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU1_2_1 recuperaCartaTestNonPresente")
	public void recuperaCartaTestNonPresente() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        cartaDaoData = new CartaDaoDataSource(ds);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
       
        Utente mockUser = Mockito.mock(Utente.class);
        when(mockUser.getId()).thenReturn(1);
        Carta result = cartaDaoData.recuperaCarta(mockUser);
        assertNull(result);
		
        Mockito.verify(preparedStatement, times(1)).setInt(eq(1), eq(1));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        resultSet.close();
	}
	
}
