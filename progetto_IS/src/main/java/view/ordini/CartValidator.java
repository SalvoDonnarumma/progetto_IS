package view.ordini;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartValidator {
	public static boolean isValidFormat(String input) {
        String regex = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isValidMonth(String inputMonth) {
        String[] months = {"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno",
                           "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"};
        List<String> monthList = Arrays.asList(months);
        return monthList.contains(inputMonth.toLowerCase());
    }

    public static boolean isYearNotExpired(String inputYear) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            int year = Integer.parseInt(inputYear);
            return year >= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCVV(String input) {
        if (input.length() != 3) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
