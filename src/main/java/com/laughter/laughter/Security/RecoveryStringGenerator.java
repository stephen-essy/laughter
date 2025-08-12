package com.laughter.laughter.Security;

import java.security.SecureRandom;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecoveryStringGenerator {
    private static final String CHARACTER_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String SYMBOLS = "~!@#$%^&*()";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRecoveryString(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Fields can not be empty !");
        }
        int emailRandom = secureRandom.nextInt(email.length());
        int nameRandom = secureRandom.nextInt(name.length());
        int passRandom = secureRandom.nextInt(password.length());
        int charRandom = secureRandom.nextInt(CHARACTER_STRING.length());
        int symbolsRandom = secureRandom.nextInt(SYMBOLS.length());

        return CHARACTER_STRING.charAt(charRandom) + "@" + email.charAt(emailRandom) + password.charAt(passRandom)
                + SYMBOLS.charAt(symbolsRandom) + name.charAt(nameRandom);
    }

}
