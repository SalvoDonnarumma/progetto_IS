package progetto_IS;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
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
import gestioneprodotti.PhotoDaoDataSource;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.Utente;

public class IPhotoDaoDataTest {
	private static IDatabaseTester tester;
	private DataSource ds;
    private PhotoDaoDataSource productDaoData;
    private static String table = "prodotto";

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
    	refreshDataSet("db/init/gestioneprodotti/ProdottiInit2.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        productDaoData = new PhotoDaoDataSource(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
    @Test
    @DisplayName("TCU3_2_1 updatePhotoTestCorretto")
    public void updatePhotoTestCorretto() throws DatabaseUnitException {
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IPhotoDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneprodotti/modificaImagePathCorretto.xml"))
                .getTable("prodotto");
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(4);
    	prodotto.setImagePath("imagepmodificata");
    	
    	try {
			productDaoData.updatePhoto(prodotto);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	ITable actualTable = null;
		try {
			actualTable = tester.getConnection().createDataSet().getTable("prodotto");
		} catch (Exception e) {
			e.printStackTrace();
		}
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
    }
    
    
    @ParameterizedTest
    @MethodSource("doSaveTestProviderUpdate")
    @DisplayName("TCU3_2_1 updatePhotoTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void modificaPhotoTestParamNullorVuoto(Integer idprodotto, String imagepath) {
    	assertThrows(CheckException.class, () -> {
    		Prodotto prodotto = new Prodotto();
    		prodotto.setCode(idprodotto);
    		prodotto.setStats(imagepath);
    		productDaoData.updatePhoto(prodotto);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProviderUpdate(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(4, ""),
    			Arguments.of(4, null),
    			
    			Arguments.of(-1,"imagepmodificata")
    			);
    }
    
    @Test
    @DisplayName("TCU3_2_1 updatePhotoTestNull")
    public void updatePhotoTestNull() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ productDaoData.updatePhoto(null);});
	}
    
    @Test
    @DisplayName("TCU3_2_1 updatePhotoTestVuoto")
    public void updatePhotoTestVuoto() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ productDaoData.updatePhoto(new Prodotto());});
	}
    
    @Test
    @DisplayName("TCU3_2_1 updatePhotoTestNonPresente")
    public void updatePhotoTestNonPresente() throws DatabaseUnitException{
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/init/gestioneprodotti/ProdottiInit.xml"))
                .getTable(table);
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(5);   	
    	try {
			productDaoData.updatePhoto(prodotto);
		} catch (SQLException | CheckException e) {
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
    
    
    
}
