package gestioneprodotti;

import java.sql.SQLException;

import checking.CheckException;

public interface IPhotoDao {
	public void updatePhoto(Prodotto p) throws SQLException, CheckException;
}
