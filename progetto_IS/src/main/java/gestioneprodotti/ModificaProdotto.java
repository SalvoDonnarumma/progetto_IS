package gestioneprodotti;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
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

import checking.CheckException;

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
								
					Integer quantityM = Integer.parseInt(request.getParameter("tagliaM")); 
					Integer quantityL = Integer.parseInt(request.getParameter("tagliaL"));
					Integer quantityXL = Integer.parseInt(request.getParameter("tagliaXL"));
					Integer quantityXXL = Integer.parseInt(request.getParameter("tagliaXXL"));
					
					String imagePath = null;
					String tempPath = null;
					Collection<Part> parts = request.getParts();
					boolean fileupload = true;
					
					if (!parts.isEmpty()) {
						for (Part part : parts) {
							if(part.getSize()>0) {
								String fileName = part.getSubmittedFileName();
								tempPath = "C:/Users/Salvatore/git/progettoIS/progetto_IS/src/main/WebContent/img_products"+ File.separator + fileName;
								//tempPath = getServletContext().getRealPath("/" +"img_products"+ File.separator + fileName);
								imagePath = "./img_products/" + fileName;
								if (fileName != null && !fileName.equals("")) {
									uploadFile(part.getInputStream(), tempPath);
								}
							} else {
								fileupload = false;
							}
						}
						String partedaRimuovere = "target/";
						if( fileupload ) {
							for (Part part : parts) {
								String fileName = part.getSubmittedFileName();
								tempPath = getServletContext().getRealPath("/" +"img_products"+ File.separator + fileName);
								uploadFile(part.getInputStream(), tempPath);
								String realPath = tempPath.replace(partedaRimuovere , "src/main/WebContent/");
								if (fileName != null && !fileName.equals("")) {
									uploadFile(part.getInputStream(), realPath);
								}
							}
						}
					}
					
					IPhotoDao photoDao = null;
		    		photoDao = new PhotoDaoDataSource(ds);
		    		int code = Integer.parseInt(request.getParameter("id"));
		    		String stats = request.getParameter("stats");
		    		
		    		Prodotto bean = new Prodotto();
		    		bean.setCode(code);
					bean = productDao.doRetrieveByKey(bean);
					bean.setNome(nome);
					bean.setDescrizione(descrizione);
					bean.setCategoria(categoria);
					bean.setStats(stats);
					bean.setPrice(Double.parseDouble(price));
					if( fileupload ) {
						bean.setImagePath(imagePath);
					}
				
					bean.setCode(code);
					try {
						productDao.doUpdate(code, bean);
					} catch (CheckException e) {
						e.printStackTrace();
						dispatcher.forward(request, response);
						return;
					}
					
					Taglie sizes = new Taglie();			
					sizes.setQuantitaM(quantityM);
					sizes.setQuantitaL(quantityL);
					sizes.setQuantitaXL(quantityXL);
					sizes.setQuantitaXXL(quantityXXL);
					bean.setTaglie(sizes);
					try {
						productDao.doUpdateSizes(code, sizes);
						if( fileupload )
							photoDao.updatePhoto(bean);
					} catch (CheckException e) {
						e.printStackTrace();
						dispatcher.forward(request, response);
						return;
					}	
					dispatcher = null;	
					dispatcher = getServletContext().getRequestDispatcher("/admin/ProductView.jsp");	
					dispatcher.forward(request, response);
		} catch (SQLException | CheckException e) {
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
