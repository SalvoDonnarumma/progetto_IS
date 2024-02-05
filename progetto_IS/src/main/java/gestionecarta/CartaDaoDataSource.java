package gestionecarta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import checking.CheckException;
import gestioneutenti.Utente;

public class CartaDaoDataSource implements ICartaDaoData{

	private DataSource ds = null;

	public CartaDaoDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public boolean salvaCarta(Carta carta) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertSQL = "INSERT INTO carta (idcarta, proprietario, numero_carta, data_scadenza) VALUES (?,?,?,?)";
		
		if(carta == null || carta.getIdCarta()==0)
			throw new CheckException("carta non valida");
		
		if(carta.getProprietario() == null || carta.getProprietario().equals(""))
			throw new CheckException("carta non valida");
		
		if(carta.getData_scadenza() == null || carta.getData_scadenza().equals(""))
			throw new CheckException("carta non valida");
		
		String mm = carta.getData_scadenza().substring(0, 2);
		String aa = carta.getData_scadenza().substring(3, 7);	
		if( !CardValidator.isValidMonth(CardValidator.convertiNumeroInMese(mm)))
			throw new CheckException("carta non valida");
		if( !CardValidator.isYearNotExpired(aa))
			throw new CheckException("carta non valida");
		if( !CardValidator.isMonthNotExpired(Integer.parseInt(mm), Integer.parseInt(aa)))
			throw new CheckException("carta non valida");	
		if(carta.getNumero_carta() == null || carta.getNumero_carta().equals("") || !CardValidator.isValidFormat(carta.getNumero_carta()))
			throw new CheckException("carta non valida");
		
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
	
	@Override
	public Carta recuperaCarta(Utente utente) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String selectSQL = "SELECT * from carta WHERE idcarta = ?";
		
	
		if(utente == null || utente.getId() == 0)
			throw new CheckException("carta non valida");
		
		Carta recuperata = new Carta();
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, utente.getId());
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {		
		            int idPCarta = resultset.getInt("idcarta");
		            String proprietario = resultset.getString("proprietario");
		            String scadenza = resultset.getString("data_scadenza");
		            String numero_carta = resultset.getString("numero_carta");
		            recuperata.setIdCarta(idPCarta);
		            recuperata.setProprietario(proprietario);
		            recuperata.setData_scadenza(scadenza);
		            recuperata.setNumero_carta(numero_carta);
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
	public boolean cancellaCarta(Carta carta) throws SQLException, CheckException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if(carta == null || carta.getIdCarta()==0)
			throw new CheckException("carta non valida");
		
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