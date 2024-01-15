package view.prodotti;

import java.io.IOException; 
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import gestioneprodotti.PhotoDaoDataSource;

/**
 * Servlet implementation class UpdatePhoto
 */
@WebServlet("/UpdatePhoto")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
    public class UpdatePhoto extends HttpServlet {
    	private static final long serialVersionUID = 1L;

    	public UpdatePhoto() {
    		super();
    	}

    	protected void doGet(HttpServletRequest request, HttpServletResponse response)
    			throws ServletException, IOException {
    		PrintWriter out = response.getWriter();
    		response.setContentType("text/plain");

    		out.write("Error: GET method is used but POST method is required");
    		out.close();
    	}

    	protected void doPost(HttpServletRequest request, HttpServletResponse response)
    			throws ServletException, IOException {

    		String id = request.getParameter("id");

    		for (Part part : request.getParts()) {
    			String fileName = part.getSubmittedFileName();
    			if (fileName != null && !fileName.equals("")) {
    				try {
						PhotoDaoDataSource photoDaoDataSource = new PhotoDaoDataSource();
						photoDaoDataSource.updatePhoto(id, part.getInputStream());
    				} catch (SQLException sqlException) {
    					sqlException.printStackTrace();
    				}
    			}
    		}

    		response.sendRedirect("admin/ProductView.jsp");
    	}

    }
