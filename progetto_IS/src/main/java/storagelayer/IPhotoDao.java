package storagelayer;

import java.sql.SQLException;

import gestioneprodotti.Prodotto;

public interface IPhotoDao {
	public void updatePhoto(Prodotto p) throws SQLException;
}
