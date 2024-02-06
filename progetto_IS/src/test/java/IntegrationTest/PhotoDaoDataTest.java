package IntegrationTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class PhotoDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private PhotoDaoDataSource photoDaoData;

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
        photoDaoData = new PhotoDaoDataSource(ds);
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
    @DisplayName("TCI updatePhotoTestCorretto")
    public void updatePhotoTestCorretto() throws DatabaseUnitException, SQLException {
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(34);
    	prodotto.setImagePath("imagepmodificata");
    	
    	try {
			photoDaoData.updatePhoto(prodotto);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
 
    	String resultSQL = "SELECT * FROM prodotto WHERE idprodotto = 34";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
        assertEquals( "imagepmodificata" , rs.getString("image"));
		c.close();
		
		String updateSQL = "UPDATE prodotto SET image = './img_products/MK2EVO.jpg' WHERE idprodotto IN (34)";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(updateSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
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
    		photoDaoData.updatePhoto(prodotto);
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
    @DisplayName("TCI updatePhotoTestNull")
    public void updatePhotoTestNull() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ photoDaoData.updatePhoto(null);});
	}
    
    @Test
    @DisplayName("TCI updatePhotoTestVuoto")
    public void updatePhotoTestVuoto() throws DatabaseUnitException{
    	assertThrows(CheckException.class, ()->{ photoDaoData.updatePhoto(new Prodotto());});
	}
    
    @Test
    @DisplayName("TCI updatePhotoTestNonPresente")
    public void updatePhotoTestNonPresente() throws DatabaseUnitException, SQLException{
    	Prodotto prodotto = new Prodotto();
    	prodotto.setCode(5);   	
    	try {
			photoDaoData.updatePhoto(prodotto);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
    	String resultSQL = "SELECT * FROM prodotto WHERE idprodotto = 5";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
		c.close();
	}    
    
}
