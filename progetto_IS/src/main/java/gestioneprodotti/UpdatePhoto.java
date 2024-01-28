package gestioneprodotti;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import checking.CheckException;

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

    		IPhotoDao photoDao = null;
    		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
    		photoDao = new PhotoDaoDataSource(ds);
    		
    		String id = request.getParameter("id");
    		System.out.println("id: "+id);
    		String imagePath = null;
			String tempPath = null;
			for (Part part : request.getParts()) {
    			String fileName = part.getSubmittedFileName();
    			tempPath = "C:/Users/Salvatore/git/progettoIS/progetto_IS/src/main/WebContent/img_products"+ File.separator + fileName;
    			//tempPath = getServletContext().getRealPath("/" +"img_products"+ File.separator + fileName);
    			imagePath = "./img_products/" + fileName;
    			if (fileName != null && !fileName.equals("")) {
    				uploadFile(part.getInputStream(), tempPath);
    			}
    		}
			
			String partedaRimuovere = "target/";
			for (Part part : request.getParts()) {
    			String fileName = part.getSubmittedFileName();
    			tempPath = getServletContext().getRealPath("/" +"img_products"+ File.separator + fileName);
    			uploadFile(part.getInputStream(), tempPath);
    			String realPath = tempPath.replace(partedaRimuovere , "src/main/WebContent/");
    			if (fileName != null && !fileName.equals("")) {
    				uploadFile(part.getInputStream(), realPath);
    			}
    		}
			
			System.out.println("image path: "+imagePath);
			Prodotto modify = new Prodotto();
			modify.setCode(Integer.parseInt(id));
			modify.setImagePath(imagePath);
			
			try {
				photoDao.updatePhoto(modify);
				System.out.println("Inserimento avvenuto");
			} catch (SQLException | CheckException e) {
				e.printStackTrace();
			}
    		response.sendRedirect("admin/ProductView.jsp");
    	}
    	
    	private boolean uploadFile(InputStream is, String path) {
    		boolean test = false;
    		System.out.println("Path: "+path);
    		try(FileOutputStream fops = new FileOutputStream(path);){
    			byte[] byt = new byte[is.available()];
    			is.read(byt);
    	
    			fops.write(byt);
    			fops.flush();

    			test = true;

    		}catch(Exception e){
    			e.printStackTrace();
    		}

    		return test;
    	}

    }
