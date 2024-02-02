package IntegrationTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*; 

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.dataset.DataSetException;
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
import gestionecarta.CartaDaoDataSource;
import gestioneutenti.Utente;

class CartaDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private CartaDaoDataSource cartaDaoData; 
    
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
        cartaDaoData = new CartaDaoDataSource(ds);
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
	@DisplayName("TCU1_2_1 salvaCartaTestCorretto")
	public void salvaCartaTestCorretto() throws CheckException, DataSetException, SQLException{
    	Carta carta = new Carta(2, "Giorno Giovanna", "1111-2222-3333-4444", "05/2029");
    	try {
			cartaDaoData.salvaCarta(carta);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	String resultSQL = "SELECT * FROM carta WHERE idcarta = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
	}
    
    
    @Test
	@DisplayName("TCU1_2_1 salvaCartaTestPresente")
	public void salvaCartaTestPresente() throws CheckException, DataSetException, SQLException{
    	Carta carta = new Carta(2, "Giorno Giovanni", "1111-2222-3333-4444", "05/2030");
    	Utente utente = new Utente();
    	utente.setId(carta.getIdCarta());
    	boolean flag = true;
    	try {
			if( cartaDaoData.recuperaCarta(utente) != null )
				flag = true;
			else
				flag = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	try {
    		if(flag)
    			cartaDaoData.cancellaCarta(carta);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	try {
			cartaDaoData.salvaCarta(carta);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	  	
    	String resultSQL = "SELECT * FROM Carta WHERE idcarta = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
        assertEquals( "Giorno Giovanni" , rs.getString("proprietario"));
        assertEquals( "1111-2222-3333-4444" , rs.getString("numero_carta"));
        assertEquals( "05/2030" , rs.getString("data_scadenza"));
		c.close(); 
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
	public void cancellaCartaCorrettoTest() throws CheckException, DataSetException, SQLException{
    	Carta carta = new Carta(2, "Giorno Giovanni", "1111-2222-3333-4444", "05/2030");
    	try {
			cartaDaoData.cancellaCarta(carta);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM Carta WHERE idcarta = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
	}
    
    @Test
	@DisplayName("TCU1_2_2 cancellaCartaTestNull")
	public void cancellaCartaTestNull() throws CheckException, DataSetException{
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
    	
    	Utente utente = Mockito.mock(Utente.class);
    	Mockito.when(utente.getId()).thenReturn(1);
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
    }
}
