package IntegrationTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.junit.jupiter.api.BeforeAll;
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
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.Utente;

public class CarrelloDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private CarrelloDaoDataSource carrelloDaoData;
    
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
	
	public static Connection newConnection() throws SQLException {
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
	@DisplayName("TCI salvaCarrelloTestCorretto")
	public void salvaCarrelloTestCorretto() throws CheckException, DataSetException, SQLException{
		Prodotto prod1 = new Prodotto(34, "MK2EVO", "I sub amatoriali che vogliono un erogatore dalle prestazioni affidabili e sono alla ricerca di qualcosa di meglio dei loro erogatori entry-level si innamoreranno immediatamente del sistema di erogazione MK2 EVO/S270. Il secondo stadio S270 è dotato di un design bilanciato pneumaticamente e, grazie al suo alloggio compatto e alla sua aerodinamica interna, offre una grande facilità respiratoria.",
                "Erogatori", 440.0, "Design 2 stadio, Bilanciato\n1. Stufe Art, Bilanciato\n2. Stufe Serie, Serie S\nWasser Temperatura, Freddo, Temperato\nVerbindungsart, INT\nNitrox Mix, Fino al 40%\nHochdruckans, 1\nLP Ports, 4\n1. Stufe Material, Ottone cromato\nProduktgewicht, 3.92 lb\nAnpassbare 2. Stufe, No", new Taglie(), "./img_products/MK2EVO.jpg");
        Prodotto prod2 = new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon",
                "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg");
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(2);
    	carrello.addProduct(prod1);
    	carrello.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(carrello.getIdcarrello());
    	try {
			carrelloDaoData.salvaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String deleteSQL = "DELETE FROM prodottocarrello WHERE idcarrello = 2";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(deleteSQL);
		ps1.executeUpdate();
		c.close();
	}
	
	@Test
	@DisplayName("TCI salvaCarrelloTestNull")
	public void salvaCarrelloTestNull() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.salvaCarrello(null);});
	}
	
	@Test
	@DisplayName("TCI salvaCarrelloTestVuoto")
	public void salvaCarrelloTestVuoto() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.salvaCarrello(new Carrello());});
	}
	
	@Test
	@DisplayName("TCI salvaCarrelloTestPresente")
	public void salvaCarrelloTestPresente() throws CheckException, DataSetException, SQLException{
		Prodotto prod1 = new Prodotto(34, "MK2EVO", "I sub amatoriali che vogliono un erogatore dalle prestazioni affidabili e sono alla ricerca di qualcosa di meglio dei loro erogatori entry-level si innamoreranno immediatamente del sistema di erogazione MK2 EVO/S270. Il secondo stadio S270 è dotato di un design bilanciato pneumaticamente e, grazie al suo alloggio compatto e alla sua aerodinamica interna, offre una grande facilità respiratoria.",
                "Erogatori", 440.0, "Design 2 stadio, Bilanciato\n1. Stufe Art, Bilanciato\n2. Stufe Serie, Serie S\nWasser Temperatura, Freddo, Temperato\nVerbindungsart, INT\nNitrox Mix, Fino al 40%\nHochdruckans, 1\nLP Ports, 4\n1. Stufe Material, Ottone cromato\nProduktgewicht, 3.92 lb\nAnpassbare 2. Stufe, No", new Taglie(), "./img_products/MK2EVO.jpg");
        Prodotto prod2 = new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon",
                "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg");
    	
    	ArrayList<Prodotto> list = new ArrayList<>();
    	list.add(prod1);
    	list.add(prod2);
    	
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(2);
    	carrello.addProduct(prod1);
    	carrello.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(carrello.getIdcarrello());
    	
    	try {
			carrelloDaoData.salvaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 2";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();
		
		String deleteSQL = "DELETE FROM prodottocarrello WHERE idcarrello = 2";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(deleteSQL);
		ps1.executeUpdate();
		c.close();
	}
	
	@Test
	@DisplayName("TCI recuperaCarrelloTestCorretto")
	public void recuperaCarrelloTestCorretto() throws CheckException, DataSetException, SQLException{
    	Prodotto prod1 = new Prodotto(38, "Definition", "Definition 7 mm, lo steamer con cappuccio, si propone di riempire il vuoto rimasto tra gli steamer tradizionali e le mute semistagne. Si tratta di una muta modernissima con tutte le caratteristiche necessarie per immergersi in acque fredde. Il design, ispirato allo steamer di grande successo Definition 6.5 mm, è realizzato con un neoprene più spesso ed elasticizzato in modo tale da garantire una maggior protezione termica e, al contempo, aumentare la libertà di movimento.",
                "Mute", 449.0, "Wetsuit Type Steamer\nThickness 7 mm\nMaterials Neoprene X-Foam\nProduct Weight (M) 2.53 kg\nActivity Tempo libero, Viaggio\nInterior Lining Pile/peluche\nUPC 404833639451", new Taglie(), "./img_products/DefinistionSteamerUomo.jpg");
    	Prodotto prod2 = new Prodotto(39, "DOLPHIN", "Queste versatili pinne sono caratterizzate da un design a tallone aperto per poter essere indossate, per chi lo preferisce, assieme a dei calzari in neoprene. Nonostante ciò, l'alloggiamento per il piede è così morbido e di sostegno che possono tranquillamente essere indossate anche a piedi nudi.",
                "Pinne", 37.0, "Buoyancy Neutra\nTipologia Prodotto Marketing - Pinne, Lama, Scarpetta Aperta\nMateriali - Pinne, Doppio composito\nPeso Prodotto 0.95 lb\nHeel Straps Gomma regolabile\nAttività Snorkeling, Tempo libero, Viaggio", new Taglie(), "./img_products/Dolphin.jpg");
    	
    	Carrello expected = new Carrello();
    	expected.setIdcarrello(1);
    	expected.addProduct(prod1);
    	expected.addProduct(prod2);

    	Utente u = new Utente();
    	u.setId(1);
    	
    	Carrello actual = null;
    	try {
    		Mockito.when(ds.getConnection()).thenReturn(newConnection());
			actual = carrelloDaoData.recuperaCarrello(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
  //  	assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("TCI recuperaCarrelloTestVuoto")
	public void recuperaCarrelloTestVuoto() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.recuperaCarrello(new Utente());});
	}
	
	@Test
	@DisplayName("TCI salvaCarrelloTestVuoto")
	public void recuperaCarrelloTestNull() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.recuperaCarrello(null);});
	}
	
	@Test
	@DisplayName("TCI recuperaCarrelloTestNonPresente")
	public void recuperaCarrelloTestNonPresente() throws CheckException, DataSetException, SQLException{	
    	Carrello expected = new Carrello();
    	expected.setIdcarrello(3);

    	Utente u = new Utente();
    	u.setId(expected.getIdcarrello());
    	
    	Carrello actual = null;
    	try {
			actual = carrelloDaoData.recuperaCarrello(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	assertEquals(new Carrello(), actual);
	}

	@Test
	@DisplayName("TCI cancellaCartaTestCorretto")
	public void cancellaCartaCorrettoTest() throws CheckException, DataSetException, SQLException{
    	Carrello carrello = new Carrello();
    	carrello.setIdcarrello(1);
    	try {
			carrelloDaoData.eliminaCarrello(carrello);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodottocarrello WHERE idcarrello = 1";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
		c.close();
		
		String insertSQL = "INSERT INTO prodottocarrello (idprodottoc, idcarrello) VALUES (38,1);";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
		
		insertSQL = "INSERT INTO prodottocarrello (idprodottoc, idcarrello) VALUES (39,1);";
		c1 = newConnection();
		ps1 = c1.prepareStatement(insertSQL);
		rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI cancellaCarrelloTestNull")
	public void cancellaCarrelloTestNull() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.eliminaCarrello(null);});
	}
	
	@Test
	@DisplayName("TCI salvaCarrelloTestVuoto")
	public void cancellaCarrelloTestVuoto() {
		assertThrows(CheckException.class, ()->{carrelloDaoData.eliminaCarrello(new Carrello());});
	}
	
}
