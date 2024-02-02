package IntegrationTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.dbunit.Assertion;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.Utente;

public class CarrelloDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private CarrelloDaoDataSource carrelloDaoData;
    
    @BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		String ip = "localhost";
		String port = "3306";
		String db = "storageprogettotest";
		String username = "root";
		String password = "root1234@Z*";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db + "?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
		
		ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);
        carrelloDaoData = new CarrelloDaoDataSource(ds);
	}
	
	public static Connection newConnection() throws SQLException {
		String ip = "localhost";
		String port = "3306";
		String db = "storageprogettotest";
		String username = "root";
		String password = "root1234@Z*";
		Connection connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db + "?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        return connection;
    }
	
	@AfterEach
	public void tearDown() throws SQLException{
		conn.close();
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCarrelloTestCorretto")
	public void salvaCarrelloTestCorretto() throws CheckException, DataSetException, SQLException{
    	Prodotto prod1 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(34);
    	
    	Prodotto prod2 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod2.getCode()).thenReturn(35);
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(2);
    	carrello.addProduct(prod1);
    	carrello.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(carrello.getIdcarrello());
    	try {
			carrelloDaoData.salvaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String deleteSQL = "DELETE FROM prodottocarrello WHERE idcarrello = 2";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(deleteSQL);
		ps1.executeUpdate();
		c.close();
	}
	
	@Test
	@DisplayName("TCU1_2_1 salvaCarrelloTestPresente")
	public void salvaCarrelloTestPresente() throws CheckException, DataSetException, SQLException{
    	Prodotto prod1 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(34);
    	
    	Prodotto prod2 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod2.getCode()).thenReturn(35);
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(2);
    	carrello.addProduct(prod1);
    	carrello.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(carrello.getIdcarrello());
    	
    	try {
			carrelloDaoData.salvaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String deleteSQL = "DELETE FROM prodottocarrello WHERE idcarrello = 2";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(deleteSQL);
		ps1.executeUpdate();
		c.close();
	}
	
	@Test
	@DisplayName("TCI1_2_1 recuperaCarrelloTestCorretto")
	public void recuperaCarrelloTestCorretto() throws CheckException, DataSetException, SQLException{
    	ProductDaoDataSource productDaoData = Mockito.mock(ProductDaoDataSource.class);
		Prodotto prod1 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(35);
    	Taglie taglie1 = Mockito.mock(Taglie.class);
    	Mockito.when(productDaoData.getSizesByKey(prod1)).thenReturn(taglie1);
    	
    	Prodotto prod2 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod2.getCode()).thenReturn(84);
    	Taglie taglie2 = Mockito.mock(Taglie.class);
    	Mockito.when(productDaoData.doRetrieveByKey(prod1)).thenReturn(prod1).thenReturn(prod2);
    	Mockito.when(productDaoData.getSizesByKey(prod2)).thenReturn(taglie2);
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	
    	Carrello expected = new Carrello();
    	expected.setIdcarrello(1);
    	expected.addProduct(prod1);
    	expected.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(expected.getIdcarrello());
    	
    	Carrello actual = null;
    	try {
			actual = carrelloDaoData.recuperaCarrello(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCI1_2_1 recuperaCarrelloTestNonPresente")
	public void recuperaCarrelloTestNonPresente() throws CheckException, DataSetException, SQLException{	
    	Carrello expected = new Carrello();
    	expected.setIdcarrello(-1);

    	Utente u = new Utente();
    	u.setId(expected.getIdcarrello());
    	
    	Carrello actual = null;
    	try {
			actual = carrelloDaoData.recuperaCarrello(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	assertEquals(null, actual);
	}

	@Test
	@DisplayName("TCU1_2_2 cancellaCartaTestCorretto")
	public void cancellaCartaCorrettoTest() throws CheckException, DataSetException, SQLException{
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(1);
    	try {
			carrelloDaoData.eliminaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 1";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
		c.close();
	}
	
}
