package checking;

public class DBException extends Exception{
	public DBException() {
	}
	
	public DBException(String errorMessage) {
		super(errorMessage);
	}
}