package unitTesting.gestioneutenti;

import static org.junit.Assert.assertThrows; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import checking.CheckException;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

public class LoginTest {
		
		private static UserDaoDataSource userdao; 
		
			
		@BeforeAll
		public static void init() {
			userdao = Mockito.mock(UserDaoDataSource.class);
		}
		
		@Test
		@DisplayName("TC1.1 loginCorretto") //prendi da TCS
		public void loginCorretto() throws SQLException, CheckException {
		    Utente u = new Utente();
		    u.setEmail("iltoro@gmail.com");
		    u.setPassword("lautaromartinez");
		    Utente oracolo = new Utente(1, "iltoro@gmail.com", "hashedpass", "Lautaro", "Martinez", "331-2089654", "Utente", null);
		    try (MockedStatic<UserDaoDataSource> userDaoStaticMock = Mockito.mockStatic(UserDaoDataSource.class)) {
		        userDaoStaticMock.when(() -> UserDaoDataSource.toHash("lautaromartinez")).thenReturn("hashedpass");
		        Mockito.when(userdao.login(Mockito.any(Utente.class))).thenReturn(oracolo);

		        Utente risultato = userdao.login(u);
		        assertEquals(risultato, oracolo);
		    }
		}
		
		@Test
		@DisplayName("TC1.2 loginEmailNonPresente") 
		public void loginEmailNonPresente() throws SQLException, CheckException {
		    Utente u = new Utente();
		    u.setEmail("iltoro@gmail.com");
		    u.setPassword("lautaromartinez");
		    
		    try (MockedStatic<UserDaoDataSource> userDaoStaticMock = Mockito.mockStatic(UserDaoDataSource.class)) {
		        userDaoStaticMock.when(() -> UserDaoDataSource.toHash("lautaromartinez")).thenReturn("hashedpass");
		        Mockito.when(userdao.login(Mockito.any(Utente.class))).thenReturn(null);

		        Utente risultato = userdao.login(u);
		        assertEquals(risultato, null);
		    }
		}
		@Test
		@DisplayName("TC1.3 passwordNonCorretta") //prendi da TCS
		public void passwordNonCorretta() throws SQLException, CheckException {
		    Utente u = new Utente();
		    u.setEmail("iltoro@gmail.com");
		    u.setPassword("lautaromartinez");

		    try (MockedStatic<UserDaoDataSource> userDaoStaticMock = Mockito.mockStatic(UserDaoDataSource.class)) {
		        userDaoStaticMock.when(() -> UserDaoDataSource.toHash("lautaromartinez")).thenReturn("hashedpass");
		        Mockito.when(userdao.login(Mockito.any(Utente.class))).thenReturn(null);

		        Utente risultato = userdao.login(u);
		        assertEquals(risultato, null);
		    }
		}
		
		@Test
		@DisplayName("TC1.3 loginEmailVuota") //prendi da TCS
		public void loginEmailVuota() throws SQLException, CheckException {
		    Utente u = new Utente();
		    u.setEmail("");
		    u.setPassword("password");

		    try (MockedStatic<UserDaoDataSource> userDaoStaticMock = Mockito.mockStatic(UserDaoDataSource.class)) {
		        userDaoStaticMock.when(() -> UserDaoDataSource.toHash("password")).thenReturn("hashedpass");
		        Mockito.when(userdao.login(Mockito.any(Utente.class))).thenThrow(new CheckException());
		        assertThrows( CheckException.class, ()->{ Utente result = userdao.login(u);} );
		    }
		}
		
		@Test
		@DisplayName("TC1.3 loginPasswordVuota") //prendi da TCS
		public void loginPasswordVuota() throws SQLException, CheckException {
		    Utente u = new Utente();
		    u.setEmail("iltoro@gmail.com");
		    u.setPassword("");
		    try (MockedStatic<UserDaoDataSource> userDaoStaticMock = Mockito.mockStatic(UserDaoDataSource.class)) {
		        userDaoStaticMock.when(() -> UserDaoDataSource.toHash("lautaromartinez")).thenReturn("hashedpass");
		        Mockito.when(userdao.login(Mockito.any(Utente.class))).thenThrow(new CheckException());
		        assertThrows( CheckException.class, ()->{ Utente result = userdao.login(u);} );
		    }
		}
		//
		//Registrazione c'è problema: dobbiamo mettere casi campi formati non validi/-> devo dire a mockito di aprire pagina errore
	}