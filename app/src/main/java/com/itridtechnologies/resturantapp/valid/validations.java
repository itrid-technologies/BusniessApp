package com.itridtechnologies.resturantapp.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validations {

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValidMethod(String password) {


        boolean isValid = false;

        String expressionx = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,}$";
        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(expressionx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            System.out.println("if");
            isValid = true;
        }else{
            System.out.println("else");
        }
        return isValid;
    }

}
