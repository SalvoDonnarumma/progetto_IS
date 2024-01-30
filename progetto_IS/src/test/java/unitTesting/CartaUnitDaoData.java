package unitTesting;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestionecarta.Carta;
import gestionecarta.CartaDaoDataSource;
import gestioneutenti.UserDaoDataSource;

public class CartaUnitDaoData {
	private DataSource ds;
	private Connection connection;
	private CartaDaoDataSource cartaDaoData;
		
	@BeforeEach
    public void setUp() throws Exception {
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        cartaDaoData = new CartaDaoDataSource(ds);
    }
	
	@Test
	@DisplayName("TCU1_2_1 salvaCartaTestCorretto")
	public void salvaCartaTestCorretto() throws SQLException, CheckException {
		DataSource ds = Mockito.mock(DataSource.class);
	    Connection connection = Mockito.mock(Connection.class);
	    Mockito.when(ds.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
		cartaDaoData = new CartaDaoDataSource(ds);
		Carta carta = new Carta(1, "Giorno Giovanni", "1111-2222-3333-4444", "05/2030");
		cartaDaoData.salvaCarta(carta);
		
		Mockito.verify(preparedStatement, times(1)).setInt(1, carta.getIdCarta());
        Mockito.verify(preparedStatement, times(1)).setString(2, carta.getProprietario());
        Mockito.verify(preparedStatement, times(1)).setString(3, carta.getNumero_carta());
        Mockito.verify(preparedStatement, times(1)).setString(4, carta.getData_scadenza());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	}
}
