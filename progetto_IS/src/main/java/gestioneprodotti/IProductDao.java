package gestioneprodotti;

import java.sql.SQLException;
import java.util.ArrayList;

import checking.CheckException;
import checking.DBException;

public interface IProductDao {
	
	public void doSave(Prodotto product) throws SQLException, CheckException;

	public boolean doDelete(Prodotto product) throws SQLException, CheckException;

	public Prodotto doRetrieveByKey(Prodotto product) throws SQLException, CheckException;
	
	public ArrayList<Prodotto> doRetrieveAll(String order) throws SQLException, CheckException;

	Taglie getSizesByKey(Prodotto product) throws SQLException, CheckException;

	void setSizesByKey(int code, Taglie taglie) throws SQLException, CheckException;

	public int doRetrieveByName(Prodotto product) throws SQLException;

	void doUpdateSizes(int code, Taglie sizes) throws SQLException, CheckException;

	void doUpdate(int code, Prodotto product) throws SQLException, CheckException;

	void decreaseSize(Taglie sizes, int qnt, String size, int code) throws SQLException;
	
	public Prodotto getLast() throws SQLException;
}



