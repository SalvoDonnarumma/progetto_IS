package gestioneprodotti;

import java.sql.SQLException;

import javax.sql.DataSource;

import checking.CheckException;
import gestionesito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhotoDaoDataSource implements IPhotoDao {
	
	private DataSource ds = null;

	public PhotoDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	public void updatePhoto(Prodotto modify) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String update = "UPDATE prodotto SET image=? WHERE idProdotto=?";	
		
		if( modify == null )
			throw new CheckException("Path immagine non vadido...");
		if( modify.getCode() == null || modify.getCode() < 0)
			throw new CheckException("Codice prodotto non valido...");
		if( modify.getImagePath() == null || modify.getImagePath().equals(""))
			throw new CheckException("Path immagine non valido...");
		
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
