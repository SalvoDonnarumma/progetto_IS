package progetto_IS;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import checking.CheckException;
import checking.DBException;
import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

class IUserDaoTest {
    private static IDatabaseTester tester;
	private DataSource ds;
    private UserDaoDataSource userDaoData;
    private static String table = "utente"; 
    
    @BeforeAll
    static void setUpAll() throws ClassNotFoundException, SQLException {
    	 // mem indica che il DB deve andare in memoria
        // test indica il nome del DB
        // DB_CLOSE_DELAY=-1 impone ad H2 di eliminare il DB solo quando il processo della JVM termina
        tester = new JdbcDatabaseTester(org.h2.Driver.class.getName(),
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;init=runscript from 'classpath:/db/init/init_schema.sql'",
                "prova",
                ""
        );
        // Refresh permette di svuotare la cache dopo un modifica con setDataSet
        // DeleteAll ci svuota il DB manteneno lo schema
        tester.setSetUpOperation(DatabaseOperation.REFRESH);
        tester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
    }
	    
    private static void refreshDataSet(String filename) {
        try {
            // Usa il class loader corrente invece di UserDaoDataSource.class.getClassLoader()
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(filename);

            if (inputStream == null) {
                throw new FileNotFoundException("File non trovato: " + filename);
            }

            IDataSet initialState = new FlatXmlDataSetBuilder().build(inputStream);
            tester.setDataSet(initialState);
            tester.onSetup();
        } catch (Exception e) {
            e.printStackTrace();
            // Gestisci l'eccezione o lanciala nuovamente per interrompere il test
            throw new RuntimeException("Errore durante l'aggiornamento del DataSet", e);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Prepara lo stato iniziale di default
    	refreshDataSet("db/init/gestioneutenti/UtenteInit.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        userDaoData = new UserDaoDataSource(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestPresente")
	public void doRetrieveByEmailTestPresente() throws CheckException {
		Utente expected = new Utente();
		expected.setId(1);
		expected.setEmail("salvatoredonnarumma@gmail.com");
		expected.setNome("Salvatore");
		expected.setCognome("Donnarumma");
		expected.setPassword("d0a845a8304784446b1a261ba3b59e27");
		expected.setTelefono("320-1234567");
		expected.setRuolo("utente");
		
		Utente actual = new Utente();
		actual.setEmail("salvatoredonnarumma@gmail.com");
		try {
			actual = userDaoData.doRetrieveByEmail(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestNonPresente")
	public void doRetrieveByEmailTestNonPresente() throws CheckException {
		Utente expected = null;	
		Utente actual = new Utente();
		actual.setEmail("salvatoredonnarumma2002@gmail.com");
		try {
			actual = userDaoData.doRetrieveByEmail(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestVuota")
	public void doRetrieveByEmailTestVuoto() throws CheckException {
		Utente actual = new Utente();
		actual.setEmail("");
		assertThrows(CheckException.class, ()->{Utente utente = userDaoData.doRetrieveByEmail(actual);});
	}
	
	@Test
	@DisplayName("TCU1_1_1 doRetrieveByEmailTestNull")
	public void doRetrieveByEmailTestNull() throws CheckException {
		assertThrows(CheckException.class, ()->{Utente utente = userDaoData.doRetrieveByEmail(null);});
	}
	
	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestPresente")
	public void doRetrieveByKeyTestPresente() throws CheckException {
		Utente expected = new Utente();
		expected.setId(1);
		expected.setEmail("salvatoredonnarumma@gmail.com");
		expected.setNome("Salvatore");
		expected.setCognome("Donnarumma");
		expected.setPassword("d0a845a8304784446b1a261ba3b59e27");
		expected.setTelefono("320-1234567");
		expected.setRuolo("utente");
		
		Utente actual = new Utente();
		actual.setId(1);;
		try {
			actual = userDaoData.doRetrieveByKey(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestNonPresente")
	public void doRetrieveByKeyTestNonPresente() throws CheckException {
		Utente expected = null;	
		Utente actual = new Utente();
		actual.setId(0);;
		try {
			actual = userDaoData.doRetrieveByKey(actual);
			System.out.println("Utente ottenuto:"+actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("TCU1_1_2 doRetrieveByKeyTestNull")
	public void doRetrieveBKeyTestNull() throws CheckException {
		assertThrows(CheckException.class, ()->{Utente utente = userDaoData.doRetrieveByKey(null);});
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestNull")
	public void doRetrieveAllUsersTestNull(){
		ArrayList<Utente> expected = new ArrayList<>();
		expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore","Donnarumma", "320-1234567", "utente", null));
		expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando","Tomeo", "351-1234567", "utente", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = (ArrayList<Utente>) userDaoData.doRetrieveAllUsers(null);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestVuoto")
	public void doRetrieveAllUsersTestVuoto(){
		ArrayList<Utente> expected = new ArrayList<>();
		expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore","Donnarumma", "320-1234567", "utente", null));
		expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando","Tomeo", "351-1234567", "utente", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = userDaoData.doRetrieveAllUsers("");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestEmail")
	public void doRetrieveAllUsersTestEmail() throws CheckException{
		ArrayList<Utente> expected = new ArrayList<>();
		expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando","Tomeo", "351-1234567", "utente", null));
		expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore","Donnarumma", "320-1234567", "utente", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = userDaoData.doRetrieveAllUsers("email");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestNome")
	public void doRetrieveAllUsersTestNome() throws CheckException{
		ArrayList<Utente> expected = new ArrayList<>();
		expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando","Tomeo", "351-1234567", "utente", null));
		expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore","Donnarumma", "320-1234567", "utente", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = (ArrayList<Utente>) userDaoData.doRetrieveAllUsers("nome");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCU1_1_3 doRetrieveAllUsersTestOrderNotValid")
	public void doRetrieveAllUsersTestOrder(){
		assertThrows(CheckException.class, ()->{ArrayList<Utente> actual = userDaoData.doRetrieveAllUsers("cognome");});
	}
	
	@Test
	@DisplayName("TCU1_1_4 doSaveUserTestCorretto")
	public void doSaveUserTest() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IUserDaoTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneutenti/doSaveUserCorretto.xml"))
                .getTable(table);
    	Utente utente = new Utente(3, "jojoland@gmail.com", "jojo02", "Giorno","Giovanna", "333-1234567", "utente", null);
    	try {
			userDaoData.doSaveUser(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	ITable actualTable = null;
		try {
			actualTable = tester.getConnection().createDataSet().getTable(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
	}
		
	@ParameterizedTest
    @MethodSource("doSaveTestProvider")
    @DisplayName("TCU1_1_4 doSaveTestNonSalva")
    public void doSaveTestNonSalva(Integer id, String email, String password, String nome, String cognome, String telefono, String ruolo) {
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente(id, email, password, nome, cognome, telefono, ruolo, null);
			userDaoData.doSaveUser(utente);
    	});
    }
    
	@Test
	@DisplayName("doSaveTestNonSalvaEmailPresente")
	public void doSaveTestNonSalvaEmail() {
		//email già presente
    	assertThrows(JdbcSQLIntegrityConstraintViolationException.class, () -> {
    		Utente utente = new Utente(3, "orlandotomeo@gmail.com", "Giorno", "Giovanna", "jojo02", "333-1234567", "utente", null);
			userDaoData.doSaveUser(utente);
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
	public void changePassTestCorretto() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IUserDaoTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneutenti/changePassCorretto.xml"))
                .getTable(table);
    	Utente utente = new Utente();
    	utente.setId(2);
    	utente.setEmail("orlandoTomeo@gmail.com");
    	try {
			userDaoData.changePass("jojo03", utente);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	ITable actualTable = null;
		try {
			actualTable = tester.getConnection().createDataSet().getTable(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
	}
    
    @Test
	@DisplayName("TCU1_1_5 changePassTestEmailVuota")
	public void changePassTesEmailVuota() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, () -> {
    		Utente utente = new Utente();
    		utente.setEmail("");
			userDaoData.doSaveUser(utente);
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
			userDaoData.doSaveUser(utente);
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
			userDaoData.doSaveUser(utente);
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
			userDaoData.doSaveUser(utente);
    	});
	}
}
