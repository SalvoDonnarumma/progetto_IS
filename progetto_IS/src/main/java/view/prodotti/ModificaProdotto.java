package view.prodotti;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import gestioneprodotti.Prodotto;
import gestioneprodotti.ProdottoValidator;
import gestioneprodotti.Taglie;
import storagelayer.IPhotoDao;
import storagelayer.IProductDao;
import storagelayer.PhotoDaoDataSource;
import storagelayer.ProductDaoDataSource;

/**
 * Servlet implementation class ProductControl
 */
@WebServlet("/modificaProdotto")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class ModificaProdotto extends HttpServlet {
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
					
					RequestDispatcher dispatcher = null;
					dispatcher = getServletContext().getRequestDispatcher("/admin/errorpagemodifyproduct.jsp");
					
					if( nome == null) 
						dispatcher.forward(request, response);
					
					if( price != null) {
						if( !ProdottoValidator.isValidPrice(price.toString()) ) {
							System.out.println("Campo prezzo non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					
					if( price != null) {
						if( !ProdottoValidator.isValidPrice(price.toString()) ) {
							System.out.println("Campo prezzo non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					
					if( categoria != null) {
						if( !ProdottoValidator.isValidCategoria(categoria) ) {
							System.out.println("Campo categoria non valido");
							dispatcher.forward(request, response);
						}	 
					} else {
						System.out.println("Campo categoria vuoto");	
						dispatcher.forward(request, response);
					}
					
					
					Integer quantityM = Integer.parseInt(request.getParameter("tagliaM"));
					if( quantityM != null) {
						if( !ProdottoValidator.isValidQuantity(quantityM.toString())) {
							System.out.println("Campo qnt M non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					Integer quantityL = Integer.parseInt(request.getParameter("tagliaL"));
					if( quantityL != null) {
						if( !ProdottoValidator.isValidQuantity(quantityL.toString())) {
							System.out.println("Campo qnt L non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					Integer quantityXL = Integer.parseInt(request.getParameter("tagliaXL"));
					if( quantityXL != null) {
						if( !ProdottoValidator.isValidQuantity(quantityXL.toString())) {
							System.out.println("Campo qnt XL non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					Integer quantityXXL = Integer.parseInt(request.getParameter("tagliaXXL"));
					if( quantityXXL != null) {
						if( !ProdottoValidator.isValidQuantity(quantityXXL.toString())) {
							System.out.println("Campo qnt XXL non valido");
							dispatcher.forward(request, response);
						}	 
					} 
					
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
					
					IPhotoDao photoDao = null;
		    		photoDao = new PhotoDaoDataSource(ds);
					
					String stats = request.getParameter("stats");
					Prodotto bean = new Prodotto();
					bean.setNome(nome);
					bean.setDescrizione(descrizione);
					bean.setCategoria(categoria);
					bean.setStats(stats);
					bean.setPrice(Double.parseDouble(price));
					bean.setImagePath(imagePath);
					int code = Integer.parseInt(request.getParameter("id"));
					bean.setCode(code);
					productDao.doUpdate(code, bean);
					
					
					Taglie sizes = new Taglie();			
					sizes.setQuantitaM(quantityM);
					sizes.setQuantitaL(quantityL);
					sizes.setQuantitaXL(quantityXL);
					sizes.setQuantitaXXL(quantityXXL);
					bean.setTaglie(sizes);
					productDao.doUpdateSizes(code, sizes);
					photoDao.updatePhoto(bean);
					
					dispatcher = null;	
					dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");	
					dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
