package com.dev.utils;

import com.dev.controllers.LiveUpdatesController;
import com.dev.persists.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.dev.utils.Constants.MINIMAL_PASSWORD_LENGTH;
import static java.lang.Character.*;

@Component
public class Utils {

    @Autowired
    Persist persist;

    @Autowired
    LiveUpdatesController liveUpdatesController;


    public String createHash (String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    public boolean isStrongPassword (String password) {

        boolean containsCapital = false, containsSmallLetter = false, containsDigits = false;

        for (int i = 0; i < password.length(); i++) {
            Character current = password.charAt(i);
            if (isLetter(current)) {
                if (isUpperCase(current)) {
                    containsCapital = true;
                }
                if (isLowerCase(current)) {
                    containsSmallLetter = true;
                }
            }
            if (isDigit(current)) {
                containsDigits = true;
            }
        }
        return containsCapital&&containsSmallLetter&&
                containsDigits&&password.length() >= MINIMAL_PASSWORD_LENGTH;

    }


}
