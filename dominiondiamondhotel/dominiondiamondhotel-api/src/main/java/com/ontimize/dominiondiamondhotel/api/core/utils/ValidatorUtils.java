package com.ontimize.dominiondiamondhotel.api.core.utils;


import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    public static boolean idValidator(int idType, String id) {
        switch (idType) {
            case 1:
                String regexPassport = "^[A-Z]{3}[0-9]{6}[A-Z]?$";
                return Pattern.matches(regexPassport, id);
            case 2:
                String[] DNIletter = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};
                int dni = 0;
                try {
                    dni = Integer.parseInt(id.substring(0, 8));
                } catch (NumberFormatException e) {
                    return false;
                }
                String letter = id.substring(8);
                String assignedLetter = DNIletter[dni % 23];
                return letter.equals(assignedLetter);
            default:
                return false;
        }
    }

    public static boolean emailValidator(String email) {
        String emailRegex = "^(.+)@(.+)$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean phoneValidator(String phone) {
        String phonePatterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(phonePatterns);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean dateValidator(String entry_date, String exit_date) {
        try {
            LocalDate nowDate = LocalDate.now();
            LocalDate entryDate = LocalDate.parse(entry_date);
            LocalDate exitDate = LocalDate.parse(exit_date);
            return (entryDate.isBefore(exitDate) || entryDate.isEqual(exitDate)) && (entryDate.isAfter(nowDate) || entryDate.isEqual(nowDate));
        } catch (Exception e) {
            return false;
        }
    }
}
