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
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.sql.DataSource;

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
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;

public class ProdottoDaoDataTest {
	private Connection conn;
	private DataSource ds;
	private ProductDaoDataSource productDao;
	
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
        productDao = new ProductDaoDataSource(ds);
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
	@DisplayName("TCI doSaveProdottoCorretto")
	public void doSaveProdottoCorretto() throws SQLException{
		Taglie taglie = new Taglie(1, 0,5,4,6);
		Prodotto prodotto = new Prodotto(1, "Nome prodotto", "Descrizione prodotto", "Coltelli",35.00, "statistiche", taglie, "imagepath");
		
		try {
			productDao.doSave(prodotto);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * FROM Prodotto WHERE idprodotto = 1";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next()); //il prodotto è presente nel db, dunque è stato inserito correttamente
		c.close();
		
		String insertSQL = "delete from prodotto where idprodotto = 1";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();		
	}
	
	@ParameterizedTest
    @MethodSource("doSaveTestProvider")
    @DisplayName("TCI salvaProdototoTestParamNullorVuoto")
    public void salvaCartaTestParamNullorVuoto(Integer idprodotto, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath) {
    	assertThrows(CheckException.class, () -> {
    		Prodotto prodotto = new Prodotto( idprodotto, name, descrizione, categoria, price, stats, taglie,imagePath);
    		productDao.doSave(prodotto);
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
	@DisplayName("TCI doRetrieveAllTestCorretto")
	public void doRetrieveAllTestCorretto() {
		ArrayList<Prodotto> listaProdotti = new ArrayList<>();	
		listaProdotti.add(new Prodotto(34, "MK2EVO", "I sub amatoriali che vogliono un erogatore dalle prestazioni affidabili e sono alla ricerca di qualcosa di meglio dei loro erogatori entry-level si innamoreranno immediatamente del sistema di erogazione MK2 EVO/S270. Il secondo stadio S270 è dotato di un design bilanciato pneumaticamente e, grazie al suo alloggio compatto e alla sua aerodinamica interna, offre una grande facilità respiratoria.",
                "Erogatori", 440.0, "Design 2 stadio, Bilanciato\n1. Stufe Art, Bilanciato\n2. Stufe Serie, Serie S\nWasser Temperatura, Freddo, Temperato\nVerbindungsart, INT\nNitrox Mix, Fino al 40%\nHochdruckans, 1\nLP Ports, 4\n1. Stufe Material, Ottone cromato\nProduktgewicht, 3.92 lb\nAnpassbare 2. Stufe, No", new Taglie(), "./img_products/MK2EVO.jpg"));
        listaProdotti.add(new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon",
                "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg"));
        listaProdotti.add(new Prodotto(37, "CHILD2MASK", "La maschera Child 2 a due lenti è la soluzione ideale per i più piccoli. Perfettamente sagomata per adattarsi alla maggior parte dei visi minuti o stretti, ancora più confortevole grazie al morbido facciale in silicone ipoallergenico, è dotata di lenti Ultra Clear per regale una visione nitida e a tutto campo del mondo sottomarino. Le fibbie a strap rendono la maschera facile da regolare.",
                "Maschere", 34.0, "Tipologia Prodotto Marketing - Maschere, Lente doppia\nSkirt Technology, Standard\nLenti ottiche compatibili, No\nMateriali - Maschere, Silicone di alta qualità, Policarbonato\nSkirt Color, Trasparente\nConstruction, Incorniciata\nPeso Prodotto, 0.4 lb\nAttività Snorkeling, Tempo libero", new Taglie(), "./img_products/Child2Mask.jpg"));
        listaProdotti.add(new Prodotto(38, "Definition", "Definition 7 mm, lo steamer con cappuccio, si propone di riempire il vuoto rimasto tra gli steamer tradizionali e le mute semistagne. Si tratta di una muta modernissima con tutte le caratteristiche necessarie per immergersi in acque fredde. Il design, ispirato allo steamer di grande successo Definition 6.5 mm, è realizzato con un neoprene più spesso ed elasticizzato in modo tale da garantire una maggior protezione termica e, al contempo, aumentare la libertà di movimento.",
                "Mute", 449.0, "Wetsuit Type Steamer\nThickness 7 mm\nMaterials Neoprene X-Foam\nProduct Weight (M) 2.53 kg\nActivity Tempo libero, Viaggio\nInterior Lining Pile/peluche\nUPC 404833639451", new Taglie(), "./img_products/DefinistionSteamerUomo.jpg"));
        listaProdotti.add(new Prodotto(39, "DOLPHIN", "Queste versatili pinne sono caratterizzate da un design a tallone aperto per poter essere indossate, per chi lo preferisce, assieme a dei calzari in neoprene. Nonostante ciò, l'alloggiamento per il piede è così morbido e di sostegno che possono tranquillamente essere indossate anche a piedi nudi.",
                "Pinne", 37.0, "Buoyancy Neutra\nTipologia Prodotto Marketing - Pinne, Lama, Scarpetta Aperta\nMateriali - Pinne, Doppio composito\nPeso Prodotto 0.95 lb\nHeel Straps Gomma regolabile\nAttività Snorkeling, Tempo libero, Viaggio", new Taglie(), "./img_products/Dolphin.jpg"));
        listaProdotti.add(new Prodotto(82, "Jawzti", "Il coltello subacqueo Maximus è nato dalle esigenze dei subacquei che richiedevano un coltello con maggiori funzionalità. Le sue caratteristiche e le sue dimensioni lo rendono un compagno subacqueo inseparabile. In acciaio inossidabile 440 e rivestito in PVD",
                "Coltelli", 45.0, "Coltello subacqueo multiuso\n- Lama e impugnatura sono realizzate in acciaio inossidabile 440 (w1.14116) Con rivestimento pvd per prevenire la corrosione in acqua\n- 55/56 hrc\n- Spessore della lama 4 mm\n- Lunghezza totale 230 mm\n- Presenta una punta smussata, cacciaviti, un bordo affilato, uno Seghettato, un apribottiglie e un tagliasagola\n- Robusto fodero in cordura con passante per cintura e laccioli",
                new Taglie(), "./img_products/JAWZTI.jpg"));
        listaProdotti.add(new Prodotto(84, "Sktknife", "Il coltello SKKnife per la pesca subacquea ha una lama di lunghezza ideale per molteplici usi subacquei. La lama è realizzata in acciaio inossidabile 304 e presenta un bordo affilato per affrontare varie situazioni sottomarine. La sua impugnatura ergonomica",
                "Coltelli", 83.0, "Coltello da pesca subacquea\n- Lama in acciaio inossidabile 304\n- Lunghezza della lama 11 cm\n- Bordo affilato\n- Impugnatura ergonomica\n- Fodero in plastica con blocco di sicurezza\n- Attacco per gamba regolabile",
                new Taglie(), "./img_products/SKTKnife.jpg"));
        listaProdotti.add(new Prodotto(105,"Supernova","La scarpetta di nuova concezione presenta vistose perforazioni (che impediscono il cosiddetto \"effetto paracadute\") e le articolazioni nel punto di giunzione con la pala, assorbono l'energia cinetica. La pala, con la sua lamella centrale estremamente larga, assicura un forte effetto di canalizzazione. Disponibili dalla taglia M alla XL.",
        	    "Pinne",159.0,"MATERIALE: Thermoplastic rubber\r\nMODELLO: Open Heel\r\nLUNGHEZZA LAMA: 38cm / 15in (size R)\r\nPESO SINGOLA PINNA: 0.9kg / 2lb (size R)\r\nKIT COLORE: false",
        	    new Taglie(), "./img_products/SeawingSupernova.jpg"));
    
        ArrayList<Prodotto> actual = null;
        
        try {
			actual = productDao.doRetrieveAll(null);
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
        
        for( int i = 0; i<actual.size(); i++) {
        	assertEquals(listaProdotti.get(i).getCode(), actual.get(i).getCode());
        	assertEquals(listaProdotti.get(i).getNome(), actual.get(i).getNome());
        	assertEquals(listaProdotti.get(i).getCategoria(), actual.get(i).getCategoria());
        }
	}
	
	@Test
	@DisplayName("TCI doRetrieveAllTestOrderedByNome")
	public void doRetrieveAllTestOrderedByNome() {
		ArrayList<Prodotto> listaProdotti = new ArrayList<>();	
		listaProdotti.add(new Prodotto(34, "MK2EVO", "I sub amatoriali che vogliono un erogatore dalle prestazioni affidabili e sono alla ricerca di qualcosa di meglio dei loro erogatori entry-level si innamoreranno immediatamente del sistema di erogazione MK2 EVO/S270. Il secondo stadio S270 è dotato di un design bilanciato pneumaticamente e, grazie al suo alloggio compatto e alla sua aerodinamica interna, offre una grande facilità respiratoria.",
                "Erogatori", 440.0, "Design 2 stadio, Bilanciato\n1. Stufe Art, Bilanciato\n2. Stufe Serie, Serie S\nWasser Temperatura, Freddo, Temperato\nVerbindungsart, INT\nNitrox Mix, Fino al 40%\nHochdruckans, 1\nLP Ports, 4\n1. Stufe Material, Ottone cromato\nProduktgewicht, 3.92 lb\nAnpassbare 2. Stufe, No", new Taglie(), "./img_products/MK2EVO.jpg"));
        listaProdotti.add(new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon",
                "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg"));
        listaProdotti.add(new Prodotto(37, "CHILD2MASK", "La maschera Child 2 a due lenti è la soluzione ideale per i più piccoli. Perfettamente sagomata per adattarsi alla maggior parte dei visi minuti o stretti, ancora più confortevole grazie al morbido facciale in silicone ipoallergenico, è dotata di lenti Ultra Clear per regale una visione nitida e a tutto campo del mondo sottomarino. Le fibbie a strap rendono la maschera facile da regolare.",
                "Maschere", 34.0, "Tipologia Prodotto Marketing - Maschere, Lente doppia\nSkirt Technology, Standard\nLenti ottiche compatibili, No\nMateriali - Maschere, Silicone di alta qualità, Policarbonato\nSkirt Color, Trasparente\nConstruction, Incorniciata\nPeso Prodotto, 0.4 lb\nAttività Snorkeling, Tempo libero", new Taglie(), "./img_products/Child2Mask.jpg"));
        listaProdotti.add(new Prodotto(38, "Definition", "Definition 7 mm, lo steamer con cappuccio, si propone di riempire il vuoto rimasto tra gli steamer tradizionali e le mute semistagne. Si tratta di una muta modernissima con tutte le caratteristiche necessarie per immergersi in acque fredde. Il design, ispirato allo steamer di grande successo Definition 6.5 mm, è realizzato con un neoprene più spesso ed elasticizzato in modo tale da garantire una maggior protezione termica e, al contempo, aumentare la libertà di movimento.",
                "Mute", 449.0, "Wetsuit Type Steamer\nThickness 7 mm\nMaterials Neoprene X-Foam\nProduct Weight (M) 2.53 kg\nActivity Tempo libero, Viaggio\nInterior Lining Pile/peluche\nUPC 404833639451", new Taglie(), "./img_products/DefinistionSteamerUomo.jpg"));
        listaProdotti.add(new Prodotto(39, "DOLPHIN", "Queste versatili pinne sono caratterizzate da un design a tallone aperto per poter essere indossate, per chi lo preferisce, assieme a dei calzari in neoprene. Nonostante ciò, l'alloggiamento per il piede è così morbido e di sostegno che possono tranquillamente essere indossate anche a piedi nudi.",
                "Pinne", 37.0, "Buoyancy Neutra\nTipologia Prodotto Marketing - Pinne, Lama, Scarpetta Aperta\nMateriali - Pinne, Doppio composito\nPeso Prodotto 0.95 lb\nHeel Straps Gomma regolabile\nAttività Snorkeling, Tempo libero, Viaggio", new Taglie(), "./img_products/Dolphin.jpg"));
        listaProdotti.add(new Prodotto(82, "Jawzti", "Il coltello subacqueo Maximus è nato dalle esigenze dei subacquei che richiedevano un coltello con maggiori funzionalità. Le sue caratteristiche e le sue dimensioni lo rendono un compagno subacqueo inseparabile. In acciaio inossidabile 440 e rivestito in PVD",
                "Coltelli", 45.0, "Coltello subacqueo multiuso\n- Lama e impugnatura sono realizzate in acciaio inossidabile 440 (w1.14116) Con rivestimento pvd per prevenire la corrosione in acqua\n- 55/56 hrc\n- Spessore della lama 4 mm\n- Lunghezza totale 230 mm\n- Presenta una punta smussata, cacciaviti, un bordo affilato, uno Seghettato, un apribottiglie e un tagliasagola\n- Robusto fodero in cordura con passante per cintura e laccioli",
                new Taglie(), "./img_products/JAWZTI.jpg"));
        listaProdotti.add(new Prodotto(84, "Sktknife", "Il coltello SKKnife per la pesca subacquea ha una lama di lunghezza ideale per molteplici usi subacquei. La lama è realizzata in acciaio inossidabile 304 e presenta un bordo affilato per affrontare varie situazioni sottomarine. La sua impugnatura ergonomica",
                "Coltelli", 83.0, "Coltello da pesca subacquea\n- Lama in acciaio inossidabile 304\n- Lunghezza della lama 11 cm\n- Bordo affilato\n- Impugnatura ergonomica\n- Fodero in plastica con blocco di sicurezza\n- Attacco per gamba regolabile",
                new Taglie(), "./img_products/SKTKnife.jpg"));
        listaProdotti.add(new Prodotto(105,"Supernova","La scarpetta di nuova concezione presenta vistose perforazioni (che impediscono il cosiddetto \"effetto paracadute\") e le articolazioni nel punto di giunzione con la pala, assorbono l'energia cinetica. La pala, con la sua lamella centrale estremamente larga, assicura un forte effetto di canalizzazione. Disponibili dalla taglia M alla XL.",
        	    "Pinne",159.0,"MATERIALE: Thermoplastic rubber\r\nMODELLO: Open Heel\r\nLUNGHEZZA LAMA: 38cm / 15in (size R)\r\nPESO SINGOLA PINNA: 0.9kg / 2lb (size R)\r\nKIT COLORE: false",
        	    new Taglie(), "./img_products/SeawingSupernova.jpg"));
        Collections.sort(listaProdotti, Comparator.comparing(p -> p.getNome().toLowerCase()));

        ArrayList<Prodotto> actual = null;
        
        try {
			actual = productDao.doRetrieveAll("nome");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
        
        for(Prodotto p : actual)
        	System.out.println(p);
        
        System.out.println("-----");
        for(Prodotto p : listaProdotti)
        	System.out.println(p);
        
        for( int i = 0; i<actual.size(); i++) {
        	assertEquals(listaProdotti.get(i).getCode(), actual.get(i).getCode());
        	assertEquals(listaProdotti.get(i).getNome(), actual.get(i).getNome());
        	assertEquals(listaProdotti.get(i).getCategoria(), actual.get(i).getCategoria());
        }
	}
	
	@Test
	@DisplayName("TCI doRetrieveAllTestOrderedByCategoria")
	public void doRetrieveAllTestOrderedByCategoria() {
		ArrayList<Prodotto> listaProdotti = new ArrayList<>();	
		listaProdotti.add(new Prodotto(34, "MK2EVO", "I sub amatoriali che vogliono un erogatore dalle prestazioni affidabili e sono alla ricerca di qualcosa di meglio dei loro erogatori entry-level si innamoreranno immediatamente del sistema di erogazione MK2 EVO/S270. Il secondo stadio S270 è dotato di un design bilanciato pneumaticamente e, grazie al suo alloggio compatto e alla sua aerodinamica interna, offre una grande facilità respiratoria.",
                "Erogatori", 440.0, "Design 2 stadio, Bilanciato\n1. Stufe Art, Bilanciato\n2. Stufe Serie, Serie S\nWasser Temperatura, Freddo, Temperato\nVerbindungsart, INT\nNitrox Mix, Fino al 40%\nHochdruckans, 1\nLP Ports, 4\n1. Stufe Material, Ottone cromato\nProduktgewicht, 3.92 lb\nAnpassbare 2. Stufe, No", new Taglie(), "./img_products/MK2EVO.jpg"));
        listaProdotti.add(new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon",
                "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg"));
        listaProdotti.add(new Prodotto(37, "CHILD2MASK", "La maschera Child 2 a due lenti è la soluzione ideale per i più piccoli. Perfettamente sagomata per adattarsi alla maggior parte dei visi minuti o stretti, ancora più confortevole grazie al morbido facciale in silicone ipoallergenico, è dotata di lenti Ultra Clear per regale una visione nitida e a tutto campo del mondo sottomarino. Le fibbie a strap rendono la maschera facile da regolare.",
                "Maschere", 34.0, "Tipologia Prodotto Marketing - Maschere, Lente doppia\nSkirt Technology, Standard\nLenti ottiche compatibili, No\nMateriali - Maschere, Silicone di alta qualità, Policarbonato\nSkirt Color, Trasparente\nConstruction, Incorniciata\nPeso Prodotto, 0.4 lb\nAttività Snorkeling, Tempo libero", new Taglie(), "./img_products/Child2Mask.jpg"));
        listaProdotti.add(new Prodotto(38, "Definition", "Definition 7 mm, lo steamer con cappuccio, si propone di riempire il vuoto rimasto tra gli steamer tradizionali e le mute semistagne. Si tratta di una muta modernissima con tutte le caratteristiche necessarie per immergersi in acque fredde. Il design, ispirato allo steamer di grande successo Definition 6.5 mm, è realizzato con un neoprene più spesso ed elasticizzato in modo tale da garantire una maggior protezione termica e, al contempo, aumentare la libertà di movimento.",
                "Mute", 449.0, "Wetsuit Type Steamer\nThickness 7 mm\nMaterials Neoprene X-Foam\nProduct Weight (M) 2.53 kg\nActivity Tempo libero, Viaggio\nInterior Lining Pile/peluche\nUPC 404833639451", new Taglie(), "./img_products/DefinistionSteamerUomo.jpg"));
        listaProdotti.add(new Prodotto(39, "DOLPHIN", "Queste versatili pinne sono caratterizzate da un design a tallone aperto per poter essere indossate, per chi lo preferisce, assieme a dei calzari in neoprene. Nonostante ciò, l'alloggiamento per il piede è così morbido e di sostegno che possono tranquillamente essere indossate anche a piedi nudi.",
                "Pinne", 37.0, "Buoyancy Neutra\nTipologia Prodotto Marketing - Pinne, Lama, Scarpetta Aperta\nMateriali - Pinne, Doppio composito\nPeso Prodotto 0.95 lb\nHeel Straps Gomma regolabile\nAttività Snorkeling, Tempo libero, Viaggio", new Taglie(), "./img_products/Dolphin.jpg"));
        listaProdotti.add(new Prodotto(82, "Jawzti", "Il coltello subacqueo Maximus è nato dalle esigenze dei subacquei che richiedevano un coltello con maggiori funzionalità. Le sue caratteristiche e le sue dimensioni lo rendono un compagno subacqueo inseparabile. In acciaio inossidabile 440 e rivestito in PVD",
                "Coltelli", 45.0, "Coltello subacqueo multiuso\n- Lama e impugnatura sono realizzate in acciaio inossidabile 440 (w1.14116) Con rivestimento pvd per prevenire la corrosione in acqua\n- 55/56 hrc\n- Spessore della lama 4 mm\n- Lunghezza totale 230 mm\n- Presenta una punta smussata, cacciaviti, un bordo affilato, uno Seghettato, un apribottiglie e un tagliasagola\n- Robusto fodero in cordura con passante per cintura e laccioli",
                new Taglie(), "./img_products/JAWZTI.jpg"));
        listaProdotti.add(new Prodotto(84, "Sktknife", "Il coltello SKKnife per la pesca subacquea ha una lama di lunghezza ideale per molteplici usi subacquei. La lama è realizzata in acciaio inossidabile 304 e presenta un bordo affilato per affrontare varie situazioni sottomarine. La sua impugnatura ergonomica",
                "Coltelli", 83.0, "Coltello da pesca subacquea\n- Lama in acciaio inossidabile 304\n- Lunghezza della lama 11 cm\n- Bordo affilato\n- Impugnatura ergonomica\n- Fodero in plastica con blocco di sicurezza\n- Attacco per gamba regolabile",
                new Taglie(), "./img_products/SKTKnife.jpg"));
        listaProdotti.add(new Prodotto(105,"Supernova","La scarpetta di nuova concezione presenta vistose perforazioni (che impediscono il cosiddetto \"effetto paracadute\") e le articolazioni nel punto di giunzione con la pala, assorbono l'energia cinetica. La pala, con la sua lamella centrale estremamente larga, assicura un forte effetto di canalizzazione. Disponibili dalla taglia M alla XL.",
        	    "Pinne",159.0,"MATERIALE: Thermoplastic rubber\r\nMODELLO: Open Heel\r\nLUNGHEZZA LAMA: 38cm / 15in (size R)\r\nPESO SINGOLA PINNA: 0.9kg / 2lb (size R)\r\nKIT COLORE: false",
        	    new Taglie(), "./img_products/SeawingSupernova.jpg"));
        Collections.sort(listaProdotti, Comparator.comparing(Prodotto::getCategoria));
        
        ArrayList<Prodotto> actual = null;
        
        try {
			actual = productDao.doRetrieveAll("categoria");
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
        
        for( int i = 0; i<actual.size(); i++) {
        	assertEquals(listaProdotti.get(i).getCode(), actual.get(i).getCode());
        	assertEquals(listaProdotti.get(i).getNome(), actual.get(i).getNome());
        	assertEquals(listaProdotti.get(i).getCategoria(), actual.get(i).getCategoria());
        }
	}
	
	@Test
	@DisplayName("TCI doDeleteProdottoTestCorretto")
	public void doDeleteProdottoTestCorretto() throws SQLException {
		Prodotto prodotto = new Prodotto();
		prodotto.setCode(105);
			
    	try {
			productDao.doDelete(prodotto);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	String resultSQL = "SELECT * FROM prodotto WHERE idprodotto = 105";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(!rs.next());
		c.close();
		
		String insertSQL = "INSERT INTO prodotto (idProdotto, categoria, nome, price, descrizione, stats, image)\r\n"
				+ "VALUES (105, 'Pinne', 'Supernova', 159.0, 'La scarpetta di nuova concezione presenta vistose perforazioni (che impediscono il cosiddetto \\\\\"effetto paracadute\\\\\") e le articolazioni nel punto di giunzione con la pala, assorbono l\\'energia cinetica. La pala, con la sua lamella centrale estremamente larga, assicura un forte effetto di canalizzazione. Disponibili dalla taglia M alla XL.', 'MATERIALE: Thermoplastic rubber\\r\\nMODELLO: Open Heel\\r\\nLUNGHEZZA LAMA: 38cm / 15in (size R)\\r\\nPESO SINGOLA PINNA: 0.9kg / 2lb (size R)\\r\\nKIT COLORE: false', './img_products/SeawingSupernova.jpg');\r\n"
				+ "";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI doDeleteProdottoNonPresente")
	public void doDeleteProdottoTestNonPresente() throws SQLException {
		Prodotto prodotto = new Prodotto();
		prodotto.setCode(1);
		 
		boolean res = false;
    	try {
			res = productDao.doDelete(prodotto);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CheckException e) {
			e.printStackTrace();
		}
    	
    	assertTrue(!res);    	
	}
	
	@Test
	@DisplayName("TCI setTaglieTestCorretto")
	public void setTaglieProdottoTestCorretto() throws SQLException{	
		String insertSQL = "delete from taglie where idprodotto = 105";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(insertSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();	
		
		Taglie taglie = new Taglie(105, 0,5,4,6);
		
		try {
			productDao.setSizesByKey(105, taglie);;
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * FROM Taglie WHERE idprodotto = 105";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		c.close();	
	}
	
	@Test
	@DisplayName("TCI getTaglieProdottoTestCorretto")
	public void getTaglieProdottoTestCorretto() throws SQLException, CheckException{	
		Taglie excepted = new Taglie(34, 2,1,3,2);
		Prodotto p = new Prodotto();
		p.setCode(34);
		
		Taglie actual = null;
		try {
			actual = productDao.getSizesByKey(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(excepted, actual);
	}
	
	@Test
	@DisplayName("TCI getTaglieProdottoTestPresente")
	public void getTaglieProdottoTestNonPresente() throws SQLException, CheckException{	
		Taglie excepted = new Taglie();
		Prodotto p = new Prodotto();
		p.setCode(110);
		
		Taglie actual = null;
		try {
			actual = productDao.getSizesByKey(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(excepted, actual);
	}
	
	@Test
	@DisplayName("TCI doRetrieveByKeyTestCorretto")
	public void doRetrieveByKeyTestCorretto() throws SQLException, CheckException{	
		Prodotto excepted = new Prodotto(35, "ASCIUTTOBLU", "Compatibile Con Sistemi Di Tenuta Guanti Oberon", "Guanti", 65.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg");
		Prodotto actual = new Prodotto();
		actual.setCode(35);

		try {
			actual = productDao.doRetrieveByKey(actual);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(excepted, actual);
	}
	
	@Test
	@DisplayName("TCI doRetrieveByKeyTestNonPresente")
	public void doRetrieveByKeyTestNonPresente() throws SQLException, CheckException{	
		Prodotto p= new Prodotto();
		p.setCode(106);

		Prodotto actual = null;
		try {
			actual = productDao.doRetrieveByKey(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(-1, actual.getCode());
	}
	
	@Test
	@DisplayName("TCI updateProdottoTestCorretto")
	public void updateProdottoTestCorretto() throws SQLException, CheckException{	
		Prodotto product = new Prodotto(35, "ASCIUTTOBLU2", "Compatibile Con Sistemi Di Tenuta Guanti Oberon", "Guanti", 35.0, "Peso Prodotto 0.507 lb\nAttività Tecnico", new Taglie(), "./img_products/ASCIUTTOBLU.jpg");
		try {
			productDao.doUpdate(35, product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * FROM prodotto WHERE idprodotto = 35";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		assertEquals("ASCIUTTOBLU2",rs.getString("nome"));
		assertEquals(35, rs.getDouble("price"));
		c.close();
		
		String updateSQL = "UPDATE prodotto\r\n"
				+ "SET \r\n"
				+ "    nome = 'ASCIUTTOBLU',\r\n"
				+ "    descrizione = 'Compatibile Con Sistemi Di Tenuta Guanti Oberon',\r\n"
				+ "    categoria = 'Guanti',\r\n"
				+ "    price = 65.0,\r\n"
				+ "    stats = 'Peso Prodotto 0.507 lb\\nAttività Tecnico',\r\n"
				+ "    image = './img_products/ASCIUTTOBLU.jpg'\r\n"
				+ "WHERE idProdotto = 35;\r\n"
				+ "";
		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(updateSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@Test
	@DisplayName("TCI updateProdottoTestCorretto")
	public void updateTaglieTestCorretto() throws SQLException, CheckException{	
		Taglie taglie = new Taglie(35, 1,1,1,1);
		
		try {
			productDao.doUpdateSizes(35, taglie);;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resultSQL = "SELECT * FROM taglie WHERE idprodotto = 35";
		Connection c = newConnection();
		PreparedStatement ps = c.prepareStatement(resultSQL);
		ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("tagliaM"));
		assertEquals(1,rs.getInt("tagliaL"));
		assertEquals(1,rs.getInt("tagliaXL"));
		assertEquals(1,rs.getInt("tagliaXXL"));
		c.close();
		
		String updateSQL = "UPDATE taglie\r\n"
		        + "SET \r\n"
		        + "    tagliaM = '0',\r\n"
		        + "    tagliaL = '0',\r\n"
		        + "    tagliaXL = '0',\r\n"
		        + "    tagliaXXL = '2'\r\n"
		        + "WHERE idProdotto = 35;\r\n";

		Connection c1 = newConnection();
		PreparedStatement ps1 = c1.prepareStatement(updateSQL);
		int rs1 = ps1.executeUpdate();
		c1.close();
	}
	
	@ParameterizedTest
    @MethodSource("updateTestProvider")
    @DisplayName("TCI updateProdottoTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void modificaProdottoTestParamNullorVuoto(Integer idprodotto, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath) {
    	assertThrows(CheckException.class, () -> {
    		Prodotto prodotto = new Prodotto( idprodotto, name, descrizione, categoria, price, stats, taglie,imagePath);
    		productDao.doUpdate(prodotto.getCode(), prodotto);
    	});
    }

    private static Stream<Arguments> updateTestProvider(){
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
    @DisplayName("TCI updateProdottoTestNull")
    public void  updateProdottoTestNull() {
    	assertThrows(CheckException.class, () -> {
    		productDao.doUpdate(34, null);
    	});
    }
    
    @Test
    @DisplayName("TCI updateProdottoTestIdNonValido")
    public void  updateProdottoTestVuoto() {
    	assertThrows(CheckException.class, () -> {
    		Prodotto p = new Prodotto();
    		productDao.doUpdate(-1, p);
    	});
    }
	
	@ParameterizedTest
    @MethodSource("doSaveTestProviderUpdateSizes")
    @DisplayName("TCI setTaglieTestParamNullorVuoto")
    //int code, String name, String descrizione, String categoria, Double price, String stats, Taglie taglie, String imagePath
    public void updateSizesTestParamNullorVuoto(Integer idprodotto, Integer tagliaM, Integer tagliaL, Integer tagliaXL, Integer tagliaXXL) {
    	assertThrows(CheckException.class, () -> {
    		Taglie taglie = new Taglie(idprodotto, tagliaM, tagliaL, tagliaXL, tagliaXXL);
    		productDao.doUpdateSizes(idprodotto, taglie);
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
}
