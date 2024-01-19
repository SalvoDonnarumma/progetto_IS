package gestioneprodotti;

import java.sql.SQLException;

import javax.sql.DataSource;

import view.sito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhotoDaoDataSource implements IPhotoDao {
	
	private DataSource ds = null;

	public PhotoDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	public synchronized static byte[] load(String id) throws SQLException {
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
			/*commento per riempire il try-catch*/
		} 
			finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqlException) {
				/*commento per riempire il try-catch*/
			} finally {
				if (connection != null) 
					DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return bt;
	}
	
	public void updatePhoto(Prodotto modify) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String update = "UPDATE prodotto SET image=? WHERE idProdotto=?";	
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setString(1, modify.getImagePath());
			preparedStatement.setInt(2, modify.getCode());
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
}
