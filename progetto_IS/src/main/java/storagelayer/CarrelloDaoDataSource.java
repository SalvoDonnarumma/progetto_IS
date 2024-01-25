package storagelayer;

import java.sql.Connection;   
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import gestionecarrello.Carrello;
import gestioneprodotti.Prodotto;
import gestioneutenti.Utente;

public class CarrelloDaoDataSource implements ICarrelloDao{

	private DataSource ds = null;
	public CarrelloDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public void salvaCarrello(Carrello carrello, Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertSQL = "INSERT INTO prodottocarrello (idcarrello, idprodottoc) VALUES (?,?)";
		List<Prodotto> lista_prodotti = carrello.getAllProduct();
		Integer id_utente_sessione = utente.getId();
	
		if(carrelloEsistente(utente)) {
			System.out.println("carrello già presente, lo cancello");
			eliminaCarrello(utente);
		}
		
		for(int i =0; i<lista_prodotti.size(); i++) {
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(insertSQL);
				preparedStatement.setInt(1, id_utente_sessione);
				Integer id_prodotto_carrello = lista_prodotti.get(i).getCode();
				preparedStatement.setInt(2, id_prodotto_carrello);
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

	public boolean carrelloEsistente(Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT * from prodottocarrello WHERE idcarrello = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getId());
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {		
				System.out.println("Sono stati trovati prodotti");
				do {
		            int idProdotto = resultset.getInt("idprodottoc");
		            System.out.println("Id: "+idProdotto);
		        } while (resultset.next());
				return true;
			}else {
				System.out.println("Sono stati trovati prodotti");
				return false;
			}
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
	
	public void eliminaCarrello(Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "DELETE from prodottocarrello WHERE idcarrello = ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getId());
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
	
	@Override
	public Carrello recuperaCarrello(Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		IProductDao productDao = new ProductDaoDataSource(ds);
		
		String selectSQL = "SELECT * from prodottocarrello WHERE idcarrello = ?";
		
		List<Prodotto> prodotticarrello = new ArrayList<>();
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getId());
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {		
				System.out.println("Sono stati trovati prodotti");
				do {
		            int idProdotto = resultset.getInt("idprodottoc");
		            Prodotto id = new Prodotto();
		            id.setCode(idProdotto);
		            
		            prodotticarrello.add(productDao.doRetrieveByKey(id));
		        } while (resultset.next());
			}
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		Carrello carrello = new Carrello();
		for(int i = 0; i<prodotticarrello.size(); i++) {
			Prodotto p = prodotticarrello.get(i);
			carrello.addProduct(p);
		}
		return carrello;
	}
}
