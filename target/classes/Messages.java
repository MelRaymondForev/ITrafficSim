package com.fifthgen.trafficsim.localization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {

    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static String getString(String key, String locale) {
        try {
            Locale savedLocale = RESOURCE_BUNDLE.getLocale();
            RESOURCE_BUNDLE = ResourceBundle.getBundle("messages", new Locale(locale));
            String returnMessage = RESOURCE_BUNDLE.getString(key);
            RESOURCE_BUNDLE = ResourceBundle.getBundle("messages", savedLocale);
            return returnMessage;
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
