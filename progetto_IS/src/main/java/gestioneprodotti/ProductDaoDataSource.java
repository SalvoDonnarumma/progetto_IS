package gestioneprodotti;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import checking.CheckException;
import checking.DBException;

public class ProductDaoDataSource implements IProductDao {
	
	private static final String TABLE_NAME = "prodotto";
	private DataSource ds = null;

	public ProductDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public synchronized void doSave(Prodotto product) throws SQLException, CheckException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if( product == null )
			throw new CheckException("Prodotto non valido!");
		
		if(product.getCategoria() == null || product.getCategoria().equals("") || !ProdottoValidator.isValidCategoria(product.getCategoria()))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getDescrizione() == null || product.getDescrizione().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getNome() == null || product.getNome().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getStats() == null || product.getStats().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getPrice() < 0 || !ProdottoValidator.isValidPrice(product.getPrice().toString()))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getTaglie() == null)
			throw new CheckException("Prodotto non valido!");
			
		String insertSQL = "INSERT INTO " + ProductDaoDataSource.TABLE_NAME
				+ " (CATEGORIA, NOME, DESCRIZIONE, PRICE, STATS, IMAGE) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, product.getCategoria());
			preparedStatement.setString(2, product.getNome());
			preparedStatement.setString(3, product.getDescrizione());
			preparedStatement.setDouble(4, product.getPrice());
			preparedStatement.setString(5, product.getStats());
			preparedStatement.setString(6, product.getImagePath());
			
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}
	
	@Override
	public synchronized void doUpdate(int code, Prodotto product) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		if( product == null )
			throw new CheckException("Prodotto non valido!");
		
		if(product.getCategoria() == null || product.getCategoria().equals("") || !ProdottoValidator.isValidCategoria(product.getCategoria()))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getDescrizione() == null || product.getDescrizione().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getNome() == null || product.getNome().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getStats() == null || product.getStats().equals(""))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getPrice() < 0 || !ProdottoValidator.isValidPrice(product.getPrice().toString()))
			throw new CheckException("Prodotto non valido!");
		
		if(product.getTaglie() == null)
			throw new CheckException("Prodotto non valido!");
		
		String update = "UPDATE " + ProductDaoDataSource.TABLE_NAME
				+ " SET CATEGORIA=?, NOME=?, DESCRIZIONE=?, PRICE=?, STATS=?, IMAGE=? WHERE idProdotto=?";		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setString(1, product.getCategoria());
			preparedStatement.setString(2, product.getNome());
			preparedStatement.setString(3, product.getDescrizione());
			preparedStatement.setDouble(4, product.getPrice());
			preparedStatement.setString(5, product.getStats());
			preparedStatement.setString(6, product.getImagePath());
			preparedStatement.setInt(7, code);
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}
	
	@Override
	public synchronized void doUpdateSizes(int code,Taglie sizes) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		if(code < 0)
			throw new CheckException("Taglia non valida!");
		
		if(sizes.getQuantitaM() == null || sizes.getQuantitaM()<0 || sizes.getQuantitaM().toString().matches("\\d+"))
			throw new CheckException("Taglia non valida!");
		if(sizes.getQuantitaL() == null || sizes.getQuantitaL()<0 || sizes.getQuantitaL().toString().matches("\\d+") )
			throw new CheckException("Taglia non valida!");
		if(sizes.getQuantitaXL() == null || sizes.getQuantitaXL()<0 || sizes.getQuantitaXL().toString().matches("\\d+") )
			throw new CheckException("Taglia non valida!");
		if(sizes.getQuantitaXXL() == null || sizes.getQuantitaXXL()<0 || sizes.getQuantitaXXL().toString().matches("\\d+") )
			throw new CheckException("Taglia non valida!");
		
		String update = "UPDATE taglie SET tagliaM=?, tagliaL=?, tagliaXL=?, tagliaXXL=? WHERE idProdotto=?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setInt(1, sizes.getQuantitaM());
			preparedStatement.setInt(2, sizes.getQuantitaL());
			preparedStatement.setInt(3, sizes.getQuantitaXL());
			preparedStatement.setInt(4, sizes.getQuantitaXXL());
			preparedStatement.setInt(5, code);
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}

	@Override
	public void decreaseSize(Taglie sizes, int qnt, String size, int code) throws SQLException{
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			String update = null;
			if( size.equalsIgnoreCase("M") ) {
				update = "UPDATE taglie SET tagliaM=tagliaM-? WHERE idProdotto=?";
			} else if( size.equalsIgnoreCase("L")) {
					update = "UPDATE taglie SET tagliaL=tagliaL-? WHERE idProdotto=?";
					}
				else if( size.equalsIgnoreCase("XL")) {
						update = "UPDATE taglie SET tagliaXL=tagliaXL-? WHERE idProdotto=?";
					}
					else if( size.equalsIgnoreCase("XXL")) {
						update = "UPDATE taglie SET tagliaXXL=tagliaXXL-? WHERE idProdotto=?";	
			}
			
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(update);
				preparedStatement.setInt(1, qnt);
				preparedStatement.setInt(2, code);
				preparedStatement.executeUpdate();
			} finally {
				try {
					if (preparedStatement != null)
						preparedStatement.close();
				} finally {
					if (connection != null)
						connection.close();
				}
			}
	}
	
	@Override
	public synchronized Taglie getSizesByKey(Prodotto product) throws SQLException, CheckException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Taglie taglie = new Taglie();
		String selectSQL = "SELECT * FROM taglie WHERE idProdotto= ?";
		
		if( product == null )
			throw new CheckException("codice prodotto non valido");
		
		if( product.getCode() == 0 || product.getCode() < 0)
			throw new CheckException("codice prodotto non valido");
		
		Integer code = product.getCode();
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, code);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) { 
				taglie.setIdProdotto(rs.getInt("idProdotto"));
				taglie.setQuantitaM(rs.getInt("tagliaM"));
				taglie.setQuantitaL(rs.getInt("tagliaL"));
				taglie.setQuantitaXL(rs.getInt("tagliaXL"));
				taglie.setQuantitaXXL(rs.getInt("tagliaXXL"));
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}	
		return taglie;
	}
	
	@Override
	public synchronized void setSizesByKey(int code,Taglie taglie) throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO taglie (idProdotto, tagliaM, tagliaL, tagliaXL, tagliaXXL) VALUES (?,?,?,?,?)";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, code);
			preparedStatement.setInt(2, taglie.getQuantitaM());
			preparedStatement.setInt(3, taglie.getQuantitaL());
			preparedStatement.setInt(4, taglie.getQuantitaXL());
			preparedStatement.setInt(5, taglie.getQuantitaXXL());
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}	
	}
	
	
	@Override
	public synchronized Prodotto doRetrieveByKey(Prodotto product) throws SQLException, CheckException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if( product == null )
			throw new CheckException("Prodotto non valido!");
		if( product.getCode() < 0 )
			throw new CheckException("Prodotto non valido!");
		
		Prodotto bean = new Prodotto();
		
		int code = product.getCode();
		String query = "SELECT * FROM " + ProductDaoDataSource.TABLE_NAME + " WHERE idProdotto= ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, code);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) { 
				bean.setCode(rs.getInt("IDPRODOTTO"));
				bean.setCategoria(rs.getString("CATEGORIA"));
				bean.setNome(rs.getString("NOME"));
				bean.setDescrizione(rs.getString("DESCRIZIONE"));
				bean.setPrice(rs.getDouble("PRICE"));
				bean.setStats(rs.getString("STATS"));
				bean.setImagePath(rs.getString("IMAGE"));
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return bean;
	}
		
	@Override
	public int doRetrieveByName(Prodotto product) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Prodotto bean = new Prodotto();

		String nome = product.getNome();
		String query = "SELECT * FROM " + ProductDaoDataSource.TABLE_NAME + " WHERE nome= ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nome);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) { 
				bean.setCode(rs.getInt("IDPRODOTTO"));
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return bean.getCode();
	}

	@Override
	public synchronized boolean doDelete(Prodotto product) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if( product == null )
			throw new CheckException("Prodotto non valido!");
		if( product.getCode() == null || product.getCode() == -1)
			throw new CheckException("Prodotto non valido!");
		
		int result = 0;
		int code = product.getCode();
		
		String deleteSQL = "DELETE FROM " + ProductDaoDataSource.TABLE_NAME + " WHERE idProdotto = ?";
		String deleteSQL2 = "DELETE FROM prodottocarrello WHERE idProdotto = ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);
			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL2);
			preparedStatement.setInt(1, code);
			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		
		return (result != 0);
	}

	@Override
	public synchronized ArrayList<Prodotto> doRetrieveAll(String order) throws SQLException, CheckException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		ArrayList<Prodotto> products = new ArrayList<>();
		String selectSQL = "SELECT * FROM " + ProductDaoDataSource.TABLE_NAME;

		if (order != null && order.equals("categoria")) {
			selectSQL += " ORDER BY categoria";
		} else if( order != null && order.equals("nome")) {
			selectSQL += " ORDER BY nome";
		} else if( order != null)
			throw new CheckException("Ordinamento non valido!");
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			System.out.println(preparedStatement);					
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Prodotto bean = new Prodotto();
				int code = rs.getInt("idProdotto");
				bean.setCode(code);
				bean.setCategoria(rs.getString("CATEGORIA"));
				bean.setNome(rs.getString("NOME"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrice(rs.getDouble("PRICE"));
				bean.setStats(rs.getString("STATS"));
				bean.setImagePath(rs.getString("IMAGE"));
				products.add(bean);
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return products;
	}
	
	public synchronized Collection<Prodotto> sortByCategoria(String order) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Collection<Prodotto> products = new LinkedList<>();

		String selectSQL = "SELECT * FROM prodotto ORDER BY categoria";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Prodotto bean = new Prodotto();
				int code = rs.getInt("idProdotto");
				bean.setCode(code);
				bean.setCategoria(rs.getString("CATEGORIA"));
				bean.setNome(rs.getString("NOME"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setPrice(rs.getDouble("PRICE"));
				bean.setStats(rs.getString("STATS"));
				bean.setImagePath(rs.getString("IMAGE"));
				Taglie taglie = this.getSizesByKey(bean);
				bean.setTaglie(taglie);
				products.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return products;
	}	
}