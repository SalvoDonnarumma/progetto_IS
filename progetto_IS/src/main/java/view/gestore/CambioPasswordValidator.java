package view.gestore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import checking.CheckException;
import gestionegestore.GestoreDaoDataSource;
import gestionegestore.IGestoreDao;
import gestioneutenti.IUserDao;
import gestioneutenti.UserDaoDataSource;
import gestioneutenti.Utente;

/**
 * Servlet implementation class CambioPasswordValidator
 */
@WebServlet("/CambioPasswordAdminValidator")
public class CambioPasswordValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambioPasswordValidator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IGestoreDao gestoreDao = null;
		IUserDao userDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		gestoreDao = new GestoreDaoDataSource(ds);
		userDao = new UserDaoDataSource(ds);
		
		try {
			String oldPass = request.getParameter("currentPassword");
			String newPass = request.getParameter("newPassword");
			String confPass = request.getParameter("confirmPassword");
			String email = request.getParameter("email");
			System.out.println("Email: "+email);
			System.out.println("Pass1: "+newPass);
			System.out.println("Pass2: "+confPass);
			System.out.println("CurrentPass: "+oldPass);
			Utente bean = new Utente();
			bean.setEmail(email);
			
			List<String> errors = new ArrayList<>();
		    RequestDispatcher dispatcherChangePassPage = request.getRequestDispatcher("admin/changepassadmin.jsp");

		    if( !newPass.equals(confPass) ) {
				errors.add("La password nuova e la password di conferma non corrispondono!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
		    Utente old = userDao.doRetrieveByEmail(bean);
		    bean.setPassword(oldPass);
			if( !gestoreDao.validateOldPassword(bean, old)) {
				errors.add("La vecchia password inserita non &egrave; valida!");
				request.setAttribute("errors", errors);
				dispatcherChangePassPage.forward(request, response);
				return;
			}
					
			if( newPass.length()<12 ) {
				dispatcherChangePassPage.forward(request, response);
				return;
			}		
		} catch (SQLException | CheckException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = null;	
		dispatcher = getServletContext().getRequestDispatcher("/cambioPasswordAdmin");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
