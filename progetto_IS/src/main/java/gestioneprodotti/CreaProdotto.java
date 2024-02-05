package gestioneprodotti;
import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import checking.CheckException;

import javax.servlet.annotation.MultipartConfig;

/**
 * Servlet implementation class ProductControl
 */
@WebServlet("/creaProdotto")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class CreaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		IProductDao productDao = null;
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		productDao = new ProductDaoDataSource(ds);
		try { 
			String categoria = request.getParameter("categoria");
			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			String price = request.getParameter("price");
			System.out.println("Categoria: "+categoria);
			System.out.println("price: "+price);
			System.out.println("nome: "+nome);
			System.out.println("des: "+descrizione);
			
			RequestDispatcher dispatcher = null;
			dispatcher = getServletContext().getRequestDispatcher("/admin/errorpagemodifyproduct.jsp");
			
			Integer quantityM = Integer.parseInt(request.getParameter("tagliaM"));
			Integer quantityL = Integer.parseInt(request.getParameter("tagliaL"));
			Integer quantityXL = Integer.parseInt(request.getParameter("tagliaXL"));
			Integer quantityXXL = Integer.parseInt(request.getParameter("tagliaXXL"));
			String stats = request.getParameter("stats");
		
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
			
			Prodotto bean = new Prodotto();
			bean.setNome(nome);
			bean.setDescrizione(descrizione);
			bean.setCategoria(categoria);
			bean.setStats(stats);
			bean.setPrice(Double.parseDouble(price));
			bean.setImagePath(imagePath);
			try {
				productDao.doSave(bean);
			} catch (CheckException e) {
				e.printStackTrace();
				dispatcher.forward(request, response);
				return;
			}
			Taglie sizes = new Taglie();			
			int code = productDao.doRetrieveByName(bean);
			sizes.setQuantitaM(quantityM);
			sizes.setQuantitaL(quantityL);
			sizes.setQuantitaXL(quantityXL);
			sizes.setQuantitaXXL(quantityXXL);
			bean.setTaglie(sizes);
			productDao.setSizesByKey(code, sizes);												
		} catch (SQLException | CheckException e) {
			//Devo eliminare il prodtto inserito senza le taglie
			try {
				productDao.doDelete(productDao.getLast());
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (CheckException e1) {
				e1.printStackTrace();
			}
			
			RequestDispatcher dispatcherToErrorPage = getServletContext().getRequestDispatcher("/admin/errorpagemodifyproduct.jsp");
			e.printStackTrace();
			dispatcherToErrorPage.forward(request, response);
			return;
		}
		
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");					
		dispatcher.forward(request, response);
	}
	 
	private boolean uploadFile(InputStream is, String path) {
		boolean test = false;
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
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static final String DIRECTORY = "/Users/Salvatore/git/progettoIS/progetto_IS/src/main/WebContent/img_products"
			+ "";
}
