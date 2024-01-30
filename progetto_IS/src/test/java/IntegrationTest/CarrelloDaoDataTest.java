package IntegrationTest;

import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.dbunit.Assertion;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestioneprodotti.Prodotto;

public class CarrelloDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private CarrelloDaoDataSource carrelloDaoData;
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
        carrelloDaoData = new CarrelloDaoDataSource(ds);
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
	@DisplayName("TCU1_2_1 salvaCarrelloTestCorretto")
	public void salvaCarrelloTestCorretto() throws CheckException, DataSetException, SQLException{
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
    	
    	String resultSQL = "SELECT * FROM Carrello WHERE idcarrello = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
	}
}
