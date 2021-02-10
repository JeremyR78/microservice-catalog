package com.ecommerce.catalog.utils.languages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LanguageUtil {

    // --------------------------------------
    // -        Attributes static           -
    // --------------------------------------

    private static Logger logger = LoggerFactory.getLogger(LanguageUtil.class);

    // --------------------------------------
    // -        Constructor                  -
    // --------------------------------------

    private LanguageUtil(){}

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    /**
     *
     * @param isoCode
     * @return
     * @throws MissingResourceException
     */
    public static ResourceBundle getResourceBundle( String isoCode ) throws MissingResourceException {
        //détermination du bon Locale pour récupérer le bon fichier messages.properties
        Locale locale;
        try {
            locale = new Locale( isoCode );
        } catch (NullPointerException e) {
            logger.error(  "Impossible de trouver ISO CODE !! LANGUE : {} - EX : {}", isoCode, e );
            // langue par defaut
            locale = new Locale("fr");
        }

        // tentative de récupération du fichier messages.properties
        ResourceBundle msgBundle;
        try {
            msgBundle = ResourceBundle.getBundle("/WEB-INF/messages", locale, LanguageUtil.class.getClassLoader());
        } catch (MissingResourceException ex) {
            throw ex;
        }

        return msgBundle;
    }

}
