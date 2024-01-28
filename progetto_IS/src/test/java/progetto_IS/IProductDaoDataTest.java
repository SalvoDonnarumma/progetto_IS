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
import gestionecarta.Carta;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.Utente;

public class IProductDaoDataTest {
	private static IDatabaseTester tester;
	private DataSource ds;
    private ProductDaoDataSource productDaoData;
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
    	refreshDataSet("db/init/gestioneprodotti/ProdottiInit.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        productDaoData = new ProductDaoDataSource(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
    @Test
    @DisplayName("TCU3_1_1 salvaProdottoCorretto")
    public void salvaProdottoTestCorretto() throws CheckException, DatabaseUnitException, SQLException {
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneprodotti/salvaProdottoCorretto.xml"))
                .getTable(table);
    	//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    	Taglie taglie = new Taglie(5, 3, 1, 1, 3);
    	Prodotto prodotto = new Prodotto(5, "K2Donna", "descrizione", "mute", 330.0, "stats", taglie, "imagep");
    	try {
            productDaoData.doSave(prodotto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per segnalare che il test ha avuto un errore
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
	@DisplayName("TCU3_1_1 salvaProdottoTestPresente")
	public void salvaProdottoPresente() {
		//prodotto già presente
    	assertThrows(JdbcSQLIntegrityConstraintViolationException.class, () -> {
    		Taglie taglie = new Taglie(5, 3, 1, 1, 3);
        	Prodotto prodotto = new Prodotto(4, "K2Donna", "descrizione", "mute", 330.0, "stats", taglie, "imagep");
    		productDaoData.doSave(prodotto);
    	});
	}
    
    @ParameterizedTest
    @MethodSource("doSaveTestProvider")
    @DisplayName("TCU3_1_1 salvaProdottoTestParamNullorVuoto")
  //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void salvaCartaTestParamNullorVuoto(Integer idprodotto, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath) {
    	assertThrows(CheckException.class, () -> {
    		Prodotto prodotto = new Prodotto( idprodotto, name, descrizione, categoria, price, stats, taglie,imagePath);
    		productDaoData.doSave(prodotto);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProvider(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(5, "", "descrizione", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, null, "descrizione", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(5, "K2Donna", "", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, "K2Donna", null, "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(5, "K2Donna", "descrizione", "", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, "K2Donna", "descrizione", null, 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(5, "K2Donna", "descrizione", "mute", 330.0, "", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, "K2Donna", "descrizione", "mute", 330.0, null, new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, "K2Donna", "descrizione", "mute", -33.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(5, "K2Donna", "descrizione", "mute", 330.0, "stats", null, "imagep")
    			);
    }
    
    @Test
    @DisplayName("TCU3_1_2 doRetrieveByKey()")
    public void doRetrieveByKeyTest() throws CheckException {
    	Taglie taglie = new Taglie();
    	Prodotto expected = new Prodotto(4, "K2Uomo", "descrizione", "mute", 330.0, "stats", taglie, "imagep");

    	Prodotto actual = new Prodotto();
    	actual.setCode(4);
    	try {
			actual = productDaoData.doRetrieveByKey(actual);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	assertEquals(expected, actual);
    }
    
    @Test
    @DisplayName("TCU3_1_2 doRetrieveByKeyTestVuoto()")
    public void doRetrieveByKeyTestVuoto() throws SQLException, CheckException {
    	assertThrows(CheckException.class, ()->{ productDaoData.doRetrieveByKey(new Prodotto());});
    }
    
    @Test
    @DisplayName("TCU3_1_2 doRetrieveByKeyNull()")
    public void doRetrieveByKeyTestNull() {
    	assertThrows(CheckException.class, ()->{ productDaoData.doRetrieveByKey(null);});
    }
    
    @Test
	@DisplayName("TCU1_1_3 doRetrieveAllProductsTestNull")
	public void doRetrieveAllUsersTestNull(){
		ArrayList<Prodotto> expected = new ArrayList<>();
		Taglie taglie = new Taglie();
		expected.add(new Prodotto(1, "Bcknife", "descrizione", "coltelli", 30.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(2, "MK2Evo", "descrizione", "erogatori", 160.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(3, "R105", "descrizione", "erogatori", 130.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(4, "K2Uomo", "descrizione", "mute", 330.0, "stats", taglie, "imagep"));
		
		ArrayList<Prodotto> actual = null;
		try {
			actual = (ArrayList<Prodotto>) productDaoData.doRetrieveAll(null);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		try {
			for(Prodotto p : actual) {
				Taglie taglie_a = productDaoData.getSizesByKey(p);
				p.setTaglie(taglie_a);
			}
			
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}
    
    @Test
	@DisplayName("TCU1_1_3 doRetrieveAllProductsTestCategoria")
	public void doRetrieveAllUsersTestCategoria(){
		ArrayList<Prodotto> expected = new ArrayList<>();
		Taglie taglie = new Taglie();
		expected.add(new Prodotto(1, "Bcknife", "descrizione", "coltelli", 30.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(2, "MK2Evo", "descrizione", "erogatori", 160.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(3, "R105", "descrizione", "erogatori", 130.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(4, "K2Uomo", "descrizione", "mute", 330.0, "stats", taglie, "imagep"));
		
		ArrayList<Prodotto> actual = null;
		try {
			actual = (ArrayList<Prodotto>) productDaoData.doRetrieveAll("categoria");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		assertEquals(expected, actual);
	}
    
    @Test
	@DisplayName("TCU1_1_3 doRetrieveAllProductsTestNome")
	public void doRetrieveAllUsersTestNome(){
		ArrayList<Prodotto> expected = new ArrayList<>();
		Taglie taglie = new Taglie();
		expected.add(new Prodotto(1, "Bcknife", "descrizione", "coltelli", 30.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(4, "K2Uomo", "descrizione", "mute", 330.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(2, "MK2Evo", "descrizione", "erogatori", 160.0, "stats", taglie, "imagep"));
		
		taglie = new Taglie();
		expected.add(new Prodotto(3, "R105", "descrizione", "erogatori", 130.0, "stats", taglie, "imagep"));
		
		ArrayList<Prodotto> actual = null;
		try {
			actual = (ArrayList<Prodotto>) productDaoData.doRetrieveAll("nome");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		assertEquals(expected, actual);
	}
    
    @Test
	@DisplayName("TCU1_1_4 doDeleteTestCorretto")
	public void doDeleteTestCorretto() throws DatabaseUnitException{
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneprodotti/cancellaProdottoCorretto.xml"))
                .getTable(table);
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(4);   	
    	try {
			productDaoData.doDelete(prodotto);
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
    
    @Test
    @DisplayName("TCU1_1_4 doDeleteTestNull")
    public void doDeleteTestNull() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ productDaoData.doDelete(null);});
	}
    
    @Test
    @DisplayName("TCU1_1_4 doDeleteTestVuoto")
    public void doDeleteTestVuoto() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ productDaoData.doDelete(new Prodotto());});
	}
    
    @Test
    @DisplayName("TCU1_1_4 doDeleteTestNonPresente")
    public void doDeleteTestNonPresente() throws DatabaseUnitException{
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/init/gestioneprodotti/ProdottiInit.xml"))
                .getTable(table);
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(5);   	
    	try {
			productDaoData.doDelete(prodotto);
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
    
    @Test
    @DisplayName("TCU3_1_5 modificaProdottoCorretto")
    public void modificaProdottoTestCorretto() throws CheckException, DatabaseUnitException, SQLException {
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneprodotti/salvaProdottoCorretto.xml"))
                .getTable(table);
    	//int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    	Prodotto prodotto = new Prodotto(4, "K2Uomo", "descrizioneModificata", "mute", 360.0, "stats", new Taglie(), "imagep");
    	try {
            productDaoData.doUpdate(prodotto.getCode(), prodotto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per segnalare che il test ha avuto un errore
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
    @DisplayName("TCU3_1_5 modificaProdottoTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void modificaProdottoTestParamNullorVuoto(Integer idprodotto, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath) {
    	assertThrows(CheckException.class, () -> {
    		Prodotto prodotto = new Prodotto( idprodotto, name, descrizione, categoria, price, stats, taglie,imagePath);
    		productDaoData.doUpdate(prodotto.getCode(), prodotto);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProviderUpdate(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(4, "", "descrizione", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, null, "descrizione", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(4, "", "", "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, "", null, "mute", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(4, "K2Uomo", "descrizione", "", 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, "K2Uomo", "descrizione", null, 330.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			
    			Arguments.of(4, "K2Uomo", "descrizione", "mute", 330.0, "", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, "K2uomo", "descrizione", "mute", 330.0, null, new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, "K2uomo", "descrizione", "mute", -33.0, "stats", new Taglie(5, 3, 1, 1, 3), "imagep"),
    			Arguments.of(4, "K2uomo", "descrizione", "mute", 330.0, "stats", null, "imagep")
    			);
    }
    
    @Test
    @DisplayName("TCU1_1_6 setTaglieTestCorretto")
    public void setTaglieTestCorretto() throws DatabaseUnitException{
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestioneprodotti/modificaTaglieCorretto.xml"))
                .getTable("Taglie");
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(1);  
    	Taglie taglie = new Taglie(1, 0,0,3,3);
    	try {
			productDaoData.doUpdateSizes(prodotto.getCode(), taglie);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	ITable actualTable = null;
		try {
			actualTable = tester.getConnection().createDataSet().getTable("Taglie");
		} catch (Exception e) {
			e.printStackTrace();
		}
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
	}
    
    @Test
    @DisplayName("TCU1_1_6 setTaglieTestNonPresente")
    public void setTaglieTestNonPresente() throws DatabaseUnitException{
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(IProductDaoDataTest.class.getClassLoader().getResourceAsStream("db/init/gestioneprodotti/ProdottiInit.xml"))
                .getTable("taglie");
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(5);  
    	Taglie taglie = new Taglie(1,0,0,3,3);
    	try {
			productDaoData.doUpdateSizes(prodotto.getCode(), taglie);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	ITable actualTable = null;
		try {
			actualTable = tester.getConnection().createDataSet().getTable("taglie");
		} catch (Exception e) {
			e.printStackTrace();
		}
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
	}
    
    @ParameterizedTest
    @MethodSource("doSaveTestProviderUpdateSizes")
    @DisplayName("TCU3_1_6 setTaglieTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void modificaSizesTestParamNullorVuoto(Integer idprodotto, Integer tagliaM, Integer tagliaL, Integer tagliaXL, Integer tagliaXXL) {
    	assertThrows(CheckException.class, () -> {
    		Taglie taglie = new Taglie(idprodotto, tagliaM, tagliaL, tagliaXL, tagliaXXL);
    		productDaoData.doUpdateSizes(idprodotto, taglie);
    	});
    }
	
    private static Stream<Arguments> doSaveTestProviderUpdateSizes(){
    	return Stream.of(
    			//formato nome propietario non corretto
    			Arguments.of(0, -1, 0, 3, 4),
    			
    			Arguments.of(1, -1, 0, 3, 4),
    			
    			Arguments.of(1, 1,-1, 3, 4),
    			
    			Arguments.of(1, 1, 0, -3, 4),
    			
    			Arguments.of(1, -1, 0, 3, -4)
    			);
    }
    
    @Test
    @DisplayName("TCU3_1_7 getSizesTestCorretto()")
    public void getSizesTestCorretto() throws CheckException {
    	Taglie expected = new Taglie(1, 0,1,3,0);
    	Prodotto p = new Prodotto();
    	p.setCode(1);
    	Taglie actual = null;
    	try {
			actual = productDaoData.getSizesByKey(p);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	assertEquals(expected, actual);
    }
    
    @Test
    @DisplayName("TCU3_1_7 getSizesTestNull()")
    public void getSizesTestNull() throws CheckException {
    	Prodotto p = new Prodotto();
    	p.setCode(1);
    	assertThrows(CheckException.class, () -> {
    		productDaoData.getSizesByKey(null);
    	});
    }
    
    @Test
    @DisplayName("TCU3_1_7 getSizesTestVuoto()")
    public void getSizesTestVuoto() throws CheckException {
    	Prodotto p = new Prodotto();
    	assertThrows(CheckException.class, () -> {
    		productDaoData.getSizesByKey(p);
    	});
    }
    
    @Test
    @DisplayName("TCU3_1_7 getSizesTestNonPresente()")
    public void getSizesTestNonPresente() throws CheckException {
    	Taglie taglie = new Taglie();
    	Prodotto p = new Prodotto();
    	p.setCode(7);
    	Taglie actual = null;
    	try {
			actual = productDaoData.getSizesByKey(p);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	assertEquals(taglie, actual);
    }
    
}
