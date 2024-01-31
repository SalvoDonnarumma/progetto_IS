package progetto_IS;

import static org.junit.jupiter.api.Assertions.*; 

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
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
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

class ICartaDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private CartaDaoDataSource cartaDao;
    private static String table = "carta"; 
    
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
        cartaDao = new CartaDaoDataSource(ds);
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
    
	/*
    @Test
	@DisplayName("TCU1_2_1 salvaCartaTestCorretto")
	public void salvaCartaTestCorretto() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICartaDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarta/salvaCartaCorretto.xml"))
                .getTable(table);
    	Carta carta = new Carta(3, "Giorno Giovanna", "1111-2222-3333-4444", "05/2029");
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
    
    /*
    @Test
	@DisplayName("TCU1_2_1 salvaCartaTestPresente")
	public void salvaCartaTestPresente() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICartaDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarta/salvaCartaCorretto.xml"))
                .getTable(table);
    	Carta carta = new Carta(2, "Giorno Giovanna", "1111-2222-3333-4444", "05/2029");
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
    @DisplayName("TCU1_2_1 salvaCartaTestNull")
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
    
    @Test
	@DisplayName("TCU1_2_2 cancellaCartaTestCorretto")
	public void cancellaCartaCorrettoTest() throws CheckException, DataSetException, DatabaseUnitException{
		ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(ICartaDaoDataTest.class.getClassLoader().getResourceAsStream("db/expected/gestionecarta/cancellaCartaCorretto.xml"))
                .getTable(table);
    	Carta carta = new Carta();
    	carta.setIdCarta(2);
    	try {
			cartaDaoData.cancellaCarta(carta);
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
	@DisplayName("TCU1_2_2 cancellaCartaTestNull")
	public void cancellaCartaTestNull() throws CheckException, DataSetException, DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ cartaDaoData.cancellaCarta(null);});
	}
    
    @Test
	@DisplayName("TCU1_2_2 cancellaCartaTestVuoto")
	public void cancellaCartaTestVuoto() throws CheckException{
    	Carta carta = new Carta();
    	assertThrows(CheckException.class, ()->{ cartaDaoData.cancellaCarta(carta);});
	}
    
    @Test
    @DisplayName("TCU1_2_3 recuperaCartaTestCorretto")
    public void recuperaCartaTestCorretto() {
    	Carta expected = new Carta(1, "Donnarumma Salvatore","1111-2222-3333-4444","03/2028");
    	
    	Utente utente = new Utente(1, "","","","","","", null);
    	Carta actual = null;
    	try {
			actual = cartaDaoData.recuperaCarta(utente);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	assertEquals(expected, actual);
    }
    	
    @Test
	@DisplayName("TCU1_2_3 recuperaCartaTestNull")
	public void recuperaCartaTestNull() throws CheckException{
    	assertThrows(CheckException.class, ()->{ cartaDaoData.recuperaCarta(null);});
	}
    
    @Test
	@DisplayName("TCU1_2_3 recuperaCartaTestVuoto")
	public void recuperaCartaTestVuoto() throws CheckException{
    	assertThrows(CheckException.class, ()->{ cartaDaoData.recuperaCarta(new Utente());});
	}
    
    @Test
    @DisplayName("TCU1_2_3 recuperaCartaTestNonPresente")
    public void recuperaCartaTestNonPresente() throws CheckException {
    	Utente utente = new Utente(3, "","","","","","", null);
    	Carta actual = null;
    	try {
			actual = cartaDaoData.recuperaCarta(utente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	assertEquals(null, actual);
    }*/
}