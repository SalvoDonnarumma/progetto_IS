package gestionecarta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import gestionecarrello.Carrello;
import gestioneprodotti.IProductDao;
import gestioneprodotti.Prodotto;
import gestioneprodotti.ProductDaoDataSource;
import gestioneutenti.Utente;

public class CartaDaoDataSource implements ICartaDaoData{

	private DataSource ds = null;

	public CartaDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public boolean salvaCarta(Carta carta) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertSQL = "INSERT INTO carta (idcarta, proprietario, numero_carta, data_scadenza) VALUES (?,?,?,?)";
		
		if(cartaEsistente(carta)) {
			cancellaCarta(carta);
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setInt(1, carta.getIdCarta());
			preparedStatement.setString(2, carta.getProprietario());
			preparedStatement.setString(3, carta.getNumero_carta());
			preparedStatement.setString(4, carta.getData_scadenza());
			int result = preparedStatement.executeUpdate();	
			if(result > 0)
				return true;
			else
				return false;
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

	protected boolean cartaEsistente(Carta carta) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT * from carta WHERE idcarta = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, carta.getIdCarta());
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {		
				System.out.println("è già presente una carta");
				do {
		            int idCarta= resultset.getInt("idcarta");
		            System.out.println("Id: "+idCarta);
		        } while (resultset.next());
				return true;
			}else {
				System.out.println("Carta non presente");
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

	@Override
	public Carta recuperaCarta(Utente utente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String selectSQL = "SELECT * from carta WHERE idcarta = ?";
		
		Carta recuperata = new Carta();
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getId());
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {		
				do {
		            int idPCarta = resultset.getInt("idcarta");
		            String proprietario = resultset.getString("proprietario");
		            String scadenza = resultset.getString("data_scadenza");
		            String numero_carta = resultset.getString("numero_carta");
		            recuperata.setIdCarta(idPCarta);
		            recuperata.setProprietario(proprietario);
		            recuperata.setData_scadenza(scadenza);
		            recuperata.setNumero_carta(numero_carta);
		        } while (resultset.next());
				return recuperata;
			} else 
				return null; //Se non è stata trovata nessuna carta, restituisco null
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
	public boolean cancellaCarta(Carta carta) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "DELETE from carta WHERE idcarta = ?";
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, carta.getIdCarta());
			int result = preparedStatement.executeUpdate();	
			if(result > 0)
				return true;
			else
				return false;
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
