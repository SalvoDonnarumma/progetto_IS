package checking;

public class CheckException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CheckException(){
	}
	
	public CheckException(String errorMessage){
		super(errorMessage);
	}

}