package unitTesting;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import checking.CheckException;
import gestionecarrello.Carrello;
import gestionecarrello.CarrelloDaoDataSource;
import gestioneprodotti.Prodotto;
import gestioneutenti.Utente;

public class CarrelloUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private CarrelloDaoDataSource carrelloDaoData;
	private PreparedStatement preparedStatement;

	@BeforeEach
	public void setUp() throws Exception {
		ds = Mockito.mock(DataSource.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		carrelloDaoData = new CarrelloDaoDataSource(ds);
	}

	@Test
	@DisplayName("TCU salvaCarrelloTestCorretto")
	public void salvaCarrelloTestCorretto() throws SQLException, CheckException {
		Prodotto prod1 = Mockito.mock(Prodotto.class);
		Prodotto prod2 = Mockito.mock(Prodotto.class);
		Prodotto prod3 = Mockito.mock(Prodotto.class);
		Carrello carrello = new Carrello();
		carrello.addProduct(prod1);
		carrello.addProduct(prod2);
		carrello.addProduct(prod3);
		carrello.setIdcarrello(1);
		when(prod1.getCode()).thenReturn(1);
		when(prod2.getCode()).thenReturn(2);
		when(prod3.getCode()).thenReturn(3);
		carrelloDaoData.salvaCarrello(carrello);

		Mockito.verify(preparedStatement, times(3)).setInt(1, carrello.getIdcarrello());
		Mockito.verify(preparedStatement, times(3)).setInt(2, prod1.getCode());
		Mockito.verify(preparedStatement, times(3)).executeUpdate();
	}

	@Test
	@DisplayName("TCU salvaCarrelloTestNull")
	public void salvaCarrelloTestNull() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.salvaCarrello(null);
		});
	}

	@Test
	@DisplayName("TCU salvaCarrelloTestMinoreZero")
	public void salvaCarrelloTestVuoto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.salvaCarrello(new Carrello());
		});
	}

	@Test
	@DisplayName("TCU salvaCarrelloTestProdottiFallito")
	public void salvaCarrelloTestProdottiFallito() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(ds.getConnection()).thenReturn(connection);
		PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Carrello carrello = new Carrello();
		Prodotto prodottoConCodiceNegativo = Mockito.mock(Prodotto.class);
		Mockito.when(prodottoConCodiceNegativo.getCode()).thenReturn(-1);
		carrello.addProduct(prodottoConCodiceNegativo);

		assertThrows(CheckException.class, () -> {
			carrelloDaoData.salvaCarrello(carrello);
		});
	}

	@Test
	@DisplayName("TCU eliminaCarrelloSuccesso")
	public void eliminaCarrelloSuccessoTest() throws SQLException, CheckException {
		Carrello carrello = new Carrello();
		carrello.setIdcarrello(1);

		carrelloDaoData.eliminaCarrello(carrello);

		Mockito.verify(ds, times(1)).getConnection();
		Mockito.verify(connection, times(1)).prepareStatement(anyString());
		verify(preparedStatement, times(1)).setInt(1, carrello.getIdcarrello());
		verify(preparedStatement, times(1)).executeUpdate();
		verify(preparedStatement, times(1)).close();
		verify(connection, times(1)).close();
	}

	@Test
	@DisplayName("TCU eliminaCarrelloTestCarrelloNull")
	public void eliminaCarrelloTestCarrelloNull() {
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.eliminaCarrello(null);
		});
	}
	
	@Test
	@DisplayName("TCU eliminaCarrelloTestCarrelloNull")
	public void eliminaCarrelloTestCarrelloVuoto() {
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.eliminaCarrello(new Carrello());
		});
	}

	@Test
	@DisplayName("TCU recuperaCarrelloTestCarrelloNull")
	public void recuperaCarrelloTestCarrelloNull() {
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.recuperaCarrello(null);
		});
	}

	@Test
	@DisplayName("TCU recuperaCarrelloTestVuoto")
	public void recuperaCarrelloTestIdNegativo() {
		assertThrows(CheckException.class, () -> {
			carrelloDaoData.recuperaCarrello(new Utente());
		});
	}
}
