package progetto_IS;

import static org.junit.jupiter.api.Assertions.*; 

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
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
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestioneutenti.Utente;

class ICartaDaoDataTest {
    private static IDatabaseTester tester;
	private DataSource ds;
    private CartaDaoDataSource cartaDaoData;
    private static String table = "carta"; 
    
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
    	refreshDataSet("db/init/gestionecarta/CartaInit.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        cartaDaoData = new CartaDaoDataSource(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
    @Test
	@DisplayName("TCU1_2_1 salvaCartaTestCorretto")
	public void salvaCartaTest() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICartaDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarta/salvaCartaCorretto.xml"))
                .getTable(table);
    	Carta carta = new Carta(2, "Tomeo Orlando", "1111-2222-3333-4444", "05/2027");
    	try {
			cartaDaoData.salvaCarta(carta);
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
    @DisplayName("TCU1_2_2 salvaCartaTestNull")
	public void salvaCartaNull() throws CheckException {
		assertThrows(CheckException.class, ()->{ Boolean flag = cartaDaoData.salvaCarta(null);});
	}
    
    @ParameterizedTest
    @MethodSource("doSaveTestProvider")
    @DisplayName("TCU1_2_1 salvaCartaTestParamNullorVuoto")
    public void salvaCartaTestParamNullorVuoto(Integer idcarta, String proprietario, String numero_carta, String data_scadenza) {
    	assertThrows(CheckException.class, () -> {
    		Carta carta = new Carta(idcarta, proprietario, numero_carta, data_scadenza);
    		cartaDaoData.salvaCarta(carta);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProvider(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(3, "", "1111-2222-3333-4444", "06/2026"),
    			//formato nome propietario null
    			Arguments.of(3, null, "1111-2222-3333-4444", "06/2026"),
    			//numero carta non valido
    			Arguments.of(3, "Giorno Giovanna", "1111-2222-3333-44e4", "06/2026"),
    			//numero carta vuoto
    			Arguments.of(3, "Giorno Giovanna", "", "06/2026"),
    			//numero carta null
    			Arguments.of(3, "Giorno Giovanna", null, "06/2026"),
    			//data non valida
    			Arguments.of(3, "Giorno Giovanna", "1111-2222-3333-4444", "06/2023"),
    			//data vuota
    			Arguments.of(3, "Giorno Giovanna", "1111-2222-3333-44e4", ""),
    			//data null
    			Arguments.of(3, "Giorno Giovanna", "1111-2222-3333-44e4", null)
    			);
    }
    
    public void cancellaCarta() {
    	
    }
    	
}
