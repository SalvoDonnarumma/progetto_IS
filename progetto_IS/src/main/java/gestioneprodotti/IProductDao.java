package gestioneprodotti;

import java.sql.SQLException;
import java.util.ArrayList;

import checking.CheckException;
import checking.DBException;

public interface IProductDao {
	
	public void doSave(Prodotto product) throws SQLException, CheckException;

	public boolean doDelete(Prodotto product) throws SQLException;

	public Prodotto doRetrieveByKey(Prodotto product) throws SQLException, CheckException;
	
	public ArrayList<Prodotto> doRetrieveAll(String order) throws SQLException, CheckException;

	Taglie getSizesByKey(Prodotto product) throws SQLException;

	void setSizesByKey(int code, Taglie taglie) throws SQLException;

	public int doRetrieveByName(Prodotto product) throws SQLException;

	void doUpdateSizes(int code, Taglie sizes) throws SQLException;

	void doUpdate(int code, Prodotto product) throws SQLException;

	void decreaseSize(Taglie sizes, int qnt, String size, int code) throws SQLException;
}



