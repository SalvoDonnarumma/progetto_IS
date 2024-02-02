package IntegrationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
import gestionecarta.Carta;
import gestionegestioneutenti.GestoreDaoDataSource;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class GestoreDaoDataTest {

	private Connection conn;
	private DataSource ds;
	private GestoreDaoDataSource userDao;
	
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
        userDao = new GestoreDaoDataSource(ds);
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
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestVuoto")
	public void doRetrieveAllUsersTestVuoto() throws CheckException{
		ArrayList<Utente> expected = new ArrayList<>();
        expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore", "Donnarumma", "320-1234567", "Utente", null));
        expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando", "Tomeo", "351-1234567", "Utente", null));
        expected.add(new Utente(15, "moriocho@gmail.com", "4024462c4cf2afece78f8a233a05ba7e3c848331a71f65e1520c920d23c864ddf59a680faa664d03ee5c7dfc79d065451eb3af2bed4f1d68a6e3ff1a50b7e595", "Mario", "Rossi", "123-1234567", "Utente", null));
        expected.add(new Utente(42, "luigistruzzo@gmail.com", "6d7548a36a194376c17f89df600194b052422c1f529d4a8f6ba052eed39f3aabb6b98e129f990bf9e3fc3d26aa3286d43314124699120b1acd19852b3f2ab56a", "Luigi", "Struzzo", "111-1231234", "Utente", null));
        expected.add(new Utente(69, "matteoNapolitanoAdmin@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "000-0000000", "Gestore Utenti", null));
        expected.add(new Utente(73, "user1@gmail.com", "54ca9c5605b84c5d705da2db2af64bf1fd8b70c83aa9a745475eb6a741f7d44e4713330de752b66cb23c2df71bc6c458ee9b62b1c956ca9031e850be948c8885", "UtenteUNOIS", "IS", "111-1111111", "Utente", null));
        expected.add(new Utente(74, "user2@gmail.com", "d11f1561b21074d5a357a77fbccb2ce33b743d8e168e02632b69a2b519e3ddc430771ee9d5df7fb5ae8a9f898d32eafd0ee141325b382618b8815aab1f311e2d", "UserDueIS", "User", "222-1212122", "Utente", null));
        expected.add(new Utente(78, "emailGestoreUtenti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "123-1234567", "Gestore Utenti", null));
        expected.add(new Utente(79, "emailGestoreProdotti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Russo", "789-1234567", "Gestore Prodotti", null));
        expected.add(new Utente(80, "emailGestoreOrdini@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Gallo", "123-7654321", "Gestore Ordini", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = (ArrayList<Utente>) userDao.doRetrieveUtentiSorted(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for( Utente u : actual)
			System.out.println(u);
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestEmail")
	public void doRetrieveAllUsersTestEmail() throws CheckException{
		ArrayList<Utente> expected = new ArrayList<>();
        expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore", "Donnarumma", "320-1234567", "Utente", null));
        expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando", "Tomeo", "351-1234567", "Utente", null));
        expected.add(new Utente(15, "moriocho@gmail.com", "4024462c4cf2afece78f8a233a05ba7e3c848331a71f65e1520c920d23c864ddf59a680faa664d03ee5c7dfc79d065451eb3af2bed4f1d68a6e3ff1a50b7e595", "Mario", "Rossi", "123-1234567", "Utente", null));
        expected.add(new Utente(42, "luigistruzzo@gmail.com", "6d7548a36a194376c17f89df600194b052422c1f529d4a8f6ba052eed39f3aabb6b98e129f990bf9e3fc3d26aa3286d43314124699120b1acd19852b3f2ab56a", "Luigi", "Struzzo", "111-1231234", "Utente", null));
        expected.add(new Utente(69, "matteoNapolitanoAdmin@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "000-0000000", "Gestore Utenti", null));
        expected.add(new Utente(73, "user1@gmail.com", "54ca9c5605b84c5d705da2db2af64bf1fd8b70c83aa9a745475eb6a741f7d44e4713330de752b66cb23c2df71bc6c458ee9b62b1c956ca9031e850be948c8885", "UtenteUNOIS", "IS", "111-1111111", "Utente", null));
        expected.add(new Utente(74, "user2@gmail.com", "d11f1561b21074d5a357a77fbccb2ce33b743d8e168e02632b69a2b519e3ddc430771ee9d5df7fb5ae8a9f898d32eafd0ee141325b382618b8815aab1f311e2d", "UserDueIS", "User", "222-1212122", "Utente", null));
        expected.add(new Utente(78, "emailGestoreUtenti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "123-1234567", "Gestore Utenti", null));
        expected.add(new Utente(79, "emailGestoreProdotti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Russo", "789-1234567", "Gestore Prodotti", null));
        expected.add(new Utente(80, "emailGestoreOrdini@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Gallo", "123-7654321", "Gestore Ordini", null));
        Collections.sort(expected, Comparator.comparing(Utente::getEmail));
        
		ArrayList<Utente> actual = null;
		try {
			actual = (ArrayList<Utente>) userDao.doRetrieveUtentiSorted("email");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestNome")
	public void doRetrieveAllUsersTestNome() throws CheckException{
		ArrayList<Utente> expected = new ArrayList<>();
        expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore", "Donnarumma", "320-1234567", "Utente", null));
        expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando", "Tomeo", "351-1234567", "Utente", null));
        expected.add(new Utente(15, "moriocho@gmail.com", "4024462c4cf2afece78f8a233a05ba7e3c848331a71f65e1520c920d23c864ddf59a680faa664d03ee5c7dfc79d065451eb3af2bed4f1d68a6e3ff1a50b7e595", "Mario", "Rossi", "123-1234567", "Utente", null));
        expected.add(new Utente(42, "luigistruzzo@gmail.com", "6d7548a36a194376c17f89df600194b052422c1f529d4a8f6ba052eed39f3aabb6b98e129f990bf9e3fc3d26aa3286d43314124699120b1acd19852b3f2ab56a", "Luigi", "Struzzo", "111-1231234", "Utente", null));
        expected.add(new Utente(69, "matteoNapolitanoAdmin@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "000-0000000", "Gestore Utenti", null));
        expected.add(new Utente(73, "user1@gmail.com", "54ca9c5605b84c5d705da2db2af64bf1fd8b70c83aa9a745475eb6a741f7d44e4713330de752b66cb23c2df71bc6c458ee9b62b1c956ca9031e850be948c8885", "UtenteUNOIS", "IS", "111-1111111", "Utente", null));
        expected.add(new Utente(74, "user2@gmail.com", "d11f1561b21074d5a357a77fbccb2ce33b743d8e168e02632b69a2b519e3ddc430771ee9d5df7fb5ae8a9f898d32eafd0ee141325b382618b8815aab1f311e2d", "UserDueIS", "User", "222-1212122", "Utente", null));
        expected.add(new Utente(78, "emailGestoreUtenti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Napolitano", "123-1234567", "Gestore Utenti", null));
        expected.add(new Utente(79, "emailGestoreProdotti@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Russo", "789-1234567", "Gestore Prodotti", null));
        expected.add(new Utente(80, "emailGestoreOrdini@gmail.com", "e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41", "", "Gallo", "123-7654321", "Gestore Ordini", null));
        Collections.sort(expected, Comparator.comparing(Utente::getCognome));
        
		ArrayList<Utente> actual = null;
		try {
			actual = userDao.doRetrieveUtentiSorted("cognome");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestOrderNotValid")
	public void doRetrieveAllUsersTestOrder(){
		assertThrows(CheckException.class, ()->{ArrayList<Utente> actual = userDao.doRetrieveUtentiSorted("nome");});
	}
	
	@Test
	@DisplayName("TCU1_1_4 doSaveGestoreTestCorretto")
	public void doSaveGestoreTest() throws CheckException, DataSetException, DatabaseUnitException, SQLException{
    	Utente utente = new Utente(103, "gestoreutenti@gmail.com", "passwordLunga123", null,"Giovanna", "333-1234567", "Gestore Utenti", null);
    	try {
			userDao.doSaveGestore(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM Utente WHERE email = 'gestoreutenti@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String insertSQL = "delete from utente where email = 'gestoreutenti@gmail.com'";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCU1_1_4 doDeleteGestoreTestCorretto")
	public void doDeleteGestoreTestCorretto() throws CheckException, DataSetException, DatabaseUnitException, SQLException{
    	Utente utente = new Utente();
    	utente.setEmail("emailGestoreOrdini@gmail.com");;
    	
    	try {
			userDao.doDeleteGestore(utente);;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM Utente WHERE email = 'emailGestoreOrdini@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
		c.close();
		
		String insertSQL = "INSERT INTO utente (idutente, email, password, nome, cognome, telefono, ruolo)\r\n"
				+ "VALUES ('80', 'emailGestoreOrdini@gmail.com', 'e9b22f8fb7c5105513d1134fba73e448e3225481807dc468d57fb6243d171d2665fceceba3ccb455749a2b740851cf5a6364df5b33b16efb7855fe28da3fac41', '', 'Gallo', '123-7654321', 'Gestore Ordini');\r\n";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCU1_1_4 doDeleteGestoreTestNonPresente")
	public void doDeleteGestoreTestNonPresente() throws CheckException, DataSetException, DatabaseUnitException, SQLException{
    	Utente utente = new Utente();
    	utente.setEmail("salvatoredonnarumma2002@gmail.com");;
    	
    	int res = 0;
    	try {
			res = userDao.doDeleteGestore(utente);;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	assertTrue(res==0);
	}
	
	@Test
	@DisplayName("TCU1_1_3 changePassGestoreTestCorretto")
	public void changePassGestoreTestCorretto() throws CheckException, SQLException{
        Utente expected = new Utente(1, "salvatoredonnarumma@gmail.com", "d87d93db9e3662b09494e6899e205bdf07cfdce1566953a8b7f0aaa17b02de2f2bde84a6db341a2f81984cb85962fdaa9d30a5fa580819f4eff93c705d5fff5e", "Salvatore", "Donnarumma", "320-1234567", "Utente", null);
        
		Utente actual = new Utente();
		actual.setId(1);
		actual.setPassword("passwordAdmin2");
		actual.setEmail("salvatoredonnarumma@gmail.com");
		
		boolean flag = false;
		try {
				flag = userDao.changePassGestore(actual);
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
	@DisplayName("TCU1_1_3 changePassGestoreTestNonPresente")
	public void changePassGestoreTestNonPresente() throws CheckException, SQLException{
		Utente actual = new Utente();
		actual.setId(3);
		actual.setPassword("passwordAdmin2");
		actual.setEmail("salvatoredonnarumma@gmail.com");
		
		boolean flag = false;
		try {
				flag = userDao.changePassGestore(actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(!flag);
	}
	
	
}
