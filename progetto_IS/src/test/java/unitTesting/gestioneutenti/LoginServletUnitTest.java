package unitTesting.gestioneutenti;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import checking.CheckException;
import gestioneutenti.Login;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;
import test.LoginServletTest;


public class LoginServletUnitTest {

	private LoginServletTest servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private UserDaoDataSource userDao;
    private RequestDispatcher requestDispatcher;

    @Test
    @DisplayName("TCU1.1 loginCorretto")
    public void testLoginCorrect() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
         
        Utente mockUser = new Utente();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("hashedPassword");
        mockUser.setRuolo("Utente");
        
        /* 
        servlet = new Login();
        MockitoAnnotations.initMocks(this);
        DataSource dataSourceMock = mock(DataSource.class);
        ServletContext servletContextMock = mock(ServletContext.class);
        when(servlet.getServletConfig().getServletContext()).thenReturn(servletContextMock);
        when(servletContextMock.getAttribute("DataSource")).thenReturn(dataSourceMock);
        Utente oracolo = new Utente(1, "iltoro@gmail.com", "hashedpass", "Lautaro", "Martinez", "331-2089654", "Utente", null);
        when(userDao.login(mockUser)).thenReturn(oracolo);
        */
        
        //Eseguo la Servlet
        servlet.doPost(request, response);

        // Verifica che la sessione sia stata impostata correttamente
        verify(session).setAttribute("isAdmin", "Utente");
        verify(session).setAttribute("logged", mockUser);

        // Verifica il reindirizzamento
        verify(response).sendRedirect("store.jsp");
    }
    
    @Test
    @DisplayName("TCU1.1 emailNonPresente")
    public void testLoginEmailNonCorretta() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPostNonCorretta(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
    
    @Test
    @DisplayName("TCU1.3 passwordErrata")
    public void testLoginPasswordNonCorretta() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPostNonCorretta(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
    
    
    @Test
    @DisplayName("TCU1.4 emailNull")
    public void testLoginEmailNull() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPost(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
    
    @Test
    @DisplayName("TCU1.4 passwordNull")
    public void testLoginPasswordNull() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("prova@gmail.com");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPost(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
    
    @Test
    @DisplayName("TCU1.4 emailVuoto")
    public void testLoginEmailVuoto() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPost(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
    
    @Test
    @DisplayName("TCU1.4 passwordVuoto")
    public void testLoginPasswordVuoto() throws ServletException, IOException, SQLException, CheckException {
    	servlet = new LoginServletTest();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDaoDataSource.class);
        requestDispatcher = mock(RequestDispatcher.class);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("prova@gmail.com");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("jspName")).thenReturn("loginutenti");
        when(request.getRequestDispatcher(anyString())).thenReturn((requestDispatcher));
       
        //eseguo la servlet 
        servlet.doPost(request, response);

        // Verifica che gli errori siano stati impostati correttamente nell'attributo della richiesta
        verify(request).setAttribute(eq("errors"), anyList());
        // Esempio di verifica degli argomenti di forward
        verify(requestDispatcher).forward(eq(request), eq(response));
    }
}
