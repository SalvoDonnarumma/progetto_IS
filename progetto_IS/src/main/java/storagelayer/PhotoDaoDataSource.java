package storagelayer;

import java.sql.SQLException;

import javax.sql.DataSource;

import gestioneprodotti.Prodotto;
import view.sito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhotoDaoDataSource implements IPhotoDao {
	
	private DataSource ds = null;

	public PhotoDaoDataSource(DataSource ds) {
		this.ds = ds;
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
