package gestioneprodotti;

import java.sql.SQLException;   
import java.util.Collection;

public interface IProductDao {
	
	public void doSave(Prodotto product) throws SQLException;

	public boolean doDelete(Prodotto product) throws SQLException;

	public Prodotto doRetrieveByKey(Prodotto product) throws SQLException;
	
	public Collection<Prodotto> doRetrieveAll(String order) throws SQLException;

	Taglie getSizesByKey(Prodotto product) throws SQLException;

	void setSizesByKey(int code, Taglie taglie) throws SQLException;

	public int doRetrieveByName(Prodotto product) throws SQLException;
	
	Collection<Prodotto> sortByCategoria(String order) throws SQLException;
	Collection<Prodotto> sortByName(String order) throws SQLException;

	void doUpdateSizes(int code, Taglie sizes) throws SQLException;

	void doUpdate(int code, Prodotto product) throws SQLException;

	void decreaseSize(Taglie sizes, int qnt, String size, int code) throws SQLException;
}



