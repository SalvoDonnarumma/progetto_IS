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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import gestioneordini.OrderDaoDataSource;
import gestioneordini.Ordine;
import gestioneordini.ProdottoOrdinato;
import gestioneutenti.Utente;

public class OrdineDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private OrderDaoDataSource orderDao;
	
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
        orderDao = new OrderDaoDataSource(ds);
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
	@DisplayName("TCI doSaveOrdine")
	public void doSaveOrdineTestCorretto() throws SQLException {
		ArrayList<ProdottoOrdinato> prodotti = new ArrayList<>();
		ProdottoOrdinato prod1 = new ProdottoOrdinato();
		prod1.setCode(34);
		prod1.setNome("MK2EVO");
		prod1.setCategoria("Erogatori");
		prod1.setPrice(440.0);
		prod1.setQnt(1);
		prod1.setSz("L");
		
		ProdottoOrdinato prod2 = new ProdottoOrdinato();
		prod2.setCode(35);
		prod2.setNome("ASCIUTTOBLU");
		prod2.setCategoria("Guanti");
		prod2.setPrice(65.0);
		prod2.setQnt(2);
		prod2.setSz("XL");
		
		prodotti.add(prod1);
		prodotti.add(prod2);
				
		Ordine order  = new Ordine(prodotti, "useremail@gmail.com", "01/02/2024", "IN ELABORAZIONE", 1, 233.0, "Via Fontana, 95", "03/02/2024");
		try {
			orderDao.doSave(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * from ordine order by idOrdine DESC LIMIT 1";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next()); //l'ordine è presente nel db, dunque è stato inserito correttamente
		assertEquals(order.getEmailUtente(), rs.getString("idUtente"));
		assertEquals(order.getData(), rs.getString("data"));
		assertEquals(order.getStato(), rs.getString("stato"));
		assertEquals(order.getIndirizzo(), rs.getString("indirizzo"));		
		Integer code = rs.getInt("idOrdine");
		c.close();
		
		
		String insertSQL = "delete from ordine where idordine = "+code;
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI changeStateOrderTestCorretto")
	public void changeStateOrderTestCorretto() throws SQLException {
		Ordine order  = new Ordine();
		order.setStato("IN CONSEGNA");
		order.setIdOrdine(145);
		
		try {
			orderDao.changeOrderState(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * from ordine where idOrdine = 145";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next()); //l'ordine è presente nel db, dunque è stato inserito correttamente
		assertEquals(order.getStato(), rs.getString("stato"));
		c.close();
			
		String updateSQL = "UPDATE ordine\r\n"
				+ "SET \r\n"
				+ "    stato = 'IN ELABORAZIONE'\r\n"
				+ "WHERE idOrdine = 145;\r\n"
				+ "";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(updateSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI doRetrieveAllByKeyTestPresente")
	public void doRetrieveAllByKeyTestPresente() {				
		ArrayList<Ordine> expected = new ArrayList<Ordine>();
		expected.add(new Ordine(new ArrayList<ProdottoOrdinato>(), "useremail3@gmail.com", "02/02/2024", "IN ELABORAZIONE", 148, 33.0, "Via User3, 90", null));
		Utente utente = new Utente();
		utente.setEmail("useremail3@gmail.com");
		ArrayList<Ordine> actual = null;
				
		try {
			actual = orderDao.doRetrieveAllByKey(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<actual.size(); i++)
			assertEquals(expected.get(i), actual.get(i));
	}
	
	@Test
	@DisplayName("TCI doRetrieveAllByKeyTestNonPresente")
	public void doRetrieveAllByKeyNonPresente() {			
		Utente utente = new Utente();
		utente.setEmail("useremail1@gmail.com");
		ArrayList<Ordine> actual = null;
				
		try {
			actual = orderDao.doRetrieveAllByKey(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(new ArrayList<>(), actual);
	}
	
	@Test
	@DisplayName("TCI doRetrieveByIdTestCorretto")
	public void doRetrieveByIdTestCorretto() {
		ArrayList<ProdottoOrdinato> prodotti = new ArrayList<>();
		ProdottoOrdinato prod1 = new ProdottoOrdinato();
		prod1.setCode(34);
		prod1.setNome("MK2EVO");
		prod1.setCategoria("Erogatori");
		prod1.setPrice(440.0);
		prod1.setQnt(1);
		prod1.setSz("L");
		
		ProdottoOrdinato prod2 = new ProdottoOrdinato();
		prod2.setCode(35);
		prod2.setNome("ASCIUTTOBLU");
		prod2.setCategoria("Guanti");
		prod2.setPrice(65.0);
		prod2.setQnt(2);
		prod2.setSz("XL");
		
		prodotti.add(prod1);
		prodotti.add(prod2);
		
		Ordine order = new Ordine();
		order.setIdOrdine(145);
		
		ArrayList<ProdottoOrdinato> actual = new ArrayList<>();
			
		try {
			actual = orderDao.doRetrieveById(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<prodotti.size(); i++)
			assertEquals(prodotti.get(i), actual.get(i));
	}
	
	@Test
	@DisplayName("TCI doRetrieveByIdTestNonPresente")
	public void doRetrieveByIdTestNonPresente() {
		ArrayList<ProdottoOrdinato> prodotti = new ArrayList<>();
		
		Ordine order = new Ordine();
		order.setIdOrdine(146);
		
		ArrayList<ProdottoOrdinato> actual = new ArrayList<>();
		try {
			actual = orderDao.doRetrieveById(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(prodotti, actual);
	}
	
	@Test
	@DisplayName("TCI doRetrieveByAllOrderTestCorretto")
	public void doRetrieveAllOrderTestCorretto() {
		ArrayList<Ordine> listaOrdini = new ArrayList<>();
        listaOrdini.add(new Ordine(new ArrayList<ProdottoOrdinato>(), "useremail@gmail.com", "01/02/2024", "IN ELABORAZIONE", 145, 233.0, "Via Fontana, 95", null));
        listaOrdini.add(new Ordine(new ArrayList<ProdottoOrdinato>(), "useremail3@gmail.com", "02/02/2024", "IN ELABORAZIONE", 148, 33.0, "Via User3, 90", null));
        listaOrdini.add(new Ordine(new ArrayList<ProdottoOrdinato>(), "useremail@gmail.com", "01/02/2024", "IN ELABORAZIONE", 162, 233.0, "Via Fontana, 95", null));
        listaOrdini.add(new Ordine(new ArrayList<ProdottoOrdinato>(), "useremail2@gmail.com", "02/02/2024", "RIMOSSO", 163, 133.0, "Via Fontana, 99", null));
        
        ArrayList<Ordine> actual = new ArrayList<>();
        try {
			actual = orderDao.doRetrieveAllOrders(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        for(int i = 0; i<actual.size(); i++)
        	assertEquals(listaOrdini.get(i), actual.get(i));
	}
}
