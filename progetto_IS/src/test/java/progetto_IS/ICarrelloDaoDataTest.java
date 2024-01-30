package progetto_IS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
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
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarta.Carta;
import gestioneprodotti.Prodotto;
import gestioneutenti.Utente;


class ICarrelloDaoDataTest {
    private static IDatabaseTester tester;
	private DataSource ds;
    private CarrelloDaoDataSource carrelloDaoData;
    private static String table = "prodottocarrello"; 
    
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
    	refreshDataSet("db/init/gestionecarrello/CarrelloInit.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        carrelloDaoData = new CarrelloDaoDataSource(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
    @Test
	@DisplayName("TCU4_1_1 salvaCarrelloTestCorretto")
	public void salvaCarrelloTestCorretto() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICarrelloDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarrello/salvaCarrelloCorretto.xml"))
                .getTable(table);
    	
    	Prodotto prod1 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(2);
    	
    	Prodotto prod2 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(3);
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = Mockito.mock(Carrello.class);
    	Mockito.when(carrello.getAllProduct()).thenReturn(list);
    	Mockito.when(carrello.getIdcarrello()).thenReturn(4);

    	try {
			carrelloDaoData.salvaCarrello(carrello);
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
	@DisplayName("TCU4_1_1 salvaCarrelloTestPresente")
	public void salvaCarrelloTestPresente() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICarrelloDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarrello/salvaCarrelloCorretto.xml"))
                .getTable(table);
    	
    	Prodotto prod1 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(2);
    	
    	Prodotto prod2 = Mockito.mock(Prodotto.class);
    	Mockito.when(prod1.getCode()).thenReturn(3);
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = Mockito.mock(Carrello.class);
    	Mockito.when(carrello.getAllProduct()).thenReturn(list);

    	try {
			carrelloDaoData.salvaCarrello(carrello);
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
    @MethodSource("doSaveTestProviderUpdate")
    @DisplayName("TCU4_1_1 salvaCarrelloTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void modificaPhotoTestParamNullorVuoto(Integer idcarrello, Integer idprodottoc) {
    	assertThrows(CheckException.class, () -> {
    		Carrello carrello = new Carrello();
    		carrello.setIdcarrello(idcarrello);
    		Prodotto prodotto = new Prodotto();
    		prodotto.setCode(idprodottoc);
    		carrello.addProduct(prodotto);
    		
    		carrelloDaoData.salvaCarrello(carrello);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProviderUpdate(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(-1, 3),
    			Arguments.of(4, -1)
    			);
    }
   
    @Test
    @DisplayName("TCU4_1_2 cancellaCarrelloTestCorretto")
    public void cancellaCarrelloTestCorretto() throws DatabaseUnitException {
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICartaDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarrello/cancellaCarrelloCorretto.xml"))
                .getTable(table);
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(3);
    	try {
			carrelloDaoData.eliminaCarrello(carrello);
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
	@DisplayName("TCU4_1_2 cancellaCarrelloTestNull")
	public void cancellaCarrelloTestNull() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ carrelloDaoData.eliminaCarrello(null);});
	}
    
    @Test
	@DisplayName("TCU4_1_2 cancellaCarrelloTestVuoto")
	public void cancellaCarrelloTestVuoto() throws CheckException{
    	assertThrows(CheckException.class, ()->{ carrelloDaoData.eliminaCarrello(new Carrello());});
	}
    
    @Test
    @DisplayName("TCU4_1_3 recuperaCarrelloTestCorretto")
    public void recuperaCarrelloTestCorretto() {
    	Carrello expected = new Carrello();
    	expected.setIdcarrello(1);
    	Prodotto prod_ex = new Prodotto();
    	prod_ex.setCode(1);
    	expected.addProduct(prod_ex);
    	
   
    	Utente utente = new Utente();
    	utente.setId(1);
    	
    	Carrello actual = null;
    	try {
			actual = carrelloDaoData.recuperaCarrello(utente);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	assertEquals(expected, actual);
    }
    	
    @Test
	@DisplayName("TCU4_1_3 recuperaCarrelloTestNull")
	public void recuperaCartaTestNull() throws CheckException{
    	assertThrows(CheckException.class, ()->{ carrelloDaoData.recuperaCarrello(null);});
	}
    
    @Test
	@DisplayName("TCU4_1_3 recuperaCarrelloTestVuoto")
	public void recuperaCartaTestVuoto() throws CheckException{
    	assertThrows(CheckException.class, ()->{ carrelloDaoData.recuperaCarrello(new Utente());});
	}
    
    @Test
    @DisplayName("TCU4_1_3 recuperaCartaTestNonPresente")
    public void recuperaCartaTestNonPresente() throws CheckException {
    	Utente utente = new Utente(5, "","","","","","", null);
    	Carrello actual = null;
    	try {
			actual = carrelloDaoData.recuperaCarrello(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	assertEquals(new Carrello(), actual);
    }
}

