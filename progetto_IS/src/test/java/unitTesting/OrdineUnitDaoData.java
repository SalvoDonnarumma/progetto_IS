package unitTesting;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarta.Carta;
import gestioneordini.OrderDaoDataSource;
import gestioneordini.Ordine;
import gestioneordini.ProdottoOrdinato;
import gestioneprodotti.Taglie;

public class OrdineUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private OrderDaoDataSource orderDaoData;
	private PreparedStatement preparedStatement;
	
	@BeforeEach
	public void setUp() throws Exception {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
	}
	
	@Test
	@DisplayName("TCU doSaveOrdineTestCorretto")
	public void doSaveOrdine() throws SQLException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(resultSet.next()).thenReturn(true, false);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		//List<ProdottoOrdinato> orders, String idUtente, String data, String stato, Integer idOrdine, 
		//Double prezzototale, String indirizzo, String data_consegna
		
		ArrayList<ProdottoOrdinato> prodotti = new ArrayList<>();
		ProdottoOrdinato prod1 = Mockito.mock(ProdottoOrdinato.class);
		ProdottoOrdinato prod2 = Mockito.mock(ProdottoOrdinato.class);
		ProdottoOrdinato prod3 = Mockito.mock(ProdottoOrdinato.class);
		
		Mockito.when(prod1.getCategoria()).thenReturn("Coltelli");
		Mockito.when(prod1.getNome()).thenReturn("Knife");
		Mockito.when(prod1.getPrice()).thenReturn(35.00);
		Mockito.when(prod1.getCode()).thenReturn(1);
		
		Mockito.when(prod2.getCategoria()).thenReturn("Erogatori");
		Mockito.when(prod2.getNome()).thenReturn("MK2Evo");
		Mockito.when(prod2.getPrice()).thenReturn(235.00);
		Mockito.when(prod2.getCode()).thenReturn(2);
		
		Mockito.when(prod3.getCategoria()).thenReturn("Erogatori");
		Mockito.when(prod3.getNome()).thenReturn("R109");
		Mockito.when(prod3.getPrice()).thenReturn(225.00);
		Mockito.when(prod3.getCode()).thenReturn(3);
			
		prodotti.add(prod1);
		prodotti.add(prod2);
		prodotti.add(prod3);
		
		Ordine order  = new Ordine(prodotti, "useremail@gmail.com", "01/02/2024", "IN ELABORAZIONE", 1, 233.0, "Via Fontana, 95", "03/02/2024");
		try {
			orderDaoData.doSave(order);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
		Double totalprice = orderDaoData.doSaveProdottiOrdinati(prodotti, order);
		orderDaoData.updatePrezzo(totalprice, order.getIdOrdine());
		
		//Inserisco l'ordine
		Mockito.verify(preparedStatement, times(1)).setString(1, order.getEmailUtente());
        Mockito.verify(preparedStatement, times(1)).setString(2, order.getData());
        Mockito.verify(preparedStatement, times(1)).setString(3, order.getStato());
        Mockito.verify(preparedStatement, times(1)).setString(4, order.getIndirizzo());
        
        //Inserisco il primo prodotto
        Mockito.verify(preparedStatement, times(1)).setString(1, order.getEmailUtente());
        Mockito.verify(preparedStatement, times(1)).setInt(2, prod1.getCode());
        Mockito.verify(preparedStatement, times(1)).setString(3, prod1.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, prod1.getCategoria());
        Mockito.verify(preparedStatement, times(1)).setDouble(5, prod1.getPrice());
        
        //Inserisco il secondo prodotto
        Mockito.verify(preparedStatement, times(1)).setString(1, order.getEmailUtente());
        Mockito.verify(preparedStatement, times(1)).setInt(2, prod2.getCode());
        Mockito.verify(preparedStatement, times(1)).setString(3, prod2.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, prod2.getCategoria());
        Mockito.verify(preparedStatement, times(1)).setDouble(5, prod2.getPrice());
       
        //Inserisco il terzo prodotto
        Mockito.verify(preparedStatement, times(1)).setString(1, order.getEmailUtente());
        Mockito.verify(preparedStatement, times(1)).setInt(2, prod3.getCode());
        Mockito.verify(preparedStatement, times(1)).setString(3, prod3.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, prod3.getCategoria());
        Mockito.verify(preparedStatement, times(1)).setDouble(5, prod3.getPrice());
        
        Mockito.verify(preparedStatement, times(4)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU changeStateOrderTestCorretto")
	public void changeStateOrderTestCorretto() throws SQLException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		Ordine order= new Ordine();
		order.setIdOrdine(1);
		order.setStato("IN CONSEGNA");
		
		try {
			orderDaoData.changeOrderState(order);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(preparedStatement, times(1)).setString(1, order.getStato());
		Mockito.verify(preparedStatement, times(1)).setInt(2, order.getIdOrdine());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU changeStateOrderTestNull")
	public void changeStateOrderTestNull() throws SQLException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		assertThrows( CheckException.class, ()->{ orderDaoData.doRetrieveById(null);} );
	}
	
	@Test
	@DisplayName("TCU changeStateOrderTestVuoto")
	public void changeStateOrderTestVuoto() throws SQLException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		assertThrows( CheckException.class, ()->{ orderDaoData.doRetrieveById(new Ordine());} );
	}
	
	@Test
	@DisplayName("TCU doRetrieveAllTestNull")
	public void doRetrieveByAllTestNull() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idOrdine"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("idUtente"))).thenReturn("emailUser1@gmail.com", "emailUser2@gmail.com");
        Mockito.when(resultSet.getString(Mockito.eq("data"))).thenReturn("11/01/2024", "15/08/2024");       
        Mockito.when(resultSet.getString(Mockito.eq("stato"))).thenReturn("IN ELABORAZIONE", "IN CONSEGNA");
        Mockito.when(resultSet.getString(Mockito.eq("indirizzo"))).thenReturn("Via User1, 11", "Via User2, 22");
        Mockito.when(resultSet.getDouble(Mockito.eq("prezzototale"))).thenReturn(35.00, 50.00);
        
        orderDaoData.doRetrieveAllOrders(null);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idOrdine"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("idUtente"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("data"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("stato"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("indirizzo"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("prezzototale"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU doRetrieveAllTestVuoto")
	public void doRetrieveByAllTestVuoto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idOrdine"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("idUtente"))).thenReturn("emailUser1@gmail.com", "emailUser2@gmail.com");
        Mockito.when(resultSet.getString(Mockito.eq("data"))).thenReturn("11/01/2024", "15/08/2024");       
        Mockito.when(resultSet.getString(Mockito.eq("stato"))).thenReturn("IN ELABORAZIONE", "IN CONSEGNA");
        Mockito.when(resultSet.getString(Mockito.eq("indirizzo"))).thenReturn("Via User1, 11", "Via User2, 22");
        Mockito.when(resultSet.getDouble(Mockito.eq("prezzototale"))).thenReturn(35.00, 50.00);
        
        orderDaoData.doRetrieveAllOrders("");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idOrdine"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("idUtente"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("data"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("stato"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("indirizzo"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("prezzototale"));
        resultSet.close();
	}

	@Test
	@DisplayName("TCU doRetrieveAllTestNonPresente")
	public void doRetrieveByAllTestNonPresente() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);

		Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        orderDaoData.doRetrieveAllOrders("");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU doRetrieveByIdTestCorretto")
	public void doRetrieveByIdTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
			
		Ordine order = new Ordine();
		order.setIdOrdine(1);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        orderDaoData.doRetrieveById(order);
        
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setInt(1, order.getIdOrdine());
        
        Mockito.verify(resultSet, times(3)).getString( "nome" );
        Mockito.verify(resultSet, times(3)).getString( "categoria");
        Mockito.verify(resultSet, times(3)).getDouble( "prezzo");
        Mockito.verify(resultSet, times(3)).getInt( "quantita");
        Mockito.verify(resultSet, times(3)).getString( "size");
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU doRetrieveByIdTestNonPresente")
	public void doRetrieveByIdTestNonPresente() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		Ordine order = new Ordine();
		order.setIdOrdine(1);
		
		Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        orderDaoData.doRetrieveById(order);
        
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setInt(1, order.getIdOrdine());
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU doRetrieveByIdTestNonPresente")
	public void doRetrieveByIdTestNull() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		assertThrows( CheckException.class, ()->{ orderDaoData.doRetrieveById(null);} );
	}
	
	@Test
	@DisplayName("TCU doRetrieveByIdTestVuoto")
	public void doRetrieveByIdTestVuoto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		orderDaoData = new OrderDaoDataSource(ds);
		
		assertThrows( CheckException.class, ()->{ orderDaoData.doRetrieveById(new Ordine());} );
	}
	
}
