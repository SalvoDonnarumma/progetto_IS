package gestioneordini;

import java.sql.Connection;     

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import checking.CheckException;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneprodotti.Taglie;
import gestioneutenti.Utente;

public class OrderDaoDataSource implements IOrderDao{
	private DataSource ds = null;

	public OrderDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	private int getLastCode() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String getLast = "SELECT * from ordine order by idOrdine DESC LIMIT 1";
		int idOrdine = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(getLast);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) { 
				idOrdine = rs.getInt("idOrdine");
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
		return idOrdine;
	}
	
	public synchronized void doSave(Ordine bean) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL1 = "INSERT INTO ordine (idUtente, data, stato, indirizzo, prezzototale) VALUES (?, ?, ?, ?, ?)";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL1);
			preparedStatement.setString(1, bean.getEmailUtente());
			preparedStatement.setString(2, bean.getData());
			preparedStatement.setString(3, bean.getStato());
			preparedStatement.setString(4, bean.getIndirizzo());
			preparedStatement.setDouble(5, bean.getPrezzototale());
			
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
		
		int idOrdine = this.getLastCode(); //prelevo dal db il codice dell'ordine appena inserito
		
		double totalprice = 0;
		String insertSQL2  = "INSERT INTO prodottoordinato (idOrdine, idProdotto, nome, categoria, prezzo, quantita, size) VALUES (?, ?, ?, ?, ?, ?, ?)";
		List<ProdottoOrdinato> orderedProducts = bean.getOrders();
		for( ProdottoOrdinato product : orderedProducts) {
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(insertSQL2);
				preparedStatement.setInt(1, idOrdine);
				preparedStatement.setInt(2, product.getCode());
				preparedStatement.setString(3, product.getNome());
				preparedStatement.setString(4, product.getCategoria());
				preparedStatement.setDouble(5, product.getPrice());
				preparedStatement.setInt(6, product.getQnt());
				preparedStatement.setString(7, product.getSz());
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
			totalprice = totalprice + product.getPrice()*product.getQnt();
		}
		System.out.println("totale: "+totalprice);
		bean.setPrezzototale(totalprice);
		String insertSQL3 = "UPDATE ordine SET prezzototale=? WHERE idOrdine=?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL3);
			preparedStatement.setDouble(1, totalprice);	
			preparedStatement.setInt(2, idOrdine);
			preparedStatement.executeUpdate();
		} finally {
			try {
				preparedStatement.close();
			} finally {
				connection.close();
			}
		}
	}
	
	public synchronized Double doSaveProdottiOrdinati(ArrayList<ProdottoOrdinato> prodotti, Ordine bean) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Double totalprice = 0.0;
		
		String insertSQL2  = "INSERT INTO prodottoordinato (idOrdine, idProdotto, nome, categoria, prezzo, quantita, size) VALUES (?, ?, ?, ?, ?, ?, ?)";
		List<ProdottoOrdinato> orderedProducts = bean.getOrders();
		for( ProdottoOrdinato product : orderedProducts) {
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(insertSQL2);
				preparedStatement.setInt(1, bean.getIdOrdine());
				preparedStatement.setInt(2, product.getCode());
				preparedStatement.setString(3, product.getNome());
				preparedStatement.setString(4, product.getCategoria());
				preparedStatement.setDouble(5, product.getPrice());
				preparedStatement.setInt(6, product.getQnt());
				preparedStatement.setString(7, product.getSz());
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
			totalprice = totalprice + product.getPrice()*product.getQnt();
			return totalprice;
		}
		return totalprice;
	}
	
	public synchronized int updatePrezzo(Double totalprice, Integer idOrdine) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertSQL3 = "UPDATE ordine SET prezzototale=? WHERE idOrdine=?";
		
		System.out.println("Prezzo totale calcolate: "+totalprice);
		int res = 0;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL3);
			preparedStatement.setDouble(1, totalprice);	
			preparedStatement.setInt(2, idOrdine);
			res = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return res;
	}
	
	
	@Override
	public synchronized ArrayList<Ordine> doRetrieveAllOrders(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ArrayList<Ordine> orders = new ArrayList<>();
		String selectSQL = "SELECT * FROM ordine" ;
		if (order != null && !order.equals("")) {
			selectSQL += "ORDER BY ?";
		}
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			if (order != null && !order.equals("")) {
				preparedStatement.setString(1, order);
			}
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Ordine bean = new Ordine();
				bean.setIdOrdine(rs.getInt("idOrdine"));
				bean.setEmailUtente(rs.getString("idUtente"));
				bean.setData(rs.getString("data"));
				bean.setStato(rs.getString("stato"));
				bean.setIndirizzo(rs.getString("indirizzo"));
				bean.setPrezzototale(rs.getDouble("prezzototale"));
				orders.add(bean);
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
		return orders;
	}
	
	@Override
	public synchronized ArrayList<ProdottoOrdinato> doRetrieveById(Ordine ordine) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ArrayList<ProdottoOrdinato> singleorder = new ArrayList<>();

		int code = ordine.getIdOrdine();
		String selectSQL = "SELECT * FROM prodottoordinato WHERE idOrdine = ?" ;

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, code);
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				ProdottoOrdinato bean = new ProdottoOrdinato();
				bean.setNome(rs.getString("nome"));
				bean.setCategoria(rs.getString("categoria"));
				bean.setPrice(rs.getDouble("prezzo"));
				bean.setQnt(rs.getInt("quantita"));
				bean.setSz(rs.getString("size"));
				bean.setCode(rs.getInt("idProdotto"));
				
				singleorder.add(bean);
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
		return singleorder;
	}
	
	@Override
	public synchronized ArrayList<Ordine> doRetrieveAllByKey(Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ArrayList<Ordine> orders = new ArrayList<>();
		String email = utente.getEmail();
		String selectSQL = "SELECT * FROM ordine WHERE idUtente = ?" ;
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Ordine bean = new Ordine();
				bean.setIdOrdine(rs.getInt("idOrdine"));
				bean.setEmailUtente(rs.getString("idUtente"));
				bean.setData(rs.getString("data"));
				bean.setStato(rs.getString("stato"));
				bean.setIndirizzo(rs.getString("indirizzo"));
				bean.setPrezzototale(rs.getDouble("prezzototale"));
				orders.add(bean);
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
		return orders;
	}

	@Override
	public void changeOrderState(Ordine ordine) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String updateSQL = "UPDATE ordine SET stato = ? WHERE idOrdine IN (?)";

		int idOrdine = ordine.getIdOrdine();
		String stato = ordine.getStato();
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, stato);
			preparedStatement.setInt(2, idOrdine);

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
	public ProdottoOrdinato doRetrieveByKeyO(Prodotto p) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT * FROM prodotto WHERE idProdotto = ?";
		ProdottoOrdinato bean = new ProdottoOrdinato();
		try {
			int code = p.getCode();
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, code);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
		        bean.setCode(rs.getInt("IDPRODOTTO"));
		        bean.setCategoria(rs.getString("CATEGORIA"));
		        bean.setNome(rs.getString("NOME"));
		        bean.setDescrizione(rs.getString("DESCRIZIONE"));
		        bean.setPrice(rs.getDouble("PRICE"));
		        bean.setStats(rs.getString("STATS"));
		        bean.setImagePath(rs.getString("IMAGE"));
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
		ProductDaoDataSource pdds = new ProductDaoDataSource(ds);
		Taglie taglie = pdds.getSizesByKey(bean);
		bean.setTaglie(taglie);
		
		return bean;
	}
}
