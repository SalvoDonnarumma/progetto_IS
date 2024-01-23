package progetto_IS;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

class IUserDaoTest {
    private static IDatabaseTester tester;
	private DataSource ds;
    private UserDaoDataSource userDaoData;
    
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
	public void doRetrieveAllUsers(){
		ArrayList<Utente> expected = new ArrayList<>();
		expected.add(new Utente(1, "salvatoredonnarumma@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Salvatore","Donnarumma", "320-1234567", "utente", null));
		expected.add(new Utente(2, "orlandotomeo@gmail.com", "d0a845a8304784446b1a261ba3b59e27", "Orlando","Tomeo", "351-1234567", "utente", null));
		
		ArrayList<Utente> actual = null;
		try {
			actual = (ArrayList<Utente>) userDaoData.doRetrieveAllUsers(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
	
	

}
