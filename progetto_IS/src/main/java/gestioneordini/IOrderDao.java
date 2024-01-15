package gestioneordini;

import java.sql.SQLException; 

import java.util.Collection;

import gestioneprodotti.Prodotto;
import gestioneutenti.Utente;

public interface IOrderDao {

	Collection<ProdottoOrdinato> doRetrieveById(String order, Ordine ordine) throws SQLException; 
	void doSave(Ordine ordine) throws SQLException;
	int doSaveAll(Ordine ordine, Double ptotm) throws SQLException;
	Collection<Ordine> doRetrieveAllOrders(String order) throws SQLException;
	void changeOrderState(Ordine ordine) throws SQLException;
	Collection<Ordine> doRetrieveAllByKey(Utente utente) throws SQLException;
	void removeOrderByEmail(Utente utente) throws SQLException;
	ProdottoOrdinato doRetrieveByKeyO(Prodotto po) throws SQLException;
}