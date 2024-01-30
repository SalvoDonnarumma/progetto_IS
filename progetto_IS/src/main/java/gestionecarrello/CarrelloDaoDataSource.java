package gestionecarrello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;     
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import checking.CheckException;
import gestionecarta.Carta;
import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneutenti.Utente;

public class CarrelloDaoDataSource implements ICarrelloDao{

	private DataSource ds = null;
	public CarrelloDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public void salvaCarrello(Carrello carrello) throws SQLException, CheckException {
	    if (carrello == null) {
	        throw new CheckException("Carrello non valido");
	    }

	    if( carrello.getIdcarrello() <0 )
	    	throw new CheckException("Carrello non valido!");
	    
	    for( Prodotto p : carrello.getAllProduct())
	    	if( p.getCode() < 0)
	    		throw new CheckException("Carrello non valido!");
	    	
	    
	    String insertSQL = "INSERT INTO prodottocarrello (idcarrello, idprodottoc) VALUES (?,?)";
	    List<Prodotto> lista_prodotti = carrello.getAllProduct();
	    Integer id_utente_sessione = carrello.getIdcarrello();

	    if (carrelloEsistente(carrello)) {
	        System.out.println("Carrello già presente, lo cancello");
	        eliminaCarrello(carrello);
	    }

	    try (Connection connection = ds.getConnection()) {
	        for (Prodotto prodotto : lista_prodotti) {
	            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
	                preparedStatement.setInt(1, id_utente_sessione);
	                preparedStatement.setInt(2, prodotto.getCode());
	                preparedStatement.executeUpdate();
	            }
	        }
	    }
	}


	private boolean carrelloEsistente(Carrello utente) throws SQLException {
	    String selectSQL = "SELECT * from prodottocarrello WHERE idcarrello = ?";
	    boolean flag = false;

	    try (Connection connection = ds.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

	        preparedStatement.setInt(1, utente.getIdcarrello());

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                System.out.println("Sono stati trovati prodotti");
	                do {
	                    int idProdotto = resultSet.getInt("idprodottoc");
	                    System.out.println("Id: " + idProdotto);
	                } while (resultSet.next());
	                flag = true;
	            } else {
	                System.out.println("Non sono stati trovati prodotti");
	                flag = false;
	            }
	        }
	    }

	    return flag;
	}

	
	public void eliminaCarrello(Carrello utente) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if(utente == null || utente.getIdcarrello()<0 )
			throw new CheckException("Carrello non valido!");
		String selectSQL = "DELETE from prodottocarrello WHERE idcarrello = ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getIdcarrello());
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
	public Carrello recuperaCarrello(Utente utente) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		IProductDao productDao = new ProductDaoDataSource(ds);
		
		if(utente == null)
			throw new CheckException("utente non valido!");
		
		if(utente.getId()<1)
			throw new CheckException("utente non valido");
		
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
		            
		            
		            Prodotto p = productDao.doRetrieveByKey(id);
					p.setTaglie(productDao.getSizesByKey(p));
					prodotticarrello.add(p);
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
