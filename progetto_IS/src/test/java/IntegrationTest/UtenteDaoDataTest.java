package IntegrationTest;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import checking.CheckException;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class UtenteDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private UserDaoDataSource userDao;
	
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
		userDao = new UserDaoDataSource(ds);	    
	}
	
	@BeforeAll
    public static void setUpDB() throws SQLException{
    	String insertSQL = "delete from utente where email = 'gestoreutenti@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(insertSQL);
		int rs = ps.executeUpdate();

		c.close();
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
	@DisplayName("TCI1_1_1 doRetrieveByEmailTestCorretto")
	public void doRetrieveByEmailTestCorretto() throws SQLException, CheckException, ClassNotFoundException {
		Utente expected = new Utente();
		expected.setEmail("salvatoredonnarumma@gmail.com");

	    Utente result = userDao.doRetrieveByEmail(expected);

	    assertEquals(1, result.getId());
	    assertEquals("salvatoredonnarumma@gmail.com", result.getEmail());
	    assertEquals("Salvatore", result.getNome());
	    assertEquals("Donnarumma", result.getCognome());
	    assertEquals("320-1234567", result.getTelefono());
	    assertEquals("Utente", result.getRuolo());
	}
	
	@Test
	@DisplayName("TCI1_1_2 doRetrieveByEmailNonPresenteTest")
	public void doRetrieveByEmailNonPresenteTest() throws SQLException, CheckException, ClassNotFoundException {
		Utente expected = new Utente();
		expected.setEmail("email@nonesistente.com");

	    Utente actual = userDao.doRetrieveByEmail(expected);
	    assertNull(actual);
	}

	@Test
	@DisplayName("TCU1_1_3 changePassTestCorretto")
	public void changePassTestCorretto() throws CheckException, SQLException, ClassNotFoundException{
		Utente expected = new Utente(1, "salvatoredonnarumma@gmail.com", "d87d93db9e3662b09494e6899e205bdf07cfdce1566953a8b7f0aaa17b02de2f2bde84a6db341a2f81984cb85962fdaa9d30a5fa580819f4eff93c705d5fff5e", "Salvatore", "Donnarumma", "320-1234567", "Utente", null);
        
		Utente actual = new Utente();
		actual.setId(1);
		actual.setPassword("passwordAdmin2");
		actual.setEmail("salvatoredonnarumma@gmail.com");
		
		boolean flag = false;
		try {
				flag = userDao.changePass(actual.getPassword(),actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(flag);
		
		String resultSQL = "SELECT * FROM Utente WHERE email = 'salvatoredonnarumma@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		String new_pass = rs.getString("password");
		c.close();
		
		assertEquals(expected.getPassword(), new_pass);
	}
	
	@Test
	@DisplayName("TCU1_1_4 doSaveUserTestCorretto")
	public void doSaveUserTestCorretto() throws CheckException, DataSetException, DatabaseUnitException, SQLException, ClassNotFoundException{
		Utente utente = new Utente(77,"orlandotomeo02@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando", "Tomeo", "351-1234567", "Utente", null);
    	try {
			userDao.doSaveUser(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM Utente WHERE email = 'orlandotomeo02@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String insertSQL = "delete from utente where email = 'orlandotomeo02@gmail.com'";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI1_1_1 doRetrieveByKeyTestCorretto")
	public void doRetrieveByKeyTestCorretto() throws SQLException, CheckException, ClassNotFoundException {
		Utente expected = new Utente();
		expected.setId(1);

	    Utente result = userDao.doRetrieveByKey(expected);

	    assertEquals(1, result.getId());
	    assertEquals("salvatoredonnarumma@gmail.com", result.getEmail());
	    assertEquals("Salvatore", result.getNome());
	    assertEquals("Donnarumma", result.getCognome());
	    assertEquals("320-1234567", result.getTelefono());
	    assertEquals("Utente", result.getRuolo());    		
	}
	
	@Test
	@DisplayName("TCI1_1_2 doRetrieveByKeyNonEsistenteTest")
	public void doRetrieveByKeyNonEsistenteTest() throws SQLException, CheckException, ClassNotFoundException {
	    Utente expected = new Utente();
		expected.setId(2369);

	    Utente actual = userDao.doRetrieveByKey(expected);
	    assertNull(actual);	     		
	}
}
