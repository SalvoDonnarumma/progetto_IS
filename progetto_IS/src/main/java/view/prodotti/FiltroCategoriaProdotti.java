package view.prodotti;

import java.io.IOException; 
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

@WebServlet("/CategoriaServlet")
public class FiltroCategoriaProdotti extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = null;
		Gson json = new Gson();
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		ArrayList<String> categorie = new ArrayList<>();
		try {
			connection =ds.getConnection();
	
			PrintWriter out = response.getWriter();

			Statement s = connection.createStatement();
			String query = "SELECT categoria FROM prodotto";
			ResultSet rs = s.executeQuery(query);
			
			/* Aggiunta categorie all'arraylist */
			while (rs.next()) {
				String c = rs.getString("nome");
				categorie.add(c);
			}

			Collections.sort(categorie);
			out.write(json.toJson(categorie));
			connection.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}