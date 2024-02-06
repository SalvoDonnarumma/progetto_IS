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
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarta.Carta;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.UserDaoDataSource;

public class ProdottoUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private ProductDaoDataSource productDaoData;
	private PreparedStatement preparedStatement;

	@BeforeEach
	public void setUp() throws Exception {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdotto")
	public void doSaveProdottoTestCorretto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome prodotto", "Descrizione prodotto", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		
		
		productDaoData.doSave(prodotto);
		
        Mockito.verify(preparedStatement, times(1)).setString(1, prodotto.getCategoria());
        Mockito.verify(preparedStatement, times(1)).setString(2, prodotto.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(3, prodotto.getDescrizione());
        Mockito.verify(preparedStatement, times(1)).setDouble(4, prodotto.getPrice());
        Mockito.verify(preparedStatement, times(1)).setString(5, prodotto.getStats());
        Mockito.verify(preparedStatement, times(1)).setString(6, prodotto.getImagePath());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoNomeNull")
	public void doSaveProdottoTestNomeNull() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, null, "Descrizione prodotto", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoNomeVuoto")
	public void doSaveProdottoTestNomeVuoto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "", "Descrizione", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoDescrizioneNull")
	public void doSaveProdottoTestDescrizioneNull() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", null, "Coltelli",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoDescrizioneVuoto")
	public void doSaveProdottoTestDescrizioneVuoto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoCategoriaNull")
	public void doSaveProdottoCategoriaNull() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0,  "Nome", "Descrizione", null,35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoCategoriaVuoto")
	public void doSaveProdottoCategoriaVuoto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0,  "Nome", "Descrizione", "",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoCategoriaNonValida")
	public void doSaveProdottoCategoriaNonValida() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "Descrizione", "Coltello",35.00, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoPrezzoNull")
	public void doSaveProdottoPrezzoNull() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "Descrizione", "Coltelli",null, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoPrezzoNonValido")
	public void doSaveProdottoPrezzoNonValido() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "Descrizione", "Coltello",-1.0, "statistiche", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoStastisticheVuoto")
	public void doSaveProdottoStastisticheVuoto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "Descrizione", "Coltello",33.0, "", taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 doSaveProdottoStastisticheNull")
	public void doSaveProdottoStastisticheNull() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(0, "Nome", "Descrizione", "Coltello",33.0,null, taglie, "imagepath");
		assertThrows( CheckException.class, ()->{ productDaoData.doSave(prodotto);} );
	}
	
	@Test
	@DisplayName("TCU3_1_1 setTaglieByKey")
	public void doSaveTaglieTestCorretto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = new Taglie(1, 1,2,3,4);
		productDaoData.setSizesByKey(1, taglie);;
		
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(preparedStatement, times(1)).setInt(2, taglie.getQuantitaM());
        Mockito.verify(preparedStatement, times(1)).setInt(3, taglie.getQuantitaL());
        Mockito.verify(preparedStatement, times(1)).setInt(4, taglie.getQuantitaXL());
        Mockito.verify(preparedStatement, times(1)).setInt(5, taglie.getQuantitaXXL());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	
	
	@Test
	@DisplayName("TCU3_1_1 doUpdateProdottoTestCorretto")
	public void doUpdateProdottoTestCorretto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = Mockito.mock(Taglie.class);
		Prodotto prodotto = new Prodotto(1, "Nome prodotto", "Descrizione prodotto", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		
		productDaoData.doUpdate(1, prodotto);
		
        Mockito.verify(preparedStatement, times(1)).setString(1, prodotto.getCategoria());
        Mockito.verify(preparedStatement, times(1)).setString(2, prodotto.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(3, prodotto.getDescrizione());
        Mockito.verify(preparedStatement, times(1)).setDouble(4, prodotto.getPrice());
        Mockito.verify(preparedStatement, times(1)).setString(5, prodotto.getStats());
        Mockito.verify(preparedStatement, times(1)).setString(6, prodotto.getImagePath());
        Mockito.verify(preparedStatement, times(1)).setInt(7, prodotto.getCode());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU3_1_1 setTaglieByKey")
	public void doUpdateTaglieTestCorretto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
		Taglie taglie = new Taglie(1, 1,2,3,4);
		productDaoData.setSizesByKey(1, taglie);;
		
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(preparedStatement, times(1)).setInt(2, taglie.getQuantitaM());
        Mockito.verify(preparedStatement, times(1)).setInt(3, taglie.getQuantitaL());
        Mockito.verify(preparedStatement, times(1)).setInt(4, taglie.getQuantitaXL());
        Mockito.verify(preparedStatement, times(1)).setInt(5, taglie.getQuantitaXXL());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveAllTest")
	public void doRetrieveByAllTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		Taglie taglie = Mockito.mock(Taglie.class);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("CATEGORIA"))).thenReturn("Coltelli", "Erogatori");
        Mockito.when(resultSet.getString(Mockito.eq("NOME"))).thenReturn("Kckife", "MK2Evo");       
        Mockito.when(resultSet.getString(Mockito.eq("descrizione"))).thenReturn("Descrizione", "Descrizione");
        Mockito.when(resultSet.getDouble(Mockito.eq("PRICE"))).thenReturn(35.00, 36.00);
        Mockito.when(resultSet.getString(Mockito.eq("STATS"))).thenReturn("Statistiche", "Statistiche");
        Mockito.when(resultSet.getString(Mockito.eq("IMAGE"))).thenReturn("ImagePath", "ImagePath");
        
        productDaoData.doRetrieveAll(null);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idProdotto"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("CATEGORIA"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("NOME"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("descrizione"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("PRICE"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("STATS"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("IMAGE"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveAllTest")
	public void doRetrieveByAllTestNonPresente() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		Taglie taglie = Mockito.mock(Taglie.class);
		
		Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        productDaoData.doRetrieveAll(null);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveAllTest")
	public void doRetrieveByAllTestVuoto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		Taglie taglie = Mockito.mock(Taglie.class);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("CATEGORIA"))).thenReturn("Coltelli", "Erogatori");
        Mockito.when(resultSet.getString(Mockito.eq("NOME"))).thenReturn("Kckife", "MK2Evo");       
        Mockito.when(resultSet.getString(Mockito.eq("descrizione"))).thenReturn("Descrizione", "Descrizione");
        Mockito.when(resultSet.getDouble(Mockito.eq("PRICE"))).thenReturn(35.00, 36.00);
        Mockito.when(resultSet.getString(Mockito.eq("STATS"))).thenReturn("Statistiche", "Statistiche");
        Mockito.when(resultSet.getString(Mockito.eq("IMAGE"))).thenReturn("ImagePath", "ImagePath");
        
        productDaoData.doRetrieveAll("");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idProdotto"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("CATEGORIA"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("NOME"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("descrizione"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("PRICE"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("STATS"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("IMAGE"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveAllTest")
	public void doRetrieveByAllTestOrderNome() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		Taglie taglie = Mockito.mock(Taglie.class);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("CATEGORIA"))).thenReturn("Coltelli", "Erogatori");
        Mockito.when(resultSet.getString(Mockito.eq("NOME"))).thenReturn("Kckife", "MK2Evo");       
        Mockito.when(resultSet.getString(Mockito.eq("descrizione"))).thenReturn("Descrizione", "Descrizione");
        Mockito.when(resultSet.getDouble(Mockito.eq("PRICE"))).thenReturn(35.00, 36.00);
        Mockito.when(resultSet.getString(Mockito.eq("STATS"))).thenReturn("Statistiche", "Statistiche");
        Mockito.when(resultSet.getString(Mockito.eq("IMAGE"))).thenReturn("ImagePath", "ImagePath");
        
        productDaoData.doRetrieveAll("nome");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idProdotto"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("CATEGORIA"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("NOME"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("descrizione"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("PRICE"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("STATS"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("IMAGE"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveAllTest")
	public void doRetrieveByAllTestOrderCategoria() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		Taglie taglie = Mockito.mock(Taglie.class);
		
		Mockito.when(resultSet.next()).thenReturn(true, true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1, 2);
        Mockito.when(resultSet.getString(Mockito.eq("CATEGORIA"))).thenReturn("Coltelli", "Erogatori");
        Mockito.when(resultSet.getString(Mockito.eq("NOME"))).thenReturn("Kckife", "MK2Evo");       
        Mockito.when(resultSet.getString(Mockito.eq("descrizione"))).thenReturn("Descrizione", "Descrizione");
        Mockito.when(resultSet.getDouble(Mockito.eq("PRICE"))).thenReturn(35.00, 36.00);
        Mockito.when(resultSet.getString(Mockito.eq("STATS"))).thenReturn("Statistiche", "Statistiche");
        Mockito.when(resultSet.getString(Mockito.eq("IMAGE"))).thenReturn("ImagePath", "ImagePath");
        
        productDaoData.doRetrieveAll("categoria");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        // Verifica che il metodo next sia stato chiamato correttamente sul resultSet
        Mockito.verify(resultSet, times(2) ).getInt(Mockito.eq("idProdotto"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("CATEGORIA"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("NOME"));       
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("descrizione"));
        Mockito.verify(resultSet, times(2)).getDouble(Mockito.eq("PRICE"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("STATS"));
        Mockito.verify(resultSet, times(2)).getString(Mockito.eq("IMAGE"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU doDeleteProdotto")
	public void doDeleteProdottoTestCorretto() throws SQLException, CheckException {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		
		Prodotto prodotto = new Prodotto();
		prodotto.setCode(1);
		
		productDaoData.doDelete(prodotto);
		
		Mockito.verify(preparedStatement, times(2)).setInt(1, prodotto.getCode());
        Mockito.verify(preparedStatement, times(2)).executeUpdate();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveByKeyTestCorretto")
	public void doRetrieveByKeyTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		
		Mockito.when(resultSet.next()).thenReturn(true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1);
        Mockito.when(resultSet.getString(Mockito.eq("CATEGORIA"))).thenReturn("Coltelli");
        Mockito.when(resultSet.getString(Mockito.eq("NOME"))).thenReturn("Kckife");       
        Mockito.when(resultSet.getString(Mockito.eq("descrizione"))).thenReturn("Descrizione");
        Mockito.when(resultSet.getDouble(Mockito.eq("PRICE"))).thenReturn(35.00);
        Mockito.when(resultSet.getString(Mockito.eq("STATS"))).thenReturn("Statistiche");
        Mockito.when(resultSet.getString(Mockito.eq("IMAGE"))).thenReturn("ImagePath");
        
        Prodotto da_cercare = new Prodotto();
        da_cercare.setCode(1);
        
        productDaoData.doRetrieveByKey(da_cercare);
      
        Mockito.verify(preparedStatement, times(1)).setInt(1, da_cercare.getCode());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        
        Mockito.verify(resultSet, times(1) ).getInt(Mockito.eq("IDPRODOTTO"));
        Mockito.verify(resultSet, times(1)).getString(Mockito.eq("CATEGORIA"));
        Mockito.verify(resultSet, times(1)).getString(Mockito.eq("NOME"));       
        Mockito.verify(resultSet, times(1)).getString(Mockito.eq("DESCRIZIONE"));
        Mockito.verify(resultSet, times(1)).getDouble(Mockito.eq("PRICE"));
        Mockito.verify(resultSet, times(1)).getString(Mockito.eq("STATS"));
        Mockito.verify(resultSet, times(1)).getString(Mockito.eq("IMAGE"));
        resultSet.close();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveByKeyTestNonPresente")
	public void doRetrieveByKeyTestNonPresente() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		
		Mockito.when(resultSet.next()).thenReturn(false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        Prodotto da_cercare = new Prodotto();
        da_cercare.setCode(1);
        
        productDaoData.doRetrieveByKey(da_cercare);

        Mockito.verify(preparedStatement, times(1)).executeQuery();
	}
	
	@Test
	@DisplayName("TCU3_1_1 doRetrieveByKeyTestCorretto")
	public void getTaglieByKeyTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		productDaoData = new ProductDaoDataSource(ds);
		
		Mockito.when(resultSet.next()).thenReturn(true, false); // Ci sono risultati
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getInt(Mockito.eq("idProdotto"))).thenReturn(1);
        Mockito.when(resultSet.getInt(Mockito.eq("tagliaM"))).thenReturn(2);
        Mockito.when(resultSet.getInt(Mockito.eq("tagliaL"))).thenReturn(0);       
        Mockito.when(resultSet.getInt(Mockito.eq("tagliaXL"))).thenReturn(3);
        Mockito.when(resultSet.getInt(Mockito.eq("tagliaXXL"))).thenReturn(5);
        
        Prodotto da_cercare = new Prodotto();
        da_cercare.setCode(1);
        
        productDaoData.getSizesByKey(da_cercare);
      
        Mockito.verify(preparedStatement, times(1)).setInt(1, da_cercare.getCode());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        
        Mockito.verify(resultSet, times(1) ).getInt(Mockito.eq("idProdotto"));
        Mockito.verify(resultSet, times(1)).getInt(Mockito.eq("tagliaM"));
        Mockito.verify(resultSet, times(1)).getInt(Mockito.eq("tagliaL"));       
        Mockito.verify(resultSet, times(1)).getInt(Mockito.eq("tagliaXL"));
        Mockito.verify(resultSet, times(1)).getInt(Mockito.eq("tagliaXXL"));
        resultSet.close();
	}
}
