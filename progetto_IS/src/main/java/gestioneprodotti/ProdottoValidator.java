package gestioneprodotti;

public class ProdottoValidator {
	public static boolean isValidCategoria(String input) {
        String[] options = {"Erogatori", "Coltelli", "Guanti", "Maschere", "Mute", "Pinne", "Torce"};
        for (String option : options) {
            if (option.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }
	
	public static boolean isValidPrice(String input) {
	    String regex = "^[0-9]+(\\.[0-9]+)?$";
	    return input.matches(regex);
	}
	
	public static boolean isValidQuantity(String input) {
	    String regex = "^[0-9]+$";
	    return input.matches(regex);
	}
	
}
