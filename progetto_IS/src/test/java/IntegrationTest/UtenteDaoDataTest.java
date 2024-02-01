package IntegrationTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.sql.DataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarta.Carta;
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
	
	public Connection newConnection() throws SQLException {
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
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestPresente")
	public void doRetrieveByEmailTestPresente() throws CheckException {
		
		Utente expected = new Utente();
		expected.setId(15);
		
		Utente actual = new Utente();
		actual.setEmail("moriocho@gmail.com");
		try {
			actual = userDao.doRetrieveByEmail(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected.getId(), actual.getId());
	}

	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestNonPresente")
	public void doRetrieveByEmailTestNonPresente() throws CheckException {
		Utente expected = null;	
		Utente actual = new Utente();
		actual.setEmail("salvatoredonnarumma2002@gmail.com");
		try {
			actual = userDao.doRetrieveByEmail(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(null, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestVuota")
	public void doRetrieveByEmailTestVuoto() throws CheckException {
		Utente actual = new Utente();
		actual.setEmail("");
		assertThrows(CheckException.class, ()->{Utente utente = userDao.doRetrieveByEmail(actual);});
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestNull")
	public void doRetrieveByEmailTestNull() throws CheckException {
		assertThrows(CheckException.class, ()->{Utente utente = userDao.doRetrieveByEmail(null);});
	}
	
	
	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestPresente")
	public void doRetrieveByKeyTestPresente() throws CheckException {
		Utente expected = new Utente();
		expected.setId(15);
		
		Utente actual = new Utente();
		actual.setId(15);
		try {
			actual = userDao.doRetrieveByKey(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected.getId(), actual.getId());
	}

	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestNonPresente")
	public void doRetrieveByKeyTestNonPresente() throws CheckException {
		Utente expected = null;	
		Utente actual = new Utente();
		actual.setId(0);
		try {
			actual = userDao.doRetrieveByKey(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestNull")
	public void doRetrieveBKeyTestNull() throws CheckException {
		assertThrows(CheckException.class, ()->{Utente utente = userDao.doRetrieveByKey(null);});
	}
	
	/*
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestVuoto")
	public void doRetrieveAllUsersTestVuoto(){
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
			actual = (ArrayList<Utente>) userDao.doRetrieveAllUsers("");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	*/
	
	
	/*
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
			actual = userDao.doRetrieveAllUsers("email");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	*/
	
	/*
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
        Collections.sort(expected, Comparator.comparing(Utente::getNome));
        
		ArrayList<Utente> actual = null;
		try {
			actual = userDao.doRetrieveAllUsers("nome");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestOrderNotValid")
	public void doRetrieveAllUsersTestOrder(){
		assertThrows(CheckException.class, ()->{ArrayList<Utente> actual = userDao.doRetrieveAllUsers("cognome");});
	}
	*/
	
	@Test
	@DisplayName("TCU1_1_4 doSaveUserTestCorretto")
	public void doSaveUserTest() throws CheckException, DataSetException, DatabaseUnitException, SQLException{
		
		Carta carta = Mockito.mock(Carta.class);
    	Utente utente = new Utente(3, "jojoland@gmail.com", "jojo02", "Giorno","Giovanna", "333-1234567", "utente", carta);
    	try {
			userDao.doSaveUser(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM Utente WHERE email = 'jojoland@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
	}

	
	
	
	@ParameterizedTest
    @MethodSource("doSaveTestProvider")
    @DisplayName("TCU1_1_4 doSaveTestNonSalva")
    public void doSaveTestNonSalva(Integer id, String email, String password, String nome, String cognome, String telefono, String ruolo) {
    	assertThrows(CheckException.class, () -> {
    		Carta carta = Mockito.mock(Carta.class);
    		Utente utente = new Utente(id, email, password, nome, cognome, telefono, ruolo, carta);
			userDao.doSaveUser(utente);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProvider(){
    	return Stream.of(
    			//formato email non corretto
    			Arguments.of(3, "jojoland.com", "Giorno", "Giovanna", "jojo02", "333-1234567", "utente"),
    			//email vuota
    			Arguments.of(3, "", "Giorno", "Giovanna", "jojo02", "333-1234567", "utente"),
    			//password vuota
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","Giovanna", "","333-1234567", "utente"),
    			//nome vuoto
    			Arguments.of(3, "jojoland2@gmail.com", "","Giovanna", "jojo02","333-1234567", "utente"),
    			//nome null
    			Arguments.of(3, "jojoland2@gmail.com", null,"Giovanna", "jojo02","333-1234567", "utente"),
    			//cognome vuoto
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","", "jojo02","333-1234567", "utente"),
    			//cognome null
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno",null, "jojo02","333-1234567", "utente"),
    			//telefono vuoto
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","Giovanna", "jojo02","", "utente"),
    			//telefono null
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","Giovanna", "jojo02",null, "utente"),
    			//ruolo vuoto
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","Giovanna", "jojo02","333-1234567", ""),
    			//ruolo null
    			Arguments.of(3, "jojoland2@gmail.com", "Giorno","Giovanna", "jojo02","333-1234567", null)
    			);
    }
     
    @Test
	@DisplayName("TCU1_1_5 changePassTestCorretto")
	public void changePassTestCorretto() throws CheckException, DataSetException, DatabaseUnitException, SQLException{
    	Utente utente = new Utente();
    	utente.setId(2);
    	utente.setEmail("orlandoTomeo@gmail.com");
    	try {
			userDao.changePass("jojo03", utente);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	
    	String resultSQL = "SELECT * FROM Utente WHERE email = 'orlandoTomeo@gmail.com'";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
        assertEquals( UserDaoDataSource.toHash("jojo03") , rs.getString("password"));
		c.close(); 	
	}
    
    
    @Test
	@DisplayName("TCU1_1_5 changePassTestEmailVuota")
	public void changePassTesEmailVuota() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente();
    		utente.setEmail("");
			userDao.doSaveUser(utente);
    	});
	}
    	
    
    @Test
	@DisplayName("TCU1_1_5 changePassTestEmailNull")
	public void changePassTesEmailNull() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente();
    		utente.setId(2);
    		utente.setEmail(null);
    		utente.setPassword("jojo03");
			userDao.doSaveUser(utente);
    	});
	}
    
    
    @Test
	@DisplayName("TCU1_1_5 changePassTestPassVuota")
	public void changePassTesPassVuota() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente();
    		utente.setId(2);
    		utente.setEmail("orlandotomeo@gmail.com");
    		utente.setPassword("");
			userDao.doSaveUser(utente);
    	});
	}
    
    @Test
	@DisplayName("TCU1_1_5 changePassTestPassNull")
	public void changePassTestPassNull() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente();
    		utente.setId(2);
    		utente.setEmail("orlandotomeo@gmail.com");
    		utente.setPassword(null);
			userDao.doSaveUser(utente);
    	});
	}
}
