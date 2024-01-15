package gestioneprodotti;

import java.sql.SQLException;


import view.sito.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhotoDaoDataSource {
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
				/*commento per riempire il try-catch*/
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqlException) {
				/*commento per riempire il try-catch*/
			} finally {
				if (con != null)
					DriverManagerConnectionPool.releaseConnection(con);
			}
		}
	}
}
