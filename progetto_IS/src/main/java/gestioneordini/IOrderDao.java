package gestioneordini;

import java.sql.SQLException;
import java.util.ArrayList;

import checking.CheckException;
import gestioneprodotti.Prodotto;
import gestioneutenti.Utente;

public interface IOrderDao {

	ArrayList<ProdottoOrdinato> doRetrieveById(Ordine ordine) throws SQLException; 
	void doSave(Ordine ordine) throws SQLException;
	ArrayList<Ordine> doRetrieveAllOrders(String order) throws SQLException;
	void changeOrderState(Ordine ordine) throws SQLException;
	ArrayList<Ordine> doRetrieveAllByKey(Utente utente) throws SQLException;
	ProdottoOrdinato doRetrieveByKeyO(Prodotto po) throws SQLException, CheckException;
}