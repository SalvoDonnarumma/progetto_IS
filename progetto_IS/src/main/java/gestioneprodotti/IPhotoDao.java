package gestioneprodotti;

import java.sql.SQLException;

public interface IPhotoDao {
	public void updatePhoto(Prodotto p) throws SQLException;
}
