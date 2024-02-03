package unitTesting;


import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import checking.CheckException;
import gestioneprodotti.PhotoDaoDataSource;
import gestioneprodotti.Prodotto;



public class PhotoUnitDaoData {

	    private DataSource ds;
	    private Connection connection;
	    private PhotoDaoDataSource photoDaoData;
	    private PreparedStatement preparedStatement;
	    
	    @BeforeEach
	    public void setUp() throws Exception {
	        ds = Mockito.mock(DataSource.class);
	        connection = mock(Connection.class);
	        Mockito.when(ds.getConnection()).thenReturn(connection);
	        preparedStatement = Mockito.mock(PreparedStatement.class);  // Initialize preparedStatement here
	        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	        photoDaoData = new PhotoDaoDataSource(ds);
	    }

	    @Test
	    @DisplayName("updatePhotoTestCorretto")
	    public void updatePhotoTestCorretto() throws SQLException, CheckException {
	        // Preparare i dati del prodotto per il test
	        Prodotto prodotto = new Prodotto(1,"Nome","Descrizione","CAtegoria",1.0,"Stats",null,"path/immagine.jpg");   
	        photoDaoData.updatePhoto(prodotto);
	        
	        Mockito.verify(preparedStatement, times(1)).setString(1, prodotto.getImagePath());
	        Mockito.verify(preparedStatement, times(1)).setInt(2, prodotto.getCode());
	        Mockito.verify(preparedStatement, times(1)).executeUpdate();
	    }
	    @Test
	    @DisplayName("updatePhotoTestConProdottoValido")
	    public void updatePhotoTestConProdottoValido() throws SQLException, CheckException {
	        // Preparare i dati del prodotto per il test
	        Prodotto prodotto = new Prodotto(1, "Nome", "Descrizione", "Categoria", 1.0, "Stats", null, "path/immagine.jpg");

	        // Chiamare il metodo che si sta testando
	        photoDaoData.updatePhoto(prodotto);

	        // Verificare che il metodo abbia interagito correttamente con i mock
	        verify(preparedStatement, times(1)).setString(1, prodotto.getImagePath());
	        verify(preparedStatement, times(1)).setInt(2, prodotto.getCode());
	        verify(preparedStatement, times(1)).executeUpdate();
	    }

	    @Test
	    @DisplayName("updatePhotoTestNull")
	    public void updatePhotoTestConProdottoNullo() {
	        // Chiamare il metodo che si sta testando con un prodotto nullo e verificare che sollevi un'eccezione
	        assertThrows(CheckException.class, () -> photoDaoData.updatePhoto(null));
	    }

}