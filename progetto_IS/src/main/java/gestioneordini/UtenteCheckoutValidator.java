package gestioneordini;

public class UtenteCheckoutValidator {
	public static boolean isValidNome(String input) {
        String regex = "^[A-Za-z]+\\s[A-Za-z]+$";
        return input.matches(regex);
    }
	
	public static boolean isValidIndirizzo(String input) {
        String regex = "^Via\\s[A-Za-z0-9]+,\\s[0-9]+$";
        return input.matches(regex);
    }
	
	 public static boolean isValidCitta(String input) {
	        // \p{L} è utilizzata per rappresentare tutte le lettere di qualsiasi lingua, inclusi caratteri con accenti
	        String regex = "^[\\p{L}]+$";
	        return input.matches(regex);
	 }
	 
	 public static boolean isValidStato(String input) {
	        String regex = "^[A-Za-z]+$";
	        return input.matches(regex);
	 }
	 
	 public static boolean isValidCAP(String input) {
	        String regex = "^\\d{5}$";
	        return input.matches(regex);
	 }   
}
