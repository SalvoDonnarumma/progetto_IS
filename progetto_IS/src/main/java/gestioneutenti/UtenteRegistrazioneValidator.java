package gestioneutenti;

public class UtenteRegistrazioneValidator {
	 public static boolean isValidNome(String input) {
	        if (input == null || input.isEmpty()) {
	            return false;  
	        }
	        return input.matches("^[a-zA-Z]+$");
	 }
	 
	 public static boolean isValidEmail(String email) {
	        if (email == null || email.isEmpty()) {
	            return false;  
	        }
	        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	 }
	 
	 public static boolean isValidTelefono(String numeroTelefono) {
	        if (numeroTelefono == null || numeroTelefono.isEmpty()) {
	            return false; 
	        }
	        return numeroTelefono.matches("^\\d{3}-\\d{7}$");
	 }
}
