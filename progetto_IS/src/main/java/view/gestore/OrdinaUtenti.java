package view.gestore;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import storagelayer.GestoreDaoDataSource;
import storagelayer.IUserDao;
import storagelayer.UserDaoDataSource;

/**
 * Servlet implementation class OrdinaUtenti
 */
@WebServlet("/OrdinaUtenti")
public class OrdinaUtenti extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrdinaUtenti() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		GestoreDaoDataSource adminDao = new GestoreDaoDataSource(ds);
		
		String sort = request.getParameter("sort");
		try {
			request.removeAttribute("users");
			request.setAttribute("users", adminDao.doRetrieveUtentiSorted(sort));
		
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/admin/UserView.jsp");
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
