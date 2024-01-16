package gestioneprodotti;

import java.io.IOException;   

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;
import view.sito.*;

public class ProductDaoDataSource implements IProductDao {
	
	private static final String TABLE_NAME = "prodotto";
	private DataSource ds = null;

	public ProductDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public synchronized void doSave(Prodotto product) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + ProductDaoDataSource.TABLE_NAME
				+ " (CATEGORIA, NOME, DESCRIZIONE, PRICE, STATS) VALUES (?, ?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, product.getCategoria());
			preparedStatement.setString(2, product.getNome());
			preparedStatement.setString(3, product.getDescrizione());
			preparedStatement.setDouble(4, product.getPrice());
			preparedStatement.setString(5, product.getStats());
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
	public synchronized void doUpdate(int code, Prodotto product) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String update = "UPDATE " + ProductDaoDataSource.TABLE_NAME
				+ " SET CATEGORIA=?, NOME=?, DESCRIZIONE=?, PRICE=?, STATS=? WHERE idProdotto=?";		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setString(1, product.getCategoria());
			preparedStatement.setString(2, product.getNome());
			preparedStatement.setString(3, product.getDescrizione());
			preparedStatement.setDouble(4, product.getPrice());
			preparedStatement.setString(5, product.getStats());
			preparedStatement.setInt(6, code);
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
	public synchronized void doUpdateSizes(int code,Taglie sizes) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
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
	public synchronized Taglie getSizesByKey(Prodotto product) throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Taglie taglie = new Taglie();
		String selectSQL = "SELECT * FROM taglie WHERE idProdotto= ?";
		
		int code = product.getCode();
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
	public synchronized Prodotto doRetrieveByKey(Prodotto product) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
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
		
		Taglie taglie = this.getSizesByKey(bean);
		bean.setTaglie(taglie);
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
	public synchronized boolean doDelete(Prodotto product) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;
		int code = product.getCode();
		
		String deleteSQL = "DELETE FROM " + ProductDaoDataSource.TABLE_NAME + " WHERE idProdotto = ?";
		String deleteSQL2 = "DELETE FROM prodottocarrello WHERE idProdotto = ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);
			result = preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(deleteSQL2);
			preparedStatement.setInt(1, code);
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
		return (result != 0);
	}

	
	
	@Override
	public synchronized Collection<Prodotto> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Prodotto> products = new LinkedList<>();

		String selectSQL = "SELECT * FROM " + ProductDaoDataSource.TABLE_NAME;

		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY ?";
		}

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			if (order != null && !order.equals("")) {
				preparedStatement.setString(1, order);
			}
						
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
	
	public synchronized Collection<Prodotto> sortByCategoria(String order) throws SQLException {
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
	

	public synchronized void updatePhoto(String idA, InputStream photo) 
			throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = 	DriverManagerConnectionPool.getConnection();
			stmt = con.prepareStatement("UPDATE prodotto SET photo = ? WHERE idProdotto = ?");
			try {
				stmt.setBinaryStream(1, photo, photo.available());
				stmt.setString(2, idA);	
				stmt.executeUpdate();
				con.commit();
			} catch (IOException e) {
				 
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			} finally {
				if (con != null)
					DriverManagerConnectionPool.releaseConnection(con);
			}
		}
	}
	
	public synchronized byte[] load(String id) throws SQLException {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		byte[] bt = null;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			String sql = "SELECT photo FROM prodotto WHERE idProdotto = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				bt = rs.getBytes("photo");
			}

		} catch (SQLException sqlException) {
		} 
		finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			} finally {
				if (connection != null) 
					DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return bt;
	}

	@Override
	public Collection<Prodotto> sortByName(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Collection<Prodotto> products = new LinkedList<>();

		String selectSQL = "SELECT * FROM prodotto ORDER BY nome";
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